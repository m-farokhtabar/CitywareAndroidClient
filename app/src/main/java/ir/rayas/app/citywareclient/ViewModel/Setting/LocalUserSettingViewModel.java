package ir.rayas.app.citywareclient.ViewModel.Setting;



public class LocalUserSettingViewModel {

    private Integer RegionId;
    private Integer BusinessCategoryId;
    private boolean UseGprsPoint;
    private Integer GpsRangeInKm ;
    private Double latitude ;
    private Double longitude ;


    public Integer getRegionId() {
        return RegionId;
    }

    public void setRegionId(Integer regionId) {
        RegionId = regionId;
    }

    public Integer getBusinessCategoryId() {
        return BusinessCategoryId;
    }

    public void setBusinessCategoryId(Integer businessCategoryId) {
        BusinessCategoryId = businessCategoryId;
    }

    public boolean isUseGprsPoint() {
        return UseGprsPoint;
    }

    public void setUseGprsPoint(boolean useGprsPoint) {
        UseGprsPoint = useGprsPoint;
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
