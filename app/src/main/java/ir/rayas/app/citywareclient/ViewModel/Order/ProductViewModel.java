package ir.rayas.app.citywareclient.ViewModel.Order;

import java.util.List;

/**
 * Created by Programmer on 8/26/2018.
 */

public class ProductViewModel {
    /// <summary>
    /// کد محصول
    /// </summary>
    private int Id;
    /// <summary>
    /// کد بنگاه
    //اجباری
    /// </summary>        
    private int BusinessId;
    /// <summary>
    /// نام محصول
    //اجباری
    //TextShortSize
    /// </summary>
    private String Name;
    /// <summary>
    /// موجودی
    //اجباری
    //از صفر می تواند شروع شود
    //اعشاری تا دو رقم اعشار هم می تواند باشد
    /// </summary>
    private double Inventory;
    /// <summary>
    /// قیمت محصول
    //واحد تومان
    //از صفر می تواند باشد
    //اجباری
    //حداکثر 12 رقم
    //اعشاری نداشته باشد
    /// </summary>
    private double Price;
    /// <summary>
    /// پوسانت محصول
    //از 0 تا 100
    //اجباری
    /// </summary>
    private double Commission;
    /// <summary>
    /// ترتیب نمایش
    //از 0 تا بالاتر
    //اجباری
    //حداکثر 8 رقم
    /// </summary>
    private int Order;
    /// <summary>
    /// موجود بودن یا عدم موجودی
    //اجباری
    /// </summary>
    private boolean IsAvailaible;
    /// <summary>
    /// خلاصه توضیحات محصول
    //اجباری
    //TextLongSize
    //چند خطی
    /// </summary>
    private String AbstractOfDescription;
    /// <summary>
    /// توضیحات محصول
    //اختیاری
    //MaxOfTextSize
    //چند خطی
    /// </summary>        
    private String Description;
    /// <summary>
    /// تاریخ ایجاد
    /// توسط سرور
    /// </summary>
    private String Create;
    /// <summary>
    /// تاریخ ویرایش
    /// توسط سرور
    /// </summary>
    private String Modified;
    /// <summary>
    /// فعال بود یا نبودن
    /// </summary>
    private boolean IsActive;
    ///  تصاویر مربوط به محصول
    private List<ProductImageViewModel> ProductImageList;
    /// <summary>
    /// وضعیت پیک
    /// </summary>
    public boolean HasDelivery;
    /// <summary>
    /// باز بودن یا بسته بودن کسب وکار
    /// </summary>
    public boolean IsOpen;
    /// <summary>
    /// نام کسب وکار
    /// </summary>
    public String BusinessName;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getBusinessId() {
        return BusinessId;
    }

    public void setBusinessId(int businessId) {
        BusinessId = businessId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getInventory() {
        return Inventory;
    }

    public void setInventory(double inventory) {
        Inventory = inventory;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public double getCommission() {
        return Commission;
    }

    public void setCommission(double commission) {
        Commission = commission;
    }

    public int getOrder() {
        return Order;
    }

    public void setOrder(int order) {
        Order = order;
    }

    public boolean isAvailaible() {
        return IsAvailaible;
    }

    public void setAvailaible(boolean availaible) {
        IsAvailaible = availaible;
    }

    public String getAbstractOfDescription() {
        return AbstractOfDescription;
    }

    public void setAbstractOfDescription(String abstractOfDescription) {
        AbstractOfDescription = abstractOfDescription;
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

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public List<ProductImageViewModel> getProductImageList() {
        return ProductImageList;
    }

    public void setProductImageList(List<ProductImageViewModel> productImageList) {
        ProductImageList = productImageList;
    }
    public boolean isHasDelivery() {
        return HasDelivery;
    }

    public void setHasDelivery(boolean hasDelivery) {
        HasDelivery = hasDelivery;
    }

    public boolean isOpen() {
        return IsOpen;
    }

    public void setOpen(boolean open) {
        IsOpen = open;
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }

}
