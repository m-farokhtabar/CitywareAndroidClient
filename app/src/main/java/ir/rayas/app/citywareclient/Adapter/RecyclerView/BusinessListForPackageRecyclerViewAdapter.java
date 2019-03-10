package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.MyClickListener;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.View.Share.MapActivity;
import ir.rayas.app.citywareclient.View.UserProfileChildren.PackageActivity;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessViewModel;



public class BusinessListForPackageRecyclerViewAdapter extends RecyclerView.Adapter<BusinessListForPackageRecyclerViewAdapter.ViewHolder> {

    private List<BusinessViewModel> ViewModelList = null;
    private PackageActivity Context;
    private MyClickListener myClickListener;

    public BusinessListForPackageRecyclerViewAdapter(PackageActivity context, List<BusinessViewModel> ViewModel) {
        this.Context = context;
        this.ViewModelList = ViewModel;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case

        TextViewPersian UserBusinessTitleTextView;
        TextViewPersian CategoryNameTextViewUserBusiness;
        TextViewPersian ConfirmStateTextViewUserBusiness;
        TextViewPersian ShowMapBusinessIconTextViewUserBusiness;
        TextViewPersian ConfirmStateTitleTextViewUserBusiness;
        TextViewPersian AddressTextViewUserBusiness;
        TextViewPersian ShowMapBusinessTextViewUserBusiness;
        LinearLayout ShowMapLinearLayoutUserBusiness;


        public ViewHolder(View v) {
            super(v);

            UserBusinessTitleTextView = v.findViewById(R.id.UserBusinessTitleTextView);
            CategoryNameTextViewUserBusiness = v.findViewById(R.id.CategoryNameTextViewUserBusiness);
            ConfirmStateTextViewUserBusiness = v.findViewById(R.id.ConfirmStateTextViewUserBusiness);
            ShowMapBusinessIconTextViewUserBusiness = v.findViewById(R.id.ShowMapBusinessIconTextViewUserBusiness);
            ShowMapLinearLayoutUserBusiness = v.findViewById(R.id.ShowMapLinearLayoutUserBusiness);
            ConfirmStateTitleTextViewUserBusiness = v.findViewById(R.id.ConfirmStateTitleTextViewUserBusiness);
            AddressTextViewUserBusiness = v.findViewById(R.id.AddressTextViewUserBusiness);
            ShowMapBusinessTextViewUserBusiness = v.findViewById(R.id.ShowMapBusinessTextViewUserBusiness);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_business_list, parent, false);
        return new ViewHolder(CurrentView);
    }


    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.UserBusinessTitleTextView.setText(ViewModelList.get(position).getTitle());
        holder.CategoryNameTextViewUserBusiness.setText(ViewModelList.get(position).getBusinessCategoryName());
        holder.ConfirmStateTextViewUserBusiness.setText(ViewModelList.get(position).getConfirmTypeName());

        holder.ShowMapBusinessIconTextViewUserBusiness.setTypeface(Font.MasterIcon);
        holder.ShowMapBusinessIconTextViewUserBusiness.setText("\uf041");

        String Address = ViewModelList.get(position).getRegionName() + " - " + ViewModelList.get(position).getAddress();
        holder.AddressTextViewUserBusiness.setText(Address);

        if (ViewModelList.get(position).getLatitude() > 0 && ViewModelList.get(position).getLongitude() > 0) {
            holder.ShowMapLinearLayoutUserBusiness.setVisibility(View.VISIBLE);
        } else {
            holder.ShowMapLinearLayoutUserBusiness.setVisibility(View.GONE);
        }



        if (ViewModelList.get(position).isActive()) {

            holder.ShowMapLinearLayoutUserBusiness.setEnabled(true);
            holder.ShowMapLinearLayoutUserBusiness.setClickable(true);
            holder.ShowMapBusinessTextViewUserBusiness.setTextColor(Context.getResources().getColor(R.color.FontSemiDarkThemeColor));
            holder.ShowMapBusinessIconTextViewUserBusiness.setTextColor(Context.getResources().getColor(R.color.FontSemiDarkThemeColor));

            if (ViewModelList.get(position).getConfirmTypeId() == 3) {
                holder.ConfirmStateTitleTextViewUserBusiness.setTextColor(Context.getResources().getColor(R.color.FontGreenColor));
                holder.ConfirmStateTextViewUserBusiness.setTextColor(Context.getResources().getColor(R.color.FontGreenColor));
            } else if (ViewModelList.get(position).getConfirmTypeId() == 4) {
                holder.ConfirmStateTitleTextViewUserBusiness.setTextColor(Context.getResources().getColor(R.color.FontRedColor));
                holder.ConfirmStateTextViewUserBusiness.setTextColor(Context.getResources().getColor(R.color.FontRedColor));
            } else {
                holder.ConfirmStateTitleTextViewUserBusiness.setTextColor(Context.getResources().getColor(R.color.FontSemiBlackColor));
                holder.ConfirmStateTextViewUserBusiness.setTextColor(Context.getResources().getColor(R.color.FontBlackColor));
            }

            holder.ShowMapLinearLayoutUserBusiness.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent MapIntent = Context.NewIntent(MapActivity.class);
                    MapIntent.putExtra("Latitude", ViewModelList.get(position).getLatitude());
                    MapIntent.putExtra("Longitude", ViewModelList.get(position).getLongitude());
                    MapIntent.putExtra("Going", 2);
                    Context.startActivity(MapIntent);
                }
            });
        } else {

            holder.ShowMapLinearLayoutUserBusiness.setEnabled(false);
            holder.ShowMapLinearLayoutUserBusiness.setClickable(false);
            holder.ShowMapBusinessTextViewUserBusiness.setTextColor(LayoutUtility.GetColorFromResource(Context, R.color.FontSemiBlackColor));
            holder.ShowMapBusinessIconTextViewUserBusiness.setTextColor(LayoutUtility.GetColorFromResource(Context, R.color.FontSemiBlackColor));
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

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }
}