package ensan.patientapp.view.activity.login.view


import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.reflect.TypeToken
import ensan.patientapp.R
import ensan.patientapp.Utils.*
import ensan.patientapp.connection.ApiDataService
import ensan.patientapp.connection.RetrofitInstance
import ensan.patientapp.view.activity.home.view.HomeActivity
import ensan.patientapp.view.activity.login.model.LoginResponse
import ensan.patientapp.view.activity.verification.model.OtpResponse
import kotlinx.android.synthetic.main.activity_login_o_t_p.*
import kotlinx.android.synthetic.main.activity_login_o_t_p.etfirst
import kotlinx.android.synthetic.main.activity_login_o_t_p.etfourth
import kotlinx.android.synthetic.main.activity_login_o_t_p.etsecand
import kotlinx.android.synthetic.main.activity_login_o_t_p.etthird
import kotlinx.android.synthetic.main.activity_login_o_t_p.txt_resend

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class LoginOTP : AppCompatActivity() {

    private var language : String ? =null
    private var countryCode : String ? =null
    private var mobNumber : String ? =null
    private var token : String ? =null
    private var email : String ? =null
    private var progress : Progress ? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // get data from intent
        val intent = intent
        if (intent != null) {
            val bundle = intent.extras
            if (bundle != null) {
                countryCode = bundle.get(Constants.KEY_COUNTRY_CODE).toString()
                mobNumber = bundle.get(Constants.KEY_MOBILE_NUMBER).toString()
                token = bundle.get(Constants.KEY_TOKEN).toString()
                language = bundle.get(Constants.KEY_LANGUAGE).toString()
                email = bundle.get(Constants.KEY_EMAIL).toString()
                Utility.setLocale(this, language)
            }
        }

        setContentView(R.layout.activity_login_o_t_p)

        // set text
        txt_mobile.text = """${getString(R.string.numbermsg)}
        $countryCode - $mobNumber"""

        // create instance of progress
        progress = Progress(this)
        Objects.requireNonNull(progress?.window)?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progress?.setCancelable(false)
        progress?.setCanceledOnTouchOutside(false)


        //resend button clicked
        txt_resend.setOnClickListener {

            progress?.show()

            // resend otp api
            val apiDataService = RetrofitInstance.getInstance().create(ApiDataService::class.java)
            val call = apiDataService.resend(email, language,Constants.KEY_PATIENT_ROLE)
            call.enqueue(object : Callback<OtpResponse?> {
                override fun onResponse(call: Call<OtpResponse?>, response: Response<OtpResponse?>) {
                    progress!!.hide()
                    if (response.body() != null) {
                        val resp : OtpResponse? = response.body()
                        if(resp?.success == true){
                            openSuccDialog(resp.message.toString())
                        }else{
                            openDialog(resp?.message.toString())
                        }
                    } else {
                        openDialog(getString(R.string.errormsg))
                    }
                }

                override fun onFailure(call: Call<OtpResponse?>, t: Throwable) {
                    progress!!.hide()
                    call.cancel()
                    openDialog(getString(R.string.errormsg))
                }
            })
        }



        // edit text data
        textWatcherForOtp()
    }

    private fun textWatcherForOtp() {
        etfirst.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                if (etfirst.text.toString().length == 1) {
                    etsecand.requestFocus()
                } else {
                    etfirst.requestFocus()
                }
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (etfirst.text.toString().length == 1) {
                    etsecand.requestFocus()
                } else {
                    etfirst.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        etsecand.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (etsecand.text.toString().length == 1) {
                    etthird.requestFocus()
                } else {
                    etfirst.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        etthird.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (etthird.text.toString().length == 1) {
                    etfourth.requestFocus()
                } else {
                    etsecand.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        etfourth.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (etfourth.text.toString().length == 1) {
//                    fou.continueTv.requestFocus();
                    try {
                        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                    } catch (e: Exception) {
                    }
                } else {
                    etthird.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    fun continueClick(view: View) {
        val firstBox: String = etfirst.text.toString().trim()
        val secBox: String = etsecand.text.toString().trim()
        val thirdBox: String = etthird.text.toString().trim()
        val fourthBox: String = etfourth.text.toString().trim()

        when {
            firstBox.isEmpty() -> {
                Toast.makeText(this, R.string.otperror, Toast.LENGTH_SHORT).show()
                return
            }
            secBox.isEmpty() -> {
                Toast.makeText(this, R.string.otperror, Toast.LENGTH_SHORT).show()
                return
            }
            thirdBox.isEmpty() -> {
                Toast.makeText(this, R.string.otperror, Toast.LENGTH_SHORT).show()
                return
            }
            fourthBox.isEmpty() -> {
                Toast.makeText(this, R.string.otperror, Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                progress?.show()
                val fOtp = firstBox + secBox + thirdBox + fourthBox
             /*   Toast.makeText(this@LoginOTP, fOtp, Toast.LENGTH_SHORT).show()*/
                val apiDataService = RetrofitInstance.getInstance().create(ApiDataService::class.java)
                val call = apiDataService.getLoginVerifyOTP("application/x-www-form-urlencoded", "Bearer $token",fOtp)
                call.enqueue(object : Callback<LoginResponse?> {
                    override fun onResponse(call: Call<LoginResponse?>, response: Response<LoginResponse?>) {
                        progress?.hide()
                        if (response.body() != null) {
                            val resp : LoginResponse? = response.body()

                            if(resp?.success == true) {
                                // save data into database
                                Util.getInstance(applicationContext).putGsonObject(
                                        Constants.PREFS_LOGIN_RESPONSE, resp, object : TypeToken<Any?>() {})

                                resp.data.token = token

                                // update login response
                                Util.getInstance(this@LoginOTP).putGsonObject(
                                        Constants.PREFS_LOGIN_RESPONSE, resp, object : TypeToken<LoginResponse?>() {})

                                val intent = Intent(this@LoginOTP, HomeActivity::class.java)
                                intent.putExtra(Constants.KEY_LANGUAGE, language)
                                intent.putExtra(Constants.KEY_FROM, "");
                                startActivity(intent)

                                finish()
                            }else{
                                openDialog(resp?.message.toString())
                            }
                        } else {
                            openDialog(getString(R.string.errormsg))
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                        progress?.hide()
                        call.cancel()
                        openDialog(getString(R.string.errormsg))
                    }
                })
            }
        }
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

    // open success dialog
    private fun openSuccDialog(msg: String) {
        val mAlert = SuccessPopup(this)
        mAlert.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mAlert.message = msg
        mAlert.setOkButton { view: View? -> mAlert.dismiss() }
        mAlert.show()
    }

}