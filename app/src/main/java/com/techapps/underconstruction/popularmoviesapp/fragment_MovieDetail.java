package com.techapps.underconstruction.popularmoviesapp;


import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class fragment_MovieDetail extends android.support.v4.app.Fragment {

    private  FrameLayout bigBackImage;
    private  FrameLayout smallFrontImage;
    private  TextView movieTitleTV;
    private  TextView ratingValueTV;
    private  TextView releaseDateTV;
    private  TextView numberOfVotesTV;
    private  TextView moviePlotTV;
    private  RatingBar movieRatingBar;

    private String movieTitle;
    private float movieRating;
    private int voteCount;
    private String releaseDate;
    private String movieOverview;
    private String backdropPath;

    //private String mParam1;
    //private String mParam2;

    private OnMovieDetailFragmentListener mListener;


    public fragment_MovieDetail() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated  (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateDetailFragView();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragMovieDetail;
            fragMovieDetail = inflater.inflate(R.layout.fragment_movie_detail, container, false);

            bigBackImage = (FrameLayout) fragMovieDetail.findViewById(R.id.backDropLayout);
            smallFrontImage = (FrameLayout) fragMovieDetail.findViewById(R.id.moviePosterLayout);
            movieTitleTV = (TextView) fragMovieDetail.findViewById(R.id.movieTitleTV);
            ratingValueTV = (TextView) fragMovieDetail.findViewById(R.id.ratingTV);
            numberOfVotesTV = (TextView) fragMovieDetail.findViewById(R.id.votesNumberTV);
            releaseDateTV = (TextView) fragMovieDetail.findViewById(R.id.dateTV);
            moviePlotTV = (TextView) fragMovieDetail.findViewById(R.id.moviePlotTV);
            movieRatingBar = (RatingBar) fragMovieDetail.findViewById(R.id.movieDetailRatingBar);
            movieRatingBar.setNumStars(6);
            movieRatingBar.setStepSize(0.1f);
        return fragMovieDetail;
    }


    public void onButtonPressed(boolean pressed) {
        if (mListener != null) {
            mListener.backdropImageLoaded(pressed);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnMovieDetailFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMovieDetailFragmentListener");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public interface OnMovieDetailFragmentListener {

         void backdropImageLoaded(boolean isLoaded);
    }

     private boolean updateDetailFragView(){

         movieTitleTV.setText(movieTitle);
         movieRatingBar.setRating(movieRating);
         ratingValueTV.setText(String.valueOf(movieRating));
         numberOfVotesTV.setText(String.valueOf(voteCount));
         releaseDateTV.setText(releaseDate);
         moviePlotTV.setText(movieOverview);
         String smallImageKey = MyTaskLoader.provideBitmapKey(movieTitle);
         startLoadingImages(smallImageKey,null);
         getLoaderManager().restartLoader(2, null, imageLoaderCallbacks);
         String bigImageKey = MyTaskLoader.provideBitmapKey("backdrop "+movieTitle);
         startLoadingImages(bigImageKey,backdropPath);
         getLoaderManager().restartLoader(3, null, imageLoaderCallbacks);
         return false;
    }

    public void setDisplayData(ArrayList movieData){
        //displayPosition = newDisplayPosition;
        movieTitle = (String) movieData.get(0);
        String rating = (String) movieData.get(1);
        movieRating = Float.valueOf(rating);
        voteCount = (int) movieData.get(2);
        releaseDate = (String) movieData.get(3);
        movieOverview = (String) movieData.get(4);
        backdropPath = (String) movieData.get(5);
        if (isAdded()) updateDetailFragView();
    }

    LoaderManager.LoaderCallbacks<Bitmap> imageLoaderCallbacks;
    private void startLoadingImages( final String imageKeyName,  final String backdropPath) {
        imageLoaderCallbacks = new LoaderManager.LoaderCallbacks<Bitmap>() {
                @Override
                public Loader<Bitmap> onCreateLoader(int id, Bundle args) {
                    return new MyTaskLoader.BitmapLoader(getActivity(), imageKeyName, backdropPath);
                }

                @Override
                public void onLoadFinished(Loader<Bitmap> loader, Bitmap data) {
                    Log.i("Walid BL Loadfinish", String.valueOf(loader.getId()));
                    if(backdropPath == null) {
                        smallFrontImage.setBackground(new BitmapDrawable(Resources.getSystem(), data));
                    }else {
                        bigBackImage.setBackground(new BitmapDrawable(Resources.getSystem(), data));
                        if (mListener != null){
                            mListener.backdropImageLoaded(true);
                        }
                    }
                    loader.reset();
                }

                @Override
                public void onLoaderReset(Loader<Bitmap> loader) {
                    loader.reset();
                }
            };
    }

    void stretchView(final float amount){

        bigBackImage.setPivotY(0);
        Log.i("Walid MovieDetailFrag","bigImageHeight "+String.valueOf(bigBackImage.getY()));
        Log.i("Walid MovieDetailFrag", "topBarHeight " + String.valueOf(amount));
        bigBackImage.animate().setInterpolator(new OvershootInterpolator()).scaleYBy(amount / (bigBackImage.getHeight() + amount)).setDuration(5000);
    }
}
