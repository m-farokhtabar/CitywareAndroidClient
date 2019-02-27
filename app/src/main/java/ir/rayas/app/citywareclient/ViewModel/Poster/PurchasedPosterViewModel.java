package ir.rayas.app.citywareclient.ViewModel.Poster;

/**
 * Created by Programmer on 10/11/2018.
 */

public class PurchasedPosterViewModel {

    /// <summary>
    /// کلید اصلی
    /// </summary>
    private int Id;
    /// <summary>
    /// عنوان
    /// </summary>
    /// اجباری
    /// TextShortSize
    private String Title;
    /// <summary>
    /// کد کسب و کار
    /// </summary>
    /// اجباری
    private int BusinessId;
    /// <summary>
    /// کسب و کار
    /// </summary>
    ///فقط جهت نمایش
    private String BusinessName;
    /// <summary>
    /// کد نوع پوستر انتخابی
    /// </summary>
    ///اجباری انتخابی
    private int PosterTypeId;
    /// <summary>
    /// اطلاعات تکمیلی نوع پوستر
    /// </summary>
    ///فقط جهت نمایش استفاده می شود
    private PosterTypeViewModel PosterType;
    /// <summary>
    /// خلاصه توضیحات پوستر
    /// </summary>
    ///اجباری
    ///TextLongSize
    private String AbstractOfDescription;
    /// <summary>
    /// تصویر پوستر
    /// </summary>
    ///تصویر اجباری است
    private String ImagePathUrl;
    /// <summary>
    /// تاریخ انقضای پوستر به ساعت
    /// </summary>
    ///اجباری
    private int ExpireHourTime;
    /// <summary>
    /// تاریخ انقضاء
    /// </summary>
    ///فقط جهت نمایش
    private String Expire;
    /// <summary>
    /// تاریخ فعلی
    /// </summary>
    ///تاریخ جاری سرور فقط جهت نمایش  و مقایسه برای اینکه با تاریخ انقضاء مقایسه کنیم ببینیم منقضی شده یا خیر
    private String CurrentDate;
    /// <summary>
    ///قیمت پوستر به ساعت
    /// </summary>
    ///فقط جهت نمایش
    private double Price;
    /// <summary>
    /// توضیحات محصول
    /// </summary>
    ///اختیاری
    ///MaxOfTextSize
    private String Description;
    /// <summary>
    /// ترتیب نمایش مابیین پوستر های خود کاربر
    /// </summary>
    ///اجباری
    private int Order;
    /// <summary>
    /// تاریخ ایجاد
    /// </summary>
    ///جهت نمایش
    private String Create;
    /// <summary>
    /// تاریخ ویرایش
    /// </summary>
    ///جهت نمایش
    private String Modified;
    /// <summary>
    /// فعال بود یا نبودن
    /// </summary>
    ///جهت نمایش در صورت غیر فعال بودن در لیست غیر فعال شود
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

    public int getBusinessId() {
        return BusinessId;
    }

    public void setBusinessId(int businessId) {
        BusinessId = businessId;
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }

    public int getPosterTypeId() {
        return PosterTypeId;
    }

    public void setPosterTypeId(int posterTypeId) {
        PosterTypeId = posterTypeId;
    }

    public PosterTypeViewModel getPosterType() {
        return PosterType;
    }

    public void setPosterType(PosterTypeViewModel posterType) {
        PosterType = posterType;
    }

    public String getAbstractOfDescription() {
        return AbstractOfDescription;
    }

    public void setAbstractOfDescription(String abstractOfDescription) {
        AbstractOfDescription = abstractOfDescription;
    }

    public String getImagePathUrl() {
        return ImagePathUrl;
    }

    public void setImagePathUrl(String imagePathUrl) {
        ImagePathUrl = imagePathUrl;
    }

    public int getExpireHourTime() {
        return ExpireHourTime;
    }

    public void setExpireHourTime(int expireHourTime) {
        ExpireHourTime = expireHourTime;
    }

    public String getExpire() {
        return Expire;
    }

    public void setExpire(String expire) {
        Expire = expire;
    }

    public String getCurrentDate() {
        return CurrentDate;
    }

    public void setCurrentDate(String currentDate) {
        CurrentDate = currentDate;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getOrder() {
        return Order;
    }

    public void setOrder(int order) {
        Order = order;
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
