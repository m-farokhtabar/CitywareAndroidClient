package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Share.UserFactorProductDetailActivity;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorItemViewModel;

/**
 * Created by Hajar on 3/2/2019.
 */

public class UserFactorProductDetailRecyclerViewAdapter extends RecyclerView.Adapter<UserFactorProductDetailRecyclerViewAdapter.ViewHolder> {

    private List<FactorItemViewModel> ViewModelList = null;
    private RecyclerView Container = null;
    private UserFactorProductDetailActivity Context;

    public UserFactorProductDetailRecyclerViewAdapter(UserFactorProductDetailActivity context, List<FactorItemViewModel> ViewModel, RecyclerView Container) {
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
        public CardView AddOrMinBasketItemQuantityImageView;


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
            AddOrMinBasketItemQuantityImageView = v.findViewById(R.id.AddOrMinBasketItemQuantityImageView);
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

        holder.OrderItemDeleteButton.setVisibility(View.INVISIBLE);
        holder.AddOrMinBasketItemQuantityImageView.setVisibility(View.INVISIBLE);

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

}
