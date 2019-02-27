package ir.rayas.app.citywareclient.ViewModel.Business;

/**
 * Created by Programmer on 11/24/2018.
 */

public class CommentInViewModel
{
    /// <summary>
    /// کد کاربر
    //اجباری
    /// </summary>
    public int UserId;
    /// <summary>
    ///  کد کسب و کار
    //اجباری
    /// </summary>
    public int BusinessId;
    /// <summary>
    /// متن کامنت
    //اجباری
    /// </summary>
    public String CommentText;

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

    public String getCommentText() {
        return CommentText;
    }

    public void setCommentText(String commentText) {
        CommentText = commentText;
    }
}