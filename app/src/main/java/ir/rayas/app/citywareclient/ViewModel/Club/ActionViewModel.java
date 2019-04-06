package ir.rayas.app.citywareclient.ViewModel.Club;

/**
 * Created by Hajar on 3/29/2019.
 */

public class ActionViewModel {

    private int Id;
    private String Title;
    private Double Point;
    private String Description;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public Double getPoint() {
        return Point;
    }

    public void setPoint(Double point) {
        Point = point;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
