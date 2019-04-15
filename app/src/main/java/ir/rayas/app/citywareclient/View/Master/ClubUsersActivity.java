package ir.rayas.app.citywareclient.View.Master;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.PrizeAllClubRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Prize.PrizeService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.User.PointService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityIdList;
import ir.rayas.app.citywareclient.Share.Helper.ActivityMessagePassing.ActivityResult;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Base.BaseActivity;
import ir.rayas.app.citywareclient.View.Fragment.UserProfile.UserAddressFragment;
import ir.rayas.app.citywareclient.View.Fragment.UserProfile.UserBusinessFragment;
import ir.rayas.app.citywareclient.View.IRetryButtonOnClick;
import ir.rayas.app.citywareclient.View.MasterChildren.ActionPointAllActivity;
import ir.rayas.app.citywareclient.View.MasterChildren.PrizeAllActivity;
import ir.rayas.app.citywareclient.View.MasterChildren.UserActionPointActivity;
import ir.rayas.app.citywareclient.View.MasterChildren.UserPrizeActivity;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessViewModel;
import ir.rayas.app.citywareclient.ViewModel.Club.PrizeViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.UserAddressViewModel;

public class ClubUsersActivity extends BaseActivity implements IResponseService {

    private TextViewPersian MyPointTextViewClubUsersActivity = null;
    private TextViewPersian NextTextViewClubUsersActivity = null;
    private TextViewPersian PreviousTextViewClubUsersActivity = null;

    private RecyclerView GiftRecyclerViewClubUsersActivity = null;

    private Double MyPoint = 0.0;
    private int CurrentPage = 0;
    private int TotalPages = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_users);

        //تنظیم کد اکتیویتی جاری جهت شناسایی برای استفاده در کلاس پایه و یا دریافت و ارسال اطلاعات مابین اکتیویتی ها
        setCurrentActivityId(ActivityIdList.CLUB_USERS_ACTIVITY);


        //آماده سازی قسمت لودینگ و پنجره خطا در برنامه
        InitView(R.id.MasterContentLinearLayout, new IRetryButtonOnClick() {
            @Override
            public void call() {
                RetryButtonOnClick();
            }
        }, R.string.my_club);

        //ایجاد طرحبندی صفحه
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
     * دریافت اطلاعات نحوای جهت پر کردن Recycle
     */
    private void LoadData() {

        ShowLoadingProgressBar();
        PointService pointService = new PointService(this);
        pointService.Get();

    }

    /**
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    private void CreateLayout() {

        MyPointTextViewClubUsersActivity = findViewById(R.id.MyPointTextViewClubUsersActivity);
        NextTextViewClubUsersActivity = findViewById(R.id.NextTextViewClubUsersActivity);
        PreviousTextViewClubUsersActivity = findViewById(R.id.PreviousTextViewClubUsersActivity);
        TextViewPersian ReportScoresIconTextViewClubUsersActivity = findViewById(R.id.ReportScoresIconTextViewClubUsersActivity);
        TextViewPersian ScoringIconTextViewClubUsersActivity = findViewById(R.id.ScoringIconTextViewClubUsersActivity);
        TextViewPersian GiftIconTextViewClubUsersActivity = findViewById(R.id.GiftIconTextViewClubUsersActivity);
        TextViewPersian MyGiftIconTextViewClubUsersActivity = findViewById(R.id.MyGiftIconTextViewClubUsersActivity);
        TextViewPersian GiftsIconTextViewClubUsersActivity = findViewById(R.id.GiftsIconTextViewClubUsersActivity);
        CardView ReportScoresCardViewClubUsersActivity = findViewById(R.id.ReportScoresCardViewClubUsersActivity);
        CardView ScoringCardViewClubUsersActivity = findViewById(R.id.ScoringCardViewClubUsersActivity);
        CardView MyGiftCardViewClubUsersActivity = findViewById(R.id.MyGiftCardViewClubUsersActivity);
        CardView ShowAllGiftCardViewClubUsersActivity = findViewById(R.id.ShowAllGiftCardViewClubUsersActivity);

        PreviousTextViewClubUsersActivity.setClickable(false);
        NextTextViewClubUsersActivity.setClickable(true);
        PreviousTextViewClubUsersActivity.setTextColor(getResources().getColor(R.color.FontSemiBlackColor));
        NextTextViewClubUsersActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));

        GiftRecyclerViewClubUsersActivity = findViewById(R.id.GiftRecyclerViewClubUsersActivity);
        GiftRecyclerViewClubUsersActivity.setLayoutManager(new LinearLayoutManager(this));

        ReportScoresIconTextViewClubUsersActivity.setTypeface(Font.MasterIcon);
        ReportScoresIconTextViewClubUsersActivity.setText("\uf15c");

        ScoringIconTextViewClubUsersActivity.setTypeface(Font.MasterIcon);
        ScoringIconTextViewClubUsersActivity.setText("\uf005");

        GiftIconTextViewClubUsersActivity.setTypeface(Font.MasterIcon);
        GiftIconTextViewClubUsersActivity.setText("\uf06b");

        MyGiftIconTextViewClubUsersActivity.setTypeface(Font.MasterIcon);
        MyGiftIconTextViewClubUsersActivity.setText("\uf06b");

        NextTextViewClubUsersActivity.setTypeface(Font.MasterIcon);
        NextTextViewClubUsersActivity.setText("\uf137");

        PreviousTextViewClubUsersActivity.setTypeface(Font.MasterIcon);
        PreviousTextViewClubUsersActivity.setText("\uf138");

        GiftsIconTextViewClubUsersActivity.setTypeface(Font.MasterIcon);
        GiftsIconTextViewClubUsersActivity.setText("\uf06b");

        ReportScoresCardViewClubUsersActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent UserActionIntent = NewIntent(UserActionPointActivity.class);
                UserActionIntent.putExtra("MyPoint", MyPoint);
                startActivity(UserActionIntent);
            }
        });

        ScoringCardViewClubUsersActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ActionAllIntent = NewIntent(ActionPointAllActivity.class);
                startActivity(ActionAllIntent);
            }
        });

        MyGiftCardViewClubUsersActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent UserPrizeIntent = NewIntent(UserPrizeActivity.class);
                UserPrizeIntent.putExtra("MyPoint", MyPoint);
                startActivity(UserPrizeIntent);

            }
        });

        ShowAllGiftCardViewClubUsersActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent PrizeAllIntent = NewIntent(PrizeAllActivity.class);
                PrizeAllIntent.putExtra("MyPoint", MyPoint);
                startActivity(PrizeAllIntent);
            }
        });


    }

    /**
     * @param Data
     * @param ServiceMethod
     * @param <T>
     */
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {

        try {
            if (ServiceMethod == ServiceMethodType.PrizeGetAll) {
                HideLoading();
                Feedback<List<PrizeViewModel>> FeedBack = (Feedback<List<PrizeViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<PrizeViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {

                        PrizeAllClubRecyclerViewAdapter prizeAllRecyclerViewAdapter = new PrizeAllClubRecyclerViewAdapter(ClubUsersActivity.this, GeneratePage(CurrentPage, ViewModelList), MyPoint);
                        GiftRecyclerViewClubUsersActivity.setAdapter(prizeAllRecyclerViewAdapter);

                        if (ViewModelList.size() > 10)
                            TotalPages = 10;
                        else
                            TotalPages = ViewModelList.size()-1;


                        NextTextViewClubUsersActivity.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CurrentPage += 1;
                                PrizeAllClubRecyclerViewAdapter prizeAllRecyclerViewAdapter = new PrizeAllClubRecyclerViewAdapter(ClubUsersActivity.this, GeneratePage(CurrentPage, ViewModelList), MyPoint);
                                GiftRecyclerViewClubUsersActivity.setAdapter(prizeAllRecyclerViewAdapter);
                                toggleButtons();
                            }
                        });

                        PreviousTextViewClubUsersActivity.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CurrentPage -= 1;
                                PrizeAllClubRecyclerViewAdapter prizeAllRecyclerViewAdapter = new PrizeAllClubRecyclerViewAdapter(ClubUsersActivity.this, GeneratePage(CurrentPage, ViewModelList), MyPoint);
                                GiftRecyclerViewClubUsersActivity.setAdapter(prizeAllRecyclerViewAdapter);
                                toggleButtons();
                            }
                        });
                    } else {

                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);

                    } else {
                        ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.UserPointGet) {
                Feedback<Double> FeedBack = (Feedback<Double>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    MyPoint = FeedBack.getValue();
                    if (MyPoint != null) {

                        MyPointTextViewClubUsersActivity.setText(Utility.GetIntegerNumberWithComma(MyPoint));
                    } else {
                        MyPointTextViewClubUsersActivity.setText(getResources().getString(R.string.zero));
                    }

                    PrizeService prizeService = new PrizeService(ClubUsersActivity.this);
                    prizeService.GetAll();

                } else {
                    HideLoading();
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

    private void toggleButtons() {

        if (CurrentPage == TotalPages) {

            NextTextViewClubUsersActivity.setClickable(false);
            PreviousTextViewClubUsersActivity.setClickable(true);

            NextTextViewClubUsersActivity.setTextColor(getResources().getColor(R.color.FontSemiBlackColor));
            PreviousTextViewClubUsersActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));

        } else if (CurrentPage == 0) {

            PreviousTextViewClubUsersActivity.setClickable(false);
            NextTextViewClubUsersActivity.setClickable(true);

            PreviousTextViewClubUsersActivity.setTextColor(getResources().getColor(R.color.FontSemiBlackColor));
            NextTextViewClubUsersActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));

        } else if (CurrentPage >= 1 && CurrentPage < TotalPages) {

            NextTextViewClubUsersActivity.setClickable(true);
            PreviousTextViewClubUsersActivity.setClickable(true);

            NextTextViewClubUsersActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));
            PreviousTextViewClubUsersActivity.setTextColor(getResources().getColor(R.color.FontSemiDarkThemeColor));
        }
    }

    public List<PrizeViewModel> GeneratePage(int currentPage, List<PrizeViewModel> ViewModel) {

        List<PrizeViewModel> pageData = new ArrayList<>();
        pageData.add(ViewModel.get(currentPage));

        return pageData;
    }

    @Override
    protected void onGetResult(ActivityResult Result) {
        if (Result.getFromActivityId() == getCurrentActivityId()) {
            switch (Result.getToActivityId()) {
                case ActivityIdList.PRIZE_ALL_ACTIVITY:

                    finish();
                    Intent ClubUsersIntent = new Intent(ClubUsersActivity.this, ClubUsersActivity.class);
                    startActivity(ClubUsersIntent);
                    
                    break;
            }
        }
        super.onGetResult(Result);
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

