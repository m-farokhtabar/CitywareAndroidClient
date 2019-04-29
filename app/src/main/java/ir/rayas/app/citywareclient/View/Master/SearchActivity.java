package ir.rayas.app.citywareclient.View.Master;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.SearchRecyclerViewAdapter;
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
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Search.SearchViewModel;
import ir.rayas.app.citywareclient.ViewModel.Home.BusinessPosterInfoViewModel;
import ir.rayas.app.citywareclient.ViewModel.Setting.UserSettingViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;

public class SearchActivity extends BaseActivity implements IResponseService {

    private SearchRecyclerViewAdapter searchRecyclerViewAdapter = null;

    private int PageNumber = 1;
    private UserSettingViewModel userSettingViewModel = null;
    private List<BusinessPosterInfoViewModel> businessPosterInfoViewModelList = null;

    private Integer RegionId = null;
    private Integer BusinessCategoryId = null;
    private Integer GpsRangeInKm = null;
    private Double latitude = null;
    private Double longitude = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.SEARCH_ACTIVITY);

        Static.IsRefreshBookmark = true;

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                LoadData();
            }
        }, R.string.search);

        CreateLayout();

        //گرفتن تنظیمات کاربر از حافظه
        GetSetting();

    }

    private void CreateLayout() {

        RecyclerView SearchRecyclerViewSearchActivity = findViewById(R.id.SearchRecyclerViewSearchActivity);
        SearchRecyclerViewSearchActivity.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        searchRecyclerViewAdapter = new SearchRecyclerViewAdapter(SearchActivity.this, null, SearchRecyclerViewSearchActivity);
        SearchRecyclerViewSearchActivity.setAdapter(searchRecyclerViewAdapter);


    }

    public void LoadData() {
        if (PageNumber == 1)
            ShowLoadingProgressBar();

        HomeService service = new HomeService(this);
        service.GetAll(QueryType.Search.GetQueryType(), BusinessCategoryId, RegionId, GpsRangeInKm, latitude, longitude, PageNumber);

    }


    private void GetSetting() {
        AccountRepository ARepository = new AccountRepository(null);
        AccountViewModel AccountViewModel = ARepository.getAccount();

        userSettingViewModel = AccountViewModel.getUserSetting();

        setInformationSettingToView();
    }

    private void setInformationSettingToView() {
        if (userSettingViewModel.isUseGprsPoint()) {

            GpsRangeInKm = 1;
            RegionId = null;

        } else {

            GpsRangeInKm = null;
            latitude = null;
            longitude = null;

            if (userSettingViewModel.getRegionId() == null || userSettingViewModel.getRegionId() == 0) {
                RegionId = null;
            } else {
                RegionId = userSettingViewModel.getRegionId();
            }
        }

        if (userSettingViewModel.getBusinessCategoryId() == null || userSettingViewModel.getBusinessCategoryId() == 0) {
            BusinessCategoryId = null;
        } else {
            BusinessCategoryId = userSettingViewModel.getBusinessCategoryId();
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {

        HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.BusinessPosterInfoGetAll) {
                Feedback<List<BusinessPosterInfoViewModel>> FeedBack = (Feedback<List<BusinessPosterInfoViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<BusinessPosterInfoViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {


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
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }


                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;
                    final List<BusinessPosterInfoViewModel> ViewModelList = FeedBack.getValue();

                    if (ViewModelList != null) {


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

                    }

                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        if (!(PageNumber > 1 && FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId())) {
                            ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);

                        }
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


    @Override
    protected void onResume() {
        super.onResume();
        // برای اینکه بفهمیم چه زمانی نیاز به رفرش صفحه داریم
        if (Static.IsRefreshBookmark) {
            PageNumber = 1;
            //دریافت کسب و کارهای بوک مارک شده
            LoadData();

        }
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
