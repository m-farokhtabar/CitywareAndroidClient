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
import ir.rayas.app.citywareclient.Service.Factor.UserFactorService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Share.UserFactorDetailActivity;
import ir.rayas.app.citywareclient.View.Share.UserFactorListActivity;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorItemViewModel;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorViewModel;

/**
 * Created by Hajar on 2/21/2019.
 */

public class UserFactorListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IResponseService {

    private UserFactorListActivity Context;
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


    public UserFactorListRecyclerViewAdapter(UserFactorListActivity Context, List<FactorViewModel> FactorList, RecyclerView Container, OnLoadMoreListener mOnLoadMoreListener) {
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

        View view = LayoutInflater.from(Context).inflate(R.layout.recycler_view_user_factor_list, parent, false);
        return new FactorListViewHolder(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        FactorListViewHolder viewHolder = (FactorListViewHolder) holder;

        viewHolder.BusinessTitleTextView.setText(ViewModelList.get(position).getBusinessName());
        viewHolder.CreateDateUserFactorTextView.setText(ViewModelList.get(position).getCreate());
        viewHolder.NumberOfOrderItemsUserFactorTextView.setText(String.valueOf(ViewModelList.get(position).getItemList().size()));
        viewHolder.UserFactorCodeTextView.setText(String.valueOf(ViewModelList.get(position).getId()));

        boolean IsZeroPrice = false;

        List<FactorItemViewModel> ItemList =   ViewModelList.get(position).getItemList();
        for (int i = 0; i<ItemList.size(); i++){
            if (ItemList.get(i).getPrice() == 0){
                IsZeroPrice = true;
            }
        }

        if (ViewModelList.get(position).getTotalPrice() < 0) {
            viewHolder.PricePayableUserFactorTextView.setText(Context.getResources().getString(R.string.unknown));
        } else {

            double DeliveryCost = ViewModelList.get(position).getDeliveryCost();
            Double PayablePrice;
            if (DeliveryCost <= 0){
                PayablePrice =   ViewModelList.get(position).getTotalPrice();
            }  else {
                PayablePrice = ViewModelList.get(position).getTotalPrice() + DeliveryCost;
            }

            if (ViewModelList.get(position).isHasQuickOrder()){

                viewHolder.PricePayableUserFactorTextView.setText(Utility.GetIntegerNumberWithComma(PayablePrice) + " " + Context.getResources().getString(R.string.toman)+ " - " + Context.getResources().getString(R.string.price_is_not_definitive));

                viewHolder.DescriptionUserFactorTextView.setVisibility(View.VISIBLE);
                viewHolder.DescriptionUserFactorTextView.setText(Context.getResources().getString(R.string.in_your_factor_there_are_products_that_are_not_priced));
            }else {
               if (IsZeroPrice) {
                   viewHolder.PricePayableUserFactorTextView.setText(Utility.GetIntegerNumberWithComma(PayablePrice) + " " + Context.getResources().getString(R.string.toman) + " - " + Context.getResources().getString(R.string.price_is_not_definitive));

                   viewHolder.DescriptionUserFactorTextView.setVisibility(View.VISIBLE);
                   viewHolder.DescriptionUserFactorTextView.setText(Context.getResources().getString(R.string.in_your_factor_there_are_products_that_are_not_priced));
               }else {
                   viewHolder.DescriptionUserFactorTextView.setVisibility(View.GONE);
                   viewHolder.PricePayableUserFactorTextView.setText(Utility.GetIntegerNumberWithComma(PayablePrice) + " " + Context.getResources().getString(R.string.toman));
               }
            }
        }
        viewHolder.UserFactorDeleteIconTextView.setTypeface(Font.MasterIcon);
        viewHolder.UserFactorDeleteIconTextView.setText("\uf014");


        viewHolder.UserFactorDeleteIconTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Position = position;
                Context.ShowLoadingProgressBar();
                UserFactorService userFactorService = new UserFactorService(UserFactorListRecyclerViewAdapter.this);
                userFactorService.Delete(ViewModelList.get(position).getId());
            }
        });

        viewHolder.UserFactorListLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent UserFactorDetailIntent = Context.NewIntent(UserFactorDetailActivity.class);
                UserFactorDetailIntent.putExtra("FactorId", ViewModelList.get(position).getId());
                Context.startActivity(UserFactorDetailIntent);
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
            if (ServiceMethod == ServiceMethodType.UserFactorDelete) {
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

        public TextViewPersian UserFactorDeleteIconTextView;
        public TextViewPersian BusinessTitleTextView;
        public TextViewPersian CreateDateUserFactorTextView;
        public TextViewPersian PricePayableUserFactorTextView;
        public TextViewPersian DescriptionUserFactorTextView;
        public TextViewPersian NumberOfOrderItemsUserFactorTextView;
        public TextViewPersian UserFactorCodeTextView;
        public LinearLayout UserFactorListLinearLayout;

        public FactorListViewHolder(View v) {
            super(v);

            UserFactorDeleteIconTextView = v.findViewById(R.id.UserFactorDeleteIconTextView);
            BusinessTitleTextView = v.findViewById(R.id.BusinessTitleTextView);
            CreateDateUserFactorTextView = v.findViewById(R.id.CreateDateUserFactorTextView);
            PricePayableUserFactorTextView = v.findViewById(R.id.PricePayableUserFactorTextView);
            DescriptionUserFactorTextView = v.findViewById(R.id.DescriptionUserFactorTextView);
            NumberOfOrderItemsUserFactorTextView = v.findViewById(R.id.NumberOfOrderItemsUserFactorTextView);
            UserFactorCodeTextView = v.findViewById(R.id.UserFactorCodeTextView);
            UserFactorListLinearLayout = v.findViewById(R.id.UserFactorListLinearLayout);

        }
    }





}