package ir.rayas.app.citywareclient.View.MasterChildren;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import ir.rayas.app.citywareclient.Adapter.Pager.DiscountPagerAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;

public class DiscountActivity extends BaseActivity  {

    private DiscountPagerAdapter Pager = null;
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
        setContentView(R.layout.activity_discounts);

        setCurrentActivityId(ActivityIdList.USER_PRIZE_ACTIVITY);

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.get_discounts);

        //ایجاد طرح بندی صفحه
        CreateLayout();

    }

    /**
     * در صورتی که در ارتباط با اینترنت مشکلی بوجود آمده و کاربر دکمه تلاش مجدد را فشار داده است
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

    /**
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    private void CreateLayout() {
        ViewPager DiscountViewpagerDiscountActivity = findViewById(R.id.DiscountViewpagerDiscountActivity);
        TabLayout DiscountTabLayoutDiscountActivity = findViewById(R.id.DiscountTabLayoutDiscountActivity);

        String[] TabNames = new String[]{ getString(R.string.expire_discount), getString(R.string.usage_discount), getString(R.string.new_discount)};
        Pager = new DiscountPagerAdapter(getSupportFragmentManager(), TabNames);
        DiscountViewpagerDiscountActivity.setAdapter(Pager);
        //تعداد فرگمنت هایی که می تواند باز بماند در viewPager
        DiscountViewpagerDiscountActivity.setOffscreenPageLimit(3);
        DiscountTabLayoutDiscountActivity.setupWithViewPager(DiscountViewpagerDiscountActivity);

        LayoutUtility.SetTabCustomFont(DiscountTabLayoutDiscountActivity);

        TabLayout.Tab DefaultTab = DiscountTabLayoutDiscountActivity.getTabAt(2);
        DefaultTab.select();

        //رویداد های مربوط به تغییر صفحات
        final Activity CurrentActivity = this;
        DiscountViewpagerDiscountActivity.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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


