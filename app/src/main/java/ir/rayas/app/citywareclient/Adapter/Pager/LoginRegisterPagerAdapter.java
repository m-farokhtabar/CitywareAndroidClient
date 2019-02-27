package ir.rayas.app.citywareclient.Adapter.Pager;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import ir.rayas.app.citywareclient.View.Fragment.LoginRegister.LoginFragment;
import ir.rayas.app.citywareclient.View.Fragment.LoginRegister.RegisterFragment;

/** آداپتور مربوط به کنترل دو فرگمنت ورود و ثبت نام
 * Created by Programmer on 2/17/2018.
 */

public class LoginRegisterPagerAdapter extends FragmentPagerAdapter {

    private String[] TabNames = null;

    public LoginRegisterPagerAdapter(FragmentManager fm,String[] TabNames)
    {
        super(fm);
        this.TabNames = TabNames;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int index)
    {
        switch (index)
        {
            case 0:
                return new RegisterFragment();
            case 1:
                return new LoginFragment();
        }

        return null;
    }

    @Override
    public int getCount()
    {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TabNames[position];
    }

}
