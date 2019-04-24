package ir.rayas.app.citywareclient.Adapter.Pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;

import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.Fragment.MarketerCommission.MarketerArchiveFragment;
import ir.rayas.app.citywareclient.View.Fragment.MarketerCommission.MarketerCommissionReceivedFragment;
import ir.rayas.app.citywareclient.View.Fragment.MarketerCommission.MarketerNoCommissionReceivedFragment;
import ir.rayas.app.citywareclient.View.Fragment.MarketerCommission.NewSuggestionMarketerCommissionFragment;
import ir.rayas.app.citywareclient.View.Fragment.MarketerCommission.ReportMarketerCommissionFragment;


public class MarketerCommissionPagerAdapter extends FragmentPagerAdapter {

    private String[] TabNames = null;
    private HashMap<Integer, Fragment> FragmentHashMap = new HashMap<>();
    private HashMap<Integer, ILoadData> ILoadDataHashMap = new HashMap<>();

    public MarketerCommissionPagerAdapter(FragmentManager fm, String[] TabNames) {
        super(fm);
        this.TabNames = TabNames;
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {

            case 4:
                if (FragmentHashMap.get(4) == null) {
                    ReportMarketerCommissionFragment CurrentReportMarketerCommissionFragment = new ReportMarketerCommissionFragment();
                    FragmentHashMap.put(4, CurrentReportMarketerCommissionFragment);
                    ILoadDataHashMap.put(4, CurrentReportMarketerCommissionFragment);
                }
                return FragmentHashMap.get(4);
            case 3:
                if (FragmentHashMap.get(3) == null) {
                    NewSuggestionMarketerCommissionFragment CurrentNewSuggestionMarketerCommissionFragment = new NewSuggestionMarketerCommissionFragment();
                    FragmentHashMap.put(3, CurrentNewSuggestionMarketerCommissionFragment);
                    ILoadDataHashMap.put(3, CurrentNewSuggestionMarketerCommissionFragment);

                }
                return FragmentHashMap.get(3);
            case 2:
                if (FragmentHashMap.get(2) == null) {
                    MarketerNoCommissionReceivedFragment CurrentMarketerNoCommissionReceivedFragment = new MarketerNoCommissionReceivedFragment();
                    FragmentHashMap.put(2, CurrentMarketerNoCommissionReceivedFragment);
                    ILoadDataHashMap.put(2, CurrentMarketerNoCommissionReceivedFragment);

                }
                return FragmentHashMap.get(2);
            case 1:
                if (FragmentHashMap.get(1) == null) {
                    MarketerCommissionReceivedFragment CurrentMarketerCommissionReceivedFragment = new MarketerCommissionReceivedFragment();
                    FragmentHashMap.put(1, CurrentMarketerCommissionReceivedFragment);
                    ILoadDataHashMap.put(1, CurrentMarketerCommissionReceivedFragment);

                }
                return FragmentHashMap.get(1);
            case 0:
                if (FragmentHashMap.get(0) == null) {
                    MarketerArchiveFragment CurrentMarketerArchiveFragment = new MarketerArchiveFragment();
                    FragmentHashMap.put(0, CurrentMarketerArchiveFragment);
                    ILoadDataHashMap.put(0, CurrentMarketerArchiveFragment);
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
        return 5;
    }
}
