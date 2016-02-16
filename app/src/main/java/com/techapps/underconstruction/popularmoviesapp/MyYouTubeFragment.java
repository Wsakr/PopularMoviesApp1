package com.techapps.underconstruction.popularmoviesapp;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;


/**
 * Created by Walid Sakr on 2/1/2016.
 */
public class MyYouTubeFragment extends YouTubePlayerSupportFragment implements YouTubePlayer.OnInitializedListener{

    private static final String DEBUG_YOUTUBE_API_KEY = "AIzaSyBY6kuC2hRs7fsbmDW8G6VcfFTt0_eTQtI";
    private static final String VIDEO_ID = "VIDEO_ID";
    private String videoID;

    public MyYouTubeFragment(){
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
       // if (bundle != null && bundle.containsKey(VIDEO_ID)){
         //   videoID = bundle.getString(VIDEO_ID);
        //}
        //initialize(DEBUG_YOUTUBE_API_KEY,this);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        //bundle.putString(VIDEO_ID,videoID);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean isRestored) {
        Log.i("Walid MyYoutubeFragment","true");
        youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
        youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
        youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);

        if (videoID != null){
            if (isRestored){
                youTubePlayer.play();
            } else {
                youTubePlayer.loadVideo(videoID);
            }
        }
        Log.i("Walid MyYoutubeFragment",String.valueOf(videoID));
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    public void setVideoId(String newVideo){
        videoID = newVideo;
        initialize(DEBUG_YOUTUBE_API_KEY,this);
    }

    public void startPlaying(){

    }
}
