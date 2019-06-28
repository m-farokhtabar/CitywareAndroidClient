package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Master.MainActivity;
import ir.rayas.app.citywareclient.ViewModel.User.UserAddressViewModel;


public class UserAddressDialogRecyclerViewAdapter extends RecyclerView.Adapter<UserAddressDialogRecyclerViewAdapter.ViewHolder> {

    private List<UserAddressViewModel> ViewModelList = null;
    private MainActivity Context;

    public UserAddressDialogRecyclerViewAdapter(MainActivity MyContext,List<UserAddressViewModel> ViewModel) {
        this.ViewModelList = ViewModel;
        this.Context = MyContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

         TextViewPersian UserAddressDialogTextView;

        public ViewHolder(View v) {
            super(v);
            UserAddressDialogTextView = v.findViewById(R.id.UserAddressDialogTextView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_dialog_user_address, parent, false);
        return new ViewHolder(CurrentView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.UserAddressDialogTextView.setText(ViewModelList.get(position).getCurrentAddress());

        holder.UserAddressDialogTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Context.GetLatLngAddress(ViewModelList.get(position).getLatitude(),ViewModelList.get(position).getLongitude());
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

}
