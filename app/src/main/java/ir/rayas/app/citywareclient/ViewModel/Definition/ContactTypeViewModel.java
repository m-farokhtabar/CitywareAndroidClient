package ir.rayas.app.citywareclient.ViewModel.Definition;

/**
 * Created by Programmer on 8/17/2018.
 */

public class ContactTypeViewModel {
    /// <summary>
    /// کد
    /// </summary>
    public int Id;
    /// <summary>
    /// عنوان نوع
    //اجباری
    //TextShortSize
    /// </summary>
    public String Title;
    /// <summary>
    /// نوع تماس که بر اساس آن باید حداکثر طول اطلاعات ورودی و ولیدیشن ورودی تنظیم شود
    //نوع شمارشی ContactType
    /// </summary>
    public int Type;
    /// <summary>
    /// توضیحات
    //اختیاری
    // MaxOfTextSize
    /// </summary>
    public String Description;
    /// <summary>
    /// تاریخ ایجاد
    /// توسط سرور
    /// </summary>
    public String Create;
    /// <summary>
    /// تاریخ ویرایش
    /// </summary>
    public String Modified;
    /// <summary>
    /// فعال بود یا نبودن
    //توسط سرور
    /// </summary>
    public boolean IsActive;

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
