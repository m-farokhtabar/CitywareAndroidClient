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
import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Enum.CommissionBusinessType;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowBusinessDetailsActivity;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowCommissionDetailsActivity;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowMarketerCommissionDetailsActivity;
import ir.rayas.app.citywareclient.ViewModel.Marketing.BusinessCommissionAndDiscountViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.MarketingBusinessManViewModel;


public class ArchiveRecyclerViewAdapter extends RecyclerView.Adapter<ArchiveRecyclerViewAdapter.ViewHolder> {

    private List<MarketingBusinessManViewModel> ViewModelList = null;
    private ShowMarketerCommissionDetailsActivity Context;

    public ArchiveRecyclerViewAdapter(ShowMarketerCommissionDetailsActivity context, List<MarketingBusinessManViewModel> ViewModel) {
        this.Context = context;
        this.ViewModelList = ViewModel;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextViewPersian BusinessTitleTextView;
        TextViewPersian FullNameTextView;
        TextViewPersian TicketNumberTextView;
        TextViewPersian UseDateTextView;
        TextViewPersian ExpireDateTextView;
        TextViewPersian PriceTextView;
        RelativeLayout DiscountContainerRelativeLayout;
        ButtonPersianView DetailsBusinessButton;
        ButtonPersianView DetailsNoCommissionReceivedButton;
        LinearLayout ExpireDateLinearLayout;
        LinearLayout UseDateLinearLayout;


        ViewHolder(View v) {
            super(v);
            DiscountContainerRelativeLayout = v.findViewById(R.id.DiscountContainerRelativeLayout);
            BusinessTitleTextView = v.findViewById(R.id.BusinessTitleTextView);
            DetailsBusinessButton = v.findViewById(R.id.DetailsBusinessButton);
            DetailsNoCommissionReceivedButton = v.findViewById(R.id.DetailsNoCommissionReceivedButton);
            FullNameTextView = v.findViewById(R.id.FullNameTextView);
            TicketNumberTextView = v.findViewById(R.id.TicketNumberTextView);
            ExpireDateTextView = v.findViewById(R.id.ExpireDateTextView);
            UseDateTextView = v.findViewById(R.id.UseDateTextView);
            ExpireDateLinearLayout = v.findViewById(R.id.ExpireDateLinearLayout);
            UseDateLinearLayout = v.findViewById(R.id.UseDateLinearLayout);
            PriceTextView = v.findViewById(R.id.PriceTextView);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_no_commission_received, parent, false);
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

        holder.BusinessTitleTextView.setText(businessCommissionAndDiscountViewModel.getBusinessName());
        holder.FullNameTextView.setText(ViewModelList.get(position).getCustomerFullName());
        holder.TicketNumberTextView.setText(ViewModelList.get(position).getTicket());

        holder.ExpireDateTextView.setText(ViewModelList.get(position).getTicketValidity());

        if (CommissionBusinessType.NotSpecify.GetCommission() == ViewModelList.get(position).getStatus()) {
            holder.PriceTextView.setText(Context.getResources().getString(R.string.business_does_not_specify_a_commission));

        } else if (CommissionBusinessType.Specify.GetCommission() == ViewModelList.get(position).getStatus()) {
            holder.PriceTextView.setText(Utility.GetIntegerNumberWithComma(ViewModelList.get(position).getPrice()) + " " + Context.getResources().getString(R.string.toman));
        }

        if (ViewModelList.get(position).getUseTicketDate().equals("") || ViewModelList.get(position).getUseTicketDate() == null) {
            holder.UseDateLinearLayout.setVisibility(View.GONE);
        } else {
            holder.UseDateLinearLayout.setVisibility(View.VISIBLE);
            holder.UseDateTextView.setText(ViewModelList.get(position).getUseTicketDate());
        }


        holder.DetailsBusinessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ShowBusinessDetailsIntent = Context.NewIntent(ShowBusinessDetailsActivity.class);
                ShowBusinessDetailsIntent.putExtra("BusinessId", businessCommissionAndDiscountViewModel.getBusinessId());
                Context.startActivity(ShowBusinessDetailsIntent);

            }
        });

        holder.DetailsNoCommissionReceivedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ShowCommissionDetailsIntent = Context.NewIntent(ShowCommissionDetailsActivity.class);
                ShowCommissionDetailsIntent.putExtra("BusinessId", businessCommissionAndDiscountViewModel.getBusinessId());
                ShowCommissionDetailsIntent.putExtra("BusinessName", businessCommissionAndDiscountViewModel.getBusinessName());
                Context.startActivity(ShowCommissionDetailsIntent);
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
