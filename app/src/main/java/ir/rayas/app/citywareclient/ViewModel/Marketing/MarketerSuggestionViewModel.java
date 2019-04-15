package ir.rayas.app.citywareclient.ViewModel.Marketing;


public class MarketerSuggestionViewModel {

    private int BusinessId;
    private int UserCustomerId;
    private String Description;

    public int getBusinessId() {
        return BusinessId;
    }

    public void setBusinessId(int businessId) {
        BusinessId = businessId;
    }

    public int getUserCustomerId() {
        return UserCustomerId;
    }

    public void setUserCustomerId(int userCustomerId) {
        UserCustomerId = userCustomerId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
