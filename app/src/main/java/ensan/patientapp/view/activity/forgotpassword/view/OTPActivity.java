package ensan.patientapp.view.activity.forgotpassword.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;
import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.SuccessPopup;
import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.completeprofile.view.CompleteProfileFrstActivity;
import ensan.patientapp.view.activity.verification.model.OtpResponse;
import ensan.patientapp.view.activity.verification.viewmodel.OtpViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity {

    private Integer otp;
    private String email;
    private String mobNumber;

    @BindView(R.id.etfirst)
    EditText first;

    @BindView(R.id.etsecand)
    EditText second;

    @BindView(R.id.etthird)
    EditText third;

    @BindView(R.id.etfourth)
    EditText fourth;

    @BindView(R.id.txt_mobile)
    TextView txt_mobile;

    private Progress progress;

    @BindView(R.id.txt_resend)
    TextView txt_resend;

    private SharedPreferences sharedPreferences;
    private String language;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_activity);

        // bind data to butter_knife
        ButterKnife.bind(this);

        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);

      // get data from intent
        Intent intent = getIntent();
        if(intent != null){
           Bundle bundle = intent.getExtras();
           if(bundle != null){
               otp = (Integer) bundle.get(Constants.KEY_OTP);
               email = (String) bundle.get(Constants.KEY_EMAIL);
               mobNumber = (String) bundle.get(Constants.KEY_MOBILE_NUMBER);

           }
        }

        // setMobile no into text view
        txt_mobile.setText( getString(R.string.otp_number)+"\n"+mobNumber);

        textWatcherForOtp();

        // get data from shared preferences
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_LANGUAGE,MODE_PRIVATE);
        language = sharedPreferences.getString(Constants.KEY_LANGUAGE,"");

    }

    // open dialog
    private void openDialog(String msg){

        AlertPopup mAlert = new AlertPopup(this);
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
    public void backPressed(View view) {
        finish();
    }

    // continue button click
    public void continueClick(View view) {
        String firstBox = first.getText().toString().trim();
        String secBox = second.getText().toString().trim();
        String thirdBox = third.getText().toString().trim();
        String fourthBox = fourth.getText().toString().trim();

        if(firstBox.isEmpty()){
            Toast.makeText(this, R.string.otperror, Toast.LENGTH_SHORT).show();
            return;
        }else if(secBox.isEmpty()){
            Toast.makeText(this, R.string.otperror, Toast.LENGTH_SHORT).show();
            return;
        }else if(thirdBox.isEmpty()){
            Toast.makeText(this, R.string.otperror, Toast.LENGTH_SHORT).show();
            return;
        }else if(fourthBox.isEmpty()){
            Toast.makeText(this, R.string.otperror, Toast.LENGTH_SHORT).show();
            return;
        }else {
             String fOtp = firstBox+secBox+thirdBox+fourthBox;
            progress.show();

            /*call otp verification api*/
            OtpViewModel otpViewModel = ViewModelProviders.of(this).get(OtpViewModel.class);
            otpViewModel.init();
            otpViewModel.otpVerification(mobNumber,fOtp);
            otpViewModel.getVolumesResponseLiveData().observe(this, otpResponse -> {
                progress.hide();

                if(otpResponse != null){
                    if(otpResponse.getSuccess()){
                        Intent intent = new Intent(OTPActivity.this,ResetPassword.class);
                        intent.putExtra(Constants.KEY_EMAIL,email);
                        startActivity(intent);
                    }else{
                        openDialog(otpResponse.getMessage());
                    }
                }else{
                    openDialog(getString(R.string.errormsg));
                }
            });
        }
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
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
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

        // resend otp clicked
        txt_resend.setOnClickListener(v -> {
            progress.show();
            ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
            Call<OtpResponse> call = apiDataService.resend(email,language,Constants.KEY_PATIENT_ROLE);
            call.enqueue(new Callback<OtpResponse>() {
                @Override
                public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                    progress.hide();
                    if (response.body() != null) {
                        OtpResponse  otpResponse = response.body();
                        if(otpResponse.getSuccess()) {
                            openSuccDialog(otpResponse.getMessage());
                        }else {
                            openDialog(otpResponse.getMessage());
                        }
                    }else{
                        openDialog(getString(R.string.errormsg));
                    }
                }

                @Override
                public void onFailure(Call<OtpResponse> call, Throwable t) {
                    progress.hide();
                    call.cancel();
                    openDialog(getString(R.string.errormsg));
                }
            });
        });
    }

    // open dialog
    private void openSuccDialog(String msg){
        SuccessPopup mAlert = new SuccessPopup(this);
        mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert.setMessage(msg);
        mAlert.setOkButton(view -> {
            mAlert.dismiss();
        });
        mAlert.show();
    }
}