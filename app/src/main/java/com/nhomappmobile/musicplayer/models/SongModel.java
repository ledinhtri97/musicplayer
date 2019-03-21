package com.nhomappmobile.musicplayer.models;
//Class chua thong tin mot ban nhac
public class SongModel {
    private long id; //dinh danh ID
    private String title; //Tieu de
    private String artist; //Nghe si
    //Constructer cho 1 bai nhac
    public SongModel(long songID,String songTitle,String songArtist){
        id=songID;
        title=songTitle;
        artist=songArtist;
    }
    //Cac phuong thuc truy xuat thong tin bai nhac
    public long getID(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getArtist(){
        return artist;
    }
}
