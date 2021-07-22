package ensan.patientapp.view.activity.termsandcondition.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.privacypolicy.model.PrivacySecurityResp;
import ensan.patientapp.view.activity.termsandcondition.repository.TermsRepository;


public class TermsViewModel extends AndroidViewModel {

    private LiveData<PrivacySecurityResp> privacySecurityRespLiveData;
    private TermsRepository termsRepository;

    public TermsViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        termsRepository = new TermsRepository();
        privacySecurityRespLiveData = termsRepository.privacySecurityRespLiveData();
    }

    public void getTermsText(@NonNull String token,@NonNull String language) {
        termsRepository.getTerms(token,language);
    }

    public LiveData<PrivacySecurityResp> getVolumesResponseLiveData() {
        return privacySecurityRespLiveData;
    }
}
