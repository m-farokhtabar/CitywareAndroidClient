package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Package.PackageService;
import ir.rayas.app.citywareclient.Service.Poster.PosterService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Master.UserProfileActivity;
import ir.rayas.app.citywareclient.View.UserProfileChildren.BuyPosterSetActivity;
import ir.rayas.app.citywareclient.ViewModel.Poster.ExtendBuyPosterViewModel;
import ir.rayas.app.citywareclient.ViewModel.Poster.PurchasedPosterViewModel;


public class PosterValidRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IResponseService {

    private UserProfileActivity Context;
    private RecyclerView Container = null;
    private List<PurchasedPosterViewModel> ViewModelList = null;

    private int Hours = 0;
    private int Day = 0;
    private double TotalPrice = 0.0;

    private int Position = 0;

    private Dialog ExtendedPosterTypeDialog;

    private TextViewPersian TotalPriceTextView = null;

    public PosterValidRecyclerViewAdapter(UserProfileActivity Context, List<PurchasedPosterViewModel> ViewModelList, RecyclerView Container) {
        this.ViewModelList = ViewModelList;
        this.Context = Context;
        this.Container = Container;
    }
    /**
     * اضافه مودن لیست جدید
     *
     * @param ViewModel
     */
    public void AddViewModelList(List<PurchasedPosterViewModel> ViewModel) {
        if (ViewModel != null) {
            if (ViewModelList == null)
                ViewModelList = new ArrayList<>();
            ViewModelList.addAll(ViewModel);
            notifyDataSetChanged();
            Container.invalidate();
        }
    }
    /**
     * جایگزین نمودن لیست جدید
     *
     * @param ViewModel
     */
    public void SetViewModelList(List<PurchasedPosterViewModel> ViewModel) {
        ViewModelList = new ArrayList<>();
        ViewModelList.addAll(ViewModel);
        notifyDataSetChanged();
        Container.invalidate();
    }
    /**
     * اضافه مودن یک آدرس جدید به لیست
     *
     * @param ViewModel اطلاعات پوستر
     */
    public void AddViewModel(PurchasedPosterViewModel ViewModel) {
        if (ViewModel != null) {
            if (ViewModelList == null)
                ViewModelList = new ArrayList<>();
            ViewModelList.add(ViewModel);
            notifyDataSetChanged();
            Container.invalidate();
        }
    }
    /**
     * ویرایش اطلاعات یک پوستر در لیست
     * @param ViewModel
     */
    public void SetViewModel(PurchasedPosterViewModel ViewModel)
    {
        if (ViewModel!=null && ViewModelList!=null && ViewModelList.size()>0) {
            for (PurchasedPosterViewModel Item : ViewModelList) {
                if (Item.getId() == ViewModel.getId()) {
                    Item.setTitle(ViewModel.getTitle());
                    Item.setImagePathUrl(ViewModel.getImagePathUrl());

                }
            }
            notifyDataSetChanged();
            Container.invalidate();
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(Context).inflate(R.layout.recycler_view_poster_valid, parent, false);
        return new PosterViewHolder(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        PosterViewHolder holder = (PosterViewHolder) viewHolder;

        if (ViewModelList.get(position).getTitle().equals("") || ViewModelList.get(position).getTitle() == null)
            holder.PosterTitleTextView.setText(ViewModelList.get(position).getPosterTypeTitle());
        else
            holder.PosterTitleTextView.setText(ViewModelList.get(position).getTitle());

        holder.BusinessNameTextView.setText(ViewModelList.get(position).getBusinessName());
        holder.ExpireDateTextView.setText(ViewModelList.get(position).getExpireDate());
        holder.PosterCostTextView.setText(Utility.GetIntegerNumberWithComma(ViewModelList.get(position).getPosterPrice()));


        if (!ViewModelList.get(position).getImagePathUrl().equals("")) {
            LayoutUtility.LoadImageWithGlide(Context, ViewModelList.get(position).getImagePathUrl(), holder.PosterImageView);
        } else {
            holder.PosterImageView.setImageResource(R.drawable.image_default);
        }

        if (ViewModelList.get(position).isActive()) {
            holder.EditPosterButton.setVisibility(View.VISIBLE);
            holder.ExtendedPosterButton.setVisibility(View.VISIBLE);
        } else {
            holder.EditPosterButton.setVisibility(View.GONE);
            holder.ExtendedPosterButton.setVisibility(View.GONE);
        }

        holder.ExtendedPosterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context.ShowLoadingProgressBar();
                Position = position;
                PackageService packageService = new PackageService(PosterValidRecyclerViewAdapter.this);
                packageService.GetUserCredit();
            }
        });

        holder.EditPosterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent EditPosterIntent = Context.NewIntent(BuyPosterSetActivity.class);
                EditPosterIntent.putExtra("PosterId", ViewModelList.get(position).getId());
                Context.startActivity(EditPosterIntent);
            }
        });

        holder.DetailsBuyPosterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDetailsPackageBuyDialog(ViewModelList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return ViewModelList == null ? 0 : ViewModelList.size();
    }

    private class PosterViewHolder extends RecyclerView.ViewHolder {

        ButtonPersianView ExtendedPosterButton;
        ButtonPersianView EditPosterButton;
        ButtonPersianView DetailsBuyPosterButton;
        TextViewPersian PosterTitleTextView;
        TextViewPersian BusinessNameTextView;
        TextViewPersian ExpireDateTextView;
        TextViewPersian PosterCostTextView;
        ImageView PosterImageView;

        PosterViewHolder(View v) {
            super(v);
            PosterTitleTextView = v.findViewById(R.id.PosterTitleTextView);
            ExtendedPosterButton = v.findViewById(R.id.ExtendedPosterButton);
            EditPosterButton = v.findViewById(R.id.EditPosterButton);
            BusinessNameTextView = v.findViewById(R.id.BusinessNameTextView);
            DetailsBuyPosterButton = v.findViewById(R.id.DetailsBuyPosterButton);
            PosterImageView = v.findViewById(R.id.PosterImageView);
            ExpireDateTextView = v.findViewById(R.id.ExpireDateTextView);
            PosterCostTextView = v.findViewById(R.id.PosterCostTextView);
        }
    }

    /**
     * @param Data
     * @param ServiceMethod
     * @param <T>
     */
    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.UserCreditGet) {
                Feedback<Double> FeedBack = (Feedback<Double>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {
                    if (FeedBack.getValue() != null) {

                        ShowDialogExtendedPosterType(ViewModelList.get(Position), FeedBack.getValue());
                    } else {
                        ShowDialogExtendedPosterType(ViewModelList.get(Position), 0);
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            } else if (ServiceMethod == ServiceMethodType.ExtendPosterEdit) {
                Feedback<PurchasedPosterViewModel> FeedBack = (Feedback<PurchasedPosterViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.FetchSuccessful.getId()) {

                    if (FeedBack.getValue() != null) {
                        Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    }
                } else {
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

    private ExtendBuyPosterViewModel MadeViewModel(double Hours) {

        ExtendBuyPosterViewModel ViewModel = new ExtendBuyPosterViewModel();
        try {
            ViewModel.setHours((int) Hours);
            ViewModel.setPurchasedPosterId(ViewModelList.get(Position).getId());
        } catch (Exception Ex) {
            Context.ShowToast(FeedbackType.InvalidDataFormat.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }
        return ViewModel;
    }


    @SuppressLint("SetTextI18n")
    private void ShowDetailsPackageBuyDialog(PurchasedPosterViewModel ViewModel) {

        final Dialog DetailsBuyPosterDialog = new Dialog(Context);
        DetailsBuyPosterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DetailsBuyPosterDialog.setContentView(R.layout.dialog_poster_buy_details);

        ButtonPersianView DialogOkButton = DetailsBuyPosterDialog.findViewById(R.id.DialogOkButton);
        TextViewPersian PosterCostTextView = DetailsBuyPosterDialog.findViewById(R.id.PosterCostTextView);
        TextViewPersian PriorityTextView = DetailsBuyPosterDialog.findViewById(R.id.PriorityTextView);
        SwitchCompat AlwaysOnTopSwitch = DetailsBuyPosterDialog.findViewById(R.id.AlwaysOnTopSwitch);
        TextViewPersian CreateDateTextView = DetailsBuyPosterDialog.findViewById(R.id.CreateDateTextView);
        TextViewPersian DimensionPosterTextView = DetailsBuyPosterDialog.findViewById(R.id.DimensionPosterTextView);
        LinearLayout PriorityLinearLayout = DetailsBuyPosterDialog.findViewById(R.id.PriorityLinearLayout);
        LinearLayout DimensionPosterLinearLayout = DetailsBuyPosterDialog.findViewById(R.id.DimensionPosterLinearLayout);

        PosterCostTextView.setText(Utility.GetIntegerNumberWithComma(ViewModel.getPosterPrice()));
        CreateDateTextView.setText(ViewModel.getCreate());
        PriorityTextView.setText(String.valueOf(ViewModel.getPriority()));
        AlwaysOnTopSwitch.setChecked(ViewModel.isTop());

        int Rows = ViewModel.getRows();
        int Cols = ViewModel.getCols();
        DimensionPosterTextView.setText(Rows + Context.getResources().getString(R.string.star) + Cols);

        AlwaysOnTopSwitch.setClickable(false);

        if (ViewModel.isTop()) {
            PriorityLinearLayout.setVisibility(View.VISIBLE);
            DimensionPosterLinearLayout.setVisibility(View.GONE);
        } else {
            PriorityLinearLayout.setVisibility(View.GONE);
            DimensionPosterLinearLayout.setVisibility(View.VISIBLE);
        }

        DialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailsBuyPosterDialog.dismiss();
            }
        });

        DetailsBuyPosterDialog.show();
    }


    private void ShowDialogExtendedPosterType(final PurchasedPosterViewModel ViewModel, double UserCredit) {

        ExtendedPosterTypeDialog = new Dialog(Context);
        ExtendedPosterTypeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ExtendedPosterTypeDialog.setContentView(R.layout.dialog_buy_poster_type);

        TextViewPersian YourCreditTextView = ExtendedPosterTypeDialog.findViewById(R.id.YourCreditTextView);
        final TextViewPersian PosterTypeTextView = ExtendedPosterTypeDialog.findViewById(R.id.PosterTypeTextView);
        TextViewPersian priceTextView = ExtendedPosterTypeDialog.findViewById(R.id.priceTextView);
        TotalPriceTextView = ExtendedPosterTypeDialog.findViewById(R.id.TotalPriceTextView);
        final NumberPicker DayNumberPicker = ExtendedPosterTypeDialog.findViewById(R.id.DayNumberPicker);
        final NumberPicker HoursNumberPicker = ExtendedPosterTypeDialog.findViewById(R.id.HoursNumberPicker);
        ButtonPersianView DialogExtendedPosterCancelButton = ExtendedPosterTypeDialog.findViewById(R.id.DialogExtendedPosterCancelButton);
        ButtonPersianView DialogExtendedPosterOkButton = ExtendedPosterTypeDialog.findViewById(R.id.DialogExtendedPosterOkButton);
        FrameLayout Line1FrameLayout = ExtendedPosterTypeDialog.findViewById(R.id.Line1FrameLayout);


        if (ViewModel.getTitle().equals("")) {
            PosterTypeTextView.setVisibility(View.GONE);
            Line1FrameLayout.setVisibility(View.GONE);
        } else {
            PosterTypeTextView.setVisibility(View.VISIBLE);
            Line1FrameLayout.setVisibility(View.VISIBLE);
            PosterTypeTextView.setText(ViewModel.getTitle());
        }
        priceTextView.setText(Utility.GetIntegerNumberWithComma(ViewModel.getPosterPrice()));
        YourCreditTextView.setText(Utility.GetIntegerNumberWithComma(UserCredit));
        TotalPriceTextView.setText(Utility.GetIntegerNumberWithComma(TotalPrice));

        HoursNumberPicker.setMinValue(0);
        HoursNumberPicker.setMaxValue(23);
        DayNumberPicker.setMinValue(0);
        DayNumberPicker.setMaxValue(31);
        HoursNumberPicker.setValue(0);
        DayNumberPicker.setValue(0);

        final double[] PriceHours = {0.0};
        final double[] PriceDay = {0.0};


        DayNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int newDay) {
                Day = newDay;
                double ConvertToHours = Day * 24;
                PriceDay[0] = ConvertToHours * ViewModel.getPosterPrice();
                TotalPrice = PriceHours[0] + PriceDay[0];
                TotalPriceTextView.setText(Utility.GetIntegerNumberWithComma(TotalPrice));
            }
        });

        HoursNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int newHours) {
                Hours = newHours;
                PriceHours[0] = Hours * ViewModel.getPosterPrice();
                TotalPrice = PriceDay[0] + PriceHours[0];
                TotalPriceTextView.setText(Utility.GetIntegerNumberWithComma(TotalPrice));
            }
        });

        DialogExtendedPosterOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Hours == 0 && Day == 0) {
                    Context.ShowToast(Context.getResources().getString(R.string.specify_days_or_hours), Toast.LENGTH_LONG, MessageType.Warning);
                } else {
                    Context.ShowLoadingProgressBar();
                    PosterService PosterService = new PosterService(PosterValidRecyclerViewAdapter.this);
                    PosterService.EditExtendPoster(MadeViewModel(Day + Hours));

                    ExtendedPosterTypeDialog.dismiss();

                    TotalPrice = 0;
                    Day = 0;
                    Hours = 0;

                    HoursNumberPicker.setValue(0);
                    DayNumberPicker.setValue(0);

                    TotalPriceTextView.setText(Context.getResources().getString(R.string.zero));

                }
            }
        });

        DialogExtendedPosterCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExtendedPosterTypeDialog.dismiss();
            }
        });

        ExtendedPosterTypeDialog.show();
    }

}
