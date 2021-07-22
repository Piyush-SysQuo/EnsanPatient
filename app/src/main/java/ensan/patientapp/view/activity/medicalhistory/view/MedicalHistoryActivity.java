package ensan.patientapp.view.activity.medicalhistory.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Objects;

import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.Util;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.view.activity.editmedicalhistory.view.EditMedicalHistoryActivity;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import ensan.patientapp.view.activity.medicalhistory.adapter.MedicalListAdapterNew;
import ensan.patientapp.view.activity.medicalhistory.model.Activity;
import ensan.patientapp.view.activity.medicalhistory.model.Allergy;
import ensan.patientapp.view.activity.medicalhistory.model.Diet;
import ensan.patientapp.view.activity.medicalhistory.model.Employment;
import ensan.patientapp.view.activity.medicalhistory.model.Medical;
import ensan.patientapp.view.activity.medicalhistory.model.MedicalHistoryResponse;
import ensan.patientapp.view.activity.medicalhistory.viewmodel.MedicalHistoryViewModel;

public class MedicalHistoryActivity extends AppCompatActivity {

    private RecyclerView medicalRv;
    private String token;
    private Progress progress;
    private TextView activityTV,allergyTV,dietTV,empTV;
    private LinearLayout activityLayout,allergyLayout,dietLayout,empLayout,medicalHistoryLayout;
    private MedicalHistoryResponse response;
    private LoginResponse resp;
    private String language;
    private TextView bgTV, bmiTV;
    private String bloodGroup = "", bmi = "", height = "", weight = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get Token
        resp = (LoginResponse) Util.getInstance(this).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginResponse>() {
                });

        if(resp != null){
            language = resp.getData().getLanaguage();
            Utility.setLocale(this,language);
        }

        setContentView(R.layout.medical_history_activity);

        medicalRv = findViewById(R.id.medicalRv);
        activityTV = findViewById(R.id.activitiesTV);
        allergyTV = findViewById(R.id.allergiesTV);
        dietTV = findViewById(R.id.dietTV);
        empTV = findViewById(R.id.employementTV);
        medicalHistoryLayout = findViewById(R.id.medicalHistoryLayout);
        activityLayout = findViewById(R.id.activitiesLayout);
        allergyLayout = findViewById(R.id.allergyLayout);
        dietLayout = findViewById(R.id.dietLayout);
        empLayout = findViewById(R.id.employmentLayout);
        bgTV = findViewById(R.id.bloodGroupTV);
        bmiTV = findViewById(R.id.bmiTV);


        // get data from intent
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                token = (String) bundle.get(Constants.KEY_TOKEN);
                if (intent.hasExtra(Constants.KEY_BLOOD_GROUP)){
                    bloodGroup = (String) bundle.get(Constants.KEY_BLOOD_GROUP);
                }

                if (intent.hasExtra(Constants.KEY_BMI)){
                    bmi = (String) bundle.get(Constants.KEY_BMI);
                }

                if (intent.hasExtra(Constants.KEY_HEIGHT)){
                    height = (String) bundle.get(Constants.KEY_HEIGHT);
                }

                if (intent.hasExtra(Constants.KEY_WEIGHT)){
                    weight = (String) bundle.get(Constants.KEY_WEIGHT);
                }
            }
        }

        // Setting bmi and bg values
        bgTV.setText(bloodGroup);
        bmiTV.setText(bmi);

        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);
        progress.show();


        getMedicalHistory();
    }

    private void getMedicalHistory() {

        MedicalHistoryViewModel medicalHistoryViewModel = ViewModelProviders.of(this).get(MedicalHistoryViewModel.class);
        medicalHistoryViewModel.init();
        medicalHistoryViewModel.getMedicalHistory(token,"0");
        medicalHistoryViewModel.getVolumesResponseLiveData().observe(this, medicalHistoryResponse -> {
            progress.hide();
             if(medicalHistoryResponse != null){
               response = medicalHistoryResponse;
               List<Medical> medicalList =   medicalHistoryResponse.getData().getMedical();
               if (medicalList==null || medicalList.size()==0){
                   medicalHistoryLayout.setVisibility(View.GONE);
               }else{
                   medicalHistoryLayout.setVisibility(View.VISIBLE);
                   medicalRv.setLayoutManager(new LinearLayoutManager(MedicalHistoryActivity.this));
                   MedicalListAdapterNew medicalListAdapter = new MedicalListAdapterNew(medicalList,MedicalHistoryActivity.this);
                   medicalRv.setAdapter(medicalListAdapter);
               }

               // Setting activities
               List<Activity> activityList = medicalHistoryResponse.getData().getActivity();
               if (activityList==null || activityList.size()==0){
                   activityLayout.setVisibility(View.GONE);
               }else if (activityList.get(0).getSmoke().matches("no") && activityList.get(0).getExercise().matches("no")){
                   activityLayout.setVisibility(View.GONE);
               }else if((activityList.get(0).getSmoke().trim().isEmpty() && activityList.get(0).getExercise().trim().isEmpty())) {
                     activityLayout.setVisibility(View.GONE);
               } else {
                   activityLayout.setVisibility(View.VISIBLE);
                   String activities = "";
                   if (activityList.get(0).getSmoke().matches("yes")){
                       activities += "- "+getString(R.string.smoking)+"\n";
                   }
                   if (activityList.get(0).getExercise().matches("yes")){
                       activities += "- "+getString(R.string.excercise);
                   }
                   activityTV.setText(activities);
               }

                 // Setting diet
                 List<Diet> dietList = medicalHistoryResponse.getData().getDiet();
                 if (dietList==null || dietList.size()==0){
                     dietLayout.setVisibility(View.GONE);
                 }else{
                     dietLayout.setVisibility(View.VISIBLE);
                     String diets = "";
                     for (int i =0; i<dietList.size(); i++){
                         diets += "- " + dietList.get(i).getValue() + "\n";
                     }
                     dietTV.setText(diets);
                 }

                 // Setting allergies
                 List<Allergy> allergyList = medicalHistoryResponse.getData().getAllergy();
                 if (allergyList==null || allergyList.size()==0){
                     allergyLayout.setVisibility(View.GONE);
                 }else{
                     allergyLayout.setVisibility(View.VISIBLE);
                     String allergies = "";
                     for (int i =0; i<allergyList.size(); i++){
                         allergies += "- " + allergyList.get(i).getAllergyKey() + " : " + allergyList.get(i).getValue() + "\n";
                     }
                     allergyTV.setText(allergies);
                 }

                 // Setting employment
                 List<Employment> employmentList = medicalHistoryResponse.getData().getEmployment();
                 if (employmentList==null || employmentList.size()==0){
                     empLayout.setVisibility(View.GONE);
                 }else{
                     String employment = "";
                     if (employmentList.get(0).getWorking().matches("yes")){
                         employment += "- " + employmentList.get(0).getDetail();
                         empLayout.setVisibility(View.VISIBLE);
                     }else{
                         empLayout.setVisibility(View.GONE);
                     }
                     empTV.setText(employment);
                 }

             }else{
                 openDialog(this.getString(R.string.errormsg));
//                 Toast.makeText(this, getString(R.string.errormsg), Toast.LENGTH_SHORT).show();
             }
        });

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

    public void editPressed(View view) {
        // save data into database
        Intent intent = new Intent(MedicalHistoryActivity.this, EditMedicalHistoryActivity.class);
        intent.putExtra(Constants.KEY_TOKEN,token);
        intent.putExtra(Constants.KEY_BLOOD_GROUP,bloodGroup);
        intent.putExtra(Constants.KEY_BMI,bmi);
        intent.putExtra(Constants.KEY_HEIGHT,height);
        intent.putExtra(Constants.KEY_WEIGHT,weight);
        Util.getInstance(getApplicationContext()).putGsonObject(
                Constants.KEY_MEDICAL_HISTORY_RESPONSE, response, new TypeToken<MedicalHistoryResponse>() {
                });

        startActivity(intent);
        finish();
    }
}