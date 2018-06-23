package com.anandharajr.mysample.interfaces;

import com.anandharajr.mysample.model.Users;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface APIInterface {
    @GET("/api/users")
    Call<Users> doGetListResources();

    @POST("/api/users")
    Call<Users> createUser(@Body Users user);

    @GET("/api/users?")
    Call<Users> doGetUserList(@Query("page") String page);

   /* @FormUrlEncoded
    @POST("/api/users?")
    Call<UserList> doCreateUserWithField(@Field("name") String name, @Field("job") String job);*/
}
