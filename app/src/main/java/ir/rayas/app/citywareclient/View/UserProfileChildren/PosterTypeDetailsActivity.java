package ir.rayas.app.citywareclient.View.UserProfileChildren;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Package.PackageService;
import ir.rayas.app.citywareclient.Service.Poster.PosterService;
import ir.rayas.app.citywareclient.Share.Enum.PriorityType;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Poster.PosterTypeViewModel;

public class PosterTypeDetailsActivity extends BaseActivity implements IResponseService {

    private TextViewPersian UserCreditTextViewPosterTypeDetailsActivity = null;
    private TextViewPersian PosterTypeTitleTextViewPosterTypeDetailsActivity = null;
    private TextViewPersian PosterTypeCostTextViewPosterTypeDetailsActivity = null;
    private TextViewPersian PosterTypePriorityTextViewPosterTypeDetailsActivity = null;
    private TextViewPersian DimensionPosterTypeTextViewPosterTypeDetailsActivity = null;
    private SwitchCompat PosterTypeAlwaysOnTopSwitchPosterTypeDetailsActivity = null;
    private LinearLayout PosterTypePriorityLinearLayoutPosterTypeDetailsActivity = null;
    private LinearLayout DimensionPosterTypeLinearLayoutPosterTypeDetailsActivity = null;
    private TextViewPersian DescriptionPosterTypeTitleTextViewPosterTypeDetailsActivity = null;
    private  ButtonPersianView BuyPosterButtonPosterTypeDetailsActivity  = null;

    private int PosterTypeId = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_type_details);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.POSTER_TYPE_DETAILS_ACTIVITY);
        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.poster_type_details);

        PosterTypeId = getIntent().getExtras().getInt("PosterTypeId");

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
        PosterService.GetPosterType(PosterTypeId);
    }

    /**
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    private void CreateLayout() {
        UserCreditTextViewPosterTypeDetailsActivity = findViewById(R.id.UserCreditTextViewPosterTypeDetailsActivity);
        PosterTypeTitleTextViewPosterTypeDetailsActivity = findViewById(R.id.PosterTypeTitleTextViewPosterTypeDetailsActivity);
        PosterTypeCostTextViewPosterTypeDetailsActivity = findViewById(R.id.PosterTypeCostTextViewPosterTypeDetailsActivity);
        PosterTypePriorityLinearLayoutPosterTypeDetailsActivity = findViewById(R.id.PosterTypePriorityLinearLayoutPosterTypeDetailsActivity);
        PosterTypePriorityTextViewPosterTypeDetailsActivity = findViewById(R.id.PosterTypePriorityTextViewPosterTypeDetailsActivity);
        PosterTypeAlwaysOnTopSwitchPosterTypeDetailsActivity = findViewById(R.id.PosterTypeAlwaysOnTopSwitchPosterTypeDetailsActivity);
        DimensionPosterTypeLinearLayoutPosterTypeDetailsActivity = findViewById(R.id.DimensionPosterTypeLinearLayoutPosterTypeDetailsActivity);
        DimensionPosterTypeTextViewPosterTypeDetailsActivity = findViewById(R.id.DimensionPosterTypeTextViewPosterTypeDetailsActivity);
        DescriptionPosterTypeTitleTextViewPosterTypeDetailsActivity = findViewById(R.id.DescriptionPosterTypeTitleTextViewPosterTypeDetailsActivity);
        BuyPosterButtonPosterTypeDetailsActivity = findViewById(R.id.BuyPosterButtonPosterTypeDetailsActivity);

    }

    /**
     * @param Data
     * @param ServiceMethod
     * @param <T>
     */
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {

        try {
            if (ServiceMethod == ServiceMethodType.UserCreditGet) {
                Feedback<Double> FeedBack = (Feedback<Double>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    LoadData();

                    if (FeedBack.getValue() != null) {
                        UserCreditTextViewPosterTypeDetailsActivity.setText(Utility.GetIntegerNumberWithComma(FeedBack.getValue()));
                    } else {
                        UserCreditTextViewPosterTypeDetailsActivity.setText(getResources().getString(R.string.zero));
                    }

                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.PosterTypeGet) {
                HideLoading();
                Feedback<PosterTypeViewModel> FeedBack = (Feedback<PosterTypeViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    PosterTypeViewModel  ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        SetInformationToView(ViewModelList);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        if (!(FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId())) {
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
    }

    @SuppressLint("SetTextI18n")
    private void SetInformationToView(final PosterTypeViewModel ViewModel) {

        PosterTypeTitleTextViewPosterTypeDetailsActivity.setText(ViewModel.getName());
        PosterTypeCostTextViewPosterTypeDetailsActivity.setText(Utility.GetIntegerNumberWithComma(ViewModel.getPrice()));
        PosterTypeAlwaysOnTopSwitchPosterTypeDetailsActivity.setChecked(ViewModel.isTop());
        DescriptionPosterTypeTitleTextViewPosterTypeDetailsActivity.setText(ViewModel.getDescription());

        String Priority = "";

        if (ViewModel.getPriority() == PriorityType.Normal.getPriorityType())
            Priority = PriorityType.Normal.getValuePriorityType();
        else if (ViewModel.getPriority() == PriorityType.Much.getPriorityType())
            Priority = PriorityType.Much.getValuePriorityType();
        else if (ViewModel.getPriority() == PriorityType.VeryMuch.getPriorityType())
            Priority = PriorityType.VeryMuch.getValuePriorityType();

        PosterTypePriorityTextViewPosterTypeDetailsActivity.setText(Priority);

        int Rows = ViewModel.getRows();
        int Cols = ViewModel.getCols();
        DimensionPosterTypeTextViewPosterTypeDetailsActivity.setText(Rows + getResources().getString(R.string.star) + Cols);

        PosterTypeAlwaysOnTopSwitchPosterTypeDetailsActivity.setClickable(false);

        if (ViewModel.isTop()) {
            PosterTypePriorityLinearLayoutPosterTypeDetailsActivity.setVisibility(View.VISIBLE);
            DimensionPosterTypeLinearLayoutPosterTypeDetailsActivity.setVisibility(View.GONE);
        } else {
            PosterTypePriorityLinearLayoutPosterTypeDetailsActivity.setVisibility(View.GONE);
            DimensionPosterTypeLinearLayoutPosterTypeDetailsActivity.setVisibility(View.VISIBLE);
        }

        BuyPosterButtonPosterTypeDetailsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent BuyPosterTypeIntent = NewIntent(BuyPosterTypeActivity.class);
                BuyPosterTypeIntent.putExtra("PosterTypeId", PosterTypeId);
                BuyPosterTypeIntent.putExtra("PosterTypeName", ViewModel.getName());
                BuyPosterTypeIntent.putExtra("PosterTypePrice", ViewModel.getPrice());
                startActivity(BuyPosterTypeIntent);

            }
        });

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


