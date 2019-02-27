package ir.rayas.app.citywareclient.Service.Poster;

import com.android.volley.VolleyError;
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
import ir.rayas.app.citywareclient.ViewModel.Poster.PurchasedPosterViewModel;

/**
 * Created by Hajar on 10/12/2018.
 */

public class PosterService implements IService {

    private String ControllerName = "Poster";
    private String ActionGetAll = "All/Page";
    private String Page = "Page/";
    private String ActionGetAllBusiness = "All/Business";
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
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" +  PosterId;
        Current.GetService(this, Url, ServiceMethodType.UserPosterGet, PurchasedPosterViewModel.class, new TypeToken<Feedback<PurchasedPosterViewModel>>() {
        }.getType());
    }


    public void GetAllBusiness(int PageNumber, int BusinessId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionGetAllBusiness + "/" + BusinessId + "/" + Page + PageNumber;
        Current.GetService(this, Url, ServiceMethodType.UserPosterGetAll, PurchasedPosterViewModel.class, new TypeToken<Feedback<List<PurchasedPosterViewModel>>>() {
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
