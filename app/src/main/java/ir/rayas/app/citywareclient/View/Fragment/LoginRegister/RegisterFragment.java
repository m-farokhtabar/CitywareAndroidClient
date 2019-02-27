package ir.rayas.app.citywareclient.View.Fragment.LoginRegister;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import ir.rayas.app.citywareclient.View.Login.LoginRegisterActivity;
import ir.rayas.app.citywareclient.ViewModel.User.UserViewModel;

/**
 * فرگمنت ثبت نام کارب
 */
public class RegisterFragment extends Fragment implements IResponseService {
    private Button RegisterButton = null;
    private LoginRegisterActivity Context = null;
    private EditTextPersian CellPhoneEditText = null;
    private EditTextPersian NickNameEditText = null;
    private EditTextPersian NameEditText = null;
    private EditTextPersian FamilyEditText = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //دریافت اکتیوتی والد این فرگمین
        Context = (LoginRegisterActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_register, container, false);

        //طرحبندی ویو
        CreateLayout(CurrentView);
        Utility.HideKeyboard(Context);
        return CurrentView;
    }

    private void CreateLayout(View CurrentView) {

        CellPhoneEditText = CurrentView.findViewById(R.id.CellPhoneEditText);
        NickNameEditText = CurrentView.findViewById(R.id.NickNameEditText);
        NameEditText = CurrentView.findViewById(R.id.NameEditText);
        FamilyEditText = CurrentView.findViewById(R.id.FamilyEditText);

        RegisterButton = CurrentView.findViewById(R.id.RegisterButton);
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnRegisterClick(view);
            }
        });
    }

    private void OnRegisterClick(View view) {
        if (Utility.ValidateView(CellPhoneEditText, NickNameEditText, NameEditText, FamilyEditText)) {
            Context.ShowLoadingProgressBar();
            UserService Service = new UserService(this);
            Service.Add(MadeViewModel());
        }
    }

    private UserViewModel MadeViewModel() {
        UserViewModel ViewModel = new UserViewModel();
        try {

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
        try {
            Feedback<UserViewModel> FeedBack = (Feedback<UserViewModel>) Data;
            if (FeedBack.getStatus() == FeedbackType.RegisteredSuccessful.getId()) {
                UserViewModel ViewModel = FeedBack.getValue();
                if (ViewModel != null) {
                    Context.GoToConfirmTrackingCodeActivity(ViewModel.getCellPhone());
                }
                else
                {
                    Context.ShowToast(FeedbackType.InvalidDataFormat.getMessage().replace("{0}",""), Toast.LENGTH_LONG, MessageType.Warning);
                    Context.HideLoading();
                }
            } else {
                if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                    Context.HideLoading();
                    //CellPhoneEditText.requestFocus();
                    //Utility.ShowKeyboard(CellPhoneEditText);
                    Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                } else {
                    Context.ShowErrorInConnectDialog();
                }
            }
        } catch (Exception Ex) {
            Context.HideLoading();
            Context.ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
    }
}
