package ir.rayas.app.citywareclient.View.Base;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.Repository.IOnChangeUserAccount;
import ir.rayas.app.citywareclient.Service.Business.BusinessVisitedService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Constant.LayoutConstant;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResultPassing;
import ir.rayas.app.citywareclient.Share.Helper.TypefaceSpan;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.CustomToastView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.View.Initializer.IntroduceActivity;
import ir.rayas.app.citywareclient.View.Master.BookmarkActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.View.Master.ClubUsersActivity;
import ir.rayas.app.citywareclient.View.Master.CommerceActivity;
import ir.rayas.app.citywareclient.View.Master.MainActivity;
import ir.rayas.app.citywareclient.View.Master.NotificationActivity;
import ir.rayas.app.citywareclient.View.Master.SearchActivity;
import ir.rayas.app.citywareclient.View.MasterChildren.SettingActivity;
import ir.rayas.app.citywareclient.View.Master.UserProfileActivity;
import ir.rayas.app.citywareclient.View.Share.BasketActivity;
import ir.rayas.app.citywareclient.View.Share.BusinessListForFactorActivity;
import ir.rayas.app.citywareclient.View.Share.UserFactorListActivity;
import ir.rayas.app.citywareclient.View.UserProfileChildren.PackageActivity;
import ir.rayas.app.citywareclient.View.UserProfileChildren.PosterTypeActivity;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessVisitedOutViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;


public class BaseActivity extends AppCompatActivity implements View.OnClickListener, MenuItem.OnMenuItemClickListener, IOnChangeUserAccount {

    private LinearLayout LoadingLinearLayout = null;
    private ProgressBar LoadingProgressBar = null;
    private CardView LoadingCardView = null;
    private View MasterContainer = null;
    private CustomToastView CustomToast = null;
    //تنظیم نوار ابزار
    private Toolbar CurrentToolbar = null;
    //رویداد مربوط به کلیک بر روی دکمه بازگشت نوار ابزار ساده - جهت اطلاع اکتیویتی که اگر قبل از بستن بخواهند کاری انجام دهنده بتوانند
    private IButtonBackToolbarListener ButtonBackToolbarListenerList = null;
    /**
     * اکتیوتی جاری
     */
    private int CurrentActivityId = -1;
    /**
     * اکتیوتی والد
     */
    private int ParentActivity = ActivityIdList.BACK_TO_PREVIOUS_ACTIVITY;
    /**
     * در صورتی که می خواهیم دکمه Back اکتیوتی را نبندد این گزینه True کنید
     */
    private boolean DoNotFinishActivity = false;

    /**
     * تغییر نحوه خروج از برنامه با دکمه Back
     * @param doNotFinishActivity
     */
    public void setDoNotFinishActivity(boolean doNotFinishActivity) {
        DoNotFinishActivity = doNotFinishActivity;
    }

    /**
     * منوی کشویی
     */
    private NavigationView navigationView = null;
    /**
     * هدر منوی کشویی
     */
    private View HeaderView = null;
    /**
     * هدر منو نام مستعار
     */
    private TextViewPersian DrawerNickNameTextView = null;
    /**
     * هدر منو نام و نام خانوادگی
     */
    private TextViewPersian DrawerFullNameTextView = null;


    /**
     * دریافت متد از اکتیویتی و افزودن آن به این شی تا در زمان اجرای دکمه بازگشت متدهای این لیست صدا زده شود
     * بهتر در رویداد ایجاد اکتیوتی صدا زده شود
     *
     * @param Listener
     */
    public void setButtonBackToolbarListener(IButtonBackToolbarListener Listener) {
        ButtonBackToolbarListenerList = Listener;
    }

    public View getMasterContainer() {
        return MasterContainer;
    }

    public void setCurrentActivityId(int currentActivityId) {
        CurrentActivityId = currentActivityId;
    }

    public int getCurrentActivityId() {
        return CurrentActivityId;
    }

    public int getParentActivity() {
        return ParentActivity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //مشخص می کند با بازگشت از این اکتیویتی به چه اکتیویتی بایستی بریم
        ParentActivity = getIntent().getIntExtra("FromActivityId", ActivityIdList.BACK_TO_PREVIOUS_ACTIVITY);
    }

    protected void InitView(int MasterContainerId, IRetryButtonOnClick ButtonOnClick, int TitleBackToolBarResourceStringId) {
        //نمایش منوی اصلی در صورت وجود در صفحه
        ConfigAndShowBottomMenu();
        //انجام تنظیمات نوار ابزار
        ConfigToolBar();

        // انجام تنظیمات نوار ابزار بازگشت
        ConfigBackToolBar(TitleBackToolBarResourceStringId);

        // انجام تنظیمات نوار ابزار بازگشت و سبد خرید
        ConfigBasketAndBackToolBar(TitleBackToolBarResourceStringId);

        //انجام تنظیمات منوی کشویی
        ConfigDrawerMenu();

        //ایجاد تست سفارشی جهت نمایش پیغام
        CustomToast = new CustomToastView(this);

        //تنظیمات مربوط به دکمه تلاش مجدد در هنگام خطای لودینگ
        ConfigRetryButton(ButtonOnClick);

        LoadingLinearLayout = findViewById(R.id.LoadingLinearLayout);
        LoadingProgressBar = findViewById(R.id.LoadingProgressBar);
        LoadingCardView = findViewById(R.id.LoadingCardView);
        MasterContainer = findViewById(MasterContainerId);
    }

    private void ConfigRetryButton(final IRetryButtonOnClick ButtonOnClick) {
        if (ButtonOnClick != null) {
            Button LoadingButton = findViewById(R.id.LoadingButton);

            if (LoadingButton != null) {
                LoadingButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ButtonOnClick.call();
                    }
                });
            }
        }
    }

    /**
     * تنظیمات نوار ابزار
     */
    private void ConfigToolBar() {
        //تنظیم نوار ابزار
        CurrentToolbar = findViewById(R.id.ToolBar);
        if (CurrentToolbar != null) {
            setSupportActionBar(CurrentToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);


            ImageButton UserSettingImageButton = findViewById(R.id.SettingButtonToolbar);
            UserSettingImageButton.setTag(new WhichViewClicked(2, R.id.SettingButtonToolbar));
            UserSettingImageButton.setOnClickListener(this);

            ImageButton NotificationImageButton = findViewById(R.id.NotificationButtonToolbar);
            NotificationImageButton.setTag(new WhichViewClicked(2, R.id.NotificationButtonToolbar));
            NotificationImageButton.setOnClickListener(this);

            ImageButton FavoriteButtonToolbar = findViewById(R.id.FavoriteButtonToolbar);
            FavoriteButtonToolbar.setTag(new WhichViewClicked(2, R.id.FavoriteButtonToolbar));
            FavoriteButtonToolbar.setOnClickListener(this);

        }
    }


    /**
     * تنظیمات نوار ابزار
     */
    private void ConfigBackToolBar(int TitleBackToolBarResourceStringId) {
        //تنظیم نوار ابزار
        Toolbar BackToolbar = findViewById(R.id.BackToolBar);
        if (BackToolbar != null) {
            setSupportActionBar(BackToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            ImageButton BackImageButtonBackToolbar = findViewById(R.id.BackImageButtonBackToolbar);
            BackImageButtonBackToolbar.setTag(new WhichViewClicked(1, 0));
            BackImageButtonBackToolbar.setOnClickListener(this);
            TextViewPersian TitleBackToolbar = findViewById(R.id.TitleBackToolbar);
            if (TitleBackToolBarResourceStringId != 0)
                TitleBackToolbar.setText(TitleBackToolBarResourceStringId);
            else
                TitleBackToolbar.setText("");
        }
    }


    /**
     * تنظیمات نوار ابزار
     */
    private void ConfigBasketAndBackToolBar(int TitleBackToolBarResourceStringId) {
        //تنظیم نوار ابزار
        Toolbar BasketAndBackToolbar = findViewById(R.id.BasketAndBackToolBar);
        if (BasketAndBackToolbar != null) {
            setSupportActionBar(BasketAndBackToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            ImageButton BackImageButtonBackAndBasketToolbar = findViewById(R.id.BackImageButtonBackAndBasketToolbar);
            BackImageButtonBackAndBasketToolbar.setTag(new WhichViewClicked(4, 0));
            BackImageButtonBackAndBasketToolbar.setOnClickListener(this);
            ImageButton BasketImageButtonBackAndBasketToolbar = findViewById(R.id.BasketImageButtonBackAndBasketToolbar);
            BasketImageButtonBackAndBasketToolbar.setTag(new WhichViewClicked(5, 0));
            BasketImageButtonBackAndBasketToolbar.setOnClickListener(this);
            TextViewPersian TitleTextViewBackAndBasketToolbar = findViewById(R.id.TitleTextViewBackAndBasketToolbar);
            if (TitleBackToolBarResourceStringId != 0)
                TitleTextViewBackAndBasketToolbar.setText(TitleBackToolBarResourceStringId);
            else
                TitleTextViewBackAndBasketToolbar.setText("");
        }
    }

    /**
     * انجام تنظیمات منوی کشویی
     */
    private void ConfigDrawerMenu() {
        //تغییر فونت منوها
        navigationView = findViewById(R.id.DrawerNavigationView);
        if (navigationView != null) {
            HeaderView = navigationView.getHeaderView(0);
            DrawerNickNameTextView = HeaderView.findViewById(R.id.DrawerNickNameTextView);
            DrawerFullNameTextView = HeaderView.findViewById(R.id.DrawerFullNameTextView);
            ButtonPersianView DrawerSingOutButton = HeaderView.findViewById(R.id.DrawerSingOutButton);
            if (DrawerSingOutButton != null) {
                DrawerSingOutButton.setTag(new WhichViewClicked(3, 0));
                DrawerSingOutButton.setOnClickListener(this);
            }

            Menu RootMenu = navigationView.getMenu();
            for (int i = 0; i < RootMenu.size(); i++) {
                //تغییر فونت منوهای برنامه
                MenuItem CurrentMenuItem = RootMenu.getItem(i);
                //تنظیم رویداد کلیک
                CurrentMenuItem.setOnMenuItemClickListener(this);
                SpannableString NewText = new SpannableString(CurrentMenuItem.getTitle());
                NewText.setSpan(new TypefaceSpan(Font.MasterFont), 0, NewText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                CurrentMenuItem.setTitle(NewText);
                //for applying a font to subMenu ...
                SubMenu subMenu = CurrentMenuItem.getSubMenu();
                if (subMenu != null && subMenu.size() > 0) {
                    for (int j = 0; j < subMenu.size(); j++) {
                        MenuItem subMenuItem = subMenu.getItem(j);
                        //تنظیم رویداد کلیک
                        subMenuItem.setOnMenuItemClickListener(this);
                        SpannableString NewTextSunItem = new SpannableString(subMenuItem.getTitle());
                        NewTextSunItem.setSpan(new TypefaceSpan(Font.MasterFont), 0, NewTextSunItem.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        subMenuItem.setTitle(NewTextSunItem);
                    }
                }

            }
            //نمایش اطلاعات کاربر در هدر منوی کشویی
            AccountRepository Repository = new AccountRepository(this);
            UserAccountIsChanged(Repository.getAccount());
        }
    }

    /**
     * رویداد مربوط به گزین های منوی کشویی
     *
     * @param menuItem
     * @return
     */
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.SettingItemMenu:
                Intent SettingIntent = NewIntent(SettingActivity.class);
                startActivity(SettingIntent);
                break;
            case R.id.FavoriteItemMenu:
                if (CurrentActivityId != ActivityIdList.BOOKMARK_ACTIVITY)
                    GoToMasterActivity(ActivityIdList.BOOKMARK_ACTIVITY);
                break;
            case R.id.NotificationsItemMenu:
                if (CurrentActivityId != ActivityIdList.NOTIFICATION_ACTIVITY)
                    GoToMasterActivity(ActivityIdList.NOTIFICATION_ACTIVITY);
                break;

            case R.id.BasketItemMenu:
                if (CurrentActivityId != ActivityIdList.BASKET_ACTIVITY)
                    GoToMasterActivity(ActivityIdList.BASKET_ACTIVITY);
                break;

            case R.id.FactorItemMenu:
                if (CurrentActivityId != ActivityIdList.USER_FACTOR_LIST_ACTIVITY)
                    GoToMasterActivity(ActivityIdList.USER_FACTOR_LIST_ACTIVITY);
                break;

            case R.id.FactorBusinessItemMenu:
                if (CurrentActivityId != ActivityIdList.BUSINESS_LIST_FOR_FACTOR_ACTIVITY)
                    GoToMasterActivity(ActivityIdList.BUSINESS_LIST_FOR_FACTOR_ACTIVITY);
                break;

            case R.id.BuyPackageItemMenu:
                if (CurrentActivityId != ActivityIdList.PACKAGE_ACTIVITY) {
                    Intent NewPackageIntent = NewIntent(PackageActivity.class);
                    NewPackageIntent.putExtra("New", "New");
                    startActivity(NewPackageIntent);
                }
                break;

            case R.id.BuyPosterItemMenu:
                if (CurrentActivityId != ActivityIdList.POSTER_TYPE_ACTIVITY) {
                    Intent NewPosterTypeIntent = NewIntent(PosterTypeActivity.class);
                    startActivity(NewPosterTypeIntent);
                }
                break;

        }
        return false;
    }

    /**
     * رویداد مروبط به دکمه خروج در هدر منوی کشویی
     * اطلاعات کش شده کاربر را حذف می کند
     */
    public void OnDrawerSingOutButton() {
        AccountRepository Repository = new AccountRepository(this);
        Repository.ClearAccount();
    }

    /**
     * به روز رسانی اطلاعات نمایش داده شده در هدر منوی کشویی
     * نام و نام خانوادگی
     * نام مستعار
     */
    @Override
    public void UserAccountIsChanged(AccountViewModel Account) {
        if (navigationView != null && HeaderView != null) {
            if (DrawerNickNameTextView != null && DrawerFullNameTextView != null) {
                //  ---------------------- اپدیت اکانت کاربر  ------------------------------
                if (Account != null && Account.getUser() != null) {
                    DrawerNickNameTextView.setText(Account.getUser().getNickName());
                    DrawerFullNameTextView.setText(String.format("%s %s", Account.getUser().getName(), Account.getUser().getFamily()));
                } else {
                    this.finish();
                    ActivityCompat.finishAffinity(this);
                    //این فرم نیازی به استفاده از متد newintent تدارد به دلیل اینکه انگاز برنامه از نو اجرا شده است
                    Intent IntroduceIntent = new Intent(this, IntroduceActivity.class);
                    startActivity(IntroduceIntent);
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (CurrentToolbar != null) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.view_toolbar_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int SelectedId = item.getItemId();
        if (CurrentToolbar != null) {
            switch (SelectedId) {
                case R.id.ToolbarOpenDrawerItem:
                    DrawerLayout DrawerLayout = findViewById(R.id.MainDrawerLayout);
                    DrawerLayout.openDrawer(GravityCompat.END);
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * نمایش منوی پایین صفحه
     */
    private void ConfigAndShowBottomMenu() {
        //بررسی وجود منوی پایین صفحه
        LinearLayoutCompat BottomMenu = findViewById(R.id.MainMenuBottom);
        if (BottomMenu != null) {

            //LinearLayoutCompat.LayoutParams MainMenuBottomParam = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT,);
            TextView IconMenu5 = findViewById(R.id.IconMenu5);
            TextView TextMenu5 = findViewById(R.id.TextMenu5);
            LinearLayoutCompat ItemMenu5 = findViewById(R.id.ItemMenu5);
            //MyClub
            ItemMenu5.setOnClickListener(this);
            ItemMenu5.setTag(new WhichViewClicked(0, R.id.ItemMenu5));
            IconMenu5.setTypeface(Font.MasterIcon);
            IconMenu5.setText("\uf0c0");
            //TODO: بعدا باید کد اکتیوتی مورد نظر اضافه شود
            if (CurrentActivityId == ActivityIdList.CLUB_USERS_ACTIVITY) {
                IconMenu5.setTextColor(LayoutUtility.GetColorFromResource(this, R.color.FontThemeColor));
                TextMenu5.setTextColor(LayoutUtility.GetColorFromResource(this, R.color.FontThemeColor));
            } else {
                IconMenu5.setTextColor(LayoutUtility.GetColorFromResource(this, R.color.FontGrayColor));
                TextMenu5.setTextColor(LayoutUtility.GetColorFromResource(this, R.color.FontGrayColor));
            }
            //Commission
            TextView IconMenu4 = findViewById(R.id.IconMenu4);
            TextView TextMenu4 = findViewById(R.id.TextMenu4);
            LinearLayoutCompat ItemMenu4 = findViewById(R.id.ItemMenu4);
            ItemMenu4.setOnClickListener(this);
            ItemMenu4.setTag(new WhichViewClicked(0, R.id.ItemMenu4));
            IconMenu4.setTypeface(Font.MasterIcon);
            IconMenu4.setText("\uf155");
            //TODO: بعدا باید کد اکتیوتی مورد نظر اضافه شود
            if (CurrentActivityId == ActivityIdList.COMMERCE_ACTIVITY) {
                IconMenu4.setTextColor(LayoutUtility.GetColorFromResource(this, R.color.FontThemeColor));
                TextMenu4.setTextColor(LayoutUtility.GetColorFromResource(this, R.color.FontThemeColor));
            } else {
                IconMenu4.setTextColor(LayoutUtility.GetColorFromResource(this, R.color.FontGrayColor));
                TextMenu4.setTextColor(LayoutUtility.GetColorFromResource(this, R.color.FontGrayColor));
            }
            //Profile
            TextView IconMenu3 = findViewById(R.id.IconMenu3);
            TextView TextMenu3 = findViewById(R.id.TextMenu3);
            LinearLayoutCompat ItemMenu3 = findViewById(R.id.ItemMenu3);
            ItemMenu3.setOnClickListener(this);
            ItemMenu3.setTag(new WhichViewClicked(0, R.id.ItemMenu3));
            IconMenu3.setTypeface(Font.MasterIcon);
            IconMenu3.setText("\uf007");
            if (CurrentActivityId == ActivityIdList.USER_PROFILE_ACTIVITY) {
                IconMenu3.setTextColor(LayoutUtility.GetColorFromResource(this, R.color.FontThemeColor));
                TextMenu3.setTextColor(LayoutUtility.GetColorFromResource(this, R.color.FontThemeColor));
            } else {
                IconMenu3.setTextColor(LayoutUtility.GetColorFromResource(this, R.color.FontGrayColor));
                TextMenu3.setTextColor(LayoutUtility.GetColorFromResource(this, R.color.FontGrayColor));
            }
            //Search
            TextView IconMenu2 = findViewById(R.id.IconMenu2);
            TextView TextMenu2 = findViewById(R.id.TextMenu2);
            LinearLayoutCompat ItemMenu2 = findViewById(R.id.ItemMenu2);
            ItemMenu2.setOnClickListener(this);
            ItemMenu2.setTag(new WhichViewClicked(0, R.id.ItemMenu2));
            IconMenu2.setTypeface(Font.MasterIcon);
            IconMenu2.setText("\uf002");
            if (CurrentActivityId == ActivityIdList.SEARCH_ACTIVITY) {
                IconMenu2.setTextColor(LayoutUtility.GetColorFromResource(this, R.color.FontThemeColor));
                TextMenu2.setTextColor(LayoutUtility.GetColorFromResource(this, R.color.FontThemeColor));
            } else {
                IconMenu2.setTextColor(LayoutUtility.GetColorFromResource(this, R.color.FontGrayColor));
                TextMenu2.setTextColor(LayoutUtility.GetColorFromResource(this, R.color.FontGrayColor));
            }
            //Home
            TextView IconMenu1 = findViewById(R.id.IconMenu1);
            TextView TextMenu1 = findViewById(R.id.TextMenu1);
            LinearLayoutCompat ItemMenu1 = findViewById(R.id.ItemMenu1);
            ItemMenu1.setTag(new WhichViewClicked(0, R.id.ItemMenu1));
            ItemMenu1.setOnClickListener(this);
            IconMenu1.setTypeface(Font.MasterIcon);
            IconMenu1.setText("\uf015");
            if (CurrentActivityId == ActivityIdList.MAIN_ACTIVITY) {
                IconMenu1.setTextColor(LayoutUtility.GetColorFromResource(this, R.color.FontThemeColor));
                TextMenu1.setTextColor(LayoutUtility.GetColorFromResource(this, R.color.FontThemeColor));
            } else {
                IconMenu1.setTextColor(LayoutUtility.GetColorFromResource(this, R.color.FontGrayColor));
                TextMenu1.setTextColor(LayoutUtility.GetColorFromResource(this, R.color.FontGrayColor));
            }
        }
    }

    /**
     * لود عنوان صفحه بدون آیکن
     * برای صفحات قبل از صفحه اصلی برنامه استفاده می شود
     */
    public void InitToolbarWithOutButton() {
        ImageView LogoImageView = findViewById(R.id.LogoImageView);

        int ScreenHeight = LayoutUtility.GetHeightAccordingToScreen(this, 10);
        LinearLayoutCompat.LayoutParams LogoImageViewLayoutParams = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenHeight);
        LogoImageView.setLayoutParams(LogoImageViewLayoutParams);

        LayoutUtility.LoadImageWithGlide(this, R.drawable.logo, LogoImageView, LayoutConstant.SplashLogoWidthMaxSize, LayoutConstant.SplashLogoHeightMaxSize);
    }

    /**
     * نمایش پیغام برای اکتیوتی مورد نظر
     *
     * @param Message            متن پیغام
     * @param duration           زمان نمایش
     * @param CurrentMessageType توع آیکن پیغام
     */
    public void ShowToast(String Message, int duration, MessageType CurrentMessageType) {
        if (CustomToast != null)
            CustomToast.Show(Message, duration, CurrentMessageType);
    }


    /**
     * نمایش نوار پیمایش جهت دریافت اطلاعات
     */
    public void ShowLoadingProgressBar() {
        MasterContainer.setVisibility(View.GONE);

        LoadingLinearLayout.setVisibility(View.VISIBLE);
        LoadingProgressBar.setVisibility(View.VISIBLE);
        LoadingCardView.setVisibility(View.GONE);
    }

    /**
     * نمایش پیغام خطا اتصال به اینترنت
     */
    public void ShowErrorInConnectDialog() {
        MasterContainer.setVisibility(View.GONE);

        LoadingLinearLayout.setVisibility(View.VISIBLE);
        LoadingProgressBar.setVisibility(View.VISIBLE);
        LoadingCardView.setVisibility(View.VISIBLE);
    }

    /**
     * نمایش فرم اصلی برنامه
     */
    public void HideLoading() {
        MasterContainer.setVisibility(View.VISIBLE);

        LoadingLinearLayout.setVisibility(View.GONE);
        LoadingProgressBar.setVisibility(View.GONE);
        LoadingCardView.setVisibility(View.GONE);
    }

    /**
     * تمامی رویدادهای کلیک بر روی گزینه ها و منو ها در این تابع خواهد بود
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        WhichViewClicked CurrentViewClicked = (WhichViewClicked) view.getTag();
        // 0 یعنی منوی پایین صفحه
        if (CurrentViewClicked.getViewId() == 0) {
            BottomMenuOnItemClick(CurrentViewClicked.getSubViewId());
        }
        // 1 یعنی دکمه بازگشت مربوط به نوارابزار ساده که فقط همین دکمه را دارد
        if (CurrentViewClicked.getViewId() == 1) {
            BackImageButtonBackToolbarClick();
        }
        // 2 یعنی رویداد های مرتبط با نوار ابزار اصلی - مثل دکمه تنظیمات یا نوتیفیکیشن
        if (CurrentViewClicked.getViewId() == 2) {
            ToolbarOnItemClick(CurrentViewClicked.getSubViewId());
        }
        //3 یعنی بر روی دکمه خروج کاربری در هدر منوی کشویی کلیک شده است
        if (CurrentViewClicked.getViewId() == 3) {
            OnDrawerSingOutButton();
        }

        if (CurrentViewClicked.getViewId() == 4) {
            BackImageButtonBackToolbarClick();
        }

        if (CurrentViewClicked.getViewId() == 5) {
            BasketImageButtonBackAndBasketToolbarClick();
        }
    }

    private void ToolbarOnItemClick(int ViewId) {
        switch (ViewId) {
            //Setting
            case R.id.SettingButtonToolbar:
                Intent SettingIntent = NewIntent(SettingActivity.class);
                startActivity(SettingIntent);
                break;
            //Notification
            case R.id.NotificationButtonToolbar:
                if (CurrentActivityId != ActivityIdList.NOTIFICATION_ACTIVITY)
                    GoToMasterActivity(ActivityIdList.NOTIFICATION_ACTIVITY);
                break;
            //Favorite
            case R.id.FavoriteButtonToolbar:
                if (CurrentActivityId != ActivityIdList.BOOKMARK_ACTIVITY)
                    GoToMasterActivity(ActivityIdList.BOOKMARK_ACTIVITY);
                break;
        }
    }

    /**
     * بسته شدن اکتیویتی فعلی این رویداد مربوط به نوار ابزار ساده است که فقط دکمه بازگشت دارد
     */
    private void BackImageButtonBackToolbarClick() {
        if (ButtonBackToolbarListenerList != null)
            ButtonBackToolbarListenerList.ClickOnButtonBackToolbar();
        if (!DoNotFinishActivity){
            if (IsMasterActivity(ParentActivity))
                GoToMasterActivity(ParentActivity);
            this.finish();
        }
    }

    /**
     * بسته شدن اکتیویتی فعلی این رویداد مربوط به نوار ابزار ساده است که فقط دکمه بازگشت دارد
     */
    private void BasketImageButtonBackAndBasketToolbarClick() {
        Intent BasketIntent = new Intent(this, BasketActivity.class);
        BasketIntent.putExtra("FromActivityId", ActivityIdList.MAIN_ACTIVITY);
        startActivity(BasketIntent);
    }

    /**
     * منوی پایین صفحه اکتیوتی های اصلی
     *
     * @param ViewId
     */
    private void BottomMenuOnItemClick(int ViewId) {
        switch (ViewId) {
            //Home
            case R.id.ItemMenu1:
                if (CurrentActivityId != ActivityIdList.MAIN_ACTIVITY)
                    GoToMasterActivity(ActivityIdList.MAIN_ACTIVITY);
                break;
            //Search
            case R.id.ItemMenu2:
                if (CurrentActivityId != ActivityIdList.SEARCH_ACTIVITY)
                    GoToMasterActivity(ActivityIdList.SEARCH_ACTIVITY);
                break;
            //UserProfile
            case R.id.ItemMenu3:
                if (CurrentActivityId != ActivityIdList.USER_PROFILE_ACTIVITY)
                    GoToMasterActivity(ActivityIdList.USER_PROFILE_ACTIVITY);
                break;
            //Commission
            case R.id.ItemMenu4:
                if (CurrentActivityId != ActivityIdList.COMMERCE_ACTIVITY)
                    GoToMasterActivity(ActivityIdList.COMMERCE_ACTIVITY);
                break;
            //MyClub
            case R.id.ItemMenu5:
                if (CurrentActivityId != ActivityIdList.CLUB_USERS_ACTIVITY)
                    GoToMasterActivity(ActivityIdList.CLUB_USERS_ACTIVITY);
                break;
        }
    }


    /**
     * مدیریت ایجاد اکتیویتی ها
     *
     * @param ActivityId کد اکتیویتی
     */
    protected void GoToMasterActivity(int ActivityId) {
        switch (ActivityId) {
            case ActivityIdList.MAIN_ACTIVITY:
                Intent MainIntent = new Intent(this, MainActivity.class);
                MainIntent.putExtra("FromActivityId", ActivityIdList.APP_NEEDS_USER_BACK_TO_TERMINATE);
                startActivity(MainIntent);
                break;
            case ActivityIdList.USER_PROFILE_ACTIVITY:
                Intent UserProfileIntent = new Intent(this, UserProfileActivity.class);
                UserProfileIntent.putExtra("FromActivityId", ActivityIdList.MAIN_ACTIVITY);
                startActivity(UserProfileIntent);
                break;
            case ActivityIdList.BOOKMARK_ACTIVITY:
                Intent BookmarkIntent = new Intent(this, BookmarkActivity.class);
                BookmarkIntent.putExtra("FromActivityId", ActivityIdList.MAIN_ACTIVITY);
                startActivity(BookmarkIntent);
                break;
            case ActivityIdList.NOTIFICATION_ACTIVITY:
                Intent NotificationIntent = new Intent(this, NotificationActivity.class);
                NotificationIntent.putExtra("FromActivityId", ActivityIdList.MAIN_ACTIVITY);
                startActivity(NotificationIntent);
                break;
            case ActivityIdList.BASKET_ACTIVITY:
                Intent BasketIntent = new Intent(this, BasketActivity.class);
                BasketIntent.putExtra("FromActivityId", ActivityIdList.MAIN_ACTIVITY);
                startActivity(BasketIntent);
                break;

            case ActivityIdList.USER_FACTOR_LIST_ACTIVITY:
                Intent UserFactorListIntent = new Intent(this, UserFactorListActivity.class);
                UserFactorListIntent.putExtra("FromActivityId", ActivityIdList.MAIN_ACTIVITY);
                startActivity(UserFactorListIntent);
                break;

            case ActivityIdList.BUSINESS_LIST_FOR_FACTOR_ACTIVITY:
                Intent BusinessListForFactorIntent = new Intent(this, BusinessListForFactorActivity.class);
                BusinessListForFactorIntent.putExtra("FromActivityId", ActivityIdList.MAIN_ACTIVITY);
                startActivity(BusinessListForFactorIntent);
                break;
            case ActivityIdList.CLUB_USERS_ACTIVITY:
                Intent ClubUsersIntent = new Intent(this, ClubUsersActivity.class);
                ClubUsersIntent.putExtra("FromActivityId", ActivityIdList.MAIN_ACTIVITY);
                startActivity(ClubUsersIntent);
                break;
            case ActivityIdList.COMMERCE_ACTIVITY:
                Intent CommerceIntent = new Intent(this, CommerceActivity.class);
                CommerceIntent.putExtra("FromActivityId", ActivityIdList.MAIN_ACTIVITY);
                startActivity(CommerceIntent);
                break;
            case ActivityIdList.SEARCH_ACTIVITY:
                Intent SearchIntent = new Intent(this, SearchActivity.class);
                SearchIntent.putExtra("FromActivityId", ActivityIdList.MAIN_ACTIVITY);
                startActivity(SearchIntent);
                break;
        }
    }

    /**
     * تعیین این که این اکتیویتی اصلی است یا خیر
     *
     * @return
     */
    private boolean IsMasterActivity(int ActivityId) {
        boolean Output = false;
        //TODO: برای دو اکتیویتی اصلی دیگر نیز باید این قسمت درست شود
        if (ActivityId == ActivityIdList.MAIN_ACTIVITY || ActivityId == ActivityIdList.USER_PROFILE_ACTIVITY ||
                ActivityId == ActivityIdList.BOOKMARK_ACTIVITY || ActivityId == ActivityIdList.NOTIFICATION_ACTIVITY ||
                ActivityId == ActivityIdList.CLUB_USERS_ACTIVITY || ActivityId == ActivityIdList.COMMERCE_ACTIVITY ||
                ActivityId == ActivityIdList.SEARCH_ACTIVITY)
            Output = true;
        return Output;
    }

    //کدهای این قسمت برای شبیه سازی برشگت داده ها از یک اکتیویتی به اکتیویتی دیگه است برای ورژن های 4 به پایین
    //در این ورژن ها اکتیویتی نوه SingleInstance نمی تونه چیزی رو به اکتیویتی دیگه ارسال کنه
    @Override
    protected void onResume() {
        ActivityResult AResult = ActivityResultPassing.Pop();
        if (AResult != null) {
            onGetResult(AResult);
        }
        //نمایش اطلاعات کاربر در هدر منوی کشویی
        AccountRepository Repository = new AccountRepository(this);
        UserAccountIsChanged(Repository.getAccount());
        super.onResume();
    }

    /**
     * این متد بایستی جهت دریافت اطلاعات از یک اکتیویتی به اکتیویتی دیگر override شود
     *
     * @param Result نتیجه ارسالی به اکتیویتی مبدا
     */
    protected void onGetResult(ActivityResult Result) {

    }

    /**
     * بستن اکتیوتی فعلی و در صورت نیاز رفتن به اکتیویتی والد آن
     * این متد به خاطر SingleInstance بودن اکتیوتی اصلی برنامه نئشته شده است
     */
    public void FinishCurrentActivity() {
        GoToMasterActivity(ParentActivity);
        finish();
    }

    /**
     * در صورتی که بخواهیم یک اکتیویتی را صدا بزنیم حتما جهت ساخت Intent از این متد بایستی استفاده شود
     */
    public Intent NewIntent(Class<?> cls) {
        Intent CurrentIntent = new Intent(this, cls);
        CurrentIntent.putExtra("FromActivityId", CurrentActivityId);
        return CurrentIntent;
    }

    /**
     * به محض مشاهده یک کسب و کار توسط کاربر بایستی آمار بازدید آن در سرور ثبت شود
     *
     * @param BusinessId      کد کسب و کار
     * @param ResponseService شی مربوط به دریافت اطلاعات سرویس توسط اکتیویتی که این متد را صدا می زند باید ایجاد شده باش
     */
    public void OnBusinessVisitedByUser(int BusinessId, IResponseService ResponseService) {
        BusinessVisitedService Service = new BusinessVisitedService(ResponseService);
        BusinessVisitedOutViewModel OutViewModel = new BusinessVisitedOutViewModel();
        AccountRepository Repository = new AccountRepository(this);
        AccountViewModel Account = Repository.getAccount();
        if (Account != null && Account.getUser() != null) {
            OutViewModel.setUserId(Account.getUser().getId());
            OutViewModel.setBusinessId(BusinessId);
            Service.Set(OutViewModel);
        }
    }

    @Override
    public void onBackPressed() {
        boolean DrawerIsOpen = false;
        DrawerLayout DrawerLayout = findViewById(R.id.MainDrawerLayout);
        if (DrawerLayout != null) {
            if (DrawerLayout.isDrawerOpen(GravityCompat.END)) {
                DrawerIsOpen = true;
                DrawerLayout.closeDrawer(GravityCompat.END);
            }
        }
        //منوی سمت راست باز نبوده
        if (!DrawerIsOpen) {
            //کاربر باید دوباره دکمه بازگشت را بزند تا از برنامه خارج شود
            if (ParentActivity == ActivityIdList.APP_NEEDS_USER_BACK_TO_TERMINATE) {
                ParentActivity = ActivityIdList.APP_MUST_TERMINATE;
                ShowToast(getResources().getString(R.string.to_exit_app_touch_back_again), Toast.LENGTH_SHORT, MessageType.Info);
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //در صورتی که زمان تایین شده برای بازگشت بگذرد دوباره به وضعیت منتظر زدن بازگشت کاربر می شود
                        ParentActivity = ActivityIdList.APP_NEEDS_USER_BACK_TO_TERMINATE;
                    }
                }, DefaultConstant.TimeForTouchBackAgainToExitApp);
            } else {
                if (ParentActivity == ActivityIdList.APP_MUST_TERMINATE) {
                    //TODO: این قسمت باید کدی نوشته شود که همه اکتیویتی های موجود بسته شود منظورم بیشتر اکتیویتی های اصلی که یک نمونه از ان در تسک های جداگونه ریخته شود
                    super.onBackPressed();
                    this.finish();
                    ActivityCompat.finishAffinity(this);
                } else {
                    if (ParentActivity == ActivityIdList.BACK_TO_PREVIOUS_ACTIVITY)
                        super.onBackPressed();
                    else {
                        if (IsMasterActivity(ParentActivity))
                            GoToMasterActivity(ParentActivity);
                        else
                            super.onBackPressed();
                    }
                }
            }
        }
    }
}
