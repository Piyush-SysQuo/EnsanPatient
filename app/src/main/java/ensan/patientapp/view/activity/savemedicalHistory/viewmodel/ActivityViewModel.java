package ensan.patientapp.view.activity.savemedicalHistory.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.savemedicalHistory.model.ActivityResponse;
import ensan.patientapp.view.activity.savemedicalHistory.repository.SaveActivityRepository;


public class ActivityViewModel extends AndroidViewModel {

    private LiveData<ActivityResponse> dietResponseLiveData;
    private SaveActivityRepository activityRepository;

    public ActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        activityRepository = new SaveActivityRepository();
        dietResponseLiveData = activityRepository.activityResponseLiveData();
    }

    public void saveActivity(@NonNull String token, @NonNull String smoke, @NonNull String exercise, @NonNull String familyID) {
        activityRepository.saveActivity(token,smoke,exercise,familyID);
    }

    public LiveData<ActivityResponse> getVolumesResponseLiveData() {
        return dietResponseLiveData;
    }
}
