package ir.rayas.app.citywareclient.View.Fragment.MarketerCommission;


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

import ir.rayas.app.citywareclient.Adapter.RecyclerView.NewSuggestionMarketerCommissionRecyclerViewAdapter;
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
import ir.rayas.app.citywareclient.View.MasterChildren.ShowMarketerCommissionDetailsActivity;
import ir.rayas.app.citywareclient.ViewModel.Marketing.MarketingBusinessManViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewSuggestionMarketerCommissionFragment extends Fragment  implements IResponseService, ILoadData {

    private ShowMarketerCommissionDetailsActivity Context = null;

    private boolean IsSwipe = false;
    private boolean IsLoadedDataForFirst = false;

    private RecyclerView NewSuggestionRecyclerViewNewSuggestionMarketerCommissionActivity = null;
    private SwipeRefreshLayout RefreshNewSuggestionSwipeRefreshLayoutNewSuggestionMarketerCommissionActivity = null;
    private TextViewPersian ShowEmptyNewSuggestionTextViewNewSuggestionMarketerCommissionActivity = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (ShowMarketerCommissionDetailsActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_new_suggestion_marketer_commission, container, false);

        //طرحبندی ویو
        CreateLayout(CurrentView);

        return CurrentView;

    }

    private void CreateLayout(View CurrentView) {

        ShowEmptyNewSuggestionTextViewNewSuggestionMarketerCommissionActivity = CurrentView.findViewById(R.id.ShowEmptyNewSuggestionTextViewNewSuggestionMarketerCommissionActivity);
        RefreshNewSuggestionSwipeRefreshLayoutNewSuggestionMarketerCommissionActivity = CurrentView.findViewById(R.id.RefreshNewSuggestionSwipeRefreshLayoutNewSuggestionMarketerCommissionActivity);
        ShowEmptyNewSuggestionTextViewNewSuggestionMarketerCommissionActivity.setVisibility(View.GONE);

        NewSuggestionRecyclerViewNewSuggestionMarketerCommissionActivity = CurrentView.findViewById(R.id.NewSuggestionRecyclerViewNewSuggestionMarketerCommissionActivity);
        NewSuggestionRecyclerViewNewSuggestionMarketerCommissionActivity.setHasFixedSize(true);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(Context);
        NewSuggestionRecyclerViewNewSuggestionMarketerCommissionActivity.setLayoutManager(LinearLayoutManager);


        RefreshNewSuggestionSwipeRefreshLayoutNewSuggestionMarketerCommissionActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        MarketingService MarketingService = new MarketingService(this);
        MarketingService.GetAllNewSuggestionMarketerCommission();
    }

    /**
     * @param Data
     * @param ServiceMethod
     * @param <T>
     */
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context. HideLoading();
        RefreshNewSuggestionSwipeRefreshLayoutNewSuggestionMarketerCommissionActivity.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.NewSuggestionMarketerCommissionGetAll) {
                Feedback<List<MarketingBusinessManViewModel>> FeedBack = (Feedback<List<MarketingBusinessManViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<MarketingBusinessManViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null && ViewModelList.size() > 0) {
                        ShowEmptyNewSuggestionTextViewNewSuggestionMarketerCommissionActivity.setVisibility(View.GONE);

                        NewSuggestionMarketerCommissionRecyclerViewAdapter newSuggestionMarketerCommissionRecyclerViewAdapter = new NewSuggestionMarketerCommissionRecyclerViewAdapter(Context,ViewModelList);
                        NewSuggestionRecyclerViewNewSuggestionMarketerCommissionActivity.setAdapter(newSuggestionMarketerCommissionRecyclerViewAdapter);

                    } else {
                        ShowEmptyNewSuggestionTextViewNewSuggestionMarketerCommissionActivity.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId())
                            ShowEmptyNewSuggestionTextViewNewSuggestionMarketerCommissionActivity.setVisibility(View.VISIBLE);
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
            Context.setFragmentIndex(0);
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

