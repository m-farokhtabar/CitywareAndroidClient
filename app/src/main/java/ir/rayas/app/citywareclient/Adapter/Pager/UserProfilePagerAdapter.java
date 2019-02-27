package ir.rayas.app.citywareclient.Adapter.Pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;

import ir.rayas.app.citywareclient.View.Fragment.ILoadData;
import ir.rayas.app.citywareclient.View.Fragment.UserProfile.UserAddressFragment;
import ir.rayas.app.citywareclient.View.Fragment.UserProfile.UserBusinessFragment;
import ir.rayas.app.citywareclient.View.Fragment.UserProfile.UserEditFragment;
import ir.rayas.app.citywareclient.View.Fragment.UserProfile.UserExtendedInformationFragment;
import ir.rayas.app.citywareclient.View.Fragment.UserProfile.UserPackageFragment;
import ir.rayas.app.citywareclient.View.Fragment.UserProfile.UserPosterFragment;

/**
 * Created by Hajar on 7/16/2018.
 */

public class UserProfilePagerAdapter extends FragmentPagerAdapter {

    private String[] TabNames = null;
    private HashMap<Integer, Fragment> FragmentHashMap = new HashMap<>();
    private HashMap<Integer, ILoadData> ILoadDataHashMap = new HashMap<>();

    public UserProfilePagerAdapter(FragmentManager fm, String[] TabNames) {
        super(fm);
        this.TabNames = TabNames;
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 5:
                if (FragmentHashMap.get(5) == null) {
                    UserEditFragment CurrentUserEditFragment = new UserEditFragment();
                    FragmentHashMap.put(5, CurrentUserEditFragment);
                    ILoadDataHashMap.put(5, CurrentUserEditFragment);
                }
                return FragmentHashMap.get(5);
            case 4:
                if (FragmentHashMap.get(4) == null) {
                    UserAddressFragment CurrentUserAddressFragment = new UserAddressFragment();
                    FragmentHashMap.put(4, CurrentUserAddressFragment);
                    ILoadDataHashMap.put(4, CurrentUserAddressFragment);
                }
                return FragmentHashMap.get(4);
            case 3:
                if (FragmentHashMap.get(3) == null) {
                    UserExtendedInformationFragment CurrentUserExtendedInformationFragment = new UserExtendedInformationFragment();
                    FragmentHashMap.put(3, CurrentUserExtendedInformationFragment);
                    ILoadDataHashMap.put(3, CurrentUserExtendedInformationFragment);
                }
                return FragmentHashMap.get(3);
            case 2:
                if (FragmentHashMap.get(2) == null) {
                    UserBusinessFragment CurrentUserBusinessFragment = new UserBusinessFragment();
                    FragmentHashMap.put(2, CurrentUserBusinessFragment);
                    ILoadDataHashMap.put(2, CurrentUserBusinessFragment);
                }
                return FragmentHashMap.get(2);
            case 1:
                if (FragmentHashMap.get(1) == null) {
                    UserPackageFragment CurrentPackageFragment = new UserPackageFragment();
                    FragmentHashMap.put(1, CurrentPackageFragment);
                    ILoadDataHashMap.put(1, CurrentPackageFragment);
                }
                return FragmentHashMap.get(1);
            case 0:
                if (FragmentHashMap.get(0) == null) {
                    UserPosterFragment CurrentUserPosterFragment = new UserPosterFragment();
                    FragmentHashMap.put(0, CurrentUserPosterFragment);
                    ILoadDataHashMap.put(0, CurrentUserPosterFragment);
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
        return 6;
    }
}
