package ir.rayas.app.citywareclient.View.Fragment.Basket;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.BasketItemListRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Basket.BasketService;
import ir.rayas.app.citywareclient.Service.Business.BusinessService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.Share.BasketActivity;
import ir.rayas.app.citywareclient.ViewModel.Basket.BasketSummeryViewModel;
import ir.rayas.app.citywareclient.ViewModel.Basket.BasketViewModel;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class BasketItemListFragment extends Fragment implements IResponseService, ILoadData {

    private BasketActivity Context = null;

    private RecyclerView BasketItemListRecyclerViewBasketItemListFragment = null;
    private TextViewPersian BasketBusinessNameTextViewBasketItemListFragment = null;
    private SwipeRefreshLayout RefreshBasketItemListSwipeRefreshLayoutBasketItemListFragment;

    private BasketItemListRecyclerViewAdapter basketItemListRecyclerViewAdapter = null;

    private boolean IsSwipe = false;
    private boolean IsLoadedDataForFirst = false;
    private boolean IsDelivery = false;
    private String UserDescription = "";

    private BasketViewModel ViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (BasketActivity) getActivity();

        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_basket_item_list, container, false);

        Context.setFragmentIndex(3);
        //طرحبندی ویو
        CreateLayout(CurrentView);

        LoadData();

        return CurrentView;
    }

    private void CreateLayout(View CurrentView) {
        ButtonPersianView NextButtonBasketItemListFragment = CurrentView.findViewById(R.id.NextButtonBasketItemListFragment);
        ButtonPersianView ReturnButtonBasketItemListFragment = CurrentView.findViewById(R.id.ReturnButtonBasketItemListFragment);
        BasketBusinessNameTextViewBasketItemListFragment = CurrentView.findViewById(R.id.BasketBusinessNameTextViewBasketItemListFragment);
        RefreshBasketItemListSwipeRefreshLayoutBasketItemListFragment = CurrentView.findViewById(R.id.RefreshBasketItemListSwipeRefreshLayoutBasketItemListFragment);
        BasketItemListRecyclerViewBasketItemListFragment = CurrentView.findViewById(R.id.BasketItemListRecyclerViewBasketItemListFragment);
        BasketItemListRecyclerViewBasketItemListFragment.setHasFixedSize(true);
        //به دلیل اینکه من در هر سطر یک گزینه نیاز دارم
        LinearLayoutManager RegionLinearLayoutManager = new LinearLayoutManager(Context);
        BasketItemListRecyclerViewBasketItemListFragment.setLayoutManager(RegionLinearLayoutManager);


        RefreshBasketItemListSwipeRefreshLayoutBasketItemListFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                IsSwipe = true;
                LoadData();

            }
        });

        ReturnButtonBasketItemListFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getFragmentManager().popBackStack();
                Context.onBackPressed();
            }
        });


        NextButtonBasketItemListFragment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                boolean IsQuick = false;
                for (int i = 0; i < ViewModel.getItemList().size(); i++) {
                    if (ViewModel.getItemList().get(i).getPrice() <= 0) {
                        IsQuick = true;
                        break;
                    }
                }

                if (IsQuick)
                    Context.basketSummeryViewModel.setQuickItem(true);
                else
                    Context.basketSummeryViewModel.setQuickItem(false);

                Context.basketSummeryViewModel.setDelivery(Context.basketSummeryViewModel.isDelivery());
                Context.basketSummeryViewModel.setUserDescription(Context.basketSummeryViewModel.getUserDescription());

                Context.DefaultTab = Context.BasketTabLayoutBasketActivity.getTabAt(2);
                Context.DefaultTab.select();

//                BasketDetailsFragment basketDetailsFragment = new BasketDetailsFragment();
//                FragmentTransaction BasketListTransaction = Context. getSupportFragmentManager().beginTransaction();
//                BasketListTransaction.replace(R.id.BasketFrameLayoutBasketActivity,basketDetailsFragment);
//                BasketListTransaction.addToBackStack(null);
//                BasketListTransaction.commit();

            }
        });
    }

    public void LoadData() {
        if (!IsSwipe)
            Context.ShowLoadingProgressBar();

        BasketService basketService = new BasketService(this);
        Context.setRetryType(2);
        basketService.Get(Context.basketSummeryViewModel.getBasketId());

    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        RefreshBasketItemListSwipeRefreshLayoutBasketItemListFragment.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.BasketGet) {
                Feedback<BasketViewModel> FeedBack = (Feedback<BasketViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    BusinessService BusinessService = new BusinessService(this);
                    BusinessService.Get(Context.basketSummeryViewModel.getBusinessId());

                    ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {

                        IsDelivery = ViewModel.isDelivery();
                        UserDescription = ViewModel.getUserDescription();


                        Context.basketSummeryViewModel.setDelivery(IsDelivery);
                        Context.basketSummeryViewModel.setUserDescription(UserDescription);
                        BasketBusinessNameTextViewBasketItemListFragment.setText(Context.basketSummeryViewModel.getBasketName());


                        //تنظیمات مربوط به recycle سبد خرید
                        basketItemListRecyclerViewAdapter = new BasketItemListRecyclerViewAdapter(Context, ViewModel.getItemList(), BasketItemListRecyclerViewBasketItemListFragment, ViewModel.getId());
                        BasketItemListRecyclerViewBasketItemListFragment.setAdapter(basketItemListRecyclerViewAdapter);
                        basketItemListRecyclerViewAdapter.notifyDataSetChanged();
                        BasketItemListRecyclerViewBasketItemListFragment.invalidate();
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.BusinessGet) {
                Feedback<BusinessViewModel> FeedBack = (Feedback<BusinessViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    BusinessViewModel ViewModel = FeedBack.getValue();

                    if (ViewModel != null) {
                        Context.basketSummeryViewModel.setBusinessIsDelivery(ViewModel.isHasDelivery());
                        Context.basketSummeryViewModel.setBasketLatitude(ViewModel.getLatitude());
                        Context.basketSummeryViewModel.setBasketLongitude(ViewModel.getLongitude());
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


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (Context != null) {
                //برای فهمیدن کد فرگنت به UserProfilePagerAdapter مراجعه کنید
                Context.setFragmentIndex(3);
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        if (isVisibleToUser) {
//            //برای فهمیدن کد فرگنت به UserProfilePagerAdapter مراجعه کنید
//            Context.setFragmentIndex(3);
//            if (!IsLoadedDataForFirst) {
//                IsSwipe = false;
//                IsLoadedDataForFirst = true;
//                //دریافت اطلاعات از سرور
//                LoadData();
//            }
//        }
//        super.setUserVisibleHint(isVisibleToUser);
//    }


}
