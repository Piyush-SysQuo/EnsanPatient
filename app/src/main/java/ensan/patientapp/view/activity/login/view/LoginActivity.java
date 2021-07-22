package ensan.patientapp.view.activity.login.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.reflect.TypeToken;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.Util;
import ensan.patientapp.R;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.view.activity.completeprofile.view.CompleteProfileForthActivity;
import ensan.patientapp.view.activity.completeprofile.view.CompleteProfileFrstActivity;
import ensan.patientapp.view.activity.completeprofile.view.CompleteProfileNewSecondActivity;
import ensan.patientapp.view.activity.completeprofile.view.CompleteProfileSecondActivity;
import ensan.patientapp.view.activity.forgotpassword.view.ForgotPasswordActivity;
import ensan.patientapp.view.activity.home.view.HomeActivity;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import ensan.patientapp.view.activity.login.view.LoginOTP;
import ensan.patientapp.view.activity.login.viewmodel.LogInViewModel;
import ensan.patientapp.view.activity.signup.view.SignUpActivity;
import ensan.patientapp.view.activity.verification.view.VerificationActivity;


public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.emailEt)
    TextInputEditText email;

    @BindView(R.id.passwordEt)
    TextInputEditText password;

    @BindView(R.id.emailLayout)
    TextInputLayout email_layout;

    @BindView(R.id.passwordLayout)
    TextInputLayout pwd_layout;

    @BindView(R.id.img_lg)
    ImageView imageView;

    @BindView(R.id.englishLayout)
    RelativeLayout englishLayout;

    @BindView(R.id.arabicLayout)
    RelativeLayout arabicLayout;

    private Progress progress;
    private String language;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get data from shared preferences
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_LANGUAGE,MODE_PRIVATE);
        language = sharedPreferences.getString(Constants.KEY_LANGUAGE,"");
        Utility.setLocale(this,language);

        setContentView(R.layout.login_activity);

        // bind butter knife
        ButterKnife.bind(this);


        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);

        if (language.matches("en")){
            englishLayout.setBackgroundResource(R.drawable.blue_strok_rect);
            arabicLayout.setBackgroundResource(R.drawable.white_rect);
            language = "en";
        }else{
            arabicLayout.setBackgroundResource(R.drawable.blue_strok_rect);
            englishLayout.setBackgroundResource(R.drawable.white_rect);
            language = "ar";
        }

        imageView.setOnTouchListener((View.OnTouchListener) (v, event) -> {
            switch ( event.getAction() ) {
                case MotionEvent.ACTION_DOWN:
                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
                case MotionEvent.ACTION_UP:
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    break;
            }
            return true;
        });
    }

    public void backPressed(View view) {
        finish();
    }

    public void forgotPassword(View view) {
        startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
    }

    public void signUpClick(View view) {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        intent.putExtra(Constants.KEY_LANGUAGE,language);
        startActivity(intent);
        finish();
    }

    // login attempt
    public void attemptLogin(View view) {

        String email_id =  email.getText().toString().trim();
        String email_pwd = password.getText().toString().trim();

        if(email_id.isEmpty()){
            email_layout.setError(getString(R.string.loginemailerror));
            return;
        }else if(!Utility.isValidEmail(email_id)){
            email_layout.setError(getString(R.string.emailerror));
            return;
        }if(email_pwd.isEmpty()){
            pwd_layout.setError(getString(R.string.loginepwderror));
            return;
        }else {
            // show dialog
            progress.show();

            // call login Api
            LogInViewModel viewModel = ViewModelProviders.of(this).get(LogInViewModel.class);
            viewModel.init();
            viewModel.userLogin(email_id,email_pwd,FirebaseInstanceId.getInstance().getToken(),language);
            viewModel.getVolumesResponseLiveData().observe(this, loginResponse -> {
                progress.dismiss();
                if(loginResponse !=null) {
                    if (loginResponse.getSuccess()) {
                        if(!loginResponse.getData().getOtpVerified()){
                            Intent intent = new Intent(this, VerificationActivity.class);
                            intent.putExtra(Constants.KEY_TOKEN,loginResponse.getData().getToken());
                            intent.putExtra(Constants.KEY_MOBILE_NUMBER,loginResponse.getData().getPhone());
                            intent.putExtra(Constants.KEY_LANGUAGE,loginResponse.getData().getLanaguage());
                            intent.putExtra(Constants.KEY_COUNTRY_CODE, loginResponse.getData().getCountry_code());
                            startActivity(intent);
                            finish();
                        }else if(loginResponse.getData().getProfile().getProfileComplete() == 1){
                           /* // save data into database
                            Util.getInstance(getApplicationContext()).putGsonObject(
                                    Constants.PREFS_LOGIN_RESPONSE, loginResponse, new TypeToken<LoginResponse>() {
                                    });
                            Intent  intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.putExtra(Constants.KEY_FROM,"");
                            startActivity(intent);
                            finish();*/
                            Intent intent = new Intent(this, LoginOTP.class);
                            intent.putExtra(Constants.KEY_LANGUAGE,language);
                            intent.putExtra(Constants.KEY_COUNTRY_CODE, loginResponse.getData().getCountry_code());
                            intent.putExtra(Constants.KEY_MOBILE_NUMBER, loginResponse.getData().getPhone());
                            intent.putExtra(Constants.KEY_TOKEN, loginResponse.getData().getToken());
                            intent.putExtra(Constants.KEY_EMAIL, loginResponse.getData().getEmail());
                            startActivity(intent);
                            finish();
                        }else if(loginResponse.getData().getProfile().getBasicInfo() == 0){
                            Intent intent = new Intent(this, CompleteProfileFrstActivity.class);
                            intent.putExtra(Constants.KEY_LANGUAGE,loginResponse.getData().getLanaguage());
                            intent.putExtra(Constants.KEY_TOKEN,loginResponse.getData().getToken());
                            startActivity(intent);
                            finish();
                        }else if(loginResponse.getData().getProfile().getProfileInfo() == 0){
                            Intent intent = new Intent(this, CompleteProfileNewSecondActivity.class);
                            intent.putExtra(Constants.KEY_TOKEN,loginResponse.getData().getToken());
                            intent.putExtra(Constants.KEY_LANGUAGE,loginResponse.getData().getLanaguage());
                            startActivity(intent);
                            finish();
                        }else if(loginResponse.getData().getProfile().getAddress() == 0){
                            Intent intent = new Intent(this, CompleteProfileSecondActivity.class);
                            intent.putExtra(Constants.KEY_TOKEN,loginResponse.getData().getToken());
                            intent.putExtra(Constants.KEY_LANGUAGE,loginResponse.getData().getLanaguage());
                            startActivity(intent);
                            finish();
                        } else if(loginResponse.getData().getProfile().getDocument() == 0){
                            Intent intent = new Intent(this, CompleteProfileForthActivity.class);
                            intent.putExtra(Constants.KEY_TOKEN,loginResponse.getData().getToken());
                            intent.putExtra(Constants.KEY_LANGUAGE,loginResponse.getData().getLanaguage());
                            startActivity(intent);
                            finish();
                        }
                    } else {
                      //  Utility.showSnack(getWindow().getDecorView().getRootView(),R.color.colorPrimaryDark,loginResponse.getMessage(),this);
                        openDialog( loginResponse.getMessage());
                    }
                }else {
                    openDialog(getString(R.string.errormsg));
                }
            });
        }
    }


    // open dialog
    private void openDialog(String msg){

        AlertPopup mAlert = new AlertPopup(this);
        mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert.setMessage(msg);
        mAlert.setOkButton( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlert.dismiss();
                //Do want you want
            }
        });
        mAlert.show();
    }

    public void englishClick(View view) {
        if (!language.equals("en")){
            englishLayout.setBackgroundResource(R.drawable.blue_strok_rect);
            arabicLayout.setBackgroundResource(R.drawable.white_rect);
            language = "en";
            updateLanguage(language);
        }
    }

    public void arabicClick(View view) {
        if (!language.equals("ar")){
            arabicLayout.setBackgroundResource(R.drawable.blue_strok_rect);
            englishLayout.setBackgroundResource(R.drawable.white_rect);
            language = "ar";
            updateLanguage(language);
        }
    }

    private void updateLanguage(String language){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_LANGUAGE,language);
        editor.commit();
        Utility.setLocale(this,language);
        Intent refresh = new Intent(this, LoginActivity.class);
        finish();
        startActivity(refresh);
    }
}