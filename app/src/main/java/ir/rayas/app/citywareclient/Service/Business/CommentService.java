package ir.rayas.app.citywareclient.Service.Business;

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
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessContactViewModel;
import ir.rayas.app.citywareclient.ViewModel.Business.CommentInViewModel;
import ir.rayas.app.citywareclient.ViewModel.Business.CommentViewModel;

/**
 * Created by Hajar on 11/25/2018.
 */

public class CommentService implements IService {

    private String ControllerName = "Comment";
    private String ActionGetAll = "All/Business";
    private String Page = "page";
    private IResponseService ResponseService;

    public CommentService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }

    public void GetAll(int BusinessId, int PageNumber) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionGetAll + "/" + BusinessId + "/" + Page + "/" + PageNumber;
        Current.GetService(this, Url, ServiceMethodType.BusinessCommentGetAll, CommentViewModel.class, new TypeToken<Feedback<List<CommentViewModel>>>() {
        }.getType());
    }

    public void Add(CommentInViewModel ViewModel) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PostService(this, Url, JsonViewModel, ServiceMethodType.BusinessCommentAdd, CommentInViewModel.class, new TypeToken<Feedback<CommentViewModel>>() {
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
