package ensan.patientapp.view.activity.choosepanal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ensan.patientapp.R;
import ensan.patientapp.view.activity.login.view.LoginActivity;

public class ChoosePanelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_panel_activity);
    }

    public void caregiverClick(View view) {
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
    }

    public void patientClick(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}