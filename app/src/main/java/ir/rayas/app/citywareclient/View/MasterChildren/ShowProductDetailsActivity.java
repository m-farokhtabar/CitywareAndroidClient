package ir.rayas.app.citywareclient.View.MasterChildren;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


import ir.rayas.app.citywareclient.Adapter.Pager.ShowImageProductDetailsPagerAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.Service.Basket.BasketService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Order.ProductService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.EditTextPersian;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.View.Share.BasketActivity;
import ir.rayas.app.citywareclient.ViewModel.Basket.BasketItemViewModel;
import ir.rayas.app.citywareclient.ViewModel.Basket.StandardOrderItemViewModel;
import ir.rayas.app.citywareclient.ViewModel.Order.ProductImageViewModel;
import ir.rayas.app.citywareclient.ViewModel.Order.ProductViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.UserViewModel;

public class ShowProductDetailsActivity extends BaseActivity implements IResponseService, ILoadData {

    private ImageView ShareImageViewShowProductDetailsActivity = null;
    private ImageView ProductImageImageViewShowProductDetailsActivity = null;
    private RelativeLayout DescriptionRelativeLayoutShowProductDetailsActivity = null;
    private RelativeLayout AddToShoppingCartRelativeLayoutShowProductDetailsActivity = null;
    private SwitchCompat AvailableSwitchShowProductDetailsActivity = null;
    private SwitchCompat HasDeliverySwitchShowProductDetailsActivity = null;
    private SwitchCompat IsOpenSwitchShowProductDetailsActivity = null;
    private TextViewPersian ProductNameTextViewShowProductDetailsActivity = null;
    private TextViewPersian AbstractDescriptionProductTextViewShowProductDetailsActivity = null;
    private TextViewPersian DescriptionIconTextViewShowProductDetailsActivity = null;
    private TextViewPersian PriceTextViewShowProductDetailsActivity = null;
    private TextViewPersian BusinessTitleTextViewShowProductDetailsActivity = null;
    private TextViewPersian ShoppingCartIconTextViewShowProductDetailsActivity = null;
    private ViewPager SliderViewPagerShowProductDetailsActivity = null;
    private LinearLayout SliderDotsLinearLayoutShowProductDetailsActivity = null;
    private EditText DialogCustomerQuantityEditText = null;


    private int ProductId;
    private int BusinessId;
    private boolean IsSwipe = false;
    private String Description;
    private String ProductName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product_details);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.SHOW_PRODUCT_DETAILS_ACTIVITY);

        ProductId = getIntent().getExtras().getInt("ProductId");

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.product_details);

        //ایجاد طرح بندی صفحه
        CreateLayout();
        //دریافت اطلاعات از سرور
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
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    private void CreateLayout() {
        ShareImageViewShowProductDetailsActivity = findViewById(R.id.ShareImageViewShowProductDetailsActivity);
        DescriptionRelativeLayoutShowProductDetailsActivity = findViewById(R.id.DescriptionRelativeLayoutShowProductDetailsActivity);
        AddToShoppingCartRelativeLayoutShowProductDetailsActivity = findViewById(R.id.AddToShoppingCartRelativeLayoutShowProductDetailsActivity);
        AvailableSwitchShowProductDetailsActivity = findViewById(R.id.AvailableSwitchShowProductDetailsActivity);
        ProductNameTextViewShowProductDetailsActivity = findViewById(R.id.ProductNameTextViewShowProductDetailsActivity);
        AbstractDescriptionProductTextViewShowProductDetailsActivity = findViewById(R.id.AbstractDescriptionProductTextViewShowProductDetailsActivity);
        DescriptionIconTextViewShowProductDetailsActivity = findViewById(R.id.DescriptionIconTextViewShowProductDetailsActivity);
        PriceTextViewShowProductDetailsActivity = findViewById(R.id.PriceTextViewShowProductDetailsActivity);
        BusinessTitleTextViewShowProductDetailsActivity = findViewById(R.id.BusinessTitleTextViewShowProductDetailsActivity);
        ShoppingCartIconTextViewShowProductDetailsActivity = findViewById(R.id.ShoppingCartIconTextViewShowProductDetailsActivity);
        HasDeliverySwitchShowProductDetailsActivity = findViewById(R.id.HasDeliverySwitchShowProductDetailsActivity);
        IsOpenSwitchShowProductDetailsActivity = findViewById(R.id.IsOpenSwitchShowProductDetailsActivity);
        ProductImageImageViewShowProductDetailsActivity = findViewById(R.id.ProductImageImageViewShowProductDetailsActivity);
        SliderViewPagerShowProductDetailsActivity = findViewById(R.id.SliderViewPagerShowProductDetailsActivity);
        SliderDotsLinearLayoutShowProductDetailsActivity = findViewById(R.id.SliderDotsLinearLayoutShowProductDetailsActivity);

        SliderViewPagerShowProductDetailsActivity.getLayoutParams().width = LayoutUtility.GetWidthAccordingToScreen(this, 1);
        SliderViewPagerShowProductDetailsActivity.getLayoutParams().height = LayoutUtility.GetWidthAccordingToScreen(this, 2);


        DescriptionIconTextViewShowProductDetailsActivity.setTypeface(Font.MasterIcon);
        DescriptionIconTextViewShowProductDetailsActivity.setText("\uf15c");

        ShoppingCartIconTextViewShowProductDetailsActivity.setTypeface(Font.MasterIcon);
        ShoppingCartIconTextViewShowProductDetailsActivity.setText("\uf07a");

        ProductImageImageViewShowProductDetailsActivity.getLayoutParams().height = LayoutUtility.GetWidthAccordingToScreen(this, 2);
        ProductImageImageViewShowProductDetailsActivity.getLayoutParams().width = LayoutUtility.GetWidthAccordingToScreen(this, 1);

        ProductImageImageViewShowProductDetailsActivity.setVisibility(View.GONE);
        SliderViewPagerShowProductDetailsActivity.setVisibility(View.GONE);
        SliderDotsLinearLayoutShowProductDetailsActivity.setVisibility(View.GONE);

        AvailableSwitchShowProductDetailsActivity.setClickable(false);
        HasDeliverySwitchShowProductDetailsActivity.setClickable(false);
        IsOpenSwitchShowProductDetailsActivity.setClickable(false);


        DescriptionRelativeLayoutShowProductDetailsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent DescriptionProductDetailsIntent = NewIntent(DescriptionBusinessDetailsActivity.class);
                DescriptionProductDetailsIntent.putExtra("Description", Description);
                startActivity(DescriptionProductDetailsIntent);
            }
        });

        AddToShoppingCartRelativeLayoutShowProductDetailsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BasketService BasketService = new BasketService(ShowProductDetailsActivity.this);
                BasketService.GetQuantityByProductId(ProductId);

            }
        });


    }

    /**
     * دریافت اطلاعات نحوای جهت پر کردن Recycle
     */
    public void LoadData() {
        if (!IsSwipe)
            ShowLoadingProgressBar();

        ProductService BusinessService = new ProductService(this);
        BusinessService.Get(ProductId);
    }

    /**
     * @param Data
     * @param ServiceMethod
     * @param <T>
     */
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        HideLoading();
        IsSwipe = false;

        try {
            if (ServiceMethod == ServiceMethodType.ProductGet) {
                Feedback<ProductViewModel> FeedBack = (Feedback<ProductViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    ProductViewModel ViewModel = FeedBack.getValue();

                    if (ViewModel != null) {
                        //پر کردن ویو با اطلاعات دریافتی
                        SetProductToView(ViewModel);
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
            } else if (ServiceMethod == ServiceMethodType.QuantityByProductIdGet) {
                Feedback<Double> FeedBack = (Feedback<Double>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    ShowDialogBasketOrder(FeedBack.getValue());
                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    ShowDialogBasketOrder(0);
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.BasketAdd) {
                Feedback<Double> FeedBack = (Feedback<Double>) Data;

                if (FeedBack.getStatus() == FeedbackType.RegisteredSuccessful.getId()) {
                    String Message = getResources().getString(R.string.product) + " " + ProductName + " " + getResources().getString(R.string.product_added_to_basket);
                    ShowDialogContinueShoppingAndShowBasketOrder(Message);
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.BasketEditQuantityByProductId) {
                Feedback<BasketItemViewModel> FeedBack = (Feedback<BasketItemViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.UpdatedSuccessful.getId()) {

                    String Message = getResources().getString(R.string.order_quantity_product) + " " + ProductName + " " + getResources().getString(R.string.product_was_edited_in_the_basket);
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

    @SuppressLint("NewApi")
    private void SetProductToView(final ProductViewModel ViewModel) {
        ArrayList<String> imagePath = new ArrayList<String>();
        final int DotsCount;
        final ImageView[] Dots;

        BusinessId = ViewModel.getBusinessId();
        ProductName = ViewModel.getName();

        ProductNameTextViewShowProductDetailsActivity.setText(ViewModel.getName());
        AbstractDescriptionProductTextViewShowProductDetailsActivity.setText(ViewModel.getAbstractOfDescription());
        PriceTextViewShowProductDetailsActivity.setText(Utility.GetIntegerNumberWithComma((long) ViewModel.getPrice()));
        BusinessTitleTextViewShowProductDetailsActivity.setText(ViewModel.getBusinessName());
        AvailableSwitchShowProductDetailsActivity.setChecked(ViewModel.isAvailaible());
        HasDeliverySwitchShowProductDetailsActivity.setChecked(ViewModel.isHasDelivery());
        IsOpenSwitchShowProductDetailsActivity.setChecked(ViewModel.isOpen());
        Description = ViewModel.getDescription();

        List<ProductImageViewModel> ProductImageList = new ArrayList<>();
        ProductImageList = ViewModel.getProductImageList();

        if (ProductImageList != null) {
            if (ProductImageList.size() > 1) {
                ProductImageImageViewShowProductDetailsActivity.setVisibility(View.GONE);
                SliderViewPagerShowProductDetailsActivity.setVisibility(View.VISIBLE);
                SliderDotsLinearLayoutShowProductDetailsActivity.setVisibility(View.VISIBLE);

                for (int i = 0; i < ProductImageList.size(); i++) {
                    imagePath.add(ProductImageList.get(i).getFullPath());
                }

                ShowImageProductDetailsPagerAdapter showImageProductDetailsPagerAdapter = new ShowImageProductDetailsPagerAdapter(this, imagePath);
                SliderViewPagerShowProductDetailsActivity.setAdapter(showImageProductDetailsPagerAdapter);

                DotsCount = showImageProductDetailsPagerAdapter.getCount();
                Dots = new ImageView[DotsCount];

                for (int i = 0; i < DotsCount; i++) {

                    Dots[i] = new ImageView(this);
                    Dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(8, 0, 8, 0);
                    SliderDotsLinearLayoutShowProductDetailsActivity.addView(Dots[i], params);

                }

                Dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

                SliderViewPagerShowProductDetailsActivity.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        for (int i = 0; i < DotsCount; i++) {
                            Dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                        }
                        Dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });


            } else if (ProductImageList.size() == 1) {

                LayoutUtility.LoadImageWithGlide(this, ProductImageList.get(0).getFullPath(), ProductImageImageViewShowProductDetailsActivity, LayoutUtility.GetWidthAccordingToScreen(this, 1), LayoutUtility.GetWidthAccordingToScreen(this, 2));
                ProductImageImageViewShowProductDetailsActivity.setVisibility(View.VISIBLE);
                SliderViewPagerShowProductDetailsActivity.setVisibility(View.GONE);
                SliderDotsLinearLayoutShowProductDetailsActivity.setVisibility(View.GONE);
            } else {
                ProductImageImageViewShowProductDetailsActivity.setVisibility(View.VISIBLE);
                SliderViewPagerShowProductDetailsActivity.setVisibility(View.GONE);
                SliderDotsLinearLayoutShowProductDetailsActivity.setVisibility(View.GONE);
                ProductImageImageViewShowProductDetailsActivity.setImageResource(R.drawable.image_default_banner);
            }
        } else {
            ProductImageImageViewShowProductDetailsActivity.setVisibility(View.VISIBLE);
            SliderViewPagerShowProductDetailsActivity.setVisibility(View.GONE);
            SliderDotsLinearLayoutShowProductDetailsActivity.setVisibility(View.GONE);
            ProductImageImageViewShowProductDetailsActivity.setImageResource(R.drawable.image_default_banner);
        }


        ShareImageViewShowProductDetailsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareView(ViewModel);
            }
        });

    }

    private void ShareView(ProductViewModel ViewModel) {
        Intent intent;
        intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);

        String ShareMessage;
        ShareMessage = ViewModel.getName() + "\n" + ViewModel.getBusinessName() + "\n";
        ShareMessage = ShareMessage + getResources().getString(R.string.price) + "\n" + Utility.GetIntegerNumberWithComma((long) ViewModel.getPrice());
        ShareMessage = ShareMessage + Html.fromHtml(ViewModel.getDescription()).toString();


        List<ProductImageViewModel> ProductImageList = ViewModel.getProductImageList();
        String ProductImage = "";
        if (ProductImageList.size() > 0) {
            for (int i = 0; i < ProductImageList.size(); i++) {
                if (!ProductImageList.get(i).getFullPath().equals("")) {
                    if (ProductImageList.get(i).getFullPath().contains("~")) {
                        ProductImage = ProductImageList.get(i).getFullPath().replace("~", DefaultConstant.BaseUrlWebService);
                    } else {
                        ProductImage = ProductImageList.get(i).getFullPath();
                    }
                    break;
                }
            }
        }

        if (ProductImage.trim() != "") {
            String CurrentImageText = getResources().getString(R.string.image_product);
            ShareMessage = ShareMessage + "\n" + CurrentImageText + "\n" + ProductImage;
        }

        intent.putExtra(Intent.EXTRA_TEXT, ShareMessage);

        intent.setType("text/plain");
        startActivity(intent);
    }


    private void ShowDialogBasketOrder(final double ItemQuantity) {
        final Dialog DialogOrder = new Dialog(ShowProductDetailsActivity.this);
        DialogOrder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogOrder.setContentView(R.layout.dialog_order);
        DialogOrder.setCanceledOnTouchOutside(true);

        DialogCustomerQuantityEditText = DialogOrder.findViewById(R.id.CustomerQuantityEditText);
        ButtonPersianView DialogCustomerQuantityAcceptButton = DialogOrder.findViewById(R.id.CustomerQuantityAcceptButton);
        if (ItemQuantity > 0)
            DialogCustomerQuantityEditText.setText(String.valueOf(ItemQuantity));
        ImageView DialogCustomerQuantityAddImage = DialogOrder.findViewById(R.id.CustomerQuantityAddImage);
        ImageView DialogCustomerQuantitySubtractImage = DialogOrder.findViewById(R.id.CustomerQuantitySubtractImage);

        DialogCustomerQuantityAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((DialogCustomerQuantityEditText.getText() == null) || ("".equals(DialogCustomerQuantityEditText.getText().toString())) || (1 > Double.valueOf(DialogCustomerQuantityEditText.getText().toString()))) {
                    DialogCustomerQuantityEditText.setText("1");
                } else {
                    double TempCount = Double.valueOf(DialogCustomerQuantityEditText.getText().toString());
                    if (TempCount == 999) {

                    } else {
                        TempCount++;
                        DialogCustomerQuantityEditText.setText(String.valueOf(TempCount));
                    }

                }
            }
        });

        DialogCustomerQuantitySubtractImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((DialogCustomerQuantityEditText.getText() == null) || ("".equals(DialogCustomerQuantityEditText.getText().toString())) || (1 > Double.valueOf(DialogCustomerQuantityEditText.getText().toString()))) {
                    DialogCustomerQuantityEditText.setText("1");
                } else {
                    double TempCount = Double.valueOf(DialogCustomerQuantityEditText.getText().toString());
                    TempCount--;
                    if (TempCount < 1) {

                    } else {
                        DialogCustomerQuantityEditText.setText(String.valueOf(TempCount));
                    }
                }
            }
        });


        DialogCustomerQuantityAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((DialogCustomerQuantityEditText.getText() == null) || ("".equals(DialogCustomerQuantityEditText.getText().toString())) || (1 > Double.valueOf(DialogCustomerQuantityEditText.getText().toString()))) {
                    ShowToast(getResources().getString(R.string.please_enter_order_quantity), Toast.LENGTH_LONG, MessageType.Error);

                } else {
                    ShowLoadingProgressBar();
                    BasketService BasketService = new BasketService(ShowProductDetailsActivity.this);
                    if (ItemQuantity <= 0) {
                        BasketService.Add(MadeViewModel());
                    } else {
                        BasketService.EditQuantityByProductId(ProductId, Double.valueOf(DialogCustomerQuantityEditText.getText().toString()));
                    }
                    DialogOrder.dismiss();

                }
            }
        });
        DialogOrder.show();


    }

    private void ShowDialogContinueShoppingAndShowBasketOrder(String Message) {

        final Dialog DialogOrder = new Dialog(ShowProductDetailsActivity.this);
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
                DialogOrder.dismiss();
            }
        });

        DialogShowBasketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BasketIntent = new Intent(ShowProductDetailsActivity.this, BasketActivity.class);
                BasketIntent.putExtra("FromActivityId", ActivityIdList.MAIN_ACTIVITY);
                startActivity(BasketIntent);
            }
        });
        DialogOrder.show();


    }

    private StandardOrderItemViewModel MadeViewModel() {
        StandardOrderItemViewModel ViewModel = new StandardOrderItemViewModel();
        try {
            AccountRepository AccountRepository = new AccountRepository(ShowProductDetailsActivity.this);
            AccountViewModel AccountModel = AccountRepository.getAccount();
            ViewModel.setUserId(AccountModel.getUser().getId());

            ViewModel.setValue(Double.valueOf(DialogCustomerQuantityEditText.getText().toString()));
            ViewModel.setProductId(ProductId);
        } catch (Exception Ex) {
        }
        return ViewModel;
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
