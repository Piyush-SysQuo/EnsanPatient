package ensan.patientapp.view.fragment.personal.personalViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.fragment.personal.model.CoverPicResponse;
import ensan.patientapp.view.fragment.personal.repository.CoverPicRepository;
import okhttp3.MultipartBody;

public class CoverPicViewModel extends AndroidViewModel {

    private LiveData<CoverPicResponse> CoverPicResponse;
    private CoverPicRepository coverPicRepository;

    public CoverPicViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        coverPicRepository = new CoverPicRepository();
        CoverPicResponse = coverPicRepository.profileResponseLiveData();
    }

    public void uploadCoverPic(@NonNull String token, MultipartBody.Part part) {
        coverPicRepository.callUpdateCoverPic(token,part);
    }

    public LiveData<CoverPicResponse> getVolumesResponseLiveData() {
        return CoverPicResponse;
    }
}
