package ir.rayas.app.citywareclient.Repository;

import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Helper.SharedPreferenceManager;
import ir.rayas.app.citywareclient.ViewModel.Setting.LocalUserSettingViewModel;



public class LocalSettingRepository {

    private static LocalUserSettingViewModel Setting;


    /**
     * دریافت تنظیمات از کش
     *
     * @return
     */
    public LocalUserSettingViewModel getLocalSetting() {
        if (Setting == null) {
            SharedPreferenceManager ShManager = new SharedPreferenceManager();
            if (ShManager.IsContain(DefaultConstant.LocalSettingKey)) {
                Setting = ShManager.GetClass(DefaultConstant.LocalSettingKey, LocalUserSettingViewModel.class);
            }
        }
        return Setting;
    }

    /**
     * ویرایش یا اضافه کردن تنظیمات به کش
     *
     * @param setting
     */
    public void setLocalSetting(LocalUserSettingViewModel setting) {
        SharedPreferenceManager ShManager = new SharedPreferenceManager();
        if (ShManager.SetClass(DefaultConstant.LocalSettingKey, setting)) {
            Setting = setting;
        } else {
            Setting = null;
        }

    }

    /**
     * حذف تنظیمات از کش
     */
    public void ClearLocalSetting() {
        SharedPreferenceManager ShManager = new SharedPreferenceManager();
        ShManager.Clear(DefaultConstant.LocalSettingKey);
        Setting = null;

    }
}
