package ir.rayas.app.citywareclient.Share.Layout.Font;

import android.content.Context;
import android.graphics.Typeface;

/**
 * تمامی فونتهای مورد نیاز پروژه
 * Created by Programmer on 2/3/2018.
 */

public class Font {
    /**
     * فونت اصلی برنامه
     */
    public static Typeface MasterFont;
    /**
     * فونت نازک تر جهت استفاده در بنر و عنواین
     */
    public static Typeface MasterLightFont;
    /**
     * آیکن جهت استفاده در دکمه ها
     */
    public static Typeface MasterIcon;

    /**
     * پر کردن فونتهای پیش فرض پروژه از منبع
     * @param Context
     */
    public static void SetFont(Context Context)
    {
        MasterFont      = Typeface.createFromAsset(Context.getAssets(), "fonts/iransanslight.ttf");
        MasterLightFont = Typeface.createFromAsset(Context.getAssets(), "fonts/iransansmobileultralight.ttf");
        MasterIcon      = Typeface.createFromAsset(Context.getAssets(), "fonts/fontawesome.ttf");
    }
}
