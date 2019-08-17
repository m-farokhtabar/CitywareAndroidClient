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


public class PaymentCommissionActivity extends BaseActivity implements IResponseService {

    private String PricePayable = "";
    private String Id = "";
    private int BusinessId ;
    private String myId = "";
    private boolean IsPay = false;

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

        PricePayable = getIntent().getExtras().getString("PricePayable");
        Id = getIntent().getExtras().getString("Id");
        BusinessId = getIntent().getExtras().getInt("BusinessId");


        //ایجاد طرح بندی صفحه
        CreateLayout();
    }

    private void CreateLayout() {
        TextViewPersian PricePayableTextViewPaymentCommissionActivity = findViewById(R.id.PricePayableTextViewPaymentCommissionActivity);
        TextViewPersian BusinessNameTextViewPaymentCommissionActivity = findViewById(R.id.BusinessNameTextViewPaymentCommissionActivity);
        ButtonPersianView SubmitPaymentPackageButtonPaymentCommissionActivity = findViewById(R.id.SubmitPaymentPackageButtonPaymentCommissionActivity);
        final RadioButton BankSelectedRadioButtonPaymentCommissionActivity = findViewById(R.id.BankSelectedRadioButtonPaymentCommissionActivity);


        BusinessNameTextViewPaymentCommissionActivity.setVisibility(View.GONE);
        PricePayableTextViewPaymentCommissionActivity.setText(PricePayable);


        //        List<String> myList = new ArrayList<String>(Arrays.asList(Id.split("_")));
//        myId = new ArrayList<>();
//        for (int i = 0; i < myList.size(); i++) {
//            myId.add( Integer.valueOf(myList.get(i)));
//        }


        SubmitPaymentPackageButtonPaymentCommissionActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (BankSelectedRadioButtonPaymentCommissionActivity.isChecked()) {

                    int PayablePrice;
                    if (PricePayable.contains(",")) {
                        PricePayable = PricePayable.replaceAll(",", "");
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
                        String url = "http://asanpardakhtpg.zeytoonfood.com/startpayment.aspx?id=" + Id + "&type=2";
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }

                } else {
                    ShowToast(getResources().getString(R.string.please_select_bank), Toast.LENGTH_LONG, MessageType.Warning);
                }
            }
        });

    }

    public void LoadData() {
        myId = Id.replaceAll("_",",");
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

//                    if (ViewModelList != null) {
//                        boolean IsHavePackageId = false;
//
//                         for (int i=0;i<ViewModelList.size();i++){
//                             for (int j=0;j<myId.size();j++) {
//                                 if (ViewModelList.get(i).getId()==myId.get(j)){
//                                     IsHavePackageId = true;
//                                     break;
//                                 }
//                             }
//                         }

                         if (FeedBack.getValue()){
                             ShowToast(getResources().getString(R.string.your_payment_was_successfully_paid), Toast.LENGTH_LONG, MessageType.Info);
                             SendDataToParentActivity();
                             onBackPressed();
                         }else {
                             ShowToast(getResources().getString(R.string.submit_package_unsuccessful), Toast.LENGTH_LONG, MessageType.Error);
                         }

                }  else {
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
        MessageTextView.getLayoutParams().width = LayoutUtility.GetWidthAccordingToScreen(PaymentCommissionActivity.this, 1);
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

    @Override
    protected void onRestart() {
        super.onRestart();

        if (IsPay ){
            LoadData();
            IsPay = false;
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
        SendDataToParentActivity();
        super.onBackPressed();
    }


}
