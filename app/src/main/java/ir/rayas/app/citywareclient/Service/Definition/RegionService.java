package ir.rayas.app.citywareclient.Service.Definition;

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
import ir.rayas.app.citywareclient.ViewModel.Definition.RegionViewModel;


public class RegionService implements IService
{
    private String ControllerName = "Region";
    private IResponseService ResponseService;

    public RegionService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }

    public void Get(int RegionId) {
        String ActionGet = "Get";
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionGet + "/" + String.valueOf(RegionId);
        Current.GetService(this,Url, ServiceMethodType.RegionGet,RegionViewModel.class, new TypeToken<Feedback<RegionViewModel>>() {}.getType());
    }

    public void GetChildren(int RegionId) {
        String ActionGetChildren = "GetChildren";
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionGetChildren + "/" + String.valueOf(RegionId);
        Current.GetService(this,Url, ServiceMethodType.RegionChildrenGet,RegionViewModel.class, new TypeToken<Feedback<List<RegionViewModel>>>() {}.getType());
    }

    public void GetAllTree() {
        String ActionGetAllTree = "GetAllTree";
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionGetAllTree;
        Current.GetService(this,Url, ServiceMethodType.RegionAllTreeGet,RegionViewModel.class, new TypeToken<Feedback<RegionViewModel>>() {}.getType());
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
