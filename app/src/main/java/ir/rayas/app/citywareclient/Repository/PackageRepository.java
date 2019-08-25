package ir.rayas.app.citywareclient.Repository;

import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Helper.SharedPreferenceManager;
import ir.rayas.app.citywareclient.ViewModel.Payment.PackagePaymentViewModel;


public class PackageRepository {

    private static PackagePaymentViewModel Package;

    /**
     * دریافت اطلاعات پرداخت پکیج از کش
     *
     * @return
     */
    public PackagePaymentViewModel getPackagePayment() {
        if (Package == null) {
            SharedPreferenceManager ShManager = new SharedPreferenceManager();
            if (ShManager.IsContain(DefaultConstant.PackageKey)) {
                Package = ShManager.GetClass(DefaultConstant.PackageKey, PackagePaymentViewModel.class);
            }
        }
        return Package;
    }

    /**
     * ویرایش یا اضافه کردن اطلاعات پرداخت پکیج به کش
     *
     * @param packagePayment
     */
    public void setPackagePayment(PackagePaymentViewModel packagePayment) {
        SharedPreferenceManager ShManager = new SharedPreferenceManager();
        if (ShManager.SetClass(DefaultConstant.PackageKey, packagePayment)) {
            Package = packagePayment;
        } else {
            Package = null;
        }

    }

    /**
     * حذف اطلاعات پرداخت پکیج از کش
     */
    public void ClearPackagePayment() {
        SharedPreferenceManager ShManager = new SharedPreferenceManager();
        ShManager.Clear(DefaultConstant.PackageKey);
        Package = null;

    }

}
