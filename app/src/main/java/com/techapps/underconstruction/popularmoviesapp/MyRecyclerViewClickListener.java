package com.techapps.underconstruction.popularmoviesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Walid Sakr on 2/18/2016.
 */
class MyRecyclerViewClickListener implements RecyclerView.OnItemTouchListener {

    public interface RecyclerClickListener {
        void onItemClicked(int childPosition);
    }

    RecyclerClickListener myClickListener;
    Context mContext;

    MyRecyclerViewClickListener(Context context, RecyclerClickListener listener) {
        myClickListener = listener;
        mContext = context;
    }

    GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }
    });

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        if (gestureDetector.onTouchEvent(e)) {
            View mView = rv.findChildViewUnder(e.getX(), e.getY());
            int adapterPosition = rv.getChildPosition(mView);
            myClickListener.onItemClicked(adapterPosition);
            Log.i("Walid GestureDetector", String.valueOf(e));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
