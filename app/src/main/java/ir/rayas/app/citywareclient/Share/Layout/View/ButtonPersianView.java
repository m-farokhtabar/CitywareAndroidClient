package ir.rayas.app.citywareclient.Share.Layout.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.Font.PersianReshape;

/**
 * Created by Programmer on 2/11/2018.
 */

@SuppressLint("AppCompatCustomView")
public class ButtonPersianView extends Button {
    public ButtonPersianView(Context context) {
        super(context);
        this.setTypeface(Font.MasterFont);
    }

    public ButtonPersianView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Font.MasterFont);
    }

    public ButtonPersianView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setTypeface(Font.MasterFont);
    }

    public ButtonPersianView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.setTypeface(Font.MasterFont);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (text != null)
            super.setText(PersianReshape.reshape(text.toString()), type);
        else
            super.setText(text, type);
    }
}
