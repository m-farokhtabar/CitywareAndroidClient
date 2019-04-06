package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;

import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.MasterChildren.PrizeAllActivity;
import ir.rayas.app.citywareclient.View.UserProfileChildren.PackageActivity;
import ir.rayas.app.citywareclient.ViewModel.Club.PrizeViewModel;


public class PrizeAllRecyclerViewAdapter extends RecyclerView.Adapter<PrizeAllRecyclerViewAdapter.ViewHolder> {

    private List<PrizeViewModel> ViewModelList = null;
    private Double MyPoint;
    private PrizeAllActivity Context;

    public PrizeAllRecyclerViewAdapter(PrizeAllActivity context, List<PrizeViewModel> ViewModel, Double myPoint) {
        this.ViewModelList = ViewModel;
        this.MyPoint = myPoint;
        this.Context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextViewPersian PercentTextView;
        ProgressBar PercentProgressBar;
        TextViewPersian TitlePrizeTextView;
        TextViewPersian DescriptionPrizeTextView;
        TextViewPersian PointTextView;
        CardView GetPrizeCardView;


        public ViewHolder(View v) {
            super(v);
            PercentProgressBar = v.findViewById(R.id.PercentProgressBar);
            PercentTextView = v.findViewById(R.id.PercentTextView);
            TitlePrizeTextView = v.findViewById(R.id.TitlePrizeTextView);
            DescriptionPrizeTextView = v.findViewById(R.id.DescriptionPrizeTextView);
            PointTextView = v.findViewById(R.id.PointTextView);
            GetPrizeCardView = v.findViewById(R.id.GetPrizeCardView);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_prize_all, parent, false);
        return new ViewHolder(CurrentView);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.DescriptionPrizeTextView.setText(ViewModelList.get(position).getDescription());
        holder.TitlePrizeTextView.setText(ViewModelList.get(position).getTitle());
        holder.PointTextView.setText(String.valueOf((int) ViewModelList.get(position).getPoint()) + "  " + Context.getResources().getString(R.string.rate));


        Double Point = ViewModelList.get(position).getPoint();
        Double Percent;
        if (Point > 0 && Point != null) {
            Percent = (MyPoint * 100) / Point;
            if (Percent >= 100) {
                Percent = 100.0;
            } else {
                Percent = Percent;
            }
        } else {
            Percent = 0.0;
        }

        int PercentProgress = (int) Double.parseDouble(Percent.toString());
        holder.PercentTextView.setText(String.valueOf(PercentProgress) + " " + Context.getResources().getString(R.string.percent));

        Drawable drawable = Context.getResources().getDrawable(R.drawable.circular_percent_progress_bar_yellow);
        holder.PercentProgressBar.setProgress(PercentProgress);   // Main Progress
        holder.PercentProgressBar.setSecondaryProgress(100); // Secondary Progress
        holder.PercentProgressBar.setMax(100); // Maximum Progress
        holder.PercentProgressBar.setProgressDrawable(drawable);

        if (PercentProgress == 100) {
            holder.GetPrizeCardView.setVisibility(View.VISIBLE);

            holder.GetPrizeCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (ViewModelList.get(position).getPackageId() == null || ViewModelList.get(position).getPackageId() == 0) {
                        ShowBuyPrizeDialog((int) Double.parseDouble(MyPoint.toString()), (int) ViewModelList.get(position).getPoint(), ViewModelList.get(position).getTitle());
                    }else{
                        Intent NewPackageIntent = Context.NewIntent(PackageActivity.class);
                        NewPackageIntent.putExtra("New","BuyPrize");
                        NewPackageIntent.putExtra("PackageId",ViewModelList.get(position).getPackageId());
                        Context.startActivity(NewPackageIntent);
                    }

                }
            });

        } else {
            holder.GetPrizeCardView.setVisibility(View.GONE);
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

    private void ShowBuyPrizeDialog(int YourPoint, int Point, String PackageName) {

        final Dialog BuyPrizeDialog = new Dialog(Context);
        BuyPrizeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        BuyPrizeDialog.setContentView(R.layout.dialog_buy_prize);

        ButtonPersianView DialogBuyPrizeOkButton = BuyPrizeDialog.findViewById(R.id.DialogBuyPrizeOkButton);
        ButtonPersianView DialogBuyPrizeCancelButton = BuyPrizeDialog.findViewById(R.id.DialogBuyPrizeCancelButton);


        TextViewPersian DialogPointTextView = BuyPrizeDialog.findViewById(R.id.DialogPointTextView);
        TextViewPersian DialogMyPointTextView = BuyPrizeDialog.findViewById(R.id.DialogMyPointTextView);
        DialogMyPointTextView.setText(String.valueOf(YourPoint));

        String Message = Context.getResources().getString(R.string.for_buy) + " " + PackageName + " " + Context.getResources().getString(R.string.need_to) + " " + Point + " " + Context.getResources().getString(R.string.are_you_sure_you_want_to_purchase_the_package_you_are_looking_for);
        DialogPointTextView.setText(Message);


        DialogBuyPrizeOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyPrizeDialog.dismiss();

            }
        });

        DialogBuyPrizeCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyPrizeDialog.dismiss();
            }
        });

        BuyPrizeDialog.show();
    }
}

