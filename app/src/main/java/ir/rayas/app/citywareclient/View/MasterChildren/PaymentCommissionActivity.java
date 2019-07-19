package ir.rayas.app.citywareclient.View.MasterChildren;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.HashMap;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResultPassing;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;

public class PaymentCommissionActivity extends BaseActivity {

//    private String BusinessName = "";
    private String PricePayable = "";
    private String Id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_commission);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.PAYMENT_COMMISION_ACTIVITY);

        //آماده سازی قسمت لودینگ و پنجره خطا و سیستم پیغام در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {

            }
        }, R.string.payment);

        //مشخص شدن صفحه ویرایش آدرس یا آدرس جدید
//        BusinessName = getIntent().getExtras().getString("BusinessName");
        PricePayable = getIntent().getExtras().getString("PricePayable");
        Id = getIntent().getExtras().getString("Id");


        //ایجاد طرح بندی صفحه
        CreateLayout();
    }

    private void CreateLayout() {
        TextViewPersian PricePayableTextViewPaymentCommissionActivity = findViewById(R.id.PricePayableTextViewPaymentCommissionActivity);
        TextViewPersian BusinessNameTextViewPaymentCommissionActivity = findViewById(R.id.BusinessNameTextViewPaymentCommissionActivity);
        ButtonPersianView SubmitPaymentPackageButtonPaymentCommissionActivity = findViewById(R.id.SubmitPaymentPackageButtonPaymentCommissionActivity);
        final RadioButton BankSelectedRadioButtonPaymentCommissionActivity = findViewById(R.id.BankSelectedRadioButtonPaymentCommissionActivity);


//        BusinessNameTextViewPaymentCommissionActivity.setText(BusinessName);
        BusinessNameTextViewPaymentCommissionActivity.setVisibility(View.GONE);
        PricePayableTextViewPaymentCommissionActivity.setText(PricePayable);


        SubmitPaymentPackageButtonPaymentCommissionActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (BankSelectedRadioButtonPaymentCommissionActivity.isChecked()) {

                    String url = "http://asanpardakhtpg.zeytoonfood.com/startpayment.aspx?id=" + Id + "&type=2";
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);

                } else {
                    ShowToast(getResources().getString(R.string.please_select_bank), Toast.LENGTH_LONG, MessageType.Warning);
                }
            }
        });

    }

    /**
     * دریافت ویومدل پوستر خریداری شده و ارسال آن به اکتیویتی پروفایل کاربر جهت نمایش در لیست پوسترهای فعال
     *
     */
    private void SendDataToParentActivity() {
        HashMap<String, Object> Output = new HashMap<>();
        ActivityResultPassing.Push(new ActivityResult(getParentActivity(), getCurrentActivityId(), Output));
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
        SendDataToParentActivity();
        super.onBackPressed();
    }
}
