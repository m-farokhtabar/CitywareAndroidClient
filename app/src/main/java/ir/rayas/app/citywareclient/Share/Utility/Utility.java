package ir.rayas.app.citywareclient.Share.Utility;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.EditMoney;
import ir.rayas.app.citywareclient.Share.Layout.View.EditTextPersian;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;

/**
 * Created by Programmer on 2/5/2018.
 */

public class Utility {

    /**
     * خطا های مربوط به رویداد خطا والی در این متد مدیریت می شود
     * تمامی سرویس ها بایستی این متد را صدا بزنند
     */
    public static <T> void HandleServiceError(IResponseService ResponseService, VolleyError volleyError, ServiceMethodType ServiceMethod, Class<T> OutputClass) {
        FeedbackType CurrentFeedbackType = FeedbackType.ThereIsNoInternet;
        NetworkResponse NetworkResponse = volleyError.networkResponse;
        if (NetworkResponse != null) {
            // HTTP Status Code: 401 Unauthorized
            if (NetworkResponse != null && NetworkResponse.statusCode == 401) {
                CurrentFeedbackType = FeedbackType.DontHaveAuthorize;
            }
        }
        Feedback<T> Fb = new Feedback<>(CurrentFeedbackType.getId(), MessageType.Error.ordinal(), null, CurrentFeedbackType.getMessage(), volleyError.getMessage());
        ResponseService.OnResponse(Fb, ServiceMethod);
    }

    /**
     * در زمانی که والی اطلاعات را  درست ارسال می کند این متد داده ها را تبدیل می نماید
     *
     * @param ResponseService
     * @param Response
     * @param ServiceMethod
     * @param OutputClass
     * @param OutputClassType
     * @param <T>
     */
    public static <T> void HandleServiceSuccess(IResponseService ResponseService, String Response, ServiceMethodType ServiceMethod, Class<T> OutputClass, Type OutputClassType) {
        try {
            Gson gson = new Gson();
            Feedback<T> Data = gson.fromJson(Response, OutputClassType);
            //TODO: دراین قسمت باید کدی نوشت که خطاهای عمومی هم مثل ارتباط با سرور یا ارتباط با پایگاه داده میسر نیست هم به صورت خطا در سرور نمایش داده شو
            ResponseService.OnResponse(Data, ServiceMethod);
        } catch (Exception Ex) {
            Feedback<T> Fb = new Feedback<>(FeedbackType.InvalidDataFormat.getId(), MessageType.Error.ordinal(), null, FeedbackType.InvalidDataFormat.getMessage().replace("{0}", ""), Ex.getMessage());
            ResponseService.OnResponse(Fb, ServiceMethod);
        }
    }

    /**
     * بررسی صحت اطلاعات وارد شده کاربر
     *
     * @param ViewList انواع کامپوننت هایی که کاربر در آن دیتا وارد می کند مثل EditBox به شرط داشتن متد isValidated
     * @return
     */
    public static boolean ValidateView(View... ViewList) {
        boolean Output = true;
        for (View CurrentView : ViewList) {
            if (CurrentView instanceof EditTextPersian) {
                EditTextPersian CurrentEditText = (EditTextPersian) CurrentView;
                if (!CurrentEditText.isValidated()) {
                    CurrentEditText.ShowError();
                    Output = false;
                    break;
                }
            } else {
                if (CurrentView instanceof EditMoney) {
                    EditMoney CurrentEditText = (EditMoney) CurrentView;
                    if (!CurrentEditText.isValidated()) {
                        CurrentEditText.ShowError();
                        Output = false;
                        break;
                    }
                }
            }
        }
        return Output;
    }

    public static void ShowKeyboard(View CurrentView) {
        InputMethodManager imm = (InputMethodManager) CurrentView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(CurrentView, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * مخفی نمودن کیبرد
     *
     * @param activity
     */
    public static void HideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * بررسی این که رشته عدد است یا خیر
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (str != null && !str.isEmpty()) {
            return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
        } else
            return false;
    }

    /**
     * بررسی صحت تاریخ وارد شده
     *
     * @param DateTime تاریخ شمسی و زمان
     * @return
     */
    public static boolean CheckDateTime(String DateTime) {
        boolean IsCheckDateTime = false;

        if (DateTime != null && !DateTime.trim().equals("") && DateTime.contains("-") && DateTime.contains("/")) {
            String[] DateTimeArray = DateTime.split("[/,:,-]");
            if (DateTimeArray != null && DateTimeArray.length == 5)
                IsCheckDateTime = true;
        }

        return IsCheckDateTime;
    }

    /**
     * بررسی صحت تاریخ وارد شده
     *
     * @param Date تاریخ شمسی
     * @return
     */
    public static boolean CheckDate(String Date) {
        boolean IsCheckDateTime = false;

        if (Date != null && !Date.trim().equals("") && Date.contains("/")) {
            String[] DateTimeArray = Date.split("[/]");
            if (DateTimeArray != null && DateTimeArray.length == 3)
                IsCheckDateTime = true;
        }

        return IsCheckDateTime;
    }

    /**
     * جدا کردن قسمت تاریخ از رشته تاریخ زمان
     *
     * @param DateTime رشته ای که هم تاریخ دارد و هم زمان
     * @return
     */
    public static String GetDateFromDateTime(String DateTime) {
        String Output = "";
        if (CheckDateTime(DateTime)) {
            StringTokenizer FromDateTokens = new StringTokenizer(DateTime, "/");
            String Year = FromDateTokens.nextToken();
            String Month = FromDateTokens.nextToken();
            String FromRestToken = FromDateTokens.nextToken();
            StringTokenizer FromDayTokenizer = new StringTokenizer(FromRestToken, "-");
            String Day = FromDayTokenizer.nextToken();
            Output = Year + "/" + Month + "/" + Day;
        }
        return Output;
    }

    /**
     * مفایسه دو زمان با یکدیگر
     * -2 خطا
     * -1 زمان دومی بزرگتر
     * 0 مساوی
     * 1 زمان اولی بزرگتر
     *
     * @param Time1
     * @param Time2
     * @return
     */
    public static int CompareTime(String Time1, String Time2) {
        int Output = -2;
        try {
            String[] Time1Array = Time1.split("[:]");
            String[] Time2Array = Time2.split("[:]");
            if (Time1Array != null && Time2Array != null && Time1Array.length == 2 && Time2Array.length == 2) {
                int Hour1 = Integer.valueOf(Time1Array[0]);
                int Hour2 = Integer.valueOf(Time2Array[0]);
                int Min1 = Integer.valueOf(Time1Array[1]);
                int Min2 = Integer.valueOf(Time2Array[1]);
                if (Hour1 > Hour2) {
                    Output = 1;
                }
                else {
                    if (Hour2 > Hour1) {
                        Output = -1;
                    }
                    //یعنی ساعات مساویست
                    else {
                        if (Min1 > Min2) {
                            Output = 1;
                        }
                        else {
                            if (Min1 < Min2)
                                Output = -1;
                            //یعنی دقایق هم مساویست
                            else
                                Output = 0;
                        }
                    }
                }

            }
        } catch (Exception Ex) {

        }
        return Output;
    }

    public static List<Long> CalculateTimeDifference(String dateStart, String dateStop) {

        List<Long> OutPut = new ArrayList<>();
//        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd-HH:mm");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            long diff = d2.getTime() - d1.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            OutPut.add(diffDays);
            OutPut.add(diffHours);
            OutPut.add(diffMinutes);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return OutPut;
    }

    /**
     * جدا ساز برای اعداد صحیح
     *
     * @return
     */
    public static String GetIntegerNumberWithComma(double IntegerNumber) {
        String Output;
        DecimalFormat CommaSeparatorFormatter = new DecimalFormat("#,###,###");
        Output = CommaSeparatorFormatter.format(IntegerNumber);
        return Output;
    }

}
