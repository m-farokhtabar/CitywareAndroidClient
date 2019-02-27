package ir.rayas.app.citywareclient.ViewModel.Definition;

/**
 * Created by Hajar on 7/28/2018.
 */

public class SexTypeViewModel{
    /// <summary>
    /// کد
    /// </summary>
    private int Id;
    /// <summary>
    /// عنوان نوع
    /// </summary>
    private String Title;
    /// <summary>
    /// تاریخ ایجاد
    /// </summary>
    private String Create;
    /// <summary>
    /// تاریخ ویرایش
    /// </summary>
    private String Modified;
    /// <summary>
    /// فعال بود یا نبودن
    /// </summary>
    private boolean IsActive;

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

    public String getCreate() {
        return Create;
    }

    public void setCreate(String create) {
        Create = create;
    }

    public String getModified() {
        return Modified;
    }

    public void setModified(String modified) {
        Modified = modified;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }
}
