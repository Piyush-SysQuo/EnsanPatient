package ensan.patientapp.view.activity.verification.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.view.activity.completeprofile.view.CompleteProfileFrstActivity;
import ensan.patientapp.R;
import ensan.patientapp.view.activity.verification.model.GetMobileNumber;
import ensan.patientapp.view.activity.verification.viewmodel.ChangeNoViewModel;
import ensan.patientapp.view.activity.verification.viewmodel.OtpViewModel;
import ensan.patientapp.view.activity.verification.viewmodel.SendOtpViewModel;

public class VerificationActivity extends AppCompatActivity implements GetMobileNumber{

    private String mobNumber;
    private String otp;
    private String token;

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

    @BindView(R.id.txt_change_number)
    TextView txt_change_number;

    @BindView(R.id.txt_timer)
    TextView txt_timer;

    @BindView(R.id.txt_resend)
    TextView txt_resend;


    private Progress progress;
    private String countryCode;
    private String language;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // get data from intent
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                countryCode = (String) bundle.get(Constants.KEY_COUNTRY_CODE);
                mobNumber = (String) bundle.get(Constants.KEY_MOBILE_NUMBER);
                otp = (String) bundle.get(Constants.KEY_OTP);
                token = (String) bundle.get(Constants.KEY_TOKEN);
                language = bundle.getString(Constants.KEY_LANGUAGE);
                Utility.setLocale(this,language);
            }
        }

        setContentView(R.layout.verification_activity);


        // bind data to butter_knife
        ButterKnife.bind(this);

        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);


       // setMobile no into text view
        txt_mobile.setText(getString(R.string.otp_number)+ "\n"+countryCode+" - "+mobNumber);

       // otp box
      textWatcherForOtp();

      // otp timer
        showOtpTimer();

      // open dialog for change mobile number
        txt_change_number.setOnClickListener(v -> {
            ChangeNumber changeNumber = new ChangeNumber(this);
            changeNumber.setCancelable(false);
            changeNumber.show(getSupportFragmentManager(),changeNumber.getTag());
        });


        // timer clickable
        txt_resend.setOnClickListener(v -> {
            progress.show();
            SendOtpViewModel viewModel = ViewModelProviders.of(this).get(SendOtpViewModel.class);
            viewModel.init();
            viewModel.otpVerification(token);
            viewModel.getVolumesResponseLiveData().observe(this, otpResponse -> {
                progress.hide();
                if(otpResponse != null){
                    Toast.makeText(this, otpResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    // otp timer
                    showOtpTimer();

                }else {
                    Toast.makeText(this, getString(R.string.errormsg), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void showOtpTimer() {

        new CountDownTimer(180000, 1000) {

            public void onTick(long millisUntilFinished) {
                txt_timer.setText(getString(R.string.remaining) + millisUntilFinished / 1000);
                txt_resend.setClickable(false);
                //here you can have your logic to set text to edit text
            }

            public void onFinish() {
                txt_resend.setClickable(true);
                txt_resend.setTextColor(R.color.light_green);

            }

        }.start();
    }

    public void backPressed(View view) {
        finish();
    }

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
                        Intent intent = new Intent(getApplicationContext(), CompleteProfileFrstActivity.class);
                        intent.putExtra(Constants.KEY_LANGUAGE,language);
                        intent.putExtra(Constants.KEY_TOKEN,token);
                        startActivity(intent);
                        finish();
                    }else{
                        openDialog(otpResponse.getMessage());
                    }
                }else{
                    openDialog(this.getString(R.string.errormsg));
                }
            });
        }
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
    }

    @Override
    public void getMobile(String number,String countryCode) {
        mobNumber = number;
        if(number.length() != 9){
            openDialog(getString(R.string.phonelengtherror));
            return;
        }else  if(!mobNumber.startsWith("5")){
            openDialog(getString(R.string.phoneerror5));
            return;
        }else {
            progress.show();
            ChangeNoViewModel viewModel = ViewModelProviders.of(this).get(ChangeNoViewModel.class);
            viewModel.init();
            viewModel.changeNumber(token, number, countryCode);
            viewModel.getVolumesResponseLiveData().observe(this, otpResponse -> {
                progress.hide();
                if (otpResponse != null) {
                    if (otpResponse.getSuccess()) {
                        if (!otpResponse.getMessage().equals(getString(R.string.phonenoerror))) {
                            txt_mobile.setText(getString(R.string.otp_number) + "\n" + countryCode + " - " + number);
                        }
                    }
                    Toast.makeText(this, otpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.errormsg), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}