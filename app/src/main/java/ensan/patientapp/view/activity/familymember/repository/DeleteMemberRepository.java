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

public class DeleteMemberRepository {

    private MutableLiveData<FamilyMemberResponse> mutableLiveData;


    public DeleteMemberRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call delete family member API
    public void deleteFamilyMember(@NonNull String token, @NonNull String id){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<FamilyMemberResponse> call = apiDataService.deleteFamilyMember("application/json","Bearer "+token,id);
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
