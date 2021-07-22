package ensan.patientapp.view.activity.paymentMethod.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.reflect.TypeToken;
import com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity;
import com.oppwa.mobile.connect.checkout.meta.CheckoutSettings;
import com.oppwa.mobile.connect.exception.PaymentError;
import com.oppwa.mobile.connect.provider.Connect;
import com.oppwa.mobile.connect.provider.Transaction;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.SuccessPopup;
import ensan.patientapp.Utils.Util;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.paymentMethod.adapter.PaymentMethodAdapter;
import ensan.patientapp.view.activity.paymentMethod.fragment.CardSelectFragment;
import ensan.patientapp.view.activity.paymentMethod.model.CardSelectionType;
import ensan.patientapp.view.activity.paymentMethod.model.CheckOutIdResponse;
import ensan.patientapp.view.activity.paymentMethod.model.Datum;
import ensan.patientapp.view.activity.paymentMethod.model.GetCardPosition;
import ensan.patientapp.view.activity.paymentMethod.model.VerifyDetailsResponse;
import ensan.patientapp.view.activity.paymentMethod.viewmodel.CheckoutIdViewModel;
import ensan.patientapp.view.activity.paymentMethod.viewmodel.GetSaveCardViewModel;
import ensan.patientapp.view.activity.paymentMethod.viewmodel.PaymentStatusViewModel;
import ensan.patientapp.view.activity.paymentMethod.viewmodel.PaymentViewModel;
import ensan.patientapp.view.activity.paymentMethod.viewmodel.VerifyCadDetailsViewModel;
import ensan.patientapp.view.activity.bookingdetails.viewmodel.SaveBookingViewModel;
import ensan.patientapp.view.activity.home.view.HomeActivity;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import ensan.patientapp.view.fragment.cancel.CancelNoteFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PaymentMethod extends AppCompatActivity implements GetCardPosition, CardSelectionType {

    private String token;
    private Progress progress;
    private RecyclerView cardRv;
    private String cardId;
    private String price;
    private String from;
    private String bookingId;
    private boolean isPay;
    private TextView paynow;
    private LinearLayout no_payment_layout;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String notes, careGiverId;
    private ShimmerFrameLayout shimmerFrameLayout;
    private String type;
    private List<Datum> datumPaymentCardList;
    private PaymentMethodAdapter paymentMethodAdapter;
    private String bookingFor;

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
            sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_BOOKING,MODE_PRIVATE);
        }

        setContentView(R.layout.activity_payment_method);
        cardRv =  findViewById(R.id.cardRv);
        paynow = findViewById(R.id.paynow);
        no_payment_layout = findViewById(R.id.no_payment_layout);
        shimmerFrameLayout = findViewById(R.id.shimmerFrameLayout);

            Intent intent = getIntent();
            if (intent != null) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    price = (String) bundle.get(Constants.KEY_AMOUNT);
                   //  price = "5.00000SAR";
                    from = (String) bundle.get(Constants.KEY_FROM);
                    bookingId = (String) bundle.get(Constants.KEY_BOOKING_ID);
                    notes = (String) bundle.get(Constants.KEY_NOTES);
                    careGiverId = (String) bundle.get(Constants.KEY_CARE_GIVER_ID);
                    bookingFor = (String) bundle.get(Constants.KEY_RE_BOOKING_FOR);
                    if(bookingId != null) {
                        editor = sharedPreferences.edit();
                        editor.putString(Constants.KEY_BOOKING_ID, bookingId);
                        editor.putString(Constants.KEY_NOTES, notes);
                        editor.putString(Constants.KEY_CARE_GIVER_ID, careGiverId);
                        editor.putString(Constants.KEY_RE_BOOKING_FOR, bookingFor);
                        editor.commit();
                    }
                    if(bookingId == null){
                        Uri uri =  intent.getData();
                        try {
                            String resourcePath =   new UrlQuerySanitizer(uri.toString()).getValue("resourcePath");
                            bookingId = sharedPreferences.getString(Constants.KEY_BOOKING_ID,"");
                            careGiverId = sharedPreferences.getString(Constants.KEY_CARE_GIVER_ID,"");
                            notes = sharedPreferences.getString(Constants.KEY_NOTES,"");
                            cardId = sharedPreferences.getString(Constants.KEY_CARD_TOKEN,"");
                            bookingFor = sharedPreferences.getString(Constants.KEY_RE_BOOKING_FOR,"");
                            getPaymentStatusApi(resourcePath, sharedPreferences.getString(Constants.KEY_BOOKING_ID,""),bookingFor);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        }

        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);
      //  progress.show();

        getCard();

    }

    private void getCard() {
        shimmerFrameLayout.startShimmerAnimation();
        GetSaveCardViewModel viewModel = ViewModelProviders.of(this).get(GetSaveCardViewModel.class);
        viewModel.init();
        viewModel.getSaveCard(token);
        viewModel.getVolumesResponseLiveData().observe(this, verifyDetailsResponse -> {
            shimmerFrameLayout.stopShimmerAnimation();
            shimmerFrameLayout.setVisibility(View.GONE);
            //   progress.hide();
            if(verifyDetailsResponse != null){
                if(verifyDetailsResponse.getSuccess()){
                    datumPaymentCardList =   verifyDetailsResponse.getData();
                    if(datumPaymentCardList.size() == 0){
                        no_payment_layout.setVisibility(View.VISIBLE);
                        cardRv.setVisibility(View.GONE);
                        paynow.setVisibility(View.GONE);
                    }else {
                        no_payment_layout.setVisibility(View.GONE);
                        cardRv.setVisibility(View.VISIBLE);
                        if(from == null){
                            paynow.setVisibility(View.GONE);
                        }else {
                            paynow.setVisibility(View.VISIBLE);
                        }
                    }
                    cardRv.setLayoutManager(new LinearLayoutManager(this));
                    paymentMethodAdapter = new PaymentMethodAdapter(this,datumPaymentCardList,this);
                    cardRv.setAdapter(paymentMethodAdapter);
                }
            }
        });
    }

    public void backPressed(View view) {
        finish();
    }

    public void addPayment(View view) {
        CardSelectFragment cancelNoteFragment = new CardSelectFragment(this);
        cancelNoteFragment.show(getSupportFragmentManager(),cancelNoteFragment.getTag());
    }

    private void callPaymentMethod(String type) {
        CheckoutIdViewModel checkoutIdViewModel = ViewModelProviders.of(this).get(CheckoutIdViewModel.class);
        checkoutIdViewModel.init();
        checkoutIdViewModel.getCheckoutId(token,type);
        checkoutIdViewModel.getVolumesResponseLiveData().observe(this, checkOutIdResponse -> {
            if(checkOutIdResponse != null){
                Set<String> paymentBrands = new LinkedHashSet<>();

                paymentBrands.add("VISA");
                paymentBrands.add("MASTER");
                paymentBrands.add("MADA");

                CheckoutSettings checkoutSettings = new CheckoutSettings(checkOutIdResponse.getId(), paymentBrands, Connect.ProviderMode.LIVE);
//                CheckoutSettings checkoutSettings = new CheckoutSettings(checkOutIdResponse.getId(), paymentBrands, Connect.ProviderMode.TEST);
                checkoutSettings.setShopperResultUrl("checkoutui" + "://callback");

                Intent intent = checkoutSettings.createCheckoutActivityIntent(this);
                startActivityForResult(intent, CheckoutActivity.REQUEST_CODE_CHECKOUT);

            }else {
                Toast.makeText(PaymentMethod.this, getString(R.string.errormsg), Toast.LENGTH_SHORT).show();
            }
        });
    }


   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case CheckoutActivity.RESULT_OK:
                //* transaction completed *//*
                Transaction transaction = data.getParcelableExtra(CheckoutActivity.CHECKOUT_RESULT_TRANSACTION);

                //* resource path if needed *//*
                String resourcePath = data.getStringExtra(CheckoutActivity.CHECKOUT_RESULT_RESOURCE_PATH);
                if(isPay) {
                    if(bookingId != null) {
                        getPaymentStatusApi(resourcePath, bookingId,bookingFor);
                    }
                }else {
                    getVerifyDetailsAPI(resourcePath,type);
                }
                break;
            case CheckoutActivity.RESULT_CANCELED:
            //    Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
                break;
            case CheckoutActivity.RESULT_ERROR:
                 PaymentError error = data.getParcelableExtra(CheckoutActivity.CHECKOUT_RESULT_ERROR);
            //    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }


    // call getVerify details API
    private void getVerifyDetailsAPI(String resourcePath, String cardType){
        progress.show();
        VerifyCadDetailsViewModel viewModel = ViewModelProviders.of(this).get(VerifyCadDetailsViewModel.class);
        viewModel.init();
        viewModel.verifyDetails(token,resourcePath,cardType);
        viewModel.getVolumesResponseLiveData().observe(this, verifyDetailsResponse -> {
                progress.hide();
                if(verifyDetailsResponse != null){
                    if(verifyDetailsResponse.getStatus()) {
                        openSuccessDialog(verifyDetailsResponse.getMessage());
                    }else {
                        openDialog(verifyDetailsResponse.getMessage());
                    }
                }else{
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
        });
        mAlert.show();
    }

    // success dialog
    private void openCardSuccessDialog(String msg){
        getCard();
        SuccessPopup mAlert = new SuccessPopup(this);
        mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert.setMessage(msg);
        mAlert.setOkButton(view -> {
            mAlert.dismiss();
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra(Constants.KEY_FROM,"");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish(); // if the activity running has it's own context
        });
        mAlert.show();
    }

    // success dialog
    private void openSuccessDialog(String msg){
        getCard();
        SuccessPopup mAlert = new SuccessPopup(this);
        mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert.setMessage(msg);
        mAlert.setOkButton(view -> {
            mAlert.dismiss();
        });
        mAlert.show();
    }

    // delete card function
    @Override
    public void cardPosition(int position) {
        progress.show();
       int cardId =  datumPaymentCardList.get(position).getId();
        // get payment checkout id
            ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
            Call<VerifyDetailsResponse> call = apiDataService.deletePaymentCard("application/json","Bearer "+token,String.valueOf(cardId));
            call.enqueue(new Callback<VerifyDetailsResponse>() {
                @Override
                public void onResponse(Call<VerifyDetailsResponse> call, Response<VerifyDetailsResponse> response) {
                    progress.hide();
                    if (response.body() != null) {
                        VerifyDetailsResponse detailsResponse =   response.body();
                         openSuccessDialog(detailsResponse.getMessage());
                         paymentMethodAdapter.deleteCard(datumPaymentCardList.get(position));
                    }else{
                        openDialog(getString(R.string.errormsg));
                    }
                }

                @Override
                public void onFailure(Call<VerifyDetailsResponse> call, Throwable t) {
                    progress.hide();
                    call.cancel();
                    openDialog(getString(R.string.errormsg));
                }
            });
        }


    @Override
    public void getCardId(String cardId) {
        this.cardId = cardId;
        if(editor != null) {
            editor.putString(Constants.KEY_CARD_TOKEN, cardId);
            editor.apply();
        }

    }

    public void payNow(View view) {
        //   String.format("%.2f", Float.parseFloat(price.substring(0,price.length()-3)))
        isPay = true;
        PaymentViewModel viewModel = ViewModelProviders.of(this).get(PaymentViewModel.class);
        viewModel.init();
        viewModel.doPayment(token,price,cardId,String.valueOf(bookingId));
        viewModel.getVolumesResponseLiveData().observe(this, getSaveCard -> {
            progress.hide();
            if(getSaveCard!=null){

                Set<String> paymentBrands = new LinkedHashSet<>();

                paymentBrands.add("VISA");
                paymentBrands.add("MASTER");
                paymentBrands.add("MADA");

                CheckoutSettings checkoutSettings = new CheckoutSettings(getSaveCard.getId(), paymentBrands, Connect.ProviderMode.LIVE);
//                CheckoutSettings checkoutSettings = new CheckoutSettings(getSaveCard.getId(), paymentBrands, Connect.ProviderMode.TEST);
                checkoutSettings.setShopperResultUrl("checkoutui" + "://callback");


                Intent intent = checkoutSettings.createCheckoutActivityIntent(this);
                startActivityForResult(intent, CheckoutActivity.REQUEST_CODE_CHECKOUT);

            }else {
                Toast.makeText(PaymentMethod.this, getString(R.string.errormsg), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void getPaymentStatusApi(String resourcePath, String bookingId, @NonNull String bookingFor){
        PaymentStatusViewModel viewModel = ViewModelProviders.of(this).get(PaymentStatusViewModel.class);
        viewModel.init();
        viewModel.getPaymentStatus(token,resourcePath,bookingId,cardId,bookingFor);
        viewModel.getVolumesResponseLiveData().observe(this, getPaymentStatus -> {
            progress.hide();
               if(getPaymentStatus != null){
                    if(getPaymentStatus.getSuccess()){
                        if(bookingFor.equals("0")) {
                            // call booking API
                            callBookingDetailsAPI(token, careGiverId, bookingId, notes);
                            Toast.makeText(this, getPaymentStatus.getMessage(), Toast.LENGTH_SHORT).show();
                        }else {
                            openCardSuccessDialog( getPaymentStatus.getMessage());
                        }

                    }else {
                        openDialog(getPaymentStatus.getMessage());
                    }
               }else {
                   openDialog(getString(R.string.errormsg));
               }
        });
    }

    public void btnContinue(View view) {
        CardSelectFragment cancelNoteFragment = new CardSelectFragment(this);
        cancelNoteFragment.show(getSupportFragmentManager(),cancelNoteFragment.getTag());
    }


    // call booking details API
    private void callBookingDetailsAPI(String token, String careGiverId, String bookingId, String notes){
      progress.show();
        SaveBookingViewModel viewModel = ViewModelProviders.of(this).get(SaveBookingViewModel.class);
        viewModel.init();
        viewModel.saveBooking(token,bookingId,careGiverId,notes);
        viewModel.getVolumesResponseLiveData().observe(this, saveBookingResponse -> {
                 progress.hide();
                 if(saveBookingResponse != null){
                      if (saveBookingResponse.getSuccess()){
                          openCardSuccessDialog(saveBookingResponse.getMessage());
                      }else {
                          openDialog(saveBookingResponse.getMessage());
                      }
                 }else{
                     openDialog(this.getString(R.string.errormsg));
                 }
        });
    }

    @Override
    public void cardType(@NotNull String type) {
        this.type = type;
        isPay = false;
        callPaymentMethod(type);
    }
}