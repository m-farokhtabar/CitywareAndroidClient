package ir.rayas.app.citywareclient.Adapter.Pager;

/**
 * Created by Macs on 12/9/2018.
 */

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Utility.LayoutUtility;


public class ShowImageProductDetailsPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<String> ImagePathProductDetails = new ArrayList<String>();

    public ShowImageProductDetailsPagerAdapter(Context context, ArrayList<String> imagePathProductDetails) {
        this.context = context;
        this.ImagePathProductDetails = imagePathProductDetails;
    }

    @Override
    public int getCount() {
        return ImagePathProductDetails.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.view_pager_show_product_details_image, null);
        ImageView ImageProductDetails = view.findViewById(R.id.ImageProductDetailsImageViewShowProductDetailsActivity);

        int width = LayoutUtility.GetWidthAccordingToScreen((Activity) context, 1);
        int height = LayoutUtility.GetWidthAccordingToScreen((Activity) context, 2);

        ImageProductDetails.getLayoutParams().width = width;
        ImageProductDetails.getLayoutParams().height = height;


        LayoutUtility.LoadImageWithGlide(context, ImagePathProductDetails.get(position), ImageProductDetails, width, height);


        ViewPager ViewPagerProductDetails = (ViewPager) container;
        ViewPagerProductDetails.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager ViewPagerProductDetails = (ViewPager) container;
        View view = (View) object;
        ViewPagerProductDetails.removeView(view);

    }
}