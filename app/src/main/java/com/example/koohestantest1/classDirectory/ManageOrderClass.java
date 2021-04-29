package com.example.koohestantest1.classDirectory;

import com.example.koohestantest1.Utils.StringUtils;

import java.util.ArrayList;

import com.example.koohestantest1.ViewModels.Order_DetailsViewModels;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.sendOrderClass;

public class ManageOrderClass {

    private BaseCodeClass baseCodeClass = new BaseCodeClass();

//    public ManageOrderClass(SendOrderClass sendOrder, Context context) {
//        try {
//            sendOrderClass = sendOrder;
//
//            sendOrderClass.setToken(baseCodeClass.getToken());
//            sendOrderClass.setUserID(baseCodeClass.getUserID());
//
//            Toast.makeText(context, sendOrderClass.getOrderID(), Toast.LENGTH_SHORT).show();
//        }catch (Exception e){
//
//        }
//    }

    public ManageOrderClass() {
        sendOrderClass.setToken(baseCodeClass.getToken());
        sendOrderClass.setUserID(baseCodeClass.getUserID());
        sendOrderClass.setCompanyID(baseCodeClass.getCompanyID());
        sendOrderClass.setEmployeeID(baseCodeClass.EmployeeID);
        String costumerID = baseCodeClass.getCompanyID();
        costumerID += sendOrderClass.getUserID();
        sendOrderClass.setCustomerID(costumerID);
        sendOrderClass.setOrder_Details(new ArrayList<Order_DetailsViewModels>());
        sendOrderClass.setOrderDate("2020-08-18T21:54:43.5172323+04:30"); //String.valueOf(Calendar.getInstance().getTime()));
    }

    public boolean addProductToCart(ReceiveProductClass receiveProductClass) {
        try {
            Order_DetailsViewModels orderDetail = new Order_DetailsViewModels();
            orderDetail.setProductID(receiveProductClass.getProductID());
            orderDetail.setProductName(receiveProductClass.getProductName());
            orderDetail.setQuantity(1);
            orderDetail.setDiscount(receiveProductClass.getStandardCost().getShowoffPrice());

            String S = receiveProductClass.getStandardCost().getShowPrice();
            orderDetail.setUnitPrice(String.valueOf((int) StringUtils.getNumberFromStringV2(S)));

            //orderDetail.setSumPrice(p);
            orderDetail.setID("1");
            orderDetail.setOrderID("");
            orderDetail.setInventoryID("1");
            orderDetail.setStatusID("1");
            orderDetail.setPurchaseOrderID("1");
            orderDetail.setDateAllocated("2020-08-18T21:54:43.5172323+04:30");

            for (ProductPropertisClass pp : receiveProductClass.getProductPropertis()
            ) {
                orderDetail.getPropertisViewModels().add(pp);
            }


            sendOrderClass.getOrder_Details().add(orderDetail);
            return true;
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "baseCodeClass 899 : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void RemoveProductFromCart(final String pid) {

//        for (Order_DetailsViewModels item :
//                sendOrderClass.getOrder_Details()) {
//            if (item.getProductID().equals(pid))
//                sendOrderClass.getOrder_Details().remove(item);
//        }

        for (int i = 0; i < sendOrderClass.getOrder_Details().size(); i++) {
            if (sendOrderClass.getOrder_Details().get(i).getProductID().equals(pid))
                sendOrderClass.getOrder_Details().remove(sendOrderClass.getOrder_Details().get(i));
        }
//        sendOrderClass.getOrder_Details().removeIf(new Predicate<Order_DetailsViewModels>() {
//            @Override
//            public boolean test(Order_DetailsViewModels x) {
//                return x.getProductID().equals(pid);
//            }
//        });

    }

    public void setProductQTY(String pid, int qy) {

        int i = 0;
        for (Order_DetailsViewModels o : sendOrderClass.getOrder_Details()
        ) {
            if (o.getProductID().equals(pid)) {
                o.setQuantity(qy);
                sendOrderClass.getOrder_Details().set(i, o);
                break;
            }
            i++;
        }
    }

    public int getProductQTY(String pid) {
        for (Order_DetailsViewModels o : sendOrderClass.getOrder_Details()
        ) {
            if (o.getProductID().equals(pid)) {
                int qy = o.getQuantity();
                return qy;
            }
        }
        return -1;
    }

    public void setPublicDetail(String shipAddress, String shipCity, String shipStateProvince,
                                String shipZIPPostalCode, String shipCountryRegion, String shippingFee,
                                String shippedDate, String paymentType, String notes, String sumPrice) {

        sendOrderClass.setShipAddress(shipAddress);
        sendOrderClass.setShipCity(shipCity);
        sendOrderClass.setShipStateProvince(shipStateProvince);
        sendOrderClass.setShipZIPPostalCode(shipZIPPostalCode);
        sendOrderClass.setShipCountryRegion(shipCountryRegion);
        sendOrderClass.setShippingFee(shippingFee);
        sendOrderClass.setShippedDate("2020-08-18T21:54:43.5172323+04:30");
        sendOrderClass.setPaymentType(paymentType);
        sendOrderClass.setNotes(notes);
        sendOrderClass.setSumPrice(sumPrice);

    }

}
