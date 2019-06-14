package ir.rayas.app.citywareclient.View.Share;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
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
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResultPassing;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowBusinessCommissionActivity;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowMarketerCommissionDetailsActivity;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessViewModel;

public class UserBusinessListActivity extends BaseActivity implements IResponseService {

    private TextViewPersian ShowEmptyBusinessListTextViewUserBusinessListActivity = null;
    private SwipeRefreshLayout RefreshBusinessListSwipeRefreshLayoutUserBusinessListActivity = null;
    private RecyclerView businessListRecyclerViewUserBusinessListActivity = null;

    private boolean IsSwipe = false;
    private boolean IsParent = false;

    private String Title = "";
    private String Description = "";
    private String BusinessName = "";
    private int BusinessId = 0;
    private int activityIdList = 0;





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
        }, R.string.my_business);

        Title = getIntent().getExtras().getString("Title");
        Description = getIntent().getExtras().getString("Description");
        IsParent = getIntent().getExtras().getBoolean("IsParent");

        if (!IsParent) {
            activityIdList = getIntent().getExtras().getInt("activityIdList");
        }


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
        TextViewPersian TitleTextViewUserBusinessListActivity = findViewById(R.id.TitleTextViewUserBusinessListActivity);
        TextViewPersian DescriptionTextViewUserBusinessListActivity = findViewById(R.id.DescriptionTextViewUserBusinessListActivity);
        ButtonPersianView selectBusinessButtonUserBusinessListActivity = findViewById(R.id.SelectBusinessButtonUserBusinessListActivity);

        TitleTextViewUserBusinessListActivity.setTypeface(Font.MasterLightFont);
        TitleTextViewUserBusinessListActivity.setText(Title);
        DescriptionTextViewUserBusinessListActivity.setText(Description);
        ShowEmptyBusinessListTextViewUserBusinessListActivity.setVisibility(View.GONE);


        businessListRecyclerViewUserBusinessListActivity = findViewById(R.id.BusinessListRecyclerViewUserBusinessListActivity);
        businessListRecyclerViewUserBusinessListActivity.setHasFixedSize(true);
        //به دلیل اینکه من در هر سطر یک گزینه نیاز دارم
        LinearLayoutManager BusinessLinearLayoutManager = new LinearLayoutManager(this);
        businessListRecyclerViewUserBusinessListActivity.setLayoutManager(BusinessLinearLayoutManager);


        RefreshBusinessListSwipeRefreshLayoutUserBusinessListActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                IsSwipe = true;
                LoadData();

            }
        });

        selectBusinessButtonUserBusinessListActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectBusinessButtonClick();
            }
        });
    }


    private void SelectBusinessButtonClick() {
        if (BusinessId != 0) {
            if (IsParent) {
                HashMap<String, Object> Output = new HashMap<>();
                Output.put("BusinessId", BusinessId);
                Output.put("BusinessName", BusinessName);
                ActivityResultPassing.Push(new ActivityResult(getIntent().getIntExtra("FromActivityId", -1), getCurrentActivityId(), Output));

                FinishCurrentActivity();
            }  else {
                if (activityIdList == 55){
                    Intent ShowBusinessCommissionIntent = NewIntent(ShowBusinessCommissionActivity.class);
                    ShowBusinessCommissionIntent.putExtra("BusinessName",BusinessName);
                    startActivity(ShowBusinessCommissionIntent);
                   finish();
                }
            }
        } else {
            ShowToast(getResources().getString(R.string.select_businesses), Toast.LENGTH_SHORT, MessageType.Warning);
        }
    }

    public void SetBusinessNameToButton(String businessName, int businessId) {
        BusinessId = businessId;
        BusinessName = businessName;
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
                        UserBusinessListRecyclerViewAdapter userBusinessListRecyclerViewAdapter = new UserBusinessListRecyclerViewAdapter(UserBusinessListActivity.this, ViewModel, businessListRecyclerViewUserBusinessListActivity);
                        businessListRecyclerViewUserBusinessListActivity.setAdapter(userBusinessListRecyclerViewAdapter);


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
