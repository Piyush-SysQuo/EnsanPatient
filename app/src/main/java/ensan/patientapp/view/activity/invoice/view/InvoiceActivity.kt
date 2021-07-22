package ensan.patientapp.view.activity.invoice.view

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.reflect.TypeToken
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import ensan.caregiverapp.view.activity.invoice.adapter.ExtraChargeAdapter
import ensan.caregiverapp.view.activity.invoice.adapter.ServicesChargeAdapter
import ensan.patientapp.R
import ensan.patientapp.Utils.*
import ensan.patientapp.view.activity.invoice.asynctask.DownloadFile
import ensan.patientapp.view.activity.invoice.model.TransactionDetailsResponse
import ensan.patientapp.view.activity.invoice.viewmodel.TxnDetailsViewModel
import ensan.patientapp.view.activity.login.model.LoginResponse
import kotlinx.android.synthetic.main.activity_invoice.*
import java.util.*


class InvoiceActivity : AppCompatActivity() {

    private var language: String? = null
    private var resp: LoginResponse? = null
    private var token : String? = null
    private var progress : Progress? = null
    private var pdfURL : String ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // get Token
        resp = Util.getInstance(this).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, object : TypeToken<LoginResponse?>() {}) as LoginResponse

        if (resp != null) {
            language = resp?.data?.lanaguage
            Utility.setLocale(this, language)
            token = resp?.data?.token
        }

        setContentView(R.layout.activity_invoice)

       val bookingId = intent.getStringExtra(Constants.KEY_BOOKING_ID)

        // initialize progress dialog instance
        progress = Progress(this)
        Objects.requireNonNull(progress?.window)?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progress?.setCancelable(false)
        progress?.setCanceledOnTouchOutside(false)


        // invoice api called
        callInvoiceApi(bookingId)

        // download pdf file
        downloadButton.setOnClickListener{
            Dexter.withContext(this)
                    .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(response: PermissionGrantedResponse) {
                            val separated = pdfURL?.split("/")?.toTypedArray()
                            val fileName = separated?.get(separated.size - 1)
                            DownloadFile(this@InvoiceActivity, progress).execute(pdfURL, fileName)
                        }

                        override fun onPermissionDenied(response: PermissionDeniedResponse) {
                            response.requestedPermission
                        }

                        override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) { /* ... */
                        }
                    }).check()
        }

        // back button clicked
        imgback.setOnClickListener{
            finish()
        }


        shareButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_SEND)
            val shareBody = pdfURL
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.downloadfile))
            intent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(intent, pdfURL))
        }
    }


    private fun callInvoiceApi(bookingId: String){
        progress?.show()
        val viewModel = ViewModelProviders.of(this).get(TxnDetailsViewModel::class.java)
        viewModel.init()
        viewModel.getTxnDetails(token!!, bookingId)
        viewModel.volumesResponseLiveData!!.observe(this, { txnResponse: TransactionDetailsResponse? ->
            progress?.hide()
            if (txnResponse != null) {
                if (txnResponse.success) {
                    priceTextView.text = txnResponse.data.total
                    totalAmountTV.text = txnResponse.data.total
                    dueAmountTV.text = txnResponse.data.due_amount
                    pdfURL = txnResponse.data.invoiceFile
                    invoiceNumTextView.text = getString(R.string.invoice_no) + txnResponse.data.invoiceNo

                    // set  adapter
                    invoiceItemsRV.layoutManager = LinearLayoutManager(this)
                    val serviceAdapter = ServicesChargeAdapter(this@InvoiceActivity, txnResponse?.data?.services)
                    invoiceItemsRV.adapter = serviceAdapter

                    if (txnResponse?.data?.addiitionalCharges?.size != 0) {
                        extraChargesRV.layoutManager = LinearLayoutManager(this)
                        val extraChargeAdapter = ExtraChargeAdapter(this@InvoiceActivity, txnResponse?.data?.addiitionalCharges)
                        extraChargesRV.adapter = extraChargeAdapter
                    } else {
                        extraLayout.visibility = View.GONE
                    }
                }else{
                    openDialog(txnResponse.message)
                }
            } else {
              openDialog(getString(R.string.errormsg))
            }
        })
    }


    // open dialog
    private fun openDialog(msg: String) {
        val mAlert = AlertPopup(this)
        mAlert.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mAlert.message = msg
        mAlert.setOkButton {
            mAlert.dismiss()
            this.finish()
        }
        mAlert.show()
    }
}