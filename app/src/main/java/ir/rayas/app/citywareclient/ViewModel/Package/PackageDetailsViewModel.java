package ir.rayas.app.citywareclient.ViewModel.Package;

/**
 * Created by Hajar on 3/12/2019.
 */

public class PackageDetailsViewModel {
    /// <summary>
    /// کد پکیج
    /// </summary>
    public int Id;
    /// <summary>
    /// عنوان پکیج
    /// </summary>
    public String Title;
    /// <summary>
    /// مبلغ جهت پرداخت
    /// </summary>
    public double PayablePrice;
    /// <summary>
    /// مبلغ اعتبار
    /// </summary>
    public double CreditPrice;
    /// <summary>
    /// خلاصه توضیحات
    /// </summary>
    public String AbstractOfDescription;
    /// <summary>
    /// تا تاریخ معرفی
    /// </summary>
    public String IntroduceFrom;
    /// <summary>
    /// از تاریخ معرفی
    /// </summary>
    public String IntroduceTo;
    /// <summary>
    /// همیشه می توان این پکیج را برای مشتریان معرفی نمود
    /// </summary>
    public boolean AlwaysIntroduce;
    /// <summary>
    /// میزان اعتبار پکیج برای کاربر به روز
    /// </summary>
    public int CreditInDays;
    /// <summary>
    /// همیشه می توانند این پکیج را کاربران استفاده کنند
    /// </summary>
    public boolean AlwaysCredit;
    /// <summary>
    /// تصویر پوستر
    /// </summary>
    public String ImagePathUrl;
    /// <summary>
    /// میزان امتیازی که به کاربری داده می شود که این پکیج را خریداری کند
    /// </summary>
    public double Point;
    /// <summary>
    /// توضیحات
    /// </summary>
    public String Description;
    /// <summary>
    /// تاریخ ایجاد
    /// </summary>
    public String Create;
    /// <summary>
    /// تاریخ ویرایش
    /// </summary>
    public String Modified;
    /// <summary>
    /// قابلیت نمایش جهت انتخاب پکیج
    /// </summary>
    public boolean IsActive;

    //قابل خرید با امتیاز کاربر
    public boolean CanPurchaseByPoint;

    //میزان امتیازی که کاربر برای خرید این پکیج باید مصرف کند
    public double PointForPurchasing;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public double getPayablePrice() {
        return PayablePrice;
    }

    public void setPayablePrice(double payablePrice) {
        PayablePrice = payablePrice;
    }

    public double getCreditPrice() {
        return CreditPrice;
    }

    public void setCreditPrice(double creditPrice) {
        CreditPrice = creditPrice;
    }

    public String getAbstractOfDescription() {
        return AbstractOfDescription;
    }

    public void setAbstractOfDescription(String abstractOfDescription) {
        AbstractOfDescription = abstractOfDescription;
    }

    public String getIntroduceFrom() {
        return IntroduceFrom;
    }

    public void setIntroduceFrom(String introduceFrom) {
        IntroduceFrom = introduceFrom;
    }

    public String getIntroduceTo() {
        return IntroduceTo;
    }

    public void setIntroduceTo(String introduceTo) {
        IntroduceTo = introduceTo;
    }

    public boolean isAlwaysIntroduce() {
        return AlwaysIntroduce;
    }

    public void setAlwaysIntroduce(boolean alwaysIntroduce) {
        AlwaysIntroduce = alwaysIntroduce;
    }

    public int getCreditInDays() {
        return CreditInDays;
    }

    public void setCreditInDays(int creditInDays) {
        CreditInDays = creditInDays;
    }

    public boolean isAlwaysCredit() {
        return AlwaysCredit;
    }

    public void setAlwaysCredit(boolean alwaysCredit) {
        AlwaysCredit = alwaysCredit;
    }

    public String getImagePathUrl() {
        return ImagePathUrl;
    }

    public void setImagePathUrl(String imagePathUrl) {
        ImagePathUrl = imagePathUrl;
    }

    public double getPoint() {
        return Point;
    }

    public void setPoint(double point) {
        Point = point;
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

    public boolean isCanPurchaseByPoint() {
        return CanPurchaseByPoint;
    }

    public void setCanPurchaseByPoint(boolean canPurchaseByPoint) {
        CanPurchaseByPoint = canPurchaseByPoint;
    }

    public double getPointForPurchasing() {
        return PointForPurchasing;
    }

    public void setPointForPurchasing(double pointForPurchasing) {
        PointForPurchasing = pointForPurchasing;
    }
}

