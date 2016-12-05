package com.augusta.dev.personalize.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.augusta.dev.personalize.R;

public class BrowseSongActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView rcvListBrowseSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_song);

        findViewById();
        initToolBar();

    }

    private void findViewById() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rcvListBrowseSong = (RecyclerView) findViewById(R.id.rcv_list_browse_song);

    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.str_browse_songs);
    }

}
