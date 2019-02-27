package ir.rayas.app.citywareclient.Adapter.ViewModel;

/**
 * Created by Hajar on 2/15/2019.
 */

public class BasketAddressAdapterViewModel {

    public String Address = "";
    public Boolean IsSelected = false;

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
}
