package ir.rayas.app.citywareclient.ViewModel.Payment;


public class BusinessCommissionPaymentViewModel {

    private String PricePayable;
    private String Id;
    private int BusinessId;
    private boolean IsPay;


    public String getPricePayable() {
        return PricePayable;
    }

    public void setPricePayable(String pricePayable) {
        PricePayable = pricePayable;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public int getBusinessId() {
        return BusinessId;
    }

    public void setBusinessId(int businessId) {
        BusinessId = businessId;
    }

    public boolean isPay() {
        return IsPay;
    }

    public void setPay(boolean pay) {
        IsPay = pay;
    }
}
