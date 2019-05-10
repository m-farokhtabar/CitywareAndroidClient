package ir.rayas.app.citywareclient.ViewModel.Marketing;

import java.util.List;


public class Marketing_CustomerFactorViewModel {

    /// <summary>
    /// کد بازاریابی
    /// </summary>

    public int MarketingId ;
    /// <summary>
    /// کد کسب  و کار
    /// </summary>

    public int BusinessId;
    /// <summary>
    /// تیکت معرفی
    /// </summary>

    public String Ticket;
    /// <summary>
    /// مشصخصات فاکتور
    /// </summary>
    public List<Marketing_CustomerFactorDetailsViewModel> Details;

    public int getMarketingId() {
        return MarketingId;
    }

    public int getBusinessId() {
        return BusinessId;
    }

    public String getTicket() {
        return Ticket;
    }

    public List<Marketing_CustomerFactorDetailsViewModel> getDetails() {
        return Details;
    }

    public void setMarketingId(int marketingId) {
        MarketingId = marketingId;
    }

    public void setBusinessId(int businessId) {
        BusinessId = businessId;
    }

    public void setTicket(String ticket) {
        Ticket = ticket;
    }

    public void setDetails(List<Marketing_CustomerFactorDetailsViewModel> details) {
        Details = details;
    }
}
