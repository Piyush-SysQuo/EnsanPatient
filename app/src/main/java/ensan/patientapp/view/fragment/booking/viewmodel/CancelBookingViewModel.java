package ensan.patientapp.view.fragment.booking.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import ensan.patientapp.view.fragment.booking.model.CancelBookingResponse;
import ensan.patientapp.view.fragment.booking.repository.CancelBookingRepository;


public class CancelBookingViewModel extends AndroidViewModel {

    private LiveData<CancelBookingResponse> cancelBookingResponseLiveData;
    private CancelBookingRepository cancelBookingRepository;

    public CancelBookingViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        cancelBookingRepository = new CancelBookingRepository();
        cancelBookingResponseLiveData = cancelBookingRepository.cancelBookingResponseLiveData();
    }

    public void cancelBooking(@NonNull String token, @NonNull String bookingId, String notes) {
        cancelBookingRepository.cancelBooking(token,bookingId,notes);
    }

    public LiveData<CancelBookingResponse> getVolumesResponseLiveData() {
        return cancelBookingResponseLiveData;
    }
}
