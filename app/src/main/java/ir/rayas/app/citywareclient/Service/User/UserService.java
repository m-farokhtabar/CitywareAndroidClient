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
import ir.rayas.app.citywareclient.ViewModel.Search.OutUserSearchViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.UserViewModel;


public class UserService implements IService {
    private String ControllerName = "User";


    private IResponseService ResponseService;

    public UserService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }

    public void Add(UserViewModel ViewModel) {
        BaseService Current = new BaseService();
        String actionAdd = "Add";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + actionAdd;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PostService(this, Url, JsonViewModel, ServiceMethodType.UserAdd, AccountViewModel.class, new TypeToken<Feedback<UserViewModel>>() {
        }.getType());
    }

    public void Get(int UserId) {
        BaseService Current = new BaseService();
        String actionGet = "Get";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + actionGet + "/" + String.valueOf(UserId);
        Current.GetService(this, Url, ServiceMethodType.UserGet, UserViewModel.class, new TypeToken<Feedback<UserViewModel>>() {
        }.getType());
    }

    public void Set(UserViewModel ViewModel) {
        BaseService Current = new BaseService();
        String actionSet = "Set";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + actionSet;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PutService(this, Url, JsonViewModel, ServiceMethodType.UserSet, AccountViewModel.class, new TypeToken<Feedback<UserViewModel>>() {
        }.getType());
    }

    public void GetSearch(int PageNumber, String SearchText) {
        BaseService Current = new BaseService();
        String actionSearch = "Search";
        String text = "Text";
        String page = "Page";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + actionSearch + "/" + page + "/" + PageNumber + "/?" + text + "=" + SearchText;
        Current.GetService(this, Url, ServiceMethodType.SearchGet, OutUserSearchViewModel.class, new TypeToken<Feedback<List<OutUserSearchViewModel>>>() {
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
