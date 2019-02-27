package ir.rayas.app.citywareclient.Service.Uploader;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import ir.rayas.app.citywareclient.Service.BaseService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.IsFileUpload;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.ViewModel.Uploader.FileViewModel;

/**
 * Created by Hajar on 8/30/2018.
 */

public class ImageService implements IsFileUpload {
    private String ControllerName = "Uploader";
    private IResponseService ResponseService;

    public ImageService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }


    public void Add(Map<String, String> params, String mimeType, byte[] multipartBody) {

        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/Image";
        Current.PostFileService(this, Url, params, mimeType, multipartBody, ServiceMethodType.UploadFile, FileViewModel.class, new TypeToken<Feedback<List<FileViewModel>>>() {
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