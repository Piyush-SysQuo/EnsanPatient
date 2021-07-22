package ensan.patientapp.view.fragment.setting.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hbb20.CountryCodePicker;

import ensan.patientapp.R;
import ensan.patientapp.view.activity.verification.model.GetMobileNumber;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangeNewNumber#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeNewNumber extends BottomSheetDialogFragment implements CountryCodePicker.OnCountryChangeListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private GetMobileNumber getMobileNumber;
    private String countryCode;
    private String countryCodeName;
    private CountryCodePicker ccp;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChangeNewNumber(GetMobileNumber getMobileNumber) {
        // Required empty public constructor
        this.getMobileNumber = getMobileNumber;
    }

    public ChangeNewNumber() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangeNumber.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangeNewNumber newInstance(String param1, String param2) {
        ChangeNewNumber fragment = new ChangeNewNumber();
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
        View view =  inflater.inflate(R.layout.fragment_change_number, container, false);

        ImageView imageView = view.findViewById(R.id.img_cancel);
        TextView continueClickBtn = view.findViewById(R.id.continueClickBtn);
        EditText etMobile = view.findViewById(R.id.etMobile);
        LinearLayout linearLayout = view.findViewById(R.id.layout_change);
        ccp = view.findViewById(R.id.ccp);
        linearLayout.setBackgroundResource(android.R.color.transparent);

        // dialog changeButton clicked
        imageView.setOnClickListener(v -> {
             onCancel(this);
        });

        // continue button clicked
        continueClickBtn.setOnClickListener(v -> {
            String mobNum =   etMobile.getText().toString().trim();
            if(mobNum.isEmpty()){
                Toast.makeText(getActivity(), getString(R.string.mobileerror), Toast.LENGTH_SHORT).show();
                return;
            }
                getMobileNumber.getMobile(mobNum,countryCode);
                onCancel(this);
        });

        // Country Picker Listener
        ccp.setOnCountryChangeListener(this);
        countryCode = ccp.getDefaultCountryCodeWithPlus();
        countryCodeName = ccp.getDefaultCountryName();

        return view;
    }


    // dismiss dialog
    private void onCancel(ChangeNewNumber changeNumberDialog) {
        changeNumberDialog.dismiss();
    }

    @Override
    public void onCountrySelected() {
        countryCode = ccp.getSelectedCountryCodeWithPlus();
        countryCodeName = ccp.getSelectedCountryName();
    }
}