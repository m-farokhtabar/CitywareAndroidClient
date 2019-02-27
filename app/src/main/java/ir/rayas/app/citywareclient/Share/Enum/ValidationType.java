package ir.rayas.app.citywareclient.Share.Enum;

import ir.rayas.app.citywareclient.R;

/**
 * در صورت تغییر این قسمت لطفا به قسمت attrs.xml رفته و تغییرات متناظر را بدهید
 * Created by Programmer on 2/19/2018.
 */

public enum ValidationType {
    None(0, "", 0),
    Email(1, "([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})", R.string.email_format_is_not_correct),
    WebSite(2, "^(https?:\\/\\/(?:www\\.|(?!www))[^\\s\\.]+\\.[^\\s]{2,}|www\\.[^\\s]+\\.[^\\s]{2,})$", R.string.website_format_is_not_correct),
    CellPhoneNumber(3, "^[0][9]\\d{9}$", R.string.cellphone_format_is_not_correct),
    TrackingCode(4, "^\\d{8}$", R.string.tracking_code_format_is_not_correct),
    PostalCode(5, "^\\d{10}$", R.string.postal_code_format_is_not_correct),
    PersianDate(6, "^[1][2345][0-9][0-9]/(([0]?[1-6]/(([0]?[1-9])|(([1-2][0-9])|([3][0-1]))))|((([0]?[7-9])|([1][0-2]))/(([0]?[1-9])|(([1-2][0-9])|([3][0])))))$", R.string.date_format_is_not_correct),
    AccountNumber(7, "^[0-9]{6,20}", R.string.account_number_format_is_not_correct),
    PersianTimeRegular(8, "^(([0]?[0-9])|([1][0-9])|([2][0-3])):(([0]?[0-9])|([1-5][0-9]))$", R.string.time_format_is_not_correct),
    PhoneRegular(9, "^\\d{3,11}$", R.string.phone_format_is_not_correct),
    IntegerNumber(10, "^[0-9]+$", R.string.value_is_not_integer),
    FloatNumber(11, "^([0-9]+|[0-9]*\\.[0-9]+)$", R.string.value_is_not_number),
    FloatPercent(12,"^([0-1]?[0-9]?[0-9]|[0-9]?[0-9]?\\.[0-9]+)$",R.string.percent_format_is_not_correct);

    private String RegularExpression;
    private int ResourceStringId;
    private int Id;

    public String getRegularExpression() {
        return RegularExpression;
    }

    public int getId() {
        return Id;
    }

    public int getResourceStringId() {
        return ResourceStringId;
    }

    ValidationType(int Id, String RegularExpression, int ResourceStringId) {
        this.Id = Id;
        this.RegularExpression = RegularExpression;
        this.ResourceStringId = ResourceStringId;
    }

}
