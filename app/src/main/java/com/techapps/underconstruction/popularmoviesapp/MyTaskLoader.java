package com.techapps.underconstruction.popularmoviesapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import com.jakewharton.disklrucache.DiskLruCache;
import org.json.JSONException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Walid Sakr on 10/22/2015.
 */

import android.support.v7.appcompat.*;

public class MyTaskLoader<E>
  {
    private static String SrcOrDscvr;
    private static boolean internetConnected = true;



    public MyTaskLoader() {
        super();
    }

    static ArrayList<HashMap> movieList = new ArrayList<>();

    public static class DataLoader extends AsyncTaskLoader<ArrayList<HashMap>> {
        private ArrayList<HashMap> screenDataList;
        private MyUriBuilder myUriBuilder;
        private String dataUrlStr;
        private MyJSONDataParser myJSONDataParser ;

        public DataLoader(Context context, String tag, String SrcOrDscvr, int pageNumber, String sortingOptions) {
            super(context);
            myUriBuilder = new MyUriBuilder(tag, SrcOrDscvr, pageNumber, sortingOptions);
        }


        @Override
        public ArrayList<HashMap> loadInBackground() {

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            internetConnected = MainActivity.isInternetConnected();

            if (!internetConnected) {
                // Toast.makeText(getActivityContext(), "No Internet Connection", Toast.LENGTH_LONG);
                //Load data from previous data or something i have to check
                return null;
            } else {
                this.dataUrlStr = myUriBuilder.getUrlStr();
                try {
                    URL url = new URL(dataUrlStr);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    inputStream = urlConnection.getInputStream();
                    int http_status = urlConnection.getResponseCode();
                    Log.i("Walid DL Http_Status",String.valueOf(http_status));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                InputStreamReader inputStreamReader = null;
                if (inputStream != null) {
                    inputStreamReader = new InputStreamReader(inputStream);
                }
                assert inputStreamReader != null;
                BufferedReader myBufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sbTemp = new StringBuilder();
                String line;

                try {
                    while ((line = myBufferedReader.readLine()) != null) {
                        sbTemp.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        myBufferedReader.close();
                        inputStreamReader.close();
                        inputStream.close();
                        urlConnection.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String responseJSON = sbTemp.toString();
                    try {
                        myJSONDataParser = new MyJSONDataParser(responseJSON);
                    } finally {
                        movieList = myJSONDataParser.getMovieListAllData();
                    }
                }
                    screenDataList=new ArrayList<>();
                for (int i = 0 ; i<movieList.size();++i){
                    HashMap singleMovieData;
                    singleMovieData = myJSONDataParser.getSingleScreenData(movieList.get(i));
                    screenDataList.add(singleMovieData);
                }
            }

            return screenDataList;
        }

        @Override
        protected void onStartLoading (){
            super.onStartLoading();
             if (screenDataList != null){
               deliverResult(screenDataList);
           } else{
            forceLoad();
            Log.i("Walid DataLoader","onStartLoading");
            }
        }

       @Override
        public void deliverResult(ArrayList<HashMap> data){
            if (isReset()){
                if (data!=null) {
                    data.clear();
                    Log.i("Walid DataLoader", "deliverResult_isReset");
                }
            }
            if (isStarted()){
                Log.i("Walid DataLoader", "deliverResult_isStarted");
                super.deliverResult(screenDataList);
            }
        }


        @Override
        protected void onStopLoading(){
            Log.i("Walid DataLoader", "onStopLoading");
            cancelLoad();
            onCanceled(screenDataList);
        }

        @Override
        public void onCanceled(ArrayList<HashMap> data) {
            super.onCanceled(data);
            if (data != null)data.clear();
            Log.i("Walid DataLoader", "onCanceled");
        }

        @Override
        protected void onReset(){
            super.onReset();
            onStopLoading();
            if (screenDataList!=null){
                screenDataList.clear();
            }
            Log.i("Walid DataLoader", "onReset");
        }
    }
    public static class VideoLinkFetcher extends AsyncTaskLoader<String> {
        private String videoKey;
        private MyUriBuilder myUriBuilder;
        private String dataUrlStr;
        private MyJSONDataParser myJSONDataParser ;
        private int movieID;

        public VideoLinkFetcher(Context context, int movieID) {
            super(context);
            this.movieID = movieID;
            myUriBuilder = new MyUriBuilder(movieID);
        }


        @Override
        public String loadInBackground() {

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            internetConnected = MainActivity.isInternetConnected();

            if (!internetConnected) {
                // Toast.makeText(getActivityContext(), "No Internet Connection", Toast.LENGTH_LONG);
                //Load data from previous data or something i have to check
                return null;
            } else {
                this.dataUrlStr = myUriBuilder.getVideoUrlStr();
                try {
                    URL url = new URL(dataUrlStr);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    inputStream = urlConnection.getInputStream();
                    int http_status = urlConnection.getResponseCode();
                    Log.i("Walid DL Http_Status",String.valueOf(http_status));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                InputStreamReader inputStreamReader = null;
                if (inputStream != null) {
                    inputStreamReader = new InputStreamReader(inputStream);
                }
                assert inputStreamReader != null;
                BufferedReader myBufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sbTemp = new StringBuilder();
                String line;

                try {
                    while ((line = myBufferedReader.readLine()) != null) {
                        sbTemp.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        myBufferedReader.close();
                        inputStreamReader.close();
                        inputStream.close();
                        urlConnection.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String responseJSON = sbTemp.toString();
                    try {
                        myJSONDataParser = new MyJSONDataParser(movieID,responseJSON);
                    } finally {
                        videoKey = myJSONDataParser.getVideoKey();
                    }
                }
            }

            return videoKey;
        }

        @Override
        protected void onStartLoading (){
            super.onStartLoading();
             if (videoKey != null){
               deliverResult(videoKey);
           } else{
            forceLoad();
            Log.i("Walid DataLoader","onStartLoading");
            }
        }

       @Override
        public void deliverResult(String data){
            if (isReset()){
                if (data!=null) {
                    data = null;
                    Log.i("Walid DataLoader", "deliverResult_isReset");
                }
            }
            if (isStarted()){
                Log.i("Walid DataLoader", "deliverResult_isStarted");
                super.deliverResult(videoKey);
            }
        }


        @Override
        protected void onStopLoading(){
            Log.i("Walid DataLoader", "onStopLoading");
            cancelLoad();
            onCanceled(videoKey);
        }

        @Override
        public void onCanceled(String data) {
            super.onCanceled(data);
            if (data != null)data = null;
            Log.i("Walid DataLoader", "onCanceled");
        }

        @Override
        protected void onReset(){
            super.onReset();
            onStopLoading();
            if (videoKey!=null){
                videoKey = null;
            }
            Log.i("Walid DataLoader", "onReset");
        }
    }

    public static final int MAX_KEY_LENGTH = 64;
    public static int imagePositionCounter = -1;

    public static class BitmapLoader extends AsyncTaskLoader<Bitmap> {
        Bitmap bitmap;
        private DiskLruCache mDiskLruCache;
        private final Object mDiskCacheLock = new Object();
        private boolean mDiskCacheStarting = true;
        private static final long DISK_CACHE_SIZE = 1024 * 1024 * 25;
        private static final String IMAGE_DISK_CACHE_SUBDIR = "movie_posters";


        private MyUriBuilder imageUriBuilder;
        private String imageUrlStr;
        private String bitmapKey;
        private String backdropPath;
        private Context mContext;

        public BitmapLoader(Context context, String imageKey, String backdropPath) {
            super(context);
            mContext = context;
            bitmapKey = imageKey;
            this.backdropPath = backdropPath;
            if (mDiskLruCache == null  || mDiskLruCache.isClosed()) {
                final File cacheDir = getDiskCacheDir(mContext, IMAGE_DISK_CACHE_SUBDIR);
                synchronized (mDiskCacheLock) {
                    try {
                        mDiskLruCache = DiskLruCache.open(cacheDir, BuildConfig.VERSION_CODE, 1, DISK_CACHE_SIZE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mDiskCacheStarting = false;
                    Log.i("Walid MemCache Size", String.valueOf(mDiskLruCache.size()));
                    mDiskCacheLock.notifyAll();
                }
            }
            imageUriBuilder = new MyUriBuilder(MyUriBuilder.IMAGE);
        }

        @Override
        public Bitmap loadInBackground() {
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            int http_status = 0;

            if (!internetConnected) {
                //Load data from previous data or something i have to check
                return null;
            } else {
                try {
                    if (getBitmapFromDiskCache(bitmapKey)!=null) {
                        bitmap = getBitmapFromDiskCache(bitmapKey);
                        return bitmap;
                    } else {
                        this.imageUrlStr = imageUriBuilder.getImageUrlStr();
                        String imageLocation;
                        if (backdropPath == null) {
                            imageLocation = getMovieImagePath(imagePositionCounter);
                        }else {
                            imageLocation = backdropPath;
                        }
                        if (movieList.size() > 0 && !imageLocation.equals("-1")) {
                            imageUrlStr += imageLocation;
                        }
                        try {
                            URL url = new URL(imageUrlStr);
                            urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.connect();
                            http_status = urlConnection.getResponseCode();
                            Log.i("Walid Http_Status BL", String.valueOf(http_status));
                            if (http_status == HttpURLConnection.HTTP_OK)
                                inputStream = urlConnection.getInputStream();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        assert inputStream != null;
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        addBitmapToDiskCache(bitmapKey,bitmap);
                        try {
                            if (http_status == HttpURLConnection.HTTP_OK) inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        urlConnection.disconnect();
                        return bitmap;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onStartLoading (){
            super.onStartLoading();
            if (bitmap!=null){
                deliverResult(bitmap);
           } else{
                forceLoad();
                Log.i("Walid BitmapLoader", "onStartLoading");
            }
        }

        @Override
        public void deliverResult(Bitmap newbitmap){
            if (isReset()){
                if (bitmap!=null)
                {
                    bitmap = null;
                    Log.i("Walid BitmapLoader", "deliverResult_isReset");
                    return;
                }
            }
            Bitmap oldBitmap = bitmap;
            bitmap = newbitmap;
            if (isStarted()){
                super.deliverResult(bitmap);
                Log.i("Walid BitmapLoader", "deliverResult_isStarted");
            }
            if (oldBitmap!= null){
                oldBitmap = null;
            }
        }


        @Override
        protected void onStopLoading(){
            Log.i("Walid BitmapLoader", "onStopLoading");
            cancelLoad();
        }

         @Override
        public void onCanceled(Bitmap newBitmap) {
            super.onCanceled(newBitmap);
            newBitmap= null;
            Log.i("Walid BitmapLoader", "onCanceled");
        }

        @Override
        protected void onReset(){
            onStopLoading();
            if (bitmap != null){
                bitmap = null;
            }
            Log.i("Walid BitmapLoader", "onReset");
        }

        private String getMovieImagePath(int position){
            String movieImagePath="";
            if (movieList.size()> 0) {
                movieImagePath = movieList.get(position).get(MyJSONDataParser.TAG_POSTER_PATH).toString();
            }else movieImagePath = "-1";

            return movieImagePath;
        }


        private String getMovieBackdropPath(int position){
            String movieImagePath="";
            if (movieList.size()> 0) {
                movieImagePath = movieList.get(position).get(MyJSONDataParser.TAG_BACKDROP_PATH).toString();
            }else movieImagePath = "-1";

            return movieImagePath;
        }


        private File getDiskCacheDir(Context context, String uniqueName) {
            final String cachePath = context.getCacheDir().getPath();
            return new File(cachePath + File.separator + uniqueName);
        }

        void addBitmapToDiskCache(String key, Bitmap bitmap) throws IOException {
            synchronized (mDiskCacheLock) {
                while (mDiskCacheStarting) {
                    try {
                        mDiskCacheLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (mDiskLruCache != null && key != null) {

                    DiskLruCache.Editor mCacheEditor = mDiskLruCache.edit(key);

                    OutputStream stream = new BufferedOutputStream(mCacheEditor.newOutputStream(0));
                    boolean imageInCache = false;
                    if (bitmap != null)
                        imageInCache = bitmap.compress(Bitmap.CompressFormat.PNG, 75, stream);
                    Log.i("Walid MemCache addImage", stream.toString());
                    if (imageInCache) {
                        stream.close();
                        mDiskLruCache.flush();
                        mCacheEditor.commit();
                    }
                }
            }
            Log.i("Walid MemCache addImage",String.valueOf(mDiskLruCache.size()));
        }

        Bitmap getBitmapFromDiskCache(String key) throws IOException, ClassNotFoundException {
            Bitmap bitmap;
            synchronized (mDiskCacheLock) {
                while (mDiskCacheStarting){
                    try {
                        mDiskCacheLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (mDiskLruCache != null && key !=null && mDiskLruCache.get(key)!=null) {
                    DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
                    InputStream objectInputStream = new BufferedInputStream(snapshot.getInputStream(0));
                    bitmap = BitmapFactory.decodeStream(objectInputStream);
                    if (bitmap!=null){
                        snapshot.close();
                        objectInputStream.close();
                    }
                    Log.i("Walid MemCache getBTMap",String.valueOf(bitmap.getByteCount()));
                    return bitmap;
                }
            }
            return null;
        }
    }
    public static String provideBitmapKey(String movieTitle) {
        String bitmapKey = null;
        if (movieTitle != null){
            bitmapKey = movieTitle.toLowerCase().replaceAll("[^a-z0-9_-]", "_");
            int maxLength = (bitmapKey.length() < MAX_KEY_LENGTH) ? bitmapKey.length() : MAX_KEY_LENGTH;
            bitmapKey = bitmapKey.substring(0, maxLength);
            Log.i("Walid MemCache Key", bitmapKey);
        }
        return bitmapKey;
    }

}
