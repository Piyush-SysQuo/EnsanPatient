package ensan.patientapp.view.activity.forgotpassword.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
import ensan.patientapp.Utils.SuccessPopup;
import ensan.patientapp.view.activity.forgotpassword.viewmodel.ResetPwdViewModel;
import ensan.patientapp.view.activity.login.view.LoginActivity;

public class ResetPassword extends AppCompatActivity {


    @BindView(R.id.emailLayout)
    TextInputLayout emailLayout;

    @BindView(R.id.emailEt)
    TextInputEditText emailEt;


    @BindView(R.id.confirmLayout)
    TextInputLayout confirmLayout;

    @BindView(R.id.confirmEt)
    TextInputEditText confirmEt;

    private Progress progress;
    private String email;

    final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        ButterKnife.bind(this);

        // create instance of progress
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);

        // get data from intent
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                email = (String) bundle.get(Constants.KEY_EMAIL);
            }
        }
    }

    // open dialog
    private void openDialog(String msg) {

        AlertPopup mAlert = new AlertPopup(this);
        mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert.setMessage(msg);
        mAlert.setOkButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlert.dismiss();
                //Do want you want
            }
        });
        mAlert.show();
    }
    // open success dialog
    private void openSuccessDialog(String msg) {

        SuccessPopup mAlert = new SuccessPopup(this);
        mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert.setMessage(msg);
        mAlert.setOkButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlert.dismiss();
                //Do want you want
            }
        });
        mAlert.show();
    }

    public void resetPassword(View view) {
        String password = emailEt.getText().toString().trim();
        String cPwd =  confirmEt.getText().toString().trim();

        if(password.isEmpty()){
            Toast.makeText(this, getString(R.string.pwderror), Toast.LENGTH_SHORT).show();
            return;
        }else if(cPwd.isEmpty()){
            Toast.makeText(this, getString(R.string.pwderror), Toast.LENGTH_SHORT).show();
            return;
        }else if(!password.equals(cPwd)){
            Toast.makeText(this, "Password not match", Toast.LENGTH_SHORT).show();
            return;
        }else if(!(password.length() >= 6)){
            Toast.makeText(this, R.string.patternerror, Toast.LENGTH_SHORT).show();
            return;
        }else if(!password.matches(PASSWORD_PATTERN)) {
            Toast.makeText(this, R.string.patternerror, Toast.LENGTH_SHORT).show();
            return;
        }else {
            progress.show();
            ResetPwdViewModel viewModel = ViewModelProviders.of(this).get(ResetPwdViewModel.class);
            viewModel.init();
            viewModel.restPassword(email,password);
            viewModel.getVolumesResponseLiveData().observe(this, resetPwdResponse -> {
                progress.hide();
                if(resetPwdResponse != null){
//                    Toast.makeText(this, resetPwdResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ResetPassword.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    if (resetPwdResponse.getStatus()){
                        openSuccessDialog(resetPwdResponse.getMessage());
                    }else {
                        openDialog(resetPwdResponse.getMessage());
                    }
                }else{
                    openDialog(this.getString(R.string.errormsg));
//                    Toast.makeText(this, getString(R.string.errormsg), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void backPressed(View view) {
        finish();
    }
}