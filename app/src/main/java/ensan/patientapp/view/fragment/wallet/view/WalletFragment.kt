package ensan.patientapp.view.fragment.wallet.view

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.reflect.TypeToken
import ensan.patientapp.R
import ensan.patientapp.Utils.*
import ensan.patientapp.connection.ApiDataService
import ensan.patientapp.connection.RetrofitInstance
import ensan.patientapp.view.activity.login.model.LoginResponse
import ensan.patientapp.view.fragment.wallet.adapter.WalletAdapter
import ensan.patientapp.view.fragment.wallet.model.Datum
import ensan.patientapp.view.fragment.wallet.model.GetTransactionHistoryResponse
import ensan.patientapp.view.fragment.wallet.model.GetTransactionPosition
import ensan.patientapp.view.activity.invoice.view.InvoiceActivity
import kotlinx.android.synthetic.main.wallet_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class WalletFragment : Fragment(), GetTransactionPosition {

    private var language: String? = null
    private var resp: LoginResponse? = null
    private var walletAdapter: WalletAdapter? = null
    private var token : String? = null
    private var progress : Progress? =null
    private var walletDataList : ArrayList<Datum>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // get Token
        resp = Util.getInstance(activity).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, object : TypeToken<LoginResponse?>() {}) as LoginResponse

        if (resp != null) {
            language = resp?.data?.lanaguage
            Utility.setLocale(activity, language)
            token = resp?.data?.token
        }

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.wallet_fragment, container, false)

        // initialize progress dialog instance
        progress = Progress(activity)
        Objects.requireNonNull(progress?.window)?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progress?.setCancelable(false)
        progress?.setCanceledOnTouchOutside(false)
        progress?.show()

        view.backBtn.setOnClickListener { activity?.supportFragmentManager?.popBackStack() }
        view.btnContinue.setOnClickListener { activity?.supportFragmentManager?.popBackStack() }

        val apiDataService = RetrofitInstance.getInstance().create(ApiDataService::class.java)
        val response  = apiDataService.getTransactionHistory("application/json", "Bearer $token")
        response.enqueue(object : Callback<GetTransactionHistoryResponse?> {
            override fun onResponse(call: Call<GetTransactionHistoryResponse?>, response: Response<GetTransactionHistoryResponse?>) {
                progress?.hide()
                if (response.body() != null) {
                    val historyRes = response.body()
                    if (response.isSuccessful) {
                        walletDataList = historyRes?.data as ArrayList<Datum>?
                        if(historyRes?.data?.size != 0) {

                            // set wallet data to adapter
                            view.transactionRV.layoutManager = LinearLayoutManager(activity)
                            walletAdapter = WalletAdapter(activity, historyRes?.data,this@WalletFragment)
                            view.transactionRV.adapter = walletAdapter
                            view.spentTV.text = historyRes?.totalSpent
                        }else{
                            view.layout_wallet.visibility = View.GONE
                            view.no_wallet_layout.visibility = View.VISIBLE
                        }
                    } else {
                        openDialog(historyRes?.message)
                    }
                } else {
                    openDialog(getString(R.string.errormsg))
                }
            }

            override fun onFailure(call: Call<GetTransactionHistoryResponse?>, t: Throwable) {
                call.cancel()
                progress?.hide()
                openDialog(getString(R.string.errormsg))
            }
        })

        return view
    }

    // open dialog
    private fun openDialog(msg: String?) {
        val mAlert = AlertPopup(context)
        mAlert.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mAlert.message = msg
        mAlert.setOkButton { mAlert.dismiss() }
        mAlert.show()
    }

    override fun position(position: Int) {
        val intent = Intent(activity, InvoiceActivity::class.java)
        intent.putExtra(Constants.KEY_BOOKING_ID,walletDataList?.get(position)?.bookingId)
        startActivity(intent)
    }


}