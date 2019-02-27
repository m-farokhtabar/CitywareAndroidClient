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

import ir.rayas.app.citywareclient.Adapter.RecyclerView.BusinessContactRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Business.BusinessContactService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.View.UserProfileChildren.BusinessContactSetActivity;
import ir.rayas.app.citywareclient.View.UserProfileChildren.BusinessSetActivity;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessContactViewModel;

public class BusinessContactFragment extends Fragment implements IResponseService, ILoadData {

    private BusinessSetActivity Context = null;

    private RecyclerView BusinessContactRecyclerViewBusinessContactFragment = null;
    private SwipeRefreshLayout RefreshBusinessContactSwipeRefreshLayoutBusinessContactFragment;
    private boolean IsSwipe = false;
    private boolean IsLoadedDataForFirst = false;
    private BusinessContactRecyclerViewAdapter businessContactRecyclerViewAdapter = null;

    public BusinessContactRecyclerViewAdapter getBusinessContactRecyclerViewAdapter() {
        return businessContactRecyclerViewAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (BusinessSetActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_business_contact, container, false);
        //طرحبندی ویو
        CreateLayout(CurrentView);

        LoadData();

        return CurrentView;
    }


    private void CreateLayout(View CurrentView) {
        RefreshBusinessContactSwipeRefreshLayoutBusinessContactFragment = CurrentView.findViewById(R.id.RefreshBusinessContactSwipeRefreshLayoutBusinessContactFragment);
        BusinessContactRecyclerViewBusinessContactFragment = CurrentView.findViewById(R.id.BusinessContactRecyclerViewBusinessContactFragment);
        BusinessContactRecyclerViewBusinessContactFragment.setHasFixedSize(true);
        //به دلیل اینکه من در هر سطر یک گزینه نیاز دارم
        LinearLayoutManager BusinessLinearLayoutManager = new LinearLayoutManager(Context);
        BusinessContactRecyclerViewBusinessContactFragment.setLayoutManager(BusinessLinearLayoutManager);

        FloatingActionButton NewBusinessContactFloatingActionButtonBusinessContactFragment = CurrentView.findViewById(R.id.NewBusinessContactFloatingActionButtonBusinessContactFragment);
        NewBusinessContactFloatingActionButtonBusinessContactFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent NewBusinessIntent = Context.NewIntent(BusinessContactSetActivity.class);
                NewBusinessIntent.putExtra("SetBusinessContact", "New");
                NewBusinessIntent.putExtra("BusinessId",Context.getBusinessId() );
                Context.startActivity(NewBusinessIntent);
            }
        });

        RefreshBusinessContactSwipeRefreshLayoutBusinessContactFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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

        BusinessContactService businessContactService = new BusinessContactService(this);
        //دریافت اطلاعات
        Context.setRetryType(2);
        businessContactService.GetAll(Context.getBusinessId());
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        RefreshBusinessContactSwipeRefreshLayoutBusinessContactFragment.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.BusinessContactGetAll) {
                Feedback<List<BusinessContactViewModel>> FeedBack = (Feedback<List<BusinessContactViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    List<BusinessContactViewModel> ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        //تنظیمات مربوط به recycle کسب و کار
                        businessContactRecyclerViewAdapter = new BusinessContactRecyclerViewAdapter(Context, ViewModel, BusinessContactRecyclerViewBusinessContactFragment);
                        BusinessContactRecyclerViewBusinessContactFragment.setAdapter(businessContactRecyclerViewAdapter);
                        businessContactRecyclerViewAdapter.notifyDataSetChanged();
                        BusinessContactRecyclerViewBusinessContactFragment.invalidate();
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
            Context.setFragmentIndex(1);
            if (!IsLoadedDataForFirst) {
                IsLoadedDataForFirst = true;
                //دریافت اطلاعات از سرور
                LoadData();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}
