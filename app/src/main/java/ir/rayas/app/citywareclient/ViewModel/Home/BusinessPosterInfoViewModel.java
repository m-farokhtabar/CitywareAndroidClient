package ir.rayas.app.citywareclient.ViewModel.Home;



public class BusinessPosterInfoViewModel {

    private int BusinessId;
    private int PosterId;
    private String BusinessTitle;
    private String PosterTitle;
    private String PosterAbstractOfDescription;
    private String PosterDescription;
    private String PosterImagePathUrl;
    private boolean IsOpen;
    private boolean HasDelivery;
    private Double TotalScore;
    private int Rows;
    private int Cols;

    public int getBusinessId() {
        return BusinessId;
    }

    public int getPosterId() {
        return PosterId;
    }

    public String getBusinessTitle() {
        return BusinessTitle;
    }

    public String getPosterTitle() {
        return PosterTitle;
    }

    public String getPosterAbstractOfDescription() {
        return PosterAbstractOfDescription;
    }

    public String getPosterDescription() {
        return PosterDescription;
    }

    public String getPosterImagePathUrl() {
        return PosterImagePathUrl;
    }

    public boolean isOpen() {
        return IsOpen;
    }

    public boolean isHasDelivery() {
        return HasDelivery;
    }

    public Double getTotalScore() {
        return TotalScore;
    }

    public int getRows() {
        return Rows;
    }

    public int getCols() {
        return Cols;
    }
}
