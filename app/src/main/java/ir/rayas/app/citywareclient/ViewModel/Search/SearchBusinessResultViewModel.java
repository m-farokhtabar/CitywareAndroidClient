package ir.rayas.app.citywareclient.ViewModel.Search;


public class SearchBusinessResultViewModel {


    private int BusinessId;

    private String BusinessTitle;

    private String BusinessJobTitle;
    private String NewestPurchasedPosterUrl;

    public int getBusinessId() {
        return BusinessId;
    }

    public String getBusinessTitle() {
        return BusinessTitle;
    }

    public String getBusinessJobTitle() {
        return BusinessJobTitle;
    }

    public String getNewestPurchasedPosterUrl() {
        return NewestPurchasedPosterUrl;
    }
}
