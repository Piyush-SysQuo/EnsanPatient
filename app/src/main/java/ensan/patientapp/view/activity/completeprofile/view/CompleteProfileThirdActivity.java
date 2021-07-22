package ensan.patientapp.view.activity.completeprofile.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.SuccessPopup;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.view.activity.login.view.LoginActivity;

public class CompleteProfileThirdActivity extends AppCompatActivity {

    private RelativeLayout cardLayout, appleLayout, stcLayout;
    private String token;
    private String payment;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get data from intent
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                token = (String) bundle.get(Constants.KEY_TOKEN);
                language = (String) bundle.get(Constants.KEY_LANGUAGE);
                Utility.setLocale(this,language);
            }
        }

        setContentView(R.layout.complete_profile_third_activity);


        cardLayout = findViewById(R.id.cardLayout);
        appleLayout = findViewById(R.id.appleLayout);
        stcLayout = findViewById(R.id.stcLayout);

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
    // open success dialog
    private void openSuccessDialog(String msg){

        SuccessPopup mAlert = new SuccessPopup(this);
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
    public void nextClick(View view) {
        if(payment == null){
            Toast.makeText(CompleteProfileThirdActivity.this, getString(R.string.paymenterror), Toast.LENGTH_SHORT).show();
            return;
        }
        openSuccessDialog(getString(R.string.logininfo));
//        Toast.makeText(this, "Account create successful, Please Re-login", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.putExtra(Constants.KEY_TOKEN,token);
        intent.putExtra(Constants.KEY_LANGUAGE,language);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();


    }

    public void backPressed(View view) {
        finish();
    }

    public void cardClick(View view) {
        payment = "1";
        cardLayout.setBackground(getResources().getDrawable(R.drawable.blue_strok_rect));
        appleLayout.setBackground(getResources().getDrawable(R.drawable.white_rect));
        stcLayout.setBackground(getResources().getDrawable(R.drawable.white_rect));
//        maleIv.setVisibility(View.GONE);
//        femaleIv.setVisibility(View.VISIBLE);
    }

    public void appleClick(View view) {
        payment = "2";
        appleLayout.setBackground(getResources().getDrawable(R.drawable.blue_strok_rect));
        cardLayout.setBackground(getResources().getDrawable(R.drawable.white_rect));
        stcLayout.setBackground(getResources().getDrawable(R.drawable.white_rect));
    }

    public void stcClick(View view) {
        payment = "3";
        stcLayout.setBackground(getResources().getDrawable(R.drawable.blue_strok_rect));
        cardLayout.setBackground(getResources().getDrawable(R.drawable.white_rect));
        appleLayout.setBackground(getResources().getDrawable(R.drawable.white_rect));
    }
}