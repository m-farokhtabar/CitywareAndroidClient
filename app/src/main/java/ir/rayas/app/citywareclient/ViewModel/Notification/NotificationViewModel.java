package ir.rayas.app.citywareclient.ViewModel.Notification;


import java.util.List;

/** مشخصات یک نوتیفیکیشن
 * Created by Programmer on 1/3/2019.
 */
public class NotificationViewModel {
    /**
     * کد نوتیفیکشن
     */
    private int Id;
    /**
     * عنوان نوتیفیکیشن
     */
    private String Title;
    /**
     * پیام نوتیفیکیشن
     */
    private String Message;
    /**
     * لیست فایلهای نوتیفیکیشن
     * نمونه
     * [{"FileType":1,"IsDefault":0,"Url":"~/Upload/Book.PDF"},{"FileType":0,"IsDefault":1,"Url":"~/Upload/Book.jpg"}]
     * از نوع FileType استفاده شود
     */
    private String Attachments;
    /**
     * وضعیت نوتیفیکیشن
     * نوع شمارشی NotificationStatus
     */
    private int Status;
    /**
     * تاریخ ایجاد
     */
    private String Create;
    /**
     * تاریخ ارسال
     */
    private String SendDate;
    /**
     * تاریخ انقضاء
     */
    private String ExpireDate;

    private List<Integer> UserIdList;

    public List<Integer> getUserIdList() {
        return UserIdList;
    }

    public void setUserIdList(List<Integer> userIdList) {
        UserIdList = userIdList;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getAttachments() {
        return Attachments;
    }

    public void setAttachments(String attachments) {
        Attachments = attachments;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getCreate() {
        return Create;
    }

    public void setCreate(String create) {
        Create = create;
    }

    public String getSendDate() {
        return SendDate;
    }

    public void setSendDate(String sendDate) {
        SendDate = sendDate;
    }

    public String getExpireDate() {
        return ExpireDate;
    }

    public void setExpireDate(String expireDate) {
        ExpireDate = expireDate;
    }
}
