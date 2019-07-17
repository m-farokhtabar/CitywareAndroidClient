package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.ViewModel.BasketAddressAdapterViewModel;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Share.BasketActivity;
import ir.rayas.app.citywareclient.ViewModel.User.UserAddressViewModel;

public class BasketUserAddressRecyclerViewAdapter extends RecyclerView.Adapter<BasketUserAddressRecyclerViewAdapter.ViewHolder> {

    private List<UserAddressViewModel> ViewModelList = null;
    private List<BasketAddressAdapterViewModel> ViewModel = null;
    private RecyclerView Container = null;
    private BasketActivity Context;

    private int LastSelectedPosition = -1;
    private RadioButton LastSelectedRadioButton = null;
    private String SelectAddress = "";
    private int UserAddressId = 0;

    private boolean IsFirst = true;

    public String getSelectAddress() {
        return SelectAddress;
    }

    public int getUserAddressId() {
        return UserAddressId;
    }

    public int getLastSelectedPosition() {
        return LastSelectedPosition;
    }

    private double Latitude;
    private double Longitude;

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

        IsFirst = true;

        ConvertUserAddressViewModelToBasketAddressAdapterViewModel(ViewModelList);
    }


    private void ConvertUserAddressViewModelToBasketAddressAdapterViewModel(List<UserAddressViewModel> ViewModels) {

        ViewModel = new ArrayList<>();

        for (int i = 0; i < ViewModels.size(); i++) {
            BasketAddressAdapterViewModel basketAddressAdapterViewModel = new BasketAddressAdapterViewModel();

            basketAddressAdapterViewModel.setAddress(ViewModels.get(i).getCurrentAddress());
            basketAddressAdapterViewModel.setPostalCode(ViewModels.get(i).getPostalCode());
            basketAddressAdapterViewModel.setUserAddressId(ViewModels.get(i).getId());

            ViewModel.add(basketAddressAdapterViewModel);
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextViewPersian UserAddressTextView;
        TextViewPersian PostalCodeTextView;
        RadioButton AddressSelectedRadioButton;

        public ViewHolder(View v) {
            super(v);
            UserAddressTextView = v.findViewById(R.id.UserAddressTextView);
            PostalCodeTextView = v.findViewById(R.id.PostalCodeTextView);
            AddressSelectedRadioButton = v.findViewById(R.id.AddressSelectedRadioButton);



            AddressSelectedRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (LastSelectedPosition > -1)
                        ViewModel.get(LastSelectedPosition).IsSelected = false;

                    if (LastSelectedRadioButton != null)
                        LastSelectedRadioButton.setChecked(false);

                    if (LastSelectedPosition != getAdapterPosition()) {

                        AddressSelectedRadioButton.setChecked(false);

                        LastSelectedPosition = getAdapterPosition();
                        ViewModel.get(LastSelectedPosition).IsSelected = true;

                        if (ViewModel.get(LastSelectedPosition).getPostalCode().equals("")) {
                            SelectAddress = ViewModel.get(LastSelectedPosition).getAddress();
                        } else {
                            String PostalCode = Context.getResources().getString(R.string.postal_code) + " " + ViewModel.get(LastSelectedPosition).getPostalCode();
                            SelectAddress = ViewModel.get(LastSelectedPosition).getAddress() + " - " + PostalCode;
                        }

                        UserAddressId = ViewModel.get(LastSelectedPosition).getUserAddressId();
                        LastSelectedRadioButton = (RadioButton) view;
                        LastSelectedRadioButton.setChecked(true);
                    } else {
                        LastSelectedPosition = -1;
                        SelectAddress = "";
                        UserAddressId = 0;
                        LastSelectedRadioButton = null;
                    }
                }

            });

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_basket_user_address, parent, false);
        return new ViewHolder(CurrentView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.UserAddressTextView.setText(ViewModel.get(position).getAddress());
        holder.PostalCodeTextView.setText(ViewModel.get(position).getPostalCode());
        holder.AddressSelectedRadioButton.setChecked(ViewModel.get(position).getSelected());

//        if (ViewModel.get(position).getUserAddressId() == Context.basketSummeryViewModel.getUserAddressId())
//            holder.AddressSelectedRadioButton.setChecked(true);


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
