package ir.rayas.app.citywareclient.ViewModel.Business;

/**
 * Created by Programmer on 12/1/2018.
 */
/// <summary>
/// ورودی جهت ثبت امتیاز کسب و کار توسط کاربر
/// </summary>
public class ScoreInViewModel {
    /// <summary>
    /// کد کاربر
    //اجباری
    /// </summary>
    private int UserId;
    /// <summary>
    ///  کد کسب و کار
    //اجباری
    /// </summary>
    private int BusinessId;
    /// <summary>
    /// امتیاز محصول
    //اجباری
    /// </summary>
    private double Score;

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

    public double getScore() {
        return Score;
    }

    public void setScore(double score) {
        Score = score;
    }
}
