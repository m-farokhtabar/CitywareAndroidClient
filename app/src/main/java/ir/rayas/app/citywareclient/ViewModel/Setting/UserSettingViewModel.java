package ir.rayas.app.citywareclient.ViewModel.Setting;

/** / ویومدل تنظیمات کاربر
 * Created by Programmer on 2/22/2018.
 */
public class UserSettingViewModel
{
    /// <summary>
    /// کد کاربر
    /// </summary>
    private int UserId;
    /// <summary>
    /// محل و موقعیت انتخابی
    /// </summary>
    private Integer RegionId;
    /**
     * نام موقعیت انتخابی
     */
    private String RegionName;
    /// <summary>
    /// دسته بندی بنگاه
    /// </summary>
    private Integer BusinessCategoryId;
    /**
     * نام دسته یا رسته انتخابی
     */
    private String BusinessCategoryName;
    /// <summary>
    /// استفاده از موقعیت فعلی
    /// در صورت فالس بودن بایستی ناحیه انتخاب گردد
    /// </summary>
    private boolean UseGprsPoint;
    /// <summary>
    /// نحوه جستجو بر روی محصولات بنگاه یا همه موارد
    //Enum -> SearchOnDataType
    /// </summary>
    private int SearchOnData;
    /// <summary>
    /// نحوه جستجو بر روی بنگاه های پیک دار
    //->SearchOnDeliveryType
    /// </summary>
    private int SearchOnDelivery;
    /// <summary>
    /// تاریخ ایجاد
    /// سمت سرور
    /// </summary>
    private String Create;
    /// <summary>
    /// تاریخ ویرایش
    /// سمت سرور
    /// </summary>
    private String Modified;

    private Integer GpsRangeInKm ;
    private Double latitude ;
    private Double longitude ;

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

    public void setCreate(String create) {
        Create = create;
    }

    public void setModified(String modified) {
        Modified = modified;
    }

    public String getCreate() {
        return Create;
    }

    public String getModified() {
        return Modified;
    }

    public int getUserId() {

        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public boolean isUseGprsPoint() {
        return UseGprsPoint;
    }

    public void setUseGprsPoint(boolean useGprsPoint) {
        UseGprsPoint = useGprsPoint;
    }

    public int getSearchOnData() {
        return SearchOnData;
    }

    public void setSearchOnData(int searchOnData) {
        SearchOnData = searchOnData;
    }

    public int getSearchOnDelivery() {
        return SearchOnDelivery;
    }

    public void setSearchOnDelivery(int searchOnDelivery) {
        SearchOnDelivery = searchOnDelivery;
    }

    public Integer getBusinessCategoryId() {
        return BusinessCategoryId;
    }

    public void setBusinessCategoryId(Integer businessCategoryId) {
        BusinessCategoryId = businessCategoryId;
    }
    public Integer getRegionId() {
        return RegionId;
    }

    public void setRegionId(Integer regionId) {
        RegionId = regionId;
    }


    public Integer getGpsRangeInKm() {
        return GpsRangeInKm;
    }

    public void setGpsRangeInKm(Integer gpsRangeInKm) {
        GpsRangeInKm = gpsRangeInKm;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}

