package com.techapps.underconstruction.popularmoviesapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Walid Sakr on 6/29/2015.
 */
public class MyJSONDataParser {
    public static final String TAG_PAGE = "page";
    public static final String TAG_RESULTS = "results";
    public static final String TAG_ADULT = "adult";
    public static final String TAG_BACKDROP_PATH = "backdrop_path";
    public static final String TAG_GENRE_IDs = "genre_ids";
    public static final String TAG_MOVIE_ID = "id";
    public static final String TAG_ORIGINAL_LANGUAGE = "original_language";
    public static final String TAG_ORIGINAL_TITLE = "original_title";
    public static final String TAG_OVERVIEW = "overview";
    public static final String TAG_RELEASE_DATE = "release_date";
    public static final String TAG_POSTER_PATH = "poster_path";
    public static final String TAG_POPULARITY = "popularity";
    public static final String TAG_TITLE = "title";
    public static final String TAG_VIDEO = "video";
    public static final String TAG_VOTE_AVERAGE = "vote_average";
    public static final String TAG_VOTE_COUNT = "vote_count";
    public static final String TAG_VIDEO_KEY = "key";
    public static final String TAG_MOVIE_BITMAP = "movie_bitmap";
    public static final String TAG_TOTAL_PAGES = "total_pages";
    private static final int TOTAL_NUMBER_OF_PAGES = 1000;

    private JSONArray resultMovies;
    private JSONArray resultMovieVideoData;
    private String movieTitle;
    private int movieID;
    private static boolean isDataLoaded = false;
    private String jsonData;


    private ArrayList<Integer> movie_genres_id_list = new ArrayList<>();
    private ArrayList<HashMap> movieListMap = new ArrayList<>();
    private ArrayList<String> videoList = new ArrayList<>();



    public MyJSONDataParser(String jsonData){
        this.jsonData = jsonData;
        try {
            parseJsonData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public MyJSONDataParser(int movieID,String newJsonData){
        try {
            parseVideoData(movieID,newJsonData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseVideoData(int movie, String newJsonData) throws JSONException {
        JSONTokener jsonTokener = new JSONTokener(newJsonData);
        JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
        int movieID = jsonObject.getInt(TAG_MOVIE_ID);
        if (movieID == movie) {
            String videoKey;
            resultMovieVideoData = jsonObject.getJSONArray(TAG_RESULTS);
            for (int i =0; i<resultMovieVideoData.length();i++) {
                videoKey = resultMovieVideoData.getJSONObject(i).getString(TAG_VIDEO_KEY);
                videoList.add(videoKey);
            }
        }
    }

    public ArrayList<String> getVideoKeyList(){
        return videoList;
    }



    private void parseJsonData() throws JSONException {
        JSONTokener jsonTokener = new JSONTokener(jsonData);
        JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
        int pageNumber= jsonObject.getInt(TAG_PAGE);
        resultMovies=jsonObject.getJSONArray(TAG_RESULTS);
        movieListMap.clear();
        for (int i =0; i<resultMovies.length();i++){
            HashMap movieDataMAP = new HashMap();
            JSONObject eachMovieDataJSON = resultMovies.getJSONObject(i);
            boolean adult = eachMovieDataJSON.getBoolean(TAG_ADULT);
            movieDataMAP.put(TAG_ADULT,adult);
            String backdrop_path = eachMovieDataJSON.getString(TAG_BACKDROP_PATH);
            movieDataMAP.put(TAG_BACKDROP_PATH,backdrop_path);
            JSONArray genre_ids = eachMovieDataJSON.getJSONArray(TAG_GENRE_IDs);
            for (int j=0;j<genre_ids.length();j++) movie_genres_id_list.add(genre_ids.getInt(j));
            movieDataMAP.put(TAG_GENRE_IDs, movie_genres_id_list);
            movieID = eachMovieDataJSON.getInt(TAG_MOVIE_ID);
            movieDataMAP.put(TAG_MOVIE_ID,movieID);
            String original_language = eachMovieDataJSON.getString(TAG_ORIGINAL_LANGUAGE);
            movieDataMAP.put(TAG_ORIGINAL_LANGUAGE,original_language);
            String original_title = eachMovieDataJSON.getString(TAG_ORIGINAL_TITLE);
            movieDataMAP.put(TAG_ORIGINAL_TITLE,original_title);
            String overview = eachMovieDataJSON.getString(TAG_OVERVIEW);
            movieDataMAP.put(TAG_OVERVIEW,overview);
            String release_date = eachMovieDataJSON.getString(TAG_RELEASE_DATE);
            movieDataMAP.put(TAG_RELEASE_DATE,release_date);
            String poster_path = eachMovieDataJSON.getString(TAG_POSTER_PATH);
            movieDataMAP.put(TAG_POSTER_PATH,poster_path);
            double popularity = eachMovieDataJSON.getDouble(TAG_POPULARITY);
            movieDataMAP.put(TAG_POPULARITY,popularity);
            movieTitle = eachMovieDataJSON.getString(TAG_TITLE);
            movieDataMAP.put(TAG_TITLE,movieTitle);
            boolean video = eachMovieDataJSON.getBoolean(TAG_VIDEO);
            movieDataMAP.put(TAG_VIDEO,video);
            double vote_average = eachMovieDataJSON.getDouble(TAG_VOTE_AVERAGE);
            DecimalFormat decimalFormat = new DecimalFormat("0.0");
            decimalFormat.setRoundingMode(RoundingMode.CEILING);
            movieDataMAP.put(TAG_VOTE_AVERAGE,decimalFormat.format(vote_average*0.6));
            int vote_count = eachMovieDataJSON.getInt(TAG_VOTE_COUNT);
            movieDataMAP.put(TAG_VOTE_COUNT, vote_count);

            movieListMap.add(movieDataMAP);
        }

    }

    public ArrayList<HashMap> getMovieListAllData() {
        return movieListMap;
    }

    public static int getTotalPages(){
        return TOTAL_NUMBER_OF_PAGES;
    }

    public HashMap getSingleScreenData (HashMap movieDataMap){
        HashMap singleMovieData = new HashMap();
        singleMovieData.put(MyJSONDataParser.TAG_TITLE,movieDataMap.get(MyJSONDataParser.TAG_TITLE));
        singleMovieData.put(MyJSONDataParser.TAG_VOTE_AVERAGE, movieDataMap.get(MyJSONDataParser.TAG_VOTE_AVERAGE));
        return singleMovieData;
    }


}
