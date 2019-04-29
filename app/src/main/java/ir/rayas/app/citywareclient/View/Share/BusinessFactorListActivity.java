package ir.rayas.app.citywareclient.View.Share;

import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.BusinessFactorListRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Factor.BusinessFactorService;
import ir.rayas.app.citywareclient.Service.Factor.FactorStatusService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorStatusViewModel;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorViewModel;

public class BusinessFactorListActivity extends BaseActivity implements IResponseService {

    private TextViewPersian ShowEmptyFactorListTextViewBusinessFactorListActivity = null;
    private SwipeRefreshLayout RefreshFactorListSwipeRefreshLayoutBusinessFactorListActivity;
    private BusinessFactorListRecyclerViewAdapter BusinessFactorListRecyclerViewAdapter = null;

    private int PageNumber = 1;
    private boolean IsSwipe = false;
    private int BusinessId = 0;

    private List<FactorStatusViewModel> FactorStatusViewModel = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_factor_list);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.BUSINESS_FACTOR_LIST_ACTIVITY);
        Static.IsRefreshBookmark = true;
        BusinessId = getIntent().getExtras().getInt("BusinessId");

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.factor_business);

        //ایجاد طرحبندی صفحه
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
     * دریافت اطلاعات نحوای جهت پر کردن Recycle
     */
    private void LoadData() {
        if (!IsSwipe)
            if (PageNumber == 1)
                ShowLoadingProgressBar();


        FactorStatusService factorStatusService = new FactorStatusService(this);
        factorStatusService.GetAll();
    }

    private void LoadDataFactor() {

        BusinessFactorService BusinessFactorService = new BusinessFactorService(this);
        BusinessFactorService.GetAll(BusinessId, PageNumber);

    }

    /**
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    private void CreateLayout() {
        RefreshFactorListSwipeRefreshLayoutBusinessFactorListActivity = findViewById(R.id.RefreshFactorListSwipeRefreshLayoutBusinessFactorListActivity);
        ShowEmptyFactorListTextViewBusinessFactorListActivity = findViewById(R.id.ShowEmptyFactorListTextViewBusinessFactorListActivity);

        ShowEmptyFactorListTextViewBusinessFactorListActivity.setVisibility(View.GONE);

        RecyclerView factorListRecyclerViewBusinessFactorListActivity = findViewById(R.id.FactorListRecyclerViewBusinessFactorListActivity);
        factorListRecyclerViewBusinessFactorListActivity.setLayoutManager(new LinearLayoutManager(this));
        BusinessFactorListRecyclerViewAdapter = new BusinessFactorListRecyclerViewAdapter(this, null,FactorStatusViewModel, factorListRecyclerViewBusinessFactorListActivity);
        factorListRecyclerViewBusinessFactorListActivity.setAdapter(BusinessFactorListRecyclerViewAdapter);

        RefreshFactorListSwipeRefreshLayoutBusinessFactorListActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                IsSwipe = true;
                PageNumber = 1;
                LoadData();
            }
        });
    }

    /**
     * @param Data
     * @param ServiceMethod
     * @param <T>
     */
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {

        RefreshFactorListSwipeRefreshLayoutBusinessFactorListActivity.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.BusinessFactorGetAll) {
                HideLoading();
                Feedback<List<FactorViewModel>> FeedBack = (Feedback<List<FactorViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<FactorViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        if (PageNumber == 1) {
                            if (ViewModelList.size() < 1) {
                                ShowEmptyFactorListTextViewBusinessFactorListActivity.setVisibility(View.VISIBLE);
                            } else {
                                ShowEmptyFactorListTextViewBusinessFactorListActivity.setVisibility(View.GONE);
                                BusinessFactorListRecyclerViewAdapter.SetViewModelList(ViewModelList);

                                if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                    PageNumber = PageNumber + 1;
                                    LoadDataFactor();
                                }
                            }

                        } else {
                            ShowEmptyFactorListTextViewBusinessFactorListActivity.setVisibility(View.GONE);
                            BusinessFactorListRecyclerViewAdapter.AddViewModelList(ViewModelList);

                            if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                PageNumber = PageNumber + 1;
                                LoadDataFactor();
                            }
                        }
                    }
                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    if (PageNumber > 1) {
                        ShowEmptyFactorListTextViewBusinessFactorListActivity.setVisibility(View.GONE);
                    } else {
                        ShowEmptyFactorListTextViewBusinessFactorListActivity.setVisibility(View.VISIBLE);
                    }
                } else {
                    ShowEmptyFactorListTextViewBusinessFactorListActivity.setVisibility(View.GONE);
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }

            }  else if (ServiceMethod == ServiceMethodType.FactorStatusGetAll) {
                Feedback<List<FactorStatusViewModel>> FeedBack = (Feedback<List<FactorStatusViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    FactorStatusViewModel =  new ArrayList<>();
                    FactorStatusViewModel = FeedBack.getValue();

                    LoadDataFactor();

                } else {
                    HideLoading();
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
    protected void onResume() {
        super.onResume();
        // برای اینکه بفهمیم چه زمانی نیاز به رفرش صفحه داریم
        if (Static.IsRefreshBookmark) {
            PageNumber = 1;
            LoadData();

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
