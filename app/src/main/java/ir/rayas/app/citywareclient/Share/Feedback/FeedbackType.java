package ir.rayas.app.citywareclient.Share.Feedback;

/**
 * Created by Programmer on 2/6/2018.
 */

public enum FeedbackType {
    CouldNotConnectToServer(0, "امکان ارتباط با سرور میسر نمی باشد."),
    CouldNotConnectToDataBase(1, "امکان ارتباط با پایگاه داده میسر نمی باشد."),
    DataIsNotFound(2, "{0}" + " " + "مورد نظر پیدا نشده است."),
    InvalidDataFormat(3, "فرمت داده ی وارد شده اشتباه می باشد." + "{0}"),
    FetchSuccessful(4, "دریافت اطلاعات با موفقیت انجام شد."),
    RegisteredSuccessful(5, "اطلاعات با موفقیت ثبت شد."),
    UpdatedSuccessful(6, "اطلاعات با موفقیت ویرایش شد."),
    DeletedSuccessful(7, "اطلاعات با موفقیت حذف شد."),
    UserIsNotActive(8, "کاربر غیر فعال بوده و  نمی تواند عملیات مورد نظر را انجام دهد. لطفا با پشتیبانی تماس حاصل فرمایید"),
    CellPhoneIsRepititive(9, "شماره همراه وارد شده قبلا در سیستم ثبت شده است"),
    TrackingCodeForUserConfirmationNotExistOrExpired(10, "کد رهگیری مورد نظر برای این کاربر وجود ندارد و یا اعتبار آن به پایان رسیده است"),
    DataIsNotActive(11, "{0}" + " " + "مورد نظر غیر فعال می باشد."),
    DataIsExpired(12, "{0}" + " " + "مورد نظر منقضی شده است."),
    ThereIsUnActivatedComment(13,  "شما برای این کسب و کار قبلا نظری ثبت کرده اید که در حال تایید می باشد"),
    DynamicMessage(100, ""),
    DontHaveAuthorize(101, "مجوز شما جهت نمایش اطلاعات به پایان رسیده است لطفا دوباره وارد برنامه را بسته دوباره وارد شوید"),
    ThereIsNoInternet(102, "امکان اتصال به اینترنت وجود ندارد"),
    ThereIsSomeProblemInApp(103, "متاسفانه در اجرای این قسمت مشکلی ایجاد شده است");

    private String Message;
    private int Id;

    FeedbackType(int Id, String Message) {
        this.Id = Id;
        this.Message = Message;
    }

    public int getId() {
        return Id;
    }

    public String getMessage() {
        return Message;
    }
}
