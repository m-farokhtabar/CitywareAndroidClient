package ir.rayas.app.citywareclient.View.Master;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.BusinessPosterInfoRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.IsTopPosterRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.OnLoadMoreListener;
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
import ir.rayas.app.citywareclient.Share.Helper.IResponseTurnOnGpsDialog;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.View.MasterChildren.SettingActivity;
import ir.rayas.app.citywareclient.ViewModel.Home.BusinessPosterInfoViewModel;
import ir.rayas.app.citywareclient.ViewModel.Setting.UserSettingViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;

public class MainActivity extends BaseActivity implements IResponseService, IResponseTurnOnGpsDialog {

    private IsTopPosterRecyclerViewAdapter isTopPosterRecyclerViewAdapter = null;
    private BusinessPosterInfoRecyclerViewAdapter businessPosterInfoRecyclerViewAdapter = null;
    private ImageView MostVisitedImageViewMainActivity = null;
    private ImageView StarredImageViewMainActivity = null;
    private ImageView NewestImageViewMainActivity = null;
    private TextViewPersian MostVisitedTextViewMainActivity = null;
    private TextViewPersian StarredTextViewMainActivity = null;
    private TextViewPersian NewestTextViewMainActivity = null;
    private LinearLayoutCompat MostVisitedLinearLayoutMainActivity = null;
    private LinearLayoutCompat StarredLinearLayoutMainActivity = null;
    private LinearLayoutCompat NewestLinearLayoutMainActivity = null;

    private int PageNumber = 1;
    private int PageNumberPoster = 1;
    private int ClickCategory = 0;
    private int ClickRegion = 0;

    private UserSettingViewModel userSettingViewModel = null;
    private Gps CurrentGps = null;

    private int queryType = 0;
    private boolean ShowAllRegion = false;
    private boolean ShowAllCategory = false;
    private int GpsRangeInKm = 1;
    private double latitude = 0.0;
    private double longitude = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //تنظیم گزینه انتخاب شده منو
        setCurrentActivityId(ActivityIdList.MAIN_ACTIVITY);

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                LoadData();
            }
        }, 0);

        //کلاس کنترل و مدیریت GPS
        CurrentGps = new Gps();

        //گرفتن تنظیمات کاربر از حافظه
        GetSetting();
        //ایجاد طرحبندی صفحه
        CreateLayout();

        //  LoadData();
    }

    // اولین بار که صفحه لود میشود و تمام سرویس ها فراخوانی می شود
    private void LoadData() {
        ShowLoadingProgressBar();

        HomeService homeService = new HomeService(MainActivity.this);
        homeService.GetAllTop(GpsRangeInKm, ShowAllRegion, latitude, longitude, ShowAllCategory, PageNumber);

        homeService.GetAll(queryType, GpsRangeInKm, ShowAllRegion, latitude, longitude, ShowAllCategory, PageNumber);
    }

    // سرویس مربوط به پوستر های دایره ای بالای صفحه
    private void LoadDataIsTop() {
        ShowLoadingProgressBar();

        HomeService homeService = new HomeService(MainActivity.this);
        homeService.GetAllTop(GpsRangeInKm, ShowAllRegion, latitude, longitude, ShowAllCategory, PageNumber);
    }

    // سرویس مربوط به پوسترهای کسب و کار با جزئیات
    private void LoadDataPoster() {
        ShowLoadingProgressBar();

        HomeService homeService = new HomeService(MainActivity.this);
        homeService.GetAll(queryType, GpsRangeInKm, ShowAllRegion, latitude, longitude, ShowAllCategory, PageNumber);
    }

    private void CreateLayout() {
        TextView SearchLogo = findViewById(R.id.MainSearchIconTextView);
        SearchLogo.setTypeface(Font.MasterIcon);
        SearchLogo.setText("\uf002");


        final RecyclerView IsTopPosterRecyclerViewMainActivity = findViewById(R.id.IsTopPosterRecyclerViewMainActivity);
        IsTopPosterRecyclerViewMainActivity.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, true));
        isTopPosterRecyclerViewAdapter = new IsTopPosterRecyclerViewAdapter(MainActivity.this, null, IsTopPosterRecyclerViewMainActivity, new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                PageNumber = PageNumber + 1;
                LoadDataIsTop();
            }
        });
        IsTopPosterRecyclerViewMainActivity.setAdapter(isTopPosterRecyclerViewAdapter);


        RecyclerView BusinessPosterInfoRecyclerViewMainActivity = findViewById(R.id.BusinessPosterInfoRecyclerViewMainActivity);
        BusinessPosterInfoRecyclerViewMainActivity.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        businessPosterInfoRecyclerViewAdapter = new BusinessPosterInfoRecyclerViewAdapter(MainActivity.this, null, BusinessPosterInfoRecyclerViewMainActivity, new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                PageNumberPoster = PageNumberPoster + 1;
                LoadDataPoster();
            }
        });
        BusinessPosterInfoRecyclerViewMainActivity.setAdapter(businessPosterInfoRecyclerViewAdapter);

        RadioGroup CategoryToggleRadioGroupMainActivity = findViewById(R.id.CategoryToggleRadioGroupMainActivity);
        RadioGroup RegionToggleRadioGroupMainActivity = findViewById(R.id.RegionToggleRadioGroupMainActivity);
        RadioButton CategoryAllRadioButtonMainActivity = findViewById(R.id.CategoryAllRadioButtonMainActivity);
        RadioButton CategoryRadioButtonMainActivity = findViewById(R.id.CategoryRadioButtonMainActivity);
        RadioButton RegionAllRadioButtonMainActivity = findViewById(R.id.RegionAllRadioButtonMainActivity);
        RadioButton RegionRadioButtonMainActivity = findViewById(R.id.RegionRadioButtonMainActivity);

        MostVisitedLinearLayoutMainActivity = findViewById(R.id.MostVisitedLinearLayoutMainActivity);
        StarredLinearLayoutMainActivity = findViewById(R.id.StarredLinearLayoutMainActivity);
        NewestLinearLayoutMainActivity = findViewById(R.id.NewestLinearLayoutMainActivity);
        MostVisitedImageViewMainActivity = findViewById(R.id.MostVisitedImageViewMainActivity);
        StarredImageViewMainActivity = findViewById(R.id.StarredImageViewMainActivity);
        NewestImageViewMainActivity = findViewById(R.id.NewestImageViewMainActivity);
        MostVisitedTextViewMainActivity = findViewById(R.id.MostVisitedTextViewMainActivity);
        StarredTextViewMainActivity = findViewById(R.id.StarredTextViewMainActivity);
        NewestTextViewMainActivity = findViewById(R.id.NewestTextViewMainActivity);


        NewestLinearLayoutMainActivity.setBackgroundResource(R.drawable.view_button_normal_without_corner_theme_style);
        MostVisitedLinearLayoutMainActivity.setBackgroundResource(R.drawable.selector_item_bottom_menu);
        StarredLinearLayoutMainActivity.setBackgroundResource(R.drawable.selector_item_bottom_menu);


        if (userSettingViewModel.isUseGprsPoint()) {
            CurrentGps.ShowTurnOnGpsDialog(this, this, R.string.turn_on_location_show_business_inside);
        } else {
            if (userSettingViewModel.getRegionId() == null || userSettingViewModel.getRegionId() == 0) {
                RegionAllRadioButtonMainActivity.setChecked(true);
                RegionRadioButtonMainActivity.setChecked(false);
                RegionRadioButtonMainActivity.setText(getResources().getString(R.string.region_name));
                ClickRegion = 0;
            } else {
                RegionAllRadioButtonMainActivity.setChecked(false);
                RegionRadioButtonMainActivity.setChecked(true);
                RegionRadioButtonMainActivity.setText(userSettingViewModel.getRegionName());
                ClickRegion = 1;
            }
        }


        if (userSettingViewModel.getBusinessCategoryId() == null || userSettingViewModel.getBusinessCategoryId() == 0) {
            CategoryAllRadioButtonMainActivity.setChecked(true);
            CategoryRadioButtonMainActivity.setChecked(false);
            CategoryRadioButtonMainActivity.setText(getResources().getString(R.string.category_name));
            ClickCategory = 0;
        } else {
            CategoryAllRadioButtonMainActivity.setChecked(false);
            CategoryRadioButtonMainActivity.setChecked(true);
            CategoryRadioButtonMainActivity.setText(userSettingViewModel.getBusinessCategoryName());
            ClickCategory = 1;
        }


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

        RegionToggleRadioGroupMainActivity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.RegionAllRadioButtonMainActivity) {
                    ShowAllRegion = true;
                    ClickRegion = 0;
                } else if (checkedId == R.id.RegionRadioButtonMainActivity) {
                    ShowAllRegion = false;
                }
                //  LoadDataPoster();
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
                    ShowAllCategory = true;
                    ClickCategory = 0;
                } else if (checkedId == R.id.CategoryRadioButtonMainActivity) {
                    ShowAllCategory = false;
                }
                //  LoadDataPoster();
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
    }

    @Override
    public void OnDismissTurnOnGpsDialog(boolean IsClickYes) {
        if (!IsClickYes) {

        } else {

        }
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.BusinessPosterInfoTopGetAll) {
                Feedback<List<BusinessPosterInfoViewModel>> FeedBack = (Feedback<List<BusinessPosterInfoViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

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
        NewestLinearLayoutMainActivity.setBackgroundResource(R.drawable.view_button_normal_without_corner_theme_style);
        MostVisitedLinearLayoutMainActivity.setBackgroundResource(R.drawable.selector_item_bottom_menu);
        StarredLinearLayoutMainActivity.setBackgroundResource(R.drawable.selector_item_bottom_menu);

        MostVisitedImageViewMainActivity.setBackgroundResource(R.drawable.ic_visibility_theme_24dp);
        StarredImageViewMainActivity.setBackgroundResource(R.drawable.ic_stars_theme_24dp);
        NewestImageViewMainActivity.setBackgroundResource(R.drawable.ic_fiber_new_white_24dp);

        MostVisitedTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));
        StarredTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));
        NewestTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontWhiteColor));

        queryType = QueryType.New.GetQueryType();

        LoadDataPoster();
    }

    private void StarredPoster() {
        StarredLinearLayoutMainActivity.setBackgroundResource(R.drawable.view_button_normal_without_corner_theme_style);
        MostVisitedLinearLayoutMainActivity.setBackgroundResource(R.drawable.selector_item_bottom_menu);
        NewestLinearLayoutMainActivity.setBackgroundResource(R.drawable.selector_item_bottom_menu);

        MostVisitedImageViewMainActivity.setBackgroundResource(R.drawable.ic_visibility_theme_24dp);
        StarredImageViewMainActivity.setBackgroundResource(R.drawable.ic_stars_white_24dp);
        NewestImageViewMainActivity.setBackgroundResource(R.drawable.ic_fiber_new_theme_24dp);

        MostVisitedTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));
        StarredTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontWhiteColor));
        NewestTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));

        queryType = QueryType.Star.GetQueryType();

        LoadDataPoster();
    }

    private void MostVisitedPoster() {
        MostVisitedLinearLayoutMainActivity.setBackgroundResource(R.drawable.view_button_normal_without_corner_theme_style);
        NewestLinearLayoutMainActivity.setBackgroundResource(R.drawable.selector_item_bottom_menu);
        StarredLinearLayoutMainActivity.setBackgroundResource(R.drawable.selector_item_bottom_menu);

        MostVisitedImageViewMainActivity.setBackgroundResource(R.drawable.ic_visibility_white_24dp);
        StarredImageViewMainActivity.setBackgroundResource(R.drawable.ic_stars_theme_24dp);
        NewestImageViewMainActivity.setBackgroundResource(R.drawable.ic_fiber_new_theme_24dp);

        MostVisitedTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontWhiteColor));
        StarredTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));
        NewestTextViewMainActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));

        queryType = QueryType.Visit.GetQueryType();

        LoadDataPoster();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
