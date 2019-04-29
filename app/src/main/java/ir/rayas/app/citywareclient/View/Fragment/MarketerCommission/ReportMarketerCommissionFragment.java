package ir.rayas.app.citywareclient.View.Fragment.MarketerCommission;


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
import ir.rayas.app.citywareclient.View.MasterChildren.ShowMarketerCommissionDetailsActivity;
import ir.rayas.app.citywareclient.ViewModel.Marketing.MarketerCommissionInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportMarketerCommissionFragment extends Fragment implements IResponseService, ILoadData {

    private ShowMarketerCommissionDetailsActivity Context = null;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //دریافت اکتیوتی والد این فرگمین
        Context = (ShowMarketerCommissionDetailsActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_report_marketer_commission, container, false);

        //طرحبندی ویو
        CreateLayout(CurrentView);
        //برای فهمیدن کد فرگنت به BusinessCommissionPagerAdapter مراجعه کنید
        Context.setFragmentIndex(4);

        //دریافت اطلاعات از سرور
        LoadData();

        return CurrentView;

    }

    public void LoadData() {
        Context.ShowLoadingProgressBar();

        Context.setRetryType(2);
        MarketingService marketingService = new MarketingService(this);
        marketingService.GetMarketerCommission();
    }


    private void CreateLayout(View CurrentView) {

        NumberCustomerUseTextViewShowMarketerCommissionDetailsActivity = CurrentView.findViewById(R.id.NumberCustomerUseTextViewShowMarketerCommissionDetailsActivity);
        TotalNumberCustomerTextViewShowMarketerCommissionDetailsActivity = CurrentView.findViewById(R.id.TotalNumberCustomerTextViewShowMarketerCommissionDetailsActivity);
        PercentCustomerNumberTextViewShowMarketerCommissionDetailsActivity = CurrentView.findViewById(R.id.PercentCustomerNumberTextViewShowMarketerCommissionDetailsActivity);
        PercentCustomerNumberProgressBarShowMarketerCommissionDetailsActivity = CurrentView.findViewById(R.id.PercentCustomerNumberProgressBarShowMarketerCommissionDetailsActivity);
        TotalAmountCommissionTextViewShowMarketerCommissionDetailsActivity = CurrentView.findViewById(R.id.TotalAmountCommissionTextViewShowMarketerCommissionDetailsActivity);
        AmountCommissionPaidTextViewShowMarketerCommissionDetailsActivity = CurrentView.findViewById(R.id.AmountCommissionPaidTextViewShowMarketerCommissionDetailsActivity);
        RemainingAmountCommissionTextViewShowMarketerCommissionDetailsActivity = CurrentView.findViewById(R.id.RemainingAmountCommissionTextViewShowMarketerCommissionDetailsActivity);
        PercentAmountCommissionProgressBarShowMarketerCommissionDetailsActivity = CurrentView.findViewById(R.id.PercentAmountCommissionProgressBarShowMarketerCommissionDetailsActivity);
        PercentAmountCommissionTextViewShowMarketerCommissionDetailsActivity = CurrentView.findViewById(R.id.PercentAmountCommissionTextViewShowMarketerCommissionDetailsActivity);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
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


        if (Percent == null) {
            Percent = 0.0;
        }

        PercentCustomerNumberTextViewShowMarketerCommissionDetailsActivity.setText(Utility.GetIntegerNumberWithComma(Percent) + " " + getResources().getString(R.string.percent));

        Drawable drawable = Context.getResources().getDrawable(R.drawable.circular_percent_progress_bar_yellow);
        PercentCustomerNumberProgressBarShowMarketerCommissionDetailsActivity.setProgress(PercentProgress);   // Main Progress
        PercentCustomerNumberProgressBarShowMarketerCommissionDetailsActivity.setSecondaryProgress(100); // Secondary Progress
        PercentCustomerNumberProgressBarShowMarketerCommissionDetailsActivity.setMax(100); // Maximum Progress
        PercentCustomerNumberProgressBarShowMarketerCommissionDetailsActivity.setProgressDrawable(drawable);


        double TotalAmount = ViewModel.getAllMarketerCommission();
        double AmountCommissionPaid = ViewModel.getAllReceivedMarketerCommission();
        Double PercentAmountCommission = (AmountCommissionPaid * 100) / TotalAmount;
        int PercentProgressAmountCommission = (int) Double.parseDouble(PercentAmountCommission.toString());

        if (PercentAmountCommission == null) {
            PercentAmountCommission = 0.0;
        }

        PercentAmountCommissionTextViewShowMarketerCommissionDetailsActivity.setText(Utility.GetIntegerNumberWithComma(PercentAmountCommission) + " " + getResources().getString(R.string.percent));

        Drawable drawablePercentAmountCommission = Context.getResources().getDrawable(R.drawable.circular_percent_progress_bar_yellow);
        PercentAmountCommissionProgressBarShowMarketerCommissionDetailsActivity.setProgress(PercentProgressAmountCommission);   // Main Progress
        PercentAmountCommissionProgressBarShowMarketerCommissionDetailsActivity.setSecondaryProgress(100); // Secondary Progress
        PercentAmountCommissionProgressBarShowMarketerCommissionDetailsActivity.setMax(100); // Maximum Progress
        PercentAmountCommissionProgressBarShowMarketerCommissionDetailsActivity.setProgressDrawable(drawablePercentAmountCommission);

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

