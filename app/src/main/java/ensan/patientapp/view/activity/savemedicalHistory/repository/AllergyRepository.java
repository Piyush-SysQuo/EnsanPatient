package ensan.patientapp.view.activity.savemedicalHistory.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.savemedicalHistory.model.AllergyResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllergyRepository {

    private MutableLiveData<AllergyResponse> mutableLiveData;


    public AllergyRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call allergy api
    public void saveAllergy(@NonNull String token, @NonNull String allergyType, @NonNull String description, @NonNull String familyID){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<AllergyResponse> call = apiDataService.addAllergy("application/json","Bearer "+token,allergyType,description,familyID);
        call.enqueue(new Callback<AllergyResponse>() {
            @Override
            public void onResponse(Call<AllergyResponse> call, Response<AllergyResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<AllergyResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<AllergyResponse> allergyResponseLiveData() {
        return mutableLiveData;
    }


}
