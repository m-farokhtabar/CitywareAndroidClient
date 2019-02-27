package ir.rayas.app.citywareclient.Service;

import android.content.Context;

import com.android.volley.VolleyError;

import java.lang.reflect.Type;

import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;

/**
 * Created by Programmer on 2/5/2018.
 * لایه سرویس جه صدا زدن تابع
 */

public interface IService {
    <T> void OnSuccess(String Response,ServiceMethodType ServiceMethod,Class<T> OutputClass,Type OutputClassType);
    <T> void OnError(VolleyError volleyError,ServiceMethodType ServiceMethod,Class<T> OutputClass);
}
