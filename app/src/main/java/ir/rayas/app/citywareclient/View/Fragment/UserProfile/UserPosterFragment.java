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

import ir.rayas.app.citywareclient.Adapter.RecyclerView.PosterExpiredRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.PosterValidRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Package.PackageService;
import ir.rayas.app.citywareclient.Service.Poster.PosterService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.OnLoadMoreListener;
import ir.rayas.app.citywareclient.View.Master.UserProfileActivity;
import ir.rayas.app.citywareclient.View.UserProfileChildren.PosterTypeActivity;
import ir.rayas.app.citywareclient.ViewModel.Poster.PurchasedPosterViewModel;


public class UserPosterFragment extends Fragment implements IResponseService, ILoadData {

    private UserProfileActivity Context = null;
    private SwipeRefreshLayout RefreshPosterSwipeRefreshLayoutUserPostersFragment;
    private TextViewPersian UserCreditTextViewUserPostersFragment = null;
    private boolean IsSwipe = false;
    private boolean IsLoadedDataForFirst = false;
    private PosterValidRecyclerViewAdapter posterValidRecyclerViewAdapter = null;
    private PosterExpiredRecyclerViewAdapter posterExpiredRecyclerViewAdapter = null;

    private ProgressBar LoadMoreProgressBar = null;
    private int PageNumberValid = 1;
    private int PageNumberExpire = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (UserProfileActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_user_poster, container, false);
        //طرحبندی ویو
        CreateLayout(CurrentView);

        return CurrentView;
    }


    private void CreateLayout(View CurrentView) {

        LoadMoreProgressBar = CurrentView.findViewById(R.id.LoadMoreProgressPosterFragment);
        RefreshPosterSwipeRefreshLayoutUserPostersFragment = CurrentView.findViewById(R.id.RefreshPosterSwipeRefreshLayoutUserPostersFragment);
        final RecyclerView PosterValidRecyclerViewUserPostersFragment = CurrentView.findViewById(R.id.PosterValidRecyclerViewUserPostersFragment);
        final RecyclerView PosterExpiredRecyclerViewUserPackageFragment = CurrentView.findViewById(R.id.PosterExpiredRecyclerViewUserPackageFragment);
        SwitchCompat ExpireAndValidatePosterSwitchUserPostersFragment = CurrentView.findViewById(R.id.ExpireAndValidatePosterSwitchUserPostersFragment);
        UserCreditTextViewUserPostersFragment = CurrentView.findViewById(R.id.UserCreditTextViewUserPostersFragment);
        final TextViewPersian ExpireAndValidatePosterTitleTextViewUserPostersFragment = CurrentView.findViewById(R.id.ExpireAndValidatePosterTitleTextViewUserPostersFragment);

        ExpireAndValidatePosterSwitchUserPostersFragment.setChecked(true);
        
        PosterValidRecyclerViewUserPostersFragment.setLayoutManager(new LinearLayoutManager(Context));
        posterValidRecyclerViewAdapter = new PosterValidRecyclerViewAdapter(Context, null, PosterValidRecyclerViewUserPostersFragment, new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                PageNumberValid = PageNumberValid + 1;
                LoadMoreProgressBar.setVisibility(View.VISIBLE);
                LoadData();
            }
        });
        PosterValidRecyclerViewUserPostersFragment.setAdapter(posterValidRecyclerViewAdapter);

        PosterExpiredRecyclerViewUserPackageFragment.setLayoutManager(new LinearLayoutManager(Context));
        posterExpiredRecyclerViewAdapter = new PosterExpiredRecyclerViewAdapter(Context, null, PosterExpiredRecyclerViewUserPackageFragment, new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                PageNumberExpire = PageNumberExpire + 1;
                LoadMoreProgressBar.setVisibility(View.VISIBLE);
                LoadData();
            }
        });
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

                LoadMoreProgressBar.setVisibility(View.GONE);
                IsSwipe = true;
                PageNumberValid = 1;
                PageNumberExpire = 1;
                posterValidRecyclerViewAdapter.setLoading(false);
                posterExpiredRecyclerViewAdapter.setLoading(false);
                LoadData();
            }
        });


        ExpireAndValidatePosterSwitchUserPostersFragment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    LoadDataValidPackage();

                    ExpireAndValidatePosterTitleTextViewUserPostersFragment.setText(Context.getResources().getString(R.string.poster_validate));

                    PosterValidRecyclerViewUserPostersFragment.setVisibility(View.VISIBLE);
                    PosterExpiredRecyclerViewUserPackageFragment.setVisibility(View.GONE);


                } else {
                    PosterValidRecyclerViewUserPostersFragment.setVisibility(View.GONE);
                    PosterExpiredRecyclerViewUserPackageFragment.setVisibility(View.VISIBLE);


                    ExpireAndValidatePosterTitleTextViewUserPostersFragment.setText(Context.getResources().getString(R.string.poster_expire));

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


    public void LoadDataValidPackage() {
        if (!IsSwipe)
            if (PageNumberValid == 1)
                Context.ShowLoadingProgressBar();

        PosterService userPosterService = new PosterService(this);
        Context.setRetryType(2);
        userPosterService.GetAllValid(PageNumberValid);

    }

    public void LoadDataClosePackage() {
        if (!IsSwipe)
            if (PageNumberExpire == 1)
                Context.ShowLoadingProgressBar();

        PosterService userPosterService = new PosterService(this);
        Context.setRetryType(2);
        userPosterService.GetAllExpired(PageNumberExpire);

    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {

        LoadMoreProgressBar.setVisibility(View.GONE);
        RefreshPosterSwipeRefreshLayoutUserPostersFragment.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.UserPosterValidGet) {
                Context.HideLoading();
                Feedback<List<PurchasedPosterViewModel>> FeedBack = (Feedback<List<PurchasedPosterViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    final List<PurchasedPosterViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        if (PageNumberValid == 1)
                            posterValidRecyclerViewAdapter.SetViewModelList(ViewModelList);
                        else
                            posterValidRecyclerViewAdapter.AddViewModelList(ViewModelList);
                    }

                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        if (!(PageNumberValid > 1 && FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()))
                            Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);

                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            }  else  if (ServiceMethod == ServiceMethodType.UserPosterExpiredGet) {
                Context.HideLoading();
                Feedback<List<PurchasedPosterViewModel>> FeedBack = (Feedback<List<PurchasedPosterViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    final List<PurchasedPosterViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        if (PageNumberExpire == 1)
                            posterExpiredRecyclerViewAdapter.SetViewModelList(ViewModelList);
                        else
                            posterExpiredRecyclerViewAdapter.AddViewModelList(ViewModelList);
                    }

                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        if (!(PageNumberValid > 1 && FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()))
                            Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);

                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.UserCreditGet) {
                Feedback<Double> FeedBack = (Feedback<Double>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    LoadDataValidPackage();

                    if (FeedBack.getValue() != null) {
                        UserCreditTextViewUserPostersFragment.setText(Utility.GetIntegerNumberWithComma(FeedBack.getValue()));
                    } else {
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
