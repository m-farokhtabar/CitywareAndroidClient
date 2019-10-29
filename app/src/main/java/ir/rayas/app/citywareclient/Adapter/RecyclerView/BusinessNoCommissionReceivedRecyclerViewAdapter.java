package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;


import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.ViewModel.MarketingPayedBusinessAdapterViewModel;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowBusinessCommissionActivity;
import ir.rayas.app.citywareclient.View.MarketerChildren.FactorDetailsActivity;
import ir.rayas.app.citywareclient.ViewModel.Marketing.MarketingPayedBusinessViewModel;


public class BusinessNoCommissionReceivedRecyclerViewAdapter extends RecyclerView.Adapter<BusinessNoCommissionReceivedRecyclerViewAdapter.ViewHolder> {

    private List<MarketingPayedBusinessViewModel> ViewModelList ;
    private List<MarketingPayedBusinessAdapterViewModel> ViewModel = new ArrayList<>();
    private ShowBusinessCommissionActivity Context;
    private RecyclerView Container ;

    public BusinessNoCommissionReceivedRecyclerViewAdapter(ShowBusinessCommissionActivity context, List<MarketingPayedBusinessViewModel> ViewModel, RecyclerView Container) {
        this.Context = context;
        this.ViewModelList = ViewModel;
        this.Container = Container;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextViewPersian FullNameTextView;
        TextViewPersian TicketNumberTextView;
        TextViewPersian PaymentDeadlineTextView;
        TextViewPersian PriceTextView;
        TextViewPersian FactorNumberTextView;
        RelativeLayout DiscountContainerRelativeLayout;
        ButtonPersianView DetailsBusinessButton;
        LinearLayout UseDateLinearLayout;
        RadioButton BusinessCommissionSelectedRadioButton;


        ViewHolder(View v) {
            super(v);
            DiscountContainerRelativeLayout = v.findViewById(R.id.DiscountContainerRelativeLayout);
            DetailsBusinessButton = v.findViewById(R.id.DetailsBusinessButton);
            FullNameTextView = v.findViewById(R.id.FullNameTextView);
            TicketNumberTextView = v.findViewById(R.id.TicketNumberTextView);
            PaymentDeadlineTextView = v.findViewById(R.id.PaymentDeadlineTextView);
            UseDateLinearLayout = v.findViewById(R.id.UseDateLinearLayout);
            PriceTextView = v.findViewById(R.id.PriceTextView);
            BusinessCommissionSelectedRadioButton = v.findViewById(R.id.BusinessCommissionSelectedRadioButton);
            FactorNumberTextView = v.findViewById(R.id.FactorNumberTextView);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_business_commission, parent, false);
        return new ViewHolder(CurrentView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.FullNameTextView.setText(ViewModelList.get(position).getCustomerFullName());
        holder.TicketNumberTextView.setText(ViewModelList.get(position).getTicket());
        holder.FactorNumberTextView.setText(String.valueOf(ViewModelList.get(position).getId()));


        holder.PriceTextView.setText(Utility.GetIntegerNumberWithComma(ViewModelList.get(position).getPrice()) + " " + Context.getResources().getString(R.string.toman));


        if (ViewModelList.get(position).getUseTicketDate().equals("") || ViewModelList.get(position).getUseTicketDate() == null) {
            holder.UseDateLinearLayout.setVisibility(View.GONE);
        } else {
            holder.UseDateLinearLayout.setVisibility(View.VISIBLE);
            holder.PaymentDeadlineTextView.setText(ViewModelList.get(position).getBusinessPayOffDeadLine());
        }

        if (ViewModelList.get(position).getFactor() == null || ViewModelList.get(position).getFactor().equals("")){
            holder.DetailsBusinessButton.setEnabled(false);
            holder.DetailsBusinessButton.setClickable(false);
            holder.DetailsBusinessButton.setText(Context.getResources().getString(R.string.not_submit_factor));

        }  else {
            holder.DetailsBusinessButton.setEnabled(true);
            holder.DetailsBusinessButton.setClickable(true);
            holder.DetailsBusinessButton.setText(Context.getResources().getString(R.string.details_factor));
            holder.DetailsBusinessButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent FactureDetailsIntent = Context.NewIntent(FactorDetailsActivity.class);
                    FactureDetailsIntent.putExtra("FactureDetails", ViewModelList.get(position).getFactor());
                    Context.startActivity(FactureDetailsIntent);


                }
            });
        }

        MarketingPayedBusinessAdapterViewModel marketingPayedBusinessAdapterViewModel = new MarketingPayedBusinessAdapterViewModel();
        marketingPayedBusinessAdapterViewModel.setSelected(false);

        ViewModel.add(marketingPayedBusinessAdapterViewModel);

        holder.BusinessCommissionSelectedRadioButton.setChecked(ViewModel.get(position).getSelected());
//        holder.BusinessCommissionSelectedRadioButton.setChecked(false);


        holder.BusinessCommissionSelectedRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ViewModel.get(position).getSelected()) {
                    ViewModel.get(position).setSelected(false);
                } else {
                    ViewModel.get(position).setSelected(true);
                }
                //   Context.SetViewPriceInFooter((int)ViewModelList.get(position).getPrice(),ViewModel.get(position).getSelected());
                Context.SetViewPriceInFooter(ViewModelList.get(position), ViewModel.get(position).getSelected(), position);
                holder.BusinessCommissionSelectedRadioButton.setChecked(ViewModel.get(position).getSelected());
            }
        });


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

    public void ClearViewModelList() {

        for (int i = 0; i < ViewModelList.size(); i++) {
            MarketingPayedBusinessAdapterViewModel marketingPayedBusinessAdapterViewModel = new MarketingPayedBusinessAdapterViewModel();
            marketingPayedBusinessAdapterViewModel.setSelected(false);

            ViewModel.add(marketingPayedBusinessAdapterViewModel);
        }

        if (ViewModelList != null) {
            if (ViewModelList.size() > 0) {
                ViewModelList.clear();
                notifyDataSetChanged();
                Container.invalidate();
            }
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
