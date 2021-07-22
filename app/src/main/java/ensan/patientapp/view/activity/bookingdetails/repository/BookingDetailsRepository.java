package ensan.patientapp.view.activity.bookingdetails.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.bookingdetails.model.CareGiverListResponse;
import ensan.patientapp.view.activity.choosedatetime.model.BookingRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingDetailsRepository {

    private MutableLiveData<CareGiverListResponse> mutableLiveData;


    public BookingDetailsRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call caregiver list api
    public void getCareGiverList(@NonNull String token, @NonNull String bookingFor, @NonNull String catId, @NonNull String subCatId, @NonNull String fromDate, @NonNull String toDate, @NonNull String time
    , @NonNull String freqId, @NonNull String lang, String familyId){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<CareGiverListResponse> call = apiDataService.getCareGiverList("application/json","Bearer "+token,new BookingRequest(bookingFor,catId,subCatId,fromDate,toDate,time,freqId,lang,familyId));
        call.enqueue(new Callback<CareGiverListResponse>() {
            @Override
            public void onResponse(Call<CareGiverListResponse> call, Response<CareGiverListResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<CareGiverListResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<CareGiverListResponse> careGiverListResponseLiveData() {
        return mutableLiveData;
    }


}
