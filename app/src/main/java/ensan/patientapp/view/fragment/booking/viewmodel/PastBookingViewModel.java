package ensan.patientapp.view.fragment.booking.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.fragment.booking.model.CurrentBookingResponse;
import ensan.patientapp.view.fragment.booking.repository.PastBookingRepository;


public class PastBookingViewModel extends AndroidViewModel {

    private LiveData<CurrentBookingResponse> pastBookingRepositoryLiveData;
    private PastBookingRepository pastBookingRepository;

    public PastBookingViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        pastBookingRepository = new PastBookingRepository();
        pastBookingRepositoryLiveData = pastBookingRepository.pastBookingResponseLiveData();
    }

    public void getPastBooking(@NonNull String token) {
        pastBookingRepository.getPastBooking(token);
    }

    public LiveData<CurrentBookingResponse> getVolumesResponseLiveData() {
        return pastBookingRepositoryLiveData;
    }
}
