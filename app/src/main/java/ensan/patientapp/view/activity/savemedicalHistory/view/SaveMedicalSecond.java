package ensan.patientapp.view.activity.savemedicalHistory.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.SuccessPopup;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.view.activity.completeprofile.model.Data;
import ensan.patientapp.view.activity.home.view.HomeActivity;
import ensan.patientapp.view.activity.login.view.LoginActivity;
import ensan.patientapp.view.activity.savemedicalHistory.viewmodel.ActivityViewModel;
import ensan.patientapp.view.activity.savemedicalHistory.viewmodel.DietViewModel;
import ensan.patientapp.view.activity.savemedicalHistory.viewmodel.EmpViewModel;

import static android.view.View.VISIBLE;

public class SaveMedicalSecond extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    String token;
    JsonArray dietArray = new JsonArray();
    ArrayList<String> stringArrayList = new ArrayList<>();
    JSONArray jsonArray = new JSONArray();



    @BindView(R.id.ck_diabetic)
    CheckBox ck_diabetic;

    @BindView(R.id.ck_keto)
    CheckBox ck_keto;

    @BindView(R.id.ck_vegan)
    CheckBox ck_vegan;

    @BindView(R.id.ck_vegetarian)
    CheckBox ck_vegetarian;

    @BindView(R.id.ck_raw_food)
    CheckBox ck_raw_food;

    @BindView(R.id.ck_law_salt)
    CheckBox ck_law_salt;

    @BindView(R.id.ck_zone)
    CheckBox ck_zone;

    @BindView(R.id.ck_smoke)
    CheckBox ck_smoke;

    @BindView(R.id.ck_exercise)
    CheckBox ck_exercise;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    @BindView(R.id.radioButton1)
    RadioButton yes;

    @BindView(R.id.radioButton2)
    RadioButton no;

    @BindView(R.id.etDes)
    EditText etDes;

    @BindView(R.id.employmentFormLayout)
    LinearLayout employmentFormLayout;

    private Progress progress;
    private String working = "";
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get data from intent
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                token = (String) bundle.get(Constants.KEY_TOKEN);
                language = (String) bundle.get(Constants.KEY_LANGUAGE);
                Utility.setLocale(this,language);
            }
        }

        setContentView(R.layout.activity_save_medical_hostory_secand);

        //
        ButterKnife.bind(this);

        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);

        ck_diabetic.setOnCheckedChangeListener(this);
        ck_keto.setOnCheckedChangeListener(this);
        ck_vegan.setOnCheckedChangeListener(this);
        ck_vegetarian.setOnCheckedChangeListener(this);
        ck_raw_food.setOnCheckedChangeListener(this);
        ck_law_salt.setOnCheckedChangeListener(this);
        ck_zone.setOnCheckedChangeListener(this);

        radioGroup.setOnCheckedChangeListener(this);
        yes.setOnCheckedChangeListener(this);
        no.setOnCheckedChangeListener(this);
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

    // open success dialog
    private void openSuccessDialog(String msg){

        SuccessPopup mAlert = new SuccessPopup(this);
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

    // open success dialog
    private void openSuccessFinalDialog(String msg){

        SuccessPopup mAlert = new SuccessPopup(this);
        mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert.setMessage(msg);
        mAlert.setCancelable(false);
        mAlert.setOkButton( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlert.dismiss();
                //Do want you want
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra(Constants.KEY_TOKEN,token);
                intent.putExtra(Constants.KEY_LANGUAGE,language);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
        mAlert.show();
    }

    public void backPressed(View view) {
        finish();
    }

    public void nextClick(View view) {
        Intent intent = new Intent(new Intent(this, HomeActivity.class));
        intent.putExtra(Constants.KEY_TOKEN,token);
        intent.putExtra(Constants.KEY_LANGUAGE,language);
        intent.putExtra(Constants.KEY_FROM,"signup");
        startActivity(intent);
        finish();
    }

    public void saveDiet(View view) {
        if(!ck_diabetic.isChecked()){
            stringArrayList.remove(ck_diabetic.getText().toString());
        }
        if(!ck_keto.isChecked()){
            stringArrayList.remove(ck_keto.getText().toString());
        }
        if(!ck_vegan.isChecked()){
            stringArrayList.remove(ck_vegan.getText().toString());
        }
        if(!ck_vegetarian.isChecked()){
            stringArrayList.remove(ck_vegetarian.getText().toString());
        }
        if(!ck_raw_food.isChecked()){
            stringArrayList.remove(ck_raw_food.getText().toString());
        }
        if(!ck_law_salt.isChecked()){
            stringArrayList.remove(ck_law_salt.getText().toString());
        }
        if(!ck_zone.isChecked()){
            stringArrayList.remove(ck_zone.getText().toString());
        }

        progress.show();

        JSONArray jsArray = new JSONArray(stringArrayList);
        DietViewModel viewModel = ViewModelProviders.of(this).get(DietViewModel.class);
        viewModel.init();
        viewModel.saveDiet(token,jsArray,"0");
        viewModel.getVolumesResponseLiveData().observe(this, dietResponse -> {
            progress.hide();
            if(dietResponse != null){
                if (dietResponse.getSuccess()){
                    openSuccessDialog(dietResponse.getMessage());
                }else {
                    openDialog(dietResponse.getMessage());
                }
            }else{
                openDialog(this.getString(R.string.errormsg));
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()){
            case R.id.ck_diabetic :
                if(buttonView.isChecked()){
                    stringArrayList.add(ck_diabetic.getText().toString());
                }
                break;
            case R.id.ck_keto :
                if(buttonView.isChecked()){
                    stringArrayList.add(ck_keto.getText().toString());
                }
                break;
            case R.id.ck_vegan :
                if(buttonView.isChecked()){
                    stringArrayList.add(ck_vegan.getText().toString());
                }
                break;
            case R.id.ck_vegetarian :
                if(buttonView.isChecked()){
                    stringArrayList.add(ck_vegetarian.getText().toString());
                }
                break;
            case R.id.ck_raw_food :
                if(buttonView.isChecked()){
                    stringArrayList.add(ck_raw_food.getText().toString());
                }
                break;
            case R.id.ck_law_salt :
                if(buttonView.isChecked()){
                    stringArrayList.add(ck_law_salt.getText().toString());
                }
                break;
            case R.id.ck_zone :
                if(buttonView.isChecked()){
                    stringArrayList.add(ck_zone.getText().toString());
                }
                break;
        }
    }

    public void saveActivity(View view) {

        progress.show();

        String smoke = "";
        String exercise = "";
        if(ck_smoke.isChecked()){
            smoke = "yes";
        }else{
            smoke = "no";
        }

        if(ck_exercise.isChecked()){
            exercise = "yes";
        }else{
            exercise = "no";
        }

        ActivityViewModel activityViewModel = ViewModelProviders.of(this).get(ActivityViewModel.class);
        activityViewModel.init();
        activityViewModel.saveActivity(token,smoke,exercise,"0");
        activityViewModel.getVolumesResponseLiveData().observe(this, activityResponse -> {
             progress.hide();
             if(activityResponse != null){
                 if (activityResponse.getSuccess()){
                     openSuccessDialog(activityResponse.getMessage());
                 }else {
                     openDialog(activityResponse.getMessage());
                 }
             }else {
                 openDialog(this.getString(R.string.errormsg));
             }
        });

    }

    public void saveEmployee(View view) {
        String description = etDes.getText().toString().trim();

        if(working.matches("yes") && description.isEmpty()){
            Toast.makeText(this, R.string.deserror, Toast.LENGTH_SHORT).show();
            return;
        }

        progress.show();

        EmpViewModel viewModel = ViewModelProviders.of(this).get(EmpViewModel.class);
        viewModel.init();
        viewModel.saveEmp(token,working,description,"0");
        viewModel.getVolumesResponseLiveData().observe(this, employmentResponse -> {
            progress.hide();
             if (employmentResponse != null){
                 if (employmentResponse.getSuccess()){
                     openSuccessDialog(employmentResponse.getMessage());
                 }else {
                     openDialog(employmentResponse.getMessage());
                 }
             }else{
                 openDialog(this.getString(R.string.errormsg));
             }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
         switch (checkedId){
             case R.id.radioButton1 :
                 working = "yes";
                 employmentFormLayout.setVisibility(VISIBLE);
                 break;
             case R.id.radioButton2:
                 working = "no";
                 employmentFormLayout.setVisibility(View.GONE);
                 break;
         }
    }
}