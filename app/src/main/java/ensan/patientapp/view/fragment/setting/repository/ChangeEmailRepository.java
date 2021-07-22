package ensan.patientapp.view.fragment.setting.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.fragment.setting.model.ChangeEmailResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeEmailRepository {

    private MutableLiveData<ChangeEmailResponse> mutableLiveData;


    public ChangeEmailRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    //  change email
    public void changeEmail(@NonNull String token, @NonNull String email){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<ChangeEmailResponse> call = apiDataService.changeEmail("application/json","Bearer "+token,email);
        call.enqueue(new Callback<ChangeEmailResponse>() {
            @Override
            public void onResponse(Call<ChangeEmailResponse> call, Response<ChangeEmailResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ChangeEmailResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<ChangeEmailResponse> changeEmailResponseLiveData() {
        return mutableLiveData;
    }


}
