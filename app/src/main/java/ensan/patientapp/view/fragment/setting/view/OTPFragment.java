package ensan.patientapp.view.fragment.setting.view;

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

import com.esafirm.imagepicker.model.Image;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Objects;

import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.SuccessPopup;
import ensan.patientapp.Utils.Util;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import ensan.patientapp.view.activity.personaldetail.view.PersonalDetailsActivity;
import ensan.patientapp.view.fragment.personal.model.GetMobile;
import ensan.patientapp.view.fragment.setting.viewmodel.ValidatePhoneViewModel;


public class OTPFragment extends BottomSheetDialogFragment {

    private String number;
    private String countryCode;
    private EditText first;
    private EditText second;
    private EditText third;
    private EditText fourth;
    private String token;
    private String language;
    private GetMobile getMobile;

    public OTPFragment() {
        // Required empty public constructor
    }

    public OTPFragment(String number, String countryCode,GetMobile getMobile) {
        this.number = number;
        this.countryCode = countryCode;
        this.getMobile = getMobile;
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
        TextView otpMessageTV = view.findViewById(R.id.otpMessageTV);
        first = view.findViewById(R.id.etfirst);
        second = view.findViewById(R.id.etsecand);
        third = view.findViewById(R.id.etthird);
        fourth = view.findViewById(R.id.etfourth);
        TextView continueClick = view.findViewById(R.id.continueClick);

        Progress progress = new Progress(getContext());
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);


        otpMessageTV.setText(getString(R.string.numbermsg)+ "\n"+countryCode+" - "+number);

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
                ValidatePhoneViewModel viewModel = ViewModelProviders.of(this).get(ValidatePhoneViewModel.class);
                viewModel.init();
                viewModel.changeLanguage(token,number,fOtp);
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
            getMobile.getMobile(number);
        });
        mAlert.show();
    }
    private void onCancel(OTPFragment otpFragment) {
        otpFragment.dismiss();
    }
}