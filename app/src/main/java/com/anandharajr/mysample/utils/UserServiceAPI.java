package com.anandharajr.mysample.utils;

import android.support.annotation.Nullable;

import com.anandharajr.mysample.api.APIClient;
import com.anandharajr.mysample.interfaces.APIInterface;
import com.anandharajr.mysample.interfaces.IUserDataCallbacks;
import com.anandharajr.mysample.interfaces.IUserService;
import com.anandharajr.mysample.model.Datum;
import com.anandharajr.mysample.model.Users;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserServiceAPI implements IUserService {
    @Override
    public void FetchUsers(int pageNo, @Nullable final IUserDataCallbacks callbacks) {
        Call<Users> call;
         APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        call = apiInterface.doGetUserList(String.valueOf(pageNo));
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                 callbacks.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                call.cancel();
                callbacks.onError(t);
            }
        });
    }
}
