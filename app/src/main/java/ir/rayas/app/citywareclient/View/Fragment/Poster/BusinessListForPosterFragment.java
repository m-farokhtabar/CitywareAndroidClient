package ir.rayas.app.citywareclient.View.Fragment.Poster;


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

import ir.rayas.app.citywareclient.Adapter.RecyclerView.BusinessListForPosterRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.MyClickListener;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Business.BusinessService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Poster.PosterService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.UserProfileChildren.BuyPosterTypeActivity;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessViewModel;
import ir.rayas.app.citywareclient.ViewModel.Poster.BuyPosterViewModel;
import ir.rayas.app.citywareclient.ViewModel.Poster.PurchasedPosterViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessListForPosterFragment extends Fragment implements IResponseService {

    private TextViewPersian ShowEmptyBusinessListTextViewBusinessListForPosterFragment = null;
    private RecyclerView BusinessListRecyclerViewBusinessListForPosterFragment = null;
    private SwipeRefreshLayout RefreshBusinessListSwipeRefreshLayoutBusinessListForPosterFragment;

    private boolean IsSwipe = false;
    private BuyPosterTypeActivity Context = null;

    private int BusinessId = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (BuyPosterTypeActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_business_list_for_poster, container, false);
        //طرحبندی ویو
        CreateLayout(CurrentView);

        LoadData();

        return CurrentView;
    }

    private void CreateLayout(View CurrentView) {

        ButtonPersianView BuyPosterButtonBusinessListForPosterFragment = CurrentView.findViewById(R.id.BuyPosterButtonBusinessListForPosterFragment);
        ButtonPersianView ReturnButtonBusinessListForPosterFragment = CurrentView.findViewById(R.id.ReturnButtonBusinessListForPosterFragment);
        ShowEmptyBusinessListTextViewBusinessListForPosterFragment = CurrentView.findViewById(R.id.ShowEmptyBusinessListTextViewBusinessListForPosterFragment);
        RefreshBusinessListSwipeRefreshLayoutBusinessListForPosterFragment = CurrentView.findViewById(R.id.RefreshBusinessListSwipeRefreshLayoutBusinessListForPosterFragment);
        BusinessListRecyclerViewBusinessListForPosterFragment = CurrentView.findViewById(R.id.BusinessListRecyclerViewBusinessListForPosterFragment);
        BusinessListRecyclerViewBusinessListForPosterFragment.setHasFixedSize(true);
        //به دلیل اینکه من در هر سطر یک گزینه نیاز دارم
        LinearLayoutManager RegionLinearLayoutManager = new LinearLayoutManager(Context);
        BusinessListRecyclerViewBusinessListForPosterFragment.setLayoutManager(RegionLinearLayoutManager);

        ShowEmptyBusinessListTextViewBusinessListForPosterFragment.setVisibility(View.GONE);

        RefreshBusinessListSwipeRefreshLayoutBusinessListForPosterFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                IsSwipe = true;
                LoadData();

            }
        });

        ReturnButtonBusinessListForPosterFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        BuyPosterButtonBusinessListForPosterFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BusinessId == 0) {
                    Context.ShowToast(Context.getResources().getString(R.string.choose_one_of_your_businesses), Toast.LENGTH_LONG, MessageType.Error);
                } else {
                    Context.ShowLoadingProgressBar();
                    PosterService PosterService = new PosterService(BusinessListForPosterFragment.this);
                    PosterService.AddPurchasedPoster(MadeViewModel());
                }
            }
        });
    }

    public void LoadData() {
        if (!IsSwipe)
            Context.ShowLoadingProgressBar();

        BusinessService businessService = new BusinessService(this);
        businessService.GetAll();

    }

    private BuyPosterViewModel MadeViewModel() {

        BuyPosterViewModel ViewModel = new BuyPosterViewModel();
        try {
            ViewModel.setBusinessId(BusinessId);
            ViewModel.setHours(Context.getHours());
            ViewModel.setPosterTypeId(Context.getPosterTypeId());

        } catch (Exception Ex) {
            Context.ShowToast(FeedbackType.InvalidDataFormat.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
        return ViewModel;
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        RefreshBusinessListSwipeRefreshLayoutBusinessListForPosterFragment.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.UserGetAllBusiness) {
                Feedback<List<BusinessViewModel>> FeedBack = (Feedback<List<BusinessViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    final List<BusinessViewModel> ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {

                        ShowEmptyBusinessListTextViewBusinessListForPosterFragment.setVisibility(View.GONE);

                        //تنظیمات مربوط به recycle کسب و کار
                        BusinessListForPosterRecyclerViewAdapter businessListForPosterRecyclerViewAdapter = new BusinessListForPosterRecyclerViewAdapter(Context, ViewModel);
                        BusinessListRecyclerViewBusinessListForPosterFragment.setAdapter(businessListForPosterRecyclerViewAdapter);
                        businessListForPosterRecyclerViewAdapter.notifyDataSetChanged();
                        BusinessListRecyclerViewBusinessListForPosterFragment.invalidate();

                        businessListForPosterRecyclerViewAdapter.setOnItemClickListener(new MyClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {
                                BusinessId = ViewModel.get(position).getId();
                            }
                        });
                    } else {
                        ShowEmptyBusinessListTextViewBusinessListForPosterFragment.setVisibility(View.VISIBLE);
                    }

                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    ShowEmptyBusinessListTextViewBusinessListForPosterFragment.setVisibility(View.VISIBLE);
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.PurchasedPosterAdd) {
                Feedback<PurchasedPosterViewModel> FeedBack = (Feedback<PurchasedPosterViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.RegisteredSuccessful.getId()) {
                    PurchasedPosterViewModel ViewModel = FeedBack.getValue();

                    if (ViewModel != null) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowToast(FeedbackType.InvalidDataFormat.getMessage().replace("{0}", ""), Toast.LENGTH_LONG, MessageType.Warning);
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
