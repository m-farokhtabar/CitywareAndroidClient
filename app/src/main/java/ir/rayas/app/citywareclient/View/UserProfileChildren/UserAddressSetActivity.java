package ir.rayas.app.citywareclient.View.UserProfileChildren;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.HashMap;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.User.UserAddressService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResultPassing;
import ir.rayas.app.citywareclient.Share.Helper.Gps;
import ir.rayas.app.citywareclient.Share.Helper.IResponseTurnOnGpsDialog;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.EditTextPersian;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.View.Share.MapActivity;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.UserAddressViewModel;

public class UserAddressSetActivity extends BaseActivity implements IResponseService, IResponseTurnOnGpsDialog {

    private EditTextPersian PostalAddressEditTextUserAddressSetActivity = null;
    private EditTextPersian PostalCodeEditTextUserAddressSetActivity = null;
    private double Latitude = 0.0;
    private double Longitude = 0.0;
    private String IsSet = "";
    private int AddressId;
    private int RetryType = 0;
    private Gps CurrentGps = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_address_set);
        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.USER_ADDRESS_SET_ACTIVITY);

        //کلاس کنترل و مدیریت GPS
        CurrentGps = new Gps();

        //آماده سازی قسمت لودینگ و پنجره خطا و سیستم پیغام در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonClick();
            }
        }, R.string.address);

        //مشخص شدن صفحه ویرایش آدرس یا آدرس جدید
        IsSet = getIntent().getExtras().getString("SetAddress");
        NewAddressOrEditAddress();

        //ایجاد طرح بندی صفحه
        CreateLayout();

    }


    /**
     * در صورتی که در ارتباط با اینترنت مشکلی بوجود آمده و کاربر دکمه تلاش مجدد را فشار داده است
     */
    private void RetryButtonClick() {
        switch (RetryType) {
            //ثبت اطلاعات
            case 1:
                HideLoading();
                break;
            //دریافت اطلاعات
            case 2:
                NewAddressOrEditAddress();
                break;
        }
    }

    private void CreateLayout() {
        PostalAddressEditTextUserAddressSetActivity = findViewById(R.id.PostalAddressEditTextUserAddressSetActivity);
        PostalCodeEditTextUserAddressSetActivity = findViewById(R.id.PostalCodeEditTextUserAddressSetActivity);
        TextViewPersian MapIconTextViewUserAddressSetActivity = findViewById(R.id.MapIconTextViewUserAddressSetActivity);
        ButtonPersianView SaveAddressButtonUserAddressSetActivity = findViewById(R.id.SaveAddressButtonUserAddressSetActivity);
        LinearLayout ShowMapLinearLayoutUserAddressSetActivity = findViewById(R.id.ShowMapLinearLayoutUserAddressSetActivity);

        MapIconTextViewUserAddressSetActivity.setTypeface(Font.MasterIcon);
        MapIconTextViewUserAddressSetActivity.setText("\uf041");

        ShowMapLinearLayoutUserAddressSetActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnShowMapClick();

            }
        });

        SaveAddressButtonUserAddressSetActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnSaveAddressButtonClick(view);
            }
        });
    }

    private void NewAddressOrEditAddress() {
        if (IsSet.equals("Edit")) {
            AddressId = getIntent().getExtras().getInt("AddressId");
            ShowLoadingProgressBar();
            UserAddressService userAddressService = new UserAddressService(this);
            //دریافت اطلاعات
            RetryType = 2;
            userAddressService.Get(AddressId);
        }
    }

    /**
     * دکمه ثبت یا ویرایش یک آدرس
     *
     * @param view
     */
    private void OnSaveAddressButtonClick(View view) {
        if (Utility.ValidateView(PostalAddressEditTextUserAddressSetActivity, PostalCodeEditTextUserAddressSetActivity)) {
            UserAddressService Service = new UserAddressService(this);
            //ثبت اطلاعات
            RetryType = 1;
            if (IsSet.equals("Edit")) {
                Service.Set(MadeViewModel(), AddressId);
            } else {
                if (Latitude > 0 && Longitude > 0) {
                    Service.Add(MadeViewModel());
                }  else {
                    ShowToast(getResources().getString(R.string.at_first_please_touch_some_where_to_select), Toast.LENGTH_LONG, MessageType.Warning);
                }

            }
        }
    }

    private UserAddressViewModel MadeViewModel() {
        UserAddressViewModel ViewModel = new UserAddressViewModel();
        try {
            ViewModel.setCurrentAddress(PostalAddressEditTextUserAddressSetActivity.getText().toString());
            ViewModel.setPostalCode(PostalCodeEditTextUserAddressSetActivity.getText().toString());
            AccountRepository AccountRepository = new AccountRepository(this);
            AccountViewModel accountViewModel = AccountRepository.getAccount();
            ViewModel.setUserId(accountViewModel.getUser().getId());
            if (Latitude > 0 && Longitude > 0) {
                ViewModel.setLatitude(Latitude);
                ViewModel.setLongitude(Longitude);
            }

        } catch (Exception Ex) {
        }
        return ViewModel;
    }

    /**
     * باز کردن اکتیویتی نقشه
     */
    private void GoToMapActivity() {
        Intent MapIntent = NewIntent(MapActivity.class);
        MapIntent.putExtra("Latitude", Latitude);
        MapIntent.putExtra("Longitude", Longitude);
        startActivity(MapIntent);
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.UserAddAddress) {
                Feedback<UserAddressViewModel> FeedBack = (Feedback<UserAddressViewModel>) Data;
                if (FeedBack.getStatus() == FeedbackType.RegisteredSuccessful.getId()) {
                    UserAddressViewModel ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        SendDataToParentActivity(true, ViewModel);
                        //این قسمت به دلیل SingleInstance بودن Parent بایستی مطمئن شوبم که اکتیویتی Parent بعد از اتمام این اکتیویتی دوباره صدا  زده می شود
                        //در حالت خروج از برنامه و ورود دوباره این اکتیوتی ممکن است Parent خود را گم کند
                        FinishCurrentActivity();
                    } else {
                        ShowToast(FeedbackType.InvalidDataFormat.getMessage().replace("{0}", ""), Toast.LENGTH_LONG, MessageType.Warning);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.UserGetAddress) {
                Feedback<UserAddressViewModel> FeedBack = (Feedback<UserAddressViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    UserAddressViewModel ViewModel = FeedBack.getValue();

                    if (ViewModel != null) {
                        PostalAddressEditTextUserAddressSetActivity.setText(ViewModel.getCurrentAddress());
                        PostalCodeEditTextUserAddressSetActivity.setText(ViewModel.getPostalCode());
                        Latitude = ViewModel.getLatitude();
                        Longitude = ViewModel.getLongitude();
                    }

                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }

            } else if (ServiceMethod == ServiceMethodType.UserSetAddress) {
                Feedback<UserAddressViewModel> FeedBack = (Feedback<UserAddressViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.UpdatedSuccessful.getId()) {
                    UserAddressViewModel ViewModel = FeedBack.getValue();

                    if (ViewModel != null) {
                        //ارسال اجازه لود شدن برای اکتیویتی پروفایل کاربر
                        PostalAddressEditTextUserAddressSetActivity.setText(ViewModel.getCurrentAddress());
                        PostalCodeEditTextUserAddressSetActivity.setText(ViewModel.getPostalCode());

                        SendDataToParentActivity(false, ViewModel);
                        //این قسمت به دلیل SingleInstance بودن Parent بایستی مطمئن شوبم که اکتیویتی Parent بعد از اتمام این اکتیویتی دوباره صدا  زده می شود
                        //در حالت خروج از برنامه و ورود دوباره این اکتیوتی ممکن است Parent خود را گم کند
                        FinishCurrentActivity();
                    } else {
                        ShowToast(FeedbackType.InvalidDataFormat.getMessage().replace("{0}", ""), Toast.LENGTH_LONG, MessageType.Warning);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            }
        } catch (Exception Ex) {
            ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
    }

    private void SendDataToParentActivity(boolean IsAdd, UserAddressViewModel ViewModel) {
        HashMap<String, Object> Output = new HashMap<>();
        Output.put("IsAdd", IsAdd);
        Output.put("AddressViewModel", ViewModel);
        ActivityResultPassing.Push(new ActivityResult(getParentActivity(), getCurrentActivityId(), Output));
    }

    /**
     * ثبت تنظیمات در صورت اکی بودن وضعیت GPS
     */
    private void OnShowMapClick() {
        if (CurrentGps.IsMapAlreadyToUse(this, this, R.string.turn_on_location_to_get_package_faster)) {
            GoToMapActivity();
        }
    }

    @Override
    public void OnDismissTurnOnGpsDialog(boolean IsClickYes) {
        if (!IsClickYes) {
            ShowToast(getResources().getString(R.string.for_register_address_turn_on_gps), Toast.LENGTH_LONG, MessageType.Warning);
        }
    }

    /**
     * زمانی که پنجره دسترسی به Gps می آید و کاربر باید انتخاب کند که اجازه می دهد ا خیر
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (CurrentGps.IsPermissionEnabled()) {
            if (!CurrentGps.IsEnabled())
                CurrentGps.ShowTurnOnGpsDialog(this, this, R.string.turn_on_location_to_get_package_faster);
            else
                GoToMapActivity();
        } else {
            ShowToast(getResources().getString(R.string.app_permission_denied), Toast.LENGTH_LONG, MessageType.Warning);
        }
    }

    @Override
    protected void onGetResult(ActivityResult Result) {
        if (Result.getFromActivityId() == getCurrentActivityId()) {
            switch (Result.getToActivityId()) {
                case ActivityIdList.MAP_ACTIVITY:
                    Longitude = (double) Result.getData().get("Longitude");
                    Latitude = (double) Result.getData().get("Latitude");
                    break;
            }
        }
        super.onGetResult(Result);
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
