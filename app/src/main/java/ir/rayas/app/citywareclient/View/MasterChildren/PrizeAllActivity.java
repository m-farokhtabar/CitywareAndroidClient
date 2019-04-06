package ir.rayas.app.citywareclient.View.MasterChildren;

import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.PrizeAllRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Club.Prize.PrizeService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Club.PrizeViewModel;

public class PrizeAllActivity extends BaseActivity implements IResponseService, ILoadData {

    private RecyclerView PrizeAllRecyclerViewPrizeAllActivity = null;
    private SwipeRefreshLayout RefreshPrizeAllSwipeRefreshLayoutPrizeAllActivity = null;
    private TextViewPersian ShowEmptyPrizeAllTextViewPrizeAllActivity = null;

    private boolean IsSwipe = false;
    private Double MyPoint = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prize_all);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.PRIZE_ALL_ACTIVITY);

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.show_all_gift);

        MyPoint = getIntent().getExtras().getDouble("MyPoint");

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
        ShowEmptyPrizeAllTextViewPrizeAllActivity = findViewById(R.id.ShowEmptyPrizeAllTextViewPrizeAllActivity);
        RefreshPrizeAllSwipeRefreshLayoutPrizeAllActivity = findViewById(R.id.RefreshPrizeAllSwipeRefreshLayoutPrizeAllActivity);

        ShowEmptyPrizeAllTextViewPrizeAllActivity.setVisibility(View.GONE);

        PrizeAllRecyclerViewPrizeAllActivity = findViewById(R.id.PrizeAllRecyclerViewPrizeAllActivity);
        PrizeAllRecyclerViewPrizeAllActivity.setHasFixedSize(true);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(PrizeAllActivity.this);
        PrizeAllRecyclerViewPrizeAllActivity.setLayoutManager(LinearLayoutManager);


        RefreshPrizeAllSwipeRefreshLayoutPrizeAllActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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

        PrizeService prizeService = new PrizeService(PrizeAllActivity.this);
        prizeService.GetAll();
    }

    /**
     * @param Data
     * @param ServiceMethod
     * @param <T>
     */
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        HideLoading();
        RefreshPrizeAllSwipeRefreshLayoutPrizeAllActivity.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.PrizeGetAll) {
                HideLoading();
                Feedback<List<PrizeViewModel>> FeedBack = (Feedback<List<PrizeViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<PrizeViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null && ViewModelList.size() > 0) {
                        ShowEmptyPrizeAllTextViewPrizeAllActivity.setVisibility(View.GONE);

                        PrizeAllRecyclerViewAdapter prizeAllRecyclerViewAdapter = new PrizeAllRecyclerViewAdapter(PrizeAllActivity.this, ViewModelList, MyPoint);
                        PrizeAllRecyclerViewPrizeAllActivity.setAdapter(prizeAllRecyclerViewAdapter);

                    } else {
                        ShowEmptyPrizeAllTextViewPrizeAllActivity.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId())
                            ShowEmptyPrizeAllTextViewPrizeAllActivity.setVisibility(View.VISIBLE);
                        else
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

