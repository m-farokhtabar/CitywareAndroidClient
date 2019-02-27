package ir.rayas.app.citywareclient.ViewModel.User;

import java.util.List;

import ir.rayas.app.citywareclient.ViewModel.Definition.BankTypeViewModel;
import ir.rayas.app.citywareclient.ViewModel.Definition.ColorTypeViewModel;
import ir.rayas.app.citywareclient.ViewModel.Definition.DegreeOfEducationViewModel;
import ir.rayas.app.citywareclient.ViewModel.Definition.SexTypeViewModel;


/**
 * Created by Programmer on 7/28/2018.
 */
public class UserInfoWithTypesViewModel {
    /// <summary>
    ///  اطلاعات تکمیلی
    /// </summary>
    private UserInfoViewModel UserInfo;
    /// <summary>
    ///  انواع رنگ
    /// </summary>
    private List<ColorTypeViewModel> ColorTypeList;
    /// <summary>
    ///  انواع جنسیست
    /// </summary>
    private List<SexTypeViewModel> SexTypeList;
    /// <summary>
    ///  انواع بانک
    /// </summary>
    private List<BankTypeViewModel> BankTypeList;
    /// <summary>
    ///  تحصیلات
    /// </summary>
    private List<DegreeOfEducationViewModel> DegreeOfEducationList;

    public UserInfoViewModel getUserInfo() {
        return UserInfo;
    }

    public List<ColorTypeViewModel> getColorTypeList() {
        return ColorTypeList;
    }

    public List<SexTypeViewModel> getSexTypeList() {
        return SexTypeList;
    }

    public List<BankTypeViewModel> getBankTypeList() {
        return BankTypeList;
    }

    public List<DegreeOfEducationViewModel> getDegreeOfEducationList() {
        return DegreeOfEducationList;
    }

    public void setUserInfo(UserInfoViewModel userInfo) {
        UserInfo = userInfo;
    }

    public void setColorTypeList(List<ColorTypeViewModel> colorTypeList) {
        ColorTypeList = colorTypeList;
    }

    public void setSexTypeList(List<SexTypeViewModel> sexTypeList) {
        SexTypeList = sexTypeList;
    }

    public void setBankTypeList(List<BankTypeViewModel> bankTypeList) {
        BankTypeList = bankTypeList;
    }

    public void setDegreeOfEducationList(List<DegreeOfEducationViewModel> degreeOfEducationList) {
        DegreeOfEducationList = degreeOfEducationList;
    }
}
