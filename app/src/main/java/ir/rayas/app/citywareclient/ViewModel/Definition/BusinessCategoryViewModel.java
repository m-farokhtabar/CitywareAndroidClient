package ir.rayas.app.citywareclient.ViewModel.Definition;

import java.util.List;

/** ویومدل اصناف
 * Created by Programmer on 8/16/2018.
 */

public class BusinessCategoryViewModel {
    /// <summary>
    /// کد صنف
    /// </summary>
    private int Id;
    /// <summary>
    /// سرگروه صنف
    /// </summary>
    private Integer ParentId;
    /// <summary>
    /// نام صنف
    /// </summary>
    private String Name;
    /// <summary>
    /// تاریخ ایجاد
    /// سمت سرور
    /// </summary>
    private String Create;
    /// <summary>
    /// تاریخ ویرایش
    /// سمت سرور
    /// </summary>
    private String Modified;
    /// <summary>
    /// فعال بود یا نبودن
    /// </summary>
    private boolean IsActive;
    /// <summary>
    /// لیست فرزندان صنف
    /// </summary>
    private List<BusinessCategoryViewModel> Children;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Integer getParentId() {
        return ParentId;
    }

    public void setParentId(Integer parentId) {
        ParentId = parentId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCreate() {
        return Create;
    }

    public void setCreate(String create) {
        Create = create;
    }

    public String getModified() {
        return Modified;
    }

    public void setModified(String modified) {
        Modified = modified;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public List<BusinessCategoryViewModel> getChildren() {
        return Children;
    }

    public void setChildren(List<BusinessCategoryViewModel> children) {
        Children = children;
    }
}
