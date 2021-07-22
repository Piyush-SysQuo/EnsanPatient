package ensan.patientapp.view.activity.personaldetail.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.view.activity.personaldetail.view.PersonalDetailsActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GenderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenderFragment extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String statusCode;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GenderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GenderSelectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GenderFragment newInstance(String param1, String param2) {
        GenderFragment fragment = new GenderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_gender, container, false);
        RelativeLayout maleLayout = view.findViewById(R.id.maleLayout);
        RelativeLayout femaleLayout = view.findViewById(R.id.femaleLayout);


        maleLayout.setOnClickListener(v -> {
            PersonalDetailsActivity.genderCode = "1";
            maleLayout.setBackground(getResources().getDrawable(R.drawable.blue_strok_rect));
            femaleLayout.setBackground(getResources().getDrawable(R.drawable.white_rect));
            PersonalDetailsActivity.txt_gender.setText(R.string.male);
            onCancel(this);
        });


        femaleLayout.setOnClickListener(v -> {
            PersonalDetailsActivity.genderCode = "0";
            maleLayout.setBackground(getResources().getDrawable(R.drawable.white_rect));
            femaleLayout.setBackground(getResources().getDrawable(R.drawable.blue_strok_rect));
            PersonalDetailsActivity.txt_gender.setText(R.string.female);
            onCancel(this);
        });


        return view;
    }
    private void onCancel(GenderFragment genderFragment) {
        genderFragment.dismiss();
    }

    // open dialog
    private void openDialog(String msg){

        AlertPopup mAlert = new AlertPopup(getContext());
        mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert.setMessage(msg);
        mAlert.setOkButton( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlert.dismiss();
                //Do want you want
            }
        });
        mAlert.show();
    }

}