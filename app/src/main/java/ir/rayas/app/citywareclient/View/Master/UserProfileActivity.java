package ir.rayas.app.citywareclient.View.Master;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import ir.rayas.app.citywareclient.Adapter.Pager.UserProfilePagerAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Fragment.UserProfile.UserAddressFragment;
import ir.rayas.app.citywareclient.View.Fragment.UserProfile.UserBusinessFragment;
import ir.rayas.app.citywareclient.View.Fragment.UserProfile.UserPosterFragment;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessViewModel;
import ir.rayas.app.citywareclient.ViewModel.Poster.PurchasedPosterViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.UserAddressViewModel;

public class UserProfileActivity extends BaseActivity {

    private UserProfilePagerAdapter Pager = null;
    private AccountRepository AccountRepository = null;
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

    public AccountRepository getAccountRepository() {
        return AccountRepository;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.USER_PROFILE_ACTIVITY);

        //ایجاد ریپو جهت مدیریت اکانت کاربر
        AccountRepository = new AccountRepository(this);

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, 0);

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
        ViewPager ProfileViewpager = findViewById(R.id.ProfileViewpager);
        TabLayout ProfileTabLayout = findViewById(R.id.ProfileTabLayout);

        String[] TabNames = new String[]{getString(R.string.posters), getString(R.string.packages), getString(R.string.business), getString(R.string.extended_information), getString(R.string.address), getString(R.string.primary_information)};
        Pager = new UserProfilePagerAdapter(getSupportFragmentManager(), TabNames);
        ProfileViewpager.setAdapter(Pager);
        //تعداد فرگمنت هایی که می تواند باز بماند در viewPager
        ProfileViewpager.setOffscreenPageLimit(6);
        ProfileTabLayout.setupWithViewPager(ProfileViewpager);

        LayoutUtility.SetTabCustomFont(ProfileTabLayout);

        TabLayout.Tab DefaultTab = ProfileTabLayout.getTabAt(5);
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
    protected void onGetResult(ActivityResult Result) {
        if (Result.getFromActivityId() == getCurrentActivityId()) {
            switch (Result.getToActivityId()) {
                case ActivityIdList.USER_ADDRESS_SET_ACTIVITY:
                    UserAddressViewModel ViewModel = (UserAddressViewModel) Result.getData().get("AddressViewModel");
                    if ((boolean) Result.getData().get("IsAdd")) {
                        ((UserAddressFragment) Pager.getFragmentByIndex(4)).getUserAddressRecyclerViewAdapter().AddViewModel(ViewModel);
                    } else {
                        ((UserAddressFragment) Pager.getFragmentByIndex(4)).getUserAddressRecyclerViewAdapter().SetViewModel(ViewModel);
                    }
                    break;
                case ActivityIdList.BUSINESS_SET_ACTIVITY:
                    BusinessViewModel ViewModelBusiness = (BusinessViewModel) Result.getData().get("BusinessViewModel");
                    if ((boolean) Result.getData().get("IsAdd")) {
                            ((UserBusinessFragment) Pager.getFragmentByIndex(2)).getBusinessListRecyclerViewAdapter().AddViewModel(ViewModelBusiness);
                    } else {
                            ((UserBusinessFragment) Pager.getFragmentByIndex(2)).getBusinessListRecyclerViewAdapter().SetViewModel(ViewModelBusiness);
                    }
                    break;

                case ActivityIdList.BUY_POSTER_SET_ACTIVITY:
                    PurchasedPosterViewModel purchasedPosterViewModel = (PurchasedPosterViewModel) Result.getData().get("PurchasedPosterViewModel");

                    ((UserPosterFragment) Pager.getFragmentByIndex(0)).getPosterValidRecyclerViewAdapter().SetViewModel(purchasedPosterViewModel);

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
