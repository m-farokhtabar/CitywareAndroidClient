package ir.rayas.app.citywareclient.View.Share;

import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.OnLoadMoreListener;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.UserFactorListRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Factor.FactorStatusService;
import ir.rayas.app.citywareclient.Service.Factor.UserFactorService;
import ir.rayas.app.citywareclient.Service.IResponseService;
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

public class UserFactorListActivity extends BaseActivity implements IResponseService {

    private RecyclerView FactorListRecyclerViewUserFactorListActivity = null;
    private TextViewPersian ShowEmptyFactorListTextViewUserFactorListActivity = null;
    private SwipeRefreshLayout RefreshFactorListSwipeRefreshLayoutUserFactorListActivity;
    private ProgressBar LoadMoreProgressBar = null;
    private UserFactorListRecyclerViewAdapter UserFactorListRecyclerViewAdapter = null;

    private int PageNumber = 1;
    private boolean IsSwipe = false;

    private List<FactorStatusViewModel> FactorStatusViewModel = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_factor_list);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.USER_FACTOR_LIST_ACTIVITY);
        Static.IsRefreshBookmark = true;

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.factor);

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
        if (!IsSwipe)
            if (PageNumber == 1)
                ShowLoadingProgressBar();

        UserFactorService UserFactorService = new UserFactorService(this);
        UserFactorService.GetAll(PageNumber);

    }

    /**
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    private void CreateLayout() {
        LoadMoreProgressBar = findViewById(R.id.LoadMoreProgressBarUserFactorListActivity);
        RefreshFactorListSwipeRefreshLayoutUserFactorListActivity = findViewById(R.id.RefreshFactorListSwipeRefreshLayoutUserFactorListActivity);
        ShowEmptyFactorListTextViewUserFactorListActivity = findViewById(R.id.ShowEmptyFactorListTextViewUserFactorListActivity);

        ShowEmptyFactorListTextViewUserFactorListActivity.setVisibility(View.GONE);

        FactorListRecyclerViewUserFactorListActivity = findViewById(R.id.FactorListRecyclerViewUserFactorListActivity);
        FactorListRecyclerViewUserFactorListActivity.setLayoutManager(new LinearLayoutManager(this));


        UserFactorListRecyclerViewAdapter = new UserFactorListRecyclerViewAdapter(this, null,FactorStatusViewModel, FactorListRecyclerViewUserFactorListActivity, new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                PageNumber = PageNumber + 1;
                LoadMoreProgressBar.setVisibility(View.VISIBLE);
                LoadDataFactor();
            }
        });
        FactorListRecyclerViewUserFactorListActivity.setAdapter(UserFactorListRecyclerViewAdapter);

        RefreshFactorListSwipeRefreshLayoutUserFactorListActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadMoreProgressBar.setVisibility(View.GONE);
                IsSwipe = true;
                PageNumber = 1;
                UserFactorListRecyclerViewAdapter.setLoading(false);
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

        LoadMoreProgressBar.setVisibility(View.GONE);
        RefreshFactorListSwipeRefreshLayoutUserFactorListActivity.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.UserFactorGetAll) {
                HideLoading();
                Feedback<List<FactorViewModel>> FeedBack = (Feedback<List<FactorViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<FactorViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        if (PageNumber == 1) {
                            if (ViewModelList.size() < 1) {
                                ShowEmptyFactorListTextViewUserFactorListActivity.setVisibility(View.VISIBLE);
                            } else {
                                ShowEmptyFactorListTextViewUserFactorListActivity.setVisibility(View.GONE);
                                UserFactorListRecyclerViewAdapter.SetViewModelList(ViewModelList);
                            }
                        } else {
                            ShowEmptyFactorListTextViewUserFactorListActivity.setVisibility(View.GONE);
                            UserFactorListRecyclerViewAdapter.AddViewModelList(ViewModelList);
                        }
                    }
                } else {
                    ShowEmptyFactorListTextViewUserFactorListActivity.setVisibility(View.GONE);
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        if (!(PageNumber > 1 && FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId())) {
                            ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);

                        }
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            }   else if (ServiceMethod == ServiceMethodType.FactorStatusGetAll) {
                Feedback<List<FactorStatusViewModel>> FeedBack = (Feedback<List<FactorStatusViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    UserFactorService UserFactorService = new UserFactorService(this);
                    UserFactorService.GetAll(PageNumber);

                    FactorStatusViewModel =  new ArrayList<>();
                    FactorStatusViewModel = FeedBack.getValue();

                    UserFactorListRecyclerViewAdapter = new UserFactorListRecyclerViewAdapter(this, null,FactorStatusViewModel, FactorListRecyclerViewUserFactorListActivity, new OnLoadMoreListener() {
                        @Override
                        public void onLoadMore() {
                            PageNumber = PageNumber + 1;
                            LoadMoreProgressBar.setVisibility(View.VISIBLE);
                            LoadDataFactor();
                        }
                    });
                    FactorListRecyclerViewUserFactorListActivity.setAdapter(UserFactorListRecyclerViewAdapter);

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
        UserFactorListRecyclerViewAdapter.setLoading(false);
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
