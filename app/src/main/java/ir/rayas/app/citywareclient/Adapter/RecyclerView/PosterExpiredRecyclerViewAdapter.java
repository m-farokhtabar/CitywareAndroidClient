package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Master.UserProfileActivity;
import ir.rayas.app.citywareclient.ViewModel.Poster.PurchasedPosterViewModel;



public class PosterExpiredRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private UserProfileActivity Context;
    private RecyclerView Container = null;
    private List<PurchasedPosterViewModel> ViewModelList = null;

    public PosterExpiredRecyclerViewAdapter(UserProfileActivity Context, List<PurchasedPosterViewModel> ViewModelList, RecyclerView Container) {
        this.ViewModelList = ViewModelList;
        this.Context = Context;
        this.Container = Container;

    }

    /**
     * اضافه مودن لیست جدید
     *
     * @param ViewModel
     */
    public void AddViewModelList(List<PurchasedPosterViewModel> ViewModel) {
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
    public void SetViewModelList(List<PurchasedPosterViewModel> ViewModel) {
        ViewModelList = new ArrayList<>();
        ViewModelList.addAll(ViewModel);
        notifyDataSetChanged();
        Container.invalidate();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(Context).inflate(R.layout.recycler_view_poster_expired, parent, false);
        return new PosterViewHolder(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        PosterViewHolder holder = (PosterViewHolder) viewHolder;

        if (ViewModelList.get(position).getTitle().equals("") || ViewModelList.get(position).getTitle() == null)
            holder.PosterTitleTextView.setText(ViewModelList.get(position).getPosterTypeTitle());
        else
            holder.PosterTitleTextView.setText(ViewModelList.get(position).getTitle());

        holder.BusinessNameTextView.setText(ViewModelList.get(position).getBusinessName());
        holder.ExpireDateTextView.setText(ViewModelList.get(position).getExpireDate());


        if (!ViewModelList.get(position).getImagePathUrl().equals("")) {
            LayoutUtility.LoadImageWithGlide(Context, ViewModelList.get(position).getImagePathUrl(), holder.PosterImageView);
        } else {
            holder.PosterImageView.setImageResource(R.drawable.image_default);
        }

        if (ViewModelList.get(position).isActive()) {
            holder.EditPosterButton.setVisibility(View.VISIBLE);
            holder.ExtendedPosterButton.setVisibility(View.VISIBLE);
        } else {
            holder.EditPosterButton.setVisibility(View.GONE);
            holder.ExtendedPosterButton.setVisibility(View.GONE);
        }

        holder.ExtendedPosterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.EditPosterButton.setVisibility(View.GONE);

        holder.DetailsBuyPosterButton.setOnClickListener(new View.OnClickListener() {
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


    private class PosterViewHolder extends RecyclerView.ViewHolder {

        ButtonPersianView ExtendedPosterButton;
        ButtonPersianView EditPosterButton;
        ButtonPersianView DetailsBuyPosterButton;
        TextViewPersian PosterTitleTextView;
        TextViewPersian BusinessNameTextView;
        TextViewPersian ExpireDateTextView;
        ImageView PosterImageView;

        PosterViewHolder(View v) {
            super(v);
            PosterTitleTextView = v.findViewById(R.id.PosterTitleTextView);
            ExtendedPosterButton = v.findViewById(R.id.ExtendedPosterButton);
            EditPosterButton = v.findViewById(R.id.EditPosterButton);
            BusinessNameTextView = v.findViewById(R.id.BusinessNameTextView);
            DetailsBuyPosterButton = v.findViewById(R.id.DetailsBuyPosterButton);
            PosterImageView = v.findViewById(R.id.PosterImageView);
            ExpireDateTextView = v.findViewById(R.id.ExpireDateTextView);
        }
    }


    @SuppressLint("SetTextI18n")
    private void ShowDetailsPackageBuyDialog(PurchasedPosterViewModel ViewModel) {

        final Dialog DetailsBuyPosterDialog = new Dialog(Context);
        DetailsBuyPosterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DetailsBuyPosterDialog.setContentView(R.layout.dialog_poster_buy_details);

        ButtonPersianView DialogOkButton = DetailsBuyPosterDialog.findViewById(R.id.DialogOkButton);
        TextViewPersian PosterCostTextView = DetailsBuyPosterDialog.findViewById(R.id.PosterCostTextView);
        TextViewPersian PriorityTextView = DetailsBuyPosterDialog.findViewById(R.id.PriorityTextView);
        SwitchCompat AlwaysOnTopSwitch = DetailsBuyPosterDialog.findViewById(R.id.AlwaysOnTopSwitch);
        TextViewPersian CreateDateTextView = DetailsBuyPosterDialog.findViewById(R.id.CreateDateTextView);
        TextViewPersian DimensionPosterTextView = DetailsBuyPosterDialog.findViewById(R.id.DimensionPosterTextView);
        LinearLayout PriorityLinearLayout = DetailsBuyPosterDialog.findViewById(R.id.PriorityLinearLayout);
        LinearLayout DimensionPosterLinearLayout = DetailsBuyPosterDialog.findViewById(R.id.DimensionPosterLinearLayout);

        PosterCostTextView.setText(Utility.GetIntegerNumberWithComma(ViewModel.getPosterPrice()));
        CreateDateTextView.setText(ViewModel.getCreate());
        PriorityTextView.setText(String.valueOf(ViewModel.getPriority()));
        AlwaysOnTopSwitch.setChecked(ViewModel.isTop());

        int Rows = ViewModel.getRows();
        int Cols = ViewModel.getCols();
        DimensionPosterTextView.setText(Rows + Context.getResources().getString(R.string.star) + Cols);

        AlwaysOnTopSwitch.setClickable(false);

        if (ViewModel.isTop()) {
            PriorityLinearLayout.setVisibility(View.VISIBLE);
            DimensionPosterLinearLayout.setVisibility(View.GONE);
        } else {
            PriorityLinearLayout.setVisibility(View.GONE);
            DimensionPosterLinearLayout.setVisibility(View.VISIBLE);
        }

        DialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailsBuyPosterDialog.dismiss();
            }
        });

        DetailsBuyPosterDialog.show();
    }
}
