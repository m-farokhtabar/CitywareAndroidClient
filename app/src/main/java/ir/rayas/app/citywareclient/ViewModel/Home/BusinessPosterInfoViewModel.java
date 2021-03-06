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
    private boolean IsBookmark;
    

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

    public boolean isBookmark() {
        return IsBookmark;
    }

    public void setBookmark(boolean bookmark) {
        IsBookmark = bookmark;
    }

    public void setBusinessId(int businessId) {
        BusinessId = businessId;
    }

    public void setPosterId(int posterId) {
        PosterId = posterId;
    }

    public void setBusinessTitle(String businessTitle) {
        BusinessTitle = businessTitle;
    }

    public void setPosterTitle(String posterTitle) {
        PosterTitle = posterTitle;
    }

    public void setPosterAbstractOfDescription(String posterAbstractOfDescription) {
        PosterAbstractOfDescription = posterAbstractOfDescription;
    }

    public void setPosterDescription(String posterDescription) {
        PosterDescription = posterDescription;
    }

    public void setPosterImagePathUrl(String posterImagePathUrl) {
        PosterImagePathUrl = posterImagePathUrl;
    }

    public void setOpen(boolean open) {
        IsOpen = open;
    }

    public void setHasDelivery(boolean hasDelivery) {
        HasDelivery = hasDelivery;
    }

    public void setTotalScore(Double totalScore) {
        TotalScore = totalScore;
    }

    public void setRows(int rows) {
        Rows = rows;
    }

    public void setCols(int cols) {
        Cols = cols;
    }
}
