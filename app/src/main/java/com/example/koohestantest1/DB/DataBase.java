package com.example.koohestantest1.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {

    private String ErrorMessage = "null";

    private static final String DATABASE_NAME = "DehkadeDB";

    public static String getBaseTable() {
        return BASE_TABLE;
    }

    public static String getUserId() {
        return USER_ID;
    }

    public static String getTOKEN() {
        return TOKEN;
    }

    public static String getDeviceModel() {
        return DEVICE_MODEL;
    }

    public static String getIMEI() {
        return IMEI;
    }

    public static String getMobileNumber() {
        return MOBILE_NUMBER;
    }

    public static String getUserName() {
        return USER_NAME;
    }

    public static String getPASSWORD() {
        return PASSWORD;
    }

    public static String[] getBaseTableField() {
        return BaseTableField;
    }

    //DATA BASE
    public static final String BASE_TABLE = "baseTable";

    //Table
    public static final String Company_TABLE = "CompanyTable";
    public static final String CART_TABLE = "CartTable";
    public static final String CART_PROPERTIES_TABLE = "CartPropertiesTable";

    //Field
    private static final String USER_ID = "UserId";
    private static final String TOKEN = "token";
    private static final String DEVICE_MODEL = "deviceModel";
    private static final String IMEI = "imei";
    private static final String MOBILE_NUMBER = "mobileNumber";
    private static final String USER_NAME = "userName";
    private static final String PASSWORD = "password";
    private static final String Comany_ID = "CompanyID";
    private static final String Comany_Name = "CompanyName";
    private static final String ORDER_ID = "OrderID";
    private static final String EMPLOYEE_ID = "EmployeeID";
    private static final String CUSTOMER_ID = "CustomerID";
    private static final String ORDER_DATE = "OrderDate";
    private static final String SHIPPED_DATE = "ShippedDate";
    private static final String SHIPPER_ID = "ShipperID";
    private static final String TAXE = "Taxe";
    private static final String PAYMENT_TYPE = "PaymentType";
    private static final String PAID_DATE = "PaidDate";
    private static final String NOTES = "Notes";
    private static final String STATUS_ID = "StatusID";
    private static final String SUM_PRICE = "SumPrice";
    private static final String PRODUCT_ID = "ProductID";
    private static final String PROPERTIES_GROUP = "PropertiesGroup";
    private static final String PROPERTIES_NAME = "PropertiesName";
    private static final String PROPERTIES_VALUE = "PropertiesValue";
    private static final String UPDATE_TIME = "UpdateTime";
    private static final String PRICE = "Price";

    public static String getComany_ID() {
        return Comany_ID;
    }

    public static String getComany_Name() {
        return Comany_Name;
    }


    private static final String Base_MOD = "DROP IF TABLE EXISTS ";

    //TableField
    public static final String[] BaseTableField = {USER_ID, TOKEN, DEVICE_MODEL, IMEI, MOBILE_NUMBER, USER_NAME, PASSWORD};
    public static final String[] CompanyTableField = {Comany_ID, Comany_Name};
    public static final String[] CartTableField = {TOKEN, USER_ID, ORDER_ID, EMPLOYEE_ID, CUSTOMER_ID, Comany_ID, ORDER_DATE,
            SHIPPED_DATE, SHIPPER_ID, TAXE, PAYMENT_TYPE, PAID_DATE, NOTES, STATUS_ID, SUM_PRICE};
    public static final String[] CartPropertiesField = {PRODUCT_ID, PROPERTIES_GROUP, PROPERTIES_NAME, PROPERTIES_VALUE, UPDATE_TIME, PRICE};


    public static String[] getCompanyTableField() {
        return CompanyTableField;
    }


    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        ErrorMessage = null;
        if (createTable(db, BASE_TABLE, BaseTableField)) {
//            addData(BaseTableField,BASE_TABLE,BaseTableField);
        } else {
        }

        if (createTable(db, Company_TABLE, CompanyTableField)) {
            addData(CompanyTableField, Company_TABLE, CompanyTableField);
        }
        if (createTable(db, CART_TABLE, CartTableField)) {
            addData(CartTableField, CART_TABLE, CartTableField);
        }
        if (createTable(db, CART_PROPERTIES_TABLE, CartPropertiesField)) {
            addData(CartPropertiesField, CART_PROPERTIES_TABLE, CartPropertiesField);
        } else {
        }
    }

    private void createDatabase(SQLiteDatabase db) {
        ErrorMessage = null;
        if (createTable(db, BASE_TABLE, BaseTableField)) {
//            addData(BaseTableField,BASE_TABLE,BaseTableField);
        } else {
        }

        if (createTable(db, Company_TABLE, CompanyTableField)) {
            addData(CompanyTableField, Company_TABLE, CompanyTableField);
        }
        if (createTable(db, CART_TABLE, CartTableField)) {
            addData(CartTableField, CART_TABLE, CartTableField);
        }
        if (createTable(db, CART_PROPERTIES_TABLE, CartPropertiesField)) {
            addData(CartPropertiesField, CART_PROPERTIES_TABLE, CartPropertiesField);
        } else {
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Base_MOD + BASE_TABLE);
        db.execSQL(Base_MOD + Company_TABLE);
    }//end public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)

    public boolean createTable(SQLiteDatabase db, String tableName, String[] field) {
        try {
            String createTable = "CREATE TABLE " + tableName + " (ID INTEGER PRIMARY KEY AUTOINCREMENT";
            for (String a : field
            ) {
                createTable += ", " + a;
            }
            createTable += " TEXT)";
            db.execSQL(createTable);
            return true;
        } catch (Exception e) {
            ErrorMessage = e.getMessage().toString();
            return false;
        }
    }//end public boolean createTable(SQLiteDatabase db, String tableName, String[] field)

    public boolean addData(String[] item, String tableName, String[] field) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            int i = 0;
            for (String a : field
            ) {
                contentValues.put(a, item[i]);
                i++;
            }

            long result = db.insert(tableName, null, contentValues);

            //if data as inserted incorrectly it will return -1

            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            ErrorMessage = e.getMessage().toString();
            return false;
        }
    }//end public boolean addData

    public Cursor getAllData(String tableName) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "SELECT * FROM " + tableName;
            Cursor data = db.rawQuery(query, null);
            return data;
        } catch (Exception e) {
            ErrorMessage = e.getMessage().toString();
            return null;
        }
    }//end public Cursor getAllData

    public void updateToken(String newToken) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + BASE_TABLE + " SET " + TOKEN + " = '" + newToken + "' WHERE ID = 1";
        db.execSQL(query);
    }// end public void updateToken

    public void updateUserID(String newUserID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + BASE_TABLE + " SET " + USER_ID + " = '" + newUserID + "' WHERE ID = 1";
        db.execSQL(query);
    }//end public void updateUserI

    public void updateUserPass(String userName, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + BASE_TABLE + " SET " + USER_NAME + " = '" + userName + "' WHERE ID = 1";
        db.execSQL(query);
        updatePass(password);
    }

    public void updatePass(String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + BASE_TABLE + " SET " + PASSWORD + " = '" + password + "' WHERE ID = 1";
        db.execSQL(query);
    }

    public void updateIMEIModel(String imei, String model) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + BASE_TABLE + " SET " + IMEI + " = '" + imei + "' WHERE ID = 1";
        db.execSQL(query);
        updateModel(model);
    }

    public void updateModel(String model) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + BASE_TABLE + " SET " + DEVICE_MODEL + " = '" + model + "' WHERE ID = 1";
        db.execSQL(query);
    }

    public void updateCompanyID(String Company) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + Company_TABLE + " SET " + Comany_ID + " = '" + Company + "' WHERE ID = 1";
        db.execSQL(query);
    }// end public void updateTo

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();

//        db.execSQL("DELETE FROM " + Company_TABLE);
//        db.execSQL("DELETE FROM " + CART_TABLE);
//        db.execSQL("DELETE FROM " + CART_PROPERTIES_TABLE);

//        db.execSQL("DELETE FROM " + BASE_TABLE);

        SQLiteDatabase database = getWritableDatabase();
//        database.execSQL("DROP TABLE IF EXISTS " + Company_TABLE);
//        database.execSQL("DROP TABLE IF EXISTS " + CART_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + CART_PROPERTIES_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + BASE_TABLE);

        createDatabase(database);
        database.close();
    }
}
