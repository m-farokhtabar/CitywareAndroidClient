package ir.rayas.app.citywareclient.View.Master;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.BookmarkRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Business.BookmarkService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Business.BookmarkViewModel;


public class BookmarkActivity extends BaseActivity implements IResponseService {

    private SwipeRefreshLayout RefreshBookmarkSwipeRefreshLayoutBookmarkFragment;
    private BookmarkRecyclerViewAdapter BookmarkRecyclerViewAdapter = null;
    private TextViewPersian ShowEmptyBookmarkListTextViewBookmarkActivity = null;

    private int PageNumber = 1;
    private boolean IsSwipe = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.BOOKMARK_ACTIVITY);
        Static.IsRefreshBookmark = true;

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, 0);

        //ایجاد طرحبندی صفحه
        CreateLayout();

    }

    /**
     * در صورتی که در ارتباط با اینترنت مشکلی بوجود آمده و کاربر دکمه تلاش مجدد را فشار داده است
     */
    private void RetryButtonOnClick() {
        //دریافت اطلاعات
        LoadData();
    }

    /**
     * دریافت اطلاعات نحوای جهت پر کردن Recycle
     */
    private void LoadData() {
        if (!IsSwipe)
            if (PageNumber == 1)
                ShowLoadingProgressBar();

        BookmarkService BookmarkService = new BookmarkService(this);
        BookmarkService.GetAll(PageNumber);
    }

    /**
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    private void CreateLayout() {
        RefreshBookmarkSwipeRefreshLayoutBookmarkFragment = findViewById(R.id.RefreshBookmarkSwipeRefreshLayoutBookmarkActivity);
        ShowEmptyBookmarkListTextViewBookmarkActivity = findViewById(R.id.ShowEmptyBookmarkListTextViewBookmarkActivity);

        ShowEmptyBookmarkListTextViewBookmarkActivity.setVisibility(View.GONE);

        RecyclerView bookmarkRecyclerView = findViewById(R.id.BookmarkRecyclerViewBookmarkActivity);
        bookmarkRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        BookmarkRecyclerViewAdapter = new BookmarkRecyclerViewAdapter(this, null, bookmarkRecyclerView);
        bookmarkRecyclerView.setAdapter(BookmarkRecyclerViewAdapter);

        RefreshBookmarkSwipeRefreshLayoutBookmarkFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                IsSwipe = true;
                PageNumber = 1;
                LoadData();
            }
        });
    }

    /**
     * @param Data
     * @param ServiceMethod
     * @param <T>
     */
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        HideLoading();
        RefreshBookmarkSwipeRefreshLayoutBookmarkFragment.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.UserBookmarkGetAll) {
                Feedback<List<BookmarkViewModel>> FeedBack = (Feedback<List<BookmarkViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<BookmarkViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        if (PageNumber == 1) {
                            if (ViewModelList.size() < 1) {
                                ShowEmptyBookmarkListTextViewBookmarkActivity.setVisibility(View.VISIBLE);
                            } else {
                                ShowEmptyBookmarkListTextViewBookmarkActivity.setVisibility(View.GONE);
                                BookmarkRecyclerViewAdapter.SetViewModelList(ViewModelList);

                                if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                    PageNumber = PageNumber + 1;
                                    LoadData();
                                }
                            }

                        } else {
                            ShowEmptyBookmarkListTextViewBookmarkActivity.setVisibility(View.GONE);
                            BookmarkRecyclerViewAdapter.AddViewModelList(ViewModelList);

                            if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                PageNumber = PageNumber + 1;
                                LoadData();
                            }
                        }
                    }
                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    if (PageNumber > 1) {
                        ShowEmptyBookmarkListTextViewBookmarkActivity.setVisibility(View.GONE);
                    } else {
                        ShowEmptyBookmarkListTextViewBookmarkActivity.setVisibility(View.VISIBLE);
                    }
                } else {
                    ShowEmptyBookmarkListTextViewBookmarkActivity.setVisibility(View.GONE);
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
