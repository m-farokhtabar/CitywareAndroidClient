package ir.rayas.app.citywareclient.View.MasterChildren;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.CommissionProductsRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Marketing.MarketingService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Marketing.BusinessCommissionAndDiscountViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.ProductCommissionAndDiscountViewModel;

public class ShowCommissionDetailsActivity extends BaseActivity implements IResponseService {


    private RecyclerView ShowProductListRecyclerViewShowCommissionDetailsActivity = null;
    private CardView CommissionProductCardViewShowCommissionDetailsActivity = null;
    private CardView TitleProductCardViewShowCommissionDetailsActivity = null;
    private TextViewPersian MarketerPercentTextViewShowCommissionDetailsActivity = null;
    private TextViewPersian AddressTextViewShowCommissionDetailsActivity = null;

    private int BusinessId = 0;
    private String BusinessName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_commission_details);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.SHOW_COMMISSION_DETAILS_ACTIVITY);

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                LoadData();
            }
        }, R.string.commission_details);

        BusinessId = getIntent().getExtras().getInt("BusinessId");
        BusinessName = getIntent().getExtras().getString("BusinessName");

        CreateLayout();
        //دریافت اطلاعات از سرور
        LoadData();
    }

    private void CreateLayout() {

        TextViewPersian TitleBusinessTextViewShowCommissionDetailsActivity = findViewById(R.id.TitleBusinessTextViewShowCommissionDetailsActivity);
        CommissionProductCardViewShowCommissionDetailsActivity = findViewById(R.id.CommissionProductCardViewShowCommissionDetailsActivity);
        MarketerPercentTextViewShowCommissionDetailsActivity = findViewById(R.id.MarketerPercentTextViewShowCommissionDetailsActivity);
        AddressTextViewShowCommissionDetailsActivity = findViewById(R.id.AddressTextViewShowCommissionDetailsActivity);
        TitleProductCardViewShowCommissionDetailsActivity = findViewById(R.id.TitleProductCardViewShowCommissionDetailsActivity);


        ShowProductListRecyclerViewShowCommissionDetailsActivity = findViewById(R.id.ShowProductListRecyclerViewShowCommissionDetailsActivity);
        ShowProductListRecyclerViewShowCommissionDetailsActivity.setHasFixedSize(true);
        //به دلیل اینکه من در هر سطر یک گزینه نیاز دارم
        LinearLayoutManager RegionLinearLayoutManager = new LinearLayoutManager(ShowCommissionDetailsActivity.this);
        ShowProductListRecyclerViewShowCommissionDetailsActivity.setLayoutManager(RegionLinearLayoutManager);

        TitleBusinessTextViewShowCommissionDetailsActivity.setText(BusinessName);

    }

    public void LoadData() {
        ShowLoadingProgressBar();

        MarketingService marketingService = new MarketingService(this);
        marketingService.Get(BusinessId);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.BusinessCommissionAndDiscountGet) {

                Feedback<BusinessCommissionAndDiscountViewModel> FeedBack = (Feedback<BusinessCommissionAndDiscountViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    final BusinessCommissionAndDiscountViewModel ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        if (ViewModel.getProductList() != null) {
                            List<ProductCommissionAndDiscountViewModel> ViewModelList = ViewModel.getProductList();

                            if (ViewModelList.size() > 0) {
                                TitleProductCardViewShowCommissionDetailsActivity.setVisibility(View.VISIBLE);
                            } else {
                                TitleProductCardViewShowCommissionDetailsActivity.setVisibility(View.GONE);
                            }

                            CommissionProductsRecyclerViewAdapter commissionProductsRecyclerViewAdapter = new CommissionProductsRecyclerViewAdapter(ShowCommissionDetailsActivity.this, ViewModelList);
                            ShowProductListRecyclerViewShowCommissionDetailsActivity.setAdapter(commissionProductsRecyclerViewAdapter);
                        }

                        if (ViewModel.getCustomerPercent() == 0 && ViewModel.getMarketerPercent() == 0) {
                            CommissionProductCardViewShowCommissionDetailsActivity.setVisibility(View.GONE);
                        } else {
                            CommissionProductCardViewShowCommissionDetailsActivity.setVisibility(View.VISIBLE);

                            MarketerPercentTextViewShowCommissionDetailsActivity.setText(String.valueOf(ViewModel.getCustomerPercent()) + " " + getResources().getString(R.string.percent));
                            AddressTextViewShowCommissionDetailsActivity.setText(ViewModel.getBusinessAddress());
                        }
                    } else {
                        TitleProductCardViewShowCommissionDetailsActivity.setVisibility(View.GONE);
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
            HideLoading();
            ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
