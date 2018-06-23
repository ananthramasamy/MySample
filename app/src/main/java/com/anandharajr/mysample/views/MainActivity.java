package com.anandharajr.mysample.views;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by anandharajr on 23-06-18.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    boolean doubleBackToExitPressedOnce = false;
    private View ContainerMainLayout;
    private ProgressDialog progressDialog;
    private List<Datum> datumArrayList = new ArrayList<>();
    private RecyclerView UserRecyclerView;
    private UsersAdapter mAdapter;
    private Context mContext;
    private int mCurrentPageCount = 1;
    private int mTotalPageCount = 1;
    private TextView CurrentPageNoTV,FirstNameTV,LastNameTV;
    private ImageView UserProfileIV;
    private IUserService UserService;
    private SQLiteDBHelper db;
    private BottomSheetBehavior sheetBehavior;
    private AlertDialog mAlertDialog;

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
        db = new SQLiteDBHelper(mContext);
        mAdapter = new UsersAdapter(mContext, datumArrayList, new OnItemUsersClickListener() {
            @Override
            public void onItemClick(Datum item) {
                FirstNameTV.setText(item.getFirst_name());
                LastNameTV.setText(item.getLast_name());
                if (item.getBitmapAvatar() == null) {
                    Glide.with(mContext)
                            .load(item.getAvatar())
                            .apply(RequestOptions.circleCropTransform())
                            .into(UserProfileIV);
                } else {
                    UserProfileIV.setImageBitmap(item.getBitmapAvatar());
                }

               sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        UserRecyclerView.setLayoutManager(mLayoutManager);
        UserRecyclerView.setItemAnimator(new DefaultItemAnimator());
        UserRecyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        UserRecyclerView.setAdapter(mAdapter);
        if (progressDialog != null) progressDialog.show();
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
                    if (progressDialog != null) progressDialog.cancel();
                }
            });
        }
    }

    private void SaveEachPageToDb(int index) {
        UserService = new UserServiceAPI();
        UserService.FetchUsers(index, new IUserDataCallbacks() {
            @Override
            public void onSuccess(@NonNull Users value) {
                new ImageConversionAsyncTask(value).execute();
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
               if (progressDialog != null) progressDialog.cancel();
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
        if (progressDialog != null) progressDialog.cancel();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Snackbar snackbar = Snackbar.make(ContainerMainLayout, "Please click BACK again to exit", Snackbar.LENGTH_LONG);
        snackbar.show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void initViews() {
        LinearLayout layoutBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        ContainerMainLayout = findViewById(R.id.container_main_layout);

        Button actionModeCloseBTN = (Button) findViewById(R.id.action_mode_close_button);
        FirstNameTV=(TextView)findViewById(R.id.first_name_tv);
        LastNameTV=(TextView) findViewById(R.id.last_name_tv);
        UserProfileIV=(ImageView)findViewById(R.id.profile_action_iv);

        UserRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        CurrentPageNoTV = (TextView) findViewById(R.id.current_page_count_tv);
        Button previousBTN = (Button) findViewById(R.id.previous_action_btn);
        Button nextBTN = (Button) findViewById(R.id.next_action_btn);
        previousBTN.setOnClickListener(this);
        nextBTN.setOnClickListener(this);
        actionModeCloseBTN.setOnClickListener(this);
        WhiteTransparentBar(UserRecyclerView);
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
                    if (progressDialog != null) progressDialog.show();
                    fetchUsers(mCurrentPageCount);
                } else {
                    Configuration.warningAlertDialog(mContext, getString(R.string.warning_previous_pages));
                }
                break;
            case R.id.next_action_btn:
                if (mCurrentPageCount < mTotalPageCount) {
                    mCurrentPageCount = mCurrentPageCount + 1;
                    if (progressDialog != null) progressDialog.show();
                    fetchUsers(mCurrentPageCount);
                } else {
                    Configuration.warningAlertDialog(mContext, getString(R.string.warning_next_pages));
                }
                break;
            case R.id.action_mode_close_button:
                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
        }
    }

    private void fetchUsers(int pageCount) {
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

    @SuppressLint("DefaultLocale")
    private void onUserSuccessListener(Users value) {
        mTotalPageCount = value.getTotal_pages();
        CurrentPageNoTV.setText(String.format("%d / %d", value.getPage(), value.getTotal_pages()));
        datumArrayList.clear();
        datumArrayList.addAll(value.getData());
        mAdapter.notifyDataSetChanged();
       if (progressDialog != null) progressDialog.cancel();
    }


    private void onUserFailureListener(Throwable throwable) {
        if (progressDialog != null) progressDialog.cancel();

        if (TextUtils.equals(throwable.getMessage(),"Internet is required for first run")){
            try {
                if (mAlertDialog != null) {
                    if (mAlertDialog.isShowing()) {
                        mAlertDialog.dismiss();
                    }
                }
                mAlertDialog = new AlertDialog.Builder(mContext).setCancelable(false)
                        .setMessage(throwable.getMessage()).setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                LoadUserDataToDb();
                                fetchUsers(mCurrentPageCount);
                            }
                        }).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Configuration.warningAlertDialog(mContext, throwable.getMessage());
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class ImageConversionAsyncTask extends AsyncTask<Void, Void, Integer> {
        private Users UserImageList;

        ImageConversionAsyncTask(Users value) {
            this.UserImageList = value;

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

    public static Bitmap getBitmapFromURL(String urlPath) {
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
