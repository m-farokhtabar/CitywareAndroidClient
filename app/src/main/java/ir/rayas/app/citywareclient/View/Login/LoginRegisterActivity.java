package ir.rayas.app.citywareclient.View.Login;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Adapter.Pager.LoginRegisterPagerAdapter;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;

public class LoginRegisterActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        //آماده سازی قسمت لودینگ و پنجره خطا و سیستم پیغام در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                ShowActivity();
            }
        },0);
        //نمایش نوار ابزار
        InitToolbarWithOutButton();
        //ایجاد طرح بندی صفحه
        CreateLayout();
    }

    /**
     * نمایش دوباره همین اکتیویتی
     */
    private void ShowActivity()
    {
        HideLoading();
    }

    private void CreateLayout()
    {
        ViewPager LoginRegisterViewpager = findViewById(R.id.LoginRegisterViewpager);
        TabLayout LoginRegisterTabLayout =  findViewById(R.id.LoginRegisterTabLayout);

        String[] TabNames = new String[]{getString(R.string.register_user),getString(R.string.login_user)};
        LoginRegisterPagerAdapter Pager = new LoginRegisterPagerAdapter(getSupportFragmentManager(),TabNames);
        LoginRegisterViewpager.setAdapter(Pager);
        LoginRegisterTabLayout.setupWithViewPager(LoginRegisterViewpager);

        LayoutUtility.SetTabCustomFont(LoginRegisterTabLayout);

        TabLayout.Tab DefaultTab = LoginRegisterTabLayout.getTabAt(1);
        DefaultTab.select();
    }

    public void GoToConfirmTrackingCodeActivity(long CellPhone)
    {
        Intent ConfirmTrackingCodeActivityIntent = new Intent(this, ConfirmTrackingCodeActivity.class);
        ConfirmTrackingCodeActivityIntent.putExtra("CellPhone",CellPhone);
        startActivity(ConfirmTrackingCodeActivityIntent);
        finish();
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
