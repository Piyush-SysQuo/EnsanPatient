package ensan.patientapp.view.fragment.addresses.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ensan.patientapp.R;
import ensan.patientapp.Utils.AlertPopup;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.Utils.Progress;
import ensan.patientapp.Utils.SuccessPopup;
import ensan.patientapp.Utils.Util;
import ensan.patientapp.Utils.Utility;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import ensan.patientapp.view.fragment.addresses.adapter.AddressesAdapter;
import ensan.patientapp.view.fragment.addresses.model.Datum;
import ensan.patientapp.view.fragment.addresses.model.DefaultAddress;
import ensan.patientapp.view.fragment.addresses.model.DeleteAddress;
import ensan.patientapp.view.fragment.addresses.viewmodel.AddressViewModel;
import ensan.patientapp.view.fragment.addresses.viewmodel.DefaultViewModel;

public class AddressesFragment extends Fragment implements View.OnClickListener, DeleteAddress, DefaultAddress {

    private String token;
    private View view;
    private Button addAddress;
    private Progress progress;
    private List<Datum> addressList=new ArrayList<>();
    private RecyclerView getAddressRecyclerview;
    private AddressesAdapter addressesAdapter;

    public AddressesFragment(String token) {
        this.token = token;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // get Token
        LoginResponse resp = (LoginResponse) Util.getInstance(getActivity()).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginResponse>() {
                });

        if(resp != null){
            String language = resp.getData().getLanaguage();
            Utility.setLocale(getActivity(), language);
        }

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_addresses, container, false);
        addAddress = view.findViewById(R.id.addAddress);
        addAddress.setOnClickListener(this);
        getAddressRecyclerview = view.findViewById(R.id.addressRecyclerview);
        ImageView closeLayout = view.findViewById(R.id.img_back);
        progress = new Progress(getContext());

        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);

        // fetch address from API
        getAddresses(token);

        // close screen
        closeLayout.setOnClickListener(v -> getFragmentManager().popBackStack());

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.addAddress:
                Intent i=new Intent(getActivity(),SaveNewAddressActivity.class);
                startActivity(i);
                break;

        }
    }

    @Override
    public void onResume() {
        getAddresses(token);
        super.onResume();
    }

    private void getAddresses(String token) {
        progress.show();
        AddressViewModel viewModel = ViewModelProviders.of(this).get(AddressViewModel.class);
        viewModel.init();
        viewModel.getAddressesList(token);
        viewModel.getVolumesResponseLiveData().observe(getActivity(), getAddressesResponse -> {
            progress.hide();
            if(getAddressesResponse != null) {
                addressList = getAddressesResponse.getData();
                getAddressRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                addressesAdapter = new AddressesAdapter(getActivity(), addressList, this,this);
                getAddressRecyclerview.setAdapter(addressesAdapter);
            }
        });



    }

    @Override
    public void getItemPosition(int id) {

        progress.show();
        AddressViewModel viewModel = ViewModelProviders.of(this).get(AddressViewModel.class);
        viewModel.init();
        viewModel.deleteAddress(token,id);
        viewModel.getDeleteAddressResponseLiveData().observe(getActivity(), getAddressesResponse -> {
            progress.hide();
            if(getAddressesResponse!=null){
                Toast.makeText(getContext(), getAddressesResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void getAddressPosition(int position) {
        MakeAddressDefault makeAddressDefault = new MakeAddressDefault(getActivity(),String.valueOf(addressList.get(position).getId()),this);
        makeAddressDefault.show(getActivity().getSupportFragmentManager(),makeAddressDefault.getTag());
    }

    @Override
    public void getId(String addressId) {
        progress.show();
        DefaultViewModel defaultViewModel = ViewModelProviders.of(this).get(DefaultViewModel.class);
        defaultViewModel.init();
        defaultViewModel.setDefaultAddress(token,addressId);
        defaultViewModel.getVolumesResponseLiveData().observe(getActivity(), saveAddressResponse -> {
            progress.hide();
            if(saveAddressResponse != null){
                if (saveAddressResponse.getSuccess()){
                    openSuccessDialog(saveAddressResponse.getMessage());
                }else {
                    openDialog(saveAddressResponse.getMessage());
                }
            } else {
                openDialog(getString(R.string.errormsg));
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
    // open success dialog
    private void openSuccessDialog(String msg){
        SuccessPopup mAlert = new SuccessPopup(getContext());
        mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert.setMessage(msg);
        mAlert.setOkButton(view -> {
            mAlert.dismiss();
            //Do want you want
        });
        mAlert.show();
    }
}