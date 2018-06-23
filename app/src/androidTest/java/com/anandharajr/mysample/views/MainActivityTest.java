package com.anandharajr.mysample.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.test.annotation.UiThreadTest;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anandharajr.mysample.R;
import com.anandharajr.mysample.database.SQLiteDBHelper;
import com.anandharajr.mysample.model.Users;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mTestActivity;
    private TextView mTestEmptyText;
    private View ContainerMainLayout;
    private ProgressDialog progressDialog;
    private RecyclerView UserRecyclerView;
    private Context mContext;
    private TextView CurrentPageNoTV, FirstNameTV, LastNameTV;
    private ImageView UserProfileIV;
    private Button previousBTN, nextBTN;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mTestActivity = getActivity();
        LinearLayout layoutBottomSheet = (LinearLayout) mTestActivity.findViewById(R.id.bottom_sheet);
        ContainerMainLayout = mTestActivity.findViewById(R.id.container_main_layout);
        Button actionModeCloseBTN = (Button) mTestActivity.findViewById(R.id.action_mode_close_button);
        FirstNameTV = (TextView) mTestActivity.findViewById(R.id.first_name_tv);
        LastNameTV = (TextView) mTestActivity.findViewById(R.id.last_name_tv);
        UserProfileIV = (ImageView) mTestActivity.findViewById(R.id.profile_action_iv);
        UserRecyclerView = (RecyclerView) mTestActivity.findViewById(R.id.recycler_view);
        CurrentPageNoTV = (TextView) mTestActivity.findViewById(R.id.current_page_count_tv);
        previousBTN = (Button) mTestActivity.findViewById(R.id.previous_action_btn);
        nextBTN = (Button) mTestActivity.findViewById(R.id.next_action_btn);

    }


    public void testPreconditions() {
        assertNotNull("mTestActivity is null", mTestActivity);
        assertNotNull("mTestEmptyText is null", FirstNameTV);
        assertNotNull("mTestEmptyText is null", LastNameTV);
        assertNotNull("mTestEmptyText is null", CurrentPageNoTV);
        assertNotNull("mTestEmptyText is null", UserRecyclerView);
    }

    @UiThreadTest
    public void testMainThread_operations() {
        FirstNameTV.setText("Blue");
//        LastNameTV.setText("Geroge");
    }


}
