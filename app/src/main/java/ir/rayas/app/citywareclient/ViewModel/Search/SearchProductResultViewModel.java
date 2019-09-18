package ir.rayas.app.citywareclient.ViewModel.Search;

/**
 * Created by Hajar on 9/12/2019.
 */

public class SearchProductResultViewModel {

    private int BusinessId;
    private int ProductId;
    private String BusinessTitle;
    private String ProductTitle;
    private String ProductAbstractOfDescription;
    private String ProductImageUrl;


    public int getBusinessId() {
        return BusinessId;
    }

    public int getProductId() {
        return ProductId;
    }

    public String getBusinessTitle() {
        return BusinessTitle;
    }

    public String getProductTitle() {
        return ProductTitle;
    }

    public String getProductAbstractOfDescription() {
        return ProductAbstractOfDescription;
    }

    public String getProductImageUrl() {
        return ProductImageUrl;
    }
}
