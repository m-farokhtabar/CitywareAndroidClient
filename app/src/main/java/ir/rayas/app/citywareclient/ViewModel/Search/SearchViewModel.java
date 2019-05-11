package ir.rayas.app.citywareclient.ViewModel.Search;

import java.util.List;

import ir.rayas.app.citywareclient.ViewModel.Home.BusinessPosterInfoViewModel;



public class SearchViewModel {

    private  int Cols;
    private List<BusinessPosterInfoViewModel> BusinessPosterInfoViewModel;

    public int getCols() {
        return Cols;
    }

    public void setCols(int cols) {
        Cols = cols;
    }

    public List<BusinessPosterInfoViewModel> getBusinessPosterInfoViewModel() {
        return BusinessPosterInfoViewModel;
    }

    public void setBusinessPosterInfoViewModel(List<BusinessPosterInfoViewModel> businessPosterInfoViewModel) {
        BusinessPosterInfoViewModel = businessPosterInfoViewModel;
    }
}
