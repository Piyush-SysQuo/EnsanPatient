package ensan.patientapp.view.activity.idProof.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import ensan.patientapp.view.activity.completeprofile.model.VerificationDocResponse;
import ensan.patientapp.view.activity.idProof.adapter.AddIdAdapter;
import ensan.patientapp.view.activity.idProof.model.GetIdImagePosition;
import ensan.patientapp.view.activity.idProof.model.IdProofResponse;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddNewIdProof extends AppCompatActivity implements GetIdImagePosition {

    private ArrayList<Image> images = new ArrayList<>();
    private LinearLayout upLayoutQualification;
    private RecyclerView addnewRV;
    private AddIdAdapter addIdAdapter;
    private LoginResponse resp;
    private String token;
    private Progress progress;


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

        setContentView(R.layout.activity_add_new_id_proof);

        upLayoutQualification = findViewById(R.id.upLayoutQualification);
        addnewRV = findViewById(R.id.addnewRV);


        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);


        // set default adapter
        addnewRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        addIdAdapter = new AddIdAdapter(images,AddNewIdProof.this,this);
        addnewRV.setAdapter(addIdAdapter);

    }


    public void imageClick(View view) {
          selectImages(2);
    }


    private void selectImages(int limit) {
        ImagePicker imagePicker =   ImagePicker.create(AddNewIdProof.this)
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            images.addAll(ImagePicker.getImages(data));
            upLayoutQualification.setVisibility(View.GONE);
            addIdAdapter.addAllImage(images);
            return;
        }
    }

    public void backPressed(View view) {
        finish();
    }

    @Override
    public void position(int position) {
         addIdAdapter.deleteImage(images.get(position));
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
        Call<IdProofResponse> call = apiDataService.saveIdProof("application/json","Bearer "+token,imagesFiles);
        call.enqueue(new Callback<IdProofResponse>() {
            @Override
            public void onResponse(Call<IdProofResponse> call, Response<IdProofResponse> response) {
                progress.hide();
                if (response.body() != null) {
                   IdProofResponse idProofResponse =  response.body();
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
            public void onFailure(Call<IdProofResponse> call, Throwable t) {
                progress.hide();
                openDialog(getString(R.string.errormsg));
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
}
