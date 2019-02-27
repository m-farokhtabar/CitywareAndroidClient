package ir.rayas.app.citywareclient.View.Share;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.BusinessCategoryRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Definition.BusinessCategoryService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResultPassing;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Definition.BusinessCategoryViewModel;

public class SelectBusinessCategoryActivity extends BaseActivity implements IResponseService {

    private BusinessCategoryRecyclerViewAdapter BusinessCategoryRecyclerViewAdapter = null;
    private BusinessCategoryViewModel BusinessCategory = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_business_category);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.SELECT_BUSINESS_CATEGORY_ACTIVITY);

        //آماده سازی قسمت لودینگ و پنجره خطا و سیستم پیغام در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonClick();
            }
        },R.string.category_select);

        //دریافت اطلاعات صنف
        LoadData();
    }

    /**
     * در صورتی که در ارتباط با اینترنت مشکلی بوجود آمده و کاربر دکمه تلاش مجدد را فشار داده است
     */
    private void RetryButtonClick() {
        //دریافت اطلاعات
        LoadData();
    }

    /**
     * دریافت اطلاعات نحوای جهت پر کردن Recycle
     */
    private void LoadData() {
        ShowLoadingProgressBar();
        BusinessCategoryService Service = new BusinessCategoryService(this);
        Service.GetAllTree();
    }

    private void CreateLayout() {

        TextViewPersian TitleTextViewBusinessCategoryActivity = findViewById(R.id.TitleTextViewBusinessCategoryActivity);
        ButtonPersianView SelectCategoryButtonBusinessCategoryActivity = findViewById(R.id.SelectCategoryButtonBusinessCategoryActivity);
        TitleTextViewBusinessCategoryActivity.setTypeface(Font.MasterLightFont);
        SelectCategoryButtonBusinessCategoryActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnSelectBusinessCategoryButtonClick();
            }
        });

        //تنظیمات مربوط به recycle انتخاب صنف
        RecyclerView BusinessCategoryRecyclerViewBusinessCategoryActivity = findViewById(R.id.BusinessCategoryRecyclerViewBusinessCategoryActivity);
        BusinessCategoryRecyclerViewBusinessCategoryActivity.setHasFixedSize(true);
        //به دلیل اینکه من در هر سطر یک گزینه نیاز دارم
        LinearLayoutManager BusinessCategoryLinearLayoutManager = new LinearLayoutManager(this);
        BusinessCategoryRecyclerViewBusinessCategoryActivity.setLayoutManager(BusinessCategoryLinearLayoutManager);
        BusinessCategoryRecyclerViewAdapter = new BusinessCategoryRecyclerViewAdapter(BusinessCategory, BusinessCategoryRecyclerViewBusinessCategoryActivity);
        BusinessCategoryRecyclerViewBusinessCategoryActivity.setAdapter(BusinessCategoryRecyclerViewAdapter);
    }

    private void OnSelectBusinessCategoryButtonClick() {
        if (BusinessCategoryRecyclerViewAdapter.getSelectedBusinessCategoryId() != -1) {
            if (getIntent().getIntExtra("FromActivityId",-1)>-1) {
                HashMap<String, Object> Output = new HashMap<>();
                Output.put("BusinessCategoryId", BusinessCategoryRecyclerViewAdapter.getSelectedBusinessCategoryId());
                Output.put("BusinessCategoryName", BusinessCategoryRecyclerViewAdapter.getSelectedBusinessCategoryName());
                ActivityResultPassing.Push(new ActivityResult(getIntent().getIntExtra("FromActivityId", -1), getCurrentActivityId(), Output));
            }
            FinishCurrentActivity();
        } else {
            ShowToast(getResources().getString(R.string.please_select_business_category), Toast.LENGTH_SHORT, MessageType.Warning);
        }
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        HideLoading();
        if (ServiceMethod == ServiceMethodType.BusinessCategoryTreeGet) {

            Feedback<BusinessCategoryViewModel> FeedBack = (Feedback<BusinessCategoryViewModel>) Data;
            if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                BusinessCategory = FeedBack.getValue();
                if (BusinessCategory != null) {
                    //ایجاد طرح بندی صفحه
                    CreateLayout();
                    HideLoading();
                } else {
                    ShowToast(FeedbackType.InvalidDataFormat.getMessage().replace("{0}", ""), Toast.LENGTH_LONG, MessageType.Warning);
                    ShowErrorInConnectDialog();
                }
            } else {
                if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                    ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                }
                ShowErrorInConnectDialog();
            }
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

