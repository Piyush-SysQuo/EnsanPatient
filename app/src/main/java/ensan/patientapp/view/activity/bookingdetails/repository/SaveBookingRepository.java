package ensan.patientapp.view.activity.bookingdetails.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.bookingdetails.model.SaveBookingBody;
import ensan.patientapp.view.activity.bookingdetails.model.SaveBookingResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaveBookingRepository {

    private MutableLiveData<SaveBookingResponse> mutableLiveData;


    public SaveBookingRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call save booking  api
    public void saveBooking(@NonNull String token, @NonNull String bookingId, @NonNull String careGiverId, String notes){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<SaveBookingResponse> call = apiDataService.saveBooking("application/json","Bearer "+token,new SaveBookingBody(bookingId,careGiverId,notes));
        call.enqueue(new Callback<SaveBookingResponse>() {
            @Override
            public void onResponse(Call<SaveBookingResponse> call, Response<SaveBookingResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<SaveBookingResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<SaveBookingResponse> saveBookingResponseLiveData() {
        return mutableLiveData;
    }


}
