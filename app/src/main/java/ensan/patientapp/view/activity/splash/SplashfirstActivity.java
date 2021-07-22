package ensan.patientapp.view.activity.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ensan.patientapp.R;
import ensan.patientapp.Utils.Constants;

public class SplashfirstActivity extends AppCompatActivity {

    private String language;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__splashfirst);

        Intent intent =  getIntent();
        if(intent !=null){
            language =  intent.getExtras().getString(Constants.KEY_LANGUAGE);
        }
    }

    public void forwardClick(View view) {
        Intent intent = new Intent(getApplicationContext(), SplashSecondActivity.class);
        intent.putExtra(Constants.KEY_LANGUAGE,language);
        startActivity(intent);
        finish();
    }
}