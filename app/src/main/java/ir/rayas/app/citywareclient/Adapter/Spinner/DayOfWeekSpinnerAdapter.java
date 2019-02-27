package ir.rayas.app.citywareclient.Adapter.Spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ir.rayas.app.citywareclient.R;

/**
 * Created by Hajar on 8/25/2018.
 */

public class DayOfWeekSpinnerAdapter  extends BaseAdapter {

    Context context;
    String[] DayOfWeekViewModel;
    LayoutInflater inflter;

    public DayOfWeekSpinnerAdapter(Context applicationContext,  String[] DayOfWeekViewModels) {
        this.context = applicationContext;
        this.DayOfWeekViewModel = DayOfWeekViewModels;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return DayOfWeekViewModel.length;
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

        ContentSpinnerTextViewPersian.setText(DayOfWeekViewModel[i]);
        return view;
    }
}
