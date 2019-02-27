package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.ViewModel.BusinessCategoryAdapterViewModel;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.ViewModel.Definition.BusinessCategoryViewModel;

/**
 * Created by Hajar on 8/16/2018.
 */

public class BusinessCategoryRecyclerViewAdapter extends RecyclerView.Adapter<BusinessCategoryRecyclerViewAdapter.ViewHolder> {


    private List<BusinessCategoryAdapterViewModel> ViewModelList = null;
    private BusinessCategoryViewModel CategoryRootNode = null;
    private int LastSelectedPosition = -1;
    private int SelectedBusinessCategoryId = -1;
    private String SelectedBusinessCategoryName = "";
    private RadioButton LastSelectedRadioButton = null;
    private RecyclerView Container = null;
    private List<Integer> SubItemList = null;

    public int getSelectedBusinessCategoryId() {
        return SelectedBusinessCategoryId;
    }

    public String getSelectedBusinessCategoryName() {
        return SelectedBusinessCategoryName;
    }

    public BusinessCategoryRecyclerViewAdapter(BusinessCategoryViewModel ViewModel, RecyclerView Container) {
        this.Container = Container;
        this.CategoryRootNode = ViewModel;
        this.SubItemList = new ArrayList<>();
        //1 به معنی انتخاب ریشه است در پایگاه داده نیز باید ریشه 1 باشد
        ConvertBusinessCategoryViewModelToBusinessCategoryAdapterViewModel(CategoryRootNode, 1);
        //برای بازشگت به لیست قبل
        SubItemList.add(1);
    }



    private int ConvertBusinessCategoryViewModelToBusinessCategoryAdapterViewModel(BusinessCategoryViewModel Node, int ParentId) {
        if (Node.getId() == ParentId) {

            if (Node.getChildren() != null && Node.getChildren().size() > 0) {
                ViewModelList = new ArrayList<>();
                //در صورتی که ریشه نیست باید پیغام برگشت را داشته باشد
                if (ParentId != 1) {
                    BusinessCategoryAdapterViewModel AdapterItem = new BusinessCategoryAdapterViewModel();
                    AdapterItem.Id = -1;
                    AdapterItem.Name = Container.getContext().getResources().getString(R.string.back_to_parent);
                    AdapterItem.IsSelected = false;
                    ViewModelList.add(AdapterItem);
                }
                for (BusinessCategoryViewModel Item : Node.getChildren()) {
                    BusinessCategoryAdapterViewModel AdapterItem = new BusinessCategoryAdapterViewModel();
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
            for (BusinessCategoryViewModel Item : Node.getChildren()) {
                int Output = ConvertBusinessCategoryViewModelToBusinessCategoryAdapterViewModel(Item, ParentId);
                if (Output != -1)
                    return Output;
            }
            //پیدا نشده
            return -1;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextViewPersian BusinessCategoryVTitleTextView;
        public RadioButton BusinessCategoryVSelectedRadioButton;
        public RelativeLayout BusinessCategoryVContainerRelativeLayout;

        public ViewHolder(View v) {
            super(v);
            BusinessCategoryVTitleTextView = v.findViewById(R.id.RegionTitleTextView);

            BusinessCategoryVSelectedRadioButton = v.findViewById(R.id.RegionSelectedRadioButton);
            BusinessCategoryVSelectedRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (LastSelectedPosition > -1)
                        ViewModelList.get(LastSelectedPosition).IsSelected = false;
                    if (LastSelectedRadioButton != null)
                        LastSelectedRadioButton.setChecked(false);

                    if (LastSelectedPosition != getAdapterPosition()) {
                        LastSelectedPosition = getAdapterPosition();
                        ViewModelList.get(LastSelectedPosition).IsSelected = true;
                        SelectedBusinessCategoryId = ViewModelList.get(LastSelectedPosition).Id;
                        SelectedBusinessCategoryName = ViewModelList.get(LastSelectedPosition).Name;
                        LastSelectedRadioButton = (RadioButton) view;
                        LastSelectedRadioButton.setChecked(true);
                    } else {
                        LastSelectedPosition = -1;
                        SelectedBusinessCategoryId = -1;
                        SelectedBusinessCategoryName = "";
                        LastSelectedRadioButton = null;
                    }
                }
            });

            BusinessCategoryVContainerRelativeLayout = v.findViewById(R.id.RegionContainerRelativeLayout);
            BusinessCategoryVContainerRelativeLayout.setOnClickListener(new View.OnClickListener() {
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
                        if (ConvertBusinessCategoryViewModelToBusinessCategoryAdapterViewModel(CategoryRootNode, SelectedId) == 1) {
                            LastSelectedPosition = -1;
                            SelectedBusinessCategoryId = -1;
                            SelectedBusinessCategoryName ="";
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
        holder.BusinessCategoryVTitleTextView.setText(ViewModelList.get(position).Name);
        if (ViewModelList.get(position).Id == -1) {
            int Color = LayoutUtility.GetColorFromResource(Container.getContext(), R.color.FontBlackColor);
            holder.BusinessCategoryVTitleTextView.setTextColor(Color);
            holder.BusinessCategoryVSelectedRadioButton.setVisibility(View.INVISIBLE);
        } else {
            int Color = LayoutUtility.GetColorFromResource(Container.getContext(), R.color.FontSemiBlackColor);
            holder.BusinessCategoryVTitleTextView.setTextColor(Color);
            holder.BusinessCategoryVSelectedRadioButton.setChecked(ViewModelList.get(position).IsSelected);
            holder.BusinessCategoryVSelectedRadioButton.setVisibility(View.VISIBLE);
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
