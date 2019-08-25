package ir.rayas.app.citywareclient.Adapter.ViewModel;



public class BasketAddressAdapterViewModel {

    public String Address = "";
    public String PostalCode = "";
    public int UserAddressId ;
    public Boolean IsSelected = false;
    public double Latitude = 0;
    public double Longitude = 0;

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public Boolean getSelected() {
        return IsSelected;
    }

    public void setSelected(Boolean selected) {
        IsSelected = selected;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public int getUserAddressId() {
        return UserAddressId;
    }

    public void setUserAddressId(int userAddressId) {
        UserAddressId = userAddressId;
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
}
