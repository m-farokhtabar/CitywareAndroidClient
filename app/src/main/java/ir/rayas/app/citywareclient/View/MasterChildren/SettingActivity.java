package ir.rayas.app.citywareclient.View.MasterChildren;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
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
import ir.rayas.app.citywareclient.Share.Layout.View.EditTextPersian;
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
    private TextViewPersian SelectCategoryNameTextViewSettingActivity = null;
    private SwitchCompat categorySwitchSettingActivity = null;
    private SwitchCompat regionSwitchSettingActivity = null;
    private SwitchCompat SearchLocationSwitchSettingActivity = null;
    private Spinner SearchTypeSpinnerSettingActivity = null;
    private Spinner SearchStateSpinnerSettingActivity = null;
    private EditText GpsRangeEditTextSettingActivity = null;

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

        final ButtonPersianView selectRegionButtonSettingActivity = findViewById(R.id.SelectRegionButtonSettingActivity);
        final ButtonPersianView selectCategoryButtonSettingActivity = findViewById(R.id.SelectCategoryButtonSettingActivity);
        SelectRegionNameTextViewSettingActivity = findViewById(R.id.SelectRegionNameTextViewSettingActivity);
        SelectCategoryNameTextViewSettingActivity = findViewById(R.id.SelectCategoryNameTextViewSettingActivity);
        SearchLocationSwitchSettingActivity = findViewById(R.id.SearchLocationSwitchSettingActivity);
        SearchTypeSpinnerSettingActivity = findViewById(R.id.SearchTypeSpinnerSettingActivity);
        SearchStateSpinnerSettingActivity = findViewById(R.id.SearchStateSpinnerSettingActivity);
        categorySwitchSettingActivity = findViewById(R.id.CategorySwitchSettingActivity);
        regionSwitchSettingActivity = findViewById(R.id.RegionSwitchSettingActivity);
        ButtonPersianView SaveButtonSettingActivity = findViewById(R.id.SaveButtonSettingActivity);
        final LinearLayout GpsRangeLinearLayoutSettingActivity = findViewById(R.id.GpsRangeLinearLayoutSettingActivity);
        GpsRangeEditTextSettingActivity = findViewById(R.id.GpsRangeEditTextSettingActivity);
        SeekBar gpsRangeSeekBarSettingActivity = findViewById(R.id.GpsRangeSeekBarSettingActivity);

        SetInformationToSpinner();
        SearchTypeSpinnerSettingActivity.setOnItemSelectedListener(this);
        SearchStateSpinnerSettingActivity.setOnItemSelectedListener(this);

        selectRegionButtonSettingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SelectRegionIntent = NewIntent(SelectRegionActivity.class);
                startActivity(SelectRegionIntent);
            }
        });

        selectCategoryButtonSettingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SelectBusinessCategoryIntent = NewIntent(SelectBusinessCategoryActivity.class);
                startActivity(SelectBusinessCategoryIntent);
            }
        });

        SearchLocationSwitchSettingActivity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    IsLocationSearch = true;
                    GpsRangeLinearLayoutSettingActivity.setVisibility(View.VISIBLE);
                } else {
                    IsLocationSearch = false;
                    GpsRangeLinearLayoutSettingActivity.setVisibility(View.GONE);
                }
            }
        });


        categorySwitchSettingActivity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectCategoryButtonSettingActivity.setClickable(true);
                    selectCategoryButtonSettingActivity.setEnabled(true);
                } else {
                    selectCategoryButtonSettingActivity.setClickable(false);
                    selectCategoryButtonSettingActivity.setEnabled(false);
                }
            }
        });


        gpsRangeSeekBarSettingActivity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                GpsRangeEditTextSettingActivity.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        regionSwitchSettingActivity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectRegionButtonSettingActivity.setClickable(true);
                    selectRegionButtonSettingActivity.setEnabled(true);
                } else {
                    selectRegionButtonSettingActivity.setClickable(false);
                    selectRegionButtonSettingActivity.setEnabled(false);
                }
            }
        });

        SaveButtonSettingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveUserSetting();
            }
        });

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
            ServiceCall();
        } else {
            if (RegionId != null && RegionId > 0) {
                ServiceCall();
            } else {
                ShowToast(getResources().getString(R.string.please_select_region_or_location), Toast.LENGTH_LONG, MessageType.Warning);
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

                        SetLocalSettingToRepository(ViewModel.getBusinessCategoryId(), ViewModel.getRegionId(), ViewModel.isUseGprsPoint());

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

        if (ViewModel.getRegionId() != null)
            SelectRegionNameTextViewSettingActivity.setText(regionRepository.GetFullName(ViewModel.getRegionId()));

        if (ViewModel.getBusinessCategoryId() != null)
            SelectCategoryNameTextViewSettingActivity.setText(businessCategoryRepository.GetFullName(ViewModel.getBusinessCategoryId()));

        RegionId = ViewModel.getRegionId();
        FirstRegionId = ViewModel.getRegionId();
        CategoryId = ViewModel.getBusinessCategoryId();
        FirstCategoryId = ViewModel.getBusinessCategoryId();
        IsLocationSearch = ViewModel.isUseGprsPoint();
        SearchLocationSwitchSettingActivity.setChecked(IsLocationSearch);
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