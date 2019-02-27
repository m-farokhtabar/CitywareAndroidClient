package ir.rayas.app.citywareclient.Service.User;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.ContentHandler;

import ir.rayas.app.citywareclient.Service.BaseService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.IService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;

/**
 * Created by Programmer on 2/5/2018.
 */

public class AccountService implements IService {
    private String ControllerName = "Accounting";
    private String ActionLogin = "Login";
    private String ActionRequestTrackingCode = "RequestTrackingCode";
    private String ActionConfirmAndLogin = "ConfirmAndLogin";
    private IResponseService ResponseService;

    public AccountService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }

    public void Login(long CellPhone, int ConfirmationId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionLogin + "/" + String.valueOf(CellPhone) + "/" + String.valueOf(ConfirmationId);
        Current.GetService(this,Url,ServiceMethodType.Login,AccountViewModel.class, new TypeToken<Feedback<AccountViewModel>>() {}.getType());
    }

    public void RequestTrackingCode(long CellPhone) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionRequestTrackingCode + "/" + String.valueOf(CellPhone);
        Current.GetService(this,Url,ServiceMethodType.RequestTrackingCode,Boolean.class, new TypeToken<Feedback<Boolean>>() {}.getType());
    }

    public void ConfirmAndLogin(long CellPhone, long TrackingCode)
    {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionConfirmAndLogin + "/" + String.valueOf(CellPhone) + "/" + String.valueOf(TrackingCode);
        Current.GetService(this,Url,ServiceMethodType.ConfirmAndLogin,AccountViewModel.class, new TypeToken<Feedback<AccountViewModel>>() {}.getType());
    }

    @Override
    public <T> void OnSuccess(String Response, ServiceMethodType ServiceMethod,Class<T> OutputClass,Type OutputClassType) {
        Utility.HandleServiceSuccess(ResponseService,Response,ServiceMethod,OutputClass,OutputClassType);
    }

    @Override
    public <T> void OnError(VolleyError volleyError, ServiceMethodType ServiceMethod,Class<T> OutputClass) {
        Utility.HandleServiceError(ResponseService,volleyError,ServiceMethod,OutputClass);
    }
}