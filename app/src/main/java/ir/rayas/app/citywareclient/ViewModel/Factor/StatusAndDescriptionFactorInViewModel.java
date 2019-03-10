package ir.rayas.app.citywareclient.ViewModel.Factor;

/**
 * Created by Hajar on 3/7/2019.
 */

public class StatusAndDescriptionFactorInViewModel {

    private int FactorId;
    private int FactorStatusId;
    private String Description;

    public int getFactorId() {
        return FactorId;
    }

    public void setFactorId(int factorId) {
        FactorId = factorId;
    }

    public int getFactorStatusId() {
        return FactorStatusId;
    }

    public void setFactorStatusId(int factorStatusId) {
        FactorStatusId = factorStatusId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
