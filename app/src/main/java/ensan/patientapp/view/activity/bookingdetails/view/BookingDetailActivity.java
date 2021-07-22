package ensan.patientapp.view.activity.bookingdetails.view;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.SuccessPopup;
import ensan.patientapp.Utils.Util;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.view.activity.bookingdetails.adapter.AdditionalServiceAdapter;
import ensan.patientapp.view.activity.bookingdetails.adapter.SubCatAdapter;
import ensan.patientapp.view.activity.bookingdetails.model.AddiitionalCharge;
import ensan.patientapp.view.activity.bookingdetails.model.SubCat;
import ensan.patientapp.view.activity.bookingdetails.viewmodel.BookingDetailViewModel;
import ensan.patientapp.view.activity.caregiverprofile.view.CareGiverProfileActivity;
import ensan.patientapp.view.activity.home.view.HomeActivity;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import ensan.patientapp.view.activity.paymentMethod.view.PaymentMethod;
import ensan.patientapp.view.fragment.booking.viewmodel.CancelBookingViewModel;
import ensan.patientapp.view.fragment.cancel.CancelDatum;
import ensan.patientapp.view.fragment.cancel.CancelNoteFragment;

public class BookingDetailActivity extends AppCompatActivity implements CancelDatum {

    private String token;
    private String bookingId;
    private Progress progress;
    private String careGiverId;
    private String from;
    private String status;
    private String total;

    @BindView(R.id.txt_cat)
    TextView txt_cat;

    @BindView(R.id.nameTV)
    TextView nameTV;

    @BindView(R.id.txt_price)
    TextView txt_price;

    @BindView(R.id.txt_date)
    TextView txt_date;

    @BindView(R.id.recycle_additional)
    RecyclerView recycle_additional;

    @BindView(R.id.txt_pay)
    TextView txt_pay;

    @BindView(R.id.price_total)
    TextView price_total;

    @BindView(R.id.layout_total)
    LinearLayout layout_total;

    @BindView(R.id.ServicesRv)
    RecyclerView ServicesRv;

    @BindView(R.id.usrImg)
    CircleImageView usrImg;

    @BindView(R.id.clickToPayButton)
    TextView clickToPayButton;

    @BindView(R.id.extraChargeLayout)
    RelativeLayout extraChargeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get Token
        LoginResponse resp = (LoginResponse) Util.getInstance(this).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginResponse>() {
                });

        if(resp != null) {
            String language = resp.getData().getLanaguage();
            Utility.setLocale(this, language);
        }

        setContentView(R.layout.booking_detail_activity);

        ButterKnife.bind(this);

        // get data from intent
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                token = (String) bundle.get(Constants.KEY_TOKEN);
                bookingId = (String) bundle.get(Constants.KEY_BOOKING_ID);
                from = (String) bundle.get(Constants.KEY_FROM);
                status = (String) bundle.get(Constants.STATUS_CODE);
            }
        }

        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);
        progress.show();

        if(from != null) {
            if (from.equals("past")) {
                txt_pay.setVisibility(View.GONE);
            }
        }

        if(status != null){
             if(status.equals("2") || status.equals("4")|| status.equals("5")){
                 txt_pay.setVisibility(View.GONE);
             }
        }

        // status 7 on the way
        getBookingDetails();

    }

    // open dialog
    private void openDialog(String msg){

        AlertPopup mAlert = new AlertPopup(this);
        mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert.setMessage(msg);
        mAlert.setOkButton(view -> mAlert.dismiss());
        mAlert.show();

    }

    private void getBookingDetails() {
        BookingDetailViewModel viewModel = ViewModelProviders.of(this).get(BookingDetailViewModel.class);
        viewModel.init();
        viewModel.getBookingDetail(token,bookingId);
        viewModel.getVolumesResponseLiveData().observe(this, bookingDetailsResponse -> {
              progress.hide();
              if(bookingDetailsResponse != null){
                  Glide.with(this).load(bookingDetailsResponse.getData().getProfilePic()).into(usrImg);
                  careGiverId = bookingDetailsResponse.getData().getCareGiverId().toString();
                  total = bookingDetailsResponse.getData().getTotal();
                  txt_cat.setText(bookingDetailsResponse.getData().getCat());
                  nameTV.setText(bookingDetailsResponse.getData().getName());
                  txt_price.setText(bookingDetailsResponse.getData().getPrice());
                  txt_date.setText(bookingDetailsResponse.getData().getDate());
                  recycle_additional.setLayoutManager(new LinearLayoutManager(BookingDetailActivity.this));
                  ServicesRv.setLayoutManager(new LinearLayoutManager(BookingDetailActivity.this));
                  price_total.setText(bookingDetailsResponse.getData().getTotal());
                  List<AddiitionalCharge> chargeList = bookingDetailsResponse.getData().getAddiitionalCharges();
                  List<SubCat> subCatList = bookingDetailsResponse.getData().getSubCat();
                 /* if(chargeList.size() == 0){
                      txt_pay.setVisibility(View.GONE);
                      layout_total.setVisibility(View.GONE);
                  }*/
                  int pay = bookingDetailsResponse.getData().getExtraPayment();
                  if(pay == 0){
                      clickToPayButton.setText(R.string.know_paid);
                      clickToPayButton.setClickable(false);
                  }else if(pay == 2){
                      clickToPayButton.setVisibility(View.INVISIBLE);
                  }

                  if(chargeList.size() == 0){
                      extraChargeLayout.setVisibility(View.GONE);
                      layout_total.setVisibility(View.GONE);
                  }else {
                      extraChargeLayout.setVisibility(View.VISIBLE);
                      layout_total.setVisibility(View.VISIBLE);
                  }
                  AdditionalServiceAdapter bookingAdapter = new AdditionalServiceAdapter(this,chargeList);
                  recycle_additional.setAdapter(bookingAdapter);

                  SubCatAdapter subCatAdapter = new SubCatAdapter(this,subCatList);
                  ServicesRv.setAdapter(subCatAdapter);
              }else{
                  openDialog(this.getString(R.string.errormsg));
              }
        });

    }

    public void backPressed(View view) {
        finish();
    }

    public void viewProfile(View view) {
        Intent intent = new Intent(this, CareGiverProfileActivity.class);
        intent.putExtra(Constants.KEY_TOKEN, token);
        intent.putExtra(Constants.KEY_CARE_GIVER_ID,careGiverId);
        intent.putExtra(Constants.KEY_BOOKING_ID,String.valueOf(bookingId));
        startActivity(intent);
    }

    public void cancelBooking(View view) {
        CancelNoteFragment cancelNoteFragment = new CancelNoteFragment(this,this);
        cancelNoteFragment.show(this.getSupportFragmentManager(),cancelNoteFragment.getTag());
    }

    @Override
    public void notes(String note) {
        progress.show();
        CancelBookingViewModel viewModel = ViewModelProviders.of(this).get(CancelBookingViewModel.class);
        viewModel.init();
        viewModel.cancelBooking(token,bookingId,note);
        viewModel.getVolumesResponseLiveData().observe(this, cancelBookingResponse -> {
            progress.hide();
            if(cancelBookingResponse != null){
                if(cancelBookingResponse.getSuccess()) {
                    openSuccessDialog(cancelBookingResponse.getMessage());
                }else {
                    openDialog(cancelBookingResponse.getMessage());
                }
            }
        });
    }


    // open success dialog
    private void openSuccessDialog(String msg){

        SuccessPopup mAlert = new SuccessPopup(this);
        mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert.setMessage(msg);
        mAlert.setOkButton(view -> {
            mAlert.dismiss();
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });
        mAlert.show();
    }

    public void clickToPay(View view) {
        Intent intent = new Intent(this, PaymentMethod.class);
        intent.putExtra(Constants.KEY_CARE_GIVER_ID,careGiverId);
        intent.putExtra(Constants.KEY_AMOUNT,total);
        intent.putExtra(Constants.KEY_NOTES,"");
        intent.putExtra(Constants.KEY_BOOKING_ID,bookingId);
        intent.putExtra(Constants.KEY_RE_BOOKING_FOR,"1");
        intent.putExtra(Constants.KEY_FROM,"booking");
        finish();
        startActivity(intent);
    }
}