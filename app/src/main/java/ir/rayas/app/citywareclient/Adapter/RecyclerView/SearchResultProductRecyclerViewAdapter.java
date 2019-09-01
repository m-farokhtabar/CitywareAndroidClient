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
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.View.Master.SearchActivity;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowBusinessInfoActivity;
import ir.rayas.app.citywareclient.ViewModel.Search.SearchResultViewModel;


public class SearchResultProductRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private SearchActivity Context;
    private RecyclerView Container = null;
    private List<SearchResultViewModel> ViewModelList = null;


    public SearchResultProductRecyclerViewAdapter(SearchActivity Context, List<SearchResultViewModel> ViewModel, RecyclerView Container) {
        this.ViewModelList = ViewModel;
        this.Context = Context;
        this.Container = Container;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(Context).inflate(R.layout.recycler_view_result_search, parent, false);
        return new SearchViewHolder(view);

    }

    /**
     * اضافه مودن لیست جدید
     *
     * @param ViewModel
     */
    public void AddViewModelList(List<SearchResultViewModel> ViewModel) {
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
    public void SetViewModelList(List<SearchResultViewModel> ViewModel) {
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
        final SearchViewHolder viewHolder = (SearchViewHolder) holder;

        viewHolder.BusinessTitleTextView.setText(ViewModelList.get(position).BusinessTitle);
        viewHolder.BusinessJobTitleTextView.setText(ViewModelList.get(position).BusinessJobTitle);

        int ScreenWidth = LayoutUtility.GetWidthAccordingToScreen(Context, 5)-40;

        viewHolder.SearchBusinessImageView.getLayoutParams().width = ScreenWidth;
        viewHolder.SearchBusinessImageView.getLayoutParams().height = ScreenWidth;

        viewHolder.SearchBusinessLinearLayout.getLayoutParams().width = ScreenWidth;
        viewHolder.SearchBusinessLinearLayout.getLayoutParams().height = ScreenWidth;


        String ProductImage = "";
        if (ViewModelList.get(position).getNewestPurchasedPosterUrl() != null) {
            if (!ViewModelList.get(position).getNewestPurchasedPosterUrl().equals("")) {
                if (ViewModelList.get(position).getNewestPurchasedPosterUrl().contains("~")) {
                    ProductImage = ViewModelList.get(position).getNewestPurchasedPosterUrl().replace("~", DefaultConstant.BaseUrlWebService);
                } else {
                    ProductImage = ViewModelList.get(position).getNewestPurchasedPosterUrl();
                }
            }
        } else {
            ProductImage = "";
        }

        if (!ProductImage.equals("")) {
            Glide.with(Context).load(ProductImage).apply(RequestOptions.circleCropTransform()).into(viewHolder.SearchBusinessImageView);
        } else {
            viewHolder.SearchBusinessImageView.setImageResource(R.drawable.image_default_banner);
        }


        viewHolder.BusinessLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ShowBusinessInfoActivityIntent = Context.NewIntent(ShowBusinessInfoActivity.class);
                ShowBusinessInfoActivityIntent.putExtra("BusinessId", ViewModelList.get(position).getBusinessId());
                Context.startActivity(ShowBusinessInfoActivityIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ViewModelList == null ? 0 : ViewModelList.size();
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder {

        ImageView SearchBusinessImageView;
        LinearLayout BusinessLinearLayout;
        LinearLayout SearchBusinessLinearLayout;
        TextViewPersian BusinessTitleTextView;
        TextViewPersian BusinessJobTitleTextView;

        SearchViewHolder(View v) {
            super(v);
            SearchBusinessImageView = v.findViewById(R.id.SearchBusinessImageView);
            BusinessLinearLayout = v.findViewById(R.id.BusinessLinearLayout);
            BusinessTitleTextView = v.findViewById(R.id.BusinessTitleTextView);
            BusinessJobTitleTextView = v.findViewById(R.id.BusinessJobTitleTextView);
            SearchBusinessLinearLayout = v.findViewById(R.id.SearchBusinessLinearLayout);
        }
    }

}