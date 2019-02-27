package ir.rayas.app.citywareclient.Share.Helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import ir.rayas.app.citywareclient.Service.Helper.AppController;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;

/**
 * Created by Programmer on 2/5/2018.
 * مدیریت حافظه کش برنامه
 */
public class SharedPreferenceManager {
    private SharedPreferences Current = null;
    public SharedPreferenceManager() {
        Current = AppController.getInstance().getSharedPreferences(DefaultConstant.PreferenceFileName,Context.MODE_PRIVATE);
    }

    /**
     * پاک کردن کلید مربوطه
     * @param Key
     */
    public void Clear(String Key)
    {
        SharedPreferences.Editor Editor = Current.edit();
        Editor.remove(Key);
        Editor.apply();
    }

    /**
     * کلید مورد نظر وجود دارد یا خیر
     * @param Key
     * @return
     */
    public boolean IsContain(String Key)
    {
        boolean Output = false;
        try {
            if (Current.contains(Key))
                Output =  true;
        }
        catch (Exception Ex)
        {

        }
        return Output;
    }

    /**
     * یک مقدار رشته ای را در حافظه ذخیره می نماید
     * @param Key
     * @param Value
     */
    public boolean SetString(String Key,String Value)
    {
        boolean Output = false;
        try {
            SharedPreferences.Editor Editor = Current.edit();
            Editor.putString(Key,Value);
            Editor.apply();
            Output = true;
        }
        catch(Exception Ex)
        {

        }
        return Output;
    }

    /**
     * یک کلاس در حافظه داخلی ذخیره میکند
     * @param Key نام کلید
     * @param Value شی کلاس
     * @param <T> نوع
     * @return
     */
    public <T> boolean SetClass(String Key,T Value)
    {
        boolean Output = false;
        try {
            SharedPreferences.Editor Editor = Current.edit();
            Gson gson = new Gson();
            String JsonValue = gson.toJson(Value);
            Editor.putString(Key,JsonValue);
            Editor.apply();
            Output = true;
        }
        catch(Exception Ex)
        {

        }
        return Output;
    }

    /**
     * دریافت کلاس از حافظه داخلی
     * @param Key نام کلید
     * @param <T> نوع کلاس
     * @return
     */
    public <T> T GetClass(String Key,Class<T> type)
    {
        T Output = null;
        try{
            String JsonValue  = Current.getString(Key,null);
            if (JsonValue!=null)
            {
                Gson gson = new Gson();
                Output  = gson.fromJson(JsonValue,type);
            }
        }
        catch (Exception Ex)
        {

        }
        return Output;
    }

    /**
     * دریافت مقدار رشته ای از حافظه داخلی
     * @param Key نام کلید
     * @return
     */
    public String GetString(String Key)
    {
        String Output = null;
        try{
            Output  = Current.getString(Key,null);
        }
        catch (Exception Ex)
        {

        }
        return Output;
    }
}
