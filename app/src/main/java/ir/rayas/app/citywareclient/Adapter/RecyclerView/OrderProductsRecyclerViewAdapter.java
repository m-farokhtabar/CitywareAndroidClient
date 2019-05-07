package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResultPassing;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.MasterChildren.ProductListOrderActivity;
import ir.rayas.app.citywareclient.ViewModel.Marketing.ProductCommissionAndDiscountModel;


public class OrderProductsRecyclerViewAdapter extends RecyclerView.Adapter<OrderProductsRecyclerViewAdapter.ViewHolder> {

    private List<ProductCommissionAndDiscountModel> ViewModelList = null;
    private ProductListOrderActivity Context;
    private RecyclerView Container = null;

    public OrderProductsRecyclerViewAdapter(ProductListOrderActivity context, List<ProductCommissionAndDiscountModel> ViewModel, RecyclerView Container) {
        this.ViewModelList = ViewModel;
        this.Container = Container;
        this.Context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextViewPersian ProductNameTextView;
        RelativeLayout OrderRelativeLayout;
        TextViewPersian MarketerPercentTextView;
        TextViewPersian CustomerPercentTextView;


        ViewHolder(View v) {
            super(v);
            ProductNameTextView = v.findViewById(R.id.ProductNameTextView);
            OrderRelativeLayout = v.findViewById(R.id.OrderRelativeLayout);
            MarketerPercentTextView = v.findViewById(R.id.MarketerPercentTextView);
            CustomerPercentTextView = v.findViewById(R.id.CustomerPercentTextView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_order, parent, false);
        return new ViewHolder(CurrentView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.ProductNameTextView.setText(ViewModelList.get(position).getProductName());

        holder.MarketerPercentTextView.setText(ViewModelList.get(position).getMarketerPercent() + " " + Context.getResources().getString(R.string.percent));
        holder.CustomerPercentTextView.setText(ViewModelList.get(position).getCustomerPercent() + " " + Context.getResources().getString(R.string.percent));


        holder.OrderRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowEditOrderProductDialog(ViewModelList.get(position));
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

    private void SendDataToParentActivity(ProductCommissionAndDiscountModel ViewModel) {

        HashMap<String, Object> Output = new HashMap<>();
        Output.put("ProductCommissionAndDiscountModel", ViewModel);
        ActivityResultPassing.Push(new ActivityResult(Context.getParentActivity(), Context.getCurrentActivityId(), Output));
    }


    private void ShowEditOrderProductDialog(final ProductCommissionAndDiscountModel productCommissionAndDiscountModel) {

        final Dialog EditOrderProductDialog = new Dialog(Context);
        EditOrderProductDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        EditOrderProductDialog.setContentView(R.layout.dialog_order_product);

        Typeface typeface = Typeface.createFromAsset(Context.getAssets(), "fonts/iransanslight.ttf");

        ButtonPersianView DialogOrderProductOkButton = EditOrderProductDialog.findViewById(R.id.DialogOrderProductOkButton);
        ButtonPersianView DialogOrderProductCancelButton = EditOrderProductDialog.findViewById(R.id.DialogOrderProductCancelButton);
        final EditText UnitPriceProductTextView = EditOrderProductDialog.findViewById(R.id.UnitPriceProductTextView);
        final EditText CountProductTextView = EditOrderProductDialog.findViewById(R.id.CountProductTextView);


        UnitPriceProductTextView.setTypeface(typeface);
        CountProductTextView.setTypeface(typeface);

        UnitPriceProductTextView.setText(Utility.GetIntegerNumberWithComma(productCommissionAndDiscountModel.getPrice()));
        CountProductTextView.setText(String.valueOf(productCommissionAndDiscountModel.getNumberOfOrder()));

        DialogOrderProductOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProductCommissionAndDiscountModel ViewModel = new ProductCommissionAndDiscountModel();
                ViewModel.setProductName(productCommissionAndDiscountModel.getProductName());
                ViewModel.setPrice(Double.parseDouble(UnitPriceProductTextView.getText().toString()));
                ViewModel.setNumberOfOrder(Double.parseDouble(CountProductTextView.getText().toString()));
                ViewModel.setApplicationPercent(productCommissionAndDiscountModel.getApplicationPercent());
                ViewModel.setCustomerPercent(productCommissionAndDiscountModel.getCustomerPercent());
                ViewModel.setMarketerPercent(productCommissionAndDiscountModel.getMarketerPercent());
                ViewModel.setProductId(productCommissionAndDiscountModel.getProductId());

                Double TotalPrice = Double.parseDouble(UnitPriceProductTextView.getText().toString()) * Double.parseDouble(CountProductTextView.getText().toString());
                ViewModel.setTotalPrice(TotalPrice);

                SetViewModel(ViewModel);


                SendDataToParentActivity(productCommissionAndDiscountModel);
//        //این قسمت به دلیل SingleInstance بودن Parent بایستی مطمئن شوبم که اکتیویتی Parent بعد از اتمام این اکتیویتی دوباره صدا  زده می شود
//        //در حالت خروج از برنامه و ورود دوباره این اکتیوتی ممکن است Parent خود را گم کند
                Context.FinishCurrentActivity();


                EditOrderProductDialog.dismiss();

            }
        });

        DialogOrderProductCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditOrderProductDialog.dismiss();
            }
        });

        EditOrderProductDialog.show();
    }


}
