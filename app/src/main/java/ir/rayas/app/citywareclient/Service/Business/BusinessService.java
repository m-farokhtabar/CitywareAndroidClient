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
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessViewModel;



public class BusinessService implements IService {

    private String ControllerName = "Business";
    private IResponseService ResponseService;

    public BusinessService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }

    public void GetAll() {
        BaseService Current = new BaseService();
        String actionGetAll = "all";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + actionGetAll;
        Current.GetService(this, Url, ServiceMethodType.UserGetAllBusiness, BusinessViewModel.class, new TypeToken<Feedback<List<BusinessViewModel>>>() {
        }.getType());
    }

    public void Get(int BusinessId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + BusinessId;
        Current.GetService(this, Url, ServiceMethodType.BusinessGet, BusinessViewModel.class, new TypeToken<Feedback<BusinessViewModel>>() {
        }.getType());
    }

    public void Delete(int BusinessId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + BusinessId;
        Current.DeleteService(this, Url, ServiceMethodType.BusinessDelete, BusinessViewModel.class, new TypeToken<Feedback<BusinessViewModel>>() {
        }.getType());
    }

    public void Add(BusinessViewModel ViewModel) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PostService(this, Url, JsonViewModel, ServiceMethodType.BusinessAdd, BusinessViewModel.class, new TypeToken<Feedback<BusinessViewModel>>() {
        }.getType());
    }

    public void Set(BusinessViewModel ViewModel, int BusinessId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + BusinessId;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PutService(this, Url, JsonViewModel, ServiceMethodType.BusinessSet, BusinessViewModel.class, new TypeToken<Feedback<BusinessViewModel>>() {
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
