package ir.rayas.app.citywareclient.View.MasterChildren;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.HashMap;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Coupon.CouponService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Package.PackageService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResultPassing;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.EditTextPersian;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Coupon.CouponViewModel;
import ir.rayas.app.citywareclient.ViewModel.Coupon.UserCouponViewModel;
import ir.rayas.app.citywareclient.ViewModel.Package.OutputPackageTransactionViewModel;
import ir.rayas.app.citywareclient.ViewModel.Package.PurchasePackageViewModel;


public class PaymentPackageActivity extends BaseActivity implements IResponseService {

    private TextViewPersian PricePayableCouponTextViewPaymentPackageActivity = null;
    private EditTextPersian SubmitCouponEditTextPaymentPackageActivity = null;
    private FrameLayout CouponFrameLayoutPaymentPackageActivity = null;
    private RelativeLayout CouponRelativeLayoutPaymentPackageActivity = null;

    private int PackageId = 0;
    private String PackageName = "";
    //    private String BusinessName = "";
    private int PricePayable = 0;

    private int RetryType = 0;
    private int TotalPaymentPrice = 0;
    private boolean IsValidCoupon = false;
    private String CouponCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_package);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.PAYMENT_PACKAGE_ACTIVITY);

        //آماده سازی قسمت لودینگ و پنجره خطا و سیستم پیغام در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonClick();
            }
        }, R.string.payment);

        //مشخص شدن صفحه ویرایش آدرس یا آدرس جدید
        PackageName = getIntent().getExtras().getString("PackageName");
//        BusinessName = getIntent().getExtras().getString("BusinessName");
        PricePayable = getIntent().getExtras().getInt("PricePayable");
        PackageId = getIntent().getExtras().getInt("PackageId");

        TotalPaymentPrice = PricePayable;

        //ایجاد طرح بندی صفحه
        CreateLayout();
    }

    private void CreateLayout() {
        TextViewPersian PricePayableTextViewPaymentPackageActivity = findViewById(R.id.PricePayableTextViewPaymentPackageActivity);
        PricePayableCouponTextViewPaymentPackageActivity = findViewById(R.id.PricePayableCouponTextViewPaymentPackageActivity);
        CouponFrameLayoutPaymentPackageActivity = findViewById(R.id.CouponFrameLayoutPaymentPackageActivity);
        CouponRelativeLayoutPaymentPackageActivity = findViewById(R.id.CouponRelativeLayoutPaymentPackageActivity);
//        TextViewPersian BusinessNameTextViewPaymentPackageActivity = findViewById(R.id.BusinessNameTextViewPaymentPackageActivity);
        TextViewPersian PackageNameTextViewPaymentPackageActivity = findViewById(R.id.PackageNameTextViewPaymentPackageActivity);
        ButtonPersianView SubmitPaymentPackageButtonPaymentPackageActivity = findViewById(R.id.SubmitPaymentPackageButtonPaymentPackageActivity);
        final ButtonPersianView SubmitCouponButtonUserPaymentPackageActivity = findViewById(R.id.SubmitCouponButtonUserPaymentPackageActivity);
        SubmitCouponEditTextPaymentPackageActivity = findViewById(R.id.SubmitCouponEditTextPaymentPackageActivity);
        final RadioButton bankSelectedRadioButtonPaymentPackageActivity = findViewById(R.id.BankSelectedRadioButtonPaymentPackageActivity);


        CouponRelativeLayoutPaymentPackageActivity.setVisibility(View.GONE);
        CouponFrameLayoutPaymentPackageActivity.setVisibility(View.GONE);

        PackageNameTextViewPaymentPackageActivity.setText(PackageName);
        PricePayableTextViewPaymentPackageActivity.setText(Utility.GetIntegerNumberWithComma(PricePayable));

        SubmitCouponButtonUserPaymentPackageActivity.setClickable(false);
        SubmitCouponButtonUserPaymentPackageActivity.setEnabled(false);

        SubmitCouponEditTextPaymentPackageActivity.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    SubmitCouponButtonUserPaymentPackageActivity.setClickable(true);
                    SubmitCouponButtonUserPaymentPackageActivity.setEnabled(true);
                } else {
                    SubmitCouponButtonUserPaymentPackageActivity.setClickable(false);
                    SubmitCouponButtonUserPaymentPackageActivity.setEnabled(false);
                }
            }
        });

        SubmitPaymentPackageButtonPaymentPackageActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (bankSelectedRadioButtonPaymentPackageActivity.isChecked())
                    PaymentPackage();
                else
                    ShowToast(getResources().getString(R.string.please_select_bank), Toast.LENGTH_LONG, MessageType.Warning);

            }
        });

        SubmitCouponButtonUserPaymentPackageActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckCoupon();
            }
        });
    }

    /**
     * در صورتی که در ارتباط با اینترنت مشکلی بوجود آمده و کاربر دکمه تلاش مجدد را فشار داده است
     */
    private void RetryButtonClick() {
        switch (RetryType) {
            //بررسی کوپن
            case 1:
                CheckCoupon();
                break;
            //پرداخت
            case 2:
                PaymentPackage();
                break;
        }
    }

    private void CheckCoupon() {

        ShowLoadingProgressBar();
        CouponService Service = new CouponService(this);
        RetryType = 1;
        Service.Get(SubmitCouponEditTextPaymentPackageActivity.getText().toString());

    }

    private void PaymentPackage() {

        ShowLoadingProgressBar();
        PackageService packageService = new PackageService(this);
        RetryType = 2;
        packageService.Add(MadeViewModel());
    }

    private PurchasePackageViewModel MadeViewModel() {

        PurchasePackageViewModel ViewModel = new PurchasePackageViewModel();
        try {
            if (IsValidCoupon)
                ViewModel.setCouponCode(CouponCode);
            else
                ViewModel.setCouponCode(null);
            ViewModel.setPackageId(PackageId);

        } catch (Exception ignored) {
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
        HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.CouponGet) {
                Feedback<UserCouponViewModel> FeedBack = (Feedback<UserCouponViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    final UserCouponViewModel ViewModel = FeedBack.getValue();

                    if (ViewModel.isUsed()) {

                        IsValidCoupon = false;
                        CouponCode = null;

                        CouponRelativeLayoutPaymentPackageActivity.setVisibility(View.GONE);
                        CouponFrameLayoutPaymentPackageActivity.setVisibility(View.GONE);

                        TotalPaymentPrice = PricePayable;
                    } else {

                        IsValidCoupon = true;
                        CouponRelativeLayoutPaymentPackageActivity.setVisibility(View.VISIBLE);
                        CouponFrameLayoutPaymentPackageActivity.setVisibility(View.VISIBLE);

                        CouponViewModel couponViewModel = ViewModel.getCoupon();
                        CouponCode = couponViewModel.getCode();

                        if (couponViewModel.isPercent()) {
                            double Percent = couponViewModel.getValue();
                            TotalPaymentPrice = PricePayable - (int) (PricePayable * Percent);
                        } else {
                            TotalPaymentPrice = PricePayable - (int) couponViewModel.getValue();
                        }

                        if (TotalPaymentPrice < 0)
                            TotalPaymentPrice = 0;

                        PricePayableCouponTextViewPaymentPackageActivity.setText(Utility.GetIntegerNumberWithComma(TotalPaymentPrice));
                    }

                } else {
                    IsValidCoupon = false;
                    CouponCode = null;
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {

                        TotalPaymentPrice = PricePayable;
                        CouponRelativeLayoutPaymentPackageActivity.setVisibility(View.GONE);
                        CouponFrameLayoutPaymentPackageActivity.setVisibility(View.GONE);

                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }

            } else if (ServiceMethod == ServiceMethodType.PaymentPackage) {
                Feedback<OutputPackageTransactionViewModel> FeedBack = (Feedback<OutputPackageTransactionViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.RegisteredSuccessful.getId()) {

                    final OutputPackageTransactionViewModel ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {

                        if (ViewModel.isActive()) {
                            ShowToast(getResources().getString(R.string.submit_package_successful), Toast.LENGTH_LONG, MessageType.Warning);
                        } else {
                            String url = "http://asanpardakhtpg.zeytoonfood.com/startpayment.aspx?type=1&id="+ ViewModel.getId();
                            Uri uri = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }

                        SendDataToParentActivity(ViewModel);
                        onBackPressed();
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

    /**
     * دریافت ویومدل پوستر خریداری شده و ارسال آن به اکتیویتی پروفایل کاربر جهت نمایش در لیست پوسترهای فعال
     * @param ViewModel اطلاعات پوستر
     */
    private void SendDataToParentActivity(OutputPackageTransactionViewModel ViewModel) {
        HashMap<String, Object> Output = new HashMap<>();
        Output.put("IsAdd", true);
        Output.put("OutputPackageTransactionViewModel", ViewModel);
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
        super.onBackPressed();
    }
}
