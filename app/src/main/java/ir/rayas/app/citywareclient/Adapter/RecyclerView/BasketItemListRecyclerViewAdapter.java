package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Basket.BasketService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Share.BasketActivity;
import ir.rayas.app.citywareclient.ViewModel.Basket.BasketItemViewModel;

/**
 * Created by Hajar on 2/3/2019.
 */

public class BasketItemListRecyclerViewAdapter extends RecyclerView.Adapter<BasketItemListRecyclerViewAdapter.ViewHolder> implements IResponseService {

    private List<BasketItemViewModel> ViewModelList = null;
    private RecyclerView Container = null;
    private BasketActivity Context;
    private int Position;

    public BasketItemListRecyclerViewAdapter(BasketActivity context, List<BasketItemViewModel> ViewModel, RecyclerView Container) {
        this.Context = context;
        this.Container = Container;
        this.ViewModelList = ViewModel;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextViewPersian BasketItemProductNameTextView;
        public TextViewPersian BasketItemQuantityTextView;
        public TextViewPersian BasketItemTotalPriceTextView;
        public TextViewPersian BasketItemPriceTextView;
        public TextViewPersian BasketItemTotalPriceTomanTextView;
        public TextViewPersian BasketItemPriceTomanTextView;
        public ButtonPersianView OrderItemDeleteButton;
        public ImageView ImageBasketItemImageView;


        public ViewHolder(View v) {
            super(v);
            BasketItemProductNameTextView = v.findViewById(R.id.BasketItemProductNameTextView);
            BasketItemQuantityTextView = v.findViewById(R.id.BasketItemQuantityTextView);
            BasketItemTotalPriceTextView = v.findViewById(R.id.BasketItemTotalPriceTextView);
            BasketItemPriceTextView = v.findViewById(R.id.BasketItemPriceTextView);
            OrderItemDeleteButton = v.findViewById(R.id.OrderItemDeleteButton);
            ImageBasketItemImageView = v.findViewById(R.id.ImageBasketItemImageView);
            BasketItemPriceTomanTextView = v.findViewById(R.id.BasketItemPriceTomanTextView);
            BasketItemTotalPriceTomanTextView = v.findViewById(R.id.BasketItemTotalPriceTomanTextView);
        }


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_basket_list_item, parent, false);
        ViewHolder CurrentViewHolder = new ViewHolder(CurrentView);
        return CurrentViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.BasketItemQuantityTextView.setText(String.valueOf((int) ViewModelList.get(position).getValue()));
        holder.BasketItemProductNameTextView.setText(ViewModelList.get(position).getProductName());


        if (ViewModelList.get(position).getPrice() <= 0 ) {

            holder.BasketItemPriceTomanTextView .setVisibility(View.GONE);
            holder.BasketItemTotalPriceTomanTextView .setVisibility(View.GONE);
            holder.BasketItemPriceTextView.setText(Context.getResources().getString(R.string.unknown));
            holder.BasketItemTotalPriceTextView.setText(Context.getResources().getString(R.string.unknown));

        } else {

            holder.BasketItemPriceTomanTextView .setVisibility(View.VISIBLE);
            holder.BasketItemTotalPriceTomanTextView .setVisibility(View.VISIBLE);
            holder.BasketItemPriceTextView.setText(Utility.GetIntegerNumberWithComma(ViewModelList.get(position).getPrice()));
            double TotalPrice = ViewModelList.get(position).getPrice() * ViewModelList.get(position).getValue();
            holder.BasketItemTotalPriceTextView.setText(Utility.GetIntegerNumberWithComma(TotalPrice));

        }


        if (!ViewModelList.get(position).getImagePathUrl().equals("")) {
            LayoutUtility.LoadImageWithGlide(Context, ViewModelList.get(position).getImagePathUrl(), holder.ImageBasketItemImageView);
        } else {
            holder.ImageBasketItemImageView.setImageResource(R.drawable.image_default);
        }

        holder.OrderItemDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Position = position;
                Context.ShowLoadingProgressBar();
                Context.setRetryType(1);
                BasketService basketService = new BasketService(BasketItemListRecyclerViewAdapter.this);
                basketService.DeleteItemByItemId(ViewModelList.get(position).getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        int Output;
        if (ViewModelList == null)
            Output = 0;
        else
            Output = ViewModelList.size();
        return Output;
    }


    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.BasketDeleteItemByItemId) {
                Feedback<BasketItemViewModel> FeedBack = (Feedback<BasketItemViewModel>) Data;

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
}
