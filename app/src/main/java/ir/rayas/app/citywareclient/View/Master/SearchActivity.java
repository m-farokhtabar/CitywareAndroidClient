package ir.rayas.app.citywareclient.View.Master;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.SearchRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.SearchResultProductRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.SearchResultBusinessRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Search.SearchService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Helper.Gps;
import ir.rayas.app.citywareclient.Share.Helper.GpsCurrentLocation;
import ir.rayas.app.citywareclient.Share.Helper.IResponseTurnOnGpsDialog;
import ir.rayas.app.citywareclient.Share.Layout.View.EditTextPersian;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Search.SearchBusinessResultViewModel;
import ir.rayas.app.citywareclient.ViewModel.Search.SearchProductResultViewModel;
import ir.rayas.app.citywareclient.ViewModel.Search.SearchViewModel;
import ir.rayas.app.citywareclient.ViewModel.Home.BusinessPosterInfoViewModel;
import ir.rayas.app.citywareclient.ViewModel.Setting.UserSettingViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;

public class SearchActivity extends BaseActivity implements IResponseService, IResponseTurnOnGpsDialog {

    private SwipeRefreshLayout RefreshSearchSwipeRefreshLayoutSearchActivity = null;
//    private RecyclerView SearchRecyclerViewSearchActivity = null;
    private RecyclerView SearchResultBusinessRecyclerViewSearchActivity = null;
    private RecyclerView SearchResultProductRecyclerViewSearchActivity = null;
    private TextViewPersian ShowEmptySearchTextViewSearchActivity = null;
    private TextView LineBusinessTextViewSearchActivity = null;
    private TextView LineProductTextViewSearchActivity = null;
    private TextViewPersian ProductTextViewSearchActivity = null;
    private TextViewPersian BusinessTextViewSearchActivity = null;
    private EditTextPersian SearchEditTextSearchActivity = null;

    private SearchResultBusinessRecyclerViewAdapter searchResultBusinessRecyclerViewAdapter = null;
    private SearchResultProductRecyclerViewAdapter searchResultProductRecyclerViewAdapter = null;
    private UserSettingViewModel userSettingViewModel = null;

    private Integer RegionId = null;
    private Integer BusinessCategoryId = null;
    private Integer GpsRangeInKm = null;
    private Double latitude = null;
    private Double longitude = null;
    private Integer SearchOnDelivery = 0;

    private Gps CurrentGps = null;

    private String TextSearch = "";
    private int PageNumber = 1;
    private int PageItems = 10;
    // SelectTab = 1 Business
    // SelectTab = 2 Product
    private int SelectTab = 0;
    private boolean IsFirstClickTab = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.SEARCH_ACTIVITY);

        Static.IsRefreshBookmark = true;
        //کلاس کنترل و مدیریت GPS
        CurrentGps = new Gps();

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                //  LoadData();
            }
        }, R.string.search);

        //ایجاد طرحبندی صفحه
        CreateLayout();

        //گرفتن تنظیمات کاربر از حافظه
        GetSetting();

    }

    private void CreateLayout() {

        AccountRepository ARepository = new AccountRepository(null);
        AccountViewModel AccountViewModel = ARepository.getAccount();

        userSettingViewModel = AccountViewModel.getUserSetting();

        ShowEmptySearchTextViewSearchActivity = findViewById(R.id.ShowEmptySearchTextViewSearchActivity);
        ShowEmptySearchTextViewSearchActivity.setVisibility(View.GONE);
        SelectTab = 1;


        //Search Start -------------------------------------------------------------------------------------------
        final ImageView SearchImageViewSearchActivity = findViewById(R.id.SearchImageViewSearchActivity);
        SearchEditTextSearchActivity = findViewById(R.id.SearchEditTextSearchActivity);
        RefreshSearchSwipeRefreshLayoutSearchActivity = findViewById(R.id.RefreshSearchSwipeRefreshLayoutSearchActivity);
        RefreshSearchSwipeRefreshLayoutSearchActivity.setRefreshing(false);


//        SearchRecyclerViewSearchActivity = findViewById(R.id.SearchRecyclerViewSearchActivity);
//        SearchRecyclerViewSearchActivity.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
//        SearchRecyclerViewAdapter searchRecyclerViewAdapter = new SearchRecyclerViewAdapter(SearchActivity.this, null, SearchRecyclerViewSearchActivity);
//        SearchRecyclerViewSearchActivity.setAdapter(searchRecyclerViewAdapter);


        ProductTextViewSearchActivity = findViewById(R.id.ProductTextViewSearchActivity);
        LineProductTextViewSearchActivity = findViewById(R.id.LineProductTextViewSearchActivity);
        BusinessTextViewSearchActivity = findViewById(R.id.BusinessTextViewSearchActivity);
        LineBusinessTextViewSearchActivity = findViewById(R.id.LineBusinessTextViewSearchActivity);
        LinearLayoutCompat ProductLinearLayoutSearchActivity = findViewById(R.id.ProductLinearLayoutSearchActivity);
        LinearLayoutCompat BusinessLinearLayoutSearchActivity = findViewById(R.id.BusinessLinearLayoutSearchActivity);


        SearchResultProductRecyclerViewSearchActivity = findViewById(R.id.SearchResultProductRecyclerViewSearchActivity);
        SearchResultProductRecyclerViewSearchActivity.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        searchResultProductRecyclerViewAdapter = new SearchResultProductRecyclerViewAdapter(SearchActivity.this, null, SearchResultProductRecyclerViewSearchActivity);
        SearchResultProductRecyclerViewSearchActivity.setAdapter(searchResultProductRecyclerViewAdapter);


        SearchResultBusinessRecyclerViewSearchActivity = findViewById(R.id.SearchResultBusinessRecyclerViewSearchActivity);
        SearchResultBusinessRecyclerViewSearchActivity.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        searchResultBusinessRecyclerViewAdapter = new SearchResultBusinessRecyclerViewAdapter(SearchActivity.this, null, SearchResultBusinessRecyclerViewSearchActivity);
        SearchResultBusinessRecyclerViewSearchActivity.setAdapter(searchResultBusinessRecyclerViewAdapter);

        ShowBusinessSearch();

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
//                    SearchRecyclerViewSearchActivity.setVisibility(View.GONE);

                    searchResultBusinessRecyclerViewAdapter.ClearViewModelList();
                    searchResultProductRecyclerViewAdapter.ClearViewModelList();

                    String Search = s.toString();
                    TextSearch = EditTextPersian.ConvertNumber(Search);

                    IsFirstClickTab = true;

                    try {
                        String Temp = URLEncoder.encode(TextSearch, "utf-8");
                        TextSearch = Temp;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    PageNumber = 1;

                    if (SelectTab == 1)
                        LoadDataBusinessSearch();
                    else
                        LoadDataProductSearch();

                } else {
                    RefreshSearchSwipeRefreshLayoutSearchActivity.setRefreshing(false);
                    ShowEmptySearchTextViewSearchActivity.setVisibility(View.GONE);
//                    SearchRecyclerViewSearchActivity.setVisibility(View.VISIBLE);
                }
            }
        });


        SearchImageViewSearchActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard(SearchImageViewSearchActivity);

                String SearchOffer = SearchEditTextSearchActivity.getText().toString();
                if (!SearchOffer.equals("")) {

                    searchResultBusinessRecyclerViewAdapter.ClearViewModelList();
                    searchResultProductRecyclerViewAdapter.ClearViewModelList();

                    String Search = SearchOffer;
                    TextSearch = EditTextPersian.ConvertNumber(Search);

                    try {
                        String Temp = URLEncoder.encode(TextSearch, "utf-8");
                        TextSearch = Temp;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    PageNumber = 1;
                    IsFirstClickTab = true;

                    if (SelectTab == 1)
                        LoadDataBusinessSearch();
                    else
                        LoadDataProductSearch();

                } else {
                    ShowToast(getResources().getString(R.string.please_enter_word), Toast.LENGTH_LONG, MessageType.Warning);
                }



            }
        });


        RefreshSearchSwipeRefreshLayoutSearchActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PageNumber = 1;
                IsFirstClickTab = false;
                SearchEditTextSearchActivity.setText("");
//                SearchRecyclerViewSearchActivity.setVisibility(View.VISIBLE);
                searchResultProductRecyclerViewAdapter.ClearViewModelList();
                searchResultBusinessRecyclerViewAdapter.ClearViewModelList();
            }
        });

        ProductLinearLayoutSearchActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SelectTab == 1) {
                    SelectTab = 2;
                    ShowProductSearch();
                }
            }
        });
        BusinessLinearLayoutSearchActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SelectTab == 2) {
                    SelectTab = 1;
                    ShowBusinessSearch();
                }
            }
        });
        // End (Search) --------------------------------------------------------------------------------------

    }

    private void ShowProductSearch() {

        ProductTextViewSearchActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));
        BusinessTextViewSearchActivity.setTextColor(getResources().getColor(R.color.FontSemiBlackColor));

        LineProductTextViewSearchActivity.setBackgroundColor(getResources().getColor(R.color.BackgroundThemeColor));
        LineBusinessTextViewSearchActivity.setBackgroundColor(getResources().getColor(R.color.BackgroundWhiteColor));

        SearchResultProductRecyclerViewSearchActivity.setVisibility(View.VISIBLE);
        SearchResultBusinessRecyclerViewSearchActivity.setVisibility(View.GONE);

        if (IsFirstClickTab) {
            RefreshSearchSwipeRefreshLayoutSearchActivity.setRefreshing(true);
            PageNumber = 1;
            IsFirstClickTab = false;
            LoadDataProductSearch();
        }
    }


    private void ShowBusinessSearch() {

        ProductTextViewSearchActivity.setTextColor(getResources().getColor(R.color.FontSemiBlackColor));
        BusinessTextViewSearchActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));

        LineProductTextViewSearchActivity.setBackgroundColor(getResources().getColor(R.color.BackgroundWhiteColor));
        LineBusinessTextViewSearchActivity.setBackgroundColor(getResources().getColor(R.color.BackgroundThemeColor));

        SearchResultBusinessRecyclerViewSearchActivity.setVisibility(View.VISIBLE);
        SearchResultProductRecyclerViewSearchActivity.setVisibility(View.GONE);

        if (IsFirstClickTab) {
            RefreshSearchSwipeRefreshLayoutSearchActivity.setRefreshing(true);
            PageNumber = 1;
            IsFirstClickTab = false;
            LoadDataBusinessSearch();
        }
    }

//    public void LoadData() {
////        if (PageNumber == 1)
////            ShowLoadingProgressBar();
////
////        if (localSettingRepository.getLocalSetting().isUseGprsPoint())
////            if (IsClickYesGPS)
////                GetMyLocation();
////
////        HomeService service = new HomeService(this);
//        //  service.GetAll(QueryType.Search.GetQueryType(), BusinessCategoryId, RegionId, GpsRangeInKm, latitude, longitude, PageNumber);
//
//    }

    public void LoadDataBusinessSearch() {

        SearchService service = new SearchService(this);
        service.GetAllBusinessSearch(BusinessCategoryId, RegionId, GpsRangeInKm, latitude, longitude, SearchOnDelivery, PageNumber, PageItems, TextSearch);
    }


    public void LoadDataProductSearch() {

        SearchService service = new SearchService(this);
        service.GetAllProductSearch(BusinessCategoryId, RegionId, GpsRangeInKm, latitude, longitude, SearchOnDelivery, PageNumber, PageItems, TextSearch);
    }

    private void GetSetting() {

        AccountRepository ARepository = new AccountRepository(null);
        AccountViewModel AccountViewModel = ARepository.getAccount();
        userSettingViewModel = AccountViewModel.getUserSetting();

        setInformationSetting();
    }


    private void setInformationSetting() {
        BusinessCategoryId = userSettingViewModel.getBusinessCategoryId();
        RegionId = userSettingViewModel.getRegionId();
        SearchOnDelivery = userSettingViewModel.getSearchOnDelivery();
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
            GpsRangeInKm = userSettingViewModel.getGpsRangeInKm();
            RegionId = null;

        } else {
            GpsRangeInKm = null;
            latitude = null;
            longitude = null;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        RefreshSearchSwipeRefreshLayoutSearchActivity.setRefreshing(false);
        HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.SearchResultBusinessGet) {

                Feedback<List<SearchBusinessResultViewModel>> FeedBack = (Feedback<List<SearchBusinessResultViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    final List<SearchBusinessResultViewModel> ViewModel = FeedBack.getValue();

//                    SearchRecyclerViewSearchActivity.setVisibility(View.GONE);

                    if (ViewModel != null) {
                        if (PageNumber == 1) {
                            if (ViewModel.size() > 0) {
                                ShowEmptySearchTextViewSearchActivity.setVisibility(View.GONE);
                                searchResultBusinessRecyclerViewAdapter.SetViewModelList(ViewModel);

                                if (PageItems == ViewModel.size()) {
                                    PageNumber = PageNumber + 1;
                                    //    LoadData();
                                }
                            } else {
                                ShowEmptySearchTextViewSearchActivity.setVisibility(View.VISIBLE);
                            }

                        } else {
                            ShowEmptySearchTextViewSearchActivity.setVisibility(View.GONE);

                            searchResultBusinessRecyclerViewAdapter.AddViewModelList(ViewModel);

                            if (PageItems == ViewModel.size()) {
                                PageNumber = PageNumber + 1;
                                //  LoadData();
                            }
                        }
                    }

                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    if (PageNumber > 1) {
                        ShowEmptySearchTextViewSearchActivity.setVisibility(View.GONE);
                    } else {
                        ShowEmptySearchTextViewSearchActivity.setVisibility(View.VISIBLE);
                    }
                } else {
                    ShowEmptySearchTextViewSearchActivity.setVisibility(View.GONE);
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                        HideLoading();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.SearchResultProductGet) {

                Feedback<List<SearchProductResultViewModel>> FeedBack = (Feedback<List<SearchProductResultViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    final List<SearchProductResultViewModel> ViewModel = FeedBack.getValue();

//                    SearchRecyclerViewSearchActivity.setVisibility(View.GONE);

                    if (ViewModel != null) {
                        if (PageNumber == 1) {
                            if (ViewModel.size() > 0) {
                                ShowEmptySearchTextViewSearchActivity.setVisibility(View.GONE);
                                searchResultProductRecyclerViewAdapter.SetViewModelList(ViewModel);

                                if (PageItems == ViewModel.size()) {
                                    PageNumber = PageNumber + 1;
                                    //    LoadData();
                                }
                            } else {
                                ShowEmptySearchTextViewSearchActivity.setVisibility(View.VISIBLE);
                            }

                        } else {
                            ShowEmptySearchTextViewSearchActivity.setVisibility(View.GONE);

                            searchResultProductRecyclerViewAdapter.AddViewModelList(ViewModel);

                            if (PageItems == ViewModel.size()) {
                                PageNumber = PageNumber + 1;
                                //  LoadData();
                            }
                        }
                    }

                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    if (PageNumber > 1) {
                        ShowEmptySearchTextViewSearchActivity.setVisibility(View.GONE);
                    } else {
                        ShowEmptySearchTextViewSearchActivity.setVisibility(View.VISIBLE);
                    }
                } else {
                    ShowEmptySearchTextViewSearchActivity.setVisibility(View.GONE);
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                        HideLoading();
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


    @Override
    public void OnDismissTurnOnGpsDialog(boolean IsClickYes) {

        if (IsClickYes) {
            GpsCurrentLocation gpsCurrentLocation = new GpsCurrentLocation(this);
            latitude = gpsCurrentLocation.getLatitude();
            longitude = gpsCurrentLocation.getLongitude();
            GetMyLocation();

        } else {
            //   LoadData();
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
            }
        } else {
            //  LoadData();
        }
    }

    private void GetMyLocation() {

        GpsCurrentLocation gpsCurrentLocation = new GpsCurrentLocation(this);
        latitude = gpsCurrentLocation.getLatitude();
        longitude = gpsCurrentLocation.getLongitude();


        //   LoadData();
    }


    public void GetLatLngAddress(Double Latitude, Double Longitude) {

        latitude = Latitude;
        longitude = Longitude;

        //  LoadData();

    }

    private void HideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
            GpsRangeInKm = userSettingViewModel.getGpsRangeInKm();
            RegionId = null;

        }
        PageNumber = 1;
        SearchEditTextSearchActivity.setText("");
        searchResultProductRecyclerViewAdapter.ClearViewModelList();
        searchResultBusinessRecyclerViewAdapter.ClearViewModelList();

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
        super.onBackPressed();
    }
}
