package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.RegionRepository;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Business.BusinessService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.View.Share.MapActivity;
import ir.rayas.app.citywareclient.View.UserProfileChildren.BusinessSetActivity;
import ir.rayas.app.citywareclient.View.Master.UserProfileActivity;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessViewModel;


public class BusinessListRecyclerViewAdapter extends RecyclerView.Adapter<BusinessListRecyclerViewAdapter.ViewHolder> implements IResponseService {

    private List<BusinessViewModel> ViewModelList = null;
    private RecyclerView Container = null;
    private UserProfileActivity Context;
    private int Position;

    private RegionRepository regionRepository = new RegionRepository();

    public BusinessListRecyclerViewAdapter(UserProfileActivity context, List<BusinessViewModel> ViewModel, RecyclerView Container) {
        this.Context = context;
        this.Container = Container;
        this.ViewModelList = ViewModel;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
         TextViewPersian UserDeleteBusinessIconTextView;
         ButtonPersianView UserEditBusinessButton;
         TextViewPersian UserBusinessTitleTextView;
         TextViewPersian JobTextViewUserBusiness;
         TextViewPersian KeyWordTextViewUserBusiness;
         TextViewPersian ConfirmStateTextViewUserBusiness;
         TextViewPersian ShowMapBusinessIconTextViewUserBusiness;
         TextViewPersian ConfirmStateTitleTextViewUserBusiness;
         TextViewPersian AddressTextViewUserBusiness;
         TextViewPersian ShowMapBusinessTextViewUserBusiness;
         LinearLayout ShowMapLinearLayoutUserBusiness;
         SwitchCompat IsOpenSwitchUserBusiness;
         SwitchCompat HasDeliverySwitchUserBusiness;

        public ViewHolder(View v) {
            super(v);
            UserDeleteBusinessIconTextView = v.findViewById(R.id.UserDeleteBusinessIconTextView);
            UserEditBusinessButton = v.findViewById(R.id.UserEditBusinessIconButton);
            UserBusinessTitleTextView = v.findViewById(R.id.UserBusinessTitleTextView);
            JobTextViewUserBusiness = v.findViewById(R.id.JobTextViewUserBusiness);
            KeyWordTextViewUserBusiness = v.findViewById(R.id.KeyWordTextViewUserBusiness);
            ConfirmStateTextViewUserBusiness = v.findViewById(R.id.ConfirmStateTextViewUserBusiness);
            ShowMapBusinessIconTextViewUserBusiness = v.findViewById(R.id.ShowMapBusinessIconTextViewUserBusiness);
            ShowMapLinearLayoutUserBusiness = v.findViewById(R.id.ShowMapLinearLayoutUserBusiness);
            IsOpenSwitchUserBusiness = v.findViewById(R.id.IsOpenSwitchUserBusiness);
            HasDeliverySwitchUserBusiness = v.findViewById(R.id.HasDeliverySwitchUserBusiness);
            ConfirmStateTitleTextViewUserBusiness = v.findViewById(R.id.ConfirmStateTitleTextViewUserBusiness);
            AddressTextViewUserBusiness = v.findViewById(R.id.AddressTextViewUserBusiness);
            ShowMapBusinessTextViewUserBusiness = v.findViewById(R.id.ShowMapBusinessTextViewUserBusiness);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_user_business, parent, false);
        return new ViewHolder(CurrentView);
    }


    @SuppressLint({"NewApi", "SetTextI18n"})
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.UserBusinessTitleTextView.setText(ViewModelList.get(position).getTitle());
        holder.JobTextViewUserBusiness.setText(ViewModelList.get(position).getJobTitle());
        holder.KeyWordTextViewUserBusiness.setText(ViewModelList.get(position).getKeyword());
        holder.ConfirmStateTextViewUserBusiness.setText(ViewModelList.get(position).getConfirmTypeName());
        holder.IsOpenSwitchUserBusiness.setChecked(ViewModelList.get(position).isOpen());
        holder.HasDeliverySwitchUserBusiness.setChecked(ViewModelList.get(position).isHasDelivery());

        holder.UserDeleteBusinessIconTextView.setTypeface(Font.MasterIcon);
        holder.UserDeleteBusinessIconTextView.setText("\uf014");

        holder.ShowMapBusinessIconTextViewUserBusiness.setTypeface(Font.MasterIcon);
        holder.ShowMapBusinessIconTextViewUserBusiness.setText("\uf041");

//        String Address = ViewModelList.get(position).getRegionName() + " - " + ViewModelList.get(position).getAddress();

        holder.AddressTextViewUserBusiness.setText(regionRepository.GetFullName(ViewModelList.get(position).getRegionId())+ " - " + ViewModelList.get(position).getAddress());

        if (ViewModelList.get(position).getLatitude() > 0 && ViewModelList.get(position).getLongitude() > 0) {
            holder.ShowMapLinearLayoutUserBusiness.setVisibility(View.VISIBLE);
        } else {
            holder.ShowMapLinearLayoutUserBusiness.setVisibility(View.GONE);
        }

        holder.HasDeliverySwitchUserBusiness.setClickable(false);
        holder.IsOpenSwitchUserBusiness.setClickable(false);


        if (ViewModelList.get(position).isActive()) {
            holder.UserEditBusinessButton.setEnabled(true);
            holder.UserDeleteBusinessIconTextView.setEnabled(true);
            holder.UserDeleteBusinessIconTextView.setClickable(true);
            holder.UserDeleteBusinessIconTextView.setTextColor(Context.getResources().getColor(R.color.FontRedColor));

            holder.ShowMapLinearLayoutUserBusiness.setEnabled(true);
            holder.ShowMapLinearLayoutUserBusiness.setClickable(true);
            holder.ShowMapBusinessTextViewUserBusiness.setTextColor(Context.getResources().getColor(R.color.FontSemiDarkThemeColor));
            holder.ShowMapBusinessIconTextViewUserBusiness.setTextColor(Context.getResources().getColor(R.color.FontSemiDarkThemeColor));

            holder.HasDeliverySwitchUserBusiness.setEnabled(true);
            holder.IsOpenSwitchUserBusiness.setEnabled(true);



            if (ViewModelList.get(position).getConfirmTypeId() == 3) {
                holder.ConfirmStateTitleTextViewUserBusiness.setTextColor(Context.getResources().getColor(R.color.FontGreenColor));
                holder.ConfirmStateTextViewUserBusiness.setTextColor(Context.getResources().getColor(R.color.FontGreenColor));
            } else if (ViewModelList.get(position).getConfirmTypeId() == 4) {
                holder.ConfirmStateTitleTextViewUserBusiness.setTextColor(Context.getResources().getColor(R.color.FontRedColor));
                holder.ConfirmStateTextViewUserBusiness.setTextColor(Context.getResources().getColor(R.color.FontRedColor));
            }
            else{
                holder.ConfirmStateTitleTextViewUserBusiness.setTextColor(Context.getResources().getColor(R.color.FontSemiBlackColor));
                holder.ConfirmStateTextViewUserBusiness.setTextColor(Context.getResources().getColor(R.color.FontBlackColor));
            }

            holder.UserDeleteBusinessIconTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Position = position;
                    ShowBusinessDeleteDialog(position);

                }
            });

            holder.UserEditBusinessButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent EditBusinessIconIntent = Context.NewIntent(BusinessSetActivity.class);
                    EditBusinessIconIntent.putExtra("SetBusiness", "Edit");
                    EditBusinessIconIntent.putExtra("BusinessId", ViewModelList.get(position).getId());
                    //2 Edit - you can find Add in UserAddressFragment
                    Context.startActivity(EditBusinessIconIntent);

                }
            });

            holder.ShowMapLinearLayoutUserBusiness.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent MapIntent = Context.NewIntent(MapActivity.class);
                    MapIntent.putExtra("Latitude", ViewModelList.get(position).getLatitude());
                    MapIntent.putExtra("Longitude", ViewModelList.get(position).getLongitude());
                    MapIntent.putExtra("Going", 2);
                    Context.startActivity(MapIntent);
                }
            });
        } else {

            holder.UserEditBusinessButton.setEnabled(false);
            holder.UserDeleteBusinessIconTextView.setEnabled(false);
            holder.UserDeleteBusinessIconTextView.setClickable(false);
            holder.UserDeleteBusinessIconTextView.setTextColor(LayoutUtility.GetColorFromResource(Context,R.color.FontSemiBlackColor));


            holder.HasDeliverySwitchUserBusiness.setEnabled(false);
            holder.IsOpenSwitchUserBusiness.setEnabled(false);

            holder.ShowMapLinearLayoutUserBusiness.setEnabled(false);
            holder.ShowMapLinearLayoutUserBusiness.setClickable(false);
            holder.ShowMapBusinessTextViewUserBusiness.setTextColor(LayoutUtility.GetColorFromResource(Context,R.color.FontSemiBlackColor));
            holder.ShowMapBusinessIconTextViewUserBusiness.setTextColor(LayoutUtility.GetColorFromResource(Context,R.color.FontSemiBlackColor));
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

    private void ShowBusinessDeleteDialog(final int Position) {

        final Dialog BusinessDeleteDialog = new Dialog(Context);
        BusinessDeleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        BusinessDeleteDialog.setContentView(R.layout.dialog_yes_no_question);
        ButtonPersianView DialogBusinessDeleteOkButton = BusinessDeleteDialog.findViewById(R.id.DialogYesNoQuestionOkButton);
        ButtonPersianView DialogBusinessDeleteCancelButton = BusinessDeleteDialog.findViewById(R.id.DialogYesNoQuestionCancelButton);

        DialogBusinessDeleteOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusinessDeleteDialog.dismiss();

                Context.setRetryType(1);
                Context.ShowLoadingProgressBar();
                BusinessService userBusinessService = new BusinessService(BusinessListRecyclerViewAdapter.this);
                userBusinessService.Delete(ViewModelList.get(Position).getId());

            }
        });

        DialogBusinessDeleteCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusinessDeleteDialog.dismiss();
            }
        });

        BusinessDeleteDialog.show();

    }


    /**
     * ویرایش اطلاعات یک آدرس در لیست
     *
     * @param ViewModel
     */
    public void SetViewModel(BusinessViewModel ViewModel) {
        if (ViewModel != null && ViewModelList != null && ViewModelList.size() > 0) {
            for (BusinessViewModel Item : ViewModelList) {
                if (Item.getId() == ViewModel.getId()) {
                    Item.setTitle(ViewModel.getTitle());
                    Item.setJobTitle(ViewModel.getJobTitle());
                    Item.setOpen(ViewModel.isOpen());
                    Item.setHasDelivery(ViewModel.isHasDelivery());
                    Item.setKeyword(ViewModel.getKeyword());
                    Item.setConfirmTypeId(ViewModel.getConfirmTypeId());
                    Item.setConfirmTypeName(ViewModel.getConfirmTypeName());
                    Item.setLatitude(ViewModel.getLatitude());
                    Item.setLongitude(ViewModel.getLongitude());
                    Item.setAddress(ViewModel.getAddress());
                    Item.setRegionName(ViewModel.getRegionName());
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
    public void AddViewModel(BusinessViewModel ViewModel) {
        if (ViewModel != null) {
            if (ViewModelList == null)
                ViewModelList = new ArrayList<>();
            ViewModelList.add(ViewModel);
            notifyDataSetChanged();
            Container.invalidate();
        }
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
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.BusinessDelete) {
                Feedback<BusinessViewModel> FeedBack = (Feedback<BusinessViewModel>) Data;

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