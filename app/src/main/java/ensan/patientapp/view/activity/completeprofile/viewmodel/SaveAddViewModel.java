package ensan.patientapp.view.activity.completeprofile.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.completeprofile.model.ProfileResponse;
import ensan.patientapp.view.activity.completeprofile.repository.SaveAddressRepository;
import okhttp3.RequestBody;


public class SaveAddViewModel extends AndroidViewModel {

    private LiveData<ProfileResponse> profileResponseLiveData;
    private SaveAddressRepository profileFirstRepository;

    public SaveAddViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        profileFirstRepository = new SaveAddressRepository();
        profileResponseLiveData = profileFirstRepository.profileResponseLiveData();
    }

    public void saveAddress(@NonNull String token, @NonNull RequestBody address, @NonNull RequestBody poBox, @NonNull RequestBody city , @NonNull RequestBody country, @NonNull RequestBody familyId, @NonNull RequestBody lat, @NonNull RequestBody lng, @NonNull RequestBody tag) {
        profileFirstRepository.saveAddress(token,address,poBox,city,country,familyId,lat,lng,tag);
    }

    public LiveData<ProfileResponse> getVolumesResponseLiveData() {
        return profileResponseLiveData;
    }
}
