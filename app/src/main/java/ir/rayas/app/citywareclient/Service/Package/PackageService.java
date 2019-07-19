package ir.rayas.app.citywareclient.Service.Package;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ir.rayas.app.citywareclient.Service.BaseService;
import ir.rayas.app.citywareclient.Service.IResponseService;
import ir.rayas.app.citywareclient.Service.IService;
import ir.rayas.app.citywareclient.Share.Constant.DefaultConstant;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.Share.Feedback.Feedback;
import ir.rayas.app.citywareclient.Share.Utility.Utility;
import ir.rayas.app.citywareclient.ViewModel.Basket.StandardOrderItemViewModel;
import ir.rayas.app.citywareclient.ViewModel.Package.OutPackageViewModel;
import ir.rayas.app.citywareclient.ViewModel.Package.OutputPackageTransactionViewModel;
import ir.rayas.app.citywareclient.ViewModel.Package.PackageDetailsViewModel;
import ir.rayas.app.citywareclient.ViewModel.Package.PurchasePackageViewModel;


public class PackageService implements IService {

    private String ControllerName = "Package/Purchased";
    private String ActionPackage = "Package";
    private String ActionGetAll = "All/Page";
    private IResponseService ResponseService;

    public PackageService(IResponseService ResponseService) {
        this.ResponseService = ResponseService;
    }

    public void GetAll(int PageNumber) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + ActionGetAll + "/" + PageNumber;
        Current.GetService(this, Url, ServiceMethodType.UserPackageGetAll, OutputPackageTransactionViewModel.class, new TypeToken<Feedback<List<OutputPackageTransactionViewModel>>>() {
        }.getType());
    }

    public void GetAllOpen(int PageNumber) {
        BaseService Current = new BaseService();
        String actionOpen = "Open";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + actionOpen + "/" + ActionGetAll + "/" + PageNumber;
        Current.GetService(this, Url, ServiceMethodType.UserPackageOpenGetAll, OutputPackageTransactionViewModel.class, new TypeToken<Feedback<List<OutputPackageTransactionViewModel>>>() {
        }.getType());
    }

    public void GetAllClose(int PageNumber) {
        BaseService Current = new BaseService();
        String actionClose = "Close";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ControllerName + "/" + actionClose + "/" + ActionGetAll + "/" + PageNumber;
        Current.GetService(this, Url, ServiceMethodType.UserPackageCloseGetAll, OutputPackageTransactionViewModel.class, new TypeToken<Feedback<List<OutputPackageTransactionViewModel>>>() {
        }.getType());
    }

    public void GetUserCredit() {
        BaseService Current = new BaseService();
        String actionUser = "User/Credit";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ActionPackage + "/" + actionUser;
        Current.GetService(this, Url, ServiceMethodType.UserCreditGet, OutputPackageTransactionViewModel.class, new TypeToken<Feedback<Double>>() {
        }.getType());
    }

    public void Get(int PackageId) {
        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + ActionPackage + "/" + PackageId;
        Current.GetService(this, Url, ServiceMethodType.PackageDetailsGet, PackageDetailsViewModel.class, new TypeToken<Feedback<PackageDetailsViewModel>>() {
        }.getType());
    }

    public void GetAllPackageList(int BusinessId) {
        BaseService Current = new BaseService();
        String actionAll = "All";
        String actionBusiness = "Business";
        String Url = DefaultConstant.BaseUrlWebService + "/" + ActionPackage + "/" + actionAll + "/" + actionBusiness + "/" + BusinessId;
        Current.GetService(this, Url, ServiceMethodType.PackageListGetAll, OutPackageViewModel.class, new TypeToken<Feedback<List<OutPackageViewModel>>>() {
        }.getType());
    }


    public void Add(PurchasePackageViewModel ViewModel) {
        String Controller = "Package/Purchase";

        BaseService Current = new BaseService();
        String Url = DefaultConstant.BaseUrlWebService + "/" + Controller ;
        Gson gson = new Gson();
        String JsonViewModel = gson.toJson(ViewModel);
        Current.PostService(this, Url, JsonViewModel, ServiceMethodType.PaymentPackage, PurchasePackageViewModel.class, new TypeToken<Feedback<OutputPackageTransactionViewModel>>() {
        }.getType());
    }

    @Override
    public <T> void OnSuccess(String Response, ServiceMethodType ServiceMethod, Class<T> OutputClass, Type OutputClassType) {
        Utility.HandleServiceSuccess(ResponseService, Response, ServiceMethod, OutputClass, OutputClassType);
    }

    @Override
    public <T> void OnError(VolleyError volleyError, ServiceMethodType ServiceMethod, Class<T> OutputClass) {
        Utility.HandleServiceError(ResponseService, volleyError, ServiceMethod, OutputClass);
    }
}

