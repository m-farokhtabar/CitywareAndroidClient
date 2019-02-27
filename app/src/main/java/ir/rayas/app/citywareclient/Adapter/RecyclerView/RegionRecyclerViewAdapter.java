package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.ViewModel.RegionAdapterViewModel;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.ViewModel.Definition.RegionViewModel;

/**
 * Created by Programmer on 3/8/2018.
 */

public class RegionRecyclerViewAdapter extends RecyclerView.Adapter<RegionRecyclerViewAdapter.ViewHolder> {

    private List<RegionAdapterViewModel> ViewModelList = null;
    private RegionViewModel RegionRootNode = null;
    private int LastSelectedPosition = -1;
    private int SelectedRegionId = -1;
    private String SelectedRegionName = "";
    private RadioButton LastSelectedRadioButton = null;
    private RecyclerView Container = null;
    private List<Integer> SubItemList = null;

    public int getSelectedRegionId() {
        return SelectedRegionId;
    }

    public String getSelectedRegionName() {
        return SelectedRegionName;
    }

    public RegionRecyclerViewAdapter(RegionViewModel ViewModel, RecyclerView Container) {
        this.Container = Container;
        this.RegionRootNode = ViewModel;
        this.SubItemList = new ArrayList<>();
        //1 به معنی انتخاب ریشه است در پایگاه داده نیز باید ریشه 1 باشد
        ConvertRegionViewModelToRegionAdapterViewModel(RegionRootNode, 1);
        //برای بازشگت به لیست قبل
        SubItemList.add(1);
    }

    private int ConvertRegionViewModelToRegionAdapterViewModel(RegionViewModel Node, int ParentId) {
        if (Node.getId() == ParentId) {
            if (Node.getChildren() != null && Node.getChildren().size() > 0) {
                ViewModelList = new ArrayList<>();
                //در صورتی که ریشه نیست باید پیغام برگشت را داشته باشد
                if (ParentId != 1) {
                    RegionAdapterViewModel AdapterItem = new RegionAdapterViewModel();
                    AdapterItem.Id = -1;
                    AdapterItem.Name = Container.getContext().getResources().getString(R.string.back_to_parent);
                    AdapterItem.IsSelected = false;
                    ViewModelList.add(AdapterItem);
                }
                for (RegionViewModel Item : Node.getChildren()) {
                    RegionAdapterViewModel AdapterItem = new RegionAdapterViewModel();
                    AdapterItem.Id = Item.getId();
                    AdapterItem.Name = Item.getName();
                    AdapterItem.IsSelected = false;
                    ViewModelList.add(AdapterItem);
                }
                //پیدا شده اما فرزند هم دارد
                return 1;
            }
            //پیدا شده اما فرزندی ندارد
            else
                return 0;
        } else {
            for (RegionViewModel Item : Node.getChildren()) {
                int Output = ConvertRegionViewModelToRegionAdapterViewModel(Item, ParentId);
                if (Output != -1)
                    return Output;
            }
            //پیدا نشده
            return -1;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextViewPersian RegionTitleTextView;
        public RadioButton RegionSelectedRadioButton;
        public RelativeLayout RegionContainerRelativeLayout;

        public ViewHolder(View v) {
            super(v);
            RegionTitleTextView = v.findViewById(R.id.RegionTitleTextView);

            RegionSelectedRadioButton = v.findViewById(R.id.RegionSelectedRadioButton);
            RegionSelectedRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (LastSelectedPosition > -1)
                        ViewModelList.get(LastSelectedPosition).IsSelected = false;
                    if (LastSelectedRadioButton != null)
                        LastSelectedRadioButton.setChecked(false);

                    if (LastSelectedPosition != getAdapterPosition()) {
                        LastSelectedPosition = getAdapterPosition();
                        ViewModelList.get(LastSelectedPosition).IsSelected = true;
                        SelectedRegionId = ViewModelList.get(LastSelectedPosition).Id;
                        SelectedRegionName = ViewModelList.get(LastSelectedPosition).Name;
                        LastSelectedRadioButton = (RadioButton) view;
                        LastSelectedRadioButton.setChecked(true);
                    } else {
                        LastSelectedPosition = -1;
                        SelectedRegionId = -1;
                        SelectedRegionName  = "";
                        LastSelectedRadioButton = null;
                    }
                }
            });

            RegionContainerRelativeLayout = v.findViewById(R.id.RegionContainerRelativeLayout);
            RegionContainerRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ViewModelList != null && getAdapterPosition() >= 0 && getAdapterPosition() < ViewModelList.size() && ViewModelList.get(getAdapterPosition()) != null) {
                        int SelectedId = ViewModelList.get(getAdapterPosition()).Id;
                        //بازگشت به صفحه قبل
                        if (SelectedId == -1) {
                            SubItemList.remove(SubItemList.size() - 1);
                            SelectedId = SubItemList.get(SubItemList.size() - 1);
                        }
                        //رفتن به صفحه داخلی
                        else {
                            SubItemList.add(SelectedId);
                        }
                        if (ConvertRegionViewModelToRegionAdapterViewModel(RegionRootNode, SelectedId) == 1) {
                            LastSelectedPosition = -1;
                            SelectedRegionId = -1;
                            SelectedRegionName = "";
                            LastSelectedRadioButton = null;
                            notifyDataSetChanged();
                            Container.invalidate();
                        }
                    }
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_region, parent, false);
        ViewHolder CurrentViewHolder = new ViewHolder(CurrentView);
        return CurrentViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.RegionTitleTextView.setText(ViewModelList.get(position).Name);
        if (ViewModelList.get(position).Id == -1) {
            int Color = LayoutUtility.GetColorFromResource(Container.getContext(), R.color.FontBlackColor);
            holder.RegionTitleTextView.setTextColor(Color);
            holder.RegionSelectedRadioButton.setVisibility(View.INVISIBLE);
        } else {
            int Color = LayoutUtility.GetColorFromResource(Container.getContext(), R.color.FontSemiBlackColor);
            holder.RegionTitleTextView.setTextColor(Color);
            holder.RegionSelectedRadioButton.setChecked(ViewModelList.get(position).IsSelected);
            holder.RegionSelectedRadioButton.setVisibility(View.VISIBLE);
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
