package ir.rayas.app.citywareclient.View.MasterChildren;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Typeface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.OrderProductsRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Order.ProductService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResultPassing;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Marketing.BusinessCommissionAndDiscountViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.ProductCommissionAndDiscountModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.ProductCommissionAndDiscountViewModel;
import ir.rayas.app.citywareclient.ViewModel.Marketing.ProductInfoViewModel;

public class ProductListOrderActivity extends BaseActivity implements IResponseService, ILoadData {

    private SwipeRefreshLayout RefreshOrderSwipeRefreshLayoutProductListOrderActivity = null;
    private TextViewPersian ShowEmptyOrderTextViewProductListOrderActivity = null;
    private TextViewPersian OtherProductMarketerPercentTextViewProductListOrderActivity = null;
    private TextViewPersian OtherProductCustomerPercentTextViewProductListOrderActivity = null;
    private RecyclerView ProductRecyclerViewProductListOrderActivity = null;


    private boolean IsSwipe = false;
    private int BusinessId = 0;
    private String Percents = "";
    private Double MarketerPercent;
    private Double CustomerPercent;
    private Double ApplicationPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list_order);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.PRODUCT_LIST_ORDER_ACTIVITY);

        BusinessId = getIntent().getExtras().getInt("BusinessId");
        Percents = getIntent().getExtras().getString("Percents");

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.select_product_service);


        //ایجاد طرح بندی صفحه
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
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    private void CreateLayout() {
        ShowEmptyOrderTextViewProductListOrderActivity = findViewById(R.id.ShowEmptyOrderTextViewProductListOrderActivity);
        RefreshOrderSwipeRefreshLayoutProductListOrderActivity = findViewById(R.id.RefreshOrderSwipeRefreshLayoutProductListOrderActivity);
        OtherProductMarketerPercentTextViewProductListOrderActivity = findViewById(R.id.OtherProductMarketerPercentTextViewProductListOrderActivity);
        OtherProductCustomerPercentTextViewProductListOrderActivity = findViewById(R.id.OtherProductCustomerPercentTextViewProductListOrderActivity);
        TableRow TitleOtherProductTableRowProductListOrderActivity = findViewById(R.id.TitleOtherProductTableRowProductListOrderActivity);

        ShowEmptyOrderTextViewProductListOrderActivity.setVisibility(View.GONE);

        ProductRecyclerViewProductListOrderActivity = findViewById(R.id.ProductRecyclerViewProductListOrderActivity);
        ProductRecyclerViewProductListOrderActivity.setHasFixedSize(true);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(ProductListOrderActivity.this);
        ProductRecyclerViewProductListOrderActivity.setLayoutManager(LinearLayoutManager);


        RefreshOrderSwipeRefreshLayoutProductListOrderActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                IsSwipe = true;
                LoadData();
            }
        });

        TitleOtherProductTableRowProductListOrderActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowEditOrderProductDialog();
            }
        });
    }

    /**
     * دریافت اطلاعات نحوای جهت پر کردن Recycle
     */
    public void LoadData() {
        if (!IsSwipe)
            ShowLoadingProgressBar();

        ProductService productService = new ProductService(this);
        productService.GetAll(BusinessId);

    }

    /**
     * @param Data
     * @param ServiceMethod
     * @param <T>
     */
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        HideLoading();
        RefreshOrderSwipeRefreshLayoutProductListOrderActivity.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.ProductInfoGetAll) {
                Feedback<List<ProductInfoViewModel>> FeedBack = (Feedback<List<ProductInfoViewModel>>) Data;

                List<ProductInfoViewModel> productViewModelList;
                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    if (FeedBack.getValue() != null) {
                        productViewModelList = FeedBack.getValue();
                        ComparisonProduct(productViewModelList);
                    } else {
                        productViewModelList = new ArrayList<>();
                        ComparisonProduct(productViewModelList);
                    }

                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {

                    productViewModelList = new ArrayList<>();
                    ComparisonProduct(productViewModelList);

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

    @SuppressLint("SetTextI18n")
    private void ComparisonProduct(List<ProductInfoViewModel> productViewModels) {

        final BusinessCommissionAndDiscountViewModel businessCommissionAndDiscountViewModel;
        Gson gson = new Gson();
        Type listType = new TypeToken<BusinessCommissionAndDiscountViewModel>() {
        }.getType();
        businessCommissionAndDiscountViewModel = gson.fromJson(Percents, listType);

        ApplicationPercent = businessCommissionAndDiscountViewModel.getApplicationPercent();
        MarketerPercent = businessCommissionAndDiscountViewModel.getMarketerPercent();
        CustomerPercent = businessCommissionAndDiscountViewModel.getCustomerPercent();

        double Percent = MarketerPercent +   businessCommissionAndDiscountViewModel.getApplicationPercent();
        Percent = Math.round(Percent*Math.pow(10,2))/Math.pow(10,2);

        OtherProductMarketerPercentTextViewProductListOrderActivity.setText(Percent + " " + getResources().getString(R.string.percent));
        OtherProductCustomerPercentTextViewProductListOrderActivity.setText(CustomerPercent + " " + getResources().getString(R.string.percent));

        List<ProductCommissionAndDiscountModel> ResultProductList = new ArrayList<>();
        List<ProductCommissionAndDiscountViewModel> ProductList = new ArrayList<>();
        boolean IsEmptyPercent;
        boolean IsEmptyProduct;

        if (businessCommissionAndDiscountViewModel.getProductList() == null) {
            IsEmptyPercent = true;
        } else if (businessCommissionAndDiscountViewModel.getProductList().size() == 0) {
            IsEmptyPercent = true;
        } else {
            IsEmptyPercent = false;
            ProductList = businessCommissionAndDiscountViewModel.getProductList();
        }

        if (productViewModels == null) {
            IsEmptyProduct = true;
        } else if (productViewModels.size() == 0) {
            IsEmptyProduct = true;
        } else {
            IsEmptyProduct = false;
        }


        if (IsEmptyProduct) {
            ProductRecyclerViewProductListOrderActivity.setVisibility(View.GONE);
            ShowEmptyOrderTextViewProductListOrderActivity.setVisibility(View.VISIBLE);

        } else {

            if (IsEmptyPercent) {
                // همه محصولات از تخفیف و پورسانت یکسانی استفاده می کنند

                for (int i = 0; i < productViewModels.size(); i++) {

                    ProductCommissionAndDiscountModel productCommissionAndDiscountViewModel = new ProductCommissionAndDiscountModel();
                    productCommissionAndDiscountViewModel.setCustomerPercent(businessCommissionAndDiscountViewModel.getCustomerPercent());
                    productCommissionAndDiscountViewModel.setMarketerPercent(businessCommissionAndDiscountViewModel.getMarketerPercent());
                    productCommissionAndDiscountViewModel.setApplicationPercent(businessCommissionAndDiscountViewModel.getApplicationPercent());
                    productCommissionAndDiscountViewModel.setProductId(productViewModels.get(i).getId());
                    productCommissionAndDiscountViewModel.setProductName(productViewModels.get(i).getName());
                    productCommissionAndDiscountViewModel.setApplicationPercent(businessCommissionAndDiscountViewModel.getApplicationPercent());
                    productCommissionAndDiscountViewModel.setNumberOfOrder(0.0);
                    productCommissionAndDiscountViewModel.setTotalPrice(0.0);
                    if (productViewModels.get(i).getPrice() == null) {
                        productCommissionAndDiscountViewModel.setPrice(0.0);
                    } else {
                        productCommissionAndDiscountViewModel.setPrice(productViewModels.get(i).getPrice());
                    }

                    ResultProductList.add(productCommissionAndDiscountViewModel);
                }
            } else {

                List<Integer> ProductListIndex = new ArrayList<>();
                List<Integer> productViewModelsIndex = new ArrayList<>();

                for (int j = 0; j < ProductList.size(); j++) {
                    for (int i = 0; i < productViewModels.size(); i++) {

                        if (ProductList.get(j).getProductId() == productViewModels.get(i).getId()) {

                            ProductCommissionAndDiscountModel productCommissionAndDiscountViewModel = new ProductCommissionAndDiscountModel();
                            productCommissionAndDiscountViewModel.setCustomerPercent(ProductList.get(i).getCustomerPercent());
                            productCommissionAndDiscountViewModel.setMarketerPercent(ProductList.get(i).getMarketerPercent());
                            productCommissionAndDiscountViewModel.setApplicationPercent(ProductList.get(i).getApplicationPercent());
                            productCommissionAndDiscountViewModel.setProductId(ProductList.get(i).getProductId());
                            productCommissionAndDiscountViewModel.setProductName(ProductList.get(i).getProductName());
                            productCommissionAndDiscountViewModel.setApplicationPercent(ProductList.get(i).getApplicationPercent());
                            productCommissionAndDiscountViewModel.setPrice(productViewModels.get(i).getPrice());
                            productCommissionAndDiscountViewModel.setTotalPrice(0.0);
                            productCommissionAndDiscountViewModel.setNumberOfOrder(0.0);


                            ResultProductList.add(productCommissionAndDiscountViewModel);

                            ProductListIndex.add(j);
                            productViewModelsIndex.add(i);
                        }
                    }
                }

                for (int j = 0; j < ProductList.size(); j++) {

                    boolean IsCheck = false;

                    for (int i = 0; i < ProductListIndex.size(); i++) {
                        if (j == ProductListIndex.get(i)) {
                            IsCheck = true;
                        }
                    }
                    if (!IsCheck) {
                        ProductCommissionAndDiscountModel productCommissionAndDiscountViewModel = new ProductCommissionAndDiscountModel();
                        productCommissionAndDiscountViewModel.setCustomerPercent(ProductList.get(j).getCustomerPercent());
                        productCommissionAndDiscountViewModel.setMarketerPercent(ProductList.get(j).getMarketerPercent());
                        productCommissionAndDiscountViewModel.setApplicationPercent(ProductList.get(j).getApplicationPercent());
                        productCommissionAndDiscountViewModel.setProductId(ProductList.get(j).getProductId());
                        productCommissionAndDiscountViewModel.setProductName(ProductList.get(j).getProductName());
                        productCommissionAndDiscountViewModel.setApplicationPercent(ProductList.get(j).getApplicationPercent());
                        productCommissionAndDiscountViewModel.setPrice(0.0);
                        productCommissionAndDiscountViewModel.setTotalPrice(0.0);
                        productCommissionAndDiscountViewModel.setNumberOfOrder(0.0);

                        ResultProductList.add(productCommissionAndDiscountViewModel);
                    }
                }

                for (int j = 0; j < productViewModels.size(); j++) {

                    boolean IsCheck = false;

                    for (int i = 0; i < productViewModelsIndex.size(); i++) {
                        if (j == productViewModelsIndex.get(i)) {
                            IsCheck = true;
                        }
                    }
                    if (!IsCheck) {
                        ProductCommissionAndDiscountModel productCommissionAndDiscountViewModel = new ProductCommissionAndDiscountModel();
                        productCommissionAndDiscountViewModel.setCustomerPercent(businessCommissionAndDiscountViewModel.getCustomerPercent());
                        productCommissionAndDiscountViewModel.setMarketerPercent(businessCommissionAndDiscountViewModel.getMarketerPercent());
                        productCommissionAndDiscountViewModel.setApplicationPercent(businessCommissionAndDiscountViewModel.getApplicationPercent());
                        productCommissionAndDiscountViewModel.setProductId(productViewModels.get(j).getId());
                        productCommissionAndDiscountViewModel.setProductName(productViewModels.get(j).getName());
                        productCommissionAndDiscountViewModel.setApplicationPercent(businessCommissionAndDiscountViewModel.getApplicationPercent());
                        productCommissionAndDiscountViewModel.setNumberOfOrder(0.0);
                        productCommissionAndDiscountViewModel.setTotalPrice(0.0);
                        if (productViewModels.get(j).getPrice() == null) {
                            productCommissionAndDiscountViewModel.setPrice(0.0);
                        } else {
                            productCommissionAndDiscountViewModel.setPrice(productViewModels.get(j).getPrice());
                        }
                        ResultProductList.add(productCommissionAndDiscountViewModel);
                    }
                }

            }
        }


        OrderProductsRecyclerViewAdapter orderProductsRecyclerViewAdapter = new OrderProductsRecyclerViewAdapter(ProductListOrderActivity.this, ResultProductList, ProductRecyclerViewProductListOrderActivity);
        ProductRecyclerViewProductListOrderActivity.setAdapter(orderProductsRecyclerViewAdapter);
    }

    private void ShowEditOrderProductDialog() {

        final Dialog EditOrderProductDialog = new Dialog(ProductListOrderActivity.this);
        EditOrderProductDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        EditOrderProductDialog.setContentView(R.layout.dialog_order_other_product);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/iransanslight.ttf");

        ButtonPersianView DialogOrderProductOkButton = EditOrderProductDialog.findViewById(R.id.DialogOrderProductOkButton);
        ButtonPersianView DialogOrderProductCancelButton = EditOrderProductDialog.findViewById(R.id.DialogOrderProductCancelButton);
        final EditText UnitPriceProductTextView = EditOrderProductDialog.findViewById(R.id.UnitPriceProductTextView);
        final EditText CountProductTextView = EditOrderProductDialog.findViewById(R.id.CountProductTextView);
        final EditText ProductNameTextView = EditOrderProductDialog.findViewById(R.id.ProductNameTextView);


        UnitPriceProductTextView.setTypeface(typeface);
        CountProductTextView.setTypeface(typeface);
        ProductNameTextView.setTypeface(typeface);

        UnitPriceProductTextView.setText(getResources().getString(R.string.zero));
        CountProductTextView.setText(getResources().getString(R.string.zero));

        DialogOrderProductOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ProductNameTextView.getText().toString().equals("")) {
                    ShowToast(getResources().getString(R.string.please_enter_product_name), Toast.LENGTH_LONG, MessageType.Warning);
                } else {
                    if (Double.parseDouble(CountProductTextView.getText().toString()) <=  0) {
                        ShowToast(getResources().getString(R.string.please_enter_order_quantity), Toast.LENGTH_LONG, MessageType.Warning);
                    } else {
                        ProductCommissionAndDiscountModel ViewModel = new ProductCommissionAndDiscountModel();
                        ViewModel.setProductName(ProductNameTextView.getText().toString());
                        ViewModel.setPrice(Double.parseDouble(UnitPriceProductTextView.getText().toString()));
                        ViewModel.setNumberOfOrder(Double.parseDouble(CountProductTextView.getText().toString()));
                        ViewModel.setApplicationPercent(ApplicationPercent);
                        ViewModel.setCustomerPercent(CustomerPercent);
                        ViewModel.setMarketerPercent(MarketerPercent);
                        ViewModel.setProductId(0);

                        Double TotalPrice = Double.parseDouble(UnitPriceProductTextView.getText().toString()) * Double.parseDouble(CountProductTextView.getText().toString());
                        ViewModel.setTotalPrice(TotalPrice);


                        SendDataToParentActivity(ViewModel);
//        //این قسمت به دلیل SingleInstance بودن Parent بایستی مطمئن شوبم که اکتیویتی Parent بعد از اتمام این اکتیویتی دوباره صدا  زده می شود
//        //در حالت خروج از برنامه و ورود دوباره این اکتیوتی ممکن است Parent خود را گم کند
                        FinishCurrentActivity();


                        EditOrderProductDialog.dismiss();

                    }
                }

            }
        });

        DialogOrderProductCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditOrderProductDialog.dismiss();
            }
        });

        EditOrderProductDialog.show();
    }

    private void SendDataToParentActivity(ProductCommissionAndDiscountModel ViewModel) {

        HashMap<String, Object> Output = new HashMap<>();
        Output.put("ProductCommissionAndDiscountModel", ViewModel);
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
        super.onBackPressed();
    }
}



