package ir.rayas.app.citywareclient.Share.Enum;


public enum CommissionBusinessType {


    NotSpecify("NotSpecify", 0),
    Specify("Specify", 1);

    private String Commission;
    private int ValueCommission;

    CommissionBusinessType(String Key, int Value) {
        Commission = Key;
        ValueCommission = Value;
    }

    public int GetCommission() {
        return ValueCommission;
    }
}
