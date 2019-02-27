package ir.rayas.app.citywareclient.ViewModel.Basket;

/**
 * Created by Hajar on 2/17/2019.
 */

public class QuickOrderItemViewModel {

    private String ProductName;
    private double Value;

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public double getValue() {
        return Value;
    }

    public void setValue(double value) {
        Value = value;
    }
}
