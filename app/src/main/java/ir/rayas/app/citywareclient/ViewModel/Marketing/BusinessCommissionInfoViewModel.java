package ir.rayas.app.citywareclient.ViewModel.Marketing;


public class BusinessCommissionInfoViewModel {

    private Integer AllSuggestedCustomers;
    private Integer AllCustomersInUse;
    private Integer AllUnPayedCommission;
    private Integer AllPayedCommission;
    private Double PricePayedCommission;
    private Double PriceUnPayedCommission;

    public Integer getAllSuggestedCustomers() {
        return AllSuggestedCustomers;
    }

    public Integer getAllCustomersInUse() {
        return AllCustomersInUse;
    }

    public Integer getAllUnPayedCommission() {
        return AllUnPayedCommission;
    }

    public Integer getAllPayedCommission() {
        return AllPayedCommission;
    }

    public Double getPricePayedCommission() {
        return PricePayedCommission;
    }

    public Double getPriceUnPayedCommission() {
        return PriceUnPayedCommission;
    }
}
