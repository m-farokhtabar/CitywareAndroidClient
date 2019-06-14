package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResultPassing;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Share.UserSearchActivity;
import ir.rayas.app.citywareclient.ViewModel.Search.OutUserSearchViewModel;


public class UserSearchRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private UserSearchActivity Context;
    private RecyclerView Container = null;
    private List<OutUserSearchViewModel> ViewModelList = null;


    public UserSearchRecyclerViewAdapter(UserSearchActivity Context, List<OutUserSearchViewModel> UserList, RecyclerView Container) {
        this.ViewModelList = UserList;
        this.Context = Context;
        this.Container = Container;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(Context).inflate(R.layout.recycler_view_user_search, parent, false);
        return new UserSearchViewHolder(view);

    }

    /**
     * اضافه مودن لیست جدید
     *
     * @param ViewModel
     */
    public void AddViewModelList(List<OutUserSearchViewModel> ViewModel) {
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
    public void SetViewModelList(List<OutUserSearchViewModel> ViewModel) {
        ViewModelList = new ArrayList<>();
        ViewModelList.addAll(ViewModel);
        notifyDataSetChanged();
        Container.invalidate();
    }


    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final UserSearchViewHolder viewHolder = (UserSearchViewHolder) holder;



        String Name = ViewModelList.get(position).getName();
        String Family = ViewModelList.get(position).getFamily();
        final String UserName = Name + " " + Family;
        final String NickName = ViewModelList.get(position).getNickName();

        viewHolder.UserNameTextView.setText(UserName + " - " + NickName);
        viewHolder.UserNameTextView.setTag(ViewModelList.get(position).getId());

        viewHolder.UserNameRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OnGetUserIdFloatingActionButtonClick(ViewModelList.get(position).getId(),UserName);

            }
        });

    }

    
    @Override
    public int getItemCount() {
        return ViewModelList == null ? 0 : ViewModelList.size();
    }


    private class UserSearchViewHolder extends RecyclerView.ViewHolder  {

        TextViewPersian UserNameTextView;
        RelativeLayout UserNameRelativeLayout;

        UserSearchViewHolder(View v) {
            super(v);

            UserNameTextView = v.findViewById(R.id.UserNameTextView);
            UserNameRelativeLayout = v.findViewById(R.id.UserNameRelativeLayout);

        }

    }

    private void OnGetUserIdFloatingActionButtonClick(int UserId, String UserName) {
        if (UserId > 0) {
            if (Context.getIntent().getIntExtra("FromActivityId", -1) > -1) {
                HashMap<String, Object> Output = new HashMap<>();
                Output.put("UserName", UserName);
                Output.put("UserId", UserId);
                ActivityResultPassing.Push(new ActivityResult(Context.getIntent().getIntExtra("FromActivityId", -1), Context.getCurrentActivityId(), Output));
            }
            Context.FinishCurrentActivity();
        } else {
            Context.ShowToast(Context.getResources().getString(R.string.please_enter_user_selection), Toast.LENGTH_LONG, MessageType.Warning);
        }
    }


}