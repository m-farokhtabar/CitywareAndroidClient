package ir.rayas.app.citywareclient.Service.Marketing;

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
import ir.rayas.app.citywareclient.ViewModel.Marketing.BusinessCommissionAndDiscountViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.BusinessCommissionInfoViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.MarketerCommissionInfoViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.MarketerSuggestionViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.MarketingBusinessManViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.MarketingBusinessViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.MarketingCustomerViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.MarketingPayedBusinessManViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.MarketingPayedBusinessViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.Marketing_CustomerFactorViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.SuggestionInfoViewModel;



public class MarketingService implements IService {

    private String ControllerName = "Marketing";

    private IResponseService ResponseService;

    public MarketingService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }


    public void Get(int BusinessId) {
        BaseService Current = new BaseService();
        String actionPercents = "Percents";
        String actionBusiness = "Business";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + actionBusiness + "/" + BusinessId + "/" + actionPercents;
        Current.GetService(this, Url, ServiceMethodType.BusinessCommissionAndDiscountGet, BusinessCommissionAndDiscountViewModel.class, new TypeToken<Feedback<BusinessCommissionAndDiscountViewModel>>() {
        }.getType());
    }

    public void Add(MarketerSuggestionViewModel ViewModel) {
        BaseService Current = new BaseService();
        String actionMarketerSuggestion = "MarketerSuggestion";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + actionMarketerSuggestion;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PostService(this, Url, JsonViewModel, ServiceMethodType.MarketerSuggestionAdd, MarketerSuggestionViewModel.class, new TypeToken<Feedback<SuggestionInfoViewModel>>() {
        }.getType());
    }

    public void GetCustomerPercents() {
        BaseService Current = new BaseService();
        String controllerCustomerPercents = "CustomerPercents";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + controllerCustomerPercents;
        Current.GetService(this, Url, ServiceMethodType.CustomerPercentsGet, MarketingCustomerViewModel.class, new TypeToken<Feedback<List<MarketingCustomerViewModel>>>() {
        }.getType());
    }

    public void GetMarketerCommission() {
        BaseService Current = new BaseService();
        String controllerMarketerCommission = "MarketerCommission";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + controllerMarketerCommission;
        Current.GetService(this, Url, ServiceMethodType.MarketerCommissionGet, MarketerCommissionInfoViewModel.class, new TypeToken<Feedback<MarketerCommissionInfoViewModel>>() {
        }.getType());
    }

    public void GetAllNotReceivedMarketerCommission() {
        BaseService Current = new BaseService();
        String controllerNotReceivedMarketerCommission = "GetAllNotReceivedMarketerCommission";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + controllerNotReceivedMarketerCommission;
        Current.GetService(this, Url, ServiceMethodType.NotReceivedMarketerCommissionGetAll, MarketingBusinessManViewModel.class, new TypeToken<Feedback<List<MarketingBusinessManViewModel>>>() {
        }.getType());
    }

    public void GetAllReceivedMarketerCommission() {
        BaseService Current = new BaseService();
        String controllerReceivedMarketerCommission = "GetAllReceivedMarketerCommission";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + controllerReceivedMarketerCommission;
        Current.GetService(this, Url, ServiceMethodType.ReceivedMarketerCommissionGetAll, MarketingPayedBusinessManViewModel.class, new TypeToken<Feedback<List<MarketingPayedBusinessManViewModel>>>() {
        }.getType());
    }


    public void GetAllNewSuggestionMarketerCommission() {
        BaseService Current = new BaseService();
        String controllerNewSuggestionMarketerCommission = "GetAllNewSuggestionMarketerCommission";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + controllerNewSuggestionMarketerCommission;
        Current.GetService(this, Url, ServiceMethodType.NewSuggestionMarketerCommissionGetAll, MarketingBusinessManViewModel.class, new TypeToken<Feedback<List<MarketingBusinessManViewModel>>>() {
        }.getType());
    }

    public void GetAllExpiredMarketerCommission() {
        BaseService Current = new BaseService();
        String controllerExpiredMarketerCommission = "GetAllExpiredMarketerCommission";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + controllerExpiredMarketerCommission;
        Current.GetService(this, Url, ServiceMethodType.ExpiredMarketerCommissionGetAll, MarketingBusinessManViewModel.class, new TypeToken<Feedback<List<MarketingBusinessManViewModel>>>() {
        }.getType());
    }

    public void GetBusinessCommission(int BusinessId) {
        BaseService Current = new BaseService();
        String controllerBusinessCommission = "BusinessCommission";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + controllerBusinessCommission + "/" + BusinessId;
        Current.GetService(this, Url, ServiceMethodType.BusinessCommissionGet, BusinessCommissionInfoViewModel.class, new TypeToken<Feedback<BusinessCommissionInfoViewModel>>() {
        }.getType());
    }

    public void GetAllNotPayedBusinessCommission(int BusinessId) {
        BaseService Current = new BaseService();
        String controllerNotPayedBusinessCommission = "GetAllNotPayedBusinessCommission";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + controllerNotPayedBusinessCommission + "/" + BusinessId;
        Current.GetService(this, Url, ServiceMethodType.NotPayedBusinessCommissionGetAll, MarketingBusinessViewModel.class, new TypeToken<Feedback<List<MarketingBusinessViewModel>>>() {
        }.getType());
    }


    public void GetAllPayedBusinessCommission(int BusinessId) {
        BaseService Current = new BaseService();
        String controllerPayedBusinessCommission = "GetAllPayedBusinessCommission";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + controllerPayedBusinessCommission + "/" + BusinessId;
        Current.GetService(this, Url, ServiceMethodType.PayedBusinessCommissionGetAll, MarketingPayedBusinessViewModel.class, new TypeToken<Feedback<List<MarketingPayedBusinessViewModel>>>() {
        }.getType());
    }

    public void GetAllExpiredBusinessCommission(int BusinessId) {
        BaseService Current = new BaseService();
        String controllerExpiredBusinessCommission = "GetAllExpiredBusinessCommission";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + controllerExpiredBusinessCommission + "/" + BusinessId;
        Current.GetService(this, Url, ServiceMethodType.ExpiredBusinessCommissionGetAll, MarketingBusinessViewModel.class, new TypeToken<Feedback<List<MarketingBusinessViewModel>>>() {
        }.getType());
    }

    public void GetAllNewSuggestionBusinessCommission(int BusinessId) {
        BaseService Current = new BaseService();
        String controllerNewSuggestionBusinessCommission = "GetAllNewSuggestionBusinessCommission";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + controllerNewSuggestionBusinessCommission + "/" + BusinessId;
        Current.GetService(this, Url, ServiceMethodType.NewSuggestionBusinessCommissionGetAll, MarketingBusinessViewModel.class, new TypeToken<Feedback<List<MarketingBusinessViewModel>>>() {
        }.getType());
    }

    public void AddCustomerFactor(Marketing_CustomerFactorViewModel ViewModel){

        BaseService Current = new BaseService();
        String actionAddCustomerFactor = "AddCustomerFactor";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + actionAddCustomerFactor;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PostService(this, Url, JsonViewModel, ServiceMethodType.AddCustomerFactorAdd, Marketing_CustomerFactorViewModel.class, new TypeToken<Feedback<Boolean>>() {
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