package ir.rayas.app.citywareclient.View.MasterChildren;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import ir.rayas.app.citywareclient.Adapter.Pager.BusinessCommissionPagerAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Fragment.BusinessCommission.BusinessNoCommissionReceivedFragment;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;

public class ShowBusinessCommissionActivity extends BaseActivity {

    private BusinessCommissionPagerAdapter Pager = null;
    private int RetryType = 0;
    private int FragmentIndex = 0;

    private String BusinessName ="";
    double TotalPrice = 0.0;


    /**
     * ثبت اطلعات یک
     * دریافت اطلاعات دو
     *
     * @param retryType
     */
    public void setRetryType(int retryType) {
        RetryType = retryType;
    }

    public void setFragmentIndex(int fragmentIndex) {
        FragmentIndex = fragmentIndex;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_business_commission);


        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.SHOW_BUSINESS_COMMISSION_ACTIVITY);

        BusinessName =getIntent().getExtras().getString("BusinessName")  ;

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.income_from_business);

        //ایجاد طرحبندی صفحه
        CreateLayout();
    }

    /**
     * رویداد زمانی اجرا  می شود که کاربر دکمه تلاش مجدد را ضار دهد
     */
    private void RetryButtonOnClick() {
        switch (RetryType) {
            //ثبت اطلاعات
            case 1:
                HideLoading();
                break;
            //دریافت اطلاعات
            case 2:
                Pager.getLoadDataByIndex(FragmentIndex).LoadData();
                break;
        }
    }

    private void CreateLayout() {
        TextViewPersian BusinessNameTextViewShowBusinessCommissionActivity = findViewById(R.id.BusinessNameTextViewShowBusinessCommissionActivity);
        BusinessNameTextViewShowBusinessCommissionActivity.setText(BusinessName);

        ViewPager ProfileViewpager = findViewById(R.id.BusinessCommissionViewpagerShowBusinessCommissionActivity);
        TabLayout ProfileTabLayout = findViewById(R.id.BusinessCommissionTabLayoutShowBusinessCommissionActivity);

        String[] TabNames = new String[]{ getString(R.string.archives), getString(R.string.commission_paid), getString(R.string.commission_un_paid),  getString(R.string.customer_search),getString(R.string.report)};
        Pager = new BusinessCommissionPagerAdapter(getSupportFragmentManager(), TabNames);
        ProfileViewpager.setAdapter(Pager);
        //تعداد فرگمنت هایی که می تواند باز بماند در viewPager
        ProfileViewpager.setOffscreenPageLimit(5);
        ProfileTabLayout.setupWithViewPager(ProfileViewpager);

        LayoutUtility.SetTabCustomFont(ProfileTabLayout);

        TabLayout.Tab DefaultTab = ProfileTabLayout.getTabAt(4);
        DefaultTab.select();

        //رویداد های مربوط به تغییر صفحات
        final Activity CurrentActivity = this;
        ProfileViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Utility.HideKeyboard(CurrentActivity);
            }

            @Override
            public void onPageScrolled(int position, float offset, int offsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }


    @SuppressLint("SetTextI18n")
    public void SetViewPriceInFooter(double Price, boolean IsAdd) {

        if (IsAdd) {
            TotalPrice = TotalPrice + Price;
        } else {
            TotalPrice = TotalPrice - Price;
        }

        ((BusinessNoCommissionReceivedFragment) Pager.getFragmentByIndex(2)).SetViewPriceInFooter(TotalPrice);

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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

