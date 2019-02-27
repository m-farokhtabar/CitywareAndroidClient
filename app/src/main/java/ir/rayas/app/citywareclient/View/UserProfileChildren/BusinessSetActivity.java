package ir.rayas.app.citywareclient.View.UserProfileChildren;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

import ir.rayas.app.citywareclient.Adapter.Pager.UserBusinessSetPagerAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResultPassing;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Base.IButtonBackToolbarListener;
import ir.rayas.app.citywareclient.View.Fragment.Business.BusinessContactFragment;
import ir.rayas.app.citywareclient.View.Fragment.Business.BusinessFragment;
import ir.rayas.app.citywareclient.View.Fragment.Business.BusinessOpenTimeFragment;
import ir.rayas.app.citywareclient.View.Fragment.Business.ProductListFragment;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessContactViewModel;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessOpenTimeViewModel;
import ir.rayas.app.citywareclient.ViewModel.Order.ProductViewModel;

public class BusinessSetActivity extends BaseActivity implements IButtonBackToolbarListener {

    private UserBusinessSetPagerAdapter Pager = null;
    private TabLayout BusinessFragmentTabLayout = null;
    private int RetryType = 0;
    private int FragmentIndex = 0;
    ViewPager BusinessFragmentViewpager;

    private String IsSet;
    private boolean IsAdd;
    private int BusinessId;

    /**
     * ثبت اطلعات یک
     * دریافت اطلاعات دو
     *
     * @param retryType
     */
    public void setRetryType(int retryType) {
        RetryType = retryType;
    }

    /**
     * توسط این متد که توسط فرگمنت صدا زده می شود اکتیوتی می تواند فرگمنت جاری را بدست آورد
     *
     * @param fragmentIndex
     */
    public void setFragmentIndex(int fragmentIndex) {
        FragmentIndex = fragmentIndex;
    }

    public String getIsSet() {
        return IsSet;
    }

    public void setIsSet(String isSet) {
        IsSet = isSet;
    }

    public int getBusinessId() {
        return BusinessId;
    }

    public void setBusinessId(int businessId) {
        BusinessId = businessId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_set);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.BUSINESS_SET_ACTIVITY);

        //مشخص شدن صفحه ویرایش  یا جدید - البته با ثبت اولیه این حالت به ویرایش تغییر خواهد کرد
        IsSet = getIntent().getExtras().getString("SetBusiness");
        //برای حل مشکل بالا و اینکه بفهیم واقعا الان در صفحه جدید هستیم یا ویرایش
        IsAdd = IsSet.equals("New");
        //یا قبلا کسب وکار وجود داشته
        //اگر وجود ندارد و جدید است  این متغیر باید از طریق فرگمنت ایجاد کسب و کار پر شود
        BusinessId = getIntent().getExtras().getInt("BusinessId");

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        },R.string.business);

        //اضافه کردن رویداد دکمه بازگشت نوار ابزار ساده تا قبل از بازشگت در صورت نیاز بتوان عملیاتی انجام داد
        this.setButtonBackToolbarListener(this);
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
        BusinessFragmentViewpager = findViewById(R.id.BusinessFragmentViewpager);
        BusinessFragmentTabLayout = findViewById(R.id.BusinessFragmentTabLayout);

        String[] TabNames = new String[]{getString(R.string.working_hours), getString(R.string.call_way), getString(R.string.product), getString(R.string.business)};

        Pager = new UserBusinessSetPagerAdapter(getSupportFragmentManager(), TabNames);
        BusinessFragmentViewpager.setAdapter(Pager);
        //تعداد فرگمنت هایی که می تواند باز بماند در viewPager
        BusinessFragmentViewpager.setOffscreenPageLimit(4);
        BusinessFragmentTabLayout.setupWithViewPager(BusinessFragmentViewpager);

        LayoutUtility.SetTabCustomFont(BusinessFragmentTabLayout);

        TabLayout.Tab DefaultTab = BusinessFragmentTabLayout.getTabAt(3);
        DefaultTab.select();


        if (IsSet.equals("New")) {
            EnableOrDisableChildTab(false);
        } else if (IsSet.equals("Edit")) {
            EnableOrDisableChildTab(true);
        }


        //رویداد های مربوط به تغییر صفحات
        final Activity CurrentActivity = this;
        BusinessFragmentViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    public void EnableOrDisableChildTab(boolean IsCheck) {

        LinearLayout tabBusiness = ((LinearLayout) BusinessFragmentTabLayout.getChildAt(0));
        tabBusiness.setEnabled(IsCheck);
        for (int i = 0; i < tabBusiness.getChildCount(); i++) {
            tabBusiness.getChildAt(i).setClickable(IsCheck);
        }
        if (!IsCheck) {
            TouchViewPager();
            SelectedTab();
        }
    }

    private void TouchViewPager() {

        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (IsSet.equals("New")) {
                    BusinessFragmentViewpager.setCurrentItem(3);
                }
            }

            public void onPageScrollStateChanged(int state) {
                if (IsSet.equals("New")) {
                    ShowToast(getResources().getString(R.string.please_submit_your_business_first), Toast.LENGTH_LONG, MessageType.Warning);
                }
            }

            public void onPageSelected(int position) {
            }
        };
        BusinessFragmentViewpager.addOnPageChangeListener(onPageChangeListener);
    }

    @Override
    protected void onGetResult(ActivityResult Result) {
        if (Result.getFromActivityId() == getCurrentActivityId()) {
            switch (Result.getToActivityId()) {
                case ActivityIdList.SELECT_REGION_ACTIVITY:
                    ((BusinessFragment) Pager.getFragmentByIndex(3)).setRegionId((int)Result.getData().get("RegionId"));
                    ((BusinessFragment) Pager.getFragmentByIndex(3)).setRegionName((String)Result.getData().get("RegionName"));
                    break;
                case ActivityIdList.SELECT_BUSINESS_CATEGORY_ACTIVITY:
                    ((BusinessFragment) Pager.getFragmentByIndex(3)).setBusinessCategoryId((int)Result.getData().get("BusinessCategoryId"));
                    ((BusinessFragment) Pager.getFragmentByIndex(3)).setBusinessCategoryName((String)Result.getData().get("BusinessCategoryName"));
                    break;
                case ActivityIdList.MAP_ACTIVITY:
                    ((BusinessFragment) Pager.getFragmentByIndex(3)).setLatitude((double)Result.getData().get("Latitude"));
                    ((BusinessFragment) Pager.getFragmentByIndex(3)).setLongitude((double)Result.getData().get("Longitude"));
                    break;
                case ActivityIdList.BUSINESS_CONTACT_SET_ACTIVITY:
                    if ((boolean) Result.getData().get("IsAdd"))
                        ((BusinessContactFragment) Pager.getFragmentByIndex(1)).getBusinessContactRecyclerViewAdapter().AddViewModel((BusinessContactViewModel)Result.getData().get("BusinessContactViewModel"));
                    else
                        ((BusinessContactFragment) Pager.getFragmentByIndex(1)).getBusinessContactRecyclerViewAdapter().SetViewModel((BusinessContactViewModel)Result.getData().get("BusinessContactViewModel"));
                    break;
                case ActivityIdList.BUSINESS_OPEN_TIME_SET_ACTIVITY:
                    if ((boolean) Result.getData().get("IsAdd"))
                        ((BusinessOpenTimeFragment) Pager.getFragmentByIndex(0)).getBusinessOpenTimeRecyclerViewAdapter().AddViewModel((BusinessOpenTimeViewModel)Result.getData().get("BusinessOpenTimeViewModel"));
                    else
                        ((BusinessOpenTimeFragment) Pager.getFragmentByIndex(0)).getBusinessOpenTimeRecyclerViewAdapter().SetViewModel((BusinessOpenTimeViewModel)Result.getData().get("BusinessOpenTimeViewModel"));
                    break;
                case ActivityIdList.PRODUCT_SET_ACTIVITY:
                    ProductViewModel CurrentProductViewModel = (ProductViewModel) Result.getData().get("ProductViewModel");
                    if ((boolean) Result.getData().get("IsAdd")) {
                        ((ProductListFragment) Pager.getFragmentByIndex(2)).getProductListRecyclerViewAdapter().AddViewModel(CurrentProductViewModel);
                        if (CurrentProductViewModel!=null)
                            ((ProductListFragment) Pager.getFragmentByIndex(2)).getProductListRecyclerViewAdapter().SetImage(CurrentProductViewModel.getId(),(String) Result.getData().get("BusinessImagePath"));
                    }
                    else {
                        ((ProductListFragment) Pager.getFragmentByIndex(2)).getProductListRecyclerViewAdapter().SetViewModel(CurrentProductViewModel);
                        if (CurrentProductViewModel!=null)
                            ((ProductListFragment) Pager.getFragmentByIndex(2)).getProductListRecyclerViewAdapter().SetImage(CurrentProductViewModel.getId(), (String) Result.getData().get("BusinessImagePath"));
                    }
                    break;
            }
        }
        super.onGetResult(Result);
    }

    /**
     * دریافت ویومدل از فرگمنت کسب وکار جهت ارسال به اکتیویتی پروفایل کاربر
     */
    private void SendDataToParentActivity() {

        HashMap<String, Object> Output = new HashMap<>();
        Output.put("IsAdd", IsAdd);
        Output.put("BusinessViewModel", ((BusinessFragment) Pager.getFragmentByIndex(3)).getOutputViewModel());
        ActivityResultPassing.Push(new ActivityResult(getParentActivity(), getCurrentActivityId(), Output));
    }

    @Override
    public void ClickOnButtonBackToolbar() {
        SendDataToParentActivity();
    }

    public void SelectedTab() {
        BusinessFragmentTabLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (IsSet.equals("New")) {
                    ShowToast(getResources().getString(R.string.please_submit_your_business_first), Toast.LENGTH_LONG, MessageType.Warning);
                }
                return false;
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
        SendDataToParentActivity();
        super.onBackPressed();
    }
}
