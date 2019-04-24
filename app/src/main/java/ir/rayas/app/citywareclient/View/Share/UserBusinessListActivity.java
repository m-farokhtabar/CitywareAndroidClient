package ir.rayas.app.citywareclient.View.Share;

import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.UserBusinessListRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Business.BusinessService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessViewModel;

public class UserBusinessListActivity extends BaseActivity implements IResponseService {

    private RecyclerView BusinessListRecyclerViewUserBusinessListActivity = null;
    private TextViewPersian ShowEmptyBusinessListTextViewUserBusinessListActivity = null;
    private SwipeRefreshLayout RefreshBusinessListSwipeRefreshLayoutUserBusinessListActivity;

    private boolean IsSwipe = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_business_list);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.USER_BUSINESS_LIST_ACTIVITY);

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.business);

        //ایجاد طرحبندی صفحه
        CreateLayout();

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
     * دریافت اطلاعات نحوای جهت پر کردن Recycle
     */
    private void LoadData() {
        if (!IsSwipe)
            ShowLoadingProgressBar();

        BusinessService businessService = new BusinessService(this);
        businessService.GetAll();
    }



    /**
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    private void CreateLayout() {
        ShowEmptyBusinessListTextViewUserBusinessListActivity = findViewById(R.id.ShowEmptyBusinessListTextViewUserBusinessListActivity);
        RefreshBusinessListSwipeRefreshLayoutUserBusinessListActivity = findViewById(R.id.RefreshBusinessListSwipeRefreshLayoutUserBusinessListActivity);
        BusinessListRecyclerViewUserBusinessListActivity = findViewById(R.id.BusinessListRecyclerViewUserBusinessListActivity);
        BusinessListRecyclerViewUserBusinessListActivity.setHasFixedSize(true);
        //به دلیل اینکه من در هر سطر یک گزینه نیاز دارم
        LinearLayoutManager BusinessLinearLayoutManager = new LinearLayoutManager(this);
        BusinessListRecyclerViewUserBusinessListActivity.setLayoutManager(BusinessLinearLayoutManager);


        ShowEmptyBusinessListTextViewUserBusinessListActivity.setVisibility(View.GONE);

        RefreshBusinessListSwipeRefreshLayoutUserBusinessListActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                IsSwipe = true;
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
        RefreshBusinessListSwipeRefreshLayoutUserBusinessListActivity.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.UserGetAllBusiness) {
                Feedback<List<BusinessViewModel>> FeedBack = (Feedback<List<BusinessViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    List<BusinessViewModel> ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        ShowEmptyBusinessListTextViewUserBusinessListActivity.setVisibility(View.GONE);

                        //تنظیمات مربوط به recycle کسب و کار
                        UserBusinessListRecyclerViewAdapter userBusinessListRecyclerViewAdapter = new UserBusinessListRecyclerViewAdapter(UserBusinessListActivity.this, ViewModel);
                        BusinessListRecyclerViewUserBusinessListActivity.setAdapter(userBusinessListRecyclerViewAdapter);
                        userBusinessListRecyclerViewAdapter.notifyDataSetChanged();
                        BusinessListRecyclerViewUserBusinessListActivity.invalidate();
                    } else {
                        ShowEmptyBusinessListTextViewUserBusinessListActivity.setVisibility(View.VISIBLE);
                    }

                } else {
                    ShowEmptyBusinessListTextViewUserBusinessListActivity.setVisibility(View.GONE);
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
