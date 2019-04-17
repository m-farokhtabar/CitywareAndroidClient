package ir.rayas.app.citywareclient.Adapter.ListView;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Layout.View.TextViewPersian;
import ir.rayas.app.citywareclient.View.Share.UserSearchActivity;
import ir.rayas.app.citywareclient.ViewModel.Search.OutUserSearchViewModel;


public class OfferSearchListViewAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<OutUserSearchViewModel> ViewModelList = null;
    private UserSearchActivity Context;


    public OfferSearchListViewAdapter(UserSearchActivity context, List<OutUserSearchViewModel> viewModel) {
        super();
        this.ViewModelList = viewModel;
        this.Context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return ViewModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.list_view_offer_search, null);

        TextViewPersian UserNameSearchTextView = convertView.findViewById(R.id.UserNameSearchTextView);
        TextViewPersian NickNameSearchTextView = convertView.findViewById(R.id.NickNameSearchTextView);
        String Name = ViewModelList.get(position).getName();
        String Family = ViewModelList.get(position).getFamily();
        String UserName = Name + " " + Family;

        UserNameSearchTextView.setText(UserName);
        UserNameSearchTextView.setTag(ViewModelList.get(position).getId());
        NickNameSearchTextView.setText(ViewModelList.get(position).getNickName());

        return convertView;
    }

}