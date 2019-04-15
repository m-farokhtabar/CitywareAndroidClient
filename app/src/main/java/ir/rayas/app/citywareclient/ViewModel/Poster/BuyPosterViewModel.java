package ir.rayas.app.citywareclient.ViewModel.Poster;

/**
 * Created by Hajar on 4/13/2019.
 */

public class BuyPosterViewModel {

    private int BusinessId;
    private int PosterTypeId;
    private int Hours;


    public void setBusinessId(int businessId) {
        BusinessId = businessId;
    }

    public void setPosterTypeId(int posterTypeId) {
        PosterTypeId = posterTypeId;
    }

    public void setHours(int hours) {
        Hours = hours;
    }
}
