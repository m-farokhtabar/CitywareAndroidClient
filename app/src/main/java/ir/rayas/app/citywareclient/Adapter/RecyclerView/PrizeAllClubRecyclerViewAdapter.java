package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Prize.PrizeService;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Master.ClubUsersActivity;
import ir.rayas.app.citywareclient.View.MasterChildren.PrizeAllActivity;
import ir.rayas.app.citywareclient.View.UserProfileChildren.PackageActivity;
import ir.rayas.app.citywareclient.ViewModel.Club.PrizeViewModel;
import ir.rayas.app.citywareclient.ViewModel.Club.RequestPrizeViewModel;
import ir.rayas.app.citywareclient.ViewModel.Club.UserConsumePointViewModel;


public class PrizeAllClubRecyclerViewAdapter extends RecyclerView.Adapter<PrizeAllClubRecyclerViewAdapter.ViewHolder> implements IResponseService {

    private List<PrizeViewModel> ViewModelList = null;
    private Double MyPoint;
    private ClubUsersActivity Context;

    public PrizeAllClubRecyclerViewAdapter(ClubUsersActivity context, List<PrizeViewModel> ViewModel, Double myPoint) {
        this.ViewModelList = ViewModel;
        this.MyPoint = myPoint;
        this.Context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextViewPersian PercentTextView;
        ProgressBar PercentProgressBar;
        TextViewPersian TitlePrizeTextView;
        TextViewPersian DescriptionPrizeTextView;
        TextViewPersian PointTextView;
        RelativeLayout PrizeAllRelativeLayout;
        CardView GetPrizeCardView;


        public ViewHolder(View v) {
            super(v);
            PercentProgressBar = v.findViewById(R.id.PercentProgressBar);
            PercentTextView = v.findViewById(R.id.PercentTextView);
            TitlePrizeTextView = v.findViewById(R.id.TitlePrizeTextView);
            DescriptionPrizeTextView = v.findViewById(R.id.DescriptionPrizeTextView);
            PointTextView = v.findViewById(R.id.PointTextView);
            PrizeAllRelativeLayout = v.findViewById(R.id.PrizeAllRelativeLayout);
            GetPrizeCardView = v.findViewById(R.id.GetPrizeCardView);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_club_prize_all, parent, false);
        return new ViewHolder(CurrentView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.DescriptionPrizeTextView.setText(ViewModelList.get(position).getDescription());
        holder.TitlePrizeTextView.setText(ViewModelList.get(position).getTitle());
        holder.PointTextView.setText(Utility.GetIntegerNumberWithComma(ViewModelList.get(position).getPoint()) + "  " + Context.getResources().getString(R.string.rate));


        Double Point = ViewModelList.get(position).getPoint();
        Double Percent;
        if (Point > 0 && Point != null) {
            Percent = (MyPoint * 100) / Point;
            if (Percent >= 100) {
                Percent = 100.0;
            } else {
                Percent = Percent;
            }
        } else {
            Percent = 0.0;
        }

        int PercentProgress = (int) Double.parseDouble(Percent.toString());
        holder.PercentTextView.setText(String.valueOf(PercentProgress) + " " + Context.getResources().getString(R.string.percent));

        Drawable drawable = Context.getResources().getDrawable(R.drawable.circular_percent_progress_bar);
        holder.PercentProgressBar.setProgress(PercentProgress);   // Main Progress
        holder.PercentProgressBar.setSecondaryProgress(100); // Secondary Progress
        holder.PercentProgressBar.setMax(100); // Maximum Progress
        holder.PercentProgressBar.setProgressDrawable(drawable);

        holder.PrizeAllRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent PrizeAllIntent = Context.NewIntent(PrizeAllActivity.class);
                PrizeAllIntent.putExtra("MyPoint", MyPoint);
                Context.startActivity(PrizeAllIntent);
            }
        });


        if (PercentProgress == 100) {
            holder.GetPrizeCardView.setVisibility(View.VISIBLE);

            holder.GetPrizeCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (ViewModelList.get(position).getPackageId() == null || ViewModelList.get(position).getPackageId() == 0) {
                        ShowBuyPrizeDialog(MyPoint, ViewModelList.get(position).getPoint(), ViewModelList.get(position).getTitle(), ViewModelList.get(position).getId());
                    } else {
                        Intent NewPackageIntent = Context.NewIntent(PackageActivity.class);
                        NewPackageIntent.putExtra("New", "BuyPrize");
                        NewPackageIntent.putExtra("PackageId", ViewModelList.get(position).getPackageId());
                        Context.startActivity(NewPackageIntent);
                    }
                }
            });

        } else {
            holder.GetPrizeCardView.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        int Output;
        if (ViewModelList == null)
            Output = 0;
        else
            Output = ViewModelList.size();
        return Output;
    }

    private void ShowBuyPrizeDialog(Double YourPoint, Double Point, String PackageName, final int Id) {

        final Dialog BuyPrizeDialog = new Dialog(Context);
        BuyPrizeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        BuyPrizeDialog.setContentView(R.layout.dialog_buy_prize);

        Typeface typeface = Typeface.createFromAsset(Context.getAssets(), "fonts/iransanslight.ttf");

        ButtonPersianView DialogBuyPrizeOkButton = BuyPrizeDialog.findViewById(R.id.DialogBuyPrizeOkButton);
        ButtonPersianView DialogBuyPrizeCancelButton = BuyPrizeDialog.findViewById(R.id.DialogBuyPrizeCancelButton);


        TextView DialogPointTextView = BuyPrizeDialog.findViewById(R.id.DialogPointTextView);
        TextViewPersian DialogMyPointTextView = BuyPrizeDialog.findViewById(R.id.DialogMyPointTextView);
        DialogMyPointTextView.setText(Utility.GetIntegerNumberWithComma(YourPoint));

        DialogPointTextView.setTypeface(typeface);

        String Name = "<font color='#ff0000'><b>" + PackageName + "</b></font>";
        String point = "<font color='#ff0000'><b>" + Utility.GetIntegerNumberWithComma(Point) + "</b></font>";
        String Message = Context.getResources().getString(R.string.for_buy) + " " + Name + " " + Context.getResources().getString(R.string.need_to) + " " + point + " " + Context.getResources().getString(R.string.are_you_sure_you_want_to_purchase_the_point_you_are_looking_for);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            DialogPointTextView.setText(Html.fromHtml(Message, Html.FROM_HTML_MODE_COMPACT));
        } else {
            DialogPointTextView.setText(Html.fromHtml(Message));
        }


        DialogBuyPrizeOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context.ShowLoadingProgressBar();

                PrizeService prizeService = new PrizeService(PrizeAllClubRecyclerViewAdapter.this);
                prizeService.AddPrizeRequest(MadeViewModel(Id));

                BuyPrizeDialog.dismiss();

            }
        });

        DialogBuyPrizeCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyPrizeDialog.dismiss();
            }
        });

        BuyPrizeDialog.show();
    }


    private RequestPrizeViewModel MadeViewModel(int Id) {
        RequestPrizeViewModel ViewModel = new RequestPrizeViewModel();
        try {
            ViewModel.setId(Id);

        } catch (Exception Ex) {
        }
        return ViewModel;
    }

    @Override
    public <T> void OnResponse(T Data, ServiceMethodType ServiceMethod) {
        Context.HideLoading();
        try {
            if (ServiceMethod == ServiceMethodType.PrizeRequestAdd) {
                Feedback<UserConsumePointViewModel> FeedBack = (Feedback<UserConsumePointViewModel>) Data;

                if (FeedBack.getStatus() == FeedbackType.RegisteredSuccessful.getId()) {

                    UserConsumePointViewModel ViewModelList = FeedBack.getValue();
                    if (ViewModelList != null) {
                        ShowMessageBuyPrizeDialog();
                    }
                } else {
                    if (FeedBack.getStatus() != FeedbackType.ThereIsNoInternet.getId()) {
                        if (FeedBack.getStatus() == FeedbackType.DataIsNotFound.getId())
                            Context.ShowToast(FeedBack.getMessage(), Toast.LENGTH_LONG, MessageType.values()[FeedBack.getMessageType()]);
                    } else {
                        Context.ShowErrorInConnectDialog();
                    }
                }
            }
        } catch (Exception e) {
            Context.HideLoading();
            Context.ShowToast(FeedbackType.ThereIsSomeProblemInApp.getMessage(), Toast.LENGTH_LONG, MessageType.Error);
        }

    }


    private void ShowMessageBuyPrizeDialog() {

        final Dialog OkBuyPrizeDialog = new Dialog(Context);
        OkBuyPrizeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        OkBuyPrizeDialog.setContentView(R.layout.dialog_ok_buy_prize);

        ButtonPersianView DialogOkButton = OkBuyPrizeDialog.findViewById(R.id.DialogOkButton);
        TextViewPersian DialogMessageTextView = OkBuyPrizeDialog.findViewById(R.id.DialogMessageTextView);

        DialogMessageTextView.setText(Context.getResources().getString(R.string.message_show_get_prize));


        DialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkBuyPrizeDialog.dismiss();

                Context.finish();
                Intent ClubUsersIntent = new Intent(Context, ClubUsersActivity.class);
                Context.startActivity(ClubUsersIntent);
            }
        });

        OkBuyPrizeDialog.show();
    }

}

