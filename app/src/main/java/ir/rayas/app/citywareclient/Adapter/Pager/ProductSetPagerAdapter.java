package ir.rayas.app.citywareclient.Adapter.Pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import java.util.HashMap;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.Fragment.Product.ProductFragment;
import ir.rayas.app.citywareclient.View.Fragment.Product.ProductImageFragment;


/**
 * Created by Hajar on 8/30/2018.
 */

public class ProductSetPagerAdapter extends FragmentPagerAdapter {

    private String[] TabNames = null;
    private HashMap<Integer, Fragment> FragmentHashMap     = new HashMap<>();
    private HashMap<Integer,ILoadData> ILoadDataHashMap    = new HashMap<>();

    public ProductSetPagerAdapter(FragmentManager fm, String[] TabNames) {
        super(fm);
        this.TabNames = TabNames;
    }

    @Override
    public Fragment getItem(int index) {
        switch (index)
        {
            case 0:
                if (FragmentHashMap.get(0)==null) {
                    ProductImageFragment productImageFragment = new ProductImageFragment();
                    FragmentHashMap.put(0, productImageFragment);
                  //  ILoadDataHashMap.put(0,productImageFragment);
                }
                return FragmentHashMap.get(0);
            case 1:
                if (FragmentHashMap.get(1)==null) {
                    ProductFragment productFragment = new ProductFragment();
                    FragmentHashMap.put(1, productFragment);
                    ILoadDataHashMap.put(1,productFragment);
                }
                return FragmentHashMap.get(1);

        }
        return null;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        FragmentHashMap.remove(position);
    }

    public  Fragment getFragmentByIndex(int index)
    {
        if (FragmentHashMap !=null && FragmentHashMap.size()>0)
            return FragmentHashMap.get(index);
        else
            return null;
    }

    public ILoadData getLoadDataByIndex(int index)
    {
        if (ILoadDataHashMap !=null && ILoadDataHashMap.size()>0)
            return ILoadDataHashMap.get(index);
        else
            return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TabNames[position];
    }

    @Override
    public int getCount() {
        return 2;
    }
}
