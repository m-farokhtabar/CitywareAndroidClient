package ir.rayas.app.citywareclient.Share.Constant;

/**
 * Created by Programmer on 2/4/2018.
 * مقادیر پیش فرض در پروژه
 */
public class DefaultConstant {
    /**
     * کارکترهایی که اگر در مسیر فایل باشد گلاید نمی تواند آن را لود کند
     */
    public static String InvalidChar = "@#&=*+-_.,:!?()/~'%";
    /**
     * نام فایلی که اطلاعات کش در آن ذخیره می شود
     */
    public static String PreferenceFileName = "MyCityWareMemory";
    /**
     * آدرس پایه وب سرویس
     */
    public static String BaseUrlWebService = "http://webservice.cityware.ir";
    /**
     * کلید اکانت جهت نگهداری اطلاعات کاربر در حافظه داخلی
     */
    public static String AccountKey = "Account";
    /**
     * زمان پیش فرض برای فعال کردن مجدد ارسال پیام مجدد
     * به میلی ثانیه
     */
    public static int DefaultDelayForEnabledReSendMessage = 10 * 60 * 1000;
    /**
     * کد مجوز برای دریافت مجوز استفاده از لوکیشن برنامه
     */
    public static int RequestLocationPermission = 1;
    /**
     * حداکثر زمانی که باید برنامه صبر کند تا کاربر دوباره دکمه بک را جهت خروج از برنامه فشار دهد
     * به میلی ثانیه
     */
    public static int TimeForTouchBackAgainToExitApp = 2000;
    /// <summary>
    /// اندازه تصویر محصولات
    /// به بایت 102400
    /// 100 Kb
    /// </summary>
    public static int ProductImageSize = 102400;
    /**
     * کلید اکانت جهت نگهداری تمامی پیام ها در حافظه داخلی
     */
    public static String NotificationKey = "NotificationList";
    /**
     * کلید اکانت نگهداری اطلاعات مناطق به صورت درختی
     */
    public static String RegionKey = "RegionKey";
    /**
     * کلید اکانت نگهداری اطلاعات اصناف به صورت درختی
     */
    public static String BusinessCategoryKey = "BusinessCategoryKey";
    /**
     * تعداد آیتم های نمایش در صفحه
     */
    public static int PageNumberSize = 10;

    public static String LocalSettingKey = "LocalSetting";
    public static int MaxPayment = 50000000;
    public static int MinPayment = 1000;


}
