package ir.rayas.app.citywareclient.View.Share;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.UserSearchRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.User.UserService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Layout.View.EditTextPersian;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Search.OutUserSearchViewModel;

public class UserSearchActivity extends BaseActivity implements IResponseService {

    private UserSearchRecyclerViewAdapter userSearchRecyclerViewAdapter = null;
    private SwipeRefreshLayout RefreshUserSwipeRefreshLayoutUserSearchActivity = null;
    private EditTextPersian SearchUserEditTextUserSearchActivity = null;
    private RecyclerView ShowUserListRecyclerViewUserSearchActivity = null;
    private TextViewPersian ShowEmptyUserTextViewUserSearchActivity = null;

    private String TextSearch = "";
    private int PageNumber = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);
        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.USER_SEARCH_ACTIVITY);

        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                LoadData();
            }
        }, R.string.user_selection);

        CreateLayout();
    }

    private void CreateLayout() {

        final ImageView SearchUserImageViewUserSearchActivity = findViewById(R.id.SearchUserImageViewUserSearchActivity);
        SearchUserEditTextUserSearchActivity = findViewById(R.id.SearchUserEditTextUserSearchActivity);
        RefreshUserSwipeRefreshLayoutUserSearchActivity = findViewById(R.id.RefreshUserSwipeRefreshLayoutUserSearchActivity);
        ShowUserListRecyclerViewUserSearchActivity = findViewById(R.id.ShowUserListRecyclerViewUserSearchActivity);
        ShowEmptyUserTextViewUserSearchActivity = findViewById(R.id.ShowEmptyUserTextViewUserSearchActivity);

        ShowEmptyUserTextViewUserSearchActivity.setVisibility(View.GONE);

        ShowUserListRecyclerViewUserSearchActivity.setLayoutManager(new LinearLayoutManager(UserSearchActivity.this));
        userSearchRecyclerViewAdapter = new UserSearchRecyclerViewAdapter(UserSearchActivity.this, null, ShowUserListRecyclerViewUserSearchActivity);
        ShowUserListRecyclerViewUserSearchActivity.setAdapter(userSearchRecyclerViewAdapter);

        RefreshUserSwipeRefreshLayoutUserSearchActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PageNumber = 1;
                LoadData();
            }
        });

        SearchUserEditTextUserSearchActivity.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    ShowUserListRecyclerViewUserSearchActivity.setVisibility(View.VISIBLE);

                    userSearchRecyclerViewAdapter.ClearViewModelList();

                    TextSearch = s.toString();

                    try {
                        String Temp = URLEncoder.encode(TextSearch, "utf-8");
                        TextSearch = Temp;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    PageNumber = 1;
                    LoadData();

                }  else {
                    ShowUserListRecyclerViewUserSearchActivity.setVisibility(View.GONE);
                }
            }
        });


        SearchUserImageViewUserSearchActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String SearchOffer = SearchUserEditTextUserSearchActivity.getText().toString();

                if (!SearchOffer.equals("")) {
                    ShowUserListRecyclerViewUserSearchActivity.setVisibility(View.VISIBLE);

                    userSearchRecyclerViewAdapter.ClearViewModelList();
                    TextSearch = SearchOffer;

                    try {
                        String Temp = URLEncoder.encode(TextSearch, "utf-8");
                        TextSearch = Temp;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    PageNumber = 1;
                    LoadData();
                }  else {
                    ShowUserListRecyclerViewUserSearchActivity.setVisibility(View.GONE);
                }

             HideKeyboard(SearchUserImageViewUserSearchActivity);
            }
        });

    }

    public void LoadData() {
        RefreshUserSwipeRefreshLayoutUserSearchActivity.setRefreshing(true);

        UserService UserService = new UserService(this);
        UserService.GetSearch(PageNumber, TextSearch);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        RefreshUserSwipeRefreshLayoutUserSearchActivity.setRefreshing(false);
        try {
            if (ServiceMethod == ServiceMethodType.SearchGet) {

                Feedback<List<OutUserSearchViewModel>> FeedBack = (Feedback<List<OutUserSearchViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    final List<OutUserSearchViewModel> ViewModel = FeedBack.getValue();

                        if (ViewModel != null) {
                            if (PageNumber == 1) {
                                if (ViewModel.size() > 0) {
                                    ShowEmptyUserTextViewUserSearchActivity.setVisibility(View.GONE);
                                    ShowUserListRecyclerViewUserSearchActivity.setVisibility(View.VISIBLE);
                                    userSearchRecyclerViewAdapter.SetViewModelList(ViewModel);

                                    if (DefaultConstant.PageNumberSize == ViewModel.size()) {
                                        PageNumber = PageNumber + 1;
                                        LoadData();
                                    }
                                } else {
                                    ShowEmptyUserTextViewUserSearchActivity.setVisibility(View.VISIBLE);
                                    ShowUserListRecyclerViewUserSearchActivity.setVisibility(View.GONE);
                                }

                            } else {
                                ShowEmptyUserTextViewUserSearchActivity.setVisibility(View.GONE);
                                ShowUserListRecyclerViewUserSearchActivity.setVisibility(View.VISIBLE);
                                userSearchRecyclerViewAdapter.AddViewModelList(ViewModel);

                                if (DefaultConstant.PageNumberSize == ViewModel.size()) {
                                    PageNumber = PageNumber + 1;
                                    LoadData();
                                }
                            }
                        }

                }  else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    if (PageNumber > 1) {
                        ShowEmptyUserTextViewUserSearchActivity.setVisibility(View.GONE);
                        ShowUserListRecyclerViewUserSearchActivity.setVisibility(View.VISIBLE);
                    } else {
                        ShowEmptyUserTextViewUserSearchActivity.setVisibility(View.VISIBLE);
                        ShowUserListRecyclerViewUserSearchActivity.setVisibility(View.GONE);
                    }
                }else {
                    if (FeedBack.getStatus() == FeedbackType.ThereIsNoInternet.getId()) {
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

    private void   HideKeyboard(View view){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
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
