package ir.rayas.app.citywareclient.ViewModel.Etc;

import ir.rayas.app.citywareclient.Share.Enum.LinkType;
import ir.rayas.app.citywareclient.Share.Enum.PlaceType;

/**
 * Created by Programmer on 2/10/2018.
 */

/// <summary>
/// اخبار  و رویدادها که در صفحه اول یا صفحاتی دیگر نمایش داده می شود
/// </summary>
public class EventOrNewsViewModel
{
    /// <summary>
    /// کلید اصلی
    /// </summary>
    public int Id;
    /// <summary>
    /// عنوان
    /// </summary>
    public String Title;
    /// <summary>
    /// خلاصه توضیحات
    /// </summary>
    public String AbstractOfDescription;
    /// <summary>
    /// توضیحات
    /// </summary>
    public String Description;
    /// <summary>
    /// آدرس تصویر و بنر
    /// </summary>
    public String ImagePath;
    /// <summary>
    /// آدرس صفحه وبی که پس از خوندن رویداد یا کلیلک بر ان می خواهیم برود
    /// شاید بخواهیم آدرس فرمها در این بدهیم
    /// به همین خاطر از عبارات باقاعده آدرس وب استفاده نشده است
    /// </summary>
    public String PageLink;
    /// <summary>
    /// نوع لینکی که یک رویداد می تواند باشد
    /// </summary>
    public int Link;
    /// <summary>
    /// مکان نمایش
    /// </summary>
    public int Place;
    /// <summary>
    /// از تاریخ اعتبار نمایش خبر یا رویداد
    /// </summary>
    public String FromDate;
    /// <summary>
    /// تا تاریخ اعتبار نمایش خبر یا رویداد
    /// </summary>
    public String ToDate;
    /// <summary>
    /// تاریخ ایجاد
    /// </summary>
    public String Create;
    /// <summary>
    /// تاریخ ویرایش
    /// </summary>
    public String Modified;
    /// <summary>

    public int getId() {
        return Id;
    }

    public String getTitle() {
        return Title;
    }

    public String getAbstractOfDescription() {
        return AbstractOfDescription;
    }

    public String getDescription() {
        return Description;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public String getPageLink() {
        return PageLink;
    }

    public int getLink() {
        return Link;
    }

    public int getPlace() {
        return Place;
    }

    public String getFromDate() {
        return FromDate;
    }

    public String getToDate() {
        return ToDate;
    }

    public String getCreate() {
        return Create;
    }

    public String getModified() {
        return Modified;
    }

    public boolean isActive() {
        return IsActive;
    }

    /// فعال بود یا نبودن
    /// </summary>
    public boolean IsActive;
}