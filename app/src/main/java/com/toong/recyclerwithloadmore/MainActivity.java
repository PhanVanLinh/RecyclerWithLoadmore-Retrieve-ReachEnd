package com.toong.recyclerwithloadmore;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import com.toong.recyclerwithloadmore.adapter.MyRecyclerViewAdapter;
import com.toong.recyclerwithloadmore.helper.EndlessRecyclerViewScrollListener;
import com.toong.recyclerwithloadmore.model.Item;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements MyRecyclerViewAdapter.ItemClickListener, MyRecyclerViewAdapter.RetryLoadMoreListener {
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter adapter;
    private ArrayList<Item> data;
    private int currentPage;

    private boolean emulatorLoadMoreFaild = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Recycler With Loadmore");

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        data = new ArrayList<>();
        data.add(new Item("0", "b"));
        data.add(new Item("1", "b"));
        data.add(new Item("2", "b"));
        data.add(new Item("3", "b"));
        data.add(new Item("4", "b"));
        data.add(new Item("5", "b"));
        data.add(new Item("6", "b"));
        data.add(new Item("7", "b"));
        data.add(new Item("8", "b"));
        data.add(new Item("9", "b"));
        data.add(new Item("10", "b"));

        // set up the RecyclerView
        LinearLayoutManager l = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(l);
        adapter = new MyRecyclerViewAdapter(this, this, this);

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(l){
            @Override
            public void onLoadMore(final int page) {
                Log.i("TAG", "load more "+page);
                currentPage = page;
                loadMore(page);
            }
        });

        adapter.add(data);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onRetryLoadMore() {
        loadMore(currentPage);
    }

    /**
     * The logic is only for testing
     */
    private void loadMore(final int page){
        adapter.startLoadMore();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(page == 2 && emulatorLoadMoreFaild){
                    adapter.onLoadMoreFailed();
                    emulatorLoadMoreFaild = false;
                    return;
                }
                if(page == 3){
                    adapter.onReachEnd();
                    return;
                }
                adapter.add(data);
            }
        }, 500);
    }
}
