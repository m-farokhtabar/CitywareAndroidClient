package ir.rayas.app.citywareclient.ViewModel.Basket;

/**
 * Created by Hajar on 2/13/2019.
 */

public class BasketSummeryViewModel {

    private int BusinessId;
    private String Path;
    private double TotalPrice;
    private String Modified;
    private int BasketCount;
    private int UserId;
    private int BasketId;
    private int UserAddressId;
    private int LastSelectedPositionAddress;
    private String BasketName;
    private int PriceDelivery;
    private boolean BusinessIsDelivery;
    private boolean IsDelivery;
    private String UserDescription;
    private String CurrentAddress;
    private double UserLatitude;
    private double UserLongitude;
    private double BasketLatitude;
    private double BasketLongitude;
    private boolean IsQuickItem;

    public int getLastSelectedPositionAddress() {
        return LastSelectedPositionAddress;
    }

    public void setLastSelectedPositionAddress(int lastSelectedPositionAddress) {
        LastSelectedPositionAddress = lastSelectedPositionAddress;
    }

    public int getUserAddressId() {
        return UserAddressId;
    }

    public void setUserAddressId(int userAddressId) {
        UserAddressId = userAddressId;
    }

    public int getBusinessId() {
        return BusinessId;
    }

    public void setBusinessId(int businessId) {
        BusinessId = businessId;
    }


    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getModified() {
        return Modified;
    }

    public void setModified(String modified) {
        Modified = modified;
    }

    public int getBasketId() {
        return BasketId;
    }

    public void setBasketId(int basketId) {
        BasketId = basketId;
    }

    public String getBasketName() {
        return BasketName;
    }

    public void setBasketName(String basketName) {
        BasketName = basketName;
    }

    public boolean isBusinessIsDelivery() {
        return BusinessIsDelivery;
    }

    public void setBusinessIsDelivery(boolean businessIsDelivery) {
        BusinessIsDelivery = businessIsDelivery;
    }

    public boolean isDelivery() {
        return IsDelivery;
    }

    public void setDelivery(boolean delivery) {
        IsDelivery = delivery;
    }

    public String getUserDescription() {
        return UserDescription;
    }

    public void setUserDescription(String userDescription) {
        UserDescription = userDescription;
    }


    public String getCurrentAddress() {
        return CurrentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        CurrentAddress = currentAddress;
    }

    public double getUserLatitude() {
        return UserLatitude;
    }

    public void setUserLatitude(double userLatitude) {
        UserLatitude = userLatitude;
    }

    public double getUserLongitude() {
        return UserLongitude;
    }

    public void setUserLongitude(double userLongitude) {
        UserLongitude = userLongitude;
    }

    public double getBasketLatitude() {
        return BasketLatitude;
    }

    public void setBasketLatitude(double basketLatitude) {
        BasketLatitude = basketLatitude;
    }

    public double getBasketLongitude() {
        return BasketLongitude;
    }

    public void setBasketLongitude(double basketLongitude) {
        BasketLongitude = basketLongitude;
    }

    public int getBasketCount() {
        return BasketCount;
    }

    public void setBasketCount(int basketCount) {
        BasketCount = basketCount;
    }

    public int getPriceDelivery() {
        return PriceDelivery;
    }

    public void setPriceDelivery(int priceDelivery) {
        PriceDelivery = priceDelivery;
    }

    public boolean isQuickItem() {
        return IsQuickItem;
    }

    public void setQuickItem(boolean quickItem) {
        IsQuickItem = quickItem;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }
}
