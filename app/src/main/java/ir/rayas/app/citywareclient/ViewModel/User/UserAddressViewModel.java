package ir.rayas.app.citywareclient.ViewModel.User;

/**
 * Created by Programmer on 7/19/2018.
 */

public class UserAddressViewModel {

    /// <summary>
    /// کد آدرس
    /// </summary>
    public int Id;
    /// <summary>
    /// کد کاربر
    /// </summary>
    public int UserId;
    /// <summary>
    /// آدرس
    /// </summary>
    public String CurrentAddress;
    /// <summary>
    /// عرض جغرافیایی
    /// </summary>
    public double Latitude;
    /// <summary>
    /// طول جغرافیایی
    /// </summary>
    public double Longitude;
    /// <summary>
    /// کد پستی
    /// </summary>
    public String PostalCode;
    /// <summary>
    /// تاریخ ایجاد
    /// </summary>
    public String Create;
    /// <summary>
    /// تاریخ ویرایش
    /// </summary>
    public String Modified;

    public int getId() {
        return Id;
    }

    public int getUserId() {
        return UserId;
    }

    public String getCurrentAddress() {
        return CurrentAddress;
    }

    public double getLatitude() {
        return Latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public String getCreate() {
        return Create;
    }

    public String getModified() {
        return Modified;
    }
    public void setId(int id) {
        Id = id;
    }
    public void setUserId(int userId) {
        UserId = userId;
    }

    public void setCurrentAddress(String currentAddress) {
        CurrentAddress = currentAddress;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

}
