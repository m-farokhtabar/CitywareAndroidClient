package ir.rayas.app.citywareclient.View.MasterChildren;

import android.app.Dialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.BusinessCommentRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.Service.Business.CommentService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.EditTextPersian;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Business.CommentInViewModel;
import ir.rayas.app.citywareclient.ViewModel.Business.CommentViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;


public class ShowCommentBusinessActivity extends BaseActivity implements IResponseService {

    private SwipeRefreshLayout RefreshCommentSwipeRefreshLayoutShowCommentBusinessActivity;
    private BusinessCommentRecyclerViewAdapter businessCommentRecyclerViewAdapter = null;
    private EditTextPersian MessageCommentEditTextDialogBusinessComment = null;
    private TextViewPersian ShowEmptyCommentListTextViewShowCommentBusinessActivity = null;
    private LinearLayout LoadingLinearLayout = null;

    private Dialog ShowBusinessCommentDialog;

    private int PageNumber = 1;
    private int BusinessId = 1;
    private boolean IsSwipe = false;

    private String BusinessName;
    private Float TotalScore;

    private AccountRepository AccountRepository = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_business);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.SHOW_COMMENT_BUSINESS_ACTIVITY);

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.comment_user);


        BusinessName = getIntent().getExtras().getString("BusinessName");
        TotalScore = getIntent().getExtras().getFloat("TotalScore");
        BusinessId = getIntent().getExtras().getInt("BusinessId");

        AccountRepository = new AccountRepository(this);

        //ایجاد طرحبندی صفحه
        CreateLayout();
        //دریافت کسب و کارهای بوک مارک شده
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
        RefreshCommentSwipeRefreshLayoutShowCommentBusinessActivity = findViewById(R.id.RefreshCommentSwipeRefreshLayoutShowCommentBusinessActivity);
        TextViewPersian BusinessNameTextViewShowCommentBusinessActivity = findViewById(R.id.BusinessNameTextViewShowCommentBusinessActivity);
        ShowEmptyCommentListTextViewShowCommentBusinessActivity = findViewById(R.id.ShowEmptyCommentListTextViewShowCommentBusinessActivity);
        RatingBar RatingBusinessRatingBarShowCommentBusinessActivity = findViewById(R.id.RatingBusinessRatingBarShowCommentBusinessActivity);
        FloatingActionButton NewCommentFloatingActionButtonShowCommentBusinessActivity = findViewById(R.id.NewCommentFloatingActionButtonShowCommentBusinessActivity);

        BusinessNameTextViewShowCommentBusinessActivity.setText(BusinessName);
        RatingBusinessRatingBarShowCommentBusinessActivity.setRating(TotalScore);
        ShowEmptyCommentListTextViewShowCommentBusinessActivity.setVisibility(View.GONE);

        RecyclerView businessCommentRecyclerViewShowCommentBusinessActivity = findViewById(R.id.BusinessCommentRecyclerViewShowCommentBusinessActivity);
        businessCommentRecyclerViewShowCommentBusinessActivity.setLayoutManager(new LinearLayoutManager(this));
        businessCommentRecyclerViewAdapter = new BusinessCommentRecyclerViewAdapter(this, null, businessCommentRecyclerViewShowCommentBusinessActivity);
        businessCommentRecyclerViewShowCommentBusinessActivity.setAdapter(businessCommentRecyclerViewAdapter);

        RefreshCommentSwipeRefreshLayoutShowCommentBusinessActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                IsSwipe = true;
                PageNumber = 1;
                LoadData();
            }
        });

        NewCommentFloatingActionButtonShowCommentBusinessActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialogNewComment();
            }
        });
    }

    /**
     * دریافت اطلاعات نحوای جهت پر کردن Recycle
     */
    private void LoadData() {
        if (!IsSwipe)
            if (PageNumber == 1)
                ShowLoadingProgressBar();

        CommentService CommentService = new CommentService(this);
        CommentService.GetAll(BusinessId, PageNumber);

    }

    private CommentInViewModel MadeViewModel() {
        AccountViewModel AccountModel = AccountRepository.getAccount();
        CommentInViewModel ViewModel = new CommentInViewModel();
        try {
            ViewModel.setCommentText(MessageCommentEditTextDialogBusinessComment.getText().toString());
            ViewModel.setBusinessId(BusinessId);
            ViewModel.setUserId(AccountModel.getUser().getId());

        } catch (Exception ignored) {
        }
        return ViewModel;
    }

    private void ShowDialogNewComment() {

        ShowBusinessCommentDialog = new Dialog(ShowCommentBusinessActivity.this);
        ShowBusinessCommentDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ShowBusinessCommentDialog.setContentView(R.layout.dialog_new_comment);

        MessageCommentEditTextDialogBusinessComment = ShowBusinessCommentDialog.findViewById(R.id.MessageCommentEditTextDialogBusinessComment);
        final TextViewPersian ShowCharacterCommentTextViewDialogBusinessComment = ShowBusinessCommentDialog.findViewById(R.id.ShowCharacterCommentTextViewDialogBusinessComment);
        TextViewPersian HeaderTextViewDialogBusinessComment = ShowBusinessCommentDialog.findViewById(R.id.HeaderTextViewDialogBusinessComment);
        HeaderTextViewDialogBusinessComment.getLayoutParams().width = LayoutUtility.GetWidthAccordingToScreen(ShowCommentBusinessActivity.this, 1);
        ButtonPersianView SaveCommentButtonDialogBusinessComment = ShowBusinessCommentDialog.findViewById(R.id.SaveCommentButtonDialogBusinessComment);
        ButtonPersianView CancelCommentButtonDialogBusinessComment = ShowBusinessCommentDialog.findViewById(R.id.CancelCommentButtonDialogBusinessComment);
        LoadingLinearLayout = ShowBusinessCommentDialog.findViewById(R.id.LoadingLinearLayout);


        LoadingLinearLayout.setVisibility(View.GONE);

        MessageCommentEditTextDialogBusinessComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

                int CommentCountCharacter = s.length();
                ShowCharacterCommentTextViewDialogBusinessComment.setText(String.valueOf(CommentCountCharacter) + " / 250");

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });


        CancelCommentButtonDialogBusinessComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowBusinessCommentDialog.dismiss();
            }
        });

        SaveCommentButtonDialogBusinessComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentService CommentService = new CommentService(ShowCommentBusinessActivity.this);

                if (Utility.ValidateView(MessageCommentEditTextDialogBusinessComment)) {
                    LoadingLinearLayout.setVisibility(View.VISIBLE);
                    CommentService.Add(MadeViewModel());
                }

            }
        });

        ShowBusinessCommentDialog.show();
    }

    /**
     * @param Data
     * @param ServiceMethod
     * @param <T>
     */
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        HideLoading();
        RefreshCommentSwipeRefreshLayoutShowCommentBusinessActivity.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.BusinessCommentGetAll) {
                Feedback<List<CommentViewModel>> FeedBack = (Feedback<List<CommentViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<CommentViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        if (PageNumber == 1) {
                            if (ViewModelList.size() < 1) {
                                ShowEmptyCommentListTextViewShowCommentBusinessActivity.setVisibility(View.VISIBLE);
                            } else {
                                ShowEmptyCommentListTextViewShowCommentBusinessActivity.setVisibility(View.GONE);
                                businessCommentRecyclerViewAdapter.SetViewModelList(ViewModelList);

                                if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                    PageNumber = PageNumber + 1;
                                    LoadData();
                                }
                            }

                        } else {
                            ShowEmptyCommentListTextViewShowCommentBusinessActivity.setVisibility(View.GONE);
                            businessCommentRecyclerViewAdapter.AddViewModelList(ViewModelList);

                            if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                PageNumber = PageNumber + 1;
                                LoadData();
                            }
                        }
                    }
                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    if (PageNumber > 1) {
                        ShowEmptyCommentListTextViewShowCommentBusinessActivity.setVisibility(View.GONE);
                    } else {
                        ShowEmptyCommentListTextViewShowCommentBusinessActivity.setVisibility(View.VISIBLE);
                    }
                } else {
                    ShowEmptyCommentListTextViewShowCommentBusinessActivity.setVisibility(View.GONE);
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.BusinessCommentAdd) {
                LoadingLinearLayout.setVisibility(View.GONE);
                Feedback<CommentViewModel> FeedBack = (Feedback<CommentViewModel>) Data;
                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    CommentViewModel ViewModel = FeedBack.getValue();
                    ShowBusinessCommentDialog.dismiss();
                    if (ViewModel != null) {
                        ShowToast(getResources().getString(R.string.save_comment_and), Toast.LENGTH_LONG, MessageType.Warning);

                    } else {
                        ShowToast(FeedbackType.InvalidDataFormat.getMessage().replace("{0}", ""), Toast.LENGTH_LONG, MessageType.Warning);
                    }
                } else if (FeedBack.getStatus() == FeedbackType.ThereIsUnActivatedComment.getId()) {
                    CommentViewModel ViewModel = FeedBack.getValue();
                    ShowBusinessCommentDialog.dismiss();
                    if (ViewModel != null) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                        MessageCommentEditTextDialogBusinessComment.setText(ViewModel.getText());
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
