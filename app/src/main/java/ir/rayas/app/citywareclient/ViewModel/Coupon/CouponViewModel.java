package ir.rayas.app.citywareclient.ViewModel.Coupon;


public class CouponViewModel {
    private  int Id;
    private  String Code;
    private  double Value;
    private  boolean IsPercent;
    private  String Validity;
    private  String Create;
    private  String Modified;
    private  boolean IsActive;

    public int getId() {
        return Id;
    }

    public String getCode() {
        return Code;
    }

    public double getValue() {
        return Value;
    }

    public boolean isPercent() {
        return IsPercent;
    }

    public String getValidity() {
        return Validity;
    }

    public String getCreate() {
        return Create;
    }

    public String getModified() {
        return Modified;
    }

    public boolean isActive() {
        return IsActive;
    }
}
