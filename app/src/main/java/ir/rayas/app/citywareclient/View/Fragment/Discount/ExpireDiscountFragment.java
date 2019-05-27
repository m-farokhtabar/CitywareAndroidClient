package ir.rayas.app.citywareclient.View.Fragment.Discount;


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

import ir.rayas.app.citywareclient.Adapter.RecyclerView.ExpireDiscountRecyclerViewAdapter;
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
import ir.rayas.app.citywareclient.View.MasterChildren.DiscountActivity;
import ir.rayas.app.citywareclient.ViewModel.Marketing.MarketingCustomerViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpireDiscountFragment extends Fragment implements IResponseService, ILoadData {

    private DiscountActivity Context = null;

    private SwipeRefreshLayout RefreshDiscountSwipeRefreshLayoutExpireDiscountFragment = null;
    private TextViewPersian ShowEmptyDiscountTextViewExpireDiscountFragment = null;

    private ExpireDiscountRecyclerViewAdapter expireDiscountRecyclerViewAdapter = null;

    private boolean IsSwipe = false;
    private int PageNumber = 1;
    private boolean IsLoadedDataForFirst = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (DiscountActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_expire_discount, container, false);

        //طرحبندی ویو
        CreateLayout(CurrentView);
        //برای فهمیدن کد فرگنت به BusinessCommissionPagerAdapter مراجعه کنید
        Context.setFragmentIndex(0);

        return CurrentView;
    }

    public void LoadData() {
        if (!IsSwipe)
            if (PageNumber == 1)
                Context.ShowLoadingProgressBar();

        Context.setRetryType(2);
        MarketingService MarketingService = new MarketingService(this);
        MarketingService.GetCustomerPercents(PageNumber);
    }

    /**
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    private void CreateLayout(View CurrentView) {
        ShowEmptyDiscountTextViewExpireDiscountFragment = CurrentView.findViewById(R.id.ShowEmptyDiscountTextViewExpireDiscountFragment);
        RefreshDiscountSwipeRefreshLayoutExpireDiscountFragment = CurrentView.findViewById(R.id.RefreshDiscountSwipeRefreshLayoutExpireDiscountFragment);
        ShowEmptyDiscountTextViewExpireDiscountFragment.setVisibility(View.GONE);

        RecyclerView discountRecyclerViewExpireDiscountFragment = CurrentView.findViewById(R.id.DiscountRecyclerViewExpireDiscountFragment);
        discountRecyclerViewExpireDiscountFragment.setHasFixedSize(true);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(Context);
        discountRecyclerViewExpireDiscountFragment.setLayoutManager(LinearLayoutManager);


        expireDiscountRecyclerViewAdapter = new ExpireDiscountRecyclerViewAdapter( null, discountRecyclerViewExpireDiscountFragment);
        discountRecyclerViewExpireDiscountFragment.setAdapter(expireDiscountRecyclerViewAdapter);


        RefreshDiscountSwipeRefreshLayoutExpireDiscountFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                IsSwipe = true;
                PageNumber = 1;
                LoadData();
            }
        });
    }


    /**
     * @param Data
     * @param ServiceMethod
     * @param <T>
     */
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        RefreshDiscountSwipeRefreshLayoutExpireDiscountFragment.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.CustomerPercentsGet) {
                Feedback<List<MarketingCustomerViewModel>> FeedBack = (Feedback<List<MarketingCustomerViewModel>>) Data;


                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<MarketingCustomerViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        if (PageNumber == 1) {
                            if (ViewModelList.size() < 1) {
                                ShowEmptyDiscountTextViewExpireDiscountFragment.setVisibility(View.VISIBLE);
                            } else {
                                ShowEmptyDiscountTextViewExpireDiscountFragment.setVisibility(View.GONE);
                                expireDiscountRecyclerViewAdapter.SetViewModelList(ViewModelList);

                                if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                    PageNumber = PageNumber + 1;
                                    LoadData();
                                }
                            }

                        } else {
                            ShowEmptyDiscountTextViewExpireDiscountFragment.setVisibility(View.GONE);
                            expireDiscountRecyclerViewAdapter.AddViewModelList(ViewModelList);

                            if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                PageNumber = PageNumber + 1;
                                LoadData();
                            }
                        }
                    }
                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    if (PageNumber > 1) {
                        ShowEmptyDiscountTextViewExpireDiscountFragment.setVisibility(View.GONE);
                    } else {
                        ShowEmptyDiscountTextViewExpireDiscountFragment.setVisibility(View.VISIBLE);
                    }
                } else {
                    ShowEmptyDiscountTextViewExpireDiscountFragment.setVisibility(View.GONE);
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
