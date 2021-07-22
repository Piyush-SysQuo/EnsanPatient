package ensan.patientapp.view.activity.requestsent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ensan.patientapp.R;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.view.activity.home.view.HomeActivity;

public class RequestSent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_sent_activity);
    }

    public void continueClick(View view) {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.putExtra(Constants.KEY_FROM,"");
        finishAffinity();
    }
}