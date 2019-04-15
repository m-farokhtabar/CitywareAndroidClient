package ir.rayas.app.citywareclient.View.MasterChildren;

import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.ActionAllRecyclerViewAdapter;
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
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Club.ActionViewModel;

public class ActionPointAllActivity extends BaseActivity implements IResponseService, ILoadData {

    private RecyclerView ActionRecyclerViewActionPointAllActivity = null;
    private SwipeRefreshLayout RefreshActionSwipeRefreshLayoutActionPointAllActivity = null;
    private TextViewPersian ShowEmptyActionTextViewActionPointAllActivity = null;

    private boolean IsSwipe = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_point_all);
        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.ACTION_POINT_ALL_ACTIVITY);

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.scoring);


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
        ShowEmptyActionTextViewActionPointAllActivity = findViewById(R.id.ShowEmptyActionTextViewActionPointAllActivity);
        RefreshActionSwipeRefreshLayoutActionPointAllActivity = findViewById(R.id.RefreshActionSwipeRefreshLayoutActionPointAllActivity);

        ShowEmptyActionTextViewActionPointAllActivity.setVisibility(View.GONE);

        ActionRecyclerViewActionPointAllActivity = findViewById(R.id.ActionRecyclerViewActionPointAllActivity);
        ActionRecyclerViewActionPointAllActivity.setHasFixedSize(true);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(ActionPointAllActivity.this);
        ActionRecyclerViewActionPointAllActivity.setLayoutManager(LinearLayoutManager);


        RefreshActionSwipeRefreshLayoutActionPointAllActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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

        PrizeService prizeService = new PrizeService(ActionPointAllActivity.this);
        prizeService.GetActionPointAll();
    }

    /**
     * @param Data
     * @param ServiceMethod
     * @param <T>
     */
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        HideLoading();
        RefreshActionSwipeRefreshLayoutActionPointAllActivity.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.ActionPointGetAll) {
                Feedback<List<ActionViewModel>> FeedBack = (Feedback<List<ActionViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<ActionViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null && ViewModelList.size() > 0) {
                        ShowEmptyActionTextViewActionPointAllActivity.setVisibility(View.GONE);

                        ActionAllRecyclerViewAdapter actionAllRecyclerViewAdapter = new ActionAllRecyclerViewAdapter(ActionPointAllActivity.this,ViewModelList);
                        ActionRecyclerViewActionPointAllActivity.setAdapter(actionAllRecyclerViewAdapter);

                    } else {
                        ShowEmptyActionTextViewActionPointAllActivity.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId())
                            ShowEmptyActionTextViewActionPointAllActivity.setVisibility(View.VISIBLE);
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

