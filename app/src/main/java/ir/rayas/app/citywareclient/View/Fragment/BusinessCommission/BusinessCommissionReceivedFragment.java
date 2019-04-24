package ir.rayas.app.citywareclient.View.Fragment.BusinessCommission;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.BusinessCommissionReceivedRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Marketing.MarketingService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowBusinessCommissionActivity;
import ir.rayas.app.citywareclient.ViewModel.Marketing.MarketingPayedBusinessViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessCommissionReceivedFragment extends Fragment implements IResponseService, ILoadData {

    private ShowBusinessCommissionActivity Context = null;

    private boolean IsSwipe = false;
    private boolean IsLoadedDataForFirst = false;
    private int BusinessId = 0;

    private RecyclerView CommissionReceivedRecyclerViewBusinessCommissionReceivedFragment = null;
    private SwipeRefreshLayout RefreshCommissionReceivedSwipeRefreshLayoutBusinessCommissionReceivedFragment = null;
    private TextViewPersian ShowEmptyCommissionReceivedTextViewBusinessCommissionReceivedFragment = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (ShowBusinessCommissionActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_business_commission_received, container, false);

        BusinessId = Context.getIntent().getExtras().getInt("BusinessId");
        //طرحبندی ویو
        CreateLayout(CurrentView);

        return CurrentView;
    }

    private void CreateLayout(View CurrentView) {

        ShowEmptyCommissionReceivedTextViewBusinessCommissionReceivedFragment = CurrentView.findViewById(R.id.ShowEmptyCommissionReceivedTextViewBusinessCommissionReceivedFragment);
        RefreshCommissionReceivedSwipeRefreshLayoutBusinessCommissionReceivedFragment = CurrentView.findViewById(R.id.RefreshCommissionReceivedSwipeRefreshLayoutBusinessCommissionReceivedFragment);
        ShowEmptyCommissionReceivedTextViewBusinessCommissionReceivedFragment.setVisibility(View.GONE);

        CommissionReceivedRecyclerViewBusinessCommissionReceivedFragment = CurrentView.findViewById(R.id.CommissionReceivedRecyclerViewBusinessCommissionReceivedFragment);
        CommissionReceivedRecyclerViewBusinessCommissionReceivedFragment.setHasFixedSize(true);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(Context);
        CommissionReceivedRecyclerViewBusinessCommissionReceivedFragment.setLayoutManager(LinearLayoutManager);


        RefreshCommissionReceivedSwipeRefreshLayoutBusinessCommissionReceivedFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                IsSwipe = true;
                LoadData();
            }
        });
    }

    /**
     * دریافت اطلاعات نحوای جهت پر کردن Recycle
     */
    public void LoadData() {
        if (!IsSwipe)
            Context.ShowLoadingProgressBar();

        Context.setRetryType(2);
        MarketingService MarketingService = new MarketingService(BusinessCommissionReceivedFragment.this);
        MarketingService.GetAllPayedBusinessCommission(BusinessId);
    }

    /**
     * @param Data
     * @param ServiceMethod
     * @param <T>
     */
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        RefreshCommissionReceivedSwipeRefreshLayoutBusinessCommissionReceivedFragment.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.PayedBusinessCommissionGetAll) {
                Feedback<List<MarketingPayedBusinessViewModel>> FeedBack = (Feedback<List<MarketingPayedBusinessViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<MarketingPayedBusinessViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null && ViewModelList.size() > 0) {
                        ShowEmptyCommissionReceivedTextViewBusinessCommissionReceivedFragment.setVisibility(View.GONE);

                        BusinessCommissionReceivedRecyclerViewAdapter CommissionReceivedRecyclerViewAdapter = new BusinessCommissionReceivedRecyclerViewAdapter(Context, ViewModelList);
                        CommissionReceivedRecyclerViewBusinessCommissionReceivedFragment.setAdapter(CommissionReceivedRecyclerViewAdapter);

                    } else {
                        ShowEmptyCommissionReceivedTextViewBusinessCommissionReceivedFragment.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId())
                            ShowEmptyCommissionReceivedTextViewBusinessCommissionReceivedFragment.setVisibility(View.VISIBLE);
                        else
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


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            //برای فهمیدن کد فرگنت به UserProfilePagerAdapter مراجعه کنید
            Context.setFragmentIndex(1);
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
