package ensan.patientapp.view.activity.signup.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.view.activity.signup.viewmodel.SignUpViewModel;
import ensan.patientapp.view.activity.termsandcondition.view.ConditionActivity;
import ensan.patientapp.view.activity.verification.view.VerificationActivity;

public class SignUpActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, CountryCodePicker.OnCountryChangeListener{

    @BindView(R.id.NameLayout)
    TextInputLayout nameLayout;

    @BindView(R.id.familyNameLayout)
    TextInputLayout familyNameLayout;

    @BindView(R.id.emailLayout)
    TextInputLayout emailLayout;

    @BindView(R.id.etpasswordLayout)
    TextInputLayout passwordLayout;

    @BindView(R.id.cnfrmPswrdLayout)
    TextInputLayout cnfrmPswrdLayout;

    @BindView(R.id.dobLayout)
    TextInputLayout dobLayout;

    @BindView(R.id.mobileNumberLayout)
    TextInputLayout mobileNumberLayout;

    @BindView(R.id.nameEt)
    TextInputEditText fName;

    @BindView(R.id.familyNameEt)
    TextInputEditText famName;

    @BindView(R.id.emailEt)
    TextInputEditText email;

    @BindView(R.id.passwordEt)
    TextInputEditText password;

    @BindView(R.id.cnfrmPswrdEt)
    TextInputEditText confPwd;

    @BindView(R.id.mobileNumberEt)
    TextInputEditText mobileNumberEt;

    @BindView(R.id.dobEt)
    TextView dobEt;

    @BindView(R.id.ck_condition)
    CheckBox checkBox;

    @BindView(R.id.img_cpwd)
    ImageView img_cpwd;

    @BindView(R.id.img_pwd)
    ImageView img_pwd;

    @BindView(R.id.txt_terms)
    TextView txt_terms;


    @BindView(R.id.ccp)
    CountryCodePicker ccp;

    @BindView(R.id.idNumberEt)
    TextInputEditText idNumberEt;

    @BindView(R.id.idNumberLayout)
    TextInputLayout idNumberLayout;


    private Progress progress;
    final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
    private boolean isValid = true;
    private String countryCode;
    private String countryCodeName;
    private String language;
    private  String replaceMobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get data from intent
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                language = bundle.getString(Constants.KEY_LANGUAGE);
                Utility.setLocale(this,language);
            }
        }

        setContentView(R.layout.sign_up_activity);

        // bind butter knife
        ButterKnife.bind(this);

        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);

        dobLayout.setOnClickListener(v -> {
            DialogFragment datePicker = new ensan.patientapp.Utils.DatePickerDob();
            datePicker.show(getSupportFragmentManager(), "date picker");
        });


        // Country Picker Listener
        ccp.setOnCountryChangeListener(this);
        countryCode = ccp.getDefaultCountryCodeWithPlus();
        countryCodeName = ccp.getDefaultCountryName();


        // password show
        img_pwd.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch ( event.getAction() ) {
                    case MotionEvent.ACTION_DOWN:
                        password.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case MotionEvent.ACTION_UP:
                        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return true;
            }
        });


        // confirm password show
        img_cpwd.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch ( event.getAction() ) {
                    case MotionEvent.ACTION_DOWN:
                        confPwd.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case MotionEvent.ACTION_UP:
                        confPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return true;
            }
        });


        // set hyper link into textView
        txt_terms.setText(Html.fromHtml( "<a href='ensane.patient.view.activity.signup.view://Kode'>"+getString(R.string.i_accept_all_terms_and_conditions)+"</a>"));
        txt_terms.setClickable(true);
        txt_terms.setMovementMethod(LinkMovementMethod.getInstance());
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

    public void nextClick(View view) {
      //  startActivity(new Intent(getApplicationContext(), VerificationActivity.class));
        String firstName =  fName.getText().toString().trim();
        String familyName = famName.getText().toString().trim();
        String user_mail = email.getText().toString().trim();
        String pwd = password.getText().toString().trim();
        String conPwd = confPwd.getText().toString().trim();
        String mobile = mobileNumberEt.getText().toString().trim();
        String dob = dobEt.getText().toString().trim();
        String idNo = idNumberEt.getText().toString().trim();

        if(mobile.startsWith("0")){
             replaceMobile = mobile.replace("0", "");
        }else {
            replaceMobile = mobile;
        }

        if(firstName.isEmpty()){
            nameLayout.setError(getString(R.string.nameerror));
            return;
        }else if(familyName.isEmpty()){
            familyNameLayout.setError(getString(R.string.familynameerror));
            return;
        }else if(dob.isEmpty()){
            dobLayout.setError(getString(R.string.doberror));
            return;
        } else if(user_mail.isEmpty()){
            emailLayout.setError(getString(R.string.emailerror));
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(user_mail).matches()){
            emailLayout.setError(getString(R.string.emailerror));
        } else if(pwd.isEmpty()){
            passwordLayout.setError(getString(R.string.pwderror));
            return;
        }else if(!(pwd.length() >= 6)){
            Toast.makeText(this, R.string.patternerror, Toast.LENGTH_SHORT).show();
            return;
        }else if(!pwd.matches(PASSWORD_PATTERN)){
            Toast.makeText(this, R.string.patternerror, Toast.LENGTH_SHORT).show();
            return;
        }else if (conPwd.isEmpty()){
            cnfrmPswrdLayout.setError(getString(R.string.pwderror));
            return;
        }else if(!pwd.equals(conPwd)){
            Toast.makeText(this,R.string.passmatcherror, Toast.LENGTH_SHORT).show();
            return;
        }else if(mobile.isEmpty()){
            mobileNumberLayout.setError(getString(R.string.mobileerror));
            return;
        }else if(!mobile.startsWith("5")){
            mobileNumberLayout.setError(getString(R.string.phoneerror5));
            return;
        }else if(mobile.length() != 9){
            mobileNumberLayout.setError(getString(R.string.phonelengtherror));
            return;
        }else if(!isValid){
            Toast.makeText(this, getString(R.string.doberrors), Toast.LENGTH_SHORT).show();
            return;
        }else if(!(idNo.startsWith("1") || idNo.startsWith("2"))){
            Toast.makeText(SignUpActivity.this, getString(R.string.iqamaerror), Toast.LENGTH_SHORT).show();
            return;
        }else if(checkBox.isChecked()){
            progress.show();
            // call sign up api
            SignUpViewModel viewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
            viewModel.init();
            viewModel.userSignUp(firstName,familyName,replaceMobile.trim(),user_mail,pwd, Constants.KEY_PATIENT_ROLE,dob,language,idNo,countryCode);
            viewModel.getVolumesResponseLiveData().observe(this, signUpResponse -> {

                progress.hide();

                if(signUpResponse!=null){
                    boolean status = signUpResponse.getStatus();
                    if(status){
                        String otp = signUpResponse.getData().getOtp();
                        Intent intent = new Intent(SignUpActivity.this, VerificationActivity.class);
                        intent.putExtra(Constants.KEY_MOBILE_NUMBER,mobile);
                        intent.putExtra(Constants.KEY_COUNTRY_CODE, countryCode);
                        intent.putExtra(Constants.KEY_OTP,otp);
                        intent.putExtra(Constants.KEY_LANGUAGE,language);
                        intent.putExtra(Constants.KEY_TOKEN,signUpResponse.getData().getToken());
                        startActivity(intent);
                        finish();
                    }else{
                        openDialog(signUpResponse.getMessage());
                    }
                }
                else{
                    openDialog(this.getString(R.string.errormsg));
                }
            });
        }else{
            openDialog(this.getString(R.string.acceptcondtionerror));
            return;
        }
    }

    public void backPressed(View view) {
        finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar userAge = new GregorianCalendar(year,month,dayOfMonth);
        Calendar minAdultAge = new GregorianCalendar();
        minAdultAge.add(Calendar.YEAR, -18);
        if (minAdultAge.before(userAge)) {
            isValid = false;
        }else{
            isValid = true;
        }
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime());
        dobEt.setText(currentDateString);

    }

    @Override
    public void onCountrySelected() {
        countryCode = ccp.getSelectedCountryCodeWithPlus();
        countryCodeName = ccp.getSelectedCountryName();
    }

    public void acceptTerms(View view) {
        Intent intent = new Intent(this, ConditionActivity.class);
        intent.putExtra(Constants.KEY_LANGUAGE,language);
       startActivity(intent);
    }
}