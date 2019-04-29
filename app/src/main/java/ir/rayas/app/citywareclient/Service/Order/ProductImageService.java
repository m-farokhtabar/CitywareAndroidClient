package ir.rayas.app.citywareclient.Service.Order;

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
import ir.rayas.app.citywareclient.ViewModel.Order.ProductImageViewModel;


public class ProductImageService implements IService {

    private String ControllerName = "ProductImage";

    private IResponseService ResponseService;

    public ProductImageService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }

    public void GetAll(int ProductId, int NumberPage) {
        BaseService Current = new BaseService();
        String actionGetAll = "all";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + actionGetAll + "/Product/" + ProductId + "/Page/" + NumberPage;
        Current.GetService(this, Url, ServiceMethodType.GetAllProductImage, ProductImageViewModel.class, new TypeToken<Feedback<List<ProductImageViewModel>>>() {
        }.getType());
    }

    public void Get(int ProductImageId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName  + "/" + ProductImageId;
        Current.GetService(this, Url, ServiceMethodType.ProductImageGet, ProductImageViewModel.class, new TypeToken<Feedback<ProductImageViewModel>>() {
        }.getType());
    }

    public void Delete(int BusinessId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + BusinessId;
        Current.DeleteService(this, Url, ServiceMethodType.ProductImageDelete, ProductImageViewModel.class, new TypeToken<Feedback<ProductImageViewModel>>() {
        }.getType());
    }

    public void Add(ProductImageViewModel ViewModel) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName ;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PostService(this, Url, JsonViewModel, ServiceMethodType.ProductImageAdd, ProductImageViewModel.class, new TypeToken<Feedback<ProductImageViewModel>>() {
        }.getType());
    }

    public void Set(ProductImageViewModel ViewModel, int ProductImageId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService  + "/" + ControllerName + "/" + ProductImageId;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PutService(this, Url, JsonViewModel, ServiceMethodType.ProductImageSet, ProductImageViewModel.class, new TypeToken<Feedback<ProductImageViewModel>>() {
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