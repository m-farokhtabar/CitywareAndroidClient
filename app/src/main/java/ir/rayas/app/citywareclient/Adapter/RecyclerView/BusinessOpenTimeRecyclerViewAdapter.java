package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Business.BusinessOpenTimeService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.UserProfileChildren.BusinessOpenTimeSetActivity;
import ir.rayas.app.citywareclient.View.UserProfileChildren.BusinessSetActivity;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessOpenTimeViewModel;

/**
 * Created by Hajar on 8/24/2018.
 */

public class BusinessOpenTimeRecyclerViewAdapter extends RecyclerView.Adapter<BusinessOpenTimeRecyclerViewAdapter.ViewHolder> implements IResponseService {

    private List<BusinessOpenTimeViewModel> ViewModelList = null;
    private RecyclerView Container = null;
    private BusinessSetActivity Context;
    private int layoutResourceId;
    private int Position;

    public BusinessOpenTimeRecyclerViewAdapter(BusinessSetActivity context, int layoutResourceId, List<BusinessOpenTimeViewModel> ViewModel, RecyclerView Container) {
        this.Context = context;
        this.layoutResourceId = layoutResourceId;
        this.Container = Container;
        this.ViewModelList = ViewModel;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextViewPersian DeleteBusinessOpenTimeIconTextView;
        public ButtonPersianView EditBusinessOpenTimeButton;
        public TextViewPersian BusinessOpenTimeTitleTextView;
        public TextViewPersian ToHoursTextViewBusinessOpenTime;
        public TextViewPersian FromHoursTextViewBusinessOpenTime;


        public ViewHolder(View v) {
            super(v);
            DeleteBusinessOpenTimeIconTextView = v.findViewById(R.id.DeleteBusinessOpenTimeIconTextView);
            EditBusinessOpenTimeButton = v.findViewById(R.id.EditBusinessOpenTimeButton);
            BusinessOpenTimeTitleTextView = v.findViewById(R.id.BusinessOpenTimeTitleTextView);
            ToHoursTextViewBusinessOpenTime = v.findViewById(R.id.ToHoursTextViewBusinessOpenTime);
            FromHoursTextViewBusinessOpenTime = v.findViewById(R.id.FromHoursTextViewBusinessOpenTime);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(layoutResourceId, parent, false);
        ViewHolder CurrentViewHolder = new ViewHolder(CurrentView);
        return CurrentViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String[] DayOfWeek = new String[]{Context.getResources().getString(R.string.saturday), Context.getResources().getString(R.string.monday), Context.getResources().getString(R.string.sunday), Context.getResources().getString(R.string.wednesday),
                Context.getResources().getString(R.string.tuesday), Context.getResources().getString(R.string.thursday), Context.getResources().getString(R.string.friday)};

        holder.BusinessOpenTimeTitleTextView.setText(DayOfWeek[ViewModelList.get(position).getDayOfWeek()]);
        holder.ToHoursTextViewBusinessOpenTime.setText(ViewModelList.get(position).getTo());
        holder.FromHoursTextViewBusinessOpenTime.setText(ViewModelList.get(position).getFrom());

        holder.DeleteBusinessOpenTimeIconTextView.setTypeface(Font.MasterIcon);
        holder.DeleteBusinessOpenTimeIconTextView.setText("\uf014");

        if (ViewModelList.get(position).isActive()) {
            holder.EditBusinessOpenTimeButton.setEnabled(true);
            holder.DeleteBusinessOpenTimeIconTextView.setEnabled(true);
            holder.DeleteBusinessOpenTimeIconTextView.setClickable(true);
            holder.DeleteBusinessOpenTimeIconTextView.setTextColor(Context.getResources().getColor(R.color.FontRedColor));

            holder.DeleteBusinessOpenTimeIconTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowDeleteDialog(position);
                }
            });

            holder.EditBusinessOpenTimeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent EditOpenTimeIntent = Context.NewIntent(BusinessOpenTimeSetActivity.class);
                    EditOpenTimeIntent.putExtra("SetBusinessOpenTime", "Edit");
                    EditOpenTimeIntent.putExtra("BusinessOpenTimeId", ViewModelList.get(position).getId());
                    EditOpenTimeIntent.putExtra("BusinessId", Context.getBusinessId());
                    //2 Edit - you can find Add in UserAddressFragment
                    Context.startActivity(EditOpenTimeIntent);
                }
            });
        } else {
            holder.EditBusinessOpenTimeButton.setEnabled(false);
            holder.DeleteBusinessOpenTimeIconTextView.setEnabled(false);
            holder.DeleteBusinessOpenTimeIconTextView.setClickable(false);
            holder.DeleteBusinessOpenTimeIconTextView.setTextColor(Context.getResources().getColor(R.color.FontSemiBlackColor));

        }

    }

    private void ShowDeleteDialog(final int CurrentPosition) {
        final Dialog DeleteDialog = new Dialog(Context);
        DeleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DeleteDialog.setContentView(R.layout.dialog_yes_no_question);
        ButtonPersianView DialogBusinessDeleteOkButton = DeleteDialog.findViewById(R.id.DialogYesNoQuestionOkButton);
        ButtonPersianView DialogBusinessDeleteCancelButton = DeleteDialog.findViewById(R.id.DialogYesNoQuestionCancelButton);

        TextViewPersian DialogYesNoQuestionTitleTextView = DeleteDialog.findViewById(R.id.DialogYesNoQuestionTitleTextView);
        DialogYesNoQuestionTitleTextView.setText(R.string.business_open_time_delete);

        TextViewPersian DialogYesNoQuestionDescriptionTextView = DeleteDialog.findViewById(R.id.DialogYesNoQuestionDescriptionTextView);
        DialogYesNoQuestionDescriptionTextView.setText(R.string.description_business_open_time_delete);

        TextViewPersian DialogYesNoQuestionWarningTextView = DeleteDialog.findViewById(R.id.DialogYesNoQuestionWarningTextView);
        DialogYesNoQuestionWarningTextView.setText(R.string.warning_business_open_time_delete);

        DialogBusinessDeleteOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteDialog.dismiss();

                Position = CurrentPosition;
                Context.setRetryType(1);
                Context.ShowLoadingProgressBar();
                BusinessOpenTimeService businessOpenTimeService = new BusinessOpenTimeService(BusinessOpenTimeRecyclerViewAdapter.this);
                businessOpenTimeService.Delete(ViewModelList.get(CurrentPosition).getId());
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
    public void SetViewModel(BusinessOpenTimeViewModel ViewModel) {
        if (ViewModel != null && ViewModelList != null && ViewModelList.size() > 0) {
            for (BusinessOpenTimeViewModel Item : ViewModelList) {
                if (Item.getId() == ViewModel.getId()) {
                    Item.setTo(ViewModel.getTo());
                    Item.setFrom(ViewModel.getFrom());
                    Item.setDayOfWeek(ViewModel.getDayOfWeek());
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
    public void AddViewModel(BusinessOpenTimeViewModel ViewModel) {
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
            if (ServiceMethod == ServiceMethodType.BusinessOpenTimeDelete) {
                Feedback<BusinessOpenTimeViewModel> FeedBack = (Feedback<BusinessOpenTimeViewModel>) Data;

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