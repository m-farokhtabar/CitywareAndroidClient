package ir.rayas.app.citywareclient.View.Base;

/**
 * Created by Programmer on 7/17/2018.
 */

public class WhichViewClicked {
    public WhichViewClicked(int view, int subView) {
        ViewId = view;
        SubViewId = subView;
    }

    private int ViewId;
    private int SubViewId;

    public int getViewId() {
        return ViewId;
    }

    public void setViewId(int viewId) {
        ViewId = viewId;
    }

    public int getSubViewId() {
        return SubViewId;
    }

    public void setSubViewId(int subViewId) {
        SubViewId = subViewId;
    }

}
