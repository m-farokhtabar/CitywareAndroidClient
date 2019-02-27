package ir.rayas.app.citywareclient.Service.Definition;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import ir.rayas.app.citywareclient.Service.BaseService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.IService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.ViewModel.Definition.BusinessCategoryViewModel;

/**
 * Created by Hajar on 8/16/2018.
 */

public class BusinessCategoryService implements IService {

    private String ControllerName = "BusinessCategory";
    private IResponseService ResponseService;
    private String ActionGetAllTree = "Tree";

    public BusinessCategoryService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }

    public void GetAllTree() {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionGetAllTree;
        Current.GetService(this,Url, ServiceMethodType.BusinessCategoryTreeGet,BusinessCategoryViewModel.class, new TypeToken<Feedback<BusinessCategoryViewModel>>() {}.getType());
    }

    @Override
    public <T> void OnSuccess(String Response, ServiceMethodType ServiceMethod, Class<T> OutputClass, Type OutputClassType) {
        Utility.HandleServiceSuccess(ResponseService,Response,ServiceMethod,OutputClass,OutputClassType);
    }

    @Override
    public <T> void OnError(VolleyError volleyError, ServiceMethodType ServiceMethod, Class<T> OutputClass) {
        Utility.HandleServiceError(ResponseService,volleyError,ServiceMethod,OutputClass);
    }
}
