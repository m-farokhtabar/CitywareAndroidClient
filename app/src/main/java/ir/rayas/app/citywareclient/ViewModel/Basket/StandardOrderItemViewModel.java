package ir.rayas.app.citywareclient.ViewModel.Basket;

/**
 * Created by Hajar on 1/28/2019.
 */

public class StandardOrderItemViewModel {

    private int UserId;
    private int ProductId;

    //تعداد یا مقدار سفارش
    private double Value;


    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public double getValue() {
        return Value;
    }

    public void setValue(double value) {
        Value = value;
    }
}
