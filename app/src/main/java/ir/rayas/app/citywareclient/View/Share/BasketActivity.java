package ir.rayas.app.citywareclient.View.Share;


import android.app.Activity;
import android.support.design.widget.TabLayout;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import ir.rayas.app.citywareclient.Adapter.Pager.BasketPagerAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Fragment.Basket.BasketDeliveryFragment;

import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Basket.BasketSummeryViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.UserAddressViewModel;


public class BasketActivity extends BaseActivity {

    private BasketPagerAdapter Pager = null;

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

    public static BasketSummeryViewModel basketSummeryViewModel = new BasketSummeryViewModel();

    public TabLayout.Tab DefaultTab;
    public TabLayout BasketTabLayoutBasketActivity;
    public ViewPager BasketViewpagerBasketActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.BASKET_ACTIVITY);

        AccountRepository AccountRepository = new AccountRepository(this);
        AccountViewModel accountViewModel = AccountRepository.getAccount();
        basketSummeryViewModel.setUserId(accountViewModel.getUser().getId());


        int BusinessId  = getIntent().getExtras().getInt("BusinessId");
        int Id  = getIntent().getExtras().getInt("Id");
        String BusinessName  = getIntent().getExtras().getString("BusinessName");
        int BasketCount  = getIntent().getExtras().getInt("BasketCount");
        String Path  = getIntent().getExtras().getString("Path");
        double TotalPrice  = getIntent().getExtras().getDouble("TotalPrice");
        String Modified  = getIntent().getExtras().getString("Modified");
        boolean QuickItem  = getIntent().getExtras().getBoolean("QuickItem");


        basketSummeryViewModel.setBusinessId(BusinessId);
        basketSummeryViewModel.setBasketId(Id);
        basketSummeryViewModel.setBasketName(BusinessName);
        basketSummeryViewModel.setBasketCount(BasketCount);
        basketSummeryViewModel.setPath(Path);
        basketSummeryViewModel.setTotalPrice(TotalPrice);
        basketSummeryViewModel.setModified(Modified);
        basketSummeryViewModel.setQuickItem(QuickItem);


        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.basket);

        //ایجاد طرح بندی صفحه
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

    /**
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    private void CreateLayout() {

        BasketViewpagerBasketActivity = findViewById(R.id.BasketViewpagerBasketActivity);
        BasketTabLayoutBasketActivity = findViewById(R.id.BasketTabLayoutBasketActivity);

        String[] TabNames = new String[]{"1", "2", "3", "4"};
        Pager = new BasketPagerAdapter(getSupportFragmentManager(), TabNames);
        BasketViewpagerBasketActivity.setAdapter(Pager);
        //تعداد فرگمنت هایی که می تواند باز بماند در viewPager
        BasketViewpagerBasketActivity.setOffscreenPageLimit(4);
        BasketTabLayoutBasketActivity.setupWithViewPager(BasketViewpagerBasketActivity);

        LayoutUtility.SetTabCustomFont(BasketTabLayoutBasketActivity);

        DefaultTab = BasketTabLayoutBasketActivity.getTabAt(3);
        DefaultTab.select();

        //رویداد های مربوط به تغییر صفحات
        final Activity CurrentActivity = this;
        BasketViewpagerBasketActivity.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
    protected void onGetResult(ActivityResult Result) {
        if (Result.getFromActivityId() == getCurrentActivityId()) {
            switch (Result.getToActivityId()) {
                case ActivityIdList.USER_ADDRESS_SET_ACTIVITY:
                    UserAddressViewModel ViewModel = (UserAddressViewModel) Result.getData().get("AddressViewModel");

                        ((BasketDeliveryFragment) Pager.getFragmentByIndex(1)).getBasketUserAddressRecyclerViewAdapter().AddViewModel(ViewModel);

                    break;

            }
        }
        super.onGetResult(Result);
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
