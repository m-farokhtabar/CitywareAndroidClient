package ir.rayas.app.citywareclient.Adapter.Pager;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;

import ir.rayas.app.citywareclient.View.Fragment.Discount.CustomerExpireDiscountFragment;
import ir.rayas.app.citywareclient.View.Fragment.Discount.CustomerNewDiscountFragment;
import ir.rayas.app.citywareclient.View.Fragment.Discount.CustomerUsageDiscountFragment;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;


public class DiscountPagerAdapter extends FragmentPagerAdapter {

    private String[] TabNames = null;
    private HashMap<Integer, Fragment> FragmentHashMap = new HashMap<>();
    private HashMap<Integer, ILoadData> ILoadDataHashMap = new HashMap<>();

    public DiscountPagerAdapter(FragmentManager fm, String[] TabNames) {
        super(fm);
        this.TabNames = TabNames;
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {

            case 2:
                if (FragmentHashMap.get(2) == null) {
                    CustomerNewDiscountFragment CurrentNewDiscount = new CustomerNewDiscountFragment();
                    FragmentHashMap.put(2, CurrentNewDiscount);
                    ILoadDataHashMap.put(2, CurrentNewDiscount);

                }
                return FragmentHashMap.get(2);
            case 1:
                if (FragmentHashMap.get(1) == null) {
                    CustomerUsageDiscountFragment CurrentUsageDiscountFragment = new CustomerUsageDiscountFragment();
                    FragmentHashMap.put(1, CurrentUsageDiscountFragment);
                    ILoadDataHashMap.put(1, CurrentUsageDiscountFragment);

                }
                return FragmentHashMap.get(1);
            case 0:
                if (FragmentHashMap.get(0) == null) {
                    CustomerExpireDiscountFragment currentCustomerExpireDiscountFragment = new CustomerExpireDiscountFragment();
                    FragmentHashMap.put(0, currentCustomerExpireDiscountFragment);
                    ILoadDataHashMap.put(0, currentCustomerExpireDiscountFragment);
                }
                return FragmentHashMap.get(0);
        }
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        FragmentHashMap.remove(position);
    }

    public Fragment getFragmentByIndex(int index) {
        if (FragmentHashMap != null && FragmentHashMap.size() > 0)
            return FragmentHashMap.get(index);
        else
            return null;
    }

    public ILoadData getLoadDataByIndex(int index) {
        if (ILoadDataHashMap != null && ILoadDataHashMap.size() > 0)
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
        return 3;
    }
}
