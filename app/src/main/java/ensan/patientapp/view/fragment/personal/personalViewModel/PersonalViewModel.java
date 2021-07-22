package ensan.patientapp.view.fragment.personal.personalViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.fragment.personal.model.UserProfileResponse;
import ensan.patientapp.view.fragment.personal.repository.PersonalRepository;


public class PersonalViewModel extends AndroidViewModel {

    private LiveData<UserProfileResponse> detailResponseLiveData;
    private PersonalRepository personalRepository;

    public PersonalViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        personalRepository = new PersonalRepository();
        detailResponseLiveData = personalRepository.detailResponseLiveData();
    }

    public void getUserDetails(@NonNull String token) {
        personalRepository.callGetUserDetail(token);
    }

    public LiveData<UserProfileResponse> getVolumesResponseLiveData() {
        return detailResponseLiveData;
    }
}
