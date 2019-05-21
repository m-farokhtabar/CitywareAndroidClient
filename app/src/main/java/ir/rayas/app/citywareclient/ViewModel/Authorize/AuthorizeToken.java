package ir.rayas.app.citywareclient.ViewModel.Authorize;

/**
 * Created by Programmer on 5/20/2019.
 */

import java.util.Random;
import com.google.gson.Gson;
import ir.rayas.app.citywareclient.Share.Utility.Utility;

/**
 * کلاس اطلاعات دسترسی برای سوریس های که از سیستم دسترسی کاربر استفاده نمی کنند
 * برای سرویس های عمومی استفاده می شود
 */
public class AuthorizeToken {
    /**
     * نام کاربری
     */
    private String U;
    /**
     * کلمه عبور
     */
    private String P;
    /**
     * مقدار دهی اولیه مقادیر
     * @param u نام کاربری
     * @param p کلمه عبور
     */
    public AuthorizeToken(String u, String p) {
        Random Rnd = new Random();
        U = Utility.GenerateRandomString(Rnd.nextInt(3) + 3) + u + Utility.GenerateRandomString(Rnd.nextInt(4) + 3);
        P = Utility.GenerateRandomString(Rnd.nextInt(2) + 4) + p + Utility.GenerateRandomString(Rnd.nextInt(3) + 2);
    }

    /**
     * تولید توکن رمز شده
     * @return -رشته  توکن رمز شده
     */
    public String GetEncryptedToken(){
        return Utility.Encrypt(new Gson().toJson(this));
    }

    public String getU() {
        return U;
    }

    public String getP() {
        return P;
    }
}