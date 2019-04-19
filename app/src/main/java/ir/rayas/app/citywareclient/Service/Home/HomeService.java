package ir.rayas.app.citywareclient.Service.Home;

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
import ir.rayas.app.citywareclient.ViewModel.Home.BusinessPosterInfoViewModel;


public class HomeService implements IService {

    private String ControllerName = "Home";
    private String ActionAll = "All";
    private String ControllerPage = "Page";
    private String ControllerRegion = "Region";
    private String ControllerPosition = "Position";
    private String ControllerCategory = "Category";
    private String ControllerTop = "Top";


    private IResponseService ResponseService;

    public HomeService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }


    public void GetAll(int QueryType, int GpsRangeInKm, boolean ShowAllRegion, double latitude, double longitude, boolean ShowAllCategory, int Page) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionAll + "/" + QueryType + "/" + ControllerRegion + "/" + GpsRangeInKm + "/" + ShowAllRegion + "/" + ControllerPosition + "/" + latitude
                + "/" + longitude + "/" + ControllerCategory + "/" + ShowAllCategory + "/" + ControllerPage + "/" + Page;
        Current.GetService(this, Url, ServiceMethodType.BusinessPosterInfoGetAll, BusinessPosterInfoViewModel.class, new TypeToken<Feedback<List<BusinessPosterInfoViewModel>>>() {
        }.getType());
    }


    public void GetAllTop( int GpsRangeInKm, boolean ShowAllRegion, double latitude, double longitude, boolean ShowAllCategory, int Page) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionAll + "/" + ControllerTop + "/" + ControllerRegion + "/" + GpsRangeInKm + "/" + ShowAllRegion + "/" + ControllerPosition + "/" + latitude
                + "/" + longitude + "/" + ControllerCategory + "/" + ShowAllCategory + "/" + ControllerPage + "/" + Page;
        Current.GetService(this, Url, ServiceMethodType.BusinessPosterInfoTopGetAll, BusinessPosterInfoViewModel.class, new TypeToken<Feedback<List<BusinessPosterInfoViewModel>>>() {
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
