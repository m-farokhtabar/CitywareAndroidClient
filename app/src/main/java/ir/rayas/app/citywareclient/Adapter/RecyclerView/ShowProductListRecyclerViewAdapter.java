package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.MyClickListener;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.OnLoadMoreListener;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowProductListActivity;
import ir.rayas.app.citywareclient.ViewModel.Order.ProductImageViewModel;
import ir.rayas.app.citywareclient.ViewModel.Order.ProductViewModel;

/**
 * Created by Hajar on 11/22/2018.
 */

public class ShowProductListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ShowProductListActivity Context;
    private RecyclerView Container = null;
    private List<ProductViewModel> ViewModelList = null;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private boolean IsGride;

    private int visibleThreshold = 1;
    private int lastVisibleItem;
    private int totalItemCount;

    private MyClickListener myClickListener;

    public void setLoading(boolean loading) {
        isLoading = loading;
    }


    public ShowProductListRecyclerViewAdapter(ShowProductListActivity Context, boolean IsGride, List<ProductViewModel> ProductList, RecyclerView Container, OnLoadMoreListener mOnLoadMoreListener) {
        this.ViewModelList = ProductList;
        this.Context = Context;
        this.IsGride = IsGride;
        this.Container = Container;
        this.onLoadMoreListener = mOnLoadMoreListener;
        CreateLayout();
    }

    private void CreateLayout() {
        final GridLayoutManager linearLayoutManager = (GridLayoutManager) Container.getLayoutManager();
        Container.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                if (lastVisibleItem < linearLayoutManager.findLastVisibleItemPosition()) {
                    if (!isLoading && totalItemCount <= (linearLayoutManager.findLastVisibleItemPosition() + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            isLoading = true;
                            onLoadMoreListener.onLoadMore();
                        }
                    }
                }
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    /**
     * اضافه مودن لیست جدید
     *
     * @param ViewModel
     */
    public List<ProductViewModel> AddViewModelList(List<ProductViewModel> ViewModel) {
        if (ViewModel != null) {
            if (ViewModelList == null)
                ViewModelList = new ArrayList<>();
            ViewModelList.addAll(ViewModel);
            notifyDataSetChanged();
            Container.invalidate();
        }
        return  ViewModelList;
    }

    /**
     * جایگزین نمودن لیست جدید
     *
     * @param ViewModelList
     */
    public List<ProductViewModel> SetViewModelList(List<ProductViewModel> ViewModelList) {
        this.ViewModelList = new ArrayList<>();
        this.ViewModelList.addAll(ViewModelList);
        notifyDataSetChanged();
        Container.invalidate();

        return this.ViewModelList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           View view;
        if (IsGride) {
             view = LayoutInflater.from(Context).inflate(R.layout.recycler_view_show_product_list_one_column, parent, false);
        } else {
             view = LayoutInflater.from(Context).inflate(R.layout.recycler_view_show_product_list_two_column, parent, false);
        }

        return new ShowProductListViewHolder(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ShowProductListViewHolder viewHolder = (ShowProductListViewHolder) holder;

        viewHolder.ProductNameTextView.setText(ViewModelList.get(position).getName());
        viewHolder.AbstractDescriptionTextView.setText(ViewModelList.get(position).getAbstractOfDescription());
        viewHolder.PriceTextView.setText(String.valueOf(Utility.GetIntegerNumberWithComma((long) ViewModelList.get(position).getPrice())));

        int ScreenWidth;
        if (IsGride) {
            ScreenWidth = LayoutUtility.GetWidthAccordingToScreen(Context, 3) - 30;
        } else {
            ScreenWidth = LayoutUtility.GetWidthAccordingToScreen(Context, 2) - 30;
        }

        viewHolder.ImageProductImageView.getLayoutParams().width  = ScreenWidth;
        viewHolder.ImageProductImageView.getLayoutParams().height = ScreenWidth;

        List<ProductImageViewModel> ProductImageList = new ArrayList<>();
        ProductImageList = ViewModelList.get(position).getProductImageList();

        String ProductImage = "";
        if (ProductImageList.size() > 0) {
            for (int i = 0; i < ProductImageList.size(); i++) {
                if (!ProductImageList.get(i).getFullPath().equals("")) {
                    if (ProductImageList.get(i).getFullPath().contains("~"))  {
                        ProductImage = ProductImageList.get(i).getFullPath().replace("~", DefaultConstant.BaseUrlWebService);
                    } else {
                        ProductImage = ProductImageList.get(i).getFullPath();
                    }
                    break;
                }
            }
        }

        if (!ProductImage.equals("")) {
            LayoutUtility.LoadImageWithGlide(Context, ProductImage, viewHolder.ImageProductImageView, ScreenWidth, ScreenWidth);
        } else {
            viewHolder.ImageProductImageView.setImageResource(R.drawable.image_default);
        }
    }

    @Override
    public int getItemCount() {
        return ViewModelList == null ? 0 : ViewModelList.size();
    }


    public class ShowProductListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextViewPersian ProductNameTextView;
        public TextViewPersian AbstractDescriptionTextView;
        public TextViewPersian PriceTextView;
        public ImageView ImageProductImageView;


        public ShowProductListViewHolder(View v) {
            super(v);
            ProductNameTextView = v.findViewById(R.id.ProductNameTextView);
            AbstractDescriptionTextView = v.findViewById(R.id.AbstractDescriptionTextView);
            PriceTextView = v.findViewById(R.id.PriceTextView);
            ImageProductImageView = v.findViewById(R.id.ImageProductImageView);

            v.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
        
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

}