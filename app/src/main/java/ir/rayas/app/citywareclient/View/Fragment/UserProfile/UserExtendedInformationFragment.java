package ir.rayas.app.citywareclient.View.Fragment.UserProfile;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import ir.rayas.app.citywareclient.Adapter.Spinner.BankTypeSpinnerAdapter;
import ir.rayas.app.citywareclient.Adapter.Spinner.ColorTypeSpinnerAdapter;
import ir.rayas.app.citywareclient.Adapter.Spinner.DegreeOfEducationSpinnerAdapter;
import ir.rayas.app.citywareclient.Adapter.Spinner.SexTypeSpinnerAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.User.UserInfoService;
import ir.rayas.app.citywareclient.Share.Enum.ColorType;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.PersianCalendarConverter;
import ir.rayas.app.citywareclient.Share.Helper.TypefaceSpan;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.CustomNumberPicker;
import ir.rayas.app.citywareclient.Share.Layout.View.EditTextPersian;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.Master.UserProfileActivity;
import ir.rayas.app.citywareclient.ViewModel.Definition.BankTypeViewModel;
import ir.rayas.app.citywareclient.ViewModel.Definition.ColorTypeViewModel;
import ir.rayas.app.citywareclient.ViewModel.Definition.DegreeOfEducationViewModel;
import ir.rayas.app.citywareclient.ViewModel.Definition.SexTypeViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.UserInfoViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.UserInfoWithTypesViewModel;


public class UserExtendedInformationFragment extends Fragment implements IResponseService, ILoadData, AdapterView.OnItemSelectedListener {

    private UserProfileActivity Context = null;

    private TextViewPersian BirthDateTextViewUserExtendedInformation = null;
    private TextViewPersian MarriedDateTextViewUserExtendedInformation = null;
    private TextViewPersian MotherBirthDayTextViewUserExtendedInformation = null;
    private TextViewPersian FatherBirthDayTextViewUserExtendedInformation = null;
    private TextViewPersian SpouseBirthDayTextViewUserExtendedInformation = null;
    private TextViewPersian FirstChildBirthDayTextViewUserExtendedInformation = null;
    private TextViewPersian SecondChildBirthDayTextViewUserExtendedInformation = null;

    private EditTextPersian EmailEditTextUserExtendedInformation = null;
    private EditTextPersian HeightEditTextUserExtendedInformation = null;
    private EditTextPersian WeightEditTextUserExtendedInformation = null;
    private EditTextPersian AboutMeEditTextUserExtendedInformation = null;
    private EditTextPersian AccountNumberEditTextUserExtendedInformation = null;
    private EditTextPersian WebSiteEditTextUserExtendedInformation = null;
    private EditTextPersian MyFavoriteFoodEditTextUserExtendedInformation = null;
    private EditTextPersian MyFavoriteSweetEditTextUserExtendedInformation = null;
    private EditTextPersian MyFavoriteCityToTravelEditTextUserExtendedInformation = null;
    private EditTextPersian MyFavoriteBrandEditTextUserExtendedInformation = null;
    private EditTextPersian MyFavoriteGiftEditTextUserExtendedInformation = null;
    private EditTextPersian JobOrBusinessEditTextUserExtendedInformation = null;
    private EditTextPersian SchoolEditTextUserExtendedInformation = null;
    private EditTextPersian FootballTeamEditTextUserExtendedInformation = null;
    private EditTextPersian TelegramEditTextUserExtendedInformation = null;
    private EditTextPersian InstagramEditTextUserExtendedInformation = null;

    private Spinner SexTypeSpinnerUserExtendedInformation = null;
    private Spinner DegreeOfEducationSpinnerUserExtendedInformation = null;
    private Spinner SkinColorSpinnerUserExtendedInformation = null;
    private Spinner MyFavoriteColorSpinnerUserExtendedInformation = null;
    private Spinner EyeColorSpinnerUserExtendedInformation = null;
    private Spinner BankIdSpinnerUserExtendedInformation = null;

    private SwipeRefreshLayout SwipeContainerUserExtendedInformation;

    private boolean IsSwipe = false;
    private boolean IsLoadedDataForFirst = false;

    private Integer SexTypeId;
    private Integer EyeColorId;
    private Integer MyFavoriteColorId;
    private Integer SkinColorId;
    private Integer DegreeOfEducationId;
    private Integer BankId;

    private List<SexTypeViewModel> sexTypeListViewModel;
    private List<BankTypeViewModel> bankTypeListViewModels;
    private List<DegreeOfEducationViewModel> degreeOfEducationListViewModels;

    private List<ColorTypeViewModel> colorTypeSkinColorListViewModel = new ArrayList<>();
    private List<ColorTypeViewModel> colorTypeMyFavoriteColorListViewModel = new ArrayList<>();
    private List<ColorTypeViewModel> colorTypeEyeColorListViewModel = new ArrayList<>();

    private String PersianDate = "";
    int Year = 0;
    int Month = 0;
    int Day = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (UserProfileActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_user_extended_information, container, false);
        //طرحبندی ویو
        CreateLayout(CurrentView);

        return CurrentView;
    }

    private void CreateLayout(View CurrentView) {
        SwipeContainerUserExtendedInformation = CurrentView.findViewById(R.id.SwipeContainerUserExtendedInformation);

        EmailEditTextUserExtendedInformation = CurrentView.findViewById(R.id.EmailEditTextUserExtendedInformation);
        HeightEditTextUserExtendedInformation = CurrentView.findViewById(R.id.HeightEditTextUserExtendedInformation);
        WeightEditTextUserExtendedInformation = CurrentView.findViewById(R.id.WeightEditTextUserExtendedInformation);
        AboutMeEditTextUserExtendedInformation = CurrentView.findViewById(R.id.AboutMeEditTextUserExtendedInformation);
        AccountNumberEditTextUserExtendedInformation = CurrentView.findViewById(R.id.AccountNumberEditTextUserExtendedInformation);
        WebSiteEditTextUserExtendedInformation = CurrentView.findViewById(R.id.WebSiteEditTextUserExtendedInformation);
        MyFavoriteFoodEditTextUserExtendedInformation = CurrentView.findViewById(R.id.MyFavoriteFoodEditTextUserExtendedInformation);
        MyFavoriteSweetEditTextUserExtendedInformation = CurrentView.findViewById(R.id.MyFavoriteSweetEditTextUserExtendedInformation);
        MyFavoriteCityToTravelEditTextUserExtendedInformation = CurrentView.findViewById(R.id.MyFavoriteCityToTravelEditTextUserExtendedInformation);
        MyFavoriteBrandEditTextUserExtendedInformation = CurrentView.findViewById(R.id.MyFavoriteBrandEditTextUserExtendedInformation);
        MyFavoriteGiftEditTextUserExtendedInformation = CurrentView.findViewById(R.id.MyFavoriteGiftEditTextUserExtendedInformation);
        JobOrBusinessEditTextUserExtendedInformation = CurrentView.findViewById(R.id.JobOrBusinessEditTextUserExtendedInformation);
        SchoolEditTextUserExtendedInformation = CurrentView.findViewById(R.id.SchoolEditTextUserExtendedInformation);
        FootballTeamEditTextUserExtendedInformation = CurrentView.findViewById(R.id.FootballTeamEditTextUserExtendedInformation);
        TelegramEditTextUserExtendedInformation = CurrentView.findViewById(R.id.TelegramEditTextUserExtendedInformation);
        InstagramEditTextUserExtendedInformation = CurrentView.findViewById(R.id.InstagramEditTextUserExtendedInformation);

        BirthDateTextViewUserExtendedInformation = CurrentView.findViewById(R.id.BirthDateTextViewUserExtendedInformation);
        MarriedDateTextViewUserExtendedInformation = CurrentView.findViewById(R.id.MarriedDateTextViewUserExtendedInformation);
        MotherBirthDayTextViewUserExtendedInformation = CurrentView.findViewById(R.id.MotherBirthDayTextViewUserExtendedInformation);
        FatherBirthDayTextViewUserExtendedInformation = CurrentView.findViewById(R.id.FatherBirthDayTextViewUserExtendedInformation);
        SpouseBirthDayTextViewUserExtendedInformation = CurrentView.findViewById(R.id.SpouseBirthDayTextViewUserExtendedInformation);
        FirstChildBirthDayTextViewUserExtendedInformation = CurrentView.findViewById(R.id.FirstChildBirthDayTextViewUserExtendedInformation);
        SecondChildBirthDayTextViewUserExtendedInformation = CurrentView.findViewById(R.id.SecondChildBirthDayTextViewUserExtendedInformation);

        SexTypeSpinnerUserExtendedInformation = CurrentView.findViewById(R.id.SexTypeSpinnerUserExtendedInformation);
        DegreeOfEducationSpinnerUserExtendedInformation = CurrentView.findViewById(R.id.DegreeOfEducationSpinnerUserExtendedInformation);
        SkinColorSpinnerUserExtendedInformation = CurrentView.findViewById(R.id.SkinColorSpinnerUserExtendedInformation);
        MyFavoriteColorSpinnerUserExtendedInformation = CurrentView.findViewById(R.id.MyFavoriteColorSpinnerUserExtendedInformation);
        EyeColorSpinnerUserExtendedInformation = CurrentView.findViewById(R.id.EyeColorSpinnerUserExtendedInformation);
        BankIdSpinnerUserExtendedInformation = CurrentView.findViewById(R.id.BankIdSpinnerUserExtendedInformation);

        ButtonPersianView RegisterButtonUserExtendedInformation = CurrentView.findViewById(R.id.RegisterButtonUserExtendedInformation);
        FloatingActionButton BottomOfPageFloatingActionButtonUserExtendedInformation = CurrentView.findViewById(R.id.BottomOfPageFloatingActionButtonUserExtendedInformation);
        final ScrollView ScrollViewUserExtendedInformation = CurrentView.findViewById(R.id.ScrollViewUserExtendedInformation);

        BirthDateTextViewUserExtendedInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calender(BirthDateTextViewUserExtendedInformation);
            }
        });
        MarriedDateTextViewUserExtendedInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calender(MarriedDateTextViewUserExtendedInformation);
            }
        });
        MotherBirthDayTextViewUserExtendedInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calender(MotherBirthDayTextViewUserExtendedInformation);
            }
        });
        FatherBirthDayTextViewUserExtendedInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calender(FatherBirthDayTextViewUserExtendedInformation);
            }
        });
        SpouseBirthDayTextViewUserExtendedInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calender(SpouseBirthDayTextViewUserExtendedInformation);
            }
        });
        FirstChildBirthDayTextViewUserExtendedInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calender(FirstChildBirthDayTextViewUserExtendedInformation);
            }
        });
        SecondChildBirthDayTextViewUserExtendedInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calender(SecondChildBirthDayTextViewUserExtendedInformation);
            }
        });

        SwipeContainerUserExtendedInformation.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                IsSwipe = true;
                LoadData();

                sexTypeListViewModel = new ArrayList<>();
                degreeOfEducationListViewModels = new ArrayList<>();
                colorTypeSkinColorListViewModel = new ArrayList<>();
                colorTypeMyFavoriteColorListViewModel = new ArrayList<>();
                bankTypeListViewModels = new ArrayList<>();
                colorTypeEyeColorListViewModel = new ArrayList<>();
            }
        });

        SexTypeSpinnerUserExtendedInformation.setOnItemSelectedListener(this);
        DegreeOfEducationSpinnerUserExtendedInformation.setOnItemSelectedListener(this);
        SkinColorSpinnerUserExtendedInformation.setOnItemSelectedListener(this);
        MyFavoriteColorSpinnerUserExtendedInformation.setOnItemSelectedListener(this);
        EyeColorSpinnerUserExtendedInformation.setOnItemSelectedListener(this);
        BankIdSpinnerUserExtendedInformation.setOnItemSelectedListener(this);

        RegisterButtonUserExtendedInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnSetUserExtendedInformationButtonClick();
            }
        });

        BottomOfPageFloatingActionButtonUserExtendedInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScrollViewUserExtendedInformation.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    @Override
    public void LoadData() {
        if (!IsSwipe)
            Context.ShowLoadingProgressBar();

        Context.setRetryType(2);
        UserInfoService userInfoService = new UserInfoService(this);
        userInfoService.Get();

    }

    private void OnSetUserExtendedInformationButtonClick() {
        if (Utility.ValidateView(EmailEditTextUserExtendedInformation, HeightEditTextUserExtendedInformation, WeightEditTextUserExtendedInformation,
                AboutMeEditTextUserExtendedInformation, AccountNumberEditTextUserExtendedInformation, WebSiteEditTextUserExtendedInformation,
                MyFavoriteFoodEditTextUserExtendedInformation, MyFavoriteSweetEditTextUserExtendedInformation, MyFavoriteCityToTravelEditTextUserExtendedInformation,
                MyFavoriteBrandEditTextUserExtendedInformation, MyFavoriteGiftEditTextUserExtendedInformation, JobOrBusinessEditTextUserExtendedInformation,
                SchoolEditTextUserExtendedInformation, FootballTeamEditTextUserExtendedInformation, TelegramEditTextUserExtendedInformation, InstagramEditTextUserExtendedInformation,
                BirthDateTextViewUserExtendedInformation, MarriedDateTextViewUserExtendedInformation, MotherBirthDayTextViewUserExtendedInformation, FatherBirthDayTextViewUserExtendedInformation,
                SpouseBirthDayTextViewUserExtendedInformation, FirstChildBirthDayTextViewUserExtendedInformation, SecondChildBirthDayTextViewUserExtendedInformation)) {
            Context.ShowLoadingProgressBar();
            //ثبت اطلاعات
            Context.setRetryType(1);
            UserInfoService Service = new UserInfoService(this);
            Service.Set(MadeViewModel());

        }
    }

    private UserInfoViewModel MadeViewModel() {

        AccountViewModel accountViewModel = Context.getAccountRepository().getAccount();

        UserInfoViewModel ViewModel = new UserInfoViewModel();
        ViewModel.setUserId(accountViewModel.getUser().getId());
        ViewModel.setEmail(EmailEditTextUserExtendedInformation.getText().toString());
        ViewModel.setSexTypeId(SexTypeId);
        ViewModel.setEyeColorId(EyeColorId);
        ViewModel.setMyFavoriteColorId(MyFavoriteColorId);
        ViewModel.setSkinColorId(SkinColorId);
        if (HeightEditTextUserExtendedInformation.getText() != null && Utility.isNumeric(HeightEditTextUserExtendedInformation.getText().toString().trim()))
            ViewModel.setHeight(Double.parseDouble(HeightEditTextUserExtendedInformation.getText().toString().trim()));
        else
            ViewModel.setHeight(0);

        if (WeightEditTextUserExtendedInformation.getText() != null && Utility.isNumeric(WeightEditTextUserExtendedInformation.getText().toString().trim()))
            ViewModel.setWeight(Double.parseDouble(WeightEditTextUserExtendedInformation.getText().toString()));
        else
            ViewModel.setWeight(0);

        ViewModel.setAboutMe(AboutMeEditTextUserExtendedInformation.getText().toString());
        if (Utility.CheckDate(BirthDateTextViewUserExtendedInformation.getText().toString()))
            if (BirthDateTextViewUserExtendedInformation.getText().toString().contains("/"))
                ViewModel.setBirthDate(BirthDateTextViewUserExtendedInformation.getText().toString());
            else
                ViewModel.setBirthDate("");
        else
            ViewModel.setBirthDate("");
        if (Utility.CheckDate(MarriedDateTextViewUserExtendedInformation.getText().toString()))
            if (MarriedDateTextViewUserExtendedInformation.getText().toString().contains("/"))
                ViewModel.setMarriedDate(MarriedDateTextViewUserExtendedInformation.getText().toString());
            else
                ViewModel.setMarriedDate("");
        else
            ViewModel.setMarriedDate("");

        ViewModel.setAccountNumber(AccountNumberEditTextUserExtendedInformation.getText().toString());
        ViewModel.setBankTypeId(BankId);
        ViewModel.setDegreeOfEducationId(DegreeOfEducationId);
        ViewModel.setWebSite(WebSiteEditTextUserExtendedInformation.getText().toString());
        ViewModel.setMyFavoriteFood(MyFavoriteFoodEditTextUserExtendedInformation.getText().toString());
        ViewModel.setMyFavoriteSweet(MyFavoriteSweetEditTextUserExtendedInformation.getText().toString());
        ViewModel.setMyFavoriteCityToTravel(MyFavoriteCityToTravelEditTextUserExtendedInformation.getText().toString());
        ViewModel.setMyFavoriteBrand(MyFavoriteBrandEditTextUserExtendedInformation.getText().toString());
        ViewModel.setMyFavoriteGift(MyFavoriteGiftEditTextUserExtendedInformation.getText().toString());
        ViewModel.setJob(JobOrBusinessEditTextUserExtendedInformation.getText().toString());
        ViewModel.setSchool(SchoolEditTextUserExtendedInformation.getText().toString());
        if (Utility.CheckDate(MotherBirthDayTextViewUserExtendedInformation.getText().toString()))
            if (MotherBirthDayTextViewUserExtendedInformation.getText().toString().contains("/"))
                ViewModel.setMotherBirthDay(MotherBirthDayTextViewUserExtendedInformation.getText().toString());
            else
                ViewModel.setMotherBirthDay("");
        else
            ViewModel.setMotherBirthDay("");
        if (Utility.CheckDate(FatherBirthDayTextViewUserExtendedInformation.getText().toString()))
            if (FatherBirthDayTextViewUserExtendedInformation.getText().toString().contains("/"))
                ViewModel.setFatherBirthDay(FatherBirthDayTextViewUserExtendedInformation.getText().toString());
            else
                ViewModel.setFatherBirthDay("");
        else
            ViewModel.setFatherBirthDay("");
        if (Utility.CheckDate(SpouseBirthDayTextViewUserExtendedInformation.getText().toString()))
            if (SpouseBirthDayTextViewUserExtendedInformation.getText().toString().contains("/"))
                ViewModel.setSpouseBirthDay(SpouseBirthDayTextViewUserExtendedInformation.getText().toString());
            else
                ViewModel.setSpouseBirthDay("");
        else
            ViewModel.setSpouseBirthDay("");
        if (Utility.CheckDate(FirstChildBirthDayTextViewUserExtendedInformation.getText().toString()))
            if (FirstChildBirthDayTextViewUserExtendedInformation.getText().toString().contains("/"))
                ViewModel.setFirstChildBirthDay(FirstChildBirthDayTextViewUserExtendedInformation.getText().toString());
            else
                ViewModel.setFirstChildBirthDay("");
        else
            ViewModel.setFirstChildBirthDay("");
        if (Utility.CheckDate(SecondChildBirthDayTextViewUserExtendedInformation.getText().toString()))
            if (SecondChildBirthDayTextViewUserExtendedInformation.getText().toString().contains("/"))
                ViewModel.setSecondChildBirthDay(SecondChildBirthDayTextViewUserExtendedInformation.getText().toString());
            else
                ViewModel.setSecondChildBirthDay("");
        else
            ViewModel.setSecondChildBirthDay("");
        ViewModel.setMyFavoriteSoccerTeam(FootballTeamEditTextUserExtendedInformation.getText().toString());
        ViewModel.setTelegramId(TelegramEditTextUserExtendedInformation.getText().toString());
        ViewModel.setInstagramId(InstagramEditTextUserExtendedInformation.getText().toString());

        return ViewModel;
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
        CustomNumberPicker YearNumberPicker = calender.findViewById(R.id.YearNumberPicker);
        CustomNumberPicker MonthNumberPicker = calender.findViewById(R.id.MonthNumberPicker);
        final CustomNumberPicker DayNumberPicker = calender.findViewById(R.id.DayNumberPicker);


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

            int MaxDaysOfMonth = PersianCalendarConverter.GetMaxDaysOfMonth(Year,Month);
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
                int MaxDaysOfMonth = PersianCalendarConverter.GetMaxDaysOfMonth(Year,Month);
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
                int MaxDaysOfMonth = PersianCalendarConverter.GetMaxDaysOfMonth(Year,Month);
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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (adapterView.getId()) {
            case R.id.SexTypeSpinnerUserExtendedInformation: {
                if (i > 0)
                    SexTypeId = sexTypeListViewModel.get(i).getId();
                else
                    SexTypeId = null;

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

            case R.id.DegreeOfEducationSpinnerUserExtendedInformation: {
                if (i > 0)
                    DegreeOfEducationId = degreeOfEducationListViewModels.get(i).getId();
                else
                    DegreeOfEducationId = null;
                ImageView ArrowDropDownImageView = view.findViewById(R.id.ArrowDropDownImageView);
                int Position = (int) ArrowDropDownImageView.getTag();

                if (Position == i) {
                    ArrowDropDownImageView.setVisibility(View.VISIBLE);
                } else {
                    ArrowDropDownImageView.setVisibility(View.GONE);
                }
                break;
            }

            case R.id.SkinColorSpinnerUserExtendedInformation: {
                if (i > 0)
                    SkinColorId = colorTypeSkinColorListViewModel.get(i).getId();
                else
                    SkinColorId = null;
                ImageView ArrowDropDownImageView = view.findViewById(R.id.ArrowDropDownImageView);
                int Position = (int) ArrowDropDownImageView.getTag();

                if (Position == i) {
                    ArrowDropDownImageView.setVisibility(View.VISIBLE);
                } else {
                    ArrowDropDownImageView.setVisibility(View.GONE);
                }
                break;
            }

            case R.id.MyFavoriteColorSpinnerUserExtendedInformation: {
                if (i > 0)
                    MyFavoriteColorId = colorTypeMyFavoriteColorListViewModel.get(i).getId();
                else
                    MyFavoriteColorId = null;
                ImageView ArrowDropDownImageView = view.findViewById(R.id.ArrowDropDownImageView);
                int Position = (int) ArrowDropDownImageView.getTag();

                if (Position == i) {
                    ArrowDropDownImageView.setVisibility(View.VISIBLE);
                } else {
                    ArrowDropDownImageView.setVisibility(View.GONE);
                }
                break;
            }

            case R.id.BankIdSpinnerUserExtendedInformation: {
                if (i > 0)
                    BankId = bankTypeListViewModels.get(i).getId();
                else
                    BankId = null;

                ImageView ArrowDropDownImageView = view.findViewById(R.id.ArrowDropDownImageView);
                int Position = (int) ArrowDropDownImageView.getTag();

                if (Position == i) {
                    ArrowDropDownImageView.setVisibility(View.VISIBLE);
                } else {
                    ArrowDropDownImageView.setVisibility(View.GONE);
                }
                break;
            }

            case R.id.EyeColorSpinnerUserExtendedInformation: {
                if (i > 0)
                    EyeColorId = colorTypeEyeColorListViewModel.get(i).getId();
                else
                    EyeColorId = null;
                ImageView ArrowDropDownImageView = view.findViewById(R.id.ArrowDropDownImageView);
                int Position = (int) ArrowDropDownImageView.getTag();

                if (Position == i) {
                    ArrowDropDownImageView.setVisibility(View.VISIBLE);
                } else {
                    ArrowDropDownImageView.setVisibility(View.GONE);
                }
                break;
            }


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        SwipeContainerUserExtendedInformation.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.UserGetInfo) {
                Feedback<UserInfoWithTypesViewModel> FeedBack = (Feedback<UserInfoWithTypesViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    UserInfoWithTypesViewModel ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        //پر کردن اطلاعات dropdown ها
                        SetInformationToView(ViewModel);

                        UserInfoViewModel userInfoViewModel = ViewModel.getUserInfo();
                        if (userInfoViewModel != null) {
                            //پر کردن ویو با اطلاعات دریافتی
                            SetUserInfoToView(ViewModel.getUserInfo());
                        }
                        //در این حالت داده ای برای نمایش وجود ندارد به همین تمامی اطلاعات کامپوننت های باید پاک شود
                        else {
                            SetUserInfoToView(new UserInfoViewModel());
                        }

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
            } else if (ServiceMethod == ServiceMethodType.UserSetInfo) {
                Feedback<UserInfoViewModel> FeedBack = (Feedback<UserInfoViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.UpdatedSuccessful.getId()) {
                    UserInfoViewModel ViewModel = FeedBack.getValue();

                    if (ViewModel != null) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                        SetUserInfoToView(ViewModel);
                    } else {
                        Context.ShowToast(FeedbackType.InvalidDataFormat.getMessage().replace("{0}", ""), Toast.LENGTH_LONG, MessageType.Warning);
                    }


                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        //EmailEditTextUserExtendedInformation.requestFocus();
                        //Utility.ShowKeyboard(EmailEditTextUserExtendedInformation);
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            }
        } catch (Exception e) {
            Context.ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
    }

    public void SetInformationToView(UserInfoWithTypesViewModel ViewModel) {
        if (ViewModel.getColorTypeList() != null) {
            List<ColorTypeViewModel> colorTypeViewModels = ViewModel.getColorTypeList();
            for (int i = 0; i < colorTypeViewModels.size(); i++) {
                if (colorTypeViewModels.get(i).getType() == ColorType.Eye.GetColorType()) {
                    colorTypeEyeColorListViewModel.add(colorTypeViewModels.get(i));
                } else if (colorTypeViewModels.get(i).getType() == ColorType.Body.GetColorType()) {
                    colorTypeSkinColorListViewModel.add(colorTypeViewModels.get(i));
                } else if (colorTypeViewModels.get(i).getType() == ColorType.Favorite.GetColorType()) {
                    colorTypeMyFavoriteColorListViewModel.add(colorTypeViewModels.get(i));
                }
            }
        } else {
            colorTypeEyeColorListViewModel = new ArrayList<>();
            colorTypeSkinColorListViewModel = new ArrayList<>();
            colorTypeMyFavoriteColorListViewModel = new ArrayList<>();
        }

        ColorTypeViewModel colorTypeViewModel = new ColorTypeViewModel();
        colorTypeViewModel.setId(0);
        colorTypeViewModel.setTitle(getResources().getString(R.string.none));

        colorTypeEyeColorListViewModel.add(0, colorTypeViewModel);
        colorTypeSkinColorListViewModel.add(0, colorTypeViewModel);
        colorTypeMyFavoriteColorListViewModel.add(0, colorTypeViewModel);

        ColorTypeSpinnerAdapter colorTypeSkinSpinnerAdapter = new ColorTypeSpinnerAdapter(Context, colorTypeSkinColorListViewModel);
        SkinColorSpinnerUserExtendedInformation.setAdapter(colorTypeSkinSpinnerAdapter);
        ColorTypeSpinnerAdapter colorTypeMyFavoriteSpinnerAdapter = new ColorTypeSpinnerAdapter(Context, colorTypeMyFavoriteColorListViewModel);
        MyFavoriteColorSpinnerUserExtendedInformation.setAdapter(colorTypeMyFavoriteSpinnerAdapter);
        ColorTypeSpinnerAdapter colorTypeEyeSpinnerAdapter = new ColorTypeSpinnerAdapter(Context, colorTypeEyeColorListViewModel);
        EyeColorSpinnerUserExtendedInformation.setAdapter(colorTypeEyeSpinnerAdapter);


        if (ViewModel.getSexTypeList() != null)
            sexTypeListViewModel = ViewModel.getSexTypeList();
        else
            sexTypeListViewModel = new ArrayList<>();
        SexTypeViewModel sexTypeModel = new SexTypeViewModel();
        sexTypeModel.setId(0);
        sexTypeModel.setTitle(getResources().getString(R.string.none));
        sexTypeListViewModel.add(0, sexTypeModel);
        SexTypeSpinnerAdapter sexTypeSpinnerAdapter = new SexTypeSpinnerAdapter(Context, sexTypeListViewModel);
        SexTypeSpinnerUserExtendedInformation.setAdapter(sexTypeSpinnerAdapter);

        if (ViewModel.getBankTypeList() != null)
            bankTypeListViewModels = ViewModel.getBankTypeList();
        else
            bankTypeListViewModels = new ArrayList<>();
        BankTypeViewModel bankTypeViewModel = new BankTypeViewModel();
        bankTypeViewModel.setId(0);
        bankTypeViewModel.setTitle(getResources().getString(R.string.none));
        bankTypeListViewModels.add(0, bankTypeViewModel);
        BankTypeSpinnerAdapter bankTypeSpinnerAdapter = new BankTypeSpinnerAdapter(Context, bankTypeListViewModels);
        BankIdSpinnerUserExtendedInformation.setAdapter(bankTypeSpinnerAdapter);


        if (ViewModel.getDegreeOfEducationList() != null)
            degreeOfEducationListViewModels = ViewModel.getDegreeOfEducationList();
        else
            degreeOfEducationListViewModels = new ArrayList<>();
        DegreeOfEducationViewModel degreeOfEducationModel = new DegreeOfEducationViewModel();
        degreeOfEducationModel.setId(0);
        degreeOfEducationModel.setTitle(getResources().getString(R.string.none));
        degreeOfEducationListViewModels.add(0, degreeOfEducationModel);
        DegreeOfEducationSpinnerAdapter degreeOfEducationSpinnerAdapter = new DegreeOfEducationSpinnerAdapter(Context, degreeOfEducationListViewModels);
        DegreeOfEducationSpinnerUserExtendedInformation.setAdapter(degreeOfEducationSpinnerAdapter);
    }

    /**
     * پر کردن اطلاعات تکمیلی کاربر
     *
     * @param userInfoViewModel
     */
    private void SetUserInfoToView(UserInfoViewModel userInfoViewModel) {
        EmailEditTextUserExtendedInformation.setText(userInfoViewModel.getEmail() != null ? userInfoViewModel.getEmail() : "");
        HeightEditTextUserExtendedInformation.setText(String.valueOf((int) userInfoViewModel.getHeight()));
        WeightEditTextUserExtendedInformation.setText(String.valueOf((int) userInfoViewModel.getWeight()));
        AboutMeEditTextUserExtendedInformation.setText(userInfoViewModel.getAboutMe() != null ? userInfoViewModel.getAboutMe() : "");
        if (Utility.CheckDateTime(userInfoViewModel.getBirthDate())) {
            String Date = Utility.GetDateFromDateTime(userInfoViewModel.getBirthDate());
            if (!Date.equals(""))
                BirthDateTextViewUserExtendedInformation.setText(Date);
        }
        if (Utility.CheckDateTime(userInfoViewModel.getMarriedDate())) {
            String Date = Utility.GetDateFromDateTime(userInfoViewModel.getMarriedDate());
            if (!Date.equals(""))
                MarriedDateTextViewUserExtendedInformation.setText(Date);
        }
        AccountNumberEditTextUserExtendedInformation.setText(userInfoViewModel.getAccountNumber() != null ? userInfoViewModel.getAccountNumber() : "");
        WebSiteEditTextUserExtendedInformation.setText(userInfoViewModel.getWebSite() != null ? userInfoViewModel.getWebSite() : "");
        MyFavoriteFoodEditTextUserExtendedInformation.setText(userInfoViewModel.getMyFavoriteFood() != null ? userInfoViewModel.getMyFavoriteFood() : "");
        MyFavoriteSweetEditTextUserExtendedInformation.setText(userInfoViewModel.getMyFavoriteSweet() != null ? userInfoViewModel.getMyFavoriteSweet() : "");
        MyFavoriteCityToTravelEditTextUserExtendedInformation.setText(userInfoViewModel.getMyFavoriteCityToTravel() != null ? userInfoViewModel.getMyFavoriteCityToTravel() : "");
        MyFavoriteBrandEditTextUserExtendedInformation.setText(userInfoViewModel.getMyFavoriteBrand() != null ? userInfoViewModel.getMyFavoriteBrand() : "");
        MyFavoriteGiftEditTextUserExtendedInformation.setText(userInfoViewModel.getMyFavoriteGift() != null ? userInfoViewModel.getMyFavoriteGift() : "");
        JobOrBusinessEditTextUserExtendedInformation.setText(userInfoViewModel.getJob() != null ? userInfoViewModel.getJob() : "");
        SchoolEditTextUserExtendedInformation.setText(userInfoViewModel.getSchool() != null ? userInfoViewModel.getSchool() : "");
        FootballTeamEditTextUserExtendedInformation.setText(userInfoViewModel.getMyFavoriteSoccerTeam() != null ? userInfoViewModel.getMyFavoriteSoccerTeam() : "");
        TelegramEditTextUserExtendedInformation.setText(userInfoViewModel.getTelegramId() != null ? userInfoViewModel.getTelegramId() : "");
        InstagramEditTextUserExtendedInformation.setText(userInfoViewModel.getInstagramId() != null ? userInfoViewModel.getInstagramId() : "");

        if (Utility.CheckDateTime(userInfoViewModel.getMotherBirthDay())) {
            String Date = Utility.GetDateFromDateTime(userInfoViewModel.getMotherBirthDay());
            if (!Date.equals(""))
                MotherBirthDayTextViewUserExtendedInformation.setText(Date);
        } else {
            MotherBirthDayTextViewUserExtendedInformation.setText("");
        }
        if (Utility.CheckDateTime(userInfoViewModel.getFatherBirthDay())) {
            String Date = Utility.GetDateFromDateTime(userInfoViewModel.getFatherBirthDay());
            if (!Date.equals(""))
                FatherBirthDayTextViewUserExtendedInformation.setText(Date);
        } else {
            FatherBirthDayTextViewUserExtendedInformation.setText("");
        }
        if (Utility.CheckDateTime(userInfoViewModel.getSpouseBirthDay())) {
            String Date = Utility.GetDateFromDateTime(userInfoViewModel.getSpouseBirthDay());
            if (!Date.equals(""))
                SpouseBirthDayTextViewUserExtendedInformation.setText(Date);
        } else {
            SpouseBirthDayTextViewUserExtendedInformation.setText("");
        }
        if (Utility.CheckDateTime(userInfoViewModel.getFirstChildBirthDay())) {
            String Date = Utility.GetDateFromDateTime(userInfoViewModel.getFirstChildBirthDay());
            if (!Date.equals(""))
                FirstChildBirthDayTextViewUserExtendedInformation.setText(Date);
        } else {
            FirstChildBirthDayTextViewUserExtendedInformation.setText("");
        }
        if (Utility.CheckDateTime(userInfoViewModel.getSecondChildBirthDay())) {
            String Date = Utility.GetDateFromDateTime(userInfoViewModel.getSecondChildBirthDay());
            if (!Date.equals(""))
                SecondChildBirthDayTextViewUserExtendedInformation.setText(Date);
        } else {
            SecondChildBirthDayTextViewUserExtendedInformation.setText("");
        }


        if (userInfoViewModel.getSexTypeId() != null)
            SexTypeSpinnerUserExtendedInformation.setSelection(SetPositionToSpinnerSexType(userInfoViewModel.getSexTypeId(), sexTypeListViewModel));

        if (userInfoViewModel.getSkinColorId() != null)
            SkinColorSpinnerUserExtendedInformation.setSelection(SetPositionToSpinnerColorType(userInfoViewModel.getSkinColorId(), colorTypeSkinColorListViewModel));

        if (userInfoViewModel.getMyFavoriteColorId() != null)
            MyFavoriteColorSpinnerUserExtendedInformation.setSelection(SetPositionToSpinnerColorType(userInfoViewModel.getMyFavoriteColorId(), colorTypeMyFavoriteColorListViewModel));

        if (userInfoViewModel.getEyeColorId() != null)
            EyeColorSpinnerUserExtendedInformation.setSelection(SetPositionToSpinnerColorType(userInfoViewModel.getEyeColorId(), colorTypeEyeColorListViewModel));

        if (userInfoViewModel.getBankTypeId() != null)
            BankIdSpinnerUserExtendedInformation.setSelection(SetPositionToSpinnerBankId(userInfoViewModel.getBankTypeId(), bankTypeListViewModels));

        if (userInfoViewModel.getDegreeOfEducationId() != null)
            DegreeOfEducationSpinnerUserExtendedInformation.setSelection(SetPositionToSpinnerDegreeOfEducation(userInfoViewModel.getDegreeOfEducationId(), degreeOfEducationListViewModels));
    }

    private int SetPositionToSpinnerSexType(int SelectId, List<SexTypeViewModel> ViewModel) {
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

    private int SetPositionToSpinnerColorType(int SelectId, List<ColorTypeViewModel> ViewModel) {
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


    private int SetPositionToSpinnerDegreeOfEducation(int SelectId, List<DegreeOfEducationViewModel> ViewModel) {
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

    private int SetPositionToSpinnerBankId(int SelectId, List<BankTypeViewModel> ViewModel) {
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            //برای فهمیدن کد فرگنت به UserProfilePagerAdapter مراجعه کنید
            Context.setFragmentIndex(3);
            if (!IsLoadedDataForFirst) {
                IsSwipe = false;
                IsLoadedDataForFirst = true;
                //دریافت اطلاعات از سرور
                LoadData();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

}
