package ir.rayas.app.citywareclient.View.Share;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.RegionRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Definition.RegionService;
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
import ir.rayas.app.citywareclient.ViewModel.Definition.RegionViewModel;

public class SelectRegionActivity extends BaseActivity implements IResponseService {

    private RegionRecyclerViewAdapter  RegionRecyclerViewAdapter = null;
    private RegionViewModel Region = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_region);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.SELECT_REGION_ACTIVITY);

        //آماده سازی قسمت لودینگ و پنجره خطا و سیستم پیغام در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonClick();
            }
        },R.string.region_select);


        //دریافت اطلاعات مناطق
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
        RegionService Service = new RegionService(this);
        Service.GetAllTree();
    }

    private void CreateLayout() {

        TextViewPersian RegionFragmentTitleTextView = findViewById(R.id.RegionFragmentTitleTextView);
        ButtonPersianView RegionFragmentSelectRegionButton =  findViewById(R.id.RegionFragmentSelectRegionButton);
        RegionFragmentTitleTextView.setTypeface(Font.MasterLightFont);
        RegionFragmentSelectRegionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnRegionFragmentSelectRegionButtonClick();
            }
        });

        //تنظیمات مربوط به recycle انتخاب منطقه
        RecyclerView RegionFragmentRegionRecyclerView = findViewById(R.id.RegionFragmentRegionRecyclerView);
        RegionFragmentRegionRecyclerView.setHasFixedSize(true);
        //به دلیل اینکه من در هر سطر یک گزینه نیاز دارم
        LinearLayoutManager RegionLinearLayoutManager = new LinearLayoutManager(this);
        RegionFragmentRegionRecyclerView.setLayoutManager(RegionLinearLayoutManager);
        RegionRecyclerViewAdapter = new RegionRecyclerViewAdapter(Region,RegionFragmentRegionRecyclerView);
        RegionFragmentRegionRecyclerView.setAdapter(RegionRecyclerViewAdapter);
    }

    private void OnRegionFragmentSelectRegionButtonClick()
    {
        if (RegionRecyclerViewAdapter.getSelectedRegionId()!=-1) {
            if (getIntent().getIntExtra("FromActivityId",-1)>-1) {
                HashMap<String, Object> Output = new HashMap<>();
                Output.put("RegionId", RegionRecyclerViewAdapter.getSelectedRegionId());
                Output.put("RegionName", RegionRecyclerViewAdapter.getSelectedRegionName());
                ActivityResultPassing.Push(new ActivityResult(getIntent().getIntExtra("FromActivityId", -1), getCurrentActivityId(), Output));
            }
            FinishCurrentActivity();
        } else {
          ShowToast(getResources().getString(R.string.please_select_region), Toast.LENGTH_SHORT, MessageType.Warning);
        }
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {

        if (ServiceMethod == ServiceMethodType.RegionAllTreeGet) {

            Feedback<RegionViewModel> FeedBack = (Feedback<RegionViewModel>) Data;
            if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                Region = FeedBack.getValue();
                if (Region != null) {
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
