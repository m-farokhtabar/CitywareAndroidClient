package ir.rayas.app.citywareclient.View.Fragment.Product;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import java.math.BigDecimal;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Order.ProductService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.EditMoney;
import ir.rayas.app.citywareclient.Share.Layout.View.EditTextPersian;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.UserProfileChildren.ProductSetActivity;
import ir.rayas.app.citywareclient.ViewModel.Order.ProductViewModel;

public class ProductFragment extends Fragment implements IResponseService, ILoadData {

    private ProductSetActivity Context = null;
    private EditTextPersian ProductNameEditTextProductFragment = null;
    private EditTextPersian AbstractOfDescriptionEditTextProductFragment = null;
    private EditMoney PriceEditTextProductFragment = null;
    private EditTextPersian InventoryEditTextProductFragment = null;
    private EditTextPersian OrderEditTextProductFragment = null;
    private EditTextPersian ProductCommissionEditTextProductFragment = null;
    private EditTextPersian ProductDescriptionEditTextProductFragment = null;
    private SwipeRefreshLayout SwipeRefreshLayoutProductFragment;
    private SwitchCompat IsAvailableSwitchProductFragment = null;

    private boolean IsSwipe = false;
    private int ProductId = 0;

    //جهت ارسال به اکتیویتی لیست محصولات ها تا در صورت ویرایش کاربر آیتم مورد نظر نیز در لیست ویرایش شود
    private ProductViewModel OutputViewModel = null;

    /**
     * ارسال آخرین ویومدل تغییرات محصول به اکتیویتی کسب و کار
     *
     * @return بیزینس آخرین تغییرات
     */
    public ProductViewModel getOutputViewModel() {
        return OutputViewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (ProductSetActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_product, container, false);
        //طرحبندی ویو
        CreateLayout(CurrentView);
        //کد 0 فرگمنت ویرایش اطلاعات کاربر است
        //برای فهمیدن کد فرگنت به UserProfilePagerAdapter مراجعه کنید
        Context.setFragmentIndex(1);
        ProductId = Context.getProductId();
        //دریافت اطلاعات از سرور
        LoadData();

        Utility.HideKeyboard(Context);
        return CurrentView;
    }

    private void CreateLayout(View CurrentView) {
        ProductCommissionEditTextProductFragment = CurrentView.findViewById(R.id.ProductCommissionEditTextProductFragment);
        TextViewPersian MinusCommissionTextViewUserProductFragment = CurrentView.findViewById(R.id.MinusCommissionTextViewUserProductFragment);
        TextViewPersian PlusCommissionTextViewUserProductFragment = CurrentView.findViewById(R.id.PlusCommissionTextViewUserProductFragment);

        MinusCommissionTextViewUserProductFragment.setTypeface(Font.MasterIcon);
        MinusCommissionTextViewUserProductFragment.setText("\uf068");

        MinusCommissionTextViewUserProductFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double Commission;
                if ((ProductCommissionEditTextProductFragment.getText().toString().equals(""))) {
                    Commission = 0.0;
                } else {
                    Commission = Double.parseDouble(ProductCommissionEditTextProductFragment.getText().toString().trim());
                }

                if (Commission>100)
                    ProductCommissionEditTextProductFragment.setText(String.valueOf(100));
                else {
                    if (Commission > 0.5) {
                        Commission = Commission - 0.5;
                        ProductCommissionEditTextProductFragment.setText(String.valueOf(Commission));
                    } else {
                        ProductCommissionEditTextProductFragment.setText(String.valueOf(0));
                    }
                }
            }
        });

        PlusCommissionTextViewUserProductFragment.setTypeface(Font.MasterIcon);
        PlusCommissionTextViewUserProductFragment.setText("\uf067");

        PlusCommissionTextViewUserProductFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double Commission;
                if ((ProductCommissionEditTextProductFragment.getText().toString().equals(""))) {
                    Commission = 0.0;
                } else {
                    Commission = Double.parseDouble(ProductCommissionEditTextProductFragment.getText().toString().trim());
                }
                if (Commission<0){
                    ProductCommissionEditTextProductFragment.setText(String.valueOf(0));
                }
                else {
                    if (Commission < 99.5) {
                        Commission = Commission + 0.5;
                        ProductCommissionEditTextProductFragment.setText(String.valueOf(Commission));
                    }
                    else
                        ProductCommissionEditTextProductFragment.setText(String.valueOf(100));
                }
            }
        });

        TextViewPersian MinusOrderTextViewUserProductFragment = CurrentView.findViewById(R.id.MinusOrderTextViewUserProductFragment);
        TextViewPersian PlusOrderTextViewUserProductFragment = CurrentView.findViewById(R.id.PlusOrderTextViewUserProductFragment);
        OrderEditTextProductFragment = CurrentView.findViewById(R.id.OrderEditTextProductFragment);

        MinusOrderTextViewUserProductFragment.setTypeface(Font.MasterIcon);
        MinusOrderTextViewUserProductFragment.setText("\uf068");

        MinusOrderTextViewUserProductFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long Counter;
                if ((OrderEditTextProductFragment.getText().toString().equals(""))) {
                    Counter = 0;
                } else {
                    Counter = Long.parseLong(OrderEditTextProductFragment.getText().toString().trim());
                }

                if (Counter>9999)
                    OrderEditTextProductFragment.setText(String.valueOf(9999));
                else {
                    if (Counter > 1) {
                        Counter = Counter - 1;
                        OrderEditTextProductFragment.setText(String.valueOf(Counter));
                    } else {
                        OrderEditTextProductFragment.setText(String.valueOf(0));
                    }
                }
            }
        });

        PlusOrderTextViewUserProductFragment.setTypeface(Font.MasterIcon);
        PlusOrderTextViewUserProductFragment.setText("\uf067");

        PlusOrderTextViewUserProductFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long Counter;
                if ((OrderEditTextProductFragment.getText().toString().equals(""))) {
                    Counter = 0;
                } else {
                    Counter = Long.parseLong(OrderEditTextProductFragment.getText().toString().trim());
                }
                if (Counter<0){
                    OrderEditTextProductFragment.setText(String.valueOf(0));
                }
                else {
                    if (Counter < 9998) {
                        Counter = Counter + 1;
                        OrderEditTextProductFragment.setText(String.valueOf(Counter));
                    }
                    else
                        OrderEditTextProductFragment.setText(String.valueOf(9999));
                }
            }
        });

        TextViewPersian MinusInventoryTextViewUserProductFragment = CurrentView.findViewById(R.id.MinusInventoryTextViewUserProductFragment);
        TextViewPersian PlusInventoryTextViewUserProductFragment = CurrentView.findViewById(R.id.PlusInventoryTextViewUserProductFragment);
        InventoryEditTextProductFragment = CurrentView.findViewById(R.id.InventoryEditTextProductFragment);

        MinusInventoryTextViewUserProductFragment.setTypeface(Font.MasterIcon);
        MinusInventoryTextViewUserProductFragment.setText("\uf068");

        MinusInventoryTextViewUserProductFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double Counter;
                if ((InventoryEditTextProductFragment.getText().toString().equals(""))) {
                    Counter = 0.0;
                } else {
                    Counter = Double.parseDouble(InventoryEditTextProductFragment.getText().toString().trim());
                }

                if (Counter>99999998)
                    InventoryEditTextProductFragment.setText(String.valueOf(99999999));
                else {
                    if (Counter > 1) {
                        Counter = Counter - 1;
                        InventoryEditTextProductFragment.setText(String.valueOf(Counter));
                    } else {
                        InventoryEditTextProductFragment.setText(String.valueOf(0));
                    }
                }
            }
        });

        PlusInventoryTextViewUserProductFragment.setTypeface(Font.MasterIcon);
        PlusInventoryTextViewUserProductFragment.setText("\uf067");

        PlusInventoryTextViewUserProductFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double Counter;
                if ((InventoryEditTextProductFragment.getText().toString().equals(""))) {
                    Counter = 0.0;
                } else {
                    Counter = Double.parseDouble(InventoryEditTextProductFragment.getText().toString().trim());
                }
                if (Counter<0){
                    InventoryEditTextProductFragment.setText(String.valueOf(0));
                }
                else {
                    if (Counter < 99999998) {
                        Counter = Counter + 1;
                        InventoryEditTextProductFragment.setText(String.valueOf(Counter));
                    }
                    else
                        InventoryEditTextProductFragment.setText(String.valueOf(99999999));
                }
            }
        });


        ProductNameEditTextProductFragment = CurrentView.findViewById(R.id.ProductNameEditTextProductFragment);
        AbstractOfDescriptionEditTextProductFragment = CurrentView.findViewById(R.id.AbstractOfDescriptionEditTextProductFragment);
        PriceEditTextProductFragment = CurrentView.findViewById(R.id.PriceEditTextProductFragment);
        SwipeRefreshLayoutProductFragment = CurrentView.findViewById(R.id.SwipeRefreshLayoutProductFragment);
        IsAvailableSwitchProductFragment = CurrentView.findViewById(R.id.IsAvailableSwitchProductFragment);

        ProductDescriptionEditTextProductFragment = CurrentView.findViewById(R.id.ProductDescriptionEditTextProductFragment);

        SwipeRefreshLayoutProductFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                IsSwipe = true;
                LoadData();
            }
        });

        ButtonPersianView SetButtonProductFragment = CurrentView.findViewById(R.id.SetButtonProductFragment);
        SetButtonProductFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnSaveProductButtonClick(view);
            }
        });
    }

    @Override
    public void LoadData() {
        if (Context.getIsSet().equals("Edit")) {
            if (!IsSwipe)
                Context.ShowLoadingProgressBar();
            ProductService BusinessService = new ProductService(this);
            //دریافت اطلاعات
            Context.setRetryType(2);
            BusinessService.Get(ProductId);
        } else if (Context.getIsSet().equals("New")) {
            SwipeRefreshLayoutProductFragment.setRefreshing(false);
        }
    }

    private void OnSaveProductButtonClick(View view) {
        if (Utility.ValidateView(ProductNameEditTextProductFragment, AbstractOfDescriptionEditTextProductFragment, PriceEditTextProductFragment,
                InventoryEditTextProductFragment, IsAvailableSwitchProductFragment, OrderEditTextProductFragment,
                ProductCommissionEditTextProductFragment, ProductDescriptionEditTextProductFragment)) {

            Context.ShowLoadingProgressBar();
            ProductService Service = new ProductService(this);
            //ثبت اطلاعات
            Context.setRetryType(1);
            if (Context.getIsSet().equals("Edit")) {
                Service.Set(MadeViewModel(),Context.getProductId());
            } else {
                Service.Add(MadeViewModel());
            }
        }
    }

    private ProductViewModel MadeViewModel() {
        ProductViewModel ViewModel = new ProductViewModel();
        try {
            ViewModel.setId(ProductId);
            ViewModel.setBusinessId(Context.getBusinessId());
            ViewModel.setName(ProductNameEditTextProductFragment.getText().toString());
            ViewModel.setAbstractOfDescription(AbstractOfDescriptionEditTextProductFragment.getText().toString());
            //String PriceText = PriceEditTextProductFragment.getText().toString().replaceAll("[^0-9]","");
            String PriceText = PriceEditTextProductFragment.getText().toString().replaceAll("[,]", "");
            PriceText = PriceText.replaceAll("[٬]", "");
            if (!PriceText.trim().equals("")) {
                BigDecimal PriceDecimal = new BigDecimal(PriceText);
                ViewModel.setPrice(PriceDecimal.doubleValue());
            }
            else
                ViewModel.setPrice(0);

            double Inventory = Double.parseDouble(InventoryEditTextProductFragment.getText().toString());
            ViewModel.setInventory(Inventory);

            ViewModel.setAvailaible(IsAvailableSwitchProductFragment.isChecked());

            int Order = Integer.parseInt(OrderEditTextProductFragment.getText().toString());
            if (Order >= 0)
                ViewModel.setOrder(Order);
            else
                Context.ShowToast(Context.getResources().getString(R.string.order_must_be_greater_than_zero), Toast.LENGTH_LONG, MessageType.values()[1]);

            double Commission = Double.parseDouble(ProductCommissionEditTextProductFragment.getText().toString());
            if (Commission >= 0) {
                if (Commission <= 100) {
                    ViewModel.setCommission(Commission);
                } else {
                    Context.ShowToast(Context.getResources().getString(R.string.order_must_be_greater_than_zero), Toast.LENGTH_LONG, MessageType.values()[1]);
                }
            } else {
                Context.ShowToast(Context.getResources().getString(R.string.commission_should_be_between_zero_to_hundred), Toast.LENGTH_LONG, MessageType.values()[1]);
            }

            if (ProductDescriptionEditTextProductFragment.getText()!=null)
                ViewModel.setDescription(ProductDescriptionEditTextProductFragment.getText().toString());
            else
                ViewModel.setDescription("");
            ViewModel.setActive(true);
            ViewModel.setCreate("");
            ViewModel.setModified("");
            ViewModel.setProductImageList(null);


        } catch (Exception Ex) {
            Context.ShowToast(FeedbackType.InvalidDataFormat.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
        return ViewModel;
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        SwipeRefreshLayoutProductFragment.setRefreshing(false);
        try {
            if (ServiceMethod == ServiceMethodType.ProductGet) {
                Feedback<ProductViewModel> FeedBack = (Feedback<ProductViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    ProductViewModel ViewModel = FeedBack.getValue();

                    if (ViewModel != null) {
                        //پر کردن ویو با اطلاعات دریافتی
                        SetProductToView(ViewModel);
                        //پر کردن ویومدل برای ارسال به لیست اکتیویتی والد جهت به روز رسانی لیست
                        OutputViewModel = ViewModel;
                    } else {
                        Context.ShowToast(FeedbackType.InvalidDataFormat.getMessage().replace("{0}", ""), Toast.LENGTH_LONG, MessageType.Warning);
                    }

                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }

            } else if (ServiceMethod == ServiceMethodType.ProductSet) {
                Feedback<ProductViewModel> FeedBack = (Feedback<ProductViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.UpdatedSuccessful.getId()) {
                    ProductViewModel ViewModel = FeedBack.getValue();

                    if (ViewModel != null) {
                        //پر کردن ویومدل برای ارسال به لیست اکتیویتی والد جهت به روز رسانی لیست
                        OutputViewModel = ViewModel;
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                        SetProductToView(ViewModel);
                    } else {
                        Context.ShowToast(FeedbackType.InvalidDataFormat.getMessage().replace("{0}", ""), Toast.LENGTH_LONG, MessageType.Warning);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        //ProductNameEditTextProductFragment.requestFocus();
                        //Utility.ShowKeyboard(ProductNameEditTextProductFragment);
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.ProductAdd) {
                Feedback<ProductViewModel> FeedBack = (Feedback<ProductViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.RegisteredSuccessful.getId()) {
                    ProductViewModel ViewModel = FeedBack.getValue();

                    if (ViewModel != null) {
                        //پر کردن ویومدل برای ارسال به لیست اکتیویتی والد جهت به روز رسانی لیست
                        OutputViewModel = ViewModel;
                        Context.setProductId(ViewModel.getId());
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                        SetProductToView(ViewModel);
                        Context.setIsSet("Edit");
                        Context.EnableOrDisableChildTab(true);
                    } else {
                        Context.ShowToast(FeedbackType.InvalidDataFormat.getMessage().replace("{0}", ""), Toast.LENGTH_LONG, MessageType.Warning);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        //ProductNameEditTextProductFragment.requestFocus();
                        //Utility.ShowKeyboard(ProductNameEditTextProductFragment);
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            }
        } catch (Exception e) {
            Context.HideLoading();
            Context.ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
    }

    private void SetProductToView(ProductViewModel ViewModel) {

        ProductId = ViewModel.getId();
        ProductNameEditTextProductFragment.setText(ViewModel.getName());
        AbstractOfDescriptionEditTextProductFragment.setText(ViewModel.getAbstractOfDescription());
        PriceEditTextProductFragment.setText(String.valueOf((long)ViewModel.getPrice()));
        InventoryEditTextProductFragment.setText(String.valueOf(ViewModel.getInventory()));
        OrderEditTextProductFragment.setText(String.valueOf(ViewModel.getOrder()));
        ProductCommissionEditTextProductFragment.setText(String.valueOf(ViewModel.getCommission()));
        ProductDescriptionEditTextProductFragment.setText(ViewModel.getDescription());
        IsAvailableSwitchProductFragment.setChecked(ViewModel.isAvailaible());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (Context != null) {
                //برای فهمیدن کد فرگنت به ProductSetPagerAdapter مراجعه کنید
                Context.setFragmentIndex(1);
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}
