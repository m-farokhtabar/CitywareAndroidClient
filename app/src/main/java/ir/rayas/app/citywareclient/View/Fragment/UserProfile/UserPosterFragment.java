package ir.rayas.app.citywareclient.View.Fragment.UserProfile;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.List;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.PosterRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Poster.PosterService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.OnLoadMoreListener;
import ir.rayas.app.citywareclient.View.Master.UserProfileActivity;
import ir.rayas.app.citywareclient.ViewModel.Poster.PurchasedPosterViewModel;


public class UserPosterFragment extends Fragment implements IResponseService, ILoadData {

    private UserProfileActivity Context = null;
    private RecyclerView PosterRecyclerViewUserPostersFragment = null;
    private SwipeRefreshLayout RefreshPosterSwipeRefreshLayoutUserPostersFragment;
    private boolean IsSwipe = false;
    private boolean IsLoadedDataForFirst = false;
    private PosterRecyclerViewAdapter posterRecyclerViewAdapter = null;

    private ProgressBar LoadMoreProgressBar = null;
    private int PageNumber = 1;

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
        PosterRecyclerViewUserPostersFragment = CurrentView.findViewById(R.id.PosterRecyclerViewUserPostersFragment);

        PosterRecyclerViewUserPostersFragment.setLayoutManager(new LinearLayoutManager(Context));
        posterRecyclerViewAdapter = new PosterRecyclerViewAdapter(Context, null, PosterRecyclerViewUserPostersFragment, new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                PageNumber = PageNumber + 1;
                LoadMoreProgressBar.setVisibility(View.VISIBLE);
                LoadData();
            }
        });
        PosterRecyclerViewUserPostersFragment.setAdapter(posterRecyclerViewAdapter);


        FloatingActionButton NewPosterFloatingActionButtonUserPostersFragment = CurrentView.findViewById(R.id.NewPosterFloatingActionButtonUserPostersFragment);
        NewPosterFloatingActionButtonUserPostersFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        RefreshPosterSwipeRefreshLayoutUserPostersFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                LoadMoreProgressBar.setVisibility(View.GONE);
                IsSwipe = true;
                PageNumber = 1;
                posterRecyclerViewAdapter.setLoading(false);
                LoadData();
            }
        });
    }

    public void LoadData() {
        if (!IsSwipe)
            if (PageNumber == 1)
                Context.ShowLoadingProgressBar();

        PosterService userPosterService = new PosterService(this);
        Context.setRetryType(2);
        userPosterService.GetAll(PageNumber);

    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        LoadMoreProgressBar.setVisibility(View.GONE);
        RefreshPosterSwipeRefreshLayoutUserPostersFragment.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.UserPosterGetAll) {
                Feedback<List<PurchasedPosterViewModel>> FeedBack = (Feedback<List<PurchasedPosterViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    final List<PurchasedPosterViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        if (PageNumber == 1)
                            posterRecyclerViewAdapter.SetViewModelList(ViewModelList);
                        else
                            posterRecyclerViewAdapter.AddViewModelList(ViewModelList);
                    }

                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        if (!(PageNumber > 1 && FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()))
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
