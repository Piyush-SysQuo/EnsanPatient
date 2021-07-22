package ensan.patientapp.view.activity.familymember.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ensan.patientapp.connection.ApiDataService;
import ensan.patientapp.connection.RetrofitInstance;
import ensan.patientapp.view.activity.familymember.model.FamilyMemberListResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberListRepository {

    private MutableLiveData<FamilyMemberListResponse> mutableLiveData;


    public MemberListRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call get member list api
    public void callGetMemberDetail(@NonNull String token){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<FamilyMemberListResponse> call = apiDataService.getFamilyMember("application/json","Bearer "+token);
        call.enqueue(new Callback<FamilyMemberListResponse>() {
            @Override
            public void onResponse(Call<FamilyMemberListResponse> call, Response<FamilyMemberListResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<FamilyMemberListResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<FamilyMemberListResponse> detailResponseLiveData() {
        return mutableLiveData;
    }


}
