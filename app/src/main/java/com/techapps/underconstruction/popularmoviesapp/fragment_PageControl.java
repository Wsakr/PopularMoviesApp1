package com.techapps.underconstruction.popularmoviesapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class fragment_PageControl extends android.support.v4.app.Fragment {

    private Button nextPageButton;
    private Button previousPageButton;
    private Button firstPageButton;
    private Button lastPageButton;
    private TextView pageNumberTextView;
    private int currentPageNumber = 1;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_PageControl.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_PageControl newInstance(String param1, String param2) {
        fragment_PageControl fragment = new fragment_PageControl();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public fragment_PageControl() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            currentPageNumber = savedInstanceState.getInt("currentPageNmber",1);
            if (mListener != null) {
                mListener.onPageControlFragInteract(getCurrentPageNumber());
            }
            Log.d("Walid PageControl Frag","current page"+String.valueOf(currentPageNumber));
        }
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

       //
       // setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentPageNmber", currentPageNumber);
        Log.i("Walid PageControl Frag", "current page" + String.valueOf(currentPageNumber));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragPageControls = inflater.inflate(R.layout.fragment_page_control, container, false);
        nextPageButton = (Button) fragPageControls.findViewById(R.id.next_page);
        lastPageButton = (Button) fragPageControls.findViewById(R.id.last_page);
        previousPageButton = (Button) fragPageControls.findViewById(R.id.previous_page);
        firstPageButton = (Button) fragPageControls.findViewById(R.id.first_page);
        pageNumberTextView = (TextView) fragPageControls.findViewById(R.id.pageNumberTV);
        pageNumberTextView.setText(String.valueOf(getCurrentPageNumber()));
        // Inflate the layout for this fragment
        return fragPageControls;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonClick(View mButton) {
        if (mButton != null) {
            switch (mButton.getId()) {
                case R.id.next_page:
                    if (getCurrentPageNumber() < MyJSONDataParser.getTotalPages()) {
                        setCurrentPageNumber(getCurrentPageNumber() + 1);
                        break;
                    } else {
                        break;
                    }
                case R.id.previous_page:
                    if (getCurrentPageNumber() > 1) {
                        setCurrentPageNumber(getCurrentPageNumber() - 1);
                        break;
                    } else {
                        break;
                    }
                case R.id.first_page:
                    if (getCurrentPageNumber() != 1) {
                        setCurrentPageNumber(1);
                        break;
                    } else {
                        break;
                    }
                case R.id.last_page:
                    if (getCurrentPageNumber() < MyJSONDataParser.getTotalPages()) {
                        setCurrentPageNumber(MyJSONDataParser.getTotalPages());
                        break;
                    } else {
                        break;
                    }
            }
        }
        if (mListener != null) {
            mListener.onPageControlFragInteract(getCurrentPageNumber());
        }
        pageNumberTextView.setText(String.valueOf(getCurrentPageNumber()));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener = null;
    }

    private int getCurrentPageNumber(){
        return currentPageNumber;
    }

      void setCurrentPageNumber(int newPageNumber){
        currentPageNumber = newPageNumber;
         if (pageNumberTextView != null) {
             pageNumberTextView.setText(String.valueOf(getCurrentPageNumber()));
         }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
         void onPageControlFragInteract(int pageNumber);
    }

}
