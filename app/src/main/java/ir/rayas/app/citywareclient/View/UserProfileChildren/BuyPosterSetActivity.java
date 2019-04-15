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
import ir.rayas.app.citywareclient.Service.Poster.PosterService;
import ir.rayas.app.citywareclient.Service.Uploader.ImageService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Helper.ExternalStorage;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.EditTextPersian;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Poster.EditPurchasedPosterViewModel;
import ir.rayas.app.citywareclient.ViewModel.Poster.PurchasedPosterViewModel;
import ir.rayas.app.citywareclient.ViewModel.Uploader.FileViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;


public class BuyPosterSetActivity extends BaseActivity implements IResponseService {

    private EditTextPersian TitlePosterEditTextBuyPosterSetActivity = null;
    private EditTextPersian DescriptionAbstractEditTextBuyPosterSetActivity = null;
    private EditTextPersian DescriptionEditTextBuyPosterSetActivity = null;
    private ImageView ShowImageViewBuyPosterSetActivity = null;
    private EditTextPersian OrderEditTextBuyPosterSetActivity = null;

    private int RetryType = 0;
    private int PosterId = 0;
    private String PosterImage = "";
    private int Order = 0;
    private Uri UrlOfSelectedImageFromLocal = null;

    //Upload
    private final String twoHyphens = "--";
    private final String lineEnd = "\r\n";
    private final String boundary = "apiclient-" + System.currentTimeMillis();
    private final String mimeType = "multipart/form-data;boundary=" + boundary;
    private byte[] multipartBody;

    private ExternalStorage CurrentExternalStorage = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_buy_poster);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.BUY_POSTER_SET_ACTIVITY);

        PosterId = getIntent().getExtras().getInt("PosterId");

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonClick();
            }
        }, R.string.poster_profile);
        CurrentExternalStorage = new ExternalStorage();
        //ایجاد طرحبندی صفحه
        CreateLayout();

        LoadData();
    }


    private void CreateLayout() {
        TextViewPersian MinusOrderTextViewBuyPosterSetActivity = findViewById(R.id.MinusOrderTextViewBuyPosterSetActivity);
        TextViewPersian PlusOrderTextViewBuyPosterSetActivity = findViewById(R.id.PlusOrderTextViewBuyPosterSetActivity);
        TextViewPersian ProductImageIconTextViewBuyPosterSetActivity = findViewById(R.id.ProductImageIconTextViewBuyPosterSetActivity);
        LinearLayout ShowGalleryLinearLayoutBuyPosterSetActivity = findViewById(R.id.ShowGalleryLinearLayoutBuyPosterSetActivity);
        ButtonPersianView SaveButtonBuyPosterSetActivity = findViewById(R.id.SaveButtonBuyPosterSetActivity);
        TitlePosterEditTextBuyPosterSetActivity = findViewById(R.id.TitlePosterEditTextBuyPosterSetActivity);
        DescriptionAbstractEditTextBuyPosterSetActivity = findViewById(R.id.DescriptionAbstractEditTextBuyPosterSetActivity);
        DescriptionEditTextBuyPosterSetActivity = findViewById(R.id.DescriptionEditTextBuyPosterSetActivity);
        ShowImageViewBuyPosterSetActivity = findViewById(R.id.ShowImageViewBuyPosterSetActivity);
        OrderEditTextBuyPosterSetActivity = findViewById(R.id.OrderEditTextBuyPosterSetActivity);



        ProductImageIconTextViewBuyPosterSetActivity.setTypeface(Font.MasterIcon);
        ProductImageIconTextViewBuyPosterSetActivity.setText("\uf1c5");


        MinusOrderTextViewBuyPosterSetActivity.setTypeface(Font.MasterIcon);
        MinusOrderTextViewBuyPosterSetActivity.setText("\uf068");

        PlusOrderTextViewBuyPosterSetActivity.setTypeface(Font.MasterIcon);
        PlusOrderTextViewBuyPosterSetActivity.setText("\uf067");

        MinusOrderTextViewBuyPosterSetActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int MinusCountOfEmployees;
                if ((OrderEditTextBuyPosterSetActivity.getText().toString().equals(""))) {
                    MinusCountOfEmployees = 0;
                } else {
                    MinusCountOfEmployees = Integer.parseInt(OrderEditTextBuyPosterSetActivity.getText().toString().trim());
                }
                if (MinusCountOfEmployees > 0) {
                    Order = MinusCountOfEmployees - 1;
                    OrderEditTextBuyPosterSetActivity.setText(String.valueOf(Order));
                }
            }
        });

        PlusOrderTextViewBuyPosterSetActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int PlusCountOfEmployees;
                if ((OrderEditTextBuyPosterSetActivity.getText().toString().equals(""))) {
                    PlusCountOfEmployees = 0;
                } else {
                    PlusCountOfEmployees = Integer.parseInt(OrderEditTextBuyPosterSetActivity.getText().toString().trim());
                }
                Order = PlusCountOfEmployees + 1;
                OrderEditTextBuyPosterSetActivity.setText(String.valueOf(Order));
            }
        });


        ShowGalleryLinearLayoutBuyPosterSetActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnShowUploadImageClick();
            }
        });

        SaveButtonBuyPosterSetActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnSaveProductImageButtonClick();
            }
        });

    }

    /**
     * دریافت اطلاعات نحوای جهت پر کردن Recycle
     */
    private void LoadData() {
        ShowLoadingProgressBar();

        PosterService PosterService = new PosterService(this);
        PosterService.Get(PosterId);
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
                LoadData();
                break;
        }
    }

    /**
     * دکمه ثبت یا ویرایش یک آدرس
     *
     */
    private void OnSaveProductImageButtonClick() {
        if (Utility.ValidateView(TitlePosterEditTextBuyPosterSetActivity)) {

            PosterService PosterService = new PosterService(this);
            //ثبت اطلاعات
            RetryType = 1;
            PosterService.EditPurchasedPoster(MadeViewModel());
        } else {
            ShowToast(getString(R.string.please_enter_title), Toast.LENGTH_LONG, MessageType.Warning);
        }
    }

    private EditPurchasedPosterViewModel MadeViewModel() {

        EditPurchasedPosterViewModel ViewModel = new EditPurchasedPosterViewModel();
        try {
            ViewModel.setTitle(TitlePosterEditTextBuyPosterSetActivity.getText().toString());
            ViewModel.setId(PosterId);
            ViewModel.setDescription(DescriptionEditTextBuyPosterSetActivity.getText().toString());
            ViewModel.setAbstractOfDescription(DescriptionAbstractEditTextBuyPosterSetActivity.getText().toString());
            ViewModel.setImagePathUrl(PosterImage);
            ViewModel.setOrder(Order);


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
            if (ServiceMethod == ServiceMethodType.UserPosterGet) {
                Feedback<PurchasedPosterViewModel> FeedBack = (Feedback<PurchasedPosterViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    PurchasedPosterViewModel ViewModelList = FeedBack.getValue();
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
            } else if (ServiceMethod == ServiceMethodType.PurchasedPosterEdit) {
                Feedback<PurchasedPosterViewModel> FeedBack = (Feedback<PurchasedPosterViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.UpdatedSuccessful.getId()) {
                    PurchasedPosterViewModel ViewModel = FeedBack.getValue();

                    if (ViewModel != null) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
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
                            ShowImageViewBuyPosterSetActivity.setImageURI(UrlOfSelectedImageFromLocal);
                            PosterImage = ViewModel.get(0).getUrl();
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


    private void SetInformationToView(final PurchasedPosterViewModel ViewModel) {
        TitlePosterEditTextBuyPosterSetActivity.setText(ViewModel.getTitle());
        DescriptionAbstractEditTextBuyPosterSetActivity.setText(ViewModel.getAbstractOfDescription());
        DescriptionEditTextBuyPosterSetActivity.setText(ViewModel.getDescription());
        OrderEditTextBuyPosterSetActivity.setText(String.valueOf(ViewModel.getOrder()));
        Order = ViewModel.getOrder();

        if (!ViewModel.getImagePathUrl().equals("")) {
            if (ViewModel.getImagePathUrl().contains("~")) {
                PosterImage = ViewModel.getImagePathUrl().replace("~", DefaultConstant.BaseUrlWebService);
            } else {
                PosterImage = ViewModel.getImagePathUrl();
            }
            LayoutUtility.LoadImageWithGlide(this, PosterImage, ShowImageViewBuyPosterSetActivity);
        } else {
            ShowImageViewBuyPosterSetActivity.setImageResource(R.drawable.image_default);
        }



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
                            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                            multipartBody = bos.toByteArray();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Map<String, String> params = new HashMap<>();
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
