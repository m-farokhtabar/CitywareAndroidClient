package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Marketing.MarketingService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.View.MasterChildren.OrderActivity;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowBusinessCommissionActivity;
import ir.rayas.app.citywareclient.ViewModel.Marketing.BusinessCommissionAndDiscountViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.MarketingBusinessViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.Marketing_FastCustomerFactorViewModel;


public class NewSuggestionBusinessCommissionRecyclerViewAdapter extends RecyclerView.Adapter<NewSuggestionBusinessCommissionRecyclerViewAdapter.ViewHolder> implements IResponseService {

    private List<MarketingBusinessViewModel> ViewModelList = null;
    private ShowBusinessCommissionActivity Context;
    private RecyclerView Container = null;
    private int Position = -1;

    public NewSuggestionBusinessCommissionRecyclerViewAdapter(ShowBusinessCommissionActivity context, List<MarketingBusinessViewModel> ViewModel, RecyclerView Container) {
        this.Context = context;
        this.ViewModelList = ViewModel;
        this.Container = Container;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextViewPersian FullNameTextView;
        TextViewPersian TicketNumberTextView;
        TextViewPersian ExpireDateTextView;
        RelativeLayout DiscountContainerRelativeLayout;
        ButtonPersianView SaveOrderButton;
        ButtonPersianView SaveQuickOrderButton;
        LinearLayout ExpireDateLinearLayout;
        LinearLayout UseDateLinearLayout;


        ViewHolder(View v) {
            super(v);
            DiscountContainerRelativeLayout = v.findViewById(R.id.DiscountContainerRelativeLayout);
            SaveOrderButton = v.findViewById(R.id.SaveOrderButton);
            FullNameTextView = v.findViewById(R.id.FullNameTextView);
            TicketNumberTextView = v.findViewById(R.id.TicketNumberTextView);
            ExpireDateTextView = v.findViewById(R.id.ExpireDateTextView);
            ExpireDateLinearLayout = v.findViewById(R.id.ExpireDateLinearLayout);
            UseDateLinearLayout = v.findViewById(R.id.UseDateLinearLayout);
            SaveQuickOrderButton = v.findViewById(R.id.SaveQuickOrderButton);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_business_customer_search, parent, false);
        return new ViewHolder(CurrentView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final BusinessCommissionAndDiscountViewModel businessCommissionAndDiscountViewModel;
        Gson gson = new Gson();
        Type listType = new TypeToken<BusinessCommissionAndDiscountViewModel>() {
        }.getType();
        businessCommissionAndDiscountViewModel = gson.fromJson(ViewModelList.get(position).getPercents(), listType);

        holder.FullNameTextView.setText(ViewModelList.get(position).getCustomerFullName());
        holder.TicketNumberTextView.setText(ViewModelList.get(position).getTicket());

        holder.ExpireDateTextView.setText(ViewModelList.get(position).getTicketValidity());

        holder.SaveOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent OrderIntent = Context.NewIntent(OrderActivity.class);
                OrderIntent.putExtra("BusinessId", businessCommissionAndDiscountViewModel.getBusinessId());
                OrderIntent.putExtra("Percents", ViewModelList.get(position).getPercents());
                OrderIntent.putExtra("Ticket", ViewModelList.get(position).getTicket());
                OrderIntent.putExtra("MarketerId", ViewModelList.get(position).getId());
                OrderIntent.putExtra("Position", position);
                Context.startActivity(OrderIntent);

            }
        });

        holder.SaveQuickOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Position = position;
                ShowSaveQuickOrderDialog(ViewModelList.get(position), businessCommissionAndDiscountViewModel);
            }
        });


    }

    /**
     * اضافه مودن لیست جدید
     *
     * @param ViewModel
     */
    public void AddViewModelList(List<MarketingBusinessViewModel> ViewModel) {
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
    public void SetViewModelList(List<MarketingBusinessViewModel> ViewModel) {
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

    public void DeleteViewModeList(int Position) {
        ViewModelList.remove(Position);
        notifyDataSetChanged();
        Container.invalidate();

        Context.ShowViewMessageEmpty(ViewModelList.size());
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

    private void ShowSaveQuickOrderDialog(final MarketingBusinessViewModel marketingBusinessViewModel, final BusinessCommissionAndDiscountViewModel businessCommissionAndDiscountViewModel) {

        final Dialog ShowSaveQuickOrderDialog = new Dialog(Context);
        ShowSaveQuickOrderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ShowSaveQuickOrderDialog.setCanceledOnTouchOutside(false);
        ShowSaveQuickOrderDialog.setContentView(R.layout.dialog_save_quick_order);

        TextViewPersian HeaderColorDialog = ShowSaveQuickOrderDialog.findViewById(R.id.HeaderColorDialog);
        HeaderColorDialog.getLayoutParams().width = LayoutUtility.GetWidthAccordingToScreen(Context, 1);

        final EditText CommissionPriceTextView = ShowSaveQuickOrderDialog.findViewById(R.id.CommissionPriceTextView);
        ButtonPersianView DialogFastOrderCancelButton = ShowSaveQuickOrderDialog.findViewById(R.id.DialogFastOrderCancelButton);
        ButtonPersianView DialogFastOrderOkButton = ShowSaveQuickOrderDialog.findViewById(R.id.DialogFastOrderOkButton);

        Typeface typeface = Typeface.createFromAsset(Context.getAssets(), "fonts/iransanslight.ttf");
        CommissionPriceTextView.setTypeface(typeface);

        DialogFastOrderCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowSaveQuickOrderDialog.dismiss();
            }
        });

        DialogFastOrderOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommissionPriceTextView.getText().toString().equals("")) {
                    Context.ShowToast(Context.getResources().getString(R.string.please_enter_commission_total), Toast.LENGTH_LONG, MessageType.Warning);

                } else if (Integer.parseInt(CommissionPriceTextView.getText().toString()) == 0) {
                    Context.ShowToast(Context.getResources().getString(R.string.please_enter_commission_total), Toast.LENGTH_LONG, MessageType.Warning);

                }/* else if (Double.parseDouble(CommissionPriceTextView.getText().toString()) == 0.0) {
                    Context.ShowToast(Context.getResources().getString(R.string.please_enter_commission_total), Toast.LENGTH_LONG, MessageType.Warning);

                }*/ else {
                    Marketing_FastCustomerFactorViewModel marketing_fastCustomerFactorViewModel = new Marketing_FastCustomerFactorViewModel();
                    marketing_fastCustomerFactorViewModel.setBusinessId(businessCommissionAndDiscountViewModel.getBusinessId());
                    marketing_fastCustomerFactorViewModel.setCommissionPrice(Double.parseDouble(CommissionPriceTextView.getText().toString()));
                    marketing_fastCustomerFactorViewModel.setMarketingId(marketingBusinessViewModel.getId());
                    marketing_fastCustomerFactorViewModel.setTicket(marketingBusinessViewModel.getTicket());

                    Context.ShowLoadingProgressBar();
                    MarketingService service = new MarketingService(NewSuggestionBusinessCommissionRecyclerViewAdapter.this);
                    service.AddFastCustomerFactor(marketing_fastCustomerFactorViewModel);
                    ShowSaveQuickOrderDialog.dismiss();
                }
            }
        });

        ShowSaveQuickOrderDialog.show();
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.AddCustomerFastFactorAdd) {
                Feedback<Boolean> FeedBack = (Feedback<Boolean>) Data;

                if (FeedBack.getStatus() == FeedbackType.RegisteredSuccessful.getId()) {

                    if (FeedBack.getValue() != null) {
                        if (FeedBack.getValue()) {
                            Context.ShowToast(Context.getResources().getString(R.string.you_order_submit_successful), Toast.LENGTH_LONG, MessageType.Info);
                            Context.SetFastOrder(Position);
                            Position = -1;
                            // onBackPressed();
                        } else {
                            Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                        }
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
            Context. ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
    }

}
