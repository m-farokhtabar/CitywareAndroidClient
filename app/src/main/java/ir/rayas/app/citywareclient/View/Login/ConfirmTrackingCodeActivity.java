package ir.rayas.app.citywareclient.View.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Timer;
import java.util.TimerTask;

import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.Repository.NotificationRepository;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.User.AccountService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.EditTextPersian;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.View.Master.MainActivity;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;
import ir.rayas.app.citywareclient.R;


public class ConfirmTrackingCodeActivity extends BaseActivity implements IResponseService {

    private EditTextPersian TrackingCodeEditText = null;
    private ButtonPersianView ReSendMessageButton = null;
    private TextViewPersian TimerCounterTextView = null;
    private long CellPhone = 0;
    private int Counter = 0;


    final public static String FCM = "firebase_service";
    final public static String FCM_TOPIC_CUSTOMERS = "customers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_tracking_code);
        //دریافت شماره تلفن از فرم قبلی یا ثبت نام یا ورود
        CellPhone = getIntent().getLongExtra("CellPhone", 0);

        //آماده سازی قسمت لودینگ و پنجره خطا و سیستم پیغام در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                ShowActivity();
            }
        }, 0);
        //نمایش نوار ابزار
        InitToolbarWithOutButton();
        //ایجاد طرح بندی صفحه
        CreateLayout();
    }

    /**
     * نمایش دوباره همین اکتیویتی
     */
    private void ShowActivity() {
        HideLoading();
    }

    private void CreateLayout() {

        TrackingCodeEditText = findViewById(R.id.TrackingCodeEditText);
        TimerCounterTextView = findViewById(R.id.TimerCounterTextView);


        ButtonPersianView confirmButton = findViewById(R.id.ConfirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnConfirmButtonClick();
            }
        });

        ReSendMessageButton = findViewById(R.id.ReSendMessageButton);
        ReSendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnReSendMessageButtonClick();
            }
        });
        DisableReSendAndStartTimer();
    }

    /**
     * غیر فعال کردن دکمه و شروع تایمر
     */
    private void DisableReSendAndStartTimer() {
        ReSendMessageButton.setEnabled(false);

        int AllSecond = DefaultConstant.DefaultDelayForEnabledReSendMessage / 1000;
        int Minute = AllSecond / 60;
        int Second = AllSecond - (Minute * 60);
        String TimerText = String.format("%02d", Minute) + ":" + String.format("%02d", Second);
        TimerCounterTextView.setText(TimerText);
        TimerCounterTextView.setVisibility(View.VISIBLE);

        Counter = 0;
        final Timer CurrentTimer = new Timer();
        CurrentTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (Counter >= DefaultConstant.DefaultDelayForEnabledReSendMessage) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ReSendMessageButton.setEnabled(true);
                            TimerCounterTextView.setVisibility(View.GONE);
                        }
                    });
                    Counter = 0;
                    CurrentTimer.cancel();
                    CurrentTimer.purge();
                } else {
                    Counter = Counter + 1000;
                    int AllSecond = (DefaultConstant.DefaultDelayForEnabledReSendMessage / 1000) - (Counter / 1000);
                    int Minute = AllSecond / 60;
                    int Second = AllSecond - (Minute * 60);
                    final String TimerText = String.format("%02d", Minute) + ":" + String.format("%02d", Second);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TimerCounterTextView.setText(TimerText);
                        }
                    });

                }
            }
        }, 1000, 1000);
    }

    private void OnConfirmButtonClick() {
        if (Utility.ValidateView(TrackingCodeEditText)) {
            ShowLoadingProgressBar();
            AccountService Service = new AccountService(this);
            long TrackingCode = Long.valueOf(TrackingCodeEditText.getText().toString());
            Service.ConfirmAndLogin(CellPhone, TrackingCode);
        }
    }

    private void OnReSendMessageButtonClick() {
        ShowLoadingProgressBar();
        AccountService Service = new AccountService(this);
        Service.RequestTrackingCode(CellPhone);
    }

    private void ReadTrackingCodeFromSms() {
        //Todo: در این قسمت باید پیام به طور اوتوماتیک خوانده شود و در textbox نمایش داده شود
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        try {
            //دریافت کد رهگیری مجدد
            if (ServiceMethod == ServiceMethodType.RequestTrackingCode) {

                Feedback<Boolean> FeedBack = (Feedback<Boolean>) Data;
                if (FeedBack.getStatus() == FeedbackType.RegisteredSuccessful.getId()) {
                    Boolean ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        ShowToast(getResources().getString(R.string.re_send_request_successful), Toast.LENGTH_LONG, MessageType.Info);
                        //روشن کردن تایمر و غیر عال تمودن دکمه ارسال مجدد
                        DisableReSendAndStartTimer();
                    } else {
                        ShowToast(FeedbackType.InvalidDataFormat.getMessage().replace("{0}", ""), Toast.LENGTH_LONG, MessageType.Warning);
                    }
                    HideLoading();
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        HideLoading();
                        //TrackingCodeEditText.requestFocus();
                        //Utility.ShowKeyboard(TrackingCodeEditText);
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            }
            //تایید کد رهگیری و ثبت آن در کش برنامه
            if (ServiceMethod == ServiceMethodType.ConfirmAndLogin) {
                Feedback<AccountViewModel> FeedBack = (Feedback<AccountViewModel>) Data;
                if (FeedBack.getStatus() == FeedbackType.UpdatedSuccessful.getId()) {
                    AccountViewModel ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {


                            // با این کد همه کاربران عضو تاپیک customers میشن و با انتخاب این تاپیک نوتیفیکیشن  برای همه کاربران ارسال میشود.
                            FirebaseMessaging.getInstance().subscribeToTopic(FCM_TOPIC_CUSTOMERS);

                        AccountRepository Repository = new AccountRepository(this);
                        AccountViewModel Account = Repository.getAccount();
                        if (Account != null) {
                            if (ViewModel.getUser().getId() != Repository.getAccount().getUser().getId()) {
                                NotificationRepository NotificationRepository = new NotificationRepository();
                                NotificationRepository.ClearAllNotification();
                            }
                        }
                        Repository.setAccount(ViewModel);

                        if (ViewModel.getUserSetting() == null) {
                            //نمایش فرم تنظیمات جستجو
                            Intent SearchInAppIntent = new Intent(this, HowToSearchInAppGpsOrRegionActivity.class);
                            startActivity(SearchInAppIntent);
                        } else {
                            Intent ActivityIntent = new Intent(this, MainActivity.class);
                            ActivityIntent.putExtra("FromActivityId", ActivityIdList.APP_NEEDS_USER_BACK_TO_TERMINATE);
                            startActivity(ActivityIntent);
                        }
                        finish();
                    } else {
                        ShowToast(FeedbackType.InvalidDataFormat.getMessage().replace("{0}", ""), Toast.LENGTH_LONG, MessageType.Warning);
                        HideLoading();
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        HideLoading();
                        TrackingCodeEditText.requestFocus();
                        Utility.ShowKeyboard(TrackingCodeEditText);
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            }
        } catch (Exception Ex) {
            HideLoading();
            ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }

    }

    private boolean getUserCustomer() {
        return getSharedPreferences(FCM, 0).getBoolean(FCM_TOPIC_CUSTOMERS, false);
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
}
