package ir.rayas.app.citywareclient.Service.Setting;

import android.content.Context;

import com.android.volley.NetworkResponse;
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
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.ViewModel.Setting.SettingViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;

/** دریافت تنظیمات کلی برنامه
 * Created by Programmer on 2/10/2018.
 */

public class SettingService implements IService {
    private String ControllerName = "Setting";
    private String ActionGet = "Get";
    private String ActionGetIntroduceBackground = "GetIntroduceBackground";
    private IResponseService ResponseService;

    public SettingService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }

    public void Get() {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionGet;
        Current.GetService(this, Url, ServiceMethodType.SettingGet,SettingViewModel.class,new TypeToken<Feedback<SettingViewModel>>() {}.getType());
    }

    public void GetIntroduceBackground() {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionGetIntroduceBackground;
        Current.GetService(this, Url, ServiceMethodType.IntroduceBackgroundGet,String.class,new TypeToken<Feedback<String>>() {}.getType());
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
