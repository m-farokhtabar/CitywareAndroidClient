package ir.rayas.app.citywareclient.Share.Feedback;

/**
 * Created by Programmer on 2/6/2018.
 */

/// <summary>
/// کلاس لفاف جهت ارسال داده ها به کلاینت به همراه اطلاعات اضافه
/// </summary>
/// <typeparam name="T">هر نوعی که قرار است به کلاینت ارسال شود</typeparam>
public class Feedback<T>
{
    public Feedback() {
    }

    public Feedback(int status, int messageType, T value, String message, String exceptionMessage) {
        Status = status;
        MessageType = messageType;
        Value = value;
        Message = message;
        ExceptionMessage = exceptionMessage;
    }

    /// <summary>
    /// برای تعیین وضعیت عملیات انجام شده
    /// </summary>
    private int Status;
    /// <summary>
    /// برای تشخیص warning - info - error
    /// </summary>
    private int MessageType;
    /// <summary>
    /// داده
    /// </summary>
    private T Value;
    /// <summary>
    /// پیغام جهت نمایش به کاربر
    /// </summary>
    private String Message;
    /// <summary>
    /// پیغام خطا جهت مشاهده برنامه نویس
    /// </summary>
    private String ExceptionMessage;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getMessageType() {
        return MessageType;
    }

    public void setMessageType(int messageType) {
        MessageType = messageType;
    }

    public T getValue() {
        return Value;
    }

    public void setValue(T value) {
        Value = value;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getExceptionMessage() {
        return ExceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        ExceptionMessage = exceptionMessage;
    }
}

