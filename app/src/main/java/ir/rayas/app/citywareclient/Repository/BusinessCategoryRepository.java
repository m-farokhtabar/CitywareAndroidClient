package ir.rayas.app.citywareclient.Repository;

import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Helper.SharedPreferenceManager;
import ir.rayas.app.citywareclient.ViewModel.Definition.BusinessCategoryViewModel;



public class BusinessCategoryRepository {

    private static BusinessCategoryViewModel BusinessCategoryModel;

    /**
     * دریافت لیست پیام ها از کش
     *
     * @return
     */
    public BusinessCategoryViewModel getAllBusinessCategory() {
        if (BusinessCategoryModel == null) {
            SharedPreferenceManager ShManager = new SharedPreferenceManager();
            if (ShManager.IsContain(DefaultConstant.BusinessCategoryKey)) {
                BusinessCategoryModel = ShManager.GetClass(DefaultConstant.BusinessCategoryKey, BusinessCategoryViewModel.class);
            }
        }
        return BusinessCategoryModel;
    }

    /**
     * ویرایش یا اضافه کردن پیام ها به کش
     *
     * @param regionModel
     */
    public void setAllBusinessCategory(BusinessCategoryViewModel regionModel) {
        SharedPreferenceManager ShManager = new SharedPreferenceManager();
        if (ShManager.SetClass(DefaultConstant.BusinessCategoryKey, regionModel)) {
            BusinessCategoryModel = regionModel;
        } else {
            BusinessCategoryModel = null;
        }

    }

}
