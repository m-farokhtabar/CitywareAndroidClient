package ir.rayas.app.citywareclient.View.MasterChildren;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.HashMap;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.BusinessCommissionRepository;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Marketing.MarketingService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResultPassing;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Payment.BusinessCommissionPaymentViewModel;


public class PaymentCommissionActivity extends BaseActivity implements IResponseService {

    private String PricePayable = "";
    private String Id = "";
    private int BusinessId;
    private boolean IsPay = false;


    private TextViewPersian PricePayableTextViewPaymentCommissionActivity = null;
    private BusinessCommissionRepository businessCommissionRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_commission);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.PAYMENT_COMMISION_ACTIVITY);

        //آماده سازی قسمت لودینگ و پنجره خطا و سیستم پیغام در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {

            }
        }, R.string.payment);


        businessCommissionRepository = new BusinessCommissionRepository();

        PricePayable = getIntent().getExtras().getString("PricePayable");
        Id = getIntent().getExtras().getString("Id");
        BusinessId = getIntent().getExtras().getInt("BusinessId");


        if (businessCommissionRepository.getBusinessCommission() != null) {

            BusinessCommissionPaymentViewModel ViewModel = businessCommissionRepository.getBusinessCommission();
            Id = ViewModel.getId();
            PricePayable = ViewModel.getPricePayable();
            BusinessId = ViewModel.getBusinessId();
            IsPay = ViewModel.isPay();

            businessCommissionRepository.ClearBusinessCommission();
            if (IsPay)
                LoadData();
            IsPay = false;
        }

        if (PricePayable != null) {
            if (!PricePayable.equals("")) {
                PricePayable = PricePayable.replaceAll("ﺗﻮﻣﺎﻥ", "");
            }else {
                ShowNotValidRequestDialog();
            }
        } else {
            ShowNotValidRequestDialog();
        }


        //ایجاد طرح بندی صفحه
        CreateLayout();
    }

    private void CreateLayout() {
        PricePayableTextViewPaymentCommissionActivity = findViewById(R.id.PricePayableTextViewPaymentCommissionActivity);
        TextViewPersian BusinessNameTextViewPaymentCommissionActivity = findViewById(R.id.BusinessNameTextViewPaymentCommissionActivity);
        ButtonPersianView SubmitPaymentPackageButtonPaymentCommissionActivity = findViewById(R.id.SubmitPaymentPackageButtonPaymentCommissionActivity);
        final RadioButton BankSelectedRadioButtonPaymentCommissionActivity = findViewById(R.id.BankSelectedRadioButtonPaymentCommissionActivity);

        BusinessNameTextViewPaymentCommissionActivity.setVisibility(View.GONE);
        PricePayableTextViewPaymentCommissionActivity.setText(PricePayable);

        SubmitPaymentPackageButtonPaymentCommissionActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (BankSelectedRadioButtonPaymentCommissionActivity.isChecked()) {

                    if (PricePayable != null) {
                        int PayablePrice;
                        if (PricePayable.contains(",")) {
                            PricePayable = PricePayable.replaceAll(",", "");
                        }
                        if (PricePayable.contains("٬")) {
                            PricePayable = PricePayable.replaceAll("٬", "");
                        }
                        if (PricePayable.contains("ﺗﻮﻣﺎﻥ")) {
                            PricePayable = PricePayable.replaceAll("ﺗﻮﻣﺎﻥ", "");
                        }
                        if (PricePayable.contains(" ")) {
                            PricePayable = PricePayable.replaceAll(" ", "");
                        }

                        PayablePrice = Integer.valueOf(PricePayable);

                        if (PayablePrice > DefaultConstant.MaxPayment || PayablePrice < DefaultConstant.MinPayment) {
                            ShowPaymentPackageDialog();
                        } else {

                            IsPay = true;

                            if (businessCommissionRepository.getBusinessCommission() != null)
                                businessCommissionRepository.ClearBusinessCommission();

                            BusinessCommissionPaymentViewModel businessCommissionPaymentViewModel = new BusinessCommissionPaymentViewModel();
                            businessCommissionPaymentViewModel.setBusinessId(BusinessId);
                            businessCommissionPaymentViewModel.setId(Id);
                            businessCommissionPaymentViewModel.setPricePayable(PricePayable);
                            businessCommissionPaymentViewModel.setPay(IsPay);

                            businessCommissionRepository.setBusinessCommission(businessCommissionPaymentViewModel);


                            String url = "http://asanpardakhtpg.zeytoonfood.com/startpayment.aspx?id=" + Id + "&type=2";
                            Uri uri = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    }


                } else {
                    ShowToast(getResources().getString(R.string.please_select_bank), Toast.LENGTH_LONG, MessageType.Warning);
                }
            }
        });

    }

    public void LoadData() {
        String myId = Id.replaceAll("_", ",");
        ShowLoadingProgressBar();

        MarketingService MarketingService = new MarketingService(this);
        MarketingService.IsCommissionPayed(BusinessId, myId);
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
            if (ServiceMethod == ServiceMethodType.IsCommissionPayedGet) {
                Feedback<Boolean> FeedBack = (Feedback<Boolean>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    if (FeedBack.getValue()) {
                        ShowToast(getResources().getString(R.string.your_payment_was_successfully_paid), Toast.LENGTH_LONG, MessageType.Info);
                        SendDataToParentActivity();
                        onBackPressed();
                    } else {
                        ShowToast(getResources().getString(R.string.submit_package_unsuccessful), Toast.LENGTH_LONG, MessageType.Warning);
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
            ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
    }

    /**
     * دریافت ویومدل پوستر خریداری شده و ارسال آن به اکتیویتی پروفایل کاربر جهت نمایش در لیست پوسترهای فعال
     */
    private void SendDataToParentActivity() {
        HashMap<String, Object> Output = new HashMap<>();
        ActivityResultPassing.Push(new ActivityResult(getParentActivity(), getCurrentActivityId(), Output));
    }

    private void ShowPaymentPackageDialog() {

        final Dialog ShowPaymentPackageDialog = new Dialog(PaymentCommissionActivity.this);
        ShowPaymentPackageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ShowPaymentPackageDialog.setCanceledOnTouchOutside(false);
        ShowPaymentPackageDialog.setContentView(R.layout.dialog_payment_package);

        TextViewPersian MessageTextView = ShowPaymentPackageDialog.findViewById(R.id.MessageTextView);
        TextViewPersian HeaderColorDialog = ShowPaymentPackageDialog.findViewById(R.id.HeaderColorDialog);
        HeaderColorDialog.getLayoutParams().width = LayoutUtility.GetWidthAccordingToScreen(PaymentCommissionActivity.this, 1);
        MessageTextView.setText(getResources().getString(R.string.due_to_bank_ports_limitations_payment_is_not_possible));
        ButtonPersianView DialogOkButton = ShowPaymentPackageDialog.findViewById(R.id.DialogOkButton);
        DialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //       SendDataToParentActivity();
//        //این قسمت به دلیل SingleInstance بودن Parent بایستی مطمئن شوبم که اکتیویتی Parent بعد از اتمام این اکتیویتی دوباره صدا  زده می شود
//        //در حالت خروج از برنامه و ورود دوباره این اکتیوتی ممکن است Parent خود را گم کند
                FinishCurrentActivity();
                ShowPaymentPackageDialog.dismiss();
            }
        });

        ShowPaymentPackageDialog.show();
    }

    private void ShowNotValidRequestDialog() {

        final Dialog ShowNotValidRequestDialog = new Dialog(PaymentCommissionActivity.this);
        ShowNotValidRequestDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ShowNotValidRequestDialog.setCanceledOnTouchOutside(false);
        ShowNotValidRequestDialog.setContentView(R.layout.dialog_payment_package);

        TextViewPersian MessageTextView = ShowNotValidRequestDialog.findViewById(R.id.MessageTextView);
        TextViewPersian HeaderColorDialog = ShowNotValidRequestDialog.findViewById(R.id.HeaderColorDialog);
        HeaderColorDialog.getLayoutParams().width = LayoutUtility.GetWidthAccordingToScreen(PaymentCommissionActivity.this, 1);
        MessageTextView.setText(getResources().getString(R.string.not_valid_request));
        ButtonPersianView DialogOkButton = ShowNotValidRequestDialog.findViewById(R.id.DialogOkButton);
        DialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FinishCurrentActivity();
                ShowNotValidRequestDialog.dismiss();
            }
        });

        ShowNotValidRequestDialog.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (IsPay) {

            if (businessCommissionRepository.getBusinessCommission() != null) {

                BusinessCommissionPaymentViewModel ViewModel = businessCommissionRepository.getBusinessCommission();
                Id = ViewModel.getId();
                PricePayable = ViewModel.getPricePayable();
                BusinessId = ViewModel.getBusinessId();
                IsPay = ViewModel.isPay();

                businessCommissionRepository.ClearBusinessCommission();
            }

            if (PricePayable != null) {
                if (!PricePayable.equals("")) {
                    PricePayableTextViewPaymentCommissionActivity.setText(PricePayable);
                    LoadData();
                    IsPay = false;
                } else {
                    ShowNotValidRequestDialog();
                }
            } else {
                ShowNotValidRequestDialog();
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
        if (businessCommissionRepository.getBusinessCommission() != null) {
            businessCommissionRepository.ClearBusinessCommission();
        }

        SendDataToParentActivity();
        super.onBackPressed();
    }


}
