package ir.rayas.app.citywareclient.View.MasterChildren;

import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.DiscountRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Global.Static;
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
import ir.rayas.app.citywareclient.ViewModel.Marketing.MarketingCustomerViewModel;

public class DiscountActivity extends BaseActivity implements IResponseService {

    private RecyclerView DiscountRecyclerViewDiscountActivity = null;
    private SwipeRefreshLayout RefreshDiscountSwipeRefreshLayoutDiscountActivity = null;
    private TextViewPersian ShowEmptyDiscountTextViewDiscountActivity = null;

    private boolean IsSwipe = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discounts);

        setCurrentActivityId(ActivityIdList.USER_PRIZE_ACTIVITY);

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.get_discounts);


        //ایجاد طرح بندی صفحه
        CreateLayout();
        //دریافت اطلاعات از سرور
        LoadData();
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
        ShowEmptyDiscountTextViewDiscountActivity = findViewById(R.id.ShowEmptyDiscountTextViewDiscountActivity);
        RefreshDiscountSwipeRefreshLayoutDiscountActivity = findViewById(R.id.RefreshDiscountSwipeRefreshLayoutDiscountActivity);
        ShowEmptyDiscountTextViewDiscountActivity.setVisibility(View.GONE);

        DiscountRecyclerViewDiscountActivity = findViewById(R.id.DiscountRecyclerViewDiscountActivity);
        DiscountRecyclerViewDiscountActivity.setHasFixedSize(true);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(DiscountActivity.this);
        DiscountRecyclerViewDiscountActivity.setLayoutManager(LinearLayoutManager);


        RefreshDiscountSwipeRefreshLayoutDiscountActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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

        MarketingService MarketingService = new MarketingService(DiscountActivity.this);
        MarketingService.GetCustomerPercents();
    }

    /**
     * @param Data
     * @param ServiceMethod
     * @param <T>
     */
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        HideLoading();
        RefreshDiscountSwipeRefreshLayoutDiscountActivity.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.CustomerPercentsGet) {
                Feedback<List<MarketingCustomerViewModel>> FeedBack = (Feedback<List<MarketingCustomerViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<MarketingCustomerViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null && ViewModelList.size() > 0) {
                        ShowEmptyDiscountTextViewDiscountActivity.setVisibility(View.GONE);

                        DiscountRecyclerViewAdapter discountRecyclerViewAdapter = new DiscountRecyclerViewAdapter(DiscountActivity.this,ViewModelList);
                        DiscountRecyclerViewDiscountActivity.setAdapter(discountRecyclerViewAdapter);

                    } else {
                        ShowEmptyDiscountTextViewDiscountActivity.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId())
                            ShowEmptyDiscountTextViewDiscountActivity.setVisibility(View.VISIBLE);
                        else
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


