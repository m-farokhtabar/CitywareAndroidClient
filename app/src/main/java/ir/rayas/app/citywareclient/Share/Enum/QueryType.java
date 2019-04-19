package ir.rayas.app.citywareclient.Share.Enum;


// نوع درخواست صفحه اصلی رو مشخص می کند
//نوع درخواست را مشخص می کند - 0 جدیدترین ها، 1 پرامتیاز ترین ها یا همان ستاره دارها، 2 پربازدیدترین ها

public enum QueryType {

    New("New", 0),
    Star("Star", 1),
    Visit("Visit", 2);

    private String queryType;
    private int ValueQueryType;

    QueryType(String Key, int Value) {
        queryType = Key;
        ValueQueryType = Value;
    }

    public int GetQueryType() {
        return ValueQueryType;
    }
}
