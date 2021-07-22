package ensan.patientapp.view.activity.completeprofile.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.features.imageloader.DefaultImageLoader;
import com.esafirm.imagepicker.model.Image;
import com.google.gson.reflect.TypeToken;

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.Util;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.view.activity.completeprofile.adapter.InsuranceRecycleAdapter;
import ensan.patientapp.view.activity.completeprofile.adapter.VerificationRecycleAdapter;
import ensan.patientapp.view.activity.completeprofile.model.Data;
import ensan.patientapp.view.activity.completeprofile.model.InsuranceImagePosition;
import ensan.patientapp.view.activity.completeprofile.model.VerificationImagePosition;
import ensan.patientapp.view.activity.completeprofile.viewmodel.VerificationDocViewModel;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import ensan.patientapp.view.activity.savemedicalHistory.view.SaveMedicalFirst;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CompleteProfileForthActivity extends AppCompatActivity implements VerificationImagePosition, InsuranceImagePosition {

    private boolean isInsurance = false;
    private boolean isVerification = false;
    private boolean isProfile = false;
    private String token;
    private Progress progress;
    private ArrayList<Image> verificationImages = new ArrayList<>();
    private ArrayList<Image> insuranceImages = new ArrayList<>();
    private List<Image> profileList = new ArrayList<>();
    private VerificationRecycleAdapter verificationRecycleAdapter;
    private InsuranceRecycleAdapter insuranceRecycleAdapter;

    @BindView(R.id.idVerification)
    RelativeLayout verification;

    @BindView(R.id.insuranceImage)
    RelativeLayout insurance;

    @BindView(R.id.insurance_msg)
    RelativeLayout insuranceMsg;

    @BindView(R.id.id_msg)
    RelativeLayout id_msg;

    @BindView(R.id.idVerificationRV)
    RecyclerView verificationRV;

    @BindView(R.id.insuranceRV)
    RecyclerView insuranceRV;

    @BindView(R.id.txt_more)
    TextView txt_more;

    @BindView(R.id.insurance_more)
    TextView insurance_more;

    @BindView(R.id.layout_upload_profile)
    LinearLayout layout_upload_profile;

    @BindView(R.id.profileImage)
    ImageView profileImage;

    @BindView(R.id.profileImageLayout)
    RelativeLayout profileImageLayout;


    private File file;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get data from intent
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                token = (String) bundle.get(Constants.KEY_TOKEN);
                language = (String) bundle.get(Constants.KEY_LANGUAGE);
                Utility.setLocale(this,language);
            }
        }

        setContentView(R.layout.omplete_profile_forth_activity);

        // bind butter knife
        ButterKnife.bind(this);

        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);


        // set qualification adapter to image recycle view
        verificationRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        verificationRecycleAdapter = new VerificationRecycleAdapter(verificationImages, this,this);
        verificationRV.setAdapter(verificationRecycleAdapter);

        // set qualification adapter to image recycle view
        insuranceRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        insuranceRecycleAdapter = new InsuranceRecycleAdapter(insuranceImages, this,this);
        insuranceRV.setAdapter(insuranceRecycleAdapter);
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

    public void nextClick(View view) throws IOException {
           if(verificationImages.size() ==0){
                Toast.makeText(this,getString( R.string.idvererror), Toast.LENGTH_SHORT).show();
                return;
            }else {
                progress.show();

                // convert field into Request body and image convert into multipart
                MultipartBody.Part[] insuranceFiles = new MultipartBody.Part[insuranceImages.size()];
                MultipartBody.Part[] verificationFiles = new MultipartBody.Part[verificationImages.size()];

                MultipartBody.Part profile = null;
                if(profileList.size() != 0) {
                    RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), new Compressor(this).compressToFile(new File(profileList.get(0).getPath())));
                    profile = MultipartBody.Part.createFormData("profile_pic", profileList.get(0).getPath(), fileBody);
                }


                if(insuranceImages.size() != 0) {
                    for (int i = 0; i < insuranceImages.size(); i++) {
                        try {
                            insuranceFiles[i] = MultipartBody.Part.createFormData("insurance_doc[" + i + "]",
                                    Calendar.getInstance().getTimeInMillis() + ".jpg",
                                    RequestBody.create(MediaType.parse("image/*"),  new Compressor(this).compressToFile(new File(insuranceImages.get(i).getPath()))));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                if(verificationImages.size() != 0) {
                    for (int i = 0; i < verificationImages.size(); i++) {
                        try {
                            verificationFiles[i] = MultipartBody.Part.createFormData("verification_doc[" + i + "]",
                                    Calendar.getInstance().getTimeInMillis() + ".jpg",
                                    RequestBody.create(MediaType.parse("image/*"), new Compressor(this).compressToFile(new File(verificationImages.get(i).getPath()))));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                VerificationDocViewModel viewModel = ViewModelProviders.of(this).get(VerificationDocViewModel.class);
                viewModel.init();
                viewModel.uploadVerificationId(token, profile, insuranceFiles, verificationFiles);
                viewModel.getVolumesResponseLiveData().observe(this, verificationDocResponse -> {
                    progress.hide();
                    if (verificationDocResponse != null) {
                        if(verificationDocResponse.getSuccess()) {
                            // save data into database
                            Util.getInstance(this).putGsonObject(
                                    Constants.PREFS_LOGIN_RESPONSE, verificationDocResponse, new TypeToken<LoginResponse>() {
                                    });

                            // get Token
                           LoginResponse resp = (LoginResponse) Util.getInstance(this).pickGsonObject(
                                    Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginResponse>() {
                                    });

                            // update profile pic in shared prefrence
                            resp.getData().setToken(token);

                            // update login response
                            Util.getInstance(this).putGsonObject(
                                    Constants.PREFS_LOGIN_RESPONSE,resp, new TypeToken<LoginResponse>() {
                                    });

                            Intent intent = new Intent(getApplicationContext(), SaveMedicalFirst.class);
                            intent.putExtra(Constants.KEY_LANGUAGE, language);
                            intent.putExtra(Constants.KEY_TOKEN, token);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(this, R.string.retry, Toast.LENGTH_SHORT).show();
                        id_msg.setVisibility(View.GONE);
                        verification.setVisibility(View.VISIBLE);
                    }
                });
            }
    }

    @OnClick(R.id.idVerification) void setIdDoc(){
        isInsurance = false;
        isVerification = true;
        isProfile = false;
        selectImages(2);
    }

    @OnClick(R.id.insuranceImage) void setInsuranceImage(){
        isInsurance = true;
        isVerification = false;
        isProfile = false;
        selectImages(2);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(isVerification) {
            if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
                verificationImages.addAll(ImagePicker.getImages(data));
                verificationRV.setVisibility(View.VISIBLE);
                verificationRecycleAdapter.allAllImage(verificationImages);
                txt_more.setVisibility(View.VISIBLE);
                verification.setVisibility(View.GONE);
                return;
            }
        }else if(isInsurance){
            if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
                insuranceImages.addAll(ImagePicker.getImages(data));
                insuranceRV.setVisibility(View.VISIBLE);
                insuranceRecycleAdapter.allAllImage(insuranceImages);
                insurance_more.setVisibility(View.VISIBLE);
                insurance.setVisibility(View.GONE);
                return;
            }
        }else if(isProfile){
            if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
                profileList =  ImagePicker.getImages(data);
                profileImageLayout.setVisibility(View.VISIBLE);
                layout_upload_profile.setVisibility(View.GONE);
                Glide.with(this)
                        .load(profileList.get(0).getUri())
                        .into(profileImage);
                return;
            }
        }
    }


    private void selectImages(int limit) {

        ImagePicker imagePicker =   ImagePicker.create(CompleteProfileForthActivity.this)
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
    public void imageVerificationPosition(int position) {
              verificationRecycleAdapter.deleteImage(verificationImages.get(position));
    }

    public void addMoreImages(View view) {
        isInsurance = false;
        isVerification = true;
        isProfile = false;
        selectImages(2);
    }

    public void addMoreImagesInsurance(View view) {
        isInsurance = true;
        isVerification = false;
        isProfile = false;
        selectImages(2);
    }

    @Override
    public void imageInsurancePosition(int position) {
         insuranceRecycleAdapter.deleteImage(insuranceImages.get(position));
    }

    public void uploadProfile(View view) {
        isInsurance = false;
        isVerification = false;
        isProfile = true;
        selectImages(1);
    }


    public void deleteImage(View view) {
        profileList = new ArrayList<>();
        profileImageLayout.setVisibility(View.GONE);
        layout_upload_profile.setVisibility(View.VISIBLE);
    }
}