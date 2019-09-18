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

    private String ControllerName = "Home/All";
    private String ControllerPage = "Page";
    private String ControllerRegion = "Region";
    private String ControllerCategory = "Category";
    private String ControllerLocation = "Location";
    private String ControllerBookmark = "Bookmark";


    private IResponseService ResponseService;

    public HomeService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }


    public void GetAll(int QueryType, Integer BusinessCategoryId, Integer RegionId, Integer GpsRangeInKm, Double latitude, Double longitude, int Page, int PageItems) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + QueryType + "/" + ControllerCategory + "/" +
                BusinessCategoryId + "/" + ControllerRegion + "/" + RegionId + "/" + ControllerLocation
                + "/" + GpsRangeInKm + "/" + latitude + "/" + longitude + "/" + ControllerPage + "/" + Page + "/" + PageItems;
        Current.GetService(this, Url, ServiceMethodType.BusinessPosterInfoGetAll, BusinessPosterInfoViewModel.class, new TypeToken<Feedback<List<BusinessPosterInfoViewModel>>>() {
        }.getType());
    }

    public void GetAllTop(int QueryType, Integer BusinessCategoryId, Integer RegionId, Integer GpsRangeInKm, Double latitude, Double longitude, int Page , int PageItems) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + QueryType + "/" + ControllerCategory + "/" +
                BusinessCategoryId + "/" + ControllerRegion + "/" + RegionId + "/" + ControllerLocation
                + "/" + GpsRangeInKm + "/" + latitude + "/" + longitude + "/" + ControllerPage + "/" + Page + "/" + PageItems;
        Current.GetService(this, Url, ServiceMethodType.BusinessPosterInfoTopGetAll, BusinessPosterInfoViewModel.class, new TypeToken<Feedback<List<BusinessPosterInfoViewModel>>>() {
        }.getType());
    }

    public void GetAllBookmark(int Page , int PageItems) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ControllerBookmark + "/" + ControllerPage + "/" + Page + "/" + PageItems;
        Current.GetService(this, Url, ServiceMethodType.BookmarkPosterInfoGetAll, BusinessPosterInfoViewModel.class, new TypeToken<Feedback<List<BusinessPosterInfoViewModel>>>() {
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
