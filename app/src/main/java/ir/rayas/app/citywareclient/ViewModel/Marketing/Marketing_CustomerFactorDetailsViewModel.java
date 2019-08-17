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
    public double MarketerCommissionPrice ;

    public Integer getProductId() {
        return ProductId;
    }

    public void setProductId(Integer productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public double getCommissionPrice() {
        return CommissionPrice;
    }

    public void setCommissionPrice(double commissionPrice) {
        CommissionPrice = commissionPrice;
    }

    public double getDiscountPrice() {
        return DiscountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        DiscountPrice = discountPrice;
    }

    public double getMarketerCommissionPrice() {
        return MarketerCommissionPrice;
    }

    public void setMarketerCommissionPrice(double marketerCommissionPrice) {
        MarketerCommissionPrice = marketerCommissionPrice;
    }
}
