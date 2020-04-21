package com.metrocem.mis.Subclasses;

import android.content.Context;
import android.util.Log;

import com.metrocem.mis.Container.DOOrderContainer;
import com.metrocem.mis.Container.DeliveredOrderContainer;
import com.metrocem.mis.Model.DataManager;
import com.metrocem.mis.Model.Order;

import java.util.ArrayList;

public class Container {


    static ArrayList<DOOrderContainer> doArrayList = new ArrayList<DOOrderContainer>();

    public static void saveDO(Order order, Context context){

        //ArrayList<DOOrderContainer> doArrayList = new ArrayList<DOOrderContainer>();

        //doArrayList.clear();
        DeliveredOrderContainer doOrderContainer = new DeliveredOrderContainer();
        doOrderContainer.id = order.getId();
        doOrderContainer.type = order.getType();
        doOrderContainer.actualBagQty = order.getActualBagQty();
        doOrderContainer.unitPrice = order.getUnitPrice();
        doOrderContainer.doNumber = order.getDoNumber();
        doOrderContainer.deliveryMode = order.getDeliveryMode();
        doOrderContainer.approvedAt = order.getApprovedAt();
        doOrderContainer.status = order.getStatus();
        doOrderContainer.createdAt = order.getCreatedAt();
        doOrderContainer.employeeName = order.getEmployeeName();
        doOrderContainer.dealerName = order.getDealerName();
        doOrderContainer.productName = order.getProductName();
        doOrderContainer.numberOfChallan = order.getNumberOfChallan();
        doOrderContainer.deliveryAddress = order.getDeliveryAddress().getAddress();
        doOrderContainer.contactNumber = order.getDeliveryAddress().getContactNumber();
        doArrayList.add(doOrderContainer);
        DataManager.setDOOrderList(doArrayList, context);

        Log.d("response count", String.valueOf(doArrayList.size()));
    }
}
