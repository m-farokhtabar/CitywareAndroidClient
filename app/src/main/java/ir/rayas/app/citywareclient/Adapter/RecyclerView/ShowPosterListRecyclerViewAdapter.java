package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.MyClickListener;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowBusinessPosterDetailsActivity;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowBusinessPosterListActivity;
import ir.rayas.app.citywareclient.ViewModel.Poster.PurchasedPosterViewModel;



public class ShowPosterListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ShowBusinessPosterListActivity Context;
    private RecyclerView Container = null;
    private List<PurchasedPosterViewModel> ViewModelList = null;

    private MyClickListener myClickListener;



    public ShowPosterListRecyclerViewAdapter(ShowBusinessPosterListActivity Context, List<PurchasedPosterViewModel> PosterList, RecyclerView Container) {
        this.ViewModelList = PosterList;
        this.Context = Context;
        this.Container = Container;
    }


    /**
     * اضافه مودن لیست جدید
     *
     * @param ViewModel
     */
    public void AddViewModelList(List<PurchasedPosterViewModel> ViewModel) {
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
    public void SetViewModelList(List<PurchasedPosterViewModel> ViewModel) {
        ViewModelList = new ArrayList<>();
        ViewModelList.addAll(ViewModel);
        notifyDataSetChanged();
        Container.invalidate();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(Context).inflate(R.layout.recycler_view_show_poster_list, parent, false);
        return new ShowPosterListViewHolder(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ShowPosterListViewHolder viewHolder = (ShowPosterListViewHolder) holder;

        viewHolder.BusinessTitlePosterTextView.setText(ViewModelList.get(position).getTitle());
//        viewHolder.BusinessPosterContainerRelativeLayout.getLayoutParams().width = LayoutUtility.GetWidthAccordingToScreen((Activity) Context, 1);
//        viewHolder.BusinessPosterContainerRelativeLayout.getLayoutParams().height = LayoutUtility.GetWidthAccordingToScreen((Activity) Context, 2);


        String PosterImage = "";

        if (!ViewModelList.get(position).getImagePathUrl().equals("")) {
            if (ViewModelList.get(position).getImagePathUrl().contains("~")) {
                PosterImage = ViewModelList.get(position).getImagePathUrl().replace("~", DefaultConstant.BaseUrlWebService);
            } else {
                PosterImage = ViewModelList.get(position).getImagePathUrl();
            }

        }

        if (!PosterImage.equals("")) {
            LayoutUtility.LoadImageWithGlide(Context, PosterImage, viewHolder.BusinessImagePosterImageView, LayoutUtility.GetWidthAccordingToScreen((Activity) Context, 1), LayoutUtility.GetWidthAccordingToScreen((Activity) Context, 2));
        } else {
            viewHolder.BusinessImagePosterImageView.setImageResource(R.drawable.image_default);
        }


        viewHolder.BusinessPosterContainerRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ShowBusinessPosterDetailsIntent = Context.NewIntent(ShowBusinessPosterDetailsActivity.class);
                ShowBusinessPosterDetailsIntent.putExtra("PosterId", ViewModelList.get(position).getId());
                Context.startActivity(ShowBusinessPosterDetailsIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ViewModelList == null ? 0 : ViewModelList.size();
    }


    public class ShowPosterListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

         TextViewPersian BusinessTitlePosterTextView;
         RelativeLayout BusinessPosterContainerRelativeLayout;
         ImageView BusinessImagePosterImageView;


         ShowPosterListViewHolder(View v) {
            super(v);
            BusinessTitlePosterTextView = v.findViewById(R.id.BusinessTitlePosterTextView);
            BusinessPosterContainerRelativeLayout = v.findViewById(R.id.BusinessPosterContainerRelativeLayout);
            BusinessImagePosterImageView = v.findViewById(R.id.BusinessImagePosterImageView);

            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }

    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

}