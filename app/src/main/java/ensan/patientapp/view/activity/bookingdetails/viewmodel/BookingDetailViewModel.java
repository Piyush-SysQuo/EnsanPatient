package ensan.patientapp.view.activity.bookingdetails.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import ensan.patientapp.view.activity.bookingdetails.model.BookingDetailsResponse;
import ensan.patientapp.view.activity.bookingdetails.repository.BookingDetailRepository;

public class BookingDetailViewModel extends AndroidViewModel {

    private LiveData<BookingDetailsResponse> bookingDetailResponseLiveData;
    private BookingDetailRepository bookingDetailRepository;

    public BookingDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        bookingDetailRepository = new BookingDetailRepository();
        bookingDetailResponseLiveData = bookingDetailRepository.bookingDetailResponseLiveData();
    }

    public void getBookingDetail(@NonNull String token, @NonNull String careGiverId) {
        bookingDetailRepository.getBookingDetail(token,careGiverId);
    }

    public LiveData<BookingDetailsResponse> getVolumesResponseLiveData() {
        return bookingDetailResponseLiveData;
    }
}
