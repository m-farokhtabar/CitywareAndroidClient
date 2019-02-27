package ir.rayas.app.citywareclient.ViewModel.Business;

/**
 * Created by m.farokhtabar on 12/08/2018.
 */

public class BusinessViewModel {
    /// <summary>
    /// کد بنگاه        
    /// </summary>
    private int Id;
    /// <summary>
    /// کد کاربر
    /// </summary>
    private int UserId;
    /// <summary>
    /// کد منطقه
    //اجباری
    /// </summary>
    private int RegionId;
    /// <summary>
    /// نام منطقه
    /// خروجی
    /// </summary>     
    private String RegionName;
    /// <summary>
    ///  دسته یا گروه
    //اجباری
    /// </summary>
    private int BusinessCategoryId;
    /// <summary>
    /// نام دسته یا گروه
    /// خروجی
    /// </summary>
    private String BusinessCategoryName;
    /// <summary>
    /// عنوان 
    //اجباری
    //TextMediumSize
    /// </summary>
    private String Title;
    /// <summary>
    /// عنوان کسب و کار
    //اجباری
    //TextMediumSize
    /// </summary>
    private String JobTitle;
    /// <summary>
    /// کلمات کلیدی
    //اختیاری
    //KeyWordsLength
    /// </summary>
    private String Keyword;
    /// <summary>
    /// عرض جغرافیایی
    //اجباری
    /// </summary>
    private double Latitude;
    /// <summary>
    /// طول جغرافیایی
    //اجباری
    /// </summary>        
    private double Longitude;
    /// <summary>
    /// آدرس
    //اجباری
    //TextLongSize
    /// </summary>
    private String Address;
    /// <summary>
    /// باز هست یا خیر
    /// </summary>        
    private boolean IsOpen;
    /// <summary>
    /// پیک دارد یا خیر
    /// </summary>
    private boolean HasDelivery;
    /// <summary>
    /// تعداد کارمندان
    /// </summary>
    private int CountOfEmployees;
    /// <summary>
    /// تاریخ تاسیس
    //اختیاری
    //DateSize
    //PersianDate validation
    /// </summary>
    private String Establishment;
    /// <summary>
    /// کد پستی
    //اختیاری
    //PostalCode validation
    //NationalCodeOrPostalCodeSize
    /// </summary>
    private String PostalCode;
    /// <summary>
    /// توضیحات بنگاه
    //اختیاری
    //MaxOfTextSize
    /// </summary>
    private String Description;
    /// <summary>
    /// پورسانت محصولات بنگاه
    /// در صورتی که محصولات بنگاه پورسانتشان صفر بود از این فیلد خوانده می شود
    //Range(0, 100)
    /// </summary>        
    private double Commission;
    /// <summary>
    /// وضعیت تایید بنگاه
    /// توسط سرور پر می شود
    /// </summary>
    private int ConfirmTypeId;
    /// <summary>
    /// نام نوع تایید
    /// خروجی
    /// </summary>
    private String ConfirmTypeName;
    /// <summary>
    /// توضیحات مربوط به وضعیت تایید یک بنگاه توسط ادمین
    /// توسط سرور پر می شود
    /// </summary>
    private String ConfirmComment;
    /// <summary>
    /// تاریخ ایجاد
    /// توسط سرور پر می شود
    /// </summary>
    private String Create;
    /// <summary>
    /// تاریخ ویرایش
    /// توسط سرور پر می شود
    /// </summary>
    private String Modified;
    /// <summary>
    /// فعال بود یا نبودن
    /// توسط سرور پر می شود
    /// </summary>
    private boolean IsActive;
    /**
     * تصویر پوستر
     */
    private String ImagePathUrl;
    /**
     * امتیاز کسب وکار
     */
    private double TotalScore;
    /**
     * کسب و کار نشان شده است یا خیر
     */
    private boolean IsBookmarked;

    public boolean isBookmarked() {
        return IsBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        IsBookmarked = bookmarked;
    }

    public String getImagePathUrl() {
        return ImagePathUrl;
    }

    public void setImagePathUrl(String imagePathUrl) {
        ImagePathUrl = imagePathUrl;
    }

    public double getTotalScore() {
        return TotalScore;
    }

    public void setTotalScore(double totalScore) {
        TotalScore = totalScore;
    }

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

    public int getRegionId() {
        return RegionId;
    }

    public void setRegionId(int regionId) {
        RegionId = regionId;
    }

    public String getRegionName() {
        return RegionName;
    }

    public void setRegionName(String regionName) {
        RegionName = regionName;
    }

    public int getBusinessCategoryId() {
        return BusinessCategoryId;
    }

    public void setBusinessCategoryId(int businessCategoryId) {
        BusinessCategoryId = businessCategoryId;
    }

    public String getBusinessCategoryName() {
        return BusinessCategoryName;
    }

    public void setBusinessCategoryName(String businessCategoryName) {
        BusinessCategoryName = businessCategoryName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getJobTitle() {
        return JobTitle;
    }

    public void setJobTitle(String jobTitle) {
        JobTitle = jobTitle;
    }

    public String getKeyword() {
        return Keyword;
    }

    public void setKeyword(String keyword) {
        Keyword = keyword;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public boolean isOpen() {
        return IsOpen;
    }

    public void setOpen(boolean open) {
        IsOpen = open;
    }

    public boolean isHasDelivery() {
        return HasDelivery;
    }

    public void setHasDelivery(boolean hasDelivery) {
        HasDelivery = hasDelivery;
    }

    public int getCountOfEmployees() {
        return CountOfEmployees;
    }

    public void setCountOfEmployees(int countOfEmployees) {
        CountOfEmployees = countOfEmployees;
    }

    public String getEstablishment() {
        return Establishment;
    }

    public void setEstablishment(String establishment) {
        Establishment = establishment;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public double getCommission() {
        return Commission;
    }

    public void setCommission(double commission) {
        Commission = commission;
    }

    public int getConfirmTypeId() {
        return ConfirmTypeId;
    }

    public void setConfirmTypeId(int confirmTypeId) {
        ConfirmTypeId = confirmTypeId;
    }

    public String getConfirmTypeName() {
        return ConfirmTypeName;
    }

    public void setConfirmTypeName(String confirmTypeName) {
        ConfirmTypeName = confirmTypeName;
    }

    public String getConfirmComment() {
        return ConfirmComment;
    }

    public void setConfirmComment(String confirmComment) {
        ConfirmComment = confirmComment;
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
