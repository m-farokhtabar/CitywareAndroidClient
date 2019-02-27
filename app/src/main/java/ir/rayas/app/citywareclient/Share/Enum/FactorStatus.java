package ir.rayas.app.citywareclient.Share.Enum;

/** وضعیت  فاکتور
 * Created by Programmer on 1/8/2019.
 */
public enum FactorStatus {
    /**
     * کاربر در حال سفارش
     */
    Ordering(0),
    /**
     * توسط کسب کار مشاهده نشده
     */
    NotShow(1),
    /**
     * در حال بررسی سفارش
     */
    Reviewing(2),
    /**
     * در حال آماده سازی
     */
    Preparing(3),
    /**
     *تحویل داده شده به پیک
     */
    DeliveredToCourier(4),
    /**
     *در حال ارسال
     */
    Sending(5),
    /**
     * تحویل داده شده
     */
    Delivered(6),
    /**
     * دریافت شده
     */
    Received(7),
    /**
     * کنسل شده توسط کاربر
     */
    CanceledByUser(8),
    /**
     * کنسل  شده توسط کسب و کار
     */
    CanceledByBusiness(9),
    /**
     * موارد دیگر
     */
    Etc(10);
    private int Id;

    public int getId() {
        return Id;
    }

    FactorStatus(int id) {
        Id = id;
    }
}
