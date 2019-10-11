package ir.rayas.app.citywareclient.ViewModel.Marketing;


public class Marketing_FastCustomerFactorViewModel {

    private int MarketingId;
    private int BusinessId;
    private String Ticket;
    private double CommissionPrice;

    public int getMarketingId() {
        return MarketingId;
    }

    public void setMarketingId(int marketingId) {
        MarketingId = marketingId;
    }

    public int getBusinessId() {
        return BusinessId;
    }

    public void setBusinessId(int businessId) {
        BusinessId = businessId;
    }

    public String getTicket() {
        return Ticket;
    }

    public void setTicket(String ticket) {
        Ticket = ticket;
    }

    public double getCommissionPrice() {
        return CommissionPrice;
    }

    public void setCommissionPrice(double commissionPrice) {
        CommissionPrice = commissionPrice;
    }
}
