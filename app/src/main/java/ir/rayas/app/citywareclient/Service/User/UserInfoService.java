package ir.rayas.app.citywareclient.Service.User;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ir.rayas.app.citywareclient.Service.BaseService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.IService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.ViewModel.User.UserAddressViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.UserInfoViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.UserInfoWithTypesViewModel;

/**
 * Created by Hajar on 7/28/2018.
 */

public class UserInfoService implements IService {

    private String ControllerName = "UserInfo";
    private IResponseService ResponseService;

    public UserInfoService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }

    public void Get() {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName;
        Current.GetService(this, Url, ServiceMethodType.UserGetInfo, UserInfoWithTypesViewModel.class, new TypeToken<Feedback<UserInfoWithTypesViewModel>>() {
        }.getType());
    }


    public void Set(UserInfoViewModel ViewModel) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PutService(this, Url, JsonViewModel, ServiceMethodType.UserSetInfo, UserInfoViewModel.class, new TypeToken<Feedback<UserInfoViewModel>>() {
        }.getType());
    }

    @Override
    public <T> void OnSuccess(String Response, ServiceMethodType ServiceMethod, Class<T> OutputClass, Type OutputClassType) {
        Utility.HandleServiceSuccess(ResponseService, Response, ServiceMethod, OutputClass, OutputClassType);
    }

    @Override
    public <T> void OnError(VolleyError volleyError, ServiceMethodType ServiceMethod, Class<T> OutputClass) {
        Utility.HandleServiceError(ResponseService, volleyError, ServiceMethod, OutputClass);
    }

}
