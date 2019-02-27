package ir.rayas.app.citywareclient.ViewModel.User;

import ir.rayas.app.citywareclient.ViewModel.Setting.UserSettingViewModel;

/**
 * Created by Programmer on 2/4/2018.
 */
/// <summary>
/// ویومدل اکانت/
/// </summary>
public class AccountViewModel
{
    /// <summary>
    /// اطلاعات کاربر
    /// </summary>
    private UserViewModel User;
    /// <summary>
    /// تنظیمات کاربر
    /// </summary>
    private UserSettingViewModel UserSetting;
    /// <summary>
    /// توکن جهت درخواست های بعدی کاربر
    /// </summary>
    private String Token;
    /// <summary>
    /// کد تایید جهت اجازه ورود در بار اول
    /// </summary>
    private int ConfirmationId;

    public UserViewModel getUser() {
        return User;
    }

    public void setUser(UserViewModel user) {
        User = user;
    }


    public UserSettingViewModel getUserSetting() {
        return UserSetting;
    }

    public void setUserSetting(UserSettingViewModel userSetting) {
        UserSetting = userSetting;
    }


    public String getToken() {
        return Token;
    }

    public int getConfirmationId() {
        return ConfirmationId;
    }
}
