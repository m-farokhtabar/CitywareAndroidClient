package ir.rayas.app.citywareclient.View.UserProfileChildren;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.Spinner.ContactTypeSpinnerAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Business.BusinessContactService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResultPassing;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.EditTextPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessContactViewModel;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessContactWithTypesViewModel;
import ir.rayas.app.citywareclient.ViewModel.Definition.ContactTypeViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.UserAddressViewModel;


public class BusinessContactSetActivity extends BaseActivity implements IResponseService, AdapterView.OnItemSelectedListener {

    private Spinner TypeContactSpinnerBusinessContactSetActivity = null;
    private EditTextPersian ContactEditTextBusinessContactSetActivity = null;
    private EditTextPersian ContactTitleEditTextBusinessContactSetActivity = null;

    private int RetryType = 0;
    private String IsSet = "";
    private int BusinessContactId;
    private Integer TypeContactId;
    private int BusinessId;

    private List<ContactTypeViewModel> contactTypeViewModels;

    private BusinessContactViewModel OutputViewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_contact_set);
        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.BUSINESS_CONTACT_SET_ACTIVITY);

        //آماده سازی قسمت لودینگ و پنجره خطا و سیستم پیغام در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonClick();
            }
        },R.string.call_way);

        //مشخص شدن صفحه ویرایش آدرس یا آدرس جدید
        IsSet = getIntent().getExtras().getString("SetBusinessContact");
        BusinessId = getIntent().getExtras().getInt("BusinessId");

        NewContactOrEditAddress();

        //ایجاد طرح بندی صفحه
        CreateLayout();
    }

    /**
     * در صورتی که در ارتباط با اینترنت مشکلی بوجود آمده و کاربر دکمه تلاش مجدد را فشار داده است
     */
    private void RetryButtonClick() {
        switch (RetryType) {
            //ثبت اطلاعات
            case 1:
                HideLoading();
                break;
            //دریافت اطلاعات
            case 2:
                NewContactOrEditAddress();
                break;
        }
    }

    private void CreateLayout() {
        TypeContactSpinnerBusinessContactSetActivity = findViewById(R.id.TypeContactSpinnerBusinessContactSetActivity);
        ContactEditTextBusinessContactSetActivity = findViewById(R.id.ContactEditTextBusinessContactSetActivity);
        ContactTitleEditTextBusinessContactSetActivity = findViewById(R.id.ContactTitleEditTextBusinessContactSetActivity);
        ButtonPersianView SaveContactButtonBusinessContactSetActivity = findViewById(R.id.SaveContactButtonBusinessContactSetActivity);

        SaveContactButtonBusinessContactSetActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnSaveContactButtonClick(view);
            }
        });

        TypeContactSpinnerBusinessContactSetActivity.setOnItemSelectedListener(this);
    }

    private void NewContactOrEditAddress() {
        if (IsSet.equals("Edit")) {
            BusinessContactId = getIntent().getExtras().getInt("BusinessContactId");
            ShowLoadingProgressBar();
            BusinessContactService businessContactService = new BusinessContactService(this);
            //دریافت اطلاعات
            RetryType = 2;
            businessContactService.Get(BusinessContactId);
        } else if (IsSet.equals("New")) {
            BusinessContactId = 0;
            ShowLoadingProgressBar();
            BusinessContactService businessContactService = new BusinessContactService(this);
            //دریافت اطلاعات
            RetryType = 2;
            businessContactService.Get(BusinessContactId);
        }
    }

    /**
     * دکمه ثبت یا ویرایش یک آدرس
     *
     * @param view
     */
    private void OnSaveContactButtonClick(View view) {
        if (Utility.ValidateView(ContactTitleEditTextBusinessContactSetActivity, ContactEditTextBusinessContactSetActivity)) {
            BusinessContactService Service = new BusinessContactService(this);
            //ثبت اطلاعات
            RetryType = 1;
            ShowLoadingProgressBar();
            if (IsSet.equals("Edit")) {
                Service.Set(MadeViewModel(), BusinessContactId);
            } else {
                Service.Add(MadeViewModel());
            }
        }
    }

    private BusinessContactViewModel MadeViewModel() {

        BusinessContactViewModel ViewModel = new BusinessContactViewModel();
        try {
            ViewModel.setBusinessId(BusinessId);
            ViewModel.setTypeId(TypeContactId);
            ViewModel.setTitle(ContactTitleEditTextBusinessContactSetActivity.getText().toString());
            ViewModel.setValue(ContactEditTextBusinessContactSetActivity.getText().toString());
            ViewModel.setId(BusinessContactId);
            ViewModel.setCreate("");
            ViewModel.setModified("");
            ViewModel.setActive(true);

        } catch (Exception Ex) {
        }
        return ViewModel;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.TypeContactSpinnerBusinessContactSetActivity: {
                if (i >= 0)
                    TypeContactId = contactTypeViewModels.get(i).getId();
                else
                    TypeContactId = null;

                //نمایش ایکون کنار spinner تنها در انتخاب یک position خاص یا اولین position
                ImageView ArrowDropDownImageView = view.findViewById(R.id.ArrowDropDownImageView);
                int Position = (int) ArrowDropDownImageView.getTag();

                if (Position == i) {
                    ArrowDropDownImageView.setVisibility(View.VISIBLE);
                } else {
                    ArrowDropDownImageView.setVisibility(View.GONE);
                }
                //--------------------------------------------------------------------------
                break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.BusinessContactGet) {
                Feedback<BusinessContactWithTypesViewModel> FeedBack = (Feedback<BusinessContactWithTypesViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    BusinessContactWithTypesViewModel ViewModel = FeedBack.getValue();

                    if (ViewModel != null) {
                        SetContactTypeToView(ViewModel);
                        OutputViewModel = ViewModel.getBusinessContact();

                        BusinessContactViewModel businessContact = ViewModel.getBusinessContact();
                        if (businessContact != null) {
                            //پر کردن ویو با اطلاعات دریافتی
                            SetContactToView(businessContact);
                        }
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
            } else if (ServiceMethod == ServiceMethodType.BusinessContactSet) {
                Feedback<BusinessContactViewModel> FeedBack = (Feedback<BusinessContactViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.UpdatedSuccessful.getId()) {
                    BusinessContactViewModel ViewModel = FeedBack.getValue();

                    if (ViewModel != null) {
                        //پر کردن ویو با اطلاعات دریافتی
                        SetContactToView(ViewModel);
                        OutputViewModel = ViewModel;
                        SendDataToParentActivity(false);
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
            } else if (ServiceMethod == ServiceMethodType.BusinessContactAdd) {
                Feedback<BusinessContactViewModel> FeedBack = (Feedback<BusinessContactViewModel>) Data;
                if (FeedBack.getStatus() == FeedbackType.RegisteredSuccessful.getId()) {
                    BusinessContactViewModel ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        OutputViewModel = ViewModel;
                        SendDataToParentActivity(true);
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
            }
        } catch (Exception e) {
            HideLoading();
            ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }

    }

    public void SetContactTypeToView(BusinessContactWithTypesViewModel ViewModel) {
        if (ViewModel.getContactTypeList() != null)
            contactTypeViewModels = ViewModel.getContactTypeList();
        else
            contactTypeViewModels = new ArrayList<>();

        ContactTypeSpinnerAdapter contactTypeSpinnerAdapter = new ContactTypeSpinnerAdapter(this, contactTypeViewModels);
        TypeContactSpinnerBusinessContactSetActivity.setAdapter(contactTypeSpinnerAdapter);

    }

    public void SetContactToView(BusinessContactViewModel ViewModel) {

        BusinessContactId = ViewModel.getId();
        ContactTitleEditTextBusinessContactSetActivity.setText(ViewModel.getTitle());
        ContactEditTextBusinessContactSetActivity.setText(ViewModel.getValue());

        if (ViewModel.getTypeId() != 0)
            TypeContactSpinnerBusinessContactSetActivity.setSelection(SetPositionToSpinnerTypeContact(ViewModel.getTypeId(), contactTypeViewModels));
    }

    private int SetPositionToSpinnerTypeContact(int SelectId, List<ContactTypeViewModel> ViewModel) {
        int Position = 0;

        if (ViewModel != null) {
            for (int i = 0; i < ViewModel.size(); i++) {
                if (SelectId == ViewModel.get(i).getId()) {
                    Position = i;
                }
            }
        } else {
            Position = 0;
        }
        return Position;
    }
    /**
     * دریافت ویومدل از اکتیویتی تماس جهت ارسال به فرگمنت لیست تماس
     */
    private void SendDataToParentActivity(boolean IsAdd) {
        HashMap<String, Object> Output = new HashMap<>();
        try {
            for (int i=0;i<contactTypeViewModels.size();i++){
                if (OutputViewModel.getTypeId() == contactTypeViewModels.get(i).getId()){
                    OutputViewModel.setTypeName(contactTypeViewModels.get(i).getTitle());
                }
            }
            Output.put("IsAdd", IsAdd);
            Output.put("BusinessContactViewModel", OutputViewModel);
        } catch (Exception Ex) {
            Output.put("BusinessContactViewModel", null);
        }
        ActivityResultPassing.Push(new ActivityResult(getParentActivity(), getCurrentActivityId(), Output));
        FinishCurrentActivity();
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
