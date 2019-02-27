package ir.rayas.app.citywareclient.Share.Enum;

/**
 * Created by Programmer on 2/10/2018.
 */

/// <summary>
/// نوع رنگ
/// </summary>
public enum ColorType {
        Eye("Eye", 0),
        Body("Body", 1),
        Favorite("Favorite", 2);

        private String colorType;
        private int ValueColorType;

        ColorType(String Key, int Value) {
                colorType = Key;
                ValueColorType = Value;
        }

        public int GetColorType() {
                return ValueColorType;
        }
}
