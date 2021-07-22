package ensan.patientapp.view.activity.completeprofile.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ensan.patientapp.R;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.view.activity.login.view.LoginActivity;

public class CompleteProfileFifthActivity extends AppCompatActivity {

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complete_profile_fifth_activity);

        // get data from intent
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                token = (String) bundle.get(Constants.KEY_TOKEN);
            }
        }
    }

    public void nextClick(View view) {

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

    }
}