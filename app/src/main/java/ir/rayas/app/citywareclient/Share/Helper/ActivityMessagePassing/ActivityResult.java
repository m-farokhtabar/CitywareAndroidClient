package ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing;

import java.util.HashMap;

/**
 * Created by Programmer on 10/30/2018.
 */

public class ActivityResult {
    private int FromActivityId;
    private int ToActivityId;
    private HashMap<String,Object> Data;

    /**
     * ایجاد یک نتیجه از یک اکتیوتی به یک اکتیویتی دیگر
     * @param FromActivityId
     * @param ToActivityId
     * @param Data
     */
    public ActivityResult(int FromActivityId, int ToActivityId, HashMap<String,Object> Data) {
        this.FromActivityId = FromActivityId;
        this.ToActivityId = ToActivityId;
        this.Data = Data;
    }


    public int getFromActivityId() {
        return FromActivityId;
    }

    public void setFromActivityId(int FromActivityId) {
        this.FromActivityId = FromActivityId;
    }

    public int getToActivityId() {
        return ToActivityId;
    }

    public void setToActivityId(int ToActivityId) {
        this.ToActivityId = ToActivityId;
    }

    public HashMap<String,Object> getData() {
        return Data;
    }

    public void setData(HashMap<String,Object> Data) {
        this.Data = Data;
    }
}
