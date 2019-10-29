package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.MarketerChildren.FactorDetailsActivity;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowBusinessCommissionActivity;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowBusinessDetailsActivity;
import ir.rayas.app.citywareclient.ViewModel.Marketing.BusinessCommissionAndDiscountViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.MarketingPayedBusinessViewModel;


public class BusinessCommissionReceivedRecyclerViewAdapter extends RecyclerView.Adapter<BusinessCommissionReceivedRecyclerViewAdapter.ViewHolder> {

    private List<MarketingPayedBusinessViewModel> ViewModelList;
    private ShowBusinessCommissionActivity Context;
    private RecyclerView Container ;

    public BusinessCommissionReceivedRecyclerViewAdapter(ShowBusinessCommissionActivity context, List<MarketingPayedBusinessViewModel> ViewModel, RecyclerView Container) {
        this.Context = context;
        this.ViewModelList = ViewModel;
        this.Container = Container;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextViewPersian FullNameTextView;
        TextViewPersian TicketNumberTextView;
        TextViewPersian UseDateTextView;
        TextViewPersian ReceivedTextView;
        TextViewPersian ReceivedDateTitleTextView;
        TextViewPersian PriceTextView;
        TextViewPersian TransactionNumberTextView;
        RelativeLayout DiscountContainerRelativeLayout;
        ButtonPersianView DetailsBusinessButton;
        ButtonPersianView DetailsNoCommissionReceivedButton;
        LinearLayout ReceivedDateLinearLayout;
        LinearLayout UseDateLinearLayout;


        ViewHolder(View v) {
            super(v);
            DiscountContainerRelativeLayout = v.findViewById(R.id.DiscountContainerRelativeLayout);
            DetailsBusinessButton = v.findViewById(R.id.DetailsBusinessButton);
            DetailsNoCommissionReceivedButton = v.findViewById(R.id.DetailsNoCommissionReceivedButton);
            FullNameTextView = v.findViewById(R.id.FullNameTextView);
            TicketNumberTextView = v.findViewById(R.id.TicketNumberTextView);
            ReceivedTextView = v.findViewById(R.id.ExpireDateTextView);
            UseDateTextView = v.findViewById(R.id.UseDateTextView);
            ReceivedDateLinearLayout = v.findViewById(R.id.ExpireDateLinearLayout);
            UseDateLinearLayout = v.findViewById(R.id.UseDateLinearLayout);
            PriceTextView = v.findViewById(R.id.PriceTextView);
            ReceivedDateTitleTextView = v.findViewById(R.id.ExpireDateTitleTextView);
            TransactionNumberTextView = v.findViewById(R.id.TransactionNumberTextView);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_business_commission_paid, parent, false);
        return new ViewHolder(CurrentView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final BusinessCommissionAndDiscountViewModel businessCommissionAndDiscountViewModel;
        Gson gson = new Gson();
        Type listType = new TypeToken<BusinessCommissionAndDiscountViewModel>() {
        }.getType();
        businessCommissionAndDiscountViewModel = gson.fromJson(ViewModelList.get(position).getPercents(), listType);

        holder.FullNameTextView.setText(ViewModelList.get(position).getCustomerFullName());
        holder.TicketNumberTextView.setText(ViewModelList.get(position).getTicket());
        holder.TransactionNumberTextView.setText(ViewModelList.get(position).getTransactionNumber());


        holder.PriceTextView.setText(Utility.GetIntegerNumberWithComma(ViewModelList.get(position).getPrice()) + " " + Context.getResources().getString(R.string.toman));


        if (ViewModelList.get(position).getUseTicketDate().equals("") || ViewModelList.get(position).getUseTicketDate() == null) {
            holder.UseDateLinearLayout.setVisibility(View.GONE);
        } else {
            holder.UseDateLinearLayout.setVisibility(View.VISIBLE);
            holder.UseDateTextView.setText(ViewModelList.get(position).getUseTicketDate());
        }

        if (ViewModelList.get(position).getPayedDate() ==null){
            holder.ReceivedDateLinearLayout.setVisibility(View.GONE);
        }else {
            if (ViewModelList.get(position).getPayedDate().equals("")) {
                holder.ReceivedDateLinearLayout.setVisibility(View.GONE);
            } else {
                holder.ReceivedDateLinearLayout.setVisibility(View.VISIBLE);
                holder.ReceivedTextView.setText(ViewModelList.get(position).getPayedDate());
                holder.ReceivedDateTitleTextView.setText(Context.getResources().getString(R.string.paid_date));
            }
        }

        holder.DetailsBusinessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ShowBusinessDetailsIntent = Context.NewIntent(ShowBusinessDetailsActivity.class);
                ShowBusinessDetailsIntent.putExtra("BusinessId", businessCommissionAndDiscountViewModel.getBusinessId());
                Context.startActivity(ShowBusinessDetailsIntent);

            }
        });

        if (ViewModelList.get(position).getFactor() == null || ViewModelList.get(position).getFactor().equals("")){
            holder.DetailsNoCommissionReceivedButton.setEnabled(false);
            holder.DetailsNoCommissionReceivedButton.setClickable(false);
            holder.DetailsNoCommissionReceivedButton.setText(Context.getResources().getString(R.string.not_submit_factor));

        }  else {
            holder.DetailsNoCommissionReceivedButton.setEnabled(true);
            holder.DetailsNoCommissionReceivedButton.setClickable(true);
            holder.DetailsNoCommissionReceivedButton.setText(Context.getResources().getString(R.string.details_factor));

            holder.DetailsNoCommissionReceivedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Intent ShowCommissionDetailsIntent = Context.NewIntent(ShowCommissionDetailsActivity.class);
//                ShowCommissionDetailsIntent.putExtra("BusinessId", businessCommissionAndDiscountViewModel.getBusinessId());
//                ShowCommissionDetailsIntent.putExtra("BusinessName", businessCommissionAndDiscountViewModel.getBusinessName());
//                Context.startActivity(ShowCommissionDetailsIntent);

                    Intent FacturDetailsIntent = Context.NewIntent(FactorDetailsActivity.class);
                    FacturDetailsIntent.putExtra("FactureDetails", ViewModelList.get(position).getFactor());
                    Context.startActivity(FacturDetailsIntent);
                }
            });
        }


    }


    /**
     * اضافه مودن لیست جدید
     *
     * @param ViewModel
     */
    public void AddViewModelList(List<MarketingPayedBusinessViewModel> ViewModel) {
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
    public void SetViewModelList(List<MarketingPayedBusinessViewModel> ViewModel) {
        ViewModelList = new ArrayList<>();
        ViewModelList.addAll(ViewModel);
        notifyDataSetChanged();
        Container.invalidate();
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
