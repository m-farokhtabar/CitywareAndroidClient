package ir.rayas.app.citywareclient.View.Fragment.Package;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Package.PackageService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.UserProfileChildren.PackageActivity;
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
            }
        } catch (Exception e) {
            Context.HideLoading();
            Context.ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
    }


    private void SetView(PackageDetailsViewModel ViewModel) {

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
            PointValueTextViewPackageDetailsFragment.setText(Utility.GetIntegerNumberWithComma((int) ViewModel.getPointForPurchasing()) + " " + Context.getResources().getString(R.string.point_toman));
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
        }else {
            ValidateTextViewPackageDetailsFragment.setText(String.valueOf(ViewModel.getCreditInDays()) + " " + Context.getResources().getString(R.string.day));
        }
    }

}
