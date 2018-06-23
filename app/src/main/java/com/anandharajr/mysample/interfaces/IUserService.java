package com.anandharajr.mysample.interfaces;

import android.support.annotation.Nullable;

import com.anandharajr.mysample.model.Datum;

import java.util.ArrayList;
import java.util.List;

public interface IUserService {
     void FetchUsers(int pageNo, @Nullable IUserDataCallbacks callbacks);
}
