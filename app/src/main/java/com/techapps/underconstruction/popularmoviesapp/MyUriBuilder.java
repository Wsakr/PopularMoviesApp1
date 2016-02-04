package com.techapps.underconstruction.popularmoviesapp;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Walid Sakr on 7/14/2015.
 */

/*
THE TMDB API to be inserted in the "MyUriBuilder" class in the TMDB_API static class member

this is the format for searching TMDB api
https://api.themoviedb.org/3/search/movie?api_key=**************************&query=kill

then this is the format for discovering with generes
http://api.themoviedb.org/3/discover/movie?api_key=***************************&with_genres=878&sort_by=popularity.desc
to get more than one page we add "&page=1 or 2 or 3 .......

format for images URI
To build an image URL, you will need 3 pieces of data. The base_url, size and file_path.
Simply combine them all and you will have a fully qualified URL.
Here’s an example URL:http://image.tmdb.org/t/p/w500/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg
*/
 class MyUriBuilder {
    private static final String TMDB_API = "e8692fec9d27c7482fb5cbbc0f49f443";
    protected static final String DISCOVER = "discover";
    protected static final String SEARCH = "search";
    protected static final String GENRE = "genre";
    protected static final String MOVIE = "movie";
    protected static final String MOVIE_TITLE = "movie titles";
    protected static final String MOVIE_RATING = "movie rating";
    protected static final String IMAGE = "image";
    protected static final String VIDEO = "videos";
    private static final String BASE_URI = "api.themoviedb.org";
    private static final String BASE_IMAGE_URI = "image.tmdb.org";
    private String imageSize = "w" + "300";
    private String imagePath = "";

    private String urlStr;
    private  Uri.Builder builderData = new Uri.Builder();
    private  Uri.Builder videoLinkData = new Uri.Builder();
    private  Uri.Builder builderImage = new Uri.Builder();
    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    public MyUriBuilder(String TAG, String srchOrDscvrStr, int pageNumber, String sortingOption){

        builderData = new Uri.Builder().scheme("http").authority(BASE_URI).appendPath("3");

        switch (TAG){
            case DISCOVER:
                builderData.appendPath(DISCOVER);
                builderData.appendPath(MOVIE);
                builderData.appendQueryParameter("api_key", TMDB_API);
                builderData.appendQueryParameter("release_date.lte", date);
                if (sortingOption != null){
                builderData.appendQueryParameter("sort_by",sortingOption); }   //sort_by=popularity.desc
                builderData.appendQueryParameter("page",String.valueOf(pageNumber));
                Log.i("Walid URL", String.valueOf(builderData));
                break;

            case SEARCH:
                builderData.appendPath(SEARCH).appendPath(MOVIE)
                        .appendQueryParameter("api_key", TMDB_API)
                        .appendQueryParameter("release_date.lte", date)
                        .appendQueryParameter("query", srchOrDscvrStr);
                        //.appendQueryParameter("sort_by","");    sort_by=popularity.desc;
                break;

            case GENRE:
                // to get the list of movie genres available and their IDs
                builderData.appendPath(GENRE).appendPath(MOVIE).appendPath("list")
                                        .appendQueryParameter("api_key", TMDB_API);
                break;

        }

    }


    public MyUriBuilder (String TAG){
        builderImage = new Uri.Builder().scheme("http").authority(BASE_IMAGE_URI)
                .appendPath("t").appendPath("p");
        builderImage.appendPath(imageSize);
    }

    public MyUriBuilder(int movieID){
        videoLinkData = new Uri.Builder().scheme("http").authority(BASE_URI).appendPath("3");
        videoLinkData.appendPath(MOVIE).appendPath(String.valueOf(movieID)).appendPath(VIDEO).appendQueryParameter("api_key", TMDB_API);
        Log.i("Walid videoURL", String.valueOf(videoLinkData));
    }


    String getUrlStr(){
        this.urlStr= builderData.build().toString() ;
        return this.urlStr;
    }


    String getImageUrlStr(){
        this.urlStr= builderImage.build().toString() ;
        return this.urlStr;
    }
    String getVideoUrlStr(){
         return videoLinkData.build().toString() ;
    }


}

