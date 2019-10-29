package ir.rayas.app.citywareclient.View.MasterChildren;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.ProductListOrderRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Marketing.MarketingService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResultPassing;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;

import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;

import ir.rayas.app.citywareclient.ViewModel.Marketing.Marketing_CustomerFactorDetailsViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.Marketing_CustomerFactorViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.ProductCommissionAndDiscountModel;

public class OrderActivity extends BaseActivity implements IResponseService, ILoadData {

    private ProductListOrderRecyclerViewAdapter productListOrderRecyclerViewAdapter = null;
    private TextViewPersian TotalPriceFactoreTextViewOrderActivity = null;
    private TextViewPersian FactureOfIncomeTextViewOrderActivity = null;
    private TextViewPersian TotalPriceTextViewOrderActivity = null;
    private TextViewPersian ShowEmptyProductTextViewOrderActivity = null;
    private LinearLayout TotalLinearLayoutOrderActivity = null;
    private FrameLayout Line2 = null;

    List<ProductCommissionAndDiscountModel> productCommissionAndDiscountModels = new ArrayList<>();
    List<Marketing_CustomerFactorDetailsViewModel> marketing_customerFactorDetailsViewModels = new ArrayList<>();

    private int BusinessId = 0;
    private int Position = 0;
    private int MarketerId = 0;
    private String Percents = "";
    private String Ticket = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.ORDER_ACTIVITY);

        BusinessId = getIntent().getExtras().getInt("BusinessId");
        MarketerId = getIntent().getExtras().getInt("MarketerId");
        Percents = getIntent().getExtras().getString("Percents");
        Ticket = getIntent().getExtras().getString("Ticket");
        Position = getIntent().getExtras().getInt("position");

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
        TotalPriceFactoreTextViewOrderActivity = findViewById(R.id.TotalPriceFactoreTextViewOrderActivity);
        FactureOfIncomeTextViewOrderActivity = findViewById(R.id.FactureOfIncomeTextViewOrderActivity);
        TotalPriceTextViewOrderActivity = findViewById(R.id.TotalPriceTextViewOrderActivity);
        TotalLinearLayoutOrderActivity = findViewById(R.id.TotalLinearLayoutOrderActivity);
        ShowEmptyProductTextViewOrderActivity = findViewById(R.id.ShowEmptyProductTextViewOrderActivity);
        Line2 = findViewById(R.id.Line2);
        ButtonPersianView SubmitOrderButtonOrderActivity = findViewById(R.id.SubmitOrderButtonOrderActivity);

        RecyclerView productListOrderRecyclerViewOrderActivity = findViewById(R.id.ProductListOrderRecyclerViewOrderActivity);
        productListOrderRecyclerViewOrderActivity.setHasFixedSize(true);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(OrderActivity.this);
        productListOrderRecyclerViewOrderActivity.setLayoutManager(LinearLayoutManager);

        productListOrderRecyclerViewAdapter = new ProductListOrderRecyclerViewAdapter(OrderActivity.this, null, productListOrderRecyclerViewOrderActivity);
        productListOrderRecyclerViewOrderActivity.setAdapter(productListOrderRecyclerViewAdapter);

        TotalLinearLayoutOrderActivity.setVisibility(View.GONE);
        ShowEmptyProductTextViewOrderActivity.setVisibility(View.VISIBLE);
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

        SubmitOrderButtonOrderActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadData();
            }
        });

    }

    /**
     * دریافت اطلاعات نحوای جهت پر کردن Recycle
     */
    public void LoadData() {

        ShowLoadingProgressBar();

        MarketingService service = new MarketingService(this);
        service.AddCustomerFactor(MadeViewModel());

    }

    private Marketing_CustomerFactorViewModel MadeViewModel() {

        Marketing_CustomerFactorViewModel ViewModel = new Marketing_CustomerFactorViewModel();
        try {
            ViewModel.setBusinessId(BusinessId);
            ViewModel.setDetails(marketing_customerFactorDetailsViewModels);
            ViewModel.setMarketingId(MarketerId);
            ViewModel.setTicket(Ticket);

        } catch (Exception Ex) {
            ShowToast(FeedbackType.InvalidDataFormat.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
        return ViewModel;
    }

    /**
     * @param Data
     * @param ServiceMethod
     * @param <T>
     */
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.AddCustomerFactorAdd) {
                Feedback<Boolean> FeedBack = (Feedback<Boolean>) Data;

                if (FeedBack.getStatus() == FeedbackType.RegisteredSuccessful.getId()) {

                    if (FeedBack.getValue() != null) {
                        if (FeedBack.getValue()) {
                            ShowToast(getResources().getString(R.string.you_order_submit_successful), Toast.LENGTH_LONG, MessageType.Info);
                            SendDataToParentActivity();
                            FinishCurrentActivity();
                            // onBackPressed();
                        } else {
                            ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                        }
                    } else {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    }

                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
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

                    if (productCommissionAndDiscountModels.size() == 0) {
                        productListOrderRecyclerViewAdapter.SetViewModel(ViewModel);
                    } else {
                        productListOrderRecyclerViewAdapter.AddViewModel(ViewModel);
                    }


                    SetViewFooter(productCommissionAndDiscountModels);
                    break;
            }
        }
        super.onGetResult(Result);
    }

    public void SetproductCommissionAndDiscountModels(int Position) {

        productCommissionAndDiscountModels.remove(Position);

    }

    public void SetViewFooter(List<ProductCommissionAndDiscountModel> productCommissionAndDiscountModels) {

        if (productCommissionAndDiscountModels.size() > 0) {

            ShowEmptyProductTextViewOrderActivity.setVisibility(View.GONE);
            TotalLinearLayoutOrderActivity.setVisibility(View.VISIBLE);
            Line2.setVisibility(View.VISIBLE);

            int MarketingCommission = 0;
            int CustomerDiscount = 0;
            int TotalPriceFacture = 0;
            int PayablePrice = 0;

            if (marketing_customerFactorDetailsViewModels != null) {
                if (marketing_customerFactorDetailsViewModels.size() > 0) {
                    marketing_customerFactorDetailsViewModels.clear();
                }
            }

            for (int i = 0; i < productCommissionAndDiscountModels.size(); i++) {

                int TotalPrice = (int) (productCommissionAndDiscountModels.get(i).getPrice() * productCommissionAndDiscountModels.get(i).getNumberOfOrder());
                double discount = ((float)TotalPrice * (float)productCommissionAndDiscountModels.get(i).getCustomerPercent()) / 100;
                CustomerDiscount = CustomerDiscount + (int) (discount);


                int MarketerPercent = (int) (((float)TotalPrice * (float)productCommissionAndDiscountModels.get(i).getMarketerPercent()) / 100);
                int ApplicationPercent = (int) (((float)TotalPrice * (float)productCommissionAndDiscountModels.get(i).getApplicationPercent()) / 100);

                int Commission = MarketerPercent + ApplicationPercent;
                MarketingCommission = MarketingCommission + Commission;
                PayablePrice = (PayablePrice + TotalPrice) - (int) discount;
                TotalPriceFacture = TotalPriceFacture + TotalPrice;

                Marketing_CustomerFactorDetailsViewModel marketing_customerFactorDetailsViewModel = new Marketing_CustomerFactorDetailsViewModel();
                marketing_customerFactorDetailsViewModel.setCommissionPrice(Commission);
                marketing_customerFactorDetailsViewModel.setDiscountPrice(discount);
                marketing_customerFactorDetailsViewModel.setPrice(TotalPrice);
                marketing_customerFactorDetailsViewModel.setProductId(productCommissionAndDiscountModels.get(i).getProductId());
                marketing_customerFactorDetailsViewModel.setProductName(productCommissionAndDiscountModels.get(i).getProductName());

                marketing_customerFactorDetailsViewModels.add(marketing_customerFactorDetailsViewModel);
            }

            TotalPriceFactoreTextViewOrderActivity.setText(Utility.GetIntegerNumberWithComma(TotalPriceFacture));
            FactureOfIncomeTextViewOrderActivity.setText(Utility.GetIntegerNumberWithComma(TotalPriceFacture - CustomerDiscount - MarketingCommission));
            TotalPriceTextViewOrderActivity.setText(Utility.GetIntegerNumberWithComma(PayablePrice));
        } else {
            TotalLinearLayoutOrderActivity.setVisibility(View.GONE);
            Line2.setVisibility(View.GONE);
            ShowEmptyProductTextViewOrderActivity.setVisibility(View.VISIBLE);
        }
    }

    private void SendDataToParentActivity() {

        HashMap<String, Object> Output = new HashMap<>();
        Output.put("Position", Position);
        ActivityResultPassing.Push(new ActivityResult(getParentActivity(), getCurrentActivityId(), Output));
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


