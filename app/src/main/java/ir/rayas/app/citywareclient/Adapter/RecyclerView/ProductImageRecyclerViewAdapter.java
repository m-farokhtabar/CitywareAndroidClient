package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Order.ProductImageService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.View.UserProfileChildren.ProductImageSetActivity;
import ir.rayas.app.citywareclient.View.UserProfileChildren.ProductSetActivity;
import ir.rayas.app.citywareclient.ViewModel.Order.ProductImageViewModel;

/**
 * Created by Hajar on 8/30/2018.
 */

public class ProductImageRecyclerViewAdapter extends RecyclerView.Adapter<ProductImageRecyclerViewAdapter.ViewHolder> implements IResponseService {

    private List<ProductImageViewModel> ViewModelList = null;
    private RecyclerView Container = null;
    private ProductSetActivity Context;
    private int Position;

    public List<ProductImageViewModel> getViewModelList() {
        return ViewModelList;
    }

    public ProductImageRecyclerViewAdapter(ProductSetActivity context, List<ProductImageViewModel> ViewModel, RecyclerView Container) {
        this.Context = context;
        this.Container = Container;
        this.ViewModelList = ViewModel;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextViewPersian DeleteProductImageIconTextView;
        public ButtonPersianView EditProductImageButton;
        public TextViewPersian ProductImageOrderTextView;
        public ImageView ImageProductImageView;


        public ViewHolder(View v) {
            super(v);
            DeleteProductImageIconTextView = v.findViewById(R.id.DeleteProductImageIconTextView);
            EditProductImageButton = v.findViewById(R.id.EditProductImageButton);
            ProductImageOrderTextView = v.findViewById(R.id.ProductImageOrderTextView);
            ImageProductImageView = v.findViewById(R.id.ImageProductImageView);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_product_image, parent, false);
        ViewHolder CurrentViewHolder = new ViewHolder(CurrentView);
        return CurrentViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.ProductImageOrderTextView.setText(String.valueOf(ViewModelList.get(position).getOrder()));
        //Get Width And Height Of Screen
        int ScreenWidth = LayoutUtility.GetWidthAccordingToScreen(Context, 3);
        int ScreenHeight = LayoutUtility.GetHeightAccordingToScreen(Context, 3);

        LayoutUtility.LoadImageWithGlide(Context, ViewModelList.get(position).getFullPath(), holder.ImageProductImageView, ScreenWidth , ScreenHeight );

        holder.DeleteProductImageIconTextView.setTypeface(Font.MasterIcon);
        holder.DeleteProductImageIconTextView.setText("\uf014");

        if (ViewModelList.get(position).isActive()) {
            holder.EditProductImageButton.setEnabled(true);
            holder.DeleteProductImageIconTextView.setEnabled(true);
            holder.DeleteProductImageIconTextView.setClickable(true);
            holder.DeleteProductImageIconTextView.setTextColor(Context.getResources().getColor(R.color.FontRedColor));

            holder.DeleteProductImageIconTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Position = position;
                    Context.setRetryType(1);
                    Context.ShowLoadingProgressBar();
                    ProductImageService productImageService = new ProductImageService(ProductImageRecyclerViewAdapter.this);
                    productImageService.Delete(ViewModelList.get(position).getId());
                }
            });

            holder.EditProductImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent EditProductImageIntent = Context.NewIntent(ProductImageSetActivity.class);
                    EditProductImageIntent.putExtra("SetProductImage", "Edit");
                    EditProductImageIntent.putExtra("ProductImageId", ViewModelList.get(position).getId());
                    EditProductImageIntent.putExtra("ProductId", Context.getProductId());
                    //2 Edit - you can find Add in UserAddressFragment
                    Context.startActivity(EditProductImageIntent);

                }
            });
        } else {
            holder.EditProductImageButton.setEnabled(false);
            holder.DeleteProductImageIconTextView.setEnabled(false);
            holder.DeleteProductImageIconTextView.setClickable(false);
            holder.DeleteProductImageIconTextView.setTextColor(Context.getResources().getColor(R.color.FontSemiBlackColor));
        }


    }

    @Override
    public int getItemCount() {
        int Output;
        if (ViewModelList == null)
            Output = 0;
        else
            Output = ViewModelList.size();
        return Output;
    }

    /**
     * ویرایش اطلاعات یک آدرس در لیست
     *
     * @param ViewModel
     */
    public void SetViewModel(ProductImageViewModel ViewModel) {


        if (ViewModel != null && ViewModelList != null && ViewModelList.size() > 0) {
            for (ProductImageViewModel Item : ViewModelList) {
                if (Item.getId() == ViewModel.getId()) {
                    Item.setOrder(ViewModel.getOrder());
                    Item.setFullPath(ViewModel.getFullPath());

                }
            }
            notifyDataSetChanged();
            Container.invalidate();
        }
    }

    /**
     * اضافه مودن یک آدرس جدید به لیست
     *
     * @param ViewModel
     */
    public void AddViewModel(ProductImageViewModel ViewModel) {
        if (ViewModel != null) {
            if (ViewModelList == null)
                ViewModelList = new ArrayList<>();
            ViewModelList.add(ViewModel);
            notifyDataSetChanged();
            Container.invalidate();
        }
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.ProductImageDelete) {
                Feedback<ProductImageViewModel> FeedBack = (Feedback<ProductImageViewModel>) Data;

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

}

