package ir.rayas.app.citywareclient.View.MasterChildren;

import android.app.Activity;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import ir.rayas.app.citywareclient.Adapter.Pager.MarketerCommissionPagerAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;

public class ShowMarketerCommissionDetailsActivity extends BaseActivity  {

    private MarketerCommissionPagerAdapter Pager = null;
    private int RetryType = 0;
    private int FragmentIndex = 0;
    

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
        setContentView(R.layout.activity_show_marketer_commission_details);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.SHOW_MARKETER_COMMISSION_ACTIVITY);

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.marketing_and_Business_income);


        CreateLayout();
        //دریافت اطلاعات از سرور
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

        ViewPager ProfileViewpager = findViewById(R.id.MarketerCommissionViewpagerShowMarketerCommissionDetailsActivity);
        TabLayout ProfileTabLayout = findViewById(R.id.MarketerCommissionTabLayoutShowMarketerCommissionDetailsActivity);

        String[] TabNames = new String[]{ getString(R.string.archives), getString(R.string.commission_received), getString(R.string.no_commission_received),  getString(R.string.new_introductions),getString(R.string.report)};
        Pager = new MarketerCommissionPagerAdapter(getSupportFragmentManager(), TabNames);
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

