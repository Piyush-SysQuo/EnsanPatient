package ensan.patientapp.view.activity.idProof.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;

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
import ensan.patientapp.view.activity.idProof.adapter.IdProofAdapter;
import ensan.patientapp.view.activity.idProof.model.GetIdImagePosition;
import ensan.patientapp.view.activity.idProof.model.IdProofResponse;
import ensan.patientapp.view.activity.idProof.viewmodel.IdProofViewModel;
import ensan.patientapp.view.activity.idProof.model.Data;
import ensan.patientapp.view.activity.idinsurance.model.InsuranceResponse;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IdProofActivity extends AppCompatActivity implements GetIdImagePosition {

    private RecyclerView IdProofRV;
    private String token;
    private Progress progress;
    private  List<Data> dataList;
    private  IdProofAdapter idProofAdapter;
    private LinearLayout no_idproof_layout;

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

        setContentView(R.layout.activity_id_proof);

        IdProofRV = findViewById(R.id.IdProofRV);
        no_idproof_layout = findViewById(R.id.no_idproof_layout);

        // get data from intent
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                token = (String) bundle.get(Constants.KEY_TOKEN);
            }
        }

        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);
        progress.show();


        IdProofViewModel viewModel = ViewModelProviders.of(this).get(IdProofViewModel.class);
        viewModel.init();
        viewModel.getIdProof(token);
        viewModel.getVolumesResponseLiveData().observe(this, insuranceResponse -> {
            progress.hide();
            if(insuranceResponse!=null){
                dataList = insuranceResponse.getData();
                if(dataList.size() != 0) {
                    IdProofRV.setVisibility(View.VISIBLE);
                    no_idproof_layout.setVisibility(View.GONE);
                    IdProofRV.setLayoutManager(new LinearLayoutManager(IdProofActivity.this));
                    idProofAdapter = new IdProofAdapter(dataList, IdProofActivity.this, this);
                    IdProofRV.setAdapter(idProofAdapter);
                }else {
                    IdProofRV.setVisibility(View.GONE);
                    no_idproof_layout.setVisibility(View.VISIBLE);
                }
            }else{
                openDialog(this.getString(R.string.errormsg));
                Toast.makeText(IdProofActivity.this, getString(R.string.errormsg), Toast.LENGTH_SHORT).show();
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

    public void addIdProof(View view) {
        startActivity(new Intent(this,AddNewIdProof.class));
        finish();
    }

    @Override
    public void position(int position) {
        progress.show();
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<IdProofResponse> call = apiDataService.deleteIDCard("application/json","Bearer "+token,dataList.get(position).getId());
        call.enqueue(new Callback<IdProofResponse>() {
            @Override
            public void onResponse(Call<IdProofResponse> call, Response<IdProofResponse> response) {
                progress.hide();
                if (response.body() != null) {
                    IdProofResponse idProofResponse =  response.body();
                    if(idProofResponse.getSuccess()){
                        idProofAdapter.deleteImage(dataList.get(position));
                        openSuccDialog(idProofResponse.getMessage());
                    }else {
                        openDialog(idProofResponse.getMessage());
                    }
                }else{
                    openDialog(getString(R.string.errormsg));
                }
            }

            @Override
            public void onFailure(Call<IdProofResponse> call, Throwable t) {
                openDialog(getString(R.string.errormsg));
            }
        });
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