package ensan.patientapp.view.activity.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import ensan.patientapp.R;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.view.activity.login.view.LoginActivity;

public class SplashThirdActivity extends AppCompatActivity {

    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_third_activity);

        Intent intent =  getIntent();
        if(intent !=null){
            language =  intent.getExtras().getString(Constants.KEY_LANGUAGE);
        }

        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_SPLASH_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.KEY_SKIP_SPLASH,true);
        editor.commit();

    }

    public void forwardClick(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.putExtra(Constants.KEY_LANGUAGE,language);
        startActivity(intent);
        finish();
    }
}