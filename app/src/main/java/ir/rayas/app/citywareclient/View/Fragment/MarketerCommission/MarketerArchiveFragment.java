package ir.rayas.app.citywareclient.View.Fragment.MarketerCommission;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.ArchiveRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Marketing.MarketingService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowMarketerCommissionDetailsActivity;
import ir.rayas.app.citywareclient.ViewModel.Marketing.MarketingBusinessManViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarketerArchiveFragment extends Fragment implements IResponseService, ILoadData {

    private ShowMarketerCommissionDetailsActivity Context = null;

    private boolean IsSwipe = false;
    private boolean IsLoadedDataForFirst = false;

    private SwipeRefreshLayout RefreshArchiveSwipeRefreshLayoutShowArchiveActivity = null;
    private TextViewPersian ShowEmptyArchiveTextViewShowArchiveActivity = null;
    private  ArchiveRecyclerViewAdapter archiveRecyclerViewAdapter = null;

    private int PageNumber = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //دریافت اکتیوتی والد این فرگمین
        Context = (ShowMarketerCommissionDetailsActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_marketer_archive, container, false);

        //طرحبندی ویو
        CreateLayout(CurrentView);

        return CurrentView;

    }

    private void CreateLayout(View CurrentView) {
        ShowEmptyArchiveTextViewShowArchiveActivity = CurrentView.findViewById(R.id.ShowEmptyArchiveTextViewShowArchiveActivity);
        RefreshArchiveSwipeRefreshLayoutShowArchiveActivity = CurrentView.findViewById(R.id.RefreshArchiveSwipeRefreshLayoutShowArchiveActivity);
        ShowEmptyArchiveTextViewShowArchiveActivity.setVisibility(View.GONE);

        RecyclerView archiveRecyclerViewShowArchiveActivity = CurrentView.findViewById(R.id.ArchiveRecyclerViewShowArchiveActivity);
        archiveRecyclerViewShowArchiveActivity.setHasFixedSize(true);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(Context);
        archiveRecyclerViewShowArchiveActivity.setLayoutManager(LinearLayoutManager);

         archiveRecyclerViewAdapter = new ArchiveRecyclerViewAdapter(Context, null, archiveRecyclerViewShowArchiveActivity);
        archiveRecyclerViewShowArchiveActivity.setAdapter(archiveRecyclerViewAdapter);


        RefreshArchiveSwipeRefreshLayoutShowArchiveActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                IsSwipe = true;
                PageNumber = 1;
                LoadData();
            }
        });

    }

    /**
     * دریافت اطلاعات نحوای جهت پر کردن Recycle
     */
    public void LoadData() {
        if (!IsSwipe)
            if (PageNumber == 1)
                Context.ShowLoadingProgressBar();

        Context.setRetryType(2);
        MarketingService MarketingService = new MarketingService(this);
        MarketingService.GetAllExpiredMarketerCommission(PageNumber);
    }

    /**
     * @param Data
     * @param ServiceMethod
     * @param <T>
     */
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        RefreshArchiveSwipeRefreshLayoutShowArchiveActivity.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.ExpiredMarketerCommissionGetAll) {
                Feedback<List<MarketingBusinessManViewModel>> FeedBack = (Feedback<List<MarketingBusinessManViewModel>>) Data;


                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<MarketingBusinessManViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        if (PageNumber == 1) {
                            if (ViewModelList.size() < 1) {
                                ShowEmptyArchiveTextViewShowArchiveActivity.setVisibility(View.VISIBLE);
                            } else {
                                ShowEmptyArchiveTextViewShowArchiveActivity.setVisibility(View.GONE);
                                archiveRecyclerViewAdapter.SetViewModelList(ViewModelList);

                                if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                    PageNumber = PageNumber + 1;
                                    LoadData();
                                }
                            }

                        } else {
                            ShowEmptyArchiveTextViewShowArchiveActivity.setVisibility(View.GONE);
                            archiveRecyclerViewAdapter.AddViewModelList(ViewModelList);

                            if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                PageNumber = PageNumber + 1;
                                LoadData();
                            }
                        }
                    }
                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    if (PageNumber > 1) {
                        ShowEmptyArchiveTextViewShowArchiveActivity.setVisibility(View.GONE);
                    } else {
                        ShowEmptyArchiveTextViewShowArchiveActivity.setVisibility(View.VISIBLE);
                    }
                } else {
                    ShowEmptyArchiveTextViewShowArchiveActivity.setVisibility(View.GONE);
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            }
        } catch (Exception e) {
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
