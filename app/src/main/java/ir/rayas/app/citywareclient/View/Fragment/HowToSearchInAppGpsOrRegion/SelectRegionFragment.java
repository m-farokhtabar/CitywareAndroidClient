package ir.rayas.app.citywareclient.View.Fragment.HowToSearchInAppGpsOrRegion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.RegionRecyclerViewAdapter;
import ir.rayas.app.citywareclient.View.Login.HowToSearchInAppGpsOrRegionActivity;
import ir.rayas.app.citywareclient.View.Master.MainActivity;
import ir.rayas.app.citywareclient.ViewModel.Setting.UserSettingViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;

/**
 * انتخاب نحوه جستجو به صورت ناحیه
 */
public class SelectRegionFragment extends Fragment implements IResponseService{
    private HowToSearchInAppGpsOrRegionActivity Context = null;
    private RegionRecyclerViewAdapter  RegionRecyclerViewAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context = (HowToSearchInAppGpsOrRegionActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_select_region, container, false);
        //طرحبندی ویو
        CreateLayout(CurrentView);

        return CurrentView;
    }

    private void CreateLayout(View CurrentView) {

        TextViewPersian RegionFragmentTitleTextView = CurrentView.findViewById(R.id.RegionFragmentTitleTextView);
        ButtonPersianView RegionFragmentSelectRegionButton =  CurrentView.findViewById(R.id.RegionFragmentSelectRegionButton);
        RegionFragmentTitleTextView.setTypeface(Font.MasterLightFont);
        RegionFragmentSelectRegionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnRegionFragmentSelectRegionButtonClick();
            }
        });

        //تنظیمات مربوط به recycle انتخاب منطقه
        RecyclerView RegionFragmentRegionRecyclerView = CurrentView.findViewById(R.id.RegionFragmentRegionRecyclerView);
        RegionFragmentRegionRecyclerView.setHasFixedSize(true);
        //به دلیل اینکه من در هر سطر یک گزینه نیاز دارم
        LinearLayoutManager RegionLinearLayoutManager = new LinearLayoutManager(Context);
        RegionFragmentRegionRecyclerView.setLayoutManager(RegionLinearLayoutManager);
        RegionRecyclerViewAdapter = new RegionRecyclerViewAdapter(Context.getRegion(),RegionFragmentRegionRecyclerView);
        RegionFragmentRegionRecyclerView.setAdapter(RegionRecyclerViewAdapter);
    }

    private void OnRegionFragmentSelectRegionButtonClick()
    {
        if (RegionRecyclerViewAdapter.getSelectedRegionId()!=-1)
        {
            AccountViewModel AViewModel = Context.getAccountRepository().getAccount();
            UserSettingViewModel USViewModel = AViewModel.getUserSetting();
            if (USViewModel == null) {
                USViewModel = new UserSettingViewModel();
                USViewModel.setUserId(AViewModel.getUser().getId());
                USViewModel.setBusinessCategoryId(null);
            }
            USViewModel.setUseGprsPoint(false);
            USViewModel.setRegionId(RegionRecyclerViewAdapter.getSelectedRegionId());
            UserSettingService UsService = new UserSettingService(this);
            UsService.Set(USViewModel);
        }
        else
        {
            Context.ShowToast(Context.getResources().getString(R.string.please_select_region), Toast.LENGTH_SHORT, MessageType.Warning);
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
