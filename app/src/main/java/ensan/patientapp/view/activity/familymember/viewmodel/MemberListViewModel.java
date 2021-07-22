package ensan.patientapp.view.activity.familymember.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.familymember.model.FamilyMemberListResponse;
import ensan.patientapp.view.activity.familymember.repository.MemberListRepository;


public class MemberListViewModel extends AndroidViewModel {

    private LiveData<FamilyMemberListResponse> detailResponseLiveData;
    private MemberListRepository memberListRepository;

    public MemberListViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        memberListRepository = new MemberListRepository();
        detailResponseLiveData = memberListRepository.detailResponseLiveData();
    }

    public void getFamilyMember(@NonNull String token) {
        memberListRepository.callGetMemberDetail(token);
    }

    public LiveData<FamilyMemberListResponse> getVolumesResponseLiveData() {
        return detailResponseLiveData;
    }
}
