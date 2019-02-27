package ir.rayas.app.citywareclient.Adapter.Spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.ViewModel.Definition.ContactTypeViewModel;
import ir.rayas.app.citywareclient.ViewModel.Definition.SexTypeViewModel;

/**
 * Created by Hajar on 8/24/2018.
 */

public class ContactTypeSpinnerAdapter extends BaseAdapter {

    Context context;
    List<ContactTypeViewModel> contactTypeViewModels;
    LayoutInflater inflter;

    public ContactTypeSpinnerAdapter(Context applicationContext,  List<ContactTypeViewModel> contactTypeViewModel) {
        this.context = applicationContext;
        this.contactTypeViewModels = contactTypeViewModel;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return contactTypeViewModels.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_items, null);

        ImageView ArrowDropDownImageView = view.findViewById(R.id.ArrowDropDownImageView);
        TextView ContentSpinnerTextViewPersian = view.findViewById(R.id.ContentSpinnerTextViewPersian);

        ArrowDropDownImageView.setVisibility(View.INVISIBLE);
        ArrowDropDownImageView.setTag(i);

        ContentSpinnerTextViewPersian.setText(contactTypeViewModels.get(i).getTitle());
        return view;
    }
}
