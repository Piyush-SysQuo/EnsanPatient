package ensan.patientapp.view.activity.notification.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.notification.model.NotificationResponse;
import ensan.patientapp.view.activity.notification.repository.NotificationRepository;


public class NotificationViewModel extends AndroidViewModel {

    private LiveData<NotificationResponse> notificationResponseLiveData;
    private NotificationRepository notificationRepository;

    public NotificationViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        notificationRepository = new NotificationRepository();
        notificationResponseLiveData = notificationRepository.notificationResponseLiveData();
    }

    public void getNotification(@NonNull String token) {
        notificationRepository.getNotification(token);
    }

    public LiveData<NotificationResponse> getVolumesResponseLiveData() {
        return notificationResponseLiveData;
    }
}
