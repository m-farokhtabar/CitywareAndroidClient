package ir.rayas.app.citywareclient.View.Fragment.Basket;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.BasketListRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.MyClickListener;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Basket.BasketService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.Share.BasketActivity;
import ir.rayas.app.citywareclient.ViewModel.Basket.BasketViewModel;


public class BasketListFragment extends Fragment implements IResponseService, ILoadData {

    private BasketActivity Context = null;

    private TextViewPersian ShowEmptyBasketListTextViewBasketListFragment = null;
    private RecyclerView BasketListRecyclerViewBasketListFragment = null;
    private SwipeRefreshLayout RefreshBasketListSwipeRefreshLayoutBasketListFragment;
    private boolean IsSwipe = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (BasketActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_basket_list, container, false);
        //طرحبندی ویو
        CreateLayout(CurrentView);

        Context.setFragmentIndex(4);

        LoadData();

        return CurrentView;
    }

    private void CreateLayout(View CurrentView) {
        ShowEmptyBasketListTextViewBasketListFragment = CurrentView.findViewById(R.id.ShowEmptyBasketListTextViewBasketListFragment);
        RefreshBasketListSwipeRefreshLayoutBasketListFragment = CurrentView.findViewById(R.id.RefreshBasketListSwipeRefreshLayoutBasketListFragment);
        BasketListRecyclerViewBasketListFragment = CurrentView.findViewById(R.id.BasketListRecyclerViewBasketListFragment);
        BasketListRecyclerViewBasketListFragment.setHasFixedSize(true);
        //به دلیل اینکه من در هر سطر یک گزینه نیاز دارم
        LinearLayoutManager RegionLinearLayoutManager = new LinearLayoutManager(Context);
        BasketListRecyclerViewBasketListFragment.setLayoutManager(RegionLinearLayoutManager);

        ShowEmptyBasketListTextViewBasketListFragment.setVisibility(View.GONE);

        RefreshBasketListSwipeRefreshLayoutBasketListFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                IsSwipe = true;
                LoadData();

            }
        });
    }

    public void LoadData() {
        if (!IsSwipe)
            Context.ShowLoadingProgressBar();

        BasketService basketService = new BasketService(this);
        Context.setRetryType(2);
        basketService.GetAll();

    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        RefreshBasketListSwipeRefreshLayoutBasketListFragment.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.BasketGetAll) {
                Feedback<List<BasketViewModel>> FeedBack = (Feedback<List<BasketViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    final List<BasketViewModel> ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {

                        ShowEmptyBasketListTextViewBasketListFragment.setVisibility(View.GONE);


                        //تنظیمات مربوط به recycle سبد خرید
//                        BasketListRecyclerViewAdapter basketListRecyclerViewAdapter = new BasketListRecyclerViewAdapter(Context, ViewModel, BasketListRecyclerViewBasketListFragment);
//                        BasketListRecyclerViewBasketListFragment.setAdapter(basketListRecyclerViewAdapter);
//                        basketListRecyclerViewAdapter.notifyDataSetChanged();
//                        BasketListRecyclerViewBasketListFragment.invalidate();

//                        basketListRecyclerViewAdapter.setOnItemClickListener(new MyClickListener() {
//                            @Override
//                            public void onItemClick(int position, View v) {
//
//                                Context.basketSummeryViewModel.setBusinessId(ViewModel.get(position).getBusinessId());
//                                Context.basketSummeryViewModel.setBasketId(ViewModel.get(position).getId());
//                                Context.basketSummeryViewModel.setBasketName(ViewModel.get(position).getBusinessName());
//                                Context.basketSummeryViewModel.setBasketCount(ViewModel.get(position).getItemList().size());
//                                Context.basketSummeryViewModel.setPath(ViewModel.get(position).getPath());
//                                Context.basketSummeryViewModel.setTotalPrice(ViewModel.get(position).getTotalPrice());
//                                Context.basketSummeryViewModel.setModified(ViewModel.get(position).getModified());
//
//                                Context.DefaultTab = Context.BasketTabLayoutBasketActivity.getTabAt(3);
//                                Context.DefaultTab.select();
//
////                                BasketItemListFragment basketItemListFragment = new BasketItemListFragment();
////                                FragmentTransaction BasketListTransaction = Context.getSupportFragmentManager().beginTransaction();
////                                BasketListTransaction.replace(R.id.BasketFrameLayoutBasketActivity, basketItemListFragment);
////                                BasketListTransaction.addToBackStack(null);
////                                BasketListTransaction.commit();
//                            }
//                        });
                    } else {
                        ShowEmptyBasketListTextViewBasketListFragment.setVisibility(View.VISIBLE);
                    }

                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    ShowEmptyBasketListTextViewBasketListFragment.setVisibility(View.VISIBLE);
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
            if (Context != null) {
                //برای فهمیدن کد فرگنت به UserProfilePagerAdapter مراجعه کنید
                Context.setFragmentIndex(4);
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }



}
