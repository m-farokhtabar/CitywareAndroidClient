package ir.rayas.app.citywareclient.Share.Layout.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.Font.PersianReshape;

/**
 * کامپوننت فارسی ایبل
 */
public class TextViewPersian extends android.support.v7.widget.AppCompatTextView {

    public TextViewPersian(Context context) {
        super(context);
        this.setTypeface(Font.MasterFont);
    }

    public TextViewPersian(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Font.MasterFont);
    }

    public TextViewPersian(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setTypeface(Font.MasterFont);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        if (text != null)
            super.setText(PersianReshape.reshape(text.toString()), type);
        else
            super.setText(text, type);
    }
}
