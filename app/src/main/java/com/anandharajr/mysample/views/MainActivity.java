package com.anandharajr.mysample.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anandharajr.mysample.R;
import com.anandharajr.mysample.adapter.UsersAdapter;
import com.anandharajr.mysample.database.SQLiteDBHelper;
import com.anandharajr.mysample.interfaces.IUserDataCallbacks;
import com.anandharajr.mysample.interfaces.IUserService;
import com.anandharajr.mysample.interfaces.OnItemUsersClickListener;
import com.anandharajr.mysample.model.Datum;
import com.anandharajr.mysample.model.Users;
import com.anandharajr.mysample.utils.Configuration;
import com.anandharajr.mysample.utils.MyDividerItemDecoration;
import com.anandharajr.mysample.utils.UserServiceAPI;
import com.anandharajr.mysample.utils.UserServiceLocal;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog progressDialog;
    private List<Datum> datumArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private UsersAdapter mAdapter;
    private Context mContext;
    private int mCurrentPageCount = 1;
    private int mTotalPageCount = 1;
    private TextView CurrentPageNoTV;
    private IUserService UserService;
    private SQLiteDBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        progressDialog = Configuration.generateProgressDialog(mContext, false);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.toolbar_title);
        initViews();
        db = new SQLiteDBHelper(this);
        mAdapter = new UsersAdapter(mContext, datumArrayList, new OnItemUsersClickListener() {
            @Override
            public void onItemClick(Datum item) {
                Toast.makeText(mContext, item.getFirst_name(), Toast.LENGTH_LONG).show();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);
        LoadUserDataToDb();
        fetchUsers(mCurrentPageCount);
    }

    private void LoadUserDataToDb() {
        if (Configuration.checkConnection(mContext)) {
            UserService = new UserServiceAPI();
            UserService.FetchUsers(1, new IUserDataCallbacks() {
                @Override
                public void onSuccess(@NonNull Users value) {
                    db.deleteRecords();
                    db.CreateTable();
                    for (int i = 1; i <= value.getTotal_pages(); i++) {
                        SaveEachPageToDb(i);
                    }
                }

                @Override
                public void onError(@NonNull Throwable throwable) {

                }
            });
        }
    }

    private void SaveEachPageToDb(int index) {
        UserService = new UserServiceAPI();
        UserService.FetchUsers(index, new IUserDataCallbacks() {
            @Override
            public void onSuccess(@NonNull Users value) {

                new ImageConversionAsyncTask(value, value.getTotal_pages(), value.getPage(), value.getTotal()).execute();
            }

            @Override
            public void onError(@NonNull Throwable throwable) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {

    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view);
        CurrentPageNoTV = (TextView) findViewById(R.id.current_page_count_tv);
        Button previousBTN = (Button) findViewById(R.id.previous_action_btn);
        Button nextBTN = (Button) findViewById(R.id.next_action_btn);
        previousBTN.setOnClickListener(this);
        nextBTN.setOnClickListener(this);
        WhiteTransparentBar(recyclerView);
    }


    private void WhiteTransparentBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previous_action_btn:
                if (mCurrentPageCount > 1) {
                    mCurrentPageCount = mCurrentPageCount - 1;
                    fetchUsers(mCurrentPageCount);
                } else {
                    Toast.makeText(mContext, "No More Previous Pages", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.next_action_btn:
                if (mCurrentPageCount < mTotalPageCount) {
                    mCurrentPageCount = mCurrentPageCount + 1;
                    fetchUsers(mCurrentPageCount);
                } else {
                    Toast.makeText(mContext, "No More Next Pages", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void fetchUsers(int pageCount) {
        progressDialog.show();

        UserService = Configuration.checkConnection(mContext) ? new UserServiceAPI() : new UserServiceLocal(mContext);
        UserService.FetchUsers(pageCount, new IUserDataCallbacks() {
            @Override
            public void onSuccess(@NonNull Users value) {

                onUserSuccessListener(value);

            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                onUserFailureListener(throwable);
            }
        });
    }

    private void onUserSuccessListener(Users value) {
        // new SQLiteDBHelper(mContext).insertImage(value);
        mTotalPageCount = value.getTotal_pages();
        CurrentPageNoTV.setText(String.format("%d / %d", value.getPage(), value.getTotal_pages()));
        Integer total = value.getTotal();
        datumArrayList.clear();
        datumArrayList.addAll(value.getData());
        mAdapter.notifyDataSetChanged();
        if (progressDialog != null) progressDialog.cancel();
    }


    private void onUserFailureListener(Throwable throwable) {
        if (progressDialog != null) progressDialog.cancel();
        Toast.makeText(mContext, throwable.getMessage(), Toast.LENGTH_LONG).show();
    }


    private class ImageConversionAsyncTask extends AsyncTask<Void, Void, Integer> {
        private Users UserImageList;
        private int mTotalPageCount, page, total;


        ImageConversionAsyncTask(Users value, int mTotalPageCount, Integer page, Integer total) {
            this.UserImageList = value;
            this.mTotalPageCount = mTotalPageCount;
            this.page = page;
            this.total = total;
        }


        @Override
        protected Integer doInBackground(Void... params) {
            List<Datum> datumArrayList = new ArrayList<>();
            for (int i = 0; i < UserImageList.getData().size(); i++) {
                int id = UserImageList.getData().get(i).getId();
                String FirstName = UserImageList.getData().get(i).getFirst_name();
                String LastName = UserImageList.getData().get(i).getLast_name();
                String AvatarUrl = UserImageList.getData().get(i).getAvatar();
                Bitmap mBitMap = getBitmapFromURL(AvatarUrl);
                if (mBitMap != null) {
                    Datum datum = new Datum();
                    datum.setId(id);
                    datum.setFirst_name(FirstName);
                    datum.setLast_name(LastName);
                    datum.setAvatar(AvatarUrl);
                    datum.setBitmapAvatar(mBitMap);
                    datumArrayList.add(datum);

                }
            }
            UserImageList.setData(datumArrayList);
            new SQLiteDBHelper(mContext).insertImage(UserImageList);

            return UserImageList.getData().size();
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);


        }
    }


    private Bitmap getBitmapFromURL(String urlPath) {
        HttpURLConnection connection;
        int serverResponseCode;
        try {
            URL url = new URL(urlPath);
            boolean isHttps = urlPath.toLowerCase().startsWith("https");
            if (isHttps) {
                connection = (HttpsURLConnection) url.openConnection();
            } else {
                connection = (HttpURLConnection) url.openConnection();
            }
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(45000);
            connection.connect();
            serverResponseCode = connection.getResponseCode();

            if (serverResponseCode == HttpURLConnection.HTTP_OK) {
                InputStream input = new BufferedInputStream(url.openStream());
                return BitmapFactory.decodeStream(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

}
