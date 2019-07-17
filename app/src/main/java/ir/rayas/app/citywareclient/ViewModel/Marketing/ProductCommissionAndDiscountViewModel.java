package ir.rayas.app.citywareclient.ViewModel.Marketing;

public class ProductCommissionAndDiscountViewModel {

    private int ProductId;
    private String ProductName;
    private double MarketerPercent;
    private double CustomerPercent;
    private double ApplicationPercent;

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
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

}
