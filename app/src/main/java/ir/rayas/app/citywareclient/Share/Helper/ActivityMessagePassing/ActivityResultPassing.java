package ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing;

import java.util.Stack;

/**
 * Created by Programmer on 10/30/2018.
 */

public class ActivityResultPassing {
    private static Stack<ActivityResult> ResultsInStack = null;

    /**
     *برداشت اطلاعات از پشته جهت استفاده در اکتیویتی مقصد
     * @return
     */
    public static ActivityResult Pop(){
        ActivityResult Result =null;
        if (ResultsInStack!=null && ResultsInStack.size()>0){
            Result =  ResultsInStack.pop();
        }
        return  Result;
    }

    /**
     * ثبت نتیجه اکتیویتی در پشته
     * @param Result اطلاعاتی که اکتیویتی می خواهد به یک اکتیویتی دیگر ارسال کند
     */
    public static void Push(ActivityResult Result){
        if (ResultsInStack==null){
            ResultsInStack = new Stack<>();
        }
        ResultsInStack.push(Result);
    }
//    public static bool IsEmpty(){
//
//    }

}
