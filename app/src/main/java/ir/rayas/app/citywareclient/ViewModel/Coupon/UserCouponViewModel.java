package ir.rayas.app.citywareclient.ViewModel.Coupon;


public class UserCouponViewModel {

    private CouponViewModel Coupon;
    private String UsedDate;
    private boolean IsUsed	;

    public CouponViewModel getCoupon() {
        return Coupon;
    }

    public String getUsedDate() {
        return UsedDate;
    }

    public boolean isUsed() {
        return IsUsed;
    }
}
