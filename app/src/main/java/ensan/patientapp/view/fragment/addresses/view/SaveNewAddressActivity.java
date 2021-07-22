package ensan.patientapp.view.fragment.addresses.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

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
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ensan.patientapp.R;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Helper;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.Util;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.view.activity.completeprofile.adapter.DropDownProfileAdapter;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import ensan.patientapp.view.fragment.addresses.viewmodel.SaveNewAddressViewModel;

public class SaveNewAddressActivity extends AppCompatActivity implements OnMapReadyCallback
        , View.OnClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapClickListener{


    @BindView(R.id.saveAddress)
    Button saveAddress;


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


    TextInputEditText tagET;

    @BindView(R.id.poBoxEt)
    TextInputEditText poBoxEt;

    @BindView(R.id.btn_reset)
    TextView reset;

    @BindView(R.id.mapMarker)
    ImageView mapMarker;

    @BindView(R.id.neighbourLayout)
    TextInputLayout neighbourLayout;

    @BindView(R.id.neighbourEt)
    TextInputEditText neighbourEt;

    @BindView(R.id.cityEt)
    TextInputEditText cityEt;

    @BindView(R.id.cityLayout)
    TextInputLayout cityLayout;

    private FusedLocationProviderClient providerClient;
    SupportMapFragment mapFragment;
    private Location currentLocation;
    double latitude, longitude;
    private Location mLastLocation;
    private GoogleMap mMap;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    String[] country = new String[]{};
    String[] city =  new String[]{};
    private LoginResponse resp;
    private Progress progress;
    private String cityS;
    private String countryS;
    String token;
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
        }

        setContentView(R.layout.activity_save_new_address);

        ButterKnife.bind(this);

        country =  getResources().getStringArray(R.array.countryname);
        city = getResources().getStringArray(R.array.city);

        tagET = findViewById(R.id.tagETF);

        // initialize progress dialog instance
        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        DropDownProfileAdapter cityAdapter = new DropDownProfileAdapter(this, Arrays.asList(city));
        DropDownProfileAdapter countryAdapter = new DropDownProfileAdapter(this, Arrays.asList(country));
        countrySpinner.setAdapter(countryAdapter);
        citySpinner.setAdapter(cityAdapter);
        providerClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
        configureCameraIdle();
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

        resp = (LoginResponse) Util.getInstance(getApplicationContext()).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginResponse>() {
                });

        if(resp != null){
            token = resp.getData().getToken();
        }
        saveAddress.setOnClickListener(this);
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
            mLastLocation = location;
            fetchAddress();
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        });
    }

    public void closeLayout(View view){
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveAddress:

                saveAddress();
        }
    }

    private void saveAddress() {

        String add = addressEt.getText().toString().trim();
        String tag = tagET.getText().toString().trim();
        String pBox = poBoxEt.getText().toString().trim();
        String landMark = neighbourEt.getText().toString().trim();
        String cityName = cityEt.getText().toString().trim();

        if(add.isEmpty()){
            addressLayout.setError(getString(R.string.adderror));
            return;
        } else if(tag.isEmpty()){
            tagLayout.setError(getString(R.string.tagerror));
            return;
        } else if(pBox.isEmpty()){
           poBoxLayout.setError(getString(R.string.pboxerror));
       } else if(pBox.length() != 5){
            poBoxLayout.setError(getString(R.string.poboxerror));
            return;
        }if (cityName.isEmpty()){
            cityLayout.setError(getString(R.string.cityerror));
            return;
        } else
            {
            progress.show();
            SaveNewAddressViewModel viewModel = ViewModelProviders.of(this).get(SaveNewAddressViewModel.class);

            viewModel.init();
            viewModel.saveAddress(token,"0",add,pBox,cityName,"",String.valueOf(latitude),String.valueOf(longitude),"","0","1", tag);
            viewModel.getVolumesResponseLiveData().observe(this, addressResponse -> {

                progress.hide();
                if(addressResponse != null){
                    Toast.makeText(this, addressResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(this, getString(R.string.errormsg), Toast.LENGTH_SHORT).show();
                }


//                    Toast.makeText(this, R.string.errormsg, Toast.LENGTH_SHORT).show();

            });
        }

    }

    @Override
    public void onMapClick(LatLng latLng) {
        latitude = latLng.latitude;
        longitude = latLng.longitude;
        mLastLocation.setLatitude(latitude);
        mLastLocation.setLongitude(longitude);
        currentLocation.setLatitude(latitude);
        currentLocation.setLongitude(longitude);
        mMap.clear();
        fetchAddress();
        addMarker(latitude,longitude);
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
        mLastLocation.setLatitude(latitude);
        mLastLocation.setLongitude(longitude);
        currentLocation.setLatitude(latitude);
        currentLocation.setLongitude(longitude);
        fetchAddress();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        //mMap.setMyLocationEnabled(true);
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


        if(currentLocation !=null) {
            addMarker(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.setOnMarkerDragListener(this);
            mMap.setOnMapClickListener(this);
        }

    }

    private void fetchAddress() {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            if(mLastLocation!=null) {
//                addMarker(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String cityName = addresses.get(0).getAdminArea(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String postalCode = addresses.get(0).getPostalCode(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                addressEt.setText(address);
                //poBoxEt.setText(postalCode);


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

    private void configureCameraIdle() {
        onCameraIdleListener = () -> {
            LatLng latLng = mMap.getCameraPosition().target;
            this.latitude = latLng.latitude;
            this.longitude = latLng.longitude;
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchLastLocation();
    }
}