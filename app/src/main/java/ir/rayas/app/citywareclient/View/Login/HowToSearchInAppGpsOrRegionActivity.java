package ir.rayas.app.citywareclient.View.Login;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.Toast;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.Service.Definition.RegionService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Adapter.Pager.HowToSearchInAppGpsOrRegionPagerAdapter;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Definition.RegionViewModel;

public class HowToSearchInAppGpsOrRegionActivity extends BaseActivity implements IResponseService {

    private ViewPager GpsRegionViewpager;
    private HowToSearchInAppGpsOrRegionPagerAdapter Pager;
    private RegionViewModel Region = null;
    private boolean LoadRegionIsSucceeded = false;

    private AccountRepository AccountRepository = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_search_in_app_gps_or_region);
        //ایجاد ریپو جهت مدیریت اکانت کاربر
        AccountRepository = new AccountRepository(this);
        //آماده سازی قسمت لودینگ و پنجره خطا و سیستم پیغام در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                ShowActivity();
            }
        },0);
        //نمایش نوار ابزار
        InitToolbarWithOutButton();
        //دریافت اطلاعات مناطق
        LoadData();
    }

    public AccountRepository getAccountRepository() {
        return AccountRepository;
    }

    public RegionViewModel getRegion() {
        return Region;
    }

    /**
     * نمایش دوباره همین اکتیویتی
     */
    private void ShowActivity() {
        if (!LoadRegionIsSucceeded)
            LoadData();
        else
            HideLoading();
    }

    /**
     * ایجاد طرح بندی ویوپیچر جهت بود کردن فرگمنت ها
     */
    private void CreateLayout() {
        GpsRegionViewpager = findViewById(R.id.GpsRegionViewpager);
        Pager = new HowToSearchInAppGpsOrRegionPagerAdapter(getSupportFragmentManager());
        GpsRegionViewpager.setAdapter(Pager);
    }

    /**
     * دریافت اطلاعات نحوای جهت پر کردن Recycle
     */
    private void LoadData() {
        ShowLoadingProgressBar();
        RegionService Service = new RegionService(this);
        Service.GetAllTree();
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        if (ServiceMethod == ServiceMethodType.RegionAllTreeGet) {

            Feedback<RegionViewModel> FeedBack = (Feedback<RegionViewModel>) Data;
            if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                Region = FeedBack.getValue();
                if (Region != null) {
                    LoadRegionIsSucceeded = true;
                    //ایجاد طرح بندی صفحه
                    CreateLayout();
                    HideLoading();
                } else {
                    ShowToast(FeedbackType.InvalidDataFormat.getMessage().replace("{0}", ""), Toast.LENGTH_LONG, MessageType.Warning);
                    ShowErrorInConnectDialog();
                }
            } else {
                if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                    ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                }
                ShowErrorInConnectDialog();
            }
        }
    }

    public void GoToFragmentById(int Id) {
        GpsRegionViewpager.setCurrentItem(Id);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Pager.getFragmentByIndex(GpsRegionViewpager.getCurrentItem()).onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        //اگر فرگمنت انتخاب منطقه بود با بک زدن به فرگمنت اصلی بر می گردد
        if (GpsRegionViewpager!=null && GpsRegionViewpager.getCurrentItem() == 1) {
            GpsRegionViewpager.setCurrentItem(0);
        } else {
            super.onBackPressed();
        }
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
