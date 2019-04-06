package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.MasterChildren.ActionPointAllActivity;
import ir.rayas.app.citywareclient.ViewModel.Club.ActionViewModel;



public class ActionAllRecyclerViewAdapter  extends RecyclerView.Adapter<ActionAllRecyclerViewAdapter.ViewHolder> {

    private List<ActionViewModel> ViewModelList = null;
    private ActionPointAllActivity Context;

    public ActionAllRecyclerViewAdapter(ActionPointAllActivity context,List<ActionViewModel> ViewModel) {
        this.ViewModelList = ViewModel;
        this.Context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextViewPersian ActionTextView;
        TextViewPersian PointTextView;
        TextViewPersian DescriptionPointTextView;


        public ViewHolder(View v) {
            super(v);
            ActionTextView = v.findViewById(R.id.ActionTextView);
            PointTextView = v.findViewById(R.id.PointTextView);
            DescriptionPointTextView = v.findViewById(R.id.DescriptionPointTextView);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_action_all, parent, false);
        return new ViewHolder(CurrentView);
    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        int Point = (int) Double.parseDouble(ViewModelList.get(position).getPoint().toString());
        if (Point==0){
            holder.PointTextView.setText(Context.getResources().getString(R.string.variable_point));
        }   else {
            holder.PointTextView.setText(String.valueOf(Point));
        }
        holder.ActionTextView.setText(ViewModelList.get(position).getTitle());
        holder.DescriptionPointTextView.setText(ViewModelList.get(position).getDescription());

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

