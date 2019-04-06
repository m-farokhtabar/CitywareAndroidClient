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
import ir.rayas.app.citywareclient.ViewModel.Club.UserConsumePointViewModel;


public class UserPrizeRecyclerViewAdapter extends RecyclerView.Adapter<UserPrizeRecyclerViewAdapter.ViewHolder> {

    private List<UserConsumePointViewModel> ViewModelList = null;

    public UserPrizeRecyclerViewAdapter(List<UserConsumePointViewModel> ViewModel) {
        this.ViewModelList = ViewModel;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextViewPersian PrizePointTextView;
        TextViewPersian PrizeNameTextView;
        TextViewPersian PrizeDateTextView;


        public ViewHolder(View v) {
            super(v);
            PrizePointTextView = v.findViewById(R.id.PrizePointTextView);
            PrizeNameTextView = v.findViewById(R.id.PrizeNameTextView);
            PrizeDateTextView = v.findViewById(R.id.PrizeDateTextView);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_user_prize, parent, false);
        return new ViewHolder(CurrentView);
    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        int Point = (int) Double.parseDouble(ViewModelList.get(position).getPoint().toString());
        holder.PrizePointTextView.setText(String.valueOf(Point));
        holder.PrizeNameTextView.setText(ViewModelList.get(position).getPrizeTitle());
        holder.PrizeDateTextView.setText(ViewModelList.get(position).getCreate());

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

