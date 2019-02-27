package ir.rayas.app.citywareclient.ViewModel.Business;

/** آمار بازدید کسب وکار
 * Created by Programmer on 12/28/2018.
 */

public class BusinessVisitedOutViewModel {
    /**
     * کد کاربر
     * اجباری
     */
    private int UserId;
    /**
     * کد کسب وکار
     * اجباری
     */
    private int BusinessId;

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
}
