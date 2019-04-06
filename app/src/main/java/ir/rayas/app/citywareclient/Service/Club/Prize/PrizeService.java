package ir.rayas.app.citywareclient.Service.Club.Prize;

import com.android.volley.VolleyError;
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

import ir.rayas.app.citywareclient.ViewModel.Club.ActionViewModel;
import ir.rayas.app.citywareclient.ViewModel.Club.PrizeViewModel;
import ir.rayas.app.citywareclient.ViewModel.Club.UserActionPointViewModel;
import ir.rayas.app.citywareclient.ViewModel.Club.UserConsumePointViewModel;

/**
 * Created by Hajar on 3/28/2019.
 */

public class PrizeService implements IService {

    private String ControllerName = "Prize";
    private String ActionGetAll = "All";
    private String ActionUser = "User";
    private String Action = "Action";

    private IResponseService ResponseService;

    public PrizeService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }

    public void GetAll() {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionGetAll ;
        Current.GetService(this, Url, ServiceMethodType.PrizeGetAll, PrizeViewModel.class, new TypeToken<Feedback<List<PrizeViewModel>>>() {
        }.getType());
    }

    public void GetUserPrize() {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName+ "/" + ActionUser + "/" + ActionGetAll ;
        Current.GetService(this, Url, ServiceMethodType.PrizeUserGetAll, UserConsumePointViewModel.class, new TypeToken<Feedback<List<UserConsumePointViewModel>>>() {
        }.getType());
    }


    public void GetActionPointAll() {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + Action + "/"+  ActionGetAll;
        Current.GetService(this, Url, ServiceMethodType.ActionPointGetAll, ActionViewModel.class, new TypeToken<Feedback<List<ActionViewModel>>>() {
        }.getType());
    }


    public void GetUserActionPoint() {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService  + "/" + Action +"/" + ActionUser + "/"+  ActionGetAll;
        Current.GetService(this, Url, ServiceMethodType.UserActionPointGet, UserActionPointViewModel.class, new TypeToken<Feedback<List<UserActionPointViewModel>>>() {
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
