package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.app.Dialog;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Basket.BasketService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Share.BasketActivity;
import ir.rayas.app.citywareclient.ViewModel.Basket.BasketItemViewModel;


public class BasketItemListRecyclerViewAdapter extends RecyclerView.Adapter<BasketItemListRecyclerViewAdapter.ViewHolder> implements IResponseService {

    private List<BasketItemViewModel> ViewModelList = null;
    private RecyclerView Container = null;
    private BasketActivity Context;
    private int Position;
    private int BasketId;
    private int CountQuickItem = 0;

    public BasketItemListRecyclerViewAdapter(BasketActivity context, List<BasketItemViewModel> ViewModel, RecyclerView Container, int BasketId) {
        this.Context = context;
        this.Container = Container;
        this.ViewModelList = ViewModel;
        this.BasketId = BasketId;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextViewPersian BasketItemProductNameTextView;
        TextViewPersian BasketItemQuantityTextView;
        TextViewPersian BasketItemTotalPriceTextView;
        TextViewPersian BasketItemPriceTextView;
        TextViewPersian BasketItemTotalPriceTomanTextView;
        TextViewPersian BasketItemPriceTomanTextView;
        ButtonPersianView OrderItemDeleteButton;
        ImageView ImageBasketItemImageView;
        CardView AddOrMinBasketItemQuantityImageView;


        public ViewHolder(View v) {
            super(v);
            BasketItemProductNameTextView = v.findViewById(R.id.BasketItemProductNameTextView);
            BasketItemQuantityTextView = v.findViewById(R.id.BasketItemQuantityTextView);
            BasketItemTotalPriceTextView = v.findViewById(R.id.BasketItemTotalPriceTextView);
            BasketItemPriceTextView = v.findViewById(R.id.BasketItemPriceTextView);
            OrderItemDeleteButton = v.findViewById(R.id.OrderItemDeleteButton);
            ImageBasketItemImageView = v.findViewById(R.id.ImageBasketItemImageView);
            BasketItemPriceTomanTextView = v.findViewById(R.id.BasketItemPriceTomanTextView);
            BasketItemTotalPriceTomanTextView = v.findViewById(R.id.BasketItemTotalPriceTomanTextView);
            AddOrMinBasketItemQuantityImageView = v.findViewById(R.id.AddOrMinBasketItemQuantityImageView);
        }


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_basket_list_item, parent, false);
        return new ViewHolder(CurrentView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        holder.BasketItemQuantityTextView.setText(ViewModelList.get(position).getValue() + "");
        holder.BasketItemProductNameTextView.setText(ViewModelList.get(position).getProductName());


        if (ViewModelList.get(position).getPrice() <= 0) {

            holder.BasketItemPriceTomanTextView.setVisibility(View.GONE);
            holder.BasketItemTotalPriceTomanTextView.setVisibility(View.GONE);
            holder.BasketItemPriceTextView.setText(Context.getResources().getString(R.string.unknown));
            holder.BasketItemTotalPriceTextView.setText(Context.getResources().getString(R.string.unknown));

            CountQuickItem = CountQuickItem + 1;

        } else {

            holder.BasketItemPriceTomanTextView.setVisibility(View.VISIBLE);
            holder.BasketItemTotalPriceTomanTextView.setVisibility(View.VISIBLE);
            holder.BasketItemPriceTextView.setText(Utility.GetIntegerNumberWithComma(ViewModelList.get(position).getPrice()));
            double TotalPrice = ViewModelList.get(position).getPrice() * ViewModelList.get(position).getValue();
            holder.BasketItemTotalPriceTextView.setText(Utility.GetIntegerNumberWithComma(TotalPrice));

        }


        if (!ViewModelList.get(position).getImagePathUrl().equals("")) {
            LayoutUtility.LoadImageWithGlide(Context, ViewModelList.get(position).getImagePathUrl(), holder.ImageBasketItemImageView);
        } else {
            holder.ImageBasketItemImageView.setImageResource(R.drawable.image_default);
        }

        holder.OrderItemDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Position = position;
                Context.ShowLoadingProgressBar();
                Context.setRetryType(1);

                if (ViewModelList.size() > 1) {
                    BasketService basketService = new BasketService(BasketItemListRecyclerViewAdapter.this);
                    basketService.DeleteItemByItemId(ViewModelList.get(position).getId());
                } else {
                    BasketService basketService = new BasketService(BasketItemListRecyclerViewAdapter.this);
                    basketService.DeleteBasket(BasketId);
                }
            }
        });

        holder.AddOrMinBasketItemQuantityImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Position = position;
                ShowDialogBasketOrder(ViewModelList.get(position).getValue(), ViewModelList.get(position).getId());
            }
        });


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


    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.BasketDeleteItemByItemId) {
                Feedback<BasketItemViewModel> FeedBack = (Feedback<BasketItemViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.DeletedSuccessful.getId()) {

                    double Quantity = ViewModelList.get(Position).getValue();
                    double Price = ViewModelList.get(Position).getPrice();
                    double TotalPrice = Quantity * Price;

                    Context.basketSummeryViewModel.setTotalPrice(Context.basketSummeryViewModel.getTotalPrice() - TotalPrice);
                    Context.basketSummeryViewModel.setBasketCount(Context.basketSummeryViewModel.getBasketCount() - 1);

                    ViewModelList.remove(Position);
                    notifyDataSetChanged();
                    Container.invalidate();


                    BasketItemViewModel basketItemViewModel = FeedBack.getValue();
                    if (basketItemViewModel.getPrice() > 0) {
                        CountQuickItem = CountQuickItem;
                        //  Context.basketSummeryViewModel.setQuickItem(false);
                    } else {
                        CountQuickItem = CountQuickItem - 1;
                    }

                    if (CountQuickItem == 0) {
                        Context.basketSummeryViewModel.setQuickItem(false);
                    } else {
                        Context.basketSummeryViewModel.setQuickItem(true);
                    }

                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.BasketEditQuantityByItemId) {
                Feedback<BasketItemViewModel> FeedBack = (Feedback<BasketItemViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.UpdatedSuccessful.getId()) {
                    if (FeedBack.getValue() != null) {

                        double Quantity = ViewModelList.get(Position).getValue();
                        double Price = ViewModelList.get(Position).getPrice();

                        double NewQuantity = FeedBack.getValue().getValue();
                        double FinalPrice = 0;

                        if (NewQuantity > Quantity) {
                            FinalPrice = (NewQuantity - Quantity) * Price;
                            Context.basketSummeryViewModel.setTotalPrice(Context.basketSummeryViewModel.getTotalPrice() + FinalPrice);
                        } else if (NewQuantity == Quantity) {
                            Context.basketSummeryViewModel.setTotalPrice(Context.basketSummeryViewModel.getTotalPrice());
                        } else {
                            FinalPrice = (Quantity - NewQuantity) * Price;
                            Context.basketSummeryViewModel.setTotalPrice(Context.basketSummeryViewModel.getTotalPrice() - FinalPrice);
                        }

                        BasketItemViewModel basketItemViewModel = ViewModelList.get(Position);
                        basketItemViewModel.setValue(FeedBack.getValue().getValue());
                        ViewModelList.remove(Position);
                        ViewModelList.add(Position, basketItemViewModel);
                        notifyDataSetChanged();
                        Container.invalidate();
                    } else {
                        Context.ShowToast(Context.getResources().getString(R.string.retry), Toast.LENGTH_LONG, MessageType.Warning);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.BasketDelete) {
                Feedback<BasketItemViewModel> FeedBack = (Feedback<BasketItemViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.DeletedSuccessful.getId()) {
                    Context.finish();
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


    private void ShowDialogBasketOrder(final double ItemQuantity, final int ItemId) {
        final Dialog DialogOrder = new Dialog(Context);
        DialogOrder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogOrder.setContentView(R.layout.dialog_order);
        DialogOrder.setCanceledOnTouchOutside(true);

        Typeface typeface = Typeface.createFromAsset(Context.getAssets(), "fonts/iransanslight.ttf");

        final EditText DialogCustomerQuantityEditText = DialogOrder.findViewById(R.id.CustomerQuantityEditText);
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
                    Context.ShowToast(Context.getResources().getString(R.string.please_enter_order_quantity), Toast.LENGTH_LONG, MessageType.Error);

                } else {
                    Context.ShowLoadingProgressBar();
                    BasketService BasketService = new BasketService(BasketItemListRecyclerViewAdapter.this);
                    BasketService.EditQuantityByItemId(ItemId, Double.valueOf(DialogCustomerQuantityEditText.getText().toString()));
                    DialogOrder.dismiss();

                }
            }
        });
        DialogOrder.show();
    }

}
