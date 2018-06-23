package com.anandharajr.mysample.interfaces;

import android.support.annotation.NonNull;

/*
 * Created by anandharajr on 21-06-18.
 */

import com.anandharajr.mysample.model.Users;

public interface IUserDataCallbacks {
    void onSuccess(@NonNull Users value);

    void onError(@NonNull Throwable throwable);
}
