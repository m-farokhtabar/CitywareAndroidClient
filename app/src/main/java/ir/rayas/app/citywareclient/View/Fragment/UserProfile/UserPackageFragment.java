package ir.rayas.app.citywareclient.View.Fragment.UserProfile;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.ClosePackageRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.PackageRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Package.PackageService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.OnLoadMoreListener;
import ir.rayas.app.citywareclient.View.Master.UserProfileActivity;
import ir.rayas.app.citywareclient.View.UserProfileChildren.PackageActivity;
import ir.rayas.app.citywareclient.ViewModel.Package.OutputPackageTransactionViewModel;

public class UserPackageFragment extends Fragment implements IResponseService, ILoadData {

    private UserProfileActivity Context = null;
    private SwipeRefreshLayout RefreshPackageSwipeRefreshLayoutUserPackageFragment = null;
    private TextViewPersian UserCreditTextViewUserPackageFragment = null;
    private ProgressBar LoadMoreProgressBar = null;
    private boolean IsSwipe = false;
    private boolean IsLoadedDataForFirst = false;
    private PackageRecyclerViewAdapter packageRecyclerViewAdapter = null;
    private ClosePackageRecyclerViewAdapter closePackageRecyclerViewAdapter = null;
    private int PageNumberOpen = 1;
    private int PageNumberClose = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (UserProfileActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_user_packge, container, false);
        //طرحبندی ویو
        CreateLayout(CurrentView);

        return CurrentView;
    }


    private void CreateLayout(View CurrentView) {

        LoadMoreProgressBar = CurrentView.findViewById(R.id.LoadMoreProgressPackageFragment);
        RefreshPackageSwipeRefreshLayoutUserPackageFragment = CurrentView.findViewById(R.id.RefreshPackageSwipeRefreshLayoutUserPackageFragment);
        final RecyclerView PackageOpenRecyclerViewUserPackageFragment = CurrentView.findViewById(R.id.PackageOpenRecyclerViewUserPackageFragment);
        final RecyclerView PackageCloseRecyclerViewUserPackageFragment = CurrentView.findViewById(R.id.PackageCloseRecyclerViewUserPackageFragment);
        SwitchCompat ExpireAndValidatePackageSwitchUserPackageFragment = CurrentView.findViewById(R.id.ExpireAndValidatePackageSwitchUserPackageFragment);
        UserCreditTextViewUserPackageFragment = CurrentView.findViewById(R.id.UserCreditTextViewUserPackageFragment);
        final TextViewPersian ExpireAndValidatePackageTitleTextViewUserPackageFragment = CurrentView.findViewById(R.id.ExpireAndValidatePackageTitleTextViewUserPackageFragment);


        ExpireAndValidatePackageSwitchUserPackageFragment.setChecked(true);


        PackageOpenRecyclerViewUserPackageFragment.setLayoutManager(new LinearLayoutManager(Context));
        packageRecyclerViewAdapter = new PackageRecyclerViewAdapter(Context, null, PackageOpenRecyclerViewUserPackageFragment, new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                PageNumberOpen = PageNumberOpen + 1;
                LoadMoreProgressBar.setVisibility(View.VISIBLE);
                LoadData();
            }
        });
        PackageOpenRecyclerViewUserPackageFragment.setAdapter(packageRecyclerViewAdapter);


        PackageCloseRecyclerViewUserPackageFragment.setLayoutManager(new LinearLayoutManager(Context));
        closePackageRecyclerViewAdapter = new ClosePackageRecyclerViewAdapter(Context, null, PackageCloseRecyclerViewUserPackageFragment, new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                PageNumberClose = PageNumberClose + 1;
                LoadMoreProgressBar.setVisibility(View.VISIBLE);
                LoadData();
            }
        });
        PackageCloseRecyclerViewUserPackageFragment.setAdapter(closePackageRecyclerViewAdapter);


        FloatingActionButton NewPackageFloatingActionButtonUserPackageFragment = CurrentView.findViewById(R.id.NewPackageFloatingActionButtonUserPackageFragment);
        NewPackageFloatingActionButtonUserPackageFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent NewPackageIntent = Context.NewIntent(PackageActivity.class);
                NewPackageIntent.putExtra("New", "New");
                Context.startActivity(NewPackageIntent);
            }
        });


        RefreshPackageSwipeRefreshLayoutUserPackageFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                LoadMoreProgressBar.setVisibility(View.GONE);
                IsSwipe = true;
                PageNumberOpen = 1;
                PageNumberClose = 1;
                packageRecyclerViewAdapter.setLoading(false);
                closePackageRecyclerViewAdapter.setLoading(false);
                LoadData();
            }
        });


        ExpireAndValidatePackageSwitchUserPackageFragment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    LoadDataOpenPackage();

                    ExpireAndValidatePackageTitleTextViewUserPackageFragment.setText(Context.getResources().getString(R.string.package_validate));

                    PackageOpenRecyclerViewUserPackageFragment.setVisibility(View.VISIBLE);
                    PackageCloseRecyclerViewUserPackageFragment.setVisibility(View.GONE);


                } else {
                    PackageOpenRecyclerViewUserPackageFragment.setVisibility(View.GONE);
                    PackageCloseRecyclerViewUserPackageFragment.setVisibility(View.VISIBLE);


                    ExpireAndValidatePackageTitleTextViewUserPackageFragment.setText(Context.getResources().getString(R.string.package_expire));

                    LoadDataClosePackage();
                }
            }
        });

    }

    public void LoadData() {

        Context.ShowLoadingProgressBar();

        PackageService packageService = new PackageService(this);
        Context.setRetryType(2);
        packageService.GetUserCredit();

    }

    public void LoadDataOpenPackage() {
        if (!IsSwipe)
            if (PageNumberOpen == 1)
                Context.ShowLoadingProgressBar();

        PackageService packageService = new PackageService(this);
        Context.setRetryType(2);
        packageService.GetAllOpen(PageNumberOpen);

    }

    public void LoadDataClosePackage() {
        if (!IsSwipe)
            if (PageNumberClose == 1)
                Context.ShowLoadingProgressBar();

        PackageService packageService = new PackageService(this);
        Context.setRetryType(2);
        packageService.GetAllClose(PageNumberClose);

    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {

        LoadMoreProgressBar.setVisibility(View.GONE);
        RefreshPackageSwipeRefreshLayoutUserPackageFragment.setRefreshing(false);
        IsSwipe = false;

        try {
            if (ServiceMethod == ServiceMethodType.UserPackageOpenGetAll) {
                Context.HideLoading();
                Feedback<List<OutputPackageTransactionViewModel>> FeedBack = (Feedback<List<OutputPackageTransactionViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    final List<OutputPackageTransactionViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {

                        if (PageNumberOpen == 1)
                            packageRecyclerViewAdapter.SetViewModelList(ViewModelList);
                        else
                            packageRecyclerViewAdapter.AddViewModelList(ViewModelList);
                    }

                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    Context.ShowToast(Context.getResources().getString(R.string.there_is_no_valid_package), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                } else {

                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        if (!(PageNumberOpen > 1 && FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()))
                            Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);

                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.UserPackageCloseGetAll) {
                Context.HideLoading();
                Feedback<List<OutputPackageTransactionViewModel>> FeedBack = (Feedback<List<OutputPackageTransactionViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    final List<OutputPackageTransactionViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {

                        if (PageNumberClose == 1)
                            closePackageRecyclerViewAdapter.SetViewModelList(ViewModelList);
                        else
                            closePackageRecyclerViewAdapter.AddViewModelList(ViewModelList);
                    }

                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    Context.ShowToast(Context.getResources().getString(R.string.there_is_no_expire_package), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                } else {

                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        if (!(PageNumberClose > 1 && FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()))
                            Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);

                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.UserCreditGet) {
                Feedback<Double> FeedBack = (Feedback<Double>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    LoadDataOpenPackage();

                    if (FeedBack.getValue() != null) {
                        UserCreditTextViewUserPackageFragment.setText(Utility.GetIntegerNumberWithComma(FeedBack.getValue()));
                    } else {
                        UserCreditTextViewUserPackageFragment.setText(Context.getResources().getString(R.string.zero));
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
                IsSwipe = false;
                IsLoadedDataForFirst = true;
                //دریافت اطلاعات از سرور
                LoadData();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}
