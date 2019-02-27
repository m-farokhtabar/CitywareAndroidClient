package ir.rayas.app.citywareclient.View.Fragment.Business;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.BusinessOpenTimeRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Business.BusinessOpenTimeService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.View.UserProfileChildren.BusinessOpenTimeSetActivity;
import ir.rayas.app.citywareclient.View.UserProfileChildren.BusinessSetActivity;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessOpenTimeViewModel;

public class BusinessOpenTimeFragment extends Fragment implements IResponseService, ILoadData {

    private BusinessSetActivity Context = null;

    private RecyclerView BusinessOpenTimeRecyclerViewBusinessOpenTimeFragment = null;
    private SwipeRefreshLayout RefreshBusinessOpenTimeSwipeRefreshLayoutBusinessOpenTimeFragment = null;
    private boolean IsSwipe = false;
    private boolean IsLoadedDataForFirst = false;
    private BusinessOpenTimeRecyclerViewAdapter businessOpenTimeRecyclerViewAdapter = null;

    public BusinessOpenTimeRecyclerViewAdapter getBusinessOpenTimeRecyclerViewAdapter() {
        return businessOpenTimeRecyclerViewAdapter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (BusinessSetActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_business_open_time, container, false);
        //طرحبندی ویو
        CreateLayout(CurrentView);

        LoadData();

        return CurrentView;
    }

    private void CreateLayout(View CurrentView) {
        RefreshBusinessOpenTimeSwipeRefreshLayoutBusinessOpenTimeFragment = CurrentView.findViewById(R.id.RefreshBusinessOpenTimeSwipeRefreshLayoutBusinessOpenTimeFragment);
        BusinessOpenTimeRecyclerViewBusinessOpenTimeFragment = CurrentView.findViewById(R.id.BusinessOpenTimeRecyclerViewBusinessOpenTimeFragment);
        BusinessOpenTimeRecyclerViewBusinessOpenTimeFragment.setHasFixedSize(true);

        //به دلیل اینکه من در هر سطر یک گزینه نیاز دارم
        LinearLayoutManager BusinessOpenTimeLinearLayoutManager = new LinearLayoutManager(Context);
        BusinessOpenTimeRecyclerViewBusinessOpenTimeFragment.setLayoutManager(BusinessOpenTimeLinearLayoutManager);

        FloatingActionButton NewBusinessOpenTimeFloatingActionButtonBusinessOpenTimeFragment = CurrentView.findViewById(R.id.NewBusinessOpenTimeFloatingActionButtonBusinessOpenTimeFragment);
        NewBusinessOpenTimeFloatingActionButtonBusinessOpenTimeFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent NewBusinessOpenTimeIntent =  Context.NewIntent(BusinessOpenTimeSetActivity.class);
                NewBusinessOpenTimeIntent.putExtra("SetBusinessOpenTime", "New");
                NewBusinessOpenTimeIntent.putExtra("BusinessId",Context.getBusinessId() );
                Context.startActivity(NewBusinessOpenTimeIntent);
            }
        });

        RefreshBusinessOpenTimeSwipeRefreshLayoutBusinessOpenTimeFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                IsSwipe = true;
                LoadData();

            }
        });
    }

    public void LoadData() {
        if (!IsSwipe)
            Context.ShowLoadingProgressBar();

        BusinessOpenTimeService businessOpenTimeService = new BusinessOpenTimeService(this);
        //دریافت اطلاعات
        Context.setRetryType(2);
        businessOpenTimeService.GetAll(Context.getBusinessId());
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        RefreshBusinessOpenTimeSwipeRefreshLayoutBusinessOpenTimeFragment.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.BusinessOpenTimeGetAll) {
                Feedback<List<BusinessOpenTimeViewModel>> FeedBack = (Feedback<List<BusinessOpenTimeViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    List<BusinessOpenTimeViewModel> ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        //تنظیمات مربوط به recycle کسب و کار
                        businessOpenTimeRecyclerViewAdapter = new BusinessOpenTimeRecyclerViewAdapter(Context,R.layout.recycler_view_open_time, ViewModel, BusinessOpenTimeRecyclerViewBusinessOpenTimeFragment);
                        BusinessOpenTimeRecyclerViewBusinessOpenTimeFragment.setAdapter(businessOpenTimeRecyclerViewAdapter);
                        businessOpenTimeRecyclerViewAdapter.notifyDataSetChanged();
                        BusinessOpenTimeRecyclerViewBusinessOpenTimeFragment.invalidate();
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


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            //برای فهمیدن کد فرگنت به UserProfilePagerAdapter مراجعه کنید
            Context.setFragmentIndex(0);
            if (!IsLoadedDataForFirst) {
                IsLoadedDataForFirst = true;
                //دریافت اطلاعات از سرور
                LoadData();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}
