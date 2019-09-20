package ir.rayas.app.citywareclient.View.Master;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.BusinessPosterInfoBookmarkRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.BusinessPosterInfoRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.IsTopPosterRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.AccountRepository;

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
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Home.BusinessPosterInfoViewModel;
import ir.rayas.app.citywareclient.ViewModel.Setting.UserSettingViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;

public class MainActivity extends BaseActivity implements IResponseService, IResponseTurnOnGpsDialog {


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
    private SwipeRefreshLayout RefreshSwipeRefreshLayoutMainActivity = null;
    private ProgressBar LoadMoreProgressBarMainActivity = null;

    private IsTopPosterRecyclerViewAdapter isTopPosterRecyclerViewAdapter = null;
    private BusinessPosterInfoRecyclerViewAdapter businessPosterInfoRecyclerViewAdapter = null;
    private BusinessPosterInfoBookmarkRecyclerViewAdapter businessPosterInfoBookmarkRecyclerViewAdapter = null;


    private UserSettingViewModel userSettingViewModel = null;

    private int queryType = 0;
    private Integer RegionId = null;
    private Integer BusinessCategoryId = null;
    private Integer GpsRangeInKm = null;
    private Double latitude = null;
    private Double longitude = null;

    private int PageNumberPosterTop = 1;
    private int PageNumberPoster = 1;

    private boolean IsClickYesGPS = false;
    private boolean IsFirst = false;
    private Gps CurrentGps = null;


    // SelectTab = 1 newPoster
    // SelectTab = 2 Star
    // SelectTab = 3 Show
    // SelectTab = 4 Bookmark
    private int SelectTab = 0;
    private int PageItems = 5;


    boolean isScrolling = false;
    boolean isScrollingBookmark = false;
    int currentItems, totalItems, scrollOutItems;
    int currentItemsBookmark, totalItemsBookmark, scrollOutItemsBookmark;
    LinearLayoutManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //تنظیم گزینه انتخاب شده منو
        setCurrentActivityId(ActivityIdList.MAIN_ACTIVITY);

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

        IsFirst = true;

        //ایجاد طرحبندی صفحه
        CreateLayout();

        //گرفتن تنظیمات کاربر از حافظه
        GetSetting();

        if (!userSettingViewModel.isUseGprsPoint())
            LoadData();

    }

    private void CreateLayout() {
        SelectTab = 1;

        AccountRepository ARepository = new AccountRepository(null);
        AccountViewModel AccountViewModel = ARepository.getAccount();
        userSettingViewModel = AccountViewModel.getUserSetting();

        // Recycler View Poster Top, Poster, Bookmark Start------------------------------------------------------------------
        LoadMoreProgressBarMainActivity = findViewById(R.id.LoadMoreProgressBarMainActivity);
        Line = findViewById(R.id.Line);
        RefreshSwipeRefreshLayoutMainActivity = findViewById(R.id.RefreshSwipeRefreshLayoutMainActivity);

        RecyclerView isTopPosterRecyclerViewMainActivity = findViewById(R.id.IsTopPosterRecyclerViewMainActivity);
        isTopPosterRecyclerViewMainActivity.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, true));
        isTopPosterRecyclerViewMainActivity.setHasFixedSize(true);
        isTopPosterRecyclerViewMainActivity.setNestedScrollingEnabled(false);
        isTopPosterRecyclerViewAdapter = new IsTopPosterRecyclerViewAdapter(MainActivity.this, null, isTopPosterRecyclerViewMainActivity);
        isTopPosterRecyclerViewMainActivity.setAdapter(isTopPosterRecyclerViewAdapter);

        businessPosterInfoRecyclerViewMainActivity = findViewById(R.id.BusinessPosterInfoRecyclerViewMainActivity);
//        manager = new LinearLayoutManager(this);
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

        LoadMoreProgressBarMainActivity.setVisibility(View.GONE);
        
//        businessPosterInfoRecyclerViewMainActivity.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                    isScrolling = true;
//                }
//            }
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                // تعداد ایتم های نمایش داده شده در صفحه
//                currentItems = manager.getChildCount();
//                // تعداد ایتم های نمایش داده شده در یک پیج
//                totalItems = manager.getItemCount();
//                // تعداد ایتم هایی که از صفحه قسمت بالا و پایین خارج می شود
//                scrollOutItems = manager.findFirstVisibleItemPosition();
//
//                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
//                    // data fetch
//                    isScrolling = false;
//                    PageNumberPoster = PageNumberPoster + 1;
//                    LoadMoreProgressBarMainActivity.setVisibility(View.VISIBLE);
//                    LoadDataInfo();
//                }
//            }
//        });


//        BusinessPosterInfoBookmarkRecyclerViewMainActivity.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                    isScrollingBookmark = true;
//                }
//            }
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                // تعداد ایتم های نمایش داده شده در صفحه
//                currentItemsBookmark = manager.getChildCount();
//                // تعداد ایتم های نمایش داده شده در یک پیج
//                totalItemsBookmark = PageItems;
//                // تعداد ایتم هایی که از صفحه قسمت بالا و پایین خارج می شود
//                scrollOutItemsBookmark = manager.findFirstVisibleItemPosition();
//
//                if (isScrollingBookmark && (currentItemsBookmark + scrollOutItemsBookmark == totalItemsBookmark)) {
//                    // data fetch
//                    isScrollingBookmark = false;
//                    PageNumberPoster = PageNumberPoster + 1;
//                    LoadMoreProgressBarMainActivity.setVisibility(View.VISIBLE);
//                    LoadDataBookmarkPoster();
//                }
//            }
//        });
        //End (Recycler View Poster Top, Poster, Bookmark)-----------------------------------------------------------------------------


        RefreshSwipeRefreshLayoutMainActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                RefreshSwipeRefreshLayoutMainActivity.setRefreshing(true);
                isTopPosterRecyclerViewAdapter.ClearViewModelList();
                businessPosterInfoRecyclerViewAdapter.ClearViewModelList();
                businessPosterInfoBookmarkRecyclerViewAdapter.ClearViewModelList();

                PageNumberPoster = 1;

                if (SelectTab == 4) {
                    LoadDataBookmarkPoster();
                } else {
                    PageNumberPosterTop = 1;
                    LoadData();
                }
            }
        });

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
    }


    // اولین بار که صفحه لود میشود و تمام سرویس ها فراخوانی می شود
    private void LoadData() {

        if (IsFirst)
            ShowLoadingProgressBar();

        if (userSettingViewModel.isUseGprsPoint())
            if (IsClickYesGPS)
                GetMyLocation();

        HomeService homeService = new HomeService(MainActivity.this);
        homeService.GetAllTop(QueryType.Top.GetQueryType(), BusinessCategoryId, RegionId, GpsRangeInKm, latitude, longitude, PageNumberPosterTop, PageItems);

        homeService.GetAll(queryType, BusinessCategoryId, RegionId, GpsRangeInKm, latitude, longitude, PageNumberPoster, PageItems);

    }

    private void LoadDataTop() {

        HomeService homeService = new HomeService(MainActivity.this);
        homeService.GetAllTop(QueryType.Top.GetQueryType(), BusinessCategoryId, RegionId, GpsRangeInKm, latitude, longitude, PageNumberPosterTop, PageItems);
    }

    private void LoadDataInfo() {

        HomeService homeService = new HomeService(MainActivity.this);
        homeService.GetAll(queryType, BusinessCategoryId, RegionId, GpsRangeInKm, latitude, longitude, PageNumberPoster, PageItems);
    }

    //سرویس مربوط به لیست علاقه مندی ها
    private void LoadDataBookmarkPoster() {
        HomeService homeService = new HomeService(MainActivity.this);
        homeService.GetAllBookmark(PageNumberPoster, PageItems);
    }

    private void GetSetting() {

        AccountRepository ARepository = new AccountRepository(null);
        AccountViewModel AccountViewModel = ARepository.getAccount();
        userSettingViewModel = AccountViewModel.getUserSetting();

        GpsRangeInKm = userSettingViewModel.getGpsRangeInKm();
        RegionId = userSettingViewModel.getRegionId();
        BusinessCategoryId = userSettingViewModel.getBusinessCategoryId();

        if (userSettingViewModel.isUseGprsPoint()) {

            if (!CurrentGps.IsPermissionEnabled()) {
                CurrentGps.ShowPermissionDialog(this);
            } else {
                if (!CurrentGps.IsEnabled()) {
                    CurrentGps.ShowTurnOnGpsDialog(this, this, R.string.TurnOnLocation);
                } else {
                    GetMyLocation();
                    LoadData();
                }
            }

        } else {
            GpsRangeInKm = null;
            latitude = null;
            longitude = null;
        }

    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        RefreshSwipeRefreshLayoutMainActivity.setRefreshing(false);
        LoadMoreProgressBarMainActivity.setVisibility(View.GONE);
        IsFirst = false;
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
                            if (ViewModelList.size() > 0) {
                                businessPosterInfoRecyclerViewAdapter.SetViewModelList(ViewModelList);

                                if (PageItems == ViewModelList.size()) {
                                    PageNumberPoster = PageNumberPoster + 1;
                                    LoadDataInfo();
                                }
                            }
                        } else {
                            businessPosterInfoRecyclerViewAdapter.AddViewModelList(ViewModelList);

                            if (PageItems == ViewModelList.size()) {
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

                Line.setVisibility(View.GONE);

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<BusinessPosterInfoViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        if (PageNumberPoster == 1) {
                            if (ViewModelList.size() > 0) {
                                businessPosterInfoBookmarkRecyclerViewAdapter.SetViewModelList(ViewModelList);

                                if (PageItems == ViewModelList.size()) {
                                    PageNumberPoster = PageNumberPoster + 1;
                                    LoadDataBookmarkPoster();
                                }
                            }
                        } else {
                            businessPosterInfoBookmarkRecyclerViewAdapter.AddViewModelList(ViewModelList);

                            if (PageItems == ViewModelList.size()) {
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

    private void NewestPoster() {
        SelectTab = 1;
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

        businessPosterInfoRecyclerViewAdapter.ClearViewModelList();
        isTopPosterRecyclerViewAdapter.ClearViewModelList();
        businessPosterInfoBookmarkRecyclerViewAdapter.ClearViewModelList();

        RefreshSwipeRefreshLayoutMainActivity.setRefreshing(true);
        LoadData();
    }

    private void StarredPoster() {
        SelectTab = 2;
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

        businessPosterInfoRecyclerViewAdapter.ClearViewModelList();
        isTopPosterRecyclerViewAdapter.ClearViewModelList();
        businessPosterInfoBookmarkRecyclerViewAdapter.ClearViewModelList();

        RefreshSwipeRefreshLayoutMainActivity.setRefreshing(true);
        LoadData();
    }

    private void MostVisitedPoster() {

        SelectTab = 3;
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

        businessPosterInfoRecyclerViewAdapter.ClearViewModelList();
        isTopPosterRecyclerViewAdapter.ClearViewModelList();
        businessPosterInfoBookmarkRecyclerViewAdapter.ClearViewModelList();

        RefreshSwipeRefreshLayoutMainActivity.setRefreshing(true);
        LoadData();
    }

    private void BookmarkPoster() {
        SelectTab = 4;

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

        businessPosterInfoRecyclerViewAdapter.ClearViewModelList();
        isTopPosterRecyclerViewAdapter.ClearViewModelList();
        businessPosterInfoBookmarkRecyclerViewAdapter.ClearViewModelList();

        RefreshSwipeRefreshLayoutMainActivity.setRefreshing(true);
        LoadDataBookmarkPoster();
    }

    @Override
    public void OnDismissTurnOnGpsDialog(boolean IsClickYes) {

        IsClickYesGPS = IsClickYes;

        if (IsClickYes) {
            GpsCurrentLocation gpsCurrentLocation = new GpsCurrentLocation(this);
            latitude = gpsCurrentLocation.getLatitude();
            longitude = gpsCurrentLocation.getLongitude();
            GetMyLocation();

            LoadData();

        } else {
            latitude = null;
            longitude = null;
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

        AccountRepository ARepository = new AccountRepository(null);
        AccountViewModel AccountViewModel = ARepository.getAccount();
        userSettingViewModel = AccountViewModel.getUserSetting();

        PageNumberPosterTop = 1;
        PageNumberPoster = 1;

        businessPosterInfoRecyclerViewAdapter.ClearViewModelList();
        isTopPosterRecyclerViewAdapter.ClearViewModelList();
        businessPosterInfoBookmarkRecyclerViewAdapter.ClearViewModelList();

        GpsRangeInKm = userSettingViewModel.getGpsRangeInKm();
        RegionId = userSettingViewModel.getRegionId();
        BusinessCategoryId = userSettingViewModel.getBusinessCategoryId();

        if (userSettingViewModel.isUseGprsPoint()) {

            if (!CurrentGps.IsPermissionEnabled()) {
                CurrentGps.ShowPermissionDialog(this);
            } else {
                if (!CurrentGps.IsEnabled()) {
                    CurrentGps.ShowTurnOnGpsDialog(this, this, R.string.TurnOnLocation);
                } else {
                    GetMyLocation();
                    LoadData();
                }
            }

        } else {
            GpsRangeInKm = null;
            latitude = null;
            longitude = null;


            if (SelectTab == 1) {
                NewestPoster();
            } else if (SelectTab == 2) {
                StarredPoster();
            } else if (SelectTab == 3) {
                MostVisitedPoster();
            } else if (SelectTab == 4) {
                BookmarkPoster();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        // برای اینکه بفهمیم چه زمانی نیاز به رفرش صفحه داریم
     //   if (Static.IsRefreshBookmark) {
            PageNumberPosterTop = 1;
            PageNumberPoster = 1;
      //  }
    }

}