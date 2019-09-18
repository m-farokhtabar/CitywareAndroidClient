package ir.rayas.app.citywareclient.View.MasterChildren;

import android.annotation.SuppressLint;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.DiscountProductRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.RegionRepository;
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

public class ShowDiscountDetailsActivity extends BaseActivity {


    private RecyclerView ShowProductListRecyclerViewShowDiscountDetailsActivity = null;
    private TextViewPersian CustomerPercentTextViewShowDiscountDetailsActivity = null;
    private TextViewPersian AddressTextViewShowDiscountDetailsActivity = null;

    private int BusinessId = 0;
    private String BusinessName = "";
    private String Percents = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_discount_details);
        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.DISCOUNT_DETAILS_ACTIVITY);

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {

            }
        }, R.string.discount_details);

        BusinessId = getIntent().getExtras().getInt("BusinessId");
        BusinessName = getIntent().getExtras().getString("BusinessName");
        Percents = getIntent().getExtras().getString("Percents");

        CreateLayout();
        //دریافت اطلاعات از سرور
        //  LoadData();
    }

    private void CreateLayout() {

        TextViewPersian ShowEmptyDiscountTextViewShowDiscountDetailsActivity = findViewById(R.id.ShowEmptyDiscountTextViewShowDiscountDetailsActivity);
        TextViewPersian TitleBusinessTextViewShowDiscountDetailsActivity = findViewById(R.id.TitleBusinessTextViewShowDiscountDetailsActivity);
        CustomerPercentTextViewShowDiscountDetailsActivity = findViewById(R.id.CustomerPercentTextViewShowDiscountDetailsActivity);
        AddressTextViewShowDiscountDetailsActivity = findViewById(R.id.AddressTextViewShowDiscountDetailsActivity);

        ShowEmptyDiscountTextViewShowDiscountDetailsActivity.setVisibility(View.GONE);


        ShowProductListRecyclerViewShowDiscountDetailsActivity = findViewById(R.id.ShowProductListRecyclerViewShowDiscountDetailsActivity);
        ShowProductListRecyclerViewShowDiscountDetailsActivity.setHasFixedSize(true);
        //به دلیل اینکه من در هر سطر یک گزینه نیاز دارم
        LinearLayoutManager RegionLinearLayoutManager = new LinearLayoutManager(ShowDiscountDetailsActivity.this);
        ShowProductListRecyclerViewShowDiscountDetailsActivity.setLayoutManager(RegionLinearLayoutManager);

        TitleBusinessTextViewShowDiscountDetailsActivity.setText(BusinessName);


        Gson gson = new Gson();
        Type listType = new TypeToken<BusinessCommissionAndDiscountViewModel>() {
        }.getType();
        BusinessCommissionAndDiscountViewModel ViewModel = gson.fromJson(Percents, listType);

        if (ViewModel != null) {
            if (ViewModel.getProductList() != null && ViewModel.getProductList().size() > 0) {
                List<ProductCommissionAndDiscountViewModel> ViewModelList = ViewModel.getProductList();


                DiscountProductRecyclerViewAdapter discountProductRecyclerViewAdapter = new DiscountProductRecyclerViewAdapter(ShowDiscountDetailsActivity.this, ViewModelList);
                ShowProductListRecyclerViewShowDiscountDetailsActivity.setAdapter(discountProductRecyclerViewAdapter);
            } else {

            }
            CustomerPercentTextViewShowDiscountDetailsActivity.setText(String.valueOf(ViewModel.getCustomerPercent()) + " " + getResources().getString(R.string.percent));
        }

    }

//    public void LoadData() {
//        ShowLoadingProgressBar();
//
//        MarketingService marketingService = new MarketingService(this);
//        marketingService.Get(BusinessId);
//    }


//    @SuppressLint("SetTextI18n")
//    @Override
//    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
//        HideLoading();
//        try {
//            if (ServiceMethod == ServiceMethodType.BusinessCommissionAndDiscountGet) {
//
//                Feedback<BusinessCommissionAndDiscountViewModel> FeedBack = (Feedback<BusinessCommissionAndDiscountViewModel>) Data;
//
//                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
//
//                    final BusinessCommissionAndDiscountViewModel ViewModel = FeedBack.getValue();
//                    if (ViewModel != null) {
//                        if (ViewModel.getProductList() != null) {
//                            List<ProductCommissionAndDiscountViewModel> ViewModelList = ViewModel.getProductList();
//
//
//                            DiscountProductRecyclerViewAdapter discountProductRecyclerViewAdapter = new DiscountProductRecyclerViewAdapter(ShowDiscountDetailsActivity.this, ViewModelList);
//                            ShowProductListRecyclerViewShowDiscountDetailsActivity.setAdapter(discountProductRecyclerViewAdapter);
//                        }
//
//                        CustomerPercentTextViewShowDiscountDetailsActivity.setText(String.valueOf(ViewModel.getCustomerPercent()) + " " + getResources().getString(R.string.percent));
//
//                        RegionRepository regionRepository = new RegionRepository();
//                        AddressTextViewShowDiscountDetailsActivity.setText(regionRepository.GetFullName(ViewModel.getRegionId())+" " + ViewModel.getBusinessAddress());
//                    }
//                } else {
//                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
//                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
//                    } else {
//                        ShowErrorInConnectDialog();
//                    }
//                }
//            }
//        } catch (Exception e) {
//            HideLoading();
//            ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
//        }
//    }


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
