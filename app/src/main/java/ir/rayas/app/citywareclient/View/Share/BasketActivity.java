package ir.rayas.app.citywareclient.View.Share;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import java.util.HashMap;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResultPassing;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Fragment.Basket.BasketDeliveryFragment;
import ir.rayas.app.citywareclient.View.Fragment.Basket.BasketListFragment;

import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Basket.BasketSummeryViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.UserAddressViewModel;


public class BasketActivity extends BaseActivity  {

    private FrameLayout BasketFrameLayoutBasketActivity = null;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.BASKET_ACTIVITY);

        AccountRepository AccountRepository = new AccountRepository(this);
        AccountViewModel accountViewModel = AccountRepository.getAccount();
        basketSummeryViewModel.setUserId(accountViewModel.getUser().getId());

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
               // getLoadDataByIndex(FragmentIndex).LoadData();
                break;
        }
    }

    /**
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    private void CreateLayout() {

        BasketFrameLayoutBasketActivity = findViewById(R.id.BasketFrameLayoutBasketActivity);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction BasketListTransaction = fragmentManager.beginTransaction();
        BasketListTransaction.replace(R.id.BasketFrameLayoutBasketActivity, new BasketListFragment());
        BasketListTransaction.commit();
    }

    @Override
    protected void onGetResult(ActivityResult Result) {
        if (Result.getFromActivityId() == getCurrentActivityId()) {
            switch (Result.getToActivityId()) {
                case ActivityIdList.USER_ADDRESS_SET_ACTIVITY:
                    UserAddressViewModel ViewModel = (UserAddressViewModel) Result.getData().get("AddressViewModel");
                    BasketDeliveryFragment.getBasketUserAddressRecyclerViewAdapter().AddViewModel(ViewModel);
                    BasketDeliveryFragment basketDeliveryFragment = new BasketDeliveryFragment();
                    FragmentTransaction BasketListTransaction = getSupportFragmentManager().beginTransaction();
                    BasketListTransaction.replace(R.id.BasketFrameLayoutBasketActivity, basketDeliveryFragment);
                    BasketListTransaction.commit();

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



        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }

    }


}
