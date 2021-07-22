package ensan.patientapp.view.activity.completeprofile.view;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.features.imageloader.DefaultImageLoader;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Helper;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.view.activity.completeprofile.adapter.DropDownProfileAdapter;
import ensan.patientapp.view.activity.completeprofile.viewmodel.SaveAddViewModel;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class CompleteProfileSecondActivity extends FragmentActivity implements AdapterView.OnItemSelectedListener, OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapClickListener {

    String[] country = new String[]{};
    String[] city = new String[]{};
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    double latitude, longitude;
    private String token;
    private FusedLocationProviderClient providerClient;
    private Location currentLocation;
    private File file;
    private List<Image> profileList = new ArrayList<>();
    private String language;

    @BindView(R.id.citySpinner)
    Spinner citySpinner;

    @BindView(R.id.countrySpinner)
    Spinner countrySpinner;

    @BindView(R.id.addressLayout)
    TextInputLayout addressLayout;

    @BindView(R.id.tagLayout)
    TextInputLayout tagLayout;

    @BindView(R.id.poBoxLayout)
    TextInputLayout poBoxLayout;

    @BindView(R.id.addressEt)
    TextInputEditText addressEt;


    @BindView(R.id.cityEt)
    TextInputEditText cityEt;

    @BindView(R.id.cityLayout)
    TextInputLayout cityLayout;

    TextInputEditText tagET;

    @BindView(R.id.poBoxEt)
    TextInputEditText poBoxEt;

    @BindView(R.id.btn_reset)
    TextView reset;

    @BindView(R.id.layout_upload_profile)
    RelativeLayout uploadProfilePic;

    @BindView(R.id.profileImage)
    ImageView profileImage;

    @BindView(R.id.img_delete)
    ImageView img_delete;

    @BindView(R.id.profileImageLayout)
    RelativeLayout profileImageLayout;


    @BindView(R.id.mapMarker)
    ImageView mapMarker;

    private Progress progress;
    private String cityS;
    private  String countryS;

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

        setContentView(R.layout.complete_profile_second_activity);
        ButterKnife.bind(this);

        tagET = findViewById(R.id.tagETF);

        city = getResources().getStringArray(R.array.city);
        country = getResources().getStringArray(R.array.country);

        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);

        DropDownProfileAdapter cityAdapter = new DropDownProfileAdapter(this, Arrays.asList(city));
        DropDownProfileAdapter countryAdapter = new DropDownProfileAdapter(this, Arrays.asList(country));
        countrySpinner.setAdapter(countryAdapter);
        citySpinner.setAdapter(cityAdapter);

        citySpinner.setOnItemSelectedListener(this);
        countrySpinner.setOnItemSelectedListener(this);
        uploadProfilePic.setOnClickListener(this);
        img_delete.setOnClickListener(this);

        providerClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();

        reset.setOnClickListener(v -> {
            providerClient = LocationServices.getFusedLocationProviderClient(this);
            mapMarker.setVisibility(View.VISIBLE);
            fetchLastLocation();
        });

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 cityS = city[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryS = country[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*check run time permission*/
        configureCameraIdle();
    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},100);
            return;
        }
        mapMarker.setVisibility(View.GONE);
        Task<Location> task = providerClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            currentLocation = location;
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            fetchAddress();
        });
    }

    public void backPressed(View view) {
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

    // next button clicked
    public void nextClick(View view) {
        String add = addressEt.getText().toString().trim();
        String tag = tagET.getText().toString().trim();
        String pBox = poBoxEt.getText().toString().trim();
        String cityName = cityEt.getText().toString().trim();

        if(add.isEmpty()){
            addressLayout.setError(getString(R.string.adderror));
            return;
        }else if(tag.isEmpty()){
            tagLayout.setError(getString(R.string.tagerror));
            return;
        }else if(pBox.isEmpty()){
            poBoxLayout.setError(getString(R.string.postalError));
            return;
        } else if(pBox.length() != 5){
            poBoxLayout.setError(getString(R.string.poboxerror));
            return;
        } else if (cityName.isEmpty()){
            cityLayout.setError(getString(R.string.cityerror));
            return;
        }else {
            progress.show();
            RequestBody addressRequest =  RequestBody.create(MediaType.parse("text/plain"), add);
            RequestBody pBoxRequest = RequestBody.create(MediaType.parse("text/plain"), pBox);
            RequestBody cityRequest = RequestBody.create(MediaType.parse("text/plain"), cityName);
            RequestBody countryRequest = RequestBody.create(MediaType.parse("text/plain"), "");
            RequestBody familyIdRequest = RequestBody.create(MediaType.parse("text/plain"), "0");
            RequestBody latRequest = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(latitude));
            RequestBody lngRequest = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(longitude));
            RequestBody tagRequest = RequestBody.create(MediaType.parse("text/plain"), tag);
            SaveAddViewModel viewModel = ViewModelProviders.of(this).get(SaveAddViewModel.class);
            viewModel.init();
            viewModel.saveAddress(token,addressRequest,pBoxRequest,cityRequest,countryRequest,familyIdRequest,latRequest,lngRequest,tagRequest);
            viewModel.getVolumesResponseLiveData().observe(this, profileResponse -> {
                progress.hide();
                if(profileResponse != null){
                    if(profileResponse.getSuccess()) {
                        Intent intent = new Intent(getApplicationContext(), CompleteProfileForthActivity.class);
                        intent.putExtra(Constants.KEY_TOKEN,token);
                        intent.putExtra(Constants.KEY_LANGUAGE,language);
                        startActivity(intent);
                        finish();
                    }else{
                        openDialog(profileResponse.getMessage());
                    }
                }else{
                    openDialog(this.getString(R.string.errormsg));
                }
            });
        }
    }

    private void fetchAddress() {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            if(currentLocation!=null) {
//                addMarker(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                if(addresses != null && addresses.size()>0) {
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String cityName = addresses.get(0).getAdminArea(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String postalCode = addresses.get(0).getPostalCode(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                    addressEt.setText(address);
                    poBoxEt.setText(postalCode);
                    boolean inCountry = false;
                    for (int i =0; i<city.length; i++){
                        if (city[i].matches(cityName)){
                            citySpinner.setSelection(i);
                            countrySpinner.setSelection(1);
                            inCountry = true;
                            break;
                        }
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMarker(double lat, double lng) {
        if (mMap!=null){
            mMap.clear();
            LatLng loc = new LatLng(lat, lng);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(loc);
            markerOptions.draggable(true);
            markerOptions.icon(Helper.getBitmapFromVector(this, R.drawable.ic_gps));
            mMap.addMarker(markerOptions);
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(
                    loc, 15);
            mMap.animateCamera(update);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        LatLng latLng = null;
        if(currentLocation == null){
            latLng = new LatLng(24.774265, 46.738586);
        }else{
            latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,20));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        mMap.setOnCameraIdleListener(onCameraIdleListener);
        if(currentLocation != null) {
            addMarker(currentLocation.getLatitude(), currentLocation.getLongitude());
        }
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapClickListener(this);
    }


    // get latitude and longitude
    private void configureCameraIdle() {
        onCameraIdleListener = () -> {
            LatLng latLng = mMap.getCameraPosition().target;
            latitude = latLng.latitude;
            longitude = latLng.longitude;
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_upload_profile:
                selectImages(1);
                break;
            case R.id.img_delete:
                /*uploadProfilePic.setVisibility(View.VISIBLE);
                profileImageLayout.setVisibility(View.GONE);*/
                break;
        }
    }


    private void selectImages(int limit) {

        ImagePicker imagePicker =   ImagePicker.create(CompleteProfileSecondActivity.this)
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
            profileList =  ImagePicker.getImages(data);
            uploadProfilePic.setVisibility(View.GONE);
            profileImageLayout.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(profileList.get(0).getUri())
                    .into(profileImage);
            return;
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        LatLng position = marker.getPosition();
        latitude = position.latitude;
        longitude = position.longitude;
        currentLocation.setLatitude(latitude);
        currentLocation.setLongitude(longitude);
        currentLocation.setLatitude(latitude);
        currentLocation.setLongitude(longitude);

        fetchAddress();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        latitude = latLng.latitude;
        longitude = latLng.longitude;
        if(currentLocation != null) {
            currentLocation.setLatitude(latitude);
            currentLocation.setLongitude(longitude);
            currentLocation.setLatitude(latitude);
            currentLocation.setLongitude(longitude);
        }
        mMap.clear();
        fetchAddress();
        addMarker(latitude,longitude);
    }


    public  void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}