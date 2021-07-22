package ensan.patientapp.view.activity.familymember.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.familymember.model.FamilyMemberResponse;
import ensan.patientapp.view.activity.familymember.repository.FamilyMemberRepository;


public class FamilyMemberViewModel extends AndroidViewModel {

    private LiveData<FamilyMemberResponse> familyMemberResponseLiveData;
    private FamilyMemberRepository familyMemberRepository;

    public FamilyMemberViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        familyMemberRepository = new FamilyMemberRepository();
        familyMemberResponseLiveData = familyMemberRepository.familyMemberResponseLiveData();
    }

    // save family member
    public void saveFamilyMember(@NonNull String token, @NonNull String name, @NonNull String relationShip, @NonNull String phone, @NonNull String type) {
        familyMemberRepository.saveFamilyMember(token,name,relationShip,phone,type);
    }

    public LiveData<FamilyMemberResponse> getVolumesResponseLiveData() {
        return familyMemberResponseLiveData;
    }
}
