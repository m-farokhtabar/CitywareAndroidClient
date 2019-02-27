package ir.rayas.app.citywareclient.Adapter.Pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;

import ir.rayas.app.citywareclient.View.Fragment.Basket.BasketDeliveryFragment;
import ir.rayas.app.citywareclient.View.Fragment.Basket.BasketDetailsFragment;
import ir.rayas.app.citywareclient.View.Fragment.Basket.BasketItemListFragment;
import ir.rayas.app.citywareclient.View.Fragment.Basket.BasketListFragment;
import ir.rayas.app.citywareclient.View.Fragment.Basket.BasketSummeryFragment;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;


/**
 * Created by Hajar on 2/15/2019.
 */

public class BasketPagerAdapter extends FragmentPagerAdapter {

    private HashMap<Integer, Fragment> FragmentHashMap = new HashMap<>();
    private HashMap<Integer, ILoadData> ILoadDataHashMap = new HashMap<>();

    public BasketPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
//            case 4:
//                if (FragmentHashMap.get(4) == null) {
//                    BasketListFragment CurrentBasketListFragment = new BasketListFragment();
//                    FragmentHashMap.put(4, CurrentBasketListFragment);
//                    ILoadDataHashMap.put(4, CurrentBasketListFragment);
//                }
//                return FragmentHashMap.get(4);
//            case 3:
//                if (FragmentHashMap.get(3) == null) {
//                    BasketItemListFragment CurrentBasketItemListFragment = new BasketItemListFragment();
//                    FragmentHashMap.put(3, CurrentBasketItemListFragment);
//                    ILoadDataHashMap.put(3, CurrentBasketItemListFragment);
//                }
//                return FragmentHashMap.get(3);
//            case 2:
//                if (FragmentHashMap.get(2) == null) {
//                    BasketDetailsFragment CurrentBasketDetailsFragment = new BasketDetailsFragment();
//                    FragmentHashMap.put(2, CurrentBasketDetailsFragment);
//                    ILoadDataHashMap.put(2, CurrentBasketDetailsFragment);
//                }
//                return FragmentHashMap.get(2);
//            case 1:
//                if (FragmentHashMap.get(1) == null) {
//                    BasketDeliveryFragment CurrentBasketDeliveryFragment = new BasketDeliveryFragment();
//                    FragmentHashMap.put(1, CurrentBasketDeliveryFragment);
//                    ILoadDataHashMap.put(1, CurrentBasketDeliveryFragment);
//                }
//                return FragmentHashMap.get(1);
//            case 0:
//                if (FragmentHashMap.get(0) == null) {
//                    BasketSummeryFragment CurrentBasketSummeryFragment = new BasketSummeryFragment();
//                    FragmentHashMap.put(0, CurrentBasketSummeryFragment);
//                    ILoadDataHashMap.put(0, CurrentBasketSummeryFragment);
//                }
//                return FragmentHashMap.get(0);
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
    public int getCount() {
        return 5;
    }
}
