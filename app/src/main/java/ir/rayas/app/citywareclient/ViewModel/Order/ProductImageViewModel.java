package ir.rayas.app.citywareclient.ViewModel.Order;

/**
 * Created by Programmer on 8/26/2018.
 */

public class ProductImageViewModel {
    /// <summary>
    /// کد تصویر
    /// </summary>
    private int Id;
    /// <summary>
    /// کد محصول
    //اجباری
    /// </summary>
    private int ProductId;
    /// <summary>
    /// مسیر تصویر بر روی سرور
    //InternetAddressOrPathSize
    //اجباری
    /// </summary>
    private String Path;
    /// <summary>
    /// مسیر کامل تصویر
    /// </summary>
    private String FullPath;
    /// <summary>
    /// ترتیب نمایش
    //اجباری
    //از 0 تا بالاتر
    //حداکثر 8 رقم
    /// </summary>
    private int Order;
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

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public int getOrder() {
        return Order;
    }

    public void setOrder(int order) {
        Order = order;
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

    public String getFullPath() {
        return FullPath;
    }

    public void setFullPath(String fullPath) {
        FullPath = fullPath;
    }
}
