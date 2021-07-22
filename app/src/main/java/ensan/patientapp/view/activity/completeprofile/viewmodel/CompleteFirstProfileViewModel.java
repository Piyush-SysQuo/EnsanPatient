package ensan.patientapp.view.activity.completeprofile.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import ensan.patientapp.view.activity.completeprofile.model.ProfileResponse;
import ensan.patientapp.view.activity.completeprofile.repository.CompleteFirstProfileRepository;

public class CompleteFirstProfileViewModel extends AndroidViewModel {

    private LiveData<ProfileResponse> profileResponseLiveData;
    private CompleteFirstProfileRepository completeFirstProfileRepository;

    public CompleteFirstProfileViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        completeFirstProfileRepository = new CompleteFirstProfileRepository();
        profileResponseLiveData = completeFirstProfileRepository.profileResponseLiveData();
    }

    public void completeFirstProfile(@NonNull String token, @NonNull String gender, @NonNull String nationality, @NonNull String maritalStatus) {
        completeFirstProfileRepository.callFirstProfilerApi(token,gender,nationality,maritalStatus);
    }

    public LiveData<ProfileResponse> getVolumesResponseLiveData() {
        return profileResponseLiveData;
    }
}
