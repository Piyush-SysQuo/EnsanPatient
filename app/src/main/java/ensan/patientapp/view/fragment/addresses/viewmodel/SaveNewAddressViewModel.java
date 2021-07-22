package ensan.patientapp.view.fragment.addresses.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.fragment.addresses.model.SaveAddressResponse;
import ensan.patientapp.view.fragment.addresses.repository.SaveNewAddressRepository;


public class SaveNewAddressViewModel extends AndroidViewModel {
    public SaveNewAddressViewModel(@NonNull Application application) {
        super(application);
    }




    private LiveData<SaveAddressResponse> addressResponseLiveData;
    private SaveNewAddressRepository saveAddressRepository;


    public void init() {
        saveAddressRepository = new SaveNewAddressRepository();
        addressResponseLiveData = saveAddressRepository.saveAddressResponseLiveData();
    }

    public void saveAddress(@NonNull String token,@NonNull String familyIdRequest, @NonNull String address, @NonNull String poBox, @NonNull String city , @NonNull String country, @NonNull String lat, @NonNull String lng, @NonNull String landmark,String primaryadd,String addmore, String tag) {
        saveAddressRepository.saveAddress(token,familyIdRequest,address,poBox,city,country,lat,lng,landmark,primaryadd,addmore,tag);
    }


    public LiveData<SaveAddressResponse> getVolumesResponseLiveData() {
        return addressResponseLiveData;
    }
}
