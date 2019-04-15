package ir.rayas.app.citywareclient.Service.Poster;

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
import ir.rayas.app.citywareclient.ViewModel.Basket.StandardOrderItemViewModel;
import ir.rayas.app.citywareclient.ViewModel.Poster.BuyPosterViewModel;
import ir.rayas.app.citywareclient.ViewModel.Poster.EditPurchasedPosterViewModel;
import ir.rayas.app.citywareclient.ViewModel.Poster.ExtendBuyPosterViewModel;
import ir.rayas.app.citywareclient.ViewModel.Poster.PosterTypeViewModel;
import ir.rayas.app.citywareclient.ViewModel.Poster.PurchasedPosterViewModel;


public class PosterService implements IService {

    private String ControllerName = "Poster";
    private String ActionGetAll = "All/Page";
    private String ActionAll = "All";
    private String Page = "Page/";
    private String ActionGetAllBusiness = "All/Business";
    private String ActionValid = "Valid";
    private String ActionExpired = "Expired";
    private String ActionType = "Type";
    private String ActionExtend = "Extend";
    private String ActionPurchase = "Purchase";


    private IResponseService ResponseService;

    public PosterService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }

    public void GetAll(int PageNumber) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionGetAll + "/" + PageNumber;
        Current.GetService(this, Url, ServiceMethodType.UserPosterGetAll, PurchasedPosterViewModel.class, new TypeToken<Feedback<List<PurchasedPosterViewModel>>>() {
        }.getType());
    }

    public void Get(int PosterId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + PosterId;
        Current.GetService(this, Url, ServiceMethodType.UserPosterGet, PurchasedPosterViewModel.class, new TypeToken<Feedback<PurchasedPosterViewModel>>() {
        }.getType());
    }

    public void GetAllExpired(int PageNumber) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionAll + "/" + ActionExpired + "/" + Page + "/" + PageNumber;
        Current.GetService(this, Url, ServiceMethodType.UserPosterExpiredGet, PurchasedPosterViewModel.class, new TypeToken<Feedback<List<PurchasedPosterViewModel>>>() {
        }.getType());
    }

    public void GetAllValid(int PageNumber) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionAll + "/" + ActionValid + "/" + Page + "/" + PageNumber;
        Current.GetService(this, Url, ServiceMethodType.UserPosterValidGet, PurchasedPosterViewModel.class, new TypeToken<Feedback<List<PurchasedPosterViewModel>>>() {
        }.getType());
    }

    public void GetAllBusiness(int PageNumber, int BusinessId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionGetAllBusiness + "/" + BusinessId + "/" + Page + PageNumber;
        Current.GetService(this, Url, ServiceMethodType.UserPosterGetAll, PurchasedPosterViewModel.class, new TypeToken<Feedback<List<PurchasedPosterViewModel>>>() {
        }.getType());
    }

    public void EditExtendPoster(ExtendBuyPosterViewModel ViewModel) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionExtend;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PutService(this, Url, JsonViewModel, ServiceMethodType.ExtendPosterEdit, ExtendBuyPosterViewModel.class, new TypeToken<Feedback<PurchasedPosterViewModel>>() {
        }.getType());
    }


    public void GetAllPosterType(int PageNumber) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionType + "/" + ActionGetAll + "/" + PageNumber;
        Current.GetService(this, Url, ServiceMethodType.PosterTypeGetAll, PosterTypeViewModel.class, new TypeToken<Feedback<List<PosterTypeViewModel>>>() {
        }.getType());
    }

    public void GetPosterType(int PosterTypeId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionType + "/" + PosterTypeId;
        Current.GetService(this, Url, ServiceMethodType.PosterTypeGet, PosterTypeViewModel.class, new TypeToken<Feedback<PosterTypeViewModel>>() {
        }.getType());
    }

    public void EditPurchasedPoster(EditPurchasedPosterViewModel ViewModel) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PutService(this, Url, JsonViewModel, ServiceMethodType.PurchasedPosterEdit, EditPurchasedPosterViewModel.class, new TypeToken<Feedback<PurchasedPosterViewModel>>() {
        }.getType());
    }

    public void AddPurchasedPoster(BuyPosterViewModel ViewModel) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionPurchase;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PostService(this, Url, JsonViewModel, ServiceMethodType.PurchasedPosterAdd, BuyPosterViewModel.class, new TypeToken<Feedback<PurchasedPosterViewModel>>() {
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
