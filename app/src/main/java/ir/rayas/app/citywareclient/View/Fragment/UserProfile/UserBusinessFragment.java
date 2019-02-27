package ir.rayas.app.citywareclient.View.Fragment.UserProfile;

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

import ir.rayas.app.citywareclient.Adapter.RecyclerView.BusinessListRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Business.BusinessService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.View.UserProfileChildren.BusinessSetActivity;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.Master.UserProfileActivity;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;

public class UserBusinessFragment extends Fragment implements IResponseService, ILoadData {

    private UserProfileActivity Context = null;
    private RecyclerView BusinessRecyclerViewUserBusinessFragment = null;
    private SwipeRefreshLayout RefreshBusinessSwipeRefreshLayoutUserBusinessFragment;
    private boolean IsSwipe = false;
    private boolean IsLoadedDataForFirst = false;
    private BusinessListRecyclerViewAdapter businessListRecyclerViewAdapter = null;

    public BusinessListRecyclerViewAdapter getBusinessListRecyclerViewAdapter() {
        return businessListRecyclerViewAdapter;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (UserProfileActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_user_business, container, false);
        //طرحبندی ویو
        CreateLayout(CurrentView);

        return CurrentView;
    }

    private void CreateLayout(View CurrentView) {
        RefreshBusinessSwipeRefreshLayoutUserBusinessFragment = CurrentView.findViewById(R.id.RefreshBusinessSwipeRefreshLayoutUserBusinessFragment);
        BusinessRecyclerViewUserBusinessFragment = CurrentView.findViewById(R.id.BusinessRecyclerViewUserBusinessFragment);
        BusinessRecyclerViewUserBusinessFragment.setHasFixedSize(true);
        //به دلیل اینکه من در هر سطر یک گزینه نیاز دارم
        LinearLayoutManager BusinessLinearLayoutManager = new LinearLayoutManager(Context);
        BusinessRecyclerViewUserBusinessFragment.setLayoutManager(BusinessLinearLayoutManager);

        FloatingActionButton NewBusinessFloatingActionButtonUserBusinessFragment = CurrentView.findViewById(R.id.NewBusinessFloatingActionButtonUserBusinessFragment);
        NewBusinessFloatingActionButtonUserBusinessFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent NewBusinessIntent = Context.NewIntent(BusinessSetActivity.class);
                NewBusinessIntent.putExtra("SetBusiness", "New");
                NewBusinessIntent.putExtra("BusinessId", 0);
                //1 Add - you  can find edit in UserAddressRecyclerViewAdapter
                Context.startActivity(NewBusinessIntent);
            }
        });

        RefreshBusinessSwipeRefreshLayoutUserBusinessFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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

        BusinessService businessService = new BusinessService(this);
        AccountViewModel ViewModel = Context.getAccountRepository().getAccount();
        if (ViewModel != null) {
            //دریافت اطلاعات
            Context.setRetryType(2);
            businessService.GetAll();
        }
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        RefreshBusinessSwipeRefreshLayoutUserBusinessFragment.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.UserGetAllBusiness) {
                Feedback<List<BusinessViewModel>> FeedBack = (Feedback<List<BusinessViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    List<BusinessViewModel> ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        //تنظیمات مربوط به recycle کسب و کار
                        businessListRecyclerViewAdapter = new BusinessListRecyclerViewAdapter(Context, ViewModel, BusinessRecyclerViewUserBusinessFragment);
                        BusinessRecyclerViewUserBusinessFragment.setAdapter(businessListRecyclerViewAdapter);
                        businessListRecyclerViewAdapter.notifyDataSetChanged();
                        BusinessRecyclerViewUserBusinessFragment.invalidate();
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
