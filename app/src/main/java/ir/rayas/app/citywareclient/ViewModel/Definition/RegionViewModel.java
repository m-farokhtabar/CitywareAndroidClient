package ir.rayas.app.citywareclient.ViewModel.Definition;

import java.util.List;

/** ویومدل مناطق
 * Created by Programmer on 3/10/2018.
 */

public class RegionViewModel {
    /// <summary>
    /// کد منطقه
    /// </summary>
    private int Id;
    /// <summary>
    /// سرگروه منطقه
    /// </summary>
    private Integer ParentId;
    /// <summary>
    /// نام منطقه
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
    /// لیست فرزندان منطقه
    /// </summary>
    private List<RegionViewModel> Children;


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

    public List<RegionViewModel> getChildren() {
        return Children;
    }

    public void setChildren(List<RegionViewModel> children) {
        Children = children;
    }

}
