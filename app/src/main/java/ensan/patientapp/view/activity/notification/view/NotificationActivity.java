package ensan.patientapp.view.activity.notification.view;

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

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.Util;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.view.activity.bookingdetails.view.BookingDetailActivity;
import ensan.patientapp.view.activity.home.view.HomeActivity;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import ensan.patientapp.view.activity.notification.adapter.NotificationAdapter;
import ensan.patientapp.view.activity.notification.model.Datum;
import ensan.patientapp.view.activity.notification.model.NotificationPosition;
import ensan.patientapp.view.activity.notification.viewmodel.NotificationViewModel;
import ensan.patientapp.view.activity.notification.viewmodel.ReadNotificationViewModel;

public class NotificationActivity extends AppCompatActivity implements NotificationPosition {

    private Progress progress;
    private String token;
    private LoginResponse resp;
    private List<Datum> datumArrayList;
    private NotificationAdapter notificationAdapter;

    @BindView(R.id.notificationRv)
    RecyclerView notificationRv;

    @BindView(R.id.notificationLayout)
    LinearLayout notificationLayout;

    @BindView(R.id.no_notification_layout)
    LinearLayout no_notification_layout;

    @BindView(R.id.shimmerFrameLayout)
    ShimmerFrameLayout shimmerFrameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get Token
        resp = (LoginResponse) Util.getInstance(this).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginResponse>() {
                });

        if(resp != null){
            token =  resp.getData().getToken();
            Utility.setLocale(this,resp.getData().getLanaguage());
        }

        setContentView(R.layout.notification_activity);

        ButterKnife.bind(this);

        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);
        shimmerFrameLayout.startShimmerAnimation();
        callNotificationApi(token);
    }

    private void callNotificationApi(String token) {
       // progress.show();
        NotificationViewModel viewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);
        viewModel.init();
        viewModel.getNotification(token);
        viewModel.getVolumesResponseLiveData().observe(this, notificationResponse -> {
            progress.hide();
            if(notificationResponse != null){
                if(notificationResponse.getSuccess()){
                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    datumArrayList = notificationResponse.getData();
                    notificationRv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
                    if(datumArrayList.size() == 0){
                        no_notification_layout.setVisibility(View.VISIBLE);
                        notificationLayout.setVisibility(View.GONE);
                    }else {
                        notificationLayout.setVisibility(View.VISIBLE);
                        notificationRv.setVisibility(View.VISIBLE);
                    }
                    notificationAdapter  = new NotificationAdapter(this,datumArrayList,this);
                    notificationRv.setAdapter(notificationAdapter);

                }else{
                    openDialog(notificationResponse.getMessage());
                }
            }else{
                openDialog(this.getString(R.string.errormsg));
            }
        });
    }

    public void backPressed(View view) {
        finish();
    }


    // open dialog
    private void openDialog(String msg){

        AlertPopup mAlert = new AlertPopup(this);
        mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert.setMessage(msg);
        mAlert.setOkButton(view -> {
            mAlert.dismiss();
            //Do want you want
        });
        mAlert.show();
    }

    @Override
    public void notificationPosition(int position) {
        progress.show();
        String bookingId = datumArrayList.get(position).getBooking_id();
        String status = datumArrayList.get(position).getBooking_status();
        ReadNotificationViewModel viewModel = ViewModelProviders.of(this).get(ReadNotificationViewModel.class);
        viewModel.init();
        viewModel.readNotification(token,datumArrayList.get(position).getId());
        viewModel.getVolumesResponseLiveData().observe(this, notificationResponse -> {
            progress.hide();
               if(notificationResponse != null){
                    if(notificationResponse.getSuccess()){
                        if(!bookingId.equals("0")) {
                            Intent intent = new Intent(NotificationActivity.this, BookingDetailActivity.class);
                            intent.putExtra(Constants.KEY_TOKEN, token);
                            intent.putExtra(Constants.KEY_BOOKING_ID, bookingId);
                            intent.putExtra(Constants.STATUS_CODE, status);
                            startActivity(intent);
                        }
                        notificationAdapter.removeNotification(datumArrayList.get(position));
                    }else{
                        openDialog(notificationResponse.getMessage());
                    }
               }else {
                   openDialog(getString(R.string.errormsg));
               }
        });
    }

    public void btnContinue(View view) {
        finish();
    }
}