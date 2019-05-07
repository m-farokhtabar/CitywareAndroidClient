package ir.rayas.app.citywareclient.View.Fragment.Poster;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.Toast;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Package.PackageService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.UserProfileChildren.BuyPosterTypeActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyPosterTypeFragment extends Fragment implements IResponseService {

    private BuyPosterTypeActivity Context = null;
    private TextViewPersian YourCreditTextViewBuyPosterTypeFragment = null;
    private double TotalPrice = 0.0;
    private double PriceHours = 0.0;
    private double PriceDay = 0.0;

    int NewDay;
    int NewHours;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (BuyPosterTypeActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_buy_poster_type, container, false);
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

        PosterTypeTextViewBuyPosterTypeFragment.setText(Context.getPosterTypeName());
        priceTextViewBuyPosterTypeFragment.setText(Utility.GetIntegerNumberWithComma(Context.getPosterTypePrice()));
        TotalPriceTextViewBuyPosterTypeFragment.setText(Utility.GetIntegerNumberWithComma(TotalPrice));

        HoursNumberPickerBuyPosterTypeFragment.setMinValue(0);
        HoursNumberPickerBuyPosterTypeFragment.setMaxValue(23);
        DayNumberPickerBuyPosterTypeFragment.setMinValue(0);
        DayNumberPickerBuyPosterTypeFragment.setMaxValue(31);
        HoursNumberPickerBuyPosterTypeFragment.setValue(0);
        DayNumberPickerBuyPosterTypeFragment.setValue(0);


        DayNumberPickerBuyPosterTypeFragment.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int newDay) {
                NewDay = newDay * 24;
                double ConvertToHours = newDay * 24;
                PriceDay = ConvertToHours * Context.getPosterTypePrice();
                TotalPrice = PriceDay + PriceHours;
                TotalPriceTextViewBuyPosterTypeFragment.setText(Utility.GetIntegerNumberWithComma(TotalPrice));
            }
        });

        HoursNumberPickerBuyPosterTypeFragment.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int newHours) {
                NewHours = newHours;
                PriceHours = newHours * Context.getPosterTypePrice();
                TotalPrice = PriceDay + PriceHours;
                TotalPriceTextViewBuyPosterTypeFragment.setText(Utility.GetIntegerNumberWithComma(TotalPrice));
            }
        });


        ReturnButtonBuyPosterTypeFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context.onBackPressed();
            }
        });

        NextButtonBuyPosterTypeFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Context.getPosterTypePrice() > 0) {
                    if (TotalPrice == 0) {
                        Context.ShowToast(Context.getResources().getString(R.string.specify_days_or_hours), Toast.LENGTH_LONG, MessageType.Warning);
                    } else {
                        Context.setHours((int) (TotalPrice / Context.getPosterTypePrice()));

                        BusinessListForPosterFragment businessListForPosterFragment = new BusinessListForPosterFragment();
                        FragmentTransaction BusinessListTransaction = Context.getSupportFragmentManager().beginTransaction();
                        BusinessListTransaction.replace(R.id.PosterFrameLayoutBuyPosterTypeActivity, businessListForPosterFragment);
                        BusinessListTransaction.addToBackStack(null);
                        BusinessListTransaction.commit();
                    }
                } else {
                    Context.setHours(NewDay + NewHours);

                    BusinessListForPosterFragment businessListForPosterFragment = new BusinessListForPosterFragment();
                    FragmentTransaction BusinessListTransaction = Context.getSupportFragmentManager().beginTransaction();
                    BusinessListTransaction.replace(R.id.PosterFrameLayoutBuyPosterTypeActivity, businessListForPosterFragment);
                    BusinessListTransaction.addToBackStack(null);
                    BusinessListTransaction.commit();
                }
            }
        });
    }

    public void LoadData() {
        Context.ShowLoadingProgressBar();

        PackageService packageService = new PackageService(BuyPosterTypeFragment.this);
        packageService.GetUserCredit();

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
            }
        } catch (Exception e) {
            Context.ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
    }

}
