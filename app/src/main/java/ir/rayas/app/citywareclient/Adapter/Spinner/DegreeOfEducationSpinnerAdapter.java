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
import ir.rayas.app.citywareclient.ViewModel.Definition.DegreeOfEducationViewModel;

/**
 * Created by Hajar on 8/2/2018.
 */

public class DegreeOfEducationSpinnerAdapter extends BaseAdapter {

    Context context;
    List<DegreeOfEducationViewModel> DegreeOfEducationViewModel;
    LayoutInflater inflter;

    public DegreeOfEducationSpinnerAdapter(Context applicationContext,  List<DegreeOfEducationViewModel> degreeOfEducationViewModels) {
        this.context = applicationContext;
        this.DegreeOfEducationViewModel = degreeOfEducationViewModels;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return DegreeOfEducationViewModel.size();
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

        ContentSpinnerTextViewPersian.setText(DegreeOfEducationViewModel.get(i).getTitle());
        return view;
    }
}

