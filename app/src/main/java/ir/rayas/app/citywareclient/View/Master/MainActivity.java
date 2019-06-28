package ir.rayas.app.citywareclient.View.Master;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.BusinessPosterInfoBookmarkRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.BusinessPosterInfoRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.IsTopPosterRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.UserAddressDialogRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.Repository.BusinessCategoryRepository;
import ir.rayas.app.citywareclient.Repository.LocalSettingRepository;
import ir.rayas.app.citywareclient.Repository.RegionRepository;
import ir.rayas.app.citywareclient.Service.Home.HomeService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.QueryType;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Helper.Gps;
import ir.rayas.app.citywareclient.Share.Helper.GpsCurrentLocation;
import ir.rayas.app.citywareclient.Share.Helper.IResponseTurnOnGpsDialog;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.View.MasterChildren.SettingActivity;
import ir.rayas.app.citywareclient.ViewModel.Home.BusinessPosterInfoViewModel;
import ir.rayas.app.citywareclient.ViewModel.Setting.LocalUserSettingViewModel;
import ir.rayas.app.citywareclient.ViewModel.Setting.UserSettingViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.UserAddressViewModel;

public class MainActivity extends BaseActivity implements IResponseService, IResponseTurnOnGpsDialog {

    private SwitchCompat CategoryNameSwitchMainActivity = null;
    private SwitchCompat RegionNameSwitchMainActivity = null;
    private TextViewPersian CategoryNameTextViewMainActivity = null;
    private TextViewPersian RegionNameTextViewMainActivity = null;
    private RelativeLayout MenuRelativeLayoutMainActivity = null;
    private TextViewPersian StarredTextViewMainActivity = null;
    private TextViewPersian NewestTextViewMainActivity = null;
    private TextViewPersian BookmarkTextViewMainActivity = null;
    private TextViewPersian MostVisitedTextViewMainActivity = null;
    private TextView LineNewestTextViewMainActivity = null;
    private TextView LineStarredTextViewMainActivity = null;
    private TextView LineMostVisitedTextViewMainActivity = null;
    private TextView LineBookmarkTextViewMainActivity = null;
    private RecyclerView businessPosterInfoRecyclerViewMainActivity = null;
    private RecyclerView BusinessPosterInfoBookmarkRecyclerViewMainActivity = null;
    private FrameLayout Line = null;

    private IsTopPosterRecyclerViewAdapter isTopPosterRecyclerViewAdapter = null;
    private BusinessPosterInfoRecyclerViewAdapter businessPosterInfoRecyclerViewAdapter = null;
    private BusinessPosterInfoBookmarkRecyclerViewAdapter businessPosterInfoBookmarkRecyclerViewAdapter = null;


    private RegionRepository regionRepository = new RegionRepository();
    private BusinessCategoryRepository businessCategoryRepository = new BusinessCategoryRepository();

    private UserSettingViewModel userSettingViewModel = null;
    private List<UserAddressViewModel> userAddressViewModels = null;

    private int queryType = 0;
    private Integer RegionId = null;
    private Integer BusinessCategoryId = null;
    private Integer GpsRangeInKm = null;
    private Double latitude = null;
    private Double longitude = null;

    private int PageNumberPosterTop = 1;
    private int PageNumberPoster = 1;

    private boolean IsFirst = false;
    private boolean IsClickYesGPS = false;
    private boolean IsAddress = false;
    private Gps CurrentGps = null;

    private LocalUserSettingViewModel localUserSettingViewModel = new LocalUserSettingViewModel();
    private LocalSettingRepository localSettingRepository = new LocalSettingRepository();

    private Dialog ShowAddressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //تنظیم گزینه انتخاب شده منو
        setCurrentActivityId(ActivityIdList.MAIN_ACTIVITY);

        IsFirst = true;
        Static.IsRefreshBookmark = true;
        //کلاس کنترل و مدیریت GPS
        CurrentGps = new Gps();

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                LoadData();
            }
        }, 0);


        String ArrayAddressString = getIntent().getExtras().getString("ArrayAddressString");
        Type listType = new TypeToken<List<UserAddressViewModel>>() {
        }.getType();
        userAddressViewModels = new Gson().fromJson(ArrayAddressString, listType);

        //ایجاد طرحبندی صفحه
        CreateLayout();

        //گرفتن تنظیمات کاربر از حافظه
        GetSetting();

        if (!userSettingViewModel.isUseGprsPoint())
            LoadData();

    }

    private void CreateLayout() {

        //Hide And Show Menu Setting Start --------------------------------------------------------------------------
        FloatingActionButton MainMenuBottomCategoryAndRegionFloatingActionButtonMainActivity = findViewById(R.id.MainMenuBottomCategoryAndRegionFloatingActionButtonMainActivity);
        MenuRelativeLayoutMainActivity = findViewById(R.id.MenuRelativeLayoutMainActivity);
        CategoryNameSwitchMainActivity = findViewById(R.id.CategoryNameSwitchMainActivity);
        CategoryNameTextViewMainActivity = findViewById(R.id.CategoryNameTextViewMainActivity);
        RegionNameTextViewMainActivity = findViewById(R.id.RegionNameTextViewMainActivity);
        RegionNameSwitchMainActivity = findViewById(R.id.RegionNameSwitchMainActivity);

        MenuRelativeLayoutMainActivity.setVisibility(View.GONE);

        MainMenuBottomCategoryAndRegionFloatingActionButtonMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IsFirst = false;
                if (MenuRelativeLayoutMainActivity.getVisibility() == View.GONE) {
                    MenuRelativeLayoutMainActivity.setVisibility(View.VISIBLE);
                } else {
                    MenuRelativeLayoutMainActivity.setVisibility(View.GONE);
                }
            }
        });

        MenuRelativeLayoutMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuRelativeLayoutMainActivity.setVisibility(View.GONE);
            }
        });

        CategoryNameSwitchMainActivity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                PageNumberPosterTop = 1;
                PageNumberPoster = 1;

                if (isChecked) {
                    if (userSettingViewModel.getBusinessCategoryId() == null || userSettingViewModel.getBusinessCategoryId() == 0)
                        BusinessCategoryId = null;
                    else
                        BusinessCategoryId = userSettingViewModel.getBusinessCategoryId();

                } else {
                    BusinessCategoryId = null;
                }

                businessPosterInfoRecyclerViewAdapter.SetViewModelList(new ArrayList<BusinessPosterInfoViewModel>());
                isTopPosterRecyclerViewAdapter.SetViewModelList(new ArrayList<BusinessPosterInfoViewModel>());

                if (!IsFirst) {
                    SetLocalSettingToRepository(BusinessCategoryId, RegionId);
                    LoadData();
                    MenuRelativeLayoutMainActivity.setVisibility(View.GONE);
                }
            }
        });

        RegionNameSwitchMainActivity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                PageNumberPosterTop = 1;
                PageNumberPoster = 1;

                if (isChecked) {
                    if (userSettingViewModel.isUseGprsPoint()) {
                        RegionId = null;
                    } else {
                        if (userSettingViewModel.getRegionId() == null || userSettingViewModel.getRegionId() == 0) {
                            RegionId = null;
                        } else {
                            RegionId = userSettingViewModel.getRegionId();
                        }
                    }
                } else {
                    RegionId = null;
                }

                businessPosterInfoRecyclerViewAdapter.SetViewModelList(new ArrayList<BusinessPosterInfoViewModel>());
                isTopPosterRecyclerViewAdapter.SetViewModelList(new ArrayList<BusinessPosterInfoViewModel>());

                if (!IsFirst) {
                    SetLocalSettingToRepository(BusinessCategoryId, RegionId);
                    LoadData();
                }
            }
        });

        RegionNameTextViewMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (RegionNameSwitchMainActivity.isChecked()) {
                    if (userSettingViewModel.isUseGprsPoint()) {

                        if (IsAddress) {
                            //if off GPS
                            // انتخاب آدرس کاربر
                            ShowDialogAddress();
                        } else {
                            //if on GPS
                            // نمایش دیالوگ مربوط  به انتخاب کیلومتر
                            ShowGpsRangeInKm();
                        }
                    } else {
                        Intent SettingIntent = NewIntent(SettingActivity.class);
                        startActivity(SettingIntent);
                    }
                }
            }
        });

        CategoryNameTextViewMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CategoryNameSwitchMainActivity.isChecked()) {
                    Intent SettingIntent = NewIntent(SettingActivity.class);
                    startActivity(SettingIntent);

                }
            }
        });

        //End (Hide And Show Menu Setting) --------------------------------------------------------------------------

        queryType = QueryType.New.GetQueryType();

        //Tab Top Start----------------------------------------------------------------------------------------------
        LinearLayoutCompat bookmarkLinearLayoutMainActivity = findViewById(R.id.BookmarkLinearLayoutMainActivity);
        LinearLayoutCompat mostVisitedLinearLayoutMainActivity = findViewById(R.id.MostVisitedLinearLayoutMainActivity);
        LinearLayoutCompat starredLinearLayoutMainActivity = findViewById(R.id.StarredLinearLayoutMainActivity);
        LinearLayoutCompat newestLinearLayoutMainActivity = findViewById(R.id.NewestLinearLayoutMainActivity);
        MostVisitedTextViewMainActivity = findViewById(R.id.MostVisitedTextViewMainActivity);
        StarredTextViewMainActivity = findViewById(R.id.StarredTextViewMainActivity);
        NewestTextViewMainActivity = findViewById(R.id.NewestTextViewMainActivity);
        BookmarkTextViewMainActivity = findViewById(R.id.BookmarkTextViewMainActivity);
        LineNewestTextViewMainActivity = findViewById(R.id.LineNewestTextViewMainActivity);
        LineStarredTextViewMainActivity = findViewById(R.id.LineStarredTextViewMainActivity);
        LineMostVisitedTextViewMainActivity = findViewById(R.id.LineMostVisitedTextViewMainActivity);
        LineBookmarkTextViewMainActivity = findViewById(R.id.LineBookmarkTextViewMainActivity);

        newestLinearLayoutMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewestPoster();
            }
        });

        mostVisitedLinearLayoutMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MostVisitedPoster();
            }
        });

        starredLinearLayoutMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StarredPoster();
            }
        });

        bookmarkLinearLayoutMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookmarkPoster();
            }
        });
        //End (Top Tab)------------------------------------------------------------------------------------------------


        // Recycler View Poster Top, Poster, Bookmark Start------------------------------------------------------------------
        Line = findViewById(R.id.Line);

        RecyclerView isTopPosterRecyclerViewMainActivity = findViewById(R.id.IsTopPosterRecyclerViewMainActivity);
        isTopPosterRecyclerViewMainActivity.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, true));
        isTopPosterRecyclerViewMainActivity.setHasFixedSize(true);
        isTopPosterRecyclerViewMainActivity.setNestedScrollingEnabled(false);
        isTopPosterRecyclerViewAdapter = new IsTopPosterRecyclerViewAdapter(MainActivity.this, null, isTopPosterRecyclerViewMainActivity);
        isTopPosterRecyclerViewMainActivity.setAdapter(isTopPosterRecyclerViewAdapter);


        businessPosterInfoRecyclerViewMainActivity = findViewById(R.id.BusinessPosterInfoRecyclerViewMainActivity);
        businessPosterInfoRecyclerViewMainActivity.setLayoutManager(new LinearLayoutManager(this));
        businessPosterInfoRecyclerViewMainActivity.setHasFixedSize(true);
        businessPosterInfoRecyclerViewMainActivity.setNestedScrollingEnabled(false);
        businessPosterInfoRecyclerViewAdapter = new BusinessPosterInfoRecyclerViewAdapter(MainActivity.this, null, businessPosterInfoRecyclerViewMainActivity);
        businessPosterInfoRecyclerViewMainActivity.setAdapter(businessPosterInfoRecyclerViewAdapter);


        BusinessPosterInfoBookmarkRecyclerViewMainActivity = findViewById(R.id.BusinessPosterInfoBookmarkRecyclerViewMainActivity);
        BusinessPosterInfoBookmarkRecyclerViewMainActivity.setLayoutManager(new LinearLayoutManager(this));
        BusinessPosterInfoBookmarkRecyclerViewMainActivity.setHasFixedSize(true);
        BusinessPosterInfoBookmarkRecyclerViewMainActivity.setNestedScrollingEnabled(false);
        businessPosterInfoBookmarkRecyclerViewAdapter = new BusinessPosterInfoBookmarkRecyclerViewAdapter(MainActivity.this, null, BusinessPosterInfoBookmarkRecyclerViewMainActivity);
        BusinessPosterInfoBookmarkRecyclerViewMainActivity.setAdapter(businessPosterInfoBookmarkRecyclerViewAdapter);

        //End (Recycler View Poster Top, Poster, Bookmark)-----------------------------------------------------------------------------


    }


    // اولین بار که صفحه لود میشود و تمام سرویس ها فراخوانی می شود
    private void LoadData() {

        ShowLoadingProgressBar();

        if (userSettingViewModel.isUseGprsPoint())
            if (IsClickYesGPS)
                GetMyLocation();

        HomeService homeService = new HomeService(MainActivity.this);
        homeService.GetAllTop(QueryType.Top.GetQueryType(), BusinessCategoryId, RegionId, GpsRangeInKm, latitude, longitude, PageNumberPosterTop);

        homeService.GetAll(queryType, BusinessCategoryId, RegionId, GpsRangeInKm, latitude, longitude, PageNumberPoster);

    }

    private void LoadDataTop() {

        HomeService homeService = new HomeService(MainActivity.this);
        homeService.GetAllTop(QueryType.Top.GetQueryType(), BusinessCategoryId, RegionId, GpsRangeInKm, latitude, longitude, PageNumberPosterTop);
    }

    private void LoadDataInfo() {

        HomeService homeService = new HomeService(MainActivity.this);
        homeService.GetAll(queryType, BusinessCategoryId, RegionId, GpsRangeInKm, latitude, longitude, PageNumberPoster);
    }

    //سرویس مربوط به لیست علاقه مندی ها
    private void LoadDataBookmarkPoster() {
        HomeService homeService = new HomeService(MainActivity.this);
        homeService.GetAllBookmark(PageNumberPoster);
    }


    private void GetSetting() {

        AccountRepository ARepository = new AccountRepository(null);
        AccountViewModel AccountViewModel = ARepository.getAccount();

        userSettingViewModel = AccountViewModel.getUserSetting();
        SetLocalSettingToRepository(userSettingViewModel.getBusinessCategoryId(), userSettingViewModel.getRegionId());

        setInformationSettingToView();
    }

    private void GetLocalSetting() {

        localUserSettingViewModel = localSettingRepository.getLocalSetting();
        setInformationSettingToView();
    }


    private void setInformationSettingToView() {

        if (localUserSettingViewModel.getBusinessCategoryId() == null || localUserSettingViewModel.getBusinessCategoryId() == 0) {

            CategoryNameSwitchMainActivity.setChecked(false);

            CategoryNameTextViewMainActivity.setText(getResources().getString(R.string.category_name));
            BusinessCategoryId = null;
        } else {

            CategoryNameSwitchMainActivity.setChecked(true);

            CategoryNameTextViewMainActivity.setText(businessCategoryRepository.GetFullName(localUserSettingViewModel.getBusinessCategoryId()));
            BusinessCategoryId = localUserSettingViewModel.getBusinessCategoryId();
        }


        if (localUserSettingViewModel.isUseGprsPoint()) {

            if (!CurrentGps.IsPermissionEnabled()) {
                CurrentGps.ShowPermissionDialog(this);
            } else {
                if (!CurrentGps.IsEnabled()) {
                    CurrentGps.ShowTurnOnGpsDialog(this, this, R.string.TurnOnLocation);
                } else {
                    GetMyLocation();
                    IsAddress = false;
                    LoadData();
                }
            }
            RegionNameSwitchMainActivity.setChecked(true);

            GpsRangeInKm = 1;
            RegionId = null;
            BusinessCategoryId = null;

        } else {

            IsAddress = false;

            GpsRangeInKm = null;
            latitude = null;
            longitude = null;

            if (localUserSettingViewModel.getRegionId() == null || localUserSettingViewModel.getRegionId() == 0) {

                RegionNameSwitchMainActivity.setChecked(false);

                RegionNameTextViewMainActivity.setText(getResources().getString(R.string.region_name));
                RegionId = null;
            } else {
                RegionNameSwitchMainActivity.setChecked(true);

                RegionNameTextViewMainActivity.setText(regionRepository.GetFullName(localUserSettingViewModel.getRegionId()));
                RegionId = localUserSettingViewModel.getRegionId();
            }
        }


    }

    private void SetLocalSettingToRepository(Integer businessCategoryId, Integer regionId) {

        localUserSettingViewModel.setBusinessCategoryId(businessCategoryId);
        localUserSettingViewModel.setRegionId(regionId);
        localUserSettingViewModel.setGpsRangeInKm(GpsRangeInKm);
        localUserSettingViewModel.setUseGprsPoint(userSettingViewModel.isUseGprsPoint());
        localUserSettingViewModel.setLatitude(latitude);
        localUserSettingViewModel.setLongitude(longitude);

        localSettingRepository.setLocalSetting(localUserSettingViewModel);
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        HideLoading();
        try {

            if (ServiceMethod == ServiceMethodType.BusinessPosterInfoTopGetAll) {
                HideLoading();
                Feedback<List<BusinessPosterInfoViewModel>> FeedBack = (Feedback<List<BusinessPosterInfoViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<BusinessPosterInfoViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        if (PageNumberPosterTop == 1) {
                            if (ViewModelList.size() < 1) {
                                Line.setVisibility(View.GONE);
                            } else {
                                Line.setVisibility(View.VISIBLE);
                                isTopPosterRecyclerViewAdapter.SetViewModelList(ViewModelList);

                                if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                    PageNumberPosterTop = PageNumberPosterTop + 1;
                                    LoadDataTop();
                                }
                            }

                        } else {
                            Line.setVisibility(View.VISIBLE);
                            isTopPosterRecyclerViewAdapter.AddViewModelList(ViewModelList);

                            if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                PageNumberPosterTop = PageNumberPosterTop + 1;
                                LoadDataTop();
                            }
                        }
                    }
                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    if (PageNumberPosterTop > 1) {
                        Line.setVisibility(View.VISIBLE);
                    } else {
                        Line.setVisibility(View.GONE);
                    }
                } else {
                    Line.setVisibility(View.GONE);
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
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
                        if (PageNumberPoster == 1) {
                            if (ViewModelList.size() > 1) {
                                businessPosterInfoRecyclerViewAdapter.SetViewModelList(ViewModelList);

                                if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                    PageNumberPoster = PageNumberPoster + 1;
                                    LoadDataInfo();
                                }
                            }
                        } else {
                            businessPosterInfoRecyclerViewAdapter.AddViewModelList(ViewModelList);

                            if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                PageNumberPoster = PageNumberPoster + 1;
                                LoadDataInfo();
                            }
                        }
                    }
                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {

                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.BookmarkPosterInfoGetAll) {
                Feedback<List<BusinessPosterInfoViewModel>> FeedBack = (Feedback<List<BusinessPosterInfoViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<BusinessPosterInfoViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        if (PageNumberPoster == 1) {
                            if (ViewModelList.size() > 1) {
                                businessPosterInfoBookmarkRecyclerViewAdapter.SetViewModelList(ViewModelList);

                                if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                    PageNumberPoster = PageNumberPoster + 1;
                                    LoadDataBookmarkPoster();
                                }
                            }
                        } else {
                            businessPosterInfoBookmarkRecyclerViewAdapter.AddViewModelList(ViewModelList);

                            if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                PageNumberPoster = PageNumberPoster + 1;
                                LoadDataBookmarkPoster();
                            }
                        }
                    }
                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {

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

    private void ShowGpsRangeInKm() {

        MenuRelativeLayoutMainActivity.setVisibility(View.GONE);

        final Dialog GpsRangeDialog = new Dialog(MainActivity.this);
        GpsRangeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        GpsRangeDialog.setContentView(R.layout.dialog_gps_range);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/iransanslight.ttf");

        TextViewPersian HeaderColorDialog = GpsRangeDialog.findViewById(R.id.HeaderColorDialog);
        HeaderColorDialog.getLayoutParams().width = LayoutUtility.GetWidthAccordingToScreen(MainActivity.this, 1);

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

                SetLocalSettingToRepository(BusinessCategoryId, RegionId);

                LoadData();

                GpsRangeDialog.dismiss();

            }
        });

        GpsRangeDialog.show();
    }


    private void NewestPoster() {
        businessPosterInfoRecyclerViewMainActivity.setVisibility(View.VISIBLE);
        BusinessPosterInfoBookmarkRecyclerViewMainActivity.setVisibility(View.GONE);

        LineNewestTextViewMainActivity.setBackgroundColor(getResources().getColor(R.color.BackgroundThemeColor));
        LineStarredTextViewMainActivity.setBackgroundColor(getResources().getColor(R.color.BackgroundWhiteColor));
        LineMostVisitedTextViewMainActivity.setBackgroundColor(getResources().getColor(R.color.BackgroundWhiteColor));
        LineBookmarkTextViewMainActivity.setBackgroundColor(getResources().getColor(R.color.BackgroundWhiteColor));

        MostVisitedTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiBlackColor));
        StarredTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiBlackColor));
        BookmarkTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiBlackColor));
        NewestTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));

        queryType = QueryType.New.GetQueryType();

        PageNumberPosterTop = 1;
        PageNumberPoster = 1;

        List<BusinessPosterInfoViewModel> ViewModelList = new ArrayList<>();
        businessPosterInfoRecyclerViewAdapter.SetViewModelList(ViewModelList);
        businessPosterInfoBookmarkRecyclerViewAdapter.SetViewModelList(ViewModelList);
        isTopPosterRecyclerViewAdapter.SetViewModelList(ViewModelList);

        LoadData();
    }

    private void StarredPoster() {
        businessPosterInfoRecyclerViewMainActivity.setVisibility(View.VISIBLE);
        BusinessPosterInfoBookmarkRecyclerViewMainActivity.setVisibility(View.GONE);

        LineNewestTextViewMainActivity.setBackgroundColor(getResources().getColor(R.color.BackgroundWhiteColor));
        LineStarredTextViewMainActivity.setBackgroundColor(getResources().getColor(R.color.BackgroundThemeColor));
        LineMostVisitedTextViewMainActivity.setBackgroundColor(getResources().getColor(R.color.BackgroundWhiteColor));
        LineBookmarkTextViewMainActivity.setBackgroundColor(getResources().getColor(R.color.BackgroundWhiteColor));

        MostVisitedTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiBlackColor));
        StarredTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));
        NewestTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiBlackColor));
        BookmarkTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiBlackColor));

        queryType = QueryType.Star.GetQueryType();

        PageNumberPosterTop = 1;
        PageNumberPoster = 1;

        List<BusinessPosterInfoViewModel> ViewModelList = new ArrayList<>();
        businessPosterInfoRecyclerViewAdapter.SetViewModelList(ViewModelList);
        businessPosterInfoBookmarkRecyclerViewAdapter.SetViewModelList(ViewModelList);
        isTopPosterRecyclerViewAdapter.SetViewModelList(ViewModelList);

        LoadData();
    }

    private void MostVisitedPoster() {
        businessPosterInfoRecyclerViewMainActivity.setVisibility(View.VISIBLE);
        BusinessPosterInfoBookmarkRecyclerViewMainActivity.setVisibility(View.GONE);

        MostVisitedTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));
        StarredTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiBlackColor));
        NewestTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiBlackColor));
        BookmarkTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiBlackColor));

        LineNewestTextViewMainActivity.setBackgroundColor(getResources().getColor(R.color.BackgroundWhiteColor));
        LineStarredTextViewMainActivity.setBackgroundColor(getResources().getColor(R.color.BackgroundWhiteColor));
        LineMostVisitedTextViewMainActivity.setBackgroundColor(getResources().getColor(R.color.BackgroundThemeColor));
        LineBookmarkTextViewMainActivity.setBackgroundColor(getResources().getColor(R.color.BackgroundWhiteColor));

        queryType = QueryType.Visit.GetQueryType();

        PageNumberPosterTop = 1;
        PageNumberPoster = 1;

        List<BusinessPosterInfoViewModel> ViewModelList = new ArrayList<>();
        businessPosterInfoRecyclerViewAdapter.SetViewModelList(ViewModelList);
        businessPosterInfoBookmarkRecyclerViewAdapter.SetViewModelList(ViewModelList);
        isTopPosterRecyclerViewAdapter.SetViewModelList(ViewModelList);

        LoadData();
    }

    private void BookmarkPoster() {

        businessPosterInfoRecyclerViewMainActivity.setVisibility(View.GONE);
        BusinessPosterInfoBookmarkRecyclerViewMainActivity.setVisibility(View.VISIBLE);


        LineNewestTextViewMainActivity.setBackgroundColor(getResources().getColor(R.color.BackgroundWhiteColor));
        LineStarredTextViewMainActivity.setBackgroundColor(getResources().getColor(R.color.BackgroundWhiteColor));
        LineMostVisitedTextViewMainActivity.setBackgroundColor(getResources().getColor(R.color.BackgroundWhiteColor));
        LineBookmarkTextViewMainActivity.setBackgroundColor(getResources().getColor(R.color.BackgroundThemeColor));

        BookmarkTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));
        StarredTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiBlackColor));
        NewestTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiBlackColor));
        MostVisitedTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiBlackColor));

        queryType = QueryType.Visit.GetQueryType();

        PageNumberPosterTop = 1;
        PageNumberPoster = 1;

        List<BusinessPosterInfoViewModel> ViewModelList = new ArrayList<>();
        businessPosterInfoRecyclerViewAdapter.SetViewModelList(ViewModelList);
        businessPosterInfoBookmarkRecyclerViewAdapter.SetViewModelList(ViewModelList);

        LoadDataBookmarkPoster();
    }

    @Override
    public void OnDismissTurnOnGpsDialog(boolean IsClickYes) {

        IsClickYesGPS = IsClickYes;

        if (IsClickYes) {
            GpsCurrentLocation gpsCurrentLocation = new GpsCurrentLocation(this);
            latitude = gpsCurrentLocation.getLatitude();
            longitude = gpsCurrentLocation.getLongitude();
            RegionNameTextViewMainActivity.setText(getResources().getString(R.string.km));
            GetMyLocation();

            IsAddress = false;

            LoadData();

        } else {
            RegionNameTextViewMainActivity.setText(getResources().getString(R.string.address));

            IsAddress = true;

            if (userAddressViewModels == null || userAddressViewModels.size() == 0) {
                latitude = null;
                longitude = null;
            } else if (userAddressViewModels.size() > 1) {
                latitude = userAddressViewModels.get(userAddressViewModels.size() - 1).getLatitude();
                longitude = userAddressViewModels.get(userAddressViewModels.size() - 1).getLongitude();
            }

            LoadData();
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
        } else {
            LoadData();
        }
    }

    private void GetMyLocation() {

        RegionNameTextViewMainActivity.setText(getResources().getString(R.string.km));

        GpsCurrentLocation gpsCurrentLocation = new GpsCurrentLocation(this);
        latitude = gpsCurrentLocation.getLatitude();
        longitude = gpsCurrentLocation.getLongitude();
    }


    private void ShowDialogAddress() {

        ShowAddressDialog = new Dialog(MainActivity.this);
        ShowAddressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ShowAddressDialog.setContentView(R.layout.dialog_user_address);

        TextViewPersian HeaderTextView = ShowAddressDialog.findViewById(R.id.HeaderTextView);
        HeaderTextView.getLayoutParams().width = LayoutUtility.GetWidthAccordingToScreen(MainActivity.this, 1);

        RecyclerView UserAddressRecyclerView = ShowAddressDialog.findViewById(R.id.UserAddressRecyclerView);

        UserAddressDialogRecyclerViewAdapter userAddressDialogRecyclerViewAdapter = new UserAddressDialogRecyclerViewAdapter(MainActivity.this, userAddressViewModels);
        LinearLayoutManager BusinessOpenTimeLinearLayoutManager = new LinearLayoutManager(MainActivity.this);
        UserAddressRecyclerView.setLayoutManager(BusinessOpenTimeLinearLayoutManager);
        UserAddressRecyclerView.setAdapter(userAddressDialogRecyclerViewAdapter);

        ShowAddressDialog.show();
    }

    public void GetLatLngAddress(Double Latitude, Double Longitude) {
        MenuRelativeLayoutMainActivity.setVisibility(View.GONE);

        latitude = Latitude;
        longitude = Longitude;

        ShowAddressDialog.dismiss();

        SetLocalSettingToRepository(BusinessCategoryId, RegionId);

        LoadData();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        PageNumberPosterTop = 1;
        PageNumberPoster = 1;
        MenuRelativeLayoutMainActivity.setVisibility(View.GONE);

        GetLocalSetting();

        NewestPoster();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // برای اینکه بفهمیم چه زمانی نیاز به رفرش صفحه داریم
        if (Static.IsRefreshBookmark) {
            PageNumberPosterTop = 1;
            PageNumberPoster = 1;
        }
    }
}
