package ensan.patientapp.view.activity.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.reflect.TypeToken;

import java.util.Locale;

import ensan.patientapp.R;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Util;
import ensan.patientapp.view.activity.chooselanguage.ChooseLanguageActivity;
import ensan.patientapp.view.activity.home.view.HomeActivity;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import ensan.patientapp.view.activity.login.view.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private LoginResponse loginResponse;
    private FirebaseAnalytics mFirebaseAnalytics;
    private boolean isSkip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setContentView(R.layout.activity_splash);

        // get data from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_SPLASH_NAME,MODE_PRIVATE);
        isSkip = sharedPreferences.getBoolean(Constants.KEY_SKIP_SPLASH,false);

        new Handler().postDelayed(() -> {
            //Get saved login response, if we have
            try {
                loginResponse = (LoginResponse) Util.getInstance(getApplicationContext()).
                        pickGsonObject(Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginResponse>() {
                        });
            } catch (Exception e) {
                /*Do Nothing*/
            }

            if(loginResponse!=null){

                Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                i.putExtra(Constants.KEY_FROM,"splash");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

                String lang = loginResponse.getData().getLanaguage();
                if(lang!=null){
                    setLocale(lang);
                }


            }else{

                Intent i = null;
                String lngCode = sharedPreferences.getString(Constants.KEY_LANGUAGE,"");
                if(isSkip){
                    i = new Intent(SplashActivity.this, LoginActivity.class);
                }else {
                    i = new Intent(SplashActivity.this, ChooseLanguageActivity.class);
                }
                i.putExtra(Constants.KEY_LANGUAGE,lngCode);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
            finish();
        }, 2000);
    }


    public  void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}