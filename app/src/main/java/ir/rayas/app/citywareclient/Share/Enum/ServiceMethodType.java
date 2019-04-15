package ir.rayas.app.citywareclient.Share.Enum;

/**
 * Created by Programmer on 2/6/2018.
 * نام تمامی متدهای سرویس باید در اینجا باشد تا از طریق آن بتوان تشخیص داد الان متد ResponseService.OnResponse جواب کدام متد را ارسال کرده است
 */

public enum ServiceMethodType {
    RequestTrackingCode,
    Login,
    ConfirmAndLogin,
    SettingGet,
    IntroduceBackgroundGet,
    RandomlyEventOrNewsGet,
    UserAdd,
    UserSettingSet,
    UserSettingGet,
    RegionGet,
    RegionChildrenGet,
    RegionAllTreeGet,
    UserGet,
    UserSet,
    UserGetAllAddress,
    UserAddAddress,
    UserDeleteAddress,
    UserGetAddress,
    UserSetAddress,
    UserGetInfo,
    UserSetInfo,
    UserAddInfo,
    UserGetAllBusiness,
    BusinessGet,
    BusinessDelete,
    BusinessAdd,
    BusinessSet,
    BusinessCategoryTreeGet,
    BusinessContactGetAll,
    BusinessContactGet,
    BusinessContactDelete,
    BusinessContactAdd,
    BusinessContactSet,
    BusinessOpenTimeGetAll,
    BusinessOpenTimeGet,
    BusinessOpenTimeDelete,
    BusinessOpenTimeAdd,
    BusinessOpenTimeSet,
    ProductGetAll,
    ProductGet,
    ProductSet,
    ProductAdd,
    ProductDelete,
    GetAllProductImage,
    ProductImageGet,
    ProductImageAdd,
    ProductImageSet,
    ProductImageDelete,
    UploadFile,
    UserBookmarkGetAll,
    UserPosterGetAll,
    UserPackageGetAll,
    BookmarkDelete,
    BusinessCommentGetAll,
    BusinessCommentAdd,
    BusinessScoreAdd,
    BusinessScoreGet,
    BusinessScoreSet,
    BookmarkAdd,
    UserPosterGet,
    BusinessVisitedSet,
    NotificationGetAll,
    NotificationSetStatus,
    QuantityByProductIdGet,
    BasketAdd,
    BasketGetAll,
    BasketGet,
    BasketEditQuantityByProductId,
    BasketEditQuantityByItemId,
    BasketDeleteItemByItemId,
    BasketDelete,
    BasketEditUserDescriptionAndDelivery,
    DeliveryPriceGet,
    BasketQuickAdd,
    FactorAdd,
    UserFactorGetAll,
    UserFactorDelete,
    BusinessFactorGetAll,
    BusinessFactorDelete,
    UserFactorGet,
    FactorStatusGetAll,
    UserStatusAndDescriptionSet,
    UserDescriptionSet,
    BusinessUserFactorGet,
    BusinessStatusAndDescriptionSet,
    BusinessDescriptionSet,
    PackageListGetAll,
    UserPointGet,
    PackageDetailsGet,
    PrizeGetAll,
    PrizeUserGetAll,
    ActionPointGetAll,
    UserActionPointGet,
    PrizeRequestAdd,
    PrizeRequestPackageAdd,
    UserPackageOpenGetAll,
    UserPackageCloseGetAll,
    UserCreditGet,
    UserPosterValidGet,
    UserPosterExpiredGet,
    PurchasedPosterEdit,
    PosterTypeGetAll,
    PosterTypeGet,
    ExtendPosterEdit,
    PurchasedPosterAdd ,
    BusinessCommissionAndDiscountGet,
    MarketerSuggestionAdd ,
    SearchGet

}
