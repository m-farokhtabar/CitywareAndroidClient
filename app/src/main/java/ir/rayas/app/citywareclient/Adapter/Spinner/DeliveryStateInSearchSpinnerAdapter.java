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

/**
 * Created by Hajar on 10/11/2018.
 */

public class DeliveryStateInSearchSpinnerAdapter extends BaseAdapter {

    Context context;
    List<String> DeliveryStateInSearch;
    LayoutInflater inflter;

    public DeliveryStateInSearchSpinnerAdapter(Context applicationContext, List<String> deliveryStateInSearch) {
        this.context = applicationContext;
        this.DeliveryStateInSearch = deliveryStateInSearch;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return DeliveryStateInSearch.size();
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

        ContentSpinnerTextViewPersian.setText(DeliveryStateInSearch.get(i));
        return view;
    }
}
