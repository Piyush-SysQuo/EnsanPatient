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

public class CurrentBookingRepository {

    private MutableLiveData<CurrentBookingResponse> mutableLiveData;


    public CurrentBookingRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call current booking list api
    public void getCurrentBooking(@NonNull String token){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<CurrentBookingResponse> call = apiDataService.getPatientBooking("application/json","Bearer "+token);
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

    public LiveData<CurrentBookingResponse> currentBookingResponseLiveData() {
        return mutableLiveData;
    }


}
