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
import ir.rayas.app.citywareclient.View.Share.BusinessFactorListActivity;
import ir.rayas.app.citywareclient.View.Share.BusinessListForFactorActivity;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessViewModel;

/**
 * Created by Hajar on 2/22/2019.
 */

public class BusinessListForFactorRecyclerViewAdapter extends RecyclerView.Adapter<BusinessListForFactorRecyclerViewAdapter.ViewHolder> {

    private List<BusinessViewModel> ViewModelList = null;
    private RecyclerView Container = null;
    private BusinessListForFactorActivity Context;

    public BusinessListForFactorRecyclerViewAdapter(BusinessListForFactorActivity context, List<BusinessViewModel> ViewModel, RecyclerView Container) {
        this.Context = context;
        this.Container = Container;
        this.ViewModelList = ViewModel;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextViewPersian BusinessTitleTextView;
        public TextViewPersian AddressTextView;
        public RelativeLayout BusinessContainerRelativeLayout;


        public ViewHolder(View v) {
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
        ViewHolder CurrentViewHolder = new ViewHolder(CurrentView);
        return CurrentViewHolder;
    }


    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.BusinessTitleTextView.setText(ViewModelList.get(position).getTitle());
        String Address = ViewModelList.get(position).getRegionName() + " - " + ViewModelList.get(position).getAddress();
        holder.AddressTextView.setText(Address);


        if (ViewModelList.get(position).isActive()) {

            holder.BusinessContainerRelativeLayout.setClickable(true);
            holder.BusinessContainerRelativeLayout.setBackgroundColor(Context.getResources().getColor(R.color.BackgroundWhiteColor));

            holder.BusinessContainerRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent EditBusinessIconIntent = Context.NewIntent(BusinessFactorListActivity.class);
                    EditBusinessIconIntent.putExtra("BusinessId", ViewModelList.get(position).getId());
                    Context.startActivity(EditBusinessIconIntent);

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