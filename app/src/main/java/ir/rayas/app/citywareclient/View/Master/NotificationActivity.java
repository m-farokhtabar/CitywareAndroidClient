package ir.rayas.app.citywareclient.View.Master;


import android.os.Bundle;

import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.ExpandableList.NotificationExpandableListAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.NotificationRepository;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Notification.NotificationService;
import ir.rayas.app.citywareclient.Share.Enum.NotificationStatus;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;

import ir.rayas.app.citywareclient.ViewModel.Notification.NotificationListViewModel;
import ir.rayas.app.citywareclient.ViewModel.Notification.NotificationViewModel;

public class NotificationActivity extends BaseActivity implements IResponseService, ILoadData {

    private TextViewPersian ShowEmptyNotificationListTextViewNotificationActivity = null;
    private ExpandableListView NotificationExpandableListViewNotificationActivity = null;
    private ExpandableListAdapter expandableListAdapter;
    private List<NotificationViewModel> ViewModel = new ArrayList<>();
    private int lastExpandedPosition = -1;

    private NotificationRepository NotificationRepository = new NotificationRepository();
    private List<NotificationViewModel> NotificationViewModels = null;
    private NotificationListViewModel NotificationListViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.NOTIFICATION_ACTIVITY);

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                LoadData();
            }
        }, R.string.notifications);

//        NotificationRepository NotificationRepository = new NotificationRepository();
//        NotificationRepository.ClearAllNotification();

        NotificationListViewModel = NotificationRepository.getAllNotification();
        if (NotificationListViewModel != null)
            NotificationViewModels = NotificationListViewModel.getNotificationListViewModel();
        else
            NotificationViewModels = new ArrayList<>();

        //ایجاد طرح بندی صفحه
        CreateLayout();
        //دریافت اطلاعات از سرور
        LoadData();
    }


    private void CreateLayout() {
        NotificationExpandableListViewNotificationActivity = findViewById(R.id.NotificationExpandableListViewNotificationActivity);
        ShowEmptyNotificationListTextViewNotificationActivity = findViewById(R.id.ShowEmptyNotificationListTextViewNotificationActivity);

        ShowEmptyNotificationListTextViewNotificationActivity.setVisibility(View.GONE);

    }

    @Override
    public void LoadData() {
        ShowLoadingProgressBar();

        NotificationService notificationService = new NotificationService(this);

        if (NotificationViewModels != null) {
            if (NotificationViewModels.size() > 0) {
                notificationService.GetAll(NotificationViewModels.get(NotificationViewModels.size() - 1).getId());
            } else {
                notificationService.GetAll(0);
            }
        } else {
            notificationService.GetAll(0);
        }


    }


    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.NotificationGetAll) {
                Feedback<List<NotificationViewModel>> FeedBack = (Feedback<List<NotificationViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    List<NotificationViewModel> ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        if (NotificationViewModels == null)
                            NotificationViewModels = new ArrayList<>();

                        //پر کردن ویو با اطلاعات دریافتی
                        for (int i = 0; i < ViewModel.size(); i++) {
                            NotificationViewModels.add(ViewModel.get(i));
                        }

                        NotificationListViewModel notificationListViewModel = new NotificationListViewModel();
                        notificationListViewModel.setNotificationListViewModel(NotificationViewModels);
                        NotificationRepository.setAllNotification(notificationListViewModel);

                        SetInformationToView(NotificationViewModels);
                    } else {
                        if (NotificationViewModels.size() > 0)
                            SetInformationToView(NotificationViewModels);
                        else
                        ShowToast(FeedbackType.InvalidDataFormat.getMessage().replace("{0}", ""), Toast.LENGTH_LONG, MessageType.Warning);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.NotificationSetStatus) {
                Feedback<Boolean> FeedBack = (Feedback<Boolean>) Data;

                if (FeedBack.getStatus() == FeedbackType.UpdatedSuccessful.getId()) {

                    for (int i = 0; i < NotificationViewModels.size(); i++) {
                        NotificationExpandableListAdapter adapter = new NotificationExpandableListAdapter();
                        if (adapter.IsDeleteNotification) {

                            if (NotificationViewModels.get(i).getId() == adapter.NotificationId) {
                                NotificationViewModels.remove(i);
                                NotificationListViewModel.setNotificationListViewModel(NotificationViewModels);
                                NotificationRepository.setAllNotification(NotificationListViewModel);
                                adapter.notifyDataSetChanged();
                                SetInformationToView(NotificationViewModels);
                            }
                        } else {

                            if (NotificationViewModels.get(i).getId() == adapter.NotificationId) {
                                NotificationViewModels.get(i).setStatus(NotificationStatus.Read.getId());
                                NotificationListViewModel.setNotificationListViewModel(NotificationViewModels);
                                NotificationRepository.setAllNotification(NotificationListViewModel);
                                adapter.notifyDataSetChanged();
                            }
                        }

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
            ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
    }

    private void SetInformationToView(List<NotificationViewModel> ViewModel) {

        if (ViewModel.size() > 0) {
            expandableListAdapter = new NotificationExpandableListAdapter(NotificationActivity.this, ViewModel);
            NotificationExpandableListViewNotificationActivity.setAdapter(expandableListAdapter);
        } else {
            ShowEmptyNotificationListTextViewNotificationActivity.setVisibility(View.VISIBLE);
        }


        CloseCategory();
        OpenCategory(ViewModel);
    }


    private void CloseCategory() {
        NotificationExpandableListViewNotificationActivity.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });
    }

    public void OpenCategory(final List<NotificationViewModel> NotificationModel) {
        NotificationExpandableListViewNotificationActivity.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

                if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
                    NotificationExpandableListViewNotificationActivity.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;


            }
        });
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
