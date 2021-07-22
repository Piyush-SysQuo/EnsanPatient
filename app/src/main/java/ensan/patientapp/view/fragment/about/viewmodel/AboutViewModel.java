package ensan.patientapp.view.fragment.about.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.fragment.about.model.AboutResponse;
import ensan.patientapp.view.fragment.about.repository.AboutRepository;


public class AboutViewModel extends AndroidViewModel {

    private LiveData<AboutResponse> aboutResponseLiveData;
    private AboutRepository aboutRepository;

    public AboutViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        aboutRepository = new AboutRepository();
        aboutResponseLiveData = aboutRepository.aboutResponseLiveData();
    }

    public void getAboutText(@NonNull String token,@NonNull String language) {
        aboutRepository.getAbout(token,language);
    }

    public LiveData<AboutResponse> getVolumesResponseLiveData() {
        return aboutResponseLiveData;
    }
}
