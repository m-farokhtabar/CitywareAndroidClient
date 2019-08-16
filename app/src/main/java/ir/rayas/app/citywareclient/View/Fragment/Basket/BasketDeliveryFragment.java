package ir.rayas.app.citywareclient.View.Fragment.Basket;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import java.util.HashMap;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.BasketUserAddressRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.User.UserAddressService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.Share.BasketActivity;
import ir.rayas.app.citywareclient.View.UserProfileChildren.UserAddressSetActivity;
import ir.rayas.app.citywareclient.ViewModel.User.UserAddressViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class BasketDeliveryFragment extends Fragment implements IResponseService, ILoadData {

    private BasketActivity Context = null;
    private RecyclerView AddressRecyclerViewBasketDeliveryFragment = null;
    private SwipeRefreshLayout RefreshAddressSwipeRefreshLayoutBasketDeliveryFragment;
    private boolean IsSwipe = false;
    private boolean IsLoadedDataForFirst = false;

    private  BasketUserAddressRecyclerViewAdapter basketUserAddressRecyclerViewAdapter = null;

    public  BasketUserAddressRecyclerViewAdapter getBasketUserAddressRecyclerViewAdapter() {
        return basketUserAddressRecyclerViewAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (BasketActivity) getActivity();

        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_basket_delivery, container, false);


        //ایجاد طرحبندی صفحه
        CreateLayout(CurrentView);

    //    LoadData();

        return CurrentView;
    }

    private void CreateLayout(View CurrentView) {

        final FloatingActionButton NewAddressFloatingActionButtonBasketDeliveryFragment = CurrentView.findViewById(R.id.NewAddressFloatingActionButtonBasketDeliveryFragment);
        ButtonPersianView NextButtonBasketDeliveryFragment = CurrentView.findViewById(R.id.NextButtonBasketDeliveryFragment);
        ButtonPersianView ReturnButtonBasketDeliveryFragment = CurrentView.findViewById(R.id.ReturnButtonBasketDeliveryFragment);
        RefreshAddressSwipeRefreshLayoutBasketDeliveryFragment = CurrentView.findViewById(R.id.RefreshAddressSwipeRefreshLayoutBasketDeliveryFragment);
        AddressRecyclerViewBasketDeliveryFragment = CurrentView.findViewById(R.id.AddressRecyclerViewBasketDeliveryFragment);
        AddressRecyclerViewBasketDeliveryFragment.setHasFixedSize(true);
        //به دلیل اینکه من در هر سطر یک گزینه نیاز دارم
        LinearLayoutManager RegionLinearLayoutManager = new LinearLayoutManager(Context);
        AddressRecyclerViewBasketDeliveryFragment.setLayoutManager(RegionLinearLayoutManager);

        NewAddressFloatingActionButtonBasketDeliveryFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent NewAddressIntent = Context.NewIntent(UserAddressSetActivity.class);
                NewAddressIntent.putExtra("SetAddress", "New");
                Context.startActivity(NewAddressIntent);

            }
        });

        RefreshAddressSwipeRefreshLayoutBasketDeliveryFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                IsSwipe = true;
                LoadData();

            }
        });

        ReturnButtonBasketDeliveryFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context.DefaultTab = Context.BasketTabLayoutBasketActivity.getTabAt(2);
                Context.DefaultTab.select();
              //  getFragmentManager().popBackStack();
            }
        });

        NextButtonBasketDeliveryFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectNextButtonClick();
            }
        });

        AddressRecyclerViewBasketDeliveryFragment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && NewAddressFloatingActionButtonBasketDeliveryFragment.getVisibility() == View.VISIBLE) {
                    NewAddressFloatingActionButtonBasketDeliveryFragment.hide();
                } else if (dy < 0 && NewAddressFloatingActionButtonBasketDeliveryFragment.getVisibility() != View.VISIBLE) {
                    NewAddressFloatingActionButtonBasketDeliveryFragment.show();
                }
            }
        });

    }


    private void SelectNextButtonClick() {
        if (basketUserAddressRecyclerViewAdapter.getUserAddressId() != 0) {

            Context.basketSummeryViewModel.setCurrentAddress(basketUserAddressRecyclerViewAdapter.getSelectAddress());
            Context.basketSummeryViewModel.setUserAddressId(basketUserAddressRecyclerViewAdapter.getUserAddressId());
            Context.basketSummeryViewModel.setLastSelectedPositionAddress(basketUserAddressRecyclerViewAdapter.getLastSelectedPosition());
            Context.basketSummeryViewModel.setUserLatitude(basketUserAddressRecyclerViewAdapter.getLatitude());
            Context.basketSummeryViewModel.setUserLongitude(basketUserAddressRecyclerViewAdapter.getLongitude());
//            BasketSummeryFragment basketSummeryFragment = new BasketSummeryFragment();
//            FragmentTransaction BasketListTransaction = Context.getSupportFragmentManager().beginTransaction();
//            BasketListTransaction.replace(R.id.BasketFrameLayoutBasketActivity, basketSummeryFragment);
//            BasketListTransaction.addToBackStack(null);
//            BasketListTransaction.commit();

            Context.DefaultTab = Context.BasketTabLayoutBasketActivity.getTabAt(0);
            Context.DefaultTab.select();

        } else {
            Context.ShowToast(getResources().getString(R.string.please_select_address_for_order), Toast.LENGTH_SHORT, MessageType.Warning);
        }
    }


    public void LoadData() {
        if (!IsSwipe)
            Context.ShowLoadingProgressBar();

        UserAddressService userAddressService = new UserAddressService(this);
        Context.setRetryType(2);
        userAddressService.GetAll();

    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        RefreshAddressSwipeRefreshLayoutBasketDeliveryFragment.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.UserGetAllAddress) {
                Feedback<List<UserAddressViewModel>> FeedBack = (Feedback<List<UserAddressViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    List<UserAddressViewModel> ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {

                        //تنظیمات مربوط به recycle آدرس
                        basketUserAddressRecyclerViewAdapter = new BasketUserAddressRecyclerViewAdapter(Context, ViewModel, AddressRecyclerViewBasketDeliveryFragment);
                        AddressRecyclerViewBasketDeliveryFragment.setAdapter(basketUserAddressRecyclerViewAdapter);
                        basketUserAddressRecyclerViewAdapter.notifyDataSetChanged();
                        AddressRecyclerViewBasketDeliveryFragment.invalidate();
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
