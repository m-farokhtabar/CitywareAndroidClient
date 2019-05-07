package ir.rayas.app.citywareclient.View.Fragment.Package;


import android.annotation.SuppressLint;
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
import ir.rayas.app.citywareclient.Adapter.RecyclerView.PackageListRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.MyClickListener;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Business.BusinessService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Package.PackageService;
import ir.rayas.app.citywareclient.Service.User.PointService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Fragment.Basket.BasketItemListFragment;
import ir.rayas.app.citywareclient.View.UserProfileChildren.PackageActivity;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessViewModel;
import ir.rayas.app.citywareclient.ViewModel.Package.OutPackageViewModel;


public class PackageListFragment extends Fragment implements IResponseService {

    private PackageActivity Context = null;

    private TextViewPersian UserPointTextViewPackageListFragment = null;
    private TextViewPersian ShowEmptyPackageListTextViewPackageListFragment = null;
    private RecyclerView PackageListRecyclerViewPackageListFragment = null;
    private SwipeRefreshLayout RefreshPackageListSwipeRefreshLayoutPackageListFragment;

    private boolean IsSwipe = false;

    private int BusinessId = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (PackageActivity) getActivity();

        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_package_list, container, false);

        BusinessId = getArguments().getInt("BusinessId");

        CreateLayout(CurrentView);

        LoadData();

        return CurrentView;
    }

    private void CreateLayout(View CurrentView) {
        UserPointTextViewPackageListFragment = CurrentView.findViewById(R.id.UserPointTextViewPackageListFragment);
        ShowEmptyPackageListTextViewPackageListFragment = CurrentView.findViewById(R.id.ShowEmptyPackageListTextViewPackageListFragment);
        RefreshPackageListSwipeRefreshLayoutPackageListFragment = CurrentView.findViewById(R.id.RefreshPackageListSwipeRefreshLayoutPackageListFragment);
        PackageListRecyclerViewPackageListFragment = CurrentView.findViewById(R.id.PackageListRecyclerViewPackageListFragment);
        PackageListRecyclerViewPackageListFragment.setHasFixedSize(true);
        //به دلیل اینکه من در هر سطر یک گزینه نیاز دارم
        LinearLayoutManager RegionLinearLayoutManager = new LinearLayoutManager(Context);
        PackageListRecyclerViewPackageListFragment.setLayoutManager(RegionLinearLayoutManager);

        ShowEmptyPackageListTextViewPackageListFragment.setVisibility(View.GONE);

        RefreshPackageListSwipeRefreshLayoutPackageListFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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

        PointService pointService = new PointService(this);
        pointService.Get();

        PackageService packageService = new PackageService(this);
        packageService.GetAllPackageList(BusinessId);


    }

    @SuppressLint("SetTextI18n")
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {

        RefreshPackageListSwipeRefreshLayoutPackageListFragment.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.PackageListGetAll) {
                Context.HideLoading();
                Feedback<List<OutPackageViewModel>> FeedBack = (Feedback<List<OutPackageViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    final List<OutPackageViewModel> ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {

                        ShowEmptyPackageListTextViewPackageListFragment.setVisibility(View.GONE);

                        //تنظیمات مربوط به recycle کسب و کار
                        PackageListRecyclerViewAdapter packageListRecyclerViewAdapter = new PackageListRecyclerViewAdapter(Context, ViewModel);
                        PackageListRecyclerViewPackageListFragment.setAdapter(packageListRecyclerViewAdapter);
                        packageListRecyclerViewAdapter.notifyDataSetChanged();
                        PackageListRecyclerViewPackageListFragment.invalidate();

                        packageListRecyclerViewAdapter.setOnItemClickListener(new MyClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {

                                Bundle PackageIdBundle = new Bundle();
                                PackageIdBundle.putInt("PackageId",ViewModel.get(position).getId());
                                PackageDetailsFragment packageDetailsFragment = new PackageDetailsFragment();
                                packageDetailsFragment.setArguments(PackageIdBundle);

                                FragmentTransaction BasketListTransaction = Context.getSupportFragmentManager().beginTransaction();
                                BasketListTransaction.replace(R.id.PackageFrameLayoutPackageActivity, packageDetailsFragment);
                                BasketListTransaction.addToBackStack(null);
                                BasketListTransaction.commit();
                            }
                        });
                    } else {
                        ShowEmptyPackageListTextViewPackageListFragment.setVisibility(View.VISIBLE);
                    }

                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    ShowEmptyPackageListTextViewPackageListFragment.setVisibility(View.VISIBLE);
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.UserPointGet) {
                Feedback<Double> FeedBack = (Feedback<Double>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    Double DeliveryPrice = FeedBack.getValue();
                    if (DeliveryPrice != null) {

                        UserPointTextViewPackageListFragment.setText(String.valueOf((int) Math.round(DeliveryPrice)));
                    } else {
                        UserPointTextViewPackageListFragment.setText( Context.getResources().getString(R.string.zero));

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


}
