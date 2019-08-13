package ir.rayas.app.citywareclient.View.Share;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.BasketListRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.MyClickListener;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.Service.Basket.BasketService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Basket.BasketViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;

public class BasketListActivity extends BaseActivity implements IResponseService, ILoadData {

    private TextViewPersian ShowEmptyBasketListTextViewBasketListFragment = null;
    private RecyclerView BasketListRecyclerViewBasketListFragment = null;
    private SwipeRefreshLayout RefreshBasketListSwipeRefreshLayoutBasketListFragment;
    private boolean IsSwipe = false;
    private boolean QuickItem;

    public boolean isQuickItem() {
        return QuickItem;
    }

    public void setQuickItem(boolean quickItem) {
        QuickItem = quickItem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_list);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.BASKET_LIST_ACTIVITY);


        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonClick();
            }
        }, R.string.basket);

        //طرحبندی ویو
        CreateLayout();

        LoadData();

    }


    private void CreateLayout() {
        ShowEmptyBasketListTextViewBasketListFragment = findViewById(R.id.ShowEmptyBasketListTextViewBasketListFragment);
        RefreshBasketListSwipeRefreshLayoutBasketListFragment =findViewById(R.id.RefreshBasketListSwipeRefreshLayoutBasketListFragment);
        BasketListRecyclerViewBasketListFragment = findViewById(R.id.BasketListRecyclerViewBasketListFragment);
        BasketListRecyclerViewBasketListFragment.setHasFixedSize(true);
        //به دلیل اینکه من در هر سطر یک گزینه نیاز دارم
        LinearLayoutManager RegionLinearLayoutManager = new LinearLayoutManager(BasketListActivity.this);
        BasketListRecyclerViewBasketListFragment.setLayoutManager(RegionLinearLayoutManager);

        ShowEmptyBasketListTextViewBasketListFragment.setVisibility(View.GONE);

        RefreshBasketListSwipeRefreshLayoutBasketListFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                IsSwipe = true;
                LoadData();

            }
        });
    }


    /**
     * در صورتی که در ارتباط با اینترنت مشکلی بوجود آمده و کاربر دکمه تلاش مجدد را فشار داده است
     */
    private void RetryButtonClick() {
        //دریافت اطلاعات
        LoadData();
    }

    @Override
    public void LoadData() {
        if (!IsSwipe)
            ShowLoadingProgressBar();

        BasketService basketService = new BasketService(this);
        basketService.GetAll();
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        HideLoading();
        RefreshBasketListSwipeRefreshLayoutBasketListFragment.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.BasketGetAll) {
                Feedback<List<BasketViewModel>> FeedBack = (Feedback<List<BasketViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    final List<BasketViewModel> ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {

                        ShowEmptyBasketListTextViewBasketListFragment.setVisibility(View.GONE);


                        //تنظیمات مربوط به recycle سبد خرید
                        BasketListRecyclerViewAdapter basketListRecyclerViewAdapter = new BasketListRecyclerViewAdapter(BasketListActivity.this, ViewModel, BasketListRecyclerViewBasketListFragment);
                        BasketListRecyclerViewBasketListFragment.setAdapter(basketListRecyclerViewAdapter);
                        basketListRecyclerViewAdapter.notifyDataSetChanged();
                        BasketListRecyclerViewBasketListFragment.invalidate();

                        basketListRecyclerViewAdapter.setOnItemClickListener(new MyClickListener() {
                            @Override
                            public void onItemClick(int position, View v) {

                                Intent BasketIntent = new Intent(BasketListActivity.this, BasketActivity.class);
                                BasketIntent.putExtra("BusinessId", ViewModel.get(position).getBusinessId());
                                BasketIntent.putExtra("Id", ViewModel.get(position).getId());
                                BasketIntent.putExtra("BusinessName", ViewModel.get(position).getBusinessName());
                                BasketIntent.putExtra("BasketCount", ViewModel.get(position).getItemList().size());
                                BasketIntent.putExtra("Path", ViewModel.get(position).getPath());
                                BasketIntent.putExtra("TotalPrice", ViewModel.get(position).getTotalPrice());
                                BasketIntent.putExtra("Modified", ViewModel.get(position).getModified());
                                BasketIntent.putExtra("QuickItem", QuickItem);
                                startActivity(BasketIntent);

                            }
                        });
                    } else {
                        ShowEmptyBasketListTextViewBasketListFragment.setVisibility(View.VISIBLE);
                    }

                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    ShowEmptyBasketListTextViewBasketListFragment.setVisibility(View.VISIBLE);
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
