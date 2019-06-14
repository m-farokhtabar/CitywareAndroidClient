package ir.rayas.app.citywareclient.View.MasterChildren;

import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.UserActionRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Prize.PrizeService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Club.UserActionPointViewModel;

public class UserActionPointActivity extends BaseActivity implements IResponseService, ILoadData {

    private RecyclerView ActionRecyclerViewUserActionPointActivity = null;
    private SwipeRefreshLayout RefreshActionSwipeRefreshLayoutUserActionPointActivity = null;
    private TextViewPersian ShowEmptyActionTextViewUserActionPointActivity = null;
    private TextViewPersian MyPointTextViewUserActionPointActivity = null;
    private TextViewPersian PointsSpentTextViewUserActionPointActivity = null;

    private boolean IsSwipe = false;
    private Double MyPoint = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_action_point);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.USER_ACTION_POINT_ACTIVITY);

        MyPoint = getIntent().getExtras().getDouble("MyPoint");

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.report_scores);


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
        ShowEmptyActionTextViewUserActionPointActivity = findViewById(R.id.ShowEmptyActionTextViewUserActionPointActivity);
        RefreshActionSwipeRefreshLayoutUserActionPointActivity = findViewById(R.id.RefreshActionSwipeRefreshLayoutUserActionPointActivity);
        MyPointTextViewUserActionPointActivity = findViewById(R.id.MyPointTextViewUserActionPointActivity);
        PointsSpentTextViewUserActionPointActivity = findViewById(R.id.PointsSpentTextViewUserActionPointActivity);
        TextViewPersian RemainingPointTextViewUserActionPointActivity = findViewById(R.id.RemainingPointTextViewUserActionPointActivity);

        ShowEmptyActionTextViewUserActionPointActivity.setVisibility(View.GONE);

        ActionRecyclerViewUserActionPointActivity = findViewById(R.id.ActionRecyclerViewUserActionPointActivity);
        ActionRecyclerViewUserActionPointActivity.setHasFixedSize(true);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(UserActionPointActivity.this);
        ActionRecyclerViewUserActionPointActivity.setLayoutManager(LinearLayoutManager);

        TextViewPersian ReportPointIconTextViewUserActionPointActivity = findViewById(R.id.ReportPointIconTextViewUserActionPointActivity);
        ReportPointIconTextViewUserActionPointActivity.setTypeface(Font.MasterIcon);
        ReportPointIconTextViewUserActionPointActivity.setText("\uf080");

        if (MyPoint == null) {
            MyPoint = 0.0;
            RemainingPointTextViewUserActionPointActivity.setText(String.valueOf((int) Math.round(MyPoint)));
        } else {
            RemainingPointTextViewUserActionPointActivity.setText(String.valueOf((int) Math.round(MyPoint)));
        }

        RefreshActionSwipeRefreshLayoutUserActionPointActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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

        PrizeService prizeService = new PrizeService(UserActionPointActivity.this);
        prizeService.GetUserActionPoint();
    }

    /**
     * @param Data
     * @param ServiceMethod
     * @param <T>
     */
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        HideLoading();
        RefreshActionSwipeRefreshLayoutUserActionPointActivity.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.UserActionPointGet) {
                Feedback<List<UserActionPointViewModel>> FeedBack = (Feedback<List<UserActionPointViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<UserActionPointViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null && ViewModelList.size() > 0) {
                        ShowEmptyActionTextViewUserActionPointActivity.setVisibility(View.GONE);

                        UserActionRecyclerViewAdapter userActionRecyclerViewAdapter = new UserActionRecyclerViewAdapter(ViewModelList);
                        ActionRecyclerViewUserActionPointActivity.setAdapter(userActionRecyclerViewAdapter);

                        Double Point = 0.0;
                        for (int i = 0; i < ViewModelList.size(); i++) {
                            Point = Point + ViewModelList.get(i).getPoint();
                        }

                        PointsSpentTextViewUserActionPointActivity.setText(String.valueOf((int) Math.round(Point - MyPoint)));
                        MyPointTextViewUserActionPointActivity.setText(String.valueOf((int) Math.round(Point)));


                    } else {
                        ShowEmptyActionTextViewUserActionPointActivity.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId())
                            ShowEmptyActionTextViewUserActionPointActivity.setVisibility(View.VISIBLE);
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


