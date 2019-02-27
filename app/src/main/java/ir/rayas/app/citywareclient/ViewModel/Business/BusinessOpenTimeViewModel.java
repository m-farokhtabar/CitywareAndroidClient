package ir.rayas.app.citywareclient.ViewModel.Business;

/** زمان باز بودن بنگاه اقتصادی در هفته
 * Created by Programmer on 8/17/2018.
 */

public class BusinessOpenTimeViewModel {
    /// <summary>
    /// کد زمان باز بودن
    /// </summary>
    private int Id;
    /// <summary>
    /// کد بنگاه
    //اجباری
    /// </summary>
    private int BusinessId;
    /// <summary>
    /// از ساعت
    //اجباری/
    ///TimeSize
    ///Validation --> PersianTimeRegular
    /// </summary>
    private String From;
    /// <summary>
    /// تا ساعت
    //اجباری/
    ///TimeSize
    ///Validation --> PersianTimeRegular
    /// </summary>
    private String To;
    /// <summary>
    /// روز هفته
    /// 0 شنبه
    /// .
    /// .
    /// 6 جمعه
    /// از Sppiner استفاده شود
    ///اجباری
    /// </summary>
    private int DayOfWeek;
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
    /// وضعیت فعال بودن
    /// توسط سرور
    /// </summary>
    private boolean IsActive;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getBusinessId() {
        return BusinessId;
    }

    public void setBusinessId(int businessId) {
        BusinessId = businessId;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public int getDayOfWeek() {
        return DayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        DayOfWeek = dayOfWeek;
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
