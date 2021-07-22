package ensan.patientapp.view.activity.chooselanguage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import java.util.Locale;

import ensan.patientapp.R;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.view.activity.splash.SplashfirstActivity;
import ensan.patientapp.databinding.ActivityMainBinding;


public class ChooseLanguageActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

    }

    public void englishClick(View view) {

        setLocale(this,"en");
        activityMainBinding.englishLayout.setBackground(getResources().getDrawable(R.drawable.blue_strok_rect));
        activityMainBinding.arabicLayout.setBackground(getResources().getDrawable(R.drawable.white_rect));
        activityMainBinding.englishIV.setVisibility(View.VISIBLE);
        activityMainBinding.arabicIv.setVisibility(View.GONE);
        Intent intent = new Intent(getApplicationContext(), SplashfirstActivity.class);
        intent.putExtra(Constants.KEY_LANGUAGE,"en");
        startActivity(intent);
        finish();

    }

    public void arabicClick(View view) {
        setLocale(this,"ar");
        activityMainBinding.englishLayout.setBackground(getResources().getDrawable(R.drawable.white_rect));
        activityMainBinding.arabicLayout.setBackground(getResources().getDrawable(R.drawable.blue_strok_rect));
        activityMainBinding.arabicIv.setVisibility(View.VISIBLE);
        activityMainBinding.englishIV.setVisibility(View.GONE);
        Intent intent = new Intent(getApplicationContext(), SplashfirstActivity.class);
        intent.putExtra(Constants.KEY_LANGUAGE,"ar");
        startActivity(intent);
        finish();

    }



    public void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

          // Storing data into SharedPreferences
        SharedPreferences sharedPreferencesLang = getSharedPreferences(Constants.SHARED_PREF_LANGUAGE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesLang.edit();
        editor.putString(Constants.KEY_LANGUAGE,languageCode);
        editor.commit();

    }
}