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
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessContactViewModel;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessContactWithTypesViewModel;



public class BusinessContactService implements IService {

    private String ControllerName = "BusinessContact";
    private IResponseService ResponseService;

    public BusinessContactService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }

    public void GetAll(int BusinessId) {
        BaseService Current = new BaseService();
        String actionGetAll = "All";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/Business/" + BusinessId + "/" + actionGetAll;
        Current.GetService(this, Url, ServiceMethodType.BusinessContactGetAll, BusinessContactViewModel.class, new TypeToken<Feedback<List<BusinessContactViewModel>>>() {
        }.getType());
    }

    public void Get(int BusinessId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + BusinessId;
        Current.GetService(this, Url, ServiceMethodType.BusinessContactGet, BusinessContactWithTypesViewModel.class, new TypeToken<Feedback<BusinessContactWithTypesViewModel>>() {
        }.getType());
    }

    public void Delete(int BusinessId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + BusinessId;
        Current.DeleteService(this, Url, ServiceMethodType.BusinessContactDelete,  BusinessContactViewModel.class, new TypeToken<Feedback< BusinessContactViewModel>>() {
        }.getType());
    }

    public void Add(BusinessContactViewModel ViewModel) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PostService(this, Url, JsonViewModel, ServiceMethodType.BusinessContactAdd, BusinessContactViewModel.class, new TypeToken<Feedback<BusinessContactViewModel>>() {
        }.getType());
    }

    public void Set(BusinessContactViewModel ViewModel, int BusinessId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + BusinessId;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PutService(this, Url, JsonViewModel, ServiceMethodType.BusinessContactSet, BusinessContactViewModel.class, new TypeToken<Feedback<BusinessContactViewModel>>() {
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