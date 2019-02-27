package ir.rayas.app.citywareclient.ViewModel.Business;

/**
 * Created by Programmer on 11/30/2018.
 */

/**
 * ویومدل امتیاز ثبت شده کاربر برای یک کسب وکار
 */
public class ScoreViewModel {
    /// <summary>
    /// کد
    /// </summary>
    private int Id;
    /// <summary>
    /// کد کاربر
    /// </summary>
    private int UserId;
    /// <summary>
    /// کد بنگاه
    /// </summary>
    private int BusinessId;
    /// <summary>
    /// امتیاز هر کاربر به کسب و کار
    /// </summary>
    private double Value;
    /// <summary>
    /// تاریخ ایجاد
    /// </summary>
    private String Create;
    /// <summary>
    /// تاریخ ویرایش
    /// </summary>
    public String Modified;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getBusinessId() {
        return BusinessId;
    }

    public void setBusinessId(int businessId) {
        BusinessId = businessId;
    }

    public double getValue() {
        return Value;
    }

    public void setValue(double value) {
        Value = value;
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
}
