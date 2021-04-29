package com.example.koohestantest1.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.List;

import com.example.koohestantest1.ViewModels.Order_DetailsViewModels;
import com.example.koohestantest1.classDirectory.ProductPropertisClass;
import com.example.koohestantest1.classDirectory.SendOrderClass;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

//safty
public class MyDataBase extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "data.db";
    public static final int DATABASE_VERSION = 1;
    public final SQLiteDatabase database;

    private final String TAG = MyDataBase.class.getSimpleName();

    public static final String Category = "Category";
    public static final String Deleted = "Deleted";
    public static final String Deleted1 = "Deleted1";
    public static final String Description = "Description";
    public static final String Discontinued = "Discontinued";
    public static final String id_ = "id";
    public static final String ListPrice = "ListPrice";
    public static final String MinimumReorderQuantity = "MinimumReorderQuantity";
    public static final String ProductID = "ProductID";
    public static final String ProductName = "ProductName";
    public static final String QuantityPerUnit = "QuantityPerUnit";
    public static final String ReorderLevel = "ReorderLevel";
    public static final String Show = "Show";
    public static final String Spare1 = "Spare1";
    public static final String Spare2 = "Spare2";
    public static final String Spare3 = "Spare3";
    public static final String StandardCost = "StandardCost";
    public static final String SupplierID = "SupplierID";
    public static final String TargetLevel = "TargetLevel";
    public static final String Unit = "Unit";
    public static final String UpdateDate = "UpdateDate";
    public static final String SellCount = "SellCount";


    public static final String UpdateTime = "UpdatTime";
    public static final String deleted = "deleted";
    public static final String PropertisGroup = "PropertisGroup";
    public static final String PropertisName = "PropertisName";
    public static final String PropertisValue = "PropertisValue";
    public static final String CopanyID = "CopanyID";
    public static final String CustomerID = "CustomerID";
    public static final String EmployeeID = "EmployeeID";

    public static final String Notes = "Notes";
    public static final String OrderDate = "OrderDate";
    public static final String OrderID = "OrderID";
    public static final String PaidDate = "PaidDate";
    public static final String PaymentType = "PaymentType";
    public static final String ShipAddress = "ShipAddress";
    public static final String ShipCity = "ShipCity";
    public static final String ShipCountryRegion = "ShipCountryRegion";
    public static final String ShipName = "ShipName";
    public static final String ShippedDate = "ShippedDate";
    public static final String ShipperID = "ShipperID";
    public static final String ShippingFee = "ShippingFee";
    public static final String ShipStateProvince = "ShipStateProvince";
    public static final String ShipZIPPostalCode = "ShipZIPPostalCode";

    public static final String StatusID = "StatusID";
    public static final String Taxes = "Taxes";
    public static final String TaxRate = "TaxRate";
    public static final String TaxStatus = "TaxStatus";

    public static final String DateAllocated = "DateAllocated";
    public static final String Discount = "Discount";
    public static final String ID = "ID";
    public static final String InventoryID = "InventoryID";
    public static final String PurchaseOrderID = "PurchaseOrderID";
    public static final String Quantity = "Quantity";
    public static final String UnitPrice = "UnitPrice";
    public static final String UpdatTime = "UpdatTime";

    public MyDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.database = getReadableDatabase();
    }

    public MyDataBase(Context context, String name, String storageDirectory, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, storageDirectory, factory, version);
        this.database = getReadableDatabase();
    }

    public MyDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.database = getReadableDatabase();
    }

    public void CloseDB() {
        database.close();
    }

    public Cursor GetAllRawOfTable(Context context, String tableName) {
        try {
            tableName = tableName.replace("dbo.", "");
            String sql = "SELECT * FROM \"main\"." + "\"dbo." + tableName + "\"";
            return database.rawQuery(sql, null);
        } catch (Exception e) {
            Toast.makeText(context, "GetAllRawOfTable<0>" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }

//    public int RowCountOfTable (Context context, String tableName)
//    {
//        try {
//            tableName=   tableName.replace("dbo.", "");
//            String sql = "SELECT COUNT * FROM \"main\"." + "\"dbo." + tableName + "\"";
////            return database.query
//        }
//        catch (Exception e)
//        {
//            Toast.makeText(context, "GetAllRawOfTable<0>" + e.getMessage(), Toast.LENGTH_LONG).show();
//        }
//        return null;
//    }

    public Cursor GetFilterRawOfTable(Context context, String tableName, String field, String filter) {
        try {
            if (filter.equals("") || filter == null || filter.isEmpty()) {
                return GetAllRawOfTable(context, tableName);
            }
            tableName = tableName.replace("dbo.", "");
            String sql = "SELECT * FROM \"main\"." + "\"dbo." + tableName + "\" WHERE \"" + field + "\" = \"" + filter + "\"";
            return database.rawQuery(sql, null);
        } catch (Exception e) {
            Toast.makeText(context, "GetAllRawOfTable<0>" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }


    public String[] GetTablesName(Context context) {
        String[] tablelist;
        int i = 0;
        try {
            Cursor cursor = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
            tablelist = new String[cursor.getCount()];
            cursor.moveToFirst();
            do {
                try {
                    tablelist[i] = cursor.getString(0);
                    i++;
                } catch (Exception e) {
                    Toast.makeText(context, "GetTablesName<0>" + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            } while (cursor.moveToNext());
            return tablelist;
        } catch (Exception e) {
            Toast.makeText(context, "GetTablesName<1>" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        //  database.insert()
        return null;
    }

    public long insertToTble(String tableName, ContentValues Value) {
        try {
            return database.insert(tableName, null, Value);
        } catch (Exception e) {
        }
        return -1;
    }

    public long UpdateTable(String tableName, ContentValues Value, String where, String WherC[]) {
        try {
            return database.update(tableName, Value, where, WherC);
        } catch (Exception e) {
        }
        return -1;
    }


    public String[] GetTableColumesName(Context context, String tableName) {

        try {
            Cursor dbCursor = GetAllRawOfTable(context, tableName);//database.query(tableName, null, null, null, null, null, null);
            String[] colums = dbCursor.getColumnNames();
            return colums;
        } catch (Exception e) {
            Toast.makeText(context, "GetTableColumesName<0>" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }


    public int SetAllProductEnable(Context context, String CompanyID) {
        String TBLNAME = "\"main\"." + "\"dbo.Product\"";
        try {

            ContentValues cv = new ContentValues();
            cv.put(Deleted1, "Exist");
            return database.update(TBLNAME, cv, deleted + "<>? " + "AND " + SupplierID + "=? ", new String[]{"Exist", CompanyID});
            // return 1;
        } catch (Exception ex) {
            Toast.makeText(context, "SetAllProductEnable<0>" + ex.getMessage(), Toast.LENGTH_LONG).show();
            return 0;
        }
    }

    public Single<Integer> setAllProductEnabledAsync(String companyId) {
        return Single.create((SingleOnSubscribe<Integer>) emitter -> {
            String TBLNAME = "\"main\"." + "\"dbo.Product\"";
            try {

                ContentValues cv = new ContentValues();
                cv.put(Deleted1, "Exist");
                int updatedId = database.update(TBLNAME, cv, deleted + "<>? " + "AND " + SupplierID + "=? ", new String[]{"Exist", companyId});
                emitter.onSuccess(updatedId);
            } catch (Exception ex) {
                emitter.onError(ex);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


    }

    public int DeletNotExsistProduct(Context context) {
        String TBLNAME = "\"main\"." + "\"dbo.Product\"";
        try {

            return database.delete(TBLNAME, Deleted1 + "=?", new String[]{"Exist"});
        } catch (Exception ex) {
            Toast.makeText(context, "DeletNotExsistProduct<0>" + ex.getMessage(), Toast.LENGTH_LONG).show();
            return 0;
        }
    }

    public int deleteAllProduct(Context context) {
        deleteAllProductProperties(context);
        String TBLNAME = "\"main\"." + "\"dbo.Product\"";
        try {

            return database.delete(TBLNAME, ProductName + "<>?", new String[]{"5"});
        } catch (Exception ex) {
            Toast.makeText(context, "DeletNotExsistProduct<0>" + ex.getMessage(), Toast.LENGTH_LONG).show();
            return 0;
        }
    }

    public int deleteAllProductProperties(Context context) {
        String TBLNAME = "\"main\"." + "\"dbo.ProductPropertis\"";
        try {

            return database.delete(TBLNAME, ProductID + "<>?", new String[]{"5"});
        } catch (Exception ex) {
            Toast.makeText(context, "DeletNotExsistProduct<0>" + ex.getMessage(), Toast.LENGTH_LONG).show();
            return 0;
        }
    }

    public long insertProduct(Context context,
                              String Category_,
                              boolean Deleted_,
                              String Deleted1_,
                              String Description_,
                              String Discontinued_,
                              String ID_,
                              String ListPrice_,
                              int MinimumReorderQuantity_,
                              String ProductID_,
                              String ProductName_,
                              String QuantityPerUnit_,
                              int ReorderLevel_,
                              boolean Show_,
                              String Spare1_,
                              String Spare2_,
                              String Spare3_,
                              String StandardCost_,
                              String SupplierID_,
                              int TargetLevel_,
                              String Unit_,
                              long UpdateDate_,
                              boolean LikeIt,
                              boolean SaveIt,
                              int SellCount_
    ) {

        try {
            String TBLNAME = "\"main\"." + "\"dbo.Product\"";
            String whereclause = ProductID + "=? AND " + SupplierID + "=? ";
            String[] whereargs = new String[]{
                    ProductID_,
                    SupplierID_,
            };


            ContentValues cv = new ContentValues();
            cv.put(Category, Category_);
            cv.put(Deleted, Deleted_);
            cv.put(Deleted1, Deleted1_);
            cv.put(Description, Description_);
            cv.put(Discontinued, Discontinued_);
            //   cv.put(id_, null_);
            cv.put(ListPrice, ListPrice_);
            cv.put(MinimumReorderQuantity, MinimumReorderQuantity_);
            cv.put(ProductID, ProductID_);
            cv.put(ProductName, ProductName_);
            cv.put(QuantityPerUnit, QuantityPerUnit_);
            cv.put(ReorderLevel, ReorderLevel_);
            cv.put(Show, Show_);
            cv.put(Spare1, Spare1_);
            cv.put(Spare2, Spare2_ + "&" + LikeIt);
            cv.put(Spare3, Spare3_ + "&" + SaveIt);
            cv.put(StandardCost, StandardCost_);
            cv.put(SupplierID, SupplierID_);
            cv.put(TargetLevel, TargetLevel_);
            cv.put(Unit, Unit_);
            cv.put(UpdateDate, UpdateDate_);
            //TODO Add  this field to to com.example.koohestantest1.DB
            cv.put(SellCount, SellCount_);

            Cursor csr = database.query(TBLNAME, null, whereclause, whereargs, null, null, null);
            boolean rowexists = (csr.getCount() > 0);

            if (rowexists) {
                // Don't insert if the poem already exists
                csr.moveToFirst();
                database.update(TBLNAME, cv, whereclause, whereargs);
                if (csr.getString(csr.getColumnIndex(UpdateDate)).equals(UpdateDate_)) {
                    csr.close();
                    return -2;
                } else {
                    csr.close();
                    return -3;
                }

            }
            csr.close();
            return database.insert(TBLNAME, null, cv);
        } catch (Exception ex) {
            Toast.makeText(context, "insertProduct<0>" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return -1;
    }

    public long insertProductProperties(Context context, String deleted_, String id_,
                                        String ProductID_, String PropertisGroup_, String PropertisName_, String PropertisValue_,
                                        String Spare1_, String Spare2_, String Spare3_, String UpdatTime_) {
        try {
            String TBLNAME = "\"main\"." + "\"dbo.ProductPropertis\"";
            String whereclause = ProductID + "=?  AND " + PropertisName + "=? ";
            String[] whereargs = new String[]{
                    ProductID_,
                    PropertisName_
            };
            ContentValues cv = new ContentValues();
            cv.put(ProductID, ProductID_);
            cv.put(Spare1, Spare1_);
            cv.put(Spare2, Spare2_);
            cv.put(Spare3, Spare3_);
            cv.put(PropertisGroup, PropertisGroup_);
            cv.put(PropertisName, PropertisName_);
            cv.put(PropertisValue, PropertisValue_);
            cv.put(UpdateTime, UpdatTime_);
            cv.put(deleted, deleted_);
            Cursor csr = database.query(TBLNAME, null, whereclause, whereargs, null, null, null);
            boolean rowexists = (csr.getCount() > 0);

            if (rowexists) {
                // Don't insert if the poem already exists
                csr.moveToFirst();
                database.update(TBLNAME, cv, whereclause, whereargs);
                if (csr.getString(csr.getColumnIndex(UpdateTime)).equals(UpdatTime_)) {
                    csr.close();
                    Log.d(TAG, "insertProductProperties: -2");
                    return -2;
                } else {
                    Log.d(TAG, "insertProductProperties: -3");
                    csr.close();
                    return -3L;
                }
            }

            csr.close();
            long id = database.insert(TBLNAME, null, cv);
            return id;
        } catch (Exception ex) {
            Log.d(TAG, "insertProductProperties: " + ex.getMessage());
            return -1;
        }
    }

    public Single<Long> insertProductPropertiesAsync(Context context, String deleted_, String id_,
                                                     String ProductID_, String PropertisGroup_, String PropertisName_, String PropertisValue_,
                                                     String Spare1_, String Spare2_, String Spare3_, String UpdatTime_) {

        Log.d(TAG, "insertProductProperties: " + Thread.currentThread().getName());

        return Single.create((SingleOnSubscribe<Long>) emitter -> {
            Log.d(TAG, "insertProductProperties: " + Thread.currentThread().getName());
            try {
                String TBLNAME = "\"main\"." + "\"dbo.ProductPropertis\"";
                String whereclause = ProductID + "=?  AND " + PropertisName + "=? ";
                String[] whereargs = new String[]{
                        ProductID_,
                        PropertisName_
                };
                ContentValues cv = new ContentValues();
                cv.put(ProductID, ProductID_);
                cv.put(Spare1, Spare1_);
                cv.put(Spare2, Spare2_);
                cv.put(Spare3, Spare3_);
                cv.put(PropertisGroup, PropertisGroup_);
                cv.put(PropertisName, PropertisName_);
                cv.put(PropertisValue, PropertisValue_);
                cv.put(UpdateTime, UpdatTime_);
                cv.put(deleted, deleted_);
                Cursor csr = database.query(TBLNAME, null, whereclause, whereargs, null, null, null);
                boolean rowexists = (csr.getCount() > 0);

                if (rowexists) {
                    // Don't insert if the poem already exists
                    csr.moveToFirst();
                    database.update(TBLNAME, cv, whereclause, whereargs);
                    if (csr.getString(csr.getColumnIndex(UpdateTime)).equals(UpdatTime_)) {
                        csr.close();
                        Log.d(TAG, "insertProductProperties: -2");
                        emitter.onSuccess(-2L);
                    } else {
                        Log.d(TAG, "insertProductProperties: -3");
                        csr.close();
                        emitter.onSuccess(-3L);
                    }
                }

                csr.close();
                long id = database.insert(TBLNAME, null, cv);
                emitter.onSuccess(id);
            } catch (Exception ex) {
                emitter.onError(ex);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Cursor GetProductViwe(Context context, String ProductID) {
        try {
            String sql = "SELECT * FROM \"main\"." + "\"dbo.ViwePost\"" + " WHERE \"ProductID\"" + " ==\"" + ProductID + "\"";
            return database.rawQuery(sql, null);
        } catch (Exception e) {
            Toast.makeText(context, "GetProductViwe<0>" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }


    public Cursor GetProduct(Context context, String ProductID) {
        try {
            String sql = "SELECT * FROM \"main\"." + "\"dbo.Product\"" + " WHERE \"ProductID\"" + " ==\"" + ProductID + "\"";
            return database.rawQuery(sql, null);
        } catch (Exception e) {
            Toast.makeText(context, "GetProductViwe<0>" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }

    public Cursor GetProductProperties(Context context, String ProductID) {
        try {
            String sql = "SELECT * FROM \"main\"." + "\"dbo.ProductPropertis\"" + " WHERE \"ProductID\"" + " ==\"" + ProductID + "\"";
            return database.rawQuery(sql, null);
        } catch (Exception e) {
            Toast.makeText(context, "GetProductViwe<0>" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }


    public long insertOrder(Context context, SendOrderClass orderClass) {

        try {
            String TBLNAME = "\"main\"." + "\"dbo.Orders\"";
            String whereclause;//= OrderDate + "=? OR " + OrderID + "=? ";
            String[] whereargs;/*= new String[]{
                    orderClass.getOrderDate()
            };*/
            if (orderClass.getOrderID() == null || orderClass.getOrderID() == "" || orderClass.getOrderID().isEmpty()) {
                whereclause = OrderDate + "=? ";
                whereargs = new String[]{
                        orderClass.getOrderDate()
                };
            } else {
                whereclause = OrderID + "=? ";
                whereargs = new String[]{
                        orderClass.getOrderID()
                };
            }
            ContentValues cv = new ContentValues();
            cv.put(CopanyID, orderClass.getCompanyID());
            cv.put(CustomerID, orderClass.getCustomerID());
            cv.put(Deleted, "false");
            cv.put(EmployeeID, orderClass.getEmployeeID());
            // cv.put( id_, orderClass.get);
            cv.put(Notes, orderClass.getNotes());
            cv.put(OrderDate, orderClass.getOrderDate());
            cv.put(OrderID, orderClass.getOrderID());
            cv.put(PaidDate, orderClass.getPaidDate());
            cv.put(PaymentType, orderClass.getPaymentType());
            cv.put(ShipAddress, orderClass.getShipAddress());
            cv.put(ShipCity, orderClass.getShipCity());
            cv.put(ShipCountryRegion, orderClass.getShipCountryRegion());
            cv.put(ShipName, orderClass.getShipName());
            cv.put(ShippedDate, orderClass.getShippedDate());
            cv.put(ShipperID, orderClass.getShipperID());
            cv.put(ShippingFee, orderClass.getShippingFee());
            cv.put(ShipStateProvince, orderClass.getShipStateProvince());
            cv.put(ShipZIPPostalCode, orderClass.getShipZIPPostalCode());
            //  cv.put( Spare1, orderClass.getsp);
            // cv.put( Spare2, orderClass.get);
            //  cv.put( Spare3, orderClass.get);
            cv.put(StatusID, orderClass.getStatusID());
            cv.put(Taxes, orderClass.getTaxes());
            cv.put(TaxRate, orderClass.getTaxRate());
            cv.put(TaxStatus, orderClass.getTaxStatus());
            //   cv.put( UpdateDate, orderClass.get);
            Cursor csr = database.query(TBLNAME, null, whereclause, whereargs, null, null, null);
            boolean rowexists = (csr.getCount() > 0);
            if (rowexists) {
                // Don't insert if the poem already exists
                csr.close();
                database.update(TBLNAME, cv, whereclause, whereargs);

            } else database.insert(TBLNAME, null, cv);
            csr.close();
            insertOrderDetails(context, orderClass.getOrder_Details());
        } catch (Exception ex) {
            Toast.makeText(context, "insertProduct<0>" + ex.getMessage(), Toast.LENGTH_LONG).show();
            return -1;
        }
        return 1;
    }

    public long insertOrderDetails(Context context, List<Order_DetailsViewModels> orderDetailses) {
        String od = "";
        String pd = "";
        String[] whereargs = new String[2];
        try {
            String TBLNAME = "\"main\"." + "\"dbo.Order Details\"";
            String whereclause = OrderID + "=? AND " + ProductID + "=? ";

            for (Order_DetailsViewModels orderDetails : orderDetailses) {
                od = orderDetails.getOrderID();
                pd = orderDetails.getProductID();
                whereargs = new String[]{
                        orderDetails.getOrderID(),
                        orderDetails.getProductID()
                };

                ContentValues cv = new ContentValues();
                cv.put(DateAllocated, orderDetails.getDateAllocated());
                cv.put(Discount, orderDetails.getDiscount());
                cv.put(ID, orderDetails.getID());
                cv.put(InventoryID, orderDetails.getInventoryID());
                cv.put(OrderID, orderDetails.getOrderID());
                cv.put(ProductID, orderDetails.getProductID());
                cv.put(PurchaseOrderID, orderDetails.getPurchaseOrderID());
                cv.put(Quantity, orderDetails.getQuantity());
                //   cv.put(Spare1,orderDetails.getSp);
                //    cv.put(Spare2,orderDetails.get);
                //  cv.put(Spare3,orderDetails.get);
                cv.put(StatusID, orderDetails.getStatusID());
                cv.put(UnitPrice, orderDetails.getUnitPrice());


                Cursor csr = database.query(TBLNAME, null, whereclause, whereargs, null, null, null);
                boolean rowexists = (csr.getCount() > 0);
                if (rowexists) {
                    // Don't insert if the poem already exists
                    csr.moveToFirst();
                    database.update(TBLNAME, cv, whereclause, whereargs);
                    // return -3;

                } else database.insert(TBLNAME, null, cv);
                csr.close();
                insertOrderDetailsProductProprty(context, orderDetails.getPropertisViewModels(), orderDetails.getOrderID());
            }
        } catch (Exception ex) {
            Toast.makeText(context, "insertOrderDetails<0>" + whereargs[0] + ">>" + whereargs[1] + ex.getMessage(), Toast.LENGTH_LONG).show();
            return -1;
        }

        return 1;
    }

    public long insertOrderDetailsProductProprty(Context context, List<ProductPropertisClass> orderDetailsPPs, String OrderID) {

        try {
            String TBLNAME = "\"main\"." + "\"dbo.ProductPropertis\"";
            String whereclause = ProductID + "=? AND " + PropertisName + "=? ";

            for (ProductPropertisClass orderDetailsPP : orderDetailsPPs) {
                String PRoID = "O" + OrderID + orderDetailsPP.getProductID();
                String[] whereargs = new String[]{
                        PRoID,
                        orderDetailsPP.getPropertisName()
                };

                ContentValues cv = new ContentValues();
                cv.put(deleted, "false");
                // cv.put(id,orderDetailsPP.get);
                cv.put(ProductID, PRoID);
                cv.put(PropertisGroup, orderDetailsPP.getPropertisGroup());
                cv.put(PropertisName, orderDetailsPP.getPropertisName());
                cv.put(PropertisValue, orderDetailsPP.getPropertisValue());
                // cv.put(Spare1,orderDetailsPP.get);
                // cv.put(Spare2,orderDetailsPP.get);
                // cv.put(Spare3,orderDetailsPP.get);
                cv.put(UpdatTime, orderDetailsPP.getUpdatTime());


                Cursor csr = database.query(TBLNAME, null, whereclause, whereargs, null, null, null);
                boolean rowexists = (csr.getCount() > 0);
                if (rowexists) {
                    // Don't insert if the poem already exists
                    csr.moveToFirst();
                    database.update(TBLNAME, cv, whereclause, whereargs);
                }
                csr.close();
                database.insert(TBLNAME, null, cv);
            }
        } catch (Exception ex) {
            Toast.makeText(context, "insertOrderDetailsProductProprty<0>" + ex.getMessage(), Toast.LENGTH_LONG).show();
            return -1;
        }
        return 1;
    }

    public SendOrderClass GetOrder(Context context, String OrdrId) {
        String TBLNAME = "\"main\"." + "\"dbo.Orders\"";
        String TBLOrderDetail = "\"main\"." + "\"dbo.Order Details\"";
        // String TBLProductProprty = "\"main\"." + "\"dbo.ProductPropertis\"";
        String whereclause = OrderID + "=? ";
        String[] whereargs = new String[]{
                OrdrId
        };
        SendOrderClass orderClass = new SendOrderClass();
        String er = "1";
        try {
            Cursor csr = database.query(TBLNAME, null, whereclause, whereargs, null, null, null);
            if (csr.moveToFirst()) {
                orderClass.setCompanyID(csr.getString(csr.getColumnIndex(CopanyID)));
                orderClass.setCustomerID(csr.getString(csr.getColumnIndex(CustomerID)));
                orderClass.setEmployeeID(csr.getString(csr.getColumnIndex(EmployeeID)));
                orderClass.setOrderID(csr.getString(csr.getColumnIndex(OrderID)));
                orderClass.setNotes(csr.getString(csr.getColumnIndex(Notes)));
                orderClass.setOrderDate(csr.getString(csr.getColumnIndex(OrderDate)));
                orderClass.setPaidDate(csr.getString(csr.getColumnIndex(PaidDate)));
                orderClass.setPaymentType(csr.getString(csr.getColumnIndex(PaymentType)));
                orderClass.setShipAddress(csr.getString(csr.getColumnIndex(ShipAddress)));
                orderClass.setShipCity(csr.getString(csr.getColumnIndex(ShipCity)));
                orderClass.setShipCountryRegion(csr.getString(csr.getColumnIndex(ShipCountryRegion)));
                orderClass.setShipName(csr.getString(csr.getColumnIndex(ShipName)));
                orderClass.setShippedDate(csr.getString(csr.getColumnIndex(ShippedDate)));
                orderClass.setShipperID(csr.getString(csr.getColumnIndex(ShipperID)));
                orderClass.setShippingFee(csr.getString(csr.getColumnIndex(ShippingFee)));
                orderClass.setShipStateProvince(csr.getString(csr.getColumnIndex(ShipStateProvince)));
                orderClass.setShipZIPPostalCode(csr.getString(csr.getColumnIndex(ShipZIPPostalCode)));
                orderClass.setStatusID(csr.getString(csr.getColumnIndex(StatusID)));
                orderClass.setSumPrice(csr.getString(csr.getColumnIndex(Spare1)));
                orderClass.setTaxes(csr.getString(csr.getColumnIndex(Taxes)));
                orderClass.setTaxRate(csr.getString(csr.getColumnIndex(TaxRate)));
                orderClass.setTaxStatus(csr.getString(csr.getColumnIndex(TaxStatus)));
                csr.close();
                er = "4";
                csr = database.query(TBLOrderDetail, null, whereclause, whereargs, null, null, null);
                if (csr.moveToFirst()) {
                    Order_DetailsViewModels orderDetails = new Order_DetailsViewModels();
                    do {
                        orderDetails.setDateAllocated(csr.getString(csr.getColumnIndex(DateAllocated)));
                        orderDetails.setDiscount(csr.getString(csr.getColumnIndex(Discount)));
                        orderDetails.setID(csr.getString(csr.getColumnIndex(ID)));
                        orderDetails.setInventoryID(csr.getString(csr.getColumnIndex(InventoryID)));
                        orderDetails.setOrderID(csr.getString(csr.getColumnIndex(OrderID)));
                        orderDetails.setProductID(csr.getString(csr.getColumnIndex(ProductID)));
                        orderDetails.setPurchaseOrderID(csr.getString(csr.getColumnIndex(PurchaseOrderID)));
                        orderDetails.setQuantity(Integer.parseInt(csr.getString(csr.getColumnIndex(Quantity))));
                        orderDetails.setStatusID(csr.getString(csr.getColumnIndex(StatusID)));
                        orderDetails.setUnitPrice(csr.getString(csr.getColumnIndex(UnitPrice)));

                        er = "5";

                        Cursor cursor = GetProductProperties(context, "O" + orderDetails.getOrderID() + orderDetails.getProductID());
                        if (cursor.moveToFirst()) {
                            do {
                                ProductPropertisClass propertisClass = new ProductPropertisClass(cursor.getString(cursor.getColumnIndex(ProductID)),
                                        cursor.getString(cursor.getColumnIndex(PropertisGroup)), cursor.getString(cursor.getColumnIndex(PropertisName)),
                                        cursor.getString(cursor.getColumnIndex(PropertisValue)), cursor.getString(cursor.getColumnIndex(UpdateTime)));
                                orderDetails.getPropertisViewModels().add(propertisClass);
                            } while (cursor.moveToNext());
                        }
                        orderClass.getOrder_Details().add(orderDetails);
                    } while (csr.moveToNext());
                }
            } else {
                orderClass = null;
            }
        } catch (Exception ex) {
            Toast.makeText(context, "GetDefualtOrder<0>" + ">>>>" + er + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return orderClass;
    }


    public SendOrderClass GetDefualtOrder(Context context) {
        return GetOrder(context, "1");
    }
}
