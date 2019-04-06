package ir.rayas.app.citywareclient.ViewModel.Club;

/**
 * Created by Hajar on 3/29/2019.
 */

public class UserConsumePointViewModel {

    private int Id;

    //کاربری که امتیاز را دریافت کرده
    private int UserId;

    //نام جایزه جهت آرشیو
    private String PrizeTitle;

    //نوع عملیاتی که انجام داده است
    private int PrizeId;

    //امتیاز دریافتی آرشیو می شود چون ممکن امتیاز بعدا تغییر کند
    private Double Point;

    //تاریخ دریافت جایزه
    private String Create;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getPrizeTitle() {
        return PrizeTitle;
    }

    public void setPrizeTitle(String prizeTitle) {
        PrizeTitle = prizeTitle;
    }

    public int getPrizeId() {
        return PrizeId;
    }

    public void setPrizeId(int prizeId) {
        PrizeId = prizeId;
    }

    public Double getPoint() {
        return Point;
    }

    public void setPoint(Double point) {
        Point = point;
    }

    public String getCreate() {
        return Create;
    }

    public void setCreate(String create) {
        Create = create;
    }
}
