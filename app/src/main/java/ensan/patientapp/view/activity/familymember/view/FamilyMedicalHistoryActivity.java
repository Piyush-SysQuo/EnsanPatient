package ensan.patientapp.view.activity.familymember.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
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
import ensan.patientapp.view.activity.familymember.adapter.MedicalHistoryViewPagerAdapter;
import ensan.patientapp.view.activity.familymember.fragment.ActivityDetailsFragment;
import ensan.patientapp.view.activity.familymember.fragment.AllergyDetailsFragment;
import ensan.patientapp.view.activity.familymember.fragment.DietDetailsFragment;
import ensan.patientapp.view.activity.familymember.fragment.EmploymentDetailsFragment;
import ensan.patientapp.view.activity.familymember.fragment.MedicalHistoryDetailsFragment;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import ensan.patientapp.view.activity.medicalhistory.model.Activity;
import ensan.patientapp.view.activity.medicalhistory.model.Allergy;
import ensan.patientapp.view.activity.medicalhistory.model.Diet;
import ensan.patientapp.view.activity.medicalhistory.model.Employment;
import ensan.patientapp.view.activity.medicalhistory.model.Medical;
import ensan.patientapp.view.activity.medicalhistory.model.MedicalHistoryResponse;
import ensan.patientapp.view.activity.medicalhistory.viewmodel.MedicalHistoryViewModel;

public class FamilyMedicalHistoryActivity extends AppCompatActivity {

    private String token;
    private String familyID;
    private ViewPager medicalHistoryViewPager;
    private TabLayout tabLayout;
    private Progress progress;
    private MedicalHistoryResponse response;
    private Bundle bundle;
    public  List<Medical> medicalList;
    public  List<Allergy> allergyList;
    public  List<Diet> dietList;
    public  List<Activity> activityList;
    public  List<Employment> employmentList;
    private MedicalHistoryDetailsFragment medicalHistoryDetailsFragment;
    private AllergyDetailsFragment allergyDetailsFragment;
    private DietDetailsFragment dietDetailsFragment;
    private ActivityDetailsFragment activityDetailsFragment;
    private EmploymentDetailsFragment employmentDetailsFragment;
    private MedicalHistoryViewPagerAdapter viewPagerAdapter;
    private LoginResponse resp;
    private String language;

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

        setContentView(R.layout.activity_family_medical_history);

        // get data from intent
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                token = (String) bundle.get(Constants.KEY_TOKEN);
                int id = (int) bundle.get(Constants.KEY_FAMILY_MEMBER_ID);
                familyID = String.valueOf(id);
            }
        }

        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);
        progress.show();

        // Getting Medical History Data
        getMedicalHistory();


        // Finding the views
        medicalHistoryViewPager = findViewById(R.id.medicalHistoryViewPager);
        tabLayout = findViewById(R.id.tabLayout);

    }

    // open dialog
    private void openDialog(String msg){
        AlertPopup mAlert = new AlertPopup(this);
        mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert.setMessage(msg);
        mAlert.setOkButton(view -> mAlert.dismiss());
        mAlert.show();
    }

    protected void getMedicalHistory() {
        MedicalHistoryViewModel medicalHistoryViewModel = ViewModelProviders.of(this).get(MedicalHistoryViewModel.class);
        medicalHistoryViewModel.init();
        medicalHistoryViewModel.getMedicalHistory(token,familyID);
        medicalHistoryViewModel.getVolumesResponseLiveData().observe(this, medicalHistoryResponse -> {
            progress.hide();
            if(medicalHistoryResponse != null){

                medicalList =   medicalHistoryResponse.getData().getMedical();
                allergyList = medicalHistoryResponse.getData().getAllergy();
                dietList = medicalHistoryResponse.getData().getDiet();
                activityList = medicalHistoryResponse.getData().getActivity();
                employmentList = medicalHistoryResponse.getData().getEmployment();

                // Initializing the fragments
                medicalHistoryDetailsFragment = new MedicalHistoryDetailsFragment(medicalList);
                allergyDetailsFragment = new AllergyDetailsFragment(allergyList);
                dietDetailsFragment = new DietDetailsFragment(dietList);
                activityDetailsFragment = new ActivityDetailsFragment(activityList);
                employmentDetailsFragment = new EmploymentDetailsFragment(employmentList);


                // Setup Tab Layout
                tabLayout.setupWithViewPager(medicalHistoryViewPager);
                viewPagerAdapter = new MedicalHistoryViewPagerAdapter(getSupportFragmentManager(),0);
                viewPagerAdapter.addFragment(medicalHistoryDetailsFragment,getString(R.string.medical_history));
                viewPagerAdapter.addFragment(allergyDetailsFragment,getString(R.string.allergy));
                viewPagerAdapter.addFragment(dietDetailsFragment,getString(R.string.diet));
                viewPagerAdapter.addFragment(activityDetailsFragment,getString(R.string.activity));
                viewPagerAdapter.addFragment(employmentDetailsFragment,getString(R.string.employment));
                medicalHistoryViewPager.setAdapter(viewPagerAdapter);

                response = medicalHistoryResponse;

            }else{
                openDialog(this.getString(R.string.errormsg));
            }
        });

    }

    public void backPressed(View view) {
        finish();
    }

    public void editPressed(View view) {

        // save data into database
        Intent intent = new Intent(FamilyMedicalHistoryActivity.this, EditMedicalHistoryActivity.class);
        intent.putExtra(Constants.KEY_TOKEN,token);
        intent.putExtra(Constants.KEY_FAMILY_MEMBER_ID,familyID);
        Util.getInstance(getApplicationContext()).putGsonObject(
                Constants.KEY_MEDICAL_HISTORY_RESPONSE, response, new TypeToken<MedicalHistoryResponse>() {
                });
        startActivity(intent);
        finish();
    }
}