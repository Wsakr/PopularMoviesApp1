package com.techapps.underconstruction.popularmoviesapp;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techapps.underconstruction.popularmoviesapp.MyTaskLoader.DataLoader;

import java.util.ArrayList;
import java.util.HashMap;


public class fragment_MovieStartGridlayout extends android.support.v4.app.Fragment {

    private RecyclerView myRecyclerView;
    private MyGridAdapter myGridAdapter;
    private int pageNumber = 1;
    private String sortingOption = null;
    private boolean doScrollToPosition = false;

    private Context mContext;

    private OnFragmentInteractionListener mListener;

    public fragment_MovieStartGridlayout() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            pageNumber = savedInstanceState.getInt("pageNumber",1);
           sortingOption = savedInstanceState.getString("sortOption");
        }
    }

    @Override
    public void onActivityCreated  (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Walid Frag", "onActivityCreated");
    }

    private MyRecyclerViewClickListener movieChoiceListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragGridLayout = inflater.inflate(R.layout.fragment_gridlayout_movie_start, container, false);
            prepareDataLoader();
            GridLayoutManager rvLayoutManager;

            myRecyclerView = (RecyclerView) fragGridLayout.findViewById(R.id.myViewRecycler);
            myRecyclerView.setHasFixedSize(true);
            rvLayoutManager = new GridLayoutManager(mContext, 2);
            myRecyclerView.setLayoutManager(rvLayoutManager);
            movieChoiceListener = new MyRecyclerViewClickListener(mContext, new MyRecyclerViewClickListener.RecyclerClickListener() {
                @Override
                public void onItemClicked(int childPosition) {
                    if (mListener != null) {
                        mListener.onMovieChosen(getChosenMovieData(childPosition));
                    }
                }
            });
            myRecyclerView.addOnItemTouchListener(movieChoiceListener);
            startLoadingImages();
            if (myGridAdapter == null) {
                myGridAdapter = new MyGridAdapter(getImagePlaceHolder());
                myGridAdapter.registerAdapterListener(new MyGridAdapter.MyGridAdapterListener() {
                    @Override
                    public void onDataSetChanged(boolean state) {
                        if (state) {
                            updateImagePositionCounter(1);
                        }
                        Log.i("setPagNumbr onDataChang", String.valueOf(state));
                    }
                });
            }
            myRecyclerView.setAdapter(myGridAdapter);


            Log.i("Walid Frag", "onCreateView");
        return fragGridLayout;
        }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("pageNumber", pageNumber);
        outState.putString("sortOption", sortingOption);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext = null;
        mListener = null;
        myRecyclerView.removeOnItemTouchListener(movieChoiceListener);
        myGridAdapter.dropReferences();
        //myRecyclerView.setOnScrollListener(null);
        //myGridAdapter.unRegisterAdapterListener();
        //myRecyclerView.setAdapter(null);
        //myGridAdapter = null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private LoaderManager.LoaderCallbacks<ArrayList<HashMap>> dataLoaderCallbacks;

    private void prepareDataLoader(){
         dataLoaderCallbacks = new LoaderManager.LoaderCallbacks<ArrayList<HashMap>>() {
            @Override
            public Loader<ArrayList<HashMap>> onCreateLoader(int id, Bundle args) {
                return new DataLoader(mContext, MyUriBuilder.DISCOVER, null, pageNumber, sortingOption);
            }

            @Override
            public void onLoadFinished(Loader<ArrayList<HashMap>> loader, ArrayList<HashMap> data) {
                myGridAdapter.setMyAdapterData(data);
                if (doScrollToPosition) {
                    myRecyclerView.smoothScrollToPosition(0);
                }
                doScrollToPosition = false;

                Log.i("Walid SetAdapter DL", " onLoadFinished" + loader.getId());
            }

            @Override
            public void onLoaderReset(Loader<ArrayList<HashMap>> loader) {
                loader.reset();
                Log.i("Walid_onLoaderReset", "called");
            }

        };
    }

   private void startLoadingData(boolean loadStatus) {

       if (loadStatus) {
           try {
               getLoaderManager().initLoader(0, null, dataLoaderCallbacks);
           }catch (Exception e){

           }
       } else {
           try {
               getLoaderManager().restartLoader(0, null, dataLoaderCallbacks);
           } catch (Exception e){

           }
       }
   }

    private LoaderManager.LoaderCallbacks<Bitmap> bitmapLoaderCallbacks;

    private void startLoadingImages() {

             bitmapLoaderCallbacks = new LoaderManager.LoaderCallbacks<Bitmap>() {
                @Override
                public Loader<Bitmap> onCreateLoader(int id, Bundle args) {
                    String bitmapName = myGridAdapter.getMovieBitmapKey(MyTaskLoader.imagePositionCounter);
                    return new MyTaskLoader.BitmapLoader(mContext, bitmapName,null);
                }

                @Override
                public void onLoadFinished(Loader<Bitmap> loader, Bitmap data) {
                    Log.i("Walid BL Loadfinish", String.valueOf(loader.getId()));
                    myGridAdapter.replaceBitmap(MyTaskLoader.imagePositionCounter, data);
                    updateImagePositionCounter(1);
                }

                @Override
                public void onLoaderReset(Loader<Bitmap> loader) {
                    loader.reset();
                }
            };
    }

    public interface OnFragmentInteractionListener {
         void onMovieFragmentInteraction(int pageNumber);
         void onMovieChosen(ArrayList movieDetailData);
    }

    private void updateImagePositionCounter(int loaderID){
        MyTaskLoader.imagePositionCounter++;
        if (MyTaskLoader.imagePositionCounter<20){
            getLoaderManager().restartLoader(1, null, bitmapLoaderCallbacks);
        } else {
            MyTaskLoader.imagePositionCounter = -1;
            if (getLoaderManager().getLoader(loaderID) != null)getLoaderManager().destroyLoader(loaderID);
        }
    }

     private boolean setSortingOption(String sortChoice, String sortDirection) {
         boolean status;
         String newSortingOption;
         if (sortChoice != null) {
             newSortingOption = sortChoice + "." + sortDirection;
             if (newSortingOption.equals(sortingOption)){
                 status = true;
             }else {
                 sortingOption = newSortingOption;
                 status = false;
             }
         } else {
             if (sortingOption == null) {
                 status = true;
             } else {
                 sortingOption = null;
                 status = false;
             }
         }
         return status;
     }

     void loadNewData(String sortChoice, String sortDirection, int newPage){
        boolean sortingStatus = setSortingOption(sortChoice,sortDirection);
        boolean loaderStatus;
        if (sortingStatus) {
            if (pageNumber == newPage){
                loaderStatus = true;
            } else {
                pageNumber = newPage;
                loaderStatus = false;
                doScrollToPosition = true;
            }

        } else {
            pageNumber = 1;
            mListener.onMovieFragmentInteraction(pageNumber);
            loaderStatus = false;
            doScrollToPosition = true;
        }
        startLoadingData(loaderStatus);
    }

    private ArrayList getChosenMovieData(int position){
        ArrayList chosenMovieData = new ArrayList();
        chosenMovieData.add(MyTaskLoader.movieList.get(position).get(MyJSONDataParser.TAG_TITLE));
        chosenMovieData.add(MyTaskLoader.movieList.get(position).get(MyJSONDataParser.TAG_VOTE_AVERAGE));
        chosenMovieData.add(MyTaskLoader.movieList.get(position).get(MyJSONDataParser.TAG_VOTE_COUNT));
        chosenMovieData.add(MyTaskLoader.movieList.get(position).get(MyJSONDataParser.TAG_RELEASE_DATE));
        chosenMovieData.add(MyTaskLoader.movieList.get(position).get(MyJSONDataParser.TAG_OVERVIEW));
        chosenMovieData.add(MyTaskLoader.movieList.get(position).get(MyJSONDataParser.TAG_BACKDROP_PATH));
        chosenMovieData.add(MyTaskLoader.movieList.get(position).get(MyJSONDataParser.TAG_MOVIE_ID));

        return chosenMovieData;
    }

    Bitmap getImagePlaceHolder(){
        Bitmap placeHolder= BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.android_blue);
        return placeHolder;
    }
}
