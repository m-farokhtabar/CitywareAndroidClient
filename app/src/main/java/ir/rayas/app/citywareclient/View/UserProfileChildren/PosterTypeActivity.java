package ir.rayas.app.citywareclient.View.UserProfileChildren;

import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.PosterTypeRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.OnLoadMoreListener;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Package.PackageService;
import ir.rayas.app.citywareclient.Service.Poster.PosterService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Poster.PosterTypeViewModel;

public class PosterTypeActivity extends BaseActivity implements IResponseService {

    private SwipeRefreshLayout RefreshPosterTypeSwipeRefreshLayoutPosterTypeActivity = null;
    private TextViewPersian UserCreditTextViewPosterTypeActivity = null;
    private ProgressBar LoadMoreProgressBar = null;
    private PosterTypeRecyclerViewAdapter PosterTypeRecyclerViewAdapter = null;

    private int PageNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_type);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.POSTER_TYPE_ACTIVITY);
        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.poster_type);

        //ایجاد طرحبندی صفحه
        CreateLayout();

        LoadDataUserCredit();
    }
    /**
     * در صورتی که در ارتباط با اینترنت مشکلی بوجود آمده و کاربر دکمه تلاش مجدد را فشار داده است
     */
    private void RetryButtonOnClick() {
        //دریافت اطلاعات
        LoadDataUserCredit();
    }

    public void LoadDataUserCredit() {

        ShowLoadingProgressBar();

        PackageService packageService = new PackageService(this);
        packageService.GetUserCredit();

    }

    /**
     * دریافت اطلاعات نحوای جهت پر کردن Recycle
     */
    private void LoadData() {

        PosterService PosterService = new PosterService(this);
        PosterService.GetAllPosterType(PageNumber);
    }

    /**
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    private void CreateLayout() {
        LoadMoreProgressBar = findViewById(R.id.LoadMoreProgressBarPosterTypeActivity);
        RefreshPosterTypeSwipeRefreshLayoutPosterTypeActivity = findViewById(R.id.RefreshPosterTypeSwipeRefreshLayoutPosterTypeActivity);
        UserCreditTextViewPosterTypeActivity = findViewById(R.id.UserCreditTextViewPosterTypeActivity);

        RecyclerView PosterTypeRecyclerViewPosterTypeActivity = findViewById(R.id.PosterTypeRecyclerViewPosterTypeActivity);
        PosterTypeRecyclerViewPosterTypeActivity.setLayoutManager(new LinearLayoutManager(this));
        PosterTypeRecyclerViewAdapter = new PosterTypeRecyclerViewAdapter(this, null, PosterTypeRecyclerViewPosterTypeActivity, new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                PageNumber = PageNumber + 1;
                LoadMoreProgressBar.setVisibility(View.VISIBLE);
                LoadData();
            }
        });
        PosterTypeRecyclerViewPosterTypeActivity.setAdapter(PosterTypeRecyclerViewAdapter);

        RefreshPosterTypeSwipeRefreshLayoutPosterTypeActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadMoreProgressBar.setVisibility(View.GONE);
                PageNumber = 1;
                PosterTypeRecyclerViewAdapter.setLoading(false);
                LoadDataUserCredit();
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
        RefreshPosterTypeSwipeRefreshLayoutPosterTypeActivity.setRefreshing(false);
        try {
            if (ServiceMethod == ServiceMethodType.UserCreditGet) {
                Feedback<Double> FeedBack = (Feedback<Double>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    LoadData();

                    if (FeedBack.getValue() != null) {
                        UserCreditTextViewPosterTypeActivity.setText(Utility.GetIntegerNumberWithComma(FeedBack.getValue()));
                    } else {
                        UserCreditTextViewPosterTypeActivity.setText(getResources().getString(R.string.zero));
                    }

                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }

            }else if (ServiceMethod == ServiceMethodType.PosterTypeGetAll) {
                HideLoading();
                Feedback<List<PosterTypeViewModel>> FeedBack = (Feedback<List<PosterTypeViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<PosterTypeViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        if (PageNumber == 1)
                            PosterTypeRecyclerViewAdapter.SetViewModelList(ViewModelList);
                        else
                            PosterTypeRecyclerViewAdapter.AddViewModelList(ViewModelList);
                    }
                } else {
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
        PosterTypeRecyclerViewAdapter.setLoading(false);
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

