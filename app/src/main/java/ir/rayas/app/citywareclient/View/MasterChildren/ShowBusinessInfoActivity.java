package ir.rayas.app.citywareclient.View.MasterChildren;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Business.BusinessService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessViewModel;

public class ShowBusinessInfoActivity extends BaseActivity implements IResponseService, ILoadData {

    private TextViewPersian TitleBusinessInfoTextViewShowBusinessInfoActivity = null;
    private ImageView ImageBusinessInfoImageViewShowBusinessInfoActivity = null;
    private RatingBar RatingShowBusinessInfoRatingBarShowBusinessInfoActivity = null;
    private SwitchCompat HasDeliverySwitchShowBusinessInfoActivity = null;
    private SwitchCompat IsOpenSwitchShowBusinessInfoActivity = null;

    private int BusinessId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_business_info);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.SHOW_BUSINESS_INFO_ACTIVITY);

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                LoadData();
            }
        }, R.string.business_info);

        BusinessId = getIntent().getExtras().getInt("BusinessId");
        //ایجاد طرح بندی صفحه
        CreateLayout();
        //دریافت اطلاعات از سرور
        LoadData();
        //ثبت آمار مشاهده کاربر
        OnBusinessVisitedByUser(BusinessId,this);
    }

    private void CreateLayout() {

        TitleBusinessInfoTextViewShowBusinessInfoActivity = findViewById(R.id.TitleBusinessInfoTextViewShowBusinessInfoActivity);
        ImageBusinessInfoImageViewShowBusinessInfoActivity = findViewById(R.id.ImageBusinessInfoImageViewShowBusinessInfoActivity);
        ImageBusinessInfoImageViewShowBusinessInfoActivity.getLayoutParams().width = LayoutUtility.GetWidthAccordingToScreen(this, 1);
        ImageBusinessInfoImageViewShowBusinessInfoActivity.getLayoutParams().height = LayoutUtility.GetWidthAccordingToScreen(this, 2);
        RatingShowBusinessInfoRatingBarShowBusinessInfoActivity = findViewById(R.id.RatingShowBusinessInfoRatingBarShowBusinessInfoActivity);
        HasDeliverySwitchShowBusinessInfoActivity = findViewById(R.id.HasDeliverySwitchShowBusinessInfoActivity);
        IsOpenSwitchShowBusinessInfoActivity = findViewById(R.id.IsOpenSwitchShowBusinessInfoActivity);
        ButtonPersianView ShowBusinessInfoButtonShowBusinessInfoActivity = findViewById(R.id.ShowBusinessInfoButtonShowBusinessInfoActivity);
        ButtonPersianView ShowProductButtonShowBusinessInfoActivity = findViewById(R.id.ShowProductButtonShowBusinessInfoActivity);
        ButtonPersianView ShowPosterAndOfferButtonShowBusinessInfoActivity = findViewById(R.id.ShowPosterAndOfferButtonShowBusinessInfoActivity);
        ButtonPersianView CreateAndShowCommentButtonShowBusinessInfoActivity = findViewById(R.id.CreateAndShowCommentButtonShowBusinessInfoActivity);

        IsOpenSwitchShowBusinessInfoActivity.setClickable(false);
        HasDeliverySwitchShowBusinessInfoActivity.setClickable(false);

        ShowBusinessInfoButtonShowBusinessInfoActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ShowBusinessDetailsIntent = NewIntent(ShowBusinessDetailsActivity.class);
                ShowBusinessDetailsIntent.putExtra("BusinessId",BusinessId);
                startActivity(ShowBusinessDetailsIntent);
            }
        });

        ShowProductButtonShowBusinessInfoActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ShowProductListIntent = NewIntent(ShowProductListActivity.class);
                ShowProductListIntent.putExtra("BusinessId",BusinessId);
                startActivity(ShowProductListIntent);

            }
        });


        ShowPosterAndOfferButtonShowBusinessInfoActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ShowBusinessPosterListIntent = NewIntent(ShowBusinessPosterListActivity.class);
                ShowBusinessPosterListIntent.putExtra("BusinessId",BusinessId);
                startActivity(ShowBusinessPosterListIntent);
            }
        });

        CreateAndShowCommentButtonShowBusinessInfoActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ShowCommentBusinessIntent = NewIntent(ShowCommentBusinessActivity.class);
                ShowCommentBusinessIntent.putExtra("BusinessName", TitleBusinessInfoTextViewShowBusinessInfoActivity.getText().toString());
                ShowCommentBusinessIntent.putExtra("TotalScore", RatingShowBusinessInfoRatingBarShowBusinessInfoActivity.getRating());
                ShowCommentBusinessIntent.putExtra("BusinessId", BusinessId);
                startActivity(ShowCommentBusinessIntent);
            }
        });

    }

    @Override
    public void LoadData() {
        ShowLoadingProgressBar();

        BusinessService BusinessService = new BusinessService(this);
        BusinessService.Get(BusinessId);
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.BusinessGet) {
                Feedback<BusinessViewModel> FeedBack = (Feedback<BusinessViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    BusinessViewModel ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        //پر کردن ویو با اطلاعات دریافتی
                        SetInformationToView(ViewModel);
                    } else {
                        ShowToast(FeedbackType.InvalidDataFormat.getMessage().replace("{0}", ""), Toast.LENGTH_LONG, MessageType.Warning);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            }
        } catch (Exception e) {
            ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
    }


    private void SetInformationToView(BusinessViewModel ViewModel) {
        TitleBusinessInfoTextViewShowBusinessInfoActivity.setText(ViewModel.getTitle());

        if (ViewModel.getImagePathUrl() == null) {
            ImageBusinessInfoImageViewShowBusinessInfoActivity.setImageResource(R.drawable.image_default);
        } else if (ViewModel.getImagePathUrl().equals("")) {
            ImageBusinessInfoImageViewShowBusinessInfoActivity.setImageResource(R.drawable.image_default);
        } else {
            ImageBusinessInfoImageViewShowBusinessInfoActivity.setVisibility(View.VISIBLE);
            String ImageUrl = "";
            if (ViewModel.getImagePathUrl().contains("~")) {
                ImageUrl = ViewModel.getImagePathUrl().replace("~", DefaultConstant.BaseUrlWebService);
            } else {
                ImageUrl = ViewModel.getImagePathUrl();
            }
            LayoutUtility.LoadImageWithGlide(this, ImageUrl, ImageBusinessInfoImageViewShowBusinessInfoActivity, LayoutUtility.GetWidthAccordingToScreen(this, 1), LayoutUtility.GetWidthAccordingToScreen(this, 2));
        }


        RatingShowBusinessInfoRatingBarShowBusinessInfoActivity.setMax(5);
        String Rating = String.valueOf(ViewModel.getTotalScore());
        RatingShowBusinessInfoRatingBarShowBusinessInfoActivity.setRating(Float.parseFloat(Rating));
        HasDeliverySwitchShowBusinessInfoActivity.setChecked(ViewModel.isHasDelivery());
        IsOpenSwitchShowBusinessInfoActivity.setChecked(ViewModel.isOpen());

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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
