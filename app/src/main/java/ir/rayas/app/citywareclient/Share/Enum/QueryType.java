package ir.rayas.app.citywareclient.Share.Enum;


// نوع درخواست صفحه اصلی رو مشخص می کند
//نوع درخواست را مشخص می کند - 0 جدیدترین ها، 1 پرامتیاز ترین ها یا همان ستاره دارها، 2 پربازدیدترین ها
// 3 پوسترهای ناپ، 4 نمایش پوستر برای گرید صفحه جستجو

public enum QueryType {

    New("New", 0),
    Star("Star", 1),
    Visit("Visit", 2),
    Top("Top", 3),
    Search("Search", 4);

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
