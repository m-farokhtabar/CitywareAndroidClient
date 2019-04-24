package ir.rayas.app.citywareclient.Adapter.RecyclerView;

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
import ir.rayas.app.citywareclient.Share.Enum.StatusDiscountType;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.MasterChildren.DiscountActivity;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowBusinessDetailsActivity;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowDiscountDetailsActivity;
import ir.rayas.app.citywareclient.ViewModel.Marketing.BusinessCommissionAndDiscountViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.MarketingCustomerViewModel;


public class DiscountRecyclerViewAdapter extends RecyclerView.Adapter<DiscountRecyclerViewAdapter.ViewHolder> {

    private List<MarketingCustomerViewModel> ViewModelList = null;
    private DiscountActivity Context;

    public DiscountRecyclerViewAdapter(DiscountActivity context, List<MarketingCustomerViewModel> ViewModel) {
        this.Context = context;
        this.ViewModelList = ViewModel;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextViewPersian BusinessTitleTextView;
        TextViewPersian FullNameTextView;
        TextViewPersian TicketNumberTextView;
        TextViewPersian StateTextView;
        TextViewPersian UseDateTextView;
        TextViewPersian ExpireDateTextView;
        RelativeLayout DiscountContainerRelativeLayout;
        ButtonPersianView DetailsBusinessButton;
        ButtonPersianView DetailsDiscountButton;
        LinearLayout ExpireDateLinearLayout;
        LinearLayout UseDateLinearLayout;


        ViewHolder(View v) {
            super(v);
            DiscountContainerRelativeLayout = v.findViewById(R.id.DiscountContainerRelativeLayout);
            BusinessTitleTextView = v.findViewById(R.id.BusinessTitleTextView);
            DetailsBusinessButton = v.findViewById(R.id.DetailsBusinessButton);
            DetailsDiscountButton = v.findViewById(R.id.DetailsDiscountButton);
            FullNameTextView = v.findViewById(R.id.FullNameTextView);
            TicketNumberTextView = v.findViewById(R.id.TicketNumberTextView);
            StateTextView = v.findViewById(R.id.StateTextView);
            ExpireDateTextView = v.findViewById(R.id.ExpireDateTextView);
            UseDateTextView = v.findViewById(R.id.UseDateTextView);
            ExpireDateLinearLayout = v.findViewById(R.id.ExpireDateLinearLayout);
            UseDateLinearLayout = v.findViewById(R.id.UseDateLinearLayout);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_discount, parent, false);
        return new ViewHolder(CurrentView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final BusinessCommissionAndDiscountViewModel businessCommissionAndDiscountViewModel;
        Gson gson = new Gson();
        Type listType = new TypeToken<BusinessCommissionAndDiscountViewModel>() {
        }.getType();
        businessCommissionAndDiscountViewModel = gson.fromJson(ViewModelList.get(position).getPercents(), listType);

        holder.BusinessTitleTextView.setText(businessCommissionAndDiscountViewModel.getBusinessName());
        holder.FullNameTextView.setText(ViewModelList.get(position).getMarketerFullName());
        holder.TicketNumberTextView.setText(ViewModelList.get(position).getTicket());
        holder.UseDateTextView.setText(ViewModelList.get(position).getUseTicketDate());
        holder.ExpireDateTextView.setText(ViewModelList.get(position).getTicketValidity());

        if (StatusDiscountType.NotUse.GetStatusDiscount() == ViewModelList.get(position).getStatus()) {

            holder.DiscountContainerRelativeLayout.setBackgroundColor(Context.getResources().getColor(R.color.BackgroundWhiteColor));
            holder.ExpireDateLinearLayout.setVisibility(View.VISIBLE);
            holder.UseDateLinearLayout.setVisibility(View.GONE);
            holder.StateTextView.setText(Context.getResources().getString(R.string.not_use));

        } else if (StatusDiscountType.Use.GetStatusDiscount() == ViewModelList.get(position).getStatus()) {

            holder.DiscountContainerRelativeLayout.setBackgroundColor(Context.getResources().getColor(R.color.BackgroundBlueColorPrize));
            holder.ExpireDateLinearLayout.setVisibility(View.GONE);
            holder.UseDateLinearLayout.setVisibility(View.VISIBLE);
            holder.StateTextView.setText(Context.getResources().getString(R.string.use));

        } else if (StatusDiscountType.Expire.GetStatusDiscount() == ViewModelList.get(position).getStatus()) {

            holder.DiscountContainerRelativeLayout.setBackgroundColor(Context.getResources().getColor(R.color.BackgroundRedColorPrize));
            holder.ExpireDateLinearLayout.setVisibility(View.VISIBLE);
            holder.UseDateLinearLayout.setVisibility(View.GONE);
            holder.StateTextView.setText(Context.getResources().getString(R.string.expire));

        }


        holder.DetailsBusinessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ShowBusinessDetailsIntent = Context.NewIntent(ShowBusinessDetailsActivity.class);
                ShowBusinessDetailsIntent.putExtra("BusinessId",businessCommissionAndDiscountViewModel.getBusinessId());
                Context.startActivity(ShowBusinessDetailsIntent);

            }
        });

        holder.DetailsDiscountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ShowDiscountDetailsIntent = Context.NewIntent(ShowDiscountDetailsActivity.class);
                ShowDiscountDetailsIntent.putExtra("BusinessId", businessCommissionAndDiscountViewModel.getBusinessId());
                ShowDiscountDetailsIntent.putExtra("BusinessName",businessCommissionAndDiscountViewModel.getBusinessName());
                Context.startActivity(ShowDiscountDetailsIntent);
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
