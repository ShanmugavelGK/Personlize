package com.augusta.dev.personalize.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.augusta.dev.personalize.R;
import com.augusta.dev.personalize.adapter.BrowseSongAdapter;
import com.augusta.dev.personalize.bean.SongBean;
import com.augusta.dev.personalize.interfaces.OnClickPlay;
import com.augusta.dev.personalize.utliz.Constants;
import com.augusta.dev.personalize.utliz.MPermission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BrowseSongActivity extends AppCompatActivity {

    //how to get count of selected items in recyclerview android
    private Toolbar toolbar;
    private RecyclerView rcvListBrowseSong;

    private BrowseSongAdapter mAdapter;
    private List<SongBean> listBrowseSongList;
    private ArrayList<SongBean> selectedSongList;
    private MediaPlayer mediaPlayer;
    public int position = 0;
    int count;

    String[] permissionsList = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_song);

        findViewById();
        initToolBar();

        bindRecyclerView();
        if (MPermission.checkPermissions(this, permissionsList)) {
            bindData();
        }

    }

    private void bindData() {
        listBrowseSongList = new ArrayList<>();
        listBrowseSongList = getAudioList();
        mAdapter = new BrowseSongAdapter(listBrowseSongList, this);
        rcvListBrowseSong.setAdapter(mAdapter);


        mAdapter.setOnClickPlay(new OnClickPlay() {
            @Override
            public void onClickPlay(int position) {

                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer = new MediaPlayer();
                    } else {
                        mediaPlayer = new MediaPlayer();
                    }
                } else {
                    mediaPlayer = new MediaPlayer();
                }

                BrowseSongActivity.this.position = position;

                if (mAdapter.getListBrowseSongList().get(position).isPlay()) {
                    try {
                        mediaPlayer.setDataSource(mAdapter.getListBrowseSongList().get(position).getSongPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();
                }
            }
        });


    }

    private void bindRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcvListBrowseSong.setLayoutManager(mLayoutManager);
        rcvListBrowseSong.setItemAnimator(new DefaultItemAnimator());
    }

    private void findViewById() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rcvListBrowseSong = (RecyclerView) findViewById(R.id.rcv_list_browse_song);
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.str_browse_songs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public List<SongBean> getAudioList() {
        List<SongBean> browseSongList = new ArrayList<>();

        Cursor mCursor;
        mCursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media._ID}, null, null, MediaStore.Audio.Media.DISPLAY_NAME);


        SongBean model;
        while (mCursor.moveToNext()) {
            int id = mCursor.getInt(mCursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
            String name = mCursor.getString(mCursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
            String path = mCursor.getString(mCursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

            model = new SongBean(id, name, path, false);
            browseSongList.add(model);
        }
        mCursor.close();
        return browseSongList;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null)
            mediaPlayer.stop();

        if (mAdapter != null) {
            mAdapter.getListBrowseSongList().get(position).setPlay(false);
            mAdapter.notifyItemChanged(position);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_browser_song, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_song_select:
                count = 0;

                selectedSongList = new ArrayList<>();
                for (int i = 0; i < listBrowseSongList.size(); i++) {
                    if (listBrowseSongList.get(i).isSelect()) {
                        selectedSongList.add(listBrowseSongList.get(i));
                        count++;
                    }
                }
                goBackActivity();
                return true;
            case android.R.id.home:
                goBackActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void goBackActivity() {
        Intent intent = new Intent(BrowseSongActivity.this, RouseBrowseActivity.class);
        Bundle bundle = new Bundle();
        if (count > 0)
            bundle.putParcelableArrayList("selected_song_list", selectedSongList);
        else
            bundle.putParcelableArrayList("selected_song_list", null);
        intent.putExtras(bundle);
        setResult(2, intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    bindData();
                }
                return;
            }
        }
    }
}
