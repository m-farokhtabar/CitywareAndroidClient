package ir.rayas.app.citywareclient.Share.Layout.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;


public class GridViewScrollable extends GridView {

    public GridViewScrollable(Context context) {
        super(context);
    }

    public GridViewScrollable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewScrollable(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev){
        // Called when a child does not want this parent and its ancestors to intercept touch events.
        requestDisallowInterceptTouchEvent(true);
        return super.onTouchEvent(ev);
    }
}