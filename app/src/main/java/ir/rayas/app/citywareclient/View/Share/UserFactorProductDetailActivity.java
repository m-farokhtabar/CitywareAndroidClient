package ir.rayas.app.citywareclient.View.Share;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.UserFactorProductDetailRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.ViewModel.FactorStatusAdapterViewModel;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorItemViewModel;

public class UserFactorProductDetailActivity extends BaseActivity {

    private List<FactorItemViewModel> FactorItemViewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_factor_product_detail);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.USER_FACTOR_PRODUCT_DETAIL_ACTIVITY);

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {

            }
        }, R.string.factor_products);

        String ArrayAsString = getIntent().getExtras().getString("ArrayAsString");
        Type listType = new TypeToken<List<FactorItemViewModel>>() {
        }.getType();
        FactorItemViewModel = new Gson().fromJson(ArrayAsString, listType);

        //طرحبندی ویو
        CreateLayout();
    }

    private void CreateLayout() {

        RecyclerView ProductListRecyclerViewUserFactorProductDetailActivity = findViewById(R.id.ProductListRecyclerViewUserFactorProductDetailActivity);
        ProductListRecyclerViewUserFactorProductDetailActivity.setHasFixedSize(true);
        //به دلیل اینکه من در هر سطر یک گزینه نیاز دارم
        LinearLayoutManager RegionLinearLayoutManager = new LinearLayoutManager(UserFactorProductDetailActivity.this);
        ProductListRecyclerViewUserFactorProductDetailActivity.setLayoutManager(RegionLinearLayoutManager);

        UserFactorProductDetailRecyclerViewAdapter userFactorProductDetailRecyclerViewAdapter = new UserFactorProductDetailRecyclerViewAdapter(UserFactorProductDetailActivity.this, FactorItemViewModel, ProductListRecyclerViewUserFactorProductDetailActivity);
        ProductListRecyclerViewUserFactorProductDetailActivity.setAdapter(userFactorProductDetailRecyclerViewAdapter);
        userFactorProductDetailRecyclerViewAdapter.notifyDataSetChanged();
        ProductListRecyclerViewUserFactorProductDetailActivity.invalidate();


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
