package ir.rayas.app.citywareclient.Service.Setting;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import ir.rayas.app.citywareclient.Service.BaseService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.IService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.ViewModel.Setting.UserSettingViewModel;

/**
 * Created by Programmer on 3/7/2018.
 */

public class UserSettingService implements IService{
    private String ControllerName = "UserSetting";
    private IResponseService ResponseService;

    public UserSettingService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }

    public void Get() {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName;
        Current.GetService(this,Url,ServiceMethodType.UserSettingGet,UserSettingViewModel.class, new TypeToken<Feedback<UserSettingViewModel>>() {}.getType());
    }

    public void Set(UserSettingViewModel ViewModel) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PutService(this,Url,JsonViewModel, ServiceMethodType.UserSettingSet,UserSettingViewModel.class, new TypeToken<Feedback<UserSettingViewModel>>() {}.getType());
    }

    @Override
    public <T> void OnSuccess(String Response, ServiceMethodType ServiceMethod, Class<T> OutputClass, Type OutputClassType) {
        Utility.HandleServiceSuccess(ResponseService,Response,ServiceMethod,OutputClass,OutputClassType);
    }

    @Override
    public <T> void OnError(VolleyError volleyError, ServiceMethodType ServiceMethod, Class<T> OutputClass) {
        Utility.HandleServiceError(ResponseService,volleyError,ServiceMethod,OutputClass);
    }
}
