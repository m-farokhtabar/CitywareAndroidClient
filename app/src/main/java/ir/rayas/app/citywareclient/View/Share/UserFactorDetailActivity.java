package ir.rayas.app.citywareclient.View.Share;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.UserFactorBusinessContactRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.UserFactorBusinessOpenTimeRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.Spinner.FactorStatusSpinnerAdapter;
import ir.rayas.app.citywareclient.Adapter.ViewModel.FactorStatusAdapterViewModel;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.RegionRepository;
import ir.rayas.app.citywareclient.Service.Business.BusinessContactService;
import ir.rayas.app.citywareclient.Service.Business.BusinessOpenTimeService;
import ir.rayas.app.citywareclient.Service.Factor.BusinessFactorService;
import ir.rayas.app.citywareclient.Service.Factor.FactorStatusService;
import ir.rayas.app.citywareclient.Service.Factor.UserFactorService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.FactorStatus;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResultPassing;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessContactViewModel;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessOpenTimeViewModel;
import ir.rayas.app.citywareclient.ViewModel.Factor.DescriptionFactorInViewModel;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorItemViewModel;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorStatusViewModel;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorViewModel;
import ir.rayas.app.citywareclient.ViewModel.Factor.StatusAndDescriptionFactorInViewModel;

public class UserFactorDetailActivity extends BaseActivity implements IResponseService, AdapterView.OnItemSelectedListener {

    private TextViewPersian BusinessNameTextViewUserFactorDetailActivity = null;
    private TextViewPersian CreateDateUserFactorTextViewUserFactorDetailActivity = null;
    private TextViewPersian UserFactorCodeTextViewUserFactorDetailActivity = null;
    private TextViewPersian NumberOfOrderItemsUserFactorTextViewUserFactorDetailActivity = null;
    private TextViewPersian PricePayableUserFactorTextViewUserFactorDetailActivity = null;
    private TextViewPersian DescriptionUserFactorTextViewUserFactorDetailActivity = null;
    private TextViewPersian BusinessDescriptionTextViewUserFactorDetailActivity = null;
    private EditText UserDescriptionEditTextUserFactorDetailActivity = null;
    private TextViewPersian BusinessAddressTextViewUserFactorDetailActivity = null;
    private TextViewPersian StatusFactorTextViewUserFactorDetailActivity = null;
    private CardView BusinessDescriptionCardViewUserFactorDetailActivity = null;
    private LinearLayout ShowMapLinearLayoutUserFactorDetailActivity = null;
    private Spinner StatusFactorSpinnerUserFactorDetailActivity = null;
    private ButtonPersianView EditButtonUserFactorDetailActivity = null;


    private int BusinessId = 0;
    private double Latitude = 0;
    private double Longitude = 0;
    private int FactorId = 0;
    private String Description = "";
    private int FactorStatusId = 0;
    private int CurrentFactorStatusId = 0;
    private String FactorStatusTitle = "";
    private boolean IsChangeDescription = false;
    private boolean IsChange = false;
    private boolean IsFirst = false;

    private List<FactorItemViewModel> ItemList = new ArrayList<>();
    private List<FactorStatusViewModel> FactorStatusViewModels = new ArrayList<>();
    private List<FactorStatusAdapterViewModel> FactorStatusAdapterViewModel = new ArrayList<>();

    private RegionRepository regionRepository = new RegionRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_factor_detail);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.USER_FACTOR_DETAIL_ACTIVITY);

        FactorId = getIntent().getExtras().getInt("FactorId");
        IsChange = false;
        IsFirst = true;

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

        BusinessNameTextViewUserFactorDetailActivity = findViewById(R.id.BusinessNameTextViewUserFactorDetailActivity);
        CreateDateUserFactorTextViewUserFactorDetailActivity = findViewById(R.id.CreateDateUserFactorTextViewUserFactorDetailActivity);
        UserFactorCodeTextViewUserFactorDetailActivity = findViewById(R.id.UserFactorCodeTextViewUserFactorDetailActivity);
        NumberOfOrderItemsUserFactorTextViewUserFactorDetailActivity = findViewById(R.id.NumberOfOrderItemsUserFactorTextViewUserFactorDetailActivity);
        PricePayableUserFactorTextViewUserFactorDetailActivity = findViewById(R.id.PricePayableUserFactorTextViewUserFactorDetailActivity);
        DescriptionUserFactorTextViewUserFactorDetailActivity = findViewById(R.id.DescriptionUserFactorTextViewUserFactorDetailActivity);
        BusinessDescriptionTextViewUserFactorDetailActivity = findViewById(R.id.BusinessDescriptionTextViewUserFactorDetailActivity);
        BusinessDescriptionCardViewUserFactorDetailActivity = findViewById(R.id.BusinessDescriptionCardViewUserFactorDetailActivity);
        UserDescriptionEditTextUserFactorDetailActivity = findViewById(R.id.UserDescriptionEditTextUserFactorDetailActivity);
        ShowMapLinearLayoutUserFactorDetailActivity = findViewById(R.id.ShowMapLinearLayoutUserFactorDetailActivity);
        RelativeLayout ContactRelativeLayoutUserFactorDetailActivity = findViewById(R.id.ContactRelativeLayoutUserFactorDetailActivity);
        RelativeLayout OpenTimeRelativeLayoutUserFactorDetailActivity = findViewById(R.id.OpenTimeRelativeLayoutUserFactorDetailActivity);
        TextViewPersian showMapBusinessIconTextViewUserFactorDetailActivity = findViewById(R.id.ShowMapBusinessIconTextViewUserFactorDetailActivity);
        TextViewPersian contactIconTextViewUserFactorDetailActivity = findViewById(R.id.ContactIconTextViewUserFactorDetailActivity);
        BusinessAddressTextViewUserFactorDetailActivity = findViewById(R.id.BusinessAddressTextViewUserFactorDetailActivity);
        TextViewPersian OpenTimeIconTextViewUserFactorDetailActivity = findViewById(R.id.OpenTimeIconTextViewUserFactorDetailActivity);
        StatusFactorSpinnerUserFactorDetailActivity = findViewById(R.id.StatusFactorSpinnerUserFactorDetailActivity);
        StatusFactorTextViewUserFactorDetailActivity = findViewById(R.id.StatusFactorTextViewUserFactorDetailActivity);

        ButtonPersianView ShowProductButtonUserFactorDetailActivity = findViewById(R.id.ShowProductButtonUserFactorDetailActivity);
        EditButtonUserFactorDetailActivity = findViewById(R.id.EditButtonUserFactorDetailActivity);

        contactIconTextViewUserFactorDetailActivity.setTypeface(Font.MasterIcon);
        contactIconTextViewUserFactorDetailActivity.setText("\uf095");

        showMapBusinessIconTextViewUserFactorDetailActivity.setTypeface(Font.MasterIcon);
        showMapBusinessIconTextViewUserFactorDetailActivity.setText("\uf041");

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
                MapIntent.putExtra("Longitude", Longitude);
                MapIntent.putExtra("Going", 2);
                startActivity(MapIntent);
            }
        });

        ShowProductButtonUserFactorDetailActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ProductDetailIntent = NewIntent(UserFactorProductDetailActivity.class);
                String ArrayAsString = new Gson().toJson(ItemList);
                ProductDetailIntent.putExtra("ArrayAsString", ArrayAsString);
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

        EditButtonUserFactorDetailActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeFactorStatus();
            }
        });

        StatusFactorSpinnerUserFactorDetailActivity.setOnItemSelectedListener(this);

    }

    /**
     * @param Data
     * @param ServiceMethod
     * @param <T>
     */
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {

        try {
            if (ServiceMethod == ServiceMethodType.UserFactorGet) {
                HideLoading();
                Feedback<FactorViewModel> FeedBack = (Feedback<FactorViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final FactorViewModel ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        CurrentFactorStatusId = ViewModelList.getFactorStatusId();
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
            } else if (ServiceMethod == ServiceMethodType.BusinessOpenTimeGetAll) {
                HideLoading();
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
                HideLoading();
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
            } else if (ServiceMethod == ServiceMethodType.FactorStatusGetAll) {
                Feedback<List<FactorStatusViewModel>> FeedBack = (Feedback<List<FactorStatusViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    UserFactorService UserFactorService = new UserFactorService(this);
                    UserFactorService.Get(FactorId);

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
            } else if (ServiceMethod == ServiceMethodType.UserStatusAndDescriptionSet) {
                HideLoading();
                Feedback<Boolean> FeedBack = (Feedback<Boolean>) Data;

                if (FeedBack.getStatus() == FeedbackType.UpdatedSuccessful.getId()) {
                    ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    if (FeedBack.getValue()) {

                        CurrentFactorStatusId = FactorStatusId;
                        IsChange = true;
                        IsFirst = true;

                        SetInformationToSpinner(FactorStatusViewModels);

                        StatusFactorTextViewUserFactorDetailActivity.setText(StatusFactorTextViewUserFactorDetailActivity.getText().toString());
                        if (IsChangeDescription) {
                            UserDescriptionEditTextUserFactorDetailActivity.setText(UserDescriptionEditTextUserFactorDetailActivity.getText().toString());
                        } else {
                            UserDescriptionEditTextUserFactorDetailActivity.setText(Description);
                        }
                    } else {
                        StatusFactorTextViewUserFactorDetailActivity.setText(FactorStatusTitle);
                        UserDescriptionEditTextUserFactorDetailActivity.setText(Description);
                    }
                } else {
                    StatusFactorTextViewUserFactorDetailActivity.setText(FactorStatusTitle);
                    UserDescriptionEditTextUserFactorDetailActivity.setText(Description);

                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.UserDescriptionSet) {
                HideLoading();
                Feedback<Boolean> FeedBack = (Feedback<Boolean>) Data;

                if (FeedBack.getStatus() == FeedbackType.UpdatedSuccessful.getId()) {
                    ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);

                    if (FeedBack.getValue()) {
                        IsChange = true;
                        UserDescriptionEditTextUserFactorDetailActivity.setText(UserDescriptionEditTextUserFactorDetailActivity.getText().toString());
                    } else {
                        UserDescriptionEditTextUserFactorDetailActivity.setText(Description);
                    }
                } else {
                    UserDescriptionEditTextUserFactorDetailActivity.setText(Description);
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
            case R.id.StatusFactorSpinnerUserFactorDetailActivity: {

                if (IsFirst) {
                    StatusFactorTextViewUserFactorDetailActivity.setText(FactorStatusTitle);
                    FactorStatusId = -1;
                } else {
                    StatusFactorTextViewUserFactorDetailActivity.setText(FactorStatusAdapterViewModel.get(i).getTitle());
                    FactorStatusId = FactorStatusAdapterViewModel.get(i).getId();
                }

                //نمایش ایکون کنار spinner تنها در انتخاب یک position خاص یا اولین position
                ImageView ArrowDropDownImageView = view.findViewById(R.id.ArrowDropDownImageView);
                int Position = (int) ArrowDropDownImageView.getTag();

                if (Position == i) {
                    ArrowDropDownImageView.setVisibility(View.VISIBLE);
                } else {
                    ArrowDropDownImageView.setVisibility(View.GONE);
                }
                //--------------------------------------------------------------------------

                IsFirst = false;
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
        if (Description.equals(UserDescriptionEditTextUserFactorDetailActivity.getText().toString()) || Description.equals("")) {
            ViewModel.setDescription(null);
            IsChangeDescription = false;
        } else {
            ViewModel.setDescription(UserDescriptionEditTextUserFactorDetailActivity.getText().toString());
            IsChangeDescription = true;
        }
        ViewModel.setFactorStatusId(FactorStatusId);

        return ViewModel;
    }

    private DescriptionFactorInViewModel MadeViewModels() {

        DescriptionFactorInViewModel ViewModel = new DescriptionFactorInViewModel();
        ViewModel.setFactorId(FactorId);
        ViewModel.setDescription(UserDescriptionEditTextUserFactorDetailActivity.getText().toString());

        return ViewModel;
    }

    @SuppressLint("SetTextI18n")
    private void SetInformationToView(FactorViewModel ViewModelList) {

        BusinessNameTextViewUserFactorDetailActivity.setText(ViewModelList.getBusinessName());
        CreateDateUserFactorTextViewUserFactorDetailActivity.setText(ViewModelList.getCreate());
        NumberOfOrderItemsUserFactorTextViewUserFactorDetailActivity.setText(String.valueOf(ViewModelList.getItemList().size()));
        UserFactorCodeTextViewUserFactorDetailActivity.setText(String.valueOf(ViewModelList.getId()));
        BusinessAddressTextViewUserFactorDetailActivity.setText(regionRepository.GetFullName(ViewModelList.getRegionId()) + " - " + ViewModelList.getBusinessAddress());

        BusinessId = ViewModelList.getBusinessId();
        Latitude = ViewModelList.getBusinessLatitude();
        Longitude = ViewModelList.getBusinessLongitude();
        Description = ViewModelList.getUserDescription();

        if (Longitude > 0) {
            ShowMapLinearLayoutUserFactorDetailActivity.setVisibility(View.VISIBLE);
        } else {
            ShowMapLinearLayoutUserFactorDetailActivity.setVisibility(View.GONE);
        }

        boolean IsZeroPrice = false;

        ItemList = ViewModelList.getItemList();
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

        if (ViewModelList.getUserDescription() != null || !ViewModelList.getUserDescription().equals("")) {
            UserDescriptionEditTextUserFactorDetailActivity.setText(ViewModelList.getUserDescription());
        }


        if (ViewModelList.getFactorStatusId() >= 0)
            StatusFactorSpinnerUserFactorDetailActivity.setSelection(SetPositionToSpinnerUserFactorStatus(ViewModelList.getFactorStatusId(), FactorStatusAdapterViewModel));

    }

    private void SetInformationToSpinner(List<FactorStatusViewModel> ViewModel) {
        Integer factorStatus = 0;

        FactorStatusAdapterViewModel = new ArrayList<>();
        FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(-1, getResources().getString(R.string.please_select), -1));

        for (int i = 0; i < ViewModel.size(); i++) {

            if (CurrentFactorStatusId == ViewModel.get(i).getId()) {
                factorStatus = ViewModel.get(i).getStatus();
                StatusFactorTextViewUserFactorDetailActivity.setText(ViewModel.get(i).getTitle());
                FactorStatusTitle = ViewModel.get(i).getTitle();
            }
        }

        if (factorStatus == FactorStatus.Received.getId() || factorStatus == FactorStatus.CanceledByUser.getId() || factorStatus == FactorStatus.CanceledByBusiness.getId()) {
            StatusFactorSpinnerUserFactorDetailActivity.setVisibility(View.GONE);
            EditButtonUserFactorDetailActivity.setVisibility(View.GONE);
            UserDescriptionEditTextUserFactorDetailActivity.setEnabled(false);
            UserDescriptionEditTextUserFactorDetailActivity.setClickable(false);

        } else {
            StatusFactorSpinnerUserFactorDetailActivity.setVisibility(View.VISIBLE);
            EditButtonUserFactorDetailActivity.setVisibility(View.VISIBLE);
            UserDescriptionEditTextUserFactorDetailActivity.setEnabled(true);
            UserDescriptionEditTextUserFactorDetailActivity.setClickable(true);


            if (factorStatus == FactorStatus.DeliveredToCourier.getId() || factorStatus == FactorStatus.Delivered.getId() ||
                    factorStatus == FactorStatus.Sending.getId() || factorStatus == FactorStatus.Preparing.getId() || factorStatus == FactorStatus.Sending.getId()) {

                for (int i = 0; i < ViewModel.size(); i++) {

                    if (ViewModel.get(i).getStatus() == FactorStatus.Received.getId())
                        FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(ViewModel.get(i).getId(), ViewModel.get(i).getTitle(), ViewModel.get(i).getStatus()));
                }
            } else {
                for (int i = 0; i < ViewModel.size(); i++) {

                    if (factorStatus == FactorStatus.Received.getId())
                        FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(ViewModel.get(i).getId(), ViewModel.get(i).getTitle(), ViewModel.get(i).getStatus()));
                    else if (ViewModel.get(i).getStatus() == FactorStatus.CanceledByUser.getId())
                        FactorStatusAdapterViewModel.add(new FactorStatusAdapterViewModel(ViewModel.get(i).getId(), ViewModel.get(i).getTitle(), ViewModel.get(i).getStatus()));

                }
            }
        }


        FactorStatusSpinnerAdapter factorStatusSpinnerAdapter = new FactorStatusSpinnerAdapter(UserFactorDetailActivity.this, FactorStatusAdapterViewModel);
        StatusFactorSpinnerUserFactorDetailActivity.setAdapter(factorStatusSpinnerAdapter);

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

    private void ShowDialogBusinessOpenTime(List<BusinessOpenTimeViewModel> ViewModel) {


        final Dialog ShowBusinessDetailsDialog = new Dialog(UserFactorDetailActivity.this);
        ShowBusinessDetailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ShowBusinessDetailsDialog.setContentView(R.layout.dialog_business_open_time);

        RecyclerView BusinessOpenTimeRecyclerViewShowBusinessDetailsActivity = ShowBusinessDetailsDialog.findViewById(R.id.BusinessOpenTimeRecyclerViewShowBusinessDetailsActivity);
        UserFactorBusinessOpenTimeRecyclerViewAdapter userFactorBusinessOpenTimeRecyclerViewAdapter = new UserFactorBusinessOpenTimeRecyclerViewAdapter(UserFactorDetailActivity.this, R.layout.recycler_view_dialog_open_time, ViewModel, BusinessOpenTimeRecyclerViewShowBusinessDetailsActivity);
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
        UserFactorBusinessContactRecyclerViewAdapter userFactorBusinessContactRecyclerViewAdapter = new UserFactorBusinessContactRecyclerViewAdapter(UserFactorDetailActivity.this, ViewModel, BusinessContactRecyclerViewShowBusinessDetailsActivity);
        LinearLayoutManager BusinessOpenTimeLinearLayoutManager = new LinearLayoutManager(UserFactorDetailActivity.this);
        BusinessContactRecyclerViewShowBusinessDetailsActivity.setLayoutManager(BusinessOpenTimeLinearLayoutManager);
        BusinessContactRecyclerViewShowBusinessDetailsActivity.setAdapter(userFactorBusinessContactRecyclerViewAdapter);

        ShowBusinessDetailsDialog.show();
    }

    private void ChangeFactorStatus() {

        UserFactorService Service = new UserFactorService(this);

        if (FactorStatusId == -1) {
            if (Description.equals(UserDescriptionEditTextUserFactorDetailActivity.getText().toString())) {
                ShowToast(getResources().getString(R.string.no_change_description_status_factor), Toast.LENGTH_LONG, MessageType.Warning);
            } else {
                ShowLoadingProgressBar();
                Service.SetDescription(MadeViewModels());
            }
        } else {
            if (FactorStatusId == CurrentFactorStatusId) {
                if (Description.equals(UserDescriptionEditTextUserFactorDetailActivity.getText().toString())) {
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

    private void SendDataToParentActivity() {
        HashMap<String, Object> Output = new HashMap<>();
        Output.put("IsChange", IsChange);
        ActivityResultPassing.Push(new ActivityResult(getParentActivity(), getCurrentActivityId(), Output));
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

        SendDataToParentActivity();
        super.onBackPressed();
    }
}

