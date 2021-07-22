package ensan.patientapp.view.fragment.personal.personalViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.fragment.personal.model.ProfilePicResponse;
import ensan.patientapp.view.fragment.personal.repository.ProfilePicRepository;
import okhttp3.MultipartBody;

public class ProfilePicViewModel extends AndroidViewModel {

    private LiveData<ProfilePicResponse> ProfilePicResponse;
    private ProfilePicRepository profilePicRepository;

    public ProfilePicViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        profilePicRepository = new ProfilePicRepository();
        ProfilePicResponse = profilePicRepository.profileResponseLiveData();
    }

    public void uploadProfilePic(@NonNull String token, MultipartBody.Part part) {
        profilePicRepository.callUpdateProfilePic(token,part);
    }

    public LiveData<ProfilePicResponse> getVolumesResponseLiveData() {
        return ProfilePicResponse;
    }
}
