package ir.rayas.app.citywareclient.Adapter.Pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;

import ir.rayas.app.citywareclient.View.Fragment.HowToSearchInAppGpsOrRegion.GpsDialogSearchFragment;
import ir.rayas.app.citywareclient.View.Fragment.HowToSearchInAppGpsOrRegion.SelectRegionFragment;

/** آداپتور مربوط به کنترل فرگمنت در انتخاب منطقه یا GPs
 * Created by Programmer on 2/24/2018.
 */

public class HowToSearchInAppGpsOrRegionPagerAdapter extends FragmentPagerAdapter {

    private HashMap<Integer,Fragment> FragmentHashMap = new HashMap<>();

    public HowToSearchInAppGpsOrRegionPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int index)
    {
        switch (index)
        {
            case 0:
                if (FragmentHashMap.get(0)==null)
                    FragmentHashMap.put(0,new GpsDialogSearchFragment());
                return FragmentHashMap.get(0);
            case 1:
                if (FragmentHashMap.get(1)==null)
                    FragmentHashMap.put(1,new SelectRegionFragment());
                return FragmentHashMap.get(1);
        }

        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        FragmentHashMap.remove(position);
    }

    public android.support.v4.app.Fragment getFragmentByIndex(int index)
    {
        if (FragmentHashMap !=null && FragmentHashMap.size()>0)
            return FragmentHashMap.get(index);
        else
            return null;
    }


    @Override
    public int getCount()
    {
        return 2;
    }
}
