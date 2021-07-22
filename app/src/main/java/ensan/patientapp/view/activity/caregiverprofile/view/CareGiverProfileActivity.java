package ensan.patientapp.view.activity.caregiverprofile.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.SuccessPopup;
import ensan.patientapp.view.activity.bookingdetails.viewmodel.SaveBookingViewModel;
import ensan.patientapp.view.activity.caregiverprofile.adapter.LanguageAdapter;
import ensan.patientapp.view.activity.caregiverprofile.adapter.ServicesAdapter;
import ensan.patientapp.view.activity.caregiverprofile.viewmodel.CareGiverProfileViewModel;
import ensan.patientapp.view.activity.requestsent.RequestSent;

import static android.view.View.GONE;

public class CareGiverProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private String token, careGiverId, bookingID, notes;
    private Progress progress;

    @BindView(R.id.roundedUserImageView)
    ImageView roundedUserImageView;

    @BindView(R.id.coverIV)
    ImageView coverIV;

    @BindView(R.id.userNameTV)
    TextView userNameTV;

    @BindView(R.id.userLocationTV)
    TextView userLocationTV;

    @BindView(R.id.priceTV)
    TextView priceTV;

    @BindView(R.id.expTV)
    TextView expTV;

    @BindView(R.id.userNationalityTV)
    TextView userNationalityTV;

    @BindView(R.id.caredForTV)
    TextView caredForTV;

    @BindView(R.id.userLanguagesRV)
    RecyclerView userLanguagesRV;

    @BindView(R.id.userServicesRV)
    RecyclerView userServicesRV;

    @BindView(R.id.ageTV)
    TextView ageTV;

    @BindView(R.id.aboutUserTV)
    TextView aboutUserTV;

    @BindView(R.id.et_notes)
    EditText et_notes;

    @BindView(R.id.btn_book)
    TextView btn_book;

    @BindView(R.id.userQualificationTV)
    TextView userQualificationTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_giver_profile);

        ButterKnife.bind(this);


        // get data from intent
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                token = (String) bundle.get(Constants.KEY_TOKEN);
                careGiverId = (String) bundle.get(Constants.KEY_CARE_GIVER_ID);
                bookingID = (String) bundle.get(Constants.KEY_BOOKING_ID);
                if(bundle.get(Constants.KEY_STATUS_CONFIRMED)!=null && bundle.get(Constants.KEY_STATUS_CONFIRMED).toString().equals("true")){
                    et_notes.setVisibility(GONE);
                    btn_book.setVisibility(GONE);
                }
            }
        }

        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);
        progress.show();

        btn_book.setOnClickListener(this);


        CareGiverProfileViewModel viewModel = ViewModelProviders.of(this).get(CareGiverProfileViewModel.class);
        viewModel.init();
        viewModel.getCareGiverProfile(token,careGiverId);
        viewModel.getVolumesResponseLiveData().observe(this, careGiverProfileResponse -> {
            progress.hide();
            if(careGiverProfileResponse!=null){
                Glide.with(this).load(careGiverProfileResponse.getData().getProfilePic()).into(roundedUserImageView);
                userNameTV.setText(careGiverProfileResponse.getData().getName());
                if(careGiverProfileResponse.getData().getAddress() != null)
                {
                    userLocationTV.setText(careGiverProfileResponse.getData().getAddress().getCity());
                }
                userNationalityTV.setText(careGiverProfileResponse.getData().getNationality());
                expTV.setText(careGiverProfileResponse.getData().getExperience());
                ageTV.setText(careGiverProfileResponse.getData().getAge());
                caredForTV.setText(getString(R.string.carefor) + careGiverProfileResponse.getData().getCared());
                aboutUserTV.setText(getString(R.string.abt) + careGiverProfileResponse.getData().getName());
                userQualificationTV.setText(careGiverProfileResponse.getData().getQualification());

                // set language adapter
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                userLanguagesRV.setLayoutManager(mLayoutManager);
                LanguageAdapter languageAdapter = new LanguageAdapter(this, careGiverProfileResponse.getData().getLangList());
                userLanguagesRV.setAdapter(languageAdapter);

                // set services adapter
                RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                userServicesRV.setLayoutManager(linearLayoutManager);
                ServicesAdapter servicesAdapter = new ServicesAdapter(this, careGiverProfileResponse.getData().getServiceList());
                userServicesRV.setAdapter(servicesAdapter);
            }else {
                openDialog(this.getString(R.string.errormsg));
//                Toast.makeText(this, getString(R.string.errormsg), Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(CareGiverProfileActivity.this, RequestSent.class));
                finish();
            }
        });
        mAlert.show();
    }


    public void backPressed(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_book:
                saveBooking();
        }
    }

    private void saveBooking() {
        notes = et_notes.getText().toString().trim();
        progress.show();
        SaveBookingViewModel viewModel = ViewModelProviders.of(this).get(SaveBookingViewModel.class);
        viewModel.init();
        viewModel.saveBooking(token,bookingID,careGiverId,notes);
        viewModel.getVolumesResponseLiveData().observe(this, saveBookingResponse -> {
            progress.hide();
            if(saveBookingResponse != null){
                if (saveBookingResponse.getSuccess()){
                    openSuccessDialog(saveBookingResponse.getMessage());
                }else {
                    openDialog(saveBookingResponse.getMessage());
                }
//                     Toast.makeText(this, saveBookingResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }else{
                openDialog(this.getString(R.string.errormsg));
//                     Toast.makeText(this, R.string.errormsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}