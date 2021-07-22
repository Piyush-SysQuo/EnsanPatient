package ensan.patientapp.view.fragment.about.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.util.Objects;

import ensan.patientapp.R;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.Util;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import ensan.patientapp.view.fragment.about.viewmodel.AboutViewModel;


public class AboutFragment extends Fragment {

    private LoginResponse resp;
    private String language;
    private String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // get Token
        resp = (LoginResponse) Util.getInstance(getActivity()).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginResponse>() {
                });

        if(resp != null){
            token = resp.getData().getToken();
            language = resp.getData().getLanaguage();
            Utility.setLocale(getActivity(),language);
        }
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.about_app_activity, container, false);

        ImageView imageView = view.findViewById(R.id.backBtn);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        TextView txt_about = view.findViewById(R.id.txt_about);

        // create instance of progress
        Progress progress = new Progress(getActivity());
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        // call api
        AboutViewModel viewModel = ViewModelProviders.of(this).get(AboutViewModel.class);
        viewModel.init();
        viewModel.getAboutText(token,language);
        viewModel.getVolumesResponseLiveData().observe(getActivity(), aboutResponse -> {
            progress.hide();
            if(aboutResponse.getSuccess()){
                //  txt_about.setText(aboutResponse.getData().getText());
                txt_about.setText(Html.fromHtml(aboutResponse.getData().getText()));

            }
        });


        return view;
    }
}