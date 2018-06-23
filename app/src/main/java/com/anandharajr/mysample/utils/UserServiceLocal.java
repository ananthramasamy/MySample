package com.anandharajr.mysample.utils;

import android.content.Context;
import android.support.annotation.Nullable;

import com.anandharajr.mysample.database.SQLiteDBHelper;
import com.anandharajr.mysample.interfaces.IUserDataCallbacks;
import com.anandharajr.mysample.interfaces.IUserService;
import com.anandharajr.mysample.model.Users;

public class UserServiceLocal implements IUserService {

    SQLiteDBHelper db;

    public UserServiceLocal(Context context)
    {
      db=new SQLiteDBHelper(context);
    }
    @Override
    public void FetchUsers(int pageNo, @Nullable IUserDataCallbacks callbacks) {

        try {

            Users users = db.getUserRecords(pageNo);
            callbacks.onSuccess(users);

        }
        catch (Exception e)
        {
            callbacks.onError(e);
        }
    }
}
