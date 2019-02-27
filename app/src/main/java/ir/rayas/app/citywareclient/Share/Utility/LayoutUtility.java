package ir.rayas.app.citywareclient.Share.Utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;

/**
 * توابع مروبط به طرح بندی وتصاویر در برنامه
 * Created by Programmer on 2/4/2018.
 */

public class LayoutUtility {
    /**
     * دریافت ارتفاع صفح نمایش
     *
     * @param CurrentActivity اکتیویتی فعال
     * @param Scale           محاسبه ارتفاع بر اساس مقیاس مورد نظر
     * @return پیکسل
     */
    public static int GetHeightAccordingToScreen(Activity CurrentActivity, double Scale) {
        Display display = CurrentActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        DisplayMetrics ScreenMetrics = new DisplayMetrics();
        CurrentActivity.getWindowManager().getDefaultDisplay().getMetrics(ScreenMetrics);
        return (int) (((double) (metrics.heightPixels)) / Scale);
    }

    /**
     * دریافت عرض صفحه نمایش
     *
     * @param CurrentActivity اکتیویتی فعال
     * @param Scale           محاسبه ارتفاع بر اساس مقیاس مورد نظر
     * @return پیکسل
     */
    public static int GetWidthAccordingToScreen(Activity CurrentActivity, double Scale) {
        Display display = CurrentActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        DisplayMetrics ScreenMetrics = new DisplayMetrics();
        CurrentActivity.getWindowManager().getDefaultDisplay().getMetrics(ScreenMetrics);
        return (int) (((double) (metrics.widthPixels)) / Scale);
    }

    /**
     * نمایش تصویر در سایز مشخص توسط آدرس اینترنی
     *
     * @param CurrentContext اکتیوتی مورد نظر
     * @param ImageUrl       آدرس تصویر اینترنتی
     * @param CurrentImage   کامپوننتی که باید تصویر در آن نمایش داده شود
     * @param ImageWidth     عرضی که می خوهیم تصویر داشته باشد
     * @param ImageHeight    ارتفاعی که می خواهیم تصویر داشته باشد
     */
    public static void LoadImageWithGlide(Context CurrentContext, String ImageUrl, ImageView CurrentImage, int ImageWidth, int ImageHeight) {
        //if there is a image url for downloading
        if (ImageUrl.trim() != "") {
            //remove invalid char
            final String UrlForLoadingImage = Uri.encode(ImageUrl, DefaultConstant.InvalidChar);
            RequestOptions myOptions = new RequestOptions().override(ImageWidth, ImageHeight).fitCenter();
            Glide.with(CurrentContext).load(UrlForLoadingImage).apply(myOptions).into(CurrentImage);
        }
    }
    /**
     * نمایش تصویر در سایز دلخواه توسط آدرس اینترنی
     *
     * @param CurrentContext اکتیوتی مورد نظر
     * @param ImageUrl       آدرس تصویر اینترنتی
     * @param CurrentImage   کامپوننتی که باید تصویر در آن نمایش داده شود
     */
    public static void LoadImageWithGlide(Context CurrentContext, String ImageUrl, ImageView CurrentImage) {
        //if there is a image url for downloading
        if (ImageUrl.trim() != "") {
            //remove invalid char
            final String UrlForLoadingImage = Uri.encode(ImageUrl, DefaultConstant.InvalidChar);
            RequestOptions myOptions = new RequestOptions().fitCenter();
            Glide.with(CurrentContext).load(UrlForLoadingImage).apply(myOptions).into(CurrentImage);
        }
    }
    /**
     * نمایش تصویر در سایز مشخص توسط آدرس اینترنی
     *
     * @param CurrentContext اکتیوتی مورد نظر
     * @param ImageUrl       آدرس تصویر اینترنتی
     * @param CurrentImage   کامپوننتی که باید تصویر در آن نمایش داده شود
     * @param ImageWidth     عرضی که می خوهیم تصویر داشته باشد
     * @param ImageHeight    ارتفاعی که می خواهیم تصویر داشته باشد
     */
    public static void LoadImageWithGlide(Context CurrentContext, int ImageDrawableId, ImageView CurrentImage, int ImageWidth, int ImageHeight) {
        Drawable ImageDrawable;
        int CurrentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (CurrentApiVersion >= Build.VERSION_CODES.LOLLIPOP)
            ImageDrawable = CurrentContext.getResources().getDrawable(ImageDrawableId, CurrentContext.getTheme());
        else
            ImageDrawable = CurrentContext.getResources().getDrawable(ImageDrawableId);
        RequestOptions myOptions = new RequestOptions().override(ImageWidth, ImageHeight).fitCenter();
        Glide.with(CurrentContext).load(ImageDrawable).apply(myOptions).into(CurrentImage);
    }
    /**
     * لود کردن پس زمینه برای یک ویو
     * @param CurrentContext
     * @param ImageUrl
     * @param CurrentView
     * @param ViewWidth عرض صفحه ای که باید عکس را نمایش دهد
     * @param ViewHeight
     * @param ImageWidth عرضی که باید تصویر لود شود
     * @param ImageHeight
     */
    public static void LoadBackgroundImageWithGlide(final Context CurrentContext, String ImageUrl, final View CurrentView, final int ViewWidth, final int ViewHeight, final int ImageWidth, final int ImageHeight) {
        if (ImageUrl.trim()!="")
        {
            final String UrlForLoadingImage = Uri.encode(ImageUrl, DefaultConstant.InvalidChar);
            RequestOptions myOptions = new RequestOptions().override(ImageWidth, ImageHeight).fitCenter();
            Glide.with(CurrentContext).asBitmap().load(UrlForLoadingImage).apply(myOptions).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                    Bitmap CenterCropBitmap = GetCenterCropImage(bitmap, ViewWidth, ViewHeight);
                    Drawable drawable = new BitmapDrawable(CenterCropBitmap);
                    setBackgroundDrawable(CurrentView,drawable);
                }
            });
        }
    }

    /**
     * Convert Image To Center Crop Image
     *قرار دادن تصویر در مرکز اندازه و پاک کردن قسمت های اضافی
     * @param source
     * @param HeightView
     * @param WidthView
     * @return
     */
    public static Bitmap GetCenterCropImage(Bitmap source, int WidthView, int HeightView) {
        int BitmapWidth = source.getWidth();
        int BitmapHeight = source.getHeight();
        int NewWidth;
        int NewHeight;
        int TopPosition = 0;
        int LeftPosition = 0;

        if (WidthView > HeightView) {
            NewWidth = WidthView;
            NewHeight = (NewWidth * BitmapHeight) / BitmapWidth;
            if (NewHeight < HeightView) {
                NewHeight = HeightView;
                NewWidth = (NewHeight * BitmapWidth) / BitmapHeight;
            }
        } else {
            NewHeight = HeightView;
            NewWidth = (NewHeight * BitmapWidth) / BitmapHeight;
            if (NewWidth < WidthView) {
                NewWidth = WidthView;
                NewHeight = (NewWidth * BitmapHeight) / BitmapWidth;
            }
        }
        if (NewHeight > HeightView)
            TopPosition = (NewHeight - HeightView) / 2;
        if (NewWidth > WidthView)
            LeftPosition = (NewWidth - WidthView) / 2;
        Bitmap ScaledBitmap = Bitmap.createScaledBitmap(source, NewWidth, NewHeight, true);
        Rect RectSource = new Rect(LeftPosition, TopPosition, LeftPosition + WidthView, TopPosition + HeightView);

        Bitmap BitmapOutPut = Bitmap.createBitmap(WidthView, HeightView, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(BitmapOutPut);
        RectF RectDestination = new RectF(0, 0, WidthView, HeightView);
        canvas.drawBitmap(ScaledBitmap, RectSource, RectDestination, null);
        return BitmapOutPut;
    }

    /**
     * ایجاد طیف نوری خطی
     *
     * @param BackgroundColorDark رنگ تیره
     * @param BackgroundColorLight رنگ روشن
     * @return
     */
    public static PaintDrawable SetLinearGradientBackgroundColor(final int BackgroundColorDark, final int BackgroundColorLight) {

        ShapeDrawable.ShaderFactory CurrentShaderFactory = new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                int StartX = width;
                int EndX = 0;

                LinearGradient CurrentLinearGradient = new LinearGradient(StartX, height / 2, EndX, height / 2,
                        new int[]{BackgroundColorDark, BackgroundColorLight},
                        new float[]{0.65f, 1.1f}, Shader.TileMode.REPEAT);
                return CurrentLinearGradient;
            }
        };
        PaintDrawable CurrentPaintDrawable = new PaintDrawable();
        CurrentPaintDrawable.setShape(new RectShape());
        CurrentPaintDrawable.setShaderFactory(CurrentShaderFactory);
        return CurrentPaintDrawable;
    }

    /**
     * دریافت شماره رنگ از منبع به صورت عددی
     * @param CurrentContext اکتیوتی جاری
     * @param ColorResourceId کد منبع رنگ
     * @return
     */
    public static int GetColorFromResource(Context CurrentContext, int ColorResourceId)
    {
        int Output;
        int CurrentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (CurrentApiVersion >= Build.VERSION_CODES.M)
            Output = CurrentContext.getResources().getColor(ColorResourceId, CurrentContext.getTheme());
        else
            Output = CurrentContext.getResources().getColor(ColorResourceId);
        return  Output;
    }

    /**
     * تنظیم پس زمینه یک کامپوننت
     * هر چیزی می تواند باشد
     * @param CurrentView کامپونتت
     * @param background شیی از نوع رسم
     * @return
     */
    public static  void setBackgroundDrawable(View CurrentView,Drawable background)
    {
        int CurrentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (CurrentApiVersion >= Build.VERSION_CODES.JELLY_BEAN)
            CurrentView.setBackground(background);
        else
            CurrentView.setBackgroundDrawable(background);
    }

    /**
     * تغییر فونت تب ها در tablayout
     * @param Current
     */
    public static void SetTabCustomFont(TabLayout Current) {

        ViewGroup vg = (ViewGroup) Current.getChildAt(0);
        int tabsCount = vg.getChildCount();

        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);

            int tabChildrenCount = vgTab.getChildCount();

            for (int i = 0; i < tabChildrenCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(Font.MasterFont);
                }
            }
        }
    }


}
