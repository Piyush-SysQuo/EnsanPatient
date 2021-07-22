package ensan.patientapp.view.activity.signup.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ensan.patientapp.R;
import ensan.patientapp.view.activity.verification.view.VerificationActivity;

public class SignUpSecndActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_secnd_activity);
    }

    public void nextClick(View view) {
        startActivity(new Intent(getApplicationContext(), VerificationActivity.class));
//        finish();
    }
}