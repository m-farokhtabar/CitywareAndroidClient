package ir.rayas.app.citywareclient.View.MasterChildren;


import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.MyClickListener;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.ShowPosterListRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Poster.PosterService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Poster.PurchasedPosterViewModel;

public class ShowBusinessPosterListActivity extends BaseActivity implements IResponseService, ILoadData {


    private SwipeRefreshLayout RefreshShowPosterListSwipeRefreshLayoutShowBusinessPosterListActivity;
    private ShowPosterListRecyclerViewAdapter ShowPosterListRecyclerViewAdapter = null;
    private TextViewPersian ShowEmptyPosterListTextViewShowBusinessPosterListActivity = null;

    private boolean IsSwipe = false;
    private int PageNumber = 1;
    private int BusinessId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_business_poster_list);
        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.DESCRIPTION_BUSINESS_POSTER_ACTIVITY);

        BusinessId = getIntent().getExtras().getInt("BusinessId");

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.show_poster);

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
        ShowEmptyPosterListTextViewShowBusinessPosterListActivity = findViewById(R.id.ShowEmptyPosterListTextViewShowBusinessPosterListActivity);
        RefreshShowPosterListSwipeRefreshLayoutShowBusinessPosterListActivity = findViewById(R.id.RefreshShowPosterListSwipeRefreshLayoutShowBusinessPosterListActivity);
        RecyclerView showPosterListRecyclerViewShowBusinessPosterListActivity = findViewById(R.id.ShowPosterListRecyclerViewShowBusinessPosterListActivity);

        ShowEmptyPosterListTextViewShowBusinessPosterListActivity.setVisibility(View.GONE);

        showPosterListRecyclerViewShowBusinessPosterListActivity.setLayoutManager(new LinearLayoutManager(this));
        ShowPosterListRecyclerViewAdapter = new ShowPosterListRecyclerViewAdapter(this, null, showPosterListRecyclerViewShowBusinessPosterListActivity);
        showPosterListRecyclerViewShowBusinessPosterListActivity.setAdapter(ShowPosterListRecyclerViewAdapter);

        RefreshShowPosterListSwipeRefreshLayoutShowBusinessPosterListActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                IsSwipe = true;
                PageNumber = 1;
                LoadData();
            }
        });

    }

    /**
     * دریافت اطلاعات نحوای جهت پر کردن Recycle
     */
    public void LoadData() {
        if (!IsSwipe)
            if (PageNumber == 1)
                ShowLoadingProgressBar();

        PosterService PosterService = new PosterService(this);
        PosterService.GetAllBusiness(PageNumber, BusinessId);
    }

    /**
     * @param Data
     * @param ServiceMethod
     * @param <T>
     */
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        HideLoading();
        RefreshShowPosterListSwipeRefreshLayoutShowBusinessPosterListActivity.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.UserPosterGetAll) {
                Feedback<List<PurchasedPosterViewModel>> FeedBack = (Feedback<List<PurchasedPosterViewModel>>) Data;


                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    final List<PurchasedPosterViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        if (PageNumber == 1) {
                            if (ViewModelList.size() < 1) {
                                ShowEmptyPosterListTextViewShowBusinessPosterListActivity.setVisibility(View.VISIBLE);
                            } else {
                                ShowEmptyPosterListTextViewShowBusinessPosterListActivity.setVisibility(View.GONE);
                                ShowPosterListRecyclerViewAdapter.SetViewModelList(ViewModelList);

                                if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                    PageNumber = PageNumber + 1;
                                    LoadData();
                                }


                                ShowPosterListRecyclerViewAdapter.setOnItemClickListener(new MyClickListener() {
                                    @Override
                                    public void onItemClick(int position, View v) {
                                        Intent ShowBusinessPosterDetailsIntent = NewIntent(ShowBusinessPosterDetailsActivity.class);
                                        ShowBusinessPosterDetailsIntent.putExtra("PosterId", ViewModelList.get(position).getId());
                                        startActivity(ShowBusinessPosterDetailsIntent);

                                    }
                                });
                            }

                        } else {
                            ShowEmptyPosterListTextViewShowBusinessPosterListActivity.setVisibility(View.GONE);
                            ShowPosterListRecyclerViewAdapter.AddViewModelList(ViewModelList);

                            if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                PageNumber = PageNumber + 1;
                                LoadData();
                            }


                            ShowPosterListRecyclerViewAdapter.setOnItemClickListener(new MyClickListener() {
                                @Override
                                public void onItemClick(int position, View v) {
                                    Intent ShowBusinessPosterDetailsIntent = NewIntent(ShowBusinessPosterDetailsActivity.class);
                                    ShowBusinessPosterDetailsIntent.putExtra("PosterId", ViewModelList.get(position).getId());
                                    startActivity(ShowBusinessPosterDetailsIntent);

                                }
                            });
                        }
                    }
                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    if (PageNumber > 1) {
                        ShowEmptyPosterListTextViewShowBusinessPosterListActivity.setVisibility(View.GONE);
                    } else {
                        ShowEmptyPosterListTextViewShowBusinessPosterListActivity.setVisibility(View.VISIBLE);
                    }
                } else {
                    ShowEmptyPosterListTextViewShowBusinessPosterListActivity.setVisibility(View.GONE);
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
