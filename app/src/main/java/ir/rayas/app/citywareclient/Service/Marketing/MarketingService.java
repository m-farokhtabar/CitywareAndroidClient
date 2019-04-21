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
import ir.rayas.app.citywareclient.ViewModel.Marketing.MarketerCommissionInfoViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.MarketerSuggestionViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.MarketingCustomerViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.SuggestionInfoViewModel;


public class MarketingService implements IService {

    private String ControllerName = "Marketing";
    private String ActionBusiness = "Business";
    private String ActionPercents = "Percents";
    private String ActionMarketerSuggestion = "MarketerSuggestion";
    private String ControllerCustomerPercents = "CustomerPercents";
    private String ControllerMarketerCommission = "MarketerCommission";

    private IResponseService ResponseService;

    public MarketingService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }


    public void Get(int BusinessId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionBusiness + "/" + BusinessId + "/" + ActionPercents;
        Current.GetService(this, Url, ServiceMethodType.BusinessCommissionAndDiscountGet, BusinessCommissionAndDiscountViewModel.class, new TypeToken<Feedback<BusinessCommissionAndDiscountViewModel>>() {
        }.getType());
    }

    public void Add(MarketerSuggestionViewModel ViewModel) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionMarketerSuggestion;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PostService(this, Url, JsonViewModel, ServiceMethodType.MarketerSuggestionAdd, MarketerSuggestionViewModel.class, new TypeToken<Feedback<SuggestionInfoViewModel>>() {
        }.getType());
    }

    public void GetCustomerPercents() {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ControllerCustomerPercents;
        Current.GetService(this, Url, ServiceMethodType.CustomerPercentsGet, MarketingCustomerViewModel.class, new TypeToken<Feedback<List<MarketingCustomerViewModel>>>() {
        }.getType());
    }

    public void GetMarketerCommission() {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ControllerMarketerCommission;
        Current.GetService(this, Url, ServiceMethodType.MarketerCommissionGet, MarketerCommissionInfoViewModel.class, new TypeToken<Feedback<List<MarketerCommissionInfoViewModel>>>() {
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
