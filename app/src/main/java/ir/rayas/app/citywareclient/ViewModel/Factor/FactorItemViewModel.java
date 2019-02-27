package ir.rayas.app.citywareclient.ViewModel.Factor;

/**
 * Created by Hajar on 2/22/2019.
 */

public class FactorItemViewModel {

    private  int Id;
    private  int FactorId;
    private  int ProductId;
    private String ProductName;
    private String ImagePathUrl;
    private double Value;
    private double Price;
    private String Create;
    private String Modified;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getFactorId() {
        return FactorId;
    }

    public void setFactorId(int factorId) {
        FactorId = factorId;
    }

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

    public String getImagePathUrl() {
        return ImagePathUrl;
    }

    public void setImagePathUrl(String imagePathUrl) {
        ImagePathUrl = imagePathUrl;
    }

    public double getValue() {
        return Value;
    }

    public void setValue(double value) {
        Value = value;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public String getCreate() {
        return Create;
    }

    public void setCreate(String create) {
        Create = create;
    }

    public String getModified() {
        return Modified;
    }

    public void setModified(String modified) {
        Modified = modified;
    }
}
