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
import ir.rayas.app.citywareclient.Service.Business.BusinessContactService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.UserProfileChildren.BusinessContactSetActivity;
import ir.rayas.app.citywareclient.View.UserProfileChildren.BusinessSetActivity;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessContactViewModel;

/**
 * Created by Hajar on 8/22/2018.
 */

public class BusinessContactRecyclerViewAdapter extends RecyclerView.Adapter<BusinessContactRecyclerViewAdapter.ViewHolder> implements IResponseService {

    private List<BusinessContactViewModel> ViewModelList = null;
    private RecyclerView Container = null;
    private BusinessSetActivity Context;
    private int Position;

    public BusinessContactRecyclerViewAdapter(BusinessSetActivity context, List<BusinessContactViewModel> ViewModel, RecyclerView Container) {
        this.Context = context;
        this.Container = Container;
        this.ViewModelList = ViewModel;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextViewPersian DeleteBusinessContactIconTextView;
        public ButtonPersianView EditBusinessContactButton;
        public TextViewPersian BusinessContactTitleTextView;
        public TextViewPersian ContentTextViewBusinessContact;
        public TextViewPersian TitleTextViewBusinessContact;


        public ViewHolder(View v) {
            super(v);
            DeleteBusinessContactIconTextView = v.findViewById(R.id.DeleteBusinessContactIconTextView);
            EditBusinessContactButton = v.findViewById(R.id.EditBusinessContactButton);
            BusinessContactTitleTextView = v.findViewById(R.id.BusinessContactTitleTextView);
            ContentTextViewBusinessContact = v.findViewById(R.id.ContentTextViewBusinessContact);
            TitleTextViewBusinessContact = v.findViewById(R.id.TitleTextViewBusinessContact);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_business_contact, parent, false);
        ViewHolder CurrentViewHolder = new ViewHolder(CurrentView);
        return CurrentViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.BusinessContactTitleTextView.setText(ViewModelList.get(position).getTypeName());
        holder.ContentTextViewBusinessContact.setText(ViewModelList.get(position).getValue());
        holder.TitleTextViewBusinessContact.setText(ViewModelList.get(position).getTitle());

        holder.DeleteBusinessContactIconTextView.setTypeface(Font.MasterIcon);
        holder.DeleteBusinessContactIconTextView.setText("\uf014");

        if (ViewModelList.get(position).isActive()) {
            holder.EditBusinessContactButton.setEnabled(true);
            holder.DeleteBusinessContactIconTextView.setEnabled(true);
            holder.DeleteBusinessContactIconTextView.setClickable(true);
            holder.DeleteBusinessContactIconTextView.setTextColor(Context.getResources().getColor(R.color.FontRedColor));


            holder.DeleteBusinessContactIconTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowDeleteDialog(position);
                }
            });

            holder.EditBusinessContactButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent EditContactIntent = Context.NewIntent(BusinessContactSetActivity.class);
                    EditContactIntent.putExtra("SetBusinessContact", "Edit");
                    EditContactIntent.putExtra("BusinessContactId", ViewModelList.get(position).getId());
                    EditContactIntent.putExtra("BusinessId",Context.getBusinessId() );
                    //2 Edit - you can find Add in UserAddressFragment
                    Context.startActivity(EditContactIntent);

                }
            });
        } else {
            holder.EditBusinessContactButton.setEnabled(false);
            holder.DeleteBusinessContactIconTextView.setEnabled(false);
            holder.DeleteBusinessContactIconTextView.setClickable(false);
            holder.DeleteBusinessContactIconTextView.setTextColor(Context.getResources().getColor(R.color.FontSemiBlackColor));
        }


    }

    private void ShowDeleteDialog(final int CurrentPosition){
        final Dialog DeleteDialog = new Dialog(Context);
        DeleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DeleteDialog.setContentView(R.layout.dialog_yes_no_question);
        ButtonPersianView DialogBusinessDeleteOkButton = DeleteDialog.findViewById(R.id.DialogYesNoQuestionOkButton);
        ButtonPersianView DialogBusinessDeleteCancelButton = DeleteDialog.findViewById(R.id.DialogYesNoQuestionCancelButton);

        TextViewPersian DialogYesNoQuestionTitleTextView = DeleteDialog.findViewById(R.id.DialogYesNoQuestionTitleTextView);
        DialogYesNoQuestionTitleTextView.setText(R.string.business_contact_delete);

        TextViewPersian DialogYesNoQuestionDescriptionTextView = DeleteDialog.findViewById(R.id.DialogYesNoQuestionDescriptionTextView);
        DialogYesNoQuestionDescriptionTextView.setText(R.string.description_business_contact_delete);

        TextViewPersian DialogYesNoQuestionWarningTextView = DeleteDialog.findViewById(R.id.DialogYesNoQuestionWarningTextView);
        DialogYesNoQuestionWarningTextView.setText(R.string.warning_business_contact_delete);

        DialogBusinessDeleteOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteDialog.dismiss();

                Position = CurrentPosition;
                Context.setRetryType(1);
                Context.ShowLoadingProgressBar();
                BusinessContactService businessContactService = new BusinessContactService(BusinessContactRecyclerViewAdapter.this);
                businessContactService.Delete(ViewModelList.get(CurrentPosition).getId());
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
    public void SetViewModel(BusinessContactViewModel ViewModel) {
        if (ViewModel != null && ViewModelList != null && ViewModelList.size() > 0) {
            for (BusinessContactViewModel Item : ViewModelList) {
                if (Item.getId() == ViewModel.getId()) {
                    Item.setTitle(ViewModel.getTitle());
                    Item.setValue(ViewModel.getValue());
                    Item.setTypeId(ViewModel.getTypeId());
                    Item.setTypeName(ViewModel.getTypeName());
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
    public void AddViewModel(BusinessContactViewModel ViewModel) {
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
            if (ServiceMethod == ServiceMethodType.BusinessContactDelete) {
                Feedback<BusinessContactViewModel> FeedBack = (Feedback<BusinessContactViewModel>) Data;

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

