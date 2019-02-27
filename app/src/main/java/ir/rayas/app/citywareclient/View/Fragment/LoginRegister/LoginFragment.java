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
import ir.rayas.app.citywareclient.Service.User.AccountService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.EditTextPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Login.LoginRegisterActivity;

/**
 * فرگمنت ورود کاربر
 */
public class LoginFragment extends Fragment  implements IResponseService {
    private EditTextPersian LoginCellPhoneEditText = null;
    private LoginRegisterActivity Context = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //دریافت اکتیوتی والد این فرگمین
        Context = (LoginRegisterActivity)getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_login, container, false);

        //طرحبندی ویو
        CreateLayout(CurrentView);

        Utility.HideKeyboard(Context);
        return CurrentView;
    }

    private void CreateLayout(View CurrentView)
    {
        LoginCellPhoneEditText = CurrentView.findViewById(R.id.LoginCellPhoneEditText);

        Button LoginButton = CurrentView.findViewById(R.id.LoginButton);
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnLoginButtonClick(view);
            }
        });
    }
    private void OnLoginButtonClick(View view)
    {
        if (Utility.ValidateView(LoginCellPhoneEditText))
        {
            Context.ShowLoadingProgressBar();
            AccountService Service = new AccountService(this);
            long Cellphone = Long.valueOf(LoginCellPhoneEditText.getText().toString());
            Service.RequestTrackingCode(Cellphone);
        }
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        try {
            Feedback<Boolean> FeedBack = (Feedback<Boolean>) Data;
            if (FeedBack.getStatus() == FeedbackType.RegisteredSuccessful.getId()) {
                Boolean ViewModel = FeedBack.getValue();
                if (ViewModel!=null)
                {
                    long Cellphone = Long.valueOf(LoginCellPhoneEditText.getText().toString());
                    Context.GoToConfirmTrackingCodeActivity(Cellphone);
                }
                else
                {
                    Context.ShowToast(FeedbackType.InvalidDataFormat.getMessage().replace("{0}",""), Toast.LENGTH_LONG, MessageType.Warning);
                    Context.HideLoading();
                }
            } else {
                if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId())
                {
                    Context.HideLoading();
                    //LoginCellPhoneEditText.requestFocus();
                    //Utility.ShowKeyboard(LoginCellPhoneEditText);
                    Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                }
                else {
                    Context.ShowErrorInConnectDialog();
                }
            }
        } catch (Exception Ex) {
            Context.HideLoading();
            Context.ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
    }

}
