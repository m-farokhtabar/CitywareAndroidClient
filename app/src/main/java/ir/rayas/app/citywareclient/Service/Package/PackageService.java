package ir.rayas.app.citywareclient.Service.Package;

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
import ir.rayas.app.citywareclient.ViewModel.Package.OutputPackageTransactionViewModel;

/**
 * Created by Hajar on 11/3/2018.
 */

public class PackageService implements IService {

    private String ControllerName = "Package/Purchased";
    private String ActionGetAll = "All/Page";
    private IResponseService ResponseService;

    public PackageService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }

    public void GetAll(int PageNumber) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionGetAll + "/" + PageNumber;
        Current.GetService(this, Url, ServiceMethodType.UserPackageGetAll, OutputPackageTransactionViewModel.class, new TypeToken<Feedback<List<OutputPackageTransactionViewModel>>>() {
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

