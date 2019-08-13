package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.MyClickListener;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.Service.Basket.BasketService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowProductListActivity;
import ir.rayas.app.citywareclient.View.Share.BasketActivity;
import ir.rayas.app.citywareclient.View.Share.BasketListActivity;
import ir.rayas.app.citywareclient.ViewModel.Basket.BasketItemViewModel;
import ir.rayas.app.citywareclient.ViewModel.Basket.StandardOrderItemViewModel;
import ir.rayas.app.citywareclient.ViewModel.Order.ProductImageViewModel;
import ir.rayas.app.citywareclient.ViewModel.Order.ProductViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;


public class ShowProductListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IResponseService {

    private ShowProductListActivity Context;
    private RecyclerView Container = null;
    private List<ProductViewModel> ViewModelList = null;
    private boolean IsGrid;

    private String ProductName = "";
    private int ProductId = 0;

    private MyClickListener myClickListener;
    private EditText DialogCustomerQuantityEditText = null;


    public ShowProductListRecyclerViewAdapter(ShowProductListActivity Context, boolean IsGrid, List<ProductViewModel> ProductList, RecyclerView Container) {
        this.ViewModelList = ProductList;
        this.Context = Context;
        this.IsGrid = IsGrid;
        this.Container = Container;
    }

    /**
     * اضافه مودن لیست جدید
     *
     * @param ViewModel
     */
    public List<ProductViewModel> AddViewModelList(List<ProductViewModel> ViewModel) {
        if (ViewModel != null) {
            if (ViewModelList == null)
                ViewModelList = new ArrayList<>();
            ViewModelList.addAll(ViewModel);
            notifyDataSetChanged();
            Container.invalidate();
        }
        return ViewModelList;
    }

    /**
     * جایگزین نمودن لیست جدید
     *
     * @param ViewModelList
     */
    public List<ProductViewModel> SetViewModelList(List<ProductViewModel> ViewModelList) {
        this.ViewModelList = new ArrayList<>();
        this.ViewModelList.addAll(ViewModelList);
        notifyDataSetChanged();
        Container.invalidate();

        return this.ViewModelList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (IsGrid) {
            view = LayoutInflater.from(Context).inflate(R.layout.recycler_view_show_product_list_one_column, parent, false);
        } else {
            view = LayoutInflater.from(Context).inflate(R.layout.recycler_view_show_product_list_two_column, parent, false);
        }

        return new ShowProductListViewHolder(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ShowProductListViewHolder viewHolder = (ShowProductListViewHolder) holder;

        viewHolder.ProductNameTextView.setText(ViewModelList.get(position).getName());
        viewHolder.AbstractDescriptionTextView.setText(ViewModelList.get(position).getAbstractOfDescription());
        viewHolder.PriceTextView.setText(String.valueOf(Utility.GetIntegerNumberWithComma((long) ViewModelList.get(position).getPrice())));


        int ScreenWidth;
        if (IsGrid) {
            ScreenWidth = LayoutUtility.GetWidthAccordingToScreen(Context, 3) - 30;
        } else {
            ScreenWidth = LayoutUtility.GetWidthAccordingToScreen(Context, 2) - 30;
        }

        viewHolder.ImageProductImageView.getLayoutParams().width = ScreenWidth;
        viewHolder.ImageProductImageView.getLayoutParams().height = ScreenWidth;

        List<ProductImageViewModel> ProductImageList = new ArrayList<>();
        ProductImageList = ViewModelList.get(position).getProductImageList();

        String ProductImage = "";
        if (ProductImageList.size() > 0) {
            for (int i = 0; i < ProductImageList.size(); i++) {
                if (!ProductImageList.get(i).getFullPath().equals("")) {
                    if (ProductImageList.get(i).getFullPath().contains("~")) {
                        ProductImage = ProductImageList.get(i).getFullPath().replace("~", DefaultConstant.BaseUrlWebService);
                    } else {
                        ProductImage = ProductImageList.get(i).getFullPath();
                    }
                    break;
                }
            }
        }

        if (!ProductImage.equals("")) {
            LayoutUtility.LoadImageWithGlide(Context, ProductImage, viewHolder.ImageProductImageView, ScreenWidth, ScreenWidth);
        } else {
            viewHolder.ImageProductImageView.setImageResource(R.drawable.image_default);
        }

        viewHolder.ShoppingCartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductName = ViewModelList.get(position).getName();
                ProductId = ViewModelList.get(position).getId();
                BasketService BasketService = new BasketService(ShowProductListRecyclerViewAdapter.this);
                BasketService.GetQuantityByProductId(ProductId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ViewModelList == null ? 0 : ViewModelList.size();
    }


    public class ShowProductListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

         TextViewPersian ProductNameTextView;
         TextViewPersian AbstractDescriptionTextView;
         TextViewPersian PriceTextView;
         ImageView ImageProductImageView;
         ImageView ShoppingCartImageView;


         ShowProductListViewHolder(View v) {
            super(v);
            ProductNameTextView = v.findViewById(R.id.ProductNameTextView);
            AbstractDescriptionTextView = v.findViewById(R.id.AbstractDescriptionTextView);
            PriceTextView = v.findViewById(R.id.PriceTextView);
            ImageProductImageView = v.findViewById(R.id.ImageProductImageView);
            ShoppingCartImageView = v.findViewById(R.id.ShoppingCartImageView);

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

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();

        try {
            if (ServiceMethod == ServiceMethodType.QuantityByProductIdGet) {
                Feedback<Double> FeedBack = (Feedback<Double>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    ShowDialogBasketOrder(FeedBack.getValue());
                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    ShowDialogBasketOrder(0);
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.BasketAdd) {
                Feedback<Double> FeedBack = (Feedback<Double>) Data;

                if (FeedBack.getStatus() == FeedbackType.RegisteredSuccessful.getId()) {
                    String Message = Context.getResources().getString(R.string.product) + " " + ProductName + " " + Context.getResources().getString(R.string.product_added_to_basket);
                    ShowDialogContinueShoppingAndShowBasketOrder(Message);
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.BasketEditQuantityByProductId) {
                Feedback<BasketItemViewModel> FeedBack = (Feedback<BasketItemViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.UpdatedSuccessful.getId()) {

                    String Message = Context.getResources().getString(R.string.order_quantity_product) + " " + ProductName + " " + Context.getResources().getString(R.string.product_was_edited_in_the_basket);
                    ShowDialogContinueShoppingAndShowBasketOrder(Message);
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            }
        } catch (Exception e) {
            Context.HideLoading();
            Context.ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
    }

    private void ShowDialogBasketOrder(final double ItemQuantity) {
        final Dialog DialogOrder = new Dialog(Context);
        DialogOrder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogOrder.setContentView(R.layout.dialog_order);
        DialogOrder.setCanceledOnTouchOutside(true);

        Typeface typeface = Typeface.createFromAsset(Context.getAssets(), "fonts/iransanslight.ttf");

        DialogCustomerQuantityEditText = DialogOrder.findViewById(R.id.CustomerQuantityEditText);
        ButtonPersianView DialogCustomerQuantityAcceptButton = DialogOrder.findViewById(R.id.CustomerQuantityAcceptButton);

        DialogCustomerQuantityEditText.setTypeface(typeface);

        if (ItemQuantity > 0)
            DialogCustomerQuantityEditText.setText(String.valueOf(ItemQuantity));
        ImageView DialogCustomerQuantityAddImage = DialogOrder.findViewById(R.id.CustomerQuantityAddImage);
        ImageView DialogCustomerQuantitySubtractImage = DialogOrder.findViewById(R.id.CustomerQuantitySubtractImage);

        DialogCustomerQuantityAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((DialogCustomerQuantityEditText.getText() == null) || ("".equals(DialogCustomerQuantityEditText.getText().toString())) || (1 > Double.valueOf(DialogCustomerQuantityEditText.getText().toString()))) {
                    DialogCustomerQuantityEditText.setText("1");
                } else {
                    double TempCount = Double.valueOf(DialogCustomerQuantityEditText.getText().toString());
                    if (TempCount == 999) {

                    } else {
                        TempCount++;
                        DialogCustomerQuantityEditText.setText(String.valueOf(TempCount));
                    }

                }
            }
        });

        DialogCustomerQuantitySubtractImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((DialogCustomerQuantityEditText.getText() == null) || ("".equals(DialogCustomerQuantityEditText.getText().toString())) || (1 > Double.valueOf(DialogCustomerQuantityEditText.getText().toString()))) {
                    DialogCustomerQuantityEditText.setText("1");
                } else {
                    double TempCount = Double.valueOf(DialogCustomerQuantityEditText.getText().toString());
                    TempCount--;
                    if (TempCount < 1) {

                    } else {
                        DialogCustomerQuantityEditText.setText(String.valueOf(TempCount));
                    }
                }
            }
        });


        DialogCustomerQuantityAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((DialogCustomerQuantityEditText.getText() == null) || ("".equals(DialogCustomerQuantityEditText.getText().toString())) || (1 > Double.valueOf(DialogCustomerQuantityEditText.getText().toString()))) {
                    Context.ShowToast(Context.getResources().getString(R.string.please_enter_order_quantity), Toast.LENGTH_LONG, MessageType.Warning);

                } else {
                    Context.ShowLoadingProgressBar();
                    BasketService BasketService = new BasketService(ShowProductListRecyclerViewAdapter.this);
                    if (ItemQuantity <= 0) {
                        BasketService.Add(MadeViewModel());
                    } else {
                        BasketService.EditQuantityByProductId(ProductId, Double.valueOf(DialogCustomerQuantityEditText.getText().toString()));
                    }
                    DialogOrder.dismiss();

                }
            }
        });
        DialogOrder.show();


    }

    private void ShowDialogContinueShoppingAndShowBasketOrder(String Message) {

        final Dialog DialogOrder = new Dialog(Context);
        DialogOrder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogOrder.setContentView(R.layout.dialog_continue_shopping_show_basket);
        DialogOrder.setCanceledOnTouchOutside(true);

        TextViewPersian DialogDescriptionTextView = DialogOrder.findViewById(R.id.DialogDescriptionTextView);
        ButtonPersianView DialogContinueShoppingProcessButton = DialogOrder.findViewById(R.id.DialogContinueShoppingProcessButton);
        ButtonPersianView DialogShowBasketButton = DialogOrder.findViewById(R.id.DialogShowBasketButton);

        DialogDescriptionTextView.setText(Message);

        DialogContinueShoppingProcessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogOrder.dismiss();
            }
        });

        DialogShowBasketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BasketIntent = new Intent(Context, BasketListActivity.class);
                Context.startActivity(BasketIntent);
            }
        });
        DialogOrder.show();


    }

    private StandardOrderItemViewModel MadeViewModel() {
        StandardOrderItemViewModel ViewModel = new StandardOrderItemViewModel();
        try {
            AccountRepository AccountRepository = new AccountRepository(Context);
            AccountViewModel AccountModel = AccountRepository.getAccount();
            ViewModel.setUserId(AccountModel.getUser().getId());

            ViewModel.setValue(Double.valueOf(DialogCustomerQuantityEditText.getText().toString()));
            ViewModel.setProductId(ProductId);
        } catch (Exception ignored) {
        }
        return ViewModel;
    }


}