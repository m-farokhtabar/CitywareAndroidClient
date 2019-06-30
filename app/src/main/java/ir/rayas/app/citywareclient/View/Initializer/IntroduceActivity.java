package ir.rayas.app.citywareclient.View.Initializer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.Service.Etc.EventOrNewsService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Setting.SettingService;
import ir.rayas.app.citywareclient.Service.User.AccountService;
import ir.rayas.app.citywareclient.Service.User.UserAddressService;
import ir.rayas.app.citywareclient.Share.Constant.LayoutConstant;
import ir.rayas.app.citywareclient.Share.Enum.LinkType;
import ir.rayas.app.citywareclient.Share.Enum.PlaceType;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Helper.Gps;
import ir.rayas.app.citywareclient.Share.Helper.IResponseTurnOnGpsDialog;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Login.DescriptionAppActivity;
import ir.rayas.app.citywareclient.View.Login.HowToSearchInAppGpsOrRegionActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.View.Master.MainActivity;
import ir.rayas.app.citywareclient.ViewModel.Etc.EventOrNewsViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.UserAddressViewModel;

public class IntroduceActivity extends BaseActivity implements IResponseService, IResponseTurnOnGpsDialog {

    private ImageView IntroduceBannerImageView = null;
    private int LoadDataCounter = 0;
    private int MaxLoadData = 2;
    /**
     * 0 -> DescriptionAppActivity
     * 1 -> HowToSearchInAppGpsOrRegionActivity
     * 2 -> MainActivity
     */
    private int GoToWhichActivity = 0;
    private String SViewModel = null;
    private EventOrNewsViewModel EViewModel = null;

    public static String ArrayAddressString = "";
    private Gps CurrentGps = null;

    public static String getArrayAddressString() {
        return ArrayAddressString;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                LoadData();
            }
        }, 0);

        //کلاس کنترل و مدیریت GPS
        CurrentGps = new Gps();

        //ایجاد طرح بندی صفحه
        CreateLayout();
        //دریافت اطلاعات از سرور
        LoadData();
    }

    /**
     * رویداد برای تصویر که اگر بر روی آن کلیک کرد به صفحه مورد نظر وارد شود
     */
    private void CreateLayout() {
        IntroduceBannerImageView = findViewById(R.id.IntroduceBannerImageView);
        RelativeLayout.LayoutParams IntroduceBannerImageViewLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutUtility.GetWidthAccordingToScreen(this, 3));
        IntroduceBannerImageViewLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        IntroduceBannerImageViewLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        IntroduceBannerImageView.setLayoutParams(IntroduceBannerImageViewLayoutParams);
        IntroduceBannerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnImageViewOnClick();
            }
        });
        ButtonPersianView introduceButton = findViewById(R.id.IntroduceButton);
        introduceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnButtonClick();
            }
        });
    }

    private void LoadData() {
        //به دلیل اینکه از دو سرویس باید اطلاعات دریافت شود پس وقتی اطلاعات به درستی دریافت شده که این متغیر 2 شده باشد
        LoadDataCounter = 0;
        SViewModel = null;
        EViewModel = null;
        ShowLoadingProgressBar();
        AccountRepository Repository = new AccountRepository(this);
        AccountViewModel ViewModel = Repository.getAccount();
        if (ViewModel != null) {
            MaxLoadData = 3;
            AccountService AService = new AccountService(this);
            AService.Login(ViewModel.getUser().getCellPhone(), ViewModel.getConfirmationId());
        } else {
            MaxLoadData = 2;
            GoToWhichActivity = 0;
        }

        SettingService SService = new SettingService(this);
        SService.GetIntroduceBackground();
        EventOrNewsService EService = new EventOrNewsService(this);
        EService.GetRandomly(PlaceType.IntroducePage);
    }

    private void OnImageViewOnClick() {
        if (EViewModel.getLink() == LinkType.WebLink.ordinal()) {
            Intent BrowserIntent = new Intent(Intent.ACTION_VIEW);
            BrowserIntent.setData(Uri.parse(EViewModel.getPageLink()));
            startActivity(BrowserIntent);
        }
    }

    private void OnButtonClick() {
        AccountRepository ARepository = new AccountRepository(null);
        AccountViewModel AccountViewModel = ARepository.getAccount();

        if (AccountViewModel != null) {
            if (AccountViewModel.getUserSetting()!= null && AccountViewModel.getUserSetting().isUseGprsPoint()) {
                if (CurrentGps.IsMapAlreadyToUse(this, this, R.string.turn_on_location_show_business_inside)) {
                    GoToNextPage();
                }
            } else {
                GoToNextPage();
            }
        }else {
            GoToNextPage();
        }
    }


    private void GoToNextPage() {

        if (GoToWhichActivity == 0) {
            Intent ActivityIntent = new Intent(this, DescriptionAppActivity.class);
            startActivity(ActivityIntent);
        } else {
            if (GoToWhichActivity == 1) {
                Intent ActivityIntent = new Intent(this, HowToSearchInAppGpsOrRegionActivity.class);
                startActivity(ActivityIntent);
            } else {
                Intent ActivityIntent = new Intent(this, MainActivity.class);
                ActivityIntent.putExtra("FromActivityId", ActivityIdList.APP_NEEDS_USER_BACK_TO_TERMINATE);
                ActivityIntent.putExtra("ArrayAddressString", ArrayAddressString);
                startActivity(ActivityIntent);
            }
        }
        finish();
    }

    @Override
    public void OnDismissTurnOnGpsDialog(boolean IsClickYes) {
        if (!IsClickYes) {
            GoToNextPage();
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
                CurrentGps.ShowTurnOnGpsDialog(this, this, R.string.turn_on_location_show_business_inside);
            else
                GoToNextPage();
        } else {
            ShowToast(getResources().getString(R.string.app_permission_denied), Toast.LENGTH_LONG, MessageType.Warning);
        }
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        try {
            if (ServiceMethod == ServiceMethodType.IntroduceBackgroundGet) {
                Feedback<String> FeedBack = (Feedback<String>) Data;
                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    SViewModel = FeedBack.getValue();
                    LoadDataCounter++;
                } else {
                    if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                        LoadDataCounter++;
                    } else {
                        //نیازی به نمایش خطا نیست
                        ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.UserGetAllAddress) {
                Feedback<List<UserAddressViewModel>> FeedBack = (Feedback<List<UserAddressViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    List<UserAddressViewModel> ViewModel = FeedBack.getValue();

                    if (ViewModel != null) {
                        ArrayAddressString = new Gson().toJson(ViewModel);
                    } else {
                        ViewModel = new ArrayList<>();
                        ArrayAddressString = new Gson().toJson(ViewModel);
                    }


                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            } else {
                if (ServiceMethod == ServiceMethodType.RandomlyEventOrNewsGet) {
                    LoadDataCounter++;
                    Feedback<EventOrNewsViewModel> FeedBack = (Feedback<EventOrNewsViewModel>) Data;
                    if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                        EViewModel = FeedBack.getValue();
                    } else {
                        //نیازی به نمایش خطا نیست
                        ShowErrorInConnectDialog();
                    }
                } else {
                    Feedback<AccountViewModel> FeedBack = (Feedback<AccountViewModel>) Data;
                    if (FeedBack.getStatus() == FeedbackType.UpdatedSuccessful.getId()) {
                        AccountViewModel AViewModel = FeedBack.getValue();
                        if (AViewModel != null) {
                            AccountRepository ARepository = new AccountRepository(this);
                            ARepository.setAccount(AViewModel);
                            if (AViewModel.getUserSetting() == null) {
                                //Go to Description App
                                GoToWhichActivity = 1;
                            } else {
                                //Go to Main
                                GoToWhichActivity = 2;
                                MaxLoadData++;
                                LoadDataCounter++;

                                AccountRepository AccountRepository = new AccountRepository(this);
                                AccountViewModel ViewModel = AccountRepository.getAccount();
                                if (ViewModel != null) {
                                    UserAddressService userAddressService = new UserAddressService(this);
                                    //دریافت اطلاعات
                                    userAddressService.GetAll();
                                } else {
                                    Intent ActivityIntent = new Intent(this, MainActivity.class);
                                    ActivityIntent.putExtra("FromActivityId", ActivityIdList.APP_NEEDS_USER_BACK_TO_TERMINATE);
                                    List<UserAddressViewModel> ViewModelList = new ArrayList<>();
                                    String ArrayAddressString = new Gson().toJson(ViewModelList);
                                    ActivityIntent.putExtra("ArrayAddressString", ArrayAddressString);
                                    startActivity(ActivityIntent);
                                }

                            }
                        }
                        LoadDataCounter++;
                    } else {
                        //در صورتی که کد تایید وجود نداشته باشد باید اکانت از کش پاک شود تا بتوان اکانت جدید را جایگزین کرد
                        if (FeedBack.getStatus() == FeedbackType.TrackingCodeForUserConfirmationNotExistOrExpired.getId()) {
                            AccountRepository ARepository = new AccountRepository(this);
                            ARepository.ClearAccount();
                        }
                        if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                            ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                        }
                        ShowErrorInConnectDialog();
                    }
                }
            }
            if (LoadDataCounter == MaxLoadData) {
                if (SViewModel != null) {
                    int ScreenWidth = LayoutUtility.GetWidthAccordingToScreen(this, 1);
                    int ScreenHeight = LayoutUtility.GetHeightAccordingToScreen(this, 1);
                    LayoutUtility.LoadBackgroundImageWithGlide(this, SViewModel, getMasterContainer(), ScreenWidth, ScreenHeight, ScreenWidth / 4, ScreenHeight / 4);
                }
                if (EViewModel != null) {
                    LayoutUtility.LoadImageWithGlide(this, EViewModel.getImagePath(), IntroduceBannerImageView, LayoutConstant.ImageWidthFullWidth, LayoutConstant.ImageHeightQuarterOfWidth);
                }
                HideLoading();
            }
        } catch (Exception Ex) {
            ShowErrorInConnectDialog();
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


}
