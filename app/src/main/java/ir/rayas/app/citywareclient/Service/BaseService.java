package ir.rayas.app.citywareclient.Service;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import ir.rayas.app.citywareclient.Repository.AccountRepository;
import ir.rayas.app.citywareclient.Service.Helper.AppController;
import ir.rayas.app.citywareclient.Service.Uploader.MultipartRequest;
import ir.rayas.app.citywareclient.Share.Enum.ServiceMethodType;
import ir.rayas.app.citywareclient.ViewModel.User.AccountViewModel;

/**
 * Created by Programmer on 2/5/2018.
 * توابع مورد نیاز جهت ارتباط با وب سرویس
 */

public class BaseService {
    public <T> void GetService(final IService Service, String URL, final ServiceMethodType ServiceMethod, final Class<T> OutputClass, final Type OutputClassType) {
        StringRequest CurrentRequest;

        try {
            CurrentRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Service.OnSuccess(response, ServiceMethod, OutputClass, OutputClassType);
                    //-------------------------------------------------------------
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Service.OnError(volleyError, ServiceMethod, OutputClass);
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    AccountRepository ARepository = new AccountRepository(null);
                    AccountViewModel AccountViewModel = ARepository.getAccount();
                    if (AccountViewModel != null) {
                        params.put("Authorization", AccountViewModel.getToken());
                    } else {
                        params.put("Authorization", "");
                    }
                    return params;
                }
            };
            //to prevent get twice
            CurrentRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 16, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(CurrentRequest, "request");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> void PostService(final IService Service, String URL, final String jsonData, final ServiceMethodType ServiceMethod, final Class<T> OutputClass, final Type OutputClassType) {
        StringRequest CurrentRequest;
        try {
            CurrentRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Service.OnSuccess(response, ServiceMethod, OutputClass, OutputClassType);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Service.OnError(volleyError, ServiceMethod, OutputClass);
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    AccountRepository ARepository = new AccountRepository(null);
                    AccountViewModel AccountViewModel = ARepository.getAccount();
                    if (AccountViewModel != null) {
                        params.put("Authorization", AccountViewModel.getToken());
                    } else {
                        params.put("Authorization", "");
                    }
                    return params;
                }

                public String getBodyContentType() {
                    return "application/json; charset=" + getParamsEncoding();
                }


                public byte[] getBody() throws AuthFailureError {
                    try {
                        return jsonData.getBytes(getParamsEncoding());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

            };
            //to prevent post twice
            CurrentRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 16, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(CurrentRequest, "postRequest");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public <T> void PutService(final IService Service, String URL, final String jsonData, final ServiceMethodType ServiceMethod, final Class<T> OutputClass, final Type OutputClassType) {
        StringRequest CurrentRequest;
        try {
            CurrentRequest = new StringRequest(Request.Method.PUT, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Service.OnSuccess(response, ServiceMethod, OutputClass, OutputClassType);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Service.OnError(volleyError, ServiceMethod, OutputClass);
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    AccountRepository ARepository = new AccountRepository(null);
                    AccountViewModel AccountViewModel = ARepository.getAccount();
                    if (AccountViewModel != null) {
                        params.put("Authorization", AccountViewModel.getToken());
                    } else {
                        params.put("Authorization", "");
                    }
                    return params;
                }

                public String getBodyContentType() {
                    return "application/json; charset=" + getParamsEncoding();
                }


                public byte[] getBody() throws AuthFailureError {
                    try {
                        return jsonData.getBytes(getParamsEncoding());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

            };
            //to prevent post twice
            CurrentRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 16, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(CurrentRequest, "postRequest");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public <T> void DeleteService(final IService Service, String URL, final ServiceMethodType ServiceMethod, final Class<T> OutputClass, final Type OutputClassType) {

        StringRequest CurrentDeleteRequest;

        try {
            CurrentDeleteRequest = new StringRequest(Request.Method.DELETE, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Service.OnSuccess(response, ServiceMethod, OutputClass, OutputClassType);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Service.OnError(volleyError, ServiceMethod, OutputClass);
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    AccountRepository ARepository = new AccountRepository(null);
                    AccountViewModel AccountViewModel = ARepository.getAccount();
                    if (AccountViewModel != null) {
                        params.put("Authorization", AccountViewModel.getToken());
                    } else {
                        params.put("Authorization", "");
                    }
                    return params;
                }


            };
            //to prevent post twice
            CurrentDeleteRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 16,
                    0,//DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(CurrentDeleteRequest, "request");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> void PostFileService(final IsFileUpload Service, String url,Map<String, String> params, final String mimeType,byte[] multipartBody, final ServiceMethodType ServiceMethod, final Class<T> OutputClass, final Type OutputClassType){

        MultipartRequest multipartRequest = new MultipartRequest(url,params, mimeType, multipartBody, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    String json = null;
                    json = new String(response.data);
                    Service.OnSuccess(json, ServiceMethod, OutputClass, OutputClassType);
                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Service.OnError(volleyError, ServiceMethod, OutputClass);
            }
        }) ;

        AppController.getInstance().addToRequestQueue(multipartRequest);
    }

}
