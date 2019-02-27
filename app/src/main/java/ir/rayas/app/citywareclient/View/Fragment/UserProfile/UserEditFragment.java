package ir.rayas.app.citywareclient.View.Fragment.UserProfile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.User.UserService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.EditTextPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.Master.UserProfileActivity;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.UserViewModel;


public class UserEditFragment extends Fragment implements IResponseService, ILoadData {

    private UserProfileActivity Context = null;
    private EditTextPersian CellPhoneEditText = null;
    private EditTextPersian NickNameEditText = null;
    private EditTextPersian NameEditText = null;
    private EditTextPersian FamilyEditText = null;
    private SwipeRefreshLayout SwipeContainer;
    private boolean IsSwipe = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //دریافت اکتیوتی والد این فرگمین
        Context = (UserProfileActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_user_edit, container, false);

        //طرحبندی ویو
        CreateLayout(CurrentView);
        //برای فهمیدن کد فرگنت به UserProfilePagerAdapter مراجعه کنید
        Context.setFragmentIndex(5);
        //دریافت اطلاعات از سرور
        LoadData();

        Utility.HideKeyboard(Context);
        return CurrentView;
    }

    private void CreateLayout(View CurrentView) {

        CellPhoneEditText = CurrentView.findViewById(R.id.CellPhoneEditText);
        NickNameEditText = CurrentView.findViewById(R.id.NickNameEditText);
        NameEditText = CurrentView.findViewById(R.id.NameEditText);
        FamilyEditText = CurrentView.findViewById(R.id.FamilyEditText);
        SwipeContainer = CurrentView.findViewById(R.id.SwipeContainer);

        CellPhoneEditText.setFocusable(false);
        CellPhoneEditText.setClickable(false);
        CellPhoneEditText.setEnabled(false);

        SwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                IsSwipe = true;
                LoadData();
            }
        });

        Button RegisterButton = CurrentView.findViewById(R.id.RegisterButton);
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnSaveInformationButtonClick(view);
            }
        });
    }

    public void LoadData() {
        if (!IsSwipe)
            Context.ShowLoadingProgressBar();

        UserService UsService = new UserService(this);
        AccountViewModel ViewModel = Context.getAccountRepository().getAccount();
        if (ViewModel != null) {
            //دریافت اطلاعات
            Context.setRetryType(2);
            UsService.Get(ViewModel.getUser().getId());
        }


    }

    private void OnSaveInformationButtonClick(View view) {
        if (Utility.ValidateView(NickNameEditText, NameEditText, FamilyEditText)) {
            Context.ShowLoadingProgressBar();
            //ثبت اطلاعات
            Context.setRetryType(1);
            UserService Service = new UserService(this);
            Service.Set(MadeViewModel());

        }
    }

    private UserViewModel MadeViewModel() {
        UserViewModel ViewModel = new UserViewModel();
        try {
            AccountViewModel accountViewModel = Context.getAccountRepository().getAccount();
            ViewModel.setId(accountViewModel.getUser().getId());
            ViewModel.setCellPhone(Long.valueOf(CellPhoneEditText.getText().toString()));
            ViewModel.setNickName(NickNameEditText.getText().toString());
            ViewModel.setName(NameEditText.getText().toString());
            ViewModel.setFamily(FamilyEditText.getText().toString());
        } catch (Exception Ex) {
        }
        return ViewModel;
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        SwipeContainer.setRefreshing(false);
        IsSwipe = false;

        try {
            if (ServiceMethod == ServiceMethodType.UserGet) {
                Feedback<UserViewModel> FeedBack = (Feedback<UserViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    //NickNameEditText.requestFocus();
                    //Utility.ShowKeyboard(NickNameEditText);

                    UserViewModel ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        CellPhoneEditText.setText(String.valueOf(ViewModel.getCellPhone()));
                        NickNameEditText.setText(ViewModel.getNickName());
                        NameEditText.setText(ViewModel.getName());
                        FamilyEditText.setText(ViewModel.getFamily());
                    } else {
                        Context.ShowToast(FeedbackType.InvalidDataFormat.getMessage().replace("{0}", ""), Toast.LENGTH_LONG, MessageType.Warning);
                    }

                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.UserSet) {
                Feedback<UserViewModel> FeedBack = (Feedback<UserViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.UpdatedSuccessful.getId()) {
                    Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);

                    UserViewModel ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        //  ---------------------- اپدیت اکانت کاربر  ------------------------------
                        AccountViewModel accountViewModel = Context.getAccountRepository().getAccount();
                        accountViewModel.setUser(ViewModel);
                        Context.getAccountRepository().setAccount(accountViewModel);
                        //----------------------------------------------------------

                        CellPhoneEditText.setText(String.valueOf(ViewModel.getCellPhone()));
                        NickNameEditText.setText(ViewModel.getNickName());
                        NameEditText.setText(ViewModel.getName());
                        FamilyEditText.setText(ViewModel.getFamily());
                    } else {
                        Context.ShowToast(FeedbackType.InvalidDataFormat.getMessage().replace("{0}", ""), Toast.LENGTH_LONG, MessageType.Warning);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        //NickNameEditText.requestFocus();
                        //Utility.ShowKeyboard(NickNameEditText);
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            }

        } catch (Exception Ex) {
            Context.ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (Context!=null) {
                //برای فهمیدن کد فرگنت به UserProfilePagerAdapter مراجعه کنید
                Context.setFragmentIndex(5);
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

}
