package ir.rayas.app.citywareclient.Adapter.RecyclerView;

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
import ir.rayas.app.citywareclient.ViewModel.Marketing.BusinessCommissionAndDiscountViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.MarketingCustomerViewModel;



public class ExpireDiscountRecyclerViewAdapter extends RecyclerView.Adapter<ExpireDiscountRecyclerViewAdapter.ViewHolder> {

    private List<MarketingCustomerViewModel> ViewModelList = null;
    private RecyclerView Container = null;

    public ExpireDiscountRecyclerViewAdapter( List<MarketingCustomerViewModel> ViewModel, RecyclerView Container) {
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


        holder.UseDateLinearLayout.setVisibility(View.GONE);
        holder.ExpireDateLinearLayout.setVisibility(View.VISIBLE);


        holder.DetailsBusinessButton.setVisibility(View.GONE);
        holder.DetailsDiscountButton.setVisibility(View.GONE);

    }


    /**
     * اضافه مودن لیست جدید
     *
     * @param ViewModel
     */
    public void AddViewModelList(List<MarketingCustomerViewModel> ViewModel) {
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
    public void SetViewModelList(List<MarketingCustomerViewModel> ViewModel) {
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
