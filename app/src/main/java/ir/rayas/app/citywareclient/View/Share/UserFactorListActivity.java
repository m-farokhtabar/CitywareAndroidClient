package ir.rayas.app.citywareclient.View.Share;

import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.UserFactorListRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Factor.FactorStatusService;
import ir.rayas.app.citywareclient.Service.Factor.UserFactorService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Fragment.UserProfile.UserAddressFragment;
import ir.rayas.app.citywareclient.View.Fragment.UserProfile.UserBusinessFragment;
import ir.rayas.app.citywareclient.View.Fragment.UserProfile.UserPackageFragment;
import ir.rayas.app.citywareclient.View.Fragment.UserProfile.UserPosterFragment;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessViewModel;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorStatusViewModel;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorViewModel;
import ir.rayas.app.citywareclient.ViewModel.Package.OutputPackageTransactionViewModel;
import ir.rayas.app.citywareclient.ViewModel.Poster.PurchasedPosterViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.UserAddressViewModel;

public class UserFactorListActivity extends BaseActivity implements IResponseService {

    private TextViewPersian ShowEmptyFactorListTextViewUserFactorListActivity = null;
    private SwipeRefreshLayout RefreshFactorListSwipeRefreshLayoutUserFactorListActivity;
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

        //ایجاد طرحبندی صفحه
        CreateLayout();

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.factor);


    }

    /**
     * در صورتی که در ارتباط با اینترنت مشکلی بوجود آمده و کاربر دکمه تلاش مجدد را فشار داده است
     */
    private void RetryButtonOnClick() {
        //دریافت اطلاعات
        PageNumber = 1;
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

        UserFactorService UserFactorService = new UserFactorService(this);
        UserFactorService.GetAll(PageNumber);

    }

    /**
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    private void CreateLayout() {
        RefreshFactorListSwipeRefreshLayoutUserFactorListActivity = findViewById(R.id.RefreshFactorListSwipeRefreshLayoutUserFactorListActivity);
        ShowEmptyFactorListTextViewUserFactorListActivity = findViewById(R.id.ShowEmptyFactorListTextViewUserFactorListActivity);

        ShowEmptyFactorListTextViewUserFactorListActivity.setVisibility(View.GONE);

        RecyclerView factorListRecyclerViewUserFactorListActivity = findViewById(R.id.FactorListRecyclerViewUserFactorListActivity);
        factorListRecyclerViewUserFactorListActivity.setLayoutManager(new LinearLayoutManager(this));
        UserFactorListRecyclerViewAdapter = new UserFactorListRecyclerViewAdapter(this, null, FactorStatusViewModel, factorListRecyclerViewUserFactorListActivity);
        factorListRecyclerViewUserFactorListActivity.setAdapter(UserFactorListRecyclerViewAdapter);

        RefreshFactorListSwipeRefreshLayoutUserFactorListActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
                                UserFactorListRecyclerViewAdapter.SetViewModelList(ViewModelList, FactorStatusViewModel);

                                if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                    PageNumber = PageNumber + 1;
                                    LoadDataFactor();
                                }
                            }

                        } else {
                            ShowEmptyFactorListTextViewUserFactorListActivity.setVisibility(View.GONE);
                            UserFactorListRecyclerViewAdapter.AddViewModelList(ViewModelList, FactorStatusViewModel);

                            if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                PageNumber = PageNumber + 1;
                                LoadDataFactor();
                            }
                        }
                    }
                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    if (PageNumber > 1) {
                        ShowEmptyFactorListTextViewUserFactorListActivity.setVisibility(View.GONE);
                    } else {
                        ShowEmptyFactorListTextViewUserFactorListActivity.setVisibility(View.VISIBLE);
                    }
                } else {
                    ShowEmptyFactorListTextViewUserFactorListActivity.setVisibility(View.GONE);
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.FactorStatusGetAll) {
                Feedback<List<FactorStatusViewModel>> FeedBack = (Feedback<List<FactorStatusViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    LoadDataFactor();

                    FactorStatusViewModel = new ArrayList<>();
                    FactorStatusViewModel = FeedBack.getValue();

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
    protected void onGetResult(ActivityResult Result) {
        if (Result.getFromActivityId() == getCurrentActivityId()) {
            switch (Result.getToActivityId()) {
                case ActivityIdList.USER_FACTOR_DETAIL_ACTIVITY:

                    ShowLoadingProgressBar();
                    PageNumber = 1;
                    LoadDataFactor();

                    break;
            }
        }
        super.onGetResult(Result);
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
