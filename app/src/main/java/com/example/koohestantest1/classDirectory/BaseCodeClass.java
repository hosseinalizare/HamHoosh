package com.example.koohestantest1.classDirectory;

//hello this is a message from basecode

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.koohestantest1.R;
import com.example.koohestantest1.Utils.SharedPreferenceUtils;

import com.example.koohestantest1.constants.CompanyId;
import com.example.koohestantest1.local_db.entity.Product;
import com.example.koohestantest1.local_db.entity.Properties;
import com.example.koohestantest1.model.Permission;
import com.example.koohestantest1.model.network.RetrofitInstance;
import com.google.android.material.badge.BadgeDrawable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.example.koohestantest1.ApiDirectory.CheckAccessApi;
import com.example.koohestantest1.DB.DataBase;
import com.example.koohestantest1.ViewModels.CompanyFollowerViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.nostra13.universalimageloader.utils.StorageUtils.getCacheDirectory;


public class BaseCodeClass extends Application {
    public static  Context context =null;

    public static long appId = 0;
    private static List<Permission> permissions = new ArrayList<>();

    public static String appName = "";
    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }



    private String globalTest;

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        BaseCodeClass.permissions = permissions;
    }

    public String getGlobalTest() {
        return globalTest;
    }

    public void setGlobalTest(String globalTest) {
        this.globalTest = globalTest;
    }

    public static String selectedPID;

    public static String deviceModel;
    public static String IMEI;
    public static String mobileNumber;
    public static String token;
    public boolean debug = true;
    public static String userName;
    public static String password;
    public static String userID;

    public static Product localProduct = new Product();
    public static List<Product> localProducts = new ArrayList<>();
    public static Properties localProperties = new Properties();
    public static List<Properties> localPropertiesList = new ArrayList<>();
    //  public static int bottomNavClick = 0;

    //Chrbnihukva
    //CIuiybTlgAL
    public static String CompanyID = CompanyId.COMPANY_ID;

    public static String EmployeeID = "EmnJnjtsqhU";

    public static String DownloadParam = "Dl_param";

    public static String DownloadImg = "Dl_img";

    public static UserProfile userProfile;

//    public AllProductData loadingItem = new AllProductData();

    public static boolean compare(String str1, String str2) {
        return ((str1 == null & str2 == null) ? true : (str1 == null | str2 == null) ? false : str1.equals(str2));
    }

    /**
     * order
     */
    public static SendOrderClass sendOrderClass = new SendOrderClass();
    public static AllProductData selectedProduct;
    public static SendOrderClass myStoreSelectedOrder;
    public static List<SendOrderClass> orderClassList = new ArrayList<>();
    public static List<SendOrderClass> userOrder = new ArrayList<>();


    public static BadgeDrawable badge;

    public static AddProductImageRecyclerViewAdapter addProductImageRecyclerViewAdapter;
    public static BulletinRecyclerViewAdapter bulletinRecyclerViewAdapter;
    public static CartProductRecyclerViewAdapter cartProductRecyclerViewAdapter;
    public static CategoryRecyclerViewAdapter categoryRecyclerViewAdapter;
    public static CompanyRecyclerViewAdapter companyRecyclerViewAdapter;
    public static EditProfileRecyclerViewAdapter editProfileRecyclerViewAdapter;
    public static MyStoreProductRecyclerViewAdapter myStoreProductRecyclerViewAdapter;
    public static ParticularProductRecyclerViewAdapter particularProductRecyclerViewAdapter;
    public static ProducrtGridRecyclerViewAdapter producrtGridRecyclerViewAdapter;
    public static ProductPropertiesRecyclerViewAdapter productPropertiesRecyclerViewAdapter;
    public static ProductRecyclerViewAdapter productRecyclerViewAdapter;
    public static ProfileRecyclerViewAdapter profileRecyclerViewAdapter;
    public static ProfileSettingRecyclerViewAdapter profileSettingRecyclerViewAdapter;

    public Intent myServiceIntent = null;

    /**
     * company
     */
    public static CompanyProfile companyProfile;
    public static List<CompanyEmployeesClass> companyEmployees;
    private final String TAG = BaseCodeClass.class.getSimpleName();

    public String getEmployeeID(String userID) {
        if (companyEmployees != null) {
            Log.d(TAG, "getEmployeeID: searching");
            Log.d(TAG, "getEmployeeID: " + companyEmployees.size());
            for (CompanyEmployeesClass cec : companyEmployees
            ) {
                Log.d(TAG, "getEmployeeID: " + cec.getEmployeeID());
                if (cec.getUserID().equals(userID)) {
                    Log.d(TAG, "getEmployeeID: found");
                    return cec.getEmployeeID();
                }
            }
        }
        if (companyEmployees == null)
            Log.d(TAG, "getEmployeeID: employees are null");

        return "false";

    }

    static List<CompanyFollowerViewModel> companyFollower = new ArrayList<>();

    public List<CompanyFollowerViewModel> getCompanyFollower() {
        return companyFollower;
    }

    public void setCompanyFollower(List<CompanyFollowerViewModel> companyFollower) {
        this.companyFollower = companyFollower;
    }

    public static String hashtagsValue = null;


//// call onCreate for start service

    @Override
    public void onCreate() {
        super.onCreate();
        if (context ==null){
            context = getApplicationContext();
        }

        if (myServiceIntent ==null){
            myServiceIntent = new Intent(this,AppService.class);
            startService(myServiceIntent);
        }else {
            startService(myServiceIntent);
        }
    }


    public enum variableType {
        string_(0),
        int_(1),
        date(2),
        bool_(3),
        datetime(4),
        time(5),
        float_(6),
        email(7),
        webPage(8),
        mobile(9),
        phoneNumber(10),
        number(11),
        location(12),
        marital(13),
        LongOfTime(14),
        StartEndTime(15),
        city(17),
        Media_(18),
        Order_(19),
        Product_(20),
        File_(21),
        Music_(22),
        Voice_(23),
        Link_(24),
        News_(25),
        Employee_(26),
        Contact_(27),
        Video_(28),
        Image_(29),
        manageShowProduct(30);
        private final int value;

        variableType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    enum AppEvent
    {
        UserMSG(0),
        CompanyMSG(1),
        CompanyOrder(2),
        UserOrder(3),
        Notify(4),
        calls(5),
        NewCompany(6),
        EmployeeUpdate(7),
        ErrorReport(8),
        ErrorReportAnswer(9),
        NewProduct(10),
        UserProfileChange(11),
        CompanyProfileChange(12),
        Comment(13),
        Address(14),
        NewVersion(15),
        OtherPersoLoginWhitthisUser(16),
        MustLogOut(17),
        AppMustUpdate(18);

        private final int value;

        AppEvent(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    public enum FeildGroupType {
        Public_,//عمومی
        Products_,//محصولات
        Employees_,//کارمندان
        Orders_,//سفارشات
        Ux_,//نمایش های گرافیکی
        ManageQueue_,//صف  انتشار
        Message_,//پیامها
        Advanced_,//پیشرفته
        Deleverytime_,//زمان تحویل کالا
        DailyWorkTime_,//زمان فعالیت فروشگاه
        Calls_menu,//اطلاعات تماس
        Profile_menu,// پروفایل
        Mony_menu,// اطلاعات مالی
        Address_menu,//ادرس فروشگاه
        None_,//غیر قابل نمایش
        Media_,//رسانه ها
    }


    public enum EmploeeAccessLevel {
        AddEmploee(0),            //افزودن کارمند جدید  Owner
        EditeEmploee(1),           // ویرایش دسترسی کارمندان // Owner
        ViweOrderReport(2),       // دیدن گزارش سفارشات Operator + Admin + Owner
        ViewNewOrder(3),           // دیدن سفارشات جدید Operator + Admin + Owner
        CanCancleOrder(4),         // لغو یک سفارش    Admin + Owner
        CreatNewProduct(5),         //ثبت محصول       Owner
        EditeProduct(6),           //ویرایش محصول  Admin + Owner
        EditeProductPrice(7),         //ویرایش قیمت محصول  Admin + Owner
        RemoveProduct(8),        // حذف یک محصول Owner
        EditProductDiscont(9),      // ویرایش موجودی کالا Operator + Admin + Owner
        ViewCustomerList(10),       // دیدن لیست مشتریان  Admin + Owner
        ShowChatMenu(11),                //پاسخ دادن به درخواست مشتریان Operator + Admin + Owner
        StartChatWhitCustmer(12),      // شروع چت با مشتری   Admin + Owner
        EditeCompanyProfile(13),    // پروفایل را ویرایش کند  Owner
        EditeCompanySettinge(14),  //  تنظیمات فروشکاه   Admin + Owner
        ViewCustomerMobilePhone(15);// دیدن شماره موبایل مشتری ها  Admin + Owner
        private final int value;

        EmploeeAccessLevel(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum CompanySettingEnum {
        CompanyOwnerID,
        ExpiredTime,
        ActiveInvoice,
        ActiveNewsletters,
        ActivNotification,
        ActiveEmployee,
        ActiveChatMessage,
        ActivePublicMessages,
        ActiveFilterMenu,
        ActiveSpecialProducts,
        ActiveSortMenu,
        ActiveCart,
        ActiveAdvancedSearch,
        WorkTime1,
        WorkTime2,
        WorkTime3,
        WorkTime4,
        WorkTime5,
        WorkTime6,
        WorkTime7,
        ActiveProduct,
        OnlyForDefenition,
        ShowInDefinePane,
        CreateReleaseQueue,
        ParentProductId,
        Editedqueue,
        DELEVERYTime1,
        DELEVERYime2,
        DELEVERYime3,
        DELEVERYime4,
        DELEVERYime5,
        DELEVERYTime6,
        DELEVERYTime7,
        MaxProductNumber,
        FirstMessageToCustomer,
        FirstMessageToEmployee,
        CompanyID,
        CompanyName,
        Bio,
        EmailAddress,
        MobilePhone,
        BusinessPhone,
        FaxNumber,
        NationalCode,
        RegistrationNumber,
        DateOfRegistration,
        EconomicaNumber,
        CreationDate,
        WebPage,
        Referredby,
        Privasy,
        Union,
        ObjectID,
        Country,
        City,
        State,
        PostalCode,
        Area,
        Location,
        Neighborhood,
        Address,
        ShowNewsletters,
        ShowNotification,
        ShowEmployee,
        ShowChatMessage,
        ShowPublicMessages,
        ShowFilterMenu,
        ShowSpecialProducts,
        ShowSortMenu,
        ShowCart,
        ShowAdvancedSearch,
        ActiveOnPublicHolidays,
        DeliveryOnHolidays,
    }
//anddroid to change data
//    public class EditProfileFieldViewModels
//    {
//        public string UserID { get; set; }
//        public string Token { get; set; }
//        public string ObjectID { get; set; }
//        public String Value { get; set; }
//        public CompanyEnum Cenum { get; set; }
    //
//    }

//    public class CompanySettingViewModels
//    {
//        public string CompanyOwnerID { get; set; }
//        public Nullable<System.DateTime> ExpiredTime { get; set; }
//        public Nullable<bool> ActiveInvoice { get; set; }
//        public Nullable<bool> ActiveNewsletters { get; set; }
//        public Nullable<bool> ActivNotification { get; set; }
//        public Nullable<bool> ActiveEmployee { get; set; }
//        public Nullable<bool> ActiveChatMessage { get; set; }
//        public Nullable<bool> ActivePublicMessages { get; set; }
//        public Nullable<bool> ActiveFilterMenu { get; set; }
//        public Nullable<bool> ActiveSpecialProducts { get; set; }
//        public Nullable<bool> ActiveSortMenu { get; set; }
//        public Nullable<bool> ActiveCart { get; set; }
//        public Nullable<bool> ActiveAdvancedSearch { get; set; }
//        public string WorkTime1 { get; set; }
//        public string WorkTime2 { get; set; }
//        public string WorkTime3 { get; set; }
//        public string WorkTime4 { get; set; }
//        public string WorkTime5 { get; set; }
//        public string WorkTime6 { get; set; }
//        public string WorkTime7 { get; set; }
//        public Nullable<bool> ActiveProduct { get; set; }
//        public Nullable<bool> OnlyForDefenition { get; set; }
//        public Nullable<bool> ShowInDefinePane { get; set; }
//        public Nullable<bool> CreateReleaseQueue { get; set; }
//        public string ParentProductId { get; set; }
//        public Nullable<bool> Editedqueue { get; set; }
//        public string DELEVERYTime1 { get; set; }
//        public string DELEVERYime2 { get; set; }
//        public string DELEVERYime3 { get; set; }
//        public string DELEVERYime4 { get; set; }
//        public string DELEVERYime5 { get; set; }
//        public string DELEVERYTime6 { get; set; }
//        public string DELEVERYTime7 { get; set; }
//        public Nullable<int> MaxProductNumber { get; set; }
//        public string FirstMessageToCustomer { get; set; }
//        public string FirstMessageToEmployee { get; set; }
//        /// <summary>
//        /// ///////////////last data
//        /// </summary>
//        public string CompanyID { get; set; }
//        public string CompanyName { get; set; }
//        public string Bio { get; set; }
//        public string EmailAddress { get; set; }
//        public string MobilePhone { get; set; }
//        public string BusinessPhone { get; set; }
//        public string FaxNumber { get; set; }
//        public string NationalCode { get; set; }
//        public string RegistrationNumber { get; set; }
//        public Nullable<System.DateTime> DateOfRegistration { get; set; }
//        public string EconomicaNumber { get; set; }
//        public Nullable<System.DateTime> CreationDate { get; set; }
//        public string WebPage { get; set; }
//        public string Referredby { get; set; }
//        public string Privasy { get; set; }
//        public string Union { get; set; }
//        public string ObjectID { get; set; }
//        public string Country { get; set; }
//        public string City { get; set; }
//        public string State { get; set; }
//        public string PostalCode { get; set; }
//        public string Area { get; set; }
//        public string Location { get; set; }
//        public string Neighborhood { get; set; }
//        public string Address { get; set; }
//
//    }


    public enum productFieldEnum {
        CompanyID,

        SupplierID,

        ProductID,

        ProductName,

        Description,

        StandardCost,

        ListPrice,
        ReorderLevel,

        TargetLevel,

        Unit,

        QuantityPerUnit,
        Discontinued,
        MinimumReorderQuantity,

        Category,

        Show,
        Deleted
    }

    public static enum editMode {
        userProfile,
        companyProfile
    }

    public static enum CompanyEnum {
        CompanyName,
        Bio,
        EmailAddress,
        MobilePhone,
        BusinessPhone,
        FaxNumber,
        NationalCode,
        RegistrationNumber,
        DateOfRegistration,
        EconomicaNumber,
        CreationDate,
        WebPage,
        Referredby,
        Privasy,
        Union,
        ObjectID,
        Country,
        City,
        State,
        PostalCode,
        Area,
        Location,
        Neighborhood,
        Address,
        CompanyType,
        UpdateDate,
        Deleted,
        HasCourier,
        StartTime,
        EndTime,
        ActiveDayID,
        Spare1,
        Spare2,
        Spare3,
        TimeToCourier,
        AddresMenu,
        SettingMenu,
        CallInfo,
        money,
        NONE,

        U_MobilePhone,
        U_PID,
        U_FirstName,
        U_LastName,
        U_EmailAddress,
        U_HomePhone,
        U_BusinessPhone,
        U_NationalCode,
        U_CompanyID,
        U_JobTitle,
        U_FaxNumber,
        U_Country,
        U_City,
        U_State,
        U_PostalCode,
        U_Area,
        U_Location,
        U_Address,
        U_Bio,
        U_WebPage,
        U_Referredby,
        U_Hometown,
        U_Birthdate,
        U_MaritalStatus,
        U_ImageAttachments,
        U_IsOnline,
        U_LastSeen,
        U_Privasy,
        U_ErrorMsg,
        Umenu_CallInfo,
        Umenu_Setting,
        Umenu_Address,
        Umenu_Money
    }

    public static EditProfileField selectedProfileField = new EditProfileField();


    public static ManageOrderClass manageOrderClass = new ManageOrderClass();

    //    public static List<SendProductClass> productClasses = new ArrayList<>();
    public static List<AllProductData> particularProduct = new ArrayList<>();
    public static List<AllProductData> bulletinProduct = new ArrayList<>();
    public static List<AllProductData> productDataList = new ArrayList<>();


    public static List<String> statList = new ArrayList<>();
    public static List<String> currentCityList = new ArrayList<>();
    public static List<ArrayList<String>> cityList = new ArrayList<>();

    public static AddressViewModel addressViewModel;
    public static List<AddressViewModel> allAddress = new ArrayList<>();
    public static String selectedState = "تهران";
    public static boolean addressSaved = false;
    public static boolean loadAddress = false;
    public static boolean deletedAddress = false;

    public static AddressViewModel selectedAddress;

    public static String filterValue = "همه";

    public static String getCompanyName() {
        return CompanyName;
    }

    public static void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public static String CompanyName;

    public String getCompanyID() {
        return CompanyID;
    }

    public static long insertedItemId;

    public static String getEmployeeID() {
        return EmployeeID;
    }

    public void setCompanyID(String companyID) {

        CompanyID = companyID;
        dataBase.updateCompanyID(CompanyID);
    }

    public enum myAppPage {none, ShoppCenter, Search, myStor, cartpage, myProfile, addressPage, saveAddress, DeleteAddressPg, ExitThread}

    ;
    public static myAppPage PageShow = myAppPage.ShoppCenter;

    /*
     gets product Id and says it exists or not then initialize @param selectedProduct
     */
    public boolean loadSelectedProduct(String pid, Context context) {
        try {
            for (AllProductData s : productDataList
            ) {
                if (s.getProductClass().getProductID().equals(pid)) {
                    selectedProduct = s;
                    return true;
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, "baseCodeClass 899 : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
//        Toast.makeText(context," >>> 444", Toast.LENGTH_SHORT).show();
        return false;
    }

    public static Bitmap ProductImage(String pid, Context context) {
        try {
            String root = getCacheDirectory(context).getPath();// Environment.getExternalStorageDirectory().toString();
            File imgFile = new File(root + "/Dehkade/File/" + pid + ".jpg");
            if (imgFile.exists()) {
                return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            } else {
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.finallogo);
            }
        } catch (Exception e) {
            Toast.makeText(context, "baseCodeClass.ProductImage >> " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.finallogo);
        }
    }

    public void ActionRes(GetResualt getResualt) {
        if (getResualt.getResualt().equals("119")) {
            checkAccess(new CheckAccessApi() {
                @Override
                public Call<GetCheckAccess> getCheckAccess(SendCheckAccess sendCheckAccess) {
                    return null;
                }

                @Override
                public void onResponseCheckAccess(GetCheckAccess getCheckAccess) {
                    String result = getCheckAccess.getResualt();
                    if (result.equals("111")) {
                        dataBase.updateToken(getCheckAccess.getMsg());
                        setToken(getCheckAccess.getMsg());
                        //toastMessage("token update");
                    } else if (result.equals("113")) {

                    } else {
                    }
                }
            });
        }
    }

    CheckAccessApi checkAccessApi;

    public void checkAccess(final CheckAccessApi callBack) {
        try {
            data = dataBase.getAllData(dataBase.BASE_TABLE);
            data.moveToFirst();
            SendCheckAccess sendCheckAccess = new SendCheckAccess(data.getString(data.getColumnIndex(dataBase.getUserId())),
                    data.getString(data.getColumnIndex(dataBase.getTOKEN())), data.getString(data.getColumnIndex(dataBase.getIMEI())),
                    data.getString(data.getColumnIndex(dataBase.getDeviceModel())));
//            setUserID(data.getString(data.getColumnIndex(dataBase.getUserId())));
            retrofit2.Call<GetCheckAccess> call = checkAccessApi.getCheckAccess(sendCheckAccess);
            call.enqueue(new Callback<GetCheckAccess>() {
                @Override
                public void onResponse(retrofit2.Call<GetCheckAccess> call, Response<GetCheckAccess> response) {
                    GetCheckAccess getCheckAccess = new GetCheckAccess(response.body().getResualt(), response.body().getMsg());
                    callBack.onResponseCheckAccess(getCheckAccess);
                }

                @Override
                public void onFailure(retrofit2.Call<GetCheckAccess> call, Throwable t) {
                }
            });
            //toastMessage(sendCheckAccess.getToken());
        } catch (Exception e) {
        }
    }//end public void checkAccess(final CheckAccessApi callBack)


    public String BASE_URL = "https://dehkade.nokhbgan.ir/api/";
    DataBase dataBase;
    Cursor data;

    public BaseCodeClass() {
        final Retrofit retrofit = RetrofitInstance.getRetrofit();

        checkAccessApi = retrofit.create(CheckAccessApi.class);
    }


    public boolean LoadBaseData(Context context) {
        try {
            dataBase = new DataBase(context);
            data = dataBase.getAllData(dataBase.BASE_TABLE);
            data.moveToFirst();//moveToLast();//Position(data.getColumnIndex("ID"));//ToFirst();
            do {
                userID = (data.getString(data.getColumnIndex(DataBase.getUserId())));
                SharedPreferenceUtils.saveUserId(context,userID.toString());
                token = (data.getString(data.getColumnIndex(DataBase.getTOKEN())));
                IMEI = (data.getString(data.getColumnIndex(DataBase.getIMEI())));
                deviceModel = (data.getString(data.getColumnIndex(DataBase.getDeviceModel())));
                userName = (data.getString(data.getColumnIndex(DataBase.getUserName())));
                password = (data.getString(data.getColumnIndex(DataBase.getPASSWORD())));
            } while (userID != null && data.moveToNext());

            data = dataBase.getAllData(DataBase.Company_TABLE);

            data.moveToFirst();//moveToLast();//Position(data.getColumnIndex("ID"));//ToFirst();
            do {
                CompanyID = (data.getString(data.getColumnIndex(DataBase.getComany_ID())));
                CompanyName = (data.getString(data.getColumnIndex(DataBase.getComany_Name())));
            } while (userID != null && data.moveToNext());

            return true;
        }//try
        catch (Exception e) {
            return false;
        }
    }// end public boolean signUp


    public String CheckMobileNumber(String mobile) {
        if (mobile == null | mobile == "") {
            return null;
        }
        if (mobile.matches("(0)?9\\d{9}")) {
            return mobile;
        }
        if (mobile.matches("(\\+98)?9\\d{9}")) {
            return mobile.replace("+98", "0");
        }

        return null;
    }


    public String getToken() {
        return token;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

//    public String getIMEI() {
//
//        return IMEI;
//    }


    public static String getIMEI() {
        return BaseCodeClass.CompanyID;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public boolean isDebug() {
        return debug;
    }

    public String getUserName() {
        return userName;
    }

    public static String getPassword() {
        return password;
    }

    public String getUserID() {
        return userID;
    }

    public String getBaseStoreId() {
        return BaseStoreId;
    }

    public static void setDeviceModel(String deviceModel_) {
        deviceModel = deviceModel_;
    }

    public void setIMEI(String IMEI) {

        this.IMEI = BaseCodeClass.CompanyID;
    }

    public static void setMobileNumber(String mobileNumber_) {
        mobileNumber = mobileNumber_;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setBaseStoreId(String baseStoreId) {
        BaseStoreId = baseStoreId;
    }

    private static boolean isToast = true;

    public static void logMessage(String message, Context context) {
        if (isToast)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    String BaseStoreId;
}
