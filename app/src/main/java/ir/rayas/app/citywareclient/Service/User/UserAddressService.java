package ir.rayas.app.citywareclient.Service.User;

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
import ir.rayas.app.citywareclient.ViewModel.User.UserAddressViewModel;

/**
 * Created by Hajar on 7/19/2018.
 */

public class UserAddressService implements IService {

    private String ControllerName = "UserAddress";
    private String ActionGetAll = "All";
    private IResponseService ResponseService;

    public UserAddressService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }

    public void GetAll() {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionGetAll;
        Current.GetService(this, Url, ServiceMethodType.UserGetAllAddress, UserAddressViewModel.class, new TypeToken<Feedback<List<UserAddressViewModel>>>() {
        }.getType());
    }

    public void Add(UserAddressViewModel ViewModel) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PostService(this, Url, JsonViewModel, ServiceMethodType.UserAddAddress, UserAddressViewModel.class, new TypeToken<Feedback<UserAddressViewModel>>() {
        }.getType());
    }

    public void Get(int AddressId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + AddressId;
        Current.GetService(this, Url, ServiceMethodType.UserGetAddress, UserAddressViewModel.class, new TypeToken<Feedback<UserAddressViewModel>>() {
        }.getType());
    }

    public void Delete(int AddressId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + AddressId;
        Current.DeleteService(this, Url, ServiceMethodType.UserDeleteAddress, UserAddressViewModel.class, new TypeToken<Feedback<UserAddressViewModel>>() {
        }.getType());
    }

    public void Set(UserAddressViewModel ViewModel, int AddressId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + AddressId;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PutService(this, Url, JsonViewModel, ServiceMethodType.UserSetAddress, UserAddressViewModel.class, new TypeToken<Feedback<UserAddressViewModel>>() {
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
