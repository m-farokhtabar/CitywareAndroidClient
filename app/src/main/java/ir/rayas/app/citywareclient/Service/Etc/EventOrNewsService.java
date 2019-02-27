package ir.rayas.app.citywareclient.Service.Etc;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import ir.rayas.app.citywareclient.Service.BaseService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.IService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.PlaceType;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.ViewModel.Etc.EventOrNewsViewModel;
import ir.rayas.app.citywareclient.ViewModel.Setting.SettingViewModel;

/** دریافت اخبار تبلیغات و رویدادها
 * Created by Programmer on 2/12/2018.
 */

public class EventOrNewsService implements IService{
    private String ControllerName = "EventOrNews";
    private String ActionGetRandomly = "GetRandomly";
    private String ActionGetLast = "GetLast";
    private IResponseService ResponseService;

    public EventOrNewsService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }

    /**
     * انخاب یک رویداد به طور اتفاقی
     * @param PlaceType
     */
    public void GetRandomly(PlaceType PlaceType) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionGetRandomly + "/" + String.valueOf(PlaceType.ordinal());
        Current.GetService(this, Url, ServiceMethodType.RandomlyEventOrNewsGet,SettingViewModel.class,new TypeToken<Feedback<EventOrNewsViewModel>>() {}.getType());
    }

    /**
     * دریافت آخرین رویداد برای مکان مورد نظر
     * @param PlaceType
     */
    public void GetLast(PlaceType PlaceType) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionGetLast + "/" + String.valueOf(PlaceType.ordinal());
        Current.GetService(this, Url, ServiceMethodType.RandomlyEventOrNewsGet,SettingViewModel.class,new TypeToken<Feedback<EventOrNewsViewModel>>() {}.getType());
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
