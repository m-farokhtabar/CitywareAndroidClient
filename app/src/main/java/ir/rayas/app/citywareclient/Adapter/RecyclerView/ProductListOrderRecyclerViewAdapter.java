package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.MasterChildren.OrderActivity;
import ir.rayas.app.citywareclient.ViewModel.Marketing.ProductCommissionAndDiscountModel;


public class ProductListOrderRecyclerViewAdapter extends RecyclerView.Adapter<ProductListOrderRecyclerViewAdapter.ViewHolder> {

    private List<ProductCommissionAndDiscountModel> ViewModelList = null;
    private OrderActivity Context;
    private RecyclerView Container = null;

    public ProductListOrderRecyclerViewAdapter(OrderActivity context, List<ProductCommissionAndDiscountModel> ViewModel, RecyclerView Container) {
        this.ViewModelList = ViewModel;
        this.Container = Container;
        this.Context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextViewPersian ProductNameTextView;
        TextViewPersian MarketingCommissionTextView;
        TextViewPersian CustomerPercentTextView;
        TextViewPersian TotalPriceTextView;
        TextViewPersian NumberOfOrderTextView;
        TextViewPersian DeleteOrderIconTextView;


        ViewHolder(View v) {
            super(v);
            ProductNameTextView = v.findViewById(R.id.ProductNameTextView);
            MarketingCommissionTextView = v.findViewById(R.id.MarketingCommissionTextView);
            CustomerPercentTextView = v.findViewById(R.id.CustomerPercentTextView);
            TotalPriceTextView = v.findViewById(R.id.TotalPriceTextView);
            NumberOfOrderTextView = v.findViewById(R.id.NumberOfOrderTextView);
            DeleteOrderIconTextView = v.findViewById(R.id.DeleteOrderIconTextView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_product_list_order, parent, false);
        return new ViewHolder(CurrentView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        holder.DeleteOrderIconTextView.setTypeface(Font.MasterIcon);
        holder.DeleteOrderIconTextView.setText("\uf014");

        holder.ProductNameTextView.setText(ViewModelList.get(position).getProductName());

        int TotalPrice = (int) (ViewModelList.get(position).getPrice() * ViewModelList.get(position).getNumberOfOrder());

        int DiscountPrice = (int) (((float)TotalPrice * (float)ViewModelList.get(position).getCustomerPercent()) / 100);
        holder.CustomerPercentTextView.setText(Utility.GetIntegerNumberWithComma(DiscountPrice));

        
        int MarketerPercent = (int) (((float)TotalPrice * (float)ViewModelList.get(position).getMarketerPercent()) / 100);
        int ApplicationPercent = (int) (((float)TotalPrice * (float)ViewModelList.get(position).getApplicationPercent()) / 100);

        holder.MarketingCommissionTextView.setText(Utility.GetIntegerNumberWithComma(MarketerPercent + ApplicationPercent));

        holder.TotalPriceTextView.setText(Utility.GetIntegerNumberWithComma(TotalPrice));
        holder.NumberOfOrderTextView.setText(String.valueOf(ViewModelList.get(position).getNumberOfOrder()));


        holder.DeleteOrderIconTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context.SetproductCommissionAndDiscountModels(position);

                ViewModelList.remove(position);
                notifyDataSetChanged();
                Container.invalidate();

                Context.SetViewFooter(ViewModelList);
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

    /**
     * ویرایش اطلاعات یک آدرس در لیست
     *
     * @param ViewModel
     */
    public void SetViewModel(ProductCommissionAndDiscountModel ViewModel) {
        if (ViewModel != null && ViewModelList != null && ViewModelList.size() > 0) {
            for (ProductCommissionAndDiscountModel Item : ViewModelList) {
                if (Item.getProductId() == ViewModel.getProductId()) {
                    Item.setProductName(ViewModel.getProductName());
                    Item.setNumberOfOrder(ViewModel.getNumberOfOrder());
                    Item.setPrice(ViewModel.getPrice());
                }
            }
            notifyDataSetChanged();
            Container.invalidate();
        }
    }

    /**
     * اضافه مودن یک آدرس جدید به لیست
     *
     * @param ViewModel
     */
    public void AddViewModel(ProductCommissionAndDiscountModel ViewModel) {
        if (ViewModel != null) {
            if (ViewModelList == null)
                ViewModelList = new ArrayList<>();
            ViewModelList.add(ViewModel);
            notifyDataSetChanged();
            Container.invalidate();
        }
    }

//    public void DeleteViewModel(int position) {
//
//        ViewModelList.remove(position);
//        notifyDataSetChanged();
//        Container.invalidate();
//
//    }
//
//
//    private void SendDataToParentActivity(boolean IsDelete, int Position) {
//
//        HashMap<String, Object> Output = new HashMap<>();
//        Output.put("IsDelete", IsDelete);
//        Output.put("Position", Position);
//        ActivityResultPassing.Push(new ActivityResult(Context.getCurrentActivityId(), Context.getCurrentActivityId(), Output));
//    }

}
