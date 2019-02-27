package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Master.UserProfileActivity;
import ir.rayas.app.citywareclient.ViewModel.Package.OutPackageViewModel;
import ir.rayas.app.citywareclient.ViewModel.Package.OutputPackageTransactionViewModel;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.OnLoadMoreListener;

/**
 * Created by Hajar on 11/3/2018.
 */

public class PackageRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private UserProfileActivity Context;
    private RecyclerView Container = null;
    private List<OutputPackageTransactionViewModel> ViewModelList = null;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;

    private int visibleThreshold = 1;
    private int lastVisibleItem;
    private int totalItemCount;

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public PackageRecyclerViewAdapter(UserProfileActivity Context, List<OutputPackageTransactionViewModel> PackageList, RecyclerView Container, OnLoadMoreListener mOnLoadMoreListener) {
        this.ViewModelList = PackageList;
        this.Context = Context;
        this.Container = Container;
        this.onLoadMoreListener = mOnLoadMoreListener;
        CreateLayout();
    }

    private void CreateLayout() {
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) Container.getLayoutManager();
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
    public void AddViewModelList(List<OutputPackageTransactionViewModel> ViewModel) {
        if (ViewModel != null) {
            if (ViewModelList == null)
                ViewModelList = new ArrayList<>();
            ViewModelList.addAll(ViewModel);
            notifyDataSetChanged();
            Container.invalidate();
        }
    }

    /**
     * جایگزین نمودن لیست جدید
     *
     * @param ViewModel
     */
    public void SetViewModelList(List<OutputPackageTransactionViewModel> ViewModel) {
        ViewModelList = new ArrayList<>();
        ViewModelList.addAll(ViewModel);
        notifyDataSetChanged();
        Container.invalidate();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(Context).inflate(R.layout.recycler_view_package, parent, false);
        return new PackageViewHolder(view);

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PackageViewHolder viewHolder = (PackageViewHolder) holder;
        boolean IsExpire = false;

        OutPackageViewModel outPackageViewModel = ViewModelList.get(position).getPackage();

        viewHolder.PackageTitleTextView.setText(outPackageViewModel.getTitle());
        viewHolder.ExpireTextView.setText(ViewModelList.get(position).getExpire());

        List<Long> Date = Utility.CalculateTimeDifference(ViewModelList.get(position).getCurrentDate(), ViewModelList.get(position).getExpire());

        if (ViewModelList.get(position).getExpire().equals("")) {
            viewHolder.ExpireTextView.setText(Context.getResources().getString(R.string.unlimited));
        } else {
            if (Date.get(0) > 0) {
                viewHolder.ExpireTextView.setText(ViewModelList.get(position).getExpire());
                IsExpire = false;
            } else {
                if (Date.get(1) > 0) {
                    viewHolder.ExpireTextView.setText(ViewModelList.get(position).getExpire());
                    IsExpire = false;
                } else {
                    if (Date.get(2) > 0) {
                        viewHolder.ExpireTextView.setText(ViewModelList.get(position).getExpire());
                        IsExpire = false;
                    } else {
                        viewHolder.ExpireTextView.setText(Context.getResources().getString(R.string.expire));
                        IsExpire = true;
                    }
                }

            }
        }

        if (IsExpire) {
            viewHolder.ExpireTextView.setTextColor(Context.getResources().getColor(R.color.FontRedColor));
        } else {
            if (ViewModelList.get(position).getExpire().equals("")) {
                viewHolder.ExpireTextView.setTextColor(Context.getResources().getColor(R.color.FontSemiDarkThemeColor));
            } else {
                viewHolder.ExpireTextView.setTextColor(Context.getResources().getColor(R.color.FontBlackColor));
            }
        }

        viewHolder.TransactionNumberTextView.setText(ViewModelList.get(position).getTransactionNumber());
        viewHolder.CreditPriceTextView.setText(String.valueOf((int) outPackageViewModel.getCreditPrice()));
        viewHolder.PaidPriceTextView.setText(String.valueOf((int) ViewModelList.get(position).getPaidPrice()));

        if (outPackageViewModel.getImagePathUrl() == null) {
            viewHolder.PackageImageLinearLayout.setVisibility(View.GONE);
        } else if (outPackageViewModel.getImagePathUrl().equals("")) {
            viewHolder.PackageImageLinearLayout.setVisibility(View.GONE);
        } else {
            viewHolder.PackageImageLinearLayout.setVisibility(View.VISIBLE);
            String ImageUrl = "";
            if (outPackageViewModel.getImagePathUrl().contains("~")) {
                ImageUrl = outPackageViewModel.getImagePathUrl().replace("~", DefaultConstant.BaseUrlWebService);
            } else {
                ImageUrl = outPackageViewModel.getImagePathUrl();
            }
            LayoutUtility.LoadImageWithGlide(Context, ImageUrl, viewHolder.PackageImageView);
        }
    }

    @Override
    public int getItemCount() {
        return ViewModelList == null ? 0 : ViewModelList.size();
    }


    private class PackageViewHolder extends RecyclerView.ViewHolder {

        public TextViewPersian PackageTitleTextView;
        public TextViewPersian CreditPriceTextView;
        public TextViewPersian PaidPriceTextView;
        public TextViewPersian ExpireTextView;
        public TextViewPersian TransactionNumberTextView;
        public ImageView PackageImageView;
        public LinearLayout PackageImageLinearLayout;


        public PackageViewHolder(View v) {
            super(v);
            PackageTitleTextView = v.findViewById(R.id.PackageTitleTextView);
            CreditPriceTextView = v.findViewById(R.id.CreditPriceTextView);
            PaidPriceTextView = v.findViewById(R.id.PaidPriceTextView);
            ExpireTextView = v.findViewById(R.id.ExpireTextView);
            TransactionNumberTextView = v.findViewById(R.id.TransactionNumberTextView);
            PackageImageView = v.findViewById(R.id.PackageImageView);
            PackageImageLinearLayout = v.findViewById(R.id.PackageImageLinearLayout);

        }
    }
}