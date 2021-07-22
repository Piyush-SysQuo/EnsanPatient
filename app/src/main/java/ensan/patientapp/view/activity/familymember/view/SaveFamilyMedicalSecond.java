package ensan.patientapp.view.activity.familymember.view;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.view.activity.savemedicalHistory.viewmodel.ActivityViewModel;
import ensan.patientapp.view.activity.savemedicalHistory.viewmodel.DietViewModel;
import ensan.patientapp.view.activity.savemedicalHistory.viewmodel.EmpViewModel;

public class SaveFamilyMedicalSecond extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    String token;
    JsonArray dietArray = new JsonArray();
    ArrayList<String> stringArrayList = new ArrayList<>();
    JSONArray jsonArray = new JSONArray();

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

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    @BindView(R.id.radioButton1)
    RadioButton yes;

    @BindView(R.id.radioButton2)
    RadioButton no;

    @BindView(R.id.etDes)
    EditText etDes;

    @BindView(R.id.txt_back)
    TextView txt_back;



    private Progress progress;
    private String working = "";
    private String familyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_medical_hostory_secand);

        //
        ButterKnife.bind(this);

        // get data from intent
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                token = (String) bundle.get(Constants.KEY_TOKEN);
                familyId = (String) bundle.get(Constants.KEY_FAMILY_MEMBER_ID);
            }
        }

        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);

//        ck_ploeo.setOnCheckedChangeListener(this);
//        ck_blood.setOnCheckedChangeListener(this);
//        ck_vegon.setOnCheckedChangeListener(this);
//        ck_the_mediterranean_diet.setOnCheckedChangeListener(this);
//        raw_food_diet.setOnCheckedChangeListener(this);

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
//        txt_back.setVisibility(View.GONE);
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

    public void nextClick(View view) {
        /*Intent intent = new Intent(new Intent(this, CompleteProfileThirdActivity.class));
        intent.putExtra(Constants.KEY_TOKEN,token);
        startActivity(intent);*/
        finish();
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
        viewModel.saveDiet(token,jsArray,familyId);
        viewModel.getVolumesResponseLiveData().observe(this, dietResponse -> {
            progress.hide();
            if(dietResponse != null){
                openDialog(dietResponse.getMessage());
//                Toast.makeText(SaveFamilyMedicalSecond.this, dietResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }else{
                openDialog(this.getString(R.string.errormsg));
//                Toast.makeText(SaveFamilyMedicalSecond.this, getString(R.string.errormsg), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()){
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
        activityViewModel.saveActivity(token,smoke,exercise,familyId);
        activityViewModel.getVolumesResponseLiveData().observe(this, activityResponse -> {
            progress.hide();
            if(activityResponse != null){
                openDialog(activityResponse.getMessage());
//                Toast.makeText(this, activityResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }else {
                openDialog(this.getString(R.string.errormsg));
//                Toast.makeText(this, getString(R.string.errormsg), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void saveEmployee(View view) {
        String description = etDes.getText().toString().trim();

        if(description.isEmpty()){
            Toast.makeText(this, R.string.deserror, Toast.LENGTH_SHORT).show();
            return;
        }

        progress.show();

        EmpViewModel viewModel = ViewModelProviders.of(this).get(EmpViewModel.class);
        viewModel.init();
        viewModel.saveEmp(token,working,description,familyId);
        viewModel.getVolumesResponseLiveData().observe(this, employmentResponse -> {
            progress.hide();
            if (employmentResponse != null){
                openDialog(employmentResponse.getMessage());
//                Toast.makeText(this, employmentResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }else{
                openDialog(this.getString(R.string.errormsg));
//                Toast.makeText(this, R.string.errormsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.radioButton1 :
                working = "yes";
                break;
            case R.id.radioButton2:
                working = "no";
                break;
        }
    }
}