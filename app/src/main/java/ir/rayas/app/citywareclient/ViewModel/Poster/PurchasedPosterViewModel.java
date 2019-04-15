package ir.rayas.app.citywareclient.ViewModel.Poster;

import java.util.List;


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
    //عنوان نوع پوستر
    private String PosterTypeTitle;
    //    آرشیو
    //    همیشه پوستر در بالا نمایش داده شود
    //    در صورتی همیشه بالا است که کاربر در منطقه مورد نظر باشد
    //    با فیلد اولویت تفاوت دارد
    private boolean IsTop;
    //    آرشیو
    // اندازه تعداد سطر
    private int Rows;
    //    آرشیو
    // اندازه تعداد ستون
    private int Cols;
    //   آرشیو
    //    اگر اولیت 0 باشد پوستر معمولی است در غیر این صورت به ترتیب عدد اولویت در قسمت پوسترها نمایش داده می شود بدون اینکه مکان کاربر را در نظر بگیرد
    private int Priority;
    //قیمت پوستر به ساعت
    //  هزینه یک ساعت پوستر
    //    به این دلیل لیست چون به ازای هر تمدید ممکنه قیمت واحد تغییر کنه
    private List<Double> PriceList;
    /// <summary>
    /// تاریخ انقضای پوستر به ساعت
    /// </summary>
    ///اجباری
    private int ExpireHourTime;
    //  هزینه پرداختی برای پوستر
    // هزینه یک ساعت * تعداد ساعات خریداری شده
    private double PosterPrice;
    // منقضی شده است یا خیر
    private boolean IsExpired;
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
    /// تاریخ انقضاء
    /// </summary>
    ///فقط جهت نمایش
    private String ExpireDate;

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

    public String getPosterTypeTitle() {
        return PosterTypeTitle;
    }

    public void setPosterTypeTitle(String posterTypeTitle) {
        PosterTypeTitle = posterTypeTitle;
    }

    public boolean isTop() {
        return IsTop;
    }

    public void setTop(boolean top) {
        IsTop = top;
    }

    public int getRows() {
        return Rows;
    }

    public void setRows(int rows) {
        Rows = rows;
    }

    public int getCols() {
        return Cols;
    }

    public void setCols(int cols) {
        Cols = cols;
    }

    public int getPriority() {
        return Priority;
    }

    public void setPriority(int priority) {
        Priority = priority;
    }

    public List<Double> getPriceList() {
        return PriceList;
    }

    public void setPriceList(List<Double> priceList) {
        PriceList = priceList;
    }

    public int getExpireHourTime() {
        return ExpireHourTime;
    }

    public void setExpireHourTime(int expireHourTime) {
        ExpireHourTime = expireHourTime;
    }

    public double getPosterPrice() {
        return PosterPrice;
    }

    public void setPosterPrice(double posterPrice) {
        PosterPrice = posterPrice;
    }

    public boolean isExpired() {
        return IsExpired;
    }

    public void setExpired(boolean expired) {
        IsExpired = expired;
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

    public String getExpireDate() {
        return ExpireDate;
    }

    public void setExpireDate(String expireDate) {
        ExpireDate = expireDate;
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
