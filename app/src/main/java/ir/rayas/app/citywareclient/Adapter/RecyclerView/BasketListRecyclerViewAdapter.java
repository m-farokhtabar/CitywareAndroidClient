package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.MyClickListener;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Basket.BasketService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Share.BasketActivity;
import ir.rayas.app.citywareclient.View.Share.BasketListActivity;
import ir.rayas.app.citywareclient.ViewModel.Basket.BasketItemViewModel;
import ir.rayas.app.citywareclient.ViewModel.Basket.BasketViewModel;



public class BasketListRecyclerViewAdapter extends RecyclerView.Adapter<BasketListRecyclerViewAdapter.ViewHolder> implements IResponseService {

    private List<BasketViewModel> ViewModelList = null;
    private RecyclerView Container = null;
    private BasketListActivity Context;
    private int Position;
    private MyClickListener myClickListener;



    public BasketListRecyclerViewAdapter(BasketListActivity context, List<BasketViewModel> ViewModel, RecyclerView Container) {
        this.Context = context;
        this.Container = Container;
        this.ViewModelList = ViewModel;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        TextViewPersian BasketDeleteIconTextView;
        TextViewPersian BusinessTitleTextView;
        TextViewPersian CreateDateBasketTextView;
        TextViewPersian PricePayableBasketTextView;
        TextViewPersian DescriptionBasketTextView;
        TextViewPersian NumberOfOrderItemsBasketTextView;
        ImageView ImageBasketImageView;


        public ViewHolder(View v) {
            super(v);
            BasketDeleteIconTextView = v.findViewById(R.id.BasketDeleteIconTextView);
            BusinessTitleTextView = v.findViewById(R.id.BusinessTitleTextView);
            CreateDateBasketTextView = v.findViewById(R.id.CreateDateBasketTextView);
            PricePayableBasketTextView = v.findViewById(R.id.PricePayableBasketTextView);
            DescriptionBasketTextView = v.findViewById(R.id.DescriptionBasketTextView);
            NumberOfOrderItemsBasketTextView = v.findViewById(R.id.NumberOfOrderItemsBasketTextView);
            ImageBasketImageView = v.findViewById(R.id.ImageBasketImageView);


            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_basket_list, parent, false);
        return new ViewHolder(CurrentView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.BusinessTitleTextView.setText(ViewModelList.get(position).getBusinessName());
        holder.CreateDateBasketTextView.setText(ViewModelList.get(position).getModified());
        holder.NumberOfOrderItemsBasketTextView.setText(String.valueOf(ViewModelList.get(position).getItemList().size()));

        if (ViewModelList.get(position).getTotalPrice() < 0)
            holder.PricePayableBasketTextView.setText(Context.getResources().getString(R.string.unknown));
        else
            holder.PricePayableBasketTextView.setText(Utility.GetIntegerNumberWithComma(ViewModelList.get(position).getTotalPrice()) + " " + Context.getResources().getString(R.string.toman));


        if (ViewModelList.get(position).getItemList() != null && ViewModelList.get(position).getItemList().size() > 0) {
            ArrayList<BasketItemViewModel> ItemList = new ArrayList<BasketItemViewModel>();
            ItemList.addAll(ViewModelList.get(position).getItemList());
            for (int i = 0; i < ItemList.size(); i++) {
                if (ItemList.get(i).getPrice() <= 0) {
                    Context.setQuickItem(true);
                    holder.DescriptionBasketTextView.setText(Context.getResources().getString(R.string.in_your_basket_there_are_products_that_are_not_priced));

                }
            }
        }


        holder.BasketDeleteIconTextView.setTypeface(Font.MasterIcon);
        holder.BasketDeleteIconTextView.setText("\uf014");


        if (!ViewModelList.get(position).getPath().equals("")) {
            LayoutUtility.LoadImageWithGlide(Context, ViewModelList.get(position).getPath(), holder.ImageBasketImageView);
        } else {
            holder.ImageBasketImageView.setImageResource(R.drawable.image_default);
        }

        holder.BasketDeleteIconTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Position = position;
                Context.ShowLoadingProgressBar();
                BasketService basketService = new BasketService(BasketListRecyclerViewAdapter.this);
                basketService.DeleteBasket(ViewModelList.get(position).getId());
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


    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.BasketDelete) {
                Feedback<BasketItemViewModel> FeedBack = (Feedback<BasketItemViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.DeletedSuccessful.getId()) {

                    if (ViewModelList.size() > 1) {
                        ViewModelList.remove(Position);
                        notifyDataSetChanged();
                        Container.invalidate();

                    } else {
                        Context.finish();
                    }
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
