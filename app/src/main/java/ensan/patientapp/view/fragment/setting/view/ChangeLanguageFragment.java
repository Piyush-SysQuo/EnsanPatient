package ensan.patientapp.view.fragment.setting.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.view.fragment.setting.model.GetLanguageData;

public class ChangeLanguageFragment extends BottomSheetDialogFragment {

    private String languageCode;
    private GetLanguageData getLanguageData;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChangeLanguageFragment() {
        // Required empty public constructor
    }

    public ChangeLanguageFragment(Context context, String lang, GetLanguageData getLanguageData) {
        // Required empty public constructor
        this.getLanguageData = getLanguageData;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_change_language, container, false);
        RelativeLayout englishLayout = view.findViewById(R.id.englishLayout);
        RelativeLayout arabicLayout = view.findViewById(R.id.arabicLayout);
        TextView btn_language = view.findViewById(R.id.btn_language);


        englishLayout.setOnClickListener(v -> {
            languageCode = "en";
            englishLayout.setBackground(getResources().getDrawable(R.drawable.blue_strok_rect));
            arabicLayout.setBackground(getResources().getDrawable(R.drawable.white_rect));
        });


        arabicLayout.setOnClickListener(v -> {
            languageCode = "ar";
            englishLayout.setBackground(getResources().getDrawable(R.drawable.white_rect));
            arabicLayout.setBackground(getResources().getDrawable(R.drawable.blue_strok_rect));
        });

        btn_language.setOnClickListener(v -> {
            getLanguageData.setLangData(languageCode);
            onCancel(this);
        });
        return view;
    }

    private void onCancel(ChangeLanguageFragment changeLanguageFragment) {
        changeLanguageFragment.dismiss();
    }

}