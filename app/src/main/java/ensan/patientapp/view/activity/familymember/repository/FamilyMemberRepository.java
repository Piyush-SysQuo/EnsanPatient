package ensan.patientapp.view.activity.familymember.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.familymember.model.FamilyMemberResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FamilyMemberRepository {

    private MutableLiveData<FamilyMemberResponse> mutableLiveData;


    public FamilyMemberRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call save family member API
    public void saveFamilyMember(@NonNull String token, @NonNull String name, @NonNull String relationShip, @NonNull String phone, @NonNull String type){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<FamilyMemberResponse> call = apiDataService.saveFamilyMember("application/json","Bearer "+token,name,relationShip,phone,type);
        call.enqueue(new Callback<FamilyMemberResponse>() {
            @Override
            public void onResponse(Call<FamilyMemberResponse> call, Response<FamilyMemberResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<FamilyMemberResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<FamilyMemberResponse> familyMemberResponseLiveData() {
        return mutableLiveData;
    }


}
