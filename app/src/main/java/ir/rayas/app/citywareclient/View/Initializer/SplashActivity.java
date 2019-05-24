package ir.rayas.app.citywareclient.View.Initializer;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.BusinessCategoryRepository;
import ir.rayas.app.citywareclient.Repository.RegionRepository;
import ir.rayas.app.citywareclient.Service.Definition.BusinessCategoryService;
import ir.rayas.app.citywareclient.Service.Definition.RegionService;
import ir.rayas.app.citywareclient.Service.Helper.AppController;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Constant.LayoutConstant;
import ir.rayas.app.citywareclient.Share.Layout.View.ProgressBarView;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.ViewModel.Definition.BusinessCategoryViewModel;
import ir.rayas.app.citywareclient.ViewModel.Definition.RegionViewModel;

public class SplashActivity extends BaseActivity implements IResponseService {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //تنظیم فونت برنامه
        Font.SetFont(AppController.getInstance());
        //ایجاد طرحبندی صفحه
        CreateLayout();
        //تایمر جهت نمایش صفحه splash
//           GoToIntroducePage();

        LoadData();
    }

    /**
     * ایجاد صفحه ورود برنامه
     * شامل یک تصویر و نوار پیمایش جهت نمایش حالت بارگذاری
     */
    private void CreateLayout() {
        //Find All View
        LinearLayout masterLinearLayout = this.findViewById(R.id.SplashMasterLinearLayout);
        ImageView logoImageView = this.findViewById(R.id.SplashLogoImageView);

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
        LayoutUtility.setBackgroundDrawable(masterLinearLayout, BackgroundGradientDrawable);

        //Set Size Of Logo
        int ImageWidth = (int) (ScreenWidth / 2.2F);
        int ImageHeight = (int) (ScreenWidth / 2.2F);
        LinearLayout.LayoutParams LogoImageViewLayoutParams = new LinearLayout.LayoutParams(ImageWidth, ImageHeight);
        //Set Position Of Logo From Top And Left
        int ImageLeft = (ScreenWidth / 2) - (ImageWidth / 2);
        LogoImageViewLayoutParams.setMargins(ImageLeft, ScreenHeight / 6, 0, 0);
        logoImageView.setLayoutParams(LogoImageViewLayoutParams);
        //Loading Logo From Server Or Asset
        LayoutUtility.LoadImageWithGlide(this, R.drawable.logo, logoImageView, LayoutConstant.SplashLogoWidthMaxSize, LayoutConstant.SplashLogoHeightMaxSize);

        //Add ProgressBar

        //Set Position Of ProgressBar
        int ProgressBarHeight = ScreenHeight / 600 == 0 ? 1 : ScreenHeight / 600;
        int ProgressBarWidth = (int) (ScreenWidth / 2.2F);
        int ProgressBarLeft = (ScreenWidth / 2) - (ImageWidth / 2);
        //Add ProgressBar
        ProgressBarView splashProgressBar = new ProgressBarView(this, ProgressBarWidth, ProgressBarHeight);
        ((LinearLayout.LayoutParams) splashProgressBar.getLayoutParams()).setMargins(ProgressBarLeft, ScreenWidth / 10, 0, 0);
        splashProgressBar.setBackgroundColor(LayoutUtility.GetColorFromResource(this, R.color.BackgroundColorProgressBar));
        splashProgressBar.setProgressColor(LayoutUtility.GetColorFromResource(this, R.color.ColorProgressBar));
        masterLinearLayout.addView(splashProgressBar);

        splashProgressBar.StartAnimation();
    }

    /**
     * دریافت اطلاعات نحوای جهت پر کردن Recycle
     */
    private void LoadData() {

        RegionService Service = new RegionService(this);
        Service.GetAllTree();
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        if (ServiceMethod == ServiceMethodType.RegionAllTreeGet) {

            Intent IntroduceIntent = new Intent(getApplicationContext(), IntroduceActivity.class);
            startActivity(IntroduceIntent);
            finish();


            Feedback<RegionViewModel> FeedBack = (Feedback<RegionViewModel>) Data;
            if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                if (FeedBack.getValue() != null) {
                    RegionRepository regionRepository = new RegionRepository();
                    regionRepository.SetAll(FeedBack.getValue());

                    BusinessCategoryService Service = new BusinessCategoryService(this);
                    Service.GetAllTree();

                } else {
                    ShowToast(FeedbackType.InvalidDataFormat.getMessage().replace("{0}", ""), Toast.LENGTH_LONG, MessageType.Warning);
                    ShowErrorInConnectDialog();
                }
            } else {
                if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                    ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                } else {
                    ShowErrorInConnectDialog();
                }
            }
        } else if (ServiceMethod == ServiceMethodType.BusinessCategoryTreeGet) {

            Feedback<BusinessCategoryViewModel> FeedBack = (Feedback<BusinessCategoryViewModel>) Data;

            if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                if (FeedBack.getValue() != null) {

                    BusinessCategoryRepository businessCategoryRepository = new BusinessCategoryRepository();
                    businessCategoryRepository.SetAll(FeedBack.getValue());

                    Intent IntroduceIntent = new Intent(getApplicationContext(), IntroduceActivity.class);
                    startActivity(IntroduceIntent);
                    finish();

                } else {
                    ShowToast(FeedbackType.InvalidDataFormat.getMessage().replace("{0}", ""), Toast.LENGTH_LONG, MessageType.Warning);
                    ShowErrorInConnectDialog();
                }
            } else {
                if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                    ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                }else {
                    ShowErrorInConnectDialog();
                }
            }
        }
    }

    private void GoToIntroducePage() {
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
        }, 3000, 1);
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
