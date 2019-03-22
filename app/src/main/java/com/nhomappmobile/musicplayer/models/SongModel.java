package com.nhomappmobile.musicplayer.models;
//Class chua thong tin mot ban nhac
public class SongModel {
    private long songid; //dinh danh ID
    private String title; //Tieu de
    private String artist; //Nghe si
    private String album;
    private long albumId;
    private int duration;
    private int year;
    //Constructer cho 1 bai nhac
    public SongModel(long songID,String songTitle,String songArtist,String songAlbum, long songalbumId,int songDuration){
        songid=songID;
        title=songTitle;
        artist=songArtist;
        album=songAlbum;
        albumId=songalbumId;
        duration=songDuration;
    }
    //Cac phuong thuc truy xuat thong tin bai nhac
    public long getSongID(){
        return songid;
    }

    public String getTitle(){
        return title;
    }

    public String getArtist(){
        return artist;

    }
    public String getAlbum(){
        return album;
    }
    public long getAlbumId(){
        return albumId;
    }
    public int getDuration(){
        return duration;
    }
    public int getYear(){
        return year;
    }
}
