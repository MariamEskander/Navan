package com.pharmacy.pharmacycare.ui.read_more;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.pharmacy.pharmacycare.R;
import com.pharmacy.pharmacycare.model.DataViewModel;
import com.pharmacy.pharmacycare.util.NetworkChangeReceiver;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mariam.Narouz on 1/22/2018.
 */

public class ReadMoreActivity extends AppCompatActivity implements ReadMoreView, NetworkChangeReceiver.OnNetworkStateChange, GoToDetails {

    
    @BindView(R.id.activity_read_more_pb)
    ProgressBar activity_read_more_pb;

    @BindView(R.id.activity_read_more_rv)
    RecyclerView activity_read_more_rv;

    @BindView(R.id.activity_read_more_swipe)
    SwipeRefreshLayout activity_read_more_swipe;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @BindView(R.id.title)
    TextView title;


    private boolean isLoadMore = true, firstOpen = true;
    private int offset = 1;
    private ReadMorePresenter readMorePresenter;
    private ReadMoreAdapter readMoreAdapter;
    private LinearLayoutManager layoutManager;

    private int visibleItemCount = 0;
    private int totalItemCount = 0;
    private int pastVisibleItems = 0;
    private int page = 0;
    private String type;
    private Snackbar snackbar;


    @Override
    public void onResume() {
        super.onResume();
        if (offset == 1) {
            setFirstOpen(true);
            readMorePresenter.getData(offset);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_more);
        ButterKnife.bind(this);
        type = getIntent().getStringExtra("type");
        snackbar = Snackbar.make(findViewById(android.R.id.content), "", Snackbar.LENGTH_LONG);
        
        NetworkChangeReceiver.setOnMessageListener(this);
        offset = 1 ;
        readMorePresenter = new ReadMorePresenterImpl(this);

        readMoreAdapter = new ReadMoreAdapter(this, new ArrayList<DataViewModel>(),type);
        activity_read_more_rv.setAdapter(readMoreAdapter);
        activity_read_more_rv.addOnScrollListener(scrollListener);
        layoutManager = new LinearLayoutManager(this); // (Context context, int spanCount)
        activity_read_more_rv.setLayoutManager(layoutManager);
        activity_read_more_rv.setItemAnimator(new DefaultItemAnimator());

        activity_read_more_swipe.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        doYourUpdate();
                        Log.i("here", "swipe to refresh");
                    }
                }
        );

        initToolbar();
    }

    private void initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_back_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        title.setText(type.equals("news") ? getString(R.string.health_news) :type.equals("features") ?
                getString(R.string.health_features) : getString(R.string.top_news) );
    }

    private void doYourUpdate() {
        readMorePresenter.swipeToGetData();
    }

    void onItemsLoadComplete() {
        activity_read_more_swipe.setRefreshing(false);
    }

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            Log.i("here", "onScrollDown "+ dx+"  "+dy);
            visibleItemCount = layoutManager.getChildCount();
            totalItemCount = layoutManager.getItemCount();
            pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
            if (isLoadMore && readMorePresenter != null) {
                if ((visibleItemCount + pastVisibleItems) >= totalItemCount && dy > 0 && totalItemCount >= 3) {
                    readMorePresenter.getData(offset);
                    Log.i("here", "onScrollDown req");
                }
            }

        }
    };

    

    @Override
    public void showError(String error_msg) {
        hidePDialog();
        showMessage(error_msg);
    }

    @Override
    public void showServerError() {
       
    }

    public void showMessage(String s) {
        try {
            snackbar.setText(s);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#ffed5565"));
            TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setBackgroundColor(Color.parseColor("#ffed5565"));
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            if (!snackbar.isShown())
                snackbar.show();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

    }
    
    @Override
    public void showPDialog() {
        activity_read_more_pb.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePDialog() {
        activity_read_more_pb.setVisibility(View.GONE);
    }

    @Override
    public void hideSwipe() {
        onItemsLoadComplete();
    }

    @Override
    public void resetView() {
        offset(1);
        readMoreAdapter = new ReadMoreAdapter(this, new ArrayList<DataViewModel>(),type);
        activity_read_more_rv.setAdapter(readMoreAdapter);
    }

    @Override
    public void setData(ArrayList<DataViewModel> studio) {
        readMoreAdapter.addAll(studio);
    }


    @Override
    public void offset(int offset) {
        this.offset = offset;
    }

    @Override
    public void setLoadMore(boolean b) {
        this.isLoadMore = b;
    }

    @Override
    public void setFirstOpen(boolean b) {
        this.firstOpen = b;
        if (b) activity_read_more_rv.setVisibility(View.GONE);
        else activity_read_more_rv.setVisibility(View.VISIBLE);
    }

    @Override
    public int getPage() {
        return this.page;
    }



    @Override
    public void onNetWorkStateChanged(boolean connected) {
        if (connected && offset == 1) {
            readMorePresenter.getData(offset);
        }else if(!connected && activity_read_more_pb.getVisibility() == View.VISIBLE ){
            activity_read_more_pb.setVisibility(View.GONE);
        }
    }

    @Override
    public void goToDetails(int position) {
        readMorePresenter.goToDetails(position);
    }
   
}

