package ensan.patientapp.view.activity.privacypolicy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.privacypolicy.model.PrivacySecurityResp;
import ensan.patientapp.view.activity.privacypolicy.repository.PrivacyPolicyRepository;


public class PrivacyPolicyViewModel extends AndroidViewModel {

    private LiveData<PrivacySecurityResp> privacySecurityRespLiveData;
    private PrivacyPolicyRepository privacyPolicyRepository;

    public PrivacyPolicyViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        privacyPolicyRepository = new PrivacyPolicyRepository();
        privacySecurityRespLiveData = privacyPolicyRepository.privacySecurityRespLiveData();
    }

    public void getAboutText(@NonNull String token,@NonNull String language) {
        privacyPolicyRepository.getPrivacy(token,language);
    }

    public LiveData<PrivacySecurityResp> getVolumesResponseLiveData() {
        return privacySecurityRespLiveData;
    }
}
