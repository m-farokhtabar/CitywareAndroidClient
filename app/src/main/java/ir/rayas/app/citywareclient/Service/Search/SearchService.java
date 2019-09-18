package ir.rayas.app.citywareclient.Service.Search;

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
import ir.rayas.app.citywareclient.ViewModel.Search.SearchBusinessResultViewModel;
import ir.rayas.app.citywareclient.ViewModel.Search.SearchProductResultViewModel;
import ir.rayas.app.citywareclient.ViewModel.Search.SearchResultViewModel;


public class SearchService implements IService {

    private String ControllerName = "Home/Search/Category";
    private String ControllerRegion = "Region";
    private String ControllerLimitOn = "LimitOn";
    private String ControllerLocation = "Location";
    private String ControllerPage = "Page";
    private String ControllerText = "?Text=";


    private IResponseService ResponseService;

    public SearchService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }


    public void GetAll(Integer BusinessCategoryId, Integer RegionId, Integer GpsRangeInKm, Double latitude, Double longitude, int SearchType, int DelivaryType, int Page, String TextSearch) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + BusinessCategoryId + "/" + ControllerRegion + "/" +
                RegionId + "/" + ControllerLocation + "/" + GpsRangeInKm + "/" + latitude + "/" + longitude + "/" + ControllerLimitOn + "/" +
                SearchType + "/" + DelivaryType + "/" + ControllerPage + "/" + Page + "/" + ControllerText + TextSearch;
        Current.GetService(this, Url, ServiceMethodType.SearchResultGet, SearchResultViewModel.class, new TypeToken<Feedback<List<SearchResultViewModel>>>() {
        }.getType());
    }

    public void GetAllBusinessSearch(Integer BusinessCategoryId, Integer RegionId, Integer GpsRangeInKm, Double latitude, Double longitude, int DelivaryType, int Page, int PageItems, String TextSearch) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + "Home/BusinessSearch/Category" + "/" + BusinessCategoryId + "/" + ControllerRegion + "/" +
                RegionId + "/" + ControllerLocation + "/" + GpsRangeInKm + "/" + latitude + "/" + longitude + "/" + ControllerLimitOn + "/" +
                DelivaryType + "/" + ControllerPage + "/" + Page + "/" + PageItems + "/" + ControllerText + TextSearch;
        Current.GetService(this, Url, ServiceMethodType.SearchResultBusinessGet, SearchBusinessResultViewModel.class, new TypeToken<Feedback<List<SearchBusinessResultViewModel>>>() {
        }.getType());
    }

    public void GetAllProductSearch(Integer BusinessCategoryId, Integer RegionId, Integer GpsRangeInKm, Double latitude, Double longitude, int DelivaryType, int Page, int PageItems, String TextSearch) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + "Home/ProductSearch/Category" + "/" + BusinessCategoryId + "/" + ControllerRegion + "/" +
                RegionId + "/" + ControllerLocation + "/" + GpsRangeInKm + "/" + latitude + "/" + longitude + "/" + ControllerLimitOn + "/" +
                DelivaryType + "/" + ControllerPage + "/" + Page + "/" + PageItems + "/"+ ControllerText + TextSearch;
        Current.GetService(this, Url, ServiceMethodType.SearchResultProductGet, SearchProductResultViewModel.class, new TypeToken<Feedback<List<SearchProductResultViewModel>>>() {
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
