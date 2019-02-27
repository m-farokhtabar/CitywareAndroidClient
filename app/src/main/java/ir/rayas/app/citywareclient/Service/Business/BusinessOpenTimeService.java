package ir.rayas.app.citywareclient.Service.Business;

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
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessOpenTimeViewModel;

/**
 * Created by Hajar on 8/24/2018.
 */

public class BusinessOpenTimeService implements IService {

    private String ControllerName = "BusinessOpenTime";
    private String ActionGetAll = "All";
    private IResponseService ResponseService;

    public BusinessOpenTimeService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }

    public void GetAll(int BusinessOpenTimeId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/Business/" + BusinessOpenTimeId + "/" + ActionGetAll;
        Current.GetService(this, Url, ServiceMethodType.BusinessOpenTimeGetAll, BusinessOpenTimeViewModel.class, new TypeToken<Feedback<List<BusinessOpenTimeViewModel>>>() {
        }.getType());
    }

    public void Get(int BusinessOpenTimeId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + BusinessOpenTimeId;
        Current.GetService(this, Url, ServiceMethodType.BusinessOpenTimeGet, BusinessOpenTimeViewModel.class, new TypeToken<Feedback<BusinessOpenTimeViewModel>>() {
        }.getType());
    }

    public void Delete(int BusinessOpenTimeId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + BusinessOpenTimeId;
        Current.DeleteService(this, Url, ServiceMethodType.BusinessOpenTimeDelete,  BusinessOpenTimeViewModel.class, new TypeToken<Feedback< BusinessOpenTimeViewModel>>() {
        }.getType());
    }

    public void Add(BusinessOpenTimeViewModel ViewModel) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PostService(this, Url, JsonViewModel, ServiceMethodType.BusinessOpenTimeAdd, BusinessOpenTimeViewModel.class, new TypeToken<Feedback<BusinessOpenTimeViewModel>>() {
        }.getType());
    }

    public void Set(BusinessOpenTimeViewModel ViewModel, int BusinessOpenTimeId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + BusinessOpenTimeId;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PutService(this, Url, JsonViewModel, ServiceMethodType.BusinessOpenTimeSet, BusinessOpenTimeViewModel.class, new TypeToken<Feedback<BusinessOpenTimeViewModel>>() {
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