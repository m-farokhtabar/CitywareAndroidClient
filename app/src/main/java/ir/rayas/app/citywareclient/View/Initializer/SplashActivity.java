package ir.rayas.app.citywareclient.View.Initializer;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Helper.AppController;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Constant.LayoutConstant;
import ir.rayas.app.citywareclient.Share.Layout.View.ProgressBarView;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;

public class SplashActivity extends AppCompatActivity {

    private LinearLayout MasterLinearLayout = null;
    private ImageView LogoImageView = null;
    private ProgressBarView SplashProgressBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //تنظیم فونت برنامه
        Font.SetFont(AppController.getInstance());
        //ایجاد طرحبندی صفحه
        CreateLayout();
        //تایمر جهت نمایش صفحه splash
        GoToIntroducePage();
    }

    /**
     * ایجاد صفحه ورود برنامه
     * شامل یک تصویر و نوار پیمایش جهت نمایش حالت بارگذاری
     */
    private void CreateLayout() {
        //Find All View
        MasterLinearLayout = this.findViewById(R.id.SplashMasterLinearLayout);
        LogoImageView = this.findViewById(R.id.SplashLogoImageView);

        //Get Width And Height Of Screen
        int ScreenWidth = LayoutUtility.GetWidthAccordingToScreen(this, 1);
        int ScreenHeight = LayoutUtility.GetHeightAccordingToScreen(this, 1);

        //Set Gradient Background From Server Or Asset
        int[] BackgroundColor = new int[]{LayoutUtility.GetColorFromResource(this, R.color.SplashBackgroundColorLight)
                , LayoutUtility.GetColorFromResource(this, R.color.SplashBackgroundColorDark)};
        GradientDrawable BackgroundGradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, BackgroundColor);
        BackgroundGradientDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        BackgroundGradientDrawable.setGradientRadius(ScreenWidth / 1.7F);
        BackgroundGradientDrawable.setGradientCenter(0.5f, 0.5f);
        LayoutUtility.setBackgroundDrawable(MasterLinearLayout, BackgroundGradientDrawable);

        //Set Size Of Logo
        int ImageWidth = (int) (ScreenWidth / 2.2F);
        int ImageHeight = (int) (ScreenWidth / 2.2F);
        LinearLayout.LayoutParams LogoImageViewLayoutParams = new LinearLayout.LayoutParams(ImageWidth, ImageHeight);
        //Set Position Of Logo From Top And Left
        int ImageLeft = (ScreenWidth / 2) - (ImageWidth / 2);
        LogoImageViewLayoutParams.setMargins(ImageLeft, ScreenHeight / 6, 0, 0);
        LogoImageView.setLayoutParams(LogoImageViewLayoutParams);
        //Loading Logo From Server Or Asset
        LayoutUtility.LoadImageWithGlide(this, R.drawable.logo, LogoImageView, LayoutConstant.SplashLogoWidthMaxSize, LayoutConstant.SplashLogoHeightMaxSize);

        //Add ProgressBar

        //Set Position Of ProgressBar
        int ProgressBarHeight = ScreenHeight / 600 == 0 ? 1 : ScreenHeight / 600;
        int ProgressBarWidth = (int) (ScreenWidth / 2.2F);
        int ProgressBarLeft = (ScreenWidth / 2) - (ImageWidth / 2);
        //Add ProgressBar
        SplashProgressBar = new ProgressBarView(this, ProgressBarWidth, ProgressBarHeight);
        ((LinearLayout.LayoutParams) SplashProgressBar.getLayoutParams()).setMargins(ProgressBarLeft, ScreenWidth / 10, 0, 0);
        SplashProgressBar.setBackgroundColor(LayoutUtility.GetColorFromResource(this, R.color.BackgroundColorProgressBar));
        SplashProgressBar.setProgressColor(LayoutUtility.GetColorFromResource(this, R.color.ColorProgressBar));
        MasterLinearLayout.addView(SplashProgressBar);

        SplashProgressBar.StartAnimation();
    }

    private void GoToIntroducePage()
    {
        final Timer CurrentTimer = new Timer();
        CurrentTimer.schedule(new TimerTask() {
                                  @Override
                                  public void run() {
                                      //به صفحه معرفی رفته
                                      Intent IntroduceIntent = new Intent(getApplicationContext(), IntroduceActivity.class);
                                      startActivity(IntroduceIntent);
                                      CurrentTimer.cancel();
                                      CurrentTimer.purge();
                                      finish();
                                  }
                              },3000,1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
        onLowMemory();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
