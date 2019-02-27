package ir.rayas.app.citywareclient.Repository;

import android.content.Context;

import ir.rayas.app.citywareclient.Service.Helper.AppController;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Helper.SharedPreferenceManager;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;

/**
 * ثبت اطلاعات و دسترسی و مشخصات کاربر در ریپو
 * Created by Programmer on 2/6/2018.
 */

public class AccountRepository {
    
    private static AccountViewModel Account;
    private IOnChangeUserAccount OnChangeUserAccount;

    public AccountRepository(IOnChangeUserAccount OnChangeUserAccount) {
        this.OnChangeUserAccount = OnChangeUserAccount;
    }

    /**
     * دریافت اکانت از کش
     *
     * @return
     */
    public AccountViewModel getAccount() {
        if (Account == null) {
            SharedPreferenceManager ShManager = new SharedPreferenceManager();
            if (ShManager.IsContain(DefaultConstant.AccountKey)) {
                Account = ShManager.GetClass(DefaultConstant.AccountKey, AccountViewModel.class);
            }
        }
        return Account;
    }

    /**
     * ویرایش یا اضافه کردن اکانت به کش
     *
     * @param account
     */
    public void setAccount(AccountViewModel account) {
        SharedPreferenceManager ShManager = new SharedPreferenceManager();
        if (ShManager.SetClass(DefaultConstant.AccountKey, account)) {
            Account = account;
        } else {
            Account = null;
        }
        if (OnChangeUserAccount != null)
            OnChangeUserAccount.UserAccountIsChanged(Account);
    }

    /**
     * حذف اکانت از کش
     */
    public void ClearAccount() {
        SharedPreferenceManager ShManager = new SharedPreferenceManager();
        ShManager.Clear(DefaultConstant.AccountKey);
        Account = null;
        if (OnChangeUserAccount != null)
            OnChangeUserAccount.UserAccountIsChanged(null);
    }
}
