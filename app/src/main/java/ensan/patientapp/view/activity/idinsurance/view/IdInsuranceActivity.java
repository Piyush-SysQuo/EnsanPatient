package ensan.patientapp.view.activity.idinsurance.view;

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
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.SuccessPopup;
import ensan.patientapp.Utils.Util;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.idinsurance.adapter.InsuranceDocAdapter;
import ensan.patientapp.view.activity.idinsurance.model.GetInsDocPosition;
import ensan.patientapp.view.activity.idinsurance.model.InsuranceResponse;
import ensan.patientapp.view.activity.idinsurance.viewmodel.InsuranceViewModel;
import ensan.patientapp.view.activity.idinsurance.model.Data;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IdInsuranceActivity extends AppCompatActivity implements GetInsDocPosition {

    private String token;
    private Progress progress;
    private RecyclerView insuranceRV;
    private List<Data> dataList;
    private InsuranceDocAdapter insuranceDocAdapter;
    private LinearLayout no_insurance_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get Token
        LoginResponse resp = (LoginResponse) Util.getInstance(this).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginResponse>() {
                });

        if(resp != null){
            String language = resp.getData().getLanaguage();
            token = resp.getData().getToken();
            Utility.setLocale(this, language);
        }

        setContentView(R.layout.id_insurance_activity);

        // get data from intent
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                token = (String) bundle.get(Constants.KEY_TOKEN);
            }
        }

        insuranceRV = findViewById(R.id.InsuranceRV);
        no_insurance_layout = findViewById(R.id.no_insurance_layout);

        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);
        progress.show();


        InsuranceViewModel viewModel = ViewModelProviders.of(this).get(InsuranceViewModel.class);
        viewModel.init();
        viewModel.getInsuranceCard(token);
        viewModel.getVolumesResponseLiveData().observe(this, insuranceResponse -> {
          progress.hide();
          if(insuranceResponse!=null){
                dataList = insuranceResponse.getData();
               if(dataList.size() != 0){
                   no_insurance_layout.setVisibility(View.GONE);
                   insuranceRV.setVisibility(View.VISIBLE);
                   insuranceRV.setLayoutManager(new LinearLayoutManager(IdInsuranceActivity.this));
                   insuranceDocAdapter = new InsuranceDocAdapter(IdInsuranceActivity.this,dataList,this);
                   insuranceRV.setAdapter(insuranceDocAdapter);
               }else {
                   insuranceRV.setVisibility(View.GONE);
                   no_insurance_layout.setVisibility(View.VISIBLE);
              }
            }else{
                openDialog(this.getString(R.string.errormsg));
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

    @Override
    public void getPosition(int position) {
        progress.show();
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<InsuranceResponse> call = apiDataService.deleteInsuranceCard("application/json","Bearer "+token,dataList.get(position).getId());
        call.enqueue(new Callback<InsuranceResponse>() {
            @Override
            public void onResponse(Call<InsuranceResponse> call, Response<InsuranceResponse> response) {
                progress.hide();
                if (response.body() != null) {
                    InsuranceResponse idProofResponse =  response.body();
                    if(idProofResponse.getSuccess()){
                        insuranceDocAdapter.deleteImage(dataList.get(position));
                        openSuccDialog(idProofResponse.getMessage());
                    }else {
                        openDialog(idProofResponse.getMessage());
                    }
                }else{
                    openDialog(getString(R.string.errormsg));
                }
            }

            @Override
            public void onFailure(Call<InsuranceResponse> call, Throwable t) {
                openDialog(getString(R.string.errormsg));
            }
        });

    }

    public void addNewInsurance(View view) {
        startActivity(new Intent(this,AddNewInsurance.class));
        finish();
    }

    // open dialog
    private void openSuccDialog(String msg){

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

    public void btnContinue(View view) {
        finish();
    }
}