package com.nhomappmobile.musicplayer.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhomappmobile.musicplayer.R;
import com.nhomappmobile.musicplayer.models.SongModel;
import com.nhomappmobile.musicplayer.utils.ArtworkUtils;

import java.util.List;

public class SongsListAdapter extends RecyclerView.Adapter<SongsListAdapter.AllSongsGridHolder> {
    private List<SongModel> allSongsList; //Chứa list nhạc, các class Songmodel
    private Context mContext;
    //Phương thức cập nhật list nhạc
    public SongsListAdapter(Context context,List<SongModel> allSongsList){
        this.allSongsList = allSongsList;
        this.mContext = context;
    }

    @Override
    public AllSongsGridHolder onCreateViewHolder(ViewGroup viewGroup,int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_song,null);
        AllSongsGridHolder ml = new AllSongsGridHolder(v);
        return ml;
    }

    @Override
    public void onBindViewHolder(AllSongsGridHolder allSongsGridHolder, int i){
        SongModel allSongsItem = allSongsList.get(i);

        allSongsGridHolder.title.setText(allSongsItem.getTitle());
        allSongsGridHolder.artist.setText(allSongsItem.getArtist());
        allSongsGridHolder.albumArt.setImageBitmap(ArtworkUtils.getArtworkFromFile(mContext,allSongsItem.getAlbumId(),25,25));
    }

    @Override
    public int getItemCount(){
        return (null != allSongsList ? allSongsList.size():0);
    }

    public class AllSongsGridHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        protected  TextView title,artist;
        protected ImageView albumArt;
        public AllSongsGridHolder(View view){
            super(view);
            this.title = (TextView) view.findViewById(R.id.song_title);
            this.artist = (TextView) view.findViewById(R.id.song_artist);
            this.albumArt=(ImageView) view.findViewById(R.id.albumArt) ;
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v){

        }
    }


}
