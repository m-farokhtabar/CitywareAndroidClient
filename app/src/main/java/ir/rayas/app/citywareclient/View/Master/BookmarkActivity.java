package ir.rayas.app.citywareclient.View.Master;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.BookmarkRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.OnLoadMoreListener;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Business.BookmarkService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Business.BookmarkViewModel;

/**
 * Created by Programmer on 10/23/2018.
 */

public class BookmarkActivity extends BaseActivity implements IResponseService {

    private RecyclerView BookmarkRecyclerView = null;
    private SwipeRefreshLayout RefreshBookmarkSwipeRefreshLayoutBookmarkFragment;
    private ProgressBar LoadMoreProgressBar = null;
    private BookmarkRecyclerViewAdapter BookmarkRecyclerViewAdapter = null;

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
        LoadMoreProgressBar = findViewById(R.id.LoadMoreProgressBarBookmarkActivity);
        RefreshBookmarkSwipeRefreshLayoutBookmarkFragment = findViewById(R.id.RefreshBookmarkSwipeRefreshLayoutBookmarkActivity);

        BookmarkRecyclerView = findViewById(R.id.BookmarkRecyclerViewBookmarkActivity);
        BookmarkRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        BookmarkRecyclerViewAdapter = new BookmarkRecyclerViewAdapter(this, null, BookmarkRecyclerView, new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                PageNumber = PageNumber + 1;
                LoadMoreProgressBar.setVisibility(View.VISIBLE);
                LoadData();
            }
        });
        BookmarkRecyclerView.setAdapter(BookmarkRecyclerViewAdapter);

        RefreshBookmarkSwipeRefreshLayoutBookmarkFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadMoreProgressBar.setVisibility(View.GONE);
                IsSwipe = true;
                PageNumber = 1;
                BookmarkRecyclerViewAdapter.setLoading(false);
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
        LoadMoreProgressBar.setVisibility(View.GONE);
        RefreshBookmarkSwipeRefreshLayoutBookmarkFragment.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.UserBookmarkGetAll) {
                Feedback<List<BookmarkViewModel>> FeedBack = (Feedback<List<BookmarkViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<BookmarkViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        if (PageNumber == 1)
                            BookmarkRecyclerViewAdapter.SetViewModelList(ViewModelList);
                        else
                            BookmarkRecyclerViewAdapter.AddViewModelList(ViewModelList);
                    }
                }/*else if( FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    BookmarkRecyclerViewAdapter.notifyDataSetChanged();
                    BookmarkRecyclerView.invalidate();
                }*/ else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        if (!(PageNumber > 1 && FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId())) {
                            ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);

                        }
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            }
        } catch (Exception e) {
            HideLoading();
            ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
        BookmarkRecyclerViewAdapter.setLoading(false);
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
