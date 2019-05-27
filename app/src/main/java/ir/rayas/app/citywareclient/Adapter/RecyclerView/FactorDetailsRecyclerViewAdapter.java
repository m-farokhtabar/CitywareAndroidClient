package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.ViewModel.Marketing.Marketing_CustomerFactorDetailsViewModel;


public class FactorDetailsRecyclerViewAdapter extends RecyclerView.Adapter<FactorDetailsRecyclerViewAdapter.ViewHolder> {

    private List<Marketing_CustomerFactorDetailsViewModel> ViewModelList = null;

    public FactorDetailsRecyclerViewAdapter( List<Marketing_CustomerFactorDetailsViewModel> ViewModel) {
        this.ViewModelList = ViewModel;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextViewPersian ProductNameTextView;
        TextViewPersian PriceTextView;
        TextViewPersian DiscountCustomerTextView;
        TextViewPersian MarketerCommissionTextView;


        ViewHolder(View v) {
            super(v);
            ProductNameTextView = v.findViewById(R.id.ProductNameTextView);
            PriceTextView = v.findViewById(R.id.PriceTextView);
            DiscountCustomerTextView = v.findViewById(R.id.DiscountCustomerTextView);
            MarketerCommissionTextView = v.findViewById(R.id.MarketerCommissionTextView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_factor_details, parent, false);
        return new ViewHolder(CurrentView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.ProductNameTextView.setText(ViewModelList.get(position).getProductName());
        holder.PriceTextView.setText(Utility.GetIntegerNumberWithComma(ViewModelList.get(position).getPrice()));
        holder.MarketerCommissionTextView.setText(Utility.GetIntegerNumberWithComma(ViewModelList.get(position).getCommissionPrice()));
        holder.DiscountCustomerTextView.setText(Utility.GetIntegerNumberWithComma(ViewModelList.get(position).getDiscountPrice()));


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
