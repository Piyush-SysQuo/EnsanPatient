package ensan.patientapp.view.activity.bookingdetails.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.bookingdetails.model.CareGiverListResponse;
import ensan.patientapp.view.activity.bookingdetails.repository.BookingDetailsRepository;

public class BookingDetailsViewModel extends AndroidViewModel {

    private LiveData<CareGiverListResponse> careGiverListResponseLiveData;
    private BookingDetailsRepository bookingDetailsRepository;

    public BookingDetailsViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        bookingDetailsRepository = new BookingDetailsRepository();
        careGiverListResponseLiveData = bookingDetailsRepository.careGiverListResponseLiveData();
    }

    // get caregiver list
    public void getCareGiverList(@NonNull String token, @NonNull String bookingFor, @NonNull String catId, @NonNull String subCatId, @NonNull String fromDate, @NonNull String toDate, @NonNull String time
            , @NonNull String freqId, @NonNull String lang, String familyId){
        bookingDetailsRepository.getCareGiverList(token,bookingFor,catId,subCatId,fromDate,toDate,time,freqId,lang,familyId);
    }

    public LiveData<CareGiverListResponse> getVolumesResponseLiveData() {
        return careGiverListResponseLiveData;
    }
}
