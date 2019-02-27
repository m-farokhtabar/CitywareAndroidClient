package ir.rayas.app.citywareclient.ViewModel.Business;

/** اطلاعات تماس بنگاه اقتصادی
 * Created by Programmer on 8/17/2018.
 */

public class BusinessContactViewModel {
    /// <summary>
    /// کد تماس
    /// </summary>        
    private int Id;
    /// <summary>
    /// کد بنگاه
    //اجباری
    /// </summary>
    private int BusinessId;
    /// <summary>
    /// نوع تماس
    //اجباری باید از spinner انتخاب شود
    //spinner باید از ContactTypeViewModel پر شود
    /// </summary>
    private int TypeId;
    /// <summary>
    /// نام نوع انتخابی
    /// </summary>
    private String TypeName;
    /// <summary>
    /// نوع شمارشی راه های تماس
    /// </summary>
    private int ContactType;
    /// <summary>
    /// عنوان
    //اجباری
    ///TextMediumSize
    /// </summary>
    private String Title;
    /// <summary>
    /// مقدار تماس
    //اجباری
    //TextLongSize --> حداکثر طول آن می باشد
    //بر روی این فیلد باید به طور پویا ماکزیمم ورودی و ولیدیشن را تنظیم نمود
    //پیدا کردن validation از روی جدول ContactTypeViewModel خواهد بود و فیلد Type آن که مشخص می کند این ورودی از چه نوعی است
    //برای راحتی کار با فیلد Type در جدول ContactTypeViewModel یک نوع شمارشی به نام ContactType در پوشه enum تعریف شده است
    /// </summary>
    private String Value;
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
    //توسط سرور
    /// </summary>
    private boolean IsActive;

    public int getContactType() {
        return ContactType;
    }

    public void setContactType(int contactType) {
        ContactType = contactType;
    }

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

    public int getTypeId() {
        return TypeId;
    }

    public void setTypeId(int typeId) {
        TypeId = typeId;
    }

    public String getTitle() {
        return Title;
    }

    public String getTypeName() {
        return TypeName;
    }

    public void setTypeName(String typeName) {
        TypeName = typeName;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
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

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }
}
