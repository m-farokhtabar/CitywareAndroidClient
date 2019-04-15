package ir.rayas.app.citywareclient.View.UserProfileChildren;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Uploader.ImageService;
import ir.rayas.app.citywareclient.Service.Order.ProductImageService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResultPassing;
import ir.rayas.app.citywareclient.Share.Helper.ExternalStorage;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.EditTextPersian;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Order.ProductImageViewModel;
import ir.rayas.app.citywareclient.ViewModel.Uploader.FileViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;

public class ProductImageSetActivity extends BaseActivity implements IResponseService {

    private EditTextPersian OrderEditTextProductImageSetActivity = null;
    private ImageView ShowImageViewProductImageSetActivity = null;

    private int RetryType = 0;
    private int Order = 0;
    private String IsSet = "";
    private String Path = "";
    private int ProductImageId = 0;
    private int ProductId = 0;
    private Uri UrlOfSelectedImageFromLocal = null;

    //Upload
    private final String twoHyphens = "--";
    private final String lineEnd = "\r\n";
    private final String boundary = "apiclient-" + System.currentTimeMillis();
    private final String mimeType = "multipart/form-data;boundary=" + boundary;
    private byte[] multipartBody;

    //جهت ارسال به اکتیویتی محصول ها تا در صورت ویرایش کاربر آیتم مورد نظر نیز در لیست ویرایش شود
    private ProductImageViewModel OutputViewModel = null;

    private ExternalStorage CurrentExternalStorage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_image_set);
        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.PRODUCT_IMAGE_SET_ACTIVITY);


        //مشخص شدن صفحه ویرایش آدرس یا آدرس جدید
        IsSet = getIntent().getExtras().getString("SetProductImage");
        ProductId = getIntent().getExtras().getInt("ProductId");

        CurrentExternalStorage = new ExternalStorage();


        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonClick();
            }
        },R.string.images);
        //ایجاد طرحبندی صفحه
        CreateLayout();

        NewOrEditProductImage();
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
                NewOrEditProductImage();
                break;
        }
    }

    private void CreateLayout() {
        TextViewPersian MinusOrderTextViewProductImageSetActivity = findViewById(R.id.MinusOrderTextViewProductImageSetActivity);
        TextViewPersian PlusOrderTextViewProductImageSetActivity = findViewById(R.id.PlusOrderTextViewProductImageSetActivity);
        TextViewPersian ProductImageIconTextViewProductImageSetActivity = findViewById(R.id.ProductImageIconTextViewProductImageSetActivity);
        OrderEditTextProductImageSetActivity = findViewById(R.id.OrderEditTextProductImageSetActivity);
        LinearLayout ShowGalleryLinearLayoutProductImageSetActivity = findViewById(R.id.ShowGalleryLinearLayoutProductImageSetActivity);
        ButtonPersianView SaveProductImageButtonProductImageSetActivity = findViewById(R.id.SaveProductImageButtonProductImageSetActivity);
        ShowImageViewProductImageSetActivity = findViewById(R.id.ShowImageViewProductImageSetActivity);


        MinusOrderTextViewProductImageSetActivity.setTypeface(Font.MasterIcon);
        MinusOrderTextViewProductImageSetActivity.setText("\uf068");

        PlusOrderTextViewProductImageSetActivity.setTypeface(Font.MasterIcon);
        PlusOrderTextViewProductImageSetActivity.setText("\uf067");

        ProductImageIconTextViewProductImageSetActivity.setTypeface(Font.MasterIcon);
        ProductImageIconTextViewProductImageSetActivity.setText("\uf1c5");

        MinusOrderTextViewProductImageSetActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int MinusCountOfEmployees;
                if ((OrderEditTextProductImageSetActivity.getText().toString().equals(""))) {
                    MinusCountOfEmployees = 0;
                } else {
                    MinusCountOfEmployees = Integer.parseInt(OrderEditTextProductImageSetActivity.getText().toString().trim());
                }
                if (MinusCountOfEmployees > 0) {
                    Order = MinusCountOfEmployees - 1;
                    OrderEditTextProductImageSetActivity.setText(String.valueOf(Order));
                }
            }
        });

        PlusOrderTextViewProductImageSetActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int PlusCountOfEmployees;
                if ((OrderEditTextProductImageSetActivity.getText().toString().equals(""))) {
                    PlusCountOfEmployees = 0;
                } else {
                    PlusCountOfEmployees = Integer.parseInt(OrderEditTextProductImageSetActivity.getText().toString().trim());
                }
                Order = PlusCountOfEmployees + 1;
                OrderEditTextProductImageSetActivity.setText(String.valueOf(Order));
            }
        });

        ShowGalleryLinearLayoutProductImageSetActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnShowUploadImageClick();
            }
        });

        SaveProductImageButtonProductImageSetActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnSaveProductImageButtonClick(view);
            }
        });
    }

    private void NewOrEditProductImage() {
        if (IsSet.equals("Edit")) {
            ProductImageId = getIntent().getExtras().getInt("ProductImageId");
            ShowLoadingProgressBar();
            ProductImageService productImageService = new ProductImageService(this);
            //دریافت اطلاعات
            RetryType = 2;
            productImageService.Get(ProductImageId);
        }
    }

    /**
     * دکمه ثبت یا ویرایش یک آدرس
     *
     * @param view
     */
    private void OnSaveProductImageButtonClick(View view) {
        if (Utility.ValidateView(OrderEditTextProductImageSetActivity)) {
            if (Path != null && !Path.trim().equals("")) {
                ProductImageService Service = new ProductImageService(this);
                //ثبت اطلاعات
                RetryType = 1;
                if (IsSet.equals("Edit")) {
                    Service.Set(MadeViewModel(), ProductImageId);
                } else {
                    Service.Add(MadeViewModel());
                }
            } else {
                ShowToast(getString(R.string.please_select_your_image), Toast.LENGTH_LONG, MessageType.Warning);
            }
        }
    }

    private ProductImageViewModel MadeViewModel() {

        ProductImageViewModel ViewModel = new ProductImageViewModel();
        try {
            ViewModel.setPath(Path);
            if (OrderEditTextProductImageSetActivity.getText() != null && !OrderEditTextProductImageSetActivity.getText().toString().trim().equals("")) {
                ViewModel.setOrder(Integer.parseInt(OrderEditTextProductImageSetActivity.getText().toString()));
            } else
                ViewModel.setOrder(0);
            ViewModel.setId(ProductImageId);
            ViewModel.setProductId(ProductId);
            ViewModel.setCreate("");
            ViewModel.setModified("");
            ViewModel.setActive(true);


        } catch (Exception Ex) {
            ShowToast(FeedbackType.InvalidDataFormat.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
        return ViewModel;
    }

    private void GoToFileManager() {
        Intent ProductImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        ProductImageIntent.setType("image/*");
        startActivityForResult(ProductImageIntent, 1);
    }


    /**
     * ثبت تنظیمات در صورت اکی بودن وضعیت GPS
     */
    private void OnShowUploadImageClick() {
        if (CurrentExternalStorage.IsUploadImageToUse(this)) {
            GoToFileManager();
        }
    }


    /**
     * زمانی که پنجره دسترسی به Gps می آید و کاربر باید انتخاب کند که اجازه می دهد ا خیر
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (CurrentExternalStorage.IsPermissionEnabled()) {
            GoToFileManager();
        } else {
            ShowToast(getResources().getString(R.string.app_permission_denied), Toast.LENGTH_LONG, MessageType.Warning);
        }
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.ProductImageGet) {
                Feedback<ProductImageViewModel> FeedBack = (Feedback<ProductImageViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    ProductImageViewModel ViewModel = FeedBack.getValue();

                    if (ViewModel != null) {
                        //پر کردن ویو با اطلاعات دریافتی
                        SetProductImageToView(ViewModel);
                        OutputViewModel = ViewModel;
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
            } else if (ServiceMethod == ServiceMethodType.ProductImageSet) {
                Feedback<ProductImageViewModel> FeedBack = (Feedback<ProductImageViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.UpdatedSuccessful.getId()) {
                    ProductImageViewModel ViewModel = FeedBack.getValue();

                    if (ViewModel != null) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                        //پر کردن ویو با اطلاعات دریافتی
                        SetProductImageToView(ViewModel);
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
            } else if (ServiceMethod == ServiceMethodType.ProductImageAdd) {
                Feedback<ProductImageViewModel> FeedBack = (Feedback<ProductImageViewModel>) Data;
                if (FeedBack.getStatus() == FeedbackType.RegisteredSuccessful.getId()) {
                    ProductImageViewModel ViewModel = FeedBack.getValue();
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
            } else if (ServiceMethod == ServiceMethodType.UploadFile) {
                Feedback<List<FileViewModel>> FeedBack = (Feedback<List<FileViewModel>>) Data;
                if (FeedBack.getStatus() == FeedbackType.RegisteredSuccessful.getId()) {
                    List<FileViewModel> ViewModel = FeedBack.getValue();
                    if (ViewModel != null) {
                        if (ViewModel.get(0).getStatus() == 0) {
                            ShowImageViewProductImageSetActivity.setImageURI(UrlOfSelectedImageFromLocal);
                            Path = ViewModel.get(0).getUrl();
                        } else {
                            ShowToast(ViewModel.get(0).getMessage(), Toast.LENGTH_LONG, MessageType.Warning);
                            UrlOfSelectedImageFromLocal = null;
                        }
                    } else {
                        ShowToast(FeedbackType.InvalidDataFormat.getMessage().replace("{0}", ""), Toast.LENGTH_LONG, MessageType.Warning);
                        UrlOfSelectedImageFromLocal = null;
                    }
                } else {
                    UrlOfSelectedImageFromLocal = null;
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

    public void SetProductImageToView(ProductImageViewModel ViewModel) {
        Path = ViewModel.getPath();
        OrderEditTextProductImageSetActivity.setText(String.valueOf(ViewModel.getOrder()));

        int ScreenWidth = LayoutUtility.GetWidthAccordingToScreen(this, 3);
        int ScreenHeight = LayoutUtility.GetHeightAccordingToScreen(this, 3);

        if (!ViewModel.getFullPath().equals(""))
            LayoutUtility.LoadImageWithGlide(this, ViewModel.getFullPath(), ShowImageViewProductImageSetActivity, ScreenWidth, ScreenHeight);
    }

    //the feedBack of Intent for fetching ImageGallery
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case 1: {

                    Uri chosenImageUri = data.getData();
                    UrlOfSelectedImageFromLocal = Uri.parse(chosenImageUri.toString());

                    ///Upload Images-----------------------------------------------------
                    byte[] fileData1 = getFileDataFromDrawable(this, chosenImageUri);

                    if (fileData1.length <= DefaultConstant.ProductImageSize) {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        DataOutputStream dos = new DataOutputStream(bos);
                        try {
                            buildPart(dos, fileData1, "photo.jpg");
                            buildTextPart(dos, "FilePlaceList", "3");
                            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                            multipartBody = bos.toByteArray();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Map<String, String> params = new HashMap<String, String>();
                        AccountRepository ARepository = new AccountRepository(this);
                        AccountViewModel AccountViewModel = ARepository.getAccount();
                        if (AccountViewModel != null) {
                            params.put("Authorization", AccountViewModel.getToken());
                        } else {
                            params.put("Authorization", "");
                        }
                        RetryType = 1;
                        ShowLoadingProgressBar();
                        ImageService UploadImageService = new ImageService(this);
                        UploadImageService.Add(params, mimeType, multipartBody);

                    } else {
                        ShowToast(getString(R.string.your_image_more_than_100_kb), Toast.LENGTH_LONG, MessageType.Warning);
                    }

                }
                break;
            }
        }
    }

    private void buildPart(DataOutputStream dataOutputStream, byte[] fileData, String fileName) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"Files\"; filename=\""
                + fileName + "\"" + lineEnd);


        dataOutputStream.writeBytes(lineEnd);

        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(fileData);
        int bytesAvailable = fileInputStream.available();

        int maxBufferSize = 1024 * 1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        // read file and write it into form...
        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        dataOutputStream.writeBytes(lineEnd);
    }

    private void buildTextPart(DataOutputStream dataOutputStream, String parameterName, String parameterValue) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + parameterName + "\"" + lineEnd);
        //dataOutputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
        dataOutputStream.writeBytes(lineEnd);
        dataOutputStream.writeBytes(parameterValue + lineEnd);
    }

    private byte[] getFileDataFromDrawable(Context context, Uri uri) {
        Bitmap bitmap = null;
        try {
            if(bitmap!=null)
            {
                bitmap.recycle();
                bitmap=null;
            }



            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        } else {
            Toast.makeText(context, "not", Toast.LENGTH_SHORT).show();
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * دریافت ویومدل از اکتیویتی تصاویر جهت ارسال به اکتیویتی محصولات
     */
    private void SendDataToParentActivity(boolean IsAdd) {
        HashMap<String, Object> Output = new HashMap<>();
        try {
            Output.put("IsAdd", IsAdd);
            Output.put("ProductImageViewModel", OutputViewModel);
        } catch (Exception Ex) {
            Output.put("ProductImageViewModel", null);
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