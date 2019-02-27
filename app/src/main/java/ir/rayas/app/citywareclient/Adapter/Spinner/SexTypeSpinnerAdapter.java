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
import ir.rayas.app.citywareclient.ViewModel.Definition.SexTypeViewModel;

/**
 * Created by Hajar on 7/29/2018.
 */

public class SexTypeSpinnerAdapter extends BaseAdapter {

    Context context;
    List<SexTypeViewModel> SexTypeViewModel;
    LayoutInflater inflter;

    public SexTypeSpinnerAdapter(Context applicationContext,  List<SexTypeViewModel> sexTypeViewModels) {
        this.context = applicationContext;
        this.SexTypeViewModel = sexTypeViewModels;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return SexTypeViewModel.size();
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

        ContentSpinnerTextViewPersian.setText(SexTypeViewModel.get(i).getTitle());
        return view;
    }
}
