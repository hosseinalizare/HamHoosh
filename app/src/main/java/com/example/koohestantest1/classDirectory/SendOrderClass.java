package com.example.koohestantest1.classDirectory;

import android.util.Log;

import com.example.koohestantest1.Utils.StringUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.koohestantest1.ViewModels.Order_DetailsViewModels;

public class SendOrderClass {
    private String TAG = SendOrderClass.class.getSimpleName();
    private String Token;
    private String UserID;
    private String OrderID;
    private String EmployeeID;
    private String CustomerID;
    private String CompanyID;
    private String OrderDate;
    private String ShippedDate;
    private String ShipperID;
    private String ShipName;
    private String ShipAddress;
    private String ShipCity;
    private String ShipStateProvince;
    private String ShipZIPPostalCode;
    private String ShipCountryRegion;
    private String ShippingFee;
    private String Taxes;
    private String PaymentType;
    private String PaidDate;
    private String Notes;
    private String TaxRate;
    private String TaxStatus;
    private String StatusID;
    private String UpdateDate ;
    private String  Deleted ;
    private String  Spare1 ;
    private String  Spare2;
    private String  Spare3 ;
    private String SumPrice;//کل هزینه این سفارش بدون در نظر کرفتن هزینه حمل و نقل و هزینه مالیات و تخفیفها
    private String id;
    public List<Order_DetailsViewModels> Order_Details;


    private String SumDiscount;
    private String SumTotal;

    public SendOrderClass(String token, String userID, String orderID, String employeeID, String customerID, String companyID, String orderDate, String shippedDate, String shipperID, String shipName, String shipAddress, String shipCity, String shipStateProvince, String shipZIPPostalCode, String shipCountryRegion, String shippingFee, String taxes, String paymentType, String paidDate, String notes, String taxRate, String taxStatus, String statusID, String sumPrice) {
        Token = token;
        UserID = userID;
        OrderID = orderID;
        EmployeeID ="EmnJnjtsqhU";// employeeID;
        CustomerID = customerID;
        CompanyID = companyID;
        OrderDate = orderDate;
        ShippedDate = shippedDate;
        ShipperID = shipperID;
        ShipName = shipName;
        ShipAddress = shipAddress;
        ShipCity = shipCity;
        ShipStateProvince = shipStateProvince;
        ShipZIPPostalCode = shipZIPPostalCode;
        ShipCountryRegion = shipCountryRegion;
        ShippingFee = shippingFee;
        Taxes = taxes;
        PaymentType = paymentType;
        PaidDate = paidDate;
        Notes = notes;
        TaxRate = taxRate;
        TaxStatus = taxStatus;
        StatusID = statusID;
        SumPrice = sumPrice;

         UpdateDate="" ;
         Deleted="false" ;
          Spare1="" ;
         Spare2="";
          Spare3="";


        Order_Details = new ArrayList<>();
    }

    public SendOrderClass(){
        Order_Details = new ArrayList<>();
    }

    public void setSendOrderClass(String token, String userID, String orderID, String employeeID, String customerID, String companyID, String orderDate, String shippedDate, String shipperID, String shipName, String shipAddress, String shipCity, String shipStateProvince, String shipZIPPostalCode, String shipCountryRegion, String shippingFee, String taxes, String paymentType, String paidDate, String notes, String taxRate, String taxStatus, String statusID, String sumPrice) {
        Token = token;
        UserID = userID;
        OrderID = orderID;
        EmployeeID = "EmnJnjtsqhU";//employeeID;
        CustomerID = customerID;
        CompanyID = companyID;
        OrderDate = orderDate;
        ShippedDate = shippedDate;
        ShipperID = shipperID;
        ShipName = shipName;
        ShipAddress = shipAddress;
        ShipCity = shipCity;
        ShipStateProvince = shipStateProvince;
        ShipZIPPostalCode = shipZIPPostalCode;
        ShipCountryRegion = shipCountryRegion;
        ShippingFee = shippingFee;
        Taxes = taxes;
        PaymentType = paymentType;
        PaidDate = paidDate;
        Notes = notes;
        TaxRate = taxRate;
        TaxStatus = taxStatus;
        StatusID = statusID;
        SumPrice = sumPrice;
        UpdateDate="" ;
        Deleted="false" ;
        Spare1="" ;
        Spare2="";
        Spare3="";
    }

    public String getToken() {
        return Token;
    }

    public String getUserID() {
        return UserID;
    }

    public String getOrderID() {
        return OrderID;
    }

    public String getEmployeeID() {
        return EmployeeID;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public String getCompanyID() {
        return CompanyID;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public String getShippedDate() {
        return ShippedDate;
    }

    public String getShipperID() {
        return ShipperID;
    }

    public String getShipName() {
        return ShipName;
    }

    public String getShipAddress() {
        return ShipAddress;
    }

    public String getShipCity() {
        return ShipCity;
    }

    public String getShipStateProvince() {
        return ShipStateProvince;
    }

    public String getShipZIPPostalCode() {
        return ShipZIPPostalCode;
    }

    public String getShipCountryRegion() {
        return ShipCountryRegion;
    }

    public String getShippingFee() {
        return ShippingFee;
    }

    public String getTaxes() {
        return Taxes;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public String getPaidDate() {
        return PaidDate;
    }

    public String getNotes() {
        return Notes;
    }

    public String getTaxRate() {
        return TaxRate;
    }

    public String getTaxStatus() {
        return TaxStatus;
    }

    public String getStatusID() {
        return StatusID;
    }

    public String getSumPrice() {
        calculateSumPrice();
      return  SumPrice;
    }
    public boolean calculateSumPrice()
    {
        try {
            int sumPrice = 0;
            int sumDiscount = 0;
            int TotalPrice = 0;

            for (Order_DetailsViewModels o : getOrder_Details()
            ) {
                // Number ordered
                String price = o.getUnitPrice();
                int q = o.getQuantity();

                // Unit price
                float p = o.getUnitPrice() == null ? 0 : StringUtils.getNumberFromStringV2(price);

               // Discount amount
                float d = o.getDiscount() == null ? 0 : StringUtils.getNumberFromStringV2(o.getDiscount());//Integer.parseInt(o.getDiscount().replace(".", ""));

                // جمع تخفیف این محصول با محصولات قبلی
                sumDiscount += (q * d);

                // جمع قیمت این محصول با محصولات قبلی
                sumPrice += (p * q);
                Log.d(TAG, "getSumPrice: (2): " + sumPrice);

            }
            Log.d(TAG, "getSumPrice: (3): " + sumPrice);
            TotalPrice = sumPrice - sumDiscount;
            SumPrice = String.valueOf(sumPrice);
            Log.d(TAG, "getSumPrice: (4): " + sumPrice);

            SumDiscount = String.valueOf(sumDiscount);
            SumTotal = String.valueOf(TotalPrice);
            NumberFormat formatter = new DecimalFormat("#,###");
            if (SumPrice != null && !SumPrice.isEmpty()) {
                SumPrice = formatter.format(Integer.parseInt(SumPrice));
                Log.d(TAG, "getSumPrice: (5): " + SumPrice);
                SumDiscount = formatter.format(Integer.parseInt(SumDiscount));
                SumTotal = formatter.format(Integer.parseInt(SumTotal));
            }
            else{

            }
            String a = SumPrice + "&" + SumDiscount + "&" + SumTotal;
            Log.d(TAG, "End:: " + a);
            return true;
        }catch (Exception e){
            Log.d(TAG, "error: " + e.getMessage());
            return false;
        }
    }
  /*  public String calculateSumPrice() {
        try {
            int sumPrice = 0;
            int sumDiscount = 0;
            int sumP;
            //  String SumDiscount;
            //String SumP;
            for (Order_DetailsViewModels o : getOrder_Details()
            ) {
                int p = o.getSumPrice();
                int d = o.getSumOff();
                sumDiscount += (d);
                sumPrice += (p);
            }
            sumP = sumPrice - sumDiscount;
            NumberFormat formatter = new DecimalFormat("#,###");
//            if (SumPrice != null && !SumPrice.isEmpty()) {
                SumDiscount = formatter.format(sumDiscount);
                SumP = formatter.format(sumP);
                SumPrice = formatter.format(sumPrice);
//            } else {
//
//            }
            String a = SumPrice + "&" + SumDiscount + "&" + SumP;
            return a;
        }catch (Exception e){
            SumDiscount = "5555";//formatter.format(sumDiscount);
            SumP = "6666";//formatter.format(sumP);
            SumPrice = "7777";
            return e.getMessage() + "1 & 0 & 1";
        }
//        return SumPrice + "|" + SumDiscount + "|" + SumP;
    }*/

    public String getSumDisCount(){
        calculateSumPrice();
        return SumDiscount;
    }

   public String getSumTotal(){
       calculateSumPrice();
        return SumTotal;
    }

 /*   public String getSumPrice() {
        int bb = 0;
        try {
            double sumPrice = 0;
            double sumDiscount = 0;
            double sumP;
            bb++;
            for (Order_DetailsViewModels o : getOrder_Details()
            ) {
                try {
                double q = (o.getQuantity() == null) ? 1 : Double.parseDouble(o.getQuantity());
                double p = (o.getUnitPrice() == null) ? 1 : Double.parseDouble(o.getUnitPrice());
                double d = (o.getDiscount() == null) ? 1 : Double.parseDouble(o.getDiscount());
                sumDiscount += (q * d);
                sumPrice += (p * q);
                }catch (Exception e){
                    bb += 1000;
                }
            }
            bb++;
            sumP = sumPrice - sumDiscount;
            bb++;
            SumPrice = (long)Math.floor(sumPrice + 0.5d) + "";// String.valueOf(sumPrice);
            bb++;
            SumDiscount = String.valueOf(sumDiscount);
            bb++;
            SumP = String.valueOf(sumP);
            bb++;
            NumberFormat formatter = new DecimalFormat("#,###");
            bb = 800;
            if (SumPrice != null && !SumPrice.isEmpty()) {
                SumPrice = formatter.format(Integer.parseInt(SumPrice));
                bb++;
                SumDiscount = formatter.format(Double.parseDouble(SumDiscount));
                bb++;
                SumP = formatter.format(Double.parseDouble(SumP));
                bb++;
            } else {

            }
            String a = SumPrice + "&" + SumDiscount + "&" + SumP;
            bb++;
            return a;
        }catch (Exception e){
            String a = 5000 + "&" + 0 + "&" + 5000;
//            return a;
            return e.getMessage() + " & 666 & " + bb;
        }
//        return SumPrice + "|" + SumDiscount + "|" + SumP;
    }*/

    public void setToken(String token) {
        Token = token;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public void setEmployeeID(String employeeID) {
        EmployeeID = employeeID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public void setCompanyID(String companyID) {
        CompanyID = companyID;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public void setShippedDate(String shippedDate) {
        ShippedDate = shippedDate;
    }

    public void setShipperID(String shipperID) {
        ShipperID = shipperID;
    }

    public void setShipName(String shipName) {
        ShipName = shipName;
    }

    public void setShipAddress(String shipAddress) {
        ShipAddress = shipAddress;
    }

    public void setShipCity(String shipCity) {
        ShipCity = shipCity;
    }

    public void setShipStateProvince(String shipStateProvince) {
        ShipStateProvince = shipStateProvince;
    }

    public void setShipZIPPostalCode(String shipZIPPostalCode) {
        ShipZIPPostalCode = shipZIPPostalCode;
    }

    public void setShipCountryRegion(String shipCountryRegion) {
        ShipCountryRegion = shipCountryRegion;
    }

    public void setShippingFee(String shippingFee) {
        ShippingFee = shippingFee;
    }

    public void setTaxes(String taxes) {
        Taxes = taxes;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }

    public void setPaidDate(String paidDate) {
        PaidDate = paidDate;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public void setTaxRate(String taxRate) {
        TaxRate = taxRate;
    }

    public void setTaxStatus(String taxStatus) {
        TaxStatus = taxStatus;
    }

    public void setStatusID(String statusID) {
        StatusID = statusID;
    }

    public void setSumPrice(String sumPrice) {
        SumPrice = sumPrice;
    }



    public String getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateDate(String updateDate) {
        UpdateDate = updateDate;
    }

    public String getDeleted() {
        return Deleted;
    }

    public void setDeleted(String deleted) {
        Deleted = deleted;
    }

    public String getSpare1() {
        return Spare1;
    }

    public void setSpare1(String spare1) {
        Spare1 = spare1;
    }

    public String getSpare2() {
        return Spare2;
    }

    public void setSpare2(String spare2) {
        Spare2 = spare2;
    }

    public String getSpare3() {
        return Spare3;
    }

    public void setSpare3(String spare3) {
        Spare3 = spare3;
    }



    public List<Order_DetailsViewModels> getOrder_Details() {
        return Order_Details;
    }

    public void setOrder_Details(List<Order_DetailsViewModels> order_Details) {
        Order_Details = order_Details;
        calculateSumPrice();
    }

    @Override
    public String toString() {
        return "SendOrderClass{" +
                "TAG='" + TAG + '\'' +
                ", UserID='" + UserID + '\'' +"\n"+
                ", OrderID='" + OrderID + '\'' +"\n"+
                ", EmployeeID='" + EmployeeID + '\'' +"\n"+
                ", CustomerID='" + CustomerID + '\'' +"\n"+
                ", CompanyID='" + CompanyID + '\'' +"\n"+
                ", OrderDate='" + OrderDate + '\'' +"\n"+
                ", ShippedDate='" + ShippedDate + '\'' +"\n"+
                ", ShipperID='" + ShipperID + '\'' +"\n"+
                ", ShipName='" + ShipName + '\'' +"\n"+
                ", ShipAddress='" + ShipAddress + '\'' +"\n"+
                ", ShipCity='" + ShipCity + '\'' +"\n"+
                ", ShipStateProvince='" + ShipStateProvince + '\'' +"\n"+
                ", ShipZIPPostalCode='" + ShipZIPPostalCode + '\'' +"\n"+
                ", ShipCountryRegion='" + ShipCountryRegion + '\'' +"\n"+
                ", ShippingFee='" + ShippingFee + '\'' +"\n"+
                ", Taxes='" + Taxes + '\'' +"\n"+
                ", PaymentType='" + PaymentType + '\'' +"\n"+
                ", PaidDate='" + PaidDate + '\'' +"\n"+
                ", Notes='" + Notes + '\'' +"\n"+
                ", TaxRate='" + TaxRate + '\'' +"\n"+
                ", TaxStatus='" + TaxStatus + '\'' +"\n"+
                ", StatusID='" + StatusID + '\'' +"\n"+
                ", UpdateDate='" + UpdateDate + '\'' +"\n"+
                ", Deleted='" + Deleted + '\'' +"\n"+
                ", Spare1='" + Spare1 + '\'' +"\n"+
                ", Spare2='" + Spare2 + '\'' +"\n"+
                ", Spare3='" + Spare3 + '\'' +"\n"+
                ", SumPrice='" + SumPrice + '\'' +"\n"+
                ", SumDiscount='" + SumDiscount + '\'' +"\n"+
                ", SumTotal='" + SumTotal + '\'' +"\n"+
               "{" +"\n"+
                        "Order_Details" + getOrder_Details().toString() + "\n" +
                "}"+"\n"+
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
