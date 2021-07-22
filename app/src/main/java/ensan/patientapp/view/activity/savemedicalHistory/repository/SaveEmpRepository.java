package ensan.patientapp.view.activity.savemedicalHistory.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.savemedicalHistory.model.EmploymentResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaveEmpRepository {

    private MutableLiveData<EmploymentResponse> mutableLiveData;


    public SaveEmpRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call Employee api
    public void saveEmp(@NonNull String token, @NonNull String working, @NonNull String detail, @NonNull String familyId){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<EmploymentResponse> call = apiDataService.addEmployee("application/json","Bearer "+token,working,detail,familyId);
        call.enqueue(new Callback<EmploymentResponse>() {
            @Override
            public void onResponse(Call<EmploymentResponse> call, Response<EmploymentResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<EmploymentResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<EmploymentResponse> employmentResponseLiveData() {
        return mutableLiveData;
    }


}
