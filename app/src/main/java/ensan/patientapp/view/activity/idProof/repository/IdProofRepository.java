package ensan.patientapp.view.activity.idProof.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.idProof.model.IdProofResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IdProofRepository {

    private MutableLiveData<IdProofResponse> mutableLiveData;


    public IdProofRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    public void callGetIdProof(@NonNull String token){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<IdProofResponse> call = apiDataService.getIdProof("application/json","Bearer "+token);
        call.enqueue(new Callback<IdProofResponse>() {
            @Override
            public void onResponse(Call<IdProofResponse> call, Response<IdProofResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<IdProofResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<IdProofResponse> idProofResponseLiveData() {
        return mutableLiveData;
    }


}
