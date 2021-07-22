package ensan.patientapp.view.fragment.addresses.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.reflect.TypeToken;

import ensan.patientapp.R;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Util;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import ensan.patientapp.view.fragment.addresses.model.DefaultAddress;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MakeAddressDefault#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MakeAddressDefault extends BottomSheetDialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private LoginResponse resp;
    private String token;
    private String addressId;
    private DefaultAddress defaultAddress;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public MakeAddressDefault() {
        // Required empty public constructor
    }
    public MakeAddressDefault(Context context, String addressId, DefaultAddress defaultAddress) {
        // Required empty public constructor
        this.addressId = addressId;
        this.defaultAddress = defaultAddress;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MakeAddressDefault.
     */
    // TODO: Rename and change types and number of parameters
    public static MakeAddressDefault newInstance(String param1, String param2) {
        MakeAddressDefault fragment = new MakeAddressDefault();
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
        View view =  inflater.inflate(R.layout.fragment_make_address_default, container, false);
        // get Token
        resp = (LoginResponse) Util.getInstance(getContext()).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginResponse>() {
                });
        if(resp != null){
            token =  resp.getData().getToken();
        }
        TextView btn_add = view.findViewById(R.id.btn_add);
        CheckBox checkBox  = view.findViewById(R.id.checkBox);
        btn_add.setOnClickListener(v -> {
            // onCancel(this);
            if(checkBox.isChecked()){
                this.onCancel(this);
                defaultAddress.getId(addressId);
            }else{
                Toast.makeText(getContext(),getString(R.string.checkboxer), Toast.LENGTH_SHORT).show();
                return;
            }
        });
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
        });
        return view;
    }
    private void onCancel(MakeAddressDefault makeAddressDefault) {
        makeAddressDefault.dismiss();
    }
}