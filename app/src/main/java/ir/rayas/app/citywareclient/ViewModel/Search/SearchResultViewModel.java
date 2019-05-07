package ir.rayas.app.citywareclient.ViewModel.Search;



public class SearchResultViewModel {

    /// <summary>
    ///  کد کسب و کار
    /// </summary>
    public int BusinessId ;
    /// <summary>
    /// عنوان کسب وکار
    /// </summary>

    public String BusinessTitle;
    /// <summary>
    /// عنوان کاری کسب و کار
    /// </summary>
    public String BusinessJobTitle ;
    /// <summary>
    /// آدرس جدیدترین پوستر
    /// </summary>
    public String NewestPurchasedPosterUrl ;


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
