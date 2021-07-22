package ensan.patientapp.view.activity.editmedicalhistory.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.editmedicalhistory.model.UpdateProfileInfoResponse;
import ensan.patientapp.view.activity.editmedicalhistory.repository.UpdateProfileInfoRepository;


public class UpdateProfileInfoResponseViewModel extends AndroidViewModel {

    private LiveData<UpdateProfileInfoResponse> updateProfileInfoResponseLiveData;
    private UpdateProfileInfoRepository updateProfileInfoRepository;

    public UpdateProfileInfoResponseViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        updateProfileInfoRepository = new UpdateProfileInfoRepository();
        updateProfileInfoResponseLiveData = updateProfileInfoRepository.updateProfileInfoResponseLiveData();
    }

    // update profile info
    public void updateProfileInfo(@NonNull String token, @NonNull String bloodGroup, @NonNull String height, @NonNull String weight) {
        updateProfileInfoRepository.updateProfileInfo(token,bloodGroup,height,weight);
    }

    public LiveData<UpdateProfileInfoResponse> getVolumesResponseLiveData() {
        return updateProfileInfoResponseLiveData;
    }
}
