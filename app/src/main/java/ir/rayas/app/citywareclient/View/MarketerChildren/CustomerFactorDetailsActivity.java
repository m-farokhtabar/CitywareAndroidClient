package ir.rayas.app.citywareclient.View.MarketerChildren;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.CustomerFactorDetailsRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.MarketerFactorDetailsRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Marketing.Marketing_CustomerFactorDetailsViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.Marketing_CustomerFactorViewModel;

public class CustomerFactorDetailsActivity extends BaseActivity {

    private String FactorDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_factore_details);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.CUSTOMER_FACTORE_DETAILS_ACTIVITY);

        FactorDetails = getIntent().getExtras().getString("FactureDetails");

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {

            }
        }, R.string.details_factor);

        //ایجاد طرح بندی صفحه
        CreateLayout();


    }

    /**
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    @SuppressLint("SetTextI18n")
    private void CreateLayout() {

        TextViewPersian DiscountTextViewCustomerFactorDetailsActivity = findViewById(R.id.DiscountTextViewCustomerFactorDetailsActivity);
        TextViewPersian TotalPriceFactorTextViewCustomerFactorDetailsActivity = findViewById(R.id.TotalPriceFactorTextViewCustomerFactorDetailsActivity);

        RecyclerView ProductListRecyclerViewCustomerFactorDetailsActivity = findViewById(R.id.ProductListRecyclerViewCustomerFactorDetailsActivity);
        ProductListRecyclerViewCustomerFactorDetailsActivity.setHasFixedSize(true);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(this);
        ProductListRecyclerViewCustomerFactorDetailsActivity.setLayoutManager(LinearLayoutManager);

        final Marketing_CustomerFactorViewModel marketing_customerFactorViewModel;
        Gson gson = new Gson();
        Type listType = new TypeToken<Marketing_CustomerFactorViewModel>() {
        }.getType();
        marketing_customerFactorViewModel = gson.fromJson(FactorDetails, listType);

        CustomerFactorDetailsRecyclerViewAdapter customerFactorDetailsRecyclerViewAdapter = new CustomerFactorDetailsRecyclerViewAdapter(marketing_customerFactorViewModel.getDetails());
        ProductListRecyclerViewCustomerFactorDetailsActivity.setAdapter(customerFactorDetailsRecyclerViewAdapter);

        double FactorDiscount = 0;
        double TotalPrice = 0;

        if (marketing_customerFactorViewModel.getDetails()!= null) {
            List<Marketing_CustomerFactorDetailsViewModel> ViewModelList = marketing_customerFactorViewModel.getDetails();

            for (int i = 0; i < ViewModelList.size(); i++) {
                FactorDiscount = FactorDiscount + ViewModelList.get(i).getDiscountPrice();
                TotalPrice = TotalPrice + ViewModelList.get(i).getPrice();
            }
        }

        DiscountTextViewCustomerFactorDetailsActivity.setText(Utility.GetIntegerNumberWithComma(FactorDiscount) + " " + getResources().getString(R.string.toman));
        TotalPriceFactorTextViewCustomerFactorDetailsActivity.setText(Utility.GetIntegerNumberWithComma(TotalPrice) + " " + getResources().getString(R.string.toman));


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
