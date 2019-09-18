package ir.rayas.app.citywareclient.View.MasterChildren;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.PackageRepository;
import ir.rayas.app.citywareclient.Service.Coupon.CouponService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Package.PackageService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.EditTextPersian;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Base.IButtonBackToolbarListener;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Coupon.CouponViewModel;
import ir.rayas.app.citywareclient.ViewModel.Coupon.UserCouponViewModel;
import ir.rayas.app.citywareclient.ViewModel.Package.OutputPackageTransactionViewModel;
import ir.rayas.app.citywareclient.ViewModel.Package.PurchasePackageViewModel;
import ir.rayas.app.citywareclient.ViewModel.Payment.PackagePaymentViewModel;


public class PaymentPackageActivity extends BaseActivity implements IResponseService, IButtonBackToolbarListener {

    private TextViewPersian PricePayableCouponTextViewPaymentPackageActivity = null;
    private EditTextPersian SubmitCouponEditTextPaymentPackageActivity = null;
    private FrameLayout CouponFrameLayoutPaymentPackageActivity = null;
    private RelativeLayout CouponRelativeLayoutPaymentPackageActivity = null;
    private TextViewPersian PackageNameTextViewPaymentPackageActivity = null;
    private TextViewPersian PricePayableTextViewPaymentPackageActivity = null;

    private int PackageId = 0;
    private int Id = 0;
    private String PackageName = "";
    private Integer PricePayable = 0;

    private int RetryType = 0;
    private int TotalPaymentPrice = 0;
    private Integer PriceCoupon = 0;
    private boolean IsValidCoupon = false;
    private String CouponCode = "";

    private boolean IsPay = false;

    private PackageRepository packageRepository;

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

        packageRepository = new PackageRepository();

        this.setButtonBackToolbarListener(this);

        PackageName = getIntent().getExtras().getString("PackageName");
        PricePayable = getIntent().getExtras().getInt("PricePayable");
        PackageId = getIntent().getExtras().getInt("PackageId");


        if (packageRepository.getPackagePayment() != null) {

            PackagePaymentViewModel ViewModel = packageRepository.getPackagePayment();
            PackageName = ViewModel.getPackageName();
            PricePayable = ViewModel.getPricePayable();
            PackageId = ViewModel.getPackageId();
            IsPay = ViewModel.isPay();
            Id = ViewModel.getId();
            PriceCoupon = ViewModel.getPriceCoupon();
            CouponCode = ViewModel.getCouponCode();

            packageRepository.ClearPackagePayment();
            if (IsPay)
                LoadDataValidPackage();
            IsPay = false;
        }

        if (PricePayable != null) {
            if (PricePayable != 0) {
                TotalPaymentPrice = PricePayable;
            } else {
                ShowNotValidRequestDialog();
            }
        } else {
            ShowNotValidRequestDialog();
        }

        //ایجاد طرح بندی صفحه
        CreateLayout();
    }

    private void CreateLayout() {
        PricePayableTextViewPaymentPackageActivity = findViewById(R.id.PricePayableTextViewPaymentPackageActivity);
        PricePayableCouponTextViewPaymentPackageActivity = findViewById(R.id.PricePayableCouponTextViewPaymentPackageActivity);
        CouponFrameLayoutPaymentPackageActivity = findViewById(R.id.CouponFrameLayoutPaymentPackageActivity);
        CouponRelativeLayoutPaymentPackageActivity = findViewById(R.id.CouponRelativeLayoutPaymentPackageActivity);
        PackageNameTextViewPaymentPackageActivity = findViewById(R.id.PackageNameTextViewPaymentPackageActivity);
        ButtonPersianView SubmitPaymentPackageButtonPaymentPackageActivity = findViewById(R.id.SubmitPaymentPackageButtonPaymentPackageActivity);
        final ButtonPersianView SubmitCouponButtonUserPaymentPackageActivity = findViewById(R.id.SubmitCouponButtonUserPaymentPackageActivity);
        SubmitCouponEditTextPaymentPackageActivity = findViewById(R.id.SubmitCouponEditTextPaymentPackageActivity);
        final RadioButton bankSelectedRadioButtonPaymentPackageActivity = findViewById(R.id.BankSelectedRadioButtonPaymentPackageActivity);

        PackageNameTextViewPaymentPackageActivity.setText(PackageName);
        PricePayableTextViewPaymentPackageActivity.setText(Utility.GetIntegerNumberWithComma(PricePayable));

        if (CouponCode != null && !CouponCode.equals(""))
            SubmitCouponEditTextPaymentPackageActivity.setText(CouponCode);

        if (PriceCoupon != null && PriceCoupon != 0) {
            CouponRelativeLayoutPaymentPackageActivity.setVisibility(View.VISIBLE);
            CouponFrameLayoutPaymentPackageActivity.setVisibility(View.VISIBLE);

            PricePayableCouponTextViewPaymentPackageActivity.setText(Utility.GetIntegerNumberWithComma(PriceCoupon));
        } else {
            CouponRelativeLayoutPaymentPackageActivity.setVisibility(View.GONE);
            CouponFrameLayoutPaymentPackageActivity.setVisibility(View.GONE);
        }

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
                HideKeyboard(SubmitCouponButtonUserPaymentPackageActivity);
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

        if (Id == 0) {
            PackageService packageService = new PackageService(this);
            RetryType = 2;
            packageService.Add(MadeViewModel());
        } else {
            IsPay = true;

            if (packageRepository.getPackagePayment() != null)
                packageRepository.ClearPackagePayment();

            PackagePaymentViewModel packagePaymentViewModel = new PackagePaymentViewModel();
            packagePaymentViewModel.setPackageId(PackageId);
            packagePaymentViewModel.setId(Id);
            packagePaymentViewModel.setPackageName(PackageName);
            packagePaymentViewModel.setPay(IsPay);
            packagePaymentViewModel.setPricePayable(PricePayable);
            packagePaymentViewModel.setCouponCode(SubmitCouponEditTextPaymentPackageActivity.getText().toString());
            packagePaymentViewModel.setPriceCoupon(PriceCoupon);

            packageRepository.setPackagePayment(packagePaymentViewModel);


            String url = "http://asanpardakhtpg.zeytoonfood.com/startpayment.aspx?type=1&id=" + Id;
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
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

    public void LoadDataValidPackage() {
        ShowLoadingProgressBar();
        PackageService packageService = new PackageService(this);
        packageService.GetAllOpen(1);

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
                            TotalPaymentPrice = PricePayable - (int) ((PricePayable * Percent) / 100);
                        } else {
                            TotalPaymentPrice = PricePayable - (int) couponViewModel.getValue();
                        }

                        if (TotalPaymentPrice < 0)
                            TotalPaymentPrice = 0;

                        PriceCoupon = TotalPaymentPrice;

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
                            ShowMessageBuyDialog();

                        } else {
                            if (TotalPaymentPrice > DefaultConstant.MaxPayment || TotalPaymentPrice < DefaultConstant.MinPayment) {
                                ShowPaymentPackageDialog();
                            } else {

                                IsPay = true;

                                if (packageRepository.getPackagePayment() != null)
                                    packageRepository.ClearPackagePayment();

                                PackagePaymentViewModel packagePaymentViewModel = new PackagePaymentViewModel();
                                packagePaymentViewModel.setPackageId(PackageId);
                                packagePaymentViewModel.setId(ViewModel.getId());
                                packagePaymentViewModel.setPackageName(PackageName);
                                packagePaymentViewModel.setPay(IsPay);
                                packagePaymentViewModel.setPricePayable(PricePayable);
                                packagePaymentViewModel.setCouponCode(SubmitCouponEditTextPaymentPackageActivity.getText().toString());
                                packagePaymentViewModel.setPriceCoupon(PriceCoupon);

                                packageRepository.setPackagePayment(packagePaymentViewModel);


                                String url = "http://asanpardakhtpg.zeytoonfood.com/startpayment.aspx?type=1&id=" + ViewModel.getId();
                                Uri uri = Uri.parse(url);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        }

                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }

            } else if (ServiceMethod == ServiceMethodType.UserPackageOpenGetAll) {
                Feedback<List<OutputPackageTransactionViewModel>> FeedBack = (Feedback<List<OutputPackageTransactionViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    final List<OutputPackageTransactionViewModel> ViewModelList = FeedBack.getValue();

                    boolean IsHavePackageId = false;

                    for (int i = 0; i < ViewModelList.size(); i++) {
                        if (ViewModelList.get(i).getId() == Id) {
                            if (ViewModelList.get(i).isActive()) {
                                IsHavePackageId = true;
                            }
                            break;
                        }
                    }
                    if (IsHavePackageId) {
                        ShowMessageBuyDialog();
                    } else {
                        ShowToast(getResources().getString(R.string.submit_package_unsuccessful), Toast.LENGTH_LONG, MessageType.Warning);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(getResources().getString(R.string.submit_package_unsuccessful), Toast.LENGTH_LONG, MessageType.Warning);
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
//
//    /**
//     * دریافت ویومدل پوستر خریداری شده و ارسال آن به اکتیویتی پروفایل کاربر جهت نمایش در لیست پوسترهای فعال
//     *
//     * @param ViewModel اطلاعات پوستر
//     */
//    private void SendDataToParentActivity(OutputPackageTransactionViewModel ViewModel) {
//        HashMap<String, Object> Output = new HashMap<>();
//        Output.put("IsAdd", true);
//        Output.put("OutputPackageTransactionViewModel", ViewModel);
//        ActivityResultPassing.Push(new ActivityResult(ActivityIdList.PACKAGE_ACTIVITY, ActivityIdList.PAYMENT_PACKAGE_ACTIVITY, Output));
//    }

    private void HideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    private void ShowPaymentPackageDialog() {

        final Dialog ShowPaymentPackageDialog = new Dialog(PaymentPackageActivity.this);
        ShowPaymentPackageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ShowPaymentPackageDialog.setCanceledOnTouchOutside(false);
        ShowPaymentPackageDialog.setContentView(R.layout.dialog_payment_package);

        TextViewPersian MessageTextView = ShowPaymentPackageDialog.findViewById(R.id.MessageTextView);
        TextViewPersian HeaderColorDialog = ShowPaymentPackageDialog.findViewById(R.id.HeaderColorDialog);
        HeaderColorDialog.getLayoutParams().width = LayoutUtility.GetWidthAccordingToScreen(PaymentPackageActivity.this, 1);
        MessageTextView.setText(getResources().getString(R.string.package_submit_due_to_bank_ports_limitations_payment_is_not_possible));
        ButtonPersianView DialogOkButton = ShowPaymentPackageDialog.findViewById(R.id.DialogOkButton);
        DialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //   SendDataToParentActivity(ViewModel);
//        //این قسمت به دلیل SingleInstance بودن Parent بایستی مطمئن شوبم که اکتیویتی Parent بعد از اتمام این اکتیویتی دوباره صدا  زده می شود
//        //در حالت خروج از برنامه و ورود دوباره این اکتیوتی ممکن است Parent خود را گم کند
                FinishCurrentActivity();
                ShowPaymentPackageDialog.dismiss();
            }
        });

        ShowPaymentPackageDialog.show();
    }

    private void ShowMessageBuyDialog() {

        final Dialog OkBuyPackageDialog = new Dialog(PaymentPackageActivity.this);
        OkBuyPackageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        OkBuyPackageDialog.setCanceledOnTouchOutside(false);
        OkBuyPackageDialog.setContentView(R.layout.dialog_ok_buy_prize);

        ButtonPersianView DialogOkButton = OkBuyPackageDialog.findViewById(R.id.DialogOkButton);
        TextViewPersian HeaderColorDialog = OkBuyPackageDialog.findViewById(R.id.HeaderColorDialog);
        HeaderColorDialog.getLayoutParams().width = LayoutUtility.GetWidthAccordingToScreen(PaymentPackageActivity.this, 1);
        TextViewPersian DialogMessageTextView = OkBuyPackageDialog.findViewById(R.id.DialogMessageTextView);

        DialogMessageTextView.setText(getResources().getString(R.string.message_show_get_package));
        DefaultConstant.RefreshUserCredit = 1;

        DialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkBuyPackageDialog.dismiss();

//                SendDataToParentActivity(ViewModel);
                onBackPressed();
            }
        });

        OkBuyPackageDialog.show();
    }

    private void ShowNotValidRequestDialog() {

        final Dialog ShowNotValidRequestDialog = new Dialog(PaymentPackageActivity.this);
        ShowNotValidRequestDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ShowNotValidRequestDialog.setCanceledOnTouchOutside(false);
        ShowNotValidRequestDialog.setContentView(R.layout.dialog_payment_package);

        TextViewPersian MessageTextView = ShowNotValidRequestDialog.findViewById(R.id.MessageTextView);
        TextViewPersian HeaderColorDialog = ShowNotValidRequestDialog.findViewById(R.id.HeaderColorDialog);
        HeaderColorDialog.getLayoutParams().width = LayoutUtility.GetWidthAccordingToScreen(PaymentPackageActivity.this, 1);
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

    private void onBack() {
        super.onBackPressed();

        if (packageRepository.getPackagePayment() != null) {
            packageRepository.ClearPackagePayment();
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
        onBack();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (IsPay) {

            if (packageRepository.getPackagePayment() != null) {

                PackagePaymentViewModel ViewModel = packageRepository.getPackagePayment();
                PackageName = ViewModel.getPackageName();
                PricePayable = ViewModel.getPricePayable();
                PackageId = ViewModel.getPackageId();
                IsPay = ViewModel.isPay();
                Id = ViewModel.getId();
                PriceCoupon = ViewModel.getPriceCoupon();
                CouponCode = ViewModel.getCouponCode();

                packageRepository.ClearPackagePayment();

                PackageNameTextViewPaymentPackageActivity.setText(PackageName);
                PricePayableTextViewPaymentPackageActivity.setText(Utility.GetIntegerNumberWithComma(PricePayable));

                if (CouponCode != null && !CouponCode.equals(""))
                    SubmitCouponEditTextPaymentPackageActivity.setText(CouponCode);


                if (PriceCoupon != null && PriceCoupon != 0) {
                    CouponRelativeLayoutPaymentPackageActivity.setVisibility(View.VISIBLE);
                    CouponFrameLayoutPaymentPackageActivity.setVisibility(View.VISIBLE);

                    PricePayableCouponTextViewPaymentPackageActivity.setText(Utility.GetIntegerNumberWithComma(PriceCoupon));
                } else {
                    CouponRelativeLayoutPaymentPackageActivity.setVisibility(View.GONE);
                    CouponFrameLayoutPaymentPackageActivity.setVisibility(View.GONE);
                }

            }

            if (PricePayable != null) {
                if (PricePayable != 0) {
                    LoadDataValidPackage();
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
    public void ClickOnButtonBackToolbar() {
        onBack();
    }
}
