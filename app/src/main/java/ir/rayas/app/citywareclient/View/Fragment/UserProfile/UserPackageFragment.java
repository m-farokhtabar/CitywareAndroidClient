package ir.rayas.app.citywareclient.View.Fragment.UserProfile;


import android.annotation.SuppressLint;
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
    private SwitchCompat ExpireAndValidatePackageSwitchUserPackageFragment = null;
    private int PageNumberOpen = 1;
    private int PageNumberClose = 1;

    private boolean IsFirst = false;
    private double UserCredit = 0;

    private boolean IsValid = true;

    public PackageRecyclerViewAdapter getPackageRecyclerViewAdapter() {
        return packageRecyclerViewAdapter;
    }

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
        ExpireAndValidatePackageSwitchUserPackageFragment = CurrentView.findViewById(R.id.ExpireAndValidatePackageSwitchUserPackageFragment);
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

                packageRecyclerViewAdapter.ClearViewModelList();
                closePackageRecyclerViewAdapter.ClearViewModelList();

                IsSwipe = true;
                ShowEmptyTextViewUserPackageFragment.setVisibility(View.GONE);

                if (IsValid) {
                    PageNumberOpen = 1;
                    ExpireAndValidatePackageSwitchUserPackageFragment.setChecked(true);
                } else {
                    PageNumberClose = 1;
                    ExpireAndValidatePackageSwitchUserPackageFragment.setChecked(false);
                }

                Context.SetLoadPackageAndPoster();

            }
        });


        ExpireAndValidatePackageSwitchUserPackageFragment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RefreshPackageSwipeRefreshLayoutUserPackageFragment.setRefreshing(true);

                packageRecyclerViewAdapter.ClearViewModelList();
                closePackageRecyclerViewAdapter.ClearViewModelList();

                PageNumberOpen = 1;
                PageNumberClose = 1;

                ShowEmptyTextViewUserPackageFragment.setVisibility(View.GONE);

                if (isChecked) {

                    IsValid = true;
                    if (!IsFirst)
                        LoadDataOpenPackage();

                    ExpireAndValidatePackageTitleTextViewUserPackageFragment.setText(Context.getResources().getString(R.string.package_validate));

                    PackageOpenRecyclerViewUserPackageFragment.setVisibility(View.VISIBLE);
                    PackageCloseRecyclerViewUserPackageFragment.setVisibility(View.GONE);

                } else {

                    IsValid = false;
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
        if (!Context.isLoadPackage()) {
            Context.ShowLoadingProgressBar();
        } else {
            RefreshPackageSwipeRefreshLayoutUserPackageFragment.setRefreshing(true);
            PageNumberOpen = 1;
            packageRecyclerViewAdapter.ClearViewModelList();
            packageRecyclerViewAdapter.ClearViewModelListSort();
        }

        Context.setLoadPackage(true);

        PackageService packageService = new PackageService(this);
        Context.setRetryType(2);
        packageService.GetUserCredit();

    }

    public void LoadDataOpenPackage() {

        ExpireAndValidatePackageSwitchUserPackageFragment.setClickable(false);
        ExpireAndValidatePackageSwitchUserPackageFragment.setEnabled(false);

        PackageService packageService = new PackageService(this);
        Context.setRetryType(2);
        packageService.GetAllOpen(PageNumberOpen);

    }

    public void LoadDataClosePackage() {

        ExpireAndValidatePackageSwitchUserPackageFragment.setClickable(false);
        ExpireAndValidatePackageSwitchUserPackageFragment.setEnabled(false);

        PackageService packageService = new PackageService(this);
        Context.setRetryType(2);
        packageService.GetAllClose(PageNumberClose);

    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        ExpireAndValidatePackageSwitchUserPackageFragment.setClickable(true);
        ExpireAndValidatePackageSwitchUserPackageFragment.setEnabled(true);

        IsSwipe = false;

        try {
            if (ServiceMethod == ServiceMethodType.UserPackageOpenGetAll) {
                Context.HideLoading();
                Feedback<List<OutputPackageTransactionViewModel>> FeedBack = (Feedback<List<OutputPackageTransactionViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<OutputPackageTransactionViewModel> ViewModelList = FeedBack.getValue();

                    List<OutputPackageTransactionViewModel> outputPackageTransactionViewModels = new ArrayList<>();
                    for (int i = 0; i < ViewModelList.size(); i++) {
                        if (ViewModelList.get(i).isActive()) {
                            outputPackageTransactionViewModels.add(ViewModelList.get(i));
                        }
                    }

                    if (outputPackageTransactionViewModels != null) {
                        if (PageNumberOpen == 1) {
                            if (outputPackageTransactionViewModels.size() < 1) {
                                ShowEmptyTextViewUserPackageFragment.setVisibility(View.VISIBLE);
                            } else {
                                ShowEmptyTextViewUserPackageFragment.setVisibility(View.GONE);
                                packageRecyclerViewAdapter.SetViewModelList(outputPackageTransactionViewModels);

                                if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                    PageNumberOpen = PageNumberOpen + 1;
                                    LoadDataOpenPackage();
                                }
                            }

                        } else {
                            ShowEmptyTextViewUserPackageFragment.setVisibility(View.GONE);
                            packageRecyclerViewAdapter.AddViewModelList(outputPackageTransactionViewModels);

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
                    if (IsValid)
                        LoadDataOpenPackage();
                    else
                        LoadDataClosePackage();

                    if (FeedBack.getValue() != null) {
                        UserCredit = FeedBack.getValue();
                        UserCreditTextViewUserPackageFragment.setText(Utility.GetIntegerNumberWithComma(UserCredit));
                    } else {
                        UserCredit = 0;
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

        RefreshPackageSwipeRefreshLayoutUserPackageFragment.setRefreshing(false);
    }

//    @SuppressLint("SetTextI18n")
//    public void SetViewUserCreditPackage(double Price, boolean IsPackage) {
//
//        if (ExpireAndValidatePackageSwitchUserPackageFragment.isChecked()) {
//            if (packageRecyclerViewAdapter.getItemCount() > 0)
//                ShowEmptyTextViewUserPackageFragment.setVisibility(View.GONE);
//            else
//                ShowEmptyTextViewUserPackageFragment.setVisibility(View.VISIBLE);
//        }
//
//        if (IsPackage) {
//            UserCredit = UserCredit + Price;
//        } else {
//            UserCredit = UserCredit - Price;
//        }
//        UserCreditTextViewUserPackageFragment.setText(Utility.GetIntegerNumberWithComma(UserCredit));
//    }


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
