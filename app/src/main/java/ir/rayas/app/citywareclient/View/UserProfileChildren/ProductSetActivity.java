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
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.Pager.ProductSetPagerAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResultPassing;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Base.IButtonBackToolbarListener;
import ir.rayas.app.citywareclient.View.Fragment.Product.ProductFragment;
import ir.rayas.app.citywareclient.View.Fragment.Product.ProductImageFragment;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Order.ProductImageViewModel;

public class ProductSetActivity extends BaseActivity implements IButtonBackToolbarListener {

    private ProductSetPagerAdapter Pager = null;
    private TabLayout TabLayoutProductSetActivity = null;
    private ViewPager ViewpagerProductSetActivity = null;
    private int RetryType = 0;
    private int FragmentIndex = 1;

    private String IsSet;
    private boolean IsAdd;
    private int BusinessId;
    private int ProductId;

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

    public String getIsSet() {
        return IsSet;
    }

    public void setIsSet(String isSet) {
        IsSet = isSet;
    }

    public int getBusinessId() {
        return BusinessId;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_set);
        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.PRODUCT_SET_ACTIVITY);


        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.product);
        //اضافه کردن رویداد دکمه بازگشت نوار ابزار ساده تا قبل از بازشگت در صورت نیاز بتوان عملیاتی انجام داد
        this.setButtonBackToolbarListener(this);

        //مشخص شدن صفحه ویرایش آدرس یا آدرس جدید
        IsSet = getIntent().getExtras().getString("SetProduct");
        BusinessId = getIntent().getExtras().getInt("BusinessId");
        ProductId = getIntent().getExtras().getInt("ProductId");

        //برای حل مشکل بالا و اینکه بفهیم واقعا الان در صفحه جدید هستیم یا ویرایش
        IsAdd = IsSet.equals("New");


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
        ViewpagerProductSetActivity = findViewById(R.id.ViewpagerProductSetActivity);
        TabLayoutProductSetActivity = findViewById(R.id.TabLayoutProductSetActivity);

        String[] TabNames = new String[]{getString(R.string.images), getString(R.string.product)};
        Pager = new ProductSetPagerAdapter(getSupportFragmentManager(), TabNames);
        ViewpagerProductSetActivity.setAdapter(Pager);
        //تعداد فرگمنت هایی که می تواند باز بماند در viewPager
        ViewpagerProductSetActivity.setOffscreenPageLimit(2);
        TabLayoutProductSetActivity.setupWithViewPager(ViewpagerProductSetActivity);

        LayoutUtility.SetTabCustomFont(TabLayoutProductSetActivity);

        TabLayout.Tab DefaultTab = TabLayoutProductSetActivity.getTabAt(1);
        DefaultTab.select();

        if (IsSet.equals("New")) {
            EnableOrDisableChildTab(false);
        } else if (IsSet.equals("Edit")) {
            EnableOrDisableChildTab(true);
        }


        //رویداد های مربوط به تغییر صفحات
        final Activity CurrentActivity = this;
        ViewpagerProductSetActivity.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

        LinearLayout tabBusiness = ((LinearLayout) TabLayoutProductSetActivity.getChildAt(0));
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
                    ViewpagerProductSetActivity.setCurrentItem(1);
                }
            }

            public void onPageScrollStateChanged(int state) {
                if (IsSet.equals("New")) {
                    ShowToast(getResources().getString(R.string.please_submit_your_product_first), Toast.LENGTH_LONG, MessageType.Warning);
                }
            }

            public void onPageSelected(int position) {
            }
        };
        ViewpagerProductSetActivity.addOnPageChangeListener(onPageChangeListener);
    }

    @Override
    protected void onGetResult(ActivityResult Result) {
        if (Result.getFromActivityId() == getCurrentActivityId()) {
            switch (Result.getToActivityId()) {
                case ActivityIdList.PRODUCT_IMAGE_SET_ACTIVITY:
                    ProductImageViewModel ViewModel;
                    ViewModel = (ProductImageViewModel) Result.getData().get("ProductImageViewModel");
                    if ((boolean) Result.getData().get("IsAdd")) {
                        ((ProductImageFragment) Pager.getFragmentByIndex(0)).getProductImageRecyclerViewAdapter().AddViewModel(ViewModel);
                    } else {
                        ((ProductImageFragment) Pager.getFragmentByIndex(0)).getProductImageRecyclerViewAdapter().SetViewModel(ViewModel);
                    }
                    break;
            }
        }
        super.onGetResult(Result);
    }

    /**
     * دریافت ویومدل از فرگمنت محصولات جهت ارسال به اکتیویتی کسب وکار
     */
    private void SendDataToParentActivity() {
        HashMap<String, Object> Output = new HashMap<>();
        Output.put("IsAdd", IsAdd);
        try {
            Output.put("ProductViewModel", ((ProductFragment) Pager.getFragmentByIndex(1)).getOutputViewModel());
            if (Pager.getFragmentByIndex(0) != null && (Pager.getFragmentByIndex(0) instanceof ProductImageFragment) && (((ProductImageFragment) Pager.getFragmentByIndex(0)).getProductImageRecyclerViewAdapter() != null)) {
                List<ProductImageViewModel> ProductImageList = ((ProductImageFragment) Pager.getFragmentByIndex(0)).getProductImageRecyclerViewAdapter().getViewModelList();
                if (ProductImageList != null && ProductImageList.size() > 0) {
                    int Index = 0;
                    int MaxOrder = ProductImageList.get(0).getOrder();
                    for (int i = 0; i < ProductImageList.size(); i++) {
                        if (ProductImageList.get(i).getOrder() > MaxOrder) {
                            MaxOrder = ProductImageList.get(i).getOrder();
                            Index = i;
                        }
                    }
                    Output.put("BusinessImagePath", ProductImageList.get(Index).getFullPath());
                } else {
                    Output.put("BusinessImagePath", "");
                }
            } else {
                Output.put("BusinessImagePath", "");
            }
        } catch (Exception Ex) {
            Output.put("ProductViewModel", null);
            Output.put("BusinessImagePath", "");
        }
        ActivityResultPassing.Push(new ActivityResult(getParentActivity(), getCurrentActivityId(), Output));
    }


    @Override
    public void ClickOnButtonBackToolbar() {
        SendDataToParentActivity();
    }

    public void SelectedTab() {

        TabLayoutProductSetActivity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (IsSet.equals("New")) {
                    ShowToast(getResources().getString(R.string.please_submit_your_product_first), Toast.LENGTH_LONG, MessageType.Warning);
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
