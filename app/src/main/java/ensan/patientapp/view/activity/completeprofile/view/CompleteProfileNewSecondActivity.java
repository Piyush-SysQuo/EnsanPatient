package ensan.patientapp.view.activity.completeprofile.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Arrays;
import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;
import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.view.activity.completeprofile.adapter.DropDownProfileAdapter;
import ensan.patientapp.view.activity.completeprofile.viewmodel.CompleteSecondProfileViewModel;


public class CompleteProfileNewSecondActivity extends AppCompatActivity {

    String[] bloodGroup = new String[]{};

    @BindView(R.id.spinner)
    Spinner spinner;

    @BindView(R.id.noofLayout)
    TextInputLayout noofLayout;

    @BindView(R.id.childEt)
    TextInputEditText childEt;

    @BindView(R.id.heightLayout)
    TextInputLayout heightLayout;

    @BindView(R.id.heightEt)
    TextInputEditText heightEt;

    @BindView(R.id.weightLayout)
    TextInputLayout weightLayout;

    @BindView(R.id.weightEt)
    TextInputEditText weightEt;

    @BindView(R.id.emobileLayout)
    TextInputLayout emobileLayout;

    @BindView(R.id.emobileEt)
    TextInputEditText emobileEt;

    private String token;
    private Progress progress;
    private String bloodGrp;
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

        setContentView( R.layout.complete_profile_new_second_activity);

        // bind butter knife
        ButterKnife.bind(this);


        bloodGroup = getResources().getStringArray(R.array.blood_group);

        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);


        DropDownProfileAdapter bloodGroupAdapter = new DropDownProfileAdapter(this, Arrays.asList(bloodGroup));
        spinner.setAdapter(bloodGroupAdapter);


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
    private void openDialog(String msg){

        AlertPopup mAlert = new AlertPopup(this);
        mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert.setMessage(msg);
        mAlert.setOkButton(view -> {
            mAlert.dismiss();
        });
        mAlert.show();
    }
    public void backPressed(View view) {
        finish();
    }

    public void nextClick(View view) {
        String noOfChild = childEt.getText().toString().trim();
        String height = heightEt.getText().toString().trim();
        String weight = weightEt.getText().toString().trim();
        String emergencyNum = emobileEt.getText().toString().trim();

        if(noOfChild.equals(""))
            noOfChild = "0";

        if(height.isEmpty()){
            heightLayout.setError(getString(R.string.heighterror));
            return;
        }else if(weight.isEmpty()){
            weightLayout.setError(getString(R.string.weighterror));
            return;
        }else if(bloodGrp.equals(getString(R.string.select_blood_group))){
            Toast.makeText(this, R.string.bloodgrperror, Toast.LENGTH_SHORT).show();
            return;
        }else if(emergencyNum.isEmpty()){
            emobileLayout.setError(getString(R.string.emergencynoerror));
            return;
        }else if(emergencyNum.startsWith("5") ){

            // show progress dialog
            progress.show();

            CompleteSecondProfileViewModel viewModel = ViewModelProviders.of(this).get(CompleteSecondProfileViewModel.class);
            viewModel.init();
            viewModel.completeSecondProfile(token,noOfChild,height,weight,bloodGrp,emergencyNum,"0");
            viewModel.getVolumesResponseLiveData().observe(this, profileResponse -> {

                // hide progress dialog
                progress.hide();

                if(profileResponse != null){
                    if(profileResponse.getSuccess()){
                        Intent intent = new Intent(getApplicationContext(), CompleteProfileSecondActivity.class);
                        intent.putExtra(Constants.KEY_TOKEN,token);
                        intent.putExtra(Constants.KEY_LANGUAGE,language);
                        startActivity(intent);
                        finish();
                    }else{
                        openDialog(profileResponse.getMessage());
                    }
                }else{
                    openDialog(this.getString(R.string.errormsg));
                }
            });
        }else {
            openDialog(getString(R.string.invalid_emergency_number));

        }
    }
}