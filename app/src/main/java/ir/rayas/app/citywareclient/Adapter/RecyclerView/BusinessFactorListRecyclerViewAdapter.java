package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Factor.BusinessFactorService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.FactorStatus;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Share.BusinessFactorDetailsActivity;
import ir.rayas.app.citywareclient.View.Share.BusinessFactorListActivity;
import ir.rayas.app.citywareclient.View.Share.MapActivity;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorItemViewModel;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorStatusViewModel;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorViewModel;


public class BusinessFactorListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IResponseService {

    private BusinessFactorListActivity Context;
    private RecyclerView Container = null;
    private List<FactorViewModel> ViewModelList = null;


    private int Position;

    private List<FactorStatusViewModel> FactorStatusViewModel = null;


    public BusinessFactorListRecyclerViewAdapter(BusinessFactorListActivity Context, List<FactorViewModel> FactorList, List<FactorStatusViewModel> FactorStatusViewModel, RecyclerView Container) {
        this.ViewModelList = FactorList;
        this.Context = Context;
        this.Container = Container;
        this.FactorStatusViewModel = FactorStatusViewModel;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(Context).inflate(R.layout.recycler_view_business_factor_list, parent, false);
        return new FactorListViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final FactorListViewHolder viewHolder = (FactorListViewHolder) holder;

        viewHolder.UserNameTitleTextView.setText(ViewModelList.get(position).getUserFullName());
        viewHolder.CreateDateBusinessFactorTextView.setText(ViewModelList.get(position).getCreate());
        viewHolder.NumberOfOrderItemsBusinessFactorTextView.setText(String.valueOf(ViewModelList.get(position).getItemList().size()));
        viewHolder.FactorCodeTextView.setText(String.valueOf(ViewModelList.get(position).getId()));
        viewHolder.UserCellPhoneTextView.setText(Context.getResources().getString(R.string.zero) + String.valueOf(ViewModelList.get(position).getUserCellPhone()));

        final String PhoneNumber = viewHolder.UserCellPhoneTextView.getText().toString();

        boolean IsZeroPrice = false;
        int IdFactorStatus = 0;

        for (int i = 0; i < FactorStatusViewModel.size(); i++) {
            if (ViewModelList.get(position).getFactorStatusId() == FactorStatusViewModel.get(i).getId()) {
                viewHolder.StatusBusinessFactorTextView.setText(FactorStatusViewModel.get(i).getTitle());
                IdFactorStatus = FactorStatusViewModel.get(i).getStatus();
            }
        }

        List<FactorItemViewModel> ItemList = ViewModelList.get(position).getItemList();
        for (int i = 0; i < ItemList.size(); i++) {
            if (ItemList.get(i).getPrice() == 0) {
                IsZeroPrice = true;
            }
        }

        if (ViewModelList.get(position).getTotalPrice() < 0) {
            viewHolder.PricePayableBusinessFactorTextView.setText(Context.getResources().getString(R.string.unknown));
        } else {

            double DeliveryCost = ViewModelList.get(position).getDeliveryCost();
            Double PayablePrice;
            if (DeliveryCost <= 0) {
                PayablePrice = ViewModelList.get(position).getTotalPrice();
            } else {
                PayablePrice = ViewModelList.get(position).getTotalPrice() + DeliveryCost;
            }

            if (ViewModelList.get(position).isHasQuickOrder()) {

                viewHolder.PricePayableBusinessFactorTextView.setText(Utility.GetIntegerNumberWithComma(PayablePrice) + " " + Context.getResources().getString(R.string.toman) + " - " + Context.getResources().getString(R.string.price_is_not_definitive));

                viewHolder.DescriptionBusinessFactorTextView.setVisibility(View.VISIBLE);
                viewHolder.DescriptionBusinessFactorTextView.setText(Context.getResources().getString(R.string.in_order_some_unknown_price_items_must_be_aligned_with_customer));
            } else {
                if (IsZeroPrice) {
                    viewHolder.PricePayableBusinessFactorTextView.setText(Utility.GetIntegerNumberWithComma(PayablePrice) + " " + Context.getResources().getString(R.string.toman) + " - " + Context.getResources().getString(R.string.price_is_not_definitive));

                    viewHolder.DescriptionBusinessFactorTextView.setVisibility(View.VISIBLE);
                    viewHolder.DescriptionBusinessFactorTextView.setText(Context.getResources().getString(R.string.in_order_some_unknown_price_items_must_be_aligned_with_customer));
                } else {
                    viewHolder.DescriptionBusinessFactorTextView.setVisibility(View.GONE);
                    viewHolder.PricePayableBusinessFactorTextView.setText(Utility.GetIntegerNumberWithComma(PayablePrice) + " " + Context.getResources().getString(R.string.toman));
                }
            }
        }


        viewHolder.BusinessFactorDeleteIconTextView.setTypeface(Font.MasterIcon);
        viewHolder.BusinessFactorDeleteIconTextView.setText("\uf014");


        viewHolder.BusinessFactorDeleteIconTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Position = position;
                Context.ShowLoadingProgressBar();
                BusinessFactorService businessFactorService = new BusinessFactorService(BusinessFactorListRecyclerViewAdapter.this);
                businessFactorService.Delete(ViewModelList.get(position).getId());
            }
        });


        viewHolder.ShowMapUserAddressIconTextView.setTypeface(Font.MasterIcon);
        viewHolder.ShowMapUserAddressIconTextView.setText("\uf041");

        viewHolder.ShowMapUserAddressLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MapIntent = Context.NewIntent(MapActivity.class);
                MapIntent.putExtra("Latitude", ViewModelList.get(position).getUserLatitude());
                MapIntent.putExtra("Longitude", ViewModelList.get(position).getUserLongitude());
                MapIntent.putExtra("Going", 2);
                Context.startActivity(MapIntent);
            }
        });

        viewHolder.BusinessFactorListRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent UserFactorDetailIntent = Context.NewIntent(BusinessFactorDetailsActivity.class);
                UserFactorDetailIntent.putExtra("FactorId", ViewModelList.get(position).getId());
                Context.startActivity(UserFactorDetailIntent);
            }
        });

        viewHolder.UserCellPhoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "tel:" + PhoneNumber;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                Context.startActivity(intent);
            }
        });

        if (ViewModelList.get(position).getUserAddress() == null || ViewModelList.get(position).getUserAddress().equals("")) {
            viewHolder.UserAddressTextView.setText(Context.getResources().getString(R.string.customer_order_will_be_delivered_to_the_business_address));
            viewHolder.ShowMapUserAddressLinearLayout.setVisibility(View.GONE);
        } else {
            viewHolder.ShowMapUserAddressLinearLayout.setVisibility(View.VISIBLE);
            viewHolder.UserAddressTextView.setText(ViewModelList.get(position).getUserAddress());
        }


        if (IdFactorStatus == FactorStatus.Received.getId() || IdFactorStatus == FactorStatus.CanceledByBusiness.getId() || IdFactorStatus == FactorStatus.CanceledByUser.getId() || IdFactorStatus == FactorStatus.Delivered.getId()){
            viewHolder.BusinessFactorDeleteIconTextView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.BusinessFactorDeleteIconTextView.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return ViewModelList == null ? 0 : ViewModelList.size();
    }


    /**
     * اضافه مودن لیست جدید
     *
     * @param ViewModel
     */
    public void AddViewModelList(List<FactorViewModel> ViewModel) {
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
    public void SetViewModelList(List<FactorViewModel> ViewModel) {
        ViewModelList = new ArrayList<>();
        ViewModelList.addAll(ViewModel);
        notifyDataSetChanged();
        Container.invalidate();
    }


    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.BusinessFactorDelete) {
                Feedback<FactorViewModel> FeedBack = (Feedback<FactorViewModel>) Data;

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


    private class FactorListViewHolder extends RecyclerView.ViewHolder {

         TextViewPersian BusinessFactorDeleteIconTextView;
         TextViewPersian UserNameTitleTextView;
         TextViewPersian CreateDateBusinessFactorTextView;
         TextViewPersian PricePayableBusinessFactorTextView;
         TextViewPersian DescriptionBusinessFactorTextView;
         TextViewPersian NumberOfOrderItemsBusinessFactorTextView;
         TextViewPersian FactorCodeTextView;
         TextViewPersian UserCellPhoneTextView;
         TextViewPersian UserAddressTextView;
         LinearLayout ShowMapUserAddressLinearLayout;
         TextViewPersian ShowMapUserAddressIconTextView;
         TextViewPersian StatusBusinessFactorTextView;
         RelativeLayout BusinessFactorListRelativeLayout;

         FactorListViewHolder(View v) {
            super(v);

            BusinessFactorDeleteIconTextView = v.findViewById(R.id.BusinessFactorDeleteIconTextView);
            UserNameTitleTextView = v.findViewById(R.id.UserNameTitleTextView);
            CreateDateBusinessFactorTextView = v.findViewById(R.id.CreateDateBusinessFactorTextView);
            PricePayableBusinessFactorTextView = v.findViewById(R.id.PricePayableBusinessFactorTextView);
            DescriptionBusinessFactorTextView = v.findViewById(R.id.DescriptionBusinessFactorTextView);
            NumberOfOrderItemsBusinessFactorTextView = v.findViewById(R.id.NumberOfOrderItemsBusinessFactorTextView);
            FactorCodeTextView = v.findViewById(R.id.FactorCodeTextView);
            UserCellPhoneTextView = v.findViewById(R.id.UserCellPhoneTextView);
            UserAddressTextView = v.findViewById(R.id.UserAddressTextView);
            ShowMapUserAddressLinearLayout = v.findViewById(R.id.ShowMapUserAddressLinearLayout);
            ShowMapUserAddressIconTextView = v.findViewById(R.id.ShowMapUserAddressIconTextView);
            StatusBusinessFactorTextView = v.findViewById(R.id.StatusBusinessFactorTextView);
            BusinessFactorListRelativeLayout = v.findViewById(R.id.BusinessFactorListRelativeLayout);

        }
    }


}