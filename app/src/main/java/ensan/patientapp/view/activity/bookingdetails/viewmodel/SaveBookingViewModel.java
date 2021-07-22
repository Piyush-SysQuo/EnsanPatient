package ensan.patientapp.view.activity.bookingdetails.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.bookingdetails.model.SaveBookingResponse;
import ensan.patientapp.view.activity.bookingdetails.repository.SaveBookingRepository;

public class SaveBookingViewModel extends AndroidViewModel {

    private LiveData<SaveBookingResponse> giverListResponseLiveData;
    private SaveBookingRepository saveBookingRepository;

    public SaveBookingViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        saveBookingRepository = new SaveBookingRepository();
        giverListResponseLiveData = saveBookingRepository.saveBookingResponseLiveData();
    }

    // call save booking  api
    public void saveBooking(@NonNull String token, @NonNull String bookingId, @NonNull String careGiverId,String notes){
        saveBookingRepository.saveBooking(token,bookingId,careGiverId,notes);
    }

    public LiveData<SaveBookingResponse> getVolumesResponseLiveData() {
        return giverListResponseLiveData;
    }
}
