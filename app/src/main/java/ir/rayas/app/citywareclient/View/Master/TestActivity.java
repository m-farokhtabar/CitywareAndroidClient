package ir.rayas.app.citywareclient.View.Master;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.BusinessPosterInfoRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Adapter.RecyclerView.TestAdapter;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.Home.HomeService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Share.Enum.QueryType;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.ViewModel.Home.BusinessPosterInfoViewModel;

public class TestActivity extends AppCompatActivity implements IResponseService {

    private TestAdapter businessPosterInfoRecyclerViewAdapter = null;
    private RecyclerView businessPosterInfoRecyclerViewMainActivity = null;
    private ProgressBar LoadMoreProgressBarMainActivity = null;

    GridLayoutManager layoutManager;
    private boolean isLoading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previous_total = 0;
    private int view_threshold = 10;


    private int PageNumberPoster = 1;
    private int PageItems = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);



        LoadMoreProgressBarMainActivity = findViewById(R.id.LoadMoreProgressBarMainActivity);

        businessPosterInfoRecyclerViewMainActivity = findViewById(R.id.BusinessPosterInfoRecyclerViewMainActivity);
        layoutManager = new GridLayoutManager(this, 1);
        businessPosterInfoRecyclerViewMainActivity.setHasFixedSize(true);
        businessPosterInfoRecyclerViewMainActivity.setLayoutManager(layoutManager);

        businessPosterInfoRecyclerViewAdapter = new TestAdapter(TestActivity.this, null, businessPosterInfoRecyclerViewMainActivity);
        businessPosterInfoRecyclerViewMainActivity.setAdapter(businessPosterInfoRecyclerViewAdapter);


        LoadMoreProgressBarMainActivity.setVisibility(View.GONE);


        LoadDataInfo();

        businessPosterInfoRecyclerViewMainActivity.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // تعداد ایتم های نمایش داده شده در صفحه
                visibleItemCount = layoutManager.getChildCount();
                // تعداد ایتم های نمایش داده شده در یک پیج
                totalItemCount = layoutManager.getItemCount();
                // تعداد ایتم هایی که از صفحه قسمت بالا و پایین خارج می شود
                pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                if (dy > 0) {
                    if (isLoading) {
                        if (totalItemCount > previous_total) {
                            isLoading = false;
                            previous_total = totalItemCount;
                        }
                    }

                    if (!isLoading && (totalItemCount - visibleItemCount) <= (pastVisibleItems + view_threshold)) {

                        PageNumberPoster = PageNumberPoster + 1;
                        LoadMoreProgressBarMainActivity.setVisibility(View.VISIBLE);
                        LoadDataInfo();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void LoadDataInfo() {

        HomeService homeService = new HomeService(TestActivity.this);
        homeService.GetAll( QueryType.New.GetQueryType(), null, null, null, null, null, PageNumberPoster, PageItems);
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {

        LoadMoreProgressBarMainActivity.setVisibility(View.GONE);
        if (ServiceMethod == ServiceMethodType.BusinessPosterInfoGetAll) {
            Feedback<List<BusinessPosterInfoViewModel>> FeedBack = (Feedback<List<BusinessPosterInfoViewModel>>) Data;

            if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                Static.IsRefreshBookmark = false;

                final List<BusinessPosterInfoViewModel> ViewModelList = FeedBack.getValue();
                if (ViewModelList != null) {


                    if (PageNumberPoster == 1) {
                        if (ViewModelList.size() > 0) {
                            businessPosterInfoRecyclerViewAdapter.SetViewModelList(ViewModelList);


                        }
                    } else {
                        businessPosterInfoRecyclerViewAdapter.AddViewModelList(ViewModelList);

                    }
                }
            } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {

            } else {

            }
        }
    }
}
