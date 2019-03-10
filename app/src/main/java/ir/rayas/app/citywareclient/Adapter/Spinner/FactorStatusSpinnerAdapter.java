package ir.rayas.app.citywareclient.Adapter.Spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ir.rayas.app.citywareclient.Adapter.ViewModel.FactorStatusAdapterViewModel;
import ir.rayas.app.citywareclient.R;

/**
 * Created by Hajar on 3/2/2019.
 */

public class FactorStatusSpinnerAdapter extends BaseAdapter {

    Context context;
    List<FactorStatusAdapterViewModel> ColorTypeViewModels;
    LayoutInflater inflter;

    public FactorStatusSpinnerAdapter(Context applicationContext, List<FactorStatusAdapterViewModel> colorTypeViewModels) {
        this.context = applicationContext;
        this.ColorTypeViewModels = colorTypeViewModels;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return ColorTypeViewModels.size();
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

        ContentSpinnerTextViewPersian.setText(ColorTypeViewModels.get(i).getTitle());
        return view;
    }
}
