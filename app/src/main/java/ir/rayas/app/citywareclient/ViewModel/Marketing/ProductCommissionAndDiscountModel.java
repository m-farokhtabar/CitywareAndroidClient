package ir.rayas.app.citywareclient.ViewModel.Marketing;

/**
 * Created by Hajar on 5/6/2019.
 */

public class ProductCommissionAndDiscountModel {

    private int ProductId;
    private String ProductName;
    private double MarketerPercent;
    private double CustomerPercent;
    private double ApplicationPercent;
    private Double Price;
    private Double NumberOfOrder;
    private Double TotalPrice;

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

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public Double getNumberOfOrder() {
        return NumberOfOrder;
    }

    public void setNumberOfOrder(Double numberOfOrder) {
        NumberOfOrder = numberOfOrder;
    }

    public Double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        TotalPrice = totalPrice;
    }
}
