package ir.rayas.app.citywareclient.Repository;

import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Helper.SharedPreferenceManager;
import ir.rayas.app.citywareclient.ViewModel.Definition.RegionViewModel;


public class RegionRepository {

    private static RegionViewModel RootRegion;

    /**
     * دریافت لیست مناطق از کش
     *
     * @return - نود ریشه داده می شود مثلا ایران
     */
    public RegionViewModel GetAll() {
        if (RootRegion == null) {
            SharedPreferenceManager ShManager = new SharedPreferenceManager();
            if (ShManager.IsContain(DefaultConstant.RegionKey)) {
                RootRegion = ShManager.GetClass(DefaultConstant.RegionKey, RegionViewModel.class);
            }
        }
        return RootRegion;
    }
    /**
     *ثبت تمامی مناطق به صورت درختی
     *
     * @param RootViewModel  نود ریشه
     */
    public void SetAll(RegionViewModel RootViewModel) {
        SharedPreferenceManager ShManager = new SharedPreferenceManager();
        if (ShManager.SetClass(DefaultConstant.RegionKey, RootViewModel)) {
            RootRegion = RootViewModel;
        } else {
            RootRegion = null;
        }

    }
    /**
     * دریافت نام کامل منطقه
     *
     * @param Id کد صنف
     * @return - نام کامل صنف
     */
    public String GetFullName(int Id) {
        String Output = null;
        if (RootRegion!=null){
            Output = GetFullName(RootRegion, Id);
        }
        return Output;
    }
    /**
     * پیدا کردن نود منطقه با استفاده از کد آن
     * @param ParentNode نود ریشه منطقه
     * @param Id کد منطقه
     * @return - نام کامل
     */
    private String GetFullName(RegionViewModel ParentNode, int Id){
        if (ParentNode!=null) {
            if (ParentNode.getId() == Id)
                return ParentNode.getName();
            else {
                if (ParentNode.getChildren()!=null && ParentNode.getChildren().size()>0) {
                    for (RegionViewModel Node : ParentNode.getChildren()){
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
