package ir.rayas.app.citywareclient.View.MasterChildren;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.Spinner.DeliveryStateInSearchSpinnerAdapter;
import ir.rayas.app.citywareclient.Adapter.Spinner.SearchTypeSpinnerAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.Repository.BusinessCategoryRepository;
import ir.rayas.app.citywareclient.Repository.LocalSettingRepository;
import ir.rayas.app.citywareclient.Repository.RegionRepository;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Setting.UserSettingService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.View.Share.SelectBusinessCategoryActivity;
import ir.rayas.app.citywareclient.View.Share.SelectRegionActivity;
import ir.rayas.app.citywareclient.ViewModel.Setting.LocalUserSettingViewModel;
import ir.rayas.app.citywareclient.ViewModel.Setting.UserSettingViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;


public class SettingActivity extends BaseActivity implements IResponseService, ILoadData, AdapterView.OnItemSelectedListener {

    private TextViewPersian SelectRegionNameTextViewSettingActivity = null;
    private TextViewPersian AllRegionTextViewSettingActivity = null;
    private TextViewPersian SelectCategoryNameTextViewSettingActivity = null;
    private TextViewPersian CategoryAllTextViewSettingActivity = null;
    private SwitchCompat categorySwitchSettingActivity = null;
    private SwitchCompat regionSwitchSettingActivity = null;
    private SwitchCompat SearchLocationSwitchSettingActivity = null;
    private Spinner SearchTypeSpinnerSettingActivity = null;
    private Spinner SearchStateSpinnerSettingActivity = null;
    private EditText GpsRangeEditTextSettingActivity = null;
    //   private ButtonPersianView selectRegionButtonSettingActivity = null;
    // private ButtonPersianView selectCategoryButtonSettingActivity = null;
    private LinearLayout GpsRangeLinearLayoutSettingActivity = null;
    private SeekBar gpsRangeSeekBarSettingActivity = null;

    private boolean IsLocationSearch = false;
    private Integer RegionId = null;
    private Integer CategoryId = null;
    private Integer FirstRegionId = null;
    private Integer FirstCategoryId = null;


    private List<String> SearchType = new ArrayList<>();
    private List<String> DeliveryStateInSearch = new ArrayList<>();
    private Integer SearchTypePosition;
    private Integer DeliveryStateInSearchPosition;

    private AccountRepository AccountRepository = null;

    private RegionRepository regionRepository = new RegionRepository();
    private BusinessCategoryRepository businessCategoryRepository = new BusinessCategoryRepository();

    private UserSettingViewModel userSettingViewModel = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.SETTING_ACTIVITY);

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                LoadData();
            }
        }, R.string.setting);
        //ایجاد طرح بندی صفحه
        CreateLayout();
        //دریافت اطلاعات از سرور
        LoadData();

        //ایجاد ریپو جهت مدیریت اکانت کاربر
        AccountRepository = new AccountRepository(this);
    }

    private void CreateLayout() {
        AccountRepository ARepository = new AccountRepository(null);
        AccountViewModel AccountViewModel = ARepository.getAccount();
        userSettingViewModel = AccountViewModel.getUserSetting();

        // selectRegionButtonSettingActivity = findViewById(R.id.SelectRegionButtonSettingActivity);
        // selectCategoryButtonSettingActivity = findViewById(R.id.SelectCategoryButtonSettingActivity);
        SelectRegionNameTextViewSettingActivity = findViewById(R.id.SelectRegionNameTextViewSettingActivity);
        SelectCategoryNameTextViewSettingActivity = findViewById(R.id.SelectCategoryNameTextViewSettingActivity);
        SearchLocationSwitchSettingActivity = findViewById(R.id.SearchLocationSwitchSettingActivity);
        SearchTypeSpinnerSettingActivity = findViewById(R.id.SearchTypeSpinnerSettingActivity);
        SearchStateSpinnerSettingActivity = findViewById(R.id.SearchStateSpinnerSettingActivity);
        categorySwitchSettingActivity = findViewById(R.id.CategorySwitchSettingActivity);
        regionSwitchSettingActivity = findViewById(R.id.RegionSwitchSettingActivity);
        ButtonPersianView SaveButtonSettingActivity = findViewById(R.id.SaveButtonSettingActivity);
        GpsRangeLinearLayoutSettingActivity = findViewById(R.id.GpsRangeLinearLayoutSettingActivity);
        GpsRangeEditTextSettingActivity = findViewById(R.id.GpsRangeEditTextSettingActivity);
        AllRegionTextViewSettingActivity = findViewById(R.id.AllRegionTextViewSettingActivity);
        CategoryAllTextViewSettingActivity = findViewById(R.id.CategoryAllTextViewSettingActivity);
        gpsRangeSeekBarSettingActivity = findViewById(R.id.GpsRangeSeekBarSettingActivity);

        AllRegionTextViewSettingActivity.setTextColor(getResources().getColor(R.color.FontBlackColor));
        CategoryAllTextViewSettingActivity.setTextColor(getResources().getColor(R.color.FontBlackColor));


        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/iransanslight.ttf");
        GpsRangeEditTextSettingActivity.setTypeface(typeface);

        SetInformationToSpinner();
        SearchTypeSpinnerSettingActivity.setOnItemSelectedListener(this);
        SearchStateSpinnerSettingActivity.setOnItemSelectedListener(this);

        SelectRegionNameTextViewSettingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SelectRegionIntent = NewIntent(SelectRegionActivity.class);
                startActivity(SelectRegionIntent);
            }
        });

        SelectCategoryNameTextViewSettingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SelectBusinessCategoryIntent = NewIntent(SelectBusinessCategoryActivity.class);
                startActivity(SelectBusinessCategoryIntent);
            }
        });


        gpsRangeSeekBarSettingActivity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int ProgressText = 1;
                if (progress > 1495) {
                    ProgressText = progress;
                } else if (progress < 5) {
                    ProgressText = progress;
                } else if (progress ==0) {
                    ProgressText = 1;
                } else {
                    ProgressText = progress +5;
                }
                GpsRangeEditTextSettingActivity.setText(String.valueOf(ProgressText));
//              SetProgressToEditText(ProgressText);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        GpsRangeEditTextSettingActivity.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String GPSRangeText;
                GPSRangeText = s.toString();

                int GPSRange;
                if (s.length() != 0) {
                    GPSRange = Integer.valueOf(GPSRangeText);
                } else {
                    GPSRange = 1;
                }
//                SetProgressToEditText(GPSRange);
//                gpsRangeSeekBarSettingActivity.setProgress(GPSRange);
            }
        });


        SaveButtonSettingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveUserSetting();
            }
        });

    }

    private void SetProgressToEditText(int Progress){
        GpsRangeEditTextSettingActivity.setText(String.valueOf(Progress));
        gpsRangeSeekBarSettingActivity.setProgress(Progress);
    }

    private void SetInformationToSpinner() {
        SearchType.add(getResources().getString(R.string.search_on_all_items));
        SearchType.add(getResources().getString(R.string.search_on_business));
        SearchType.add(getResources().getString(R.string.search_on_product));

        DeliveryStateInSearch.add(getResources().getString(R.string.search_on_all_items));
        DeliveryStateInSearch.add(getResources().getString(R.string.search_on_business_has_delivery));

        SearchTypeSpinnerAdapter searchTypeSpinnerAdapter = new SearchTypeSpinnerAdapter(this, SearchType);
        SearchTypeSpinnerSettingActivity.setAdapter(searchTypeSpinnerAdapter);

        DeliveryStateInSearchSpinnerAdapter colorTypeMyFavoriteSpinnerAdapter = new DeliveryStateInSearchSpinnerAdapter(this, DeliveryStateInSearch);
        SearchStateSpinnerSettingActivity.setAdapter(colorTypeMyFavoriteSpinnerAdapter);

    }

    @Override
    public void LoadData() {
        ShowLoadingProgressBar();

        UserSettingService userSettingService = new UserSettingService(this);
        userSettingService.Get();
    }

    private void SaveUserSetting() {

        if (SearchLocationSwitchSettingActivity.isChecked()) {
            if (categorySwitchSettingActivity.isChecked()) {
                if (CategoryId != null) {
                    ServiceCall();
                } else {
                    ShowToast(getResources().getString(R.string.please_select_category), Toast.LENGTH_LONG, MessageType.Warning);
                }
            } else {
                ServiceCall();
            }
        } else {
            if (regionSwitchSettingActivity.isChecked()) {
                if (RegionId != null) {
                    if (categorySwitchSettingActivity.isChecked()) {
                        if (CategoryId != null) {
                            ServiceCall();
                        } else {
                            ShowToast(getResources().getString(R.string.please_select_category), Toast.LENGTH_LONG, MessageType.Warning);
                        }
                    } else {
                        ServiceCall();
                    }
                } else {
                    ShowToast(getResources().getString(R.string.please_select_region_or_location), Toast.LENGTH_LONG, MessageType.Warning);
                }
            } else {
                if (categorySwitchSettingActivity.isChecked()) {
                    if (CategoryId != null) {
                        ServiceCall();
                    } else {
                        ShowToast(getResources().getString(R.string.please_select_category), Toast.LENGTH_LONG, MessageType.Warning);
                    }
                } else {
                    ServiceCall();
                }
            }
        }
    }

    private void ServiceCall() {
        ShowLoadingProgressBar();
        //ثبت اطلاعات
        UserSettingService Service = new UserSettingService(this);
        Service.Set(MadeViewModel());
    }

    private UserSettingViewModel MadeViewModel() {

        AccountViewModel accountViewModel = AccountRepository.getAccount();

        UserSettingViewModel ViewModel = new UserSettingViewModel();
        ViewModel.setBusinessCategoryId(CategoryId);
        ViewModel.setUserId(accountViewModel.getUser().getId());
        ViewModel.setRegionId(RegionId);
        ViewModel.setSearchOnData(SearchTypePosition);
        ViewModel.setSearchOnDelivery(DeliveryStateInSearchPosition);
        ViewModel.setUseGprsPoint(IsLocationSearch);
        ViewModel.setGpsRangeInKm(Integer.parseInt(GpsRangeEditTextSettingActivity.getText().toString()));
        ViewModel.setCreate("");
        ViewModel.setModified("");

        return ViewModel;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.SearchTypeSpinnerSettingActivity: {

                SearchTypePosition = i;
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

            case R.id.SearchStateSpinnerSettingActivity: {

                DeliveryStateInSearchPosition = i;
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
        HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.UserSettingGet) {
                Feedback<UserSettingViewModel> FeedBack = (Feedback<UserSettingViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    UserSettingViewModel ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        //پر کردن ویو با اطلاعات دریافتی
                        SetInformationToView(ViewModel);
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
            } else if (ServiceMethod == ServiceMethodType.UserSettingSet) {
                Feedback<UserSettingViewModel> FeedBack = (Feedback<UserSettingViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.UpdatedSuccessful.getId()) {
                    UserSettingViewModel ViewModel = FeedBack.getValue();

                    if (ViewModel != null) {
                        ViewModel.setRegionName(SelectRegionNameTextViewSettingActivity.getText().toString());
                        ViewModel.setBusinessCategoryName(SelectCategoryNameTextViewSettingActivity.getText().toString());

//                        if (ViewModel.isUseGprsPoint()) {
//                            ViewModel.setGpsRangeInKm(Integer.parseInt(GpsRangeEditTextSettingActivity.getText().toString()));
//                        } else {
//                            ViewModel.setGpsRangeInKm(1);
//                        }

                        // SetLocalSettingToRepository(ViewModel.getBusinessCategoryId(), ViewModel.getRegionId(), ViewModel.isUseGprsPoint());

                        //پر کردن ویو با اطلاعات دریافتی
                        SetInformationToView(ViewModel);
                        //  ---------------------- اپدیت اکانت کاربر  ------------------------------
                        AccountViewModel accountViewModel = AccountRepository.getAccount();
                        accountViewModel.setUserSetting(ViewModel);
                        AccountRepository.setAccount(accountViewModel);
                        //----------------------------------------------------------

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
        } catch (Exception e) {
            ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
    }

    @Override
    protected void onGetResult(ActivityResult Result) {
        if (Result.getFromActivityId() == getCurrentActivityId()) {
            switch (Result.getToActivityId()) {
                case ActivityIdList.SELECT_REGION_ACTIVITY:
                    RegionId = (int) Result.getData().get("RegionId");
                    //   String RegionName = (String) Result.getData().get("RegionName");
                    SelectRegionNameTextViewSettingActivity.setText(regionRepository.GetFullName(RegionId));
                    break;
                case ActivityIdList.SELECT_BUSINESS_CATEGORY_ACTIVITY:
                    CategoryId = (int) Result.getData().get("BusinessCategoryId");
                    // String CategoryName = (String) Result.getData().get("BusinessCategoryName");
                    SelectCategoryNameTextViewSettingActivity.setText(businessCategoryRepository.GetFullName(CategoryId));
                    break;
            }
        }
        super.onGetResult(Result);
    }

    private void SetInformationToView(UserSettingViewModel ViewModel) {

        SearchTypePosition = ViewModel.getSearchOnData();
        DeliveryStateInSearchPosition = ViewModel.getSearchOnDelivery();

        SearchTypeSpinnerSettingActivity.setSelection(SearchTypePosition);
        SearchStateSpinnerSettingActivity.setSelection(DeliveryStateInSearchPosition);

        if (ViewModel.getBusinessCategoryId() != null) {
            SelectCategoryNameTextViewSettingActivity.setText(businessCategoryRepository.GetFullName(ViewModel.getBusinessCategoryId()));
            SetCheckCategory(true);
            CategoryAllTextViewSettingActivity.setTextColor(getResources().getColor(R.color.FontSemiBlackColor));
            SelectCategoryNameTextViewSettingActivity.setHintTextColor(getResources().getColor(R.color.FontBlackColor));
        } else {
            CategoryAllTextViewSettingActivity.setTextColor(getResources().getColor(R.color.FontBlackColor));
            SelectCategoryNameTextViewSettingActivity.setHintTextColor(getResources().getColor(R.color.FontSemiBlackColor));
            SetCheckCategory(false);
        }

        RegionId = ViewModel.getRegionId();
        FirstRegionId = ViewModel.getRegionId();
        CategoryId = ViewModel.getBusinessCategoryId();
        FirstCategoryId = ViewModel.getBusinessCategoryId();
        IsLocationSearch = ViewModel.isUseGprsPoint();


        if (userSettingViewModel.getGpsRangeInKm() == null) {
            GpsRangeEditTextSettingActivity.setText(String.valueOf(1));
        } else {
            GpsRangeEditTextSettingActivity.setText(String.valueOf(userSettingViewModel.getGpsRangeInKm()));
        }

        if (userSettingViewModel.getGpsRangeInKm() != null)
            if (userSettingViewModel.getGpsRangeInKm() <= 500)
                gpsRangeSeekBarSettingActivity.setProgress(userSettingViewModel.getGpsRangeInKm());


        SearchLocationSwitchSettingActivity.setChecked(IsLocationSearch);

        if (IsLocationSearch) {
            SelectRegionNameTextViewSettingActivity.setText("");
            SelectRegionNameTextViewSettingActivity.setHintTextColor(getResources().getColor(R.color.FontSemiBlackColor));
            regionSwitchSettingActivity.setChecked(false);
            SetCheckRegion(false);
            GpsRangeLinearLayoutSettingActivity.setVisibility(View.VISIBLE);
            AllRegionTextViewSettingActivity.setTextColor(getResources().getColor(R.color.FontSemiBlackColor));

        } else {
            GpsRangeLinearLayoutSettingActivity.setVisibility(View.GONE);
            SetCheckRegion(true);
            if (RegionId == null || RegionId == 0) {
                regionSwitchSettingActivity.setChecked(false);
                SelectRegionNameTextViewSettingActivity.setText("");
                AllRegionTextViewSettingActivity.setTextColor(getResources().getColor(R.color.FontBlackColor));
                SelectRegionNameTextViewSettingActivity.setHintTextColor(getResources().getColor(R.color.FontSemiBlackColor));
            } else {
                AllRegionTextViewSettingActivity.setTextColor(getResources().getColor(R.color.FontSemiBlackColor));
                SelectRegionNameTextViewSettingActivity.setHintTextColor(getResources().getColor(R.color.FontBlackColor));
                regionSwitchSettingActivity.setChecked(true);
                SelectRegionNameTextViewSettingActivity.setText(regionRepository.GetFullName(ViewModel.getRegionId()));
            }
        }

        SearchLocationSwitchSettingActivity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    AllRegionTextViewSettingActivity.setTextColor(getResources().getColor(R.color.FontSemiBlackColor));
                    SelectRegionNameTextViewSettingActivity.setHintTextColor(getResources().getColor(R.color.FontSemiBlackColor));

                    regionSwitchSettingActivity.setChecked(false);
                    SetCheckRegion(false);

                    IsLocationSearch = true;
                    GpsRangeLinearLayoutSettingActivity.setVisibility(View.VISIBLE);

                } else {
                    IsLocationSearch = false;
                    GpsRangeLinearLayoutSettingActivity.setVisibility(View.GONE);
                    SetCheckRegion(true);

                    if (userSettingViewModel.getRegionId() == null || userSettingViewModel.getRegionId() == 0) {
                        AllRegionTextViewSettingActivity.setTextColor(getResources().getColor(R.color.FontBlackColor));
                        SelectRegionNameTextViewSettingActivity.setHintTextColor(getResources().getColor(R.color.FontSemiBlackColor));
                        regionSwitchSettingActivity.setChecked(false);
                        SelectRegionNameTextViewSettingActivity.setText("");
                    } else {
                        AllRegionTextViewSettingActivity.setTextColor(getResources().getColor(R.color.FontSemiBlackColor));
                        SelectRegionNameTextViewSettingActivity.setHintTextColor(getResources().getColor(R.color.FontBlackColor));
                        regionSwitchSettingActivity.setChecked(true);
                        SelectRegionNameTextViewSettingActivity.setText(regionRepository.GetFullName(userSettingViewModel.getRegionId()));
                    }
                }
            }
        });


        categorySwitchSettingActivity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    SelectCategoryNameTextViewSettingActivity.setClickable(true);
                    SelectCategoryNameTextViewSettingActivity.setEnabled(true);

                    CategoryAllTextViewSettingActivity.setTextColor(getResources().getColor(R.color.FontSemiBlackColor));
                    SelectCategoryNameTextViewSettingActivity.setHintTextColor(getResources().getColor(R.color.FontBlackColor));

                    if (userSettingViewModel.getBusinessCategoryId() == null || userSettingViewModel.getBusinessCategoryId() == 0) {
                        CategoryId = null;
                        SelectCategoryNameTextViewSettingActivity.setText("");
                    } else {
                        CategoryId = userSettingViewModel.getBusinessCategoryId();
                        SelectCategoryNameTextViewSettingActivity.setText(businessCategoryRepository.GetFullName(CategoryId));
                    }

                } else {
                    CategoryAllTextViewSettingActivity.setTextColor(getResources().getColor(R.color.FontBlackColor));
                    SelectCategoryNameTextViewSettingActivity.setHintTextColor(getResources().getColor(R.color.FontSemiBlackColor));
                    SelectCategoryNameTextViewSettingActivity.setClickable(false);
                    SelectCategoryNameTextViewSettingActivity.setEnabled(false);
                    CategoryId = null;
                    SelectCategoryNameTextViewSettingActivity.setText("");
                }
            }
        });

        regionSwitchSettingActivity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) {
                    SelectRegionNameTextViewSettingActivity.setClickable(true);
                    SelectRegionNameTextViewSettingActivity.setEnabled(true);
                    SelectRegionNameTextViewSettingActivity.setHintTextColor(getResources().getColor(R.color.FontBlackColor));
                    AllRegionTextViewSettingActivity.setTextColor(getResources().getColor(R.color.FontSemiBlackColor));

                    if (userSettingViewModel.getRegionId() == null || userSettingViewModel.getRegionId() == 0) {
                        RegionId = null;
                        SelectRegionNameTextViewSettingActivity.setText("");
                    } else {
                        RegionId = userSettingViewModel.getRegionId();
                        SelectRegionNameTextViewSettingActivity.setText(regionRepository.GetFullName(RegionId));
                    }
                } else {
                    AllRegionTextViewSettingActivity.setTextColor(getResources().getColor(R.color.FontBlackColor));
                    SelectRegionNameTextViewSettingActivity.setHintTextColor(getResources().getColor(R.color.FontSemiBlackColor));
                    RegionId = null;
                    SelectRegionNameTextViewSettingActivity.setText("");

                    SelectRegionNameTextViewSettingActivity.setClickable(false);
                    SelectRegionNameTextViewSettingActivity.setEnabled(false);
                }
            }
        });
    }

    private void SetCheckRegion(boolean IsCheck) {

        regionSwitchSettingActivity.setClickable(IsCheck);
        SelectRegionNameTextViewSettingActivity.setClickable(IsCheck);
        SelectRegionNameTextViewSettingActivity.setEnabled(IsCheck);
        regionSwitchSettingActivity.setEnabled(IsCheck);
    }

    private void SetCheckCategory(boolean IsCheck) {
        categorySwitchSettingActivity.setChecked(IsCheck);

        //  categorySwitchSettingActivity.setClickable(IsCheck);
        SelectCategoryNameTextViewSettingActivity.setClickable(IsCheck);
        SelectCategoryNameTextViewSettingActivity.setEnabled(IsCheck);
        //  categorySwitchSettingActivity.setEnabled(IsCheck);
    }

    private void SetLocalSettingToRepository(Integer businessCategoryId, Integer regionId, boolean useGprsPoint) {

        LocalUserSettingViewModel localUserSettingViewModel = new LocalUserSettingViewModel();
        LocalSettingRepository localSettingRepository = new LocalSettingRepository();

        if (FirstCategoryId != businessCategoryId)
            localUserSettingViewModel.setBusinessCategoryId(businessCategoryId);
        else
            localUserSettingViewModel.setBusinessCategoryId(localSettingRepository.getLocalSetting().getBusinessCategoryId());

        if (FirstRegionId != regionId)
            localUserSettingViewModel.setRegionId(regionId);
        else
            localUserSettingViewModel.setRegionId(localSettingRepository.getLocalSetting().getRegionId());


        localUserSettingViewModel.setUseGprsPoint(useGprsPoint);


        localUserSettingViewModel.setGpsRangeInKm(localSettingRepository.getLocalSetting().getGpsRangeInKm());
        localUserSettingViewModel.setLatitude(localSettingRepository.getLocalSetting().getLatitude());
        localUserSettingViewModel.setLongitude(localSettingRepository.getLocalSetting().getLongitude());

        localSettingRepository.setLocalSetting(localUserSettingViewModel);
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