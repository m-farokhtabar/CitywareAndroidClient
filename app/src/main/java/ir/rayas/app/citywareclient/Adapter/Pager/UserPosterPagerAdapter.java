package ir.rayas.app.citywareclient.Adapter.Pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;

import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.Fragment.Poster.BuyPosterTypeFragment;
import ir.rayas.app.citywareclient.View.Fragment.Poster.PosterTypeDetailsFragment;
import ir.rayas.app.citywareclient.View.Fragment.Poster.PosterTypeFragment;


public class UserPosterPagerAdapter  extends FragmentPagerAdapter {

    private String[] TabNames = null;
    private HashMap<Integer, Fragment> FragmentHashMap   = new HashMap<>();
    private HashMap<Integer,ILoadData> ILoadDataHashMap    = new HashMap<>();

    public UserPosterPagerAdapter(FragmentManager fm, String[] TabNames) {
        super(fm);
        this.TabNames = TabNames;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int index) {
        switch (index) {
            
            case 2:
                if (FragmentHashMap.get(2) == null) {
                    PosterTypeFragment posterTypeFragment = new PosterTypeFragment();
                    FragmentHashMap.put(2, posterTypeFragment);
                    ILoadDataHashMap.put(2, posterTypeFragment);
                }
                return FragmentHashMap.get(2);
            case 1:
                if (FragmentHashMap.get(1) == null) {
                    PosterTypeDetailsFragment posterTypeDetailsFragment = new PosterTypeDetailsFragment();
                    FragmentHashMap.put(1, posterTypeDetailsFragment);
                    ILoadDataHashMap.put(1, posterTypeDetailsFragment);
                }
                return FragmentHashMap.get(1);
            case 0:
                if (FragmentHashMap.get(0) == null) {
                    BuyPosterTypeFragment buyPosterTypeFragment = new BuyPosterTypeFragment();
                    FragmentHashMap.put(0, buyPosterTypeFragment);
                    ILoadDataHashMap.put(0, buyPosterTypeFragment);
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
        return 3;
    }
}