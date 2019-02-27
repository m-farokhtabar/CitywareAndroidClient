package ir.rayas.app.citywareclient.ViewModel.User;

/**
 * Created by Programmer on 7/28/2018.
 */

public class UserInfoViewModel {
    /// <summary>
    /// کد کاربر
    /// </summary>
    private int UserId;
    /// <summary>
    /// ایمیل کاربر
    /// </summary>
    ///ValidationType = Email
    ///FieldSize = InternetAddressSize
    private String Email;
    /// <summary>
    /// جنسیت
    /// </summary>                   
    private Integer SexTypeId;
    /// <summary>
    /// رنگ چشم
    /// </summary>                
    private Integer EyeColorId;
    /// <summary>
    /// رنگ مورد علاقه من
    /// </summary>                
    private Integer MyFavoriteColorId;
    /// <summary>
    /// رنگ بدن
    /// </summary>
    private Integer SkinColorId;
    /// <summary>
    /// قد
    /// </summary>        
    private double Height;
    /// <summary>
    /// وزن
    /// </summary>        
    private double Weight;
    /// <summary>
    /// بیوگرافی
    /// </summary>
    ///FieldSize = MaxOfTextSize
    private String AboutMe;
    /// <summary>
    /// تاریخ تولد
    /// </summary>
    /// 1395/01/15-22:30
    ///ValidationType = PersianDate
    ///FieldSize = DateSize
    private String BirthDate;
    /// <summary>
    /// تاریخ ازدواج
    /// </summary>
    /// 1395/01/15-22:30
    ///ValidationType = PersianDate
    ///FieldSize = DateSize
    private String MarriedDate;
    /// <summary>
    /// شماره حساب
    /// </summary>
    ///FieldSize = AccountNumberSize
    ///ValidationType = AccountNumber
    private String AccountNumber;
    /// <summary>
    /// کد بانک
    /// </summary>        
    private Integer BankTypeId;
    /// <summary>
    /// مدرک تحصیلی
    /// </summary>
    private Integer DegreeOfEducationId;
    /// <summary>
    /// آدرس وب سایت
    /// </summary>
    ///FieldSize = InternetAddressSize
    ///ValidationType = WebSite
    private String WebSite;
    /// <summary>
    /// نام غذای مورد علاقه
    /// </summary>
    ///FieldSize = TextShortSize
    private String MyFavoriteFood;
    /// <summary>
    /// شیرین مورد علاقه
    /// </summary>
    ///FieldSize = TextShortSize
    private String MyFavoriteSweet;
    /// <summary>
    /// شهر مورد علاقه جهت مسافرت
    /// </summary>                    
    ///FieldSize = TextShortSize
    private String MyFavoriteCityToTravel;
    /// <summary>
    /// برند مورد علاقه
    /// </summary>
    ///FieldSize = TextShortSize
    private String MyFavoriteBrand;
    /// <summary>
    /// هدیه مورد علاقه
    /// </summary>
    ///FieldSize = TextShortSize
    private String MyFavoriteGift;
    /// <summary>
    /// کار یا حرفه
    /// </summary>
    ///FieldSize = TextShortSize
    private String Job;
    /// <summary>
    /// نام مدرسه
    /// </summary>
    ///FieldSize = TextShortSize
    private String School;
    /// <summary>
    /// تاریخ تولد مادر 
    /// </summary>
    /// 1395/01/15-22:30
    ///ValidationType = PersianDate
    ///FieldSize = DateSize
    private String MotherBirthDay;
    /// <summary>
    /// تاریخ تولد پدر
    /// </summary>
    /// 1395/01/15-22:30
    ///ValidationType = PersianDate
    ///FieldSize = DateSize
    private String FatherBirthDay;
    /// <summary>
    /// تاریخ تولد همسر
    /// </summary>
    /// 1395/01/15-22:30
    ///ValidationType = PersianDate
    ///FieldSize = DateSize
    private String SpouseBirthDay;
    /// <summary>
    /// تاریخ تولد فرزند اول
    /// </summary>
    /// 1395/01/15-22:30
    ///ValidationType = PersianDate
    ///FieldSize = DateSize
    private String FirstChildBirthDay;
    /// <summary>
    /// تاریخ تولد فرزند دوم
    /// </summary>
    /// 1395/01/15-22:30
    ///ValidationType = PersianDate
    ///FieldSize = DateSize
    private String SecondChildBirthDay;
    /// <summary>
    /// نام تیم مورد علاقه
    /// </summary>
    ///FieldSize = TextShortSize
    private String MyFavoriteSoccerTeam;
    /// <summary>
    ///  آی دی تلگرام
    /// </summary>
    ///FieldSize = TextMediumSize
    private String TelegramId;
    /// <summary>
    /// آی دی اینستاگرام
    /// </summary>
    ///FieldSize = TextMediumSize
    private String InstagramId;
    /// <summary>
    /// تاریخ ایجاد
    /// سمت سرور
    /// </summary>
    private String Create;
    /// <summary>
    /// تاریخ ویرایش
    /// سمت سرور
    /// </summary>        
    private String Modified;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Integer getSexTypeId() {
        return SexTypeId;
    }

    public void setSexTypeId(Integer sexTypeId) {
        SexTypeId = sexTypeId;
    }

    public Integer getEyeColorId() {
        return EyeColorId;
    }

    public void setEyeColorId(Integer eyeColorId) {
        EyeColorId = eyeColorId;
    }

    public Integer getMyFavoriteColorId() {
        return MyFavoriteColorId;
    }

    public void setMyFavoriteColorId(Integer myFavoriteColorId) {
        MyFavoriteColorId = myFavoriteColorId;
    }

    public Integer getSkinColorId() {
        return SkinColorId;
    }

    public void setSkinColorId(Integer skinColorId) {
        SkinColorId = skinColorId;
    }

    public double getHeight() {
        return Height;
    }

    public void setHeight(double height) {
        Height = height;
    }

    public double getWeight() {
        return Weight;
    }

    public void setWeight(double weight) {
        Weight = weight;
    }

    public String getAboutMe() {
        return AboutMe;
    }

    public void setAboutMe(String aboutMe) {
        AboutMe = aboutMe;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }

    public String getMarriedDate() {
        return MarriedDate;
    }

    public void setMarriedDate(String marriedDate) {
        MarriedDate = marriedDate;
    }

    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
    }

    public Integer getBankTypeId() {
        return BankTypeId;
    }

    public void setBankTypeId(Integer bankTypeId) {
        BankTypeId = bankTypeId;
    }

    public Integer getDegreeOfEducationId() {
        return DegreeOfEducationId;
    }

    public void setDegreeOfEducationId(Integer degreeOfEducationId) {
        DegreeOfEducationId = degreeOfEducationId;
    }

    public String getWebSite() {
        return WebSite;
    }

    public void setWebSite(String webSite) {
        WebSite = webSite;
    }

    public String getMyFavoriteFood() {
        return MyFavoriteFood;
    }

    public void setMyFavoriteFood(String myFavoriteFood) {
        MyFavoriteFood = myFavoriteFood;
    }

    public String getMyFavoriteSweet() {
        return MyFavoriteSweet;
    }

    public void setMyFavoriteSweet(String myFavoriteSweet) {
        MyFavoriteSweet = myFavoriteSweet;
    }

    public String getMyFavoriteCityToTravel() {
        return MyFavoriteCityToTravel;
    }

    public void setMyFavoriteCityToTravel(String myFavoriteCityToTravel) {
        MyFavoriteCityToTravel = myFavoriteCityToTravel;
    }

    public String getMyFavoriteBrand() {
        return MyFavoriteBrand;
    }

    public void setMyFavoriteBrand(String myFavoriteBrand) {
        MyFavoriteBrand = myFavoriteBrand;
    }

    public String getMyFavoriteGift() {
        return MyFavoriteGift;
    }

    public void setMyFavoriteGift(String myFavoriteGift) {
        MyFavoriteGift = myFavoriteGift;
    }

    public String getJob() {
        return Job;
    }

    public void setJob(String job) {
        Job = job;
    }

    public String getSchool() {
        return School;
    }

    public void setSchool(String school) {
        School = school;
    }

    public String getMotherBirthDay() {
        return MotherBirthDay;
    }

    public void setMotherBirthDay(String motherBirthDay) {
        MotherBirthDay = motherBirthDay;
    }

    public String getFatherBirthDay() {
        return FatherBirthDay;
    }

    public void setFatherBirthDay(String fatherBirthDay) {
        FatherBirthDay = fatherBirthDay;
    }

    public String getSpouseBirthDay() {
        return SpouseBirthDay;
    }

    public void setSpouseBirthDay(String spouseBirthDay) {
        SpouseBirthDay = spouseBirthDay;
    }

    public String getFirstChildBirthDay() {
        return FirstChildBirthDay;
    }

    public void setFirstChildBirthDay(String firstChildBirthDay) {
        FirstChildBirthDay = firstChildBirthDay;
    }

    public String getSecondChildBirthDay() {
        return SecondChildBirthDay;
    }

    public void setSecondChildBirthDay(String secondChildBirthDay) {
        SecondChildBirthDay = secondChildBirthDay;
    }

    public String getMyFavoriteSoccerTeam() {
        return MyFavoriteSoccerTeam;
    }

    public void setMyFavoriteSoccerTeam(String myFavoriteSoccerTeam) {
        MyFavoriteSoccerTeam = myFavoriteSoccerTeam;
    }

    public String getTelegramId() {
        return TelegramId;
    }

    public void setTelegramId(String telegramId) {
        TelegramId = telegramId;
    }

    public String getInstagramId() {
        return InstagramId;
    }

    public void setInstagramId(String instagramId) {
        InstagramId = instagramId;
    }

    public String getCreate() {
        return Create;
    }

    public void setCreate(String create) {
        Create = create;
    }

    public String getModified() {
        return Modified;
    }

    public void setModified(String modified) {
        Modified = modified;
    }
}
