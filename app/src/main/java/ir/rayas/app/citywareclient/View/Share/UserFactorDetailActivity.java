package ir.rayas.app.citywareclient.View.Share;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.UserFactorBusinessContactRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.UserFactorBusinessOpenTimeRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Business.BusinessContactService;
import ir.rayas.app.citywareclient.Service.Business.BusinessOpenTimeService;
import ir.rayas.app.citywareclient.Service.Factor.UserFactorService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessContactViewModel;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessOpenTimeViewModel;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorItemViewModel;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorViewModel;

public class UserFactorDetailActivity extends BaseActivity implements IResponseService {

    private int FactorId = 0;
    private TextViewPersian BusinessNameTextViewUserFactorDetailActivity = null;
    private TextViewPersian CreateDateUserFactorTextViewUserFactorDetailActivity = null;
    private TextViewPersian UserFactorCodeTextViewUserFactorDetailActivity = null;
    private TextViewPersian NumberOfOrderItemsUserFactorTextViewUserFactorDetailActivity = null;
    private TextViewPersian PricePayableUserFactorTextViewUserFactorDetailActivity = null;
    private TextViewPersian DescriptionUserFactorTextViewUserFactorDetailActivity = null;
    private TextViewPersian BusinessDescriptionTextViewUserFactorDetailActivity = null;
    private EditText UserDescriptionEditTextUserFactorDetailActivity = null;
    private TextViewPersian ShowMapBusinessIconTextViewUserFactorDetailActivity = null;
    private TextViewPersian ContactIconTextViewUserFactorDetailActivity = null;
    private TextViewPersian BusinessAddressTextViewUserFactorDetailActivity = null;
    private CardView BusinessDescriptionCardViewUserFactorDetailActivity = null;
    private RelativeLayout UserDescriptionRelativeLayoutUserFactorDetailActivity = null;
    private LinearLayout ShowMapLinearLayoutUserFactorDetailActivity = null;


    private int BusinessId = 0;
    private double Latitude = 0;
    private double Longitude = 0;

    private UserFactorBusinessOpenTimeRecyclerViewAdapter userFactorBusinessOpenTimeRecyclerViewAdapter = null;
    private UserFactorBusinessContactRecyclerViewAdapter userFactorBusinessContactRecyclerViewAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_factor_detail);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.USER_FACTOR_DETAIL_ACTIVITY);

        FactorId = getIntent().getExtras().getInt("FactorId");

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.factor);

        //ایجاد طرحبندی صفحه
        CreateLayout();

        LoadData();
    }

    /**
     * در صورتی که در ارتباط با اینترنت مشکلی بوجود آمده و کاربر دکمه تلاش مجدد را فشار داده است
     */
    private void RetryButtonOnClick() {
        //دریافت اطلاعات
        LoadData();
    }

    /**
     * دریافت اطلاعات نحوای جهت پر کردن Recycle
     */
    private void LoadData() {
        ShowLoadingProgressBar();

        UserFactorService UserFactorService = new UserFactorService(this);
        UserFactorService.Get(FactorId);
    }

    /**
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    private void CreateLayout() {

        BusinessNameTextViewUserFactorDetailActivity = findViewById(R.id.BusinessNameTextViewUserFactorDetailActivity);
        CreateDateUserFactorTextViewUserFactorDetailActivity = findViewById(R.id.CreateDateUserFactorTextViewUserFactorDetailActivity);
        UserFactorCodeTextViewUserFactorDetailActivity = findViewById(R.id.UserFactorCodeTextViewUserFactorDetailActivity);
        NumberOfOrderItemsUserFactorTextViewUserFactorDetailActivity = findViewById(R.id.NumberOfOrderItemsUserFactorTextViewUserFactorDetailActivity);
        PricePayableUserFactorTextViewUserFactorDetailActivity = findViewById(R.id.PricePayableUserFactorTextViewUserFactorDetailActivity);
        DescriptionUserFactorTextViewUserFactorDetailActivity = findViewById(R.id.DescriptionUserFactorTextViewUserFactorDetailActivity);
        BusinessDescriptionTextViewUserFactorDetailActivity = findViewById(R.id.BusinessDescriptionTextViewUserFactorDetailActivity);
        BusinessDescriptionCardViewUserFactorDetailActivity = findViewById(R.id.BusinessDescriptionCardViewUserFactorDetailActivity);
        UserDescriptionRelativeLayoutUserFactorDetailActivity = findViewById(R.id.UserDescriptionRelativeLayoutUserFactorDetailActivity);
        UserDescriptionEditTextUserFactorDetailActivity = findViewById(R.id.UserDescriptionEditTextUserFactorDetailActivity);
        ShowMapLinearLayoutUserFactorDetailActivity = findViewById(R.id.ShowMapLinearLayoutUserFactorDetailActivity);
        RelativeLayout ContactRelativeLayoutUserFactorDetailActivity = findViewById(R.id.ContactRelativeLayoutUserFactorDetailActivity);
        RelativeLayout OpenTimeRelativeLayoutUserFactorDetailActivity = findViewById(R.id.OpenTimeRelativeLayoutUserFactorDetailActivity);
        ShowMapBusinessIconTextViewUserFactorDetailActivity = findViewById(R.id.ShowMapBusinessIconTextViewUserFactorDetailActivity);
        ContactIconTextViewUserFactorDetailActivity = findViewById(R.id.ContactIconTextViewUserFactorDetailActivity);
        BusinessAddressTextViewUserFactorDetailActivity = findViewById(R.id.BusinessAddressTextViewUserFactorDetailActivity);
        TextViewPersian OpenTimeIconTextViewUserFactorDetailActivity = findViewById(R.id.OpenTimeIconTextViewUserFactorDetailActivity);

        ButtonPersianView ShowProductButtonUserFactorDetailActivity = findViewById(R.id.ShowProductButtonUserFactorDetailActivity);

        ContactIconTextViewUserFactorDetailActivity.setTypeface(Font.MasterIcon);
        ContactIconTextViewUserFactorDetailActivity.setText("\uf095");

        ShowMapBusinessIconTextViewUserFactorDetailActivity.setTypeface(Font.MasterIcon);
        ShowMapBusinessIconTextViewUserFactorDetailActivity.setText("\uf041");

        OpenTimeIconTextViewUserFactorDetailActivity.setTypeface(Font.MasterIcon);
        OpenTimeIconTextViewUserFactorDetailActivity.setText("\uf017");

        ContactRelativeLayoutUserFactorDetailActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShowLoadingProgressBar();
                BusinessContactService businessContactService = new BusinessContactService(UserFactorDetailActivity.this);
                businessContactService.GetAll(BusinessId);

            }
        });

        ShowMapLinearLayoutUserFactorDetailActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MapIntent = NewIntent(MapActivity.class);
                MapIntent.putExtra("Latitude", Latitude);
                MapIntent.putExtra("Longitude",Longitude);
                MapIntent.putExtra("Going", 2);
                startActivity(MapIntent);
            }
        });

        ShowProductButtonUserFactorDetailActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ProductDetailIntent = NewIntent(UserFactorProductDetailActivity.class);
                ProductDetailIntent.putExtra("Latitude", Latitude);
                startActivity(ProductDetailIntent);

            }
        });

        OpenTimeRelativeLayoutUserFactorDetailActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowLoadingProgressBar();
                BusinessOpenTimeService businessOpenTimeService = new BusinessOpenTimeService(UserFactorDetailActivity.this);
                businessOpenTimeService.GetAll(BusinessId);
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
        HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.UserFactorGet) {
                Feedback<FactorViewModel> FeedBack = (Feedback<FactorViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final FactorViewModel ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        SetInformationToView(ViewModelList);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);

                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.BusinessOpenTimeGetAll) {
                Feedback<List<BusinessOpenTimeViewModel>> FeedBack = (Feedback<List<BusinessOpenTimeViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    List<BusinessOpenTimeViewModel> ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        //تنظیمات مربوط به recycle کسب و کار
                        if (ViewModel.size() > 0)
                            ShowDialogBusinessOpenTime(ViewModel);
                        else
                            ShowToast(getResources().getString(R.string.not_registered_hours_for_the_business), Toast.LENGTH_LONG, MessageType.Info);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.BusinessContactGetAll) {
                Feedback<List<BusinessContactViewModel>> FeedBack = (Feedback<List<BusinessContactViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    List<BusinessContactViewModel> ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        //تنظیمات مربوط به recycle کسب و کار
                        if (ViewModel.size() > 0)
                            ShowDialogBusinessContact(ViewModel);
                        else
                            ShowToast(getResources().getString(R.string.not_registered_contacts_for_the_business), Toast.LENGTH_LONG, MessageType.Info);
                    }

                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            }
        } catch (Exception e) {
            HideLoading();
            ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
    }

    private void SetInformationToView(FactorViewModel ViewModelList) {

        BusinessNameTextViewUserFactorDetailActivity.setText(ViewModelList.getBusinessName());
        CreateDateUserFactorTextViewUserFactorDetailActivity.setText(ViewModelList.getCreate());
        NumberOfOrderItemsUserFactorTextViewUserFactorDetailActivity.setText(String.valueOf(ViewModelList.getItemList().size()));
        UserFactorCodeTextViewUserFactorDetailActivity.setText(String.valueOf(ViewModelList.getId()));
        BusinessAddressTextViewUserFactorDetailActivity.setText(ViewModelList.getBusinessAddress());

        BusinessId = ViewModelList.getBusinessId();
        Latitude = ViewModelList.getBusinessLatitude();
        Longitude = ViewModelList.getBusinessLongitude();

        if (Longitude > 0){
            ShowMapLinearLayoutUserFactorDetailActivity.setVisibility(View.VISIBLE);
        }else {
            ShowMapLinearLayoutUserFactorDetailActivity.setVisibility(View.GONE);
        }

        boolean IsZeroPrice = false;

        List<FactorItemViewModel> ItemList = ViewModelList.getItemList();
        for (int i = 0; i < ItemList.size(); i++) {
            if (ItemList.get(i).getPrice() == 0) {
                IsZeroPrice = true;
            }
        }

        if (ViewModelList.getTotalPrice() < 0) {
            PricePayableUserFactorTextViewUserFactorDetailActivity.setText(getResources().getString(R.string.unknown));
        } else {

            double DeliveryCost = ViewModelList.getDeliveryCost();
            Double PayablePrice;
            if (DeliveryCost <= 0) {
                PayablePrice = ViewModelList.getTotalPrice();
            } else {
                PayablePrice = ViewModelList.getTotalPrice() + DeliveryCost;
            }

            if (ViewModelList.isHasQuickOrder()) {

                PricePayableUserFactorTextViewUserFactorDetailActivity.setText(Utility.GetIntegerNumberWithComma(PayablePrice) + " " + getResources().getString(R.string.toman) + " - " + getResources().getString(R.string.price_is_not_definitive));

                DescriptionUserFactorTextViewUserFactorDetailActivity.setVisibility(View.VISIBLE);
                DescriptionUserFactorTextViewUserFactorDetailActivity.setText(getResources().getString(R.string.in_your_factor_there_are_products_that_are_not_priced));
            } else {
                if (IsZeroPrice) {
                    PricePayableUserFactorTextViewUserFactorDetailActivity.setText(Utility.GetIntegerNumberWithComma(PayablePrice) + " " + getResources().getString(R.string.toman) + " - " + getResources().getString(R.string.price_is_not_definitive));

                    DescriptionUserFactorTextViewUserFactorDetailActivity.setVisibility(View.VISIBLE);
                    DescriptionUserFactorTextViewUserFactorDetailActivity.setText(getResources().getString(R.string.in_your_factor_there_are_products_that_are_not_priced));
                } else {
                    DescriptionUserFactorTextViewUserFactorDetailActivity.setVisibility(View.GONE);
                    PricePayableUserFactorTextViewUserFactorDetailActivity.setText(Utility.GetIntegerNumberWithComma(PayablePrice) + " " + getResources().getString(R.string.toman));
                }
            }
        }

        if (ViewModelList.getBusinessDescription() == null || ViewModelList.getBusinessDescription().equals("")) {
            BusinessDescriptionCardViewUserFactorDetailActivity.setVisibility(View.GONE);
        } else {
            BusinessDescriptionCardViewUserFactorDetailActivity.setVisibility(View.VISIBLE);
            BusinessDescriptionTextViewUserFactorDetailActivity.setText(ViewModelList.getBusinessDescription());
        }

        if (ViewModelList.getUserDescription() == null || ViewModelList.getUserDescription().equals("")) {
            UserDescriptionEditTextUserFactorDetailActivity.setVisibility(View.GONE);
            UserDescriptionRelativeLayoutUserFactorDetailActivity.setVisibility(View.GONE);
        } else {
            UserDescriptionRelativeLayoutUserFactorDetailActivity.setVisibility(View.VISIBLE);
            UserDescriptionEditTextUserFactorDetailActivity.setVisibility(View.VISIBLE);
            UserDescriptionEditTextUserFactorDetailActivity.setText(ViewModelList.getUserDescription());
        }


    }

    private void ShowDialogBusinessOpenTime(List<BusinessOpenTimeViewModel> ViewModel) {


        final Dialog ShowBusinessDetailsDialog = new Dialog(UserFactorDetailActivity.this);
        ShowBusinessDetailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ShowBusinessDetailsDialog.setContentView(R.layout.dialog_business_open_time);

        RecyclerView BusinessOpenTimeRecyclerViewShowBusinessDetailsActivity = ShowBusinessDetailsDialog.findViewById(R.id.BusinessOpenTimeRecyclerViewShowBusinessDetailsActivity);
        userFactorBusinessOpenTimeRecyclerViewAdapter = new UserFactorBusinessOpenTimeRecyclerViewAdapter(UserFactorDetailActivity.this, R.layout.recycler_view_dialog_open_time, ViewModel, BusinessOpenTimeRecyclerViewShowBusinessDetailsActivity);
        LinearLayoutManager BusinessOpenTimeLinearLayoutManager = new LinearLayoutManager(UserFactorDetailActivity.this);
        BusinessOpenTimeRecyclerViewShowBusinessDetailsActivity.setLayoutManager(BusinessOpenTimeLinearLayoutManager);
        BusinessOpenTimeRecyclerViewShowBusinessDetailsActivity.setAdapter(userFactorBusinessOpenTimeRecyclerViewAdapter);

        ShowBusinessDetailsDialog.show();
    }

    private void ShowDialogBusinessContact(List<BusinessContactViewModel> ViewModel) {


        final Dialog ShowBusinessDetailsDialog = new Dialog(UserFactorDetailActivity.this);
        ShowBusinessDetailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ShowBusinessDetailsDialog.setContentView(R.layout.dialog_business_contact);

        TextViewPersian HeaderTextViewShowBusinessDetailsActivity = ShowBusinessDetailsDialog.findViewById(R.id.HeaderTextViewShowBusinessDetailsActivity);
        HeaderTextViewShowBusinessDetailsActivity.getLayoutParams().width = LayoutUtility.GetWidthAccordingToScreen(UserFactorDetailActivity.this, 1);
        RecyclerView BusinessContactRecyclerViewShowBusinessDetailsActivity = ShowBusinessDetailsDialog.findViewById(R.id.BusinessContactRecyclerViewShowBusinessDetailsActivity);
        userFactorBusinessContactRecyclerViewAdapter = new UserFactorBusinessContactRecyclerViewAdapter(UserFactorDetailActivity.this, ViewModel, BusinessContactRecyclerViewShowBusinessDetailsActivity);
        LinearLayoutManager BusinessOpenTimeLinearLayoutManager = new LinearLayoutManager(UserFactorDetailActivity.this);
        BusinessContactRecyclerViewShowBusinessDetailsActivity.setLayoutManager(BusinessOpenTimeLinearLayoutManager);
        BusinessContactRecyclerViewShowBusinessDetailsActivity.setAdapter(userFactorBusinessContactRecyclerViewAdapter);

        ShowBusinessDetailsDialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
        onLowMemory();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

