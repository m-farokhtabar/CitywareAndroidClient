package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.ViewModel.Club.UserActionPointViewModel;


public class UserActionRecyclerViewAdapter extends RecyclerView.Adapter<UserActionRecyclerViewAdapter.ViewHolder> {

    private List<UserActionPointViewModel> ViewModelList = null;

    public UserActionRecyclerViewAdapter(List<UserActionPointViewModel> ViewModel) {
        this.ViewModelList = ViewModel;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextViewPersian UserActionTextView;
        TextViewPersian UserPointTextView;
        TextViewPersian UserActionDateTextView;


        public ViewHolder(View v) {
            super(v);
            UserActionTextView = v.findViewById(R.id.UserActionTextView);
            UserPointTextView = v.findViewById(R.id.UserPointTextView);
            UserActionDateTextView = v.findViewById(R.id.UserActionDateTextView);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_user_action, parent, false);
        return new ViewHolder(CurrentView);
    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.UserPointTextView.setText( String.valueOf((int)Math.round(ViewModelList.get(position).getPoint())));
        holder.UserActionTextView.setText(ViewModelList.get(position).getActionTitle());
        holder.UserActionDateTextView.setText(ViewModelList.get(position).getCreate());

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

