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

import ir.rayas.app.citywareclient.Adapter.RecyclerView.UsageDiscountRecyclerViewAdapter;
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
public class CustomerUsageDiscountFragment extends Fragment implements IResponseService, ILoadData {

    private DiscountActivity Context = null;

    private SwipeRefreshLayout RefreshDiscountSwipeRefreshLayoutUsageDiscountFragment = null;
    private TextViewPersian ShowEmptyDiscountTextViewUsageDiscountFragment = null;

    private UsageDiscountRecyclerViewAdapter usageDiscountRecyclerViewAdapter = null;

    private boolean IsSwipe = false;
    private int PageNumber = 1;
    private boolean IsLoadedDataForFirst = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (DiscountActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_customer_usage_discount, container, false);

        //طرحبندی ویو
        CreateLayout(CurrentView);
        //برای فهمیدن کد فرگنت به BusinessCommissionPagerAdapter مراجعه کنید
        Context.setFragmentIndex(1);

        return CurrentView;
    }

    public void LoadData() {
        if (!IsSwipe)
            if (PageNumber == 1)
                Context.ShowLoadingProgressBar();

        Context.setRetryType(2);
        MarketingService MarketingService = new MarketingService(this);
        MarketingService.GetAllCustomerUsedDiscounts(PageNumber);
    }

    /**
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    private void CreateLayout(View CurrentView) {
        ShowEmptyDiscountTextViewUsageDiscountFragment = CurrentView.findViewById(R.id.ShowEmptyDiscountTextViewUsageDiscountFragment);
        RefreshDiscountSwipeRefreshLayoutUsageDiscountFragment = CurrentView.findViewById(R.id.RefreshDiscountSwipeRefreshLayoutUsageDiscountFragment);
        ShowEmptyDiscountTextViewUsageDiscountFragment.setVisibility(View.GONE);

        RecyclerView discountRecyclerViewUsageDiscountFragment = CurrentView.findViewById(R.id.DiscountRecyclerViewUsageDiscountFragment);
        discountRecyclerViewUsageDiscountFragment.setHasFixedSize(true);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(Context);
        discountRecyclerViewUsageDiscountFragment.setLayoutManager(LinearLayoutManager);


        usageDiscountRecyclerViewAdapter = new UsageDiscountRecyclerViewAdapter(Context, null, discountRecyclerViewUsageDiscountFragment);
        discountRecyclerViewUsageDiscountFragment.setAdapter(usageDiscountRecyclerViewAdapter);


        RefreshDiscountSwipeRefreshLayoutUsageDiscountFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        RefreshDiscountSwipeRefreshLayoutUsageDiscountFragment.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.GetAllCustomerUsedDiscounts) {
                Feedback<List<MarketingCustomerViewModel>> FeedBack = (Feedback<List<MarketingCustomerViewModel>>) Data;


                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<MarketingCustomerViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        if (PageNumber == 1) {
                            if (ViewModelList.size() < 1) {
                                ShowEmptyDiscountTextViewUsageDiscountFragment.setVisibility(View.VISIBLE);
                            } else {
                                ShowEmptyDiscountTextViewUsageDiscountFragment.setVisibility(View.GONE);
                                usageDiscountRecyclerViewAdapter.SetViewModelList(ViewModelList);

                                if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                    PageNumber = PageNumber + 1;
                                    LoadData();
                                }
                            }

                        } else {
                            ShowEmptyDiscountTextViewUsageDiscountFragment.setVisibility(View.GONE);
                            usageDiscountRecyclerViewAdapter.AddViewModelList(ViewModelList);

                            if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                PageNumber = PageNumber + 1;
                                LoadData();
                            }
                        }
                    }
                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    if (PageNumber > 1) {
                        ShowEmptyDiscountTextViewUsageDiscountFragment.setVisibility(View.GONE);
                    } else {
                        ShowEmptyDiscountTextViewUsageDiscountFragment.setVisibility(View.VISIBLE);
                    }
                } else {
                    ShowEmptyDiscountTextViewUsageDiscountFragment.setVisibility(View.GONE);
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
