package ensan.patientapp.view.activity.personaldetail.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.SuccessPopup;
import ensan.patientapp.Utils.Util;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import ensan.patientapp.view.activity.personaldetail.fragment.ChangeEmail;
import ensan.patientapp.view.activity.personaldetail.fragment.ChangeNewNumber;
import ensan.patientapp.view.activity.personaldetail.fragment.GenderFragment;
import ensan.patientapp.view.activity.personaldetail.fragment.MaritalStatusFragment;
import ensan.patientapp.view.activity.personaldetail.fragment.NationalityFragment;
import ensan.patientapp.view.activity.personaldetail.viewmodel.UpdateProfileViewModel;
import ensan.patientapp.view.activity.verification.model.GetMobileNumber;
import ensan.patientapp.view.activity.verification.viewmodel.ChangeNoViewModel;
import ensan.patientapp.view.fragment.personal.personalViewModel.PersonalViewModel;
import ensan.patientapp.view.fragment.setting.model.GetEmailData;
import ensan.patientapp.view.fragment.setting.view.OTPEmailFragment;
import ensan.patientapp.view.fragment.setting.view.OTPFragment;
import ensan.patientapp.view.fragment.setting.viewmodel.ChangeEmailViewModel;
import ensan.patientapp.view.fragment.personal.model.GetMobile;

public class PersonalDetailsActivity extends AppCompatActivity implements GetEmailData, GetMobileNumber, GetMobile {

    private String token;
    private Progress progress;
    private EditText txt_phone;
    public static EditText txt_email;
    private EditText childrenET;
    public static String maritalStatusCode, genderCode;
    public static EditText txt_marital_status, txt_national, txt_gender;
    private EditText txt_emergency_no;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get Token
        LoginResponse resp = (LoginResponse) Util.getInstance(this).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginResponse>() {
                });

        if(resp != null){
            String language = resp.getData().getLanaguage();
            Utility.setLocale(this, language);
        }

        setContentView(R.layout.personal_details_activity);

         // get data from intent
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                token = (String) bundle.get(Constants.KEY_TOKEN);
            }
        }

        // find id's
        txt_national = findViewById(R.id.txt_national);
        txt_gender = findViewById(R.id.txt_gender);
        txt_marital_status = findViewById(R.id.txt_marital_status);
        EditText txt_dob = findViewById(R.id.txt_dob);
        childrenET = findViewById(R.id.childrenET);
        txt_email = findViewById(R.id.txt_email);
        txt_phone = findViewById(R.id.txt_phone);
        txt_emergency_no = findViewById(R.id.txt_emergency_no);
        EditText txt_address = findViewById(R.id.txt_address);
        LinearLayout maritalLayout = findViewById(R.id.maritalLayout);
        LinearLayout genderLayout = findViewById(R.id.genderLayout);
        LinearLayout nationalityLayout = findViewById(R.id.nationalityLayout);
        ImageView editEmailIV = findViewById(R.id.editEmailIcon);
        ImageView editPhoneIV = findViewById(R.id.editPhoneIcon);
        ImageView editNationalityIV = findViewById(R.id.editNationalityIcon);
        ImageView editChildrenIV = findViewById(R.id.editChildrenIcon);
        ImageView editGenderIV = findViewById(R.id.editGenderIcon);
        ImageView editMaritalStatusIV = findViewById(R.id.editMaritalStatus);
        ImageView editEmergencyIV = findViewById(R.id.editEmergencyIcon);


        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        PersonalViewModel viewModel = ViewModelProviders.of(this).get(PersonalViewModel.class);
        viewModel.init();
        viewModel.getUserDetails(token);
        viewModel.getVolumesResponseLiveData().observe(this, userProfileResponse -> {
            progress.hide();
            if(userProfileResponse != null) {
                if (userProfileResponse.getStatus()) {
                    if(userProfileResponse.getData().getUser().getAddress() != null) {
                        txt_national.setText(userProfileResponse.getData().getUser().getNationality());
                        txt_address.setText(userProfileResponse.getData().getUser().getAddress().getAddress());
                    }
                    txt_gender.setText(userProfileResponse.getData().getUser().getGender());
                   txt_marital_status.setText(userProfileResponse.getData().getUser().getMaritalStatus());

                   switch (userProfileResponse.getData().getUser().getMaritalStatus()){
                       case "Married":
                       case "متزوج":
                           maritalStatusCode = "1";
                           break;
                       case "Divorced":
                       case "مطلقة":
                           maritalStatusCode = "2";
                           break;
                       case "Widowed":
                       case "الأرامل":
                           maritalStatusCode = "3";
                           break;
                       case "Separated":
                       case "فصل":
                           maritalStatusCode = "4";
                           break;
                       default:
                           maritalStatusCode = "0";
                   }

                    switch (userProfileResponse.getData().getUser().getGender()){
                        case "Male":
                        case "الذكر":
                            genderCode = "1";
                            break;
                        default:
                            genderCode = "0";
                    }

                   txt_dob.setText(userProfileResponse.getData().getUser().getDob());
                   txt_email.setText(userProfileResponse.getData().getUser().getEmail());
                   txt_phone.setText(userProfileResponse.getData().getUser().getPhone());
                   childrenET.setText(userProfileResponse.getData().getUser().getNo_childs());
                   txt_emergency_no.setText(userProfileResponse.getData().getUser().getEmergency_number());

                } else {
                    openDialog(this.getString(R.string.errormsg));
                }
            }else {
                openDialog(this.getString(R.string.errormsg));
            }
        });


        // update marital status
        editMaritalStatusIV.setOnClickListener(v -> {
            MaritalStatusFragment maritalStatusFragment = new MaritalStatusFragment();
            maritalStatusFragment.show(this.getSupportFragmentManager(),maritalStatusFragment.getTag());
        });

        // update nationality
        editNationalityIV.setOnClickListener(v -> {
            NationalityFragment nationalityFragment = new NationalityFragment();
            nationalityFragment.show(this.getSupportFragmentManager(),nationalityFragment.getTag());
        });

        // update gender
        editGenderIV.setOnClickListener(v -> {
            GenderFragment genderFragment = new GenderFragment();
            genderFragment.show(this.getSupportFragmentManager(),genderFragment.getTag());
        });

        // update email
        editEmailIV.setOnClickListener(v -> {
            ChangeEmail changeEmail = new ChangeEmail(this,this);
            changeEmail.show(this.getSupportFragmentManager(),changeEmail.getTag());
        });

        // update phone
        editPhoneIV.setOnClickListener(v -> {
            ChangeNewNumber changeNumber = new ChangeNewNumber(this);
            changeNumber.show(this.getSupportFragmentManager(),changeNumber.getTag());
        });


        // update children
        editChildrenIV.setOnClickListener(v -> {
            childrenET.requestFocus();
            childrenET.setFocusableInTouchMode(true);

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(childrenET, InputMethodManager.SHOW_FORCED);
        });

        // update emergency contact
        editEmergencyIV.setOnClickListener(v -> {
            txt_emergency_no.requestFocus();
            txt_emergency_no.setFocusableInTouchMode(true);

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(txt_emergency_no, InputMethodManager.SHOW_FORCED);
        });
    }

    // back pressed
    public void backPressed(View view) {
        finish();
    }

    public void updateProfile(View view) {

        if (txt_phone.getText().toString().trim().length() != 9){
            openDialog(this.getString(R.string.phonelengtherror));
            return;
        }

        if(txt_emergency_no.getText().toString().trim().startsWith("0") || txt_emergency_no.getText().toString().trim().startsWith("5") ){

            progress.show();
            UpdateProfileViewModel viewModel = ViewModelProviders.of(this).get(UpdateProfileViewModel.class);
            viewModel.init();
            viewModel.updateProfile(token,txt_email.getText().toString().trim(),txt_phone.getText().toString().trim(),maritalStatusCode,txt_emergency_no.getText().toString(),childrenET.getText().toString().trim(),genderCode,txt_national.getText().toString().trim());
            viewModel.getVolumesResponseLiveData().observe(this, updateProfileResponse -> {
                progress.dismiss();
                if(updateProfileResponse != null){
                    if (updateProfileResponse.getSuccess()){
                        openSuccessDialog(updateProfileResponse.getMessage());
                    }else {
                        openDialog(updateProfileResponse.getMessage());
                    }
                }else{
                    openDialog(this.getString(R.string.errormsg));
                }
            });

        }else{
            openDialog(getString(R.string.invalid_emergency_number));
            return;
        }
    }



    // open dialog
    private void openDialog(String msg){

        AlertPopup mAlert = new AlertPopup(this);
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

        SuccessPopup mAlert = new SuccessPopup(this);
        mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert.setMessage(msg);
        mAlert.setOkButton(view -> {
            mAlert.dismiss();
            //Do want you want
        });
        mAlert.show();
    }

    @Override
    public void getEmail(String email) {
        progress.show();
        ChangeEmailViewModel changeEmailViewModel = ViewModelProviders.of(this).get(ChangeEmailViewModel.class);
        changeEmailViewModel.init();
        changeEmailViewModel.changeLanguage(token,email);
        changeEmailViewModel.getVolumesResponseLiveData().observe(this, changeEmailResponse -> {
            progress.hide();
            if(changeEmailResponse != null){
                if(changeEmailResponse.getSuccess()){
                    OTPEmailFragment otpEmailFragment = new OTPEmailFragment(this,email);
                    otpEmailFragment.show(this.getSupportFragmentManager(),otpEmailFragment.getTag());
                }else {
                    Toast.makeText(this, changeEmailResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }else{
                openDialog(getString(R.string.emailerror));
            }
        });
    }

    @Override
    public void getMobile(String number, String countryCode) {

        if(!number.startsWith("5")){
            openDialog(getString(R.string.phoneerror5));
            return;
        }else if(number.length() == 9){
            progress.show();
            ChangeNoViewModel viewModel = ViewModelProviders.of(this).get(ChangeNoViewModel.class);
            viewModel.init();
            viewModel.changeNumber(token,number,countryCode);
            viewModel.getVolumesResponseLiveData().observe(this, changeNewNoResponse -> {
                progress.hide();
                if(changeNewNoResponse != null){
                    if(!changeNewNoResponse.getMessage().equals(getString(R.string.phoneerrormsg))) {
                        OTPFragment otpFragment = new OTPFragment(number, countryCode,this);
                        otpFragment.show(this.getSupportFragmentManager(), otpFragment.getTag());
                    }else {
                        openDialog(changeNewNoResponse.getMessage());
                    }
                }else{
                    openDialog(getString(R.string.errormsg));
                }
            });

        }else {
            openDialog(getString(R.string.phonelengtherror));
            return;
        }
    }

    @Override
    public void getMobile(@NotNull String mobile) {
        txt_phone.setText(mobile);
    }
}