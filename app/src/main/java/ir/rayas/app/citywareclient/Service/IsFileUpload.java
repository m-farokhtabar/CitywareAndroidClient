package ir.rayas.app.citywareclient.Service;

import com.android.volley.VolleyError;

import java.lang.reflect.Type;

import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;

/**
 * Created by Hajar on 9/6/2018.
 */

public interface IsFileUpload {

    <T> void OnSuccess(String Response, ServiceMethodType ServiceMethod, Class<T> OutputClass, Type OutputClassType);
    <T> void OnError(VolleyError volleyError, ServiceMethodType ServiceMethod, Class<T> OutputClass);
}
