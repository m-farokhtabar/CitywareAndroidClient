package ir.rayas.app.citywareclient.View.Fragment.Product;


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
import ir.rayas.app.citywareclient.Adapter.RecyclerView.ProductImageRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Order.ProductImageService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.UserProfileChildren.ProductImageSetActivity;
import ir.rayas.app.citywareclient.View.UserProfileChildren.ProductSetActivity;
import ir.rayas.app.citywareclient.ViewModel.Order.ProductImageViewModel;


public class ProductImageFragment extends Fragment implements IResponseService, ILoadData {

    private ProductSetActivity Context = null;
    private boolean IsLoadedDataForFirst = false;

    private RecyclerView BusinessProductImageRecyclerViewProductImageFragment = null;
    private SwipeRefreshLayout RefreshProductImageSwipeRefreshLayoutProductImageFragment;
    private boolean IsSwipe = false;
    private ProductImageRecyclerViewAdapter productImageRecyclerViewAdapter = null;
    private boolean Continue = true;
    private int PageNumber = 1;
    List<ProductImageViewModel> productImageViewModels;
    private int FirstVisibleItem;

    public ProductImageRecyclerViewAdapter getProductImageRecyclerViewAdapter() {
        return productImageRecyclerViewAdapter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (ProductSetActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_product_image, container, false);
        //طرحبندی ویو
        CreateLayout(CurrentView);
        return CurrentView;

    }
    private void CreateLayout(View CurrentView) {
        productImageViewModels = new ArrayList<>();

        RefreshProductImageSwipeRefreshLayoutProductImageFragment = CurrentView.findViewById(R.id.RefreshProductImageSwipeRefreshLayoutProductImageFragment);
        BusinessProductImageRecyclerViewProductImageFragment = CurrentView.findViewById(R.id.BusinessProductImageRecyclerViewProductImageFragment);
        BusinessProductImageRecyclerViewProductImageFragment.setHasFixedSize(true);
//        //به دلیل اینکه من در هر سطر یک گزینه نیاز دارم
        LinearLayoutManager BusinessLinearLayoutManager = new LinearLayoutManager(Context);
        BusinessProductImageRecyclerViewProductImageFragment.setLayoutManager(BusinessLinearLayoutManager);

        FloatingActionButton NewBusinessProductImageFloatingActionButtonProductImageFragment = CurrentView.findViewById(R.id.NewBusinessProductImageFloatingActionButtonProductImageFragment);
        NewBusinessProductImageFloatingActionButtonProductImageFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent NewProductImageIntent =Context.NewIntent(ProductImageSetActivity.class);
                NewProductImageIntent.putExtra("SetProductImage", "New");
                NewProductImageIntent.putExtra("ProductId", Context.getProductId());
                //2 Edit - you can find Add in UserAddressFragment
                Context.startActivity(NewProductImageIntent);

            }
        });

        RefreshProductImageSwipeRefreshLayoutProductImageFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                IsSwipe = true;
                productImageViewModels = new ArrayList<>();
                PageNumber = 1;
                LoadData();

            }
        });
    }

    public void LoadData() {
        if (!IsSwipe)
            Context.ShowLoadingProgressBar();

        ProductImageService productImageService = new ProductImageService(this);
        //دریافت اطلاعات
        Context.setRetryType(2);
        productImageService.GetAll(Context.getProductId(), PageNumber);
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        RefreshProductImageSwipeRefreshLayoutProductImageFragment.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.GetAllProductImage) {
                Feedback<List<ProductImageViewModel>> FeedBack = (Feedback<List<ProductImageViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    List<ProductImageViewModel> ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        //تنظیمات مربوط به recycle کسب و کار
                        if (PageNumber == 1)
                            productImageViewModels.clear();

                        productImageViewModels.addAll(ViewModel);

                        productImageRecyclerViewAdapter = new ProductImageRecyclerViewAdapter(Context, productImageViewModels, BusinessProductImageRecyclerViewProductImageFragment);
                        BusinessProductImageRecyclerViewProductImageFragment.setAdapter(productImageRecyclerViewAdapter);
                        productImageRecyclerViewAdapter.notifyDataSetChanged();
                        BusinessProductImageRecyclerViewProductImageFragment.invalidate();
                        Continue = true;

                        if (PageNumber == 1) {
                            BusinessProductImageRecyclerViewProductImageFragment.getLayoutManager().scrollToPosition(0);
                        } else {
                            BusinessProductImageRecyclerViewProductImageFragment.getLayoutManager().scrollToPosition(FirstVisibleItem);
                        }
                    } else {
                        Continue = false;
                    }

                    BusinessProductImageRecyclerViewProductImageFragment.setOnScrollListener(new EndlessScrollListener());
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            }
        } catch (Exception e) {
            Context.HideLoading();
            Context.ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            //برای فهمیدن کد فرگنت به ProductSetPagerAdapter مراجعه کنید
            Context.setFragmentIndex(0);
            if (!IsLoadedDataForFirst) {
                IsLoadedDataForFirst = true;
                //دریافت اطلاعات از سرور
                LoadData();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
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

}
