package ir.rayas.app.citywareclient.View.Share;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.ListView.OfferSearchListViewAdapter;
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
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResultPassing;
import ir.rayas.app.citywareclient.Share.Layout.View.EditTextPersian;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.ViewModel.Search.OutUserSearchViewModel;

public class UserSearchActivity extends BaseActivity implements IResponseService {

    private ListView ListSearchListViewUserSearchActivity = null;
    private UserSearchRecyclerViewAdapter userSearchRecyclerViewAdapter = null;
    private EditTextPersian SearchUserEditTextUserSearchActivity = null;

    private String TextSearch = "";
    private boolean IsOffer = false;
    private int PageNumber = 1;
    public int UserId = 0;
    public String UserName = "";

    public void setUserId(int userId) {
        UserId = userId;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

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

        ImageView SearchUserImageViewUserSearchActivity = findViewById(R.id.SearchUserImageViewUserSearchActivity);
        SearchUserEditTextUserSearchActivity = findViewById(R.id.SearchUserEditTextUserSearchActivity);
        ListSearchListViewUserSearchActivity = findViewById(R.id.ListSearchListViewUserSearchActivity);
        RecyclerView ShowUserListRecyclerViewUserSearchActivity = findViewById(R.id.ShowUserListRecyclerViewUserSearchActivity);
        FloatingActionButton GetUserIdFloatingActionButtonUserSearchActivity = findViewById(R.id.GetUserIdFloatingActionButtonUserSearchActivity);

        ShowUserListRecyclerViewUserSearchActivity.setLayoutManager(new LinearLayoutManager(UserSearchActivity.this));
        userSearchRecyclerViewAdapter = new UserSearchRecyclerViewAdapter(UserSearchActivity.this, null, ShowUserListRecyclerViewUserSearchActivity);
        ShowUserListRecyclerViewUserSearchActivity.setAdapter(userSearchRecyclerViewAdapter);

        SearchUserEditTextUserSearchActivity.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                IsOffer = false;
                if (s.length() != 0) {

                    TextSearch = s.toString();
                    PageNumber = 1;
                    LoadData();

                } else {
                    ListSearchListViewUserSearchActivity.setVisibility(View.GONE);
                }
            }
        });


        SearchUserImageViewUserSearchActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String SearchOffer = SearchUserEditTextUserSearchActivity.getText().toString();

                if (!SearchOffer.equals("")) {
                    IsOffer = true;
                    TextSearch = SearchOffer;
                    PageNumber = 1;
                    LoadData();
                }
            }
        });

        GetUserIdFloatingActionButtonUserSearchActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnGetUserIdFloatingActionButtonClick();
            }
        });
    }

    public void LoadData() {

        UserService UserService = new UserService(this);
        UserService.GetSearch(PageNumber, TextSearch);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {

        HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.SearchGet) {

                Feedback<List<OutUserSearchViewModel>> FeedBack = (Feedback<List<OutUserSearchViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    final List<OutUserSearchViewModel> ViewModel = FeedBack.getValue();

                    if (IsOffer) {

                        ListSearchListViewUserSearchActivity.setVisibility(View.GONE);
                        if (ViewModel != null) {
                            if (PageNumber == 1) {
                                if (ViewModel.size() > 0) {
                                    userSearchRecyclerViewAdapter.SetViewModelList(ViewModel);

                                    if (DefaultConstant.PageNumberSize == ViewModel.size()) {
                                        PageNumber = PageNumber + 1;
                                        LoadData();
                                    }
                                }

                            } else {
                                userSearchRecyclerViewAdapter.AddViewModelList(ViewModel);

                                if (DefaultConstant.PageNumberSize == ViewModel.size()) {
                                    PageNumber = PageNumber + 1;
                                    LoadData();
                                }
                            }
                        }
                    } else {
                        if (ViewModel != null) {

                            ListSearchListViewUserSearchActivity.setVisibility(View.VISIBLE);

                            OfferSearchListViewAdapter offerSearchListViewAdapter = new OfferSearchListViewAdapter(UserSearchActivity.this, ViewModel);
                            ListSearchListViewUserSearchActivity.setAdapter(offerSearchListViewAdapter);

                            ListSearchListViewUserSearchActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    ListSearchListViewUserSearchActivity.setVisibility(View.GONE);
                                    SearchUserEditTextUserSearchActivity.setText("");
                                    IsOffer = true;

                                    TextViewPersian TextOfferSearch = view.findViewById(R.id.UserNameSearchTextView);
                                    TextSearch = TextOfferSearch.getText().toString();

                                    try {
                                        TextSearch = TextSearch.replaceAll("\r\n", "");
                                        LoadData();
                                    } catch (Exception ignored) {
                                    }
                                }
                            });


                        } else {
                            ListSearchListViewUserSearchActivity.setVisibility(View.GONE);
                        }
                    }
                } else {
                    ListSearchListViewUserSearchActivity.setVisibility(View.GONE);
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


    private void OnGetUserIdFloatingActionButtonClick() {
        if (UserId > 0) {
            if (getIntent().getIntExtra("FromActivityId", -1) > -1) {
                HashMap<String, Object> Output = new HashMap<>();
                Output.put("UserName", UserName);
                Output.put("UserId", UserId);
                ActivityResultPassing.Push(new ActivityResult(getIntent().getIntExtra("FromActivityId", -1), getCurrentActivityId(), Output));
            }
            FinishCurrentActivity();
        } else {
            ShowToast(getResources().getString(R.string.please_enter_user_selection), Toast.LENGTH_LONG, MessageType.Warning);
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
