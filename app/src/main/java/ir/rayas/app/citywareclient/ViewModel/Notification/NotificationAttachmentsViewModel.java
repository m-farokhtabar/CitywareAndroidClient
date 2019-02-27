package ir.rayas.app.citywareclient.ViewModel.Notification;

/**
 * Created by Hajar on 1/4/2019.
 */
public class NotificationAttachmentsViewModel {

    /**
     * از نوع FileType استفاده شود
     */
    private int FileType;
    private boolean IsDefault;
    private String Url;
    private String Title;


    public int getFileType() {
        return FileType;
    }

    public void setFileType(int fileType) {
        FileType = fileType;
    }

    public boolean isDefault() {
        return IsDefault;
    }

    public void setDefault(boolean aDefault) {
        IsDefault = aDefault;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
