package ir.rayas.app.citywareclient.Service.Factor;

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
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorStatusViewModel;

/**
 * Created by Hajar on 3/4/2019.
 */

public class FactorStatusService implements IService {

    private String ControllerName = "FactorStatus";
    private String ActionAll = "All";

    private IResponseService ResponseService;

    public FactorStatusService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }


    public void GetAll() {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/"  + ActionAll ;
        Current.GetService(this, Url, ServiceMethodType.FactorStatusGetAll, FactorStatusViewModel.class, new TypeToken<Feedback<List<FactorStatusViewModel>>>() {
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
