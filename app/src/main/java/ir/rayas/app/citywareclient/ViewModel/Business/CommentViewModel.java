package ir.rayas.app.citywareclient.ViewModel.Business;

/**
 * Created by Programmer on 11/24/2018.
 */
/// <summary>
/// نظرات کاربران بر روی کسب و کار
/// </summary>
public class CommentViewModel {
    /// <summary>
    /// کد کامنت
    /// </summary>
    private int Id;
    /// <summary>
    /// کد کاربر
    /// </summary>
    private int UserId;
    /// <summary>
    /// نام مستعار کاربر
    /// </summary>
    private String NickName;
    /// <summary>
    /// کد کسب وکار
    /// </summary>
    private int BusinessId;
    /// <summary>
    /// نظر کاربر
    /// </summary>
    private String Text;
    /// <summary>
    /// تاریخ ایجاد
    /// </summary>
    private String Create;
    /// <summary>
    /// تاریخ ویرایش
    /// </summary>
    private String Modified;
    /// <summary>
    /// امتیاز داده شده برای کسب و کار
    /// </summary>
    private Double Score;

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

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public int getBusinessId() {
        return BusinessId;
    }

    public void setBusinessId(int businessId) {
        BusinessId = businessId;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
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

    public Double getScore() {
        return Score;
    }

    public void setScore(Double score) {
        Score = score;
    }
}
