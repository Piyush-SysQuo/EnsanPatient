package ensan.patientapp.view.activity.personaldetail.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.personaldetail.model.UpdateProfileResponse;
import ensan.patientapp.view.activity.personaldetail.repository.UpdateProfileRepository;


public class UpdateProfileViewModel extends AndroidViewModel {

    private LiveData<UpdateProfileResponse> allergyResponseLiveData;
    private UpdateProfileRepository updateProfileRepository;

    public UpdateProfileViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        updateProfileRepository = new UpdateProfileRepository();
        allergyResponseLiveData = updateProfileRepository.updateProfileResponseLiveData();
    }

    public void updateProfile(@NonNull String token, @NonNull String email, @NonNull String phone, @NonNull String maritalStatus, @NonNull String emergencyNo, @NonNull String childern, @NonNull String gender, @NonNull String nationality) {
        updateProfileRepository.updateProfile(token,email,phone,maritalStatus,emergencyNo,childern, gender, nationality);
    }

    public LiveData<UpdateProfileResponse> getVolumesResponseLiveData() {
        return allergyResponseLiveData;
    }
}
