package ir.rayas.app.citywareclient.View.Master;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.View.MasterChildren.DiscountActivity;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowMarketerCommissionDetailsActivity;

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
        },0);

        //ایجاد طرحبندی صفحه
        CreateLayout();
    }

    private void LoadData() {

    }

    private void CreateLayout() {

        ButtonPersianView GetDiscountsButtonCommerceActivity = findViewById(R.id.GetDiscountsButtonCommerceActivity);
        ButtonPersianView MarketerCommissionButtonCommerceActivity = findViewById(R.id.MarketerCommissionButtonCommerceActivity);
        ButtonPersianView IncomeFromBusinessButtonCommerceActivity = findViewById(R.id.IncomeFromBusinessButtonCommerceActivity);


        GetDiscountsButtonCommerceActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent DiscountsAndIncomeIntent = NewIntent(DiscountActivity.class);
                startActivity(DiscountsAndIncomeIntent);

            }
        });

        MarketerCommissionButtonCommerceActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ShowMarketerCommissionDetailsIntent = NewIntent(ShowMarketerCommissionDetailsActivity.class);
                startActivity(ShowMarketerCommissionDetailsIntent);
            }
        });

        IncomeFromBusinessButtonCommerceActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
