package ir.rayas.app.citywareclient.View.Master;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.View.MasterChildren.DiscountActivity;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowBusinessCommissionActivity;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowMarketerCommissionDetailsActivity;
import ir.rayas.app.citywareclient.View.Share.UserBusinessListActivity;

public class CommerceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commerce);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.COMMERCE_ACTIVITY);

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                LoadData();
            }
        }, 0);

        //ایجاد طرحبندی صفحه
        CreateLayout();
    }

    private void LoadData() {

    }

    private void CreateLayout() {

        RelativeLayout GetDiscountsRelativeLayoutCommerceActivity = findViewById(R.id.GetDiscountsRelativeLayoutCommerceActivity);
        RelativeLayout MarketerCommissionRelativeLayoutCommerceActivity = findViewById(R.id.MarketerCommissionRelativeLayoutCommerceActivity);
        RelativeLayout IncomeFromBusinessRelativeLayoutCommerceActivity = findViewById(R.id.IncomeFromBusinessRelativeLayoutCommerceActivity);


        GetDiscountsRelativeLayoutCommerceActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent DiscountsAndIncomeIntent = NewIntent(DiscountActivity.class);
                startActivity(DiscountsAndIncomeIntent);

            }
        });

        MarketerCommissionRelativeLayoutCommerceActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ShowMarketerCommissionDetailsIntent = NewIntent(ShowMarketerCommissionDetailsActivity.class);
                startActivity(ShowMarketerCommissionDetailsIntent);
            }
        });

        IncomeFromBusinessRelativeLayoutCommerceActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent UserBusinessListIntent = NewIntent(UserBusinessListActivity.class);
                UserBusinessListIntent.putExtra("Title", getResources().getString(R.string.selector_business_for_income));
                UserBusinessListIntent.putExtra("Description", getResources().getString(R.string.selector_business_for_visit_income));
                UserBusinessListIntent.putExtra("IsParent", false);
                UserBusinessListIntent.putExtra("activityIdList", ActivityIdList.SHOW_BUSINESS_COMMISSION_ACTIVITY);
                startActivity(UserBusinessListIntent);
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
