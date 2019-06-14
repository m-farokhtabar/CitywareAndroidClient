package ir.rayas.app.citywareclient.Adapter.ViewModel;


public class IsSelectBusinessListAdapterViewModel {

    public Boolean IsSelected = false;
    public String BusinessName = "";
    public int BusinessId ;
    public int BusinessCategoryId ;

    public Boolean getSelected() {
        return IsSelected;
    }

    public void setSelected(Boolean selected) {
        IsSelected = selected;
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }

    public int getBusinessId() {
        return BusinessId;
    }

    public void setBusinessId(int businessId) {
        BusinessId = businessId;
    }

    public int getBusinessCategoryId() {
        return BusinessCategoryId;
    }

    public void setBusinessCategoryId(int businessCategoryId) {
        BusinessCategoryId = businessCategoryId;
    }
}
