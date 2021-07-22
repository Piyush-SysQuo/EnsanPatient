package ensan.patientapp.view.activity.idinsurance.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.idinsurance.model.InsuranceResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsuranceIdRepository {

    private MutableLiveData<InsuranceResponse> mutableLiveData;


    public InsuranceIdRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    public void callGetInsuranceCard(@NonNull String token){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<InsuranceResponse> call = apiDataService.getInsurance("application/json","Bearer "+token);
        call.enqueue(new Callback<InsuranceResponse>() {
            @Override
            public void onResponse(Call<InsuranceResponse> call, Response<InsuranceResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<InsuranceResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<InsuranceResponse> idProofResponseLiveData() {
        return mutableLiveData;
    }


}
