package ensan.patientapp.view.activity.idinsurance.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.RelativeLayout;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.features.imageloader.DefaultImageLoader;
import com.esafirm.imagepicker.model.Image;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
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
import ensan.patientapp.view.activity.idProof.adapter.AddIdAdapter;
import ensan.patientapp.view.activity.idProof.model.IdProofResponse;
import ensan.patientapp.view.activity.idProof.view.AddNewIdProof;
import ensan.patientapp.view.activity.idinsurance.adapter.AddInsuranceAdapter;
import ensan.patientapp.view.activity.idinsurance.model.InsuranceResponse;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import ensan.patientapp.view.activity.idinsurance.model.GetInsDocPosition;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewInsurance extends AppCompatActivity implements GetInsDocPosition {

    private ArrayList<Image> images = new ArrayList<>();
    private RelativeLayout upLayoutQualification;
    private RecyclerView addnewRV;
    private AddInsuranceAdapter addInsuranceAdapter;
    private LoginResponse resp;
    private String token;
    private Progress progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get Token
        resp = (LoginResponse) Util.getInstance(this).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginResponse>() {
                });

        if(resp != null){
            token =  resp.getData().getToken();
            String language = resp.getData().getLanaguage();
            Utility.setLocale(this,language);
        }

        setContentView(R.layout.activity_add_new_insurance);

        upLayoutQualification = findViewById(R.id.upLayoutQualification);
        addnewRV = findViewById(R.id.addnewRV);


        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);


        // set default adapter
        addnewRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        addInsuranceAdapter = new AddInsuranceAdapter(images, this,this);
        addnewRV.setAdapter(addInsuranceAdapter);

    }

    public void confirmClick(View view) {

        if(images.size() == 0){
            return;
        }
        MultipartBody.Part[] imagesFiles = new MultipartBody.Part[images.size()];
        for (int i = 0; i < images.size(); i++) {
            try {
                imagesFiles[i] = MultipartBody.Part.createFormData("document[" + i + "]",
                        Calendar.getInstance().getTimeInMillis() + ".jpg",
                        RequestBody.create(MediaType.parse("image/*"),  new Compressor(this).compressToFile(new File(images.get(i).getPath()))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        progress.show();

        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<InsuranceResponse> call = apiDataService.saveInsurance("application/json","Bearer "+token,imagesFiles);
        call.enqueue(new Callback<InsuranceResponse>() {
            @Override
            public void onResponse(Call<InsuranceResponse> call, Response<InsuranceResponse> response) {
                progress.hide();
                if (response.body() != null) {
                    InsuranceResponse idProofResponse =  response.body();
                    if(idProofResponse.getStatus()){
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
                progress.hide();
                openDialog(getString(R.string.errormsg));
            }
        });

    }

    public void backPressed(View view) {
        finish();
    }

    public void imageClick(View view) {
        selectImages(4);
    }

    @Override
    public void getPosition(int position) {
            addInsuranceAdapter.deleteImage(images.get(position));
    }

    private void selectImages(int limit) {
        ImagePicker imagePicker =   ImagePicker.create(this)
                .language("en")
                .theme(R.style.ImagePickerTheme)
                .returnMode(ReturnMode.NONE)
                .folderMode(false)
                .includeVideo(false)
                .toolbarArrowColor(Color.WHITE)
                .toolbarFolderTitle("Folder")
                .toolbarImageTitle("Tap to select")
                .toolbarDoneButtonText("DONE");
        imagePicker.multi();
        new DefaultImageLoader();
        imagePicker.limit(limit);
        imagePicker.showCamera(true);
        imagePicker.imageDirectory("Camera");
        imagePicker.imageFullDirectory(Environment.getExternalStorageDirectory().getPath());
        imagePicker.start();
    }

    // open dialog
    private void openDialog(String msg){

        AlertPopup mAlert = new AlertPopup(this);
        mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert.setMessage(msg);
        mAlert.setOkButton(view -> {
            mAlert.dismiss();
            finish();
            //Do want you want
        });
        mAlert.show();
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
                finish();
                //Do want you want
            }
        });
        mAlert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            images.addAll(ImagePicker.getImages(data));
            upLayoutQualification.setVisibility(View.GONE);
            addInsuranceAdapter.addAllImage(images);
            return;
        }
    }

}