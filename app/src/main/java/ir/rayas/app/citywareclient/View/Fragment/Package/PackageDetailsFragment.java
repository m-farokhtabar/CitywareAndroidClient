package ir.rayas.app.citywareclient.View.Fragment.Package;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import ir.rayas.app.citywareclient.Adapter.RecyclerView.PrizeAllClubRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Package.PackageService;
import ir.rayas.app.citywareclient.Service.Prize.PrizeService;
import ir.rayas.app.citywareclient.Service.User.PointService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Master.ClubUsersActivity;
import ir.rayas.app.citywareclient.View.UserProfileChildren.PackageActivity;
import ir.rayas.app.citywareclient.ViewModel.Club.RequestPrizeViewModel;
import ir.rayas.app.citywareclient.ViewModel.Club.UserConsumePointViewModel;
import ir.rayas.app.citywareclient.ViewModel.Package.PackageDetailsViewModel;

public class PackageDetailsFragment extends Fragment implements IResponseService {

    private int PackageId = 0;
    private PackageActivity Context = null;

    private int width = 0;
    private int height = 0;

    private ImageView PackageImageImageViewPackageDetailsFragment = null;
    private ImageView TagImageViewPackageDetailsFragment = null;
    private TextViewPersian PackageNameTextViewPackageDetailsFragment = null;
    private TextViewPersian AbstractDescriptionPackageTextViewPackageDetailsFragment = null;
    private TextViewPersian PriceTextViewPackageDetailsFragment = null;
    private TextViewPersian PaidPriceTextViewPackageDetailsFragment = null;
    private TextViewPersian OrTextViewPackageDetailsFragment = null;
    private TextViewPersian PointTextViewPackageDetailsFragment = null;
    private TextViewPersian PointValueTextViewPackageDetailsFragment = null;
    private TextViewPersian AddPointToUserTextViewPackageDetailsFragment = null;
    private TextViewPersian DescriptionTextViewPackageDetailsFragment = null;
    private TextViewPersian ValidateTextViewPackageDetailsFragment = null;
    private CardView AddPointToUserCardViewPackageDetailsFragment = null;
    private CardView DescriptionCardViewPackageDetailsFragment = null;
    private RelativeLayout BuyPackageByPointRelativeLayoutPackageDetailsFragment = null;
    private RelativeLayout BuyPackageByCashRelativeLayoutPackageDetailsFragment = null;


    private String PackageName = "";
    private int Point = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_package_details, container, false);

        PackageId = getArguments().getInt("PackageId");

        //دریافت اکتیوتی والد این فرگمین
        Context = (PackageActivity) getActivity();

        CreateLayout(CurrentView);

        LoadData();

        return CurrentView;
    }


    private void CreateLayout(View CurrentView) {
        PackageImageImageViewPackageDetailsFragment = CurrentView.findViewById(R.id.PackageImageImageViewPackageDetailsFragment);
        PackageNameTextViewPackageDetailsFragment = CurrentView.findViewById(R.id.PackageNameTextViewPackageDetailsFragment);
        AbstractDescriptionPackageTextViewPackageDetailsFragment = CurrentView.findViewById(R.id.AbstractDescriptionPackageTextViewPackageDetailsFragment);
        PriceTextViewPackageDetailsFragment = CurrentView.findViewById(R.id.PriceTextViewPackageDetailsFragment);
        PaidPriceTextViewPackageDetailsFragment = CurrentView.findViewById(R.id.PaidPriceTextViewPackageDetailsFragment);
        OrTextViewPackageDetailsFragment = CurrentView.findViewById(R.id.OrTextViewPackageDetailsFragment);
        PointTextViewPackageDetailsFragment = CurrentView.findViewById(R.id.PointTextViewPackageDetailsFragment);
        PointValueTextViewPackageDetailsFragment = CurrentView.findViewById(R.id.PointValueTextViewPackageDetailsFragment);
        TagImageViewPackageDetailsFragment = CurrentView.findViewById(R.id.TagImageViewPackageDetailsFragment);
        AddPointToUserTextViewPackageDetailsFragment = CurrentView.findViewById(R.id.AddPointToUserTextViewPackageDetailsFragment);
        AddPointToUserCardViewPackageDetailsFragment = CurrentView.findViewById(R.id.AddPointToUserCardViewPackageDetailsFragment);
        DescriptionCardViewPackageDetailsFragment = CurrentView.findViewById(R.id.DescriptionCardViewPackageDetailsFragment);
        DescriptionTextViewPackageDetailsFragment = CurrentView.findViewById(R.id.DescriptionTextViewPackageDetailsFragment);
        ValidateTextViewPackageDetailsFragment = CurrentView.findViewById(R.id.ValidateTextViewPackageDetailsFragment);
        BuyPackageByPointRelativeLayoutPackageDetailsFragment = CurrentView.findViewById(R.id.BuyPackageByPointRelativeLayoutPackageDetailsFragment);
        BuyPackageByCashRelativeLayoutPackageDetailsFragment = CurrentView.findViewById(R.id.BuyPackageByCashRelativeLayoutPackageDetailsFragment);
        TextViewPersian BuyPackageByPointTextViewPackageDetailsFragment = CurrentView.findViewById(R.id.BuyPackageByPointTextViewPackageDetailsFragment);
        TextViewPersian BuyPackageByCashTextViewPackageDetailsFragment = CurrentView.findViewById(R.id.BuyPackageByCashTextViewPackageDetailsFragment);


        width = LayoutUtility.GetWidthAccordingToScreen(Context, 1);
        height = LayoutUtility.GetWidthAccordingToScreen(Context, 2);

        PackageImageImageViewPackageDetailsFragment.getLayoutParams().width = width;
        PackageImageImageViewPackageDetailsFragment.getLayoutParams().height = height;

        BuyPackageByPointTextViewPackageDetailsFragment.setTypeface(Font.MasterIcon);
        BuyPackageByPointTextViewPackageDetailsFragment.setText("\uf005");

        BuyPackageByCashTextViewPackageDetailsFragment.setTypeface(Font.MasterIcon);
        BuyPackageByCashTextViewPackageDetailsFragment.setText("\uf09d");


    }

    public void LoadData() {
        Context.ShowLoadingProgressBar();

        PackageService packageService = new PackageService(this);
        packageService.Get(PackageId);
    }

    private RequestPrizeViewModel MadeViewModel(int Id) {
        RequestPrizeViewModel ViewModel = new RequestPrizeViewModel();
        try {
            ViewModel.setId(Id);

        } catch (Exception Ex) {
        }
        return ViewModel;
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.PackageDetailsGet) {

                Feedback<PackageDetailsViewModel> FeedBack = (Feedback<PackageDetailsViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    final PackageDetailsViewModel ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        SetView(ViewModel);
                    } else {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    }

                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.UserPointGet) {
                Feedback<Double> FeedBack = (Feedback<Double>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    Double DeliveryPrice = FeedBack.getValue();

                    if (DeliveryPrice != null) {
                        if (DeliveryPrice < Point) {
                            Context.ShowToast(Context.getResources().getString(R.string.tour_point_of_less_Purchases_package), Toast.LENGTH_LONG, MessageType.Warning);
                        } else {
                            ShowBuyPackageWithPointDialog(DeliveryPrice.toString(), Point, PackageName);
                        }
                    } else {
                        if (0 < Point) {
                            Context.ShowToast(Context.getResources().getString(R.string.tour_point_of_less_Purchases_package), Toast.LENGTH_LONG, MessageType.Warning);
                        } else {
                            ShowBuyPackageWithPointDialog(Context.getResources().getString(R.string.zero), Point, PackageName);
                        }
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.PrizeRequestPackageAdd) {
                Feedback<UserConsumePointViewModel> FeedBack = (Feedback<UserConsumePointViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.RegisteredSuccessful.getId()) {

                    UserConsumePointViewModel ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        ShowMessageBuyPrizeDialog();
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId())
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


    @SuppressLint("SetTextI18n")
    private void SetView(final PackageDetailsViewModel ViewModel) {

        if (ViewModel.getImagePathUrl().equals("") || ViewModel.getImagePathUrl() == null) {
            PackageImageImageViewPackageDetailsFragment.setImageResource(R.drawable.image_default_banner);
        } else {
            LayoutUtility.LoadImageWithGlide(Context, ViewModel.getImagePathUrl(), PackageImageImageViewPackageDetailsFragment, width, height);
        }

        PackageNameTextViewPackageDetailsFragment.setText(ViewModel.getTitle());
        AbstractDescriptionPackageTextViewPackageDetailsFragment.setText(ViewModel.getAbstractOfDescription());
        PriceTextViewPackageDetailsFragment.setText(Utility.GetIntegerNumberWithComma((int) ViewModel.getCreditPrice()));

        if (ViewModel.isCanPurchaseByPoint()) {
            PointTextViewPackageDetailsFragment.setVisibility(View.VISIBLE);
            OrTextViewPackageDetailsFragment.setVisibility(View.VISIBLE);
            PointValueTextViewPackageDetailsFragment.setVisibility(View.VISIBLE);
            TagImageViewPackageDetailsFragment.setVisibility(View.VISIBLE);
            PaidPriceTextViewPackageDetailsFragment.setText(Utility.GetIntegerNumberWithComma((int) ViewModel.getPayablePrice()) + " " + Context.getResources().getString(R.string.toman));
            PointValueTextViewPackageDetailsFragment.setText(Utility.GetIntegerNumberWithComma((int) ViewModel.getPointForPurchasing()));
        } else {
            PaidPriceTextViewPackageDetailsFragment.setText(Utility.GetIntegerNumberWithComma((int) ViewModel.getPayablePrice()) + " " + Context.getResources().getString(R.string.toman));
            PointTextViewPackageDetailsFragment.setVisibility(View.GONE);
            OrTextViewPackageDetailsFragment.setVisibility(View.GONE);
            PointValueTextViewPackageDetailsFragment.setVisibility(View.GONE);
            TagImageViewPackageDetailsFragment.setVisibility(View.GONE);
        }

        if (ViewModel.getPoint() > 0) {
            AddPointToUserCardViewPackageDetailsFragment.setVisibility(View.VISIBLE);
            AddPointToUserTextViewPackageDetailsFragment.setText(Context.getResources().getString(R.string.buy_online_this_package) + " " + (int) ViewModel.getPoint() + " " + Context.getResources().getString(R.string.get_points));
        } else {
            AddPointToUserCardViewPackageDetailsFragment.setVisibility(View.GONE);
        }

        if (ViewModel.getDescription().equals("") || ViewModel.getDescription() == null) {
            DescriptionCardViewPackageDetailsFragment.setVisibility(View.GONE);
        } else {
            DescriptionCardViewPackageDetailsFragment.setVisibility(View.VISIBLE);
            DescriptionTextViewPackageDetailsFragment.setText(ViewModel.getDescription());
        }


        if (ViewModel.isAlwaysCredit()) {
            ValidateTextViewPackageDetailsFragment.setText(Context.getResources().getString(R.string.unlimited));
        } else {
            ValidateTextViewPackageDetailsFragment.setText(String.valueOf(ViewModel.getCreditInDays()) + " " + Context.getResources().getString(R.string.day));
        }

        PackageName = ViewModel.getTitle();
        Point = (int) ViewModel.getPointForPurchasing();

        BuyPackageByCashRelativeLayoutPackageDetailsFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowBuyPackageOnlineDialog((int) ViewModel.getPayablePrice(), ViewModel.getTitle());
            }
        });

        BuyPackageByPointRelativeLayoutPackageDetailsFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context.ShowLoadingProgressBar();
                PointService pointService = new PointService(PackageDetailsFragment.this);
                pointService.Get();
            }
        });
    }

    private void ShowBuyPackageOnlineDialog(int Price, String PackageName) {

        final Dialog BuyPackageDialog = new Dialog(Context);
        BuyPackageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        BuyPackageDialog.setContentView(R.layout.dialog_buy_package_online);

        ButtonPersianView DialogBuyPackageOnlineOkButton = BuyPackageDialog.findViewById(R.id.DialogBuyPackageOnlineOkButton);
        ButtonPersianView DialogBuyPackageOnlineCancelButton = BuyPackageDialog.findViewById(R.id.DialogBuyPackageOnlineCancelButton);

        String Rial = Price + Context.getResources().getString(R.string.zero);
        String Message;

        if (PackageName.contains(Context.getResources().getString(R.string.packag))) {
            Message = Context.getResources().getString(R.string.amount) + " " + Price + " " + Context.getResources().getString(R.string.toman) + " " + Context.getResources().getString(R.string.equivalent) + " " + Rial + " " +
                    Context.getResources().getString(R.string.rial) + " " + Context.getResources().getString(R.string.for_buy) + " " + PackageName;
        } else {
            Message = Context.getResources().getString(R.string.amount) + " " + Price + " " + Context.getResources().getString(R.string.price_with_out_currency) + " " + Context.getResources().getString(R.string.equivalent) + " " + Rial + " " +
                    Context.getResources().getString(R.string.rial) + " " + Context.getResources().getString(R.string.for_buy_the_package) + " " + PackageName;
        }


        TextViewPersian DialogBuyPackageOnlineDescriptionTextView = BuyPackageDialog.findViewById(R.id.DialogBuyPackageOnlineDescriptionTextView);
        DialogBuyPackageOnlineDescriptionTextView.setText(Message);


        DialogBuyPackageOnlineOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyPackageDialog.dismiss();

            }
        });

        DialogBuyPackageOnlineCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyPackageDialog.dismiss();
            }
        });

        BuyPackageDialog.show();
    }


    private void ShowBuyPackageWithPointDialog(String YourPoint, int Point, String PackageName) {

        final Dialog BuyPackageDialog = new Dialog(Context);
        BuyPackageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        BuyPackageDialog.setContentView(R.layout.dialog_buy_package_with_point);

        ButtonPersianView DialogBuyPackageWithPointOkButton = BuyPackageDialog.findViewById(R.id.DialogBuyPackageWithPointOkButton);
        ButtonPersianView DialogBuyPackageWithPointCancelButton = BuyPackageDialog.findViewById(R.id.DialogBuyPackageWithPointCancelButton);

        String Message;

        if (PackageName.contains(Context.getResources().getString(R.string.packag))) {
            Message = Context.getResources().getString(R.string.rate) + " " + Point + " " + Context.getResources().getString(R.string.for_buy) + " " + PackageName;
        } else {
            Message = Context.getResources().getString(R.string.rate) + " " + Point + " " + Context.getResources().getString(R.string.for_buy_the_package) + " " + PackageName;
        }


        TextViewPersian DialogBuyPackageWithPointDescriptionTextView = BuyPackageDialog.findViewById(R.id.DialogBuyPackageWithPointDescriptionTextView);
        TextViewPersian YourPointTextView = BuyPackageDialog.findViewById(R.id.YourPointTextView);
        YourPointTextView.setText(YourPoint);
        DialogBuyPackageWithPointDescriptionTextView.setText(Message);


        DialogBuyPackageWithPointOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context.ShowLoadingProgressBar();
                PrizeService prizeService = new PrizeService(PackageDetailsFragment.this);
                prizeService.AddPrizeRequestPackage(MadeViewModel(PackageId));

                BuyPackageDialog.dismiss();

            }
        });

        DialogBuyPackageWithPointCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyPackageDialog.dismiss();
            }
        });

        BuyPackageDialog.show();
    }


    private void ShowMessageBuyPrizeDialog() {

        final Dialog OkBuyPrizePackageDialog = new Dialog(Context);
        OkBuyPrizePackageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        OkBuyPrizePackageDialog.setContentView(R.layout.dialog_ok_buy_prize);

        ButtonPersianView DialogOkButton = OkBuyPrizePackageDialog.findViewById(R.id.DialogOkButton);
        TextViewPersian DialogMessageTextView = OkBuyPrizePackageDialog.findViewById(R.id.DialogMessageTextView);

        DialogMessageTextView.setText(Context.getResources().getString(R.string.message_show_get_package));

        DialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkBuyPrizePackageDialog.dismiss();
            }
        });

        OkBuyPrizePackageDialog.show();
    }

}
