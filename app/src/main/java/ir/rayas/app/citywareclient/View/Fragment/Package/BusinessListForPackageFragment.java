package ir.rayas.app.citywareclient.View.Fragment.Package;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.BusinessListForPackageRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.MyClickListener;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Business.BusinessService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Fragment.Basket.BasketItemListFragment;
import ir.rayas.app.citywareclient.View.UserProfileChildren.PackageActivity;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessListForPackageFragment extends Fragment implements IResponseService {

    private PackageActivity Context = null;

    private TextViewPersian ShowEmptyBusinessListTextViewBusinessListForPackageFragment = null;
    private RecyclerView BusinessListRecyclerViewBusinessListForPackageFragment = null;
    private SwipeRefreshLayout RefreshBusinessListSwipeRefreshLayoutBusinessListForPackageFragment;

    private boolean IsSwipe = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (PackageActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_business_list_for_package, container, false);
        //طرحبندی ویو
        CreateLayout(CurrentView);

        LoadData();

        return CurrentView;
    }

    private void CreateLayout(View CurrentView) {
        ShowEmptyBusinessListTextViewBusinessListForPackageFragment = CurrentView.findViewById(R.id.ShowEmptyBusinessListTextViewBusinessListForPackageFragment);
        RefreshBusinessListSwipeRefreshLayoutBusinessListForPackageFragment = CurrentView.findViewById(R.id.RefreshBusinessListSwipeRefreshLayoutBusinessListForPackageFragment);
        BusinessListRecyclerViewBusinessListForPackageFragment = CurrentView.findViewById(R.id.BusinessListRecyclerViewBusinessListForPackageFragment);
        BusinessListRecyclerViewBusinessListForPackageFragment.setHasFixedSize(true);
        //به دلیل اینکه من در هر سطر یک گزینه نیاز دارم
        LinearLayoutManager RegionLinearLayoutManager = new LinearLayoutManager(Context);
        BusinessListRecyclerViewBusinessListForPackageFragment.setLayoutManager(RegionLinearLayoutManager);

        ShowEmptyBusinessListTextViewBusinessListForPackageFragment.setVisibility(View.GONE);

        RefreshBusinessListSwipeRefreshLayoutBusinessListForPackageFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        businessService.GetAll();

    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        RefreshBusinessListSwipeRefreshLayoutBusinessListForPackageFragment.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.UserGetAllBusiness) {
                Feedback<List<BusinessViewModel>> FeedBack = (Feedback<List<BusinessViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    final List<BusinessViewModel> ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {

                        ShowEmptyBusinessListTextViewBusinessListForPackageFragment.setVisibility(View.GONE);

                        //تنظیمات مربوط به recycle کسب و کار
                        BusinessListForPackageRecyclerViewAdapter businessListForPackageRecyclerViewAdapter = new BusinessListForPackageRecyclerViewAdapter(Context, ViewModel);
                        BusinessListRecyclerViewBusinessListForPackageFragment.setAdapter(businessListForPackageRecyclerViewAdapter);
                        businessListForPackageRecyclerViewAdapter.notifyDataSetChanged();
                        BusinessListRecyclerViewBusinessListForPackageFragment.invalidate();

                        businessListForPackageRecyclerViewAdapter.setOnItemClickListener(new MyClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {

                                BasketItemListFragment basketItemListFragment = new BasketItemListFragment();
                                FragmentTransaction BasketListTransaction = Context.getSupportFragmentManager().beginTransaction();
                                BasketListTransaction.replace(R.id.BasketFrameLayoutBasketActivity, basketItemListFragment);
                                BasketListTransaction.addToBackStack(null);
                                BasketListTransaction.commit();
                            }
                        });
                    } else {
                        ShowEmptyBusinessListTextViewBusinessListForPackageFragment.setVisibility(View.VISIBLE);
                    }

                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    ShowEmptyBusinessListTextViewBusinessListForPackageFragment.setVisibility(View.VISIBLE);
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


}
