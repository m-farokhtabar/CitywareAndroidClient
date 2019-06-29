package ir.rayas.app.citywareclient.View.UserProfileChildren;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Fragment.Package.BusinessListForPackageFragment;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;

public class PackageActivity extends BaseActivity {

    private int RetryType = 0;

    public void setRetryType(int retryType) {
        RetryType = retryType;
    }

    public String ValueIntent = "";
    public String BusinessName = "";
    public int PackageId = 0;
    public int BusinessId = 0;

    public String getBusinessName() {
        return BusinessName;
    }

    public int getBusinessId() {
        return BusinessId;
    }

    public String getValueIntent() {
        return ValueIntent;
    }

    public int getPackageId() {
        return PackageId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.PACKAGE_ACTIVITY);

        ValueIntent = getIntent().getExtras().getString("New");
        if (ValueIntent.equals("BuyPrize")) {
         PackageId =   getIntent().getExtras().getInt("PackageId");
        }

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.buy_package);

        //ایجاد طرح بندی صفحه
        CreateLayout();
    }

    /**
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    private void CreateLayout() {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction BusinessListTransaction = fragmentManager.beginTransaction();
            BusinessListTransaction.replace(R.id.PackageFrameLayoutPackageActivity, new BusinessListForPackageFragment());
            BusinessListTransaction.commit();

    }

    public void SetBusinessNameToButton(String businessName, int businessId) {
        BusinessId = businessId;
        BusinessName = businessName;
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
