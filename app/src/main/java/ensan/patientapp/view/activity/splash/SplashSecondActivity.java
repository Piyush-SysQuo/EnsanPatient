package ensan.patientapp.view.activity.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ensan.patientapp.R;
import ensan.patientapp.Utils.Constants;

public class SplashSecondActivity extends AppCompatActivity {

    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_second_activity);

        Intent intent =  getIntent();
        if(intent !=null){
            language =  intent.getExtras().getString(Constants.KEY_LANGUAGE);
        }

    }

    public void forwardClick(View view) {
        Intent intent = new Intent(getApplicationContext(), SplashThirdActivity.class);
        intent.putExtra(Constants.KEY_LANGUAGE,language);
        startActivity(intent);
        finish();
    }
}