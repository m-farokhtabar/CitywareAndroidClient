package ir.rayas.app.citywareclient.Adapter.RecyclerView;


import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowBusinessDetailsActivity;
import ir.rayas.app.citywareclient.View.UserProfileChildren.BusinessSetActivity;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessOpenTimeViewModel;

/**
 * Created by Hajar on 11/24/2018.
 */

public class BusinessDetailsOpenTimeRecyclerViewAdapter extends RecyclerView.Adapter<BusinessDetailsOpenTimeRecyclerViewAdapter.ViewHolder>  {

    private List<BusinessOpenTimeViewModel> ViewModelList = null;
    private RecyclerView Container = null;
    private ShowBusinessDetailsActivity Context;
    private int layoutResourceId;
    private int Position;

    public BusinessDetailsOpenTimeRecyclerViewAdapter(ShowBusinessDetailsActivity context, int layoutResourceId, List<BusinessOpenTimeViewModel> ViewModel, RecyclerView Container) {
        this.Context = context;
        this.layoutResourceId = layoutResourceId;
        this.Container = Container;
        this.ViewModelList = ViewModel;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextViewPersian BusinessOpenTimeTitleTextView;
        public TextViewPersian ToHoursTextViewBusinessOpenTime;
        public TextViewPersian FromHoursTextViewBusinessOpenTime;


        public ViewHolder(View v) {
            super(v);
            BusinessOpenTimeTitleTextView = v.findViewById(R.id.BusinessOpenTimeTitleTextView);
            ToHoursTextViewBusinessOpenTime = v.findViewById(R.id.ToHoursTextViewBusinessOpenTime);
            FromHoursTextViewBusinessOpenTime = v.findViewById(R.id.FromHoursTextViewBusinessOpenTime);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(layoutResourceId, parent, false);
        ViewHolder CurrentViewHolder = new ViewHolder(CurrentView);
        return CurrentViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String[] DayOfWeek = new String[]{Context.getResources().getString(R.string.saturday), Context.getResources().getString(R.string.monday), Context.getResources().getString(R.string.sunday), Context.getResources().getString(R.string.wednesday),
                Context.getResources().getString(R.string.tuesday), Context.getResources().getString(R.string.thursday), Context.getResources().getString(R.string.friday)};

        holder.BusinessOpenTimeTitleTextView.setText(DayOfWeek[ViewModelList.get(position).getDayOfWeek()]);
        holder.ToHoursTextViewBusinessOpenTime.setText(ViewModelList.get(position).getTo());
        holder.FromHoursTextViewBusinessOpenTime.setText(ViewModelList.get(position).getFrom());

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

