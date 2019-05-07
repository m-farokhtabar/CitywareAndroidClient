package ir.rayas.app.citywareclient.View.MasterChildren;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.OrderProductsRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.ProductListOrderRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;

import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;

import ir.rayas.app.citywareclient.ViewModel.Marketing.ProductCommissionAndDiscountModel;
import ir.rayas.app.citywareclient.ViewModel.Order.ProductViewModel;

public class OrderActivity extends BaseActivity implements IResponseService, ILoadData {

    private SwipeRefreshLayout RefreshOrderSwipeRefreshLayoutOrderActivity = null;
    private RecyclerView ProductListOrderRecyclerViewOrderActivity = null;
    private ProductListOrderRecyclerViewAdapter productListOrderRecyclerViewAdapter = null;
    private TextViewPersian MarketingCommissionTextViewOrderActivity = null;
    private TextViewPersian CustomerDiscountTextViewOrderActivity = null;
    private TextViewPersian TotalPriceTextViewOrderActivity = null;
    private LinearLayout TotalLinearLayoutOrderActivity = null;
    private FrameLayout Line2 = null;

    List<ProductCommissionAndDiscountModel> productCommissionAndDiscountModels = new ArrayList<>();

    private boolean IsSwipe = false;
    private int BusinessId = 0;
    private String Percents = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.ORDER_ACTIVITY);

        BusinessId = getIntent().getExtras().getInt("BusinessId");
        Percents = getIntent().getExtras().getString("Percents");

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.basket_submit);


        //ایجاد طرح بندی صفحه
        CreateLayout();

    }

    /**
     * در صورتی که در ارتباط با اینترنت مشکلی بوجود آمده و کاربر دکمه تلاش مجدد را فشار داده است
     */
    private void RetryButtonOnClick() {
        //دریافت اطلاعات
        LoadData();
    }

    /**
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    private void CreateLayout() {
        RefreshOrderSwipeRefreshLayoutOrderActivity = findViewById(R.id.RefreshOrderSwipeRefreshLayoutOrderActivity);
        MarketingCommissionTextViewOrderActivity = findViewById(R.id.MarketingCommissionTextViewOrderActivity);
        CustomerDiscountTextViewOrderActivity = findViewById(R.id.CustomerDiscountTextViewOrderActivity);
        TotalPriceTextViewOrderActivity = findViewById(R.id.TotalPriceTextViewOrderActivity);
        TotalLinearLayoutOrderActivity = findViewById(R.id.TotalLinearLayoutOrderActivity);
        Line2 = findViewById(R.id.Line2);

        ProductListOrderRecyclerViewOrderActivity = findViewById(R.id.ProductListOrderRecyclerViewOrderActivity);
        ProductListOrderRecyclerViewOrderActivity.setHasFixedSize(true);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(OrderActivity.this);
        ProductListOrderRecyclerViewOrderActivity.setLayoutManager(LinearLayoutManager);

        productListOrderRecyclerViewAdapter = new ProductListOrderRecyclerViewAdapter(OrderActivity.this, null, ProductListOrderRecyclerViewOrderActivity);
        ProductListOrderRecyclerViewOrderActivity.setAdapter(productListOrderRecyclerViewAdapter);

        TotalLinearLayoutOrderActivity.setVisibility(View.GONE);
        Line2.setVisibility(View.GONE);


        FloatingActionButton AddProductFloatingActionButtonOrderActivity = findViewById(R.id.AddProductFloatingActionButtonOrderActivity);
        AddProductFloatingActionButtonOrderActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent OrderIntent = NewIntent(ProductListOrderActivity.class);
                OrderIntent.putExtra("BusinessId", BusinessId);
                OrderIntent.putExtra("Percents", Percents);
                startActivity(OrderIntent);
            }
        });


        RefreshOrderSwipeRefreshLayoutOrderActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                IsSwipe = true;
                LoadData();
            }
        });
    }

    /**
     * دریافت اطلاعات نحوای جهت پر کردن Recycle
     */
    public void LoadData() {
        if (!IsSwipe)
            ShowLoadingProgressBar();


    }

    /**
     * @param Data
     * @param ServiceMethod
     * @param <T>
     */
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        HideLoading();
        RefreshOrderSwipeRefreshLayoutOrderActivity.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.ProductGetAll) {
                Feedback<List<ProductViewModel>> FeedBack = (Feedback<List<ProductViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                }
            }
        } catch (Exception e) {
            ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
    }

    @Override
    protected void onGetResult(ActivityResult Result) {
        if (Result.getFromActivityId() == getCurrentActivityId()) {
            switch (Result.getToActivityId()) {

                case ActivityIdList.PRODUCT_LIST_ORDER_ACTIVITY:
                    ProductCommissionAndDiscountModel ViewModel = (ProductCommissionAndDiscountModel) Result.getData().get("ProductCommissionAndDiscountModel");
                    productCommissionAndDiscountModels.add(ViewModel);

                    TotalLinearLayoutOrderActivity.setVisibility(View.VISIBLE);
                    Line2.setVisibility(View.VISIBLE);



                    if (productCommissionAndDiscountModels.size() == 0)
                        productListOrderRecyclerViewAdapter.SetViewModel(ViewModel);
                    else
                        productListOrderRecyclerViewAdapter.AddViewModel(ViewModel);


                    double MarketingCommission = 0;
                    double CustomerDiscount = 0;
                    double TotalPrice = 0;
                    for (int i = 0; i < productCommissionAndDiscountModels.size(); i++) {

                        CustomerDiscount = CustomerDiscount + (productCommissionAndDiscountModels.get(i).getPrice() * productCommissionAndDiscountModels.get(i).getCustomerPercent()) / 100;
                        MarketingCommission = MarketingCommission + (productCommissionAndDiscountModels.get(i).getPrice() * productCommissionAndDiscountModels.get(i).getMarketerPercent()) / 100;
                        TotalPrice = TotalPrice + productCommissionAndDiscountModels.get(i).getPrice() * productCommissionAndDiscountModels.get(i).getNumberOfOrder();

                    }

                    MarketingCommissionTextViewOrderActivity.setText(Utility.GetIntegerNumberWithComma(MarketingCommission));
                    CustomerDiscountTextViewOrderActivity.setText(Utility.GetIntegerNumberWithComma(CustomerDiscount));
                    TotalPriceTextViewOrderActivity.setText(Utility.GetIntegerNumberWithComma(TotalPrice));

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


