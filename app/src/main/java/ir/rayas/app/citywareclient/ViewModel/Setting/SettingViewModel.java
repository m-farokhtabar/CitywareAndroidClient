package ir.rayas.app.citywareclient.ViewModel.Setting;

/**
 * Created by Programmer on 2/10/2018.
 */
/// <summary>
/// ویومدل تنظیمات
/// </summary>
public class SettingViewModel {
    /// <summary>
    /// کد تنظیمات
    /// </summary>
    private int Id;
    /// <summary>
    /// محدوده جستجو بنگاه ها
    /// 500 متر
    /// </summary>
    private double SearchRadius;
    /// <summary>
    /// میزان زمان اعتبار کد رهگیری برای فعال سازی کاربر
    /// دقیقه
    /// </summary>
    private double TrackingCodeActivationUserDeadLineTime;
    /// <summary>
    /// زمان انقضا توکن کاربر بعد از لاگین
    /// </summary>
    private int UserTokenExpireTimeInHour;
    /// <summary>
    /// تصویر پس زیمنه صفحه معرفی برنامه
    /// </summary>
    private String IntroducePageBackgroundImagePath;
    /// <summary>
    /// تاریخ ایجاد
    /// سمت سرور
    /// </summary>
    private String Create;
    /// <summary>
    /// تاریخ ویرایش
    /// سمت سرور
    /// </summary>
    private String Modified;

    public int getId() {
        return Id;
    }


    public double getSearchRadius() {
        return SearchRadius;
    }


    public double getTrackingCodeActivationUserDeadLineTime() {
        return TrackingCodeActivationUserDeadLineTime;
    }


    public int getUserTokenExpireTimeInHour() {
        return UserTokenExpireTimeInHour;
    }


    public String getIntroducePageBackgroundImagePath() {
        return IntroducePageBackgroundImagePath;
    }


    public String getCreate() {
        return Create;
    }


    public String getModified() {
        return Modified;
    }

}
