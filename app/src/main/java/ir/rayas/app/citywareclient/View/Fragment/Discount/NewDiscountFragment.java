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

import ir.rayas.app.citywareclient.Adapter.RecyclerView.DiscountRecyclerViewAdapter;
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
public class NewDiscountFragment extends Fragment implements IResponseService, ILoadData {

    private DiscountActivity Context = null;

    private SwipeRefreshLayout RefreshDiscountSwipeRefreshLayoutNewDiscountFragment = null;
    private TextViewPersian ShowEmptyDiscountTextViewNewDiscountFragment = null;

    private DiscountRecyclerViewAdapter discountRecyclerViewAdapter = null;

    private boolean IsSwipe = false;
    private int PageNumber = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (DiscountActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_new_discount, container, false);

        //طرحبندی ویو
        CreateLayout(CurrentView);
        //برای فهمیدن کد فرگنت به BusinessCommissionPagerAdapter مراجعه کنید
        Context.setFragmentIndex(2);

        //دریافت اطلاعات از سرور
        LoadData();

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
        ShowEmptyDiscountTextViewNewDiscountFragment = CurrentView.findViewById(R.id.ShowEmptyDiscountTextViewNewDiscountFragment);
        RefreshDiscountSwipeRefreshLayoutNewDiscountFragment = CurrentView.findViewById(R.id.RefreshDiscountSwipeRefreshLayoutNewDiscountFragment);
        ShowEmptyDiscountTextViewNewDiscountFragment.setVisibility(View.GONE);

        RecyclerView discountRecyclerViewNewDiscountFragment = CurrentView.findViewById(R.id.DiscountRecyclerViewNewDiscountFragment);
        discountRecyclerViewNewDiscountFragment.setHasFixedSize(true);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(Context);
        discountRecyclerViewNewDiscountFragment.setLayoutManager(LinearLayoutManager);


        discountRecyclerViewAdapter = new DiscountRecyclerViewAdapter(Context, null, discountRecyclerViewNewDiscountFragment);
        discountRecyclerViewNewDiscountFragment.setAdapter(discountRecyclerViewAdapter);


        RefreshDiscountSwipeRefreshLayoutNewDiscountFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        RefreshDiscountSwipeRefreshLayoutNewDiscountFragment.setRefreshing(false);
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
                                ShowEmptyDiscountTextViewNewDiscountFragment.setVisibility(View.VISIBLE);
                            } else {
                                ShowEmptyDiscountTextViewNewDiscountFragment.setVisibility(View.GONE);
                                discountRecyclerViewAdapter.SetViewModelList(ViewModelList);

                                if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                    PageNumber = PageNumber + 1;
                                    LoadData();
                                }
                            }

                        } else {
                            ShowEmptyDiscountTextViewNewDiscountFragment.setVisibility(View.GONE);
                            discountRecyclerViewAdapter.AddViewModelList(ViewModelList);

                            if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                PageNumber = PageNumber + 1;
                                LoadData();
                            }
                        }
                    }
                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    if (PageNumber > 1) {
                        ShowEmptyDiscountTextViewNewDiscountFragment.setVisibility(View.GONE);
                    } else {
                        ShowEmptyDiscountTextViewNewDiscountFragment.setVisibility(View.VISIBLE);
                    }
                } else {
                    ShowEmptyDiscountTextViewNewDiscountFragment.setVisibility(View.GONE);
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
            if (Context != null) {
                //برای فهمیدن کد فرگنت به BusinessCommissionPagerAdapter مراجعه کنید
                Context.setFragmentIndex(2);
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}
