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
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorInViewModel;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorViewModel;

/**
 * Created by Hajar on 2/21/2019.
 */

public class BusinessFactorService implements IService {

    private String ControllerName = "Factor";
    private String ActionBusiness = "Business";
    private String ActionAll = "All";
    private String ActionPage = "Page";

    private IResponseService ResponseService;

    public BusinessFactorService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }


    public void GetAll(int BusinessId, int Page) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionBusiness + "/" + BusinessId + "/" + ActionAll + "/" + ActionPage + "/" + Page;
        Current.GetService(this, Url, ServiceMethodType.BusinessFactorGetAll, FactorViewModel.class, new TypeToken<Feedback<List<FactorViewModel>>>() {
        }.getType());
    }

    public void Delete(int FactorId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionBusiness + "/" + FactorId;
        Current.DeleteService(this, Url, ServiceMethodType.BusinessFactorDelete, FactorViewModel.class, new TypeToken<Feedback<FactorViewModel>>() {
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
