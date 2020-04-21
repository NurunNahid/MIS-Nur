package com.metrocem.mis.Retrofit;

import com.metrocem.mis.Firebase.FirebaseTokenResponse;
import com.metrocem.mis.SignInAndRegistration.LoginResponse;
import com.metrocem.mis.Model.ChallanList;
import com.metrocem.mis.Model.ChallanReceiveInfo;
import com.metrocem.mis.Model.Collection;
import com.metrocem.mis.Model.DORequestResponse;
import com.metrocem.mis.Model.DealerAddressData;
import com.metrocem.mis.Model.DealerList;
import com.metrocem.mis.Model.DeliveryModeList;
import com.metrocem.mis.Model.DeviceRegistration;
import com.metrocem.mis.Model.Due;
import com.metrocem.mis.Model.OrderList;
import com.metrocem.mis.Model.ProductList;
import com.metrocem.mis.Model.PromotionList;
import com.metrocem.mis.Model.RetailerList;
import com.metrocem.mis.Model.SecurityList;

import java.util.ArrayList;

import com.metrocem.mis.EmployeeDO.DealerCreditList;
import com.metrocem.mis.TradeBrandPromotion.MultimediaList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiClient {


    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> userLogin(
            @Field("identifier") String identifier,
            @Field("device_id") String device_id,
            @Field("email_or_phone") String email_or_phone,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("register/device")
    Call<DeviceRegistration> registerDevice(
            @Field("name") String name,
            @Field("device_id") String device_id,
            @Field("identifier") String identifier,
            @Field("type") String type,
            @Field("os") String os,
            @Field("version") String version
    );

    @GET("device/exists/{id}")
    Call<DeviceRegistration> checkDeviceExistence(
            @Path("id") String device_imei
    );

    //@Query("id") String device_imei
    @GET("retailer/list-by-dealer/{dealer_id}")
    Call<RetailerList> getRetailerList(
            @Path("dealer_id") Integer dealer_id
    );


    @GET("dealer/list-by-employee/{employee_id}")
    Call<DealerList> getDealerList(
            @Path("employee_id") Integer employee_id
    );

    @FormUrlEncoded
    @POST("delivery-order/create-do")
    Call<DORequestResponse> sendDORequest(
            //@Field("do_number") String do_number,
            @Field("dealer_id") Integer dealer_id,
            @Field("retailer_id") Integer retailer_id,
            @Field("product_id") Integer product_id,
            @Field("delivery_mode") String delivery_mode,
            @Field("location") String location,
            @Field("delivery_address[name]") ArrayList<String> name,
            @Field("delivery_address[address]") ArrayList<String> address,
            @Field("delivery_address[contactNumber]") ArrayList<String> contact_no,
            @Field("billed_bag_qty") Number billed_bag_qty,
            @Field("required_vehicle_capacity") Number required_vehicle_capacity,
            @Field("note") String note
            //@Field("unit_price") Number unit_price
            );


    @GET("delivery-order/list-by-dealer/{id}")
    Call<OrderList> getOrderList(
            @Path("id") Integer dealerId
    );

    @GET("delivery-order/list-by-employee/{id}")
    Call<OrderList> getOrderListByEmployee(
            @Path("id") Integer employeeId
    );

    @GET("challan/get-by-employee/{id}")
    Call<ChallanList> getChallanListByEmployee(
            @Path("id") Integer employeeId
    );

    @GET("challan/get-by-dealer/{id}")
    Call<ChallanList> getChallanListByDealer(
            @Path("id") Integer dealerId
    );

    @GET("product/dropdown-list")
    Call<ProductList> getProductList();

    @GET("constants/delivery-mode-dropdown-list")
    Call <DeliveryModeList> getDeliveryModeList();

    @GET("dealer/get-collection-amount")
    Call<Collection> getCollectionAmount(
            @Query("id") Integer dealer_id,
            @Query("start_date") String startDate,
            @Query("end_date") String end_date
    );

    @GET("dealer/get-due-amount")
    Call<Due> getDueAmount(
            @Query("id") Integer dealer_id
    );

    @GET("offer/dropdown-list")
    Call<PromotionList> getPromotionList();

    @GET("dealer/get-summary/{id}")
    Call<DealerCreditList> getDealerCreditInfo(
            @Path("id") Integer dealerId
    );

    //@FormUrlEncoded
    @POST("challan/receive/{id}")
    Call<ChallanReceiveInfo> receiveChallan(
            @Path("id") Integer challanId
    );
    @GET("dealer/get-delivery-address")
    Call<DealerAddressData> getAddress(
            @Query("dealer_id") Integer dealer_id
    );

    @GET("securities/list-by-employee/{employee_id}")
    Call<SecurityList> getSecurityList(
            @Path("employee_id") Integer employee_id
    );

    @GET("multimedia")
    Call<MultimediaList> getMultimediaList();

    @FormUrlEncoded
    @POST("device/update-token")
    Call<FirebaseTokenResponse> updateFirebaseToken(
            @Field("token") String token
    );

    @GET("delivery-order/list-by-dealer/{id}/{type}/{start_date}/{end_date}")
    Call<OrderList> getALLOrderList(
            @Path("id") Integer dealerId,
            @Path("type") String type,
            @Path("start_date") String start_date,
            @Path("end_date") String end_date

            );
}
