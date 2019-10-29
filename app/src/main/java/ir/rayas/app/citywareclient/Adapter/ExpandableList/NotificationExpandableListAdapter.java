package ir.rayas.app.citywareclient.Adapter.ExpandableList;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ir.rayas.app.citywareclient.Adapter.RecyclerView.BusinessFactorListRecyclerViewAdapter;
import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.Repository.NotificationRepository;
import ir.rayas.app.citywareclient.Service.Factor.BusinessFactorService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.Notification.NotificationService;
import ir.rayas.app.citywareclient.Share.Enum.FileType;
import ir.rayas.app.citywareclient.Share.Enum.NotificationStatus;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Feedback.FeedbackType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.ButtonPersianView;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.View.Master.NotificationActivity;
import ir.rayas.app.citywareclient.ViewModel.Notification.NotificationAttachmentsViewModel;
import ir.rayas.app.citywareclient.ViewModel.Notification.NotificationItemStatusOutViewModel;
import ir.rayas.app.citywareclient.ViewModel.Notification.NotificationListViewModel;
import ir.rayas.app.citywareclient.ViewModel.Notification.NotificationViewModel;
import ir.rayas.app.citywareclient.ViewModel.Order.ProductViewModel;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by Hajar on 1/3/2019.
 */


public class NotificationExpandableListAdapter extends BaseExpandableListAdapter implements View.OnClickListener {

    private NotificationActivity Context;
    private ArrayList<NotificationViewModel> ViewModelList;

    public static boolean IsDeleteNotification;
    public static int NotificationId;

    private DownloadManager downloadManager;
    private long progressLoading;

    private List<ProgressBar> LoadingProgressBar;
    private List<ImageView> IconAttach;

    private  Map<Integer, Long> myMap = new HashMap<>();
    private boolean IsVideoController = true;


    public NotificationExpandableListAdapter() {

    }


    public NotificationExpandableListAdapter(NotificationActivity context, List<NotificationViewModel> NotificationModel) {
        this.Context = context;
        this.ViewModelList = new ArrayList<>();
        this.ViewModelList.addAll(NotificationModel);
    }

    @Override
    public int getGroupCount() {
        return this.ViewModelList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.ViewModelList.get(groupPosition);
    }


    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.ViewModelList.get(groupPosition);
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        NotificationViewModel NotificationModel = (NotificationViewModel) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater Inflater = (LayoutInflater) this.Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = Inflater.inflate(R.layout.expandable_list_view_notification_child, null);
        }

        TextViewPersian MessageNotificationTextView = convertView.findViewById(R.id.MessageNotificationTextView);
        ImageView ImageNotificationImageView = convertView.findViewById(R.id.ImageNotificationImageView);
        VideoView FilmNotificationVideoView = convertView.findViewById(R.id.FilmNotificationVideoView);
        LinearLayout DownloadAttachmentLinearLayout = convertView.findViewById(R.id.DownloadAttachmentLinearLayout);
        LinearLayout LineBetweenMessageAndAttachLinearLayout = convertView.findViewById(R.id.LineBetweenMessageAndAttachLinearLayout);
        FrameLayout ControllerAnchorFrameLayout = convertView.findViewById(R.id.ControllerAnchorFrameLayout);
        final MediaController VidControl = new MediaController(Context);


        LineBetweenMessageAndAttachLinearLayout.getLayoutParams().width = LayoutUtility.GetWidthAccordingToScreen(Context, 2);

        ChangeNotificationStatus(NotificationModel, NotificationStatus.Read.getId(), false);

        MessageNotificationTextView.setText(NotificationModel.getMessage());

        ImageNotificationImageView.getLayoutParams().width = LayoutUtility.GetWidthAccordingToScreen(Context, 1);
        ImageNotificationImageView.getLayoutParams().height = LayoutUtility.GetWidthAccordingToScreen(Context, 2);

        FilmNotificationVideoView.getLayoutParams().width = LayoutUtility.GetWidthAccordingToScreen(Context, 1);
        FilmNotificationVideoView.getLayoutParams().height = LayoutUtility.GetWidthAccordingToScreen(Context, 2);

        String uri = "";
        String Type = "";

        ArrayList<NotificationAttachmentsViewModel> attachmentsViewModel = new ArrayList<>();
        attachmentsViewModel.clear();
        Gson gson = new Gson();
        Type listType = new TypeToken<List<NotificationAttachmentsViewModel>>() {
        }.getType();
        attachmentsViewModel = gson.fromJson(NotificationModel.getAttachments(), listType);

        if (attachmentsViewModel != null) {
            if (attachmentsViewModel.size() > 0) {
                DownloadAttachmentLinearLayout.setVisibility(View.VISIBLE);
                LineBetweenMessageAndAttachLinearLayout.setVisibility(View.VISIBLE);

                boolean isDefaultImage = false;
                boolean isDefaultVideo = false;

                for (int i = 0; i < attachmentsViewModel.size(); i++) {

                    if (attachmentsViewModel.get(i).isDefault()) {
                        if (attachmentsViewModel.get(i).getFileType() == FileType.Image.getId()) {
                            uri = attachmentsViewModel.get(i).getUrl();
                            isDefaultImage = true;
                            Type = "Image";
                            break;
                        }
                    }
                }
                if (!isDefaultImage) {
                    for (int i = 0; i < attachmentsViewModel.size(); i++) {

                        if (attachmentsViewModel.get(i).isDefault()) {
                            if (attachmentsViewModel.get(i).getFileType() == FileType.Video.getId()) {
                                uri = attachmentsViewModel.get(i).getUrl();
                                isDefaultVideo = true;
                                Type = "video";
                                break;
                            }
                        }
                    }
                }

                if (!isDefaultImage && !isDefaultVideo) {
                    for (int i = 0; i < attachmentsViewModel.size(); i++) {
                        if (attachmentsViewModel.get(i).getFileType() == FileType.Image.getId()) {
                            uri = attachmentsViewModel.get(i).getUrl();
                            Type = "Image";
                            break;
                        }
                    }
                }

                if (!isDefaultImage && !isDefaultVideo) {
                    if (Type.equals("")) {
                        for (int i = 0; i < attachmentsViewModel.size(); i++) {
                            if (attachmentsViewModel.get(i).getFileType() == FileType.Video.getId()) {
                                uri = attachmentsViewModel.get(i).getUrl();
                                Type = "video";
                                break;
                            }
                        }
                    }
                }

                DownloadAttachmentLinearLayout.removeAllViews();
                LoadingProgressBar = new ArrayList<>();
                IconAttach = new ArrayList<>();

                IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                Context.registerReceiver(downloadReceiver, filter);

                for (int i = 0; i < attachmentsViewModel.size(); i++) {

                    if (!attachmentsViewModel.get(i).getUrl().equals("")) {

                        LinearLayout.LayoutParams LayoutParamsBase = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80);
                        LayoutParamsBase.setMargins(0, 8, 0, 0);
                        LinearLayout LinearLayoutBase = new LinearLayout(Context);
                        LinearLayoutBase.setBackgroundResource(R.drawable.selector_button_theme_style);
                        LinearLayoutBase.setGravity(Gravity.CENTER);
                        LinearLayoutBase.setOrientation(LinearLayout.VERTICAL);

                        LinearLayout.LayoutParams LayoutParamsAttachAndDownload = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        LinearLayout LinearLayoutAttachAndDownload = new LinearLayout(Context);
                        LinearLayoutAttachAndDownload.setGravity(Gravity.CENTER);
                        LinearLayoutAttachAndDownload.setOrientation(LinearLayout.HORIZONTAL);

                        LinearLayout.LayoutParams LayoutParamsTitleAttachDownload = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        TextView TitleAttachDownload = new TextView(Context);
                        TitleAttachDownload.setText(" دانلود " + attachmentsViewModel.get(i).getTitle());
                        TitleAttachDownload.setTextColor(Context.getResources().getColor(R.color.FontWhiteColor));
                        TitleAttachDownload.setTextSize(15);
                        TitleAttachDownload.setTypeface(Font.MasterFont);
                        LinearLayoutAttachAndDownload.addView(TitleAttachDownload, LayoutParamsTitleAttachDownload);

                        LinearLayout.LayoutParams LayoutParamsIconAttachDownload = new LinearLayout.LayoutParams(60, 60);
                        LayoutParamsIconAttachDownload.setMargins(8, 2, 8, 2);
                        ImageView IconAttachDownload = new ImageView(Context);
                        IconAttachDownload.setId(i);
                        IconAttach.add(IconAttachDownload);

                        ProgressBar loadingProgressBar = new ProgressBar(Context);
                        loadingProgressBar.setId(i);
                        loadingProgressBar.setIndeterminateDrawable(Context.getResources().getDrawable(R.drawable.view_loading_white_style));
                        LoadingProgressBar.add(loadingProgressBar);


                        if (attachmentsViewModel.get(i).getFileType() == FileType.Image.getId()) {
                            IconAttachDownload.setBackgroundResource(R.drawable.ic_photo_white_24dp);

                        } else if (attachmentsViewModel.get(i).getFileType() == FileType.Video.getId()) {
                            IconAttachDownload.setBackgroundResource(R.drawable.ic_video_white_24dp);

                        } else if (attachmentsViewModel.get(i).getFileType() == FileType.Sound.getId()) {
                            IconAttachDownload.setBackgroundResource(R.drawable.ic_sound_white_24dp);

                        } else {
                            IconAttachDownload.setBackgroundResource(R.drawable.ic_file_white_24dp);
                        }

                        IconAttachDownload.setAdjustViewBounds(true);
                        IconAttachDownload.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        LinearLayoutAttachAndDownload.addView(IconAttachDownload, LayoutParamsIconAttachDownload);

                        loadingProgressBar.setVisibility(View.GONE);
                        LinearLayoutAttachAndDownload.addView(loadingProgressBar, LayoutParamsIconAttachDownload);

                        LinearLayoutBase.addView(LinearLayoutAttachAndDownload, LayoutParamsAttachAndDownload);

                        DownloadAttachmentLinearLayout.addView(LinearLayoutBase, LayoutParamsBase);
                        Click(LinearLayoutBase, attachmentsViewModel.get(i).getTitle(), attachmentsViewModel.get(i).getUrl(), IconAttachDownload, LoadingProgressBar.get(i));

                    }
                }

                if (uri.equals("")) {
                    ImageNotificationImageView.setVisibility(View.GONE);
                    FilmNotificationVideoView.setVisibility(View.GONE);
                } else {
                    if (Type.equals("Image")) {
                        ImageNotificationImageView.setVisibility(View.VISIBLE);
                        FilmNotificationVideoView.setVisibility(View.GONE);
                        LayoutUtility.LoadImageWithGlide(Context, uri, ImageNotificationImageView, LayoutUtility.GetWidthAccordingToScreen((Activity) Context, 1), LayoutUtility.GetWidthAccordingToScreen((Activity) Context, 2));

                    } else {
                        ImageNotificationImageView.setVisibility(View.GONE);
                        FilmNotificationVideoView.setVisibility(View.VISIBLE);

                        String videoUrl = uri;


                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        lp.gravity = Gravity.BOTTOM;
                        VidControl.setLayoutParams(lp);

                        ((ViewGroup) VidControl.getParent()).removeView(VidControl);
                        ControllerAnchorFrameLayout.addView(VidControl);

                        Uri vidUri = Uri.parse(videoUrl);
                        FilmNotificationVideoView.setVideoURI(vidUri);
                        FilmNotificationVideoView.start();

                        VidControl.setAnchorView(FilmNotificationVideoView);
                        FilmNotificationVideoView.setMediaController(VidControl);


                    }
                }


            } else {
                FilmNotificationVideoView.setVisibility(View.GONE);
                ImageNotificationImageView.setVisibility(View.GONE);
                DownloadAttachmentLinearLayout.setVisibility(View.GONE);
                LineBetweenMessageAndAttachLinearLayout.setVisibility(View.GONE);
            }
        } else {
            FilmNotificationVideoView.setVisibility(View.GONE);
            ImageNotificationImageView.setVisibility(View.GONE);
            DownloadAttachmentLinearLayout.setVisibility(View.GONE);
            LineBetweenMessageAndAttachLinearLayout.setVisibility(View.GONE);
        }




        FilmNotificationVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IsVideoController){
                    VidControl.setVisibility(View.GONE);
                    IsVideoController = false;
                } else {
                    VidControl.setVisibility(View.VISIBLE);
                    IsVideoController = true;
                }


            }
        });

        return convertView;
    }

    @Override
    public void onClick(View view) {

    }

    private void Click(View view, final String Title, final String Link, final ImageView IconAttachDownload, final ProgressBar LoadingProgressBar) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadingProgressBar.setVisibility(View.VISIBLE);
                int id = LoadingProgressBar.getId();
                IconAttachDownload.setVisibility(View.GONE);
                Uri uri = Uri.parse(Link);
                progressLoading = DownloadData(uri, Link, Title, IconAttachDownload, LoadingProgressBar);
                myMap.put(id, progressLoading);
            }
        });
    }


    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater Inflater = (LayoutInflater) this.Context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = Inflater.inflate(R.layout.expandable_list_view_notification_group, null);
        }


        TextViewPersian TitleNotificationTextView = convertView.findViewById(R.id.TitleNotificationTextView);
        TextViewPersian DateTimeNotificationTextView = convertView.findViewById(R.id.DateTimeNotificationTextView);
        TextViewPersian DeleteIconNotificationTextView = convertView.findViewById(R.id.DeleteIconNotificationTextView);
        TextViewPersian AttachIconNotificationTextView = convertView.findViewById(R.id.AttachIconNotificationTextView);
        LinearLayout ExpandableListNotificationGroupLinearLayout = convertView.findViewById(R.id.ExpandableListNotificationGroupLinearLayout);

        if (ViewModelList.get(groupPosition).getStatus() == NotificationStatus.Read.getId())
            ExpandableListNotificationGroupLinearLayout.setBackgroundColor(Context.getResources().getColor(R.color.BackgroundWhiteColor));
        else
            ExpandableListNotificationGroupLinearLayout.setBackgroundColor(Context.getResources().getColor(R.color.BackgroundColorLightLayout));


        TitleNotificationTextView.setText(ViewModelList.get(groupPosition).getTitle());

        if (ViewModelList.get(groupPosition).getCreate() == null) {
            DateTimeNotificationTextView.setText(ViewModelList.get(groupPosition).getSendDate());
        } else {
            if (ViewModelList.get(groupPosition).getCreate().equals("")) {
                DateTimeNotificationTextView.setText(ViewModelList.get(groupPosition).getSendDate());
            } else {
                DateTimeNotificationTextView.setText(ViewModelList.get(groupPosition).getCreate());
            }
        }


        DeleteIconNotificationTextView.setTypeface(Font.MasterIcon);
        DeleteIconNotificationTextView.setText("\uf014");

        AttachIconNotificationTextView.setTypeface(Font.MasterIcon);
        AttachIconNotificationTextView.setText("\uf0c6");


        Gson gson = new Gson();
        ArrayList<NotificationAttachmentsViewModel> attachmentsViewModel = new ArrayList<>();
        Type listType = new TypeToken<List<NotificationAttachmentsViewModel>>() {
        }.getType();
        attachmentsViewModel = gson.fromJson(ViewModelList.get(groupPosition).getAttachments(), listType);

        int CountAttachment = 0;

        if (attachmentsViewModel != null) {
            if (attachmentsViewModel.size() > 0) {
                for (int i = 0; i < attachmentsViewModel.size(); i++) {
                    if (!attachmentsViewModel.get(i).getUrl().equals("")) {
                        CountAttachment = CountAttachment + 1;
                    }
                }
            }

            if (CountAttachment > 0) {
                AttachIconNotificationTextView.setVisibility(View.VISIBLE);
            } else {
                AttachIconNotificationTextView.setVisibility(View.GONE);
            }
        } else {
            AttachIconNotificationTextView.setVisibility(View.GONE);
        }


        DeleteIconNotificationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowNotificationDeleteDialog(groupPosition);

            }
        });


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    private void ChangeNotificationStatus(NotificationViewModel NotificationModel, int Status, boolean IsDelete) {
        AccountRepository ARepository = new AccountRepository(null);
        AccountViewModel AccountViewModel = ARepository.getAccount();
        IsDeleteNotification = IsDelete;
        NotificationId = NotificationModel.getId();


        NotificationItemStatusOutViewModel notificationItemStatusOutViewModel = new NotificationItemStatusOutViewModel();
        notificationItemStatusOutViewModel.setUserId(AccountViewModel.getUser().getId());
        notificationItemStatusOutViewModel.setNotificationItemId(NotificationModel.getId());
        notificationItemStatusOutViewModel.setNotificationStatus(Status);

        NotificationService notificationService = new NotificationService(Context);
        notificationService.Set(notificationItemStatusOutViewModel);
    }


    private long DownloadData(Uri uri, String Link, String Title, final ImageView iconAttachDownload, final ProgressBar loadingProgressBar) {

        long downloadReference = 0;
        DownloadManager.Request request;

        try {
            downloadManager = (DownloadManager) Context.getSystemService(DOWNLOAD_SERVICE);
            request = new DownloadManager.Request(uri);


            int dot = Link.lastIndexOf(".");
            String parts = Link.substring(dot + 1);
            String sunPath = Title + "." + parts;
            request.setDestinationInExternalFilesDir(Context, Environment.DIRECTORY_DOWNLOADS, sunPath);


            downloadReference = downloadManager.enqueue(request);

        } catch (Exception e) {
            Context.ShowToast(Context.getResources().getString(R.string.not_exit_file), Toast.LENGTH_LONG, MessageType.Warning);
            loadingProgressBar.setVisibility(View.GONE);
            iconAttachDownload.setVisibility(View.VISIBLE);
        }


        return downloadReference;

    }

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (referenceId == progressLoading) {
                Context.ShowToast(Context.getResources().getString(R.string.complete_download), Toast.LENGTH_LONG, MessageType.Info);

                Iterator myVeryOwnIterator = myMap.keySet().iterator();
                while (myVeryOwnIterator.hasNext()) {
                    int key = (int) myVeryOwnIterator.next();
                    Long value = (Long) myMap.get(key);

                    if (value == referenceId) {
                        LoadingProgressBar.get(key).setVisibility(View.GONE);
                        IconAttach.get(key).setVisibility(View.VISIBLE);
                    }

                }
            }

        }
    };

    private void ShowNotificationDeleteDialog(final int GroupPosition) {

        final Dialog NotificationDeleteDialog = new Dialog(Context);
        NotificationDeleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        NotificationDeleteDialog.setContentView(R.layout.dialog_yes_no_question);

        ButtonPersianView DialogBusinessDeleteOkButton = NotificationDeleteDialog.findViewById(R.id.DialogYesNoQuestionOkButton);
        ButtonPersianView DialogBusinessDeleteCancelButton = NotificationDeleteDialog.findViewById(R.id.DialogYesNoQuestionCancelButton);
        TextViewPersian DialogYesNoQuestionTitleTextView = NotificationDeleteDialog.findViewById(R.id.DialogYesNoQuestionTitleTextView);
        TextViewPersian DialogYesNoQuestionDescriptionTextView = NotificationDeleteDialog.findViewById(R.id.DialogYesNoQuestionDescriptionTextView);
        TextViewPersian DialogYesNoQuestionWarningTextView = NotificationDeleteDialog.findViewById(R.id.DialogYesNoQuestionWarningTextView);

        DialogYesNoQuestionTitleTextView.setText(Context.getResources().getString(R.string.notification_delete));
        DialogYesNoQuestionDescriptionTextView.setText(Context.getResources().getString(R.string.description_notification_delete));
        DialogYesNoQuestionWarningTextView.setText("");

        DialogBusinessDeleteOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationDeleteDialog.dismiss();

                ChangeNotificationStatus(ViewModelList.get(GroupPosition), NotificationStatus.Deleted.getId(), true);

            }
        });

        DialogBusinessDeleteCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationDeleteDialog.dismiss();
            }
        });

        NotificationDeleteDialog.show();

    }
}