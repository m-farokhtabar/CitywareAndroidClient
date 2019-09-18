package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Master.UserProfileActivity;
import ir.rayas.app.citywareclient.ViewModel.Package.OutputPackageTransactionViewModel;


public class PackageRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private UserProfileActivity Context;
    private RecyclerView Container = null;
    private List<OutputPackageTransactionViewModel> ViewModelList = null;
    private List<OutputPackageTransactionViewModel> ViewModelListSort = null;

    public List<OutputPackageTransactionViewModel> getViewModelListSort() {
        return ViewModelListSort;
    }

    public PackageRecyclerViewAdapter(UserProfileActivity Context, List<OutputPackageTransactionViewModel> PackageList, RecyclerView Container) {
        this.ViewModelList = PackageList;
        this.Context = Context;
        this.Container = Container;
    }


    /**
     * اضافه مودن لیست جدید
     *
     * @param ViewModel
     */
    public void AddViewModelList(List<OutputPackageTransactionViewModel> ViewModel) {
        if (ViewModel != null) {
            if (ViewModelList == null)
                ViewModelList = new ArrayList<>();
            if (ViewModelListSort == null)
                ViewModelListSort = new ArrayList<>();
            ViewModelList.addAll(ViewModel);
            ViewModelListSort.addAll(ViewModel);
            notifyDataSetChanged();
            Container.invalidate();
        }
    }

    /**
     * جایگزین نمودن لیست جدید
     *
     * @param ViewModel
     */
    public void SetViewModelList(List<OutputPackageTransactionViewModel> ViewModel) {
        ViewModelList = new ArrayList<>();
        ViewModelListSort = new ArrayList<>();
        ViewModelList.addAll(ViewModel);
        ViewModelListSort.addAll(ViewModel);
        notifyDataSetChanged();
        Container.invalidate();
    }


    /**
     * اضافه مودن یک آدرس جدید به لیست
     *
     * @param ViewModel اطلاعات پوستر
     */
    public void AddViewModel(OutputPackageTransactionViewModel ViewModel) {
        if (ViewModel != null) {
            if (ViewModelList == null)
                ViewModelList = new ArrayList<>();


            ViewModelList.add(ViewModel);
            SortViewModelList(ViewModelList);
            notifyDataSetChanged();
            Container.invalidate();
        }
    }

    public void SortViewModelList(List<OutputPackageTransactionViewModel> ViewModel) {
        ClearViewModelListSort();

        if (ViewModel != null) {
            for (int i = 0; i < ViewModel.size() - 1; i++) {
                for (int j = i + 1; j < ViewModel.size(); j++) {
                    if (ViewModel.get(i).getId() < ViewModel.get(j).getId()) {
                        OutputPackageTransactionViewModel temp = ViewModel.get(i);
                        ViewModel.set(i, ViewModel.get(j));
                        ViewModel.set(j, temp);
                    }
                }
            }
        }

        ViewModelList = new ArrayList<>();
        ViewModelList.addAll(ViewModel);
        notifyDataSetChanged();
        Container.invalidate();
    }

    public void ClearViewModelList() {
        if (ViewModelList != null) {
            if (ViewModelList.size() >0) {
                ViewModelList.clear();
                notifyDataSetChanged();
                Container.invalidate();
            }
        }
    }

    public void ClearViewModelListSort() {
        if (ViewModelListSort != null) {
            if (ViewModelListSort.size() >0) {
                ViewModelListSort.clear();
                notifyDataSetChanged();
                Container.invalidate();
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(Context).inflate(R.layout.recycler_view_package, parent, false);
        return new PackageViewHolder(view);

    }


    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        PackageViewHolder viewHolder = (PackageViewHolder) holder;


        viewHolder.PackageTitleTextView.setText(ViewModelList.get(position).getPackageName());

        if (ViewModelList.get(position).getExpireDate().equals("") || ViewModelList.get(position).getExpireDate() == null)
            viewHolder.ExpireTextView.setText(Context.getResources().getString(R.string.unlimited));
        else
            viewHolder.ExpireTextView.setText(ViewModelList.get(position).getExpireDate());

        viewHolder.CreditPriceTextView.setText(Utility.GetIntegerNumberWithComma(ViewModelList.get(position).getPackageCredit()));
        viewHolder.ConsumerCreditTextView.setText(Utility.GetIntegerNumberWithComma(ViewModelList.get(position).getConsumePackageCredit()));
        viewHolder.CreateDateTextView.setText(ViewModelList.get(position).getCreate());


        Double Percent;
        if (ViewModelList.get(position).getPackageCredit() > 0 && ViewModelList.get(position).getPackageCredit() != null) {
            Percent = (ViewModelList.get(position).getConsumePackageCredit() * 100) / ViewModelList.get(position).getPackageCredit();
        } else {
            Percent = 0.0;
        }

        int PercentProgress = (int) Double.parseDouble(Percent.toString());
        viewHolder.PercentTextView.setText(String.valueOf(PercentProgress) + " " + Context.getResources().getString(R.string.percent));

        Drawable drawable = Context.getResources().getDrawable(R.drawable.circular_percent_progress_bar_yellow);
        viewHolder.PercentProgressBar.setProgress(PercentProgress);   // Main Progress
        viewHolder.PercentProgressBar.setSecondaryProgress(100); // Secondary Progress
        viewHolder.PercentProgressBar.setMax(100); // Maximum Progress
        viewHolder.PercentProgressBar.setProgressDrawable(drawable);


        viewHolder.DetailsBuyPackageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDetailsPackageBuyDialog(ViewModelList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return ViewModelList == null ? 0 : ViewModelList.size();
    }


    private class PackageViewHolder extends RecyclerView.ViewHolder {

        TextViewPersian PackageTitleTextView;
        TextViewPersian CreditPriceTextView;
        TextViewPersian ExpireTextView;
        TextViewPersian ConsumerCreditTextView;
        TextViewPersian PercentTextView;
        LinearLayout PackageLinearLayout;
        ProgressBar PercentProgressBar;
        RelativeLayout PercentRelativeLayout;
        ButtonPersianView DetailsBuyPackageButton;
        TextViewPersian CreateDateTextView;


        PackageViewHolder(View v) {
            super(v);
            PackageTitleTextView = v.findViewById(R.id.PackageTitleTextView);
            CreditPriceTextView = v.findViewById(R.id.CreditPriceTextView);
            ExpireTextView = v.findViewById(R.id.ExpireTextView);
            ConsumerCreditTextView = v.findViewById(R.id.ConsumerCreditTextView);
            PackageLinearLayout = v.findViewById(R.id.PackageLinearLayout);
            PercentProgressBar = v.findViewById(R.id.PercentProgressBar);
            PercentTextView = v.findViewById(R.id.PercentTextView);
            PercentRelativeLayout = v.findViewById(R.id.PercentRelativeLayout);
            DetailsBuyPackageButton = v.findViewById(R.id.DetailsBuyPackageButton);
            CreateDateTextView = v.findViewById(R.id.CreateDateTextView);

        }
    }


    private void ShowDetailsPackageBuyDialog(OutputPackageTransactionViewModel ViewModel) {

        final Dialog DetailsBuyPackageDialog = new Dialog(Context);
        DetailsBuyPackageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DetailsBuyPackageDialog.setContentView(R.layout.dialog_details_buy_package);


        ButtonPersianView DialogOkButton = DetailsBuyPackageDialog.findViewById(R.id.DialogOkButton);
        TextViewPersian PaidTypeTextView = DetailsBuyPackageDialog.findViewById(R.id.PaidTypeTextView);
        TextViewPersian PaidPriceTextView = DetailsBuyPackageDialog.findViewById(R.id.PaidPriceTextView);
        TextViewPersian TransactionNumberTextView = DetailsBuyPackageDialog.findViewById(R.id.TransactionNumberTextView);
        TextViewPersian CreateDateTextView = DetailsBuyPackageDialog.findViewById(R.id.CreateDateTextView);
        TextViewPersian PaidPriceTomanTextView = DetailsBuyPackageDialog.findViewById(R.id.PaidPriceTomanTextView);
        TextViewPersian PaidPriceTitleTextView = DetailsBuyPackageDialog.findViewById(R.id.PaidPriceTitleTextView);
        LinearLayout TransactionNumberLinearLayout = DetailsBuyPackageDialog.findViewById(R.id.TransactionNumberLinearLayout);

        if (ViewModel.BuyWithClubPoint > 0) {

            PaidTypeTextView.setText(Context.getResources().getString(R.string.club_point));
            TransactionNumberLinearLayout.setVisibility(View.GONE);
            PaidPriceTomanTextView.setVisibility(View.GONE);
            PaidPriceTitleTextView.setText(Context.getResources().getString(R.string.points_spent));
            PaidPriceTextView.setText(Utility.GetIntegerNumberWithComma(ViewModel.getBuyWithClubPoint()));

        }else {

            PaidTypeTextView.setText(Context.getResources().getString(R.string.cash));
            TransactionNumberLinearLayout.setVisibility(View.VISIBLE);
            PaidPriceTomanTextView.setVisibility(View.VISIBLE);
            PaidPriceTitleTextView.setText(Context.getResources().getString(R.string.paid_price));
            PaidPriceTextView.setText(Utility.GetIntegerNumberWithComma(ViewModel.getPaidPrice()));
        }

        TransactionNumberTextView.setText(ViewModel.getTransactionNumber());
        CreateDateTextView.setText(ViewModel.getCreate());


        DialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailsBuyPackageDialog.dismiss();
            }
        });

        DetailsBuyPackageDialog.show();
    }

}