package ensan.patientapp.view.activity.forgotpassword.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.view.activity.forgotpassword.viewmodel.ForgotPasViewModel;

public class ForgotPasswordActivity extends AppCompatActivity {

    @BindView(R.id.emailLayout)
    TextInputLayout emailLayout;

    @BindView(R.id.emailEt)
    TextInputEditText emailEt;

    private Progress progress;
    private SharedPreferences sharedPreferences;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_activity);

        // bind data to butter_knife
        ButterKnife.bind(this);

        // create instance of progress
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);

        // get data from shared preferences
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_LANGUAGE,MODE_PRIVATE);
        language = sharedPreferences.getString(Constants.KEY_LANGUAGE,"");

    }

    // open dialog
    private void openDialog(String msg){

        AlertPopup mAlert = new AlertPopup(this);
        mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert.setMessage(msg);
        mAlert.setOkButton(view -> {
            mAlert.dismiss();
            //Do want you want
        });
        mAlert.show();
    }

    public void forgotPassword(View view) {
       String email =  emailEt.getText().toString().trim();
       if(email.isEmpty()){
           emailLayout.setError(getString(R.string.emailerror));
       }else if(Utility.isValidEmail(email)){
           progress.show();
               // forgot password api
           ForgotPasViewModel forgotPasViewModel = ViewModelProviders.of(this).get(ForgotPasViewModel.class);
           forgotPasViewModel.init();
           forgotPasViewModel.forgotPassword(email,language);
           forgotPasViewModel.getVolumesResponseLiveData().observe(this, forgotPassResponse -> {
               progress.hide();
               if(forgotPassResponse != null){
                   if(forgotPassResponse.getStatus()) {
                       if (!forgotPassResponse.getMessage().equals(getString(R.string.errorotp))) {
                           Intent intent = new Intent(ForgotPasswordActivity.this, OTPActivity.class);
                           intent.putExtra(Constants.KEY_OTP, forgotPassResponse.getOtp());
                           intent.putExtra(Constants.KEY_MOBILE_NUMBER, forgotPassResponse.getPhone());
                           intent.putExtra(Constants.KEY_EMAIL, email);
                           startActivity(intent);
                       }
                   }else {
                        openDialog(forgotPassResponse.getMessage());
                   }
               }else{
                   openDialog(this.getString(R.string.errormsg));
               }
           });
       }else {
           emailLayout.setError(getString(R.string.emailerror));
       }
    }

    public void backPressed(View view) {
        finish();
    }


}