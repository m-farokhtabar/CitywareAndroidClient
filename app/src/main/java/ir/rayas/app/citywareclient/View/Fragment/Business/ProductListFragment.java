package ir.rayas.app.citywareclient.View.Fragment.Business;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.ProductListRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Order.ProductService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.View.UserProfileChildren.BusinessSetActivity;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.UserProfileChildren.ProductSetActivity;
import ir.rayas.app.citywareclient.ViewModel.Order.ProductViewModel;


public class ProductListFragment extends Fragment implements IResponseService, ILoadData {
    private BusinessSetActivity Context = null;
    private boolean IsLoadedDataForFirst = false;
    private RecyclerView ProductListRecyclerViewProductFragment = null;
    private SwipeRefreshLayout RefreshBusinessProductSwipeRefreshLayoutProductFragment;
    private boolean IsSwipe = false;
    private ir.rayas.app.citywareclient.Adapter.RecyclerView.ProductListRecyclerViewAdapter ProductListRecyclerViewAdapter = null;
    private boolean Continue = true;
    private int PageNumber = 1;
    List<ProductViewModel> productViewModels;
    private int FirstVisibleItem;

    public ir.rayas.app.citywareclient.Adapter.RecyclerView.ProductListRecyclerViewAdapter getProductListRecyclerViewAdapter() {
        return ProductListRecyclerViewAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //دریافت اکتیوتی والد این فرگمین
        Context = (BusinessSetActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_product_list, container, false);
        //طرحبندی ویو
        CreateLayout(CurrentView);

        return CurrentView;
    }
    
    private void CreateLayout(View CurrentView) {
        productViewModels = new ArrayList<>();

        RefreshBusinessProductSwipeRefreshLayoutProductFragment = CurrentView.findViewById(R.id.RefreshBusinessProductSwipeRefreshLayoutProductFragment);
        ProductListRecyclerViewProductFragment = CurrentView.findViewById(R.id.BusinessProductRecyclerViewProductFragment);
        ProductListRecyclerViewProductFragment.setHasFixedSize(true);
        //به دلیل اینکه من در هر سطر یک گزینه نیاز دارم
        LinearLayoutManager BusinessLinearLayoutManager = new LinearLayoutManager(Context);
        ProductListRecyclerViewProductFragment.setLayoutManager(BusinessLinearLayoutManager);

        FloatingActionButton NewBusinessProductFloatingActionButtonProductFragment = CurrentView.findViewById(R.id.NewBusinessProductFloatingActionButtonProductFragment);
        NewBusinessProductFloatingActionButtonProductFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent NewProductIntent =  Context.NewIntent(ProductSetActivity.class);
                NewProductIntent.putExtra("SetProduct", "New");
                NewProductIntent.putExtra("BusinessId", Context.getBusinessId());
                Context.startActivity(NewProductIntent);

            }
        });

        RefreshBusinessProductSwipeRefreshLayoutProductFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                IsSwipe = true;
                productViewModels = new ArrayList<>();
                PageNumber = 1;
                LoadData();

            }
        });
    }

    public void LoadData() {
        if (!IsSwipe)
            Context.ShowLoadingProgressBar();

        ProductService productService = new ProductService(this);
        //دریافت اطلاعات
        Context.setRetryType(2);
        productService.GetAll(Context.getBusinessId(), PageNumber);
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        RefreshBusinessProductSwipeRefreshLayoutProductFragment.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.ProductGetAll) {
                Feedback<List<ProductViewModel>> FeedBack = (Feedback<List<ProductViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    List<ProductViewModel> ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        //تنظیمات مربوط به recycle کسب و کار
                        if (PageNumber == 1)
                            productViewModels.clear();

                        productViewModels.addAll(ViewModel);

                        ProductListRecyclerViewAdapter = new ProductListRecyclerViewAdapter(Context, productViewModels, ProductListRecyclerViewProductFragment);
                        ProductListRecyclerViewProductFragment.setAdapter(ProductListRecyclerViewAdapter);
                        ProductListRecyclerViewAdapter.notifyDataSetChanged();
                        ProductListRecyclerViewProductFragment.invalidate();
                        Continue = true;

                        if (PageNumber == 1) {
                            ProductListRecyclerViewProductFragment.getLayoutManager().scrollToPosition(0);
                        } else {
                            ProductListRecyclerViewProductFragment.getLayoutManager().scrollToPosition(FirstVisibleItem);
                        }
                    } else {
                        Continue = false;
                    }

                    ProductListRecyclerViewProductFragment.setOnScrollListener(new EndlessScrollListener());
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            }
        } catch (Exception e) {
            Context.ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
    }

    public class EndlessScrollListener extends RecyclerView.OnScrollListener implements AbsListView.OnScrollListener {
        private int visibleThreshold = 5;
        private int previousTotal = 0;
        private boolean loading = true;

        public EndlessScrollListener() {
        }

        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            FirstVisibleItem = firstVisibleItem;
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                //initialize the progress dialog and show it
                if (Continue) {
                    PageNumber++;
                    LoadData();
                    loading = true;
                }
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            //برای فهمیدن کد فرگنت به PagerAdapter مراجعه کنید
            Context.setFragmentIndex(2);
            if (!IsLoadedDataForFirst) {
                IsSwipe = false;
                IsLoadedDataForFirst = true;
                //دریافت اطلاعات از سرور
                LoadData();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}
