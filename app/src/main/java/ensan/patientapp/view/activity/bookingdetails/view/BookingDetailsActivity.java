package ensan.patientapp.view.activity.bookingdetails.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.SuccessPopup;
import ensan.patientapp.Utils.Util;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.view.activity.bookingdetails.viewmodel.SaveBookingViewModel;
import ensan.patientapp.view.activity.paymentMethod.view.PaymentMethod;
import ensan.patientapp.view.activity.bookingdetails.adapter.BookingAdapter;
import ensan.patientapp.view.activity.bookingdetails.model.CareGiver;
import ensan.patientapp.view.activity.bookingdetails.model.GetListPosition;
import ensan.patientapp.view.activity.bookingdetails.model.ViewProfilePosition;
import ensan.patientapp.view.activity.bookingdetails.viewmodel.BookingDetailsViewModel;
import ensan.patientapp.view.activity.caregiverprofile.view.CareGiverProfileActivity;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import ensan.patientapp.view.activity.requestsent.RequestSent;

public class BookingDetailsActivity extends AppCompatActivity implements GetListPosition, ViewProfilePosition {

    private String token, freqId, lngId, subCatId, dateFrom, dateTo, keyTime, familyId, bookingFor;
    private int catId;
    private JSONArray subCatIdArray;
    private List<CareGiver> careGiverList = new ArrayList<>();
    private String bookingId;
    private Progress progress;
    private LoginResponse resp;
    private String language;

    @BindView(R.id.booking_recycle)
    RecyclerView careListRecycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get Token
        resp = (LoginResponse) Util.getInstance(this).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginResponse>() {
                });

        if(resp != null){
            language = resp.getData().getLanaguage();
            Utility.setLocale(this,language);
        }

        setContentView(R.layout.booking_details_activity);

        ButterKnife.bind(this);

        // get data from intent
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                token = (String) bundle.get(Constants.KEY_TOKEN);
                freqId = (String) bundle.get(Constants.KEY_FREQUENCY_ID);
                lngId = (String) bundle.get(Constants.KEY_LANGUAGE_ID);
                catId = (Integer) bundle.get(Constants.KEY_CAT_ID);
                subCatId = (String) bundle.get(Constants.KEY_SUB_CAT_ID);
                try {
                    subCatIdArray = new JSONArray(subCatId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dateFrom = (String) bundle.get(Constants.KEY_FROM_DATE);
                dateTo = (String) bundle.get(Constants.KEY_TO_DATE);
                keyTime = (String) bundle.get(Constants.KEY_TIME);
                familyId = (String) bundle.get(Constants.KEY_FAMILY_MEMBER_ID);
                bookingFor = (String) bundle.get(Constants.KEY_BOOKING_FOR);
            }
        }

        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);
        progress.show();

        BookingDetailsViewModel viewModel = ViewModelProviders.of(this).get(BookingDetailsViewModel.class);
        viewModel.init();
        viewModel.getCareGiverList(token,bookingFor,String.valueOf(catId),subCatIdArray.toString(),dateFrom,dateTo,keyTime,freqId,lngId,familyId);
        viewModel.getVolumesResponseLiveData().observe(this, careGiverListResponse -> {
            progress.hide();
            if(careGiverListResponse != null) {
                if (careGiverListResponse.getSuccess()) {
                    bookingId = String.valueOf(careGiverListResponse.getData().getBookingId());
                    careGiverList = careGiverListResponse.getData().getCareGivers();
                    careListRecycle.setLayoutManager(new LinearLayoutManager(BookingDetailsActivity.this));
                    BookingAdapter bookingAdapter = new BookingAdapter(careGiverList, BookingDetailsActivity.this, BookingDetailsActivity.this, this);
                    careListRecycle.setAdapter(bookingAdapter);
                    if(careGiverList.size() == 0){
                        openDialog(getString(R.string.no_care_giver_found));
                    }
                }else {
                    openDialog(careGiverListResponse.getMessage());
                }
            }else {
                openDialog(getString(R.string.errormsg));
            }
        });

    }


    // open dialog
    private void openDialog(String msg){

        AlertPopup mAlert = new AlertPopup(this);
        mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert.setMessage(msg);
        mAlert.setCancelable(false);
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
                startActivity(new Intent(BookingDetailsActivity.this,RequestSent.class));
                finish();
            }
        });
        mAlert.show();
    }

    public void backPressed(View view) {
        finish();
    }


    public void openMap(View view){

        enableRunTimePermission();

    }

    // enable run time permission
    private void enableRunTimePermission(){
        Dexter.withContext(this).withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        Intent i = new Intent(BookingDetailsActivity.this, CaregiverListMaps.class);
                        i.putExtra(Constants.KEY_TOKEN, token);
                        i.putExtra(Constants.KEY_BOOKING_ID,bookingId);
                        i.putExtra(Constants.KEY_CAREGIVER_LIST,Parcels.wrap(careGiverList));
                        startActivity(i);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public void getCareGiverPosition(int position, String notes) {
       // progress.show();
       long id = careGiverList.get(position).getId();
       String price = careGiverList.get(position).getPrice();
       Intent intent = new Intent(this, PaymentMethod.class);
       intent.putExtra(Constants.KEY_CARE_GIVER_ID,String.valueOf(id));
       intent.putExtra(Constants.KEY_AMOUNT,price);
       intent.putExtra(Constants.KEY_NOTES,notes);
       intent.putExtra(Constants.KEY_BOOKING_ID,bookingId);
       intent.putExtra(Constants.KEY_RE_BOOKING_FOR,"0");
       intent.putExtra(Constants.KEY_FROM,"booking");
       startActivity(intent);
       /* int careGiverId = careGiverList.get(position).getId();
        SaveBookingViewModel viewModel = ViewModelProviders.of(this).get(SaveBookingViewModel.class);
        viewModel.init();
        viewModel.saveBooking(token,String.valueOf(bookingId),String.valueOf(careGiverId),notes);
        viewModel.getVolumesResponseLiveData().observe(this, saveBookingResponse -> {
            progress.hide();
            if(saveBookingResponse != null){
                startActivity(new Intent(this,RequestSent.class));
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
        });*/
    }

    @Override
    public void getProfilePosition(int position) {
        long careGiverId = careGiverList.get(position).getId();
        String id = String.valueOf(careGiverId);
        Intent intent = new Intent(this, CareGiverProfileActivity.class);
        intent.putExtra(Constants.KEY_TOKEN, token);
        intent.putExtra(Constants.KEY_CARE_GIVER_ID,id);
        intent.putExtra(Constants.KEY_BOOKING_ID,bookingId);
        startActivity(intent);
    }
}