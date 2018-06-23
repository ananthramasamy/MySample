package com.anandharajr.mysample.interfaces;

import com.anandharajr.mysample.model.Users;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by anandharajr on 21-06-18.
 */

public interface APIInterface {
    @GET("/api/users?")
    Call<Users> doGetUserList(@Query("page") String page);
}
