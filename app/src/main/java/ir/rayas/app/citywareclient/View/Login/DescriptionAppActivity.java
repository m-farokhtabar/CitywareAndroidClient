package ir.rayas.app.citywareclient.View.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Etc.EventOrNewsService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Constant.LayoutConstant;
import ir.rayas.app.citywareclient.Share.Enum.PlaceType;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.View.Login.LoginRegisterActivity;
import ir.rayas.app.citywareclient.ViewModel.Etc.EventOrNewsViewModel;

public class DescriptionAppActivity extends BaseActivity implements IResponseService {
    private ImageView DescriptionImageView = null;
    private TextViewPersian DescriptionTextView = null;
    private ButtonPersianView DescriptionButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_app);
        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                LoadData();
            }
        },0);
        //ایجاد طرح بندی صفحه
        CreateLayout();
        //دریافت اطلاعات از سرور
        LoadData();
    }

    /**
     * رویداد برای تصویر که اگر بر روی آن کلیک کرد به صفحه مورد نظر وارد شود
     */
    private void CreateLayout() {
        DescriptionImageView = findViewById(R.id.DescriptionImageView);
        RelativeLayout.LayoutParams IntroduceBannerImageViewLayoutParams =   new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LayoutUtility.GetWidthAccordingToScreen(this,2.5));
        DescriptionImageView.setLayoutParams(IntroduceBannerImageViewLayoutParams);
        DescriptionTextView  = findViewById(R.id.DescriptionTextView);

        DescriptionButton = findViewById(R.id.DescriptionButton);
        DescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnButtonClick();
            }
        });

    }

    private void OnButtonClick()
    {
        Intent ActivityIntent = new Intent(this,LoginRegisterActivity.class);
        startActivity(ActivityIntent);
        finish();
    }

    private void LoadData() {
        ShowLoadingProgressBar();
        EventOrNewsService EService = new EventOrNewsService(this);
        EService.GetLast(PlaceType.DescriptionAppPage);
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        try {
            Feedback<EventOrNewsViewModel> FeedBack = (Feedback<EventOrNewsViewModel>) Data;
            if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                EventOrNewsViewModel ViewModel = FeedBack.getValue();
                if (ViewModel!=null)
                {
                    LayoutUtility.LoadImageWithGlide(this,ViewModel.ImagePath,DescriptionImageView,LayoutConstant.ImageWidthFullWidth, LayoutConstant.ImageHeight2_5OfWidth);
                    DescriptionTextView.setText(ViewModel.Title + ":" +"\n"+ViewModel.Description);
                }
                HideLoading();
            } else {
                if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                    ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                }
                ShowErrorInConnectDialog();
            }
        } catch (Exception Ex) {
            ShowErrorInConnectDialog();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
        onLowMemory();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
