package ir.rayas.app.citywareclient.View.MasterChildren;

import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.UserPrizeRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Prize.PrizeService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Club.UserConsumePointViewModel;

public class UserPrizeActivity extends BaseActivity implements IResponseService, ILoadData {

    private RecyclerView UserPrizeRecyclerViewUserPrizeActivity = null;
    private SwipeRefreshLayout RefreshUserPrizeSwipeRefreshLayoutUserPrizeActivity = null;
    private TextViewPersian ShowEmptyUserPrizeTextViewUserPrizeActivity = null;
    private TextViewPersian MyPointTextViewUserPrizeActivity = null;
    private TextViewPersian PointsSpentTextViewUserPrizeActivity = null;

    private boolean IsSwipe = false;
    private Double MyPoint = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_prize);
        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.USER_PRIZE_ACTIVITY);

        MyPoint = getIntent().getExtras().getDouble("MyPoint");

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.my_gifts);


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
        ShowEmptyUserPrizeTextViewUserPrizeActivity = findViewById(R.id.ShowEmptyUserPrizeTextViewUserPrizeActivity);
        RefreshUserPrizeSwipeRefreshLayoutUserPrizeActivity = findViewById(R.id.RefreshUserPrizeSwipeRefreshLayoutUserPrizeActivity);
        MyPointTextViewUserPrizeActivity = findViewById(R.id.MyPointTextViewUserPrizeActivity);
        PointsSpentTextViewUserPrizeActivity = findViewById(R.id.PointsSpentTextViewUserPrizeActivity);
        TextViewPersian RemainingPointTextViewUserPrizeActivity = findViewById(R.id.RemainingPointTextViewUserPrizeActivity);

        ShowEmptyUserPrizeTextViewUserPrizeActivity.setVisibility(View.GONE);

        if (MyPoint == null) {
            MyPoint = 0.0;
            RemainingPointTextViewUserPrizeActivity.setText(String.valueOf((int) Math.round(MyPoint)));
        } else {
            RemainingPointTextViewUserPrizeActivity.setText(String.valueOf((int) Math.round(MyPoint)));
        }

        UserPrizeRecyclerViewUserPrizeActivity = findViewById(R.id.UserPrizeRecyclerViewUserPrizeActivity);
        UserPrizeRecyclerViewUserPrizeActivity.setHasFixedSize(true);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(UserPrizeActivity.this);
        UserPrizeRecyclerViewUserPrizeActivity.setLayoutManager(LinearLayoutManager);


        RefreshUserPrizeSwipeRefreshLayoutUserPrizeActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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

        PrizeService prizeService = new PrizeService(UserPrizeActivity.this);
        prizeService.GetUserPrize();
    }

    /**
     * @param Data
     * @param ServiceMethod
     * @param <T>
     */
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        HideLoading();
        RefreshUserPrizeSwipeRefreshLayoutUserPrizeActivity.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.PrizeUserGetAll) {
                Feedback<List<UserConsumePointViewModel>> FeedBack = (Feedback<List<UserConsumePointViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<UserConsumePointViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null && ViewModelList.size() > 0) {
                        ShowEmptyUserPrizeTextViewUserPrizeActivity.setVisibility(View.GONE);

                        UserPrizeRecyclerViewAdapter userPrizeRecyclerViewAdapter = new UserPrizeRecyclerViewAdapter(ViewModelList);
                        UserPrizeRecyclerViewUserPrizeActivity.setAdapter(userPrizeRecyclerViewAdapter);

                        Double Point = 0.0;
                        for (int i = 0; i < ViewModelList.size(); i++) {
                            Point = Point + ViewModelList.get(i).getPoint();
                        }

                        PointsSpentTextViewUserPrizeActivity.setText(String.valueOf((int) Math.round(Point)));
                        MyPointTextViewUserPrizeActivity.setText(String.valueOf((int) Math.round(Point + MyPoint)));

                    } else {
                        ShowEmptyUserPrizeTextViewUserPrizeActivity.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId())
                            ShowEmptyUserPrizeTextViewUserPrizeActivity.setVisibility(View.VISIBLE);
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

