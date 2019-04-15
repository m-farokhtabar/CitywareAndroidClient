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
    //نام پکیج جهت آرشیو
    public String PackageName;
    //خلاصه توضیحات پکیج جهت آرشیو
    public String PackageAbstractOfDescription;
    /// <summary>
    /// کد پکیج تبلیغاتی
    /// </summary>
    public int PackageId;
    /// <summary>
    // مبلغی که باید برای خرید پکیج پرداخت شود
    /// </summary>
    public double Price;
    /// <summary>
    /// منقضی شده است یا خیر
    /// </summary>
    public boolean IsExpired;
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
    /// <summary>
    /// شماره تراکنش
    /// </summary>
    public String TransactionNumber;
    /// <summary>
    /// توضیحات تراکنش
    /// </summary>
    public String Description;
    //تاریخ انقضاء پکیج
    // اگر تاریخ خالی بود یعنی انقضاء ندارد
    public String ExpireDate;
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
    // اگر این فیلد بیشتر از صفر باشد پس نیاز به مبالغ پرداخت شده و شماره تراکنش ندارد
    //    یعنی از طریق امتیاز باشگاه خریداری شده است
    public double BuyWithClubPoint;
    //  میزان اعتبار مصرف شده
    public double ConsumePackageCredit;
    //  میزان اعتبار پکیج
    public Double PackageCredit;
    //همیشه می توانند این پکیج را کاربران استفاده کنند
    public boolean AlwaysCredit;
    //تعداد روزهای باقیمانده از اعتبار این بسته
//    اگر نال بود یعنی تعداد روز نامحدود است
    public int RemainedCreditInDays;
    // میزان اعتبار پکیج برای کاربر به روز
    public int CreditInDays;


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

    public String getPackageName() {
        return PackageName;
    }

    public void setPackageName(String packageName) {
        PackageName = packageName;
    }

    public String getPackageAbstractOfDescription() {
        return PackageAbstractOfDescription;
    }

    public void setPackageAbstractOfDescription(String packageAbstractOfDescription) {
        PackageAbstractOfDescription = packageAbstractOfDescription;
    }

    public int getPackageId() {
        return PackageId;
    }

    public void setPackageId(int packageId) {
        PackageId = packageId;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public boolean isExpired() {
        return IsExpired;
    }

    public void setExpired(boolean expired) {
        IsExpired = expired;
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

    public String getExpireDate() {
        return ExpireDate;
    }

    public void setExpireDate(String expireDate) {
        ExpireDate = expireDate;
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

    public double getBuyWithClubPoint() {
        return BuyWithClubPoint;
    }

    public void setBuyWithClubPoint(double buyWithClubPoint) {
        BuyWithClubPoint = buyWithClubPoint;
    }

    public double getConsumePackageCredit() {
        return ConsumePackageCredit;
    }

    public void setConsumePackageCredit(double consumePackageCredit) {
        ConsumePackageCredit = consumePackageCredit;
    }

    public Double getPackageCredit() {
        return PackageCredit;
    }

    public void setPackageCredit(Double packageCredit) {
        PackageCredit = packageCredit;
    }

    public boolean isAlwaysCredit() {
        return AlwaysCredit;
    }

    public void setAlwaysCredit(boolean alwaysCredit) {
        AlwaysCredit = alwaysCredit;
    }

    public int getRemainedCreditInDays() {
        return RemainedCreditInDays;
    }

    public void setRemainedCreditInDays(int remainedCreditInDays) {
        RemainedCreditInDays = remainedCreditInDays;
    }

    public int getCreditInDays() {
        return CreditInDays;
    }

    public void setCreditInDays(int creditInDays) {
        CreditInDays = creditInDays;
    }
}
