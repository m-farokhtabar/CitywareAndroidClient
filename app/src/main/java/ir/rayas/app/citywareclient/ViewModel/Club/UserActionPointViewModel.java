package ir.rayas.app.citywareclient.ViewModel.Club;

/**
 * Created by Hajar on 3/30/2019.
 */

public class UserActionPointViewModel {

    private int Id;
    private int UserId;
    private int ActionId;
    private String ActionTitle;
    private String Create;
    private Double Point;
    private boolean IsActive;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getActionId() {
        return ActionId;
    }

    public void setActionId(int actionId) {
        ActionId = actionId;
    }

    public String getActionTitle() {
        return ActionTitle;
    }

    public void setActionTitle(String actionTitle) {
        ActionTitle = actionTitle;
    }

    public String getCreate() {
        return Create;
    }

    public void setCreate(String create) {
        Create = create;
    }

    public Double getPoint() {
        return Point;
    }

    public void setPoint(Double point) {
        Point = point;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }
}
