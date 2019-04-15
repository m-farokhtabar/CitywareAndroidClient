package ir.rayas.app.citywareclient.ViewModel.Poster;

/**
 * Created by Hajar on 4/11/2019.
 */

public class EditPurchasedPosterViewModel {

    private int Id;
    private String Title;
    private String AbstractOfDescription;
    private String ImagePathUrl;
    private String Description;
    private int Order;


    public void setId(int id) {
        Id = id;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setAbstractOfDescription(String abstractOfDescription) {
        AbstractOfDescription = abstractOfDescription;
    }

    public void setImagePathUrl(String imagePathUrl) {
        ImagePathUrl = imagePathUrl;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setOrder(int order) {
        Order = order;
    }
}
