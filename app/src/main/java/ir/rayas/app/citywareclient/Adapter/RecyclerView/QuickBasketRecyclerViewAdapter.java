package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Share.AddQuickBasketActivity;
import ir.rayas.app.citywareclient.ViewModel.Basket.QuickOrderItemViewModel;

/**
 * Created by Hajar on 2/18/2019.
 */

public class QuickBasketRecyclerViewAdapter extends RecyclerView.Adapter<QuickBasketRecyclerViewAdapter.ViewHolder>  {

    private List<QuickOrderItemViewModel> ViewModelList = null;
    private RecyclerView Container = null;
    private AddQuickBasketActivity Context;

    public QuickBasketRecyclerViewAdapter(AddQuickBasketActivity context, List<QuickOrderItemViewModel> ViewModel, RecyclerView Container) {
        this.Context = context;
        this.Container = Container;
        this.ViewModelList = ViewModel;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextViewPersian productNameQuickBasketTextView;
        public TextViewPersian QuantityTextView;
        public TextViewPersian productDeleteQuickBasketIconTextView;

        public ViewHolder(View v) {
            super(v);
            productNameQuickBasketTextView = v.findViewById(R.id.productNameQuickBasketTextView);
            QuantityTextView = v.findViewById(R.id.QuantityTextView);
            productDeleteQuickBasketIconTextView = v.findViewById(R.id.productDeleteQuickBasketIconTextView);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_quick_basket, parent, false);
        ViewHolder CurrentViewHolder = new ViewHolder(CurrentView);
        return CurrentViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.productNameQuickBasketTextView.setText(ViewModelList.get(position).getProductName());
        holder.QuantityTextView.setText(String.valueOf(ViewModelList.get(position).getValue()));


        holder.productDeleteQuickBasketIconTextView.setTypeface(Font.MasterIcon);
        holder.productDeleteQuickBasketIconTextView.setText("\uf014");

        holder.productDeleteQuickBasketIconTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewModelList.remove(position);
                notifyDataSetChanged();
                Container.invalidate();
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



}
