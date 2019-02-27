package ir.rayas.app.citywareclient.ViewModel.Definition;

/**
 * Created by Hajar on 7/28/2018.
 */

public class ColorTypeViewModel{
    /// <summary>
    /// کد
    /// </summary>
    private int Id;
    /// <summary>
    /// عنوان نوع
    /// </summary>
    private String Title;
    /// <summary>
    /// مشخص می کند این رنگها برای چه کاری است
    /// مثلا چشم
    /// یا بدن
    /// </summary>
    //type = 0 eye
    //type = 1 body
    //type = 2 Favorite
    public int Type;
    /// <summary>
    /// توضیحات
    /// </summary>
    private String Description;
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

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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
