package com.nhomappmobile.musicplayer.fragments;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhomappmobile.musicplayer.R;
import com.nhomappmobile.musicplayer.adapters.SongsListAdapter;
import com.nhomappmobile.musicplayer.models.SongModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SongsFragment extends Fragment {
    private List<SongModel> songlist = new ArrayList<>();
    private SongsListAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstancesState){
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.fragment_main,container,false
        );
        //Lay du lieu danh sach nhac
        setupSongsList(recyclerView);

        //Sap xep thu tu abc
        Collections.sort(songlist,new Comparator<SongModel>(){
            public int compare(SongModel a, SongModel b){
                return a.getTitle().compareTo(b.getTitle());
            }
        });
        return recyclerView;
    }

    private void setupSongsList(RecyclerView recyclerView){
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri,null,null,null,null);

        if(musicCursor != null && musicCursor.moveToFirst()){
            //Lay cot
            int titleColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);
            int albumColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int albumIdColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int durationColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            //them nhac vao danh sach list
            do{
                long thisId = musicCursor.getLong(idColumn);
                long albumId = musicCursor.getLong(albumIdColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                //songlist.add(new SongModel(thisId,thisTitle,thisArtist));
                String thisAlbum = musicCursor.getString(albumColumn);
                int thisDuration  = musicCursor.getInt(durationColumn);
                songlist.add(new SongModel(thisId,thisTitle,thisArtist,thisAlbum,albumId,thisDuration));
            }
            while (musicCursor.moveToNext());
        }
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));

        mAdapter = new SongsListAdapter(getActivity(),songlist);
        recyclerView.setAdapter(mAdapter);
    }
}
