package ir.rayas.app.citywareclient.Share.Enum;


public enum StatusDiscountType {

    NotUse("NotUse", 0),
    Use("Use", 1),
    Expire("Expire", 2);

    private String StatusDiscount;
    private int ValueStatusDiscount;

    StatusDiscountType(String Key, int Value) {
        StatusDiscount = Key;
        ValueStatusDiscount = Value;
    }

    public int GetStatusDiscount() {
        return ValueStatusDiscount;
    }
}
