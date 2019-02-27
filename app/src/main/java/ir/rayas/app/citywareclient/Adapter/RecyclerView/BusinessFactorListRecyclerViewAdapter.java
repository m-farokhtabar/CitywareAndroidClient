package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.OnLoadMoreListener;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Factor.BusinessFactorService;
import ir.rayas.app.citywareclient.Service.Factor.UserFactorService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Master.BookmarkActivity;
import ir.rayas.app.citywareclient.View.Share.BusinessFactorListActivity;
import ir.rayas.app.citywareclient.View.Share.MapActivity;
import ir.rayas.app.citywareclient.ViewModel.Basket.BasketItemViewModel;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorItemViewModel;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorViewModel;

/**
 * Created by Hajar on 2/21/2019.
 */

public class BusinessFactorListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IResponseService {

    private BusinessFactorListActivity Context;
    private RecyclerView Container = null;
    private List<FactorViewModel> ViewModelList = null;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;

    private int visibleThreshold = 1;
    private int lastVisibleItem;
    private int totalItemCount;

    private int Position;

    public void setLoading(boolean loading) {
        isLoading = loading;
    }


    public BusinessFactorListRecyclerViewAdapter(BusinessFactorListActivity Context, List<FactorViewModel> FactorList, RecyclerView Container, OnLoadMoreListener mOnLoadMoreListener) {
        this.ViewModelList = FactorList;
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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(Context).inflate(R.layout.recycler_view_business_factor_list, parent, false);
        return new FactorListViewHolder(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        FactorListViewHolder viewHolder = (FactorListViewHolder) holder;

        viewHolder.UserNameTitleTextView.setText(ViewModelList.get(position).getUserFullName());
        viewHolder.CreateDateBusinessFactorTextView.setText(ViewModelList.get(position).getCreate());
        viewHolder.NumberOfOrderItemsBusinessFactorTextView.setText(String.valueOf(ViewModelList.get(position).getItemList().size()));
        viewHolder.FactorCodeTextView.setText(String.valueOf(ViewModelList.get(position).getId()));
        viewHolder.UserCellPhoneTextView.setText(String.valueOf(ViewModelList.get(position).getUserCellPhone()));


        boolean IsZeroPrice = false;

        List<FactorItemViewModel> ItemList =   ViewModelList.get(position).getItemList();
        for (int i = 0; i<ItemList.size(); i++){
            if (ItemList.get(i).getPrice() == 0){
                IsZeroPrice = true;
            }
        }

        if (ViewModelList.get(position).getTotalPrice() < 0) {
            viewHolder.PricePayableBusinessFactorTextView.setText(Context.getResources().getString(R.string.unknown));
        } else {

            double DeliveryCost = ViewModelList.get(position).getDeliveryCost();
            Double PayablePrice;
            if (DeliveryCost <= 0){
                PayablePrice =   ViewModelList.get(position).getTotalPrice();
            }  else {
                PayablePrice = ViewModelList.get(position).getTotalPrice() + DeliveryCost;
            }

            if (ViewModelList.get(position).isHasQuickOrder()){

                viewHolder.PricePayableBusinessFactorTextView.setText(Utility.GetIntegerNumberWithComma(PayablePrice) + " " + Context.getResources().getString(R.string.toman)+ " - " + Context.getResources().getString(R.string.price_is_not_definitive));

                viewHolder.DescriptionBusinessFactorTextView.setVisibility(View.VISIBLE);
                viewHolder.DescriptionBusinessFactorTextView.setText(Context.getResources().getString(R.string.in_order_some_unknown_price_items_must_be_aligned_with_customer));
            }else {
                if (IsZeroPrice) {
                    viewHolder.PricePayableBusinessFactorTextView.setText(Utility.GetIntegerNumberWithComma(PayablePrice) + " " + Context.getResources().getString(R.string.toman) + " - " + Context.getResources().getString(R.string.price_is_not_definitive));

                    viewHolder.DescriptionBusinessFactorTextView.setVisibility(View.VISIBLE);
                    viewHolder.DescriptionBusinessFactorTextView.setText(Context.getResources().getString(R.string.in_order_some_unknown_price_items_must_be_aligned_with_customer));
                }else {
                    viewHolder.DescriptionBusinessFactorTextView.setVisibility(View.GONE);
                    viewHolder.PricePayableBusinessFactorTextView.setText(Utility.GetIntegerNumberWithComma(PayablePrice) + " " + Context.getResources().getString(R.string.toman));
                }
            }
        }
        

        viewHolder.BusinessFactorDeleteIconTextView.setTypeface(Font.MasterIcon);
        viewHolder.BusinessFactorDeleteIconTextView.setText("\uf014");


        viewHolder.BusinessFactorDeleteIconTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Position = position;
                Context.ShowLoadingProgressBar();
                BusinessFactorService businessFactorService = new BusinessFactorService(BusinessFactorListRecyclerViewAdapter.this);
                businessFactorService.Delete(ViewModelList.get(position).getId());
            }
        });


        viewHolder.UserAddressTextView.setText(ViewModelList.get(position).getUserAddress());

        viewHolder.ShowMapUserAddressIconTextView.setTypeface(Font.MasterIcon);
        viewHolder.ShowMapUserAddressIconTextView.setText("\uf041");

        viewHolder.ShowMapUserAddressLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MapIntent = Context.NewIntent(MapActivity.class);
                MapIntent.putExtra("Latitude", ViewModelList.get(position).getUserLatitude());
                MapIntent.putExtra("Longitude", ViewModelList.get(position).getUserLongitude());
                MapIntent.putExtra("Going", 2);
                Context.startActivity(MapIntent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return ViewModelList == null ? 0 : ViewModelList.size();
    }


    /**
     * اضافه مودن لیست جدید
     *
     * @param ViewModel
     */
    public void AddViewModelList(List<FactorViewModel> ViewModel) {
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
    public void SetViewModelList(List<FactorViewModel> ViewModel) {
        ViewModelList = new ArrayList<>();
        ViewModelList.addAll(ViewModel);
        notifyDataSetChanged();
        Container.invalidate();
    }


    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.BusinessFactorDelete) {
                Feedback<FactorViewModel> FeedBack = (Feedback<FactorViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.DeletedSuccessful.getId()) {
                    ViewModelList.remove(Position);
                    notifyDataSetChanged();
                    Container.invalidate();
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


    private class FactorListViewHolder extends RecyclerView.ViewHolder {

        public TextViewPersian BusinessFactorDeleteIconTextView;
        public TextViewPersian UserNameTitleTextView;
        public TextViewPersian CreateDateBusinessFactorTextView;
        public TextViewPersian PricePayableBusinessFactorTextView;
        public TextViewPersian DescriptionBusinessFactorTextView;
        public TextViewPersian NumberOfOrderItemsBusinessFactorTextView;
        public TextViewPersian FactorCodeTextView;
        public TextViewPersian UserCellPhoneTextView;
        public TextViewPersian UserAddressTextView;
        public LinearLayout ShowMapUserAddressLinearLayout;
        public TextViewPersian ShowMapUserAddressIconTextView;
        public TextViewPersian StatusBusinessFactorTextView;

        public FactorListViewHolder(View v) {
            super(v);

            BusinessFactorDeleteIconTextView = v.findViewById(R.id.BusinessFactorDeleteIconTextView);
            UserNameTitleTextView = v.findViewById(R.id.UserNameTitleTextView);
            CreateDateBusinessFactorTextView = v.findViewById(R.id.CreateDateBusinessFactorTextView);
            PricePayableBusinessFactorTextView = v.findViewById(R.id.PricePayableBusinessFactorTextView);
            DescriptionBusinessFactorTextView = v.findViewById(R.id.DescriptionBusinessFactorTextView);
            NumberOfOrderItemsBusinessFactorTextView = v.findViewById(R.id.NumberOfOrderItemsBusinessFactorTextView);
            FactorCodeTextView = v.findViewById(R.id.FactorCodeTextView);
            UserCellPhoneTextView = v.findViewById(R.id.UserCellPhoneTextView);
            UserAddressTextView = v.findViewById(R.id.UserAddressTextView);
            ShowMapUserAddressLinearLayout = v.findViewById(R.id.ShowMapUserAddressLinearLayout);
            ShowMapUserAddressIconTextView = v.findViewById(R.id.ShowMapUserAddressIconTextView);
            StatusBusinessFactorTextView = v.findViewById(R.id.StatusBusinessFactorTextView);

        }
    }





}