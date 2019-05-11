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
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.View.Master.SearchActivity;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowBusinessPosterDetailsActivity;
import ir.rayas.app.citywareclient.ViewModel.Search.SearchViewModel;
import ir.rayas.app.citywareclient.ViewModel.Home.BusinessPosterInfoViewModel;


public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private SearchActivity Context;
    private RecyclerView Container = null;
    private List<SearchViewModel> ViewModelList = null;

    public SearchRecyclerViewAdapter(SearchActivity Context, List<SearchViewModel> ViewModel, RecyclerView Container) {
        this.ViewModelList = ViewModel;
        this.Context = Context;
        this.Container = Container;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(Context).inflate(R.layout.recycler_view_search, parent, false);
        return new UserSearchViewHolder(view);

    }

    /**
     * اضافه مودن لیست جدید
     *
     * @param ViewModel
     */
    public void AddViewModelList(List<SearchViewModel> ViewModel) {
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
    public void SetViewModelList(List<SearchViewModel> ViewModel) {
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

        final List<BusinessPosterInfoViewModel> businessPosterInfoViewModels = ViewModelList.get(position).getBusinessPosterInfoViewModel();

        int Height = LayoutUtility.GetWidthAccordingToScreen(Context, 3);

        int ScreenWidth = LayoutUtility.GetWidthAccordingToScreen(Context, 3);
        int ScreenWidthOne = LayoutUtility.GetWidthAccordingToScreen(Context, 1);

        viewHolder.Column1ImageView.getLayoutParams().width = ScreenWidth;
        viewHolder.Column1ImageView.getLayoutParams().height = ScreenWidth;

        viewHolder.Column2ImageView.getLayoutParams().width = ScreenWidth;
        viewHolder.Column2ImageView.getLayoutParams().height = ScreenWidth;

        viewHolder.Column3ImageView.getLayoutParams().width = ScreenWidth;
        viewHolder.Column3ImageView.getLayoutParams().height = ScreenWidth;


        if (ViewModelList.get(position).getCols() == 1) {
            viewHolder.OneColumnBannerRelativeLayout.setVisibility(View.GONE);
            viewHolder.ThreeColumnsLinearLayout.setVisibility(View.VISIBLE);

            for (int i = 0; i < businessPosterInfoViewModels.size(); i++) {
                if (i == 0) {
                    LayoutUtility.LoadImageWithGlide(Context, businessPosterInfoViewModels.get(i).getPosterImagePathUrl(), viewHolder.Column1ImageView, ScreenWidth, ScreenWidth);
                } else if (i == 1) {
                    if (businessPosterInfoViewModels.get(i).getPosterImagePathUrl() == null) {
                        viewHolder.Column3ImageView.setImageResource(R.drawable.image_default);
                    } else {
                        if (businessPosterInfoViewModels.get(i).getPosterImagePathUrl().equals("")) {
                            viewHolder.Column2ImageView.setImageResource(R.drawable.image_default);
                        } else {
                            LayoutUtility.LoadImageWithGlide(Context, businessPosterInfoViewModels.get(i).getPosterImagePathUrl(), viewHolder.Column2ImageView, ScreenWidth, ScreenWidth);
                        }
                    }

                } else {
                    if (businessPosterInfoViewModels.get(i).getPosterImagePathUrl() == null) {
                        viewHolder.Column3ImageView.setImageResource(R.drawable.image_default);
                    } else {
                        if (businessPosterInfoViewModels.get(i).getPosterImagePathUrl().equals("")) {
                            viewHolder.Column3ImageView.setImageResource(R.drawable.image_default);
                        } else {
                            LayoutUtility.LoadImageWithGlide(Context, businessPosterInfoViewModels.get(i).getPosterImagePathUrl(), viewHolder.Column3ImageView, ScreenWidth, ScreenWidth);
                        }
                    }
                }
            }
            
            viewHolder.Column3ImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ShowBusinessPosterDetailsIntent =  Context.NewIntent(ShowBusinessPosterDetailsActivity.class);
                    ShowBusinessPosterDetailsIntent.putExtra("PosterId",businessPosterInfoViewModels.get(2).getPosterId());
                    Context.startActivity(ShowBusinessPosterDetailsIntent);
                }
            });

            viewHolder.Column2ImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ShowBusinessPosterDetailsIntent =  Context.NewIntent(ShowBusinessPosterDetailsActivity.class);
                    ShowBusinessPosterDetailsIntent.putExtra("PosterId",businessPosterInfoViewModels.get(1).getPosterId());
                    Context.startActivity(ShowBusinessPosterDetailsIntent);
                }
            });

            viewHolder.Column1ImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ShowBusinessPosterDetailsIntent =  Context.NewIntent(ShowBusinessPosterDetailsActivity.class);
                    ShowBusinessPosterDetailsIntent.putExtra("PosterId",businessPosterInfoViewModels.get(0).getPosterId());
                    Context.startActivity(ShowBusinessPosterDetailsIntent);
                }
            });

        } else if (ViewModelList.get(position).getCols() == 3) {
            viewHolder.OneColumnBannerRelativeLayout.setVisibility(View.VISIBLE);
            viewHolder.ThreeColumnsLinearLayout.setVisibility(View.GONE);

            int ScreenH =  Height*  businessPosterInfoViewModels.get(0).getRows();
            viewHolder.OneColumnImageView.getLayoutParams().width = ScreenWidthOne;
            viewHolder.OneColumnImageView.getLayoutParams().height = ScreenH;

            LayoutUtility.LoadImageWithGlide(Context, businessPosterInfoViewModels.get(0).getPosterImagePathUrl(), viewHolder.OneColumnImageView, ScreenWidthOne, ScreenH);


            viewHolder.OneColumnImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ShowBusinessPosterDetailsIntent =  Context.NewIntent(ShowBusinessPosterDetailsActivity.class);
                    ShowBusinessPosterDetailsIntent.putExtra("PosterId",businessPosterInfoViewModels.get(0).getPosterId());
                    Context.startActivity(ShowBusinessPosterDetailsIntent);
                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return ViewModelList == null ? 0 : ViewModelList.size();
    }


    private class UserSearchViewHolder extends RecyclerView.ViewHolder {

        ImageView OneColumnImageView;
        ImageView Column1ImageView;
        ImageView Column2ImageView;
        ImageView Column3ImageView;
        RelativeLayout OneColumnBannerRelativeLayout;
        LinearLayout ThreeColumnsLinearLayout;

        UserSearchViewHolder(View v) {
            super(v);

            OneColumnImageView = v.findViewById(R.id.OneColumnImageView);
            OneColumnBannerRelativeLayout = v.findViewById(R.id.OneColumnBannerRelativeLayout);
            ThreeColumnsLinearLayout = v.findViewById(R.id.ThreeColumnsLinearLayout);
            Column1ImageView = v.findViewById(R.id.Column1ImageView);
            Column2ImageView = v.findViewById(R.id.Column2ImageView);
            Column3ImageView = v.findViewById(R.id.Column3ImageView);

        }

    }


}