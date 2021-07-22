package ensan.patientapp.view.fragment.booking.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.fragment.booking.model.CurrentBookingResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PastBookingRepository {

    private MutableLiveData<CurrentBookingResponse> mutableLiveData;


    public PastBookingRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // get past  booking list api
    public void getPastBooking(@NonNull String token){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<CurrentBookingResponse> call = apiDataService.getPatientPastBooking("application/json","Bearer "+token);
        call.enqueue(new Callback<CurrentBookingResponse>() {
            @Override
            public void onResponse(Call<CurrentBookingResponse> call, Response<CurrentBookingResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<CurrentBookingResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<CurrentBookingResponse> pastBookingResponseLiveData() {
        return mutableLiveData;
    }


}
