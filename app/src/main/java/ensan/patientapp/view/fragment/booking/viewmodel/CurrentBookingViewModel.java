package ensan.patientapp.view.fragment.booking.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.fragment.booking.model.CurrentBookingResponse;
import ensan.patientapp.view.fragment.booking.repository.CurrentBookingRepository;


public class CurrentBookingViewModel extends AndroidViewModel {

    private LiveData<CurrentBookingResponse> currentBookingRepositoryLiveData;
    private CurrentBookingRepository currentBookingRepository;

    public CurrentBookingViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        currentBookingRepository = new CurrentBookingRepository();
        currentBookingRepositoryLiveData = currentBookingRepository.currentBookingResponseLiveData();
    }

    public void getCurrentBooking(@NonNull String token) {
        currentBookingRepository.getCurrentBooking(token);
    }

    public LiveData<CurrentBookingResponse> getVolumesResponseLiveData() {
        return currentBookingRepositoryLiveData;
    }
}
