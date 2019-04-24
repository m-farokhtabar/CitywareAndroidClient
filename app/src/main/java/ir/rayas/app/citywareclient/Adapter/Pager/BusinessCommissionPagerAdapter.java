package ir.rayas.app.citywareclient.Adapter.Pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;

import ir.rayas.app.citywareclient.View.Fragment.BusinessCommission.BusinessArchiveFragment;
import ir.rayas.app.citywareclient.View.Fragment.BusinessCommission.BusinessCommissionReceivedFragment;
import ir.rayas.app.citywareclient.View.Fragment.BusinessCommission.BusinessNoCommissionReceivedFragment;
import ir.rayas.app.citywareclient.View.Fragment.BusinessCommission.CustomerSearchFragment;
import ir.rayas.app.citywareclient.View.Fragment.BusinessCommission.ReportBusinessCommissionFragment;
import ir.rayas.app.citywareclient.View.Fragment.ILoadData;




public class BusinessCommissionPagerAdapter extends FragmentPagerAdapter {

    private String[] TabNames = null;
    private HashMap<Integer, Fragment> FragmentHashMap = new HashMap<>();
    private HashMap<Integer, ILoadData> ILoadDataHashMap = new HashMap<>();

    public BusinessCommissionPagerAdapter(FragmentManager fm, String[] TabNames) {
        super(fm);
        this.TabNames = TabNames;
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {

            case 4:
                if (FragmentHashMap.get(4) == null) {
                    ReportBusinessCommissionFragment CurrentReportBusinessCommissionFragment = new ReportBusinessCommissionFragment();
                    FragmentHashMap.put(4, CurrentReportBusinessCommissionFragment);
                    ILoadDataHashMap.put(4, CurrentReportBusinessCommissionFragment);
                }
                return FragmentHashMap.get(4);
            case 3:
                if (FragmentHashMap.get(3) == null) {
                    CustomerSearchFragment CurrentUserPosterFragment = new CustomerSearchFragment();
                    FragmentHashMap.put(3, CurrentUserPosterFragment);
                    ILoadDataHashMap.put(3, CurrentUserPosterFragment);

                }
                return FragmentHashMap.get(3);
            case 2:
                if (FragmentHashMap.get(2) == null) {
                    BusinessNoCommissionReceivedFragment CurrentUserExtendedInformationFragment = new BusinessNoCommissionReceivedFragment();
                    FragmentHashMap.put(2, CurrentUserExtendedInformationFragment);
                    ILoadDataHashMap.put(2, CurrentUserExtendedInformationFragment);

                }
                return FragmentHashMap.get(2);
            case 1:
                if (FragmentHashMap.get(1) == null) {
                    BusinessCommissionReceivedFragment CurrentUserBusinessFragment = new BusinessCommissionReceivedFragment();
                    FragmentHashMap.put(1, CurrentUserBusinessFragment);
                    ILoadDataHashMap.put(1, CurrentUserBusinessFragment);

                }
                return FragmentHashMap.get(1);
            case 0:
                if (FragmentHashMap.get(0) == null) {
                    BusinessArchiveFragment CurrentPackageFragment = new BusinessArchiveFragment();
                    FragmentHashMap.put(0, CurrentPackageFragment);
                    ILoadDataHashMap.put(0, CurrentPackageFragment);
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
