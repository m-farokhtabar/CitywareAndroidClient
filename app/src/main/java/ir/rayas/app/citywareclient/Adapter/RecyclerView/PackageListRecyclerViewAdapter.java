package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.MyClickListener;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.UserProfileChildren.PackageActivity;
import ir.rayas.app.citywareclient.ViewModel.Package.OutPackageViewModel;

/**
 * Created by Hajar on 3/11/2019.
 */

public class PackageListRecyclerViewAdapter extends RecyclerView.Adapter<PackageListRecyclerViewAdapter.ViewHolder> {

    private List<OutPackageViewModel> ViewModelList = null;
    private PackageActivity Context;
    private MyClickListener myClickListener;

    public PackageListRecyclerViewAdapter(PackageActivity context, List<OutPackageViewModel> ViewModel) {
        this.Context = context;
        this.ViewModelList = ViewModel;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case

        TextViewPersian PackageTitleTextView;
        TextViewPersian CreditPriceTextView;
        TextViewPersian PaidPriceTextView;
        TextViewPersian ExpireTextView;
        TextViewPersian RateValueTextView;
        TextViewPersian RateTextView;
        TextViewPersian OrTextView;
        ImageView TagImageView;
        RelativeLayout PackageContainerRelativeLayout;


        public ViewHolder(View v) {
            super(v);

            PackageTitleTextView = v.findViewById(R.id.PackageTitleTextView);
            CreditPriceTextView = v.findViewById(R.id.CreditPriceTextView);
            PaidPriceTextView = v.findViewById(R.id.PaidPriceTextView);
            ExpireTextView = v.findViewById(R.id.ExpireTextView);
            RateValueTextView = v.findViewById(R.id.RateValueTextView);
            RateTextView = v.findViewById(R.id.RateTextView);
            OrTextView = v.findViewById(R.id.OrTextView);
            TagImageView = v.findViewById(R.id.TagImageView);
            PackageContainerRelativeLayout = v.findViewById(R.id.PackageContainerRelativeLayout);

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
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_package_list, parent, false);
        return new ViewHolder(CurrentView);
    }


    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.PackageTitleTextView.setText(ViewModelList.get(position).getTitle());
        holder.CreditPriceTextView.setText(Utility.GetIntegerNumberWithComma((int) ViewModelList.get(position).getCreditPrice()));


        if (ViewModelList.get(position).isAlwaysCredit()) {
            holder.ExpireTextView.setText(Context.getResources().getString(R.string.unlimited));
        }else {
            holder.ExpireTextView.setText(String.valueOf(ViewModelList.get(position).getCreditInDays()) + " " + Context.getResources().getString(R.string.day));
        }


        if (ViewModelList.get(position).isCanPurchaseByPoint()) {
            holder.RateTextView.setVisibility(View.VISIBLE);
            holder.OrTextView.setVisibility(View.VISIBLE);
            holder.RateValueTextView.setVisibility(View.VISIBLE);
            holder.TagImageView.setVisibility(View.VISIBLE);
            holder.PackageContainerRelativeLayout.setBackgroundColor(Context.getResources().getColor(R.color.LightThemeColorDrawable));
            holder.PaidPriceTextView.setText(Utility.GetIntegerNumberWithComma((int) ViewModelList.get(position).getPayablePrice()) + " " + Context.getResources().getString(R.string.toman));
            holder.RateValueTextView.setText(Utility.GetIntegerNumberWithComma((int) ViewModelList.get(position).getPointForPurchasing()));
        } else {
            holder.PaidPriceTextView.setText(Utility.GetIntegerNumberWithComma((int) ViewModelList.get(position).getPayablePrice()) + " " + Context.getResources().getString(R.string.toman));
            holder.PackageContainerRelativeLayout.setBackgroundColor(Context.getResources().getColor(R.color.BackgroundWhiteColor));
            holder.RateTextView.setVisibility(View.GONE);
            holder.OrTextView.setVisibility(View.GONE);
            holder.RateValueTextView.setVisibility(View.GONE);
            holder.TagImageView.setVisibility(View.GONE);
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