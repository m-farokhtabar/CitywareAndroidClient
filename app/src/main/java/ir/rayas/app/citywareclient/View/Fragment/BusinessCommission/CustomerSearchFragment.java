package ir.rayas.app.citywareclient.View.Fragment.BusinessCommission;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.NewSuggestionBusinessCommissionRecyclerViewAdapter;
import ir.rayas.app.citywareclient.Global.Static;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Marketing.MarketingService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.EditTextPersian;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.MasterChildren.ShowBusinessCommissionActivity;
import ir.rayas.app.citywareclient.ViewModel.Marketing.MarketingBusinessViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerSearchFragment extends Fragment implements IResponseService, ILoadData {

    private ShowBusinessCommissionActivity Context = null;

    private boolean IsLoadedDataForFirst = false;
    private int BusinessId = 0;
    private boolean IsSwipe = false;
    private int PageNumber = 1;
    private String TextSearch = "";

    private SwipeRefreshLayout RefreshCustomerSwipeRefreshLayoutCustomerSearchFragment = null;
    private TextViewPersian ShowEmptyCustomerTextViewCustomerSearchFragment = null;
    private NewSuggestionBusinessCommissionRecyclerViewAdapter newSuggestionBusinessCommissionRecyclerViewAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //دریافت اکتیوتی والد این فرگمین
        Context = (ShowBusinessCommissionActivity) getActivity();
        // Inflate the layout for this fragment
        View CurrentView = inflater.inflate(R.layout.fragment_customer_search, container, false);

//      BusinessId = Context.getIntent().getExtras().getInt("BusinessId");
        BusinessId = Context.getBusinessId();
        //طرحبندی ویو
        CreateLayout(CurrentView);

        return CurrentView;
    }

    private void CreateLayout(View CurrentView) {

        ShowEmptyCustomerTextViewCustomerSearchFragment = CurrentView.findViewById(R.id.ShowEmptyCustomerTextViewCustomerSearchFragment);
        RefreshCustomerSwipeRefreshLayoutCustomerSearchFragment = CurrentView.findViewById(R.id.RefreshCustomerSwipeRefreshLayoutCustomerSearchFragment);
        EditTextPersian SearchUserEditTextCustomerSearchFragment = CurrentView.findViewById(R.id.SearchUserEditTextCustomerSearchFragment);
        ShowEmptyCustomerTextViewCustomerSearchFragment.setVisibility(View.GONE);

        RecyclerView CustomerRecyclerViewCustomerSearchFragment = CurrentView.findViewById(R.id.CustomerRecyclerViewCustomerSearchFragment);
        CustomerRecyclerViewCustomerSearchFragment.setHasFixedSize(true);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(Context);
        CustomerRecyclerViewCustomerSearchFragment.setLayoutManager(LinearLayoutManager);

        newSuggestionBusinessCommissionRecyclerViewAdapter = new NewSuggestionBusinessCommissionRecyclerViewAdapter(Context, null, CustomerRecyclerViewCustomerSearchFragment);
        CustomerRecyclerViewCustomerSearchFragment.setAdapter(newSuggestionBusinessCommissionRecyclerViewAdapter);


        RefreshCustomerSwipeRefreshLayoutCustomerSearchFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                IsSwipe = true;
                PageNumber = 1;
                LoadData();
            }
        });


        SearchUserEditTextCustomerSearchFragment.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                IsSwipe = true;
                List<MarketingBusinessViewModel> ViewModelList = new ArrayList<>();
                newSuggestionBusinessCommissionRecyclerViewAdapter.SetViewModelList(ViewModelList);
                PageNumber = 1;

                if (s.length() != 0) {
                    TextSearch = s.toString();
                    try {
                        String Temp = URLEncoder.encode(TextSearch, "utf-8");
                        TextSearch = Temp;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    TextSearch = "";
                }

                LoadData();

            }
        });


    }

    /**
     * دریافت اطلاعات نحوای جهت پر کردن Recycle
     */
    public void LoadData() {
        if (!IsSwipe)
            if (PageNumber == 1)
                Context.ShowLoadingProgressBar();

        Context.setRetryType(2);
        MarketingService MarketingService = new MarketingService(CustomerSearchFragment.this);
        MarketingService.GetAllNewSuggestionBusinessCommission(BusinessId, PageNumber, TextSearch);
    }

    /**
     * @param Data
     * @param ServiceMethod
     * @param <T>
     */
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        RefreshCustomerSwipeRefreshLayoutCustomerSearchFragment.setRefreshing(false);
        IsSwipe = false;
        try {
            if (ServiceMethod == ServiceMethodType.NewSuggestionBusinessCommissionGetAll) {
                Context.HideLoading();
                Feedback<List<MarketingBusinessViewModel>> FeedBack = (Feedback<List<MarketingBusinessViewModel>>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    Static.IsRefreshBookmark = false;

                    final List<MarketingBusinessViewModel> ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        if (PageNumber == 1) {
                            if (ViewModelList.size() < 1) {
                                ShowEmptyCustomerTextViewCustomerSearchFragment.setVisibility(View.VISIBLE);
                            } else {
                                ShowEmptyCustomerTextViewCustomerSearchFragment.setVisibility(View.GONE);
                                newSuggestionBusinessCommissionRecyclerViewAdapter.SetViewModelList(ViewModelList);

                                if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                    PageNumber = PageNumber + 1;
                                    LoadData();
                                }
                            }

                        } else {
                            ShowEmptyCustomerTextViewCustomerSearchFragment.setVisibility(View.GONE);
                            newSuggestionBusinessCommissionRecyclerViewAdapter.AddViewModelList(ViewModelList);

                            if (DefaultConstant.PageNumberSize == ViewModelList.size()) {
                                PageNumber = PageNumber + 1;
                                LoadData();
                            }
                        }
                    }
                } else if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId()) {
                    if (PageNumber > 1) {
                        ShowEmptyCustomerTextViewCustomerSearchFragment.setVisibility(View.GONE);
                    } else {
                        ShowEmptyCustomerTextViewCustomerSearchFragment.setVisibility(View.VISIBLE);
                    }
                } else {
                    ShowEmptyCustomerTextViewCustomerSearchFragment.setVisibility(View.GONE);
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            }

        } catch (Exception e) {
            Context.ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            //برای فهمیدن کد فرگنت به UserProfilePagerAdapter مراجعه کنید
            Context.setFragmentIndex(3);
            if (!IsLoadedDataForFirst) {
                IsSwipe = false;
                IsLoadedDataForFirst = true;
                //دریافت اطلاعات از سرور
                LoadData();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

}
