package ensan.patientapp.view.fragment.booking.view;


import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;
import java.util.Objects;

import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.SuccessPopup;
import ensan.patientapp.Utils.Util;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.databinding.BookingFragmentBinding;
import ensan.patientapp.R;
import ensan.patientapp.view.activity.bookingdetails.view.BookingDetailActivity;
import ensan.patientapp.view.activity.caregiverprofile.view.CareGiverProfileActivity;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import ensan.patientapp.view.activity.review.view.ReviewActivity;
import ensan.patientapp.view.fragment.booking.adapter.CurrentListAdapter;
import ensan.patientapp.view.fragment.booking.adapter.PastListAdapter;
import ensan.patientapp.view.fragment.booking.model.CurrentBooking;
import ensan.patientapp.view.fragment.booking.model.GetCallPosition;
import ensan.patientapp.view.fragment.booking.model.GetCurrentPosition;
import ensan.patientapp.view.fragment.booking.model.GetPosition;
import ensan.patientapp.view.fragment.booking.model.ViewProfilePosition;
import ensan.patientapp.view.fragment.booking.viewmodel.CancelBookingViewModel;
import ensan.patientapp.view.fragment.booking.viewmodel.CurrentBookingViewModel;
import ensan.patientapp.view.fragment.booking.viewmodel.PastBookingViewModel;
import ensan.patientapp.view.fragment.cancel.CancelDatum;
import ensan.patientapp.view.fragment.cancel.CancelNoteFragment;


public class BookingFragment extends Fragment implements View.OnClickListener, GetCurrentPosition, ViewProfilePosition, GetCallPosition, GetPosition, CancelDatum {

    private BookingFragmentBinding homeFragmentBinding;
    private View view;
    private List<CurrentBooking> bookingList;
    private LoginResponse resp;
    private String token;
    private Progress progress;
    private CurrentListAdapter currentListAdapter;
    private PastListAdapter pastListAdapter;
    private String notes="";
    private String language;
    private int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // get Token
        resp = (LoginResponse) Util.getInstance(getActivity()).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginResponse>() {
                });

        if(resp != null) {
            token = resp.getData().getToken();
            language = resp.getData().getLanaguage();
            Utility.setLocale(getActivity(),language);
        }

        // Inflate the layout for this fragment
        homeFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.booking_fragment, container, false);
        view = homeFragmentBinding.getRoot();


        // initialize progress dialog instance
        progress = new Progress(getContext());
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);

        // get current booking list
        getCurrentBooking(token);

        homeFragmentBinding.currentBookingLayout.setOnClickListener(this);
        homeFragmentBinding.pastBookingLayout.setOnClickListener(this);
        homeFragmentBinding.backBtn.setOnClickListener(this);

        homeFragmentBinding.btnContinue.setOnClickListener(v -> {
           getActivity().getSupportFragmentManager().popBackStack();
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.currentBookingLayout:
                homeFragmentBinding.view1.setVisibility(View.VISIBLE);
                homeFragmentBinding.view2.setVisibility(View.GONE);
                homeFragmentBinding.currentRecyclerView.setVisibility(View.VISIBLE);
                homeFragmentBinding.pastRecyclerView.setVisibility(View.GONE);
                getCurrentBooking(token);
                break;
            case R.id.pastBookingLayout:
                homeFragmentBinding.view1.setVisibility(View.GONE);
                homeFragmentBinding.view2.setVisibility(View.VISIBLE);
                homeFragmentBinding.currentRecyclerView.setVisibility(View.GONE);
                homeFragmentBinding.pastRecyclerView.setVisibility(View.VISIBLE);
                getPastBooking(token);
                break;
            case R.id.backBtn:
                getActivity().getSupportFragmentManager().popBackStack();
                break;

        }
    }

    /*call past booking list Api*/
    private void getPastBooking(String token) {
        progress.show();
        PastBookingViewModel viewModel = ViewModelProviders.of(this).get(PastBookingViewModel.class);
        viewModel.init();
        viewModel.getPastBooking(token);
        viewModel.getVolumesResponseLiveData().observe(getActivity(), pastBookingResponse -> {
            progress.hide();
            if(pastBookingResponse != null){
                if(pastBookingResponse.getSuccess()) {
                    bookingList = pastBookingResponse.getCurrentBookingData().getBookings();
                    homeFragmentBinding.pastRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));
                    if(bookingList.size() == 0){
                        homeFragmentBinding.currentRecyclerView.setVisibility(View.GONE);
                        homeFragmentBinding.bookinglayout.setVisibility(View.GONE);
                        homeFragmentBinding.noBookingLayout.setVisibility(View.VISIBLE);
                    }else {
                        homeFragmentBinding.bookinglayout.setVisibility(View.VISIBLE);
                        homeFragmentBinding.noBookingLayout.setVisibility(View.GONE);
                    }
                    pastListAdapter = new PastListAdapter(getActivity(),bookingList,this,this,this);
                    homeFragmentBinding.pastRecyclerView.setAdapter(pastListAdapter);
                }else{
                    openDialog(pastBookingResponse.getMessage());
                }
            }else{
                // do nothing
            }
        });
    }

    /*call current booking list Api*/
    private void getCurrentBooking(String token){
        progress.show();
        CurrentBookingViewModel viewModel = ViewModelProviders.of(this).get(CurrentBookingViewModel.class);
        viewModel.init();
        viewModel.getCurrentBooking(token);
        viewModel.getVolumesResponseLiveData().observe(getActivity(), currentBookingResponse -> {
            progress.hide();
             if(currentBookingResponse != null){
                 if(currentBookingResponse.getSuccess()) {
                     bookingList = currentBookingResponse.getCurrentBookingData().getBookings();
                     homeFragmentBinding.currentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));
                     if(bookingList.size() == 0){
                         homeFragmentBinding.pastRecyclerView.setVisibility(View.GONE);
                         homeFragmentBinding.bookinglayout.setVisibility(View.GONE);
                         homeFragmentBinding.noBookingLayout.setVisibility(View.VISIBLE);
                     }else {
                         homeFragmentBinding.noBookingLayout.setVisibility(View.GONE);
                         homeFragmentBinding.currentRecyclerView.setVisibility(View.VISIBLE);
                         homeFragmentBinding.bookinglayout.setVisibility(View.VISIBLE);
                     }
                     currentListAdapter = new CurrentListAdapter(getActivity(),bookingList,this,this,this,this);
                     homeFragmentBinding.currentRecyclerView.setAdapter(currentListAdapter);
                 }else{
                     openDialog(currentBookingResponse.getMessage());
//                     Toast.makeText(getContext(), currentBookingResponse.getMessage(), Toast.LENGTH_SHORT).show();
                 }
             }else{
                 // do nothing
             }
        });
    }
    // open dialog
    private void openDialog(String msg){

        AlertPopup mAlert = new AlertPopup(getContext());
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

        SuccessPopup mAlert = new SuccessPopup(getContext());
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
    @Override
    public void getPosition(int position) {
        this.position = position;
        CancelNoteFragment cancelNoteFragment = new CancelNoteFragment(getActivity(),this);
        cancelNoteFragment.show(getActivity().getSupportFragmentManager(),cancelNoteFragment.getTag());
    }

    public void cancelBooking(int position, String notes){
        progress.show();
        String bookingId = bookingList.get(position).getBookingId();
        CancelBookingViewModel viewModel = ViewModelProviders.of(this).get(CancelBookingViewModel.class);
        viewModel.init();
        viewModel.cancelBooking(token,bookingId,notes);
        viewModel.getVolumesResponseLiveData().observe(getActivity(), cancelBookingResponse -> {
            progress.hide();
            if(cancelBookingResponse != null){
                if(cancelBookingResponse.getSuccess()) {
                    CurrentBooking currentBooking = bookingList.get(position);
                    currentListAdapter.removeDataFromList(currentBooking);
                    openSuccessDialog(cancelBookingResponse.getMessage());
                }else {
                    openDialog(cancelBookingResponse.getMessage());
                }
//                Toast.makeText(getContext(), cancelBookingResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void getProfilePosition(int position) {
        String careGiverId =  bookingList.get(position).getCareGiverId();
        Intent intent = new Intent(getActivity(), ReviewActivity.class);
        intent.putExtra(Constants.KEY_CARE_GIVER_ID,careGiverId);
        intent.putExtra(Constants.KEY_PIC,bookingList.get(position).getProfilePic());
        intent.putExtra(Constants.KEY_NAME,bookingList.get(position).getName());
        intent.putExtra(Constants.KEY_BOOKING_ID,bookingList.get(position).getBookingId());
        startActivity(intent);

    }

    @Override
    public void getOpenProfile(int position) {
        String careGiverId =  bookingList.get(position).getCareGiverId();
        Intent intent = new Intent(getActivity(), CareGiverProfileActivity.class);
        intent.putExtra(Constants.KEY_TOKEN,token);
        intent.putExtra(Constants.KEY_CARE_GIVER_ID,careGiverId);
        intent.putExtra(Constants.KEY_STATUS_CONFIRMED,"true");
        startActivity(intent);
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
    public void getPatientPosition(int position,String from) {
        String bookingId = bookingList.get(position).getBookingId();
        Intent intent = new Intent(getActivity(), BookingDetailActivity.class);
        intent.putExtra(Constants.KEY_TOKEN,token);
        intent.putExtra(Constants.KEY_BOOKING_ID,bookingId);
        intent.putExtra(Constants.KEY_FROM,from);
        startActivity(intent);
    }

    @Override
    public void notes(String note) {
        cancelBooking(position,notes);
    }
}