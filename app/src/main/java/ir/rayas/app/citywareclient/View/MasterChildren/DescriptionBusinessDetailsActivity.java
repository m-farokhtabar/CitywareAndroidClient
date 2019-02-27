package ir.rayas.app.citywareclient.View.MasterChildren;


import android.os.Bundle;
import android.webkit.WebView;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;

public class DescriptionBusinessDetailsActivity extends BaseActivity {

    private String Description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_business_details);

        Description = getIntent().getExtras().getString("Description");

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.DESCRIPTION_BUSINESS_DETAILS_ACTIVITY);

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
            }
        }, R.string.description);
        //ایجاد طرح بندی صفحه
        CreateLayout();
    }

    /**
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    private void CreateLayout() {
        WebView DescriptionWebViewDescriptionBusinessActivity = findViewById(R.id.DescriptionWebViewDescriptionBusinessActivity);

        DescriptionWebViewDescriptionBusinessActivity.getSettings().setLoadsImagesAutomatically(true);

        DescriptionWebViewDescriptionBusinessActivity.getSettings().setJavaScriptEnabled(true);
        String webText = "<html>\n" +
                "<head>\n" +
                "<style type=\"text/css\">\n" +
                "@font-face {\n" +
                "    font-family: MyFont;\n" +
                "    src: url(\"file:///android_asset/fonts/iransanslight.ttf\")\n" +
                "}\n" +
                "body {\n" +
                "    font-family: MyFont;\n" +
                "    font-size: small;\n" +
                "    text-align: justify;\n" +
                "    direction: rtl;\n" +
                "    padding: 8px;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                Description +
                "</body>\n" +
                "</html>";

        DescriptionWebViewDescriptionBusinessActivity.loadDataWithBaseURL("", webText, "text/html", "UTF-8", "");


    }

}
