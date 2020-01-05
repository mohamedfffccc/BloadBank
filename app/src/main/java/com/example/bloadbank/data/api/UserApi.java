package com.example.bloadbank.data.api;

import com.example.bloadbank.data.model.contact.Contact;
import com.example.bloadbank.data.model.donationrequestcreate.OneDonationRequest;
import com.example.bloadbank.data.model.donationrequests.Donationrequests;
import com.example.bloadbank.data.model.generalResponse.GeneralResponse;
import com.example.bloadbank.data.model.login.Login;
import com.example.bloadbank.data.model.newPassword.NewPassword;
import com.example.bloadbank.data.model.notifications.Notifications;
import com.example.bloadbank.data.model.notificationscount.NotificationsCount;
import com.example.bloadbank.data.model.notificationssetting.NotificationSsetting;
import com.example.bloadbank.data.model.postdetail.PostDetail;
import com.example.bloadbank.data.model.posts.Posts;
import com.example.bloadbank.data.model.posttogglefavourite.PostToggleFavourite;
import com.example.bloadbank.data.model.reresetPassword.ResetPassword;
import com.example.bloadbank.data.model.settings.Settings;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApi {
    @POST("login")
    @FormUrlEncoded
    Call<Login> LoginUser(@Field("phone") String phone,
                          @Field("password") String password);

    @POST("signup")
    @FormUrlEncoded
    Call<Login> SignUp(@Field("name") String name, @Field("email") String email,
                       @Field("birth_date") String birth_date, @Field("city_id") int city_id,
                       @Field("phone") String phone, @Field("donation_last_date") String donation_last_date,
                       @Field("password") String password, @Field("password_confirmation") String password_confirmation,
                       @Field("blood_type_id") int blood_type_id);


    @GET("governorates")
    Call<GeneralResponse> GetGov();

    @POST("reset-password")
    @FormUrlEncoded
    Call<ResetPassword> ResetPass(@Field("phone") String name);

    @GET("cities")
    Call<GeneralResponse> GetCites(@Query("governorate_id") int governorate_id);

    @POST("new-password")
    @FormUrlEncoded
    Call<NewPassword> ChangePassword(@Field("phone") String phone, @Field("pin_code") String pin_code
            , @Field("password") String password,
                                     @Field("password_confirmation") String password_confirmation);


    @GET("blood-types")
    Call<GeneralResponse> GetBloads();

    @GET("donation-requests")
    Call<Donationrequests> GetRequests(@Query("api_token") String api_token
            , @Query("page") int page);

    @GET("posts")
    Call<Posts> GetPosts(@Query("api_token") String api_token
            , @Query("page") int page);

    @POST("post-toggle-favourite")
    @FormUrlEncoded
    Call<PostToggleFavourite> AddToggleFav(@Field("post_id") int post_id, @Field("api_token") String api_token);

    @GET("my-favourites")
    Call<Posts> GetFav(@Query("api_token") String api_token);

    @GET("post")
    Call<PostDetail> GetDetail(@Query("api_token") String api_token, @Query("post_id") int post_id, @Query("page") int page);

    @POST("donation-request/create")
    @FormUrlEncoded
    Call<OneDonationRequest> CreateRequest(@Field("api_token") String api_token, @Field("patient_name") String patient_name
            , @Field("patient_age") int patient_age, @Field("blood_type_id") int blood_type_id
            , @Field("bags_num") int bags_num, @Field("hospital_name") String hospital_name
            , @Field("hospital_address") String hospital_address, @Field("city_id") int city_id
            , @Field("phone") String phone, @Field("notes") String notes
            , @Field("latitude") String latitude, @Field("longitude") String longitude);

    @POST("profile")
    @FormUrlEncoded
    Call<Login> GetProfData(@Field("api_token") String api_token);

    @POST("profile")
    @FormUrlEncoded
    Call<Login> EditProfile(@Field("name") String name, @Field("email") String email,
                            @Field("birth_date") String birth_date, @Field("city_id") int city_id,
                            @Field("phone") String phone, @Field("donation_last_date") String donation_last_date,
                            @Field("password") String password, @Field("password_confirmation") String password_confirmation,
                            @Field("blood_type_id") int blood_type_id, @Field("api_token") String api_token);

    @GET("donation-request")
    Call<OneDonationRequest> GetRequestDetails(@Query("api_token") String api_token,
                                               @Query("donation_id") int donation_id);

    @GET("settings")
    Call<Settings> GetSetting(@Query("api_token") String api_token);

    @POST("notifications-settings")
    @FormUrlEncoded
    Call<NotificationSsetting> GetNotificationsSetting(@Field("api_token") String api_token);
    @POST("notifications-settings")
    @FormUrlEncoded
    Call<NotificationSsetting> changetNotificationsSetting(@Field("api_token") String api_token ,
                                                           @Field("governorates[]") List<Integer> governorate_zero,
                                                           @Field("blood_types[]") List<Integer> blood_types_zero );



    @GET("categories")
    Call<GeneralResponse> GetCat();

    @GET("posts")
    Call<Posts> FilterPosts(@Query("api_token") String api_token, @Query("page") int page
            , @Query("keyword") String keyword, @Query("category_id") int category_id);

    @GET("notifications-count")
    Call<NotificationsCount> GetNotificationsCount(@Query("api_token") String api_token);

    @GET("notifications")
    Call<Notifications> GetNotifications(@Query("api_token") String api_token, @Query("page") int page);

    @POST("contact")
    @FormUrlEncoded
    Call<Contact> Contact(@Field("api_token") String api_token, @Field("title") String title, @Field("message") String message);


    @GET("donation-requests")
    Call<Donationrequests> filterRequests(@Query("api_token") String api_token ,
                                          @Query("blood_type_id") int blood_type_id,
                                          @Query("city_id") int city_id ,
                                          @Query("page") int page  );


}
