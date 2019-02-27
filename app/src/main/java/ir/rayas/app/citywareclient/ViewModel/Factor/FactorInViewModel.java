package ir.rayas.app.citywareclient.ViewModel.Factor;

/**
 * Created by Hajar on 2/20/2019.
 */

public class FactorInViewModel {

    private int FactorId;
    private int UserId;
    private String UserAddress;
    private double UserLatitude;
    private double UserLongitude;
    private String UserDescription;
    private boolean IsDelivery;


    public int getFactorId() {
        return FactorId;
    }

    public void setFactorId(int factorId) {
        FactorId = factorId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getUserAddress() {
        return UserAddress;
    }

    public void setUserAddress(String userAddress) {
        UserAddress = userAddress;
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

    public String getUserDescription() {
        return UserDescription;
    }

    public void setUserDescription(String userDescription) {
        UserDescription = userDescription;
    }

    public boolean isDelivery() {
        return IsDelivery;
    }

    public void setDelivery(boolean delivery) {
        IsDelivery = delivery;
    }
}
