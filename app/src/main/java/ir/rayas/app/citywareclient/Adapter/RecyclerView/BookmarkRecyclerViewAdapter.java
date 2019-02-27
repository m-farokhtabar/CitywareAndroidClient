package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.OnLoadMoreListener;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.Service.Basket.BasketService;
import ir.rayas.app.citywareclient.Service.Business.BookmarkService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.View.Master.BookmarkActivity;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowBusinessInfoActivity;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowProductDetailsActivity;
import ir.rayas.app.citywareclient.View.Share.AddQuickBasketActivity;
import ir.rayas.app.citywareclient.View.Share.BasketActivity;
import ir.rayas.app.citywareclient.View.Share.MapActivity;
import ir.rayas.app.citywareclient.ViewModel.Basket.QuickOrderItemViewModel;
import ir.rayas.app.citywareclient.ViewModel.Basket.QuickOrderViewModel;
import ir.rayas.app.citywareclient.ViewModel.Basket.StandardOrderItemViewModel;
import ir.rayas.app.citywareclient.ViewModel.Business.BookmarkViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;

/**
 * Created by Hajar on 10/12/2018.
 */

public class BookmarkRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IResponseService {

    private BookmarkActivity Context;
    private RecyclerView Container = null;
    private List<BookmarkViewModel> ViewModelList = null;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;

    private int visibleThreshold = 1;
    private int lastVisibleItem;
    private int totalItemCount;

    private int Position;

    public void setLoading(boolean loading) {
        isLoading = loading;
    }


    public BookmarkRecyclerViewAdapter(BookmarkActivity Context, List<BookmarkViewModel> BookmarkList, RecyclerView Container, OnLoadMoreListener mOnLoadMoreListener) {
        this.ViewModelList = BookmarkList;
        this.Context = Context;
        this.Container = Container;
        this.onLoadMoreListener = mOnLoadMoreListener;
        CreateLayout();
    }

    private void CreateLayout() {
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) Container.getLayoutManager();
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

    /**
     * اضافه مودن لیست جدید
     *
     * @param ViewModel
     */
    public void AddViewModelList(List<BookmarkViewModel> ViewModel) {
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
    public void SetViewModelList(List<BookmarkViewModel> ViewModel) {
        ViewModelList = new ArrayList<>();
        ViewModelList.addAll(ViewModel);
        notifyDataSetChanged();
        Container.invalidate();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(Context).inflate(R.layout.recycler_view_bookmark, parent, false);
        return new BookmarkViewHolder(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        BookmarkViewHolder viewHolder = (BookmarkViewHolder) holder;

        viewHolder.UserBusinessTitleTextView.setText(ViewModelList.get(position).getBusinessTitle());
        viewHolder.JobTitleTextView.setText(ViewModelList.get(position).getJobTitle());
        viewHolder.HasDeliverySwitch.setChecked(ViewModelList.get(position).isHasDelivery());
        viewHolder.IsOpenSwitch.setChecked(ViewModelList.get(position).isOpen());

        if (ViewModelList.get(position).getImagePathUrl() == null) {
            viewHolder.ImageBookmarkLinearLayout.setVisibility(View.GONE);
        } else if (ViewModelList.get(position).getImagePathUrl().equals("")) {
            viewHolder.ImageBookmarkLinearLayout.setVisibility(View.GONE);
        } else {
            viewHolder.ImageBookmarkLinearLayout.setVisibility(View.VISIBLE);
            LayoutUtility.LoadImageWithGlide(Context, ViewModelList.get(position).getImagePathUrl(), viewHolder.ImageBookmarkImageView);
        }

        String Rating = String.valueOf(ViewModelList.get(position).getTotalScore());
        viewHolder.RatingBusinessRatingBar.setRating(Float.parseFloat(Rating));

        viewHolder.UserDeleteBookmarkIconTextView.setTypeface(Font.MasterIcon);
        viewHolder.UserDeleteBookmarkIconTextView.setText("\uf014");

        viewHolder.ShowMapBusinessIconTextView.setTypeface(Font.MasterIcon);
        viewHolder.ShowMapBusinessIconTextView.setText("\uf041");

        String Address = ViewModelList.get(position).getRegionName() + " - " + ViewModelList.get(position).getAddress();
        viewHolder.AddressTextView.setText(Address);

        if (ViewModelList.get(position).getLatitude() > 0 && ViewModelList.get(position).getLongitude() > 0) {
            viewHolder.ShowMapLinearLayout.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ShowMapLinearLayout.setVisibility(View.GONE);
        }

        viewHolder.HasDeliverySwitch.setClickable(false);
        viewHolder.IsOpenSwitch.setClickable(false);


        viewHolder.ShowMapLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MapIntent = Context.NewIntent(MapActivity.class);
                MapIntent.putExtra("Latitude", ViewModelList.get(position).getLatitude());
                MapIntent.putExtra("Longitude", ViewModelList.get(position).getLongitude());
                MapIntent.putExtra("Going", 2);
                Context.startActivity(MapIntent);
            }
        });

        viewHolder.UserDeleteBookmarkIconTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Position = position;
                Context.ShowLoadingProgressBar();
                BookmarkService BookmarkService = new BookmarkService(BookmarkRecyclerViewAdapter.this);
                BookmarkService.Delete(ViewModelList.get(position).getBusinessId());
            }
        });

        viewHolder.QuickItemsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent AddQuickBasketIntent = new Intent(Context, AddQuickBasketActivity.class);
                AddQuickBasketIntent.putExtra("BusinessId", ViewModelList.get(position).getBusinessId());
                Context.startActivity(AddQuickBasketIntent);

            }
        });


        if (ViewModelList.get(position).isActive()) {
            viewHolder.IntroducingBusinessButton.setEnabled(true);
            viewHolder.InformationShowButton.setEnabled(true);
            viewHolder.ShowMapLinearLayout.setClickable(true);
            viewHolder.ShowMapLinearLayout.setEnabled(true);
            viewHolder.ShowMapBusinessIconTextView.setTextColor(LayoutUtility.GetColorFromResource(this.Context, R.color.FontSemiDarkThemeColor));
            viewHolder.ShowMapBusinessTextView.setTextColor(LayoutUtility.GetColorFromResource(this.Context, R.color.FontSemiDarkThemeColor));

            viewHolder.InformationShowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ShowBusinessInfoIntent = Context.NewIntent(ShowBusinessInfoActivity.class);
                    ShowBusinessInfoIntent.putExtra("BusinessId", ViewModelList.get(position).getBusinessId());
                    Context.startActivity(ShowBusinessInfoIntent);
                }
            });

        } else {

            viewHolder.IntroducingBusinessButton.setEnabled(false);
            viewHolder.InformationShowButton.setEnabled(false);
            viewHolder.ShowMapLinearLayout.setClickable(false);
            viewHolder.ShowMapLinearLayout.setEnabled(false);
            viewHolder.ShowMapBusinessIconTextView.setTextColor(LayoutUtility.GetColorFromResource(this.Context, R.color.FontSemiBlackColor));
            viewHolder.ShowMapBusinessTextView.setTextColor(LayoutUtility.GetColorFromResource(this.Context, R.color.FontSemiBlackColor));
        }

    }

    @Override
    public int getItemCount() {
        return ViewModelList == null ? 0 : ViewModelList.size();
    }


    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.BookmarkDelete) {
                Feedback<BookmarkViewModel> FeedBack = (Feedback<BookmarkViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.DeletedSuccessful.getId()) {
                    ViewModelList.remove(Position);
                    notifyDataSetChanged();
                    Container.invalidate();
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            }
        } catch (Exception e) {
            Context.ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
    }


    private class BookmarkViewHolder extends RecyclerView.ViewHolder {

        public TextViewPersian UserDeleteBookmarkIconTextView;
        public TextViewPersian UserBusinessTitleTextView;
        public SwitchCompat HasDeliverySwitch;
        public SwitchCompat IsOpenSwitch;
        public TextViewPersian JobTitleTextView;
        public AppCompatRatingBar RatingBusinessRatingBar;
        public ImageView ImageBookmarkImageView;
        public TextViewPersian AddressTextView;
        public LinearLayout ShowMapLinearLayout;
        public LinearLayout ImageBookmarkLinearLayout;
        public LinearLayout QuickItemsLinearLayout;
        public ButtonPersianView InformationShowButton;
        public ButtonPersianView IntroducingBusinessButton;
        public TextViewPersian ShowMapBusinessIconTextView;
        public TextViewPersian ShowMapBusinessTextView;

        public BookmarkViewHolder(View v) {
            super(v);
            UserDeleteBookmarkIconTextView = v.findViewById(R.id.UserDeleteBookmarkIconTextView);
            UserBusinessTitleTextView = v.findViewById(R.id.UserBusinessTitleTextView);
            JobTitleTextView = v.findViewById(R.id.JobTitleTextView);
            HasDeliverySwitch = v.findViewById(R.id.HasDeliverySwitch);
            IsOpenSwitch = v.findViewById(R.id.IsOpenSwitch);
            RatingBusinessRatingBar = v.findViewById(R.id.RatingBusinessRatingBar);
            ImageBookmarkImageView = v.findViewById(R.id.ImageBookmarkImageView);
            AddressTextView = v.findViewById(R.id.AddressTextView);
            ShowMapLinearLayout = v.findViewById(R.id.ShowMapLinearLayout);
            InformationShowButton = v.findViewById(R.id.InformationShowButton);
            IntroducingBusinessButton = v.findViewById(R.id.IntroducingBusinessButton);
            ImageBookmarkLinearLayout = v.findViewById(R.id.ImageBookmarkLinearLayout);
            ShowMapBusinessIconTextView = v.findViewById(R.id.ShowMapBusinessIconTextView);
            ShowMapBusinessTextView = v.findViewById(R.id.ShowMapBusinessTextView);
            QuickItemsLinearLayout = v.findViewById(R.id.QuickItemsLinearLayout);
        }
    }





}