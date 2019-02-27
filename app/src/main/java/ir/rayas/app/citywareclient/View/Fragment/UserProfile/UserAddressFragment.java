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

import ir.rayas.app.citywareclient.Adapter.RecyclerView.UserAddressRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.User.UserAddressService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.UserProfileChildren.UserAddressSetActivity;
import ir.rayas.app.citywareclient.View.Master.UserProfileActivity;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.UserAddressViewModel;


public class UserAddressFragment extends Fragment implements IResponseService, ILoadData {

    private UserProfileActivity Context = null;
    private RecyclerView AddressRecyclerViewUserAddressFragment = null;
    private SwipeRefreshLayout RefreshAddressSwipeRefreshLayoutUserAddressFragment;
    private boolean IsSwipe = false;
    private boolean IsLoadedDataForFirst = false;
    private UserAddressRecyclerViewAdapter userAddressRecyclerViewAdapter = null;

    public UserAddressRecyclerViewAdapter getUserAddressRecyclerViewAdapter() {
        return userAddressRecyclerViewAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (UserProfileActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_user_address, container, false);
        //طرحبندی ویو
        CreateLayout(CurrentView);

        return CurrentView;
    }

    private void CreateLayout(View CurrentView) {
        RefreshAddressSwipeRefreshLayoutUserAddressFragment = CurrentView.findViewById(R.id.RefreshAddressSwipeRefreshLayoutUserAddressFragment);
        AddressRecyclerViewUserAddressFragment = CurrentView.findViewById(R.id.AddressRecyclerViewUserAddressFragment);
        AddressRecyclerViewUserAddressFragment.setHasFixedSize(true);
        //به دلیل اینکه من در هر سطر یک گزینه نیاز دارم
        LinearLayoutManager RegionLinearLayoutManager = new LinearLayoutManager(Context);
        AddressRecyclerViewUserAddressFragment.setLayoutManager(RegionLinearLayoutManager);

        FloatingActionButton NewAddressFloatingActionButtonUserAddressFragment = CurrentView.findViewById(R.id.NewAddressFloatingActionButtonUserAddressFragment);
        NewAddressFloatingActionButtonUserAddressFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent NewAddressIntent = Context.NewIntent(UserAddressSetActivity.class);
                NewAddressIntent.putExtra("SetAddress", "New");
                //1 Add - you  can find edit in UserAddressRecyclerViewAdapter
                Context.startActivity(NewAddressIntent);

            }
        });

        RefreshAddressSwipeRefreshLayoutUserAddressFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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

        UserAddressService userAddressService = new UserAddressService(this);
        AccountViewModel ViewModel = Context.getAccountRepository().getAccount();
        if (ViewModel != null) {
            //دریافت اطلاعات
            Context.setRetryType(2);
            userAddressService.GetAll();
        }
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        RefreshAddressSwipeRefreshLayoutUserAddressFragment.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.UserGetAllAddress) {
                Feedback<List<UserAddressViewModel>> FeedBack = (Feedback<List<UserAddressViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    List<UserAddressViewModel> ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        //تنظیمات مربوط به recycle آدرس
                        userAddressRecyclerViewAdapter = new UserAddressRecyclerViewAdapter(Context, ViewModel, AddressRecyclerViewUserAddressFragment);
                        AddressRecyclerViewUserAddressFragment.setAdapter(userAddressRecyclerViewAdapter);
                        userAddressRecyclerViewAdapter.notifyDataSetChanged();
                        AddressRecyclerViewUserAddressFragment.invalidate();
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
            Context.setFragmentIndex(4);
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
