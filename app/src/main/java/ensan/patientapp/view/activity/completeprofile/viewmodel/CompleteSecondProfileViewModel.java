package ensan.patientapp.view.activity.completeprofile.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.completeprofile.model.ProfileResponse;
import ensan.patientapp.view.activity.completeprofile.repository.CompleteSecondProfileRepository;

public class CompleteSecondProfileViewModel extends AndroidViewModel {

    private LiveData<ProfileResponse> profileResponseLiveData;
    private CompleteSecondProfileRepository completeSecondProfileRepository;

    public CompleteSecondProfileViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        completeSecondProfileRepository = new CompleteSecondProfileRepository();
        profileResponseLiveData = completeSecondProfileRepository.profileResponseLiveData();
    }

    public void completeSecondProfile(@NonNull String token, @NonNull String child, @NonNull String height, @NonNull String weight, @NonNull String b_group, @NonNull String emergency_number, @NonNull String family_member_id) {
        completeSecondProfileRepository.callSecondProfilerApi(token,child,height,weight,b_group,emergency_number,family_member_id);
    }

    public LiveData<ProfileResponse> getVolumesResponseLiveData() {
        return profileResponseLiveData;
    }
}
