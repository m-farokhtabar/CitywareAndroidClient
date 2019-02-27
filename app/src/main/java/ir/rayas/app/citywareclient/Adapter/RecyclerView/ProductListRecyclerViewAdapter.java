package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Order.ProductService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.UserProfileChildren.BusinessSetActivity;
import ir.rayas.app.citywareclient.View.UserProfileChildren.ProductSetActivity;
import ir.rayas.app.citywareclient.ViewModel.Order.ProductImageViewModel;
import ir.rayas.app.citywareclient.ViewModel.Order.ProductViewModel;

/**
 * Created by Hajar on 8/30/2018.
 */

public class ProductListRecyclerViewAdapter extends RecyclerView.Adapter<ProductListRecyclerViewAdapter.ViewHolder> implements IResponseService {

    private List<ProductViewModel> ViewModelList = null;
    private RecyclerView Container = null;
    private BusinessSetActivity Context;
    private int Position;

    public ProductListRecyclerViewAdapter(BusinessSetActivity context, List<ProductViewModel> ViewModel, RecyclerView Container) {
        this.Context = context;
        this.Container = Container;
        this.ViewModelList = ViewModel;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextViewPersian DeleteBusinessProductIconTextView;
        public ButtonPersianView EditBusinessProductButton;
        public TextViewPersian BusinessProductTitleTextView;
        public SwitchCompat IsAvailableSwitch;
        public TextViewPersian CommissionTextView;
        public TextViewPersian PriceTextView;
        public TextViewPersian InventoryTextView;
        public TextViewPersian ViewSortTextView;
        public TextViewPersian DescriptionAbstractTextView;
        public ImageView ImageProductImageView;


        public ViewHolder(View v) {
            super(v);
            DeleteBusinessProductIconTextView = v.findViewById(R.id.DeleteBusinessProductIconTextView);
            EditBusinessProductButton = v.findViewById(R.id.EditBusinessProductButton);
            BusinessProductTitleTextView = v.findViewById(R.id.BusinessProductTitleTextView);
            IsAvailableSwitch = v.findViewById(R.id.AvailableSwitch);
            CommissionTextView = v.findViewById(R.id.CommissionTextView);
            PriceTextView = v.findViewById(R.id.PriceTextView);
            InventoryTextView = v.findViewById(R.id.InventoryTextView);
            ViewSortTextView = v.findViewById(R.id.ViewSortTextView);
            DescriptionAbstractTextView = v.findViewById(R.id.DescriptionAbstractTextView);
            ImageProductImageView = v.findViewById(R.id.ImageProductImageView);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_product_list, parent, false);
        ViewHolder CurrentViewHolder = new ViewHolder(CurrentView);
        return CurrentViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.BusinessProductTitleTextView.setText(ViewModelList.get(position).getName());
        holder.IsAvailableSwitch.setChecked(ViewModelList.get(position).isAvailaible());
        holder.CommissionTextView.setText(String.valueOf(ViewModelList.get(position).getCommission()) + "%");
        holder.PriceTextView.setText(Utility.GetIntegerNumberWithComma((long) ViewModelList.get(position).getPrice()));
        holder.InventoryTextView.setText(String.valueOf(ViewModelList.get(position).getInventory()));
        holder.ViewSortTextView.setText(String.valueOf(ViewModelList.get(position).getOrder()));
        holder.DescriptionAbstractTextView.setText(ViewModelList.get(position).getAbstractOfDescription());


        List<ProductImageViewModel> ProductImageList = new ArrayList<>();
        ProductImageList = ViewModelList.get(position).getProductImageList();
        if (ProductImageList.size() > 0) {
            String ProductImage = ProductImageList.get(0).getFullPath();
            if (!ProductImage.equals(""))
                LayoutUtility.LoadImageWithGlide(Context, ProductImage, holder.ImageProductImageView);
        }

        holder.DeleteBusinessProductIconTextView.setTypeface(Font.MasterIcon);
        holder.DeleteBusinessProductIconTextView.setText("\uf014");

        if (ViewModelList.get(position).isActive()) {
            holder.EditBusinessProductButton.setEnabled(true);
            holder.DeleteBusinessProductIconTextView.setEnabled(true);
            holder.DeleteBusinessProductIconTextView.setClickable(true);
            holder.DeleteBusinessProductIconTextView.setTextColor(LayoutUtility.GetColorFromResource(this.Context,R.color.FontRedColor));
            holder.IsAvailableSwitch.setEnabled(true);


            holder.DeleteBusinessProductIconTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowDeleteDialog(position);
                }
            });

            holder.EditBusinessProductButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent EditProductIntent = Context.NewIntent(ProductSetActivity.class);
                    EditProductIntent.putExtra("SetProduct", "Edit");
                    EditProductIntent.putExtra("ProductId", ViewModelList.get(position).getId());
                    EditProductIntent.putExtra("BusinessId", Context.getBusinessId());
                    //2 Edit - you can find Add in UserAddressFragment
                    Context.startActivity(EditProductIntent);

                }
            });
        } else {
            holder.EditBusinessProductButton.setEnabled(false);
            holder.DeleteBusinessProductIconTextView.setEnabled(false);
            holder.DeleteBusinessProductIconTextView.setClickable(false);
            holder.DeleteBusinessProductIconTextView.setTextColor(LayoutUtility.GetColorFromResource(this.Context,R.color.FontSemiBlackColor));
            holder.IsAvailableSwitch.setEnabled(false);
        }


    }

    private void ShowDeleteDialog(final int CurrentPosition) {
        final Dialog DeleteDialog = new Dialog(Context);
        DeleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DeleteDialog.setContentView(R.layout.dialog_yes_no_question);
        ButtonPersianView DialogBusinessDeleteOkButton = DeleteDialog.findViewById(R.id.DialogYesNoQuestionOkButton);
        ButtonPersianView DialogBusinessDeleteCancelButton = DeleteDialog.findViewById(R.id.DialogYesNoQuestionCancelButton);

        TextViewPersian DialogYesNoQuestionTitleTextView = DeleteDialog.findViewById(R.id.DialogYesNoQuestionTitleTextView);
        DialogYesNoQuestionTitleTextView.setText(R.string.product_delete);

        TextViewPersian DialogYesNoQuestionDescriptionTextView = DeleteDialog.findViewById(R.id.DialogYesNoQuestionDescriptionTextView);
        DialogYesNoQuestionDescriptionTextView.setText(R.string.description_product_delete);

        TextViewPersian DialogYesNoQuestionWarningTextView = DeleteDialog.findViewById(R.id.DialogYesNoQuestionWarningTextView);
        DialogYesNoQuestionWarningTextView.setText(R.string.warning_product_delete);

        DialogBusinessDeleteOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteDialog.dismiss();

                Context.setRetryType(1);
                Context.ShowLoadingProgressBar();
                ProductService ProductService = new ProductService(ProductListRecyclerViewAdapter.this);
                ProductService.Delete(ViewModelList.get(CurrentPosition).getId());
            }
        });

        DialogBusinessDeleteCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteDialog.dismiss();
            }
        });

        DeleteDialog.show();
        Position = CurrentPosition;
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
    public void SetViewModel(ProductViewModel ViewModel) {
        if (ViewModel != null && ViewModelList != null && ViewModelList.size() > 0) {
            for (ProductViewModel Item : ViewModelList) {
                if (Item.getId() == ViewModel.getId()) {
                    Item.setName(ViewModel.getName());
                    Item.setAvailaible(ViewModel.isAvailaible());
                    Item.setCommission(ViewModel.getCommission());
                    Item.setPrice(ViewModel.getPrice());
                    Item.setInventory(ViewModel.getInventory());
                    Item.setOrder(ViewModel.getOrder());
                    Item.setAbstractOfDescription(ViewModel.getAbstractOfDescription());
                }
            }
            notifyDataSetChanged();
            Container.invalidate();
        }
    }

    /**
     * تغییر تصویر محصول
     *
     * @param Id
     * @param FullPath
     */
    public void SetImage(int Id, String FullPath) {
        for (ProductViewModel Item : ViewModelList) {
            if (Item.getId() == Id) {

                if (Item.getProductImageList() != null && Item.getProductImageList().size() > 0) {
                    Item.getProductImageList().get(0).setFullPath(FullPath);
                } else {
                    if (Item.getProductImageList() == null)
                        Item.setProductImageList(new ArrayList<ProductImageViewModel>());
                    Item.getProductImageList().add(new ProductImageViewModel());
                    Item.getProductImageList().get(0).setFullPath(FullPath);
                }
            }
        }
        notifyDataSetChanged();
        Container.invalidate();
    }

    /**
     * اضافه مودن یک آدرس جدید به لیست
     *
     * @param ViewModel
     */
    public void AddViewModel(ProductViewModel ViewModel) {
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
        try {
            Context.HideLoading();
            if (ServiceMethod == ServiceMethodType.ProductDelete) {
                Feedback<ProductViewModel> FeedBack = (Feedback<ProductViewModel>) Data;

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

