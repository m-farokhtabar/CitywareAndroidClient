package ir.rayas.app.citywareclient.Share.Layout.View;

import android.content.Context;
import android.os.Build;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;


/**
 * Created by Mehdi on 6/9/2016.
 * کامپوننت نوار پیمایش
 */
public class ProgressBarView extends RelativeLayout {

    private TextView ProgressShort;
    private TextView ProgressLong;

    private int BackgroundColor;
    private int ProgressColor;

    private int CurrentWidth;

    private int Temp = 0;

    public ProgressBarView(Context context, int CurrentWidth, int CurrentHeight) {
        super(context);
        this.CurrentWidth = CurrentWidth;

        //Set Background Of LinearLayout
        int CurrentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (CurrentApiVersion >= Build.VERSION_CODES.JELLY_BEAN)
            this.setBackground(LayoutUtility.SetLinearGradientBackgroundColor(0xffffffff, 0xffffffff));
        else
            this.setBackgroundDrawable(LayoutUtility.SetLinearGradientBackgroundColor(0xffffffff, 0xffffffff));

        //Set Width And Height Of LinearLayout
        LinearLayout.LayoutParams LayoutParamsThis = new LinearLayout.LayoutParams(CurrentWidth, CurrentHeight);
        this.setLayoutParams(LayoutParamsThis);

        //Add a TextView as ProgressShort
        ProgressShort = new TextView(context);
        RelativeLayout.LayoutParams LayoutParamsProgressShort = new RelativeLayout.LayoutParams(0, CurrentHeight);
        LayoutParamsProgressShort.setMargins(0, 0, 0, 0);
        LayoutParamsProgressShort.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        ProgressShort.setLayoutParams(LayoutParamsProgressShort);
        ProgressShort.setMinWidth(0);
        ProgressShort.setMaxWidth(CurrentWidth);
        ProgressShort.setBackgroundColor(0xffff0000);

        ProgressLong = new TextView(context);
        RelativeLayout.LayoutParams LayoutParamsProgressLong = new RelativeLayout.LayoutParams(0, CurrentHeight);
        ProgressLong.setMinWidth(0);
        ProgressLong.setMaxWidth(CurrentWidth);
        LayoutParamsProgressLong.setMargins(0, 0, 0, 0);
        LayoutParamsProgressLong.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        ProgressLong.setLayoutParams(LayoutParamsProgressLong);
        ProgressLong.setBackgroundColor(0xffff0000);

        this.addView(ProgressShort);
        this.addView(ProgressLong);
    }

    public void StartAnimation() {
        final RelativeLayout.LayoutParams LayoutParamsProgressLong = (RelativeLayout.LayoutParams) ProgressLong.getLayoutParams();
        final ValueAnimator ProgressLongAnimator = ObjectAnimator.ofInt(LayoutParamsProgressLong.width, 0, ((CurrentWidth * 4 / 6) + (int) (0.75F * (CurrentWidth * 4 / 6))));
        ProgressLongAnimator.setDuration(2500);
        ProgressLongAnimator.setStartDelay(0);
        ProgressLongAnimator.setInterpolator(new LinearInterpolator());
        ProgressLongAnimator.setRepeatCount(ValueAnimator.INFINITE);
        ProgressLongAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if ((Integer) valueAnimator.getAnimatedValue() == 0) {
                    LayoutParamsProgressLong.width = 0;
                    LayoutParamsProgressLong.setMargins(0, 0, 0, 0);
                } else {
                    if ((Integer) valueAnimator.getAnimatedValue() <= (CurrentWidth * 4 / 6)) {
                        LayoutParamsProgressLong.width = (Integer) valueAnimator.getAnimatedValue();
                        Temp = LayoutParamsProgressLong.width;
                    } else {
                        int X = ((CurrentWidth * 4 / 6) + (int) (0.75F * (CurrentWidth * 4 / 6)));
                        if ((Integer) valueAnimator.getAnimatedValue() <= X) {
                            int Dist = ((Integer) valueAnimator.getAnimatedValue() - (CurrentWidth * 4 / 6));
                            LayoutParamsProgressLong.width = Temp - Dist;
                            LayoutParamsProgressLong.setMargins(Dist * 2, 0, 0, 0);
                        }
                    }
                }
                ProgressLong.requestLayout();
            }
        });
        ProgressLongAnimator.start();


        final RelativeLayout.LayoutParams LayoutParamsProgressShort = (RelativeLayout.LayoutParams) ProgressShort.getLayoutParams();
        final ValueAnimator ProgressShortAnimator = ObjectAnimator.ofInt(LayoutParamsProgressShort.width, 0, CurrentWidth + (CurrentWidth / 5));
        ProgressShortAnimator.setDuration(2500);
        ProgressShortAnimator.setStartDelay(1250);
        ProgressShortAnimator.setInterpolator(new LinearInterpolator());
        ProgressShortAnimator.setRepeatCount(ValueAnimator.INFINITE);
        ProgressShortAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if ((Integer) valueAnimator.getAnimatedValue() == 0) {
                    LayoutParamsProgressShort.width = 0;
                    LayoutParamsProgressShort.setMargins(0, 0, 0, 0);
                } else {
                    if ((Integer) valueAnimator.getAnimatedValue() <= (CurrentWidth / 5))
                        LayoutParamsProgressShort.width = (Integer) valueAnimator.getAnimatedValue();
                    else {
                        if ((Integer) valueAnimator.getAnimatedValue() <= ((CurrentWidth * 3) / 5)) {
                            LayoutParamsProgressShort.setMargins((Integer) valueAnimator.getAnimatedValue() - (int) (CurrentWidth / 5F), 0, 0, 0);
                            LayoutParamsProgressShort.width = (Integer) valueAnimator.getAnimatedValue();
                        } else {
                            LayoutParamsProgressShort.setMargins(((Integer) valueAnimator.getAnimatedValue() - (int) (CurrentWidth / 5F)), 0, 0, 0);
                        }
                    }
                }

                ProgressShort.requestLayout();
            }
        });
        ProgressShortAnimator.start();
    }

    public int getBackgroundColor() {
        return BackgroundColor;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        BackgroundColor = backgroundColor;
        //Set Background Of LinearLayout
        int CurrentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (CurrentApiVersion >= Build.VERSION_CODES.JELLY_BEAN) {
            this.setBackground(null);
            this.setBackground(LayoutUtility.SetLinearGradientBackgroundColor(BackgroundColor, BackgroundColor));
        } else {
            this.setBackgroundDrawable(null);
            this.setBackgroundDrawable(LayoutUtility.SetLinearGradientBackgroundColor(BackgroundColor, BackgroundColor));
        }
    }

    public int getProgressColor() {
        return ProgressColor;
    }

    public void setProgressColor(int progressColor) {
        ProgressColor = progressColor;
        ProgressLong.setBackgroundColor(ProgressColor);
        ProgressShort.setBackgroundColor(ProgressColor);
    }
}

