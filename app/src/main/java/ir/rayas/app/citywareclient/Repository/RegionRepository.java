package ir.rayas.app.citywareclient.Repository;

import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Helper.SharedPreferenceManager;
import ir.rayas.app.citywareclient.ViewModel.Definition.RegionViewModel;


public class RegionRepository {

    private static RegionViewModel RegionModel;

    /**
     * دریافت لیست پیام ها از کش
     *
     * @return
     */
    public RegionViewModel getAllRegion() {
        if (RegionModel == null) {
            SharedPreferenceManager ShManager = new SharedPreferenceManager();
            if (ShManager.IsContain(DefaultConstant.RegionKey)) {
                RegionModel = ShManager.GetClass(DefaultConstant.RegionKey, RegionViewModel.class);
            }
        }
        return RegionModel;
    }

    /**
     * ویرایش یا اضافه کردن پیام ها به کش
     *
     * @param regionModel
     */
    public void setAllRegion(RegionViewModel regionModel) {
        SharedPreferenceManager ShManager = new SharedPreferenceManager();
        if (ShManager.SetClass(DefaultConstant.RegionKey, regionModel)) {
            RegionModel = regionModel;
        } else {
            RegionModel = null;
        }

    }


}
