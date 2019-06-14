package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.ViewModel.IsSelectBusinessListAdapterViewModel;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.BusinessCategoryRepository;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Share.UserBusinessListActivity;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessViewModel;


public class UserBusinessListRecyclerViewAdapter extends RecyclerView.Adapter<UserBusinessListRecyclerViewAdapter.ViewHolder> {

    private List<BusinessViewModel> ViewModelList = null;
    private UserBusinessListActivity Context;
    private RecyclerView Container = null;
    private List<IsSelectBusinessListAdapterViewModel> ViewModel = new ArrayList<>();

    private BusinessCategoryRepository businessCategoryRepository = new BusinessCategoryRepository();

    private RadioButton LastSelectedRadioButton = null;
    private int LastSelectedPosition = -1;

    private String SelectBusinessName = "";
    private int SelectBusinessId;

    public UserBusinessListRecyclerViewAdapter(UserBusinessListActivity context, List<BusinessViewModel> ViewModel, RecyclerView Container) {
        this.Context = context;
        this.ViewModelList = ViewModel;
        this.Container = Container;

        ConvertUserBusinessViewModelToBasketAddressAdapterViewModel(ViewModelList);
    }

    private void ConvertUserBusinessViewModelToBasketAddressAdapterViewModel(List<BusinessViewModel> ViewModels) {

        ViewModel = new ArrayList<>();

        for (int i = 0; i < ViewModels.size(); i++) {
            IsSelectBusinessListAdapterViewModel isSelectBusinessListAdapterViewModel = new IsSelectBusinessListAdapterViewModel();
            isSelectBusinessListAdapterViewModel.setBusinessName(ViewModels.get(i).getTitle());
            isSelectBusinessListAdapterViewModel.setBusinessId(ViewModels.get(i).getId());
            isSelectBusinessListAdapterViewModel.setBusinessCategoryId(ViewModels.get(i).getBusinessCategoryId());

            ViewModel.add(isSelectBusinessListAdapterViewModel);
        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextViewPersian BusinessTitleTextView;
        TextViewPersian BusinessCategoryTextView;
        RelativeLayout BusinessContainerRelativeLayout;
        RadioButton RegionSelectedRadioButton;


        ViewHolder(View v) {
            super(v);

            BusinessTitleTextView = v.findViewById(R.id.BusinessTitleTextView);
            BusinessCategoryTextView = v.findViewById(R.id.BusinessCategoryTextView);
            BusinessContainerRelativeLayout = v.findViewById(R.id.BusinessContainerRelativeLayout);
            RegionSelectedRadioButton = v.findViewById(R.id.RegionSelectedRadioButton);


            RegionSelectedRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (LastSelectedPosition > -1)
                        ViewModel.get(LastSelectedPosition).IsSelected = false;

                    if (LastSelectedRadioButton != null)
                        LastSelectedRadioButton.setChecked(false);

                    if (LastSelectedPosition != getAdapterPosition()) {
                        LastSelectedPosition = getAdapterPosition();
                        ViewModel.get(LastSelectedPosition).IsSelected = true;
                        SelectBusinessName = ViewModel.get(LastSelectedPosition).getBusinessName();
                        SelectBusinessId = ViewModel.get(LastSelectedPosition).getBusinessId();
                        LastSelectedRadioButton = (RadioButton) view;
                        LastSelectedRadioButton.setChecked(true);
                    } else {
                        LastSelectedPosition = -1;
                        SelectBusinessName = "";
                        SelectBusinessId = 0;
                        LastSelectedRadioButton = null;
                    }

                    Context.SetBusinessNameToButton(SelectBusinessName,SelectBusinessId);
                }

            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_my_business_list, parent, false);
        return new ViewHolder(CurrentView);
    }


    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.BusinessTitleTextView.setText(ViewModel.get(position).getBusinessName());
        String CategoryName = businessCategoryRepository.GetFullName(ViewModel.get(position).getBusinessCategoryId());
        holder.BusinessCategoryTextView.setText(CategoryName);

        holder.RegionSelectedRadioButton.setChecked(ViewModel.get(position).getSelected());

    }



    /**
     * اضافه مودن لیست جدید
     *
     * @param ViewModel
     */
    public void AddViewModelList(List<BusinessViewModel> ViewModel) {
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
    public void SetViewModelList(List<BusinessViewModel> ViewModel) {
        ViewModelList = new ArrayList<>();
        ViewModelList.addAll(ViewModel);
        notifyDataSetChanged();
        Container.invalidate();
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