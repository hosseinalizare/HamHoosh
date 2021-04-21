package com.example.koohestantest1.InfoDirectory;

import android.content.Context;
import android.util.Log;

import com.example.koohestantest1.Utils.SharedPreferenceUtils;
import com.example.koohestantest1.model.BodySettingRequest;
import com.example.koohestantest1.model.CompanySetting;
import com.example.koohestantest1.model.EmployeeAdding;
import com.example.koohestantest1.model.EmployeeDeleting;
import com.example.koohestantest1.model.Permission;
import com.example.koohestantest1.model.SettingField;
import com.example.koohestantest1.model.network.RetrofitInstance;
import com.example.koohestantest1.model.repository.PermissionRepository;
import com.example.koohestantest1.model.repository.callback.PermissionCallBack;

import java.util.List;

import com.example.koohestantest1.ApiDirectory.CallBackComoanyProfile;
import com.example.koohestantest1.ApiDirectory.CartApi;
import com.example.koohestantest1.ApiDirectory.CompanyApi;
import com.example.koohestantest1.ApiDirectory.UserProfileApi;
import com.example.koohestantest1.ViewModels.CompanyFollowerViewModel;
import com.example.koohestantest1.ViewModels.CompanyProfileFieldViewModel;
import com.example.koohestantest1.ViewModels.UserReportViewModel;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.CompanyEmployeesClass;
import com.example.koohestantest1.classDirectory.CompanyProfile;
import com.example.koohestantest1.classDirectory.CompanyRequest;
import com.example.koohestantest1.classDirectory.CreateCompany;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.SendCheckAccess;
import com.example.koohestantest1.classDirectory.SendOrderClass;
import com.example.koohestantest1.classDirectory.UserProfile;
import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetOnlineInformationClass implements PermissionCallBack {

    final Retrofit retrofit;
    private int tagCounter;
    CompanyApi companyApi;
    CompanyApi callBackCompany;
    BaseCodeClass baseCodeClass = new BaseCodeClass();
    Context mContext;
    private final String TAG = GetOnlineInformationClass.class.getSimpleName();
    private PermissionCallBack permissionCallBack = this;
    CartApi cartApi;
    CartApi callBakCartApi;

    UserProfileApi userProfileApi;
    UserProfileApi callBackUserApi;

    public GetOnlineInformationClass(Context context) {

        mContext = context;

        retrofit = RetrofitInstance.getRetrofit();

        callBackCompany = new CompanyApi() {
            @Override
            public Call<GetResualt> CreateMyCompany(CreateCompany createCompany) {
                return null;
            }

            @Override
            public void onResponseCreateMyCompany(GetResualt getResualt) {

            }

            @Override
            public Call<GetResualt> UpdateCompanyProfile(CompanyProfile companyProfile) {
                return null;
            }

            @Override
            public void onResponseUpdateCompanyProfile(GetResualt getResualt) {

            }

            @Override
            public Call<CompanyProfile> ReadCompanyProfile(CompanyRequest companyRequest) {
                return null;
            }

            @Override
            public void onResponseGetCompanyProfile(CompanyProfile companyProfile) {
                if (companyProfile != null) {
                    BaseCodeClass.companyProfile = companyProfile;
                    SharedPreferenceUtils sharedPreferenceUtils = new SharedPreferenceUtils(context);
                    sharedPreferenceUtils.editCompanyName(companyProfile.getCompanyName());
                    sharedPreferenceUtils.editCompanyId(companyProfile.getCompanyID());
                }
            }

            @Override
            public Call<List<CompanyEmployeesClass>> getCompanyEmployee(String companyID) {
                return null;
            }

            @Override
            public void onResponseGetCompanyEmployee(List<CompanyEmployeesClass> employeesClasses) {
                Log.d(TAG, "onResponseGetCompanyEmployee: " + employeesClasses.size());
                BaseCodeClass.companyEmployees = employeesClasses;
                String employeeId = baseCodeClass.getEmployeeID(baseCodeClass.getUserID());
                if (!employeeId.equals("false")) {
                    PermissionRepository permissionRepository = new PermissionRepository(permissionCallBack);
                    permissionRepository.getEmployeePermissions(employeeId);
                }
            }

            @Override
            public Call<GetResualt> updateCompanyProfileField(CompanyProfileFieldViewModel companyProfileFieldViewModel) {
                return null;
            }

            @Override
            public void onResponseUpdateCompany(List<GetResualt> getResualts) {

            }

            @Override
            public Call<List<CompanyFollowerViewModel>> getCompanyFollower(String CompanyID) {
                return null;
            }

            @Override
            public void onResponseGetCompanyFollower(List<CompanyFollowerViewModel> companyFollowerViewModel) {
                baseCodeClass.setCompanyFollower(companyFollowerViewModel);
            }

            @Override
            public Call<GetResualt> callForAddingEmployee(EmployeeAdding employeeAdding) {
                return null;
            }

            @Override
            public Call<GetResualt> deleteEmployee(EmployeeDeleting employeeDeleting) {
                return null;
            }

            @Override
            public Call<List<CompanySetting>> getCompanySettings(BodySettingRequest bodySettingRequest) {
                return null;
            }

            @Override
            public Call<GetResualt> setSettingField(SettingField settingField) {
                return null;
            }

            @Override
            public Flowable<GetResualt> uploadCompanyPhoto(String companyId, String userId, String token, MultipartBody.Part file) {
                return null;
            }

        };

        callBackUserApi = new UserProfileApi() {
            @Override
            public Call<UserProfile> GetMyProfile(SendCheckAccess sendCheckAccess) {
                return null;
            }

            @Override
            public void onResponseGetMyProfile(UserProfile userProfile) {

            }

            @Override
            public Call<GetResualt> SetMyProfileData(UserProfile userProfile) {
                return null;
            }

            @Override
            public void onResponseSetMyProfileData(GetResualt getResualt) {

            }

            @Override
            public Call<GetResualt> updateUserProfileField(CompanyProfileFieldViewModel userProfileFieldViewModel) {
                return null;
            }

            @Override
            public void onResponseUpdateUserProfile(List<GetResualt> getResualts) {

            }
        };
    }

    public GetOnlineInformationClass(Context context, CartApi cartApi) {
        mContext = context;

        retrofit = RetrofitInstance.getRetrofit();
        callBakCartApi = cartApi;
    }

    public void readCompanyOrder(String companyID) {
        cartApi = retrofit.create(CartApi.class);
        try {
            Call<List<SendOrderClass>> call = cartApi.getCompanyOrder(companyID);
            call.enqueue(new Callback<List<SendOrderClass>>() {
                @Override
                public void onResponse(Call<List<SendOrderClass>> call, Response<List<SendOrderClass>> response) {
                    callBakCartApi.onResponseGetCompanyOrder(response.body());
                }

                @Override
                public void onFailure(Call<List<SendOrderClass>> call, Throwable t) {
                    BaseCodeClass.logMessage("CompanyOrder OnFailure >> " + t.getMessage(), mContext);
                }
            });
        } catch (Exception e) {
            BaseCodeClass.logMessage("readCompanyOrder" + e.getMessage(), mContext);
        }
    }

    public void readUserOrder(String userID) {
        cartApi = retrofit.create(CartApi.class);
        try {
            Call<List<SendOrderClass>> call = cartApi.getUserOrder(new UserReportViewModel("0", baseCodeClass.getToken(), userID));
            call.enqueue(new Callback<List<SendOrderClass>>() {
                @Override
                public void onResponse(Call<List<SendOrderClass>> call, Response<List<SendOrderClass>> response) {
                    callBakCartApi.onResponseUserOrder(response.body());
                }

                @Override
                public void onFailure(Call<List<SendOrderClass>> call, Throwable t) {
                    baseCodeClass.logMessage("UserOrder OnFailure >> " + t.getMessage(), mContext);
                }
            });
        } catch (Exception e) {
            baseCodeClass.logMessage("readUserOrder" + e.getMessage(), mContext);
        }
    }

    public void readCompany(String companyID) {
        try {
            companyApi = retrofit.create(CompanyApi.class);
            Call<CompanyProfile> call = companyApi.ReadCompanyProfile(new CompanyRequest(companyID,
                    baseCodeClass.getUserID(), baseCodeClass.getToken()));
            call.enqueue(new Callback<CompanyProfile>() {
                @Override
                public void onResponse(Call<CompanyProfile> call, Response<CompanyProfile> response) {
                    Log.d(TAG, "onResponse: " + response.body());
                    callBackCompany.onResponseGetCompanyProfile(response.body());
                }

                @Override
                public void onFailure(Call<CompanyProfile> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    BaseCodeClass.logMessage("readCompany >> onFailure " + t.getMessage(), mContext);
                }
            });
        } catch (Exception e) {
            BaseCodeClass.logMessage("readCompany >> " + e.getMessage(), mContext);
        }

    }

    public void readCompany(String companyID, CallBackComoanyProfile callback) {
        try {
            companyApi = retrofit.create(CompanyApi.class);
            Call<CompanyProfile> call = companyApi.ReadCompanyProfile(new CompanyRequest(companyID,
                    baseCodeClass.getUserID(), baseCodeClass.getToken()));
            Log.d(GetOnlineInformationClass.class.getSimpleName(), baseCodeClass.getToken());
            call.enqueue(new Callback<CompanyProfile>() {
                @Override
                public void onResponse(Call<CompanyProfile> call, Response<CompanyProfile> response) {
                    if (response.body() != null)
                        if (response.body().getToken() != null)
                            callback.OnReciveData(response.body());
                        else callback.OnFaile();
                }

                @Override
                public void onFailure(Call<CompanyProfile> call, Throwable t) {
                    callback.OnFaile();
                    BaseCodeClass.logMessage("readCompany >> onFailure " + t.getMessage(), mContext);
                }
            });
        } catch (Exception e) {
            BaseCodeClass.logMessage("readCompany >> " + e.getMessage(), mContext);
        }

    }

    public void companyEmployee(String companyID) {
        companyApi = retrofit.create(CompanyApi.class);
        Log.d(TAG, "companyEmployee: " + companyID);
        Call<List<CompanyEmployeesClass>> call = companyApi.getCompanyEmployee(companyID);
        call.enqueue(new Callback<List<CompanyEmployeesClass>>() {
            @Override
            public void onResponse(Call<List<CompanyEmployeesClass>> call, Response<List<CompanyEmployeesClass>> response) {
                callBackCompany.onResponseGetCompanyEmployee(response.body());
            }

            @Override
            public void onFailure(Call<List<CompanyEmployeesClass>> call, Throwable t) {
                BaseCodeClass.logMessage("readCompanyEmployee >> onFailure " + t.getMessage(), mContext);
            }
        });
    }

    public void loadCompanyFollower(String companyID) {
        companyApi = retrofit.create(CompanyApi.class);
        Call<List<CompanyFollowerViewModel>> call = companyApi.getCompanyFollower(companyID);
        call.enqueue(new Callback<List<CompanyFollowerViewModel>>() {
            @Override
            public void onResponse(Call<List<CompanyFollowerViewModel>> call, Response<List<CompanyFollowerViewModel>> response) {
                callBackCompany.onResponseGetCompanyFollower(response.body());
            }

            @Override
            public void onFailure(Call<List<CompanyFollowerViewModel>> call, Throwable t) {

            }
        });
    }

    public void loadMyProfileInformation() {
        userProfileApi = retrofit.create(UserProfileApi.class);
        try {

            SendCheckAccess sendCheckAccess = new SendCheckAccess(baseCodeClass.getUserID(), baseCodeClass.getToken(), BaseCodeClass.getIMEI(), baseCodeClass.getDeviceModel());
            Log.d(TAG, "loadMyProfileInformation: " + tagCounter++ + sendCheckAccess.toString());


            Call<UserProfile> call = userProfileApi.GetMyProfile(sendCheckAccess);
            call.enqueue(new Callback<UserProfile>() {
                @Override
                public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {

                    BaseCodeClass.userProfile = response.body();
                }

                @Override
                public void onFailure(Call<UserProfile> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            BaseCodeClass.logMessage("OnlineInformation 100 >> " + e.getMessage(), mContext);
        }
    }

    @Override
    public void onSuccessPermissionList(List<Permission> permissions) {
        baseCodeClass.setPermissions(permissions);
    }

    @Override
    public void onErrorPermissionList(String error) {

    }

    @Override
    public void onSuccessEditPermission(GetResualt result) {

    }

    @Override
    public void onErrorEditPermission(String error) {

    }
}
