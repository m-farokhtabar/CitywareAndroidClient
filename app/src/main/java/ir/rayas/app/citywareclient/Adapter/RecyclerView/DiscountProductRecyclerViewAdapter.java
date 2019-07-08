package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowDiscountDetailsActivity;
import ir.rayas.app.citywareclient.ViewModel.Marketing.ProductCommissionAndDiscountViewModel;


public class DiscountProductRecyclerViewAdapter extends RecyclerView.Adapter<DiscountProductRecyclerViewAdapter.ViewHolder> {

    private List<ProductCommissionAndDiscountViewModel> ViewModelList = null;
    private ShowDiscountDetailsActivity Context;

    public DiscountProductRecyclerViewAdapter(ShowDiscountDetailsActivity context, List<ProductCommissionAndDiscountViewModel> ViewModel) {
        this.ViewModelList = ViewModel;
        this.Context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextViewPersian ProductNameTextView;
        TextViewPersian CustomerPercentTextView;


        ViewHolder(View v) {
            super(v);
            ProductNameTextView = v.findViewById(R.id.ProductNameTextView);
            CustomerPercentTextView = v.findViewById(R.id.CustomerPercentTextView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_discount_product, parent, false);
        return new ViewHolder(CurrentView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.ProductNameTextView.setText(ViewModelList.get(position).getProductName());
        holder.CustomerPercentTextView.setText(String.valueOf(ViewModelList.get(position).getCustomerPercent()) + " " + Context.getResources().getString(R.string.percent));
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
