package ensan.patientapp.view.activity.savemedicalHistory.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

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
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.SuccessPopup;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.view.activity.completeprofile.model.Data;
import ensan.patientapp.view.activity.savemedicalHistory.viewmodel.AllergyViewModel;
import ensan.patientapp.view.activity.savemedicalHistory.viewmodel.MedicalHistoryViewModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SaveMedicalFirst extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {


    @BindView(R.id.ck_condition)
    CheckBox ck_condition;

    @BindView(R.id.layout_medical_condition)
    LinearLayout layout_medical_condition;

    @BindView(R.id.etMedicalCondition)
    EditText etMedicalCondition;

    @BindView(R.id.ck_surgery)
    CheckBox ck_surgery;

    @BindView(R.id.layout_surgery)
    LinearLayout layout_surgery;

    @BindView(R.id.etsurgery)
    EditText etsurgery;

    @BindView(R.id.ck_med_routine)
    CheckBox ck_med_routine;

    @BindView(R.id.layout_medical_routine)
    LinearLayout layout_medical_routine;

    @BindView(R.id.etmedicalroutine)
    EditText etmedicalroutine;

    @BindView(R.id.ck_trauma)
    CheckBox ck_trauma;

    @BindView(R.id.layout_trauma)
    LinearLayout layout_trauma;

    @BindView(R.id.ettrauma)
    EditText ettrauma;

    @BindView(R.id.ck_comorbidities)
    CheckBox ck_comorbidities;

    @BindView(R.id.layout_comorbidities)
    LinearLayout layout_comorbidities;

    @BindView(R.id.etcomorbidities)
    EditText etcomorbidities;

    @BindView(R.id.ck_condition_mobility)
    CheckBox ck_condition_mobility;

    @BindView(R.id.ck_allergy)
    CheckBox ck_allergy;

    @BindView(R.id.ck_allergy_food)
    CheckBox ck_allergy_food;

    @BindView(R.id.ck_allergy_drug)
    CheckBox ck_allergy_drug;

    @BindView(R.id.ck_allergy_other)
    CheckBox ck_allergy_other;

    @BindView(R.id.general_condition_mobility)
    LinearLayout general_condition_mobility;

    @BindView(R.id.etmobility)
    EditText etmobility;

    @BindView(R.id.etfood)
    EditText etfood;

    @BindView(R.id.etdrug)
    EditText etdrug;

    @BindView(R.id.etother)
    EditText etother;

    @BindView(R.id.layoutMedicalCondition)
    RelativeLayout layoutMedicalCondition;

    @BindView(R.id.uplayoutMedicalCondition)
    RelativeLayout uplayoutMedicalCondition;

    @BindView(R.id.btnmcondition)
    TextView btnmcondition;

    @BindView(R.id.layoutsurgery)
    RelativeLayout layoutsurgery;

    @BindView(R.id.uplayoutsurgery)
    RelativeLayout uplayoutsurgery;

    @BindView(R.id.btnsurgenrycondition)
    TextView btnsurgenrycondition;

    @BindView(R.id.layoutmedicalroutine)
    RelativeLayout layoutmedicalroutine;

    @BindView(R.id.uplayoutmedroutine)
    RelativeLayout uplayoutmedroutine;

    @BindView(R.id.btnmedicalroutine)
    TextView btnmedicalroutine;

    @BindView(R.id.uplayouttrauma)
    RelativeLayout uplayouttrauma;

    @BindView(R.id.layouttrauma)
    RelativeLayout layouttrauma;

    @BindView(R.id.btnTrauma)
    TextView btnTrauma;

    @BindView(R.id.uplayoutcomorbidities)
    RelativeLayout uplayoutcomorbidities;

    @BindView(R.id.layoutcomorbidities)
    RelativeLayout layoutcomorbidities;

    @BindView(R.id.btncomorbidities)
    TextView btncomorbidities;

    @BindView(R.id.uplayoutconditionmobility)
    RelativeLayout uplayoutconditionmobility;

    @BindView(R.id.layoutconditionmobility)
    RelativeLayout layoutconditionmobility;

    @BindView(R.id.btngenralcondition)
    TextView btngenralcondition;

    @BindView(R.id.layout_allergy)
    LinearLayout layout_allergy;

    @BindView(R.id.layoutfood)
    LinearLayout layoutfood;

    @BindView(R.id.layoutdruglayout)
    LinearLayout layoutdruglayout;

    @BindView(R.id.layoutdotherlayout)
    LinearLayout layoutdotherlayout;

    @BindView(R.id.layout_action)
    LinearLayout layout_action;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    @BindView(R.id.radioButton1)
    RadioButton yes;

    @BindView(R.id.radioButton2)
    RadioButton no;

    private String token;
    private Progress progress;
    private File file;
    private String imageID = "0";
    private String language;

    // Flag variable to check for which checkbox view, we have uploaded images, since first checkbox is medical-condition, thus it has the same value
    private String selectionFlag="medical-condition";

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

        setContentView(R.layout.activity_save_medical_first);

        ButterKnife.bind(this);

        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);



        ck_condition.setOnCheckedChangeListener(this);
        ck_surgery.setOnCheckedChangeListener(this);
        ck_allergy_food.setOnCheckedChangeListener(this);
        ck_med_routine.setOnCheckedChangeListener(this);
        ck_comorbidities.setOnCheckedChangeListener(this);
        ck_trauma.setOnCheckedChangeListener(this);
        ck_condition_mobility.setOnCheckedChangeListener(this);
        ck_allergy.setOnCheckedChangeListener(this);
        ck_allergy_drug.setOnCheckedChangeListener(this);
        ck_allergy_other.setOnCheckedChangeListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        yes.setOnCheckedChangeListener(this);
        no.setOnCheckedChangeListener(this);

        if(ck_condition.isChecked()){
            layout_medical_condition.setVisibility(View.VISIBLE);
        }
        if(ck_condition_mobility.isChecked()){
            general_condition_mobility.setVisibility(View.VISIBLE);
        }


        // save medical condition  data
        layoutMedicalCondition.setOnClickListener(v -> {
            selectionFlag = "medical-condition";
            checkRunTimePermission();
        });

        layoutsurgery.setOnClickListener(v -> {
            selectionFlag = "surgery";
            checkRunTimePermission();
        });

        layoutmedicalroutine.setOnClickListener(v -> {
            selectionFlag = "medical-routine";
            checkRunTimePermission();
        });

        layouttrauma.setOnClickListener(v -> {
            selectionFlag = "trauma";
             checkRunTimePermission();
        });

        layoutcomorbidities.setOnClickListener(v -> {
            selectionFlag = "comorbidities";
            checkRunTimePermission();
        });

        layoutconditionmobility.setOnClickListener(v -> {
            selectionFlag = "condition-mobility";
            checkRunTimePermission();
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

    public void nextClick(View view) {
        Intent intent = new Intent(new Intent(this, SaveMedicalSecond.class));
        intent.putExtra(Constants.KEY_TOKEN,token);
        intent.putExtra(Constants.KEY_LANGUAGE,language);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.ck_condition :
                imageID = "0";
                if(isChecked){
                    layout_medical_condition.setVisibility(View.VISIBLE);
                }else{
                    layout_medical_condition.setVisibility(View.GONE);
                }
                break;
            case R.id.ck_surgery :
                imageID = "0";
                if(isChecked){
                    layout_surgery.setVisibility(View.VISIBLE);
                }else{
                    layout_surgery.setVisibility(View.GONE);
                }
                break;
            case R.id.ck_med_routine :
                imageID = "0";
                if(isChecked){
                    layout_medical_routine.setVisibility(View.VISIBLE);
                }else{
                    layout_medical_routine.setVisibility(View.GONE);
                }
                break;
            case R.id.ck_trauma :
                imageID = "0";
                if(isChecked){
                    layout_trauma.setVisibility(View.VISIBLE);
                }else{
                    layout_trauma.setVisibility(View.GONE);
                }
                break;
            case R.id.ck_comorbidities :
                imageID = "0";
                if(isChecked){
                    layout_comorbidities.setVisibility(View.VISIBLE);
                }else{
                    layout_comorbidities.setVisibility(View.GONE);
                }
                break;
            case R.id.ck_condition_mobility :
                imageID = "0";
                if(isChecked){
                    general_condition_mobility.setVisibility(View.VISIBLE);
                }else{
                    general_condition_mobility.setVisibility(View.GONE);
                }
                break;
            case R.id.ck_allergy :
                imageID = "0";
                if(isChecked){
                    layout_allergy.setVisibility(View.VISIBLE);
                }else{
                    layout_allergy.setVisibility(View.GONE);
                }
                break;
            case R.id.ck_allergy_food :
                imageID = "0";
                if(isChecked){
                    layoutfood.setVisibility(View.VISIBLE);
                }else{
                    layoutfood.setVisibility(View.GONE);
                }
                break;
            case R.id.ck_allergy_drug :
                imageID = "0";
                if(isChecked){
                    layoutdruglayout.setVisibility(View.VISIBLE);
                }else{
                    layoutdruglayout.setVisibility(View.GONE);
                }
                break;
            case R.id.ck_allergy_other :
                imageID = "0";
                if(isChecked){
                    layoutdotherlayout.setVisibility(View.VISIBLE);
                }else{
                    layoutdotherlayout.setVisibility(View.GONE);
                }
                break;
        }
    }

    // check run time permission
    private void checkRunTimePermission() {

        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                selectImage(SaveMedicalFirst.this);
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
        final CharSequence[] options = getResources().getStringArray(R.array.cameralist);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.choosedoc);

        builder.setItems(options, (dialog, item) -> {

            if (options[item].equals(getString(R.string.take_photo))) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);

            } else if (options[item].equals(getString(R.string.choose_gallery))) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);

            } else if (options[item].equals(getString(R.string.cancel))) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        file =  Utility.getFile(this,selectedImage);
                        openImageDialog(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            file =  Utility.getFile(this,bitmap);
                            openImageDialog(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    break;
            }
        }
    }

    // Method to show clicked/selected image
    private void openImageDialog(Bitmap bitmap){
        // uploadLayout = Layout on which user clicks to upload image
        // selectionLayout = Layout which replaces the uploadLayout to show image selected message
        //btnView = Save Button for uploadLayout to save Image

        RelativeLayout uploadLayout,selectionLayout;
        TextView btnView;

        switch (selectionFlag){
            case "medical-condition":
                uploadLayout = layoutMedicalCondition;
                selectionLayout = uplayoutMedicalCondition;
                btnView = btnmcondition;
                break;
            case "surgery":
                uploadLayout = layoutsurgery;
                selectionLayout = uplayoutsurgery;
                btnView = btnsurgenrycondition;
                break;
            case "medical-routine":
                uploadLayout = layoutmedicalroutine;
                selectionLayout = uplayoutmedroutine;
                btnView = btnmedicalroutine;
                break;
            case "trauma":
                uploadLayout = layouttrauma;
                selectionLayout = uplayouttrauma;
                btnView = btnTrauma;
                break;
            case "comorbidities":
                uploadLayout = layoutcomorbidities;
                selectionLayout = uplayoutcomorbidities;
                btnView = btncomorbidities;
                break;
            case "condition-mobility":
                uploadLayout = layoutconditionmobility;
                selectionLayout = uplayoutconditionmobility;
                btnView = btngenralcondition;
                break;
            default:
                Toast.makeText(this,getString(R.string.invalidselection), Toast.LENGTH_SHORT).show();
                return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uploadLayout.setVisibility(View.GONE);
                selectionLayout.setVisibility(View.VISIBLE);
                btnView.setText(R.string.save_btn);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                file=null;
            }
        });
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.custom_image_dialog, null);
        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                ImageView image = (ImageView) dialog.findViewById(R.id.imageView);
                float imageWidthInPX = (float)image.getWidth();

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(imageWidthInPX),
                        Math.round(imageWidthInPX * (float)bitmap.getHeight() / (float)bitmap.getWidth()));
                image.setLayoutParams(layoutParams);
                image.setImageBitmap(bitmap);
            }
        });

        dialog.show();
    }

    public void addMedicalCondition(View view) {
        progress.show();
        String his = etMedicalCondition.getText().toString().trim();
        if(his.isEmpty()){
            progress.hide();
            Toast.makeText(this, getString(R.string.descriptionerror), Toast.LENGTH_SHORT).show();
            return;
        }else if(file == null){
            progress.hide();
            Toast.makeText(this,  getString(R.string.doceuperror), Toast.LENGTH_SHORT).show();
            return;
        }
        // convert field into Request body and image convert into multipart
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("document", file.getName(), fileBody);
        RequestBody medicalKey =  RequestBody.create(MediaType.parse("text/plain"), his);
        RequestBody idKey =  RequestBody.create(MediaType.parse("text/plain"),imageID);
        RequestBody familyKey =  RequestBody.create(MediaType.parse("text/plain"),"0");
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), ck_condition.getText().toString());
        MedicalHistoryViewModel viewModel = ViewModelProviders.of(this).get(MedicalHistoryViewModel.class);
        viewModel.init();
        viewModel.saveMedicalHistoryAddress(token,medicalKey,description,body,idKey,familyKey);
        viewModel.getVolumesResponseLiveData().observe(this, medicalHistoryResponse -> {
            progress.hide();
            if(medicalHistoryResponse != null){
                if(medicalHistoryResponse.getSuccess()){
                    imageID = medicalHistoryResponse.getData().getId();
                    /*uplayoutMedicalCondition.setVisibility(View.VISIBLE);
                    layoutMedicalCondition.setVisibility(View.GONE);
                    btnmcondition.setVisibility(View.GONE);*/

                    // Changing the views back to upload more images, if any
                    uplayoutMedicalCondition.setVisibility(View.GONE);
                    layoutMedicalCondition.setVisibility(View.VISIBLE);
                    btnmcondition.setText(R.string.add_new_btn);
                    openSuccessDialog(medicalHistoryResponse.getMessage());
                }else {
                    openDialog(medicalHistoryResponse.getMessage());
                }
//                Toast.makeText(this, medicalHistoryResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }else{
                openDialog(this.getString(R.string.errormsg));
//                Toast.makeText(this,R.string.errormsg, Toast.LENGTH_SHORT).show();
            }
            // Setting file as null so that if someone clicks on save for next view, then same image isn't uploaded
            file = null;
        });
    }

    public void addSurgery(View view) {
        progress.show();
        String his = etsurgery.getText().toString().trim();
        if(his.isEmpty()){
            progress.hide();
            Toast.makeText(this,  getString(R.string.descriptionerror), Toast.LENGTH_SHORT).show();
            return;
        }else if(file == null){
            progress.hide();
            Toast.makeText(this,  getString(R.string.doceuperror), Toast.LENGTH_SHORT).show();
            return;
        }
        // convert field into Request body and image convert into multipart
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("document", file.getName(), fileBody);
        RequestBody medicalKey =  RequestBody.create(MediaType.parse("text/plain"), his);
        RequestBody idKey =  RequestBody.create(MediaType.parse("text/plain"),imageID);
        RequestBody familyKey =  RequestBody.create(MediaType.parse("text/plain"),"0");
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), ck_surgery.getText().toString());
        MedicalHistoryViewModel viewModel = ViewModelProviders.of(this).get(MedicalHistoryViewModel.class);
        viewModel.init();
        viewModel.saveMedicalHistoryAddress(token,medicalKey,description,body,idKey,familyKey);
        viewModel.getVolumesResponseLiveData().observe(this, medicalHistoryResponse -> {
            progress.hide();
            if(medicalHistoryResponse != null){
                if(medicalHistoryResponse.getSuccess()){
                    imageID = medicalHistoryResponse.getData().getId();
                   /* uplayoutsurgery.setVisibility(View.VISIBLE);
                    layoutsurgery.setVisibility(View.GONE);
                    btnsurgenrycondition.setVisibility(View.GONE);*/

                    // Changing the views back to upload more images, if any
                    uplayoutsurgery.setVisibility(View.GONE);
                    layoutsurgery.setVisibility(View.VISIBLE);
                    btnsurgenrycondition.setText(R.string.add_new_btn);
                    openSuccessDialog(medicalHistoryResponse.getMessage());
                }else {
                    openDialog(medicalHistoryResponse.getMessage());
                }
//                Toast.makeText(this, medicalHistoryResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }else{
                openDialog(this.getString(R.string.errormsg));
//                Toast.makeText(this,R.string.errormsg, Toast.LENGTH_SHORT).show();
            }
            // Setting file as null so that if someone clicks on save for next view, then same image isn't uploaded
            file = null;
        });

    }

    public void addMedicalRoutine(View view) {
        progress.show();
        String his = etmedicalroutine.getText().toString().trim();
        if(his.isEmpty()){
            progress.hide();
            Toast.makeText(this,  getString(R.string.descriptionerror), Toast.LENGTH_SHORT).show();
            return;
        }else if(file == null){
            progress.hide();
            Toast.makeText(this,  getString(R.string.doceuperror), Toast.LENGTH_SHORT).show();
            return;
        }
        // convert field into Request body and image convert into multipart
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("document", file.getName(), fileBody);
        RequestBody medicalKey =  RequestBody.create(MediaType.parse("text/plain"), his);
        RequestBody idKey =  RequestBody.create(MediaType.parse("text/plain"),imageID);
        RequestBody familyKey =  RequestBody.create(MediaType.parse("text/plain"),"0");
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), ck_med_routine.getText().toString());
        MedicalHistoryViewModel viewModel = ViewModelProviders.of(this).get(MedicalHistoryViewModel.class);
        viewModel.init();
        viewModel.saveMedicalHistoryAddress(token,medicalKey,description,body,idKey,familyKey);
        viewModel.getVolumesResponseLiveData().observe(this, medicalHistoryResponse -> {
            progress.hide();
            if(medicalHistoryResponse != null){
                if(medicalHistoryResponse.getSuccess()){
                    imageID = medicalHistoryResponse.getData().getId();
                    /*uplayoutmedroutine.setVisibility(View.VISIBLE);
                    layoutmedicalroutine.setVisibility(View.GONE);
                    btnmedicalroutine.setVisibility(View.GONE);*/

                    // Changing the views back to upload more images, if any
                    uplayoutmedroutine.setVisibility(View.GONE);
                    layoutmedicalroutine.setVisibility(View.VISIBLE);
                    btnmedicalroutine.setText(R.string.add_new_btn);
                    openSuccessDialog(medicalHistoryResponse.getMessage());
                }else {
                    openDialog(medicalHistoryResponse.getMessage());
                }
//                Toast.makeText(this, medicalHistoryResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }else{
                openDialog(this.getString(R.string.errormsg));
//                Toast.makeText(this,R.string.errormsg, Toast.LENGTH_SHORT).show();
            }
            // Setting file as null so that if someone clicks on save for next view, then same image isn't uploaded
            file = null;
        });

    }

    public void addTrauma(View view) {
        progress.show();
        String his = ettrauma.getText().toString().trim();
        if(his.isEmpty()){
            progress.hide();
            Toast.makeText(this,  getString(R.string.descriptionerror), Toast.LENGTH_SHORT).show();
            return;
        }else if(file == null){
            progress.hide();
            Toast.makeText(this, getString(R.string.doceuperror), Toast.LENGTH_SHORT).show();
            return;
        }
        // convert field into Request body and image convert into multipart
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("document", file.getName(), fileBody);
        RequestBody medicalKey =  RequestBody.create(MediaType.parse("text/plain"), his);
        RequestBody idKey =  RequestBody.create(MediaType.parse("text/plain"),imageID);
        RequestBody familyKey =  RequestBody.create(MediaType.parse("text/plain"),"0");
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), ck_trauma.getText().toString());
        MedicalHistoryViewModel viewModel = ViewModelProviders.of(this).get(MedicalHistoryViewModel.class);
        viewModel.init();
        viewModel.saveMedicalHistoryAddress(token,medicalKey,description,body,idKey,familyKey);
        viewModel.getVolumesResponseLiveData().observe(this, medicalHistoryResponse -> {
            progress.hide();
            if(medicalHistoryResponse != null){
                if(medicalHistoryResponse.getSuccess()){
                    imageID = medicalHistoryResponse.getData().getId();
                    /*uplayouttrauma.setVisibility(View.VISIBLE);
                    layouttrauma.setVisibility(View.GONE);
                    btnTrauma.setVisibility(View.GONE);*/

                    // Changing the views back to upload more images, if any
                    uplayouttrauma.setVisibility(View.GONE);
                    layouttrauma.setVisibility(View.VISIBLE);
                    btnTrauma.setText(R.string.add_new_btn);
                    openSuccessDialog(medicalHistoryResponse.getMessage());
                }else {
                    openDialog(medicalHistoryResponse.getMessage());
                }
//                Toast.makeText(this, medicalHistoryResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }else{
                openDialog(this.getString(R.string.errormsg));
//                Toast.makeText(this,R.string.errormsg, Toast.LENGTH_SHORT).show();
            }
            // Setting file as null so that if someone clicks on save for next view, then same image isn't uploaded
            file = null;
        });
    }

    public void addComorbidities(View view) {
        progress.show();
        String his = etcomorbidities.getText().toString().trim();
        if(his.isEmpty()){
            progress.hide();
            Toast.makeText(this,  getString(R.string.descriptionerror), Toast.LENGTH_SHORT).show();
            return;
        }else if(file == null){
            progress.hide();
            Toast.makeText(this, getString(R.string.doceuperror), Toast.LENGTH_SHORT).show();
            return;
        }
        // convert field into Request body and image convert into multipart
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("document", file.getName(), fileBody);
        RequestBody medicalKey =  RequestBody.create(MediaType.parse("text/plain"), his);
        RequestBody idKey =  RequestBody.create(MediaType.parse("text/plain"),imageID);
        RequestBody familyKey =  RequestBody.create(MediaType.parse("text/plain"),"0");
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), ck_comorbidities.getText().toString());
        MedicalHistoryViewModel viewModel = ViewModelProviders.of(this).get(MedicalHistoryViewModel.class);
        viewModel.init();
        viewModel.saveMedicalHistoryAddress(token,medicalKey,description,body,idKey,familyKey);
        viewModel.getVolumesResponseLiveData().observe(this, medicalHistoryResponse -> {
            progress.hide();
            if(medicalHistoryResponse != null){
                if(medicalHistoryResponse.getSuccess()){
                    imageID = medicalHistoryResponse.getData().getId();
                   /* uplayoutcomorbidities.setVisibility(View.VISIBLE);
                    layoutcomorbidities.setVisibility(View.GONE);
                    btncomorbidities.setVisibility(View.GONE);*/

                    // Changing the views back to upload more images, if any
                    uplayoutcomorbidities.setVisibility(View.GONE);
                    layoutcomorbidities.setVisibility(View.VISIBLE);
                    btncomorbidities.setText(R.string.add_new_btn);
                    openSuccessDialog(medicalHistoryResponse.getMessage());
                }else {
                    openDialog(medicalHistoryResponse.getMessage());
                }
//                Toast.makeText(this, medicalHistoryResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }else{
                openDialog(this.getString(R.string.errormsg));
//                Toast.makeText(this,R.string.errormsg, Toast.LENGTH_SHORT).show();
            }
            // Setting file as null so that if someone clicks on save for next view, then same image isn't uploaded
            file = null;
        });
    }

    public void addConditionMobility(View view) {
        progress.show();
        String his = etmobility.getText().toString().trim();
        if(his.isEmpty()){
            progress.hide();
            Toast.makeText(this,  getString(R.string.descriptionerror), Toast.LENGTH_SHORT).show();
            return;
        }else if(file == null){
            progress.hide();
            Toast.makeText(this,  getString(R.string.doceuperror), Toast.LENGTH_SHORT).show();
            return;
        }
        // convert field into Request body and image convert into multipart
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("document", file.getName(), fileBody);
        RequestBody medicalKey =  RequestBody.create(MediaType.parse("text/plain"), his);
        RequestBody idKey =  RequestBody.create(MediaType.parse("text/plain"),imageID);
        RequestBody familyKey =  RequestBody.create(MediaType.parse("text/plain"),"0");
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), ck_condition_mobility.getText().toString());
        MedicalHistoryViewModel viewModel = ViewModelProviders.of(this).get(MedicalHistoryViewModel.class);
        viewModel.init();
        viewModel.saveMedicalHistoryAddress(token,medicalKey,description,body,idKey,familyKey);
        viewModel.getVolumesResponseLiveData().observe(this, medicalHistoryResponse -> {
            progress.hide();
            if(medicalHistoryResponse != null){
                if(medicalHistoryResponse.getSuccess()){
                    imageID = medicalHistoryResponse.getData().getId();
                    /*uplayoutconditionmobility.setVisibility(View.VISIBLE);
                    layoutconditionmobility.setVisibility(View.GONE);
                    btngenralcondition.setVisibility(View.GONE);*/

                    // Changing the views back to upload more images, if any
                    uplayoutconditionmobility.setVisibility(View.GONE);
                    layoutconditionmobility.setVisibility(View.VISIBLE);
                    btngenralcondition.setText(R.string.add_new_btn);
                    openSuccessDialog(medicalHistoryResponse.getMessage());
                }else {
                    openDialog(medicalHistoryResponse.getMessage());
                }
//                Toast.makeText(this, medicalHistoryResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }else{
                openDialog(this.getString(R.string.errormsg));
//                Toast.makeText(this,R.string.errormsg, Toast.LENGTH_SHORT).show();
            }
            // Setting file as null so that if someone clicks on save for next view, then same image isn't uploaded
            file = null;
        });
    }

    public void addDrugAllergy(View view) {

       String drugDescription =  etdrug.getText().toString().trim();
       if ((drugDescription.isEmpty())){
           Toast.makeText(this,  getString(R.string.deserror), Toast.LENGTH_SHORT).show();
           return;
       }

        progress.show();

        AllergyViewModel viewModel = ViewModelProviders.of(this).get(AllergyViewModel.class);
        viewModel.init();
        viewModel.saveAllergy(token,ck_allergy_drug.getText().toString().trim(),drugDescription,"0");
        viewModel.getVolumesResponseLiveData().observe(this, allergyResponse -> {
            progress.hide();
             if(allergyResponse!=null){
                 if (allergyResponse.getSuccess()){
                     openSuccessDialog(allergyResponse.getMessage());
                 }else {
                     openDialog(allergyResponse.getMessage());
                 }
//                 Toast.makeText(this,  allergyResponse.getMessage(), Toast.LENGTH_SHORT).show();
             }else{
                 openDialog(this.getString(R.string.errormsg));
//                 Toast.makeText(this, getString(R.string.errormsg), Toast.LENGTH_SHORT).show();
             }
        });
    }

    public void addOtherAllergy(View view) {
        String drugDescription = etother.getText().toString().trim();
        if ((drugDescription.isEmpty())){
            Toast.makeText(this,  getString(R.string.deserror), Toast.LENGTH_SHORT).show();
            return;
        }

        progress.show();

        AllergyViewModel viewModel = ViewModelProviders.of(this).get(AllergyViewModel.class);
        viewModel.init();
        viewModel.saveAllergy(token,ck_allergy_other.getText().toString().trim(),drugDescription,"0");
        viewModel.getVolumesResponseLiveData().observe(this, allergyResponse -> {
            progress.hide();
            if(allergyResponse!=null){
                if (allergyResponse.getSuccess()){
                    openSuccessDialog(allergyResponse.getMessage());
                }else {
                    openDialog(allergyResponse.getMessage());
                }
//                 Toast.makeText(this,  allergyResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }else{
                openDialog(this.getString(R.string.errormsg));
//                 Toast.makeText(this, getString(R.string.errormsg), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addFoodAllergy(View view) {
        String drugDescription = etfood.getText().toString().trim();
        if ((drugDescription.isEmpty())){
            Toast.makeText(this,  getString(R.string.deserror), Toast.LENGTH_SHORT).show();
            return;
        }

        progress.show();

        AllergyViewModel viewModel = ViewModelProviders.of(this).get(AllergyViewModel.class);
        viewModel.init();
        viewModel.saveAllergy(token,ck_allergy_food.getText().toString().trim(),drugDescription,"0");
        viewModel.getVolumesResponseLiveData().observe(this, allergyResponse -> {
            progress.hide();
            if(allergyResponse!=null){
                if (allergyResponse.getSuccess()){
                    openSuccessDialog(allergyResponse.getMessage());
                }else {
                    openDialog(allergyResponse.getMessage());
                }
                //                 Toast.makeText(this,  allergyResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }else{
                openDialog(this.getString(R.string.errormsg));
//                 Toast.makeText(this, getString(R.string.errormsg), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.radioButton1 :
                layout_action.setVisibility(View.VISIBLE);
                break;
            case R.id.radioButton2:
                layout_action.setVisibility(View.GONE);
                break;
        }
    }

    public void backPressed(View view) {
        finish();
    }
}