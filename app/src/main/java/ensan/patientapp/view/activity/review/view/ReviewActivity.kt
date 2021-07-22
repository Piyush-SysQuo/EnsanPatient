package ensan.patientapp.view.activity.review.view

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.gson.reflect.TypeToken
import ensan.patientapp.R
import ensan.patientapp.Utils.*
import ensan.patientapp.view.activity.home.view.HomeActivity
import ensan.patientapp.view.activity.login.model.LoginResponse
import ensan.patientapp.view.activity.review.model.RatingResponse
import ensan.patientapp.view.activity.review.viewmodel.RatingViewModel
import kotlinx.android.synthetic.main.activity_main2.view.*
import java.util.*

class ReviewActivity : AppCompatActivity() {

    private var resp : LoginResponse? = null
    private var token : String? = null
    private var pic : String? = null
    private var name :String? = null
    private var rating : Float? = null
    private var ratingBar: RatingBar? = null
    private var caregiverId: String? = null
    private var progress:Progress? = null
    private var etReview: EditText? = null;
    private var bookingId : String? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        pic = intent.getStringExtra(Constants.KEY_PIC)
        name = intent.getStringExtra(Constants.KEY_NAME)
        caregiverId = intent.getStringExtra(Constants.KEY_CARE_GIVER_ID)
        bookingId = intent.getStringExtra(Constants.KEY_BOOKING_ID)

        // get Token
        resp = Util.getInstance(this).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, object : TypeToken<LoginResponse?>() {}) as LoginResponse

        token = resp?.data?.token


        // initialize progress dialog instance
        progress = Progress(this)
        Objects.requireNonNull(progress?.getWindow())?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progress?.setCanceledOnTouchOutside(false)
        progress?.setCancelable(false)


        // find id's
        val userImg: ImageView = findViewById(R.id.userImg)
        val textName: TextView = findViewById(R.id.name)
        etReview = findViewById(R.id.etReview)
        ratingBar = findViewById(R.id.simpleRatingBar)

        // add profile pic
        Glide.with(this).load(pic).into(userImg)
        textName.name.text = name


    }

    fun backPressed(view: View) {
       finish()
    }

    fun notNow(view: View) {
        finish()
    }


    // open dialog
    private fun openDialog(msg: String) {
        val mAlert = AlertPopup(this)
        mAlert.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mAlert.message = msg
        mAlert.setOkButton {
            mAlert.dismiss()
            //Do want you want
        }
        mAlert.show()
    }

    fun submit(view: View) {
        val description : String = etReview?.text.toString()
        progress?.show()
        rating = ratingBar?.rating
        val viewModel = ViewModelProviders.of(this).get(RatingViewModel::class.java)
        viewModel.init()
        viewModel.rating(token!!, caregiverId!!, description, rating.toString(), bookingId.toString())
        viewModel.getVolumesResponseLiveData()!!.observe(this, { ratingResponse: RatingResponse? ->
            progress?.hide()
            if (ratingResponse != null) {
                openSuccessDialog(ratingResponse.getMessage()!!)
            } else {
                openDialog(getString(R.string.errormsg))
            }

        })
    }


    // open dialog
    private fun openSuccessDialog(msg: String) {
        val mAlert = SuccessPopup(this)
        mAlert.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mAlert.message = msg
        mAlert.setOkButton {
            mAlert.dismiss()
            //Do want you want
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }
        mAlert.show()
    }

}