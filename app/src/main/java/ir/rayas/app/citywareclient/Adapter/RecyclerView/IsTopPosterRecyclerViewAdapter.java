package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.View.Master.MainActivity;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowBusinessPosterDetailsActivity;
import ir.rayas.app.citywareclient.ViewModel.Home.BusinessPosterInfoViewModel;



public class IsTopPosterRecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private MainActivity Context;
    private RecyclerView Container = null;
    private List<BusinessPosterInfoViewModel> ViewModelList = null;


    public IsTopPosterRecyclerViewAdapter(MainActivity Context, List<BusinessPosterInfoViewModel> ViewModel, RecyclerView Container) {
        this.ViewModelList = ViewModel;
        this.Context = Context;
        this.Container = Container;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(Context).inflate(R.layout.recycler_view_istop_poster, parent, false);
        return new UserSearchViewHolder(view);

    }

    /**
     * اضافه مودن لیست جدید
     *
     * @param ViewModel
     */
    public void AddViewModelList(List<BusinessPosterInfoViewModel> ViewModel) {
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
    public void SetViewModelList(List<BusinessPosterInfoViewModel> ViewModel) {
        ViewModelList = new ArrayList<>();
        ViewModelList.addAll(ViewModel);
        notifyDataSetChanged();
        Container.invalidate();
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


    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final UserSearchViewHolder viewHolder = (UserSearchViewHolder) holder;


        int ScreenWidth = LayoutUtility.GetWidthAccordingToScreen(Context, 5)-40;

        viewHolder.IsTopPosterImageView.getLayoutParams().width = ScreenWidth;
        viewHolder.IsTopPosterImageView.getLayoutParams().height = ScreenWidth;

        viewHolder.IsTopBusinessPosterInfoContainerLinearLayout.getLayoutParams().width = ScreenWidth;
        viewHolder.IsTopBusinessPosterInfoContainerLinearLayout.getLayoutParams().height = ScreenWidth;



        String ProductImage = "";
        if (!ViewModelList.get(position).getPosterImagePathUrl().equals("")) {
            if (ViewModelList.get(position).getPosterImagePathUrl().contains("~")) {
                ProductImage = ViewModelList.get(position).getPosterImagePathUrl().replace("~", DefaultConstant.BaseUrlWebService);
            } else {
                ProductImage = ViewModelList.get(position).getPosterImagePathUrl();
            }
        }

        if (!ProductImage.equals("")) {
            Glide.with(Context).load(ProductImage).apply(RequestOptions.circleCropTransform()).into(viewHolder.IsTopPosterImageView);
        } else {
            viewHolder.IsTopPosterImageView.setImageResource(R.drawable.image_default_banner);
        }


        viewHolder.IsTopBusinessPosterInfoContainerLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ShowBusinessPosterDetailsIntent = Context.NewIntent(ShowBusinessPosterDetailsActivity.class);
                ShowBusinessPosterDetailsIntent.putExtra("PosterId", ViewModelList.get(position).getPosterId());
                Context.startActivity(ShowBusinessPosterDetailsIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ViewModelList == null ? 0 : ViewModelList.size();
    }

    private class UserSearchViewHolder extends RecyclerView.ViewHolder  {

        ImageView IsTopPosterImageView;
        LinearLayout IsTopBusinessPosterInfoContainerLinearLayout;

        UserSearchViewHolder(View v) {
            super(v);
            IsTopPosterImageView = v.findViewById(R.id.IsTopPosterImageView);
            IsTopBusinessPosterInfoContainerLinearLayout = v.findViewById(R.id.IsTopBusinessPosterInfoContainerLinearLayout);
        }
    }

}