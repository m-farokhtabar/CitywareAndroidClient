package ir.rayas.app.citywareclient.Share.Enum;

/** انواع نوع های تماس
 * از طریق همین نوع شمارشی حداکثر طول و نوع ولیدیشن باید بررسی شود
 * Created by Programmer on 8/17/2018.
 */
public enum ContactType {

    Phone("Phone", 0),
    CellPhone("CellPhone", 1),
    Fax("Fax", 2),
    Email("Email", 3),
    Website("Website", 4),
    Instagram("Instagram", 5),
    WhatsApp("WhatsApp", 6),
    Telegram("Telegram", 7),
    Facebook("Facebook", 8),
    LinkedIn("LinkedIn", 9),
    GooglePlus("GooglePlus", 10),
    Skype("Skype", 11);

    private String ContactType;
    private int ValueContactType;

    ContactType(String Key, int Value) {
        ContactType = Key;
        ValueContactType = Value;
    }

    public int GetContactType() {
        return ValueContactType;
    }
}