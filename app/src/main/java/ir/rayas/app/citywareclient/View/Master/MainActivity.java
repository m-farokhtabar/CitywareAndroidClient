package ir.rayas.app.citywareclient.View.Master;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.BusinessPosterInfoRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.IsTopPosterRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.OnLoadMoreListener;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.Service.Home.HomeService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.QueryType;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.Gps;
import ir.rayas.app.citywareclient.Share.Helper.GpsCurrentLocation;
import ir.rayas.app.citywareclient.Share.Helper.IResponseTurnOnGpsDialog;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.View.MasterChildren.SettingActivity;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorItemViewModel;
import ir.rayas.app.citywareclient.ViewModel.Home.BusinessPosterInfoViewModel;
import ir.rayas.app.citywareclient.ViewModel.Setting.UserSettingViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.UserAddressViewModel;

public class MainActivity extends BaseActivity implements IResponseService, IResponseTurnOnGpsDialog {

    private IsTopPosterRecyclerViewAdapter isTopPosterRecyclerViewAdapter = null;
    private BusinessPosterInfoRecyclerViewAdapter businessPosterInfoRecyclerViewAdapter = null;
    private ImageView MostVisitedImageViewMainActivity = null;
    private ImageView StarredImageViewMainActivity = null;
    private ImageView NewestImageViewMainActivity = null;
    private ImageView BookmarkImageViewMainActivity = null;
    private TextViewPersian MostVisitedTextViewMainActivity = null;
    private TextViewPersian StarredTextViewMainActivity = null;
    private TextViewPersian NewestTextViewMainActivity = null;
    private TextViewPersian BookmarkTextViewMainActivity = null;
    private LinearLayoutCompat MostVisitedLinearLayoutMainActivity = null;
    private LinearLayoutCompat StarredLinearLayoutMainActivity = null;
    private LinearLayoutCompat NewestLinearLayoutMainActivity = null;
    private LinearLayoutCompat BookmarkLinearLayoutMainActivity = null;
    private RadioButton RegionRadioButtonMainActivity = null;
    private RadioButton CategoryAllRadioButtonMainActivity = null;
    private RadioButton CategoryRadioButtonMainActivity = null;
    private RadioButton RegionAllRadioButtonMainActivity = null;

    private int PageNumber = 1;
    private int PageNumberPoster = 1;
    private int ClickCategory = 0;
    private int ClickRegion = 0;

    private UserSettingViewModel userSettingViewModel = null;
    private List<UserAddressViewModel> userAddressViewModels = null;
    private Gps CurrentGps = null;

    private int queryType = 0;
    private Integer RegionId = null;
    private Integer BusinessCategoryId = null;
    private Integer GpsRangeInKm = null;
    private Double latitude = null;
    private Double longitude = null;

    private boolean IsFirst = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //تنظیم گزینه انتخاب شده منو
        setCurrentActivityId(ActivityIdList.MAIN_ACTIVITY);

        Static.IsRefreshBookmark = true;
        IsFirst = true;

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                LoadData();
            }
        }, 0);

        //کلاس کنترل و مدیریت GPS
        CurrentGps = new Gps();

        String ArrayAddressString = getIntent().getExtras().getString("ArrayAddressString");
        Type listType = new TypeToken<List<FactorItemViewModel>>() {
        }.getType();
        userAddressViewModels = new Gson().fromJson(ArrayAddressString, listType);


        //ایجاد طرحبندی صفحه
        CreateLayout();

        //گرفتن تنظیمات کاربر از حافظه
        GetSetting();


    }

    // اولین بار که صفحه لود میشود و تمام سرویس ها فراخوانی می شود
    private void LoadData() {
        ShowLoadingProgressBar();

        HomeService homeService = new HomeService(MainActivity.this);
        homeService.GetAllTop(QueryType.Top.GetQueryType(), BusinessCategoryId, RegionId, GpsRangeInKm, latitude, longitude, PageNumberPoster);

        homeService.GetAll(queryType, BusinessCategoryId, RegionId, GpsRangeInKm, latitude, longitude, PageNumberPoster);
    }

    private void LoadDataTop() {
        ShowLoadingProgressBar();

        HomeService homeService = new HomeService(MainActivity.this);
        homeService.GetAllTop(QueryType.Top.GetQueryType(), BusinessCategoryId, RegionId, GpsRangeInKm, latitude, longitude, PageNumberPoster);
    }

    private void LoadDataInfo() {
        ShowLoadingProgressBar();

        HomeService homeService = new HomeService(MainActivity.this);
        homeService.GetAll(queryType, BusinessCategoryId, RegionId, GpsRangeInKm, latitude, longitude, PageNumberPoster);
    }


    //سرویس مربوط به لیست علاقه مندی ها
    private void LoadDataBookmarkPoster() {
        ShowLoadingProgressBar();

        HomeService homeService = new HomeService(MainActivity.this);
        homeService.GetAllBookmark(PageNumberPoster);
    }


    private void CreateLayout() {
        TextView SearchLogo = findViewById(R.id.MainSearchIconTextView);
        SearchLogo.setTypeface(Font.MasterIcon);
        SearchLogo.setText("\uf002");


        RecyclerView isTopPosterRecyclerViewMainActivity = findViewById(R.id.IsTopPosterRecyclerViewMainActivity);
        isTopPosterRecyclerViewMainActivity.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, true));
        isTopPosterRecyclerViewAdapter = new IsTopPosterRecyclerViewAdapter(MainActivity.this, null, isTopPosterRecyclerViewMainActivity, new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                PageNumber = PageNumber + 1;
                LoadDataTop();
            }
        });
        isTopPosterRecyclerViewMainActivity.setAdapter(isTopPosterRecyclerViewAdapter);


        RecyclerView businessPosterInfoRecyclerViewMainActivity = findViewById(R.id.BusinessPosterInfoRecyclerViewMainActivity);
        businessPosterInfoRecyclerViewMainActivity.setLayoutManager(new LinearLayoutManager(this));
        businessPosterInfoRecyclerViewAdapter = new BusinessPosterInfoRecyclerViewAdapter(MainActivity.this, null, businessPosterInfoRecyclerViewMainActivity, new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                PageNumberPoster = PageNumberPoster + 1;
                LoadDataInfo();
            }
        });
        businessPosterInfoRecyclerViewMainActivity.setAdapter(businessPosterInfoRecyclerViewAdapter);

        RadioGroup CategoryToggleRadioGroupMainActivity = findViewById(R.id.CategoryToggleRadioGroupMainActivity);
        RadioGroup RegionToggleRadioGroupMainActivity = findViewById(R.id.RegionToggleRadioGroupMainActivity);
        CategoryAllRadioButtonMainActivity = findViewById(R.id.CategoryAllRadioButtonMainActivity);
        CategoryRadioButtonMainActivity = findViewById(R.id.CategoryRadioButtonMainActivity);
        RegionAllRadioButtonMainActivity = findViewById(R.id.RegionAllRadioButtonMainActivity);
        RegionRadioButtonMainActivity = findViewById(R.id.RegionRadioButtonMainActivity);

        BookmarkLinearLayoutMainActivity = findViewById(R.id.BookmarkLinearLayoutMainActivity);
        MostVisitedLinearLayoutMainActivity = findViewById(R.id.MostVisitedLinearLayoutMainActivity);
        StarredLinearLayoutMainActivity = findViewById(R.id.StarredLinearLayoutMainActivity);
        NewestLinearLayoutMainActivity = findViewById(R.id.NewestLinearLayoutMainActivity);
        BookmarkImageViewMainActivity = findViewById(R.id.BookmarkImageViewMainActivity);
        MostVisitedImageViewMainActivity = findViewById(R.id.MostVisitedImageViewMainActivity);
        StarredImageViewMainActivity = findViewById(R.id.StarredImageViewMainActivity);
        NewestImageViewMainActivity = findViewById(R.id.NewestImageViewMainActivity);
        MostVisitedTextViewMainActivity = findViewById(R.id.MostVisitedTextViewMainActivity);
        StarredTextViewMainActivity = findViewById(R.id.StarredTextViewMainActivity);
        NewestTextViewMainActivity = findViewById(R.id.NewestTextViewMainActivity);
        BookmarkTextViewMainActivity = findViewById(R.id.BookmarkTextViewMainActivity);


        NewestLinearLayoutMainActivity.setBackgroundColor(getResources().getColor(R.color.ButtonBackgroundThemeColor));
        MostVisitedLinearLayoutMainActivity.setBackgroundResource(R.drawable.selector_item_bottom_menu);
        StarredLinearLayoutMainActivity.setBackgroundResource(R.drawable.selector_item_bottom_menu);
        BookmarkLinearLayoutMainActivity.setBackgroundResource(R.drawable.selector_item_bottom_menu);

        queryType = QueryType.New.GetQueryType();


        NewestLinearLayoutMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewestPoster();
            }
        });

        MostVisitedLinearLayoutMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MostVisitedPoster();
            }
        });

        StarredLinearLayoutMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StarredPoster();
            }
        });

        BookmarkLinearLayoutMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookmarkPoster();
            }
        });

        RegionToggleRadioGroupMainActivity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.RegionAllRadioButtonMainActivity) {
                    ClickRegion = 0;
                    RegionId = null;
                } else {
                    if (userSettingViewModel.isUseGprsPoint()) {
                        RegionId = null;
                    } else {
                        if (userSettingViewModel.getRegionId() == null || userSettingViewModel.getRegionId() == 0) {
                            RegionId = null;
                        } else {
                            RegionId = userSettingViewModel.getRegionId();
                        }
                    }

                }
                PageNumber = 1;
                PageNumberPoster = 1;
                if (!IsFirst)
                    LoadData();
            }
        });
        RegionRadioButtonMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickRegion = ClickRegion + 1;
                if (userSettingViewModel.isUseGprsPoint()) {
                    if (ClickRegion > 1) {
                        // نمایش دیالوگ مربوط  به انتخاب کیلومتر
                        ShowGpsRangeInKm();
                    }
                } else {
                    if (ClickRegion > 1) {
                        // انتخاب آدرس کاربر
                    }
                }
            }
        });

        CategoryToggleRadioGroupMainActivity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.CategoryAllRadioButtonMainActivity) {
                    ClickCategory = 0;
                    BusinessCategoryId = null;
                } else {
                    if (userSettingViewModel.getBusinessCategoryId() == null || userSettingViewModel.getBusinessCategoryId() == 0) {
                        BusinessCategoryId = null;
                    } else {
                        BusinessCategoryId = userSettingViewModel.getBusinessCategoryId();
                    }
                }
                PageNumber = 1;
                PageNumberPoster = 1;
                if (!IsFirst)
                    LoadData();
            }
        });
        CategoryRadioButtonMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickCategory = ClickCategory + 1;
                if (ClickCategory > 1) {
                    Intent SettingIntent = NewIntent(SettingActivity.class);
                    startActivity(SettingIntent);
                }
            }
        });

    }

    private void GetSetting() {
        AccountRepository ARepository = new AccountRepository(null);
        AccountViewModel AccountViewModel = ARepository.getAccount();

        userSettingViewModel = AccountViewModel.getUserSetting();

        setInformationSettingToView();
    }

    private void setInformationSettingToView() {
        if (userSettingViewModel.isUseGprsPoint()) {

            if (!CurrentGps.IsPermissionEnabled()) {
                CurrentGps.ShowPermissionDialog(this);
            } else {
                if (!CurrentGps.IsEnabled()) {
                    CurrentGps.ShowTurnOnGpsDialog(this, this, R.string.TurnOnLocation);
                } else {
                    GetMyLocation();
                }
            }

            RegionAllRadioButtonMainActivity.setChecked(false);
            RegionRadioButtonMainActivity.setChecked(true);

            GpsRangeInKm = 1;
            RegionId = null;

        } else {

            GpsRangeInKm = null;
            latitude = null;
            longitude = null;

            if (userSettingViewModel.getRegionId() == null || userSettingViewModel.getRegionId() == 0) {
                RegionAllRadioButtonMainActivity.setChecked(true);
                RegionRadioButtonMainActivity.setChecked(false);
                RegionRadioButtonMainActivity.setText(getResources().getString(R.string.region_name));
                ClickRegion = 0;
                RegionId = null;
            } else {
                RegionAllRadioButtonMainActivity.setChecked(false);
                RegionRadioButtonMainActivity.setChecked(true);
                RegionRadioButtonMainActivity.setText(userSettingViewModel.getRegionName());
                ClickRegion = 1;
                RegionId = userSettingViewModel.getRegionId();
            }
        }

        if (userSettingViewModel.getBusinessCategoryId() == null || userSettingViewModel.getBusinessCategoryId() == 0) {
            CategoryAllRadioButtonMainActivity.setChecked(true);
            CategoryRadioButtonMainActivity.setChecked(false);
            CategoryRadioButtonMainActivity.setText(getResources().getString(R.string.category_name));
            ClickCategory = 0;
            BusinessCategoryId = null;
        } else {
            CategoryAllRadioButtonMainActivity.setChecked(false);
            CategoryRadioButtonMainActivity.setChecked(true);
            CategoryRadioButtonMainActivity.setText(userSettingViewModel.getBusinessCategoryName());
            ClickCategory = 1;
            BusinessCategoryId = userSettingViewModel.getBusinessCategoryId();
        }

    }

    @Override
    public void OnDismissTurnOnGpsDialog(boolean IsClickYes) {
        if (IsClickYes) {
            GpsCurrentLocation gpsCurrentLocation = new GpsCurrentLocation(this);
            latitude = gpsCurrentLocation.getLatitude();
            longitude = gpsCurrentLocation.getLongitude();
            RegionRadioButtonMainActivity.setText(getResources().getString(R.string.km));
            GetMyLocation();

        } else {
            RegionRadioButtonMainActivity.setText(getResources().getString(R.string.address));
            if (userAddressViewModels.size() > 1) {
                latitude = userAddressViewModels.get(userAddressViewModels.size() - 1).getLatitude();
                longitude = userAddressViewModels.get(userAddressViewModels.size() - 1).getLongitude();
            } else {
                latitude = null;
                longitude = null;
            }
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
                CurrentGps.ShowTurnOnGpsDialog(this, this, R.string.TurnOnLocation);
            else
                GetMyLocation();
        }
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        HideLoading();
        IsFirst = false;
        try {

            if (ServiceMethod == ServiceMethodType.BusinessPosterInfoTopGetAll) {
                Feedback<List<BusinessPosterInfoViewModel>> FeedBack = (Feedback<List<BusinessPosterInfoViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<BusinessPosterInfoViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        if (PageNumber == 1)
                            isTopPosterRecyclerViewAdapter.SetViewModelList(ViewModelList);
                        else
                            isTopPosterRecyclerViewAdapter.AddViewModelList(ViewModelList);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        if (!(PageNumber > 1 && FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()))
                            ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.BusinessPosterInfoGetAll) {
                Feedback<List<BusinessPosterInfoViewModel>> FeedBack = (Feedback<List<BusinessPosterInfoViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<BusinessPosterInfoViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        if (PageNumberPoster == 1)
                            businessPosterInfoRecyclerViewAdapter.SetViewModelList(ViewModelList);
                        else
                            businessPosterInfoRecyclerViewAdapter.AddViewModelList(ViewModelList);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        if (!(PageNumberPoster > 1 && FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()))
                            ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.BookmarkPosterInfoGetAll) {
                Feedback<List<BusinessPosterInfoViewModel>> FeedBack = (Feedback<List<BusinessPosterInfoViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    final List<BusinessPosterInfoViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        if (PageNumberPoster == 1)
                            businessPosterInfoRecyclerViewAdapter.SetViewModelList(ViewModelList);
                        else
                            businessPosterInfoRecyclerViewAdapter.AddViewModelList(ViewModelList);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        if (!(PageNumberPoster > 1 && FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()))
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

    private void NewestPoster() {
        NewestLinearLayoutMainActivity.setBackgroundColor(getResources().getColor(R.color.ButtonBackgroundThemeColor));
        MostVisitedLinearLayoutMainActivity.setBackgroundResource(R.drawable.selector_item_bottom_menu);
        StarredLinearLayoutMainActivity.setBackgroundResource(R.drawable.selector_item_bottom_menu);
        BookmarkLinearLayoutMainActivity.setBackgroundResource(R.drawable.selector_item_bottom_menu);

        MostVisitedImageViewMainActivity.setBackgroundResource(R.drawable.ic_visibility_theme_24dp);
        StarredImageViewMainActivity.setBackgroundResource(R.drawable.ic_stars_theme_24dp);
        BookmarkImageViewMainActivity.setBackgroundResource(R.drawable.ic_bookmark_full_24dp);
        NewestImageViewMainActivity.setBackgroundResource(R.drawable.ic_fiber_new_white_24dp);

        MostVisitedTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));
        StarredTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));
        BookmarkTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));
        NewestTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontWhiteColor));

        queryType = QueryType.New.GetQueryType();

        PageNumber = 1;
        PageNumberPoster = 1;


        List<BusinessPosterInfoViewModel> ViewModelList = new ArrayList<>();
        businessPosterInfoRecyclerViewAdapter.SetViewModelList(ViewModelList);
        isTopPosterRecyclerViewAdapter.SetViewModelList(ViewModelList);

        LoadData();
    }

    private void StarredPoster() {
        StarredLinearLayoutMainActivity.setBackgroundColor(getResources().getColor(R.color.ButtonBackgroundThemeColor));
        MostVisitedLinearLayoutMainActivity.setBackgroundResource(R.drawable.selector_item_bottom_menu);
        NewestLinearLayoutMainActivity.setBackgroundResource(R.drawable.selector_item_bottom_menu);
        BookmarkLinearLayoutMainActivity.setBackgroundResource(R.drawable.selector_item_bottom_menu);

        MostVisitedImageViewMainActivity.setBackgroundResource(R.drawable.ic_visibility_theme_24dp);
        StarredImageViewMainActivity.setBackgroundResource(R.drawable.ic_stars_white_24dp);
        NewestImageViewMainActivity.setBackgroundResource(R.drawable.ic_fiber_new_theme_24dp);
        BookmarkImageViewMainActivity.setBackgroundResource(R.drawable.ic_bookmark_full_24dp);

        MostVisitedTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));
        StarredTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontWhiteColor));
        NewestTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));
        BookmarkTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));

        queryType = QueryType.Star.GetQueryType();

        PageNumber = 1;
        PageNumberPoster = 1;

        List<BusinessPosterInfoViewModel> ViewModelList = new ArrayList<>();
        businessPosterInfoRecyclerViewAdapter.SetViewModelList(ViewModelList);
        isTopPosterRecyclerViewAdapter.SetViewModelList(ViewModelList);

        LoadData();
    }

    private void MostVisitedPoster() {
        MostVisitedLinearLayoutMainActivity.setBackgroundColor(getResources().getColor(R.color.ButtonBackgroundThemeColor));
        NewestLinearLayoutMainActivity.setBackgroundResource(R.drawable.selector_item_bottom_menu);
        StarredLinearLayoutMainActivity.setBackgroundResource(R.drawable.selector_item_bottom_menu);
        BookmarkLinearLayoutMainActivity.setBackgroundResource(R.drawable.selector_item_bottom_menu);

        MostVisitedImageViewMainActivity.setBackgroundResource(R.drawable.ic_visibility_white_24dp);
        StarredImageViewMainActivity.setBackgroundResource(R.drawable.ic_stars_theme_24dp);
        NewestImageViewMainActivity.setBackgroundResource(R.drawable.ic_fiber_new_theme_24dp);
        BookmarkImageViewMainActivity.setBackgroundResource(R.drawable.ic_bookmark_full_24dp);

        MostVisitedTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontWhiteColor));
        StarredTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));
        NewestTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));
        BookmarkTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));

        queryType = QueryType.Visit.GetQueryType();

        PageNumber = 1;
        PageNumberPoster = 1;

        List<BusinessPosterInfoViewModel> ViewModelList = new ArrayList<>();
        businessPosterInfoRecyclerViewAdapter.SetViewModelList(ViewModelList);
        isTopPosterRecyclerViewAdapter.SetViewModelList(ViewModelList);

        LoadData();
    }

    private void BookmarkPoster() {

        BookmarkLinearLayoutMainActivity.setBackgroundColor(getResources().getColor(R.color.ButtonBackgroundThemeColor));
        NewestLinearLayoutMainActivity.setBackgroundResource(R.drawable.selector_item_bottom_menu);
        StarredLinearLayoutMainActivity.setBackgroundResource(R.drawable.selector_item_bottom_menu);
        MostVisitedLinearLayoutMainActivity.setBackgroundResource(R.drawable.selector_item_bottom_menu);

        MostVisitedImageViewMainActivity.setBackgroundResource(R.drawable.ic_visibility_theme_24dp);
        StarredImageViewMainActivity.setBackgroundResource(R.drawable.ic_stars_theme_24dp);
        NewestImageViewMainActivity.setBackgroundResource(R.drawable.ic_fiber_new_theme_24dp);
        BookmarkImageViewMainActivity.setBackgroundResource(R.drawable.ic_bookmark_white_24dp);

        BookmarkTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontWhiteColor));
        StarredTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));
        NewestTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));
        MostVisitedTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));

        queryType = QueryType.Visit.GetQueryType();

        PageNumber = 1;
        PageNumberPoster = 1;

        List<BusinessPosterInfoViewModel> ViewModelList = new ArrayList<>();
        businessPosterInfoRecyclerViewAdapter.SetViewModelList(ViewModelList);

        LoadDataBookmarkPoster();
    }

    private void ShowGpsRangeInKm() {

        final Dialog GpsRangeDialog = new Dialog(MainActivity.this);
        GpsRangeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        GpsRangeDialog.setContentView(R.layout.dialog_gps_range);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/iransanslight.ttf");

        ButtonPersianView DialogOkButton = GpsRangeDialog.findViewById(R.id.DialogOkButton);
        final EditText GpsRangeEditText = GpsRangeDialog.findViewById(R.id.GpsRangeEditText);
        SeekBar GpsRangeSeekBar = GpsRangeDialog.findViewById(R.id.GpsRangeSeekBar);
        GpsRangeEditText.setTypeface(typeface);

        GpsRangeEditText.setText("1");

        GpsRangeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                GpsRangeEditText.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        DialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GpsRangeEditText.getText().toString().equals("")) {
                    GpsRangeInKm = 1;
                } else {
                    GpsRangeInKm = Integer.parseInt(GpsRangeEditText.getText().toString());
                }
                GpsRangeDialog.dismiss();

            }
        });

        GpsRangeDialog.show();
    }

    private void GetMyLocation() {

        RegionRadioButtonMainActivity.setText(getResources().getString(R.string.km));

        GpsCurrentLocation gpsCurrentLocation = new GpsCurrentLocation(this);
        latitude = gpsCurrentLocation.getLatitude();
        longitude = gpsCurrentLocation.getLongitude();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        GetSetting();

        List<BusinessPosterInfoViewModel> ViewModelList = new ArrayList<>();
        businessPosterInfoRecyclerViewAdapter.SetViewModelList(ViewModelList);
        isTopPosterRecyclerViewAdapter.SetViewModelList(ViewModelList);

        LoadData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // برای اینکه بفهمیم چه زمانی نیاز به رفرش صفحه داریم
        if (Static.IsRefreshBookmark) {
            PageNumber = 1;
            PageNumberPoster = 1;
        }
        LoadData();
    }
}
