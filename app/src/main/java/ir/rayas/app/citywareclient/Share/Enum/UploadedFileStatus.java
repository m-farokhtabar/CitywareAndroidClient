package ir.rayas.app.citywareclient.Share.Enum;

/** وضعیت فایلهای ارسالی برای آپلود
 * Created by Programmer on 8/26/2018.
 */

public enum UploadedFileStatus {
    //به طور کامل آپلود شده
    FileIsOk,
    //فایل ارسالی خالی بوده
    FileIsEmpty,
    //اندازه فایل ارسالی بیشتر از حد مجاز بوده
    FileIsMoreThanSize,
    //فایل ارسالی تصویر نبوده
    FileFormatIsNotImage
}
