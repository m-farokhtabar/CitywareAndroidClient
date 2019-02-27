package ir.rayas.app.citywareclient.View.Fragment.Business;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SwitchCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Business.BusinessService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.PersianCalendarConverter;
import ir.rayas.app.citywareclient.Share.Helper.Gps;
import ir.rayas.app.citywareclient.Share.Helper.IResponseTurnOnGpsDialog;
import ir.rayas.app.citywareclient.Share.Helper.TypefaceSpan;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.EditTextPersian;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.UserProfileChildren.BusinessSetActivity;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.Share.MapActivity;
import ir.rayas.app.citywareclient.View.Share.SelectBusinessCategoryActivity;
import ir.rayas.app.citywareclient.View.Share.SelectRegionActivity;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessViewModel;


public class BusinessFragment extends Fragment implements IResponseService, ILoadData, IResponseTurnOnGpsDialog {

    private BusinessSetActivity Context = null;
    private TextViewPersian SetAddressOnMapTextViewUserBusinessSetActivity = null;
    private TextViewPersian RegionNameTextViewUserBusinessSetActivity = null;
    private TextViewPersian BusinessCategoryNameTextViewUserBusinessSetActivity = null;
    private TextViewPersian EstablishmentTextViewUserBusinessSetActivity = null;
    private TextViewPersian ConfirmTypeNameTextViewUserBusinessSetActivity = null;
    private TextViewPersian ConfirmCommentTextViewUserBusinessSetActivity = null;
    private EditTextPersian TitleEditTextUserBusinessSetActivity = null;
    private EditTextPersian BusinessTitleEditTextUserBusinessSetActivity = null;
    private EditTextPersian KeywordEditTextUserBusinessSetActivity = null;
    private EditTextPersian AddressEditTextUserUserBusinessSetActivity = null;
    private EditTextPersian CountOfEmployeesEditTextUserBusinessSetActivity = null;
    private EditTextPersian PostalCodeEditTextUserBusinessSetActivity = null;
    private EditTextPersian BusinessDescriptionEditTextUserBusinessSetActivity = null;
    private EditTextPersian BusinessCommissionEditTextUserBusinessSetActivity = null;
    private SwipeRefreshLayout RefreshBusinessSwipeRefreshLayoutBusinessFragment = null;
    private SwitchCompat IsOpenSwitchUserBusinessSetActivity = null;
    private SwitchCompat HasDeliverySwitchUserBusinessSetActivity = null;

    private int BusinessId = 0;
    private int UserId = 0;

    private int RegionId = 0;
    private int BusinessCategoryId = 0;
    private int CountOfEmployees = 0;
    private double BusinessCommission = 0;
    private boolean IsOpen = false;
    private boolean HasDelivery = false;
    private double Latitude = 0.0;
    private double Longitude = 0.0;

    private boolean IsSwipe = false;
    private Gps CurrentGps = null;

    private String PersianDate = "";
    int Year = 0;
    int Month = 0;
    int Day = 0;

    //جهت ارسال به اکتیویتی لیست کسب و کار ها تا در صورت ویرایش کاربر آیتم مورد نظر نیز در لیست ویرایش شود
    private BusinessViewModel OutputViewModel = null;

    /**
     * ارسال آخرین ویومدل تغییرات کسب و کار به اکتیوتی لیست پروفایل کاربر
     *
     * @return بیزینس آخرین تغییرات
     */
    public BusinessViewModel getOutputViewModel() {
        return OutputViewModel;
    }

    /**
     * جهت پر کردن کد منطقه توسط اکتیویتی انتخاب منطقه
     *
     * @param regionId کد منطقه
     */
    public void setRegionId(int regionId) {
        RegionId = regionId;
    }

    /**
     * جهت پر کردن نام منطقه توسط اکتیویتی انتخاب منطقه
     *
     * @param Name نام منطقه
     */
    public void setRegionName(String Name) {
        RegionNameTextViewUserBusinessSetActivity.setText(Name);
    }

    /**
     * جهت پر کردن کد رسته توسط اکتیویتی انتخاب رسته
     *
     * @param businessCategoryId کد رسته یا دسته
     */
    public void setBusinessCategoryId(int businessCategoryId) {
        BusinessCategoryId = businessCategoryId;
    }

    /**
     * جهت پر کردن نام رسته توسط اکتیویتی انتخاب رسته
     *
     * @param Name نام رسته یا دسته
     */
    public void setBusinessCategoryName(String Name) {
        BusinessCategoryNameTextViewUserBusinessSetActivity.setText(Name);
    }

    /**
     * جهت پر کردن نام عرض جغرافیایی توسط اکتیویتی انتخاب نقشه
     *
     * @param latitude عرض جغرافیایی
     */
    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    /**
     * جهت پر کردن نام طول جغرافیایی توسط اکتیویتی انتخاب نقشه
     *
     * @param longitude طول جغرافیایی
     */
    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (BusinessSetActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_business, container, false);
        //دریافت کد کاربر
        AccountRepository Ap = new AccountRepository(Context);
        UserId = Ap.getAccount().getUser().getId();
        //کلاس کنترل و مدیریت GPS
        CurrentGps = new Gps();
        //طرحبندی ویو
        CreateLayout(CurrentView);
        //برای فهمیدن کد فرگنت به UserBusinessSetPagerAdapter مراجعه کنید
        Context.setFragmentIndex(3);
        //بدست آوردن کد کسب و کار
        BusinessId = Context.getBusinessId();
        //دریافت اطلاعات از سرور
        LoadData();


        return CurrentView;
    }

    private void CreateLayout(View CurrentView) {

        TextViewPersian TitleConfirmCommentTextViewUserBusinessSetActivity = CurrentView.findViewById(R.id.TitleConfirmCommentTextViewUserBusinessSetActivity);
        TextViewPersian TitleConfirmTypeNameTextViewUserBusinessSetActivity = CurrentView.findViewById(R.id.TitleConfirmTypeNameTextViewUserBusinessSetActivity);
        TextViewPersian MinusCountOfEmployeesTextViewUserBusinessSetActivity = CurrentView.findViewById(R.id.MinusCountOfEmployeesTextViewUserBusinessSetActivity);
        TextViewPersian PlusCountOfEmployeesTextViewUserBusinessSetActivity = CurrentView.findViewById(R.id.PlusCountOfEmployeesTextViewUserBusinessSetActivity);
        TextViewPersian MapIconTextViewUserBusinessSetActivity = CurrentView.findViewById(R.id.MapIconTextViewUserBusinessSetActivity);
        ButtonPersianView AddRegionButtonUserBusinessSetActivity = CurrentView.findViewById(R.id.AddRegionButtonUserBusinessSetActivity);
        ButtonPersianView AddCategoryNameButtonUserBusinessSetActivity = CurrentView.findViewById(R.id.AddCategoryNameButtonUserBusinessSetActivity);
        ButtonPersianView SetButtonUserBusinessSetActivity = CurrentView.findViewById(R.id.SetButtonUserBusinessSetActivity);
        RegionNameTextViewUserBusinessSetActivity = CurrentView.findViewById(R.id.RegionNameTextViewUserBusinessSetActivity);
        BusinessCategoryNameTextViewUserBusinessSetActivity = CurrentView.findViewById(R.id.BusinessCategoryNameTextViewUserBusinessSetActivity);
        TitleEditTextUserBusinessSetActivity = CurrentView.findViewById(R.id.TitleEditTextUserBusinessSetActivity);
        BusinessTitleEditTextUserBusinessSetActivity = CurrentView.findViewById(R.id.BusinessTitleEditTextUserBusinessSetActivity);
        KeywordEditTextUserBusinessSetActivity = CurrentView.findViewById(R.id.KeywordEditTextUserBusinessSetActivity);
        AddressEditTextUserUserBusinessSetActivity = CurrentView.findViewById(R.id.AddressEditTextUserUserBusinessSetActivity);
        IsOpenSwitchUserBusinessSetActivity = CurrentView.findViewById(R.id.IsOpenSwitchUserBusinessSetActivity);
        HasDeliverySwitchUserBusinessSetActivity = CurrentView.findViewById(R.id.HasDeliverySwitchUserBusinessSetActivity);
        CountOfEmployeesEditTextUserBusinessSetActivity = CurrentView.findViewById(R.id.CountOfEmployeesEditTextUserBusinessSetActivity);
        EstablishmentTextViewUserBusinessSetActivity = CurrentView.findViewById(R.id.EstablishmentTextViewUserBusinessSetActivity);
        PostalCodeEditTextUserBusinessSetActivity = CurrentView.findViewById(R.id.PostalCodeEditTextUserBusinessSetActivity);
        BusinessDescriptionEditTextUserBusinessSetActivity = CurrentView.findViewById(R.id.BusinessDescriptionEditTextUserBusinessSetActivity);
        BusinessCommissionEditTextUserBusinessSetActivity = CurrentView.findViewById(R.id.BusinessCommissionEditTextUserBusinessSetActivity);
        ConfirmTypeNameTextViewUserBusinessSetActivity = CurrentView.findViewById(R.id.ConfirmTypeNameTextViewUserBusinessSetActivity);
        ConfirmCommentTextViewUserBusinessSetActivity = CurrentView.findViewById(R.id.ConfirmCommentTextViewUserBusinessSetActivity);
        SetAddressOnMapTextViewUserBusinessSetActivity = CurrentView.findViewById(R.id.SetAddressOnMapTextViewUserBusinessSetActivity);
        LinearLayout ShowMapLinearLayoutUserBusinessSetActivity = CurrentView.findViewById(R.id.ShowMapLinearLayoutUserBusinessSetActivity);
        TextViewPersian MinusBusinessCommissionTextViewUserBusinessSetActivity = CurrentView.findViewById(R.id.MinusBusinessCommissionTextViewUserBusinessSetActivity);
        TextViewPersian PlusBusinessCommissionTextViewUserBusinessSetActivity = CurrentView.findViewById(R.id.PlusBusinessCommissionTextViewUserBusinessSetActivity);
        RefreshBusinessSwipeRefreshLayoutBusinessFragment = CurrentView.findViewById(R.id.RefreshBusinessSwipeRefreshLayoutBusinessFragment);
        FloatingActionButton BottomOfPageFloatingActionButtonUserBusinessSetActivity = CurrentView.findViewById(R.id.BottomOfPageFloatingActionButtonUserBusinessSetActivity);
        final ScrollView ScrollViewUserBusinessSetActivity = CurrentView.findViewById(R.id.ScrollViewUserBusinessSetActivity);

        if (Context.getIsSet().equals("Edit")) {
            ConfirmCommentTextViewUserBusinessSetActivity.setVisibility(View.VISIBLE);
            TitleConfirmCommentTextViewUserBusinessSetActivity.setVisibility(View.VISIBLE);
            ConfirmTypeNameTextViewUserBusinessSetActivity.setVisibility(View.VISIBLE);
            TitleConfirmTypeNameTextViewUserBusinessSetActivity.setVisibility(View.VISIBLE);
        } else {
            ConfirmCommentTextViewUserBusinessSetActivity.setVisibility(View.GONE);
            TitleConfirmCommentTextViewUserBusinessSetActivity.setVisibility(View.GONE);
            ConfirmTypeNameTextViewUserBusinessSetActivity.setVisibility(View.GONE);
            TitleConfirmTypeNameTextViewUserBusinessSetActivity.setVisibility(View.GONE);
        }

        MinusCountOfEmployeesTextViewUserBusinessSetActivity.setTypeface(Font.MasterIcon);
        MinusCountOfEmployeesTextViewUserBusinessSetActivity.setText("\uf068");

        PlusCountOfEmployeesTextViewUserBusinessSetActivity.setTypeface(Font.MasterIcon);
        PlusCountOfEmployeesTextViewUserBusinessSetActivity.setText("\uf067");

        MinusBusinessCommissionTextViewUserBusinessSetActivity.setTypeface(Font.MasterIcon);
        MinusBusinessCommissionTextViewUserBusinessSetActivity.setText("\uf068");

        PlusBusinessCommissionTextViewUserBusinessSetActivity.setTypeface(Font.MasterIcon);
        PlusBusinessCommissionTextViewUserBusinessSetActivity.setText("\uf067");

        MapIconTextViewUserBusinessSetActivity.setTypeface(Font.MasterIcon);
        MapIconTextViewUserBusinessSetActivity.setText("\uf041");

        AddRegionButtonUserBusinessSetActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SelectRegionIntent = Context.NewIntent(SelectRegionActivity.class);
                Context.startActivity(SelectRegionIntent);
            }
        });

        AddCategoryNameButtonUserBusinessSetActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SelectCategoryIntent = Context.NewIntent(SelectBusinessCategoryActivity.class);
                Context.startActivity(SelectCategoryIntent);
            }
        });

        SetButtonUserBusinessSetActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnSaveBusinessButtonClick(view);
            }
        });


        IsOpenSwitchUserBusinessSetActivity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    IsOpen = true;
                } else {
                    IsOpen = false;
                }
            }
        });

        HasDeliverySwitchUserBusinessSetActivity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HasDelivery = true;
                } else {
                    HasDelivery = false;
                }
            }
        });

        MinusCountOfEmployeesTextViewUserBusinessSetActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int Employees;
                if ((CountOfEmployeesEditTextUserBusinessSetActivity.getText().toString().equals(""))) {
                    Employees = 0;
                } else {
                    Employees = Integer.parseInt(CountOfEmployeesEditTextUserBusinessSetActivity.getText().toString().trim());
                }
                if (Employees > 0) {
                    CountOfEmployees = Employees - 1;
                    CountOfEmployeesEditTextUserBusinessSetActivity.setText(CountOfEmployees + "");
                }
            }
        });

        PlusCountOfEmployeesTextViewUserBusinessSetActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int Employees;
                if ((CountOfEmployeesEditTextUserBusinessSetActivity.getText().toString().equals(""))) {
                    Employees = 0;
                } else {
                    Employees = Integer.parseInt(CountOfEmployeesEditTextUserBusinessSetActivity.getText().toString().trim());
                }
                if (Employees < 9999) {
                    CountOfEmployees = Employees + 1;
                    CountOfEmployeesEditTextUserBusinessSetActivity.setText(CountOfEmployees + "");
                }
            }
        });

        MinusBusinessCommissionTextViewUserBusinessSetActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double Commission;
                if ((BusinessCommissionEditTextUserBusinessSetActivity.getText().toString().equals(""))) {
                    Commission = 0.0;
                } else {
                    Commission = Double.parseDouble(BusinessCommissionEditTextUserBusinessSetActivity.getText().toString().trim());
                }
                if (Commission > 0.5) {
                    BusinessCommission = Commission - 0.5;
                    BusinessCommissionEditTextUserBusinessSetActivity.setText(BusinessCommission + "");
                } else {
                    BusinessCommission = 0;
                    BusinessCommissionEditTextUserBusinessSetActivity.setText(BusinessCommission + "");
                }
            }
        });

        PlusBusinessCommissionTextViewUserBusinessSetActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double Commission;
                if ((BusinessCommissionEditTextUserBusinessSetActivity.getText().toString().equals(""))) {
                    Commission = 0.0;
                } else {
                    Commission = Double.parseDouble(BusinessCommissionEditTextUserBusinessSetActivity.getText().toString().trim());
                }
                if (Commission < 99.5)
                    BusinessCommission = Commission + 0.5;
                else
                    BusinessCommission = 100;
                BusinessCommissionEditTextUserBusinessSetActivity.setText(BusinessCommission + "");
            }
        });


        EstablishmentTextViewUserBusinessSetActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calender(EstablishmentTextViewUserBusinessSetActivity);
            }
        });

        ShowMapLinearLayoutUserBusinessSetActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnShowMapClick();

            }
        });

        RefreshBusinessSwipeRefreshLayoutBusinessFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                IsSwipe = true;
                LoadData();

            }
        });

        BottomOfPageFloatingActionButtonUserBusinessSetActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScrollViewUserBusinessSetActivity.fullScroll(View.FOCUS_DOWN);
            }
        });


    }

    @Override
    public void LoadData() {

        if (Context.getIsSet().equals("Edit")) {
            if (!IsSwipe)
                Context.ShowLoadingProgressBar();
            BusinessService BusinessService = new BusinessService(this);
            //دریافت اطلاعات
            Context.setRetryType(2);
            BusinessService.Get(BusinessId);
        } else if (Context.getIsSet().equals("New")) {
            RefreshBusinessSwipeRefreshLayoutBusinessFragment.setRefreshing(false);
        }
    }

    private void Calender(final TextViewPersian textViewPersian) {

        PersianCalendarConverter persianCalendar;
        persianCalendar = GetDateToday();

        final Dialog calender = new Dialog(getActivity());
        calender.requestWindowFeature(Window.FEATURE_NO_TITLE);
        calender.setContentView(R.layout.layout_date_picker);

        LinearLayout CancelCalenderLinearLayout = calender.findViewById(R.id.CancelCalenderLinearLayout);
        LinearLayout SaveCalenderLinearLayout = calender.findViewById(R.id.SaveCalenderLinearLayout);
        final TextViewPersian ShowSelectDateTextView = calender.findViewById(R.id.ShowSelectDateTextView);
        NumberPicker YearNumberPicker = calender.findViewById(R.id.YearNumberPicker);
        NumberPicker MonthNumberPicker = calender.findViewById(R.id.MonthNumberPicker);
        final NumberPicker DayNumberPicker = calender.findViewById(R.id.DayNumberPicker);


        YearNumberPicker.setMinValue(PersianCalendarConverter.MinYear);
        YearNumberPicker.setMaxValue(persianCalendar.getYear() + 10);
        MonthNumberPicker.setMinValue(1);
        MonthNumberPicker.setMaxValue(12);
        DayNumberPicker.setMinValue(1);
        if (persianCalendar.getMonth() > 6) {
            DayNumberPicker.setMaxValue(30);
        } else {
            DayNumberPicker.setMaxValue(31);
        }


        if (textViewPersian.getText().toString().contains("/")) {
            ShowSelectDateTextView.setText(textViewPersian.getText().toString());
            String Date = textViewPersian.getText().toString().trim();
            StringTokenizer FromDateTokens = new StringTokenizer(Date, "/");
            Year = Integer.parseInt(FromDateTokens.nextToken());
            Month = Integer.parseInt(FromDateTokens.nextToken());
            Day = Integer.parseInt(FromDateTokens.nextToken());

            int MaxDaysOfMonth = PersianCalendarConverter.GetMaxDaysOfMonth(Year, Month);
            DayNumberPicker.setMaxValue(MaxDaysOfMonth);
            if (Day > MaxDaysOfMonth)
                Day = MaxDaysOfMonth;
            YearNumberPicker.setValue(Year);
            MonthNumberPicker.setValue(Month);
            DayNumberPicker.setValue(Day);
        } else {

            Year = persianCalendar.getYear();
            Month = persianCalendar.getMonth();
            Day = persianCalendar.getDay();
            YearNumberPicker.setValue(Year);
            MonthNumberPicker.setValue(Month);
            DayNumberPicker.setValue(Day);
        }

        ShowSelectDateTextView.setText((Year + "/" + Month + "/" + Day));

        YearNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int newYear) {

                Year = newYear;
                int MaxDaysOfMonth = PersianCalendarConverter.GetMaxDaysOfMonth(Year, Month);
                DayNumberPicker.setMaxValue(MaxDaysOfMonth);
                if (Day > MaxDaysOfMonth)
                    Day = MaxDaysOfMonth;
                ShowSelectDateTextView.setText((Year + "/" + Month + "/" + Day));
            }
        });

        MonthNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int newMonth) {
                Month = newMonth;
                int MaxDaysOfMonth = PersianCalendarConverter.GetMaxDaysOfMonth(Year, Month);
                DayNumberPicker.setMaxValue(MaxDaysOfMonth);
                if (Day > MaxDaysOfMonth)
                    Day = MaxDaysOfMonth;
                ShowSelectDateTextView.setText((Year + "/" + Month + "/" + Day));
            }
        });

        DayNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int newDay) {
                Day = newDay;
                ShowSelectDateTextView.setText((Year + "/" + Month + "/" + Day));
            }
        });


        SaveCalenderLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersianDate = ShowSelectDateTextView.getText().toString();
                if (!PersianDate.equals(""))
                    textViewPersian.setText(PersianDate);
                textViewPersian.setTextColor(Context.getResources().getColor(R.color.FontBlackColor));

                calender.dismiss();

            }
        });

        CancelCalenderLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calender.dismiss();
            }
        });

        calender.show();
    }

    private PersianCalendarConverter GetDateToday() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String CurrentDate = sdf.format(new Date());

        StringTokenizer FromDateTokens = new StringTokenizer(CurrentDate, "-");
        int Year = Integer.parseInt(FromDateTokens.nextToken());
        int Month = Integer.parseInt(FromDateTokens.nextToken());
        int Day = Integer.parseInt(FromDateTokens.nextToken());

        PersianCalendarConverter converter = new PersianCalendarConverter();
        converter.gregorianToPersian(Year, Month, Day);

        return converter;
    }


    private void OnSaveBusinessButtonClick(View view) {
        if (RegionId == 0) {
            RegionNameTextViewUserBusinessSetActivity.requestFocus();
            SpannableString SpannableString = new SpannableString(getResources().getString(R.string.please_enter_region_name));
            SpannableString.setSpan(new TypefaceSpan(Font.MasterFont), 0, SpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            RegionNameTextViewUserBusinessSetActivity.setError(SpannableString, null);
        } else {
            if (BusinessCategoryId == 0) {
                BusinessCategoryNameTextViewUserBusinessSetActivity.requestFocus();
                SpannableString SpannableString = new SpannableString(getResources().getString(R.string.please_enter_category_name));
                SpannableString.setSpan(new TypefaceSpan(Font.MasterFont), 0, SpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                BusinessCategoryNameTextViewUserBusinessSetActivity.setError(SpannableString, null);
            } else {

                if (Utility.ValidateView(TitleEditTextUserBusinessSetActivity,
                        BusinessTitleEditTextUserBusinessSetActivity, KeywordEditTextUserBusinessSetActivity, AddressEditTextUserUserBusinessSetActivity, IsOpenSwitchUserBusinessSetActivity,
                        HasDeliverySwitchUserBusinessSetActivity, CountOfEmployeesEditTextUserBusinessSetActivity, EstablishmentTextViewUserBusinessSetActivity, PostalCodeEditTextUserBusinessSetActivity,
                        BusinessDescriptionEditTextUserBusinessSetActivity, BusinessCommissionEditTextUserBusinessSetActivity)) {
                    if (Latitude == 0 || Longitude == 0) {
                        SetAddressOnMapTextViewUserBusinessSetActivity.requestFocus();
                        SpannableString SpannableString = new SpannableString(getResources().getString(R.string.please_select_your_business_Location_in_map));
                        SpannableString.setSpan(new TypefaceSpan(Font.MasterFont), 0, SpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        SetAddressOnMapTextViewUserBusinessSetActivity.setError(SpannableString, null);
                    } else {
                        Context.ShowLoadingProgressBar();
                        BusinessService Service = new BusinessService(this);
                        //ثبت اطلاعات
                        Context.setRetryType(1);
                        if (Context.getIsSet().equals("Edit")) {
                            Service.Set(MadeViewModel(), BusinessId);
                        } else {
                            Service.Add(MadeViewModel());
                        }
                    }
                }
            }
        }
    }

    private BusinessViewModel MadeViewModel() {
        BusinessViewModel ViewModel = new BusinessViewModel();
        try {
            ViewModel.setId(BusinessId);
            ViewModel.setUserId(UserId);
            ViewModel.setRegionId(RegionId);
            ViewModel.setRegionName(RegionNameTextViewUserBusinessSetActivity.getText().toString());
            ViewModel.setBusinessCategoryId(BusinessCategoryId);
            ViewModel.setBusinessCategoryName(BusinessCategoryNameTextViewUserBusinessSetActivity.getText().toString());
            ViewModel.setTitle(TitleEditTextUserBusinessSetActivity.getText().toString());
            ViewModel.setJobTitle(BusinessTitleEditTextUserBusinessSetActivity.getText().toString());
            ViewModel.setKeyword(KeywordEditTextUserBusinessSetActivity.getText().toString());
            ViewModel.setAddress(AddressEditTextUserUserBusinessSetActivity.getText().toString());
            ViewModel.setOpen(IsOpen);
            ViewModel.setHasDelivery(HasDelivery);

            if (!(CountOfEmployeesEditTextUserBusinessSetActivity.getText().toString().equals(""))) {
                ViewModel.setCountOfEmployees(Integer.parseInt(CountOfEmployeesEditTextUserBusinessSetActivity.getText().toString().trim()));
            } else {
                ViewModel.setCountOfEmployees(0);
            }
            if (!(BusinessCommissionEditTextUserBusinessSetActivity.getText().toString().equals(""))) {
                ViewModel.setCommission(Double.parseDouble(BusinessCommissionEditTextUserBusinessSetActivity.getText().toString()));
            } else {
                ViewModel.setCommission(0);
            }

            if (Utility.CheckDate(EstablishmentTextViewUserBusinessSetActivity.getText().toString()))
                if (EstablishmentTextViewUserBusinessSetActivity.getText().toString().contains("/"))
                    ViewModel.setEstablishment(EstablishmentTextViewUserBusinessSetActivity.getText().toString());
                else
                    ViewModel.setEstablishment("");
            else
                ViewModel.setEstablishment("");

            ViewModel.setPostalCode(PostalCodeEditTextUserBusinessSetActivity.getText().toString());
            ViewModel.setDescription(BusinessDescriptionEditTextUserBusinessSetActivity.getText().toString());
            ViewModel.setConfirmComment(ConfirmCommentTextViewUserBusinessSetActivity.getText().toString());
            ViewModel.setConfirmTypeName(ConfirmTypeNameTextViewUserBusinessSetActivity.getText().toString());

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
        Intent MapIntent = Context.NewIntent(MapActivity.class);
        MapIntent.putExtra("Latitude", Latitude);
        MapIntent.putExtra("Longitude", Longitude);
        Context.startActivity(MapIntent);
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        RefreshBusinessSwipeRefreshLayoutBusinessFragment.setRefreshing(false);
        try {
            if (ServiceMethod == ServiceMethodType.BusinessGet) {
                Feedback<BusinessViewModel> FeedBack = (Feedback<BusinessViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    BusinessViewModel ViewModel = FeedBack.getValue();

                    if (ViewModel != null) {
                        //پر کردن ویو با اطلاعات دریافتی
                        SetBusinessToView(ViewModel);
                        //پر کردن ویومدل برای ارسال به لیست کسب وکار جهت به روز رسانی لیست
                        OutputViewModel = ViewModel;
                    } else {
                        Context.ShowToast(FeedbackType.InvalidDataFormat.getMessage().replace("{0}", ""), Toast.LENGTH_LONG, MessageType.Warning);
                    }

                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }

            } else if (ServiceMethod == ServiceMethodType.BusinessSet) {
                Feedback<BusinessViewModel> FeedBack = (Feedback<BusinessViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.UpdatedSuccessful.getId()) {
                    BusinessViewModel ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        //پر کردن ویومدل برای ارسال به لیست کسب وکار جهت به روز رسانی لیست
                        OutputViewModel = ViewModel;
                        //به دلیل اینکه از سمت سرور نام منطقه و رسته و نام نوع تایید زمان به روز رسانی بر نمی گردد
                        OutputViewModel.setRegionName(RegionNameTextViewUserBusinessSetActivity.getText().toString());
                        OutputViewModel.setConfirmTypeName(ConfirmTypeNameTextViewUserBusinessSetActivity.getText().toString());
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowToast(FeedbackType.InvalidDataFormat.getMessage().replace("{0}", ""), Toast.LENGTH_LONG, MessageType.Warning);
                    }


                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        //TitleEditTextUserBusinessSetActivity.requestFocus();
                        //Utility.ShowKeyboard(TitleEditTextUserBusinessSetActivity);
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.BusinessAdd) {
                Feedback<BusinessViewModel> FeedBack = (Feedback<BusinessViewModel>) Data;
                if (FeedBack.getStatus() == FeedbackType.RegisteredSuccessful.getId()) {
                    BusinessViewModel ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        //پر کردن ویومدل برای ارسال به لیست کسب و کار جهت به روز رسانی لیست
                        OutputViewModel = ViewModel;
                        BusinessId = ViewModel.getId();
                        Context.setBusinessId(BusinessId);
                        Context.setIsSet("Edit");
                        Context.EnableOrDisableChildTab(true);
                        //به دلیل اینکه از سمت سرور نام منطقه و رسته زمان به روز رسانی بر نمی گردد
                        OutputViewModel.setRegionName(RegionNameTextViewUserBusinessSetActivity.getText().toString());
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowToast(FeedbackType.InvalidDataFormat.getMessage().replace("{0}", ""), Toast.LENGTH_LONG, MessageType.Warning);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        //TitleEditTextUserBusinessSetActivity.requestFocus();
                        //Utility.ShowKeyboard(TitleEditTextUserBusinessSetActivity);
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            }
        } catch (Exception e) {
            Context.HideLoading();
            Context.ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }

    }

    private void SetBusinessToView(BusinessViewModel businessViewModel) {
        RegionId = businessViewModel.getRegionId();
        RegionNameTextViewUserBusinessSetActivity.setText(businessViewModel.getRegionName());

        BusinessCategoryId = businessViewModel.getBusinessCategoryId();
        BusinessCategoryNameTextViewUserBusinessSetActivity.setText(businessViewModel.getBusinessCategoryName());

        TitleEditTextUserBusinessSetActivity.setText(businessViewModel.getTitle());
        BusinessTitleEditTextUserBusinessSetActivity.setText(businessViewModel.getJobTitle());

        if (!businessViewModel.getKeyword().equals(""))
            KeywordEditTextUserBusinessSetActivity.setText(businessViewModel.getKeyword());
        else
            KeywordEditTextUserBusinessSetActivity.setText("");

        AddressEditTextUserUserBusinessSetActivity.setText(businessViewModel.getAddress());
        IsOpenSwitchUserBusinessSetActivity.setChecked(businessViewModel.isOpen());
        HasDeliverySwitchUserBusinessSetActivity.setChecked(businessViewModel.isHasDelivery());

        if (businessViewModel.getCountOfEmployees() > 0)
            CountOfEmployeesEditTextUserBusinessSetActivity.setText(String.valueOf(businessViewModel.getCountOfEmployees()));
        else
            CountOfEmployeesEditTextUserBusinessSetActivity.setText("");

        if (Utility.CheckDateTime(businessViewModel.getEstablishment())) {
            String Date = Utility.GetDateFromDateTime(businessViewModel.getEstablishment());
            if (!Date.equals(""))
                EstablishmentTextViewUserBusinessSetActivity.setText(Date);
        } else {
            EstablishmentTextViewUserBusinessSetActivity.setText("");
        }

        if (!businessViewModel.getPostalCode().equals(""))
            PostalCodeEditTextUserBusinessSetActivity.setText(businessViewModel.getPostalCode());
        else
            PostalCodeEditTextUserBusinessSetActivity.setText("");

        if (!businessViewModel.getDescription().equals(""))
            BusinessDescriptionEditTextUserBusinessSetActivity.setText(businessViewModel.getDescription());
        else
            BusinessDescriptionEditTextUserBusinessSetActivity.setText("");

        if (businessViewModel.getCommission() > 0) {
            BusinessCommissionEditTextUserBusinessSetActivity.setText(String.valueOf(businessViewModel.getCommission()));
        } else {
            BusinessCommissionEditTextUserBusinessSetActivity.setText("");
        }

        if (!businessViewModel.getConfirmTypeName().equals(""))
            ConfirmTypeNameTextViewUserBusinessSetActivity.setText(businessViewModel.getConfirmTypeName());
        else
            ConfirmTypeNameTextViewUserBusinessSetActivity.setText("");
        if (!businessViewModel.getConfirmComment().equals(""))
            ConfirmCommentTextViewUserBusinessSetActivity.setText(businessViewModel.getConfirmComment());
        else
            ConfirmCommentTextViewUserBusinessSetActivity.setText("");

        if (businessViewModel.getConfirmTypeId() == 3) {
            ConfirmTypeNameTextViewUserBusinessSetActivity.setTextColor(Context.getResources().getColor(R.color.FontGreenColor));
            ConfirmCommentTextViewUserBusinessSetActivity.setTextColor(Context.getResources().getColor(R.color.FontGreenColor));
        } else if (businessViewModel.getConfirmTypeId() == 4) {
            ConfirmTypeNameTextViewUserBusinessSetActivity.setTextColor(Context.getResources().getColor(R.color.FontRedColor));
            ConfirmCommentTextViewUserBusinessSetActivity.setTextColor(Context.getResources().getColor(R.color.FontRedColor));
        } else {
            ConfirmTypeNameTextViewUserBusinessSetActivity.setTextColor(Context.getResources().getColor(R.color.FontSemiBlackColor));
            ConfirmCommentTextViewUserBusinessSetActivity.setTextColor(Context.getResources().getColor(R.color.FontSemiBlackColor));
        }

        Latitude = businessViewModel.getLatitude();
        Longitude = businessViewModel.getLongitude();


    }

    /**
     * ثبت تنظیمات در صورت اکی بودن وضعیت GPS
     */
    private void OnShowMapClick() {
        if (CurrentGps.IsMapAlreadyToUse(Context, this, R.string.turn_on_location_to_get_package_faster)) {
            GoToMapActivity();
        }
    }

    /**
     * رد کردن درخوسات روشن کردن مکان یابد
     *
     * @param IsClickYes
     */
    @Override
    public void OnDismissTurnOnGpsDialog(boolean IsClickYes) {
        if (!IsClickYes) {
            Context.ShowToast(getResources().getString(R.string.for_register_address_turn_on_gps), Toast.LENGTH_LONG, MessageType.Warning);
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
                CurrentGps.ShowTurnOnGpsDialog(Context, this, R.string.turn_on_location_to_get_package_faster);
            else
                GoToMapActivity();
        } else {
            Context.ShowToast(getResources().getString(R.string.app_permission_denied), Toast.LENGTH_LONG, MessageType.Warning);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (Context != null) {
                //برای فهمیدن کد فرگنت به UserBusinessSetPagerAdapter مراجعه کنید
                Context.setFragmentIndex(3);
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

}