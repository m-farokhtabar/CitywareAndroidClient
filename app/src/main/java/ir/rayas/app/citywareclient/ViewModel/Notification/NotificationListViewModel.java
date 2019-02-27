package ir.rayas.app.citywareclient.ViewModel.Notification;

import java.util.List;

/**
 * Created by Hajar on 1/22/2019.
 */

public class NotificationListViewModel {

    private List<NotificationViewModel>  NotificationListViewModel;

    public List<NotificationViewModel> getNotificationListViewModel() {
        return NotificationListViewModel;
    }

    public void setNotificationListViewModel(List<NotificationViewModel> notificationListViewModel) {
        NotificationListViewModel = notificationListViewModel;
    }
}
