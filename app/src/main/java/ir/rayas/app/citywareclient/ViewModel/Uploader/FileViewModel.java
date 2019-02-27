package ir.rayas.app.citywareclient.ViewModel.Uploader;

/** دریافت اطلاعات فایل آپلو شده
 * Created by Programmer on 8/26/2018.
 */

public class FileViewModel {
    /// <summary>
    /// نام فایل ارسالی از سمت کلاینت
    /// فقط جهت شناسایی فایل کارایی دارد
    /// </summary>
    private String FileName;
    /// <summary>
    /// مسیر کامل فایل آپلو شده
    /// مسیر فایل همراه نام جدید فایل
    /// </summary>
    private String Url;
    /// <summary>
    /// پیام ارسالی از سرور
    /// </summary>
    private String Message;
    /// <summary>
    /// وضعیت فایل
    /// اگر 0 بر گرداند یعنی همه چیز درست است
    /// </summary>
    private int Status;

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
