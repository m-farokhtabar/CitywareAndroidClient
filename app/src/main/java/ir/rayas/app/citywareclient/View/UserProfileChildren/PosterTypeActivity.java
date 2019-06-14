package ir.rayas.app.citywareclient.View.UserProfileChildren;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Fragment.Poster.BuyPosterTypeFragment;
import ir.rayas.app.citywareclient.View.Fragment.Poster.PosterTypeFragment;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;

public class PosterTypeActivity extends BaseActivity {

    private int RetryType = 0;
    private int BusinessId = 0;

    private int PosterTypeId;
    private String PosterTypeName;
    private String BusinessName;
    private double PosterTypePrice;

    private   int NewDay;
    private  int NewHours;

    public String getBusinessName() {
        return BusinessName;
    }

    public int getBusinessId() {
        return BusinessId;
    }

    public void setBusinessId(int businessId) {
        BusinessId = businessId;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }

    public int getPosterTypeId() {
        return PosterTypeId;
    }

    public void setPosterTypeId(int posterTypeId) {
        PosterTypeId = posterTypeId;
    }

    public String getPosterTypeName() {
        return PosterTypeName;
    }

    public void setPosterTypeName(String posterTypeName) {
        PosterTypeName = posterTypeName;
    }

    public double getPosterTypePrice() {
        return PosterTypePrice;
    }

    public void setPosterTypePrice(double posterTypePrice) {
        PosterTypePrice = posterTypePrice;
    }

    public void setNewDay(int newDay) {
        NewDay = newDay;
    }

    public void setNewHours(int newHours) {
        NewHours = newHours;
    }

    public int getNewDay() {
        return NewDay;
    }

    public int getNewHours() {
        return NewHours;
    }

    /**
     * ثبت اطلعات یک
     * دریافت اطلاعات دو
     *
     * @param retryType
     */
    public void setRetryType(int retryType) {
        RetryType = retryType;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_type);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.POSTER_TYPE_ACTIVITY);
        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.buy_poster);

        //ایجاد طرحبندی صفحه
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
                //getLoadDataByIndex(FragmentIndex).LoadData();
                break;
        }
    }


    /**
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    private void CreateLayout() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction PosterTypeTransaction = fragmentManager.beginTransaction();
        PosterTypeTransaction.replace(R.id.PosterFrameLayoutPosterTypeActivity, new PosterTypeFragment());
        PosterTypeTransaction.commit();

    }



    @Override
    protected void onGetResult(ActivityResult Result) {
        if (Result.getFromActivityId() == getCurrentActivityId()) {
            switch (Result.getToActivityId()) {
                case ActivityIdList.USER_BUSINESS_LIST_ACTIVITY:
                    BusinessId = (int)Result.getData().get("BusinessId");
                    BusinessName = (String)Result.getData().get("BusinessName");

                    FragmentTransaction BasketListTransaction = getSupportFragmentManager().beginTransaction();
                    BasketListTransaction.replace(R.id.PosterFrameLayoutPosterTypeActivity, new BuyPosterTypeFragment());
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
        super.onBackPressed();
    }
}

