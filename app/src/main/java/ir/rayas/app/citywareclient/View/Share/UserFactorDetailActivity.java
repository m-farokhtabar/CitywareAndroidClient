package ir.rayas.app.citywareclient.View.Share;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.Share.OnLoadMoreListener;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.UserFactorListRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Factor.UserFactorService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.EditTextPersian;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorItemViewModel;
import ir.rayas.app.citywareclient.ViewModel.Factor.FactorViewModel;

public class UserFactorDetailActivity extends BaseActivity implements IResponseService {

    private int FactorId = 0;
    private TextViewPersian BusinessNameTextViewUserFactorDetailActivity = null;
    private TextViewPersian CreateDateUserFactorTextViewUserFactorDetailActivity = null;
    private TextViewPersian UserFactorCodeTextViewUserFactorDetailActivity = null;
    private TextViewPersian NumberOfOrderItemsUserFactorTextViewUserFactorDetailActivity = null;
    private TextViewPersian PricePayableUserFactorTextViewUserFactorDetailActivity = null;
    private TextViewPersian DescriptionUserFactorTextViewUserFactorDetailActivity = null;
    private TextViewPersian BusinessDescriptionTextViewUserFactorDetailActivity = null;
    private EditText UserDescriptionEditTextUserFactorDetailActivity = null;
    private TextViewPersian AddressIconTextViewUserFactorDetailActivity = null;
    private TextViewPersian ContactIconTextViewUserFactorDetailActivity = null;
    private CardView BusinessDescriptionCardViewUserFactorDetailActivity = null;
    private RelativeLayout UserDescriptionRelativeLayoutUserFactorDetailActivity = null;

    private void SetInformationToView(FactorViewModel ViewModelList) {

        BusinessNameTextViewUserFactorDetailActivity.setText(ViewModelList.getBusinessName());
        CreateDateUserFactorTextViewUserFactorDetailActivity.setText(ViewModelList.getCreate());
        NumberOfOrderItemsUserFactorTextViewUserFactorDetailActivity.setText(String.valueOf(ViewModelList.getItemList().size()));
        UserFactorCodeTextViewUserFactorDetailActivity.setText(String.valueOf(ViewModelList.getId()));

        boolean IsZeroPrice = false;

        List<FactorItemViewModel> ItemList = ViewModelList.getItemList();
        for (int i = 0; i < ItemList.size(); i++) {
            if (ItemList.get(i).getPrice() == 0) {
                IsZeroPrice = true;
            }
        }

        if (ViewModelList.getTotalPrice() < 0) {
            PricePayableUserFactorTextViewUserFactorDetailActivity.setText(getResources().getString(R.string.unknown));
        } else {

            double DeliveryCost = ViewModelList.getDeliveryCost();
            Double PayablePrice;
            if (DeliveryCost <= 0) {
                PayablePrice = ViewModelList.getTotalPrice();
            } else {
                PayablePrice = ViewModelList.getTotalPrice() + DeliveryCost;
            }

            if (ViewModelList.isHasQuickOrder()) {

                PricePayableUserFactorTextViewUserFactorDetailActivity.setText(Utility.GetIntegerNumberWithComma(PayablePrice) + " " + getResources().getString(R.string.toman) + " - " + getResources().getString(R.string.price_is_not_definitive));

                DescriptionUserFactorTextViewUserFactorDetailActivity.setVisibility(View.VISIBLE);
                DescriptionUserFactorTextViewUserFactorDetailActivity.setText(getResources().getString(R.string.in_your_factor_there_are_products_that_are_not_priced));
            } else {
                if (IsZeroPrice) {
                    PricePayableUserFactorTextViewUserFactorDetailActivity.setText(Utility.GetIntegerNumberWithComma(PayablePrice) + " " + getResources().getString(R.string.toman) + " - " + getResources().getString(R.string.price_is_not_definitive));

                    DescriptionUserFactorTextViewUserFactorDetailActivity.setVisibility(View.VISIBLE);
                    DescriptionUserFactorTextViewUserFactorDetailActivity.setText(getResources().getString(R.string.in_your_factor_there_are_products_that_are_not_priced));
                } else {
                    DescriptionUserFactorTextViewUserFactorDetailActivity.setVisibility(View.GONE);
                    PricePayableUserFactorTextViewUserFactorDetailActivity.setText(Utility.GetIntegerNumberWithComma(PayablePrice) + " " + getResources().getString(R.string.toman));
                }
            }
        }

        if (ViewModelList.getBusinessDescription() == null || ViewModelList.getBusinessDescription().equals("")){
            BusinessDescriptionCardViewUserFactorDetailActivity.setVisibility(View.GONE);
        }   else {
            BusinessDescriptionCardViewUserFactorDetailActivity.setVisibility(View.VISIBLE);
            BusinessDescriptionTextViewUserFactorDetailActivity.setText(ViewModelList.getBusinessDescription());
        }

        if (ViewModelList.getUserDescription() == null || ViewModelList.getUserDescription().equals("")){
            UserDescriptionEditTextUserFactorDetailActivity.setVisibility(View.GONE);
            UserDescriptionRelativeLayoutUserFactorDetailActivity.setVisibility(View.GONE);
        }   else {
            UserDescriptionRelativeLayoutUserFactorDetailActivity.setVisibility(View.VISIBLE);
            UserDescriptionEditTextUserFactorDetailActivity.setVisibility(View.VISIBLE);
            UserDescriptionEditTextUserFactorDetailActivity.setText(ViewModelList.getUserDescription());
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_factor_detail);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.USER_FACTOR_DETAIL_ACTIVITY);

        FactorId = getIntent().getExtras().getInt("FactorId");

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.factor);

        //ایجاد طرحبندی صفحه
        CreateLayout();

        LoadData();
    }

    /**
     * در صورتی که در ارتباط با اینترنت مشکلی بوجود آمده و کاربر دکمه تلاش مجدد را فشار داده است
     */
    private void RetryButtonOnClick() {
        //دریافت اطلاعات
        LoadData();
    }

    /**
     * دریافت اطلاعات نحوای جهت پر کردن Recycle
     */
    private void LoadData() {
        ShowLoadingProgressBar();

        UserFactorService UserFactorService = new UserFactorService(this);
        UserFactorService.Get(FactorId);
    }

    /**
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    private void CreateLayout() {

        BusinessNameTextViewUserFactorDetailActivity = findViewById(R.id.BusinessNameTextViewUserFactorDetailActivity);
        CreateDateUserFactorTextViewUserFactorDetailActivity = findViewById(R.id.CreateDateUserFactorTextViewUserFactorDetailActivity);
        UserFactorCodeTextViewUserFactorDetailActivity = findViewById(R.id.UserFactorCodeTextViewUserFactorDetailActivity);
        NumberOfOrderItemsUserFactorTextViewUserFactorDetailActivity = findViewById(R.id.NumberOfOrderItemsUserFactorTextViewUserFactorDetailActivity);
        PricePayableUserFactorTextViewUserFactorDetailActivity = findViewById(R.id.PricePayableUserFactorTextViewUserFactorDetailActivity);
        DescriptionUserFactorTextViewUserFactorDetailActivity = findViewById(R.id.DescriptionUserFactorTextViewUserFactorDetailActivity);
        BusinessDescriptionTextViewUserFactorDetailActivity = findViewById(R.id.BusinessDescriptionTextViewUserFactorDetailActivity);
        BusinessDescriptionCardViewUserFactorDetailActivity = findViewById(R.id.BusinessDescriptionCardViewUserFactorDetailActivity);
        UserDescriptionRelativeLayoutUserFactorDetailActivity = findViewById(R.id.UserDescriptionRelativeLayoutUserFactorDetailActivity);
        UserDescriptionEditTextUserFactorDetailActivity = findViewById(R.id.UserDescriptionEditTextUserFactorDetailActivity);
        RelativeLayout BusinessAddressRelativeLayoutUserFactorDetailActivity = findViewById(R.id.BusinessAddressRelativeLayoutUserFactorDetailActivity);
        RelativeLayout ContactRelativeLayoutUserFactorDetailActivity = findViewById(R.id.ContactRelativeLayoutUserFactorDetailActivity);
        AddressIconTextViewUserFactorDetailActivity = findViewById(R.id.AddressIconTextViewUserFactorDetailActivity);
        ContactIconTextViewUserFactorDetailActivity = findViewById(R.id.ContactIconTextViewUserFactorDetailActivity);

        ButtonPersianView ShowProductButtonUserFactorDetailActivity = findViewById(R.id.ShowProductButtonUserFactorDetailActivity);

        ContactIconTextViewUserFactorDetailActivity.setTypeface(Font.MasterIcon);
        ContactIconTextViewUserFactorDetailActivity.setText("\uf095");

        AddressIconTextViewUserFactorDetailActivity.setTypeface(Font.MasterIcon);
        AddressIconTextViewUserFactorDetailActivity.setText("\uf041");

        ContactRelativeLayoutUserFactorDetailActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        BusinessAddressRelativeLayoutUserFactorDetailActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ShowProductButtonUserFactorDetailActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    /**
     * @param Data
     * @param ServiceMethod
     * @param <T>
     */
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.UserFactorGet) {
                Feedback<FactorViewModel> FeedBack = (Feedback<FactorViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final FactorViewModel ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        SetInformationToView(ViewModelList);
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
            HideLoading();
            ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

