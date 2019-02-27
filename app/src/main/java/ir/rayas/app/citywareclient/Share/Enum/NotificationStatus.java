package ir.rayas.app.citywareclient.Share.Enum;

/** وضعیت نوتیفیکیشن
 * البته موارد اول و دوم لازم نیست فقط جهت منطبق بودن با سرور اضافه شده است
 * Created by Programmer on 1/3/2019.
 */
public enum NotificationStatus {
    NotSend(0),
    Sent(1),
    Delivered(2),
    Read(3),
    Deleted(4);
    private int Id;

    public int getId() {
        return Id;
    }

    NotificationStatus(int id) {
        Id = id;
    }



}
