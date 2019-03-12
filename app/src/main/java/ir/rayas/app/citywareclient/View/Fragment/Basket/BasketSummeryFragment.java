package ir.rayas.app.citywareclient.View.Fragment.Basket;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Business.BusinessService;
import ir.rayas.app.citywareclient.Service.Factor.UserFactorService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.Share.BasketActivity;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorInViewModel;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class BasketSummeryFragment extends Fragment implements IResponseService, ILoadData {

    private BasketActivity Context = null;
    private TextViewPersian TotalPriceTextViewBasketSummeryFragment = null;
    private TextViewPersian PriceDeliveryTextViewBasketSummeryFragment = null;
    private LinearLayout PriceDeliveryLinearLayoutBasketSummeryFragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (BasketActivity) getActivity();

        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_basket_summery, container, false);

        //ایجاد طرحبندی صفحه
        CreateLayout(CurrentView);

        LoadData();

        return CurrentView;
    }

    private void CreateLayout(View CurrentView) {

        ButtonPersianView SendBasketButtonBasketSummeryFragment = CurrentView.findViewById(R.id.SendBasketButtonBasketSummeryFragment);
        ButtonPersianView ReturnButtonBasketSummeryFragment = CurrentView.findViewById(R.id.ReturnButtonBasketSummeryFragment);
        ImageView ImageBasketImageViewBasketSummeryFragment = CurrentView.findViewById(R.id.ImageBasketImageViewBasketSummeryFragment);
        SwitchCompat HasDeliverySwitchBasketSummeryFragment = CurrentView.findViewById(R.id.HasDeliverySwitchBasketSummeryFragment);
        PriceDeliveryLinearLayoutBasketSummeryFragment = CurrentView.findViewById(R.id.PriceDeliveryLinearLayoutBasketSummeryFragment);
        CardView AddressLinearLayoutBasketSummeryFragment = CurrentView.findViewById(R.id.AddressLinearLayoutBasketSummeryFragment);
        CardView DescriptionLinearLayoutBasketSummeryFragment = CurrentView.findViewById(R.id.DescriptionLinearLayoutBasketSummeryFragment);
        TextViewPersian CreateDateBasketTextViewBasketSummeryFragment = CurrentView.findViewById(R.id.CreateDateBasketTextViewBasketSummeryFragment);
        TextViewPersian NumberOfOrderItemsBasketTextViewBasketSummeryFragment = CurrentView.findViewById(R.id.NumberOfOrderItemsBasketTextViewBasketSummeryFragment);
        TextViewPersian PricePayableBasketTextViewBasketSummeryFragment = CurrentView.findViewById(R.id.PricePayableBasketTextViewBasketSummeryFragment);
        TextViewPersian HasDeliveryTitleTextViewBasketSummeryFragment = CurrentView.findViewById(R.id.HasDeliveryTitleTextViewBasketSummeryFragment);
        TextViewPersian HasNotDeliveryTitleTextViewBasketSummeryFragment = CurrentView.findViewById(R.id.HasNotDeliveryTitleTextViewBasketSummeryFragment);
        PriceDeliveryTextViewBasketSummeryFragment = CurrentView.findViewById(R.id.PriceDeliveryTextViewBasketSummeryFragment);
        TextViewPersian AddressTextViewBasketSummeryFragment = CurrentView.findViewById(R.id.AddressTextViewBasketSummeryFragment);
        TextViewPersian DescriptionTextViewBasketSummeryFragment = CurrentView.findViewById(R.id.DescriptionTextViewBasketSummeryFragment);
        TextViewPersian BasketBusinessNameTextViewBasketSummeryFragment = CurrentView.findViewById(R.id.BasketBusinessNameTextViewBasketSummeryFragment);
        TextViewPersian DescriptionBasketTextViewBasketSummeryFragment = CurrentView.findViewById(R.id.DescriptionBasketTextViewBasketSummeryFragment);
        TotalPriceTextViewBasketSummeryFragment = CurrentView.findViewById(R.id.TotalPriceTextViewBasketSummeryFragment);


        CardView BasketDetailsCardViewBasketSummeryFragment = CurrentView.findViewById(R.id.BasketDetailsCardViewBasketSummeryFragment);
        BasketDetailsCardViewBasketSummeryFragment.setVisibility(View.GONE);

        if (Context.basketSummeryViewModel.getPath().equals("")) {
            ImageBasketImageViewBasketSummeryFragment.setImageResource(R.drawable.image_default);
        } else {
            LayoutUtility.LoadImageWithGlide(Context, Context.basketSummeryViewModel.getPath(), ImageBasketImageViewBasketSummeryFragment);
        }

        BasketBusinessNameTextViewBasketSummeryFragment.setText(Context.basketSummeryViewModel.getBasketName());
        CreateDateBasketTextViewBasketSummeryFragment.setText(Context.basketSummeryViewModel.getModified());
        NumberOfOrderItemsBasketTextViewBasketSummeryFragment.setText(String.valueOf(Context.basketSummeryViewModel.getBasketCount()));

        if (Context.basketSummeryViewModel.getTotalPrice() < 0)
            DescriptionBasketTextViewBasketSummeryFragment.setText(Context.getResources().getString(R.string.unknown));
        else
            PricePayableBasketTextViewBasketSummeryFragment.setText(Utility.GetIntegerNumberWithComma(Context.basketSummeryViewModel.getTotalPrice()) + " " + Context.getResources().getString(R.string.toman));


        if (Context.basketSummeryViewModel.isQuickItem())
            DescriptionBasketTextViewBasketSummeryFragment.setText(Context.getResources().getString(R.string.in_your_basket_there_are_products_that_are_not_priced));
        else
            DescriptionBasketTextViewBasketSummeryFragment.setText("");


        if (Context.basketSummeryViewModel.isBusinessIsDelivery()) {
            HasDeliverySwitchBasketSummeryFragment.setVisibility(View.VISIBLE);
            HasDeliveryTitleTextViewBasketSummeryFragment.setVisibility(View.VISIBLE);
            HasNotDeliveryTitleTextViewBasketSummeryFragment.setVisibility(View.GONE);
            HasDeliverySwitchBasketSummeryFragment.setChecked(Context.basketSummeryViewModel.isDelivery());
            HasDeliverySwitchBasketSummeryFragment.setClickable(false);

            if (Context.basketSummeryViewModel.isDelivery()) {
                AddressLinearLayoutBasketSummeryFragment.setVisibility(View.VISIBLE);
                AddressTextViewBasketSummeryFragment.setText(Context.basketSummeryViewModel.getCurrentAddress());
            } else {
                AddressLinearLayoutBasketSummeryFragment.setVisibility(View.GONE);
            }
        } else {
            HasDeliverySwitchBasketSummeryFragment.setVisibility(View.GONE);
            HasDeliveryTitleTextViewBasketSummeryFragment.setVisibility(View.GONE);
            HasNotDeliveryTitleTextViewBasketSummeryFragment.setVisibility(View.VISIBLE);

            AddressLinearLayoutBasketSummeryFragment.setVisibility(View.GONE);
        }


        if (Context.basketSummeryViewModel.getUserDescription().equals("")) {
            DescriptionLinearLayoutBasketSummeryFragment.setVisibility(View.GONE);
        } else {
            DescriptionLinearLayoutBasketSummeryFragment.setVisibility(View.VISIBLE);
            DescriptionTextViewBasketSummeryFragment.setText(Context.basketSummeryViewModel.getUserDescription());
        }

        ReturnButtonBasketSummeryFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        SendBasketButtonBasketSummeryFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context.ShowLoadingProgressBar();
                //ثبت اطلاعات
                UserFactorService Service = new UserFactorService(BasketSummeryFragment.this);
                Service.Add(MadeViewModel());
            }
        });


    }

    @Override
    public void LoadData() {

//        if (Context.basketSummeryViewModel.isDelivery()) {
//            Context.ShowLoadingProgressBar();
//            UserFactorService userFactorService = new UserFactorService(this);
//            userFactorService.GetDeliveryPrice(Context.basketSummeryViewModel.getUserLatitude(), Context.basketSummeryViewModel.getUserLongitude(), Context.basketSummeryViewModel.getBasketLatitude(), Context.basketSummeryViewModel.getBasketLongitude());
//        } else {
        if (Context.basketSummeryViewModel.getTotalPrice() < 0) {
            TotalPriceTextViewBasketSummeryFragment.setText(Context.getResources().getString(R.string.unknown));
        } else {
            PriceDeliveryLinearLayoutBasketSummeryFragment.setVisibility(View.GONE);
            TotalPriceTextViewBasketSummeryFragment.setText(Utility.GetIntegerNumberWithComma(Context.basketSummeryViewModel.getTotalPrice()) + " " + Context.getResources().getString(R.string.toman));
        }
        //}
    }

    private FactorInViewModel MadeViewModel() {


        FactorInViewModel ViewModel = new FactorInViewModel();
        ViewModel.setFactorId(Context.basketSummeryViewModel.getBasketId());
        ViewModel.setUserId(Context.basketSummeryViewModel.getUserId());
        ViewModel.setDelivery(Context.basketSummeryViewModel.isDelivery());
        ViewModel.setUserAddress(Context.basketSummeryViewModel.getCurrentAddress());
        ViewModel.setUserDescription(Context.basketSummeryViewModel.getUserDescription());
        ViewModel.setUserLatitude(Context.basketSummeryViewModel.getUserLatitude());
        ViewModel.setUserLongitude(Context.basketSummeryViewModel.getUserLongitude());

        return ViewModel;
    }


    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();

        try {
            if (ServiceMethod == ServiceMethodType.DeliveryPriceGet) {
                Feedback<Double> FeedBack = (Feedback<Double>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    BusinessService BusinessService = new BusinessService(this);
                    BusinessService.Get(Context.basketSummeryViewModel.getBusinessId());

                    Double DeliveryPrice = FeedBack.getValue();
                    if (DeliveryPrice != null) {

                        PriceDeliveryTextViewBasketSummeryFragment.setText(Utility.GetIntegerNumberWithComma(DeliveryPrice) + " " + Context.getResources().getString(R.string.toman));

                        if (Context.basketSummeryViewModel.getTotalPrice() < 0) {
                            TotalPriceTextViewBasketSummeryFragment.setText(Context.getResources().getString(R.string.unknown));
                        } else {
                            double TotalPrice = (DeliveryPrice + Context.basketSummeryViewModel.getTotalPrice());
                            TotalPriceTextViewBasketSummeryFragment.setText(Utility.GetIntegerNumberWithComma(TotalPrice) + " " + Context.getResources().getString(R.string.toman));
                        }

                    } else {

                        PriceDeliveryTextViewBasketSummeryFragment.setText(Context.getResources().getString(R.string.unknown));
                        TotalPriceTextViewBasketSummeryFragment.setText(Context.getResources().getString(R.string.unknown));

                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.FactorAdd) {
                Feedback<FactorViewModel> FeedBack = (Feedback<FactorViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.RegisteredSuccessful.getId()) {

                    if (FeedBack.getValue() != null) {
                        Context.finish();
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
