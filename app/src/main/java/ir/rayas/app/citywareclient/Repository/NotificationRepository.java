package ir.rayas.app.citywareclient.Repository;

import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Helper.SharedPreferenceManager;
import ir.rayas.app.citywareclient.ViewModel.Notification.NotificationListViewModel;

/**
 * Created by Hajar on 1/22/2019.
 */

public class NotificationRepository {

    private static NotificationListViewModel NotificationList;

    /**
     * دریافت لیست پیام ها از کش
     *
     * @return
     */
    public NotificationListViewModel getAllNotification() {
        if (NotificationList == null) {
            SharedPreferenceManager ShManager = new SharedPreferenceManager();
            if (ShManager.IsContain(DefaultConstant.NotificationKey)) {
                NotificationList = ShManager.GetClass(DefaultConstant.NotificationKey, NotificationListViewModel.class);
            }
        }
        return NotificationList;
    }

    /**
     * ویرایش یا اضافه کردن پیام ها به کش
     *
     * @param notificationList
     */
    public void setAllNotification(NotificationListViewModel notificationList) {
        SharedPreferenceManager ShManager = new SharedPreferenceManager();
        if (ShManager.SetClass(DefaultConstant.NotificationKey, notificationList)) {
            NotificationList = notificationList;
        } else {
            NotificationList = null;
        }

    }


    /**
     * حذف پیام ها از کش
     */
    public void ClearAllNotification() {
        SharedPreferenceManager ShManager = new SharedPreferenceManager();
        ShManager.Clear(DefaultConstant.NotificationKey);
        NotificationList = null;

    }


}
