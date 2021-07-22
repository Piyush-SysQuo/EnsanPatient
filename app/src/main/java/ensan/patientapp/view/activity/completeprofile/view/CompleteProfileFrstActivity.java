package ensan.patientapp.view.activity.completeprofile.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.GpsUtils;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.view.activity.completeprofile.adapter.DropDownProfileAdapter;
import ensan.patientapp.view.activity.completeprofile.viewmodel.CompleteFirstProfileViewModel;


public class CompleteProfileFrstActivity extends AppCompatActivity implements GpsUtils.onGpsListener {


    @BindView(R.id.manLayout)
    RelativeLayout manLayout;

    @BindView(R.id.womenLayout)
    RelativeLayout womenLayout;


    @BindView(R.id.spinner)
    Spinner spinner;

    @BindView(R.id.spinnerStatus)
    Spinner spinnerStatus;


    @BindView(R.id.maleIv)
    ImageView maleIv;

    @BindView(R.id.femaleIv)
    ImageView femaleIv;

    String[] country = new String[]{};
    String[] marritalStatus = new String[]{};
    private String token;
    private String gender;
    private Progress progress;
    private String nationality;
    private String maritalStatus;
    private String language;
    private String maritalStatusCode = "0";

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

        setContentView(R.layout.complete_profile_frst_activity);

        // bind data to butter_knife
        ButterKnife.bind(this);

        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



//        country = getResources().getStringArray(R.array.natioanlity);
        country = getResources().getStringArray(R.array.countries_array);
        marritalStatus = getResources().getStringArray(R.array.maritalstatus);
        new GpsUtils(this).turnGPSOn(this);


        DropDownProfileAdapter aa = new DropDownProfileAdapter(this, Arrays.asList(country));
        spinner.setAdapter(aa);


        DropDownProfileAdapter statusAdapter = new DropDownProfileAdapter(this,Arrays.asList(marritalStatus));
        spinnerStatus.setAdapter(statusAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nationality = country[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maritalStatus = marritalStatus[position];
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
    public void womenClick(View view) {
        gender = "2";
        womenLayout.setBackground(getResources().getDrawable(R.drawable.blue_strok_rect));
        manLayout.setBackground(getResources().getDrawable(R.drawable.white_rect));
        maleIv.setVisibility(View.GONE);
        femaleIv.setVisibility(View.VISIBLE);
        marritalStatus = getResources().getStringArray(R.array.maritalstatus);
        DropDownProfileAdapter statusAdapter = new DropDownProfileAdapter(this,Arrays.asList(marritalStatus));
        spinnerStatus.setAdapter(statusAdapter);
    }

    public void maleClick(View view) {
        gender = "1";
        womenLayout.setBackground(getResources().getDrawable(R.drawable.white_rect));
        manLayout.setBackground(getResources().getDrawable(R.drawable.blue_strok_rect));
        maleIv.setVisibility(View.VISIBLE);
        femaleIv.setVisibility(View.GONE);
        marritalStatus = getResources().getStringArray(R.array.maritalstatusmale);
        DropDownProfileAdapter statusAdapter = new DropDownProfileAdapter(this,Arrays.asList(marritalStatus));
        spinnerStatus.setAdapter(statusAdapter);
    }


    public void backPressed(View view) {
        finish();
    }

    public void nextClick(View view) {
          enableRunTimePermission();
    }

    @Override
    public void gpsStatus(boolean isGPSEnable) {

    }

    // enable run time permission
    private void enableRunTimePermission(){
        Dexter.withContext(this).withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (maritalStatus.matches(getString(R.string.singleMale)) || maritalStatus.matches(getString(R.string.singleFeMale))){
                            maritalStatusCode = "0";
                        }else if (maritalStatus.matches(getString(R.string.married))){
                            maritalStatusCode = "1";
                        }else if (maritalStatus.matches(getString(R.string.divorced))){
                            maritalStatusCode = "2";
                        }else if (maritalStatus.matches(getString(R.string.widowed))){
                            maritalStatusCode = "3";
                        }else if (maritalStatus.matches(getString(R.string.separated))){
                            maritalStatusCode = "4";
                        }else{
                            maritalStatusCode = "0";
                        }
                        if(gender == null){
                            Toast.makeText(CompleteProfileFrstActivity.this, getString(R.string.gendererror), Toast.LENGTH_SHORT).show();
                            return;
                        }else if(nationality.equals(getString(R.string.nationality))){
                            Toast.makeText(CompleteProfileFrstActivity.this, getString(R.string.natinalityerror), Toast.LENGTH_SHORT).show();
                            return;
                        }else if (maritalStatus.equals(getString(R.string.marital_status))){
                            Toast.makeText(CompleteProfileFrstActivity.this, getString(R.string.maritalerror), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        progress.show();

                        CompleteFirstProfileViewModel viewModel = ViewModelProviders.of(CompleteProfileFrstActivity.this).get(CompleteFirstProfileViewModel.class);
                        viewModel.init();
                        viewModel.completeFirstProfile(token,gender,nationality,maritalStatusCode);
                        viewModel.getVolumesResponseLiveData().observe(CompleteProfileFrstActivity.this, profileResponse -> {

                            progress.hide();

                            if(profileResponse!=null){
                                boolean status = profileResponse.getSuccess();
                                if(status){
                                    Intent intent = new Intent(CompleteProfileFrstActivity.this, CompleteProfileNewSecondActivity.class);
                                    intent.putExtra(Constants.KEY_TOKEN,token);
                                    intent.putExtra(Constants.KEY_LANGUAGE,language);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    openDialog(profileResponse.getMessage());
                                }
                            }else{
                                openDialog(getString(R.string.errormsg));
                            }
                        });

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

}