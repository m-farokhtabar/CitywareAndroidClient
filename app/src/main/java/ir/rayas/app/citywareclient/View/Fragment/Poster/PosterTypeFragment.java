package ir.rayas.app.citywareclient.View.Fragment.Poster;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.PosterTypeRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Package.PackageService;
import ir.rayas.app.citywareclient.Service.Poster.PosterService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.UserProfileChildren.PosterTypeActivity;
import ir.rayas.app.citywareclient.ViewModel.Poster.PosterTypeViewModel;


public class PosterTypeFragment extends Fragment implements IResponseService, ILoadData {

    private SwipeRefreshLayout RefreshPosterTypeSwipeRefreshLayoutPosterTypeActivity = null;
    private TextViewPersian UserCreditTextViewPosterTypeActivity = null;
    private PosterTypeRecyclerViewAdapter PosterTypeRecyclerViewAdapter = null;

    private int PageNumber = 1;
   private PosterTypeActivity Context = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (PosterTypeActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_poster_type, container, false);

        //ایجاد طرحبندی صفحه
        CreateLayout(CurrentView);

        LoadDataUserCredit();

        return CurrentView;
    }



    public void LoadDataUserCredit() {

        Context. ShowLoadingProgressBar();

        PackageService packageService = new PackageService(this);
        packageService.GetUserCredit();

    }

    /**
     * دریافت اطلاعات نحوای جهت پر کردن Recycle
     */
    public void LoadData() {

        PosterService PosterService = new PosterService(this);
        PosterService.GetAllPosterType(PageNumber);
    }

    /**
     * تنظیمات مربوط به رابط کاربری این فرم
     */
    private void CreateLayout(View CurrentView) {
        RefreshPosterTypeSwipeRefreshLayoutPosterTypeActivity = CurrentView.findViewById(R.id.RefreshPosterTypeSwipeRefreshLayoutPosterTypeActivity);
        UserCreditTextViewPosterTypeActivity = CurrentView.findViewById(R.id.UserCreditTextViewPosterTypeActivity);

        RecyclerView PosterTypeRecyclerViewPosterTypeActivity =CurrentView. findViewById(R.id.PosterTypeRecyclerViewPosterTypeActivity);
        PosterTypeRecyclerViewPosterTypeActivity.setLayoutManager(new LinearLayoutManager(Context));
        PosterTypeRecyclerViewAdapter = new PosterTypeRecyclerViewAdapter(Context, null, PosterTypeRecyclerViewPosterTypeActivity);
        PosterTypeRecyclerViewPosterTypeActivity.setAdapter(PosterTypeRecyclerViewAdapter);

        RefreshPosterTypeSwipeRefreshLayoutPosterTypeActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PageNumber = 1;
                LoadDataUserCredit();
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

        RefreshPosterTypeSwipeRefreshLayoutPosterTypeActivity.setRefreshing(false);
        try {
            if (ServiceMethod == ServiceMethodType.UserCreditGet) {
                Feedback<Double> FeedBack = (Feedback<Double>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    LoadData();

                    if (FeedBack.getValue() != null) {
                        UserCreditTextViewPosterTypeActivity.setText(Utility.GetIntegerNumberWithComma(FeedBack.getValue()));
                    } else {
                        UserCreditTextViewPosterTypeActivity.setText(getResources().getString(R.string.zero));
                    }

                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }

            } else if (ServiceMethod == ServiceMethodType.PosterTypeGetAll) {
                Context.HideLoading();
                Feedback<List<PosterTypeViewModel>> FeedBack = (Feedback<List<PosterTypeViewModel>>) Data;


                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;
                    final List<PosterTypeViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        if (PageNumber == 1) {
                            if (ViewModelList.size() > 0) {

                                PosterTypeRecyclerViewAdapter.SetViewModelList(ViewModelList);

                                if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                    PageNumber = PageNumber + 1;
                                    LoadData();
                                }
                            }

                        } else {
                            PosterTypeRecyclerViewAdapter.AddViewModelList(ViewModelList);

                            if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                PageNumber = PageNumber + 1;
                                LoadData();
                            }
                        }
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context. ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context. ShowErrorInConnectDialog();
                    }
                }
            }
        } catch (Exception e) {
            Context. HideLoading();
            Context. ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
    }


}
