package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.User.UserAddressService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Share.MapActivity;
import ir.rayas.app.citywareclient.View.UserProfileChildren.UserAddressSetActivity;
import ir.rayas.app.citywareclient.View.Master.UserProfileActivity;
import ir.rayas.app.citywareclient.ViewModel.User.UserAddressViewModel;

/**
 * Created by Hajar on 7/18/2018.
 */

public class UserAddressRecyclerViewAdapter extends RecyclerView.Adapter<UserAddressRecyclerViewAdapter.ViewHolder> implements IResponseService {

    private List<UserAddressViewModel> ViewModelList = null;
    private RecyclerView Container = null;
    private UserProfileActivity Context;
    private int Position;

    public UserAddressRecyclerViewAdapter(UserProfileActivity context, List<UserAddressViewModel> ViewModel, RecyclerView Container) {
        this.Context = context;
        this.Container = Container;
        this.ViewModelList = ViewModel;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextViewPersian UserAddressTextView;
        public TextViewPersian UserPostalCodeTextView;
        public TextViewPersian UserEditAddressIconTextView;
        public TextViewPersian UserCreateDateAddressTextView;
        public TextViewPersian UserPostalCodeTitleTextView;
        public TextViewPersian UserEditAddressTextView;
        public TextViewPersian ShowMapAddressTextView;
        public TextViewPersian ShowMapAddressIconTextView;
        public RelativeLayout UserAddressContainerRelativeLayout;
        public ButtonPersianView UserDeleteAddressButtonPersianView;
        public LinearLayout EditAddressLinearLayout;
        public LinearLayout ShowMapLinearLayoutUserAddress;

        public ViewHolder(View v) {
            super(v);
            UserAddressTextView = v.findViewById(R.id.UserAddressTextView);
            UserPostalCodeTextView = v.findViewById(R.id.UserPostalCodeTextView);
            UserEditAddressIconTextView = v.findViewById(R.id.UserEditAddressIconTextView);
            UserCreateDateAddressTextView = v.findViewById(R.id.UserCreateDateAddressTextView);
            UserPostalCodeTitleTextView = v.findViewById(R.id.UserPostalCodeTitleTextView);
            UserEditAddressTextView = v.findViewById(R.id.UserEditAddressTextView);
            UserAddressContainerRelativeLayout = v.findViewById(R.id.UserAddressContainerRelativeLayout);
            UserDeleteAddressButtonPersianView = v.findViewById(R.id.UserDeleteAddressButtonPersianView);
            EditAddressLinearLayout = v.findViewById(R.id.EditAddressLinearLayout);
            ShowMapAddressTextView = v.findViewById(R.id.ShowMapAddressTextView);
            ShowMapAddressIconTextView = v.findViewById(R.id.ShowMapAddressIconTextView);
            ShowMapLinearLayoutUserAddress = v.findViewById(R.id.ShowMapLinearLayoutUserAddress);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_user_address, parent, false);
        ViewHolder CurrentViewHolder = new ViewHolder(CurrentView);
        return CurrentViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.UserAddressTextView.setText(ViewModelList.get(position).getCurrentAddress());
        if (ViewModelList.get(position).getPostalCode().equals("")) {
            holder.UserPostalCodeTextView.setText("-");
        } else {
            holder.UserPostalCodeTextView.setText(ViewModelList.get(position).getPostalCode());
        }
        holder.UserCreateDateAddressTextView.setText(ViewModelList.get(position).getCreate());

        holder.UserEditAddressIconTextView.setTypeface(Font.MasterIcon);
        holder.UserEditAddressIconTextView.setText("\uf044");

        holder.ShowMapAddressIconTextView.setTypeface(Font.MasterIcon);
        holder.ShowMapAddressIconTextView.setText("\uf041");

        holder.UserDeleteAddressButtonPersianView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Position = position;
                Context.setRetryType(1);
                Context.ShowLoadingProgressBar();
                UserAddressService userAddressService = new UserAddressService(UserAddressRecyclerViewAdapter.this);
                userAddressService.Delete(ViewModelList.get(position).getId());
            }
        });

        holder.EditAddressLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent EditAddressIntent = Context.NewIntent(UserAddressSetActivity.class);
                EditAddressIntent.putExtra("SetAddress", "Edit");
                EditAddressIntent.putExtra("AddressId", ViewModelList.get(position).getId());
                //2 Edit - you can find Add in UserAddressFragment
                Context.startActivity(EditAddressIntent);

            }
        });

        if (ViewModelList.get(position).getLatitude() > 0 && ViewModelList.get(position).getLongitude() > 0) {
            holder.ShowMapLinearLayoutUserAddress.setVisibility(View.VISIBLE);
        } else {
            holder.ShowMapLinearLayoutUserAddress.setVisibility(View.GONE);
        }

        holder.ShowMapLinearLayoutUserAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MapIntent = Context.NewIntent(MapActivity.class);
                MapIntent.putExtra("Latitude", ViewModelList.get(position).getLatitude());
                MapIntent.putExtra("Longitude", ViewModelList.get(position).getLongitude());
                MapIntent.putExtra("Going", 2);
                Context.startActivity(MapIntent);
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

    /**
     * ویرایش اطلاعات یک آدرس در لیست
     * @param ViewModel
     */
    public void SetViewModel(UserAddressViewModel ViewModel)
    {
        if (ViewModel!=null && ViewModelList!=null && ViewModelList.size()>0) {
            for (UserAddressViewModel Item : ViewModelList) {
                if (Item.getId() == ViewModel.getId()) {
                    Item.setCurrentAddress(ViewModel.getCurrentAddress());
                    Item.setPostalCode(ViewModel.getPostalCode());
                    Item.setLatitude(ViewModel.getLatitude());
                    Item.setLongitude(ViewModel.getLongitude());
                }
            }
            notifyDataSetChanged();
            Container.invalidate();
        }
    }

    /**
     * اضافه مودن یک آدرس جدید به لیست
     * @param ViewModel
     */
    public void AddViewModel(UserAddressViewModel ViewModel)
    {
        if (ViewModel!=null) {
            if (ViewModelList == null)
                ViewModelList = new ArrayList<>();
            ViewModelList.add(ViewModel);
            notifyDataSetChanged();
            Container.invalidate();
        }
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

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.UserDeleteAddress) {
                Feedback<UserAddressViewModel> FeedBack = (Feedback<UserAddressViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.DeletedSuccessful.getId()) {
                    ViewModelList.remove(Position);
                    notifyDataSetChanged();
                    Container.invalidate();
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            }
        } catch (Exception e) {
            Context.ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
    }
}
