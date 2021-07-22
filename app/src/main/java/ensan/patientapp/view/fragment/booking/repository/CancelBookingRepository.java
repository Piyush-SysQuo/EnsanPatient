package ensan.patientapp.view.fragment.booking.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.fragment.booking.model.CancelBookingResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CancelBookingRepository {

    private MutableLiveData<CancelBookingResponse> mutableLiveData;


    public CancelBookingRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call cancel booking  api
    public void cancelBooking(@NonNull String token, @NonNull String bookingId, String notes){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<CancelBookingResponse> call = apiDataService.cancelBooking("application/json","Bearer "+token,bookingId,notes);
        call.enqueue(new Callback<CancelBookingResponse>() {
            @Override
            public void onResponse(Call<CancelBookingResponse> call, Response<CancelBookingResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<CancelBookingResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<CancelBookingResponse> cancelBookingResponseLiveData() {
        return mutableLiveData;
    }


}
