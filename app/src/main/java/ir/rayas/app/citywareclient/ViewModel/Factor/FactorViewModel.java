package ir.rayas.app.citywareclient.ViewModel.Factor;

import java.util.List;

/**
 * Created by Hajar on 2/21/2019.
 */

public class FactorViewModel {

    private int Id;
    private int UserId;
    private int BusinessId;
    private String BusinessName;
    private String UserFullName;
    private Long UserCellPhone;
    private String UserAddress;
    private String BusinessAddress;
    private Integer RegionId;
    private Double UserLatitude;
    private Double UserLongitude;
    private Double BusinessLatitude;
    private Double BusinessLongitude;
    private double TotalPrice;
    private double TotalQuantity;
    private String Create;
    private String UserDescription;
    private String BusinessDescription;
    private boolean HasQuickOrder;
    private int FactorStatusId;
    private String FactorStatusName;
    private double DeliveryCost;
    private List<FactorItemViewModel> ItemList;

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

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }

    public String getUserFullName() {
        return UserFullName;
    }

    public void setUserFullName(String userFullName) {
        UserFullName = userFullName;
    }

    public Long getUserCellPhone() {
        return UserCellPhone;
    }

    public void setUserCellPhone(Long userCellPhone) {
        UserCellPhone = userCellPhone;
    }

    public String getUserAddress() {
        return UserAddress;
    }

    public void setUserAddress(String userAddress) {
        UserAddress = userAddress;
    }

    public String getBusinessAddress() {
        return BusinessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        BusinessAddress = businessAddress;
    }

    public Integer getRegionId() {
        return RegionId;
    }

    public void setRegionId(Integer regionId) {
        RegionId = regionId;
    }

    public Double getUserLatitude() {
        return UserLatitude;
    }

    public void setUserLatitude(Double userLatitude) {
        UserLatitude = userLatitude;
    }

    public Double getUserLongitude() {
        return UserLongitude;
    }

    public void setUserLongitude(Double userLongitude) {
        UserLongitude = userLongitude;
    }

    public Double getBusinessLatitude() {
        return BusinessLatitude;
    }

    public void setBusinessLatitude(Double businessLatitude) {
        BusinessLatitude = businessLatitude;
    }

    public Double getBusinessLongitude() {
        return BusinessLongitude;
    }

    public void setBusinessLongitude(Double businessLongitude) {
        BusinessLongitude = businessLongitude;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }

    public double getTotalQuantity() {
        return TotalQuantity;
    }

    public void setTotalQuantity(double totalQuantity) {
        TotalQuantity = totalQuantity;
    }

    public String getCreate() {
        return Create;
    }

    public void setCreate(String create) {
        Create = create;
    }

    public String getUserDescription() {
        return UserDescription;
    }

    public void setUserDescription(String userDescription) {
        UserDescription = userDescription;
    }

    public String getBusinessDescription() {
        return BusinessDescription;
    }

    public void setBusinessDescription(String businessDescription) {
        BusinessDescription = businessDescription;
    }

    public boolean isHasQuickOrder() {
        return HasQuickOrder;
    }

    public void setHasQuickOrder(boolean hasQuickOrder) {
        HasQuickOrder = hasQuickOrder;
    }

    public int getFactorStatusId() {
        return FactorStatusId;
    }

    public void setFactorStatusId(int factorStatusId) {
        FactorStatusId = factorStatusId;
    }

    public String getFactorStatusName() {
        return FactorStatusName;
    }

    public void setFactorStatusName(String factorStatusName) {
        FactorStatusName = factorStatusName;
    }

    public double getDeliveryCost() {
        return DeliveryCost;
    }

    public void setDeliveryCost(double deliveryCost) {
        DeliveryCost = deliveryCost;
    }

    public List<FactorItemViewModel> getItemList() {
        return ItemList;
    }

    public void setItemList(List<FactorItemViewModel> itemList) {
        ItemList = itemList;
    }
}
