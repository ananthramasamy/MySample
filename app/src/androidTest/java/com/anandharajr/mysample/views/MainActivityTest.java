package com.anandharajr.mysample.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.test.annotation.UiThreadTest;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.TextView;

import com.anandharajr.mysample.R;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mTestActivity;
    private TextView mTestEmptyText;
    private ProgressDialog progressDialog;
    private RecyclerView UserRecyclerView;
    private Context mContext;
    private TextView CurrentPageNoTV, FirstNameTV, LastNameTV;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mTestActivity = getActivity();
        FirstNameTV = (TextView) mTestActivity.findViewById(R.id.first_name_tv);
        LastNameTV = (TextView) mTestActivity.findViewById(R.id.last_name_tv);
        UserRecyclerView = (RecyclerView) mTestActivity.findViewById(R.id.recycler_view);
        CurrentPageNoTV = (TextView) mTestActivity.findViewById(R.id.current_page_count_tv);

    }


    public void testPreconditions() {
        assertNotNull("mTestActivity is null", mTestActivity);
        assertNotNull("mTestEmptyText is null", FirstNameTV);
        assertNotNull("mTestEmptyText is null", LastNameTV);
        assertNotNull("mTestEmptyText is null", CurrentPageNoTV);
        assertNotNull("mTestEmptyText is null", UserRecyclerView);
    }




}
