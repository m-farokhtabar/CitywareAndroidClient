package ir.rayas.app.citywareclient.ViewModel.Payment;

public class PackagePaymentViewModel {

    private int PackageId;
    private int Id;
    private String PackageName;
    private String CouponCode;
    private int PricePayable;
    private int PriceCoupon;
    private boolean IsPay;


    public int getPackageId() {
        return PackageId;
    }

    public void setPackageId(int packageId) {
        PackageId = packageId;
    }

    public String getPackageName() {
        return PackageName;
    }

    public void setPackageName(String packageName) {
        PackageName = packageName;
    }

    public int getPricePayable() {
        return PricePayable;
    }

    public void setPricePayable(int pricePayable) {
        PricePayable = pricePayable;
    }

    public boolean isPay() {
        return IsPay;
    }

    public void setPay(boolean pay) {
        IsPay = pay;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCouponCode() {
        return CouponCode;
    }

    public void setCouponCode(String couponCode) {
        CouponCode = couponCode;
    }

    public int getPriceCoupon() {
        return PriceCoupon;
    }

    public void setPriceCoupon(int priceCoupon) {
        PriceCoupon = priceCoupon;
    }

}
