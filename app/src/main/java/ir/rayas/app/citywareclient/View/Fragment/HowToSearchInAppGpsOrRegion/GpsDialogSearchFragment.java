package ir.rayas.app.citywareclient.View.Fragment.HowToSearchInAppGpsOrRegion;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Setting.UserSettingService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Helper.Gps;
import ir.rayas.app.citywareclient.Share.Helper.IResponseTurnOnGpsDialog;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Login.HowToSearchInAppGpsOrRegionActivity;
import ir.rayas.app.citywareclient.View.Master.MainActivity;
import ir.rayas.app.citywareclient.ViewModel.Setting.UserSettingViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;

/**
 * انتخاب جی پی اس جهت جستجو
 */
public class GpsDialogSearchFragment extends Fragment implements IResponseService, IResponseTurnOnGpsDialog {

    private HowToSearchInAppGpsOrRegionActivity Context = null;
    private Gps CurrentGps = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context = (HowToSearchInAppGpsOrRegionActivity) getActivity();
        //کلاس کنترل و مدیریت GPS
        CurrentGps = new Gps();

        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_gps_search, container, false);

        //طرحبندی ویو
        CreateLayout(CurrentView);

        return CurrentView;
    }

    private void CreateLayout(View CurrentView) {

        TextViewPersian GpsFragmentTitleTextView = CurrentView.findViewById(R.id.GpsFragmentTitleTextView);
        ButtonPersianView SearchBaseOnGpsButton = CurrentView.findViewById(R.id.SearchBaseOnGpsButton);
        ButtonPersianView SearchBaseOnRegionButton = CurrentView.findViewById(R.id.SearchBaseOnRegionButton);

        GpsFragmentTitleTextView.setTypeface(Font.MasterLightFont);

        SearchBaseOnGpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnSearchBaseOnGpsButtonClick();
            }
        });

        SearchBaseOnRegionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnSearchBaseOnRegionButtonClick();
            }
        });

    }

    /**
     * ثبت تنظیمات در صورت اکی بودن وضعیت GPS
     */
    private void OnSearchBaseOnGpsButtonClick() {
        if (!CurrentGps.IsPermissionEnabled()) {
            CurrentGps.ShowPermissionDialog(Context);
        } else {
            if (!CurrentGps.IsEnabled()) {
                CurrentGps.ShowTurnOnGpsDialog(Context,this,R.string.TurnOnLocation);
            } else {
                SetGpsInUserSetting();
            }
        }
    }

    /**
     * ثبت تنظیمات برای GPS
     */
    private void SetGpsInUserSetting()
    {
        AccountViewModel AViewModel = Context.getAccountRepository().getAccount();
        UserSettingViewModel USViewModel = AViewModel.getUserSetting();
        if (USViewModel == null) {
            USViewModel = new UserSettingViewModel();
            USViewModel.setUserId(AViewModel.getUser().getId());
            USViewModel.setBusinessCategoryId(null);
            USViewModel.setRegionId(null);
        }
        USViewModel.setUseGprsPoint(true);
        UserSettingService USService = new UserSettingService(this);
        USService.Set(USViewModel);
    }

    /**
     * برو به صفحه انتخاب منطقه
     */
    private void OnSearchBaseOnRegionButtonClick() {
        Context.GoToFragmentById(1);
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
                CurrentGps.ShowTurnOnGpsDialog(Context,this,R.string.TurnOnLocation);
            else
                SetGpsInUserSetting();
        }
    }

    /**
     * زمانی که طرف باشه یا کنسل را برای فعال کردن gps می زند
     *
     * @param IsClickYes
     */
    @Override
    public void OnDismissTurnOnGpsDialog(boolean IsClickYes) {
        if (IsClickYes) {
            SetGpsInUserSetting();
        }
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        if (ServiceMethod == ServiceMethodType.UserSettingSet) {
            try {
                Feedback<UserSettingViewModel> FeedBack = (Feedback<UserSettingViewModel>) Data;
                if (FeedBack.getStatus() == FeedbackType.RegisteredSuccessful.getId() || FeedBack.getStatus() == FeedbackType.UpdatedSuccessful.getId()) {
                    UserSettingViewModel ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        AccountViewModel AViewModel = Context.getAccountRepository().getAccount();
                        AViewModel.setUserSetting(ViewModel);
                        Context.getAccountRepository().setAccount(AViewModel);
                        //رفتن به فرم اصلی
                        Intent MainActivityIntent = new Intent(Context,MainActivity.class);
                        MainActivityIntent.putExtra("FromActivityId", ActivityIdList.APP_NEEDS_USER_BACK_TO_TERMINATE);
                        startActivity(MainActivityIntent);
                        Context.FinishCurrentActivity();
                    } else {
                        Context.ShowToast(FeedbackType.InvalidDataFormat.getMessage().replace("{0}", ""), Toast.LENGTH_LONG, MessageType.Warning);
                        Context.HideLoading();
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context.HideLoading();
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            } catch (Exception Ex) {
                Context.HideLoading();
                Context.ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
            }
        }
    }
}
