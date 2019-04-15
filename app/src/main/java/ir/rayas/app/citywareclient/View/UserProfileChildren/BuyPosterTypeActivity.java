package ir.rayas.app.citywareclient.View.UserProfileChildren;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;

import ir.rayas.app.citywareclient.View.Fragment.Poster.BuyPosterTypeFragment;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;

public class BuyPosterTypeActivity extends BaseActivity {


    private int RetryType = 0;

    public void setRetryType(int retryType) {
        RetryType = retryType;
    }

    private int PosterTypeId = 0;
    private String PosterTypeName = "";
    private double PosterTypePrice = 0;
    private int Hours = 0;

    public int getPosterTypeId() {
        return PosterTypeId;
    }

    public String getPosterTypeName() {
        return PosterTypeName;
    }

    public double getPosterTypePrice() {
        return PosterTypePrice;
    }

    public int getHours() {
        return Hours;
    }

    public void setHours(int hours) {
        Hours = hours;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_poster_type);
        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.BUY_POSTER_TYPE_ACTIVITY);

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.buy_poster);

        PosterTypeId = getIntent().getExtras().getInt("PosterTypeId");
        PosterTypePrice = getIntent().getExtras().getDouble("PosterTypePrice");
        PosterTypeName = getIntent().getExtras().getString("PosterTypeName");

        //ایجاد طرح بندی صفحه
        CreateLayout();
    }

    /**
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    private void CreateLayout() {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction BusinessListTransaction = fragmentManager.beginTransaction();
            BusinessListTransaction.replace(R.id.PosterFrameLayoutBuyPosterTypeActivity, new BuyPosterTypeFragment());
            BusinessListTransaction.commit();

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
