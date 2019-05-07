package ir.rayas.app.citywareclient.ViewModel.Marketing;


public class Marketing_CustomerFactorDetailsViewModel {

    /// <summary>
    /// کد محصول
    /// </summary>
    /// <remarks>فقط محصولات یا خدماتی نمایش داده می شود که پورسانت آنها متفاوت است</remarks>
    public Integer ProductId ;
    /// <summary>
    /// نام محصول
    /// </summary>
    public String ProductName ;
    /// <summary>
    /// مبلغ اصلی
    /// </summary>
    public double Price ;
    /// <summary>
    /// مبلغ پورسانت
    /// </summary>
    public double CommissionPrice;
    /// <summary>
    /// مبلغ تخفیف
    /// </summary>
    public double DiscountPrice;


    public Integer getProductId() {
        return ProductId;
    }

    public String getProductName() {
        return ProductName;
    }

    public double getPrice() {
        return Price;
    }

    public double getCommissionPrice() {
        return CommissionPrice;
    }

    public double getDiscountPrice() {
        return DiscountPrice;
    }
}
