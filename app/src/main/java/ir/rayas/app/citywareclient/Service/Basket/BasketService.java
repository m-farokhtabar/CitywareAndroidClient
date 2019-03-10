package ir.rayas.app.citywareclient.Service.Basket;

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
import ir.rayas.app.citywareclient.ViewModel.Basket.BasketDeliveryAndUserDescriptionViewModel;
import ir.rayas.app.citywareclient.ViewModel.Basket.BasketItemViewModel;
import ir.rayas.app.citywareclient.ViewModel.Basket.BasketViewModel;
import ir.rayas.app.citywareclient.ViewModel.Basket.QuickOrderViewModel;
import ir.rayas.app.citywareclient.ViewModel.Basket.StandardOrderItemViewModel;
import ir.rayas.app.citywareclient.ViewModel.Business.BookmarkOutViewModel;
import ir.rayas.app.citywareclient.ViewModel.Business.BookmarkViewModel;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessViewModel;

/**
 * Created by Hajar on 1/25/2019.
 */

public class BasketService implements IService {

    private String ControllerName = "Basket";
    private String ActionGetAll = "All";
    private String ActionItemQuantity = "ItemQuantity";
    private String ActionItem = "Item";
    private String ActionProductId = "ProductId";
    private String ActionUserDescriptionAndDelivery = "UserDescriptionAndDelivery";
    private String ActionQuickItems = "QuickItems";

    private IResponseService ResponseService;

    public BasketService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }

    public void GetAll() {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionGetAll;
        Current.GetService(this, Url, ServiceMethodType.BasketGetAll, BasketViewModel.class, new TypeToken<Feedback<List<BasketViewModel>>>() {
        }.getType());
    }

    public void Get(int BasketId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + BasketId;
        Current.GetService(this, Url, ServiceMethodType.BasketGet, BasketViewModel.class, new TypeToken<Feedback<BasketViewModel>>() {
        }.getType());
    }

    public void GetQuantityByProductId(int ProductId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionItemQuantity + "/" + ActionProductId + "/" + ProductId;
        Current.GetService(this, Url, ServiceMethodType.QuantityByProductIdGet, BasketViewModel.class, new TypeToken<Feedback<Double>>() {
        }.getType());
    }


    public void Add(StandardOrderItemViewModel ViewModel) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionItem;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PostService(this, Url, JsonViewModel, ServiceMethodType.BasketAdd, StandardOrderItemViewModel.class, new TypeToken<Feedback<Double>>() {
        }.getType());
    }


    public void EditQuantityByProductId(int ProductId, double Quantity) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionItem + "/" + ActionProductId + "/" + ProductId + "/" + Quantity;
        String JsonViewModel = "";
        Current.PutService(this, Url, JsonViewModel, ServiceMethodType.BasketEditQuantityByProductId, StandardOrderItemViewModel.class, new TypeToken<Feedback<BasketItemViewModel>>() {
        }.getType());
    }

    public void EditQuantityByItemId(int ItemId, double Quantity) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionItem  + "/" + ItemId + "/" + Quantity;
        String JsonViewModel = "";
        Current.PutService(this, Url, JsonViewModel, ServiceMethodType.BasketEditQuantityByItemId, StandardOrderItemViewModel.class, new TypeToken<Feedback<BasketItemViewModel>>() {
        }.getType());
    }


    public void DeleteItemByItemId(int ItemId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionItem + "/" + ItemId;
        Current.DeleteService(this, Url, ServiceMethodType.BasketDeleteItemByItemId, BasketItemViewModel.class, new TypeToken<Feedback<BasketItemViewModel>>() {
        }.getType());
    }

    public void DeleteBasket(int ItemId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/"  + ItemId;
        Current.DeleteService(this, Url, ServiceMethodType.BasketDelete, BasketViewModel.class, new TypeToken<Feedback<BasketViewModel>>() {
        }.getType());
    }


    public void EditUserDescriptionAndDelivery(BasketDeliveryAndUserDescriptionViewModel ViewModel,int BasketId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionUserDescriptionAndDelivery + "/" + BasketId;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PutService(this, Url, JsonViewModel, ServiceMethodType.BasketEditUserDescriptionAndDelivery, BasketDeliveryAndUserDescriptionViewModel.class, new TypeToken<Feedback<BasketViewModel>>() {
        }.getType());
    }

    public void AddQuick(QuickOrderViewModel ViewModel) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionQuickItems;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PostService(this, Url, JsonViewModel, ServiceMethodType.BasketQuickAdd, QuickOrderViewModel.class, new TypeToken<Feedback<Double>>() {
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
