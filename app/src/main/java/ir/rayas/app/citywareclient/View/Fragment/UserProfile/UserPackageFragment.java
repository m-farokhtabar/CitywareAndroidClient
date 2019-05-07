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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.ClosePackageRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.PackageRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Package.PackageService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.Master.UserProfileActivity;
import ir.rayas.app.citywareclient.View.UserProfileChildren.PackageActivity;
import ir.rayas.app.citywareclient.ViewModel.Package.OutputPackageTransactionViewModel;

public class UserPackageFragment extends Fragment implements IResponseService, ILoadData {

    private UserProfileActivity Context = null;
    private SwipeRefreshLayout RefreshPackageSwipeRefreshLayoutUserPackageFragment = null;
    private TextViewPersian UserCreditTextViewUserPackageFragment = null;
    private TextViewPersian ShowEmptyTextViewUserPackageFragment = null;
    private boolean IsSwipe = false;
    private boolean IsLoadedDataForFirst = false;
    private PackageRecyclerViewAdapter packageRecyclerViewAdapter = null;
    private ClosePackageRecyclerViewAdapter closePackageRecyclerViewAdapter = null;
    private int PageNumberOpen = 1;
    private int PageNumberClose = 1;

    private boolean IsFirst = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (UserProfileActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_user_packge, container, false);

        IsFirst = true;
        //طرحبندی ویو
        CreateLayout(CurrentView);

        return CurrentView;
    }


    private void CreateLayout(View CurrentView) {

        ShowEmptyTextViewUserPackageFragment = CurrentView.findViewById(R.id.ShowEmptyTextViewUserPackageFragment);
        RefreshPackageSwipeRefreshLayoutUserPackageFragment = CurrentView.findViewById(R.id.RefreshPackageSwipeRefreshLayoutUserPackageFragment);
        final RecyclerView PackageOpenRecyclerViewUserPackageFragment = CurrentView.findViewById(R.id.PackageOpenRecyclerViewUserPackageFragment);
        final RecyclerView PackageCloseRecyclerViewUserPackageFragment = CurrentView.findViewById(R.id.PackageCloseRecyclerViewUserPackageFragment);
        final SwitchCompat ExpireAndValidatePackageSwitchUserPackageFragment = CurrentView.findViewById(R.id.ExpireAndValidatePackageSwitchUserPackageFragment);
        UserCreditTextViewUserPackageFragment = CurrentView.findViewById(R.id.UserCreditTextViewUserPackageFragment);
        final TextViewPersian ExpireAndValidatePackageTitleTextViewUserPackageFragment = CurrentView.findViewById(R.id.ExpireAndValidatePackageTitleTextViewUserPackageFragment);

        ShowEmptyTextViewUserPackageFragment.setVisibility(View.GONE);
        ExpireAndValidatePackageSwitchUserPackageFragment.setChecked(true);


        PackageOpenRecyclerViewUserPackageFragment.setLayoutManager(new LinearLayoutManager(Context));
        packageRecyclerViewAdapter = new PackageRecyclerViewAdapter(Context, null, PackageOpenRecyclerViewUserPackageFragment);
        PackageOpenRecyclerViewUserPackageFragment.setAdapter(packageRecyclerViewAdapter);


        PackageCloseRecyclerViewUserPackageFragment.setLayoutManager(new LinearLayoutManager(Context));
        closePackageRecyclerViewAdapter = new ClosePackageRecyclerViewAdapter(Context, null, PackageCloseRecyclerViewUserPackageFragment);
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

                IsSwipe = true;
                PageNumberOpen = 1;
                PageNumberClose = 1;

                ShowEmptyTextViewUserPackageFragment.setVisibility(View.GONE);
                ExpireAndValidatePackageSwitchUserPackageFragment.setChecked(true);

                LoadData();
            }
        });


        ExpireAndValidatePackageSwitchUserPackageFragment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                List<OutputPackageTransactionViewModel> ViewModelList = new ArrayList<>();
                packageRecyclerViewAdapter.SetViewModelList(ViewModelList);
                closePackageRecyclerViewAdapter.SetViewModelList(ViewModelList);

                PageNumberOpen = 1;
                PageNumberClose = 1;

                ShowEmptyTextViewUserPackageFragment.setVisibility(View.GONE);

                if (isChecked) {
                    if (!IsFirst)
                        LoadDataOpenPackage();

                    ExpireAndValidatePackageTitleTextViewUserPackageFragment.setText(Context.getResources().getString(R.string.package_validate));

                    PackageOpenRecyclerViewUserPackageFragment.setVisibility(View.VISIBLE);
                    PackageCloseRecyclerViewUserPackageFragment.setVisibility(View.GONE);

                } else {
                    PackageOpenRecyclerViewUserPackageFragment.setVisibility(View.GONE);
                    PackageCloseRecyclerViewUserPackageFragment.setVisibility(View.VISIBLE);


                    ExpireAndValidatePackageTitleTextViewUserPackageFragment.setText(Context.getResources().getString(R.string.package_expire));

                    if (!IsFirst)
                        LoadDataClosePackage();
                }
            }
        });

    }

    public void LoadData() {
        if (!IsSwipe)
            if (PageNumberOpen == 1)
                Context.ShowLoadingProgressBar();

        PackageService packageService = new PackageService(this);
        Context.setRetryType(2);
        packageService.GetUserCredit();

    }

    public void LoadDataOpenPackage() {

        PackageService packageService = new PackageService(this);
        Context.setRetryType(2);
        packageService.GetAllOpen(PageNumberOpen);

    }

    public void LoadDataClosePackage() {

        PackageService packageService = new PackageService(this);
        Context.setRetryType(2);
        packageService.GetAllClose(PageNumberClose);

    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {

        RefreshPackageSwipeRefreshLayoutUserPackageFragment.setRefreshing(false);
        IsSwipe = false;

        try {
            if (ServiceMethod == ServiceMethodType.UserPackageOpenGetAll) {
                Context.HideLoading();
                Feedback<List<OutputPackageTransactionViewModel>> FeedBack = (Feedback<List<OutputPackageTransactionViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<OutputPackageTransactionViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        if (PageNumberOpen == 1) {
                            if (ViewModelList.size() < 1) {
                                ShowEmptyTextViewUserPackageFragment.setVisibility(View.VISIBLE);
                            } else {
                                ShowEmptyTextViewUserPackageFragment.setVisibility(View.GONE);
                                packageRecyclerViewAdapter.SetViewModelList(ViewModelList);

                                if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                    PageNumberOpen = PageNumberOpen + 1;
                                    LoadDataOpenPackage();
                                }
                            }

                        } else {
                            ShowEmptyTextViewUserPackageFragment.setVisibility(View.GONE);
                            packageRecyclerViewAdapter.AddViewModelList(ViewModelList);

                            if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                PageNumberOpen = PageNumberOpen + 1;
                                LoadDataOpenPackage();
                            }
                        }
                    }
                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    if (PageNumberOpen > 1) {
                        ShowEmptyTextViewUserPackageFragment.setVisibility(View.GONE);
                    } else {
                        ShowEmptyTextViewUserPackageFragment.setVisibility(View.VISIBLE);
                    }
                } else {
                    ShowEmptyTextViewUserPackageFragment.setVisibility(View.GONE);
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }

            } else if (ServiceMethod == ServiceMethodType.UserPackageCloseGetAll) {
                Context.HideLoading();
                Feedback<List<OutputPackageTransactionViewModel>> FeedBack = (Feedback<List<OutputPackageTransactionViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<OutputPackageTransactionViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        if (PageNumberClose == 1) {
                            if (ViewModelList.size() < 1) {
                                ShowEmptyTextViewUserPackageFragment.setVisibility(View.VISIBLE);
                            } else {
                                ShowEmptyTextViewUserPackageFragment.setVisibility(View.GONE);
                                closePackageRecyclerViewAdapter.SetViewModelList(ViewModelList);

                                if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                    PageNumberClose = PageNumberClose + 1;
                                    LoadDataClosePackage();
                                }
                            }

                        } else {
                            ShowEmptyTextViewUserPackageFragment.setVisibility(View.GONE);
                            closePackageRecyclerViewAdapter.AddViewModelList(ViewModelList);

                            if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                PageNumberClose = PageNumberClose + 1;
                                LoadDataClosePackage();
                            }
                        }
                    }
                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    if (PageNumberClose > 1) {
                        ShowEmptyTextViewUserPackageFragment.setVisibility(View.GONE);
                    } else {
                        ShowEmptyTextViewUserPackageFragment.setVisibility(View.VISIBLE);
                    }
                } else {
                    ShowEmptyTextViewUserPackageFragment.setVisibility(View.GONE);
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }

            } else if (ServiceMethod == ServiceMethodType.UserCreditGet) {
                Feedback<Double> FeedBack = (Feedback<Double>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    IsFirst = false;
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
