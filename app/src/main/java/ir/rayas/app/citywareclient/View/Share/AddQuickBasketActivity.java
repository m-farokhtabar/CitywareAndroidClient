package ir.rayas.app.citywareclient.View.Share;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.QuickBasketRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.Service.Basket.BasketService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Basket.QuickOrderItemViewModel;
import ir.rayas.app.citywareclient.ViewModel.Basket.QuickOrderViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;

public class AddQuickBasketActivity extends BaseActivity implements IResponseService {


    private int BusinessId = 0;
    private List<QuickOrderItemViewModel> ItemList = new ArrayList<>();


    private EditText ProductNameEditTextAddQuickBasketActivity = null;
    private EditText CustomerQuantityEditTextAddQuickBasketActivity = null;
    private RecyclerView ProductNameRecyclerViewAddQuickBasketActivity = null;

    private QuickBasketRecyclerViewAdapter quickBasketRecyclerViewAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addquick_basket);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.ADD_QUICK_BASKET_ACTIVITY);

        BusinessId = getIntent().getExtras().getInt("BusinessId");

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.quick_items);

        //ایجاد طرح بندی صفحه
        CreateLayout();

    }

    /**
     * در صورتی که در ارتباط با اینترنت مشکلی بوجود آمده و کاربر دکمه تلاش مجدد را فشار داده است
     */
    private void RetryButtonOnClick() {

        if (ItemList == null || ItemList.size() < 1) {
            ShowToast(getResources().getString(R.string.please_enter_add_product_to_list), Toast.LENGTH_LONG, MessageType.Warning);

        } else {
            ShowLoadingProgressBar();
            BasketService BasketService = new BasketService(AddQuickBasketActivity.this);
            BasketService.AddQuick(MadeViewModel());

        }
    }

    /**
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    private void CreateLayout() {
        ProductNameEditTextAddQuickBasketActivity = findViewById(R.id.ProductNameEditTextAddQuickBasketActivity);
        CustomerQuantityEditTextAddQuickBasketActivity = findViewById(R.id.CustomerQuantityEditTextAddQuickBasketActivity);
        ButtonPersianView CustomerQuantityAcceptButtonAddQuickBasketActivity = findViewById(R.id.CustomerQuantityAcceptButtonAddQuickBasketActivity);
        ButtonPersianView SubmitButtonAddQuickBasketActivity = findViewById(R.id.SubmitButtonAddQuickBasketActivity);
        ImageView CustomerQuantityAddImageAddQuickBasketActivity = findViewById(R.id.CustomerQuantityAddImageAddQuickBasketActivity);
        ImageView CustomerQuantitySubtractImageAddQuickBasketActivity = findViewById(R.id.CustomerQuantitySubtractImageAddQuickBasketActivity);

        ProductNameRecyclerViewAddQuickBasketActivity = findViewById(R.id.ProductNameRecyclerViewAddQuickBasketActivity);
        ProductNameRecyclerViewAddQuickBasketActivity.setHasFixedSize(true);
        LinearLayoutManager RegionLinearLayoutManager = new LinearLayoutManager(AddQuickBasketActivity.this);
        ProductNameRecyclerViewAddQuickBasketActivity.setLayoutManager(RegionLinearLayoutManager);

        CustomerQuantityAddImageAddQuickBasketActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((CustomerQuantityEditTextAddQuickBasketActivity.getText() == null) || ("".equals(CustomerQuantityEditTextAddQuickBasketActivity.getText().toString())) || (1 > Double.valueOf(CustomerQuantityEditTextAddQuickBasketActivity.getText().toString()))) {
                    CustomerQuantityEditTextAddQuickBasketActivity.setText("1");
                } else {
                    double TempCount = Double.valueOf(CustomerQuantityEditTextAddQuickBasketActivity.getText().toString());
                    if (TempCount == 999) {

                    } else {
                        TempCount++;
                        CustomerQuantityEditTextAddQuickBasketActivity.setText(String.valueOf(TempCount));
                    }

                }
            }
        });

        CustomerQuantitySubtractImageAddQuickBasketActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((CustomerQuantityEditTextAddQuickBasketActivity.getText() == null) || ("".equals(CustomerQuantityEditTextAddQuickBasketActivity.getText().toString())) || (1 > Double.valueOf(CustomerQuantityEditTextAddQuickBasketActivity.getText().toString()))) {
                    CustomerQuantityEditTextAddQuickBasketActivity.setText("1");
                } else {
                    double TempCount = Double.valueOf(CustomerQuantityEditTextAddQuickBasketActivity.getText().toString());
                    TempCount--;
                    if (TempCount < 1) {

                    } else {
                        CustomerQuantityEditTextAddQuickBasketActivity.setText(String.valueOf(TempCount));
                    }
                }
            }
        });

        CustomerQuantityAcceptButtonAddQuickBasketActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ("".equals(ProductNameEditTextAddQuickBasketActivity.getText().toString())) {
                    ShowToast(getResources().getString(R.string.please_enter_product_name), Toast.LENGTH_LONG, MessageType.Warning);

                } else if ((CustomerQuantityEditTextAddQuickBasketActivity.getText() == null) || ("".equals(CustomerQuantityEditTextAddQuickBasketActivity.getText().toString())) || (1 > Double.valueOf(CustomerQuantityEditTextAddQuickBasketActivity.getText().toString()))) {
                    ShowToast(getResources().getString(R.string.please_enter_order_quantity), Toast.LENGTH_LONG, MessageType.Warning);

                } else {
                    QuickOrderItemViewModel quickOrderItemViewModel = new QuickOrderItemViewModel();
                    double Quantity = Double.parseDouble(CustomerQuantityEditTextAddQuickBasketActivity.getText().toString());
                    quickOrderItemViewModel.setValue(Quantity);
                    quickOrderItemViewModel.setProductName(ProductNameEditTextAddQuickBasketActivity.getText().toString());

                    ItemList.add(quickOrderItemViewModel);

                    quickBasketRecyclerViewAdapter = new QuickBasketRecyclerViewAdapter(AddQuickBasketActivity.this, ItemList, ProductNameRecyclerViewAddQuickBasketActivity);
                    ProductNameRecyclerViewAddQuickBasketActivity.setAdapter(quickBasketRecyclerViewAdapter);

                    CustomerQuantityEditTextAddQuickBasketActivity.setText("");
                    ProductNameEditTextAddQuickBasketActivity.setText("");


                }
            }
        });


        SubmitButtonAddQuickBasketActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ItemList == null || ItemList.size() < 1) {
                    ShowToast(getResources().getString(R.string.please_enter_add_product_to_list), Toast.LENGTH_LONG, MessageType.Warning);

                } else {
                    ShowLoadingProgressBar();
                    BasketService BasketService = new BasketService(AddQuickBasketActivity.this);
                    BasketService.AddQuick(MadeViewModel());

                }
            }
        });
    }


    private QuickOrderViewModel MadeViewModel() {
        QuickOrderViewModel ViewModel = new QuickOrderViewModel();
        try {
            AccountRepository AccountRepository = new AccountRepository(AddQuickBasketActivity.this);
            AccountViewModel AccountModel = AccountRepository.getAccount();

            ViewModel.setUserId(AccountModel.getUser().getId());
            ViewModel.setBusinessId(BusinessId);
            ViewModel.setItemList(ItemList);

        } catch (Exception ignored) {
        }
        return ViewModel;
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
            if (ServiceMethod == ServiceMethodType.BasketQuickAdd) {
                Feedback<Double> FeedBack = (Feedback<Double>) Data;

                if (FeedBack.getStatus() == FeedbackType.RegisteredSuccessful.getId()) {

                    String Message = getResources().getString(R.string.products_list) + " " + getResources().getString(R.string.product_added_to_basket);
                    ShowDialogContinueShoppingAndShowBasketOrder(Message);

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


    private void ShowDialogContinueShoppingAndShowBasketOrder(String Message) {

        final Dialog DialogOrder = new Dialog(AddQuickBasketActivity.this);
        DialogOrder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogOrder.setContentView(R.layout.dialog_continue_shopping_show_basket);
        DialogOrder.setCanceledOnTouchOutside(true);

        TextViewPersian DialogDescriptionTextView = DialogOrder.findViewById(R.id.DialogDescriptionTextView);
        ButtonPersianView DialogContinueShoppingProcessButton = DialogOrder.findViewById(R.id.DialogContinueShoppingProcessButton);
        ButtonPersianView DialogShowBasketButton = DialogOrder.findViewById(R.id.DialogShowBasketButton);

        DialogDescriptionTextView.setText(Message);

        DialogContinueShoppingProcessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quickBasketRecyclerViewAdapter.ClearViewModelList();
                DialogOrder.dismiss();
            }
        });

        DialogShowBasketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BasketIntent = new Intent(AddQuickBasketActivity.this, BasketActivity.class);
                BasketIntent.putExtra("FromActivityId", ActivityIdList.MAIN_ACTIVITY);
                startActivity(BasketIntent);


                quickBasketRecyclerViewAdapter.ClearViewModelList();


                DialogOrder.dismiss();
            }
        });
        DialogOrder.show();


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
