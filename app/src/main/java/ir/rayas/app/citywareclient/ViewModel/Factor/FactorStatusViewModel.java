package ir.rayas.app.citywareclient.ViewModel.Factor;

/**
 * Created by Hajar on 3/4/2019.
 */

public class FactorStatusViewModel {

    private int Id;
    private int Status;
    private String Title;
    private String Description;
    private String Create;
    private String Modified;
    private boolean IsActive;


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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
}
