package com.anandharajr.mysample.interfaces;

import android.support.annotation.NonNull;

/*
 * Created by anandharajr on 21-06-18.
 */

import com.anandharajr.mysample.model.Users;
/**
 * Created by anandharajr on 23-06-18.
 */

public interface IUserDataCallbacks {
    void onSuccess(@NonNull Users value);

    void onError(@NonNull Throwable throwable);
}
