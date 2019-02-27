package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.ViewModel.BasketAddressAdapterViewModel;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Share.BasketActivity;
import ir.rayas.app.citywareclient.ViewModel.User.UserAddressViewModel;

/**
 * Created by Hajar on 2/15/2019.
 */

public class BasketUserAddressRecyclerViewAdapter extends RecyclerView.Adapter<BasketUserAddressRecyclerViewAdapter.ViewHolder> {

    private List<UserAddressViewModel> ViewModelList = null;
    private List<BasketAddressAdapterViewModel> ViewModel = null;
    private RecyclerView Container = null;
    private BasketActivity Context;

    private String SelectAddress = "";

    public String getSelectAddress() {
        return SelectAddress;
    }
    private  double Latitude;
    private  double Longitude;

    public double getLatitude() {
        return Latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public BasketUserAddressRecyclerViewAdapter(BasketActivity context, List<UserAddressViewModel> ViewModel, RecyclerView Container) {
        this.Context = context;
        this.Container = Container;
        this.ViewModelList = ViewModel;

        this.ViewModel = ConvertUserAddressViewModelToBasketAddressAdapterViewModel(ViewModelList);
    }


    private List<BasketAddressAdapterViewModel> ConvertUserAddressViewModelToBasketAddressAdapterViewModel(List<UserAddressViewModel> ViewModel) {

        List<BasketAddressAdapterViewModel> basketAddressAdapterViewModelList = new ArrayList<>();

        for (int i = 0; i < ViewModel.size(); i++) {
            BasketAddressAdapterViewModel basketAddressAdapterViewModel = new BasketAddressAdapterViewModel();
            if (ViewModelList.get(i).getPostalCode().equals("")) {
                basketAddressAdapterViewModel.setAddress(ViewModel.get(i).getCurrentAddress());
            } else {
                String PostalCode = Context.getResources().getString(R.string.postal_code) + " " + ViewModel.get(i).getPostalCode();
                basketAddressAdapterViewModel.setAddress(ViewModel.get(i).getCurrentAddress() + " - " + PostalCode);
            }

            if (i == ViewModel.size() - 1) {
                basketAddressAdapterViewModel.setSelected(true);
                SelectAddress = basketAddressAdapterViewModel.getAddress();
                Latitude =  ViewModel.get(i).getLatitude();
                Longitude =  ViewModel.get(i).getLongitude();
            } else {
                basketAddressAdapterViewModel.setSelected(false);
            }
            basketAddressAdapterViewModelList.add(basketAddressAdapterViewModel);
        }
        return basketAddressAdapterViewModelList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextViewPersian UserAddressTextView;
        public RadioButton AddressSelectedRadioButton;
        public LinearLayout UserAddressContainerLinearLayout;


        public ViewHolder(View v) {
            super(v);
            UserAddressTextView = v.findViewById(R.id.UserAddressTextView);
            AddressSelectedRadioButton = v.findViewById(R.id.AddressSelectedRadioButton);
            UserAddressContainerLinearLayout = v.findViewById(R.id.UserAddressContainerLinearLayout);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_basket_user_address, parent, false);
        ViewHolder CurrentViewHolder = new ViewHolder(CurrentView);
        return CurrentViewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.UserAddressTextView.setText(ViewModel.get(position).getAddress());
        holder.AddressSelectedRadioButton.setChecked(ViewModel.get(position).getSelected());

        holder.UserAddressContainerLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.AddressSelectedRadioButton.setChecked(true);
                SelectAddress = ViewModel.get(position).getAddress();
                Latitude =  ViewModelList.get(position).getLatitude();
                Longitude =  ViewModelList.get(position).getLongitude();
            }
        });
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

//    /**
//     * ویرایش اطلاعات یک آدرس در لیست
//     * @param ViewModel
//     */
//    public void SetViewModel(UserAddressViewModel ViewModel)
//    {
//        if (ViewModel!=null && ViewModelList!=null && ViewModelList.size()>0) {
//            for (UserAddressViewModel Item : ViewModelList) {
//                if (Item.getId() == ViewModel.getId()) {
//                    Item.setCurrentAddress(ViewModel.getCurrentAddress());
//                    Item.setPostalCode(ViewModel.getPostalCode());
//                    Item.setLatitude(ViewModel.getLatitude());
//                    Item.setLongitude(ViewModel.getLongitude());
//                }
//            }
//            notifyDataSetChanged();
//            Container.invalidate();
//        }
//    }

    /**
     * اضافه مودن یک آدرس جدید به لیست
     *
     * @param ViewModel
     */
    public void AddViewModel(UserAddressViewModel ViewModel) {
        if (ViewModel != null) {
            if (ViewModelList == null)
                ViewModelList = new ArrayList<>();
            ViewModelList.add(ViewModel);
            notifyDataSetChanged();
            Container.invalidate();
        }
    }


}
