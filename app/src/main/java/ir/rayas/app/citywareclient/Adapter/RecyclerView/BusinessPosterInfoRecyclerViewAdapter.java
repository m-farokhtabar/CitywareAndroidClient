package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.OnLoadMoreListener;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.Service.Business.BookmarkService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Master.MainActivity;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowBusinessPosterDetailsActivity;
import ir.rayas.app.citywareclient.ViewModel.Business.BookmarkOutViewModel;
import ir.rayas.app.citywareclient.ViewModel.Business.BookmarkViewModel;
import ir.rayas.app.citywareclient.ViewModel.Home.BusinessPosterInfoViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;


public class BusinessPosterInfoRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IResponseService {

    private MainActivity Context;
    private RecyclerView Container = null;
    private List<BusinessPosterInfoViewModel> ViewModelList = null;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;

    private int visibleThreshold = 1;
    private int lastVisibleItem;
    private int totalItemCount;

    private boolean IsBookmark = false;

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public BusinessPosterInfoRecyclerViewAdapter(MainActivity Context, List<BusinessPosterInfoViewModel> ViewModel, RecyclerView Container, OnLoadMoreListener mOnLoadMoreListener) {
        this.ViewModelList = ViewModel;
        this.Context = Context;
        this.Container = Container;
        this.onLoadMoreListener = mOnLoadMoreListener;
        CreateLayout();
    }

    private void CreateLayout() {
        final GridLayoutManager linearLayoutManager = (GridLayoutManager) Container.getLayoutManager();
        Container.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                if (lastVisibleItem < linearLayoutManager.findLastVisibleItemPosition()) {
                    if (!isLoading && totalItemCount <= (linearLayoutManager.findLastVisibleItemPosition() + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            isLoading = true;
                            onLoadMoreListener.onLoadMore();
                        }
                    }
                }
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(Context).inflate(R.layout.recycler_view_business_poster_info, parent, false);
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


    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final UserSearchViewHolder viewHolder = (UserSearchViewHolder) holder;

        Glide.with(Context).load(ViewModelList.get(position).getPosterImagePathUrl()).apply(RequestOptions.circleCropTransform()).into(viewHolder.ImagePosterInfoImageView);

        viewHolder.BusinessTitleTextView.setText(ViewModelList.get(position).getBusinessTitle());
        viewHolder.PosterTitleTextView.setText(ViewModelList.get(position).getPosterTitle());
        viewHolder.AbstractPosterTextView.setText(ViewModelList.get(position).getPosterAbstractOfDescription());

        viewHolder.RatingBusinessRatingBar.setMax(5);
        viewHolder.RatingBusinessRatingBar.setStepSize(0.01f);

        String Rating = String.valueOf(ViewModelList.get(position).getTotalScore());
        viewHolder.RatingBusinessRatingBar.setRating(Float.parseFloat(Rating));
        viewHolder.RatingBusinessRatingBar.invalidate();

        if (ViewModelList.get(position).isHasDelivery()) {
            viewHolder.IsDeliveryImageView.setBackgroundResource(R.drawable.ic_is_delivery_24dp);
        } else {
            viewHolder.IsDeliveryImageView.setBackgroundResource(R.drawable.ic_not_delivery_24dp);
        }

        if (ViewModelList.get(position).isOpen()) {
            viewHolder.IsOpenImageView.setBackgroundResource(R.drawable.ic_open_24dp);
        } else {
            viewHolder.IsOpenImageView.setBackgroundResource(R.drawable.ic_close_24dp);
        }

        if (IsBookmark)
            viewHolder.BookmarkImageView.setImageResource(R.drawable.ic_bookmark_full_24dp);
        else
            viewHolder.BookmarkImageView.setImageResource(R.drawable.ic_bookmark_empty_24dp);

        viewHolder.BookmarkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context.ShowLoadingProgressBar();
                BookmarkService BookmarkService = new BookmarkService(BusinessPosterInfoRecyclerViewAdapter.this);

                if (IsBookmark) {
                    BookmarkService.Delete(ViewModelList.get(position).getBusinessId());
                } else {
                    AccountRepository AccountRepository = new AccountRepository(Context);
                    AccountViewModel AccountModel = AccountRepository.getAccount();
                    BookmarkOutViewModel BookmarkOutViewModel = new BookmarkOutViewModel();
                    BookmarkOutViewModel.setBusinessId(AccountModel.getUser().getId());
                    BookmarkOutViewModel.setBusinessId(ViewModelList.get(position).getBusinessId());
                    BookmarkService.Add(BookmarkOutViewModel);
                }
            }
        });

        viewHolder.BusinessPosterInfoContainerLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ShowBusinessPosterDetailsIntent = Context.NewIntent(ShowBusinessPosterDetailsActivity.class);
                ShowBusinessPosterDetailsIntent.putExtra("PosterId", ViewModelList.get(position).getPosterId());
                Context.startActivity(ShowBusinessPosterDetailsIntent);
            }
        });
    }


    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        try {
             if (ServiceMethod == ServiceMethodType.BookmarkDelete) {
                Feedback<BookmarkViewModel> FeedBack = (Feedback<BookmarkViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.DeletedSuccessful.getId()) {
                    Static.IsRefreshBookmark = true;
                    BookmarkViewModel ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        IsBookmark = false;
                        notifyDataSetChanged();
                        Container.invalidate();
                    } else {
                        Context. ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context. ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context. ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.BookmarkAdd) {
                Feedback<BookmarkViewModel> FeedBack = (Feedback<BookmarkViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.RegisteredSuccessful.getId()) {
                    Static.IsRefreshBookmark = true;
                    BookmarkViewModel ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        IsBookmark = true;
                        notifyDataSetChanged();
                        Container.invalidate();
                    } else {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context. ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context. ShowErrorInConnectDialog();
                    }
                }
            }
        } catch (Exception e) {
            Context.HideLoading();
            Context.ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
    }

    @Override
    public int getItemCount() {
        return ViewModelList == null ? 0 : ViewModelList.size();
    }

    private class UserSearchViewHolder extends RecyclerView.ViewHolder {

        ImageView ImagePosterInfoImageView;
        ImageView BookmarkImageView;
        ImageView IsOpenImageView;
        ImageView IsDeliveryImageView;
        RatingBar RatingBusinessRatingBar;
        TextViewPersian BusinessTitleTextView;
        TextViewPersian PosterTitleTextView;
        TextViewPersian AbstractPosterTextView;
        RelativeLayout BusinessPosterInfoContainerLinearLayout;

        UserSearchViewHolder(View v) {
            super(v);

            BusinessTitleTextView = v.findViewById(R.id.BusinessTitleTextView);
            RatingBusinessRatingBar = v.findViewById(R.id.RatingBusinessRatingBar);
            ImagePosterInfoImageView = v.findViewById(R.id.ImagePosterInfoImageView);
            PosterTitleTextView = v.findViewById(R.id.PosterTitleTextView);
            BookmarkImageView = v.findViewById(R.id.BookmarkImageView);
            AbstractPosterTextView = v.findViewById(R.id.AbstractPosterTextView);
            IsDeliveryImageView = v.findViewById(R.id.IsDeliveryImageView);
            IsOpenImageView = v.findViewById(R.id.IsOpenImageView);
            BusinessPosterInfoContainerLinearLayout = v.findViewById(R.id.BusinessPosterInfoContainerLinearLayout);
        }
    }

}
