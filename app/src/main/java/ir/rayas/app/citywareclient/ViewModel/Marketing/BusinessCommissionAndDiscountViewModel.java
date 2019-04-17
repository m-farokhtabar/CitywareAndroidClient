package ir.rayas.app.citywareclient.ViewModel.Marketing;

import java.util.List;

public class BusinessCommissionAndDiscountViewModel {

    private int BusinessId;
    private String BusinessName;
    private String BusinessAddress;
    private double MarketerPercent;
    private double CustomerPercent;
    private double ApplicationPercent;
    private List<ProductCommissionAndDiscountViewModel> ProductList;


    public int getBusinessId() {
        return BusinessId;
    }

    public void setBusinessId(int businessId) {
        BusinessId = businessId;
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }

    public String getBusinessAddress() {
        return BusinessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        BusinessAddress = businessAddress;
    }

    public double getMarketerPercent() {
        return MarketerPercent;
    }

    public void setMarketerPercent(double marketerPercent) {
        MarketerPercent = marketerPercent;
    }

    public double getCustomerPercent() {
        return CustomerPercent;
    }

    public void setCustomerPercent(double customerPercent) {
        CustomerPercent = customerPercent;
    }

    public double getApplicationPercent() {
        return ApplicationPercent;
    }

    public void setApplicationPercent(double applicationPercent) {
        ApplicationPercent = applicationPercent;
    }

    public List<ProductCommissionAndDiscountViewModel> getProductList() {
        return ProductList;
    }

    public void setProductList(List<ProductCommissionAndDiscountViewModel> productList) {
        ProductList = productList;
    }
}
