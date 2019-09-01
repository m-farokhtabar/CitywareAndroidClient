package ir.rayas.app.citywareclient.View.UserProfileChildren;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import java.util.HashMap;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResultPassing;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Base.IButtonBackToolbarListener;
import ir.rayas.app.citywareclient.View.Fragment.Package.BusinessListForPackageFragment;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Package.OutputPackageTransactionViewModel;

public class PackageActivity extends BaseActivity implements IButtonBackToolbarListener {

    private int RetryType = 0;

    public void setRetryType(int retryType) {
        RetryType = retryType;
    }

    public String ValueIntent = "";
    public String BusinessName = "";
    public int PackageId = 0;
    public int BusinessId = 0;

    private boolean IsAdd = false;
    private OutputPackageTransactionViewModel outputPackageTransactionViewModel;


    public void setAdd(boolean add) {
        IsAdd = add;
    }

    public void setOutputPackageTransactionViewModel(OutputPackageTransactionViewModel outputPackageTransactionViewModel) {
        this.outputPackageTransactionViewModel = outputPackageTransactionViewModel;
    }

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
            PackageId = getIntent().getExtras().getInt("PackageId");
        }

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.buy_package);

        this.setButtonBackToolbarListener(this);

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


    @Override
    protected void onGetResult(ActivityResult Result) {
        if (Result.getFromActivityId() == getCurrentActivityId()) {
            switch (Result.getToActivityId()) {

                case ActivityIdList.PAYMENT_PACKAGE_ACTIVITY:
                    outputPackageTransactionViewModel = (OutputPackageTransactionViewModel) Result.getData().get("OutputPackageTransactionViewModel");
                    IsAdd = (Boolean) Result.getData().get("IsAdd");

                    break;
            }
        }
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

    private void SendToParentActivity(){
        if (IsAdd) {
            HashMap<String, Object> Output = new HashMap<>();
            Output.put("IsAdd", IsAdd);
            Output.put("TotalPrice", outputPackageTransactionViewModel.getPackageCredit());
            Output.put("OutputPackageTransactionViewModel", outputPackageTransactionViewModel);
            ActivityResultPassing.Push(new ActivityResult(getParentActivity(), getCurrentActivityId(), Output));
            super.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        SendToParentActivity();
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
    public void ClickOnButtonBackToolbar() {
        SendToParentActivity();
    }
}
