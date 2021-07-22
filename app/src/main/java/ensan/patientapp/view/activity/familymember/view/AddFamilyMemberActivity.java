package ensan.patientapp.view.activity.familymember.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Objects;

import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.SuccessPopup;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.view.activity.familymember.adapter.DropDownFamilyAdapter;
import ensan.patientapp.view.activity.familymember.viewmodel.FamilyMemberViewModel;

public class AddFamilyMemberActivity extends AppCompatActivity {

    private String token;
    private Spinner relationSpinner;
    private String[] familyMembers = new String[]{};
    private EditText nameET;
    private EditText phoneET;
    private Progress progress;
    private String nameOfPatient;
    private String phoneNumber;
    private String relation;
    private String type;
    private String language;

    private RelativeLayout adultLayout,minorLayout;

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

        setContentView(R.layout.activity_add_family_member);



        familyMembers = getResources().getStringArray(R.array.relations);

        // Finding the views
        adultLayout = findViewById(R.id.adultLayout);
        minorLayout = findViewById(R.id.minorLayout);
        nameET = findViewById(R.id.nameET);
        phoneET = findViewById(R.id.phoneNum);
        relationSpinner = findViewById(R.id.relationSpinner);

        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);

        // add family member to ArrayAdapter
        DropDownFamilyAdapter stringArrayAdapter = new DropDownFamilyAdapter(this, Arrays.asList(familyMembers));
        relationSpinner.setAdapter(stringArrayAdapter);

        // Relation spinner on click handler
        relationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                relation = familyMembers[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
    public void backPressed(View view) {
        finish();
    }

    public void continueClick(View view) {
        nameOfPatient = nameET.getText().toString().trim();
        phoneNumber = phoneET.getText().toString().trim();

        if(nameOfPatient.isEmpty())
        {
            Toast.makeText(this, R.string.nameRequired, Toast.LENGTH_SHORT).show();
            return;
        }
        /*else if(phoneNumber.isEmpty())
        {
            Toast.makeText(this, R.string.phoneRequired, Toast.LENGTH_SHORT).show();
            return;
        }*/
        else if(relation.equals(getString(R.string.chooserelationship)))
        {
            Toast.makeText(this, R.string.relationshiperror, Toast.LENGTH_SHORT).show();
            return;
        }
        else if(type==null)
        {
            Toast.makeText(this, R.string.typeerroe, Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            addFamilyMemberApi();
        }
    }

    // add family member API
    public void addFamilyMemberApi() {
        progress.show();
        FamilyMemberViewModel viewModel = ViewModelProviders.of(this).get(FamilyMemberViewModel.class);
        viewModel.init();
        viewModel.saveFamilyMember(token,nameOfPatient,relation,phoneNumber,type);
        viewModel.getVolumesResponseLiveData().observe(this, familyMemberResponse -> {
            progress.hide();
            if(familyMemberResponse != null){
                if(familyMemberResponse.getSuccess()){
                    openSuccessDialog(familyMemberResponse.getMessage());
//                    Toast.makeText(this, familyMemberResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    openDialog(familyMemberResponse.getMessage());
                }

            }else{
                openDialog(this.getString(R.string.errormsg));
            }
        });
    }

    public void adultClick(View view) {
        type = getString(R.string.adult);
        adultLayout.setBackground(getResources().getDrawable(R.drawable.blue_strok_rect));
        minorLayout.setBackground(getResources().getDrawable(R.drawable.white_rect));
    }

    public void minorClick(View view) {
        type = getString(R.string.minor);
        adultLayout.setBackground(getResources().getDrawable(R.drawable.white_rect));
        minorLayout.setBackground(getResources().getDrawable(R.drawable.blue_strok_rect));
    }
}