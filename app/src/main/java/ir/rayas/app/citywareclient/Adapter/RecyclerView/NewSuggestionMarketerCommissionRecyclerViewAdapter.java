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
import ir.rayas.app.citywareclient.View.MasterChildren.ShowBusinessDetailsActivity;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowCommissionDetailsActivity;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowMarketerCommissionDetailsActivity;
import ir.rayas.app.citywareclient.ViewModel.Marketing.BusinessCommissionAndDiscountViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.MarketingBusinessManViewModel;


public class NewSuggestionMarketerCommissionRecyclerViewAdapter extends RecyclerView.Adapter<NewSuggestionMarketerCommissionRecyclerViewAdapter.ViewHolder> {

    private List<MarketingBusinessManViewModel> ViewModelList = null;
    private ShowMarketerCommissionDetailsActivity Context;
    private RecyclerView Container = null;

    public NewSuggestionMarketerCommissionRecyclerViewAdapter(ShowMarketerCommissionDetailsActivity context, List<MarketingBusinessManViewModel> ViewModel, RecyclerView Container) {
        this.Context = context;
        this.ViewModelList = ViewModel;
        this.Container = Container;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextViewPersian BusinessTitleTextView;
        TextViewPersian FullNameTextView;
        TextViewPersian TicketNumberTextView;
        TextViewPersian UseDateTextView;
        TextViewPersian ExpireDateTextView;
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

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_new_suggestion_marketer, parent, false);
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
                ShowCommissionDetailsIntent.putExtra("Percents", ViewModelList.get(position).getPercents());
                Context.startActivity(ShowCommissionDetailsIntent);
            }
        });


    }

    /**
     * اضافه مودن لیست جدید
     *
     * @param ViewModel
     */
    public void AddViewModelList(List<MarketingBusinessManViewModel> ViewModel) {
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
    public void SetViewModelList(List<MarketingBusinessManViewModel> ViewModel) {
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
