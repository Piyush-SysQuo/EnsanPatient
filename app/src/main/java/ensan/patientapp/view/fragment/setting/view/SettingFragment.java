package ensan.patientapp.view.fragment.setting.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;
import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.Util;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.view.activity.home.view.HomeActivity;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import ensan.patientapp.view.activity.notification.view.NotificationActivity;
import ensan.patientapp.view.activity.privacypolicy.view.PrivacyPolicy;
import ensan.patientapp.view.activity.refundpolicy.view.RefundPolicyActivity;
import ensan.patientapp.view.fragment.setting.model.GetLanguageData;
import ensan.patientapp.view.fragment.setting.viewmodel.ChangeLanguageViewModel;
import ensan.patientapp.view.fragment.support.SupportFragment;
import ensan.patientapp.view.fragment.personal.model.GetMobile;


public class SettingFragment extends Fragment implements GetLanguageData, GetMobile {

    private final String language;
    private LoginResponse resp;
    private String token;

    public SettingFragment(String language) {
        this.language = language;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // get Token
        resp = (LoginResponse) Util.getInstance(getActivity()).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginResponse>() {
                });

        if(resp != null){
            token =  resp.getData().getToken();
            Utility.setLocale(getActivity(),resp.getData().getLanaguage());
        }

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.setting_fragment, container, false);

        // create instance of progress
        Progress progress = new Progress(getContext());
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);

        LinearLayout privacyLayout = view.findViewById(R.id.privacyLayout);
        LinearLayout refundLayout = view.findViewById(R.id.refundLayout);
        LinearLayout helpLayout = view.findViewById(R.id.helpLayout);
        LinearLayout notificationLayout = view.findViewById(R.id.notificationLayout);
        LinearLayout languageLayout = view.findViewById(R.id.languageLayout);


        ImageView imageView = view.findViewById(R.id.backBtn);

        // back button pressed
        imageView.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());

        privacyLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PrivacyPolicy.class);
            intent.putExtra(Constants.KEY_LANGUAGE,language);
            startActivity(intent);
        });

        refundLayout.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), RefundPolicyActivity.class));
        });

        helpLayout.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new SupportFragment()).addToBackStack(null).commit();
        });

        notificationLayout.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), NotificationActivity.class));
        });

        languageLayout.setOnClickListener(v -> {
            ChangeLanguageFragment changeLanguageFragment = new ChangeLanguageFragment(getActivity(),language,this);
            changeLanguageFragment.show(getActivity().getSupportFragmentManager(),changeLanguageFragment.getTag());
        });


        return view;
    }

    @Override
    public void setLangData(String langCode) {

        if(langCode == null){
            openDialog(getString(R.string.select_language));
            return;
        }else {
            // initialize progress dialog instance
            Progress progress = new Progress(getContext());
            (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progress.setCanceledOnTouchOutside(false);
            progress.setCancelable(false);
            progress.show();
            ChangeLanguageViewModel viewModel = ViewModelProviders.of(this).get(ChangeLanguageViewModel.class);
            viewModel.init();
            viewModel.changeLanguage(token, langCode);
            viewModel.getVolumesResponseLiveData().observe(this, changeLanguageResp -> {
                progress.hide();
                if (changeLanguageResp != null) {

                    if (changeLanguageResp.getSuccess()) {
                        // Storing data into SharedPreferences
                        SharedPreferences sharedPreferencesLang = getActivity().getSharedPreferences(Constants.SHARED_PREF_LANGUAGE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferencesLang.edit();
                        editor.putString(Constants.KEY_LANGUAGE, langCode);
                        editor.commit();

                        // update profile pic in shared prefrence
                        resp.getData().setLanaguage(langCode);

                        // update login response
                        Util.getInstance(getContext()).putGsonObject(
                                Constants.PREFS_LOGIN_RESPONSE, resp, new TypeToken<LoginResponse>() {
                                });

                        Intent refresh = new Intent(getActivity(), HomeActivity.class);
                        refresh.putExtra(Constants.KEY_FROM, "");
                        getActivity().finish();
                        startActivity(refresh);
                    } else {
                        openDialog(changeLanguageResp.getMessage());
                    }

                } else {
                    openDialog(getString(R.string.errormsg));
                }
            });
        }
    }

    // open dialog
    private void openDialog(String msg){

        AlertPopup mAlert = new AlertPopup(getContext());
        mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert.setMessage(msg);
        mAlert.setOkButton(view -> {
            mAlert.dismiss();
            //Do want you want
        });
        mAlert.show();
    }


    @Override
    public void getMobile(@NotNull String mobile) {

    }
}
