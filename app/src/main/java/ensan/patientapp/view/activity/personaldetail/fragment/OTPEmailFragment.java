package ensan.patientapp.view.activity.personaldetail.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.reflect.TypeToken;

import java.util.Objects;

import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.SuccessPopup;
import ensan.patientapp.Utils.Util;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import ensan.patientapp.view.fragment.setting.viewmodel.ValidateEmailViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OTPEmailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OTPEmailFragment extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;
    private String email;
    EditText first;
    EditText second;
    EditText third;
    EditText fourth;
    TextView txt_change_number;
    TextView txt_timer;
    TextView txt_resend;
    String token;
    String language;

    public OTPEmailFragment() {
        // Required empty public constructor
    }

    public OTPEmailFragment(Context context, String email) {
        // Required empty public constructor
        this.context = context;
        this.email = email;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OTPEmailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OTPEmailFragment newInstance(String param1, String param2) {
        OTPEmailFragment fragment = new OTPEmailFragment();
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

        // get Token
        LoginResponse resp = (LoginResponse) Util.getInstance(getActivity()).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginResponse>() {
                });

        if(resp != null){
            token =  resp.getData().getToken();
            language = resp.getData().getLanaguage();
            if(language!=null){
                Utility.setLocale(getActivity(),language);}
        }

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_o_t_p, container, false);

        TextView emailTV = view.findViewById(R.id.otpMessageTV);
        first = view.findViewById(R.id.etfirst);
        second = view.findViewById(R.id.etsecand);
        third = view.findViewById(R.id.etthird);
        fourth = view.findViewById(R.id.etfourth);
        TextView continueClick = view.findViewById(R.id.continueClick);

        Progress progress = new Progress(getContext());
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);


        emailTV.setText(email);

        textWatcherForOtp();

        continueClick.setOnClickListener(v -> {

            String firstBox = first.getText().toString().trim();
            String secBox = second.getText().toString().trim();
            String thirdBox = third.getText().toString().trim();
            String fourthBox = fourth.getText().toString().trim();

            if(firstBox.isEmpty()){
                Toast.makeText(getContext(),getString(R.string.otperror), Toast.LENGTH_SHORT).show();
                return;
            }else if(secBox.isEmpty()){
                Toast.makeText(getContext(),getString(R.string.otperror), Toast.LENGTH_SHORT).show();
                return;
            }else if(thirdBox.isEmpty()){
                Toast.makeText(getContext(),getString(R.string.otperror), Toast.LENGTH_SHORT).show();
                return;
            }else if(fourthBox.isEmpty()){
                Toast.makeText(getContext(),getString(R.string.otperror), Toast.LENGTH_SHORT).show();
                return;
            }else {
                progress.show();
                String fOtp = firstBox + secBox + thirdBox + fourthBox;
                ValidateEmailViewModel viewModel = ViewModelProviders.of(this).get(ValidateEmailViewModel.class);
                viewModel.init();
                viewModel.changeEmail(token,email,fOtp);
                viewModel.getVolumesResponseLiveData().observe(getActivity(), changeEmailResponse -> {
                    progress.hide();
                    onCancel(this);
                    if(changeEmailResponse != null){
                        if (changeEmailResponse.getSuccess()){
                            openSuccessDialog(changeEmailResponse.getMessage());
                        }else {
                            openDialog(changeEmailResponse.getMessage());
                        }
                    }else {
                        openDialog(getString(R.string.emailerror));
                    }
                });
            }
        });

        return view;
    }

    private void textWatcherForOtp() {

        first.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (first.getText().toString().length() == 1) {
                    second.requestFocus();
                } else {
                    first.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (first.getText().toString().length() == 1) {
                    second.requestFocus();
                } else {
                    first.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        second.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (second.getText().toString().length() == 1) {
                    third.requestFocus();
                } else {
                    first.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        third.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (third.getText().toString().length() == 1) {
                    fourth.requestFocus();
                } else {
                    second.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        fourth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (fourth.getText().toString().length() == 1) {
//                    fou.continueTv.requestFocus();
                    try {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {

                    }

                } else {
                    third.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
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

    // open success dialog
    private void openSuccessDialog(String msg){

        SuccessPopup mAlert = new SuccessPopup(getContext());
        mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert.setMessage(msg);
        mAlert.setOkButton(view -> {
            mAlert.dismiss();
            //Do want you want
        });
        mAlert.show();
    }
    private void onCancel(OTPEmailFragment otpEmailFragment) {
        otpEmailFragment.dismiss();
    }

}