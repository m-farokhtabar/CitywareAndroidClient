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

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.PosterExpiredRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.PosterValidRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Package.PackageService;
import ir.rayas.app.citywareclient.Service.Poster.PosterService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.Master.UserProfileActivity;
import ir.rayas.app.citywareclient.View.UserProfileChildren.PosterTypeActivity;
import ir.rayas.app.citywareclient.ViewModel.Poster.PurchasedPosterViewModel;


public class UserPosterFragment extends Fragment implements IResponseService, ILoadData {

    private UserProfileActivity Context = null;
    private SwipeRefreshLayout RefreshPosterSwipeRefreshLayoutUserPostersFragment;
    private TextViewPersian UserCreditTextViewUserPostersFragment = null;
    private TextViewPersian ShowEmptyTextViewUserPostersFragment = null;
    private boolean IsSwipe = false;
    private boolean IsLoadedDataForFirst = false;
    private PosterValidRecyclerViewAdapter posterValidRecyclerViewAdapter = null;
    private PosterExpiredRecyclerViewAdapter posterExpiredRecyclerViewAdapter = null;
    private SwitchCompat ExpireAndValidatePosterSwitchUserPostersFragment = null;

    private int PageNumberValid = 1;
    private int PageNumberExpire = 1;

    private boolean IsFirst = false;
    private double UserCredit = 0;

    private boolean IsValid = true;

    public PosterValidRecyclerViewAdapter getPosterValidRecyclerViewAdapter() {
        return posterValidRecyclerViewAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (UserProfileActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_user_poster, container, false);

        IsFirst = true;
        //طرحبندی ویو
        CreateLayout(CurrentView);

        return CurrentView;
    }


    private void CreateLayout(View CurrentView) {

        ShowEmptyTextViewUserPostersFragment = CurrentView.findViewById(R.id.ShowEmptyTextViewUserPostersFragment);
        RefreshPosterSwipeRefreshLayoutUserPostersFragment = CurrentView.findViewById(R.id.RefreshPosterSwipeRefreshLayoutUserPostersFragment);
        final RecyclerView PosterValidRecyclerViewUserPostersFragment = CurrentView.findViewById(R.id.PosterValidRecyclerViewUserPostersFragment);
        final RecyclerView PosterExpiredRecyclerViewUserPackageFragment = CurrentView.findViewById(R.id.PosterExpiredRecyclerViewUserPackageFragment);
        ExpireAndValidatePosterSwitchUserPostersFragment = CurrentView.findViewById(R.id.ExpireAndValidatePosterSwitchUserPostersFragment);
        UserCreditTextViewUserPostersFragment = CurrentView.findViewById(R.id.UserCreditTextViewUserPostersFragment);
        final TextViewPersian ExpireAndValidatePosterTitleTextViewUserPostersFragment = CurrentView.findViewById(R.id.ExpireAndValidatePosterTitleTextViewUserPostersFragment);

        ShowEmptyTextViewUserPostersFragment.setVisibility(View.GONE);
        ExpireAndValidatePosterSwitchUserPostersFragment.setChecked(true);

        PosterValidRecyclerViewUserPostersFragment.setLayoutManager(new LinearLayoutManager(Context));
        posterValidRecyclerViewAdapter = new PosterValidRecyclerViewAdapter(Context, null, PosterValidRecyclerViewUserPostersFragment);
        PosterValidRecyclerViewUserPostersFragment.setAdapter(posterValidRecyclerViewAdapter);

        PosterExpiredRecyclerViewUserPackageFragment.setLayoutManager(new LinearLayoutManager(Context));
        posterExpiredRecyclerViewAdapter = new PosterExpiredRecyclerViewAdapter(Context, null, PosterExpiredRecyclerViewUserPackageFragment);
        PosterExpiredRecyclerViewUserPackageFragment.setAdapter(posterExpiredRecyclerViewAdapter);


        FloatingActionButton NewPosterFloatingActionButtonUserPostersFragment = CurrentView.findViewById(R.id.NewPosterFloatingActionButtonUserPostersFragment);
        NewPosterFloatingActionButtonUserPostersFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent NewPosterTypeIntent = Context.NewIntent(PosterTypeActivity.class);
                Context.startActivity(NewPosterTypeIntent);
            }
        });

        RefreshPosterSwipeRefreshLayoutUserPostersFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                posterValidRecyclerViewAdapter.ClearViewModelList();
                posterExpiredRecyclerViewAdapter.ClearViewModelList();

                ShowEmptyTextViewUserPostersFragment.setVisibility(View.GONE);
                IsSwipe = true;

                if (IsValid) {
                    PageNumberValid = 1;
                    ExpireAndValidatePosterSwitchUserPostersFragment.setChecked(true);
                    LoadDataValidPoster();
                } else {
                    PageNumberExpire = 1;
                    ExpireAndValidatePosterSwitchUserPostersFragment.setChecked(false);
                    LoadDataExpirePoster();
                }


            }
        });


        ExpireAndValidatePosterSwitchUserPostersFragment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RefreshPosterSwipeRefreshLayoutUserPostersFragment.setRefreshing(true);

                posterValidRecyclerViewAdapter.ClearViewModelList();
                posterExpiredRecyclerViewAdapter.ClearViewModelList();

                PageNumberValid = 1;
                PageNumberExpire = 1;

                ShowEmptyTextViewUserPostersFragment.setVisibility(View.GONE);

                if (isChecked) {

                    IsValid = true;

                    if (!IsFirst)
                        LoadDataValidPoster();

                    ExpireAndValidatePosterTitleTextViewUserPostersFragment.setText(Context.getResources().getString(R.string.poster_validate));

                    PosterValidRecyclerViewUserPostersFragment.setVisibility(View.VISIBLE);
                    PosterExpiredRecyclerViewUserPackageFragment.setVisibility(View.GONE);


                } else {

                    IsValid = false;
                    PosterValidRecyclerViewUserPostersFragment.setVisibility(View.GONE);
                    PosterExpiredRecyclerViewUserPackageFragment.setVisibility(View.VISIBLE);


                    ExpireAndValidatePosterTitleTextViewUserPostersFragment.setText(Context.getResources().getString(R.string.poster_expire));

                    if (!IsFirst)
                        LoadDataExpirePoster();
                }
            }
        });
    }

    public void LoadData() {
        Context.ShowLoadingProgressBar();

        Context.setLoadPoster(true);

        ExpireAndValidatePosterSwitchUserPostersFragment.setClickable(false);
        ExpireAndValidatePosterSwitchUserPostersFragment.setEnabled(false);

        PackageService packageService = new PackageService(this);
        Context.setRetryType(2);
        packageService.GetUserCredit();

    }

    public void LoadDataValidPoster() {
        ExpireAndValidatePosterSwitchUserPostersFragment.setClickable(false);
        ExpireAndValidatePosterSwitchUserPostersFragment.setEnabled(false);

        PosterService userPosterService = new PosterService(this);
        Context.setRetryType(2);
        userPosterService.GetAllValid(PageNumberValid);

    }

    public void LoadDataExpirePoster() {
        ExpireAndValidatePosterSwitchUserPostersFragment.setClickable(false);
        ExpireAndValidatePosterSwitchUserPostersFragment.setEnabled(false);

        PosterService userPosterService = new PosterService(this);
        Context.setRetryType(2);
        userPosterService.GetAllExpired(PageNumberExpire);

    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {

        ExpireAndValidatePosterSwitchUserPostersFragment.setClickable(true);
        ExpireAndValidatePosterSwitchUserPostersFragment.setEnabled(true);

        IsSwipe = false;

        try {
            if (ServiceMethod == ServiceMethodType.UserPosterValidGet) {
                Context.HideLoading();
                Feedback<List<PurchasedPosterViewModel>> FeedBack = (Feedback<List<PurchasedPosterViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    final List<PurchasedPosterViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        if (PageNumberValid == 1) {
                            if (ViewModelList.size() < 1) {
                                ShowEmptyTextViewUserPostersFragment.setVisibility(View.VISIBLE);
                            } else {
                                ShowEmptyTextViewUserPostersFragment.setVisibility(View.GONE);
                                posterValidRecyclerViewAdapter.SetViewModelList(ViewModelList);

                                if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                    PageNumberValid = PageNumberValid + 1;
                                    LoadDataValidPoster();
                                }
                            }

                        } else {
                            ShowEmptyTextViewUserPostersFragment.setVisibility(View.GONE);
                            posterValidRecyclerViewAdapter.AddViewModelList(ViewModelList);

                            if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                PageNumberValid = PageNumberValid + 1;
                                LoadDataValidPoster();
                            }
                        }
                    }
                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    if (PageNumberValid > 1) {
                        ShowEmptyTextViewUserPostersFragment.setVisibility(View.GONE);
                    } else {
                        ShowEmptyTextViewUserPostersFragment.setVisibility(View.VISIBLE);
                    }
                } else {
                    ShowEmptyTextViewUserPostersFragment.setVisibility(View.GONE);
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }

            } else if (ServiceMethod == ServiceMethodType.UserPosterExpiredGet) {
                Context.HideLoading();
                Feedback<List<PurchasedPosterViewModel>> FeedBack = (Feedback<List<PurchasedPosterViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<PurchasedPosterViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        if (PageNumberExpire == 1) {
                            if (ViewModelList.size() < 1) {
                                ShowEmptyTextViewUserPostersFragment.setVisibility(View.VISIBLE);
                            } else {
                                ShowEmptyTextViewUserPostersFragment.setVisibility(View.GONE);
                                posterExpiredRecyclerViewAdapter.SetViewModelList(ViewModelList);

                                if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                    PageNumberExpire = PageNumberExpire + 1;
                                    LoadDataExpirePoster();
                                }
                            }

                        } else {
                            ShowEmptyTextViewUserPostersFragment.setVisibility(View.GONE);
                            posterExpiredRecyclerViewAdapter.AddViewModelList(ViewModelList);

                            if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                PageNumberExpire = PageNumberExpire + 1;
                                LoadDataExpirePoster();
                            }
                        }
                    }
                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    if (PageNumberExpire > 1) {
                        ShowEmptyTextViewUserPostersFragment.setVisibility(View.GONE);
                    } else {
                        ShowEmptyTextViewUserPostersFragment.setVisibility(View.VISIBLE);
                    }
                } else {
                    ShowEmptyTextViewUserPostersFragment.setVisibility(View.GONE);
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
                    LoadDataValidPoster();

                    if (FeedBack.getValue() != null) {
                        UserCredit = FeedBack.getValue();
                        UserCreditTextViewUserPostersFragment.setText(Utility.GetIntegerNumberWithComma(UserCredit));
                    } else {
                        UserCredit = 0;
                        UserCreditTextViewUserPostersFragment.setText(Context.getResources().getString(R.string.zero));
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

        RefreshPosterSwipeRefreshLayoutUserPostersFragment.setRefreshing(false);
    }

    @SuppressLint("SetTextI18n")
    public void SetViewUserCredit(double Price, boolean IsPoster) {

        if (posterValidRecyclerViewAdapter.getItemCount() > 0)
            ShowEmptyTextViewUserPostersFragment.setVisibility(View.GONE);
        else
            ShowEmptyTextViewUserPostersFragment.setVisibility(View.VISIBLE);

        if (IsPoster) {
            UserCredit = UserCredit - Price;
        } else {
            UserCredit = UserCredit + Price;
        }

        UserCreditTextViewUserPostersFragment.setText(Utility.GetIntegerNumberWithComma(UserCredit));

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
