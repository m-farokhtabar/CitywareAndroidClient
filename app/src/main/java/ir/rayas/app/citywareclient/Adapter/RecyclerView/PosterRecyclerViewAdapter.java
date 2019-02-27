package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.OnLoadMoreListener;
import ir.rayas.app.citywareclient.View.Master.UserProfileActivity;
import ir.rayas.app.citywareclient.ViewModel.Poster.PosterTypeViewModel;
import ir.rayas.app.citywareclient.ViewModel.Poster.PurchasedPosterViewModel;

/**
 * Created by Hajar on 10/11/2018.
 */

public class PosterRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private UserProfileActivity Context;
    private RecyclerView Container = null;
    private List<PurchasedPosterViewModel> ViewModelList = null;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;

    private int visibleThreshold = 1;
    private int lastVisibleItem;
    private int totalItemCount;

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public PosterRecyclerViewAdapter(UserProfileActivity Context, List<PurchasedPosterViewModel> ViewModelList, RecyclerView Container, OnLoadMoreListener mOnLoadMoreListener) {
        this.ViewModelList = ViewModelList;
        this.Context = Context;
        this.Container = Container;
        this.onLoadMoreListener = mOnLoadMoreListener;
        CreateLayout();
    }

    private void CreateLayout() {
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) Container.getLayoutManager();
        Container.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                if (lastVisibleItem < linearLayoutManager.findLastVisibleItemPosition()) {
                    if (!isLoading && totalItemCount <= (linearLayoutManager.findLastVisibleItemPosition() + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            isLoading = true;
                            onLoadMoreListener.onLoadMore();
                        }
                    }
                }
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
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

        View view = LayoutInflater.from(Context).inflate(R.layout.recycler_view_poster, parent, false);
        return new PosterViewHolder(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        PosterViewHolder holder = (PosterViewHolder) viewHolder;
        boolean IsExpire = false;

        PosterTypeViewModel posterTypeViewModel = ViewModelList.get(position).getPosterType();

        holder.PosterTitleTextView.setText(ViewModelList.get(position).getTitle());
        holder.BusinessNameTextView.setText(ViewModelList.get(position).getBusinessName());
        String Price = Utility.GetIntegerNumberWithComma((int) ViewModelList.get(position).getPrice());
        holder.PosterCostTextView.setText(Price);
        holder.AlwaysOnTopSwitch.setChecked(posterTypeViewModel.isTop());
        holder.PriorityTextView.setText(String.valueOf(posterTypeViewModel.getPriority()));
        holder.TypePosterTextView.setText(posterTypeViewModel.getName());

        LayoutUtility.LoadImageWithGlide(Context, ViewModelList.get(position).getImagePathUrl(), holder.PosterImageView);

        holder.AlwaysOnTopSwitch.setClickable(false);

        List<Long> Date = Utility.CalculateTimeDifference(ViewModelList.get(position).getCurrentDate(), ViewModelList.get(position).getExpire());

        if (Date.get(0) > 0) {
            holder.ExpireDateTextView.setText(ViewModelList.get(position).getExpire());
            IsExpire = false;
        } else {
            if (Date.get(1) > 0) {
                holder.ExpireDateTextView.setText(ViewModelList.get(position).getExpire());
                IsExpire = false;
            } else {
                if (Date.get(2) > 0) {
                    holder.ExpireDateTextView.setText(ViewModelList.get(position).getExpire());
                    IsExpire = false;
                } else {
                    holder.ExpireDateTextView.setText(Context.getResources().getString(R.string.expire));
                    IsExpire = true;
                }
            }

        }

        if (ViewModelList.get(position).isActive()) {

            holder.AlwaysOnTopSwitch.setEnabled(true);
            holder.EditPosterButton.setEnabled(true);
            holder.ExtendedPosterButton.setEnabled(true);

            holder.ExtendedPosterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            holder.EditPosterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });

            if (IsExpire) {
                holder.ExpireDateTextView.setTextColor(Context.getResources().getColor(R.color.FontRedColor));
                holder.EditPosterButton.setBackground(Context.getResources().getDrawable(R.drawable.selector_button_red_style));
                holder.ExtendedPosterButton.setBackground(Context.getResources().getDrawable(R.drawable.selector_button_red_style));
                holder.AlwaysOnTopSwitch.getThumbDrawable().setColorFilter(Context.getResources().getColor(R.color.ButtonBackgroundPressRedColor), PorterDuff.Mode.MULTIPLY);
                holder.AlwaysOnTopSwitch.getTrackDrawable().setColorFilter(Context.getResources().getColor(R.color.FontRedColor), PorterDuff.Mode.MULTIPLY);
            } else {
                holder.ExpireDateTextView.setTextColor(Context.getResources().getColor(R.color.FontBlackColor));
                holder.ExtendedPosterButton.setBackground(Context.getResources().getDrawable(R.drawable.selector_button_theme_style));
                holder.EditPosterButton.setBackground(Context.getResources().getDrawable(R.drawable.selector_button_theme_style));

                if (ViewModelList.get(position).isActive()) {
                    holder.AlwaysOnTopSwitch.getThumbDrawable().setColorFilter(Context.getResources().getColor(R.color.ThumbThemeColorDrawable), PorterDuff.Mode.MULTIPLY);
                    holder.AlwaysOnTopSwitch.getTrackDrawable().setColorFilter(Context.getResources().getColor(R.color.TrackLightThemeColorDrawable), PorterDuff.Mode.MULTIPLY);

                } else {
                    holder.AlwaysOnTopSwitch.getThumbDrawable().setColorFilter(Context.getResources().getColor(R.color.FontSemiBlackColor), PorterDuff.Mode.MULTIPLY);
                    holder.AlwaysOnTopSwitch.getTrackDrawable().setColorFilter(Context.getResources().getColor(R.color.FontSemiBlackColor), PorterDuff.Mode.MULTIPLY);
                }
            }

        } else {
            holder.AlwaysOnTopSwitch.setEnabled(false);
            holder.EditPosterButton.setEnabled(false);
            holder.ExtendedPosterButton.setEnabled(false);
            holder.ExpireDateTextView.setTextColor(Context.getResources().getColor(R.color.FontBlackColor));
        }


    }

    @Override
    public int getItemCount() {
        return ViewModelList == null ? 0 : ViewModelList.size();
    }


    private class PosterViewHolder extends RecyclerView.ViewHolder {

        public ButtonPersianView ExtendedPosterButton;
        public ButtonPersianView EditPosterButton;
        public TextViewPersian PosterTitleTextView;
        public TextViewPersian BusinessNameTextView;
        public TextViewPersian PosterCostTextView;
        public TextViewPersian ExpireDateTextView;
        public TextViewPersian TypePosterTextView;
        public TextViewPersian PriorityTextView;
        public ImageView PosterImageView;
        public SwitchCompat AlwaysOnTopSwitch;

        public PosterViewHolder(View v) {
            super(v);
            PosterTitleTextView = v.findViewById(R.id.PosterTitleTextView);
            ExtendedPosterButton = v.findViewById(R.id.ExtendedPosterButton);
            EditPosterButton = v.findViewById(R.id.EditPosterButton);
            BusinessNameTextView = v.findViewById(R.id.BusinessNameTextView);
            PosterCostTextView = v.findViewById(R.id.PosterCostTextView);
            PosterImageView = v.findViewById(R.id.PosterImageView);
            ExpireDateTextView = v.findViewById(R.id.ExpireDateTextView);
            AlwaysOnTopSwitch = v.findViewById(R.id.AlwaysOnTopSwitch);
            TypePosterTextView = v.findViewById(R.id.TypePosterTextView);
            PriorityTextView = v.findViewById(R.id.PriorityTextView);

        }
    }
}
