package ir.rayas.app.citywareclient.View.UserProfileChildren;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.StringTokenizer;

import ir.rayas.app.citywareclient.Adapter.Spinner.DayOfWeekSpinnerAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Business.BusinessOpenTimeService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResultPassing;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessOpenTimeViewModel;

public class BusinessOpenTimeSetActivity extends BaseActivity implements IResponseService, AdapterView.OnItemSelectedListener {
    private Spinner DayOfWeekSpinnerBusinessOpenTimeSetActivity = null;
    private TextViewPersian FromHoursTextViewBusinessOpenTimeSetActivity = null;
    private TextViewPersian ToHoursTextViewBusinessOpenTimeSetActivity = null;

    private int RetryType = 0;
    private String IsSet = "";
    private int BusinessOpenTimeId = 0;
    private Integer TypeDayToWeek;

    private String[] DayOfWeek;

    private int Min = 0;
    private int Hours = 0;
    private int BusinessId;
    private BusinessOpenTimeViewModel OutputViewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_open_time_set);
        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.BUSINESS_OPEN_TIME_SET_ACTIVITY);

        //آماده سازی قسمت لودینگ و پنجره خطا و سیستم پیغام در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonClick();
            }
        }, R.string.working_hours);

        //مشخص شدن صفحه ویرایش آدرس یا آدرس جدید
        IsSet = getIntent().getExtras().getString("SetBusinessOpenTime");
        BusinessId = getIntent().getExtras().getInt("BusinessId");
        NewOpenTimeOrEditAddress();

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
                NewOpenTimeOrEditAddress();
                break;
        }
    }

    private void CreateLayout() {
        DayOfWeekSpinnerBusinessOpenTimeSetActivity = findViewById(R.id.DayOfWeekSpinnerBusinessOpenTimeSetActivity);
        FromHoursTextViewBusinessOpenTimeSetActivity = findViewById(R.id.FromHoursTextViewBusinessOpenTimeSetActivity);
        ToHoursTextViewBusinessOpenTimeSetActivity = findViewById(R.id.ToHoursTextViewBusinessOpenTimeSetActivity);
        ButtonPersianView SaveOpenTimeButtonBusinessOpenTimeSetActivity = findViewById(R.id.SaveOpenTimeButtonBusinessOpenTimeSetActivity);

        SaveOpenTimeButtonBusinessOpenTimeSetActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnSaveOpenTimeBusinessButtonClick(view);
            }
        });

        DayOfWeek = new String[]{getString(R.string.saturday), getString(R.string.monday), getString(R.string.sunday),
                getString(R.string.wednesday), getString(R.string.tuesday), getString(R.string.thursday), getString(R.string.friday)};


        SetDayOfWeekToSpinner();

        DayOfWeekSpinnerBusinessOpenTimeSetActivity.setOnItemSelectedListener(this);

        FromHoursTextViewBusinessOpenTimeSetActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenTime(FromHoursTextViewBusinessOpenTimeSetActivity);
            }
        });

        ToHoursTextViewBusinessOpenTimeSetActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenTime(ToHoursTextViewBusinessOpenTimeSetActivity);
            }
        });
    }

    private void NewOpenTimeOrEditAddress() {
        if (IsSet.equals("Edit")) {
            BusinessOpenTimeId = getIntent().getExtras().getInt("BusinessOpenTimeId");
            ShowLoadingProgressBar();
            BusinessOpenTimeService businessOpenTimeService = new BusinessOpenTimeService(this);
            //دریافت اطلاعات
            RetryType = 2;
            businessOpenTimeService.Get(BusinessOpenTimeId);
        }
    }

    /**
     * دکمه ثبت یا ویرایش یک آدرس
     *
     * @param view
     */
    private void OnSaveOpenTimeBusinessButtonClick(View view) {
        if (Utility.ValidateView(FromHoursTextViewBusinessOpenTimeSetActivity, ToHoursTextViewBusinessOpenTimeSetActivity, DayOfWeekSpinnerBusinessOpenTimeSetActivity)) {
            BusinessOpenTimeService Service = new BusinessOpenTimeService(this);
            //ثبت اطلاعات
            RetryType = 1;

            String FromHours = FromHoursTextViewBusinessOpenTimeSetActivity.getText().toString();
            String ToHours = ToHoursTextViewBusinessOpenTimeSetActivity.getText().toString();

            int ComTime = Utility.CompareTime(FromHours,ToHours);
//          * -2 خطا
//          * -1 زمان دومی بزرگتر
//          * 0 مساوی
//          * 1 زمان اولی بزرگتر
            if (ComTime == 1 || ComTime == -2) {
                ShowToast(getResources().getString(R.string.from_time_bigger_than_to_time), Toast.LENGTH_LONG, MessageType.Warning);
            } else {
                if (IsSet.equals("Edit")) {
                    Service.Set(MadeViewModel(), BusinessOpenTimeId);
                } else {
                    Service.Add(MadeViewModel());
                }
            }


        }
    }

    private BusinessOpenTimeViewModel MadeViewModel() {

        BusinessOpenTimeViewModel ViewModel = new BusinessOpenTimeViewModel();
        try {
            ViewModel.setDayOfWeek(TypeDayToWeek);
            ViewModel.setFrom(FromHoursTextViewBusinessOpenTimeSetActivity.getText().toString());
            ViewModel.setTo(ToHoursTextViewBusinessOpenTimeSetActivity.getText().toString());
            ViewModel.setBusinessId(BusinessId);
            ViewModel.setId(BusinessOpenTimeId);
            ViewModel.setCreate("");
            ViewModel.setModified("");
            ViewModel.setActive(true);


        } catch (Exception Ex) {
        }
        return ViewModel;


    }

    private void OpenTime(final TextViewPersian textViewPersian) {

        final Dialog OpenTime = new Dialog(BusinessOpenTimeSetActivity.this);
        OpenTime.requestWindowFeature(Window.FEATURE_NO_TITLE);
        OpenTime.setContentView(R.layout.layout_time_picker);

        LinearLayout CancelTimeLinearLayout = OpenTime.findViewById(R.id.CancelTimeLinearLayout);
        LinearLayout SaveTimeLinearLayout = OpenTime.findViewById(R.id.SaveTimeLinearLayout);
        final TextViewPersian ShowTimeTextView = OpenTime.findViewById(R.id.ShowTimeTextView);
        NumberPicker HoursNumberPicker = OpenTime.findViewById(R.id.HoursNumberPicker);
        NumberPicker MinNumberPicker = OpenTime.findViewById(R.id.MinNumberPicker);

        HoursNumberPicker.setMinValue(0);
        HoursNumberPicker.setMaxValue(23);
        MinNumberPicker.setMinValue(0);
        MinNumberPicker.setMaxValue(59);


        if (textViewPersian.getText().toString().contains(":")) {
            String Date = textViewPersian.getText().toString().trim();
            StringTokenizer FromDateTokens = new StringTokenizer(Date, ":");
            Hours = Integer.parseInt(FromDateTokens.nextToken());
            Min = Integer.parseInt(FromDateTokens.nextToken());

            HoursNumberPicker.setValue(Hours);
            MinNumberPicker.setValue(Min);
        } else {
            HoursNumberPicker.setValue(0);
            MinNumberPicker.setValue(0);
        }

        ShowTimeTextView.setText("ساعت " + Hours + ":" + Min);

        MinNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int newMin) {
                Min = newMin;
                ShowTimeTextView.setText("ساعت " + Hours + ":" + Min);

            }
        });

        HoursNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int newHours) {
                Hours = newHours;
                ShowTimeTextView.setText("ساعت " + Hours + ":" + Min);
            }
        });


        SaveTimeLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenTime.dismiss();

                textViewPersian.setText(Hours + ":" + Min);
                textViewPersian.setTextColor(getResources().getColor(R.color.FontBlackColor));

                Min = 0;
                Hours = 0;

            }
        });

        CancelTimeLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenTime.dismiss();
            }
        });

        OpenTime.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.DayOfWeekSpinnerBusinessOpenTimeSetActivity: {
                TypeDayToWeek = i;

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

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.BusinessOpenTimeGet) {
                Feedback<BusinessOpenTimeViewModel> FeedBack = (Feedback<BusinessOpenTimeViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    BusinessOpenTimeViewModel ViewModel = FeedBack.getValue();
                    OutputViewModel = ViewModel;

                    if (ViewModel != null) {
                        //پر کردن ویو با اطلاعات دریافتی
                        SetContactToView(ViewModel);
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
            } else if (ServiceMethod == ServiceMethodType.BusinessOpenTimeSet) {
                Feedback<BusinessOpenTimeViewModel> FeedBack = (Feedback<BusinessOpenTimeViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.UpdatedSuccessful.getId()) {
                    BusinessOpenTimeViewModel ViewModel = FeedBack.getValue();

                    if (ViewModel != null) {
                        //پر کردن ویو با اطلاعات دریافتی
                        SetContactToView(ViewModel);
                        OutputViewModel = ViewModel;
                        SendDataToParentActivity(false);
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
            } else if (ServiceMethod == ServiceMethodType.BusinessOpenTimeAdd) {
                Feedback<BusinessOpenTimeViewModel> FeedBack = (Feedback<BusinessOpenTimeViewModel>) Data;
                if (FeedBack.getStatus() == FeedbackType.RegisteredSuccessful.getId()) {
                    BusinessOpenTimeViewModel ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        OutputViewModel = ViewModel;
                        SendDataToParentActivity(true);
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
        } catch (Exception e) {

        }
    }

    public void SetDayOfWeekToSpinner() {

        DayOfWeekSpinnerAdapter dayOfWeekSpinnerAdapter = new DayOfWeekSpinnerAdapter(this, DayOfWeek);
        DayOfWeekSpinnerBusinessOpenTimeSetActivity.setAdapter(dayOfWeekSpinnerAdapter);

    }

    public void SetContactToView(BusinessOpenTimeViewModel ViewModel) {
        BusinessOpenTimeId = ViewModel.getId();

        if (!ViewModel.getFrom().equals(""))
            FromHoursTextViewBusinessOpenTimeSetActivity.setText(ViewModel.getFrom());

        if (!ViewModel.getTo().equals(""))
            ToHoursTextViewBusinessOpenTimeSetActivity.setText(ViewModel.getTo());

        if (ViewModel.getDayOfWeek() != 0)
            DayOfWeekSpinnerBusinessOpenTimeSetActivity.setSelection(ViewModel.getDayOfWeek());
    }

    /**
     * دریافت ویومدل از اکتیویتی تماس جهت ارسال به فرگمنت لیست تماس
     */
    private void SendDataToParentActivity(boolean IsAdd) {
        HashMap<String, Object> Output = new HashMap<>();
        try {
            Output.put("IsAdd", IsAdd);
            Output.put("BusinessOpenTimeViewModel", OutputViewModel);
        } catch (Exception Ex) {
            Output.put("BusinessOpenTimeViewModel", null);
        }
        ActivityResultPassing.Push(new ActivityResult(getParentActivity(), getCurrentActivityId(), Output));
        FinishCurrentActivity();
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
