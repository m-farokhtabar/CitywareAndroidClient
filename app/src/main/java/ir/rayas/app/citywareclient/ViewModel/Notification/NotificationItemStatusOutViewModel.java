package ir.rayas.app.citywareclient.ViewModel.Notification;
import ir.rayas.app.citywareclient.Share.Enum.NotificationStatus;

/** جهت ارسال وضعیت برای یک نوتیفیکیشن
 * Created by Programmer on 1/3/2019.
 */
public class NotificationItemStatusOutViewModel {
    /**
     * کد کاربر
     * اجباری
     */
    private int UserId;
    /**
     * کد آیتم نوتیفیکیشن
     * اجباری
     */
    private int NotificationItemId;
    /**
     *  وضعیت جدید نوتیفیکیشن
     *  اجباری
     */
    private  int NotificationStatus;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getNotificationItemId() {
        return NotificationItemId;
    }

    public void setNotificationItemId(int notificationItemId) {
        NotificationItemId = notificationItemId;
    }

    public int getNotificationStatus() {
        return NotificationStatus;
    }

    public void setNotificationStatus(int notificationStatus) {
        NotificationStatus = notificationStatus;
    }
}
