package ir.rayas.app.citywareclient.View.Share;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.Spinner.FactorStatusSpinnerAdapter;
import ir.rayas.app.citywareclient.Adapter.ViewModel.FactorStatusAdapterViewModel;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Factor.BusinessFactorService;
import ir.rayas.app.citywareclient.Service.Factor.FactorStatusService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.FactorStatus;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Factor.DescriptionFactorInViewModel;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorItemViewModel;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorStatusViewModel;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorViewModel;
import ir.rayas.app.citywareclient.ViewModel.Factor.StatusAndDescriptionFactorInViewModel;


public class BusinessFactorDetailsActivity extends BaseActivity implements IResponseService, AdapterView.OnItemSelectedListener {

    private TextViewPersian UserNameTextViewBusinessFactorDetailActivity = null;
    private TextViewPersian CreateDateUserFactorTextViewBusinessFactorDetailActivity = null;
    private TextViewPersian BusinessFactorCodeTextViewBusinessFactorDetailActivity = null;
    private TextViewPersian NumberOfOrderItemsUserFactorTextViewBusinessFactorDetailActivity = null;
    private TextViewPersian PricePayableUserFactorTextViewBusinessFactorDetailActivity = null;
    private TextViewPersian DescriptionUserFactorTextViewBusinessFactorDetailActivity = null;
    private TextViewPersian UserDescriptionTextViewBusinessFactorDetailActivity = null;
    private TextViewPersian UserAddressTextViewBusinessFactorDetailActivity = null;
    private TextViewPersian StatusFactorTextViewBusinessFactorDetailActivity = null;
    private TextViewPersian UserCellPhoneTextViewBusinessFactorDetailActivity = null;
    private EditText BusinessDescriptionEditTextBusinessFactorDetailActivity = null;
    private Spinner StatusFactorSpinnerBusinessFactorDetailActivity = null;
    private LinearLayout ShowMapLinearLayoutBusinessFactorDetailActivity = null;
    private LinearLayout UserCellPhoneLinearLayoutBusinessFactorDetailActivity = null;
    private CardView UserDescriptionCardViewBusinessFactorDetailActivity = null;
    private ButtonPersianView EditButtonBusinessFactorDetailActivity = null;

    private double Latitude = 0;
    private double Longitude = 0;
    private Integer StatusFactor = 0;
    private int FactorId = 0;
    private String Description = "";
    private int FactorStatusId = 0;
    private String FactorStatusTitle = "";
    private boolean IsChangeDescription = false;
    private String PhoneNumber = "";

    private List<FactorItemViewModel> ItemList = new ArrayList<>();
    private List<FactorStatusViewModel> FactorStatusViewModels = new ArrayList<>();
    private List<FactorStatusAdapterViewModel> FactorStatusAdapterViewModel = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_factor_details);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.BUSINESS_FACTOR_DETAIL_ACTIVITY);

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

        FactorStatusService factorStatusService = new FactorStatusService(this);
        factorStatusService.GetAll();
    }

    /**
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    private void CreateLayout() {

        UserNameTextViewBusinessFactorDetailActivity = findViewById(R.id.UserNameTextViewBusinessFactorDetailActivity);
        CreateDateUserFactorTextViewBusinessFactorDetailActivity = findViewById(R.id.CreateDateUserFactorTextViewBusinessFactorDetailActivity);
        BusinessFactorCodeTextViewBusinessFactorDetailActivity = findViewById(R.id.BusinessFactorCodeTextViewBusinessFactorDetailActivity);
        NumberOfOrderItemsUserFactorTextViewBusinessFactorDetailActivity = findViewById(R.id.NumberOfOrderItemsUserFactorTextViewBusinessFactorDetailActivity);
        PricePayableUserFactorTextViewBusinessFactorDetailActivity = findViewById(R.id.PricePayableUserFactorTextViewBusinessFactorDetailActivity);
        DescriptionUserFactorTextViewBusinessFactorDetailActivity = findViewById(R.id.DescriptionUserFactorTextViewBusinessFactorDetailActivity);
        UserDescriptionTextViewBusinessFactorDetailActivity = findViewById(R.id.UserDescriptionTextViewBusinessFactorDetailActivity);
        BusinessDescriptionEditTextBusinessFactorDetailActivity = findViewById(R.id.BusinessDescriptionEditTextBusinessFactorDetailActivity);
        UserAddressTextViewBusinessFactorDetailActivity = findViewById(R.id.UserAddressTextViewBusinessFactorDetailActivity);
        TextViewPersian ShowMapBusinessIconTextViewBusinessFactorDetailActivity = findViewById(R.id.ShowMapBusinessIconTextViewBusinessFactorDetailActivity);
        ShowMapLinearLayoutBusinessFactorDetailActivity = findViewById(R.id.ShowMapLinearLayoutBusinessFactorDetailActivity);
        StatusFactorTextViewBusinessFactorDetailActivity = findViewById(R.id.StatusFactorTextViewBusinessFactorDetailActivity);
        StatusFactorSpinnerBusinessFactorDetailActivity = findViewById(R.id.StatusFactorSpinnerBusinessFactorDetailActivity);
        UserDescriptionCardViewBusinessFactorDetailActivity = findViewById(R.id.UserDescriptionCardViewBusinessFactorDetailActivity);
        UserCellPhoneTextViewBusinessFactorDetailActivity = findViewById(R.id.UserCellPhoneTextViewBusinessFactorDetailActivity);
        UserCellPhoneLinearLayoutBusinessFactorDetailActivity = findViewById(R.id.UserCellPhoneLinearLayoutBusinessFactorDetailActivity);

        ButtonPersianView ShowProductButtonBusinessFactorDetailActivity = findViewById(R.id.ShowProductButtonBusinessFactorDetailActivity);
        EditButtonBusinessFactorDetailActivity = findViewById(R.id.EditButtonBusinessFactorDetailActivity);


        ShowMapBusinessIconTextViewBusinessFactorDetailActivity.setTypeface(Font.MasterIcon);
        ShowMapBusinessIconTextViewBusinessFactorDetailActivity.setText("\uf041");


        ShowMapLinearLayoutBusinessFactorDetailActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MapIntent = NewIntent(MapActivity.class);
                MapIntent.putExtra("Latitude", Latitude);
                MapIntent.putExtra("Longitude", Longitude);
                MapIntent.putExtra("Going", 2);
                startActivity(MapIntent);
            }
        });

        ShowProductButtonBusinessFactorDetailActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ProductDetailIntent = NewIntent(UserFactorProductDetailActivity.class);
                String ArrayAsString = new Gson().toJson(ItemList);
                ProductDetailIntent.putExtra("ArrayAsString", ArrayAsString);
                startActivity(ProductDetailIntent);
            }
        });

        EditButtonBusinessFactorDetailActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeFactorStatus();
            }
        });

        UserCellPhoneLinearLayoutBusinessFactorDetailActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "tel:" + PhoneNumber;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                startActivity(intent);
            }
        });

        StatusFactorSpinnerBusinessFactorDetailActivity.setOnItemSelectedListener(this);
    }

    /**
     * @param Data
     * @param ServiceMethod
     * @param <T>
     */
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {

        try {
            if (ServiceMethod == ServiceMethodType.BusinessUserFactorGet) {
                HideLoading();
                Feedback<FactorViewModel> FeedBack = (Feedback<FactorViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final FactorViewModel ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        FactorStatusId = ViewModelList.getFactorStatusId();
                        SetInformationToSpinner(FactorStatusViewModels);
                        SetInformationToView(ViewModelList);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);

                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.FactorStatusGetAll) {
                Feedback<List<FactorStatusViewModel>> FeedBack = (Feedback<List<FactorStatusViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    BusinessFactorService BusinessFactorService = new BusinessFactorService(this);
                    BusinessFactorService.Get(FactorId);

                    List<FactorStatusViewModel> ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        //تنظیمات مربوط به recycle کسب و کار
                        if (ViewModel.size() > 0)
                            FactorStatusViewModels = ViewModel;
                    }

                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        //ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.BusinessStatusAndDescriptionSet) {
                HideLoading();
                Feedback<Boolean> FeedBack = (Feedback<Boolean>) Data;

                if (FeedBack.getStatus() == FeedbackType.UpdatedSuccessful.getId()) {
                    ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);

                    if (FeedBack.getValue()) {
                        FactorStatusId = StatusFactor;

                        SetInformationToSpinner(FactorStatusViewModels);

                        StatusFactorTextViewBusinessFactorDetailActivity.setText(StatusFactorTextViewBusinessFactorDetailActivity.getText().toString());
                        if (IsChangeDescription) {
                            BusinessDescriptionEditTextBusinessFactorDetailActivity.setText(BusinessDescriptionEditTextBusinessFactorDetailActivity.getText().toString());
                        } else {
                            BusinessDescriptionEditTextBusinessFactorDetailActivity.setText(Description);
                        }
                    } else {
                        StatusFactorTextViewBusinessFactorDetailActivity.setText(FactorStatusTitle);
                        BusinessDescriptionEditTextBusinessFactorDetailActivity.setText(Description);
                    }
                } else {
                    StatusFactorTextViewBusinessFactorDetailActivity.setText(FactorStatusTitle);
                    BusinessDescriptionEditTextBusinessFactorDetailActivity.setText(Description);

                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.BusinessDescriptionSet) {
                HideLoading();
                Feedback<Boolean> FeedBack = (Feedback<Boolean>) Data;

                if (FeedBack.getStatus() == FeedbackType.UpdatedSuccessful.getId()) {
                    ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);

                    if (FeedBack.getValue()) {
                        BusinessDescriptionEditTextBusinessFactorDetailActivity.setText(BusinessDescriptionEditTextBusinessFactorDetailActivity.getText().toString());
                    } else {
                        BusinessDescriptionEditTextBusinessFactorDetailActivity.setText(Description);
                    }
                } else {
                    BusinessDescriptionEditTextBusinessFactorDetailActivity.setText(Description);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (adapterView.getId()) {
            case R.id.StatusFactorSpinnerBusinessFactorDetailActivity: {

                StatusFactor = FactorStatusAdapterViewModel.get(i).getId();
                StatusFactorTextViewBusinessFactorDetailActivity.setText(FactorStatusAdapterViewModel.get(i).getTitle());

                //نمایش ایکون کنار spinner تنها در انتخاب یک position خاص یا اولین position
                ImageView ArrowDropDownImageView = view.findViewById(R.id.ArrowDropDownImageView);
                int Position = (int) ArrowDropDownImageView.getTag();

                if (Position == i) {
                    ArrowDropDownImageView.setVisibility(View.VISIBLE);
                } else {
                    ArrowDropDownImageView.setVisibility(View.GONE);
                }
                //--------------------------------------------------------------------------
                break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private StatusAndDescriptionFactorInViewModel MadeViewModel() {

        StatusAndDescriptionFactorInViewModel ViewModel = new StatusAndDescriptionFactorInViewModel();
        ViewModel.setFactorId(FactorId);
        if (Description.equals(BusinessDescriptionEditTextBusinessFactorDetailActivity.getText().toString())) {
            ViewModel.setDescription(null);
            IsChangeDescription = false;
        } else {
            ViewModel.setDescription(BusinessDescriptionEditTextBusinessFactorDetailActivity.getText().toString());
            IsChangeDescription = true;
        }
        ViewModel.setFactorStatusId(StatusFactor);

        return ViewModel;
    }

    private DescriptionFactorInViewModel MadeViewModels() {

        DescriptionFactorInViewModel ViewModel = new DescriptionFactorInViewModel();
        ViewModel.setFactorId(FactorId);
        ViewModel.setDescription(BusinessDescriptionEditTextBusinessFactorDetailActivity.getText().toString());

        return ViewModel;
    }

    @SuppressLint("SetTextI18n")
    private void SetInformationToView(FactorViewModel ViewModelList) {

        UserNameTextViewBusinessFactorDetailActivity.setText(ViewModelList.getUserFullName());
        CreateDateUserFactorTextViewBusinessFactorDetailActivity.setText(ViewModelList.getCreate());
        NumberOfOrderItemsUserFactorTextViewBusinessFactorDetailActivity.setText(String.valueOf(ViewModelList.getItemList().size()));
        BusinessFactorCodeTextViewBusinessFactorDetailActivity.setText(String.valueOf(ViewModelList.getId()));

        if (ViewModelList.getUserCellPhone() == null) {
            UserCellPhoneLinearLayoutBusinessFactorDetailActivity.setVisibility(View.GONE);
        } else {
            UserCellPhoneLinearLayoutBusinessFactorDetailActivity.setVisibility(View.VISIBLE);
            UserCellPhoneTextViewBusinessFactorDetailActivity.setText(getResources().getString(R.string.zero) + String.valueOf(ViewModelList.getUserCellPhone()));
        }

        PhoneNumber = UserCellPhoneTextViewBusinessFactorDetailActivity.getText().toString();

        Latitude = ViewModelList.getUserLatitude();
        Longitude = ViewModelList.getUserLongitude();
        Description = ViewModelList.getBusinessDescription();

        if (Longitude > 0) {
            ShowMapLinearLayoutBusinessFactorDetailActivity.setVisibility(View.VISIBLE);
        } else {
            ShowMapLinearLayoutBusinessFactorDetailActivity.setVisibility(View.GONE);
        }

        boolean IsZeroPrice = false;

        ItemList = ViewModelList.getItemList();
        for (int i = 0; i < ItemList.size(); i++) {
            if (ItemList.get(i).getPrice() == 0) {
                IsZeroPrice = true;
            }
        }

        if (ViewModelList.getTotalPrice() < 0) {
            PricePayableUserFactorTextViewBusinessFactorDetailActivity.setText(getResources().getString(R.string.unknown));
        } else {

            double DeliveryCost = ViewModelList.getDeliveryCost();
            Double PayablePrice;
            if (DeliveryCost <= 0) {
                PayablePrice = ViewModelList.getTotalPrice();
            } else {
                PayablePrice = ViewModelList.getTotalPrice() + DeliveryCost;
            }

            if (ViewModelList.isHasQuickOrder()) {

                PricePayableUserFactorTextViewBusinessFactorDetailActivity.setText(Utility.GetIntegerNumberWithComma(PayablePrice) + " " + getResources().getString(R.string.toman) + " - " + getResources().getString(R.string.price_is_not_definitive));

                DescriptionUserFactorTextViewBusinessFactorDetailActivity.setVisibility(View.VISIBLE);
                DescriptionUserFactorTextViewBusinessFactorDetailActivity.setText(getResources().getString(R.string.in_your_factor_there_are_products_that_are_not_priced));
            } else {
                if (IsZeroPrice) {
                    PricePayableUserFactorTextViewBusinessFactorDetailActivity.setText(Utility.GetIntegerNumberWithComma(PayablePrice) + " " + getResources().getString(R.string.toman) + " - " + getResources().getString(R.string.price_is_not_definitive));

                    DescriptionUserFactorTextViewBusinessFactorDetailActivity.setVisibility(View.VISIBLE);
                    DescriptionUserFactorTextViewBusinessFactorDetailActivity.setText(getResources().getString(R.string.in_your_factor_there_are_products_that_are_not_priced));
                } else {
                    DescriptionUserFactorTextViewBusinessFactorDetailActivity.setVisibility(View.GONE);
                    PricePayableUserFactorTextViewBusinessFactorDetailActivity.setText(Utility.GetIntegerNumberWithComma(PayablePrice) + " " + getResources().getString(R.string.toman));
                }
            }
        }

        if (ViewModelList.getUserDescription() == null || ViewModelList.getUserDescription().equals("")) {
            UserDescriptionCardViewBusinessFactorDetailActivity.setVisibility(View.GONE);
        } else {
            UserDescriptionCardViewBusinessFactorDetailActivity.setVisibility(View.VISIBLE);
            UserDescriptionTextViewBusinessFactorDetailActivity.setText(ViewModelList.getUserDescription());
        }


        if (ViewModelList.getBusinessDescription() != null || !ViewModelList.getBusinessDescription().equals("")) {
            BusinessDescriptionEditTextBusinessFactorDetailActivity.setText(ViewModelList.getBusinessDescription());
        }

        if (ViewModelList.getUserAddress() == null || ViewModelList.getUserAddress().equals("")) {
            UserAddressTextViewBusinessFactorDetailActivity.setText(getResources().getString(R.string.customer_order_will_be_delivered_to_the_business_address));
            ShowMapLinearLayoutBusinessFactorDetailActivity.setVisibility(View.GONE);
        } else {
            ShowMapLinearLayoutBusinessFactorDetailActivity.setVisibility(View.VISIBLE);
            UserAddressTextViewBusinessFactorDetailActivity.setText(ViewModelList.getUserAddress());
        }


        if (ViewModelList.getFactorStatusId() >= 0)
            StatusFactorSpinnerBusinessFactorDetailActivity.setSelection(SetPositionToSpinnerUserFactorStatus(ViewModelList.getFactorStatusId(), FactorStatusAdapterViewModel));

        StatusFactorTextViewBusinessFactorDetailActivity.setText(FactorStatusTitle);
    }

    private void SetInformationToSpinner(List<FactorStatusViewModel> ViewModel) {

        FactorStatusAdapterViewModel = new ArrayList<>();
        FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(-1, getResources().getString(R.string.please_select)));

        for (int i = 0; i < ViewModel.size(); i++) {

            if (FactorStatusId == ViewModel.get(i).getId()) {
                StatusFactor = ViewModel.get(i).getId();
                StatusFactorTextViewBusinessFactorDetailActivity.setText(ViewModel.get(i).getTitle());
                FactorStatusTitle = ViewModel.get(i).getTitle();
            }
        }

        if (StatusFactor == FactorStatus.Received.getId()+1 || StatusFactor == FactorStatus.CanceledByUser.getId()+1 ||
                StatusFactor == FactorStatus.CanceledByBusiness.getId()+1 || StatusFactor == FactorStatus.Delivered.getId()+1) {
            StatusFactorSpinnerBusinessFactorDetailActivity.setVisibility(View.GONE);
            EditButtonBusinessFactorDetailActivity.setVisibility(View.GONE);
        } else {
            StatusFactorSpinnerBusinessFactorDetailActivity.setVisibility(View.VISIBLE);
            EditButtonBusinessFactorDetailActivity.setVisibility(View.VISIBLE);
        }

        if (StatusFactor == FactorStatus.Ordering.getId()+1 || StatusFactor == FactorStatus.Etc.getId()+1 || StatusFactor == FactorStatus.NotShow.getId()+1) {

            for (int i = 0; i < ViewModel.size(); i++) {

                if (ViewModel.get(i).getStatus() == FactorStatus.Reviewing.getId())
                    FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(ViewModel.get(i).getId(), ViewModel.get(i).getTitle()));
                else if (ViewModel.get(i).getStatus() == FactorStatus.Preparing.getId())
                    FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(ViewModel.get(i).getId(), ViewModel.get(i).getTitle()));
                else if (ViewModel.get(i).getStatus() == FactorStatus.DeliveredToCourier.getId())
                    FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(ViewModel.get(i).getId(), ViewModel.get(i).getTitle()));
                else if (ViewModel.get(i).getStatus() == FactorStatus.Sending.getId())
                    FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(ViewModel.get(i).getId(), ViewModel.get(i).getTitle()));
                else if (ViewModel.get(i).getStatus() == FactorStatus.Delivered.getId())
                    FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(ViewModel.get(i).getId(), ViewModel.get(i).getTitle()));
                else if (ViewModel.get(i).getStatus() == FactorStatus.CanceledByBusiness.getId())
                    FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(ViewModel.get(i).getId(), ViewModel.get(i).getTitle()));
                else if (ViewModel.get(i).getStatus() == FactorStatus.Etc.getId())
                    FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(ViewModel.get(i).getId(), ViewModel.get(i).getTitle()));
            }
        }

        if (StatusFactor == FactorStatus.Reviewing.getId()+1) {

            for (int i = 0; i < ViewModel.size(); i++) {
                if (ViewModel.get(i).getStatus() == FactorStatus.Preparing.getId())
                    FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(ViewModel.get(i).getId(), ViewModel.get(i).getTitle()));
                else if (ViewModel.get(i).getStatus() == FactorStatus.DeliveredToCourier.getId())
                    FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(ViewModel.get(i).getId(), ViewModel.get(i).getTitle()));
                else if (ViewModel.get(i).getStatus() == FactorStatus.Sending.getId())
                    FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(ViewModel.get(i).getId(), ViewModel.get(i).getTitle()));
                else if (ViewModel.get(i).getStatus() == FactorStatus.Delivered.getId())
                    FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(ViewModel.get(i).getId(), ViewModel.get(i).getTitle()));
                else if (ViewModel.get(i).getStatus() == FactorStatus.CanceledByBusiness.getId())
                    FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(ViewModel.get(i).getId(), ViewModel.get(i).getTitle()));
                else if (ViewModel.get(i).getStatus() == FactorStatus.Etc.getId())
                    FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(ViewModel.get(i).getId(), ViewModel.get(i).getTitle()));
            }
        }

        if (StatusFactor == FactorStatus.Preparing.getId()+1) {

            for (int i = 0; i < ViewModel.size(); i++) {
                if (ViewModel.get(i).getStatus() == FactorStatus.DeliveredToCourier.getId())
                    FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(ViewModel.get(i).getId(), ViewModel.get(i).getTitle()));
                else if (ViewModel.get(i).getStatus() == FactorStatus.Sending.getId())
                    FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(ViewModel.get(i).getId(), ViewModel.get(i).getTitle()));
                else if (ViewModel.get(i).getStatus() == FactorStatus.Delivered.getId())
                    FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(ViewModel.get(i).getId(), ViewModel.get(i).getTitle()));
                else if (ViewModel.get(i).getStatus() == FactorStatus.CanceledByBusiness.getId())
                    FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(ViewModel.get(i).getId(), ViewModel.get(i).getTitle()));
                else if (ViewModel.get(i).getStatus() == FactorStatus.Etc.getId())
                    FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(ViewModel.get(i).getId(), ViewModel.get(i).getTitle()));
            }
        }

        if (StatusFactor == FactorStatus.DeliveredToCourier.getId()+1) {

            for (int i = 0; i < ViewModel.size(); i++) {

                if (ViewModel.get(i).getStatus() == FactorStatus.Sending.getId())
                    FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(ViewModel.get(i).getId(), ViewModel.get(i).getTitle()));
                else if (ViewModel.get(i).getStatus() == FactorStatus.Delivered.getId())
                    FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(ViewModel.get(i).getId(), ViewModel.get(i).getTitle()));
                else if (ViewModel.get(i).getStatus() == FactorStatus.CanceledByBusiness.getId())
                    FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(ViewModel.get(i).getId(), ViewModel.get(i).getTitle()));
                else if (ViewModel.get(i).getStatus() == FactorStatus.Etc.getId())
                    FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(ViewModel.get(i).getId(), ViewModel.get(i).getTitle()));
            }
        }


        if (StatusFactor == FactorStatus.Sending.getId()+1) {

            for (int i = 0; i < ViewModel.size(); i++) {

                if (ViewModel.get(i).getStatus() == FactorStatus.Delivered.getId())
                    FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(ViewModel.get(i).getId(), ViewModel.get(i).getTitle()));
                else if (ViewModel.get(i).getStatus() == FactorStatus.CanceledByBusiness.getId())
                    FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(ViewModel.get(i).getId(), ViewModel.get(i).getTitle()));
                else if (ViewModel.get(i).getStatus() == FactorStatus.Etc.getId())
                    FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(ViewModel.get(i).getId(), ViewModel.get(i).getTitle()));
            }
        }


        FactorStatusSpinnerAdapter factorStatusSpinnerAdapter = new FactorStatusSpinnerAdapter(BusinessFactorDetailsActivity.this, FactorStatusAdapterViewModel);
        StatusFactorSpinnerBusinessFactorDetailActivity.setAdapter(factorStatusSpinnerAdapter);

    }

    private int SetPositionToSpinnerUserFactorStatus(int SelectId, List<FactorStatusAdapterViewModel> ViewModel) {
        int Position = 0;

        if (ViewModel != null) {
            for (int i = 0; i < ViewModel.size(); i++) {
                if (SelectId == ViewModel.get(i).getId()) {
                    Position = i;
                }
            }
        } else {
            Position = 0;
        }
        return Position;
    }

    private void ChangeFactorStatus() {


        BusinessFactorService Service = new BusinessFactorService(this);
        if (StatusFactor == -1) {
            if (Description.equals(BusinessDescriptionEditTextBusinessFactorDetailActivity.getText().toString())) {
                ShowToast(getResources().getString(R.string.no_change_description_status_factor), Toast.LENGTH_LONG, MessageType.Warning);
            } else {
                ShowLoadingProgressBar();
                Service.SetDescription(MadeViewModels());
            }
        } else {
            if (StatusFactor == FactorStatusId) {
                if (Description.equals(BusinessDescriptionEditTextBusinessFactorDetailActivity.getText().toString())) {
                    ShowToast(getResources().getString(R.string.no_change_description_status_factor), Toast.LENGTH_LONG, MessageType.Warning);
                } else {
                    ShowLoadingProgressBar();
                    Service.SetDescription(MadeViewModels());
                }
            } else {
                ShowLoadingProgressBar();
                Service.SetStatusAndDescription(MadeViewModel());

            }
        }

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
