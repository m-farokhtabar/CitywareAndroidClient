package ir.rayas.app.citywareclient.ViewModel.Basket;

/**
 * Created by Hajar on 1/25/2019.
 */

public class BasketItemViewModel {

    private Integer Id;
    private int BasketId;
    private int ProductId;
    private String ProductName;

    // مسیر فایل محصول
    private String ImagePathUrl;

    //تعداد یا مقدار سفارش
    private double Value;

    //قیمت محصول یا خدمت به واحد
    private double Price;

    //توضیحات کاربر برای آیتم مورد نظر
    private String Description;

    // تاریخ ایجاد
    private String Create;

    //تاریخ ویرایش
    private String Modified;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public int getBasketId() {
        return BasketId;
    }

    public void setBasketId(int basketId) {
        BasketId = basketId;
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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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
