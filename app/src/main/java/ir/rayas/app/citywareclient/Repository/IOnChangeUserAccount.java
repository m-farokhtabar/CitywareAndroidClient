package ir.rayas.app.citywareclient.Repository;

/**
 * Created by Programmer on 12/18/2018.
 */

import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;

/**
 * به محض اینکه تغییر در اطلاعات اکانت داده شود این متد صدا زده می شود تا قسمت های مختلف برنامه اطلاعات کاربر را بروز کند
 * مثل هدر منوی کشویی که نام و نام مستعار را نمایش می
 */
public interface IOnChangeUserAccount {
    void UserAccountIsChanged(AccountViewModel Account);
}
