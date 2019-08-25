package ir.rayas.app.citywareclient.ViewModel.Payment;

public class PackagePaymentViewModel {

    private int PackageId;
    private int Id;
    private String PackageName;
    private int PricePayable;
    private boolean IsPay;


    public int getPackageId() {
        return PackageId;
    }

    public void setPackageId(int packageId) {
        PackageId = packageId;
    }

    public String getPackageName() {
        return PackageName;
    }

    public void setPackageName(String packageName) {
        PackageName = packageName;
    }

    public int getPricePayable() {
        return PricePayable;
    }

    public void setPricePayable(int pricePayable) {
        PricePayable = pricePayable;
    }

    public boolean isPay() {
        return IsPay;
    }

    public void setPay(boolean pay) {
        IsPay = pay;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
