package ir.rayas.app.citywareclient.Share.Enum;



public enum PriorityType {

    Normal(0, "معمولی"),
    Much(1, "زیاد"),
    VeryMuch(2, "خیلی زیاد");

    private int priorityType;
    private String ValuePriorityType;

    PriorityType(int Key, String Value) {
        priorityType = Key;
        ValuePriorityType = Value;
    }

    public String getValuePriorityType() {
        return ValuePriorityType;
    }

    public int getPriorityType() {
        return priorityType;
    }
}
