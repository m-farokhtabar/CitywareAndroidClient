package ir.rayas.app.citywareclient.ViewModel.Club;

/**
 * Created by Hajar on 3/27/2019.
 */

public class PrizeViewModel {

    private int Id;
    private  String Title;
    private String Description;
    private String ImagePath;
    private double Point;
    private Integer PackageId;


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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public double getPoint() {
        return Point;
    }

    public void setPoint(double point) {
        Point = point;
    }

    public Integer getPackageId() {
        return PackageId;
    }

    public void setPackageId(Integer packageId) {
        PackageId = packageId;
    }
}
