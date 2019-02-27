package ir.rayas.app.citywareclient.Service.Business;

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
import ir.rayas.app.citywareclient.ViewModel.Business.ScoreInViewModel;
import ir.rayas.app.citywareclient.ViewModel.Business.ScoreViewModel;

/**
 * Created by Hajar on 12/7/2018.
 */

public class ScoreService implements IService {

    private String ControllerName = "Score";
    private String ActionGet = "Business/";
    private String CurrentUser = "CurrentUser";
    private IResponseService ResponseService;


    public ScoreService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }

    public void Get(int BusinessId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionGet + BusinessId + "/" + CurrentUser;
        Current.GetService(this, Url, ServiceMethodType.BusinessScoreGet, ScoreViewModel.class, new TypeToken<Feedback<Double>>() {
        }.getType());
    }

    public void Add(ScoreInViewModel ViewModel) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PostService(this, Url, JsonViewModel, ServiceMethodType.BusinessScoreAdd, ScoreInViewModel.class, new TypeToken<Feedback<ScoreViewModel>>() {
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