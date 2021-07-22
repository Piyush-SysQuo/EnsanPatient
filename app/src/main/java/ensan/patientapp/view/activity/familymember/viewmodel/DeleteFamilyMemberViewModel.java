package ensan.patientapp.view.activity.familymember.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.familymember.model.FamilyMemberResponse;
import ensan.patientapp.view.activity.familymember.repository.DeleteMemberRepository;


public class DeleteFamilyMemberViewModel extends AndroidViewModel {

    private LiveData<FamilyMemberResponse> familyMemberResponseLiveData;
    private DeleteMemberRepository familyMemberRepository;

    public DeleteFamilyMemberViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        familyMemberRepository = new DeleteMemberRepository();
        familyMemberResponseLiveData = familyMemberRepository.familyMemberResponseLiveData();
    }

    // delete family member
    public void deleteFamilyMember(@NonNull String token, @NonNull String id) {
        familyMemberRepository.deleteFamilyMember(token,id);
    }

    public LiveData<FamilyMemberResponse> getVolumesResponseLiveData() {
        return familyMemberResponseLiveData;
    }
}
