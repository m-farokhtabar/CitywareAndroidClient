package ir.rayas.app.citywareclient.View.Master;

import android.os.Bundle;
import android.widget.TextView;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //تنظیم گزینه انتخاب شده منو
        setCurrentActivityId(ActivityIdList.MAIN_ACTIVITY);

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                LoadData();
            }
        },0);
        //ایجاد طرحبندی صفحه
        CreateLayout();
    }

    private void LoadData() {

    }

    private void CreateLayout(){
        TextView SearchLogo = findViewById(R.id.MainSearchIconTextView);
        SearchLogo.setTypeface(Font.MasterIcon);
        SearchLogo.setText("\uf002");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
