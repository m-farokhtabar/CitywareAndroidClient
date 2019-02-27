package ir.rayas.app.citywareclient.ViewModel.Business;

/**
 * Created by Programmer on 10/11/2018.
 */

public class BookmarkViewModel {
    /// <summary>
    ///  کد کسب و کار
    /// </summary>     
    private int BusinessId;
    /// <summary>
    /// کد کاربر
    /// </summary>     
    private int UserId;
    /// <summary>
    /// نام دسته یا گروه
    /// خروجی
    /// </summary>     
    private String RegionName;
    /// <summary>
    /// نام دسته یا گروه
    /// خروجی
    /// </summary>
    private String BusinessCategoryName;
    /// <summary>
    /// تصویر یک پوستر جهت نمایش
    /// </summary>                
    private String ImagePathUrl;
    /// <summary>
    /// عنوان 
    /// </summary>                
    private String BusinessTitle;
    /// <summary>
    /// عنوان کسب و کار
    /// </summary>        
    private String JobTitle;
    /// <summary>
    /// کلمات کلیدی
    /// </summary>        
    private String Keyword;
    /// <summary>
    /// عرض جغرافیایی
    /// </summary>
    private double Latitude;
    /// <summary>
    /// طول جغرافیایی
    /// </summary>        
    private double Longitude;
    /// <summary>
    /// آدرس
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
    /// امتیاز این کسب و کار از دید کاربران
    /// </summary>
    private double TotalScore;
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

    public double getTotalScore() {
        return TotalScore;
    }

    public void setTotalScore(double totalScore) {
        TotalScore = totalScore;
    }

    public int getBusinessId() {
        return BusinessId;
    }

    public void setBusinessId(int businessId) {
        BusinessId = businessId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getRegionName() {
        return RegionName;
    }

    public void setRegionName(String regionName) {
        RegionName = regionName;
    }

    public String getBusinessCategoryName() {
        return BusinessCategoryName;
    }

    public void setBusinessCategoryName(String businessCategoryName) {
        BusinessCategoryName = businessCategoryName;
    }

    public String getImagePathUrl() {
        return ImagePathUrl;
    }

    public void setImagePathUrl(String imagePathUrl) {
        ImagePathUrl = imagePathUrl;
    }

    public String getBusinessTitle() {
        return BusinessTitle;
    }

    public void setBusinessTitle(String businessTitle) {
        BusinessTitle = businessTitle;
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
