package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowCommentBusinessActivity;
import ir.rayas.app.citywareclient.ViewModel.Business.CommentViewModel;


public class BusinessCommentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ShowCommentBusinessActivity Context;
    private RecyclerView Container = null;
    private List<CommentViewModel> ViewModelList = null;

    public BusinessCommentRecyclerViewAdapter(ShowCommentBusinessActivity Context, List<CommentViewModel> CommentList, RecyclerView Container) {
        this.ViewModelList = CommentList;
        this.Context = Context;
        this.Container = Container;

    }

    /**
     * اضافه مودن لیست جدید
     *
     * @param ViewModel
     */
    public void AddViewModelList(List<CommentViewModel> ViewModel) {
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
    public void SetViewModelList(List<CommentViewModel> ViewModel) {
        ViewModelList = new ArrayList<>();
        ViewModelList.addAll(ViewModel);
        notifyDataSetChanged();
        Container.invalidate();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(Context).inflate(R.layout.recycler_view_business_comment, parent, false);
        return new CommentViewHolder(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        CommentViewHolder viewHolder = (CommentViewHolder) holder;

        viewHolder.CommentTextViewShowCommentBusinessActivity.setText(ViewModelList.get(position).getText());
        viewHolder.NickNameCommentTextViewShowCommentBusinessActivity.setText(ViewModelList.get(position).getNickName());
        viewHolder.CreateDateCommentTextViewShowCommentBusinessActivity.setText(ViewModelList.get(position).getCreate());


        if (ViewModelList.get(position).getScore() == null) {
            viewHolder.StickerCommentImageViewShowCommentBusinessActivity.setImageResource(R.drawable.ic_sentiment_24dp);
            viewHolder.RatingUserCommentRatingBarShowCommentBusinessActivity.setVisibility(View.GONE);
        } else {
            viewHolder.RatingUserCommentRatingBarShowCommentBusinessActivity.setVisibility(View.VISIBLE);
            String Rating = String.valueOf(ViewModelList.get(position).getScore());
            viewHolder.RatingUserCommentRatingBarShowCommentBusinessActivity.setRating(Float.parseFloat(Rating));

            if ( ViewModelList.get(position).getScore() <= 1) {
                viewHolder.StickerCommentImageViewShowCommentBusinessActivity.setImageResource(R.drawable.ic_sentiment_very_dissatisfied_24dp);
            } else if (1 < ViewModelList.get(position).getScore() && ViewModelList.get(position).getScore() <= 2) {
                viewHolder.StickerCommentImageViewShowCommentBusinessActivity.setImageResource(R.drawable.ic_sentiment_dissatisfied_24dp);
            } else if (2 < ViewModelList.get(position).getScore() && ViewModelList.get(position).getScore() <= 3) {
                viewHolder.StickerCommentImageViewShowCommentBusinessActivity.setImageResource(R.drawable.ic_sentiment_neutral_24dp);
            } else if (3 < ViewModelList.get(position).getScore() && ViewModelList.get(position).getScore() <= 4) {
                viewHolder.StickerCommentImageViewShowCommentBusinessActivity.setImageResource(R.drawable.ic_sentiment_satisfied_24dp);
            } else if (4 < ViewModelList.get(position).getScore() && ViewModelList.get(position).getScore() <= 5) {
                viewHolder.StickerCommentImageViewShowCommentBusinessActivity.setImageResource(R.drawable.ic_sentiment_very_satisfied_24dp);
            }
        }

    }

    @Override
    public int getItemCount() {
        return ViewModelList == null ? 0 : ViewModelList.size();
    }


    private class CommentViewHolder extends RecyclerView.ViewHolder {

         TextViewPersian CommentTextViewShowCommentBusinessActivity;
         TextViewPersian NickNameCommentTextViewShowCommentBusinessActivity;
         TextViewPersian CreateDateCommentTextViewShowCommentBusinessActivity;
         RatingBar RatingUserCommentRatingBarShowCommentBusinessActivity;
         ImageView StickerCommentImageViewShowCommentBusinessActivity;


         CommentViewHolder(View v) {
            super(v);
            CommentTextViewShowCommentBusinessActivity = v.findViewById(R.id.CommentTextViewShowCommentBusinessActivity);
            NickNameCommentTextViewShowCommentBusinessActivity = v.findViewById(R.id.NickNameCommentTextViewShowCommentBusinessActivity);
            CreateDateCommentTextViewShowCommentBusinessActivity = v.findViewById(R.id.CreateDateCommentTextViewShowCommentBusinessActivity);
            RatingUserCommentRatingBarShowCommentBusinessActivity = v.findViewById(R.id.RatingUserCommentRatingBarShowCommentBusinessActivity);
            StickerCommentImageViewShowCommentBusinessActivity = v.findViewById(R.id.StickerCommentImageViewShowCommentBusinessActivity);

        }
    }
}