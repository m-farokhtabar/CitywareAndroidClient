package ir.rayas.app.citywareclient.ViewModel.Business;

/**
 * Created by Programmer on 11/21/2018.
 */
/// <summary>
/// ارسال اطلاعات جهت نشان اطلاعات
/// </summary>
public class BookmarkOutViewModel {
    /// <summary>
    /// کد کاربر
    /// </summary>
    private int UserId;
    /// <summary>
    ///  کد کسب و کار
    /// </summary>
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