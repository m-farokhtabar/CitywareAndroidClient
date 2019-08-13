package ir.rayas.app.citywareclient.View.MasterChildren;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.Service.Business.BookmarkService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Poster.PosterService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.View.Share.AddQuickBasketActivity;
import ir.rayas.app.citywareclient.View.Share.CommissionActivity;
import ir.rayas.app.citywareclient.ViewModel.Business.BookmarkOutViewModel;
import ir.rayas.app.citywareclient.ViewModel.Business.BookmarkViewModel;
import ir.rayas.app.citywareclient.ViewModel.Poster.PurchasedPosterWithBookmarkStatusViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;

public class ShowBusinessPosterDetailsActivity extends BaseActivity implements IResponseService, ILoadData {

    private SwipeRefreshLayout RefreshShowBusinessPosterDetailsSwipeRefreshLayoutShowBusinessPosterDetailsActivity;
    private ImageView BusinessImagePosterImageViewShowBusinessPosterDetailsActivity = null;
    private ImageView BookmarkImageViewShowBusinessPosterActivity = null;
    private TextViewPersian TitleBusinessPosterTextViewShowBusinessPosterActivity = null;
    private TextViewPersian AbstractBusinessPosterTextViewShowBusinessPosterActivity = null;
    private TextViewPersian ReadMoreDescriptionPosterTextViewShowBusinessPosterDetailsActivity = null;
    private WebView DescriptionWebViewShowBusinessPosterDetailsActivity = null;
    private FloatingActionButton FullScreenFloatingActionButtonShowBusinessPosterActivity = null;

    private int PosterId = 0;
    private boolean IsSwipe = false;
    private String PosterImage = "";
    private boolean IsFullScreen = false;
    private int BusinessId = 0;
    private String BusinessName = "";
    private boolean IsBookmark = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_business_poster_details);
        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.DESCRIPTION_BUSINESS_POSTER_DETAILS_ACTIVITY);

        PosterId = getIntent().getExtras().getInt("PosterId");

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.show_poster_details);

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
        RefreshShowBusinessPosterDetailsSwipeRefreshLayoutShowBusinessPosterDetailsActivity = findViewById(R.id.RefreshShowBusinessPosterDetailsSwipeRefreshLayoutShowBusinessPosterDetailsActivity);
        BusinessImagePosterImageViewShowBusinessPosterDetailsActivity = findViewById(R.id.BusinessImagePosterImageViewShowBusinessPosterDetailsActivity);
        TitleBusinessPosterTextViewShowBusinessPosterActivity = findViewById(R.id.TitleBusinessPosterTextViewShowBusinessPosterActivity);
        AbstractBusinessPosterTextViewShowBusinessPosterActivity = findViewById(R.id.AbstractBusinessPosterTextViewShowBusinessPosterActivity);
        DescriptionWebViewShowBusinessPosterDetailsActivity = findViewById(R.id.DescriptionWebViewShowBusinessPosterDetailsActivity);
        ReadMoreDescriptionPosterTextViewShowBusinessPosterDetailsActivity = findViewById(R.id.ReadMoreDescriptionPosterTextViewShowBusinessPosterDetailsActivity);
        FullScreenFloatingActionButtonShowBusinessPosterActivity = findViewById(R.id.FullScreenFloatingActionButtonShowBusinessPosterActivity);
        final LinearLayout LineTopLinearLayoutShowBusinessPosterDetailsActivity = findViewById(R.id.LineTopLinearLayoutShowBusinessPosterDetailsActivity);
        final LinearLayout LineBottomLinearLayoutShowBusinessPosterDetailsActivity = findViewById(R.id.LineBottomLinearLayoutShowBusinessPosterDetailsActivity);
        final RelativeLayout TitleAndAbstractRelativeLayoutShowBusinessPosterDetailsActivity = findViewById(R.id.TitleAndAbstractRelativeLayoutShowBusinessPosterDetailsActivity);
        final CardView DescriptionCardViewShowBusinessPosterDetailsActivity = findViewById(R.id.DescriptionCardViewShowBusinessPosterDetailsActivity);
        ButtonPersianView InformationShowButtonShowBusinessPosterDetailsActivity = findViewById(R.id.InformationShowButtonShowBusinessPosterDetailsActivity);
        ButtonPersianView IntroducingBusinessButtonShowBusinessPosterDetailsActivity = findViewById(R.id.IntroducingBusinessButtonShowBusinessPosterDetailsActivity);
        ButtonPersianView QuickItemsButtonShowBusinessPosterDetailsActivity = findViewById(R.id.QuickItemsButtonShowBusinessPosterDetailsActivity);
        BookmarkImageViewShowBusinessPosterActivity = findViewById(R.id.BookmarkImageViewShowBusinessPosterActivity);


        BusinessImagePosterImageViewShowBusinessPosterDetailsActivity.getLayoutParams().height = LayoutUtility.GetWidthAccordingToScreen(this, 2);
        BusinessImagePosterImageViewShowBusinessPosterDetailsActivity.getLayoutParams().width = LayoutUtility.GetWidthAccordingToScreen(this, 1);


        BookmarkImageViewShowBusinessPosterActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowLoadingProgressBar();
                BookmarkService BookmarkService = new BookmarkService(ShowBusinessPosterDetailsActivity.this);

                if (IsBookmark) {
                    BookmarkService.Delete(BusinessId);
                } else {
                    AccountRepository AccountRepository = new AccountRepository(null);
                    AccountViewModel AccountModel = AccountRepository.getAccount();
                    BookmarkOutViewModel BookmarkOutViewModel = new BookmarkOutViewModel();
                    BookmarkOutViewModel.setUserId(AccountModel.getUser().getId());
                    BookmarkOutViewModel.setBusinessId(BusinessId);
                    BookmarkService.Add(BookmarkOutViewModel);
                }
            }
        });


        RefreshShowBusinessPosterDetailsSwipeRefreshLayoutShowBusinessPosterDetailsActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                IsSwipe = true;
                LoadData();
            }
        });

        FullScreenFloatingActionButtonShowBusinessPosterActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IsFullScreen = !IsFullScreen;
                if (IsFullScreen) {
                    FullScreenFloatingActionButtonShowBusinessPosterActivity.setImageResource(R.drawable.ic_fullscreen_exit_24dp);
                    LineTopLinearLayoutShowBusinessPosterDetailsActivity.setVisibility(View.GONE);
                    TitleAndAbstractRelativeLayoutShowBusinessPosterDetailsActivity.setVisibility(View.GONE);
                    LineBottomLinearLayoutShowBusinessPosterDetailsActivity.setVisibility(View.GONE);
                    DescriptionCardViewShowBusinessPosterDetailsActivity.setVisibility(View.GONE);

                    BusinessImagePosterImageViewShowBusinessPosterDetailsActivity.getLayoutParams().height = LayoutUtility.GetHeightAccordingToScreen(ShowBusinessPosterDetailsActivity.this, 1);
                    BusinessImagePosterImageViewShowBusinessPosterDetailsActivity.getLayoutParams().width = LayoutUtility.GetWidthAccordingToScreen(ShowBusinessPosterDetailsActivity.this, 1);
                    if (PosterImage.equals("")) {
                        BusinessImagePosterImageViewShowBusinessPosterDetailsActivity.setImageResource(R.drawable.image_default);
                    } else {
                        LayoutUtility.LoadImageWithGlide(ShowBusinessPosterDetailsActivity.this, PosterImage, BusinessImagePosterImageViewShowBusinessPosterDetailsActivity, LayoutUtility.GetWidthAccordingToScreen(ShowBusinessPosterDetailsActivity.this, 1), LayoutUtility.GetHeightAccordingToScreen(ShowBusinessPosterDetailsActivity.this, 1));

                    }
                } else {
                    FullScreenFloatingActionButtonShowBusinessPosterActivity.setImageResource(R.drawable.ic_fullscreen_24dp);
                    LineTopLinearLayoutShowBusinessPosterDetailsActivity.setVisibility(View.VISIBLE);
                    TitleAndAbstractRelativeLayoutShowBusinessPosterDetailsActivity.setVisibility(View.VISIBLE);
                    LineBottomLinearLayoutShowBusinessPosterDetailsActivity.setVisibility(View.VISIBLE);
                    DescriptionCardViewShowBusinessPosterDetailsActivity.setVisibility(View.VISIBLE);

                    BusinessImagePosterImageViewShowBusinessPosterDetailsActivity.getLayoutParams().height = LayoutUtility.GetWidthAccordingToScreen(ShowBusinessPosterDetailsActivity.this, 2);
                    BusinessImagePosterImageViewShowBusinessPosterDetailsActivity.getLayoutParams().width = LayoutUtility.GetWidthAccordingToScreen(ShowBusinessPosterDetailsActivity.this, 1);
                    if (PosterImage.equals("")) {
                        BusinessImagePosterImageViewShowBusinessPosterDetailsActivity.setImageResource(R.drawable.image_default);
                    } else {
                        LayoutUtility.LoadImageWithGlide(ShowBusinessPosterDetailsActivity.this, PosterImage, BusinessImagePosterImageViewShowBusinessPosterDetailsActivity, LayoutUtility.GetWidthAccordingToScreen(ShowBusinessPosterDetailsActivity.this, 1), LayoutUtility.GetWidthAccordingToScreen(ShowBusinessPosterDetailsActivity.this, 2));

                    }
                }
            }
        });

        InformationShowButtonShowBusinessPosterDetailsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ShowBusinessInfoIntent = NewIntent(ShowBusinessInfoActivity.class);
                ShowBusinessInfoIntent.putExtra("BusinessId", BusinessId);
                startActivity(ShowBusinessInfoIntent);
            }
        });

        IntroducingBusinessButtonShowBusinessPosterDetailsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CommissionIntent = NewIntent(CommissionActivity.class);
                CommissionIntent.putExtra("BusinessId", BusinessId);
                CommissionIntent.putExtra("BusinessName", BusinessName);
                startActivity(CommissionIntent);
            }
        });

        QuickItemsButtonShowBusinessPosterDetailsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent AddQuickBasketIntent = NewIntent(AddQuickBasketActivity.class);
                AddQuickBasketIntent.putExtra("BusinessId", BusinessId);
                startActivity(AddQuickBasketIntent);
            }
        });

    }

    /**
     * دریافت اطلاعات نحوای جهت پر کردن Recycle
     */
    public void LoadData() {
        if (!IsSwipe)
            ShowLoadingProgressBar();

        PosterService PosterService = new PosterService(this);
        PosterService.Get(PosterId);
    }

    /**
     * @param Data
     * @param ServiceMethod
     * @param <T>
     */
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        HideLoading();
        RefreshShowBusinessPosterDetailsSwipeRefreshLayoutShowBusinessPosterDetailsActivity.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.UserPosterGet) {
                Feedback<PurchasedPosterWithBookmarkStatusViewModel> FeedBack = (Feedback<PurchasedPosterWithBookmarkStatusViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    PurchasedPosterWithBookmarkStatusViewModel ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        SetInformationToView(ViewModelList);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {

                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            }  else if (ServiceMethod == ServiceMethodType.BookmarkDelete) {
                Feedback<BookmarkViewModel> FeedBack = (Feedback<BookmarkViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.DeletedSuccessful.getId()) {
                    Static.IsRefreshBookmark = true;
                    BookmarkViewModel ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        IsBookmark = false;
                        BookmarkImageViewShowBusinessPosterActivity.setImageResource(R.drawable.ic_bookmark_empty_24dp);
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
                        BookmarkImageViewShowBusinessPosterActivity.setImageResource(R.drawable.ic_bookmark_full_24dp);
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

    @SuppressLint("SetJavaScriptEnabled")
    private void SetInformationToView(final PurchasedPosterWithBookmarkStatusViewModel ViewModel) {
        TitleBusinessPosterTextViewShowBusinessPosterActivity.setText(ViewModel.getTitle());
        AbstractBusinessPosterTextViewShowBusinessPosterActivity.setText(ViewModel.getAbstractOfDescription());

        BusinessId = ViewModel.getBusinessId();
        BusinessName = ViewModel.getBusinessName();

        if (!ViewModel.getImagePathUrl().equals("")) {
            if (ViewModel.getImagePathUrl().contains("~")) {
                PosterImage = ViewModel.getImagePathUrl().replace("~", DefaultConstant.BaseUrlWebService);
            } else {
                PosterImage = ViewModel.getImagePathUrl();
            }
            LayoutUtility.LoadImageWithGlide(this, PosterImage, BusinessImagePosterImageViewShowBusinessPosterDetailsActivity, LayoutUtility.GetWidthAccordingToScreen(this, 1), LayoutUtility.GetWidthAccordingToScreen(this, 2));
        } else {
            BusinessImagePosterImageViewShowBusinessPosterDetailsActivity.setImageResource(R.drawable.image_default);
        }

        IsBookmark = ViewModel.isBookmark();

        if (IsBookmark)
            BookmarkImageViewShowBusinessPosterActivity.setImageResource(R.drawable.ic_bookmark_full_24dp);
        else
            BookmarkImageViewShowBusinessPosterActivity.setImageResource(R.drawable.ic_bookmark_empty_24dp);

        ReadMoreDescriptionPosterTextViewShowBusinessPosterDetailsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent DescriptionBusinessPosterDetailsIntent = NewIntent(DescriptionBusinessDetailsActivity.class);
                DescriptionBusinessPosterDetailsIntent.putExtra("Description", ViewModel.getDescription());
                startActivity(DescriptionBusinessPosterDetailsIntent);
            }
        });

        DescriptionWebViewShowBusinessPosterDetailsActivity.getSettings().setLoadsImagesAutomatically(true);
        DescriptionWebViewShowBusinessPosterDetailsActivity.getSettings().setJavaScriptEnabled(true);
        String webText = "<html>\n" +
                "<head>\n" +
                "<style type=\"text/css\">\n" +
                "@font-face {\n" +
                "    font-family: MyFont;\n" +
                "    src: url(\"file:///android_asset/fonts/iransanslight.ttf\")\n" +
                "}\n" +
                "body {\n" +
                "    font-family: MyFont;\n" +
                "    color: #ff666666;\n" +
                "    font-size: small;\n" +
                "    text-align: justify;\n" +
                "    direction: rtl;\n" +
                "    padding: 8px;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                ViewModel.getDescription() +
                "</body>\n" +
                "</html>";

        DescriptionWebViewShowBusinessPosterDetailsActivity.loadDataWithBaseURL("", webText, "text/html", "UTF-8", "");

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

