package ensan.patientapp.view.activity.doctorvisit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.google.gson.reflect.TypeToken;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.Util;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.view.activity.choosedatetime.view.ChooseDateTimeActivity;
import ensan.patientapp.R;
import ensan.patientapp.databinding.DoctorVisitActivityBinding;
import ensan.patientapp.view.activity.home.adapter.VisitServiceListAdapter;
import ensan.patientapp.view.activity.home.model.Datum;
import ensan.patientapp.view.activity.home.model.GetCategoryPosition;
import ensan.patientapp.view.activity.home.viewmodel.GetSubCategoryViewModel;
import ensan.patientapp.view.activity.login.model.LoginResponse;


public class DoctorVisitActivity extends AppCompatActivity implements GetCategoryPosition, View.OnClickListener {

    DoctorVisitActivityBinding doctorVisitActivityBinding;
    private String token;
    private int catID;
    private VisitServiceListAdapter visitServiceListAdapter;
    private List<Datum> list = new ArrayList<>();
    private List<Datum> subCategoryList = new ArrayList<>();
    private Progress progress;
    private String title;
    private LoginResponse resp;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get Token
        resp = (LoginResponse) Util.getInstance(this).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginResponse>() {
                });

        if(resp != null) {
            language = resp.getData().getLanaguage();
            Utility.setLocale(this, language);
        }

        doctorVisitActivityBinding = DataBindingUtil.setContentView(this, R.layout.doctor_visit_activity);

        // get data from intent
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                token = (String) bundle.get(Constants.KEY_TOKEN);
                catID =(int) bundle.get(Constants.KEY_CAT_ID);
                title = (String) bundle.get(Constants.KEY_SUB_CAT_TITLE);
            }
        }

        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);

        doctorVisitActivityBinding.visitServicesRv.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        visitServiceListAdapter = new VisitServiceListAdapter(this,list,this);
        doctorVisitActivityBinding.visitServicesRv.setAdapter(visitServiceListAdapter);

        // call sub category id API
        callSubCategoryApi(catID);

        doctorVisitActivityBinding.btnCross.setOnClickListener(this);
        doctorVisitActivityBinding.imgBack.setOnClickListener(this);

        // set title
        doctorVisitActivityBinding.subTitle.setText(title);
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
                if (msg.equals(getString(R.string.no_services_found))){
                    DoctorVisitActivity.this.finish();
                }
            }
        });
        mAlert.show();
    }
    public void nextClick(View view) {
        if (subCategoryList==null || subCategoryList.size() <1){
            openDialog(getString(R.string.no_service_error));
            return;
        }
        Intent intent = new Intent(getApplicationContext(), ChooseDateTimeActivity.class);
        intent.putExtra(Constants.KEY_TOKEN,token);
        intent.putExtra(Constants.KEY_DATA, Parcels.wrap(subCategoryList));
        intent.putExtra(Constants.KEY_CAT_ID,catID);
        startActivity(intent);
    }

    // call get category api
    private void callSubCategoryApi(int position) {
        progress.show();
        String catId = String.valueOf(position);
        GetSubCategoryViewModel viewModel = ViewModelProviders.of(this).get(GetSubCategoryViewModel.class);
        viewModel.init();
        viewModel.getSubCategory(token,catId);
        viewModel.getVolumesResponseLiveData().observe(DoctorVisitActivity.this, subCategoryResponse -> {
            progress.hide();
            if(subCategoryResponse != null){
                if(subCategoryResponse.getSuccess()){
                    list =  subCategoryResponse.getData();
                    if(list == null){
                        list = new ArrayList<>();
                    }
                    if(list.size() == 0){
                        openDialog(getString(R.string.no_services_found));
                    }
                 visitServiceListAdapter.setData(list);
                }else{
                    openDialog(subCategoryResponse.getMessage());
                }
            }else {
                openDialog(this.getString(R.string.errormsg));
            }
        });

    }

    @Override
    public void getPosition(int position, boolean isChecked) {
        Datum datum = list.get(position);
        if (isChecked){
            subCategoryList.add(datum);
        }else{
            subCategoryList.remove(datum);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cross:
            case R.id.img_back:
                finish();
                break;
        }
    }
}