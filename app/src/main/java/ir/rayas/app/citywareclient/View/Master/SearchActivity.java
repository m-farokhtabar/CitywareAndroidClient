package ir.rayas.app.citywareclient.View.Master;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.SearchRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.SearchResultRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.UserAddressDialogSearchRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.Repository.BusinessCategoryRepository;
import ir.rayas.app.citywareclient.Repository.LocalSettingRepository;
import ir.rayas.app.citywareclient.Repository.RegionRepository;
import ir.rayas.app.citywareclient.Service.Home.HomeService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Search.SearchService;
import ir.rayas.app.citywareclient.Service.User.UserAddressService;
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
import ir.rayas.app.citywareclient.Share.Layout.View.EditTextPersian;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.View.Initializer.IntroduceActivity;
import ir.rayas.app.citywareclient.View.MasterChildren.SettingActivity;
import ir.rayas.app.citywareclient.ViewModel.Search.SearchResultViewModel;
import ir.rayas.app.citywareclient.ViewModel.Search.SearchViewModel;
import ir.rayas.app.citywareclient.ViewModel.Home.BusinessPosterInfoViewModel;
import ir.rayas.app.citywareclient.ViewModel.Setting.LocalUserSettingViewModel;
import ir.rayas.app.citywareclient.ViewModel.Setting.UserSettingViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.UserAddressViewModel;

public class SearchActivity extends BaseActivity implements IResponseService, IResponseTurnOnGpsDialog {

    private SearchRecyclerViewAdapter searchRecyclerViewAdapter = null;
    private SearchResultRecyclerViewAdapter searchResultRecyclerViewAdapter = null;
    private SwipeRefreshLayout RefreshSearchSwipeRefreshLayoutSearchActivity = null;

    private RecyclerView SearchRecyclerViewSearchActivity = null;
    private RecyclerView SearchResultRecyclerViewSearchActivity = null;
    private TextViewPersian ShowEmptySearchTextViewSearchActivity = null;

    private SwitchCompat CategoryNameSwitchSearchActivity = null;
    private SwitchCompat RegionNameSwitchSearchActivity = null;
    private TextViewPersian CategoryNameTextViewSearchActivity = null;
    private TextViewPersian RegionNameTextViewSearchActivity = null;
    private RelativeLayout MenuRelativeLayoutSearchActivity = null;

    private int PageNumber = 1;
    private List<BusinessPosterInfoViewModel> businessPosterInfoViewModelList = null;

    private Integer RegionId = null;
    private Integer BusinessCategoryId = null;
    private Integer GpsRangeInKm = null;
    private Double latitude = null;
    private Double longitude = null;

    private boolean IsFirst = false;
    private boolean IsClickYesGPS = false;
    private boolean IsAddress = false;
    private Gps CurrentGps = null;

    private String TextSearch = "";

    private List<UserAddressViewModel> userAddressViewModels = null;

    private LocalUserSettingViewModel localUserSettingViewModel = new LocalUserSettingViewModel();
    private LocalSettingRepository localSettingRepository = new LocalSettingRepository();

    private RegionRepository regionRepository = new RegionRepository();
    private BusinessCategoryRepository businessCategoryRepository = new BusinessCategoryRepository();

    private Dialog ShowAddressDialog;
    private UserSettingViewModel userSettingViewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.SEARCH_ACTIVITY);

        Static.IsRefreshBookmark = true;
        IsFirst = true;
        //کلاس کنترل و مدیریت GPS
        CurrentGps = new Gps();

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                LoadData();
            }
        }, R.string.search);

        String ArrayAddressString = IntroduceActivity.getArrayAddressString();
        Type listType = new TypeToken<List<UserAddressViewModel>>() {
        }.getType();
        userAddressViewModels = new Gson().fromJson(ArrayAddressString, listType);

        //ایجاد طرحبندی صفحه
        CreateLayout();

        //گرفتن تنظیمات کاربر از حافظه
        GetSetting();


        if (!localSettingRepository.getLocalSetting().isUseGprsPoint())
            LoadData();


    }

    private void CreateLayout() {

        AccountRepository ARepository = new AccountRepository(null);
        AccountViewModel AccountViewModel = ARepository.getAccount();

        userSettingViewModel = AccountViewModel.getUserSetting();

        ShowEmptySearchTextViewSearchActivity = findViewById(R.id.ShowEmptySearchTextViewSearchActivity);
        ShowEmptySearchTextViewSearchActivity.setVisibility(View.GONE);


        //Hide And Show Menu Setting Start --------------------------------------------------------------------------
        FloatingActionButton MainMenuBottomCategoryAndRegionFloatingActionButtonSearchActivity = findViewById(R.id.MainMenuBottomCategoryAndRegionFloatingActionButtonSearchActivity);
        MenuRelativeLayoutSearchActivity = findViewById(R.id.MenuRelativeLayoutSearchActivity);
        CategoryNameSwitchSearchActivity = findViewById(R.id.CategoryNameSwitchSearchActivity);
        CategoryNameTextViewSearchActivity = findViewById(R.id.CategoryNameTextViewSearchActivity);
        RegionNameTextViewSearchActivity = findViewById(R.id.RegionNameTextViewSearchActivity);
        RegionNameSwitchSearchActivity = findViewById(R.id.RegionNameSwitchSearchActivity);

        MenuRelativeLayoutSearchActivity.setVisibility(View.GONE);

        MainMenuBottomCategoryAndRegionFloatingActionButtonSearchActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IsFirst = false;
                if (MenuRelativeLayoutSearchActivity.getVisibility() == View.GONE) {
                    MenuRelativeLayoutSearchActivity.setVisibility(View.VISIBLE);
                } else {
                    MenuRelativeLayoutSearchActivity.setVisibility(View.GONE);
                }
            }
        });

        MenuRelativeLayoutSearchActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuRelativeLayoutSearchActivity.setVisibility(View.GONE);
            }
        });

        CategoryNameSwitchSearchActivity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                PageNumber = 1;

                if (isChecked) {
                    if (userSettingViewModel.getBusinessCategoryId() == null || userSettingViewModel.getBusinessCategoryId() == 0)
                        BusinessCategoryId = null;
                    else
                        BusinessCategoryId = userSettingViewModel.getBusinessCategoryId();

                } else {
                    BusinessCategoryId = null;
                }

                searchRecyclerViewAdapter.ClearViewModelList();

                if (!IsFirst) {
                    SetLocalSettingToRepository(BusinessCategoryId, RegionId);
                    LoadData();
                    MenuRelativeLayoutSearchActivity.setVisibility(View.GONE);
                }
            }
        });

        RegionNameSwitchSearchActivity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                PageNumber = 1;

                if (isChecked) {
                    if (userSettingViewModel.isUseGprsPoint()) {
                        RegionId = null;
                        latitude = localUserSettingViewModel.getLatitude();
                        longitude = localUserSettingViewModel.getLongitude();
                    } else {
                        if (userSettingViewModel.getRegionId() == null || userSettingViewModel.getRegionId() == 0) {
                            RegionId = null;
                        } else {
                            RegionId = userSettingViewModel.getRegionId();
                        }
                    }
                } else {
                    RegionId = null;
                    latitude = null;
                    longitude = null;
                }

                searchRecyclerViewAdapter.ClearViewModelList();

                if (!IsFirst) {
                    MenuRelativeLayoutSearchActivity.setVisibility(View.GONE);
                    SetLocalSettingToRepository(BusinessCategoryId, RegionId);
                    LoadData();
                }
            }
        });

        RegionNameTextViewSearchActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (RegionNameSwitchSearchActivity.isChecked()) {
                    if (userSettingViewModel.isUseGprsPoint()) {

                        if (IsAddress) {
                            //if off GPS
                            // انتخاب آدرس کاربر
                            ShowLoadingProgressBar();
                            UserAddressService userAddressService = new UserAddressService(SearchActivity.this);
                            userAddressService.GetAll();
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

        CategoryNameTextViewSearchActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CategoryNameSwitchSearchActivity.isChecked()) {
                    Intent SettingIntent = NewIntent(SettingActivity.class);
                    startActivity(SettingIntent);

                }
            }
        });

        //End (Hide And Show Menu Setting) --------------------------------------------------------------------------


        //Search Start -------------------------------------------------------------------------------------------
        final ImageView SearchImageViewSearchActivity = findViewById(R.id.SearchImageViewSearchActivity);
        final EditTextPersian SearchEditTextSearchActivity = findViewById(R.id.SearchEditTextSearchActivity);
        RefreshSearchSwipeRefreshLayoutSearchActivity = findViewById(R.id.RefreshSearchSwipeRefreshLayoutSearchActivity);

        SearchRecyclerViewSearchActivity = findViewById(R.id.SearchRecyclerViewSearchActivity);
        SearchRecyclerViewSearchActivity.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        searchRecyclerViewAdapter = new SearchRecyclerViewAdapter(SearchActivity.this, null, SearchRecyclerViewSearchActivity);
        SearchRecyclerViewSearchActivity.setAdapter(searchRecyclerViewAdapter);


        SearchResultRecyclerViewSearchActivity = findViewById(R.id.SearchResultRecyclerViewSearchActivity);
        SearchResultRecyclerViewSearchActivity.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        searchResultRecyclerViewAdapter = new SearchResultRecyclerViewAdapter(SearchActivity.this, null, SearchResultRecyclerViewSearchActivity);
        SearchResultRecyclerViewSearchActivity.setAdapter(searchResultRecyclerViewAdapter);


        SearchEditTextSearchActivity.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    RefreshSearchSwipeRefreshLayoutSearchActivity.setRefreshing(true);
                    SearchResultRecyclerViewSearchActivity.setVisibility(View.VISIBLE);
                    SearchRecyclerViewSearchActivity.setVisibility(View.GONE);

                    searchResultRecyclerViewAdapter.ClearViewModelList();

                    TextSearch = s.toString();

                    try {
                        String Temp = URLEncoder.encode(TextSearch, "utf-8");
                        TextSearch = Temp;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    PageNumber = 1;
                    LoadDataSearch();

                } else {
                    RefreshSearchSwipeRefreshLayoutSearchActivity.setRefreshing(false);
                    ShowEmptySearchTextViewSearchActivity.setVisibility(View.GONE);
                    SearchResultRecyclerViewSearchActivity.setVisibility(View.GONE);
                    SearchRecyclerViewSearchActivity.setVisibility(View.VISIBLE);
                }
            }
        });


        SearchImageViewSearchActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String SearchOffer = SearchEditTextSearchActivity.getText().toString();
                if (!SearchOffer.equals("")) {

                    searchResultRecyclerViewAdapter.ClearViewModelList();
                    TextSearch = SearchOffer;

                    try {
                        String Temp = URLEncoder.encode(TextSearch, "utf-8");
                        TextSearch = Temp;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    PageNumber = 1;
                    LoadDataSearch();
                }  else {
                    ShowToast(getResources().getString(R.string.please_enter_word), Toast.LENGTH_LONG, MessageType.Warning);
                }

                HideKeyboard(SearchImageViewSearchActivity);

            }
        });


        RefreshSearchSwipeRefreshLayoutSearchActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PageNumber = 1;
                SearchEditTextSearchActivity.setText("");
                SearchResultRecyclerViewSearchActivity.setVisibility(View.GONE);
                SearchRecyclerViewSearchActivity.setVisibility(View.VISIBLE);

//                HomeService service = new HomeService(SearchActivity.this);
//                service.GetAll(QueryType.Search.GetQueryType(), BusinessCategoryId, RegionId, GpsRangeInKm, latitude, longitude, PageNumber);
            }
        });

        // End (Search) --------------------------------------------------------------------------------------

    }

    public void LoadData() {
//        if (PageNumber == 1)
//            ShowLoadingProgressBar();
//
//        if (localSettingRepository.getLocalSetting().isUseGprsPoint())
//            if (IsClickYesGPS)
//                GetMyLocation();
//
//        HomeService service = new HomeService(this);
      //  service.GetAll(QueryType.Search.GetQueryType(), BusinessCategoryId, RegionId, GpsRangeInKm, latitude, longitude, PageNumber);

    }

    public void LoadDataSearch() {

        SearchService service = new SearchService(this);
        service.GetAll(BusinessCategoryId, RegionId, GpsRangeInKm, latitude, longitude, 0, 0, PageNumber, TextSearch);
    }


    private void GetSetting() {
        if (localSettingRepository.getLocalSetting() == null) {
            SetLocalSettingToRepository(userSettingViewModel.getBusinessCategoryId(), userSettingViewModel.getRegionId());
            setInformationSettingToView();
        } else {
            GetLocalSetting();
        }

    }

    private void GetLocalSetting() {

        localUserSettingViewModel = localSettingRepository.getLocalSetting();
        setInformationSettingToView();
    }

    private void setInformationSettingToView() {

        if (localUserSettingViewModel.getBusinessCategoryId() == null || localUserSettingViewModel.getBusinessCategoryId() == 0) {

            CategoryNameSwitchSearchActivity.setChecked(false);
            BusinessCategoryId = null;

            if (userSettingViewModel.getBusinessCategoryId() == null || userSettingViewModel.getBusinessCategoryId() == 0)
                CategoryNameTextViewSearchActivity.setText(getResources().getString(R.string.category_name));
            else
                CategoryNameTextViewSearchActivity.setText(businessCategoryRepository.GetFullName(userSettingViewModel.getBusinessCategoryId()));

        } else {
            CategoryNameSwitchSearchActivity.setChecked(true);

            CategoryNameTextViewSearchActivity.setText(businessCategoryRepository.GetFullName(localUserSettingViewModel.getBusinessCategoryId()));
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
            RegionNameSwitchSearchActivity.setChecked(true);

            GpsRangeInKm = 1;
            RegionId = null;

        } else {
            IsAddress = false;

            GpsRangeInKm = null;
            latitude = null;
            longitude = null;

            if (localUserSettingViewModel.getRegionId() == null || localUserSettingViewModel.getRegionId() == 0) {

                RegionNameSwitchSearchActivity.setChecked(false);
                RegionId = null;

                if (userSettingViewModel.getRegionId() == null || userSettingViewModel.getRegionId() == 0)
                    RegionNameTextViewSearchActivity.setText(getResources().getString(R.string.region_name));
                else
                    RegionNameTextViewSearchActivity.setText(regionRepository.GetFullName(userSettingViewModel.getRegionId()));

            } else {
                RegionNameSwitchSearchActivity.setChecked(true);

                RegionNameTextViewSearchActivity.setText(regionRepository.GetFullName(localUserSettingViewModel.getRegionId()));
                RegionId = localUserSettingViewModel.getRegionId();
            }
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        RefreshSearchSwipeRefreshLayoutSearchActivity.setRefreshing(false);
        HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.BusinessPosterInfoGetAll) {
                Feedback<List<BusinessPosterInfoViewModel>> FeedBack = (Feedback<List<BusinessPosterInfoViewModel>>) Data;


                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                 //   Static.IsRefreshBookmark = false;

                    List<BusinessPosterInfoViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        if (PageNumber == 1) {
                            if (ViewModelList.size() < 1) {
                                ShowEmptySearchTextViewSearchActivity.setVisibility(View.GONE);
                            } else {
                                ShowEmptySearchTextViewSearchActivity.setVisibility(View.GONE);
                            }
                        } else {
                            ShowEmptySearchTextViewSearchActivity.setVisibility(View.GONE);
                        }

                        if (businessPosterInfoViewModelList == null)
                            businessPosterInfoViewModelList = new ArrayList<>();

                        businessPosterInfoViewModelList.addAll(ViewModelList);

                        List<SearchViewModel> ViewModel = GetListSort(businessPosterInfoViewModelList);

                        for (int i = 0; i < ViewModel.size(); i++) {
                            List<BusinessPosterInfoViewModel> businessPosterInfoViewModels = ViewModel.get(i).getBusinessPosterInfoViewModel();

                            for (int j = 0; j < businessPosterInfoViewModels.size(); j++) {
                                if (businessPosterInfoViewModels.get(j).getBusinessId() > 0) {
                                    businessPosterInfoViewModelList.add(businessPosterInfoViewModels.get(j));
                                }
                            }
                        }
                        searchRecyclerViewAdapter.SetViewModelList(ViewModel);

                        if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                            PageNumber = PageNumber + 1;
                            LoadData();
                        }

                    }
                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    if (PageNumber > 1) {
                        ShowEmptySearchTextViewSearchActivity.setVisibility(View.GONE);
                    } else {
                        ShowEmptySearchTextViewSearchActivity.setVisibility(View.GONE);
                    }
                } else {
                    ShowEmptySearchTextViewSearchActivity.setVisibility(View.GONE);
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.SearchResultGet) {

                Feedback<List<SearchResultViewModel>> FeedBack = (Feedback<List<SearchResultViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    final List<SearchResultViewModel> ViewModel = FeedBack.getValue();

                    SearchResultRecyclerViewSearchActivity.setVisibility(View.VISIBLE);
                    SearchRecyclerViewSearchActivity.setVisibility(View.GONE);

                    if (ViewModel != null) {
                        if (PageNumber == 1) {
                            if (ViewModel.size() > 0) {
                                ShowEmptySearchTextViewSearchActivity.setVisibility(View.GONE);
                                SearchResultRecyclerViewSearchActivity.setVisibility(View.VISIBLE);
                                searchResultRecyclerViewAdapter.SetViewModelList(ViewModel);

                                if (DefaultConstant.PageNumberSize == ViewModel.size()) {
                                    PageNumber = PageNumber + 1;
                                    LoadData();
                                }
                            } else {
                                ShowEmptySearchTextViewSearchActivity.setVisibility(View.VISIBLE);
                                SearchResultRecyclerViewSearchActivity.setVisibility(View.GONE);
                            }

                        } else {
                            ShowEmptySearchTextViewSearchActivity.setVisibility(View.GONE);
                            SearchResultRecyclerViewSearchActivity.setVisibility(View.VISIBLE);

                            searchResultRecyclerViewAdapter.AddViewModelList(ViewModel);

                            if (DefaultConstant.PageNumberSize == ViewModel.size()) {
                                PageNumber = PageNumber + 1;
                                LoadData();
                            }
                        }
                    }

                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    if (PageNumber > 1) {
                        ShowEmptySearchTextViewSearchActivity.setVisibility(View.GONE);
                        SearchResultRecyclerViewSearchActivity.setVisibility(View.VISIBLE);
                    } else {
                        ShowEmptySearchTextViewSearchActivity.setVisibility(View.VISIBLE);
                        SearchResultRecyclerViewSearchActivity.setVisibility(View.GONE);
                    }
                } else {
                    SearchResultRecyclerViewSearchActivity.setVisibility(View.GONE);
                    ShowEmptySearchTextViewSearchActivity.setVisibility(View.GONE);
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                        HideLoading();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.UserGetAllAddress) {
                Feedback<List<UserAddressViewModel>> FeedBack = (Feedback<List<UserAddressViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    List<UserAddressViewModel> ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        //تنظیمات مربوط به recycle آدرس
                        ShowDialogAddress(ViewModel);
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
            HideLoading();
            ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
    }

    private List<SearchViewModel> GetListSort(List<BusinessPosterInfoViewModel> ViewModel) {


        List<BusinessPosterInfoViewModel> businessPosterInfoViewModels = new ArrayList<>();
        List<SearchViewModel> searchViewModelList = new ArrayList<>();

        int SizeOneColumn = 0;
        int SizeBanner = 0;
        int RowOneColumn;
        int RowBanner;
        int AddImage = 0;

        for (int i = 0; i < ViewModel.size(); i++) {
            if (ViewModel.get(i).getCols() == 3) {
                SizeBanner = SizeBanner + 1;
            } else {
                SizeOneColumn = SizeOneColumn + 1;
            }
        }

        if ((SizeOneColumn % 3) == 0) {
            RowOneColumn = SizeOneColumn / 3;
        } else {
            RowOneColumn = (SizeOneColumn / 3) + 1;
        }

        RowBanner = SizeBanner;

        if (RowOneColumn < RowBanner) {
            if ((SizeOneColumn % 3) == 0) {
                AddImage = 0;
            } else {
                AddImage = 3 - (SizeOneColumn % 3);
            }
        }


        boolean IsFirst = true;
        boolean FinishThree = false;
        int Row = 0;
        int RowOne = 0;
        int RowThree = 0;
        int Column = 0;

        while (ViewModel.size() > 0) {
            if (IsFirst) {
                if (SizeBanner > 0) {
                    for (int i = 0; i < ViewModel.size(); i++) {
                        if (ViewModel.get(i).getCols() == 3) {

                            businessPosterInfoViewModels.add(ViewModel.get(i));
                            SearchViewModel searchViewModel = new SearchViewModel();
                            searchViewModel.setBusinessPosterInfoViewModel(businessPosterInfoViewModels);
                            searchViewModel.setCols(3);
                            searchViewModelList.add(searchViewModel);

                            ViewModel.remove(i);
                            businessPosterInfoViewModels = new ArrayList<>();

                            RowOne = 1;
                            Row = 1;
                            IsFirst = false;

                            break;
                        }
                    }
                } else {
                    IsFirst = false;
                }
            } else {
                for (int i = 0; i < ViewModel.size(); i++) {

                    if ((Row + 1) % 2 == 0) {

                        if (!FinishThree) {

                            if (Column == 0) {
                                if (ViewModel.get(i).getCols() == 1) {
                                    Column = Column + 1;
                                    businessPosterInfoViewModels.add(ViewModel.get(i));

                                    ViewModel.remove(i);
                                    break;
                                }
                            } else if ((Column % 3) != 0) {
                                if (ViewModel.get(i).getCols() == 1) {
                                    Column = Column + 1;

                                    businessPosterInfoViewModels.add(ViewModel.get(i));

                                    ViewModel.remove(i);
                                    break;
                                }
                            } else {
                                Row = Row + 1;
                                RowThree = RowThree + 1;
                                Column = 0;


                                SearchViewModel searchViewModel = new SearchViewModel();
                                searchViewModel.setBusinessPosterInfoViewModel(businessPosterInfoViewModels);
                                searchViewModel.setCols(1);
                                searchViewModelList.add(searchViewModel);

                                businessPosterInfoViewModels = new ArrayList<>();

                                break;
                            }

                            if ((3 - Column) == AddImage) {
                                if (RowThree == RowOneColumn - 1) {
                                    Row = Row + 1;
                                    RowThree = RowThree + 1;
                                }
                            }

                            if (AddImage > 0) {
                                if (RowThree == RowOneColumn) {
                                    if ((3 - Column) == AddImage) {
                                        if (AddImage == 1) {
                                            businessPosterInfoViewModels.add(new BusinessPosterInfoViewModel());

                                        } else if (AddImage == 2) {

                                            businessPosterInfoViewModels.add(new BusinessPosterInfoViewModel());
                                            businessPosterInfoViewModels.add(new BusinessPosterInfoViewModel());
                                        }


                                        SearchViewModel searchViewModel = new SearchViewModel();
                                        searchViewModel.setBusinessPosterInfoViewModel(businessPosterInfoViewModels);
                                        searchViewModel.setCols(1);
                                        searchViewModelList.add(searchViewModel);

                                        businessPosterInfoViewModels = new ArrayList<>();


                                        RowThree = RowThree + 1;
                                        FinishThree = true;
                                    }
                                }
                            } else {
                                if (RowThree == RowOneColumn) {
                                    RowThree = RowThree + 1;
                                    FinishThree = true;
                                }
                            }
                        } else {
                            Row = Row + 1;

                        }


                    } else {

                        if (SizeBanner >= RowOne) {
                            if (ViewModel.get(i).getCols() == 3) {

                                businessPosterInfoViewModels.add(ViewModel.get(i));
                                SearchViewModel searchViewModel = new SearchViewModel();
                                searchViewModel.setBusinessPosterInfoViewModel(businessPosterInfoViewModels);
                                searchViewModel.setCols(3);
                                searchViewModelList.add(searchViewModel);

                                ViewModel.remove(i);
                                businessPosterInfoViewModels = new ArrayList<>();


                                RowOne = RowOne + 1;
                                Row = Row + 1;
                                break;
                            }
                        } else {
                            Row = Row + 1;
                            break;
                        }

                    }
                }
            }
        }


        return searchViewModelList;
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

    private void ShowGpsRangeInKm() {

        final Dialog GpsRangeDialog = new Dialog(SearchActivity.this);
        GpsRangeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        GpsRangeDialog.setContentView(R.layout.dialog_gps_range);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/iransanslight.ttf");

        TextViewPersian HeaderColorDialog = GpsRangeDialog.findViewById(R.id.HeaderColorDialog);
        HeaderColorDialog.getLayoutParams().width = LayoutUtility.GetWidthAccordingToScreen(SearchActivity.this, 1);

        ButtonPersianView DialogOkButton = GpsRangeDialog.findViewById(R.id.DialogOkButton);
        final EditText GpsRangeEditText = GpsRangeDialog.findViewById(R.id.GpsRangeEditText);
        SeekBar GpsRangeSeekBar = GpsRangeDialog.findViewById(R.id.GpsRangeSeekBar);
        GpsRangeEditText.setTypeface(typeface);

        GpsRangeEditText.setText(String.valueOf(GpsRangeInKm));

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
                MenuRelativeLayoutSearchActivity.setVisibility(View.GONE);
                LoadData();
                GpsRangeDialog.dismiss();

            }
        });

        GpsRangeDialog.show();
    }

    @Override
    public void OnDismissTurnOnGpsDialog(boolean IsClickYes) {

        IsClickYesGPS = IsClickYes;

        if (IsClickYes) {
            GpsCurrentLocation gpsCurrentLocation = new GpsCurrentLocation(this);
            latitude = gpsCurrentLocation.getLatitude();
            longitude = gpsCurrentLocation.getLongitude();
            RegionNameTextViewSearchActivity.setText(getResources().getString(R.string.by_location_km));
            GetMyLocation();

            IsAddress = false;

            LoadData();

        } else {
            RegionNameTextViewSearchActivity.setText(getResources().getString(R.string.address));

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
            if (!CurrentGps.IsEnabled()) {
                CurrentGps.ShowTurnOnGpsDialog(this, this, R.string.TurnOnLocation);
            } else {
                GetMyLocation();
                LoadData();
            }
        } else {
            LoadData();
        }
    }

    private void GetMyLocation() {

        RegionNameTextViewSearchActivity.setText(getResources().getString(R.string.by_location_km));

        GpsCurrentLocation gpsCurrentLocation = new GpsCurrentLocation(this);
        latitude = gpsCurrentLocation.getLatitude();
        longitude = gpsCurrentLocation.getLongitude();
    }

    private void ShowDialogAddress(List<UserAddressViewModel> ViewModel) {

        ShowAddressDialog = new Dialog(SearchActivity.this);
        ShowAddressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ShowAddressDialog.setContentView(R.layout.dialog_user_address);

        TextViewPersian HeaderTextView = ShowAddressDialog.findViewById(R.id.HeaderTextView);
        TextViewPersian ShowEmptyUserAddressTextView = ShowAddressDialog.findViewById(R.id.ShowEmptyUserAddressTextView);
        HeaderTextView.getLayoutParams().width = LayoutUtility.GetWidthAccordingToScreen(SearchActivity.this, 1);

        RecyclerView UserAddressRecyclerView = ShowAddressDialog.findViewById(R.id.UserAddressRecyclerView);

        if (ViewModel == null || ViewModel.size() == 0) {
            ShowEmptyUserAddressTextView.setVisibility(View.VISIBLE);
        } else {
            ShowEmptyUserAddressTextView.setVisibility(View.GONE);
        }

        UserAddressDialogSearchRecyclerViewAdapter userAddressDialogRecyclerViewAdapter = new UserAddressDialogSearchRecyclerViewAdapter(SearchActivity.this, ViewModel);
        LinearLayoutManager BusinessOpenTimeLinearLayoutManager = new LinearLayoutManager(SearchActivity.this);
        UserAddressRecyclerView.setLayoutManager(BusinessOpenTimeLinearLayoutManager);
        UserAddressRecyclerView.setAdapter(userAddressDialogRecyclerViewAdapter);

        ShowAddressDialog.show();
    }

    public void GetLatLngAddress(Double Latitude, Double Longitude) {
        MenuRelativeLayoutSearchActivity.setVisibility(View.GONE);

        latitude = Latitude;
        longitude = Longitude;

        ShowAddressDialog.dismiss();

        SetLocalSettingToRepository(BusinessCategoryId, RegionId);

        LoadData();

    }

    private void HideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // برای اینکه بفهمیم چه زمانی نیاز به رفرش صفحه داریم
        if (Static.IsRefreshBookmark) {
            PageNumber = 1;

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        AccountRepository ARepository = new AccountRepository(null);
        userSettingViewModel = ARepository.getAccount().getUserSetting();

        PageNumber = 1;
        MenuRelativeLayoutSearchActivity.setVisibility(View.GONE);

        GetSetting();

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
        if (MenuRelativeLayoutSearchActivity.getVisibility() == View.VISIBLE)
            MenuRelativeLayoutSearchActivity.setVisibility(View.GONE);
        else
            super.onBackPressed();
    }
}
