package com.techapps.underconstruction.popularmoviesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Walid Sakr on 2/18/2016.
 */
public class YoutubeListAdapter extends RecyclerView.Adapter<YoutubeListAdapter.CustomViewHolder> implements YouTubeThumbnailView.OnInitializedListener{

    private LayoutInflater mLayoutInflater;
    private HashMap<View,YouTubeThumbnailLoader> thumbnailList;
    private ArrayList<String> videoList;


    public YoutubeListAdapter(ArrayList<String> list){
        videoList = list;
        thumbnailList = new HashMap<>(videoList.size());
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        View mView = mLayoutInflater.inflate(R.layout.youtube_list_item,parent,false);
        CustomViewHolder viewHolder = new CustomViewHolder(mView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        holder.thumbnailView.setTag(videoList.get(position));
        holder.thumbnailView.initialize(MyYouTubeFragment.DEBUG_YOUTUBE_API_KEY,this);
        final YouTubeThumbnailLoader thumbnailLoader = thumbnailList.get(holder.thumbnailView);
        if (thumbnailLoader != null){
            thumbnailLoader.setVideo(videoList.get(position));
        } else {
            holder.thumbnailView.setTag(videoList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    @Override
    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
        thumbnailList.put(youTubeThumbnailView,youTubeThumbnailLoader);
        youTubeThumbnailLoader.setVideo((String) youTubeThumbnailView.getTag());
    }

    @Override
    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        private YouTubeThumbnailView thumbnailView;

        public CustomViewHolder(View itemView) {
            super(itemView);
            thumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.thumbnail);
        }
    }

    public void setVideoList(ArrayList<String> newList){
        videoList = newList;
        notifyDataSetChanged();
    }
}
