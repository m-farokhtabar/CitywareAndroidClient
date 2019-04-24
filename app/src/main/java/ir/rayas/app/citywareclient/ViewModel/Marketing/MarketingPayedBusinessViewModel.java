package ir.rayas.app.citywareclient.ViewModel.Marketing;


public class MarketingPayedBusinessViewModel {

    private int Id;
    private int UserMarketerId;
    private int UserCustomerId;
    private int BusinessId;
    private String CustomerFullName;
    private String PayedDate;
    private String Ticket;
    private String UseTicketDate;
    private String Percents;
    private double Price;
    private String Factor;
     private String TransactionNumber;

    public int getId() {
        return Id;
    }

    public int getUserMarketerId() {
        return UserMarketerId;
    }

    public int getUserCustomerId() {
        return UserCustomerId;
    }

    public int getBusinessId() {
        return BusinessId;
    }

    public String getCustomerFullName() {
        return CustomerFullName;
    }

    public String getPayedDate() {
        return PayedDate;
    }

    public String getTicket() {
        return Ticket;
    }

    public String getUseTicketDate() {
        return UseTicketDate;
    }

    public String getPercents() {
        return Percents;
    }

    public double getPrice() {
        return Price;
    }

    public String getFactor() {
        return Factor;
    }

    public String getTransactionNumber() {
        return TransactionNumber;
    }
}
