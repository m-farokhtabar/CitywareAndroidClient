package ir.rayas.app.citywareclient.View.Fragment.BusinessCommission;


import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Marketing.MarketingService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowBusinessCommissionActivity;
import ir.rayas.app.citywareclient.ViewModel.Marketing.BusinessCommissionInfoViewModel;


public class ReportBusinessCommissionFragment extends Fragment implements IResponseService, ILoadData {

    private ShowBusinessCommissionActivity Context = null;

    private TextViewPersian TotalNumberCustomerIntroducedTextViewReportBusinessCommissionFragment = null;
    private TextViewPersian NumberCustomerUseDiscountTextViewReportBusinessCommissionFragment = null;
    private TextViewPersian PercentCustomerNumberTextViewReportBusinessCommissionFragment = null;
    private TextViewPersian UnpaidCommissionTextViewReportBusinessCommissionFragment = null;
    private TextViewPersian AmountCommissionUnPaidTextViewReportBusinessCommissionFragment = null;
    private TextViewPersian PaidCommissionTextViewReportBusinessCommissionFragment = null;
    private TextViewPersian AmountCommissionPaidTextViewReportBusinessCommissionFragment = null;
    private TextViewPersian PercentAmountCommissionTextViewReportBusinessCommissionFragment = null;
    private ProgressBar PercentCustomerNumberProgressBarReportBusinessCommissionFragment = null;
    private ProgressBar PercentAmountCommissionProgressBarReportBusinessCommissionFragment = null;

    private int BusinessId = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (ShowBusinessCommissionActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_report_business_commission, container, false);

        //طرحبندی ویو
        CreateLayout(CurrentView);
        //برای فهمیدن کد فرگنت به BusinessCommissionPagerAdapter مراجعه کنید
        Context.setFragmentIndex(4);

        BusinessId = Context.getIntent().getExtras().getInt("BusinessId");

        //دریافت اطلاعات از سرور
        LoadData();

        return CurrentView;
    }

    public void LoadData() {
        Context.ShowLoadingProgressBar();

        Context.setRetryType(2);
        MarketingService marketingService = new MarketingService(this);
        marketingService.GetBusinessCommission(BusinessId);
    }


    private void CreateLayout(View CurrentView) {

        TotalNumberCustomerIntroducedTextViewReportBusinessCommissionFragment = CurrentView.findViewById(R.id.TotalNumberCustomerIntroducedTextViewReportBusinessCommissionFragment);
        NumberCustomerUseDiscountTextViewReportBusinessCommissionFragment = CurrentView.findViewById(R.id.NumberCustomerUseDiscountTextViewReportBusinessCommissionFragment);
        PercentCustomerNumberProgressBarReportBusinessCommissionFragment = CurrentView.findViewById(R.id.PercentCustomerNumberProgressBarReportBusinessCommissionFragment);
        PercentCustomerNumberTextViewReportBusinessCommissionFragment = CurrentView.findViewById(R.id.PercentCustomerNumberTextViewReportBusinessCommissionFragment);
        UnpaidCommissionTextViewReportBusinessCommissionFragment = CurrentView.findViewById(R.id.UnpaidCommissionTextViewReportBusinessCommissionFragment);
        PaidCommissionTextViewReportBusinessCommissionFragment = CurrentView.findViewById(R.id.PaidCommissionTextViewReportBusinessCommissionFragment);
        AmountCommissionUnPaidTextViewReportBusinessCommissionFragment = CurrentView.findViewById(R.id.AmountCommissionUnPaidTextViewReportBusinessCommissionFragment);
        AmountCommissionPaidTextViewReportBusinessCommissionFragment = CurrentView.findViewById(R.id.AmountCommissionPaidTextViewReportBusinessCommissionFragment);
        PercentAmountCommissionProgressBarReportBusinessCommissionFragment = CurrentView.findViewById(R.id.PercentAmountCommissionProgressBarReportBusinessCommissionFragment);
        PercentAmountCommissionTextViewReportBusinessCommissionFragment = CurrentView.findViewById(R.id.PercentAmountCommissionTextViewReportBusinessCommissionFragment);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.BusinessCommissionGet) {

                Feedback<BusinessCommissionInfoViewModel> FeedBack = (Feedback<BusinessCommissionInfoViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    BusinessCommissionInfoViewModel ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        SetInformationToView(ViewModel);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            }
        } catch (Exception e) {
            Context.HideLoading();
            Context.ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
    }


    @SuppressLint("SetTextI18n")
    private void SetInformationToView(BusinessCommissionInfoViewModel ViewModel) {

        if (ViewModel.getAllSuggestedCustomers() == null) {
            TotalNumberCustomerIntroducedTextViewReportBusinessCommissionFragment.setText(Context.getResources().getString(R.string.zero));
        } else {
            TotalNumberCustomerIntroducedTextViewReportBusinessCommissionFragment.setText(Utility.GetIntegerNumberWithComma(ViewModel.getAllSuggestedCustomers()));
        }

        if (ViewModel.getAllCustomersInUse() == null) {
            NumberCustomerUseDiscountTextViewReportBusinessCommissionFragment.setText(Context.getResources().getString(R.string.zero));
        } else {
            NumberCustomerUseDiscountTextViewReportBusinessCommissionFragment.setText(Utility.GetIntegerNumberWithComma(ViewModel.getAllCustomersInUse()));
        }

        if (ViewModel.getAllPayedCommission() == null) {
            UnpaidCommissionTextViewReportBusinessCommissionFragment.setText(Context.getResources().getString(R.string.zero));
        } else {
            UnpaidCommissionTextViewReportBusinessCommissionFragment.setText(Utility.GetIntegerNumberWithComma(ViewModel.getAllPayedCommission()));
        }

        if (ViewModel.getAllUnPayedCommission() == null) {
            UnpaidCommissionTextViewReportBusinessCommissionFragment.setText(Context.getResources().getString(R.string.zero));
        } else {
            UnpaidCommissionTextViewReportBusinessCommissionFragment.setText(Utility.GetIntegerNumberWithComma(ViewModel.getAllUnPayedCommission()));
        }

        if (ViewModel.getAllPayedCommission() == null) {
            PaidCommissionTextViewReportBusinessCommissionFragment.setText(Context.getResources().getString(R.string.zero));
        } else {
            PaidCommissionTextViewReportBusinessCommissionFragment.setText(Utility.GetIntegerNumberWithComma(ViewModel.getAllPayedCommission()));
        }

        if (ViewModel.getPriceUnPayedCommission() == null) {
            AmountCommissionUnPaidTextViewReportBusinessCommissionFragment.setText(Context.getResources().getString(R.string.zero) + " " + getResources().getString(R.string.toman));
        } else {
            AmountCommissionUnPaidTextViewReportBusinessCommissionFragment.setText(Utility.GetIntegerNumberWithComma(ViewModel.getPriceUnPayedCommission()) + " " + getResources().getString(R.string.toman));
        }

        if (ViewModel.getPricePayedCommission() == null) {
            AmountCommissionPaidTextViewReportBusinessCommissionFragment.setText(Context.getResources().getString(R.string.zero) + " " + getResources().getString(R.string.toman));
        } else {
            AmountCommissionPaidTextViewReportBusinessCommissionFragment.setText(Utility.GetIntegerNumberWithComma(ViewModel.getPricePayedCommission()) + " " + getResources().getString(R.string.toman));
        }


        double TotalNumberCustomer;
        double NumberCustomerUse;

        if (ViewModel.getAllSuggestedCustomers() == null) {
            TotalNumberCustomer = 1;
        } else if (ViewModel.getAllSuggestedCustomers() == 0) {
            TotalNumberCustomer = 1;
        } else {
            TotalNumberCustomer = ViewModel.getAllSuggestedCustomers();
        }

        if (ViewModel.getAllCustomersInUse() == null) {
            NumberCustomerUse = 0;
        } else {
            NumberCustomerUse = ViewModel.getAllCustomersInUse();
        }

        Double Percent = (NumberCustomerUse * 100) / TotalNumberCustomer;
        int PercentProgress = (int) Double.parseDouble(Percent.toString());

        PercentCustomerNumberTextViewReportBusinessCommissionFragment.setText(Utility.GetIntegerNumberWithComma(Percent) + " " + getResources().getString(R.string.percent));

        Drawable drawable = Context.getResources().getDrawable(R.drawable.circular_percent_progress_bar_yellow);
        PercentCustomerNumberProgressBarReportBusinessCommissionFragment.setProgress(PercentProgress);   // Main Progress
        PercentCustomerNumberProgressBarReportBusinessCommissionFragment.setSecondaryProgress(100); // Secondary Progress
        PercentCustomerNumberProgressBarReportBusinessCommissionFragment.setMax(100); // Maximum Progress
        PercentCustomerNumberProgressBarReportBusinessCommissionFragment.setProgressDrawable(drawable);

        double TotalAmount;
        double AmountCommissionPaid;
        double AmountCommissionUnPaid;

        if (ViewModel.getPriceUnPayedCommission() == null) {
            AmountCommissionUnPaid = 1;
        } else {
            AmountCommissionUnPaid = ViewModel.getPriceUnPayedCommission();
        }

        if (ViewModel.getPricePayedCommission() == null) {
            AmountCommissionPaid = 0;
        } else {
            AmountCommissionPaid = ViewModel.getPricePayedCommission();
        }

        TotalAmount = AmountCommissionUnPaid + AmountCommissionPaid;

        if (TotalAmount == 0) {
            TotalAmount = 1;
        }

        Double PercentAmountCommission = (AmountCommissionPaid * 100) / TotalAmount;
        int PercentProgressAmountCommission = (int) Double.parseDouble(PercentAmountCommission.toString());

        PercentAmountCommissionTextViewReportBusinessCommissionFragment.setText(Utility.GetIntegerNumberWithComma(PercentAmountCommission) + " " + getResources().getString(R.string.percent));

        PercentAmountCommissionProgressBarReportBusinessCommissionFragment.setProgress(PercentProgressAmountCommission);   // Main Progress
        PercentAmountCommissionProgressBarReportBusinessCommissionFragment.setSecondaryProgress(100); // Secondary Progress
        PercentAmountCommissionProgressBarReportBusinessCommissionFragment.setMax(100); // Maximum Progress
        PercentAmountCommissionProgressBarReportBusinessCommissionFragment.setProgressDrawable(drawable);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (Context != null) {
                //برای فهمیدن کد فرگنت به BusinessCommissionPagerAdapter مراجعه کنید
                Context.setFragmentIndex(4);
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

}
