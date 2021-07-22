package ensan.patientapp.view.activity.savemedicalHistory.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.savemedicalHistory.model.MedicalHistoryResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaveMedicalHistoryRepository {

    private MutableLiveData<MedicalHistoryResponse> mutableLiveData;


    public SaveMedicalHistoryRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call save medical history api
    public void saveMedicalHistoryAddress(@NonNull String token, @NonNull RequestBody medicalKey, @NonNull RequestBody description, MultipartBody.Part part,@NonNull RequestBody id,@NonNull RequestBody family){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<MedicalHistoryResponse> call = apiDataService.saveMedicalHistory("application/json","Bearer "+token,medicalKey,description,part,id,family);
        call.enqueue(new Callback<MedicalHistoryResponse>() {
            @Override
            public void onResponse(Call<MedicalHistoryResponse> call, Response<MedicalHistoryResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<MedicalHistoryResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<MedicalHistoryResponse> medicalHistoryResponseLiveData() {
        return mutableLiveData;
    }


}
