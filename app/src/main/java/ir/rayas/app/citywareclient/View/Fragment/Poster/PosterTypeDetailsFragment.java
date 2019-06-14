package ir.rayas.app.citywareclient.View.Fragment.Poster;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.UserProfileChildren.PosterTypeActivity;
import ir.rayas.app.citywareclient.ViewModel.Poster.PosterTypeViewModel;


public class PosterTypeDetailsFragment extends Fragment implements IResponseService, ILoadData {

    private TextViewPersian UserCreditTextViewPosterTypeDetailsActivity = null;
    private TextViewPersian PosterTypeTitleTextViewPosterTypeDetailsActivity = null;
    private TextViewPersian PosterTypeCostTextViewPosterTypeDetailsActivity = null;
    private TextViewPersian PosterTypePriorityTextViewPosterTypeDetailsActivity = null;
    private TextViewPersian DimensionPosterTypeTextViewPosterTypeDetailsActivity = null;
    private SwitchCompat PosterTypeAlwaysOnTopSwitchPosterTypeDetailsActivity = null;
    private LinearLayout PosterTypePriorityLinearLayoutPosterTypeDetailsActivity = null;
    private LinearLayout DimensionPosterTypeLinearLayoutPosterTypeDetailsActivity = null;
    private TextViewPersian DescriptionPosterTypeTitleTextViewPosterTypeDetailsActivity = null;
    private ButtonPersianView NextButtonPosterTypeDetailsFragment = null;

    private int PosterTypeId = 0;

    private PosterTypeActivity Context = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (PosterTypeActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_poster_type_details, container, false);

        PosterTypeId = getArguments().getInt("PosterTypeId");

        //طرحبندی ویو
        CreateLayout(CurrentView);

        LoadDataUserCredit();

        return CurrentView;
    }


    public void LoadDataUserCredit() {

        Context.ShowLoadingProgressBar();
        PackageService packageService = new PackageService(this);
        packageService.GetUserCredit();
    }

    /**
     * دریافت اطلاعات نحوای جهت پر کردن Recycle
     */
    public void LoadData() {

        PosterService PosterService = new PosterService(this);
        PosterService.GetPosterType(PosterTypeId);
    }

    /**
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    private void CreateLayout(View CurrentView) {
        UserCreditTextViewPosterTypeDetailsActivity = CurrentView.findViewById(R.id.UserCreditTextViewPosterTypeDetailsActivity);
        PosterTypeTitleTextViewPosterTypeDetailsActivity = CurrentView.findViewById(R.id.PosterTypeTitleTextViewPosterTypeDetailsActivity);
        PosterTypeCostTextViewPosterTypeDetailsActivity = CurrentView.findViewById(R.id.PosterTypeCostTextViewPosterTypeDetailsActivity);
        PosterTypePriorityLinearLayoutPosterTypeDetailsActivity = CurrentView.findViewById(R.id.PosterTypePriorityLinearLayoutPosterTypeDetailsActivity);
        PosterTypePriorityTextViewPosterTypeDetailsActivity = CurrentView.findViewById(R.id.PosterTypePriorityTextViewPosterTypeDetailsActivity);
        PosterTypeAlwaysOnTopSwitchPosterTypeDetailsActivity = CurrentView.findViewById(R.id.PosterTypeAlwaysOnTopSwitchPosterTypeDetailsActivity);
        DimensionPosterTypeLinearLayoutPosterTypeDetailsActivity = CurrentView.findViewById(R.id.DimensionPosterTypeLinearLayoutPosterTypeDetailsActivity);
        DimensionPosterTypeTextViewPosterTypeDetailsActivity = CurrentView.findViewById(R.id.DimensionPosterTypeTextViewPosterTypeDetailsActivity);
        DescriptionPosterTypeTitleTextViewPosterTypeDetailsActivity = CurrentView.findViewById(R.id.DescriptionPosterTypeTitleTextViewPosterTypeDetailsActivity);
        NextButtonPosterTypeDetailsFragment = CurrentView.findViewById(R.id.NextButtonPosterTypeDetailsFragment);
        ButtonPersianView ReturnButtonPosterTypeDetailsFragment = CurrentView.findViewById(R.id.ReturnButtonPosterTypeDetailsFragment);

        ReturnButtonPosterTypeDetailsFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
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
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.PosterTypeGet) {
                Context.HideLoading();
                Feedback<PosterTypeViewModel> FeedBack = (Feedback<PosterTypeViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    PosterTypeViewModel ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        SetInformationToView(ViewModelList);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        if (!(FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId())) {
                            Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                        }
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

        NextButtonPosterTypeDetailsFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context.setPosterTypeName( ViewModel.getName());
                Context.setPosterTypeId( PosterTypeId);
                Context.setPosterTypePrice( ViewModel.getPrice());

                FragmentTransaction BasketListTransaction = Context.getSupportFragmentManager().beginTransaction();
                BasketListTransaction.replace(R.id.PosterFrameLayoutPosterTypeActivity, new BuyPosterTypeFragment());
                BasketListTransaction.addToBackStack(null);
                BasketListTransaction.commit();

            }
        });

    }


}
