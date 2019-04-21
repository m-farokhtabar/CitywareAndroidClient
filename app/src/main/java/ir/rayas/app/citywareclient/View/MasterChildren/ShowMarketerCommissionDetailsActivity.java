package ir.rayas.app.citywareclient.View.MasterChildren;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Marketing.MarketingService;
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
import ir.rayas.app.citywareclient.ViewModel.Marketing.MarketerCommissionInfoViewModel;

public class ShowMarketerCommissionDetailsActivity extends BaseActivity implements IResponseService {

    private TextViewPersian TotalNumberCustomerTextViewShowMarketerCommissionDetailsActivity = null;
    private TextViewPersian NumberCustomerUseTextViewShowMarketerCommissionDetailsActivity = null;
    private TextViewPersian PercentCustomerNumberTextViewShowMarketerCommissionDetailsActivity = null;
    private TextViewPersian TotalAmountCommissionTextViewShowMarketerCommissionDetailsActivity = null;
    private TextViewPersian AmountCommissionPaidTextViewShowMarketerCommissionDetailsActivity = null;
    private TextViewPersian RemainingAmountCommissionTextViewShowMarketerCommissionDetailsActivity = null;
    private TextViewPersian PercentAmountCommissionTextViewShowMarketerCommissionDetailsActivity = null;
    private ProgressBar PercentCustomerNumberProgressBarShowMarketerCommissionDetailsActivity = null;
    private ProgressBar PercentAmountCommissionProgressBarShowMarketerCommissionDetailsActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_marketer_commission_details);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.SHOW_MARKETER_COMMISSION_ACTIVITY);

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                LoadData();
            }
        }, R.string.marketer_commission);


        CreateLayout();
        //دریافت اطلاعات از سرور
        LoadData();
    }

    private void CreateLayout() {

        NumberCustomerUseTextViewShowMarketerCommissionDetailsActivity = findViewById(R.id.NumberCustomerUseTextViewShowMarketerCommissionDetailsActivity);
        TotalNumberCustomerTextViewShowMarketerCommissionDetailsActivity = findViewById(R.id.TotalNumberCustomerTextViewShowMarketerCommissionDetailsActivity);
        PercentCustomerNumberTextViewShowMarketerCommissionDetailsActivity = findViewById(R.id.PercentCustomerNumberTextViewShowMarketerCommissionDetailsActivity);
        PercentCustomerNumberProgressBarShowMarketerCommissionDetailsActivity = findViewById(R.id.PercentCustomerNumberProgressBarShowMarketerCommissionDetailsActivity);
        TotalAmountCommissionTextViewShowMarketerCommissionDetailsActivity = findViewById(R.id.TotalAmountCommissionTextViewShowMarketerCommissionDetailsActivity);
        AmountCommissionPaidTextViewShowMarketerCommissionDetailsActivity = findViewById(R.id.AmountCommissionPaidTextViewShowMarketerCommissionDetailsActivity);
        RemainingAmountCommissionTextViewShowMarketerCommissionDetailsActivity = findViewById(R.id.RemainingAmountCommissionTextViewShowMarketerCommissionDetailsActivity);
        PercentAmountCommissionProgressBarShowMarketerCommissionDetailsActivity = findViewById(R.id.PercentAmountCommissionProgressBarShowMarketerCommissionDetailsActivity);
        PercentAmountCommissionTextViewShowMarketerCommissionDetailsActivity = findViewById(R.id.PercentAmountCommissionTextViewShowMarketerCommissionDetailsActivity);
        ButtonPersianView CommissionReceivedButtonShowMarketerCommissionDetailsActivity = findViewById(R.id.CommissionReceivedButtonShowMarketerCommissionDetailsActivity);
        ButtonPersianView NoCommissionReceivedButtonShowMarketerCommissionDetailsActivity = findViewById(R.id.NoCommissionReceivedButtonShowMarketerCommissionDetailsActivity);
        ButtonPersianView ArchivesBusinessButtonShowMarketerCommissionDetailsActivity = findViewById(R.id.ArchivesBusinessButtonShowMarketerCommissionDetailsActivity);
        ButtonPersianView NewIntroductionsButtonShowMarketerCommissionDetailsActivity = findViewById(R.id.NewIntroductionsButtonShowMarketerCommissionDetailsActivity);

        CommissionReceivedButtonShowMarketerCommissionDetailsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        NoCommissionReceivedButtonShowMarketerCommissionDetailsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ArchivesBusinessButtonShowMarketerCommissionDetailsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        NewIntroductionsButtonShowMarketerCommissionDetailsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public void LoadData() {
        ShowLoadingProgressBar();

        MarketingService marketingService = new MarketingService(this);
        marketingService.GetMarketerCommission();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.MarketerCommissionGet) {

                Feedback<MarketerCommissionInfoViewModel> FeedBack = (Feedback<MarketerCommissionInfoViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    MarketerCommissionInfoViewModel ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        SetInformationToView(ViewModel);
                    }
                } else {
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

    @SuppressLint("SetTextI18n")
    private void SetInformationToView(MarketerCommissionInfoViewModel ViewModel) {


        TotalNumberCustomerTextViewShowMarketerCommissionDetailsActivity.setText(Utility.GetIntegerNumberWithComma(ViewModel.getAllMarketerSuggestion()));
        NumberCustomerUseTextViewShowMarketerCommissionDetailsActivity.setText(Utility.GetIntegerNumberWithComma(ViewModel.getAllSuccessMarketerSuggestion()));
        TotalAmountCommissionTextViewShowMarketerCommissionDetailsActivity.setText(Utility.GetIntegerNumberWithComma(ViewModel.getAllMarketerCommission()) + " " + getResources().getString(R.string.toman));
        AmountCommissionPaidTextViewShowMarketerCommissionDetailsActivity.setText(Utility.GetIntegerNumberWithComma(ViewModel.getAllReceivedMarketerCommission()) + " " + getResources().getString(R.string.toman));

        double TotalAmountCommission = ViewModel.getAllMarketerCommission() - ViewModel.getAllReceivedMarketerCommission();
        RemainingAmountCommissionTextViewShowMarketerCommissionDetailsActivity.setText(Utility.GetIntegerNumberWithComma(TotalAmountCommission) + " " + getResources().getString(R.string.toman));

        double TotalNumberCustomer = ViewModel.getAllMarketerSuggestion();
        double NumberCustomerUse = ViewModel.getAllSuccessMarketerSuggestion();
        Double Percent = (NumberCustomerUse * 100) / TotalNumberCustomer;
        int PercentProgress = (int) Double.parseDouble(Percent.toString());

        PercentCustomerNumberTextViewShowMarketerCommissionDetailsActivity.setText(Utility.GetIntegerNumberWithComma(Percent) + " " + getResources().getString(R.string.percent));

        Drawable drawable = getResources().getDrawable(R.drawable.circular_percent_progress_bar_yellow);
        PercentCustomerNumberProgressBarShowMarketerCommissionDetailsActivity.setProgress(PercentProgress);   // Main Progress
        PercentCustomerNumberProgressBarShowMarketerCommissionDetailsActivity.setSecondaryProgress(100); // Secondary Progress
        PercentCustomerNumberProgressBarShowMarketerCommissionDetailsActivity.setMax(100); // Maximum Progress
        PercentCustomerNumberProgressBarShowMarketerCommissionDetailsActivity.setProgressDrawable(drawable);


        double TotalAmount = ViewModel.getAllMarketerCommission();
        double AmountCommissionPaid = ViewModel.getAllReceivedMarketerCommission();
        Double PercentAmountCommission = (AmountCommissionPaid * 100) / TotalAmount;
        int PercentProgressAmountCommission = (int) Double.parseDouble(PercentAmountCommission.toString());

        PercentAmountCommissionTextViewShowMarketerCommissionDetailsActivity.setText(Utility.GetIntegerNumberWithComma(PercentAmountCommission) + " " + getResources().getString(R.string.percent));

        PercentAmountCommissionProgressBarShowMarketerCommissionDetailsActivity.setProgress(PercentProgressAmountCommission);   // Main Progress
        PercentAmountCommissionProgressBarShowMarketerCommissionDetailsActivity.setSecondaryProgress(100); // Secondary Progress
        PercentAmountCommissionProgressBarShowMarketerCommissionDetailsActivity.setMax(100); // Maximum Progress
        PercentAmountCommissionProgressBarShowMarketerCommissionDetailsActivity.setProgressDrawable(drawable);

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

