package ir.rayas.app.citywareclient.View.Fragment.Poster;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.HashMap;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Package.PackageService;
import ir.rayas.app.citywareclient.Service.Poster.PosterService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResultPassing;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.Share.UserBusinessListActivity;
import ir.rayas.app.citywareclient.View.UserProfileChildren.PosterTypeActivity;
import ir.rayas.app.citywareclient.ViewModel.Poster.BuyPosterViewModel;
import ir.rayas.app.citywareclient.ViewModel.Poster.PurchasedPosterViewModel;


public class BuyPosterTypeFragment extends Fragment implements IResponseService, ILoadData {

    private PosterTypeActivity Context = null;

    private TextViewPersian YourCreditTextViewBuyPosterTypeFragment = null;
    private double TotalPrice = 0.0;
    private double PriceHours = 0.0;
    private double PriceDay = 0.0;

    private int NewDay;
    private int NewHours;
    private int Hours;


    private String PosterTypeName;
    private double PosterTypePrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (PosterTypeActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_buy_poster_type, container, false);


        PosterTypeName = Context.getPosterTypeName();
        PosterTypePrice = Context.getPosterTypePrice();

        NewDay = Context.getNewDay() * 24;
        NewHours = Context.getNewHours();

        //طرحبندی ویو
        CreateLayout(CurrentView);

        LoadData();

        return CurrentView;

    }


    private void CreateLayout(View CurrentView) {

        YourCreditTextViewBuyPosterTypeFragment = CurrentView.findViewById(R.id.YourCreditTextViewBuyPosterTypeFragment);
        final TextViewPersian PosterTypeTextViewBuyPosterTypeFragment = CurrentView.findViewById(R.id.PosterTypeTextViewBuyPosterTypeFragment);
        TextViewPersian priceTextViewBuyPosterTypeFragment = CurrentView.findViewById(R.id.priceTextViewBuyPosterTypeFragment);
        final TextViewPersian TotalPriceTextViewBuyPosterTypeFragment = CurrentView.findViewById(R.id.TotalPriceTextViewBuyPosterTypeFragment);
        final NumberPicker DayNumberPickerBuyPosterTypeFragment = CurrentView.findViewById(R.id.DayNumberPickerBuyPosterTypeFragment);
        final NumberPicker HoursNumberPickerBuyPosterTypeFragment = CurrentView.findViewById(R.id.HoursNumberPickerBuyPosterTypeFragment);
        ButtonPersianView ReturnButtonBuyPosterTypeFragment = CurrentView.findViewById(R.id.ReturnButtonBuyPosterTypeFragment);
        ButtonPersianView NextButtonBuyPosterTypeFragment = CurrentView.findViewById(R.id.NextButtonBuyPosterTypeFragment);
        ButtonPersianView selectBusinessButtonBuyPosterTypeFragment = CurrentView.findViewById(R.id.SelectBusinessButtonBuyPosterTypeFragment);

        selectBusinessButtonBuyPosterTypeFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userBusinessListIntent = Context.NewIntent(UserBusinessListActivity.class);
                userBusinessListIntent.putExtra("Title", Context.getResources().getString(R.string.selector_business_buy_poster));
                userBusinessListIntent.putExtra("Description", Context.getResources().getString(R.string.selector_business_for_buy_poster));
                userBusinessListIntent.putExtra("IsParent", true);
                startActivity(userBusinessListIntent);
            }
        });

        if (Context.getBusinessName() != null)
            if (Context.getBusinessName().equals("")) {
                selectBusinessButtonBuyPosterTypeFragment.setText(Context.getResources().getString(R.string.selector_business_buy_poster));
            } else {
                selectBusinessButtonBuyPosterTypeFragment.setText(Context.getBusinessName());
            }
        else
            selectBusinessButtonBuyPosterTypeFragment.setText(Context.getResources().getString(R.string.selector_business_buy_poster));

        PosterTypeTextViewBuyPosterTypeFragment.setText(PosterTypeName);
        priceTextViewBuyPosterTypeFragment.setText(Utility.GetIntegerNumberWithComma(PosterTypePrice));
        TotalPriceTextViewBuyPosterTypeFragment.setText(Utility.GetIntegerNumberWithComma(TotalPrice));

        HoursNumberPickerBuyPosterTypeFragment.setMinValue(0);
        HoursNumberPickerBuyPosterTypeFragment.setMaxValue(23);
        DayNumberPickerBuyPosterTypeFragment.setMinValue(0);
        DayNumberPickerBuyPosterTypeFragment.setMaxValue(31);
        HoursNumberPickerBuyPosterTypeFragment.setValue(NewHours);
        DayNumberPickerBuyPosterTypeFragment.setValue(Context.getNewDay());


        DayNumberPickerBuyPosterTypeFragment.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int newDay) {
                NewDay = newDay * 24;
                Context.setNewDay(newDay);
                double ConvertToHours = newDay * 24;
                PriceDay = ConvertToHours * PosterTypePrice;
                TotalPrice = PriceDay + PriceHours;
                TotalPriceTextViewBuyPosterTypeFragment.setText(Utility.GetIntegerNumberWithComma(TotalPrice));
            }
        });

        HoursNumberPickerBuyPosterTypeFragment.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int newHours) {
                NewHours = newHours;
                Context.setNewHours(NewHours);

                PriceHours = newHours * PosterTypePrice;
                TotalPrice = PriceDay + PriceHours;
                TotalPriceTextViewBuyPosterTypeFragment.setText(Utility.GetIntegerNumberWithComma(TotalPrice));
            }
        });


        if (Context.getNewHours() != 0 || Context.getNewDay() != 0) {
            TotalPrice = (NewHours * PosterTypePrice) + (NewDay * PosterTypePrice);
            TotalPriceTextViewBuyPosterTypeFragment.setText(Utility.GetIntegerNumberWithComma(TotalPrice));
        }


        ReturnButtonBuyPosterTypeFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context.setBusinessId(0);
                Context.setBusinessName("");
                NewDay = 0;
                NewHours = 0;

                Context.setNewDay(NewDay);
                Context.setNewHours(NewHours);
                getFragmentManager().popBackStack();
            }
        });

        NextButtonBuyPosterTypeFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (PosterTypePrice > 0) {

                    if (TotalPrice == 0) {
                        Context.ShowToast(Context.getResources().getString(R.string.specify_days_or_hours), Toast.LENGTH_LONG, MessageType.Warning);
                    } else if (Context.getBusinessId() == 0) {
                        Context.ShowToast(Context.getResources().getString(R.string.choose_one_of_your_businesses), Toast.LENGTH_LONG, MessageType.Warning);
                    } else {
                        Hours = ((int) (TotalPrice / PosterTypePrice));
                        BuyPoster();

                    }
                } else {
                    Hours = (NewDay + NewHours);

                    if (Hours == 0) {
                        Context.ShowToast(Context.getResources().getString(R.string.specify_days_or_hours), Toast.LENGTH_LONG, MessageType.Warning);
                    } else if (Context.getBusinessId() == 0) {
                        Context.ShowToast(Context.getResources().getString(R.string.choose_one_of_your_businesses), Toast.LENGTH_LONG, MessageType.Warning);
                    } else {
                        BuyPoster();

                    }
                }
            }
        });


    }


    public void LoadData() {
        Context.ShowLoadingProgressBar();

        PackageService packageService = new PackageService(BuyPosterTypeFragment.this);
        packageService.GetUserCredit();
    }

    public void BuyPoster() {

        Context.ShowLoadingProgressBar();
        PosterService PosterService = new PosterService(BuyPosterTypeFragment.this);
        PosterService.AddPurchasedPoster(MadeViewModel());
    }

    private BuyPosterViewModel MadeViewModel() {

        BuyPosterViewModel ViewModel = new BuyPosterViewModel();
        try {
            ViewModel.setBusinessId(Context.getBusinessId());
            ViewModel.setHours(Hours);
            ViewModel.setPosterTypeId(Context.getPosterTypeId());

        } catch (Exception Ex) {
            Context.ShowToast(FeedbackType.InvalidDataFormat.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
        return ViewModel;
    }


    /**
     * @param Data
     * @param ServiceMethod
     * @param <T>
     */
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.UserCreditGet) {
                Feedback<Double> FeedBack = (Feedback<Double>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    if (FeedBack.getValue() != null) {
                        YourCreditTextViewBuyPosterTypeFragment.setText(Utility.GetIntegerNumberWithComma(FeedBack.getValue()));
                    } else {
                        YourCreditTextViewBuyPosterTypeFragment.setText(Context.getResources().getString(R.string.zero));
                    }
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

                        SendDataToParentActivity(ViewModel);
                        Context.onBackPressed();


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
            Context.ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
    }

    /**
     * دریافت ویومدل پوستر خریداری شده و ارسال آن به اکتیویتی پروفایل کاربر جهت نمایش در لیست پوسترهای فعال
     *
     * @param ViewModel اطلاعات پوستر
     */
    private void SendDataToParentActivity(PurchasedPosterViewModel ViewModel) {
        HashMap<String, Object> Output = new HashMap<>();
        Output.put("IsAdd", true);
        Output.put("TotalPrice", TotalPrice);
        Output.put("PurchasedPosterViewModel", ViewModel);
        ActivityResultPassing.Push(new ActivityResult(Context.getParentActivity(), Context.getCurrentActivityId(), Output));
    }

}
