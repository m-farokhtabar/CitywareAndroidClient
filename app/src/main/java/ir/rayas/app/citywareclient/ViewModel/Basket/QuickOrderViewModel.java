package ir.rayas.app.citywareclient.ViewModel.Basket;

import java.util.List;

/**
 * Created by Hajar on 2/17/2019.
 */

public class QuickOrderViewModel {

    private int UserId;
    private int BusinessId;
    private List<QuickOrderItemViewModel> ItemList;


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

    public List<QuickOrderItemViewModel> getItemList() {
        return ItemList;
    }

    public void setItemList(List<QuickOrderItemViewModel> itemList) {
        ItemList = itemList;
    }
}
