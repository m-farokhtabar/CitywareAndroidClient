package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Enum.PriorityType;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Fragment.Poster.PosterTypeDetailsFragment;
import ir.rayas.app.citywareclient.View.UserProfileChildren.PosterTypeActivity;
import ir.rayas.app.citywareclient.ViewModel.Poster.PosterTypeViewModel;


public class PosterTypeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private PosterTypeActivity Context;
    private RecyclerView Container = null;
    private List<PosterTypeViewModel> ViewModelList = null;


    public PosterTypeRecyclerViewAdapter(PosterTypeActivity Context, List<PosterTypeViewModel> BookmarkList, RecyclerView Container) {
        this.ViewModelList = BookmarkList;
        this.Context = Context;
        this.Container = Container;

    }


    /**
     * اضافه مودن لیست جدید
     *
     * @param ViewModel
     */
    public void AddViewModelList(List<PosterTypeViewModel> ViewModel) {
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
    public void SetViewModelList(List<PosterTypeViewModel> ViewModel) {
        ViewModelList = new ArrayList<>();
        ViewModelList.addAll(ViewModel);
        notifyDataSetChanged();
        Container.invalidate();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(Context).inflate(R.layout.recycler_view_poster_type, parent, false);
        return new PosterTypeViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        PosterTypeViewHolder viewHolder = (PosterTypeViewHolder) holder;

        viewHolder.PosterTypeTitleTextView.setText(ViewModelList.get(position).getName());
        viewHolder.PosterTypeCostTextView.setText(Utility.GetIntegerNumberWithComma(ViewModelList.get(position).getPrice()));
        viewHolder.PosterTypeAlwaysOnTopSwitch.setChecked(ViewModelList.get(position).isTop());

        String Priority = "";

        if (ViewModelList.get(position).getPriority() == PriorityType.Normal.getPriorityType())
            Priority = PriorityType.Normal.getValuePriorityType();
        else if (ViewModelList.get(position).getPriority() == PriorityType.Much.getPriorityType())
            Priority = PriorityType.Much.getValuePriorityType();
        else if (ViewModelList.get(position).getPriority() == PriorityType.VeryMuch.getPriorityType())
            Priority = PriorityType.VeryMuch.getValuePriorityType();

        viewHolder.PosterTypePriorityTextView.setText(Priority);


        int Rows = ViewModelList.get(position).getRows();
        int Cols = ViewModelList.get(position).getCols();
        viewHolder.DimensionPosterTypeTextView.setText(Rows + Context.getResources().getString(R.string.star) + Cols);

        viewHolder.PosterTypeAlwaysOnTopSwitch.setClickable(false);

        if (ViewModelList.get(position).isTop()) {
            viewHolder.PosterTypePriorityLinearLayout.setVisibility(View.VISIBLE);
            viewHolder.DimensionPosterTypeLinearLayout.setVisibility(View.GONE);
        } else {
            viewHolder.PosterTypePriorityLinearLayout.setVisibility(View.GONE);
            viewHolder.DimensionPosterTypeLinearLayout.setVisibility(View.VISIBLE);
        }

        viewHolder.SelectPosterTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PosterTypeDetailsFragment posterTypeDetailsFragment = new PosterTypeDetailsFragment();

                Bundle bundle = new Bundle();
                bundle.putInt("PosterTypeId", ViewModelList.get(position).getId());
                posterTypeDetailsFragment.setArguments(bundle);

                FragmentTransaction BasketListTransaction = Context. getSupportFragmentManager().beginTransaction();
                BasketListTransaction.replace(R.id.PosterFrameLayoutPosterTypeActivity,posterTypeDetailsFragment);
                BasketListTransaction.addToBackStack(null);
                BasketListTransaction.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return ViewModelList == null ? 0 : ViewModelList.size();
    }


    private class PosterTypeViewHolder extends RecyclerView.ViewHolder {

        TextViewPersian PosterTypeTitleTextView;
        TextViewPersian PosterTypeCostTextView;
        TextViewPersian PosterTypePriorityTextView;
        TextViewPersian DimensionPosterTypeTextView;
        LinearLayout DimensionPosterTypeLinearLayout;
        LinearLayout PosterTypePriorityLinearLayout;
        SwitchCompat PosterTypeAlwaysOnTopSwitch;
        RelativeLayout PosterTypeRelativeLayout;
        ButtonPersianView SelectPosterTypeButton;


        PosterTypeViewHolder(View v) {
            super(v);
            PosterTypeTitleTextView = v.findViewById(R.id.PosterTypeTitleTextView);
            PosterTypeCostTextView = v.findViewById(R.id.PosterTypeCostTextView);
            DimensionPosterTypeTextView = v.findViewById(R.id.DimensionPosterTypeTextView);
            PosterTypeAlwaysOnTopSwitch = v.findViewById(R.id.PosterTypeAlwaysOnTopSwitch);
            PosterTypePriorityTextView = v.findViewById(R.id.PosterTypePriorityTextView);
            PosterTypePriorityLinearLayout = v.findViewById(R.id.PosterTypePriorityLinearLayout);
            DimensionPosterTypeLinearLayout = v.findViewById(R.id.DimensionPosterTypeLinearLayout);
            PosterTypeRelativeLayout = v.findViewById(R.id.PosterTypeRelativeLayout);
            SelectPosterTypeButton = v.findViewById(R.id.SelectPosterTypeButton);

        }
    }


}
