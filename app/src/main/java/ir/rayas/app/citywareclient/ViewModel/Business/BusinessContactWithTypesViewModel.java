package ir.rayas.app.citywareclient.ViewModel.Business;

import java.util.List;

import ir.rayas.app.citywareclient.ViewModel.Definition.ContactTypeViewModel;

/**
 * جهت دریافت اطلاعات تماسهای یک بیزینس همراه یا نوع تماس ها
 * Created by Programmer on 8/17/2018.
 */
public class BusinessContactWithTypesViewModel {
    /// <summary>
    /// اطلاعات یک تماس یک بیزینس
    /// </summary>
    private BusinessContactViewModel BusinessContact;
    /// <summary>
    /// لیست انواع تماس
    /// </summary>
    private List<ContactTypeViewModel> ContactTypeList;

    public BusinessContactViewModel getBusinessContact() {
        return BusinessContact;
    }

    public void setBusinessContact(BusinessContactViewModel businessContact) {
        BusinessContact = businessContact;
    }

    public List<ContactTypeViewModel> getContactTypeList() {
        return ContactTypeList;
    }

    public void setContactTypeList(List<ContactTypeViewModel> contactTypeList) {
        ContactTypeList = contactTypeList;
    }
}
