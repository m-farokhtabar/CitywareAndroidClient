package ir.rayas.app.citywareclient.View.MasterChildren;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.MyClickListener;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.OnLoadMoreListener;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.ShowProductListRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Order.ProductService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Order.ProductViewModel;

public class ShowProductListActivity extends BaseActivity implements IResponseService, ILoadData {

    private RecyclerView ShowProductListRecyclerViewShowProductListActivity = null;
    private SwipeRefreshLayout RefreshShowProductListSwipeRefreshLayoutShowProductListActivity;
    private ProgressBar LoadMoreProgressBar = null;
    private ShowProductListRecyclerViewAdapter ShowProductListRecyclerViewAdapter = null;
    private TextViewPersian ShowEmptyProductListTextViewShowProductListActivity = null;

    private boolean IsSwipe = false;
    private int PageNumber = 1;
    private int BusinessId;
    private Boolean IsGrid = false;

    List<ProductViewModel> ProductList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product_list);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.SHOW_PRODUCT_LIST_ACTIVITY);

        BusinessId = getIntent().getExtras().getInt("BusinessId");

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.products_list);

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
        LoadMoreProgressBar = findViewById(R.id.LoadMoreProgressBarShowProductListActivity);
        ShowEmptyProductListTextViewShowProductListActivity = findViewById(R.id.ShowEmptyProductListTextViewShowProductListActivity);
        RefreshShowProductListSwipeRefreshLayoutShowProductListActivity = findViewById(R.id.RefreshShowProductListSwipeRefreshLayoutShowProductListActivity);
        ShowProductListRecyclerViewShowProductListActivity = findViewById(R.id.ShowProductListRecyclerViewShowProductListActivity);

        SetRecyclerView(2, null, false);

        ShowEmptyProductListTextViewShowProductListActivity.setVisibility(View.GONE);

        RefreshShowProductListSwipeRefreshLayoutShowProductListActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadMoreProgressBar.setVisibility(View.GONE);
                IsSwipe = true;
                PageNumber = 1;
                ShowProductListRecyclerViewAdapter.setLoading(false);
                LoadData();
            }
        });

        final FloatingActionButton SwitchBetweenOneColumnAndTwoColumnFloatingActionButtonShowProductListActivity = findViewById(R.id.SwitchBetweenOneColumnAndTwoColumnFloatingActionButtonShowProductListActivity);
        SwitchBetweenOneColumnAndTwoColumnFloatingActionButtonShowProductListActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!IsGrid) {
                    SetRecyclerView(1, ProductList, true);
                    ShowProductListRecyclerViewAdapter.notifyDataSetChanged();
                    ShowProductListRecyclerViewShowProductListActivity.invalidate();
                    SwitchBetweenOneColumnAndTwoColumnFloatingActionButtonShowProductListActivity.setImageResource(R.drawable.ic_show_list_two_coulmn_24dp);

                    IsGrid = true;
                } else {
                    SetRecyclerView(2, ProductList, false);
                    ShowProductListRecyclerViewAdapter.notifyDataSetChanged();
                    ShowProductListRecyclerViewShowProductListActivity.invalidate();
                    SwitchBetweenOneColumnAndTwoColumnFloatingActionButtonShowProductListActivity.setImageResource(R.drawable.ic_show_list_24dp);
                    IsGrid = false;
                }
            }
        });

        ShowProductListRecyclerViewAdapter.setOnItemClickListener(new MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent ShowProductDetailsIntent = NewIntent(ShowProductDetailsActivity.class);
                ShowProductDetailsIntent.putExtra("ProductId", ProductList.get(position).getId());
                startActivity(ShowProductDetailsIntent);

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

        ProductService productService = new ProductService(this);
        productService.GetAll(BusinessId, PageNumber);
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
        RefreshShowProductListSwipeRefreshLayoutShowProductListActivity.setRefreshing(false);
        IsSwipe = false;
        ProductList = new ArrayList<>();
        try {
            if (ServiceMethod == ServiceMethodType.ProductGetAll) {
                Feedback<List<ProductViewModel>> FeedBack = (Feedback<List<ProductViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    final List<ProductViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        if (PageNumber == 1) {
                            ProductList = ShowProductListRecyclerViewAdapter.SetViewModelList(ViewModelList);
                            if (ViewModelList.size()==0)
                                ShowEmptyProductListTextViewShowProductListActivity.setVisibility(View.VISIBLE);
                            else {
                                ShowEmptyProductListTextViewShowProductListActivity.setVisibility(View.GONE);
                            }
                        } else {
                            ProductList = ShowProductListRecyclerViewAdapter.AddViewModelList(ViewModelList);
                            ShowEmptyProductListTextViewShowProductListActivity.setVisibility(View.GONE);
                        }
                    } else {
                        if (PageNumber <= 1)
                            ShowEmptyProductListTextViewShowProductListActivity.setVisibility(View.VISIBLE);
                        else
                            ShowEmptyProductListTextViewShowProductListActivity.setVisibility(View.GONE);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        if (!(PageNumber > 1 && FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()))
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
        ShowProductListRecyclerViewAdapter.setLoading(false);
    }

    private void SetRecyclerView(int Column, final List<ProductViewModel> ProductList, final boolean IsGrid) {
        ShowProductListRecyclerViewShowProductListActivity.setLayoutManager(new GridLayoutManager(getApplicationContext(), Column));
        ShowProductListRecyclerViewAdapter = new ShowProductListRecyclerViewAdapter(this, IsGrid, ProductList, ShowProductListRecyclerViewShowProductListActivity, new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                PageNumber = PageNumber + 1;
                LoadMoreProgressBar.setVisibility(View.VISIBLE);
                LoadData();
            }
        });
        ShowProductListRecyclerViewShowProductListActivity.setAdapter(ShowProductListRecyclerViewAdapter);

        ShowProductListRecyclerViewAdapter.setOnItemClickListener(new MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent ShowProductDetailsIntent = NewIntent(ShowProductDetailsActivity.class);
                ShowProductDetailsIntent.putExtra("ProductId", ProductList.get(position).getId());
                startActivity(ShowProductDetailsIntent);

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
