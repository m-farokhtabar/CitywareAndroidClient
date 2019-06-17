package ir.rayas.app.citywareclient.ViewModel.Marketing;



public class MarketingCustomerViewModel {

    private int Id;
    private int UserMarketerId;
    private int UserCustomerId;
    private int BusinessId;

    //وضعیت استفاده از این تخفیف
    //0 - هنوز استفاده نشده است
    //1 - استفاده شده است
    //2 - منقضی شده است
    private int Status;
    private String MarketerFullName;
    private String TicketValidity;
    private String Ticket;
    private String UseTicketDate;
    private String Percents;
    private String Factor;


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

    public int getStatus() {
        return Status;
    }

    public String getMarketerFullName() {
        return MarketerFullName;
    }

    public String getTicketValidity() {
        return TicketValidity;
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

    public String getFactor() {
        return Factor;
    }
}
