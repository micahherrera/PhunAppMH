package com.micahherrera.warelication.eventlist;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.micahherrera.warelication.R;
import com.micahherrera.warelication.adapter.EventRecyclerAdapter;
import com.micahherrera.warelication.data.Event;
import com.micahherrera.warelication.data.EventDataSource;
import com.micahherrera.warelication.data.EventRepository;
import com.micahherrera.warelication.data.SWDBHelper;
import com.micahherrera.warelication.eventdetail.EventDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventListActivity extends AppCompatActivity implements EventListContract.View{
    @BindView(R.id.event_list_recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.error_view)
    LinearLayout mErrorLayout;

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private EventListContract.Presenter mPresenter;
    private EventRepository mRepo;
    private EventRecyclerAdapter mRecyclerAdapter;
    private SWDBHelper swdbHelper;
    private int mScreenSize;
    private static final String EVENT_LIST = "event_list";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        ButterKnife.bind(this);

        swdbHelper = SWDBHelper.getInstance(this);

        mRepo = new EventDataSource();
        mPresenter = new EventListPresenter(mRepo, this);

        mScreenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        if(savedInstanceState!=null){
            mRecyclerAdapter = new EventRecyclerAdapter(new ArrayList<Event>(0), this);
            mRecyclerView.setAdapter(mRecyclerAdapter);
            mRecyclerView.setLayoutManager(new GridLayoutManager(this,
                    (mScreenSize==Configuration.SCREENLAYOUT_SIZE_LARGE? 2:1)));

            mPresenter.loadEventsFromDB();
        } else {
            setupUIForEvents();
        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshEvents();
            }
        });

    }

    @Override
    public void goToDetail(String id) {
        Intent intent = new Intent(EventListActivity.this, EventDetailActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);

    }

    @Override
    public void showShareMenu(String title, String date, String address) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Join me for "+ title + " on " +
                date + " at " + address);
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this Event!");
        startActivity(Intent.createChooser(intent, "Share"));

    }

    @Override
    public void showEvents(List<Event> eventList, boolean source) {
        mRecyclerAdapter.replaceData(eventList);
        mRecyclerAdapter.notifyDataSetChanged();
        mRecyclerView.setVisibility(View.VISIBLE);

        if(source) {
            mErrorLayout.setVisibility(View.GONE);
            mPresenter.saveEvents(eventList, swdbHelper);
        }
    }

    @Override
    public void showNoEventsView() {
        mErrorLayout.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }



    private void setupUIForEvents(){
        mRecyclerAdapter = new EventRecyclerAdapter(new ArrayList<Event>(0), this);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this,
                (mScreenSize==Configuration.SCREENLAYOUT_SIZE_LARGE? 2:1)));

        mPresenter.loadEvents();
    }

    private void refreshEvents() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mRecyclerView!=null){
                    mPresenter.loadEvents();
                } else {
                    setupUIForEvents();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    public boolean checkConnection(){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("TAG", "onCreate: You are connected");
            return true;

        } else {
            Log.d("TAG", "onCreate: You are not connected");
            mErrorLayout.setVisibility(View.VISIBLE);
            return false;

        }

    }

    @Override
    public SWDBHelper getDBHelper() {
        return swdbHelper;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(EVENT_LIST, 1);
        super.onSaveInstanceState(savedInstanceState);
    }
}
