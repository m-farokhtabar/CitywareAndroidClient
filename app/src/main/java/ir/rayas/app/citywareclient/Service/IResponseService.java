package ir.rayas.app.citywareclient.Service;

import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;

/**
 * Created by Programmer on 2/6/2018.
 * این اینترفیس در زمانی که می خواهیم پاسخ را به اکتیویتی ارسال کنیم مورد استفاده قرار می گیرد
 */

public interface IResponseService {
    <T> void OnResponse(T Data,ServiceMethodType ServiceMethod);
}
