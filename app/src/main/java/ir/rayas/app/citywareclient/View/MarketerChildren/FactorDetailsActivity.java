package ir.rayas.app.citywareclient.View.MarketerChildren;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.FactorDetailsRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Marketing.Marketing_CustomerFactorDetailsViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.Marketing_CustomerFactorViewModel;

public class FactorDetailsActivity extends BaseActivity {


    private String FactorDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facture_details);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.FACTORE_DETAILS_ACTIVITY);

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

        TextViewPersian FactorOfIncomeTextViewFactorDetailsActivity = findViewById(R.id.FactorOfIncomeTextViewFactorDetailsActivity);
        TextViewPersian DiscountCustomerTextViewFactorDetailsActivity = findViewById(R.id.DiscountCustomerTextViewFactorDetailsActivity);
        TextViewPersian TotalPriceFactorTextViewFactorDetailsActivity = findViewById(R.id.TotalPriceFactorTextViewFactorDetailsActivity);
        TextViewPersian PayDiscountCustomerTextViewFactorDetailsActivity = findViewById(R.id.PayDiscountCustomerTextViewFactorDetailsActivity);

        RecyclerView ProductListRecyclerViewFactorDetailsActivity = findViewById(R.id.ProductListRecyclerViewFactorDetailsActivity);
        ProductListRecyclerViewFactorDetailsActivity.setHasFixedSize(true);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(this);
        ProductListRecyclerViewFactorDetailsActivity.setLayoutManager(LinearLayoutManager);

        final Marketing_CustomerFactorViewModel marketing_customerFactorViewModel;
        Gson gson = new Gson();
        Type listType = new TypeToken<Marketing_CustomerFactorViewModel>() {
        }.getType();
        marketing_customerFactorViewModel = gson.fromJson(FactorDetails, listType);

        double FactorOfIncome = 0;
        double DiscountCustomer = 0;
        double TotalPrice = 0;

        if (marketing_customerFactorViewModel != null) {
            if (marketing_customerFactorViewModel.getDetails() != null) {
                FactorDetailsRecyclerViewAdapter factorDetailsRecyclerViewAdapter = new FactorDetailsRecyclerViewAdapter(marketing_customerFactorViewModel.getDetails());
                ProductListRecyclerViewFactorDetailsActivity.setAdapter(factorDetailsRecyclerViewAdapter);


                List<Marketing_CustomerFactorDetailsViewModel> ViewModelList = marketing_customerFactorViewModel.getDetails();

                for (int i = 0; i < ViewModelList.size(); i++) {
                    FactorOfIncome = FactorOfIncome + ViewModelList.get(i).getCommissionPrice();
                    DiscountCustomer = DiscountCustomer + ViewModelList.get(i).getDiscountPrice();
                    TotalPrice = TotalPrice + ViewModelList.get(i).getPrice();
                }


            } else {

            }
        } else {

        }

        FactorOfIncomeTextViewFactorDetailsActivity.setText(Utility.GetIntegerNumberWithComma(FactorOfIncome) + " " + getResources().getString(R.string.toman));
        DiscountCustomerTextViewFactorDetailsActivity.setText(Utility.GetIntegerNumberWithComma(DiscountCustomer) + " " + getResources().getString(R.string.toman));
        TotalPriceFactorTextViewFactorDetailsActivity.setText(Utility.GetIntegerNumberWithComma(TotalPrice) + " " + getResources().getString(R.string.toman));
        PayDiscountCustomerTextViewFactorDetailsActivity.setText(Utility.GetIntegerNumberWithComma(TotalPrice - DiscountCustomer) + " " + getResources().getString(R.string.toman));


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
