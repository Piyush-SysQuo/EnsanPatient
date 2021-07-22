package ensan.patientapp.view.fragment.personal.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.Util;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.databinding.PersonalFragmentBinding;
import ensan.patientapp.view.activity.paymentMethod.view.PaymentMethod;
import ensan.patientapp.view.activity.familymember.view.FamilyMemberActivity;
import ensan.patientapp.view.activity.idProof.view.IdProofActivity;
import ensan.patientapp.view.fragment.personal.personalViewModel.CoverPicViewModel;
import ensan.patientapp.view.activity.idinsurance.view.IdInsuranceActivity;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import ensan.patientapp.view.activity.medicalhistory.view.MedicalHistoryActivity;
import ensan.patientapp.view.activity.personaldetail.view.PersonalDetailsActivity;
import ensan.patientapp.R;
import ensan.patientapp.view.fragment.personal.model.UserProfileResponse;
import ensan.patientapp.view.fragment.personal.personalViewModel.PersonalViewModel;
import ensan.patientapp.view.fragment.personal.personalViewModel.ProfilePicViewModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class PersonalFragment extends Fragment implements View.OnClickListener {

    private PersonalFragmentBinding personalFragmentBinding;
    private View view;
    private String token;
    private Progress progress;
    private UserProfileResponse userProfileResponse;
    private File file;
    private RelativeLayout header;
    private LoginResponse resp;
    private String language;
    private String bloodGroup,bmi;
    private String height,weight;
    private DialogInterface dialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // get Token
        resp = (LoginResponse) Util.getInstance(getActivity()).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginResponse>() {
                });

        if(resp != null){
            language = resp.getData().getLanaguage();
            token =  resp.getData().getToken();
            Utility.setLocale(getActivity(),language);
        }

        // Inflate the layout for this fragment
        personalFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.personal_fragment, container, false);
        view = personalFragmentBinding.getRoot();

        personalFragmentBinding.personalInfo.setOnClickListener(this);
        personalFragmentBinding.familyMember.setOnClickListener(this);
        personalFragmentBinding.medicalLayout.setOnClickListener(this);
        personalFragmentBinding.idLayout.setOnClickListener(this);
        personalFragmentBinding.insuranceLayout.setOnClickListener(this);
        personalFragmentBinding.backPressed.setOnClickListener(this);
        personalFragmentBinding.imgEdit.setOnClickListener(this);
        personalFragmentBinding.profileCam.setOnClickListener(this);
        personalFragmentBinding.userImg.setOnClickListener(this);
        personalFragmentBinding.paymentMethod.setOnClickListener(this);

        // initialize progress dialog instance
        progress = new Progress(getContext());
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.show();
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);


        if(resp != null){
            token = resp.getData().getToken();
        }

        // all get personal details API
        getPersonalDetails();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // all get personal details API
        getPersonalDetails();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.personalInfo:
                Intent intent = new Intent(getActivity(), PersonalDetailsActivity.class);
                intent.putExtra(Constants.KEY_TOKEN,token);
                startActivity(intent);
                break;

            case R.id.familyMember:
                Intent intentFamily = new Intent(getActivity(), FamilyMemberActivity.class);
                intentFamily.putExtra(Constants.KEY_TOKEN,token);
                startActivity(intentFamily);
                break;

            case R.id.medicalLayout:
                Intent intent1 = new Intent(getActivity(), MedicalHistoryActivity.class);
                intent1.putExtra(Constants.KEY_TOKEN,token);
                intent1.putExtra(Constants.KEY_BLOOD_GROUP,bloodGroup);
                intent1.putExtra(Constants.KEY_BMI,bmi);
                intent1.putExtra(Constants.KEY_HEIGHT,height);
                intent1.putExtra(Constants.KEY_WEIGHT,weight);
                startActivity(intent1);
                break;

            case R.id.idLayout:
                Intent intentIDProof = new Intent(getActivity(), IdProofActivity.class);
                intentIDProof.putExtra(Constants.KEY_TOKEN,token);
                startActivity(intentIDProof);

                break;
            case R.id.insuranceLayout:
                Intent intentInsurance = new Intent(getActivity(), IdInsuranceActivity.class);
                intentInsurance.putExtra(Constants.KEY_TOKEN,token);
                startActivity(intentInsurance);

                break;
            case R.id.backPressed:
                getActivity().getSupportFragmentManager().popBackStack();

                break;
            case R.id.paymentMethod:
                Intent intentPayment = new Intent(getActivity(), PaymentMethod.class);
                startActivity(intentPayment);

                break;
            case R.id.profile_cam :

            case R.id.userImg :
                checkRunTimePermission();
                break;

        }
    }

    // check run time permission
    private void checkRunTimePermission() {

        Dexter.withContext(getContext()).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                selectImage(getContext());
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }


    private void selectImage(Context context) {
        final CharSequence[] options = getResources().getStringArray(R.array.dialog_profile);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.chooseprofile));
        builder.setItems(options, (dialog, item) -> {

            this.dialog = dialog;
            if (options[item].equals(getString(R.string.take_photo))) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
                dialog.dismiss();

            } else if (options[item].equals(getString(R.string.choose_gallery))){
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);
                dialog.dismiss();

            } else if (options[item].equals(getString(R.string.cancel))) {
                dialog.dismiss();
            }
        });

        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // dismiss dialog
        if(dialog != null){
            dialog.dismiss();
        }
        if (resultCode != getActivity().RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == getActivity().RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        File file = getFile(selectedImage);
                        callProfilePicApi(file);
                    }
                    break;
                case 1:
                    if (resultCode ==getActivity(). RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                            File file = getFile(bitmap);
                            callProfilePicApi(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    break;
            }
        }
    }

    // convert image into file
    private File getFile(Bitmap selectedImage){

        File file = new File(getContext().getCacheDir(),"FileName.png");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }


    // call upload cover pic api
    private void callUpdateCoverPicApi(File file){
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("cover_pic", file.getName(), fileBody);
        CoverPicViewModel viewModel = ViewModelProviders.of(this).get(CoverPicViewModel.class);
        viewModel.init();
        viewModel.uploadCoverPic(token,body);
        viewModel.getVolumesResponseLiveData().observe(this, coverPicResponse -> {
            if(coverPicResponse != null){
                Glide.with(this).load(coverPicResponse.getData().getCoverPic()).into(personalFragmentBinding.coverImage);
                Toast.makeText(getContext(), coverPicResponse.getMessage(), Toast.LENGTH_SHORT).show();

                // update cover pic in shared prefrence
                resp.getData().setCover_pic(coverPicResponse.getData().getCoverPic());

                // update login response
                Util.getInstance(getContext()).putGsonObject(
                        Constants.PREFS_LOGIN_RESPONSE,resp, new TypeToken<LoginResponse>() {
                        });
            }else{

            }
        });
    }

    // call upload cover pic api
    private void callProfilePicApi(File file){
        progress.show();
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("profile_pic", file.getName(), fileBody);
        ProfilePicViewModel viewModel = ViewModelProviders.of(this).get(ProfilePicViewModel.class);
        viewModel.init();
        viewModel.uploadProfilePic(token,body);
        viewModel.getVolumesResponseLiveData().observe(this, profilePicResponse -> {
            progress.hide();
            if(profilePicResponse != null){
                if(!profilePicResponse.getData().getProfilePic().isEmpty()){

                    Glide.with(this).load(profilePicResponse.getData().getProfilePic()).into(personalFragmentBinding.userImg);

                     // update profile pic in shared prefrence
                     resp.getData().setProfilePic(profilePicResponse.getData().getProfilePic());

                     // update login response
                     Util.getInstance(getContext()).putGsonObject(
                            Constants.PREFS_LOGIN_RESPONSE,resp, new TypeToken<LoginResponse>() {
                            });
                }

                Toast.makeText(getContext(), profilePicResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }else{

            }
        });
    }

    private synchronized void getPersonalDetails(){
        PersonalViewModel viewModel = ViewModelProviders.of(getActivity()).get(PersonalViewModel.class);
        viewModel.init();
        viewModel.getUserDetails(token);
        viewModel.getVolumesResponseLiveData().observe(getActivity(), userProfileResponse -> {
            progress.hide();
            if(userProfileResponse != null) {
                if (userProfileResponse.getStatus()) {
                    this.userProfileResponse = userProfileResponse;
                    personalFragmentBinding.nameTv.setText(userProfileResponse.getData().getUser().getFirstName() + " " + userProfileResponse.getData().getUser().getLastName());
                    if(userProfileResponse.getData().getUser().getAddress()!=null) {
                        personalFragmentBinding.addTv.setText(userProfileResponse.getData().getUser().getAddress().getCity());
                    }
                    if(userProfileResponse.getData().getUser().getB_group()!=null) {
                        bloodGroup = userProfileResponse.getData().getUser().getB_group();
                    }
                    if(userProfileResponse.getData().getUser().getHeight()!=null) {
                        height = userProfileResponse.getData().getUser().getHeight();
                    }
                    if(userProfileResponse.getData().getUser().getWeight()!=null) {
                        weight = userProfileResponse.getData().getUser().getWeight();
                    }

                    if(userProfileResponse.getData().getUser().getHeight()!=null  )
                    {
                        if(userProfileResponse.getData().getUser().getWeight()!=null) {
                            double heightInMeters = Double.valueOf(height) / 100;
                            bmi = String.format("%.2f", (Integer.valueOf(weight) / (heightInMeters * heightInMeters)));
                        }
                    }

                    if(userProfileResponse.getData().getUser().getProfilePic()!=null){
                        Glide.with(this).load(userProfileResponse.getData().getUser().getProfilePic()).into(personalFragmentBinding.userImg);
                        Glide.with(this).load(userProfileResponse.getData().getUser().getCover_pic()).into(personalFragmentBinding.coverImage);
                    }
                } else {
                    Toast.makeText(getActivity(), getString(R.string.errormsg), Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getActivity(), getString(R.string.errormsg), Toast.LENGTH_SHORT).show();
            }
        });
    }

}