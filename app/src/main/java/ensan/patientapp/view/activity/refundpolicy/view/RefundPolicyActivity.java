package ensan.patientapp.view.activity.refundpolicy.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.util.Objects;

import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.Util;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import ensan.patientapp.view.activity.refundpolicy.viewmodel.RefundPolicyViewModel;

public class RefundPolicyActivity extends AppCompatActivity {

    private TextView txt_refundpolicy;
    private Progress progress;
    private LoginResponse resp;
    private String token;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // get Token
        resp = (LoginResponse) Util.getInstance(this).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginResponse>() {
                });

        if(resp != null){
            token =  resp.getData().getToken();
            language = resp.getData().getLanaguage();
            Utility.setLocale(this,language);
        }

        setContentView(R.layout.activity_refund_policy);
        txt_refundpolicy = findViewById(R.id.txt_refundpolicy);

        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);
        progress.show();

        RefundPolicyViewModel viewModel = ViewModelProviders.of(this).get(RefundPolicyViewModel.class);
        viewModel.init();
        viewModel.getRefundPolicy(token,language);
        viewModel.getVolumesResponseLiveData().observe(this, refundPolicyResp -> {
            progress.hide();
            if (refundPolicyResp != null) {
                if (refundPolicyResp.getSuccess()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        txt_refundpolicy.setText(Html.fromHtml(refundPolicyResp.getData().getText(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        txt_refundpolicy.setText(Html.fromHtml(refundPolicyResp.getData().getText()));
                    }
                }else{
                    txt_refundpolicy.setText(getString(R.string.refund_policy_error));
                }
            }else {
                openDialog(getString(R.string.errormsg));
            }
        });
    }

    public void backPressed(View view) {
        finish();
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
}