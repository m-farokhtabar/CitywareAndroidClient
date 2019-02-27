package ir.rayas.app.citywareclient.Adapter.Pager;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;

import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.Fragment.Business.BusinessFragment;
import ir.rayas.app.citywareclient.View.Fragment.Business.BusinessContactFragment;
import ir.rayas.app.citywareclient.View.Fragment.Business.ProductListFragment;
import ir.rayas.app.citywareclient.View.Fragment.Business.BusinessOpenTimeFragment;

/**
 * Created by Hajar on 8/17/2018.
 */

public class UserBusinessSetPagerAdapter extends FragmentPagerAdapter {

    private String[] TabNames = null;
    private HashMap<Integer, android.support.v4.app.Fragment>    FragmentHashMap   = new HashMap<>();
    private HashMap<Integer,ILoadData> ILoadDataHashMap    = new HashMap<>();

    public UserBusinessSetPagerAdapter(FragmentManager fm, String[] TabNames) {
        super(fm);
        this.TabNames = TabNames;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int index) {
        switch (index) {
            case 0:
                if (FragmentHashMap.get(0) == null) {
                    BusinessOpenTimeFragment businessOpenTimeFragment = new BusinessOpenTimeFragment();
                    FragmentHashMap.put(0, businessOpenTimeFragment);
                    ILoadDataHashMap.put(0, businessOpenTimeFragment);
                }
                return FragmentHashMap.get(0);
            case 1:
                if (FragmentHashMap.get(1) == null) {
                    BusinessContactFragment ContactFragment = new BusinessContactFragment();
                    FragmentHashMap.put(1, ContactFragment);
                    ILoadDataHashMap.put(1, ContactFragment);
                }
                return FragmentHashMap.get(1);
            case 2:
                if (FragmentHashMap.get(2) == null) {
                    ProductListFragment productListFragment = new ProductListFragment();
                    FragmentHashMap.put(2, productListFragment);
                    ILoadDataHashMap.put(2, productListFragment);
                }
                return FragmentHashMap.get(2);

            case 3:
                if (FragmentHashMap.get(3) == null) {
                    BusinessFragment biBusinessFragment = new BusinessFragment();
                    FragmentHashMap.put(3, biBusinessFragment);
                    ILoadDataHashMap.put(3, biBusinessFragment);
                }
                return FragmentHashMap.get(3);
        }
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        FragmentHashMap.remove(position);
    }

    public  android.support.v4.app.Fragment getFragmentByIndex(int index)
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
        return 4;
    }
}