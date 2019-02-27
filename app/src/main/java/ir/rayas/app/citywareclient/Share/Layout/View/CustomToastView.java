package ir.rayas.app.citywareclient.Share.Layout.View;
import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;

/**
 * Created by Programmer on 2/18/2018.
 */

public class CustomToastView {
    private Activity CurrentView = null;
    private Toast ToastMessage = null;
    private TextViewPersian MessageToastTextView = null;
    private TextView IconToastTextView = null;

    public CustomToastView(Activity CurrentView) {
        this.CurrentView = CurrentView;
        CreateLayout();
    }
    private void CreateLayout()
    {
        LayoutInflater LayoutInflaterToast = CurrentView.getLayoutInflater();
        View CurrentLayout = LayoutInflaterToast.inflate(R.layout.view_custom_toast,(ViewGroup) CurrentView.findViewById(R.id.custom_toast_layout));


        MessageToastTextView = CurrentLayout.findViewById(R.id.MessageToastTextView);
        IconToastTextView = CurrentLayout.findViewById(R.id.IconToastTextView);
        IconToastTextView.setTypeface(Font.MasterIcon);

        ToastMessage = new Toast(CurrentView);
        ToastMessage.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        ToastMessage.setView(CurrentLayout);
    }

    public void Show(String Message, int duration, MessageType CurrentMessageType)
    {
        switch (CurrentMessageType)
        {
            case Info:
                IconToastTextView.setText("\uf06a");
                IconToastTextView.setTextColor( LayoutUtility.GetColorFromResource(CurrentView,R.color.FontWhiteColor));
                break;
            case Warning:
                IconToastTextView.setText("\uf071");
                IconToastTextView.setTextColor( LayoutUtility.GetColorFromResource(CurrentView,R.color.FontYellowColor));
                break;
            case Error:
                IconToastTextView.setText("\uf057");
                IconToastTextView.setTextColor( LayoutUtility.GetColorFromResource(CurrentView,R.color.FontRedColor));
                break;
        }

        MessageToastTextView.setText(Message);
        ToastMessage.setDuration(duration);
        ToastMessage.show();
    }
}
