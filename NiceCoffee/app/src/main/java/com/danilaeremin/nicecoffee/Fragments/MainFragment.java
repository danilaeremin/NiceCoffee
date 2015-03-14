package com.danilaeremin.nicecoffee.Fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ViewFlipper;

import com.danilaeremin.nicecoffee.AboutUsActivity;
import com.danilaeremin.nicecoffee.Adapters.CategoryGridAdapter;
import com.danilaeremin.nicecoffee.Adapters.DataAdapter;
import com.danilaeremin.nicecoffee.MainActivity;
import com.danilaeremin.nicecoffee.R;
import com.danilaeremin.nicecoffee.core.Utils;

/**
 * Created by danilaeremin on 08.03.15.
 */
public class MainFragment extends Fragment implements GestureDetector.OnGestureListener {
    private static final String LOG_TAG = MainFragment.class.getSimpleName();

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_DISTANCE_Y = 50;

    private static final String SAVED_STATE = "Saved_state";

    private ViewFlipper mFlipper;
    private GestureDetector detecture;

    private int mCurrentPosition = -1;


    private GridView mMainGrid = null;
    private GridView mCategoryGrid = null;
    private DataAdapter mMainGridAdapter = null;
    private CategoryGridAdapter mCategoryGridAdapter = null;

        public static MainFragment newInstance() {
            MainFragment fragment = new MainFragment();
            return fragment;
        }

        public MainFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            mMainGrid = (GridView) rootView.findViewById(R.id.main_fragment_grid);
            mCategoryGrid = (GridView) rootView.findViewById(R.id.main_fragment_category_grid);

            // Получаем объект ViewFlipper
            mFlipper = (ViewFlipper) rootView.findViewById(R.id.special_deals);

            for (int i = 0; i < mFlipper.getChildCount(); i ++) {
                mFlipper.getChildAt(i).setDrawingCacheEnabled(true);
            }

            detecture = new GestureDetector(getActivity(), this);

            if (rootView.findViewById(R.id.scrollView) != null) {
                rootView.findViewById(R.id.scrollView).setOnTouchListener(new AdapterView.OnTouchListener() {

                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        return detecture.onTouchEvent(event);
                    }
                });
            } else {
                mFlipper.setOnTouchListener(new AdapterView.OnTouchListener() {

                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        return detecture.onTouchEvent(event);
                    }
                });
            }


            if (savedInstanceState != null) {
                mCurrentPosition = savedInstanceState.getInt(SAVED_STATE);
            }


            mMainGridAdapter = new DataAdapter(getActivity(),
                    new String[]{}, new Integer[]{});
            mMainGrid.setAdapter(mMainGridAdapter);
            mMainGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    if (position == 2) {
                        try {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + getString(R.string.drawer_main_3)));
                            startActivity(callIntent);
                        } catch (ActivityNotFoundException activityException) {
                            Log.e("Calling a Phone Number", "Call failed", activityException);
                        }
                    }

                    if (position == 3) {
                        Intent mIntent = new Intent(getActivity(), AboutUsActivity.class);
                        startActivity(mIntent);
                    }
                }
            });


            mCategoryGridAdapter = new CategoryGridAdapter(getActivity(), new String[]{}, new Integer[]{});
            mCategoryGrid.setAdapter(mCategoryGridAdapter);
            mCategoryGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    mCurrentPosition = position;

                    if (position < 8) {
                        Integer category_id = Utils.getBasicCategory (position);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, CategoryFragment.newInstance(category_id))
                                .addToBackStack( getString(R.string.back_stack_tag) )
                                .commit();
                    } else {

                    }

                }
            });

            new LoadStaticData().execute();

            return rootView;
        }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getActivity().getString(R.string.drawer_head));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_STATE, mCurrentPosition);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_DISTANCE_Y) {
                return false;
            }
            if ((e1.getX() - e2.getX()) > SWIPE_MIN_DISTANCE) {
                mFlipper.showNext();
            } else if ((e2.getX() - e1.getX()) > SWIPE_MIN_DISTANCE) {
                mFlipper.showPrevious();
            }
        } catch (Exception e) {
            Log.e (LOG_TAG, "it's really magic why it fail :) no answer in google!");
        }

        return false;
    }

    private class LoadStaticData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (isAdded()) {

                Integer[] CategoryElementsPics = {
                        R.drawable.main_grid_category_1,
                        R.drawable.main_grid_category_2,
                        R.drawable.main_grid_category_3,
                        R.drawable.main_grid_category_4,
                        R.drawable.main_grid_category_5,
                        R.drawable.main_grid_category_6,
                        R.drawable.main_grid_category_7,
                        R.drawable.main_grid_category_8,
                        R.drawable.main_grid_category_9,
                        R.drawable.main_grid_category_10,
                        R.drawable.main_grid_category_11,
                        R.drawable.main_grid_category_12
                };

                String[] CategoryElements = null;
                if (isAdded())
                    CategoryElements = new String[]
                        {
                                getString(R.string.drawer_catalog_1),
                                getString(R.string.drawer_catalog_2),
                                getString(R.string.drawer_catalog_3),
                                getString(R.string.drawer_catalog_4),
                                getString(R.string.drawer_catalog_5),
                                getString(R.string.drawer_catalog_6),
                                getString(R.string.drawer_catalog_7),
                                getString(R.string.drawer_catalog_8),
                                getString(R.string.drawer_catalog_9),
                                getString(R.string.drawer_catalog_10),
                                getString(R.string.drawer_catalog_11),
                                getString(R.string.drawer_catalog_12),
                };

                mCategoryGridAdapter.addElements(CategoryElements, CategoryElementsPics);


                String[] drawerMainElements = {"","","",""};

                if (isAdded()) drawerMainElements[0] = getString(R.string.drawer_main_1);
                if (isAdded()) drawerMainElements[1] = getString(R.string.drawer_main_2);
                if (isAdded()) drawerMainElements[2] = getString(R.string.drawer_main_3);
                if (isAdded()) drawerMainElements[3] = getString(R.string.drawer_main_4);

                Integer[] drawerMainElementsPics = {
                        R.drawable.drawer_personal,
                        R.drawable.drawer_contacts,
                        R.drawable.drawer_call,
                        R.drawable.drawer_about
                };


                mMainGridAdapter.addElements(drawerMainElements, drawerMainElementsPics);
            }

            return null;
        }
    }
}
