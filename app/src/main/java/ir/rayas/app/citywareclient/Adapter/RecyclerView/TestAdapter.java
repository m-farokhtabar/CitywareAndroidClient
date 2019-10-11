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
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.Service.Business.BookmarkService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.View.Master.MainActivity;
import ir.rayas.app.citywareclient.View.Master.TestActivity;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowBusinessPosterDetailsActivity;
import ir.rayas.app.citywareclient.View.Share.CommissionActivity;
import ir.rayas.app.citywareclient.ViewModel.Business.BookmarkOutViewModel;
import ir.rayas.app.citywareclient.ViewModel.Business.BookmarkViewModel;
import ir.rayas.app.citywareclient.ViewModel.Home.BusinessPosterInfoViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;

/**
 * Created by Hajar on 9/27/2019.
 */

public class TestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IResponseService {

    private List<BusinessPosterInfoViewModel> ViewModelList = null;
    private TestActivity Context;
    private RecyclerView Container = null;


    private boolean IsBookmark;
    private int Position;
    private int BusinessId = 0;

    private ImageView imageView;


    public TestAdapter(TestActivity Context, List<BusinessPosterInfoViewModel> ViewModel, RecyclerView Container) {
        this.ViewModelList = ViewModel;
        this.Context = Context;
        this.Container = Container;
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

    public void ClearViewModelList() {
        if (ViewModelList != null) {
            if (ViewModelList.size() > 0) {
                ViewModelList.clear();
                notifyDataSetChanged();
                Container.invalidate();
            }
        }
    }


    public void SetViewModel() {

        for (int i = 0; i < ViewModelList.size(); i++) {
            if (BusinessId == ViewModelList.get(i).getBusinessId()) {

                ViewModelList.get(i).setBookmark(IsBookmark);
                if (IsBookmark) {
                    imageView.setImageResource(R.drawable.ic_favorite_green_24dp);
                } else {
                    imageView.setImageResource(R.drawable.ic_favorite_border_24dp);
                }

                notifyDataSetChanged();
                Container.invalidate();

            }
        }

    }


    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final UserSearchViewHolder viewHolder = (UserSearchViewHolder) holder;

        viewHolder.BusinessTitleTextView.setText(ViewModelList.get(position).getBusinessTitle());
        viewHolder.PosterTitleTextView.setText(ViewModelList.get(position).getPosterTitle());
        if (ViewModelList.get(position).getPosterAbstractOfDescription().equals("") || ViewModelList.get(position).getPosterAbstractOfDescription() == null) {
            viewHolder.AbstractPosterTextView.setVisibility(View.GONE);
        } else {
            viewHolder.AbstractPosterTextView.setVisibility(View.VISIBLE);
            viewHolder.AbstractPosterTextView.setText(ViewModelList.get(position).getPosterAbstractOfDescription());
        }

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


        if (ViewModelList.get(position).isBookmark())
            viewHolder.BookmarkImageView.setImageResource(R.drawable.ic_favorite_green_24dp);
        else
            viewHolder.BookmarkImageView.setImageResource(R.drawable.ic_favorite_border_24dp);


        String ProductImage = "";
        if (!ViewModelList.get(position).getPosterImagePathUrl().equals("")) {
            if (ViewModelList.get(position).getPosterImagePathUrl().contains("~")) {
                ProductImage = ViewModelList.get(position).getPosterImagePathUrl().replace("~", DefaultConstant.BaseUrlWebService);
            } else {
                ProductImage = ViewModelList.get(position).getPosterImagePathUrl();
            }
        }

        if (!ProductImage.equals("")) {
//            LayoutUtility.LoadImageWithGlide(Context, ProductImage, viewHolder.ImagePosterInfoImageView,LayoutUtility.GetWidthAccordingToScreen(Context, 1),LayoutUtility.GetWidthAccordingToScreen(Context, 2));
            LayoutUtility.LoadImageWithGlide(Context, ProductImage, viewHolder.ImagePosterInfoImageView);
        } else {
            viewHolder.ImagePosterInfoImageView.setImageResource(R.drawable.image_default);
        }

    }


    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        try {
            if (ServiceMethod == ServiceMethodType.BookmarkDelete) {
                Feedback<BookmarkViewModel> FeedBack = (Feedback<BookmarkViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.DeletedSuccessful.getId()) {
                    Static.IsRefreshBookmark = true;
                    BookmarkViewModel ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {

                        IsBookmark = false;
                        ViewModelList.get(Position).setBookmark(IsBookmark);
                        BusinessId = ViewModel.getBusinessId();
                        SetViewModel();


                    } else {

                    }
                }
            }
        } catch (Exception e) {
        }
    }


    @Override
    public int getItemCount() {
        return ViewModelList == null ? 0 : ViewModelList.size();
    }

    public class UserSearchViewHolder extends RecyclerView.ViewHolder {

        ImageView ImagePosterInfoImageView;
        ImageView BookmarkImageView;
        ImageView IsOpenImageView;
        ImageView IsDeliveryImageView;
        RatingBar RatingBusinessRatingBar;
        TextViewPersian BusinessTitleTextView;
        TextViewPersian PosterTitleTextView;
        TextViewPersian AbstractPosterTextView;
        RelativeLayout BusinessPosterInfoContainerLinearLayout;
        ButtonPersianView IntroducingBusinessButton;

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
            IntroducingBusinessButton = v.findViewById(R.id.IntroducingBusinessButton);
        }
    }

}
