package ensan.patientapp.view.activity.notification.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ensan.patientapp.view.activity.notification.model.NotificationResponse;
import ensan.patientapp.view.activity.notification.repository.ReadNotificationRepository;


public class ReadNotificationViewModel extends AndroidViewModel {

    private LiveData<NotificationResponse> notificationResponseLiveData;
    private ReadNotificationRepository notificationRepository;

    public ReadNotificationViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        notificationRepository = new ReadNotificationRepository();
        notificationResponseLiveData = notificationRepository.notificationResponseLiveData();
    }

    public void readNotification(@NonNull String token,@NonNull String notificationId) {
        notificationRepository.readNotification(token,notificationId);
    }

    public LiveData<NotificationResponse> getVolumesResponseLiveData() {
        return notificationResponseLiveData;
    }
}
