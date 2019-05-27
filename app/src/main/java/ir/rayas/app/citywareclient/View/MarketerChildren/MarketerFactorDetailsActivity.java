package ir.rayas.app.citywareclient.View.MarketerChildren;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.MarketerFactorDetailsRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.ViewModel.Marketing.Marketing_CustomerFactorDetailsViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.Marketing_CustomerFactorViewModel;

public class MarketerFactorDetailsActivity extends BaseActivity {


    private String FactorDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketer_factor_details);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.MARKETER_FACTORE_DETAILS_ACTIVITY);

        FactorDetails = getIntent().getExtras().getString("FactureDetails");

        //ایجاد طرح بندی صفحه
        CreateLayout();


    }

    /**
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    @SuppressLint("SetTextI18n")
    private void CreateLayout() {

        TextViewPersian FactorOfIncomeTextViewFactorDetailsActivity = findViewById(R.id.FactorOfIncomeTextViewMarketerFactorDetailsActivity);
        TextViewPersian TotalPriceFactorTextViewFactorDetailsActivity = findViewById(R.id.TotalPriceFactorTextViewMarketerFactorDetailsActivity);

        RecyclerView ProductListRecyclerViewFactorDetailsActivity = findViewById(R.id.ProductListRecyclerViewMarketerFactorDetailsActivity);
        ProductListRecyclerViewFactorDetailsActivity.setHasFixedSize(true);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(this);
        ProductListRecyclerViewFactorDetailsActivity.setLayoutManager(LinearLayoutManager);

        final Marketing_CustomerFactorViewModel marketing_customerFactorViewModel;
        Gson gson = new Gson();
        Type listType = new TypeToken<Marketing_CustomerFactorViewModel>() {
        }.getType();
        marketing_customerFactorViewModel = gson.fromJson(FactorDetails, listType);

        MarketerFactorDetailsRecyclerViewAdapter factorDetailsRecyclerViewAdapter = new MarketerFactorDetailsRecyclerViewAdapter(marketing_customerFactorViewModel.getDetails());
        ProductListRecyclerViewFactorDetailsActivity.setAdapter(factorDetailsRecyclerViewAdapter);

        double FactorOfIncome = 0;
        double TotalPrice = 0;

        if (marketing_customerFactorViewModel.getDetails()!= null) {
            List<Marketing_CustomerFactorDetailsViewModel> ViewModelList = marketing_customerFactorViewModel.getDetails();

            for (int i = 0; i < ViewModelList.size(); i++) {
                FactorOfIncome = FactorOfIncome + ViewModelList.get(i).getCommissionPrice();
                TotalPrice = TotalPrice + ViewModelList.get(i).getPrice();
            }
        }

        FactorOfIncomeTextViewFactorDetailsActivity.setText(Utility.GetIntegerNumberWithComma(FactorOfIncome) + " " + getResources().getString(R.string.toman));
        TotalPriceFactorTextViewFactorDetailsActivity.setText(Utility.GetIntegerNumberWithComma(TotalPrice) + " " + getResources().getString(R.string.toman));


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
