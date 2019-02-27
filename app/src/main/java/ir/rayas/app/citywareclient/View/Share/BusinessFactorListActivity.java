package ir.rayas.app.citywareclient.View.Share;

import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.BusinessFactorListRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.OnLoadMoreListener;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Factor.BusinessFactorService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorViewModel;

public class BusinessFactorListActivity extends BaseActivity implements IResponseService {

    private RecyclerView FactorListRecyclerViewBusinessFactorListActivity = null;
    private TextViewPersian ShowEmptyFactorListTextViewBusinessFactorListActivity = null;
    private SwipeRefreshLayout RefreshFactorListSwipeRefreshLayoutBusinessFactorListActivity;
    private ProgressBar LoadMoreProgressBar = null;
    private BusinessFactorListRecyclerViewAdapter BusinessFactorListRecyclerViewAdapter = null;

    private int PageNumber = 1;
    private boolean IsSwipe = false;
    private int BusinessId = 0;

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

        BusinessFactorService BusinessFactorService = new BusinessFactorService(this);
        BusinessFactorService.GetAll(BusinessId, PageNumber);
    }

    /**
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    private void CreateLayout() {
        LoadMoreProgressBar = findViewById(R.id.LoadMoreProgressBarBusinessFactorListActivity);
        RefreshFactorListSwipeRefreshLayoutBusinessFactorListActivity = findViewById(R.id.RefreshFactorListSwipeRefreshLayoutBusinessFactorListActivity);
        ShowEmptyFactorListTextViewBusinessFactorListActivity = findViewById(R.id.ShowEmptyFactorListTextViewBusinessFactorListActivity);

        ShowEmptyFactorListTextViewBusinessFactorListActivity.setVisibility(View.GONE);

        FactorListRecyclerViewBusinessFactorListActivity = findViewById(R.id.FactorListRecyclerViewBusinessFactorListActivity);
        FactorListRecyclerViewBusinessFactorListActivity.setLayoutManager(new LinearLayoutManager(this));
        BusinessFactorListRecyclerViewAdapter = new BusinessFactorListRecyclerViewAdapter(this, null, FactorListRecyclerViewBusinessFactorListActivity, new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                PageNumber = PageNumber + 1;
                LoadMoreProgressBar.setVisibility(View.VISIBLE);
                LoadData();
            }
        });
        FactorListRecyclerViewBusinessFactorListActivity.setAdapter(BusinessFactorListRecyclerViewAdapter);

        RefreshFactorListSwipeRefreshLayoutBusinessFactorListActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadMoreProgressBar.setVisibility(View.GONE);
                IsSwipe = true;
                PageNumber = 1;
                BusinessFactorListRecyclerViewAdapter.setLoading(false);
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
        HideLoading();
        LoadMoreProgressBar.setVisibility(View.GONE);
        RefreshFactorListSwipeRefreshLayoutBusinessFactorListActivity.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.BusinessFactorGetAll) {
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
                            }
                        } else {
                            ShowEmptyFactorListTextViewBusinessFactorListActivity.setVisibility(View.GONE);
                            BusinessFactorListRecyclerViewAdapter.AddViewModelList(ViewModelList);
                        }
                    }
                } else {
                    ShowEmptyFactorListTextViewBusinessFactorListActivity.setVisibility(View.GONE);
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        if (!(PageNumber > 1 && FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId())) {
                            ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);

                        }
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            }
        } catch (Exception e) {
            HideLoading();
            ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
        BusinessFactorListRecyclerViewAdapter.setLoading(false);
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
