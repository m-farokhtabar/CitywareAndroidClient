package ir.rayas.app.citywareclient.Adapter.GridView;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.View.Master.MainActivity;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowBusinessPosterDetailsActivity;
import ir.rayas.app.citywareclient.View.Share.CommissionActivity;
import ir.rayas.app.citywareclient.ViewModel.Home.BusinessPosterInfoViewModel;


public class BusinessPosterInfoGridViewAdapter extends BaseAdapter {

    private List<BusinessPosterInfoViewModel> ViewModelList = null;
    private MainActivity Context;
    private GridView Container = null;


    // Constructor
    public BusinessPosterInfoGridViewAdapter(MainActivity Context, List<BusinessPosterInfoViewModel> ViewModel, GridView Container) {
        this.ViewModelList = ViewModel;
        this.Context = Context;
        this.Container = Container;
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


    @Override
    public int getCount() {
        return ViewModelList == null ? 0 : ViewModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return ViewModelList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View v, ViewGroup parent) {

        if (v == null) {
//            v = LayoutInflater.from(Context).inflate(R.layout.recycler_view_business_poster_info, parent, false);
//        }
            LayoutInflater Inflater = (LayoutInflater) Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = Inflater.inflate(R.layout.recycler_view_business_poster_info, null);
        }

        TextViewPersian  BusinessTitleTextView = v.findViewById(R.id.BusinessTitleTextView);
        RatingBar RatingBusinessRatingBar = v.findViewById(R.id.RatingBusinessRatingBar);
        ImageView  ImagePosterInfoImageView = v.findViewById(R.id.ImagePosterInfoImageView);
        TextViewPersian PosterTitleTextView = v.findViewById(R.id.PosterTitleTextView);
        TextViewPersian AbstractPosterTextView = v.findViewById(R.id.AbstractPosterTextView);
        ImageView  IsDeliveryImageView = v.findViewById(R.id.IsDeliveryImageView);
        ImageView  IsOpenImageView = v.findViewById(R.id.IsOpenImageView);
        RelativeLayout BusinessPosterInfoContainerLinearLayout = v.findViewById(R.id.BusinessPosterInfoContainerLinearLayout);
        ButtonPersianView IntroducingBusinessButton = v.findViewById(R.id.IntroducingBusinessButton);


        BusinessTitleTextView.setText(ViewModelList.get(position).getBusinessTitle());
        PosterTitleTextView.setText(ViewModelList.get(position).getPosterTitle());
        if (ViewModelList.get(position).getPosterAbstractOfDescription().equals("") || ViewModelList.get(position).getPosterAbstractOfDescription() == null) {
            AbstractPosterTextView.setVisibility(View.GONE);
        } else {
            AbstractPosterTextView.setVisibility(View.VISIBLE);
            AbstractPosterTextView.setText(ViewModelList.get(position).getPosterAbstractOfDescription());
        }

        RatingBusinessRatingBar.setMax(5);
        RatingBusinessRatingBar.setStepSize(0.01f);

        String Rating = String.valueOf(ViewModelList.get(position).getTotalScore());
        RatingBusinessRatingBar.setRating(Float.parseFloat(Rating));
        RatingBusinessRatingBar.invalidate();

        if (ViewModelList.get(position).isHasDelivery()) {
            IsDeliveryImageView.setBackgroundResource(R.drawable.ic_is_delivery_24dp);
        } else {
            IsDeliveryImageView.setBackgroundResource(R.drawable.ic_not_delivery_24dp);
        }

        if (ViewModelList.get(position).isOpen()) {
            IsOpenImageView.setBackgroundResource(R.drawable.ic_open_24dp);
        } else {
            IsOpenImageView.setBackgroundResource(R.drawable.ic_close_24dp);
        }


        String ProductImage = "";
        if (!ViewModelList.get(position).getPosterImagePathUrl().equals("")) {
            if (ViewModelList.get(position).getPosterImagePathUrl().contains("~")) {
                ProductImage = ViewModelList.get(position).getPosterImagePathUrl().replace("~", DefaultConstant.BaseUrlWebService);
            } else {
                ProductImage = ViewModelList.get(position).getPosterImagePathUrl();
            }
        }

        if (!ProductImage.equals("")) {
            LayoutUtility.LoadImageWithGlide(Context, ProductImage, ImagePosterInfoImageView);
        } else {
            ImagePosterInfoImageView.setImageResource(R.drawable.image_default);
        }


        BusinessPosterInfoContainerLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ShowBusinessPosterDetailsIntent = Context.NewIntent(ShowBusinessPosterDetailsActivity.class);
                ShowBusinessPosterDetailsIntent.putExtra("PosterId", ViewModelList.get(position).getPosterId());
                Context.startActivity(ShowBusinessPosterDetailsIntent);
            }
        });

        IntroducingBusinessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CommissionIntent = Context.NewIntent(CommissionActivity.class);
                CommissionIntent.putExtra("BusinessId", ViewModelList.get(position).getBusinessId());
                CommissionIntent.putExtra("BusinessName", ViewModelList.get(position).getBusinessTitle());
                Context.startActivity(CommissionIntent);
            }
        });

        return v;
    }


}
