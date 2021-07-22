package ensan.patientapp.view.activity.termsandcondition.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import ensan.patientapp.R;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.view.activity.privacypolicy.viewmodel.PrivacyPolicyViewModel;

public class ConditionActivity extends AppCompatActivity {

    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle  = getIntent().getExtras();
        if(bundle != null){
            language = (String) bundle.get(Constants.KEY_LANGUAGE);
            Utility.setLocale(this,language);
        }


        setContentView(R.layout.activity_condition_web_view);

        ImageView backBtn = findViewById(R.id.backBtn);
        TextView textView = findViewById(R.id.webview);

        backBtn.setOnClickListener(v -> finish());

        // initialize progress dialog instance
        Progress progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);
        progress.show();

        PrivacyPolicyViewModel viewModel = ViewModelProviders.of(this).get(PrivacyPolicyViewModel.class);
        viewModel.init();
        viewModel.getAboutText("",language);
        viewModel.getVolumesResponseLiveData().observe(this, privacySecurityResp -> {
            progress.hide();
            if(privacySecurityResp != null){
                textView.setText(Html.fromHtml(privacySecurityResp.getData().getText()));
            }
        });

    }
}