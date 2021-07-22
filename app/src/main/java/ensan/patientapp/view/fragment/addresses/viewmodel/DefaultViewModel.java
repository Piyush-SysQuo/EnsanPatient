package ensan.patientapp.view.fragment.addresses.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.fragment.addresses.model.SaveAddressResponse;
import ensan.patientapp.view.fragment.addresses.repository.SetDefaultAddRepository;

public class DefaultViewModel extends AndroidViewModel {

    private LiveData<SaveAddressResponse> bookingHistoryResponseLiveData;
    private SetDefaultAddRepository setDefaultAddRepository;

    public DefaultViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        setDefaultAddRepository = new SetDefaultAddRepository();
        bookingHistoryResponseLiveData = setDefaultAddRepository.addressResponseLiveData();
    }

    public void setDefaultAddress(@NonNull String token, @NonNull String bookingId) {
        setDefaultAddRepository.setDefaultAddress(token,bookingId);
    }

    public LiveData<SaveAddressResponse> getVolumesResponseLiveData() {
        return bookingHistoryResponseLiveData;
    }
}
