package ir.rayas.app.citywareclient.Adapter.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Enum.ContactType;
import ir.rayas.app.citywareclient.Share.Feedback.MessageType;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Share.UserFactorDetailActivity;
import ir.rayas.app.citywareclient.ViewModel.Business.BusinessContactViewModel;

/**
 * Created by Hajar on 3/2/2019.
 */

public class UserFactorBusinessContactRecyclerViewAdapter extends RecyclerView.Adapter<UserFactorBusinessContactRecyclerViewAdapter.ViewHolder> {

    private List<BusinessContactViewModel> ViewModelList = null;
    private RecyclerView Container = null;
    private UserFactorDetailActivity Context;

    public UserFactorBusinessContactRecyclerViewAdapter(UserFactorDetailActivity context, List<BusinessContactViewModel> ViewModel, RecyclerView Container) {
        this.Context = context;
        this.Container = Container;
        this.ViewModelList = ViewModel;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextViewPersian ContentTextViewBusinessContact;
        public TextViewPersian TitleTextViewBusinessContact;
        public TextViewPersian IconTypeContactTextViewBusinessContact;
        public LinearLayout BusinessContactContainerLinearLayout;


        public ViewHolder(View v) {
            super(v);

            ContentTextViewBusinessContact = v.findViewById(R.id.ContentTextViewBusinessContact);
            TitleTextViewBusinessContact = v.findViewById(R.id.TitleTextViewBusinessContact);
            IconTypeContactTextViewBusinessContact = v.findViewById(R.id.IconTypeContactTextViewBusinessContact);
            BusinessContactContainerLinearLayout = v.findViewById(R.id.BusinessContactContainerLinearLayout);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View CurrentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_dialog_contact, parent, false);
        ViewHolder CurrentViewHolder = new ViewHolder(CurrentView);
        return CurrentViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        holder.ContentTextViewBusinessContact.setText(ViewModelList.get(position).getValue());
        holder.TitleTextViewBusinessContact.setText(ViewModelList.get(position).getTitle());
        holder.IconTypeContactTextViewBusinessContact.setTypeface(Font.MasterIcon);

        if (ViewModelList.get(position).getContactType() == ContactType.Phone.GetContactType()) {
            holder.IconTypeContactTextViewBusinessContact.setText("\uf095");

        } else if (ViewModelList.get(position).getContactType() == ContactType.CellPhone.GetContactType()) {
            holder.IconTypeContactTextViewBusinessContact.setText("\uf10b");

        } else if (ViewModelList.get(position).getContactType() == ContactType.Fax.GetContactType()) {
            holder.IconTypeContactTextViewBusinessContact.setText("\uf1ac");

        } else if (ViewModelList.get(position).getContactType() == ContactType.Email.GetContactType()) {
            holder.IconTypeContactTextViewBusinessContact.setText("\uf003");

        } else if (ViewModelList.get(position).getContactType() == ContactType.Website.GetContactType()) {
            holder.IconTypeContactTextViewBusinessContact.setText("\uf26b");

        } else if (ViewModelList.get(position).getContactType() == ContactType.Instagram.GetContactType()) {
            holder.IconTypeContactTextViewBusinessContact.setText("\uf16d");

        } else if (ViewModelList.get(position).getContactType() == ContactType.WhatsApp.GetContactType()) {
            holder.IconTypeContactTextViewBusinessContact.setText("\uf232");

        } else if (ViewModelList.get(position).getContactType() == ContactType.Telegram.GetContactType()) {
            holder.IconTypeContactTextViewBusinessContact.setText("\uf1d8");

        } else if (ViewModelList.get(position).getContactType() == ContactType.Facebook.GetContactType()) {
            holder.IconTypeContactTextViewBusinessContact.setText("\uf082");

        } else if (ViewModelList.get(position).getContactType() == ContactType.LinkedIn.GetContactType()) {
            holder.IconTypeContactTextViewBusinessContact.setText("\uf08c");

        } else if (ViewModelList.get(position).getContactType() == ContactType.GooglePlus.GetContactType()) {
            holder.IconTypeContactTextViewBusinessContact.setText("\uf0d4");

        } else if (ViewModelList.get(position).getContactType() == ContactType.Skype.GetContactType()) {
            holder.IconTypeContactTextViewBusinessContact.setText("\uf17e");

        } else {
            holder.IconTypeContactTextViewBusinessContact.setText("\uf141");
        }

        holder.BusinessContactContainerLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenActionContactType(ViewModelList.get(position).getValue(), ViewModelList.get(position).getContactType());
            }
        });
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

    private void OpenActionContactType(String Value, int contactType) {

        if (contactType == ContactType.Phone.GetContactType()) {
            Call(Context, Value);

        } else if (contactType == ContactType.CellPhone.GetContactType()) {
            Call(Context, Value);

        } else if (contactType == ContactType.Fax.GetContactType()) {
            Call(Context, Value);

        } else if (contactType == ContactType.Email.GetContactType()) {
            Intent intentEmail = new Intent(Intent.ACTION_SEND);
            intentEmail.setType("message/rfc822");
            intentEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{Value});
            intentEmail.putExtra(Intent.EXTRA_SUBJECT, "CreateDataBases");
            intentEmail.putExtra(Intent.EXTRA_TEXT, "");
            try {
                Context.startActivity(Intent.createChooser(intentEmail, Context.getResources().getString(R.string.sending_email)));
            } catch (ActivityNotFoundException ex) {
                Context.ShowToast(Context.getResources().getString(R.string.not_email_address), Toast.LENGTH_LONG, MessageType.Error);
            }

        } else if (contactType == ContactType.Website.GetContactType()) {
            OpenURL(Context, Value);

        } else if (contactType == ContactType.Instagram.GetContactType()) {
            OpenURL(Context, Value);

        } else if (contactType == ContactType.WhatsApp.GetContactType()) {
            OpenURL(Context, Value);

        } else if (contactType == ContactType.Telegram.GetContactType()) {
            OpenURL(Context, Value);

        } else if (contactType == ContactType.Facebook.GetContactType()) {
            OpenURL(Context, Value);

        } else if (contactType == ContactType.LinkedIn.GetContactType()) {
            OpenURL(Context, Value);

        } else if (contactType == ContactType.GooglePlus.GetContactType()) {
            OpenURL(Context, Value);

        } else if (contactType == ContactType.Skype.GetContactType()) {
            OpenURL(Context, Value);
        }

    }

    private void Call(android.content.Context CurrentContext, String PhoneNumber) {
        String url = "tel:" + PhoneNumber;
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
        CurrentContext.startActivity(intent);
    }

    private void OpenURL(Context CurrentContext, String url) {
        try {

            if (!url.contains("http://"))
                url = "http://" + url;

            Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
            Intent SocialIntent = new Intent(Intent.ACTION_VIEW, uri);
            CurrentContext.startActivity(SocialIntent);


        } catch (Exception e) {
            Context.ShowToast(Context.getResources().getString(R.string.not_valid_link), Toast.LENGTH_LONG, MessageType.Error);
        }

    }
}

