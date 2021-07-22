package ensan.patientapp.view.fragment.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.Util;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.databinding.HomeFragmentBinding;
import ensan.patientapp.view.activity.doctorvisit.DoctorVisitActivity;
import ensan.patientapp.R;
import ensan.patientapp.view.activity.home.adapter.ServiceListAdapter;
import ensan.patientapp.view.activity.home.model.Datum;
import ensan.patientapp.view.activity.home.model.GetCategoryPosition;
import ensan.patientapp.view.activity.home.viewmodel.GetCategoryViewModel;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import ensan.patientapp.view.activity.notification.view.NotificationActivity;
import ensan.patientapp.view.fragment.booking.model.GetCallPosition;

import static ensan.patientapp.view.activity.home.view.HomeActivity.homeActivityBinding;


public class HomeFragment extends Fragment implements View.OnClickListener, GetCategoryPosition, GetCallPosition {


    HomeFragmentBinding homeFragmentBinding;
    private View view;
    private LoginResponse resp;
    private CircleImageView circleImageView;
    private TextView txt_username, txt_version;
    private String token;
    private List<Datum>  datumArrayList = new ArrayList<>();
    private  ServiceListAdapter serviceListAdapter;
    private Progress progress;
    private String language;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // get Token
        resp = (LoginResponse) Util.getInstance(getActivity()).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginResponse>() {
                });

        if(resp != null){
            language = resp.getData().getLanaguage();
            Utility.setLocale(getActivity(),language);
        }

        // Inflate the layout for this fragment
        homeFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);
        circleImageView = homeActivityBinding.drawer.findViewById(R.id.userImage);
        txt_username = homeActivityBinding.drawer.findViewById(R.id.txt_username);
        txt_version = homeActivityBinding.drawer.findViewById(R.id.txt_version);

        view = homeFragmentBinding.getRoot();

        // initialize progress dialog instance
        progress = new Progress(getContext());
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);


        if(resp != null){
            token = resp.getData().getToken();
            Glide.with(this).load(resp.getData().getProfilePic()).into(circleImageView);
            txt_username.setText(resp.getData().getFirstName() +" "+resp.getData().getLastName());
            homeFragmentBinding.txtUsername.setText(resp.getData().getFirstName() +" "+resp.getData().getLastName());
            language = resp.getData().getLanaguage();

        }

        homeFragmentBinding.locMenu.setOnClickListener(this);
        homeActivityBinding.drawerLayout.setScrimColor(Color.TRANSPARENT);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), homeActivityBinding.drawerLayout, R.string.open, R.string.close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;

                // If language comes null then choosing english as default language
                if (language==null){
                    language = "en";
                    Utility.setLocale(getActivity(),language);
                }

                if(language.equals("ar")) {
                    homeActivityBinding.content.setTranslationX(-slideX);
                }else {
                    homeActivityBinding.content.setTranslationX(slideX);
                }
             //   homeActivityBinding.content.setTranslationX(slideX);
                float scaleFactor = 6f;
                homeActivityBinding.content.setScaleX(1 - (slideOffset / scaleFactor));
                homeActivityBinding.content.setScaleY(1 - (slideOffset / scaleFactor));
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                Toast.makeText(getActivity(), item+"", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            }
        };

        // set version name
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = pInfo.versionName;
            txt_version.setText(getString(R.string.version)+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        homeActivityBinding.drawerLayout.setDrawerElevation(0f);
        homeActivityBinding.drawerLayout.addDrawerListener(actionBarDrawerToggle);


        homeFragmentBinding.serviceRv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        serviceListAdapter = new ServiceListAdapter(getContext(),datumArrayList,this);
        homeFragmentBinding.serviceRv.setAdapter(serviceListAdapter);

        callGetCategoryApi();

        homeFragmentBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }

        });


        homeFragmentBinding.callButton.setOnClickListener(v -> {

            long number = +966126591191L;
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setAction(Intent.ACTION_VIEW);
            sendIntent.setPackage("com.whatsapp");
            String url = "https://api.whatsapp.com/send?phone=" + number + "&text=" + "Hi";
            sendIntent.setData(Uri.parse(url));
            if (sendIntent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivity(sendIntent);
            }else{
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.whatsapp")));
                } catch (android.content.ActivityNotFoundException exception) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp")));
                }
            }


        });

        homeFragmentBinding.imgNotification.setOnClickListener(v -> {
            Intent  intent = new Intent(getActivity(), NotificationActivity.class);
            intent.putExtra(Constants.KEY_TOKEN,token);
            startActivity(intent);
        });
        return view;
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {
        homeActivityBinding.drawer.setForegroundGravity(Gravity.START);
        homeActivityBinding.drawerLayout.openDrawer(Gravity.START);
        Glide.with(this).load(resp.getData().getProfilePic()).into(circleImageView);
        txt_username.setText(resp.getData().getFirstName() +" "+resp.getData().getLastName());
    }


    // call get category api
    private void callGetCategoryApi() {

      //  progress.show();
        homeFragmentBinding.shimmerFrameLayout.startShimmerAnimation();
        GetCategoryViewModel viewModel = ViewModelProviders.of(this).get(GetCategoryViewModel.class);
        viewModel.init();
        viewModel.getCategory(token);
        viewModel.getVolumesResponseLiveData().observe(getActivity(), categoryResponse -> {

            //progress.hide();
            homeFragmentBinding.shimmerFrameLayout.stopShimmerAnimation();
            homeFragmentBinding.shimmerFrameLayout.setVisibility(View.GONE);
            homeFragmentBinding.serviceRv.setVisibility(View.VISIBLE);
            if(categoryResponse != null){
                if(categoryResponse.getSuccess()){
                    datumArrayList =  categoryResponse.getData();
                    if(datumArrayList == null){
                        datumArrayList = new ArrayList<>();
                    }
                    serviceListAdapter.setData(datumArrayList);

                    if(categoryResponse.isNotification()){
                        homeFragmentBinding.imgNotification.setImageDrawable(getResources().getDrawable(R.drawable.ic_notification_new));
                    }else{
                        homeFragmentBinding.imgNotification.setImageDrawable(getResources().getDrawable(R.drawable.ic_notification));
                    }
                }else{
                    openDialog(categoryResponse.getMessage());
                }
            }else {
                openDialog(getContext().getString(R.string.errormsg));
            }
        });

    }
    // open dialog
    private void openDialog(String msg){

        AlertPopup mAlert = new AlertPopup(getContext());
        mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert.setMessage(msg);
        mAlert.setOkButton(view -> {
            mAlert.dismiss();
            //Do want you want
        });
        mAlert.show();
    }
    @Override
    public void getPosition(int position, boolean isChecked) {
        Intent intent = new Intent(getActivity(), DoctorVisitActivity.class);
        intent.putExtra(Constants.KEY_CAT_ID,datumArrayList.get(position).getId());
        intent.putExtra(Constants.KEY_SUB_CAT_TITLE,datumArrayList.get(position).getEnName());
        intent.putExtra(Constants.KEY_TOKEN,token);
        startActivity(intent);
    }

    private void filter(String text) {
        List<Datum> filList = new ArrayList<>();

        for(Datum subDatum: datumArrayList){
            if(subDatum.getEnName().toLowerCase().contains(text.toLowerCase())){
                filList.add(subDatum);
            }
        }
        serviceListAdapter.filterItem(filList);
    }

    @Override
    public void call(String number) {
        enableRunTimePermission(number);
    }


    // enable run time permission
    private void enableRunTimePermission(String phone){
        Dexter.withContext(getActivity()).withPermissions(Manifest.permission.CALL_PHONE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + phone));//change the number
                        getActivity().startActivity(callIntent);

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public void onResume() {
        super.onResume();
        callGetCategoryApi();
    }


}