package ir.rayas.app.citywareclient.View.MasterChildren;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.BusinessDetailsContactRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.BusinessDetailsOpenTimeRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.Service.Business.BookmarkService;
import ir.rayas.app.citywareclient.Service.Business.BusinessContactService;
import ir.rayas.app.citywareclient.Service.Business.BusinessOpenTimeService;
import ir.rayas.app.citywareclient.Service.Business.BusinessService;
import ir.rayas.app.citywareclient.Service.Business.ScoreService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.View.Share.CommissionActivity;
import ir.rayas.app.citywareclient.ViewModel.Business.BookmarkOutViewModel;
import ir.rayas.app.citywareclient.ViewModel.Business.BookmarkViewModel;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessContactViewModel;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessOpenTimeViewModel;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessViewModel;
import ir.rayas.app.citywareclient.ViewModel.Business.ScoreInViewModel;
import ir.rayas.app.citywareclient.ViewModel.Business.ScoreViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;


public class ShowBusinessDetailsActivity extends BaseActivity implements IResponseService, ILoadData, OnMapReadyCallback {

    private SwipeRefreshLayout RefreshShowBusinessDetailsSwipeRefreshLayoutShowBusinessDetailsActivity = null;
    private ImageView BookmarkImageViewShowBusinessDetailsActivity = null;
    private ImageView ShareImageViewShowBusinessDetailsActivity = null;
    private ImageView BusinessImageImageViewShowBusinessDetailsActivity = null;
    private SwitchCompat IsOpenSwitchShowBusinessDetailsActivity = null;
    private SwitchCompat HasDeliverySwitchShowBusinessDetailsActivity = null;
    private RatingBar RatingBusinessRatingBarShowBusinessDetailsActivity = null;
    private TextViewPersian BusinessNameTextViewShowBusinessDetailsActivity = null;
    private TextViewPersian BusinessJobTitleTextViewShowBusinessDetailsActivity = null;
    private TextViewPersian CategoryNameTextViewShowBusinessDetailsActivity = null;
    private TextViewPersian KeywordTextViewShowBusinessDetailsActivity = null;
    private TextViewPersian EstablishmentTextViewShowBusinessDetailsActivity = null;
    private TextViewPersian CountOfEmployeesTextViewShowBusinessDetailsActivity = null;
    private TextViewPersian AddressTextViewShowBusinessDetailsActivity = null;
    private TextViewPersian PostalCodeTextViewShowBusinessDetailsActivity = null;

    private Dialog ShowBusinessDetailsRatingDialog;
    private int BusinessId;
    private boolean IsSwipe = false;
    private GoogleMap mMap;

    private String Description;
    private AccountViewModel AccountModel;
    private boolean IsBookmark = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_business_details);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.SHOW_PRODUCT_BUSINESS_ACTIVITY);

        BusinessId = getIntent().getExtras().getInt("BusinessId");
        AccountRepository accountRepository = new AccountRepository(this);
        AccountModel = accountRepository.getAccount();

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.business_details);

        //ایجاد طرح بندی صفحه
        CreateLayout();
        //دریافت اطلاعات از سرور
        LoadData();
    }

    /**
     * در صورتی که در ارتباط با اینترنت مشکلی بوجود آمده و کاربر دکمه تلاش مجدد را فشار داده است
     */
    private void RetryButtonOnClick() {
        //دریافت اطلاعات
        LoadData();
    }

    /**
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    private void CreateLayout() {
        RefreshShowBusinessDetailsSwipeRefreshLayoutShowBusinessDetailsActivity = findViewById(R.id.RefreshShowBusinessDetailsSwipeRefreshLayoutShowBusinessDetailsActivity);
        BusinessImageImageViewShowBusinessDetailsActivity = findViewById(R.id.BusinessImageImageViewShowBusinessDetailsActivity);
        BookmarkImageViewShowBusinessDetailsActivity = findViewById(R.id.BookmarkImageViewShowBusinessDetailsActivity);
        ShareImageViewShowBusinessDetailsActivity = findViewById(R.id.ShareImageViewShowBusinessDetailsActivity);
        RelativeLayout commentUserRelativeLayoutShowBusinessDetailsActivity = findViewById(R.id.CommentUserRelativeLayoutShowBusinessDetailsActivity);
        RelativeLayout descriptionRelativeLayoutShowBusinessDetailsActivity = findViewById(R.id.DescriptionRelativeLayoutShowBusinessDetailsActivity);
        CategoryNameTextViewShowBusinessDetailsActivity = findViewById(R.id.CategoryNameTextViewShowBusinessDetailsActivity);
        IsOpenSwitchShowBusinessDetailsActivity = findViewById(R.id.IsOpenSwitchShowBusinessDetailsActivity);
        BusinessNameTextViewShowBusinessDetailsActivity = findViewById(R.id.BusinessNameTextViewShowBusinessDetailsActivity);
        BusinessJobTitleTextViewShowBusinessDetailsActivity = findViewById(R.id.BusinessJobTitleTextViewShowBusinessDetailsActivity);
        TextViewPersian commentIconTextViewShowBusinessDetailsActivity = findViewById(R.id.CommentIconTextViewShowBusinessDetailsActivity);
        TextViewPersian descriptionIconTextViewShowBusinessDetailsActivity = findViewById(R.id.DescriptionIconTextViewShowBusinessDetailsActivity);
        KeywordTextViewShowBusinessDetailsActivity = findViewById(R.id.KeywordTextViewShowBusinessDetailsActivity);
        EstablishmentTextViewShowBusinessDetailsActivity = findViewById(R.id.EstablishmentTextViewShowBusinessDetailsActivity);
        CountOfEmployeesTextViewShowBusinessDetailsActivity = findViewById(R.id.CountOfEmployeesTextViewShowBusinessDetailsActivity);
        HasDeliverySwitchShowBusinessDetailsActivity = findViewById(R.id.HasDeliverySwitchShowBusinessDetailsActivity);
        RelativeLayout contactRelativeLayoutShowBusinessDetailsActivity = findViewById(R.id.ContactRelativeLayoutShowBusinessDetailsActivity);
        TextViewPersian contactIconTextViewShowBusinessDetailsActivity = findViewById(R.id.ContactIconTextViewShowBusinessDetailsActivity);
        RelativeLayout openTimeRelativeLayoutShowBusinessDetailsActivity = findViewById(R.id.OpenTimeRelativeLayoutShowBusinessDetailsActivity);
        TextViewPersian openTimeIconTextViewShowBusinessDetailsActivity = findViewById(R.id.OpenTimeIconTextViewShowBusinessDetailsActivity);
        AddressTextViewShowBusinessDetailsActivity = findViewById(R.id.AddressTextViewShowBusinessDetailsActivity);
        PostalCodeTextViewShowBusinessDetailsActivity = findViewById(R.id.PostalCodeTextViewShowBusinessDetailsActivity);
        RelativeLayout ratingRelativeLayoutShowBusinessDetailsActivity = findViewById(R.id.RatingRelativeLayoutShowBusinessDetailsActivity);
        TextViewPersian ratingIconTextViewShowBusinessDetailsActivity = findViewById(R.id.RatingIconTextViewShowBusinessDetailsActivity);
        RelativeLayout showProductRelativeLayoutShowBusinessDetailsActivity = findViewById(R.id.ShowProductRelativeLayoutShowBusinessDetailsActivity);
        RelativeLayout posterRelativeLayoutShowBusinessDetailsActivity = findViewById(R.id.PosterRelativeLayoutShowBusinessDetailsActivity);
        RatingBusinessRatingBarShowBusinessDetailsActivity = findViewById(R.id.RatingBusinessRatingBarShowBusinessDetailsActivity);
        ButtonPersianView introducingBusinessButtonShowBusinessDetailsActivity = findViewById(R.id.IntroducingBusinessButtonShowBusinessDetailsActivity);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        commentIconTextViewShowBusinessDetailsActivity.setTypeface(Font.MasterIcon);
        commentIconTextViewShowBusinessDetailsActivity.setText("\uf27a");

        descriptionIconTextViewShowBusinessDetailsActivity.setTypeface(Font.MasterIcon);
        descriptionIconTextViewShowBusinessDetailsActivity.setText("\uf15c");

        contactIconTextViewShowBusinessDetailsActivity.setTypeface(Font.MasterIcon);
        contactIconTextViewShowBusinessDetailsActivity.setText("\uf095");

        openTimeIconTextViewShowBusinessDetailsActivity.setTypeface(Font.MasterIcon);
        openTimeIconTextViewShowBusinessDetailsActivity.setText("\uf017");

        ratingIconTextViewShowBusinessDetailsActivity.setTypeface(Font.MasterIcon);
        ratingIconTextViewShowBusinessDetailsActivity.setText("\uf005");

        BusinessImageImageViewShowBusinessDetailsActivity.getLayoutParams().height = LayoutUtility.GetWidthAccordingToScreen(this, 2);
        BusinessImageImageViewShowBusinessDetailsActivity.getLayoutParams().width = LayoutUtility.GetWidthAccordingToScreen(this, 1);

        IsOpenSwitchShowBusinessDetailsActivity.setClickable(false);
        HasDeliverySwitchShowBusinessDetailsActivity.setClickable(false);

        RatingBusinessRatingBarShowBusinessDetailsActivity.setMax(5);
        RatingBusinessRatingBarShowBusinessDetailsActivity.setStepSize(0.01f);

        RefreshShowBusinessDetailsSwipeRefreshLayoutShowBusinessDetailsActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                IsSwipe = true;
                LoadData();
            }
        });

        openTimeRelativeLayoutShowBusinessDetailsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowLoadingProgressBar();
                BusinessOpenTimeService businessOpenTimeService = new BusinessOpenTimeService(ShowBusinessDetailsActivity.this);
                businessOpenTimeService.GetAll(BusinessId);
            }
        });

        contactRelativeLayoutShowBusinessDetailsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowLoadingProgressBar();
                BusinessContactService businessContactService = new BusinessContactService(ShowBusinessDetailsActivity.this);
                businessContactService.GetAll(BusinessId);
            }
        });

        ratingRelativeLayoutShowBusinessDetailsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowLoadingProgressBar();
                ScoreService ScoreService = new ScoreService(ShowBusinessDetailsActivity.this);
                ScoreService.Get(BusinessId);
            }
        });

        commentUserRelativeLayoutShowBusinessDetailsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ShowCommentBusinessIntent = NewIntent(ShowCommentBusinessActivity.class);
                ShowCommentBusinessIntent.putExtra("BusinessName", BusinessNameTextViewShowBusinessDetailsActivity.getText().toString());
                ShowCommentBusinessIntent.putExtra("TotalScore", RatingBusinessRatingBarShowBusinessDetailsActivity.getRating());
                ShowCommentBusinessIntent.putExtra("BusinessId", BusinessId);
                startActivity(ShowCommentBusinessIntent);
            }
        });

        descriptionRelativeLayoutShowBusinessDetailsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent DescriptionBusinessDetailsIntent = NewIntent(DescriptionBusinessDetailsActivity.class);
                DescriptionBusinessDetailsIntent.putExtra("Description", Description);
                startActivity(DescriptionBusinessDetailsIntent);
            }
        });

        BookmarkImageViewShowBusinessDetailsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowLoadingProgressBar();
                BookmarkService BookmarkService = new BookmarkService(ShowBusinessDetailsActivity.this);

                if (IsBookmark) {
                    BookmarkService.Delete(BusinessId);
                } else {
                    BookmarkOutViewModel BookmarkOutViewModel = new BookmarkOutViewModel();
                    BookmarkOutViewModel.setBusinessId(AccountModel.getUser().getId());
                    BookmarkOutViewModel.setBusinessId(BusinessId);
                    BookmarkService.Add(BookmarkOutViewModel);
                }

            }
        });

        showProductRelativeLayoutShowBusinessDetailsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ShowProductListIntent = NewIntent(ShowProductListActivity.class);
                ShowProductListIntent.putExtra("BusinessId", BusinessId);
                startActivity(ShowProductListIntent);

            }
        });

        posterRelativeLayoutShowBusinessDetailsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ShowBusinessPosterListIntent = NewIntent(ShowBusinessPosterListActivity.class);
                ShowBusinessPosterListIntent.putExtra("BusinessId",BusinessId);
                startActivity(ShowBusinessPosterListIntent);

            }
        });

        introducingBusinessButtonShowBusinessDetailsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CommissionIntent = NewIntent(CommissionActivity.class);
                CommissionIntent.putExtra("BusinessId", BusinessId);
                CommissionIntent.putExtra("BusinessName", BusinessNameTextViewShowBusinessDetailsActivity.getText().toString());
                startActivity(CommissionIntent);
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    /**
     * دریافت اطلاعات نحوای جهت پر کردن Recycle
     */
    public void LoadData() {
        if (!IsSwipe)
            ShowLoadingProgressBar();

        BusinessService BusinessService = new BusinessService(this);
        BusinessService.Get(BusinessId);
    }


    /**
     * @param Data
     * @param ServiceMethod
     * @param <T>
     */
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        HideLoading();
        RefreshShowBusinessDetailsSwipeRefreshLayoutShowBusinessDetailsActivity.setRefreshing(false);
        IsSwipe = false;

        try {
            if (ServiceMethod == ServiceMethodType.BusinessGet) {
                Feedback<BusinessViewModel> FeedBack = (Feedback<BusinessViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    BusinessViewModel ViewModel = FeedBack.getValue();

                    if (ViewModel != null) {
                        //پر کردن ویو با اطلاعات دریافتی
                        SetBusinessToView(ViewModel);
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
            } else if (ServiceMethod == ServiceMethodType.BusinessOpenTimeGetAll) {
                Feedback<List<BusinessOpenTimeViewModel>> FeedBack = (Feedback<List<BusinessOpenTimeViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    List<BusinessOpenTimeViewModel> ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        //تنظیمات مربوط به recycle کسب و کار
                        if (ViewModel.size() > 0)
                            ShowDialogBusinessOpenTime(ViewModel);
                        else
                            ShowToast(getResources().getString(R.string.not_registered_hours_for_the_business), Toast.LENGTH_LONG, MessageType.Info);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.BusinessContactGetAll) {
                Feedback<List<BusinessContactViewModel>> FeedBack = (Feedback<List<BusinessContactViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    List<BusinessContactViewModel> ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        //تنظیمات مربوط به recycle کسب و کار
                        if (ViewModel.size() > 0)
                            ShowDialogBusinessContact(ViewModel);
                        else
                            ShowToast(getResources().getString(R.string.not_registered_contacts_for_the_business), Toast.LENGTH_LONG, MessageType.Info);
                    }

                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.BusinessScoreAdd) {
                Feedback<ScoreViewModel> FeedBack = (Feedback<ScoreViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.UpdatedSuccessful.getId()) {

                    ScoreViewModel ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    }

                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.BusinessScoreGet) {
                Feedback<Double> FeedBack = (Feedback<Double>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Double Score = FeedBack.getValue();

                    ShowDialogBusinessRating(Score);

                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.BookmarkDelete) {
                Feedback<BookmarkViewModel> FeedBack = (Feedback<BookmarkViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.DeletedSuccessful.getId()) {
                    Static.IsRefreshBookmark = true;
                    BookmarkViewModel ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        IsBookmark = false;
                        BookmarkImageViewShowBusinessDetailsActivity.setImageResource(R.drawable.ic_bookmark_empty_24dp);
                    } else {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    }

                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.BookmarkAdd) {
                Feedback<BookmarkViewModel> FeedBack = (Feedback<BookmarkViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.RegisteredSuccessful.getId()) {
                    Static.IsRefreshBookmark = true;
                    BookmarkViewModel ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        IsBookmark = true;
                        BookmarkImageViewShowBusinessDetailsActivity.setImageResource(R.drawable.ic_bookmark_full_24dp);
                    } else {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
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

    private void SetBusinessToView(final BusinessViewModel ViewModel) {

        BusinessNameTextViewShowBusinessDetailsActivity.setText(ViewModel.getTitle());
        BusinessJobTitleTextViewShowBusinessDetailsActivity.setText(ViewModel.getJobTitle());
        CategoryNameTextViewShowBusinessDetailsActivity.setText(ViewModel.getBusinessCategoryName());
        IsOpenSwitchShowBusinessDetailsActivity.setChecked(ViewModel.isOpen());
        HasDeliverySwitchShowBusinessDetailsActivity.setChecked(ViewModel.isHasDelivery());
        EstablishmentTextViewShowBusinessDetailsActivity.setText(ViewModel.getEstablishment());
        CountOfEmployeesTextViewShowBusinessDetailsActivity.setText(String.valueOf(ViewModel.getCountOfEmployees()));

        Description = ViewModel.getDescription();

        if (!ViewModel.getKeyword().equals(""))
            KeywordTextViewShowBusinessDetailsActivity.setText(ViewModel.getKeyword());
        else
            KeywordTextViewShowBusinessDetailsActivity.setText("-");

        if (!ViewModel.getPostalCode().equals(""))
            PostalCodeTextViewShowBusinessDetailsActivity.setText(ViewModel.getPostalCode());
        else
            PostalCodeTextViewShowBusinessDetailsActivity.setText("-");

        String Address;
        if (!ViewModel.getRegionName().equals(""))
            Address = ViewModel.getRegionName() + " - " + ViewModel.getAddress();
        else
            Address = ViewModel.getAddress();
        AddressTextViewShowBusinessDetailsActivity.setText(Address);

        String ProductImage;
        if (!ViewModel.getImagePathUrl().equals("")) {
            if (ViewModel.getImagePathUrl().contains("~")) {
                ProductImage = ViewModel.getImagePathUrl().replace("~", DefaultConstant.BaseUrlWebService);
            } else {
                ProductImage = ViewModel.getImagePathUrl();
            }
            LayoutUtility.LoadImageWithGlide(this, ProductImage, BusinessImageImageViewShowBusinessDetailsActivity, LayoutUtility.GetWidthAccordingToScreen(this, 1), LayoutUtility.GetWidthAccordingToScreen(this, 2));
        } else {
            BusinessImageImageViewShowBusinessDetailsActivity.setImageResource(R.drawable.image_default);
        }

        String Rating = String.valueOf(ViewModel.getTotalScore());
        RatingBusinessRatingBarShowBusinessDetailsActivity.setRating(Float.parseFloat(Rating));
        RatingBusinessRatingBarShowBusinessDetailsActivity.invalidate();

        IsBookmark = ViewModel.isBookmarked();
        if (IsBookmark)
            BookmarkImageViewShowBusinessDetailsActivity.setImageResource(R.drawable.ic_bookmark_full_24dp);
        else
            BookmarkImageViewShowBusinessDetailsActivity.setImageResource(R.drawable.ic_bookmark_empty_24dp);


        MarkerOptions options = new MarkerOptions();
        options.position(new LatLng(ViewModel.getLatitude(), ViewModel.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        mMap.addMarker(options);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(ViewModel.getLatitude(), ViewModel.getLongitude())).zoom(17).build();
        mMap.animateCamera(
                CameraUpdateFactory.newCameraPosition(cameraPosition),
                new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                    }

                    @Override
                    public void onCancel() {
                    }
                }
        );

        ShareImageViewShowBusinessDetailsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareView(ViewModel);
            }
        });


    }

    private void ShowDialogBusinessOpenTime(List<BusinessOpenTimeViewModel> ViewModel) {


        final Dialog ShowBusinessDetailsDialog = new Dialog(ShowBusinessDetailsActivity.this);
        ShowBusinessDetailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ShowBusinessDetailsDialog.setContentView(R.layout.dialog_business_open_time);

        RecyclerView BusinessOpenTimeRecyclerViewShowBusinessDetailsActivity = ShowBusinessDetailsDialog.findViewById(R.id.BusinessOpenTimeRecyclerViewShowBusinessDetailsActivity);
        BusinessDetailsOpenTimeRecyclerViewAdapter businessDetailsOpenTimeRecyclerViewAdapter = new BusinessDetailsOpenTimeRecyclerViewAdapter(ShowBusinessDetailsActivity.this, R.layout.recycler_view_dialog_open_time, ViewModel, BusinessOpenTimeRecyclerViewShowBusinessDetailsActivity);
        LinearLayoutManager BusinessOpenTimeLinearLayoutManager = new LinearLayoutManager(ShowBusinessDetailsActivity.this);
        BusinessOpenTimeRecyclerViewShowBusinessDetailsActivity.setLayoutManager(BusinessOpenTimeLinearLayoutManager);
        BusinessOpenTimeRecyclerViewShowBusinessDetailsActivity.setAdapter(businessDetailsOpenTimeRecyclerViewAdapter);

        ShowBusinessDetailsDialog.show();
    }

    private void ShowDialogBusinessContact(List<BusinessContactViewModel> ViewModel) {


        final Dialog ShowBusinessDetailsDialog = new Dialog(ShowBusinessDetailsActivity.this);
        ShowBusinessDetailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ShowBusinessDetailsDialog.setContentView(R.layout.dialog_business_contact);

        TextViewPersian HeaderTextViewShowBusinessDetailsActivity = ShowBusinessDetailsDialog.findViewById(R.id.HeaderTextViewShowBusinessDetailsActivity);
        HeaderTextViewShowBusinessDetailsActivity.getLayoutParams().width =LayoutUtility.GetWidthAccordingToScreen(ShowBusinessDetailsActivity.this, 1);
        RecyclerView BusinessContactRecyclerViewShowBusinessDetailsActivity = ShowBusinessDetailsDialog.findViewById(R.id.BusinessContactRecyclerViewShowBusinessDetailsActivity);
        BusinessDetailsContactRecyclerViewAdapter businessDetailsContactRecyclerViewAdapter = new BusinessDetailsContactRecyclerViewAdapter(ShowBusinessDetailsActivity.this, ViewModel, BusinessContactRecyclerViewShowBusinessDetailsActivity);
        LinearLayoutManager BusinessOpenTimeLinearLayoutManager = new LinearLayoutManager(ShowBusinessDetailsActivity.this);
        BusinessContactRecyclerViewShowBusinessDetailsActivity.setLayoutManager(BusinessOpenTimeLinearLayoutManager);
        BusinessContactRecyclerViewShowBusinessDetailsActivity.setAdapter(businessDetailsContactRecyclerViewAdapter);

        ShowBusinessDetailsDialog.show();
    }

    private void ShowDialogBusinessRating(final Double Score) {

        ShowBusinessDetailsRatingDialog = new Dialog(ShowBusinessDetailsActivity.this);
        ShowBusinessDetailsRatingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ShowBusinessDetailsRatingDialog.setContentView(R.layout.dialog_business_rating);

        ButtonPersianView SaveRatingButtonDialogBusinessRating = ShowBusinessDetailsRatingDialog.findViewById(R.id.SaveRatingButtonDialogBusinessRating);
        ButtonPersianView CancelRatingButtonDialogBusinessRating = ShowBusinessDetailsRatingDialog.findViewById(R.id.CancelRatingButtonDialogBusinessRating);
        RatingBar RatingBusinessRatingBarDialogBusinessRating = ShowBusinessDetailsRatingDialog.findViewById(R.id.RatingBusinessRatingBarDialogBusinessRating);
        TextViewPersian HeaderTextViewDialogBusinessRating = ShowBusinessDetailsRatingDialog.findViewById(R.id.HeaderTextViewDialogBusinessRating);
        HeaderTextViewDialogBusinessRating.getLayoutParams().width = LayoutUtility.GetWidthAccordingToScreen(ShowBusinessDetailsActivity.this, 1);

        if (Score == null) {
            RatingBusinessRatingBarDialogBusinessRating.setRating(0);
            RatingBusinessRatingBarDialogBusinessRating.invalidate();
        } else {
            String Rating = String.valueOf(Score);
            RatingBusinessRatingBarDialogBusinessRating.setRating(Float.parseFloat(Rating));
            RatingBusinessRatingBarDialogBusinessRating.invalidate();
        }

        final ScoreInViewModel ViewModel = new ScoreInViewModel();

        RatingBusinessRatingBarDialogBusinessRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                double ScoreDialog = Double.parseDouble(String.valueOf(rating));
                ViewModel.setScore(ScoreDialog);
            }
        });

        SaveRatingButtonDialogBusinessRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    ViewModel.setBusinessId(BusinessId);
                    ViewModel.setUserId(AccountModel.getUser().getId());

                } catch (Exception ignored) {
                }

                ScoreService ScoreService = new ScoreService(ShowBusinessDetailsActivity.this);
                ShowLoadingProgressBar();
                ScoreService.Add(ViewModel);

                ShowBusinessDetailsRatingDialog.dismiss();
            }
        });


        CancelRatingButtonDialogBusinessRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowBusinessDetailsRatingDialog.dismiss();
            }
        });

        ShowBusinessDetailsRatingDialog.show();
    }

    private void ShareView(BusinessViewModel ViewModel) {
        Intent intent;
        intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);

        String ShareMessage ;
        ShareMessage = ViewModel.getTitle() + "\n" + ViewModel.getJobTitle() + "\n";
        ShareMessage = ShareMessage + Html.fromHtml(Description).toString();

        String ProductImage ;
        if (!ViewModel.getImagePathUrl().equals("")) {
            if (ViewModel.getImagePathUrl().contains("~")) {
                ProductImage = ViewModel.getImagePathUrl().replace("~", DefaultConstant.BaseUrlWebService);
            } else {
                ProductImage = ViewModel.getImagePathUrl();
            }
        } else {
            ProductImage = "";
        }

        if (ProductImage.trim() != "") {
            String CurrentImageText = getResources().getString(R.string.image_product);
            ShareMessage = ShareMessage +  Html.fromHtml(Description).toString() + "\n" + CurrentImageText + "\n" + ProductImage;
        }

        intent.putExtra(Intent.EXTRA_TEXT, ShareMessage);

        intent.setType("text/plain");
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
