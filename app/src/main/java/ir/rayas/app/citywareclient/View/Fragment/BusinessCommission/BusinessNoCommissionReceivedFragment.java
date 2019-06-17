package ir.rayas.app.citywareclient.View.Fragment.BusinessCommission;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.BusinessNoCommissionReceivedRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Marketing.MarketingService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowBusinessCommissionActivity;
import ir.rayas.app.citywareclient.ViewModel.Marketing.MarketingPayedBusinessViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessNoCommissionReceivedFragment extends Fragment implements IResponseService, ILoadData {

    private ShowBusinessCommissionActivity Context = null;

    private SwipeRefreshLayout RefreshNoCommissionReceivedSwipeRefreshLayoutBusinessNoCommissionReceivedFragment = null;
    private TextViewPersian ShowEmptyNoCommissionReceivedTextViewBusinessNoCommissionReceivedFragment = null;
    public TextViewPersian TotalPriceFactoreTextViewBusinessNoCommissionReceivedFragment = null;
    public LinearLayout TotalLinearLayoutBusinessNoCommissionReceivedFragment = null;
    private BusinessNoCommissionReceivedRecyclerViewAdapter noCommissionReceivedRecyclerViewAdapter = null;

    private boolean IsSwipe = false;
    private boolean IsLoadedDataForFirst = false;
    private int BusinessId = 0;
    private int PageNumber = 1;
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (ShowBusinessCommissionActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_business_no_commission_received, container, false);

//        BusinessId = Context.getIntent().getExtras().getInt("BusinessId");
        BusinessId = Context.getBusinessId();
        //طرحبندی ویو
        CreateLayout(CurrentView);

        return CurrentView;
    }

    private void CreateLayout(View CurrentView) {
        ShowEmptyNoCommissionReceivedTextViewBusinessNoCommissionReceivedFragment = CurrentView.findViewById(R.id.ShowEmptyNoCommissionReceivedTextViewBusinessNoCommissionReceivedFragment);
        RefreshNoCommissionReceivedSwipeRefreshLayoutBusinessNoCommissionReceivedFragment = CurrentView.findViewById(R.id.RefreshNoCommissionReceivedSwipeRefreshLayoutBusinessNoCommissionReceivedFragment);
        TotalPriceFactoreTextViewBusinessNoCommissionReceivedFragment = CurrentView.findViewById(R.id.TotalPriceFactoreTextViewBusinessNoCommissionReceivedFragment);
        TotalLinearLayoutBusinessNoCommissionReceivedFragment = CurrentView.findViewById(R.id.TotalLinearLayoutBusinessNoCommissionReceivedFragment);
        ButtonPersianView SubmitButtonBusinessNoCommissionReceivedFragment = CurrentView.findViewById(R.id.SubmitButtonBusinessNoCommissionReceivedFragment);

        ShowEmptyNoCommissionReceivedTextViewBusinessNoCommissionReceivedFragment.setVisibility(View.GONE);
        TotalLinearLayoutBusinessNoCommissionReceivedFragment.setVisibility(View.GONE);

        RecyclerView NoCommissionReceivedRecyclerViewBusinessNoCommissionReceivedFragment = CurrentView.findViewById(R.id.NoCommissionReceivedRecyclerViewBusinessNoCommissionReceivedFragment);
        NoCommissionReceivedRecyclerViewBusinessNoCommissionReceivedFragment.setHasFixedSize(true);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(Context);
        NoCommissionReceivedRecyclerViewBusinessNoCommissionReceivedFragment.setLayoutManager(LinearLayoutManager);

        noCommissionReceivedRecyclerViewAdapter = new BusinessNoCommissionReceivedRecyclerViewAdapter(Context, null, NoCommissionReceivedRecyclerViewBusinessNoCommissionReceivedFragment);
        NoCommissionReceivedRecyclerViewBusinessNoCommissionReceivedFragment.setAdapter(noCommissionReceivedRecyclerViewAdapter);


        RefreshNoCommissionReceivedSwipeRefreshLayoutBusinessNoCommissionReceivedFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                IsSwipe = true;
                PageNumber = 1;
                LoadData();
            }
        });

        SubmitButtonBusinessNoCommissionReceivedFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    /**
     * دریافت اطلاعات نحوای جهت پر کردن Recycle
     */
    public void LoadData() {
        if (!IsSwipe)
            if (PageNumber == 1)
                Context.ShowLoadingProgressBar();

        Context.setRetryType(2);
        MarketingService MarketingService = new MarketingService(this);
        MarketingService.GetAllNotPayedBusinessCommission(BusinessId, PageNumber);
    }

    /**
     * @param Data
     * @param ServiceMethod
     * @param <T>
     */
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        RefreshNoCommissionReceivedSwipeRefreshLayoutBusinessNoCommissionReceivedFragment.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.NotPayedBusinessCommissionGetAll) {
                Context.HideLoading();
                Feedback<List<MarketingPayedBusinessViewModel>> FeedBack = (Feedback<List<MarketingPayedBusinessViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<MarketingPayedBusinessViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        if (PageNumber == 1) {
                            if (ViewModelList.size() < 1) {
                                ShowEmptyNoCommissionReceivedTextViewBusinessNoCommissionReceivedFragment.setVisibility(View.VISIBLE);
                            } else {
                                ShowEmptyNoCommissionReceivedTextViewBusinessNoCommissionReceivedFragment.setVisibility(View.GONE);
                                noCommissionReceivedRecyclerViewAdapter.SetViewModelList(ViewModelList);

                                if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                    PageNumber = PageNumber + 1;
                                    LoadData();
                                }
                            }

                        } else {
                            ShowEmptyNoCommissionReceivedTextViewBusinessNoCommissionReceivedFragment.setVisibility(View.GONE);
                            noCommissionReceivedRecyclerViewAdapter.AddViewModelList(ViewModelList);

                            if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                PageNumber = PageNumber + 1;
                                LoadData();
                            }
                        }
                    }
                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    if (PageNumber > 1) {
                        ShowEmptyNoCommissionReceivedTextViewBusinessNoCommissionReceivedFragment.setVisibility(View.GONE);
                    } else {
                        ShowEmptyNoCommissionReceivedTextViewBusinessNoCommissionReceivedFragment.setVisibility(View.VISIBLE);
                    }
                } else {
                    ShowEmptyNoCommissionReceivedTextViewBusinessNoCommissionReceivedFragment.setVisibility(View.GONE);
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            }
        } catch (Exception e) {
            Context.ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
    }

    @SuppressLint("SetTextI18n")
    public void SetViewPriceInFooter(double TotalPrice) {
        TotalPriceFactoreTextViewBusinessNoCommissionReceivedFragment.setText(Utility.GetIntegerNumberWithComma(TotalPrice)+ " " + Context.getResources().getString(R.string.toman));

        if (TotalPrice <= 0)
            TotalLinearLayoutBusinessNoCommissionReceivedFragment.setVisibility(View.GONE);
        else
            TotalLinearLayoutBusinessNoCommissionReceivedFragment.setVisibility(View.VISIBLE);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            //برای فهمیدن کد فرگنت به UserProfilePagerAdapter مراجعه کنید
            Context.setFragmentIndex(2);
            if (!IsLoadedDataForFirst) {
                IsSwipe = false;
                IsLoadedDataForFirst = true;
                //دریافت اطلاعات از سرور
                LoadData();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

}
