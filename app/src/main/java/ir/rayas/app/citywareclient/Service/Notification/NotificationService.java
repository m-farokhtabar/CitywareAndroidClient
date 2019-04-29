package ir.rayas.app.citywareclient.Service.Notification;

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
import ir.rayas.app.citywareclient.ViewModel.Notification.NotificationItemStatusOutViewModel;
import ir.rayas.app.citywareclient.ViewModel.Notification.NotificationViewModel;

public class NotificationService implements IService {

    private String ControllerName = "Notification";
    private IResponseService ResponseService;

    public NotificationService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }

    public void GetAll(int ItemId) {
        BaseService Current = new BaseService();
        String actionGetAll = "All/After";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + actionGetAll + "/" + ItemId;
        Current.GetService(this, Url, ServiceMethodType.NotificationGetAll, NotificationViewModel.class, new TypeToken<Feedback<List<NotificationViewModel>>>() {
        }.getType());
    }

    public void Delete(int BusinessId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + BusinessId;
        Current.DeleteService(this, Url, ServiceMethodType.BookmarkDelete, NotificationViewModel.class, new TypeToken<Feedback<NotificationViewModel>>() {
        }.getType());
    }

    public void Set(NotificationItemStatusOutViewModel ViewModel) {
        BaseService Current = new BaseService();
        String itemStatus = "ItemStatus";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + itemStatus;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PutService(this, Url, JsonViewModel, ServiceMethodType.NotificationSetStatus, NotificationItemStatusOutViewModel.class, new TypeToken<Feedback<Boolean>>() {
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
