package ir.rayas.app.citywareclient.ViewModel.Basket;

import java.util.List;

/**
 * Created by Hajar on 1/25/2019.
 */

public class BasketViewModel {

    //  کد سبد خرید
    private int Id;

    // خریدار
    private int UserId;

    //  جهت سهولت در پیدا کردن بنگاه
    private int BusinessId;

    // نام کسب وکار
    private String BusinessName;

    // مسیر تصویر محصول
    private String Path;

    // قیمت نهایی
    private double TotalPrice;

    // تعداد یا مقدار کل اقلام سفارشی
    private double TotalQuantity;

    //  توضیحات ویا سفارشات اضافی در فاکتور
    private String UserDescription;

    // تاریخ آخرین ویرایش سبد خرید
    private String Modified;

    //کاربر درخواست پیک دارد یا خیر
    private boolean IsDelivery;

    //لیست سفارشات در سبد خرید
    private List<BasketItemViewModel> ItemList;


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

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

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }

    public double getTotalQuantity() {
        return TotalQuantity;
    }

    public void setTotalQuantity(double totalQuantity) {
        TotalQuantity = totalQuantity;
    }

    public String getUserDescription() {
        return UserDescription;
    }

    public void setUserDescription(String userDescription) {
        UserDescription = userDescription;
    }

    public String getModified() {
        return Modified;
    }

    public void setModified(String modified) {
        Modified = modified;
    }

    public List<BasketItemViewModel> getItemList() {
        return ItemList;
    }

    public void setItemList(List<BasketItemViewModel> itemList) {
        ItemList = itemList;
    }

    public boolean isDelivery() {
        return IsDelivery;
    }

    public void setDelivery(boolean delivery) {
        IsDelivery = delivery;
    }
}
