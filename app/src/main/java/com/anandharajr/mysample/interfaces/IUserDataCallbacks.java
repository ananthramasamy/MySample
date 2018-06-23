package com.anandharajr.mysample.interfaces;

import android.support.annotation.NonNull;

import com.anandharajr.mysample.model.Datum;
import com.anandharajr.mysample.model.Users;

import java.util.ArrayList;
import java.util.List;

public interface IUserDataCallbacks {
    void onSuccess(@NonNull Users value);

    void onError(@NonNull Throwable throwable);
}
