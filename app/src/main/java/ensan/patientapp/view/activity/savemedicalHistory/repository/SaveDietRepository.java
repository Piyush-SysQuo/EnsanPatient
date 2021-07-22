package ensan.patientapp.view.activity.savemedicalHistory.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import org.json.JSONArray;
import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.savemedicalHistory.model.DietResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaveDietRepository {

    private MutableLiveData<DietResponse> mutableLiveData;


    public SaveDietRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call diet api
    public void saveDiet(@NonNull String token, @NonNull JSONArray jsonArray, @NonNull String familyId){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<DietResponse> call = apiDataService.addDiet("application/json","Bearer "+token,jsonArray,familyId);
        call.enqueue(new Callback<DietResponse>() {
            @Override
            public void onResponse(Call<DietResponse> call, Response<DietResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<DietResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<DietResponse> dietResponseLiveData() {
        return mutableLiveData;
    }


}
