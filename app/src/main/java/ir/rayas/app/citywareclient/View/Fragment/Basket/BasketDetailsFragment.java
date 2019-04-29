package ir.rayas.app.citywareclient.View.Fragment.Basket;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Basket.BasketService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.Share.BasketActivity;
import ir.rayas.app.citywareclient.ViewModel.Basket.BasketDeliveryAndUserDescriptionViewModel;
import ir.rayas.app.citywareclient.ViewModel.Basket.BasketViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class BasketDetailsFragment extends Fragment implements IResponseService, ILoadData {

    private BasketActivity Context = null;

    private SwitchCompat HasDeliverySwitchBasketDetailsFragment = null;
    private EditText DescriptionEditTextBasketDetailsFragment = null;

    private boolean IsLoadedDataForFirst = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (BasketActivity) getActivity();

        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_basket_details, container, false);


        //ایجاد طرحبندی صفحه
        CreateLayout(CurrentView);


        return CurrentView;
    }

    private void CreateLayout(View CurrentView) {
        HasDeliverySwitchBasketDetailsFragment = CurrentView.findViewById(R.id.HasDeliverySwitchBasketDetailsFragment);
        TextViewPersian HasDeliveryTitleTextViewBasketDetailsFragment = CurrentView.findViewById(R.id.HasDeliveryTitleTextViewBasketDetailsFragment);
        TextViewPersian HasNotDeliveryTitleTextViewBasketDetailsFragment = CurrentView.findViewById(R.id.HasNotDeliveryTitleTextViewBasketDetailsFragment);
        ButtonPersianView NextButtonBasketDetailsFragment = CurrentView.findViewById(R.id.NextButtonBasketDetailsFragment);
        ButtonPersianView ReturnButtonBasketDetailsFragment = CurrentView.findViewById(R.id.ReturnButtonBasketDetailsFragment);
        DescriptionEditTextBasketDetailsFragment = CurrentView.findViewById(R.id.DescriptionEditTextBasketDetailsFragment);
        CardView HasDeliveryCardViewBasketDetailsFragment = CurrentView.findViewById(R.id.HasDeliveryCardViewBasketDetailsFragment);

        DescriptionEditTextBasketDetailsFragment.setText(Context.basketSummeryViewModel.getUserDescription());
        HasDeliverySwitchBasketDetailsFragment.setChecked(Context.basketSummeryViewModel.isDelivery());


        if (Context.basketSummeryViewModel.isBusinessIsDelivery()) {
            HasDeliverySwitchBasketDetailsFragment.setVisibility(View.GONE);
            HasDeliveryTitleTextViewBasketDetailsFragment.setVisibility(View.GONE);
            HasNotDeliveryTitleTextViewBasketDetailsFragment.setVisibility(View.GONE);
            HasDeliveryCardViewBasketDetailsFragment.setVisibility(View.GONE);
        } else {
            HasDeliverySwitchBasketDetailsFragment.setVisibility(View.GONE);
            HasDeliveryTitleTextViewBasketDetailsFragment.setVisibility(View.GONE);
            HasNotDeliveryTitleTextViewBasketDetailsFragment.setVisibility(View.GONE);
            HasDeliveryCardViewBasketDetailsFragment.setVisibility(View.GONE);
        }

        ReturnButtonBasketDetailsFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        NextButtonBasketDetailsFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context.ShowLoadingProgressBar();
                BasketService basketService = new BasketService(BasketDetailsFragment.this);
                Context.setRetryType(1);
                basketService.EditUserDescriptionAndDelivery(MadeViewModel(), Context.basketSummeryViewModel.getBasketId());
            }
        });

    }

    private BasketDeliveryAndUserDescriptionViewModel MadeViewModel() {
        BasketDeliveryAndUserDescriptionViewModel ViewModel = new BasketDeliveryAndUserDescriptionViewModel();
        try {
            ViewModel.setUserDescription(DescriptionEditTextBasketDetailsFragment.getText().toString());
            if (HasDeliverySwitchBasketDetailsFragment.getVisibility() == View.GONE) {
                ViewModel.setDelivery(true);

            } else {
                ViewModel.setDelivery(HasDeliverySwitchBasketDetailsFragment.isChecked());
            }
        } catch (Exception ignored) {
        }
        return ViewModel;
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();

        try {
            if (ServiceMethod == ServiceMethodType.BasketEditUserDescriptionAndDelivery) {
                Feedback<BasketViewModel> FeedBack = (Feedback<BasketViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.UpdatedSuccessful.getId()) {

                    BasketViewModel ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {

                        Context.basketSummeryViewModel.setDelivery(true);
                        Context.basketSummeryViewModel.setUserDescription(ViewModel.getUserDescription());

                        if (Context.basketSummeryViewModel.isDelivery()) {
                            BasketDeliveryFragment basketDeliveryFragment = new BasketDeliveryFragment();
                            FragmentTransaction BasketListTransaction = Context.getSupportFragmentManager().beginTransaction();
                            BasketListTransaction.replace(R.id.BasketFrameLayoutBasketActivity, basketDeliveryFragment);
                            BasketListTransaction.addToBackStack(null);
                            BasketListTransaction.commit();

                        } else {
                            BasketSummeryFragment basketSummeryFragment = new BasketSummeryFragment();
                            FragmentTransaction BasketListTransaction = Context.getSupportFragmentManager().beginTransaction();
                            BasketListTransaction.replace(R.id.BasketFrameLayoutBasketActivity, basketSummeryFragment);
                            BasketListTransaction.addToBackStack(null);
                            BasketListTransaction.commit();

                            Context.basketSummeryViewModel.setCurrentAddress("");
                        }
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
            Context.setFragmentIndex(2);
            if (!IsLoadedDataForFirst) {
                IsLoadedDataForFirst = true;
                //دریافت اطلاعات از سرور
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }


    @Override
    public void LoadData() {

    }
}
