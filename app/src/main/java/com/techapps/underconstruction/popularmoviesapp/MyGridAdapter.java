package com.techapps.underconstruction.popularmoviesapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.RatingBar;

import android.widget.TextView;



import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Walid Sakr on 6/29/2015.
 */
public class MyGridAdapter extends RecyclerView.Adapter<MyGridAdapter.MyViewHolder> {

    private static final int ONE_PAGE_MOVIES = 20;
    private LayoutInflater layoutInflater;
    private ArrayList<HashMap> data;
    private Bitmap placeHolder;


    public MyGridAdapter(Bitmap placeHolder){
        this.placeHolder = placeHolder;

    }
        public interface MyGridAdapterListener{
            public void onDataSetChanged(boolean state);
        }

    private MyGridAdapterListener listener = null;
    public void registerAdapterListener(MyGridAdapterListener newListener){
        listener = newListener;
    }
    public void unRegisterAdapterListener(){
        listener = null;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.gridlayout_item_movie_start, parent, false);
        MyViewHolder vHolder=new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (data != null && !data.isEmpty()){
            HashMap singleData = data.get(position);
            String movieRate = (String) singleData.get(MyJSONDataParser.TAG_VOTE_AVERAGE);
            Bitmap movieBitmap;
            String movieTitle = (String) data.get(position).get(MyJSONDataParser.TAG_TITLE);
            String bitmapTtle = MyTaskLoader.provideBitmapKey(movieTitle);
            if (singleData.containsKey(bitmapTtle)){
                movieBitmap = (Bitmap)singleData.get(bitmapTtle);
            }else {
                movieBitmap = placeHolder;
            }
            Drawable bitmapDrawable = new BitmapDrawable(Resources.getSystem(),movieBitmap);
            holder.movieTitle.setText((String)singleData.get(MyJSONDataParser.TAG_TITLE));
            holder.movieLayout.setBackground(bitmapDrawable);
            holder.movieRating.setRating(Float.valueOf(movieRate));
                Log.i("Walid GridAdapter", "onBindViewHolder_Load" + String.valueOf(data.get(position)));
        }else {
            holder.movieTitle.setText("Loading...");
            //holder.movieLayout.setBackground(new BitmapDrawable(Resources.getSystem(),placeHolder));
            holder.movieRating.setRating(Float.valueOf(3.3f));
        }
    }

    @Override
    public void onViewRecycled(MyViewHolder holder) {
        super.onViewRecycled(holder);
        Log.i("Walid Adapter", "ViewRecycled Before " + String.valueOf(holder.movieLayout.getBackground()));
        holder.movieLayout.setBackground(null);
        Log.i("Walid Adapter","ViewRecycled After "+String.valueOf(holder.movieLayout.getBackground()));
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }


    @Override
    public int getItemCount() {
        if (data!=null && !data.isEmpty()){
            Log.i("Walid getItemCount",String.valueOf(data.size()));
            return data.size();
        }else {
            return ONE_PAGE_MOVIES;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder  {
        private TextView movieTitle;
        private RatingBar movieRating;
        private LinearLayout movieLayout;

        public MyViewHolder(View v) {
            super(v);
            movieTitle = (TextView) v.findViewById(R.id.movieTitleTextView);
            movieRating = (RatingBar) v.findViewById(R.id.ratingBar);
            movieLayout = (LinearLayout) v.findViewById(R.id.movieLinearLayout);
            movieRating.setNumStars(6);
            movieRating.setStepSize((float) 0.1);

            //noinspection ResourceType
            //moviePoster.setBackgroundColor(R.color.transdarkred);
        }
    }

    private boolean dataChanged = false;

    public void setMyAdapterData(ArrayList<HashMap> data){
        if (data != null) {
            clearData();
            this.data = (ArrayList<HashMap>) data.clone();
            Log.i("Walid onLoadFinishedin", String.valueOf(this.data));
            Log.i("Walid GridAdaptListener", String.valueOf(dataChanged));
            } else {
            //this.data = (ArrayList<HashMap>) data.clone();
             }
            dataChanged = true;
            if (listener != null) {
                listener.onDataSetChanged(dataChanged);
            }
            //dataChanged = false;
    }

    public ArrayList<HashMap> getMyAdapterData(){
        return data;
    }

    public void replaceBitmap(int position ,Bitmap bitmap ){
        if (bitmap == null) bitmap = placeHolder;
        if (this.data != null && data.size()!=0) {
            String movieTitle = (String) data.get(position).get(MyJSONDataParser.TAG_TITLE);
            if (data.get(position).containsKey(MyTaskLoader.provideBitmapKey(movieTitle))) {
                return;
            } else {
                this.data.get(position).put(MyTaskLoader.provideBitmapKey(movieTitle), bitmap);
            }
            notifyItemChanged(position);
        }
    }

    String getMovieBitmapKey(int position){
        if (this.data!=null && !this.data.isEmpty()){
        String movieTitle = (String) data.get(position).get(MyJSONDataParser.TAG_TITLE);
        movieTitle = MyTaskLoader.provideBitmapKey(movieTitle);
        return movieTitle;
        } else {
            return null;
        }
    }

    public void clearData(){
       if (this.data != null)this.data.clear();
    }

    void dropReferences(){
        placeHolder = null;
  }
}


