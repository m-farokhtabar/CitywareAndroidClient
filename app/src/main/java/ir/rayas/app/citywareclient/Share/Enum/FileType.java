package ir.rayas.app.citywareclient.Share.Enum;

/**
 * Created by Programmer on 1/8/2019.
 */

/**
 * انواع فایل
 */
public enum FileType {
    /**
     * تصویر
     */
    Image(0),
    /**
     * سند یا متنی، ورد، پی دی اف یا اکسل
     */
    Document(1),
    /**
     * فایل تصویری
     */
    Video(2),
    /**
     * فایل صوتی
     */
    Sound(3),
    /**
     * بقیه انواع فایل
     */
    Etc(4);
    private int id;

    public int getId() {
        return id;
    }

    FileType(int id) {
        this.id = id;
    }
}
