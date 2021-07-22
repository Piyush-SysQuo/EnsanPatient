package ensan.patientapp.view.activity.editmedicalhistory.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.SuccessPopup;
import ensan.patientapp.Utils.Util;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.completeprofile.adapter.DropDownProfileAdapter;
import ensan.patientapp.view.activity.editmedicalhistory.adapter.MedicalListAdapter;
import ensan.patientapp.view.activity.editmedicalhistory.viewmodel.UpdateProfileInfoResponseViewModel;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import ensan.patientapp.view.activity.medicalhistory.model.Activity;
import ensan.patientapp.view.activity.medicalhistory.model.Allergy;
import ensan.patientapp.view.activity.medicalhistory.model.Diet;
import ensan.patientapp.view.activity.medicalhistory.model.Employment;
import ensan.patientapp.view.activity.medicalhistory.model.Medical;
import ensan.patientapp.view.activity.medicalhistory.model.MedicalHistoryResponse;
import ensan.patientapp.view.activity.savemedicalHistory.model.DietResponse;
import ensan.patientapp.view.activity.savemedicalHistory.viewmodel.ActivityViewModel;
import ensan.patientapp.view.activity.savemedicalHistory.viewmodel.AllergyViewModel;
import ensan.patientapp.view.activity.savemedicalHistory.viewmodel.DietViewModel;
import ensan.patientapp.view.activity.savemedicalHistory.viewmodel.EmpViewModel;
import ensan.patientapp.view.activity.savemedicalHistory.viewmodel.MedicalHistoryViewModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class EditMedicalHistoryActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener  {

    private MedicalHistoryResponse resp;
    String token="", familyID="0";
    String[] bloodGroup = new String[]{};
    private String bloodGrp = "";
    private String bmiVal="", height = "", weight = "";

    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.ck_condition)
    CheckBox ck_condition;

    @BindView(R.id.layout_medical_condition)
    LinearLayout layout_medical_condition;

    @BindView(R.id.etMedicalCondition)
    EditText etMedicalCondition;

    @BindView(R.id.ck_surgery)
    CheckBox ck_surgery;

    @BindView(R.id.layout_surgery)
    LinearLayout layout_surgery;

    @BindView(R.id.etsurgery)
    EditText etsurgery;

    @BindView(R.id.ck_med_routine)
    CheckBox ck_med_routine;

    @BindView(R.id.layout_medical_routine)
    LinearLayout layout_medical_routine;

    @BindView(R.id.etmedicalroutine)
    EditText etmedicalroutine;

    @BindView(R.id.ck_trauma)
    CheckBox ck_trauma;

    @BindView(R.id.layout_trauma)
    LinearLayout layout_trauma;

    @BindView(R.id.ettrauma)
    EditText ettrauma;

    @BindView(R.id.ck_comorbidities)
    CheckBox ck_comorbidities;

    @BindView(R.id.layout_comorbidities)
    LinearLayout layout_comorbidities;

    @BindView(R.id.etcomorbidities)
    EditText etcomorbidities;

    @BindView(R.id.ck_condition_mobility)
    CheckBox ck_condition_mobility;

    @BindView(R.id.ck_allergy)
    CheckBox ck_allergy;

    @BindView(R.id.ck_allergy_food)
    CheckBox ck_allergy_food;

    @BindView(R.id.ck_allergy_drug)
    CheckBox ck_allergy_drug;

    @BindView(R.id.ck_allergy_other)
    CheckBox ck_allergy_other;

    @BindView(R.id.general_condition_mobility)
    LinearLayout general_condition_mobility;

    @BindView(R.id.etmobility)
    EditText etmobility;

    @BindView(R.id.etfood)
    EditText etfood;

    @BindView(R.id.etdrug)
    EditText etdrug;

    @BindView(R.id.etother)
    EditText etother;

    @BindView(R.id.layoutMedicalCondition)
    RelativeLayout layoutMedicalCondition;

    @BindView(R.id.uplayoutMedicalCondition)
    RelativeLayout uplayoutMedicalCondition;

    @BindView(R.id.btnmcondition)
    TextView btnmcondition;

    @BindView(R.id.layoutsurgery)
    RelativeLayout layoutsurgery;

    @BindView(R.id.uplayoutsurgery)
    RelativeLayout uplayoutsurgery;

    @BindView(R.id.btnsurgenrycondition)
    TextView btnsurgenrycondition;

    @BindView(R.id.layoutmedicalroutine)
    RelativeLayout layoutmedicalroutine;

    @BindView(R.id.uplayoutmedroutine)
    RelativeLayout uplayoutmedroutine;

    @BindView(R.id.btnmedicalroutine)
    TextView btnmedicalroutine;

    @BindView(R.id.uplayouttrauma)
    RelativeLayout uplayouttrauma;

    @BindView(R.id.layouttrauma)
    RelativeLayout layouttrauma;

    @BindView(R.id.btnTrauma)
    TextView btnTrauma;

    @BindView(R.id.uplayoutcomorbidities)
    RelativeLayout uplayoutcomorbidities;

    @BindView(R.id.layoutcomorbidities)
    RelativeLayout layoutcomorbidities;

    @BindView(R.id.btncomorbidities)
    TextView btncomorbidities;

    @BindView(R.id.uplayoutconditionmobility)
    RelativeLayout uplayoutconditionmobility;

    @BindView(R.id.layoutconditionmobility)
    RelativeLayout layoutconditionmobility;

    @BindView(R.id.btngenralcondition)
    TextView btngenralcondition;

    @BindView(R.id.layout_allergy)
    LinearLayout layout_allergy;

    @BindView(R.id.layoutfood)
    LinearLayout layoutfood;

    @BindView(R.id.layoutdruglayout)
    LinearLayout layoutdruglayout;

    @BindView(R.id.layoutdotherlayout)
    LinearLayout layoutdotherlayout;

    @BindView(R.id.layout_action)
    LinearLayout layout_action;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    @BindView(R.id.radioButton1)
    RadioButton yes;

    @BindView(R.id.radioButton2)
    RadioButton no;

    @BindView(R.id.bloodGroupET)
    TextInputEditText bloodGroupET;

    @BindView(R.id.bmiET)
    TextInputEditText bmiET;

    @BindView(R.id.heightEt)
    TextInputEditText heightEt;

    @BindView(R.id.weightEt)
    TextInputEditText weightEt;

    @BindView(R.id.bmiLayout)
    TextInputLayout bmiLayout;

    @BindView(R.id.heightLayout)
    TextInputLayout heightLayout;

    @BindView(R.id.weightLayout)
    TextInputLayout weightLayout;

    @BindView(R.id.medicalConditionRv)
    RecyclerView medicalConditionRv;

    @BindView(R.id.surgeryRv)
    RecyclerView surgeryRv;

    @BindView(R.id.medicalRoutineRv)
    RecyclerView medicalRoutineRv;

    @BindView(R.id.traumaRv)
    RecyclerView traumaRv;

    @BindView(R.id.comorbiditiesRv)
    RecyclerView comorbiditiesRv;

    @BindView(R.id.mobilityRv)
    RecyclerView mobilityRv;

//    @BindView(R.id.ck_ploeo)
//    CheckBox ck_ploeo;
//
//    @BindView(R.id.ck_blood)
//    CheckBox ck_blood;
//
//    @BindView(R.id.ck_vegon)
//    CheckBox ck_vegon;
//
//    @BindView(R.id.ck_the_mediterranean_diet)
//    CheckBox ck_the_mediterranean_diet;
//
//    @BindView(R.id.ck_raw_food_diet)
//    CheckBox raw_food_diet;

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

    @BindView(R.id.radioGroupEmp)
    RadioGroup radioGroupEmp;

    @BindView(R.id.radioButton1Emp)
    RadioButton yesEmp;

    @BindView(R.id.radioButton2Emp)
    RadioButton noEmp;

    @BindView(R.id.etDes)
    EditText etDes;

    @BindView(R.id.buttonsLayout)
    LinearLayout buttonsLayout;

    @BindView(R.id.updateProfileBtn)
    TextView updateProfileBtn;

    private Progress progress;
    private File file;
    private String imageID = "0";
    private String working = "";
    JsonArray dietArray = new JsonArray();
    ArrayList<String> stringArrayList = new ArrayList<>();
    JSONArray jsonArray = new JSONArray();
    private LoginResponse loginResponse;
    private String language;

    // Flag variable to check for which checkbox view, we have uploaded images, since first checkbox is medical-condition, thus it has the same value
    private String selectionFlag="medical-condition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get Token
        loginResponse = (LoginResponse) Util.getInstance(this).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginResponse>() {
                });

        if(loginResponse != null){
            language = loginResponse.getData().getLanaguage();
            Utility.setLocale(this,language);
        }

        setContentView(R.layout.activity_edit_medical_history);
        ButterKnife.bind(this);


        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);

        // get Token
        resp = (MedicalHistoryResponse) Util.getInstance(this).pickGsonObject(
                Constants.KEY_MEDICAL_HISTORY_RESPONSE, new TypeToken<MedicalHistoryResponse>() {
                });

        // get data from intent
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                token = (String) bundle.get(Constants.KEY_TOKEN);
                if (intent.hasExtra(Constants.KEY_FAMILY_MEMBER_ID)){
                    familyID = (String) bundle.get(Constants.KEY_FAMILY_MEMBER_ID);
                }
                if (intent.hasExtra(Constants.KEY_BLOOD_GROUP)){
                    bloodGrp = (String) bundle.get(Constants.KEY_BLOOD_GROUP);
                }
                if (intent.hasExtra(Constants.KEY_BMI)){
                    bmiVal = (String) bundle.get(Constants.KEY_BMI);
                }
                if (intent.hasExtra(Constants.KEY_HEIGHT)){
                    height = (String) bundle.get(Constants.KEY_HEIGHT);
                }
                if (intent.hasExtra(Constants.KEY_WEIGHT)){
                    weight = (String) bundle.get(Constants.KEY_WEIGHT);
                }
            }
        }

        bloodGroup = getResources().getStringArray(R.array.blood_group);

        ck_condition.setOnCheckedChangeListener(this);
        ck_surgery.setOnCheckedChangeListener(this);
        ck_allergy_food.setOnCheckedChangeListener(this);
        ck_med_routine.setOnCheckedChangeListener(this);
        ck_comorbidities.setOnCheckedChangeListener(this);
        ck_trauma.setOnCheckedChangeListener(this);
        ck_condition_mobility.setOnCheckedChangeListener(this);
        ck_allergy.setOnCheckedChangeListener(this);
        ck_allergy_drug.setOnCheckedChangeListener(this);
        ck_allergy_other.setOnCheckedChangeListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        yes.setOnCheckedChangeListener(this);
        no.setOnCheckedChangeListener(this);
        ck_diabetic.setOnCheckedChangeListener(this);
        ck_keto.setOnCheckedChangeListener(this);
        ck_vegan.setOnCheckedChangeListener(this);
        ck_vegetarian.setOnCheckedChangeListener(this);
        ck_raw_food.setOnCheckedChangeListener(this);
        ck_law_salt.setOnCheckedChangeListener(this);
        ck_zone.setOnCheckedChangeListener(this);

        radioGroupEmp.setOnCheckedChangeListener(this);
        yesEmp.setOnCheckedChangeListener(this);
        noEmp.setOnCheckedChangeListener(this);

        if(ck_condition.isChecked()){
            layout_medical_condition.setVisibility(VISIBLE);
        }
        if(ck_condition_mobility.isChecked()){
            general_condition_mobility.setVisibility(VISIBLE);
        }


        // save medical condition  data
        layoutMedicalCondition.setOnClickListener(v -> {
            selectionFlag = "medical-condition";
            checkRunTimePermission();
        });

        layoutsurgery.setOnClickListener(v -> {
            selectionFlag = "surgery";
            checkRunTimePermission();
        });

        layoutmedicalroutine.setOnClickListener(v -> {
            selectionFlag = "medical-routine";
            checkRunTimePermission();
        });

        layouttrauma.setOnClickListener(v -> {
            selectionFlag = "trauma";
            checkRunTimePermission();
        });

        layoutcomorbidities.setOnClickListener(v -> {
            selectionFlag = "comorbidities";
            checkRunTimePermission();
        });

        layoutconditionmobility.setOnClickListener(v -> {
            selectionFlag = "condition-mobility";
            checkRunTimePermission();
        });

        // Setting the current values
        if(resp != null){
            // Hiding BMI and Blood group Layouts
            if (!familyID.matches("0")){
                spinner.setVisibility(GONE);
                bmiLayout.setVisibility(GONE);
                heightLayout.setVisibility(GONE);
                weightLayout.setVisibility(GONE);
                updateProfileBtn.setVisibility(GONE);
            }

            // Setting allergy status
            List<Allergy> allergyList = resp.getData().getAllergy();
            if (allergyList==null || allergyList.size()==0){
                no.setChecked(true);
                yes.setChecked(false);

            }else{
                ck_allergy.setChecked(true);
                no.setChecked(false);
                yes.setChecked(true);
                for(int i =0; i<allergyList.size(); i++){
                    if (allergyList.get(i).getAllergyKey().matches("Food Allergy")){
                        ck_allergy_food.setChecked(true);
                        etfood.setText(allergyList.get(i).getValue());
                    }else if(allergyList.get(i).getAllergyKey().matches("Drug Allergy")){
                        ck_allergy_drug.setChecked(true);
                        etdrug.setText(allergyList.get(i).getValue());
                    }else{
                        ck_allergy_other.setChecked(true);
                        etother.setText(allergyList.get(i).getValue());
                    }
                }
            }

            // Setting diet status
            List<Diet> dietList = resp.getData().getDiet();
            if (dietList==null || dietList.size()==0){

            }else{
                for(int i =0; i<dietList.size(); i++){
                    if (dietList.get(i).getValue().matches(getString(R.string.diabetic_diet))){
                        ck_diabetic.setChecked(true);
                    }else if(dietList.get(i).getValue().matches(getString(R.string.keto_diet))){
                        ck_keto.setChecked(true);
                    }else if(dietList.get(i).getValue().matches(getString(R.string.vegan_diet))){
                        ck_vegan.setChecked(true);
                    }else if(dietList.get(i).getValue().matches(getString(R.string.vegetarian_diet))){
                        ck_vegetarian.setChecked(true);
                    }else if(dietList.get(i).getValue().matches(getString(R.string.raw_diet))){
                        ck_raw_food.setChecked(true);
                    }else if(dietList.get(i).getValue().matches(getString(R.string.law_salt_diet))){
                        ck_law_salt.setChecked(true);
                    }else{
                        ck_zone.setChecked(true);
                    }
                }
            }

            // Setting Activity Status
            List<Activity> activityList = resp.getData().getActivity();
            if (activityList==null || activityList.size()==0){

            }else{
                if (activityList.get(0).getSmoke().matches("yes")){
                    ck_smoke.setChecked(true);
                }
                if (activityList.get(0).getExercise().matches("yes")){
                    ck_exercise.setChecked(true);
                }
            }

            // Setting Employment
            List<Employment> employmentList = resp.getData().getEmployment();
            if (employmentList==null || employmentList.size()==0){

            }else{

                if (employmentList.get(0).getWorking().matches("yes")){
                    yesEmp.setChecked(true);
                    etDes.setText(employmentList.get(0).getDetail());
                }else{
                    noEmp.setChecked(true);
                    etDes.setVisibility(GONE);
                }
            }

            // Setting Medical History
            List<Medical> medicalList =   resp.getData().getMedical();
            List<Medical> conditionList = new ArrayList<>();
            List<Medical> surgeryList = new ArrayList<>();
            List<Medical> routineList = new ArrayList<>();
            List<Medical> traumaList = new ArrayList<>();
            List<Medical> comorbList = new ArrayList<>();
            List<Medical> mobilityList = new ArrayList<>();

            if (medicalList==null || medicalList.size()==0){

            }else{
                for (int i = 0; i<medicalList.size(); i++){
                    if (medicalList.get(i).getValue().matches(getString(R.string.medical_condition))){
                        ck_condition.setChecked(true);
                        layout_medical_condition.setVisibility(VISIBLE);
                        conditionList.add(medicalList.get(i));
                    }else if (medicalList.get(i).getValue().matches(getString(R.string.surgery))){
                        ck_surgery.setChecked(true);
                        layout_surgery.setVisibility(VISIBLE);
                        surgeryList.add(medicalList.get(i));
                    }else if (medicalList.get(i).getValue().matches(getString(R.string.medication_routine))){
                        ck_med_routine.setChecked(true);
                        layout_medical_routine.setVisibility(VISIBLE);
                        routineList.add(medicalList.get(i));
                    }else if (medicalList.get(i).getValue().matches(getString(R.string.trauma))){
                        ck_trauma.setChecked(true);
                        layout_trauma.setVisibility(VISIBLE);
                        traumaList.add(medicalList.get(i));
                    }else if (medicalList.get(i).getValue().equals(getString(R.string.comorbidities))){
                        ck_comorbidities.setChecked(true);
                        layout_comorbidities.setVisibility(VISIBLE);
                        comorbList.add(medicalList.get(i));
                    }else{
                        ck_condition_mobility.setChecked(true);
                        general_condition_mobility.setVisibility(VISIBLE);
                        mobilityList.add(medicalList.get(i));
                    }
                }
            }

            // Setting Adapters for each medical history type
            medicalConditionRv.setLayoutManager(new LinearLayoutManager(EditMedicalHistoryActivity.this));
            MedicalListAdapter conditionAdapter = new MedicalListAdapter(conditionList,EditMedicalHistoryActivity.this);
            medicalConditionRv.setAdapter(conditionAdapter);

            surgeryRv.setLayoutManager(new LinearLayoutManager(EditMedicalHistoryActivity.this));
            MedicalListAdapter surgeryAdapter = new MedicalListAdapter(surgeryList,EditMedicalHistoryActivity.this);
            surgeryRv.setAdapter(surgeryAdapter);

            medicalRoutineRv.setLayoutManager(new LinearLayoutManager(EditMedicalHistoryActivity.this));
            MedicalListAdapter routineAdapter = new MedicalListAdapter(routineList,EditMedicalHistoryActivity.this);
            medicalRoutineRv.setAdapter(routineAdapter);

            traumaRv.setLayoutManager(new LinearLayoutManager(EditMedicalHistoryActivity.this));
            MedicalListAdapter traumaAdapter = new MedicalListAdapter(traumaList,EditMedicalHistoryActivity.this);
            traumaRv.setAdapter(traumaAdapter);

            comorbiditiesRv.setLayoutManager(new LinearLayoutManager(EditMedicalHistoryActivity.this));
            MedicalListAdapter comorbAdapter = new MedicalListAdapter(comorbList,EditMedicalHistoryActivity.this);
            comorbiditiesRv.setAdapter(comorbAdapter);

            mobilityRv.setLayoutManager(new LinearLayoutManager(EditMedicalHistoryActivity.this));
            MedicalListAdapter mobilityAdapter = new MedicalListAdapter(mobilityList,EditMedicalHistoryActivity.this);
            mobilityRv.setAdapter(mobilityAdapter);

            if (conditionList == null || conditionList.size() == 0){
                medicalConditionRv.setVisibility(GONE);
            }else{
                medicalConditionRv.setVisibility(VISIBLE);
            }

            if (surgeryList == null || surgeryList.size() == 0){
                surgeryRv.setVisibility(GONE);
            }else{
                surgeryRv.setVisibility(VISIBLE);
            }

            if (routineList == null || routineList.size() == 0){
                medicalRoutineRv.setVisibility(GONE);
            }else{
                medicalRoutineRv.setVisibility(VISIBLE);
            }

            if (traumaList == null || traumaList.size() == 0){
                traumaRv.setVisibility(GONE);
            }else{
                traumaRv.setVisibility(VISIBLE);
            }

            if (comorbList == null || comorbList.size() == 0){
                comorbiditiesRv.setVisibility(GONE);
            }else{
                comorbiditiesRv.setVisibility(VISIBLE);
            }

            if (mobilityList == null || mobilityList.size() == 0){
                mobilityRv.setVisibility(GONE);
            }else{
                mobilityRv.setVisibility(VISIBLE);
            }

        }

        DropDownProfileAdapter bloodGroupAdapter = new DropDownProfileAdapter(this, Arrays.asList(bloodGroup));
        spinner.setAdapter(bloodGroupAdapter);

        // Default selection for Blood group
        if (bloodGrp != null) {
            for (int i=0;i<bloodGroup.length;i++){
                if (bloodGroup[i].equalsIgnoreCase(bloodGrp)){
                    spinner.setSelection(i);
                    break;
                }
            }
        }

        // Default value for BMI
        if (bmiVal != null) {
            bmiET.setText(bmiVal);
        }
        // Default value for HEIGHT
        if (height != null) {
            heightEt.setText(height);
        }
        // Default value for WEIGHT
        if (weight != null) {
            weightEt.setText(weight);
        }


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodGrp = bloodGroup[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    // open dialog
    private void openDialog(String msg, boolean finishActivity){

        AlertPopup mAlert = new AlertPopup(this);
        mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert.setMessage(msg);
        mAlert.setOkButton( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlert.dismiss();
                finish();
            }
        });
        mAlert.show();
    }


    // open success dialog
    private void openSuccessDialog(String msg, boolean finishActivity){

        SuccessPopup mAlert = new SuccessPopup(this);
        mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert.setMessage(msg);
        mAlert.setOkButton( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlert.dismiss();
            }
        });
        mAlert.show();
    }

    public void nextClick(View view) {
        if (!familyID.matches("0")){
            finish();
        }else{
            if(bloodGrp.equals(getString(R.string.select_blood_group))){
                Toast.makeText(this, R.string.bloodgrperror, Toast.LENGTH_SHORT).show();
                return;
            }
            if(heightEt.getText().toString().trim().equals("")){
                Toast.makeText(this, R.string.heighterror, Toast.LENGTH_SHORT).show();
                return;
            }
            if(weightEt.getText().toString().trim().equals("")){
                Toast.makeText(this, R.string.weighterror, Toast.LENGTH_SHORT).show();
                return;
            }

            callUpdateProfileInfoAPI();
        }

    }

    private void callUpdateProfileInfoAPI() {
        progress.show();
        UpdateProfileInfoResponseViewModel viewModel = ViewModelProviders.of(this).get(UpdateProfileInfoResponseViewModel.class);
        viewModel.init();
        viewModel.updateProfileInfo(token,bloodGrp,heightEt.getText().toString().trim(),weightEt.getText().toString().trim());
        viewModel.getVolumesResponseLiveData().observe(this, updateProfileInfoResponse -> {
            progress.hide();
            if(updateProfileInfoResponse != null){
                if (updateProfileInfoResponse.getSuccess()){
                    openSuccessDialog(updateProfileInfoResponse.getMessage(),true);
                }else {
                    openDialog(updateProfileInfoResponse.getMessage(), true);
                }
            }else{
                openDialog(this.getString(R.string.errormsg),false);
            }
        });
    }

    public void saveDiet(View view) {
//        if(!ck_ploeo.isChecked()){
//            stringArrayList.remove(ck_ploeo.getText().toString());
//        }
//        if(!ck_blood.isChecked()){
//            stringArrayList.remove(ck_blood.getText().toString());
//        }
//        if(!ck_vegon.isChecked()){
//            stringArrayList.remove(ck_vegon.getText().toString());
//        }
//        if(!ck_the_mediterranean_diet.isChecked()){
//            stringArrayList.remove(ck_the_mediterranean_diet.getText().toString());
//        }
//        if(!raw_food_diet.isChecked()){
//            stringArrayList.remove(raw_food_diet.getText().toString());
//        }

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
        viewModel.saveDiet(token,jsArray,familyID);
        viewModel.getVolumesResponseLiveData().observe(this, dietResponse -> {
            progress.hide();
            if(dietResponse != null){
                if (dietResponse.getSuccess()){
                    openSuccessDialog(dietResponse.getMessage(),false);
                }else{
                    openDialog(dietResponse.getMessage(),false);
                }
//                Toast.makeText(EditMedicalHistoryActivity.this, dietResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }else{
                openDialog(this.getString(R.string.errormsg),false);
//                Toast.makeText(EditMedicalHistoryActivity.this, getString(R.string.errormsg), Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.ck_condition :
                imageID = "0";
                if(isChecked){
                    layout_medical_condition.setVisibility(VISIBLE);
                }else{
                    layout_medical_condition.setVisibility(GONE);
                }
                break;
            case R.id.ck_surgery :
                imageID = "0";
                if(isChecked){
                    layout_surgery.setVisibility(VISIBLE);
                }else{
                    layout_surgery.setVisibility(GONE);
                }
                break;
            case R.id.ck_med_routine :
                imageID = "0";
                if(isChecked){
                    layout_medical_routine.setVisibility(VISIBLE);
                }else{
                    layout_medical_routine.setVisibility(GONE);
                }
                break;
            case R.id.ck_trauma :
                imageID = "0";
                if(isChecked){
                    layout_trauma.setVisibility(VISIBLE);
                }else{
                    layout_trauma.setVisibility(GONE);
                }
                break;
            case R.id.ck_comorbidities :
                imageID = "0";
                if(isChecked){
                    layout_comorbidities.setVisibility(VISIBLE);
                }else{
                    layout_comorbidities.setVisibility(GONE);
                }
                break;
            case R.id.ck_condition_mobility :
                imageID = "0";
                if(isChecked){
                    general_condition_mobility.setVisibility(VISIBLE);
                }else{
                    general_condition_mobility.setVisibility(GONE);
                }
                break;
            case R.id.ck_allergy :
                imageID = "0";
                if(isChecked){
                    layout_allergy.setVisibility(VISIBLE);
                }else{
                    layout_allergy.setVisibility(GONE);
                }
                break;
            case R.id.ck_allergy_food :
                imageID = "0";
                if(isChecked){
                    layoutfood.setVisibility(VISIBLE);
                }else{
                    layoutfood.setVisibility(GONE);
                }
                break;
            case R.id.ck_allergy_drug :
                imageID = "0";
                if(isChecked){
                    layoutdruglayout.setVisibility(VISIBLE);
                }else{
                    layoutdruglayout.setVisibility(GONE);
                }
                break;
            case R.id.ck_allergy_other :
                imageID = "0";
                if(isChecked){
                    layoutdotherlayout.setVisibility(VISIBLE);
                }else{
                    layoutdotherlayout.setVisibility(GONE);
                }
                break;
//            case R.id.ck_ploeo :
//                if(buttonView.isChecked()){
//                    stringArrayList.add(ck_ploeo.getText().toString());
//                }
//                break;
//            case R.id.ck_blood :
//                if(buttonView.isChecked()){
//                    stringArrayList.add(ck_blood.getText().toString());
//                }
//                break;
//            case R.id.ck_vegon :
//                if(buttonView.isChecked()){
//                    stringArrayList.add(ck_vegon.getText().toString());
//                }
//                break;
//            case R.id.ck_the_mediterranean_diet :
//                if(buttonView.isChecked()){
//                    stringArrayList.add(ck_the_mediterranean_diet.getText().toString());
//                }
//                break;
//            case R.id.ck_raw_food_diet :
//                if(buttonView.isChecked()){
//                    stringArrayList.add(raw_food_diet.getText().toString());
//                }
//                break;
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
        activityViewModel.saveActivity(token,smoke,exercise,familyID);
        activityViewModel.getVolumesResponseLiveData().observe(this, activityResponse -> {
            progress.hide();
            if(activityResponse != null){
                if (activityResponse.getSuccess()){
                    openSuccessDialog(activityResponse.getMessage(),false);
                }else {
                    openDialog(activityResponse.getMessage(), false);
                }
//                Toast.makeText(this, activityResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }else {
                openDialog(this.getString(R.string.errormsg),false);
//                Toast.makeText(this, getString(R.string.errormsg), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void saveEmployee(View view) {
        String description = etDes.getText().toString().trim();
//        description = "";

        if(working.matches("yes") && description.isEmpty()){
            Toast.makeText(this, R.string.deserror, Toast.LENGTH_SHORT).show();
            return;
        }else if(working.matches("no")){
            description = "na";
        }

        progress.show();

        EmpViewModel viewModel = ViewModelProviders.of(this).get(EmpViewModel.class);
        viewModel.init();
        viewModel.saveEmp(token,working,description,familyID);
        viewModel.getVolumesResponseLiveData().observe(this, employmentResponse -> {
            progress.hide();
            if (employmentResponse != null){
                if (employmentResponse.getSuccess()){
                    openSuccessDialog(employmentResponse.getMessage(),false);
                }else {
                    openDialog(employmentResponse.getMessage(), false);
                }
//                Toast.makeText(this, employmentResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }else{
                openDialog(this.getString(R.string.errormsg),false);
//                Toast.makeText(this, R.string.errormsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // check run time permission
    private void checkRunTimePermission() {

        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                selectImage(EditMedicalHistoryActivity.this);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }


    private void selectImage(Context context) {
        final CharSequence[] options = getResources().getStringArray(R.array.cameralist);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.choosedoc);

        builder.setItems(options, (dialog, item) -> {

            if (options[item].equals(getString(R.string.take_photo))) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);

            } else if (options[item].equals(getString(R.string.choose_gallery))) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);

            } else if (options[item].equals(getString(R.string.cancel))) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        file =  Utility.getFile(this,selectedImage);
                        openImageDialog(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            file =  Utility.getFile(this,bitmap);
                            openImageDialog(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    break;
            }
        }
    }

    // Method to show clicked/selected image
    private void openImageDialog(Bitmap bitmap){
        // uploadLayout = Layout on which user clicks to upload image
        // selectionLayout = Layout which replaces the uploadLayout to show image selected message
        //btnView = Save Button for uploadLayout to save Image

        RelativeLayout uploadLayout,selectionLayout;
        TextView btnView;

        switch (selectionFlag){
            case "medical-condition":
                uploadLayout = layoutMedicalCondition;
                selectionLayout = uplayoutMedicalCondition;
                btnView = btnmcondition;
                break;
            case "surgery":
                uploadLayout = layoutsurgery;
                selectionLayout = uplayoutsurgery;
                btnView = btnsurgenrycondition;
                break;
            case "medical-routine":
                uploadLayout = layoutmedicalroutine;
                selectionLayout = uplayoutmedroutine;
                btnView = btnmedicalroutine;
                break;
            case "trauma":
                uploadLayout = layouttrauma;
                selectionLayout = uplayouttrauma;
                btnView = btnTrauma;
                break;
            case "comorbidities":
                uploadLayout = layoutcomorbidities;
                selectionLayout = uplayoutcomorbidities;
                btnView = btncomorbidities;
                break;
            case "condition-mobility":
                uploadLayout = layoutconditionmobility;
                selectionLayout = uplayoutconditionmobility;
                btnView = btngenralcondition;
                break;
            default:
                Toast.makeText(this, getString(R.string.invalidselection), Toast.LENGTH_SHORT).show();
                return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uploadLayout.setVisibility(GONE);
                selectionLayout.setVisibility(VISIBLE);
                btnView.setText(R.string.save_btn);
            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                file=null;
            }
        });
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.custom_image_dialog, null);
        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                ImageView image = (ImageView) dialog.findViewById(R.id.imageView);
                float imageWidthInPX = (float)image.getWidth();

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(imageWidthInPX),
                        Math.round(imageWidthInPX * (float)bitmap.getHeight() / (float)bitmap.getWidth()));
                image.setLayoutParams(layoutParams);
                image.setImageBitmap(bitmap);
            }
        });

        dialog.show();
    }

    public void addMedicalCondition(View view) {
        progress.show();
        String his = etMedicalCondition.getText().toString().trim();
        if(his.isEmpty()){
            progress.hide();
            Toast.makeText(this, getString(R.string.fielderror), Toast.LENGTH_SHORT).show();
            return;
        }else if(file == null){
            progress.hide();
            Toast.makeText(this,  getString(R.string.doceuperror), Toast.LENGTH_SHORT).show();
            return;
        }
        // convert field into Request body and image convert into multipart
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("document", file.getName(), fileBody);
        RequestBody medicalKey =  RequestBody.create(MediaType.parse("text/plain"), his);
        RequestBody idKey =  RequestBody.create(MediaType.parse("text/plain"),imageID);
        RequestBody familyKey =  RequestBody.create(MediaType.parse("text/plain"),familyID);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), ck_condition.getText().toString());
        MedicalHistoryViewModel viewModel = ViewModelProviders.of(this).get(MedicalHistoryViewModel.class);
        viewModel.init();
        viewModel.saveMedicalHistoryAddress(token,medicalKey,description,body,idKey,familyKey);
        viewModel.getVolumesResponseLiveData().observe(this, medicalHistoryResponse -> {
            progress.hide();
            if(medicalHistoryResponse != null){
                if(medicalHistoryResponse.getSuccess()){
                    imageID = medicalHistoryResponse.getData().getId();

                    // Changing the views back to upload more images, if any
                    uplayoutMedicalCondition.setVisibility(GONE);
                    layoutMedicalCondition.setVisibility(VISIBLE);
                    btnmcondition.setText(R.string.add_new_btn);

                    openSuccessDialog(medicalHistoryResponse.getMessage(),false);
                }else {
                    openDialog(medicalHistoryResponse.getMessage(), false);
                }
            }else{
                openDialog(this.getString(R.string.errormsg),false);
            }

            // Setting file as null so that if someone clicks on save for next view, then same image isn't uploaded
            file = null;
        });
    }

    public void addSurgery(View view) {
        progress.show();
        String his = etsurgery.getText().toString().trim();
        if(his.isEmpty()){
            progress.hide();
            Toast.makeText(this, getString(R.string.fielderror), Toast.LENGTH_SHORT).show();
            return;
        }else if(file == null){
            progress.hide();
            Toast.makeText(this,  getString(R.string.doceuperror), Toast.LENGTH_SHORT).show();
            return;
        }
        // convert field into Request body and image convert into multipart
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("document", file.getName(), fileBody);
        RequestBody medicalKey =  RequestBody.create(MediaType.parse("text/plain"), his);
        RequestBody idKey =  RequestBody.create(MediaType.parse("text/plain"),imageID);
        RequestBody familyKey =  RequestBody.create(MediaType.parse("text/plain"),familyID);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), ck_surgery.getText().toString());
        MedicalHistoryViewModel viewModel = ViewModelProviders.of(this).get(MedicalHistoryViewModel.class);
        viewModel.init();
        viewModel.saveMedicalHistoryAddress(token,medicalKey,description,body,idKey,familyKey);
        viewModel.getVolumesResponseLiveData().observe(this, medicalHistoryResponse -> {
            progress.hide();
            if(medicalHistoryResponse != null){
                if(medicalHistoryResponse.getSuccess()){
                    imageID = medicalHistoryResponse.getData().getId();

                    // Changing the views back to upload more images, if any
                    uplayoutsurgery.setVisibility(GONE);
                    layoutsurgery.setVisibility(VISIBLE);
                    btnsurgenrycondition.setText(R.string.add_new_btn);

                    openSuccessDialog(medicalHistoryResponse.getMessage(),false);
                }else {
                    openDialog(medicalHistoryResponse.getMessage(), false);
                }
            }else{
                openDialog(this.getString(R.string.errormsg),false);
            }

            // Setting file as null so that if someone clicks on save for next view, then same image isn't uploaded
            file = null;
        });

    }

    public void addMedicalRoutine(View view) {
        progress.show();
        String his = etmedicalroutine.getText().toString().trim();
        if(his.isEmpty()){
            progress.hide();
            Toast.makeText(this, getString(R.string.fielderror), Toast.LENGTH_SHORT).show();
            return;
        }else if(file == null){
            progress.hide();
            Toast.makeText(this,  getString(R.string.doceuperror), Toast.LENGTH_SHORT).show();
            return;
        }
        // convert field into Request body and image convert into multipart
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("document", file.getName(), fileBody);
        RequestBody medicalKey =  RequestBody.create(MediaType.parse("text/plain"), his);
        RequestBody idKey =  RequestBody.create(MediaType.parse("text/plain"),imageID);
        RequestBody familyKey =  RequestBody.create(MediaType.parse("text/plain"),familyID);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), ck_med_routine.getText().toString());
        MedicalHistoryViewModel viewModel = ViewModelProviders.of(this).get(MedicalHistoryViewModel.class);
        viewModel.init();
        viewModel.saveMedicalHistoryAddress(token,medicalKey,description,body,idKey,familyKey);
        viewModel.getVolumesResponseLiveData().observe(this, medicalHistoryResponse -> {
            progress.hide();
            if(medicalHistoryResponse != null){
                if(medicalHistoryResponse.getSuccess()){
                    imageID = medicalHistoryResponse.getData().getId();

                    // Changing the views back to upload more images, if any
                    uplayoutmedroutine.setVisibility(GONE);
                    layoutmedicalroutine.setVisibility(VISIBLE);
                    btnmedicalroutine.setText(R.string.add_new_btn);

                    openSuccessDialog(medicalHistoryResponse.getMessage(),false);
                }else {
                    openDialog(medicalHistoryResponse.getMessage(), false);
                }
            }else{
                openDialog(this.getString(R.string.errormsg),false);
            }

            // Setting file as null so that if someone clicks on save for next view, then same image isn't uploaded
            file = null;
        });

    }

    public void addTrauma(View view) {
        progress.show();
        String his = ettrauma.getText().toString().trim();
        if(his.isEmpty()){
            progress.hide();
            Toast.makeText(this, getString(R.string.fielderror), Toast.LENGTH_SHORT).show();
            return;
        }else if(file == null){
            progress.hide();
            Toast.makeText(this,  getString(R.string.doceuperror), Toast.LENGTH_SHORT).show();
            return;
        }
        // convert field into Request body and image convert into multipart
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("document", file.getName(), fileBody);
        RequestBody medicalKey =  RequestBody.create(MediaType.parse("text/plain"), his);
        RequestBody idKey =  RequestBody.create(MediaType.parse("text/plain"),imageID);
        RequestBody familyKey =  RequestBody.create(MediaType.parse("text/plain"),familyID);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), ck_trauma.getText().toString());
        MedicalHistoryViewModel viewModel = ViewModelProviders.of(this).get(MedicalHistoryViewModel.class);
        viewModel.init();
        viewModel.saveMedicalHistoryAddress(token,medicalKey,description,body,idKey,familyKey);
        viewModel.getVolumesResponseLiveData().observe(this, medicalHistoryResponse -> {
            progress.hide();
            if(medicalHistoryResponse != null){
                if(medicalHistoryResponse.getSuccess()){
                    imageID = medicalHistoryResponse.getData().getId();

                    // Changing the views back to upload more images, if any
                    uplayouttrauma.setVisibility(GONE);
                    layouttrauma.setVisibility(VISIBLE);
                    btnTrauma.setText(R.string.add_new_btn);

                    openSuccessDialog(medicalHistoryResponse.getMessage(),false);
                }else {
                    openDialog(medicalHistoryResponse.getMessage(), false);
                }
            }else{
                openDialog(this.getString(R.string.errormsg),false);
            }

            // Setting file as null so that if someone clicks on save for next view, then same image isn't uploaded
            file = null;
        });
    }

    public void addComorbidities(View view) {
        progress.show();
        String his = etcomorbidities.getText().toString().trim();
        if(his.isEmpty()){
            progress.hide();
            Toast.makeText(this, getString(R.string.fielderror), Toast.LENGTH_SHORT).show();
            return;
        }else if(file == null){
            progress.hide();
            Toast.makeText(this,  getString(R.string.doceuperror), Toast.LENGTH_SHORT).show();
            return;
        }
        // convert field into Request body and image convert into multipart
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("document", file.getName(), fileBody);
        RequestBody medicalKey =  RequestBody.create(MediaType.parse("text/plain"), his);
        RequestBody idKey =  RequestBody.create(MediaType.parse("text/plain"),imageID);
        RequestBody familyKey =  RequestBody.create(MediaType.parse("text/plain"),familyID);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), ck_comorbidities.getText().toString());
        MedicalHistoryViewModel viewModel = ViewModelProviders.of(this).get(MedicalHistoryViewModel.class);
        viewModel.init();
        viewModel.saveMedicalHistoryAddress(token,medicalKey,description,body,idKey,familyKey);
        viewModel.getVolumesResponseLiveData().observe(this, medicalHistoryResponse -> {
            progress.hide();
            if(medicalHistoryResponse != null){
                if(medicalHistoryResponse.getSuccess()){
                    imageID = medicalHistoryResponse.getData().getId();

                    // Changing the views back to upload more images, if any
                    uplayoutcomorbidities.setVisibility(GONE);
                    layoutcomorbidities.setVisibility(VISIBLE);
                    btncomorbidities.setText(R.string.add_new_btn);

                    openSuccessDialog(medicalHistoryResponse.getMessage(),false);
                }else {
                    openDialog(medicalHistoryResponse.getMessage(), false);
                }
            }else{
                openDialog(this.getString(R.string.errormsg),false);
            }

            // Setting file as null so that if someone clicks on save for next view, then same image isn't uploaded
            file = null;
        });
    }

    public void addConditionMobility(View view) {
        progress.show();
        String his = etmobility.getText().toString().trim();
        if(his.isEmpty()){
            progress.hide();
            Toast.makeText(this, getString(R.string.fielderror), Toast.LENGTH_SHORT).show();
            return;
        }else if(file == null){
            progress.hide();
            Toast.makeText(this,  getString(R.string.doceuperror), Toast.LENGTH_SHORT).show();
            return;
        }
        // convert field into Request body and image convert into multipart
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("document", file.getName(), fileBody);
        RequestBody medicalKey =  RequestBody.create(MediaType.parse("text/plain"), his);
        RequestBody idKey =  RequestBody.create(MediaType.parse("text/plain"),imageID);
        RequestBody familyKey =  RequestBody.create(MediaType.parse("text/plain"),familyID);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), ck_condition_mobility.getText().toString());
        MedicalHistoryViewModel viewModel = ViewModelProviders.of(this).get(MedicalHistoryViewModel.class);
        viewModel.init();
        viewModel.saveMedicalHistoryAddress(token,medicalKey,description,body,idKey,familyKey);
        viewModel.getVolumesResponseLiveData().observe(this, medicalHistoryResponse -> {
            progress.hide();
            if(medicalHistoryResponse != null){
                if(medicalHistoryResponse.getSuccess()){
                    imageID = medicalHistoryResponse.getData().getId();

                    // Changing the views back to upload more images, if any
                    uplayoutconditionmobility.setVisibility(GONE);
                    layoutconditionmobility.setVisibility(VISIBLE);
                    btngenralcondition.setText(R.string.add_new_btn);

                    openSuccessDialog(medicalHistoryResponse.getMessage(),false);
                }else {
                    openDialog(medicalHistoryResponse.getMessage(), false);
                }
            }else{
                openDialog(this.getString(R.string.errormsg),false);
            }

            // Setting file as null so that if someone clicks on save for next view, then same image isn't uploaded
            file = null;
        });
    }

    public void addDrugAllergy(View view) {

        String drugDescription =  etdrug.getText().toString().trim();
        if ((drugDescription.isEmpty())){
            Toast.makeText(this,  getString(R.string.deserror), Toast.LENGTH_SHORT).show();
            return;
        }

        progress.show();

        AllergyViewModel viewModel = ViewModelProviders.of(this).get(AllergyViewModel.class);
        viewModel.init();
        viewModel.saveAllergy(token,ck_allergy_drug.getText().toString().trim(),drugDescription,familyID);
        viewModel.getVolumesResponseLiveData().observe(this, allergyResponse -> {
            progress.hide();
            if(allergyResponse!=null){
                if (allergyResponse.getSuccess()){
                    openSuccessDialog(allergyResponse.getMessage(),false);
                }else {
                    openDialog(allergyResponse.getMessage(), false);
                }
            }else{
                openDialog(this.getString(R.string.errormsg),false);
            }
        });
    }

    public void addOtherAllergy(View view) {
        String drugDescription = etother.getText().toString().trim();
        if ((drugDescription.isEmpty())){
            Toast.makeText(this,  getString(R.string.deserror), Toast.LENGTH_SHORT).show();
            return;
        }

        progress.show();

        AllergyViewModel viewModel = ViewModelProviders.of(this).get(AllergyViewModel.class);
        viewModel.init();
        viewModel.saveAllergy(token,ck_allergy_other.getText().toString().trim(),drugDescription,familyID);
        viewModel.getVolumesResponseLiveData().observe(this, allergyResponse -> {
            progress.hide();
            if(allergyResponse!=null){
                if (allergyResponse.getSuccess()){
                    openSuccessDialog(allergyResponse.getMessage(),false);
                }else{
                    openDialog(allergyResponse.getMessage(),false);
                }
            }else{
                openDialog(this.getString(R.string.errormsg),false);
            }
        });
    }

    public void addFoodAllergy(View view) {
        String drugDescription = etfood.getText().toString().trim();
        if ((drugDescription.isEmpty())){
            Toast.makeText(this,  getString(R.string.deserror), Toast.LENGTH_SHORT).show();
            return;
        }

        progress.show();

        AllergyViewModel viewModel = ViewModelProviders.of(this).get(AllergyViewModel.class);
        viewModel.init();
        viewModel.saveAllergy(token,ck_allergy_food.getText().toString().trim(),drugDescription,familyID);
        viewModel.getVolumesResponseLiveData().observe(this, allergyResponse -> {
            progress.hide();
            if(allergyResponse!=null){
                if (allergyResponse.getSuccess()){
                    openSuccessDialog(allergyResponse.getMessage(),false);
                }else {
                    openDialog(allergyResponse.getMessage(), false);
                }
            }else{
                openDialog(this.getString(R.string.errormsg),false);
            }
        });

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.radioButton1 :
                layout_action.setVisibility(VISIBLE);
                break;
            case R.id.radioButton2:
                layout_action.setVisibility(GONE);

                break;
            case R.id.radioButton1Emp :
                working = "yes";
                etDes.setVisibility(VISIBLE);
                break;
            case R.id.radioButton2Emp:
                working = "no";
                etDes.setVisibility(GONE);
                break;
        }
    }

    public void backPressed(View view) {
        finish();
    }
}