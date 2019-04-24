package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowBusinessCommissionActivity;
import ir.rayas.app.citywareclient.View.Share.UserBusinessListActivity;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessViewModel;



public class UserBusinessListRecyclerViewAdapter extends RecyclerView.Adapter<UserBusinessListRecyclerViewAdapter.ViewHolder> {

    private List<BusinessViewModel> ViewModelList = null;
    private UserBusinessListActivity Context;

    public UserBusinessListRecyclerViewAdapter(UserBusinessListActivity context, List<BusinessViewModel> ViewModel) {
        this.Context = context;
        this.ViewModelList = ViewModel;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
         TextViewPersian BusinessTitleTextView;
         TextViewPersian AddressTextView;
         RelativeLayout BusinessContainerRelativeLayout;


         ViewHolder(View v) {
            super(v);

            BusinessTitleTextView = v.findViewById(R.id.BusinessTitleTextView);
            AddressTextView = v.findViewById(R.id.AddressTextView);
            BusinessContainerRelativeLayout = v.findViewById(R.id.BusinessContainerRelativeLayout);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_business_list_for_factor, parent, false);
        return new ViewHolder(CurrentView);
    }


    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.BusinessTitleTextView.setText(ViewModelList.get(position).getTitle());
        String Address = ViewModelList.get(position).getRegionName() + " - " + ViewModelList.get(position).getAddress();
        holder.AddressTextView.setText(Address);


        if (ViewModelList.get(position).isActive()) {

            holder.BusinessContainerRelativeLayout.setClickable(true);
            holder.BusinessContainerRelativeLayout.setBackgroundColor(Context.getResources().getColor(R.color.BackgroundWhiteColor));

            holder.BusinessContainerRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent BusinessCommissionIntent = Context.NewIntent(ShowBusinessCommissionActivity.class);
                    BusinessCommissionIntent.putExtra("BusinessId", ViewModelList.get(position).getId());
                    BusinessCommissionIntent.putExtra("BusinessName", ViewModelList.get(position).getTitle());
                    Context.startActivity(BusinessCommissionIntent);

                }
            });


        } else {
            holder.BusinessContainerRelativeLayout.setClickable(false);
            holder.BusinessContainerRelativeLayout.setBackgroundColor(Context.getResources().getColor(R.color.BackgroundColorLightLayout));
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