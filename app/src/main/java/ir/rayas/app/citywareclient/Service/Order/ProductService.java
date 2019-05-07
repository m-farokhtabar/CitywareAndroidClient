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
import ir.rayas.app.citywareclient.ViewModel.Marketing.ProductInfoViewModel;
import ir.rayas.app.citywareclient.ViewModel.Order.ProductViewModel;

public class ProductService implements IService {

    private String ControllerName = "Product";
    private IResponseService ResponseService;

    public ProductService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }

    public void GetAll(int BusinessId, int NumberPage) {
        BaseService Current = new BaseService();
        String actionGetAll = "all";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + actionGetAll + "/Business/" + BusinessId + "/Page/" + NumberPage;
        Current.GetService(this, Url, ServiceMethodType.ProductGetAll, ProductViewModel.class, new TypeToken<Feedback<List<ProductViewModel>>>() {
        }.getType());
    }


    public void GetAll(int BusinessId) {

        BaseService Current = new BaseService();
        String actionGetAll = "AllInfo";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + actionGetAll + "/Business/" + BusinessId ;
        Current.GetService(this, Url, ServiceMethodType.ProductInfoGetAll, ProductInfoViewModel.class, new TypeToken<Feedback<List<ProductInfoViewModel>>>() {
        }.getType());
    }

    public void Get(int ProductId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ProductId;
        Current.GetService(this, Url, ServiceMethodType.ProductGet, ProductViewModel.class, new TypeToken<Feedback<ProductViewModel>>() {
        }.getType());
    }

    public void Delete(int ProductId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ProductId;
        Current.DeleteService(this, Url, ServiceMethodType.ProductDelete, ProductViewModel.class, new TypeToken<Feedback<ProductViewModel>>() {
        }.getType());
    }

    public void Add(ProductViewModel ViewModel) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PostService(this, Url, JsonViewModel, ServiceMethodType.ProductAdd, ProductViewModel.class, new TypeToken<Feedback<ProductViewModel>>() {
        }.getType());
    }

    public void Set(ProductViewModel ViewModel, int ProductId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ProductId;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PutService(this, Url, JsonViewModel, ServiceMethodType.ProductSet, ProductViewModel.class, new TypeToken<Feedback<ProductViewModel>>() {
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
