package ir.rayas.app.citywareclient.ViewModel.Basket;

/**
 * Created by Hajar on 2/15/2019.
 */

public class BasketDeliveryAndUserDescriptionViewModel {

    private String  UserDescription;
    private boolean IsDelivery;


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
