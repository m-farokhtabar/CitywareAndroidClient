package ir.rayas.app.citywareclient.ViewModel.User;

/**
 * Created by Programmer on 2/4/2018.
 */
/// <summary>
/// ویومدل کاربر/
/// </summary>
public class UserViewModel
{
    /// <summary>
    /// کلید اصلی
    /// </summary>
    private int Id;
    /// <summary>
    /// نام مستعار
    /// می تواند تکراری باشد
    /// </summary>
    private String NickName;
    /// <summary>
    /// تولید یک کد منحصر بفرد برای هر کاربر
    /// توسط سرور
    /// </summary>
    private String Key;
    /// <summary>
    /// همان نام کاربری است
    /// باید منحصر بفرد باشد
    /// </summary>
    private long CellPhone;
    /// <summary>
    /// نام
    /// </summary>
    private String Name;
    /// <summary>
    /// نام خانوادگی
    /// </summary>
    private String Family;
    /// <summary>
    /// تاریخ ایجاد
    /// توسط سرور
    /// </summary>
    private String Create;
    /// <summary>
    /// تاریخ ویرایش
    /// توسط سرور
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

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getKey() {
        return Key;
    }

    public long getCellPhone() {
        return CellPhone;
    }

    public void setCellPhone(long cellPhone) {
        CellPhone = cellPhone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFamily() {
        return Family;
    }

    public void setFamily(String family) {
        Family = family;
    }

    public String getCreate() {
        return Create;
    }

    public String getModified() {
        return Modified;
    }


    public boolean isActive() {
        return IsActive;
    }
}


