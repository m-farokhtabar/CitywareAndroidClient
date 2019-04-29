package ir.rayas.app.citywareclient.Service.Factor;

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
import ir.rayas.app.citywareclient.ViewModel.Basket.BasketViewModel;
import ir.rayas.app.citywareclient.ViewModel.Factor.DescriptionFactorInViewModel;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorInViewModel;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorViewModel;
import ir.rayas.app.citywareclient.ViewModel.Factor.StatusAndDescriptionFactorInViewModel;


public class UserFactorService implements IService {

    private String ControllerName = "Factor";

    private String ActionUser = "User";

    private IResponseService ResponseService;

    public UserFactorService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }


    public void GetDeliveryPrice(double UserLatitude, double UserLongitude, double BusinessLatitude, double BusinessLongitude) {
        BaseService Current = new BaseService();
        String actionDeliveryPrice = "DeliveryPrice";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + actionDeliveryPrice + "/" + UserLatitude + "/" + UserLongitude + "/" + BusinessLatitude + "/" + BusinessLongitude;
        Current.GetService(this, Url, ServiceMethodType.DeliveryPriceGet, BasketViewModel.class, new TypeToken<Feedback<Double>>() {
        }.getType());
    }

    public void Add(FactorInViewModel ViewModel) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PostService(this, Url, JsonViewModel, ServiceMethodType.FactorAdd, FactorInViewModel.class, new TypeToken<Feedback<FactorViewModel>>() {
        }.getType());
    }


    public void GetAll(int Page) {
        BaseService Current = new BaseService();
        String actionAll = "All";
        String actionPage = "Page";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionUser + "/" + actionAll + "/" + actionPage + "/" + Page;
        Current.GetService(this, Url, ServiceMethodType.UserFactorGetAll, FactorViewModel.class, new TypeToken<Feedback<List<FactorViewModel>>>() {
        }.getType());
    }

    public void Delete(int FactorId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionUser + "/" + FactorId;
        Current.DeleteService(this, Url, ServiceMethodType.UserFactorDelete, FactorViewModel.class, new TypeToken<Feedback<FactorViewModel>>() {
        }.getType());
    }

    public void Get(int FactorId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionUser + "/" + FactorId;
        Current.GetService(this, Url, ServiceMethodType.UserFactorGet, FactorViewModel.class, new TypeToken<Feedback<FactorViewModel>>() {
        }.getType());
    }

    public void SetStatusAndDescription(StatusAndDescriptionFactorInViewModel ViewModel) {
        BaseService Current = new BaseService();
        String actionStatusAndDescription = "StatusAndDescription";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionUser + "/" + actionStatusAndDescription;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PutService(this, Url, JsonViewModel, ServiceMethodType.UserStatusAndDescriptionSet, StatusAndDescriptionFactorInViewModel.class, new TypeToken<Feedback<Boolean>>() {
        }.getType());
    }

    public void SetDescription(DescriptionFactorInViewModel ViewModel) {
        BaseService Current = new BaseService();
        String actionDescription = "Description";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionUser + "/" + actionDescription;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PutService(this, Url, JsonViewModel, ServiceMethodType.UserDescriptionSet, DescriptionFactorInViewModel.class, new TypeToken<Feedback<Boolean>>() {
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
