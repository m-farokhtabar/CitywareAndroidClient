package ir.rayas.app.citywareclient.Repository;

import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Helper.SharedPreferenceManager;
import ir.rayas.app.citywareclient.ViewModel.Payment.BusinessCommissionPaymentViewModel;


public class BusinessCommissionRepository {

    private static BusinessCommissionPaymentViewModel Commission;

    /**
     * دریافت اطلاعات پرداخت پورسانت از کش
     *
     * @return
     */
    public BusinessCommissionPaymentViewModel getBusinessCommission() {
        if (Commission == null) {
            SharedPreferenceManager ShManager = new SharedPreferenceManager();
            if (ShManager.IsContain(DefaultConstant.BusinessCommissionKey)) {
                Commission = ShManager.GetClass(DefaultConstant.BusinessCommissionKey, BusinessCommissionPaymentViewModel.class);
            }
        }
        return Commission;
    }

    /**
     * ویرایش یا اضافه کردن اطلاعات پرداخت پورسانت به کش
     *
     * @param commission
     */
    public void setBusinessCommission(BusinessCommissionPaymentViewModel commission) {
        SharedPreferenceManager ShManager = new SharedPreferenceManager();
        if (ShManager.SetClass(DefaultConstant.BusinessCommissionKey, commission)) {
            Commission = commission;
        } else {
            Commission = null;
        }

    }

    /**
     * حذف اطلاعات پرداخت پورسانت از کش
     */
    public void ClearBusinessCommission() {
        SharedPreferenceManager ShManager = new SharedPreferenceManager();
        ShManager.Clear(DefaultConstant.BusinessCommissionKey);
        Commission = null;

    }


}
