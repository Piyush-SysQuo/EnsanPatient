package ensan.patientapp.view.activity.home.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import org.parceler.Parcels;

import ensan.patientapp.R;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Util;

import ensan.patientapp.Utils.Utility;
import ensan.patientapp.databinding.HomeActivityBinding;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import ensan.patientapp.view.activity.login.view.LoginActivity;
import ensan.patientapp.view.fragment.about.view.AboutFragment;
import ensan.patientapp.view.fragment.addresses.view.AddressesFragment;
import ensan.patientapp.view.fragment.booking.view.BookingFragment;
import ensan.patientapp.view.fragment.home.HomeFragment;
import ensan.patientapp.view.fragment.personal.view.PersonalFragment;
import ensan.patientapp.view.fragment.setting.view.SettingFragment;
import ensan.patientapp.view.fragment.support.SupportFragment;
import ensan.patientapp.view.fragment.wallet.view.WalletFragment;

public class HomeActivity extends AppCompatActivity {

    public static HomeActivityBinding homeActivityBinding;
    private LinearLayout profile;
    private String token;
    private LoginResponse resp;
    private TextView txt_username;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get response from local
        resp = (LoginResponse) Util.getInstance(this).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginResponse>() {
                });

        // get and update token
        Intent intent = getIntent();
        if(intent != null){
            if(intent.getStringExtra(Constants.KEY_FROM).equals("signup")){

                resp.getData().setToken(intent.getStringExtra(Constants.KEY_TOKEN));

                // update login response
                Util.getInstance(this).putGsonObject(
                        Constants.PREFS_LOGIN_RESPONSE,resp, new TypeToken<LoginResponse>() {
                        });
            }
        }

        if(resp != null){
            token = resp.getData().getToken();
            language = resp.getData().getLanaguage();
            Utility.setLocale(this,language);

        }

        homeActivityBinding = DataBindingUtil.setContentView(this, R.layout.home_activity);
        txt_username = homeActivityBinding.drawer.findViewById(R.id.txt_username);


        if(resp != null){
            txt_username.setText(resp.getData().getFirstName() +" "+resp.getData().getLastName());
        }

       // open default fragment
       getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit();

    }


    public void personalInfoClick(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new PersonalFragment()).addToBackStack(null).commit();
        homeActivityBinding.drawerLayout.closeDrawers();

    }

    public void bookingClick(View view) {
        homeActivityBinding.drawerLayout.closeDrawers();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new BookingFragment()).addToBackStack(null).commit();

    }

    public void walletClick(View view) {
        homeActivityBinding.drawerLayout.closeDrawers();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new WalletFragment()).addToBackStack(null).commit();

    }

    public void aboutClick(View view) {
        homeActivityBinding.drawerLayout.closeDrawers();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new AboutFragment()).addToBackStack(null).commit();

    }

    public void supportClick(View view) {
        homeActivityBinding.drawerLayout.closeDrawers();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new SupportFragment()).addToBackStack(null).commit();

    }

  public void settingClick(View view) {
        homeActivityBinding.drawerLayout.closeDrawers();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new SettingFragment(language)).addToBackStack(null).commit();
    }
    public void addressClick(View view){
        homeActivityBinding.drawerLayout.closeDrawers();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new AddressesFragment(token)).addToBackStack(null).commit();

    }
    public void logoutClick(View view) {
        logoutBtn();
    }

    private void logoutBtn() {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.exitmshg))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), (dialog, id) -> {
                    dialog.dismiss();
                    Util.logout(HomeActivity.this);
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    finish();
                })
                .setNegativeButton(getString(R.string.no), (dialog, which) -> {
                     dialog.dismiss();
                })
                .show();
    }

    public void homeClick(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).addToBackStack(null).commit();

    }

    public void walletsClick(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new WalletFragment()).addToBackStack(null).commit();

    }

    public void bookingsClick(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new BookingFragment()).addToBackStack(null).commit();

    }

    public void profileClick(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new PersonalFragment()).addToBackStack(null).commit();
    }


    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
    }


}