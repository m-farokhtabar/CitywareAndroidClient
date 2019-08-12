package ir.rayas.app.citywareclient.Adapter.ViewModel;

/**
 * Created by Hajar on 3/4/2019.
 */

public class FactorStatusAdapterViewModel {

    public int Id = 0;
    public int Status = 0;
    public String Title = "";


    public FactorStatusAdapterViewModel(int id, String title, int status) {
        Id = id;
        Title = title;
        Status = status;
    }


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

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
