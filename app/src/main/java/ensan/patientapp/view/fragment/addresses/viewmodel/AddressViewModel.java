package ensan.patientapp.view.fragment.addresses.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.fragment.addresses.model.DeleteAddressResponse;
import ensan.patientapp.view.fragment.addresses.model.GetAddressesResponse;
import ensan.patientapp.view.fragment.addresses.repository.AddressesRepository;

public class AddressViewModel extends AndroidViewModel {

    public AddressViewModel(@NonNull Application application) {
        super(application);
    }



    private LiveData<GetAddressesResponse> getAddressesResponseLiveData;
    private LiveData<DeleteAddressResponse> deleteAddressResponseLiveData;

    private AddressesRepository getAddressesRepository;


    public void init() {
        getAddressesRepository = new AddressesRepository();
        getAddressesResponseLiveData = getAddressesRepository.getAddresessResponseLiveData();
        deleteAddressResponseLiveData=getAddressesRepository.deleteAddressResponseLiveData();
    }

    public void getAddressesList(@NonNull String token) {
        getAddressesRepository.callGetAddressApi(token);
    }

    public LiveData<GetAddressesResponse> getVolumesResponseLiveData() {
        return getAddressesResponseLiveData;
    }

    public void deleteAddress(String token,int id){
        getAddressesRepository.callDeleteAddress(token,id);

    }

    public LiveData<DeleteAddressResponse> getDeleteAddressResponseLiveData(){
        return deleteAddressResponseLiveData;
    }
}
