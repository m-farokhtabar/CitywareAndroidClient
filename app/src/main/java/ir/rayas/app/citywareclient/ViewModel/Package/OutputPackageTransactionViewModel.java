package ir.rayas.app.citywareclient.ViewModel.Package;

/**
 * Created by Programmer on 10/14/2018.
 */

public class OutputPackageTransactionViewModel {
    /// <summary>
    /// کد تراکنش
    /// </summary>        
    public int Id;
    /// <summary>
    /// کد کاربر
    /// </summary>        
    public int UserId;
    /// <summary>
    /// کد پکیج تبلیغاتی
    /// </summary>
    public int PackageId;
    /// <summary>
    /// اطلاعات پکیج
    /// </summary>
    public OutPackageViewModel Package;
    /// <summary>
    /// کد کپن
    /// </summary>
    public Integer CouponId;
    /// <summary>
    /// مبلغ پرداخت شده
    /// </summary>     
    public double PaidPrice;
    /// <summary>
    /// مبلغ تخفیف کپن
    /// </summary>
    public double DiscountCouponPrice;
    /// <summary>
    /// مبلغ اصلی همان مبلغ اعتبار
    /// </summary>
    public double Price;
    /// <summary>
    /// شماره تراکنش
    /// </summary>
    public String TransactionNumber;
    /// <summary>
    /// توضیحات تراکنش
    /// </summary>
    public String Description;
    /// <summary>
    /// تاریخ انقضاء پکیج
    /// اگر تاریخ خالی بود یعنی انقضاء ندارد
    /// </summary>
    public String Expire;
    /// <summary>
    /// منقضی شده است یا خیر
    /// </summary>
    public boolean IsExpired;
    /// <summary>
    /// تاریخ فعلی
    /// </summary>
    public String CurrentDate;
    /// <summary>
    /// تاریخ ایجاد
    /// </summary>        
    public String Create;
    /// <summary>
    /// تاریخ ویرایش
    /// </summary>        
    public String Modified;
    /// <summary>
    /// فعال بود یا نبودن
    /// </summary>        
    public boolean IsActive;

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

    public int getPackageId() {
        return PackageId;
    }

    public void setPackageId(int packageId) {
        PackageId = packageId;
    }

    public OutPackageViewModel getPackage() {
        return Package;
    }

    public void setPackage(OutPackageViewModel aPackage) {
        Package = aPackage;
    }

    public Integer getCouponId() {
        return CouponId;
    }

    public void setCouponId(Integer couponId) {
        CouponId = couponId;
    }

    public double getPaidPrice() {
        return PaidPrice;
    }

    public void setPaidPrice(double paidPrice) {
        PaidPrice = paidPrice;
    }

    public double getDiscountCouponPrice() {
        return DiscountCouponPrice;
    }

    public void setDiscountCouponPrice(double discountCouponPrice) {
        DiscountCouponPrice = discountCouponPrice;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public String getTransactionNumber() {
        return TransactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        TransactionNumber = transactionNumber;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getExpire() {
        return Expire;
    }

    public void setExpire(String expire) {
        Expire = expire;
    }

    public boolean isExpired() {
        return IsExpired;
    }

    public void setExpired(boolean expired) {
        IsExpired = expired;
    }

    public String getCurrentDate() {
        return CurrentDate;
    }

    public void setCurrentDate(String currentDate) {
        CurrentDate = currentDate;
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
}
