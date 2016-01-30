package com.techapps.underconstruction.popularmoviesapp;



import android.content.Context;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import android.content.res.*;

/*
THE TMDB API to be inserted in the "MyUriBuilder" class in the TMDB_API static class member

this is the format for searching TMDB api
https://api.themoviedb.org/3/search/movie?api_key=****************&query=kill

then this is the format for discovering with generes
https://api.themoviedb.org/3/discover/movie?api_key=**********************&with_genres=878&sort_by=popularity.desc
to get more than one page we add "&page=1 or 2 or 3 .......
*/

public class MainActivity extends AppCompatActivity implements fragment_MovieStartGridlayout.OnFragmentInteractionListener, fragment_PageControl.OnFragmentInteractionListener, fragment_MovieDetail.OnMovieDetailFragmentListener {

    private fragment_MovieStartGridlayout movieMainScreen;
    private fragment_PageControl pageControls;
    private fragment_MovieDetail movieDetailScreen;
    private LinearLayout topBar;
    private FrameLayout bottomBar;
	private FrameLayout gridLayout;
	private FrameLayout detailLayout;
    private Spinner choicesSpinner;
    private Button sortButton;
    private static boolean isConnected = true;
    private String screenRotation;
    private ArrayAdapter mArrayAdapter;
    private int currentPageNumber = 1;
    private String sortOption = null;
    private static String sortDirection = "desc";
	private static int orientation = Configuration.ORIENTATION_UNDEFINED;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orientation= getResources().getConfiguration().orientation;
        setContentView(R.layout.activity_main);
        isConnected = getConnectionStatus(this);
        topBar = (LinearLayout) findViewById(R.id.topBarLinearLayout);
        bottomBar = (FrameLayout) findViewById(R.id.pageControlFragment);
		gridLayout = (FrameLayout) findViewById(R.id.gridViewFragment);
		detailLayout = (FrameLayout) findViewById(R.id.movieDetailsFragment);
        choicesSpinner = (Spinner)findViewById(R.id.filterSpinner);
        sortButton = (Button) findViewById(R.id.sortingButton);
        mArrayAdapter = ArrayAdapter.createFromResource(this, R.array.sorting_list, R.layout.support_simple_spinner_dropdown_item);
        mArrayAdapter.setDropDownViewResource(android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item);
        choicesSpinner.setAdapter(mArrayAdapter);

        Log.i("Walid Spinner State",String.valueOf(choicesSpinner.hasOnClickListeners()));
            choicesSpinner.setOnItemSelectedListener(new MySpinnerSelectListener(new MySpinnerSelectListener.MySpinnerListener() {
                @Override
                public void spinnerItemSelected(String sorting, int spinnerPosition) {
                    sortOption = sorting;
                    if (sortOption != null) {
                        sortButton.setText((CharSequence) choicesSpinner.getItemAtPosition(spinnerPosition));
                    } else {
                        sortButton.setText("Sort by:");
                    }
                    movieMainScreen.loadNewData(sortOption, sortDirection, currentPageNumber);
                    //updateViewData(sorting,spinnerPosition);
                    Log.i("Walid Spinner", choicesSpinner.getItemAtPosition(spinnerPosition).toString());
                }
            }));
            Log.i("Walid Spinner State", String.valueOf(choicesSpinner.hasOnClickListeners()));
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
			detailLayout.setVisibility(View.VISIBLE);
		} else {
			detailLayout.setVisibility(View.GONE);
		}
        movieMainScreen = (fragment_MovieStartGridlayout) getSupportFragmentManager().findFragmentByTag("MovieMainScreenFRAGTAG");
        pageControls = (fragment_PageControl) getSupportFragmentManager().findFragmentByTag("PageControlsFRAGTAG");

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (movieMainScreen == null) {
            movieMainScreen = new fragment_MovieStartGridlayout();
            fragmentTransaction.replace(gridLayout.getId(), movieMainScreen, "MovieMainScreenFRAGTAG");
        }

        if (pageControls == null) {
            pageControls = new fragment_PageControl();
            fragmentTransaction.replace(R.id.pageControlFragment, pageControls, "PageControlsFRAGTAG");
        }
        fragmentTransaction.commit();

        if (!isConnected){
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {

		if (orientation != Configuration.ORIENTATION_LANDSCAPE){
            detailLayout.setVisibility(View.GONE);
            gridLayout.setVisibility(View.VISIBLE);
            if (topBar.getY()<0){
                movieDetailScreen.resetView(topBar.getHeight());
                topBar.animate().translationYBy(topBar.getHeight()).setDuration(300);
                bottomBar.animate().translationYBy(-bottomBar.getHeight()).setDuration(300);
            }
            movieMainScreen= (fragment_MovieStartGridlayout) getSupportFragmentManager().findFragmentByTag("MovieMainScreenFRAGTAG");
        } else {

           // getSupportFragmentManager().popBackStack();
        }

        


        //if(movieDetailScreen.isAdded()) {
            //topBar.animate().translationYBy(topBar.getHeight()).setDuration(300);
           // bottomBar.animate().translationYBy(-bottomBar.getHeight()).setDuration(300);
       // }
    }

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        orientation = newConfig.orientation;
        //movieDetailScreen = (fragment_MovieDetail) getSupportFragmentManager().findFragmentByTag("MovieDetailScreenFRAGTAG");
		if(orientation==Configuration.ORIENTATION_LANDSCAPE){
			detailLayout.setVisibility(View.VISIBLE);
            gridLayout.setVisibility(View.VISIBLE);
            topBar.setPivotX(0);
            topBar.setPivotY(0);
            topBar.setScaleX(0.5f);
            topBar.setScaleY(0.5f);
            bottomBar.setPivotX(0);
            bottomBar.setPivotY(bottomBar.getHeight());
            bottomBar.setScaleX(0.5f);
            bottomBar.setScaleY(0.5f);
            if (topBar.getY()<0){
                topBar.animate().translationYBy(topBar.getHeight()).setDuration(300);
                bottomBar.animate().translationYBy(-bottomBar.getHeight()).setDuration(300);
            }
		} else {
			detailLayout.setVisibility(View.GONE);
			gridLayout.setVisibility(View.VISIBLE);
            topBar.setPivotX(0);
            topBar.setPivotY(0);
            topBar.setScaleX(1.0f);
            topBar.setScaleY(1.0f);
            bottomBar.setPivotX(0);
            bottomBar.setPivotY(bottomBar.getHeight());
            bottomBar.setScaleX(1.0f);
            bottomBar.setScaleY(1.0f);
		}
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        choicesSpinner.setOnItemSelectedListener(null);
        pageControls.onButtonClick(null);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onPagesButtonClick(View mButton) {
        pageControls.onButtonClick(mButton);
    }

    public void sortButtonClick(View button) {
        choicesSpinner.performClick();
    }

     static boolean getConnectionStatus(Context mContext) {
        boolean myConnectionStatus;
        ConnectivityManager conMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            myConnectionStatus = true;
        } else myConnectionStatus = false;
        return myConnectionStatus;
    }

    public static boolean isInternetConnected(){
        return isConnected;
    }

    private static int rotationAngle;
    protected static boolean isScreenVertical(){
        if (rotationAngle == Surface.ROTATION_0 || rotationAngle == Surface.ROTATION_180){
            return true;
        }else return false;
    }

    @Override
    protected void onStart(){
        super.onStart();
         rotationAngle = getWindowManager().getDefaultDisplay().getRotation();
    }

    @Override
    public void onPageControlFragInteract(int pageNumber) {
        currentPageNumber = pageNumber;
        if(movieMainScreen != null) {
            movieMainScreen.loadNewData(sortOption, sortDirection, currentPageNumber);
        }
    }

    @Override
    public void onMovieFragmentInteraction(int pageNumber) {
        pageControls.setCurrentPageNumber(pageNumber);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    public void onMovieChosen(ArrayList movieChosenData) {

        movieDetailScreen = (fragment_MovieDetail) getSupportFragmentManager().findFragmentByTag("MovieDetailScreenFRAGTAG");
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (movieDetailScreen == null) {
            movieDetailScreen = new fragment_MovieDetail();
            fragmentTransaction.replace(detailLayout.getId(), movieDetailScreen, "MovieDetailScreenFRAGTAG");
        }
        fragmentTransaction.commit();
		 if(orientation!=Configuration.ORIENTATION_LANDSCAPE){
			 gridLayout.setVisibility(View.GONE);
			 detailLayout.setVisibility(View.VISIBLE);
		 } else{
             movieDetailScreen.resetView(topBar.getHeight());
         }
        
        movieDetailScreen.setDisplayData(movieChosenData);

    }


    @Override
    public void backdropImageLoaded(boolean isLoaded) {
        if (orientation != Configuration.ORIENTATION_LANDSCAPE){
            topBar.animate().translationYBy(-topBar.getHeight()).setDuration(1000);
            bottomBar.animate().translationYBy(bottomBar.getHeight()).setDuration(1000);
        }

        if (isLoaded) {
            movieDetailScreen.stretchView(topBar.getHeight());
        }
    }

    private static class MySpinnerSelectListener implements AdapterView.OnItemSelectedListener{
        public interface MySpinnerListener{
            void spinnerItemSelected(String sorting, int spinnerPosition);
        }
        private MySpinnerListener mySpinnerListener;

        MySpinnerSelectListener(MySpinnerListener listener){
            mySpinnerListener = listener;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String choice = getSpinnerChoice(position);
            mySpinnerListener.spinnerItemSelected(choice,position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private static String getSpinnerChoice(int position){
        String choice = null;
        switch (position) {
            case 0:
                choice = null;
                break;
            case 1:
                choice = "popularity";
                break;
            case 2:
                choice = "release_date";
                break;
            case 3:
                choice = "revenue";
                break;
            case 4:
                choice = "vote_average";
                break;
            case 5:
                choice = "vote_count";
                break;
        }
        return choice;
    }
}





