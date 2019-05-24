package ir.rayas.app.citywareclient.Repository;

import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Helper.SharedPreferenceManager;
import ir.rayas.app.citywareclient.ViewModel.Definition.BusinessCategoryViewModel;

/**
 * ثبت اطلاعات اصناف به صورت درختی
 * در این کلاس اطلاعات صنف از طریق نود ریشه قابل دسترس است
 */
public class BusinessCategoryRepository {
    /**
     * نود ریشه صنف
     */
    private static BusinessCategoryViewModel RootBusinessCategory;
    /**
     * دریافت لیست اصناف از کش به صورت درختی
     *
     * @return نود ریشه اصناف
     */
    public BusinessCategoryViewModel GetAll() {
        if (RootBusinessCategory == null) {
            SharedPreferenceManager ShManager = new SharedPreferenceManager();
            if (ShManager.IsContain(DefaultConstant.BusinessCategoryKey)) {
                RootBusinessCategory = ShManager.GetClass(DefaultConstant.BusinessCategoryKey, BusinessCategoryViewModel.class);
            }
        }
        return RootBusinessCategory;
    }
    /**
     * ثبت تمامی اصناف در کش به صورت درختی
     * @param RootViewModel لیست اصناف
     */
    public void SetAll(BusinessCategoryViewModel RootViewModel) {
        SharedPreferenceManager ShManager = new SharedPreferenceManager();
        if (ShManager.SetClass(DefaultConstant.BusinessCategoryKey, RootViewModel)) {
            RootBusinessCategory = RootViewModel;
        } else {
            RootBusinessCategory = null;
        }
    }
    /**
     * دریافت نام کامل صنف
     *
     * @param Id کد صنف
     * @return - نام کامل صنف
     */
    public String GetFullName(int Id) {
        String Output = null;
        if (RootBusinessCategory!=null){
            Output = GetFullName(RootBusinessCategory, Id);
        }
        return Output;
    }
    /**
     * پیدا کردن نود صنف با استافده از کد آن
     * @param ParentNode نود ریشه صنف
     * @param Id کد صنف
     * @return - نام کامل
     */
    private String GetFullName(BusinessCategoryViewModel ParentNode,int Id){
        if (ParentNode!=null) {
            if (ParentNode.getId() == Id)
                return ParentNode.getName();
            else {
                if (ParentNode.getChildren()!=null && ParentNode.getChildren().size()>0) {
                    for (BusinessCategoryViewModel Node : ParentNode.getChildren()){
                        String Result = GetFullName(Node,Id);
                        if (Result != null)
                            return  ParentNode.getName() + "، " + Result;
                    }
                }
            }
        }
        return  null;
    }
}
