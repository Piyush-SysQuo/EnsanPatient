package ensan.patientapp.view.activity.medicalhistory.reppsitory;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicalHistoryRepository {

    private MutableLiveData<ensan.patientapp.view.activity.medicalhistory.model.MedicalHistoryResponse> mutableLiveData;


    public MedicalHistoryRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call get medical history api
    public void getMedicalHistory(@NonNull String token, @NonNull String familyId){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<ensan.patientapp.view.activity.medicalhistory.model.MedicalHistoryResponse> call = apiDataService.getFamilyMedicalHistory("application/json","Bearer "+token,familyId);
        call.enqueue(new Callback<ensan.patientapp.view.activity.medicalhistory.model.MedicalHistoryResponse>() {
            @Override
            public void onResponse(Call<ensan.patientapp.view.activity.medicalhistory.model.MedicalHistoryResponse> call, Response<ensan.patientapp.view.activity.medicalhistory.model.MedicalHistoryResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ensan.patientapp.view.activity.medicalhistory.model.MedicalHistoryResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<ensan.patientapp.view.activity.medicalhistory.model.MedicalHistoryResponse> medicalHistoryResponseLiveData() {
        return mutableLiveData;
    }


}
