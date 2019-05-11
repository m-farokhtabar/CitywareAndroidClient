package ir.rayas.app.citywareclient.View.Share;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.CommissionProductRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Marketing.MarketingService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.EditTextPersian;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Marketing.BusinessCommissionAndDiscountViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.MarketerSuggestionViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.ProductCommissionAndDiscountViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.SuggestionInfoViewModel;


public class CommissionActivity extends BaseActivity implements IResponseService {

    private RecyclerView ShowProductListRecyclerViewCommissionActivity = null;
    private CardView CommissionProductCardViewCommissionActivity = null;
    private TextViewPersian MarketingCommissionTextViewCommissionActivity = null;
    private TextViewPersian CustomerPercentTextViewCommissionActivity = null;
    private CardView UserNameCardViewCommissionActivity = null;
    private TextViewPersian UserNameTextViewCommissionActivity = null;
    private TextViewPersian MessageTextViewCommissionActivity = null;
    private EditTextPersian DescriptionDialog = null;

    private int BusinessId = 0;
    private String BusinessName = "";
    private String UserName = "";
    private int UserId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.COMMISSION_ACTIVITY);

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                LoadData();
            }
        }, R.string.introducing_your_friends_to_receive_commission);

        BusinessId = getIntent().getExtras().getInt("BusinessId");
        BusinessName = getIntent().getExtras().getString("BusinessName");

        CreateLayout();
        //دریافت اطلاعات از سرور
        LoadData();
    }

    private void CreateLayout() {

        ButtonPersianView IntroducingFriendsButtonCommissionActivity = findViewById(R.id.IntroducingFriendsButtonCommissionActivity);
        ButtonPersianView UserSelectionButtonCommissionActivity = findViewById(R.id.UserSelectionButtonCommissionActivity);
        TextViewPersian TitleBusinessTextViewCommissionActivity = findViewById(R.id.TitleBusinessTextViewCommissionActivity);
        MessageTextViewCommissionActivity = findViewById(R.id.MessageTextViewCommissionActivity);
        CommissionProductCardViewCommissionActivity = findViewById(R.id.CommissionProductCardViewCommissionActivity);
        MarketingCommissionTextViewCommissionActivity = findViewById(R.id.MarketingCommissionTextViewCommissionActivity);
        CustomerPercentTextViewCommissionActivity = findViewById(R.id.CustomerPercentTextViewCommissionActivity);
        UserNameCardViewCommissionActivity = findViewById(R.id.UserNameCardViewCommissionActivity);
        UserNameTextViewCommissionActivity = findViewById(R.id.UserNameTextViewCommissionActivity);

        UserNameCardViewCommissionActivity.setVisibility(View.GONE);
        MessageTextViewCommissionActivity.setVisibility(View.GONE);

        ShowProductListRecyclerViewCommissionActivity = findViewById(R.id.ShowProductListRecyclerViewCommissionActivity);
        ShowProductListRecyclerViewCommissionActivity.setHasFixedSize(true);
        //به دلیل اینکه من در هر سطر یک گزینه نیاز دارم
        LinearLayoutManager RegionLinearLayoutManager = new LinearLayoutManager(CommissionActivity.this);
        ShowProductListRecyclerViewCommissionActivity.setLayoutManager(RegionLinearLayoutManager);

        TitleBusinessTextViewCommissionActivity.setText(BusinessName);

        UserSelectionButtonCommissionActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent UserSearchIntent = NewIntent(UserSearchActivity.class);
                startActivity(UserSearchIntent);
            }
        });

        IntroducingFriendsButtonCommissionActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserId == 0) {
                    ShowToast(getResources().getString(R.string.please_enter_user_selection), Toast.LENGTH_LONG, MessageType.Warning);
                } else {
                    ShowMessageIntroducingDialog();

                }
            }
        });

    }

    public void LoadData() {
        ShowLoadingProgressBar();

        MarketingService marketingService = new MarketingService(this);
        marketingService.Get(BusinessId);
    }

    private MarketerSuggestionViewModel MadeViewModel() {

        MarketerSuggestionViewModel ViewModel = new MarketerSuggestionViewModel();
        try {
            ViewModel.setBusinessId(BusinessId);
            ViewModel.setDescription(DescriptionDialog.getText().toString());
            ViewModel.setUserCustomerId(UserId);
        } catch (Exception Ex) {
            ShowToast(FeedbackType.InvalidDataFormat.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
        return ViewModel;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {

        HideLoading();

        try {
            if (ServiceMethod == ServiceMethodType.BusinessCommissionAndDiscountGet) {

                Feedback<BusinessCommissionAndDiscountViewModel> FeedBack = (Feedback<BusinessCommissionAndDiscountViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    final BusinessCommissionAndDiscountViewModel ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        if (ViewModel.getProductList() != null) {
                            MessageTextViewCommissionActivity.setVisibility(View.GONE);

                            List<ProductCommissionAndDiscountViewModel> ViewModelList = ViewModel.getProductList();

                            CommissionProductRecyclerViewAdapter commissionProductRecyclerViewAdapter = new CommissionProductRecyclerViewAdapter(CommissionActivity.this, ViewModelList);
                            ShowProductListRecyclerViewCommissionActivity.setAdapter(commissionProductRecyclerViewAdapter);
                        } else {
                            MessageTextViewCommissionActivity.setVisibility(View.VISIBLE);
                        }

                        CustomerPercentTextViewCommissionActivity.setText(String.valueOf(ViewModel.getCustomerPercent()) + " " + getResources().getString(R.string.percent));
                        MarketingCommissionTextViewCommissionActivity.setText(String.valueOf(ViewModel.getMarketerPercent()) + " " + getResources().getString(R.string.percent));

                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.MarketerSuggestionAdd) {

                Feedback<SuggestionInfoViewModel> FeedBack = (Feedback<SuggestionInfoViewModel>) Data;
                if (FeedBack.getStatus() == FeedbackType.RegisteredSuccessful.getId()) {

                    final SuggestionInfoViewModel ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        ShowTicketDialog(ViewModel);
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


    @Override
    protected void onGetResult(ActivityResult Result) {
        if (Result.getFromActivityId() == getCurrentActivityId()) {
            switch (Result.getToActivityId()) {
                case ActivityIdList.USER_SEARCH_ACTIVITY:
                    UserName = (String) Result.getData().get("UserName");
                    UserId = (int) Result.getData().get("UserId");

                    UserNameCardViewCommissionActivity.setVisibility(View.VISIBLE);
                    UserNameTextViewCommissionActivity.setText(UserName);

                    break;
            }
        }
        super.onGetResult(Result);
    }


    private void ShowMessageIntroducingDialog() {

        final Dialog IntroducingDialog = new Dialog(CommissionActivity.this);
        IntroducingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        IntroducingDialog.setContentView(R.layout.dialog_introducing);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/iransanslight.ttf");

        ButtonPersianView DialogOkButton = IntroducingDialog.findViewById(R.id.DialogOkButton);
        ButtonPersianView DialogCancelButton = IntroducingDialog.findViewById(R.id.DialogCancelButton);
        TextView MessageDialog = IntroducingDialog.findViewById(R.id.MessageDialog);
        DescriptionDialog = IntroducingDialog.findViewById(R.id.DescriptionDialog);

        MessageDialog.setTypeface(typeface);

        String NameBusiness = "<font color='#ff0000'><b>" + BusinessName + "</b></font>";
        String NameUser = "<font color='#ff0000'><b>" + UserName + "</b></font>";
        String Message = getResources().getString(R.string.you_business) + " " + NameBusiness + " " + getResources().getString(R.string.to) + " " + NameUser + " " + getResources().getString(R.string.do_you_introduce);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            MessageDialog.setText(Html.fromHtml(Message, Html.FROM_HTML_MODE_COMPACT));
        } else {
            MessageDialog.setText(Html.fromHtml(Message));
        }


        DialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowLoadingProgressBar();

                MarketingService marketingService = new MarketingService(CommissionActivity.this);
                marketingService.Add(MadeViewModel());

                IntroducingDialog.dismiss();

            }
        });

        DialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntroducingDialog.dismiss();
            }
        });

        IntroducingDialog.show();
    }

    private void ShowTicketDialog(final SuggestionInfoViewModel ViewModel) {

        final Dialog TicketDialog = new Dialog(CommissionActivity.this);
        TicketDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        TicketDialog.setContentView(R.layout.dialog_ticket);

        TextViewPersian TicketDialogTextView = TicketDialog.findViewById(R.id.TicketDialogTextView);
        TextViewPersian ExpireDateDialogTextView = TicketDialog.findViewById(R.id.ExpireDateDialogTextView);
        ButtonPersianView DialogCancelButton = TicketDialog.findViewById(R.id.DialogCancelButton);
        ButtonPersianView DialogOkButton = TicketDialog.findViewById(R.id.DialogOkButton);

        TicketDialogTextView.setText(ViewModel.getTicket());
        ExpireDateDialogTextView.setText(ViewModel.getTicketValidity());

        DialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShareView(ViewModel);
                TicketDialog.dismiss();

            }
        });

        DialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TicketDialog.dismiss();
            }
        });

        TicketDialog.show();
    }

    private void ShareView(SuggestionInfoViewModel ViewModel) {
        Intent intent;
        intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);

        BusinessCommissionAndDiscountViewModel businessCommissionAndDiscountViewModel = new BusinessCommissionAndDiscountViewModel();
        Gson gson = new Gson();
        Type listType = new TypeToken<BusinessCommissionAndDiscountViewModel>() {
        }.getType();
        businessCommissionAndDiscountViewModel = gson.fromJson(ViewModel.getPercents(), listType);


        String ShareMessage = "";

        String ShareBusinessName = "";
        String ShareBusinessAddress = "";
        String ShareMarketerPercent = "";
        String ShareCustomerPercent = "";

        if (businessCommissionAndDiscountViewModel != null) {
            ShareBusinessName = getResources().getString(R.string.business_title) + " " + businessCommissionAndDiscountViewModel.getBusinessName() + "\n";
            ShareBusinessAddress = getResources().getString(R.string.address) + " " + businessCommissionAndDiscountViewModel.getBusinessAddress() + "\n";
            ShareMarketerPercent = getResources().getString(R.string.marketer_commission) + " " + businessCommissionAndDiscountViewModel.getMarketerPercent() + " " + getResources().getString(R.string.percent) + "\n";
            ShareCustomerPercent = getResources().getString(R.string.discount_customer) + " " + businessCommissionAndDiscountViewModel.getCustomerPercent() + " " + getResources().getString(R.string.percent) + "\n";
        }

        String ShareTicket = getResources().getString(R.string.ticket_number) + " " + ViewModel.getTicket() + "\n";
        String ShareExpireDate = getResources().getString(R.string.expire_date) + " " + ViewModel.getTicketValidity() + "\n";

        ShareMessage = ShareBusinessName + ShareBusinessAddress + ShareMarketerPercent + ShareCustomerPercent + ShareTicket + ShareExpireDate;


        intent.putExtra(Intent.EXTRA_TEXT, ShareMessage);

        intent.setType("text/plain");
        startActivity(intent);
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
