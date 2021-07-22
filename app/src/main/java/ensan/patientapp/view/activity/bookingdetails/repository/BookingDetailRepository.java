package ensan.patientapp.view.activity.bookingdetails.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.bookingdetails.model.BookingDetailsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingDetailRepository {

    private MutableLiveData<BookingDetailsResponse> mutableLiveData;


    public BookingDetailRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call get booking Details api
    public void getBookingDetail(@NonNull String token, @NonNull String bookingId){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<BookingDetailsResponse> call = apiDataService.getBookingDetail("application/json","Bearer "+token,bookingId);
        call.enqueue(new Callback<BookingDetailsResponse>() {
            @Override
            public void onResponse(Call<BookingDetailsResponse> call, Response<BookingDetailsResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<BookingDetailsResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<BookingDetailsResponse> bookingDetailResponseLiveData() {
        return mutableLiveData;
    }


}
