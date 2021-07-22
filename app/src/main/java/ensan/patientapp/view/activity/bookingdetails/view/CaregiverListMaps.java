package ensan.patientapp.view.activity.bookingdetails.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.gson.reflect.TypeToken;

import org.parceler.Parcels;

import java.util.ArrayList;
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
import ensan.patientapp.view.activity.paymentMethod.view.PaymentMethod;
import ensan.patientapp.view.activity.bookingdetails.model.CareGiver;
import ensan.patientapp.view.activity.caregiverprofile.view.CareGiverProfileActivity;
import ensan.patientapp.view.activity.login.model.LoginResponse;

public class CaregiverListMaps extends FragmentActivity implements OnMapReadyCallback , GoogleMap.OnMarkerClickListener {


    private String bookingId;
    private String token;
    private Progress progress;
    private GoogleMap mMap;
    List<CareGiver> careGivers = new ArrayList<>();
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    private String language;
    private LoginResponse resp;
    private String price;


    @BindView(R.id.caregiver)
    LinearLayout caregiver;

    @BindView(R.id.userImg)
    CircleImageView userImage;

    @BindView(R.id.userName)
    TextView userName;

//    @BindView(R.id.currency)
//    TextView currency;

    @BindView(R.id.cdistance)
    TextView cdistance;

    @BindView(R.id.city)
    TextView city;

    @BindView(R.id.booknow)
    TextView bookNow;

    @BindView(R.id.viewProfile)
    TextView viewProfile;

    private FusedLocationProviderClient providerClient;
    private Location currentLocation;

    private String caregiverID;
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

        // get data from intent
        Intent mIntent = getIntent();
        if(mIntent!= null) {
            bookingId = mIntent.getStringExtra(Constants.KEY_BOOKING_ID);
            token = mIntent.getStringExtra(Constants.KEY_TOKEN);
            careGivers = Parcels.unwrap(mIntent.getParcelableExtra(Constants.KEY_CAREGIVER_LIST));
        }

        setContentView(R.layout.activity_caregiver_list_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fetchLastLocation();

        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);

        ButterKnife.bind(this);

        // set white color in rating bar
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
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

    public void backPressed(View view) {
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // initialize google map
        mMap = googleMap;

        RequestOptions requestOptions=   new RequestOptions().bitmapTransform(new RoundedCorners(14));
        for (int i = 0; i < careGivers.size(); i++) {
            if (!(careGivers.get(i).getLongitude().equals(""))) {
                 // set marker on different lat lng
                try {

                    Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(careGivers.get(i).getLatitude()), Double.parseDouble(careGivers.get(i).getLongitude()))).title(careGivers.get(i).getName()));

                    Glide.with(this).asBitmap().override(100,100).placeholder(R.drawable.user)
                            .load(careGivers.get(i).getProfilePic()) .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                            .apply(requestOptions)
                            .into(new CustomTarget<Bitmap>() {

                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition)
                                {
                                    Log.e("onResourceReady","loaded");
                                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(resource, 100, 100, false)));
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }

                            });
                    marker.setTag(careGivers.get(i).getId());
                    LatLng mrker = new LatLng(Double.parseDouble(careGivers.get(i).getLatitude()), Double.parseDouble(careGivers.get(i).getLongitude()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(mrker));
                    mMap.setOnMarkerClickListener(this);
                } catch (Exception e) {
                    Log.e("MAP Error","Caregiver:" + careGivers.get(i).getId() + " Latitude:" + careGivers.get(i).getLatitude() + " Longitude:" + careGivers.get(i).getLongitude());
                }

            }
        }
        LatLng latLng = null;
        if(currentLocation == null){
            latLng = new LatLng(24.774265, 46.738586);
        }else{
            latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,20));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},100);
            return;
        }

        if(providerClient != null) {
            Task<Location> task = providerClient.getLastLocation();
            task.addOnSuccessListener(location -> {
                currentLocation = location;
            });
        }
    }


    @Override
    public boolean onMarkerClick(Marker m) {
        caregiver.setVisibility(View.VISIBLE);

        for (int i = 0; i < careGivers.size(); i++) {
            if (m.getTag().toString().equals(careGivers.get(i).getId().toString())) {
                price = careGivers.get(i).getPrice();
                caregiverID = careGivers.get(i).getId().toString();
                userName.setText(careGivers.get(i).getName());
                ratingBar.setRating(Float.parseFloat(careGivers.get(i).getRating()));
                cdistance.setText(careGivers.get(i).getDistance().toString()+" "+getString(R.string.km));
                city.setText(careGivers.get(i).getCity());
                Glide.with(getApplicationContext())
                        .load(careGivers.get(i).getProfilePic()).placeholder(R.drawable.users)
                        .into(userImage);


            }

        }

        // book now button click based on marker value
        bookNow.setOnClickListener(view -> {
            Intent intent = new Intent(this, PaymentMethod.class);
            intent.putExtra(Constants.KEY_CARE_GIVER_ID,caregiverID);
            intent.putExtra(Constants.KEY_AMOUNT,price);
            intent.putExtra(Constants.KEY_NOTES,"notes");
            intent.putExtra(Constants.KEY_BOOKING_ID,bookingId);
            intent.putExtra(Constants.KEY_FROM,"booking");
            startActivity(intent);
        });

        // view profile button based on maker value
        viewProfile.setOnClickListener(view -> {
            for (int i = 0; i < careGivers.size(); i++) {
                if (m.getTag().toString().equals(careGivers.get(i).getId().toString())) {

                    int careGiverId = careGivers.get(i).getId();
                    String id = String.valueOf(careGiverId);
                    Intent intent = new Intent(getApplicationContext(), CareGiverProfileActivity.class);
                    intent.putExtra(Constants.KEY_TOKEN, token);
                    intent.putExtra(Constants.KEY_CARE_GIVER_ID,id);
                    startActivity(intent);

                }
            }
        });
        return true;
    }
}